/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.comm.todo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.db.RowHandler;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.CacheMgr;
import com.rh.core.comm.remind.RemindMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.Lang;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.Strings;
import com.rh.core.util.lang.Assert;

/***
 * 处理代办事务、已办事务的统一处理类
 * 
 * @author Kevin Liu
 * @version $Id$
 */
public class TodoUtils extends Object {
    /** log */
    private static Log log = LogFactory.getLog(TodoUtils.class);
    /** 待办字段 **/
    public static class ToDoItem {
        /** 待办标题 **/
        public static final String TODO_TITLE = "TODO_TITLE";        
        /** 待办编码 **/
        public static final String TODO_CODE = "TODO_CODE";
        /** 待办名称 **/
        public static final String TODO_CODE_NAME = "TODO_CODE_NAME";
        /** 待办URL **/
        public static final String TODO_URL = "TODO_URL";
        /** 发送人ID **/
        public static final String SEND_USER_CODE = "SEND_USER_CODE";
        /** 办理人ID **/
        public static final String OWNER_CODE = "OWNER_CODE";
        /** 缓急 **/
        public static final String S_EMERGENCY = "S_EMERGENCY";
        /** 分类：待办 1/待阅 2/消息 3 **/
        public static final String TODO_CATALOG = "TODO_CATALOG";
        /** 与SERV_ID对应的主数据ID **/
        public static final String TODO_OBJECT_ID1 = "TODO_OBJECT_ID1";
        /** 辅数据ID，用于区分待办 **/
        public static final String TODO_OBJECT_ID2 = "TODO_OBJECT_ID2";
        /** 办理期限 **/
        public static final String TODO_DEADLINE1 = "TODO_DEADLINE1";
        /** 办理期限 **/
        public static final String TODO_DEADLINE2 = "TODO_DEADLINE2";
        /** 代理状态，1：可代理；2：不可代理 **/
        public static final String TODO_BENCH_FLAG = "TODO_BENCH_FLAG";
        /** 待办提示消息内容 **/
        public static final String TODO_CONTENT = "TODO_CONTENT";
        /** 待办的办理提示，如：请批示，请核稿等 **/
        public static final String TODO_OPERATION = "TODO_OPERATION";
        /** 服务ID，访问URL时卡片页面对应的服务ID **/
        public static final String SERV_ID = "SERV_ID";
        /**文件编号**/
        public static final String SERV_DATA_CODE = "SERV_DATA_CODE";
        /** 待办来自哪种业务：如wf工作流，remind消息提醒 **/
        public static final String TODO_FROM = "TODO_FROM";
    };

	/** 插入操作 */
	public static final String ACTINSET = "insert";
	
	/** 更新 */
	public static final String ACTUPDATE = "update";
	
	/** 删除 */
	public static final String ACTDELETE = "delete";
	
	/** 真删除 */
	public static final String ACTDELETETRUE = "deleteTrue";

	/** 送用户 */
	public static final int SEND_USER = 1;
	
	/** 送角色 */
	public static final int SEND_ROLE = 2;
	
	/** 待办 */
	public static final int TODO_CATLOG_BAN = 1;
	
	/** 待阅 */
	public static final int TODO_CATLOG_YUE = 2;
	
	/** 消息提醒 */
    public static final int TODO_CATLOG_MSG = 3;
	
	/** 缓存类型:用户待办数 */
    private static final String CACHE_TODO = "_CACHE_TODO";
    
    /**
     * 待办提醒方式代码
     */
    public static final String REMIND_TYPE_TODO = "TODO";
    
    /**
     * 向角色发送待办
     * @param todoBean 待办Bean
     * @param deptCode 部门编码
     * @param roleCode 角色编码
     */
    public static void insertToDeptRole(TodoBean todoBean, String deptCode, String roleCode) {
        List<UserBean> userBeanList = UserMgr.getUsersByDept(deptCode, roleCode);

        for (UserBean userBean : userBeanList) {
            todoBean.setOwner(userBean.getCode());
            insert(todoBean);
        }
    }
	
    /**
     * 
     * @param dataBean
     * @return
     */
    public static Bean insert(TodoBean dataBean) {
    	return insert(dataBean, false);
    }
    
	/**
	 * 新增一条代办
	 * 
	 * @param dataBean
	 *            参数对象
	 * @return resBean 结果Bean
	 */
	public static Bean insert(TodoBean dataBean, boolean ifForceSMS) {
		// 参数判断
		if (dataBean.isEmpty("SEND_USER_CODE")) { // 发代办用户
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", ACTINSET,
					"SEND_USER_CODE"));
		}
		if (dataBean.isEmpty("TODO_TITLE")) { // 待办标题
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", ACTINSET,
					"TODO_TITLE"));
		} else {
		    dataBean.set("TODO_TITLE", Strings.escapeHtml(dataBean.getStr("TODO_TITLE"))); 
		}
		if (dataBean.isNotEmpty("TODO_CONTENT")) { // 待办内容
		    dataBean.set("TODO_CONTENT", Strings.escapeHtml(dataBean.getStr("TODO_CONTENT")));
        }
		if (dataBean.isEmpty("OWNER_CODE")) { // 待办用户[角色]
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", "insert",
					"OWNER_CODE"));
		}
		if (dataBean.isEmpty("TODO_CODE")) { // 待办编码 服务ID
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", ACTINSET,
					"TODO_CODE"));
		} else if (dataBean.isEmpty("TODO_CODE_NAME")) {
		    dataBean.set("TODO_CODE_NAME", 
		            ServUtils.getServDef(dataBean.getStr("TODO_CODE")).getStr("SERV_NAME"));
		}
		
		if (dataBean.isEmpty("TODO_CODE_NAME")) { // 待办编码名称 服务名称
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", ACTINSET,
					"TODO_CODE_NAME"));
		}
        //如果没给待办类型，默认类别为待办
        if (dataBean.getInt("TODO_CATALOG") == 0) {
            dataBean.set("TODO_CATALOG", TODO_CATLOG_BAN);
        }
		
        String url = dataBean.getStr("TODO_URL");
        
        if (!url.startsWith("http://")) {
    		if (dataBean.isEmpty("TODO_OBJECT_ID1")) { // 对象ID1 DATA Id
    			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", ACTINSET,
    					"TODO_OBJECT_ID1"));
    		}
        }
        
        // 兼容老代码：如果服务ID为NULL，则以TODO_CODE为服务ID
        if (dataBean.isEmpty("SERV_ID")) {
            dataBean.set("SERV_ID", dataBean.getStr("TODO_CODE"));
        }        

		//添加发送时间
		dataBean.set("TODO_SEND_TIME", DateUtils.getDatetime());
		
		if (dataBean.isEmpty("TODO_BENCH_FLAG")) {
		    dataBean.set("TODO_BENCH_FLAG", Constant.YES);
		}
		
//		Bean resBean = ServDao.create(ServMgr.SY_COMM_TODO, dataBean);
//通过服务保存数据		
		ParamBean queryBean = new ParamBean(dataBean);
		queryBean.setServId(ServMgr.SY_COMM_TODO);
		queryBean.setAct(ServMgr.ACT_SAVE);
		Bean resBean = ServMgr.act(queryBean);
		
        CacheMgr.getInstance().remove(dataBean.getStr("OWNER_CODE"), CACHE_TODO); //清除缓存
        
        //判断如果是由提醒触发的，待办发完后不再发提醒，防止嵌套循环
        if (dataBean.isEmpty("remindFlag")) {
            sendTodoMsg(dataBean, ifForceSMS);
        }
//        UserBean doUser = UserMgr.getUser(dataBean.getStr("OWNER_CODE"));
//        
//        if (dataBean.isCopyEntity()) {
//            // 复制实例数据，对于没有流经记录的数据，需要功能为目标公司复制数据，否则，查询有问题。
//            EntityMgr.copyEntity(dataBean.getStr("TODO_OBJECT_ID1"), doUser.getODeptCode());
//        }
        
		return resBean;
	}
	
	/**
	 * 发送待办提醒
	 * @param todoBean 待办数据
	 */
    private static void sendTodoMsg(Bean todoBean) {
    	sendTodoMsg(todoBean, false);
    }
    
    /**
	 * 发送待办提醒
	 * @param todoBean 待办数据
	 */
    private static void sendTodoMsg(Bean todoBean, boolean ifForceSMS) {
        // 自己给自己发送的待办没有提醒
        if (todoBean.getStr(ToDoItem.OWNER_CODE).equals(todoBean.getStr(ToDoItem.SEND_USER_CODE))) {
            return;
        }
        
        String remindType = TodoRemindTypeMgr.getUserRemindType(todoBean.getStr("OWNER_CODE"));
        
        sendTodoMsgByType( new Bean(todoBean), ifForceSMS, remindType, todoBean.getStr("OWNER_CODE") );
        
        //委托人
        List<Bean> agentUsers =  findTOAgentUser(todoBean.getStr(ToDoItem.OWNER_CODE));
        if(agentUsers.size()>0){
        	for(Bean agentUser : agentUsers){
        		String agentUserCode = agentUser.getStr("TO_USER_CODE");	//被委托人用户编码
        		int agentStatus = agentUser.getInt("AGT_STATUS");			//委托状态   1 为已启动
//        		String agentTypeCode = agentUser.getStr("AGT_TYPE_CODE"); 	//委托业务  
//        		String servIds = findServIdByAgentTypeCOde(agentTypeCode);  //根据委托业务，查找相应的服务编码
        		//默认全部的委托业务
        		if(1==agentStatus ){
        			String remindTypeAgent = TodoRemindTypeMgr.getUserRemindType(agentUserCode);  //被委托人提醒方式
        			//给被委托人发邮件
        			sendTodoMsgByType(new Bean(todoBean), ifForceSMS, remindTypeAgent, agentUserCode);
        		}
        	}
        }
    }

    /**
     * 查找设置委托时，选择 委托业务  所包含的servIds
     * @param agtTypeCode
     * @return servIds
     */
    private static String findServIdByAgentTypeCOde(String agtTypeCode){
    	String servIds = "";
    	String where = " and  AGT_TYPE_CODE ='"+agtTypeCode+"' order by AGT_ORDER asc";
    	List<Bean> agtTypeBeans = ServDao.finds("SY_ORG_USER_AGT_TYPE", where);
    	if(agtTypeBeans.size()>0){
    		//如果有，根据排序取第一个
    		servIds = agtTypeBeans.get(0).getStr("AGT_COND");    		
    	}
    	return servIds;
    }
    
    /**
     * 查找被委托人
     * @param fromUser    委托人用户编码
     * @return agentUser  被委托人用户编码 (如果没有返回null)
     * 						
     */
    private static List<Bean> findTOAgentUser(String fromUser){
    	List<Bean> agentUserBean = null;
    	if(fromUser.length()>0){
    		agentUserBean = ServDao.finds("SY_ORG_USER_TYPE_AGENT_FROM", " and USER_CODE = '"+fromUser+"'");
    	}
    	return agentUserBean;
    }
    
    /**
     * 发送待办数据
     * @param todoBean  待办
     * @param ifForceSMS 
     * @param remindType 指定用户的提醒方式字符串
     */
    private static void sendTodoMsgByType(Bean todoBean, boolean ifForceSMS ,String remindType ,String userCode){
    	 //某类事务或者某角色下的人，可发送短信
        boolean isSms = false;
        if (ifForceSMS) {
        	isSms = true;
        } else if (("," + Context.getSyConf("BN_SMS_REMIND_TYPE", "") + ",")
                .indexOf("," + todoBean.getStr(ToDoItem.SERV_ID) + ",") >= 0) {
            isSms = true;
        } else if (UserMgr.existInRoles(todoBean.getStr(ToDoItem.OWNER_CODE),
                Context.getSyConf("BN_SMS_REMIND_ROLES", "3DJyKwEpR8o9LktOXLz4gE"))) {
            isSms = true;
        }
        
        if (isSms) {
            if (remindType.length() > 0) {
                remindType += ",SMS";
            } else {
                remindType = "SMS";
            }
        }
        
        if (Context.getSyConf("BN_MAIL_REMIND_IFFORCE", false)) {
        	if (remindType.length() > 0) {
                remindType += ",EMAIL";
            } else {
                remindType = "EMAIL";
            }
        }

        if (remindType.indexOf("TODO,") >= 0) { //取消待办对待办的提醒
            remindType = remindType.replaceAll("TODO,", "");
        } else if (remindType.equals("TODO")) {
            return;
        }
        
        if (StringUtils.isNotBlank(remindType)) {
            Bean remindBean = new Bean();
            StringBuilder msg = new StringBuilder();
            msg.append("您有一项新待办事务：");
            msg.append(todoBean.getStr(ToDoItem.TODO_TITLE));
            
            // 外网添加系统配置，外网url向内网推送
            String sysHost = Context.getSyConf("SY_REMOTE_TODO_URL", "");
            if (sysHost.length() == 0) {
                sysHost = RequestUtils.getSysHost();
            }
            String remoteUrl = createTodoUrl(todoBean, sysHost, false);
            if (todoBean.isNotEmpty("TODO_URL")) {
            	msg.append(" [<a href='").append(remoteUrl).append("'>详情</a>]");
            }
            
            remindBean.set("REM_TITLE", todoBean.getStr(ToDoItem.TODO_TITLE));
            remindBean.set("REM_CONTENT", msg.toString());
            //remindBean.set("REM_URL", todoBean.getStr(ToDoItem.TODO_URL));
            remindBean.set("S_USER", todoBean.getStr(ToDoItem.SEND_USER_CODE));
            remindBean.set("EXECUTE_TIME", "");
            remindBean.set("TYPE", remindType);
            remindBean.set("S_EMGRENCY", todoBean.getInt(ToDoItem.S_EMERGENCY));
            remindBean.set("SERV_ID", todoBean.getStr(ToDoItem.SERV_ID));
            remindBean.set("SERV_SRC_ID", todoBean.getStr(ToDoItem.SERV_ID));
            remindBean.set("DATA_ID", todoBean.getStr(ToDoItem.TODO_OBJECT_ID2));
            /*---------------添加外部待办URL---------begin----hdy--2013-6-26 11:37--------*/
            //添加外部URL
            remindBean.set("REMOTE_URL", remoteUrl);
            /*---------------添加外部待办URL---------end--------------*/
            RemindMgr.add(remindBean, userCode);

            /**
            ImMgr.getIm().sendNotify(todoBean.getStr("OWNER_CODE"),
                    "来自【" + Context.getUserBean().getName() + "】的消息", msg);    **/        
        }
    }
    

	/**
	 * 更新一条代办
	 * 
	 * @param dataBean
	 *            参数对象
	 * @return resBean 结果Bean
	 */
	public static Bean updateById(Bean dataBean) {
		if (dataBean.getId().length() == 0) { // 主键信息
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR",
					"updateById", "setId()"));
		}
		Bean resBean = ServDao.update(ServMgr.SY_COMM_TODO, dataBean);
		return resBean;
	}

	/**
	 * 根据条件，更新多条代办
	 * @param setBean 
	 * @param whereBean
	 *            参数对象
	 * @return resInt 成功数
	 */
	public static int updates(Bean setBean, Bean whereBean) {
		if (setBean.size() == 0) { // 更改字段信息
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", "updates",
					"setBean为空"));
		}
		if (whereBean.size() == 0) { // 条件信息
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", "updates",
					"whereBean为空"));
		}
		int res = ServDao.updates(ServMgr.SY_COMM_TODO, setBean, whereBean);
		return res;
	}

	/**
	 * 删除一条代办，同时将此代办转成一条已办
	 * 
	 * @param dataBean
	 *            参数对象
	 * @return resInt 成功数
	 */
	public static int endById(Bean dataBean) {
		if (dataBean.getId().length() == 0) { // 主键信息
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", "delete",
					"setId()"));
		}
		int resInt = 0;
		// 生成一条已办
		Bean toDoBean = ServDao.find(ServMgr.SY_COMM_TODO, dataBean.getId());
		if (toDoBean != null) {
			//添加完成时间
			toDoBean.set("TODO_FINISH_TIME", DateUtils.getDatetime());
			Bean hisBean = ServDao.create(ServMgr.SY_COMM_TODO_HIS, toDoBean);
			if (hisBean != null) {
				// 删除一条代办
				resInt = ServDao.destroys(ServMgr.SY_COMM_TODO, dataBean);
			}
	        CacheMgr.getInstance().remove(toDoBean.getStr("OWNER_CODE"), CACHE_TODO); //清除缓存
		}
		return resInt;
	}

	/**
	 * 删除多条代办，同时将此代办转成多条已办
	 * 
	 * @param whereBean
	 *            参数对象
	 * @return resInt 成功数
	 */
	public static int ends(Bean whereBean) {
		if (whereBean.size() == 0) { // 为空值判断
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", "ends",
					"whereBean为空"));
		}
		int resInt = 0;
		List<Bean> toDoList = ServDao.finds(ServMgr.SY_COMM_TODO, whereBean);
		
		for (Bean toDoBean: toDoList) {
			//添加完成时间
			toDoBean.set("TODO_FINISH_TIME", DateUtils.getDatetime());
			CacheMgr.getInstance().remove(toDoBean.getStr("OWNER_CODE"), CACHE_TODO); //清除缓存
		}
		
		if (toDoList.size() > 0) {
			// 生成多条已办
			int res = ServDao.creates(ServMgr.SY_COMM_TODO_HIS, toDoList);
			if (res > 0) {
				// 删除多条代办
				ParamBean queryBean = new ParamBean(whereBean);
		        queryBean.setServId(ServMgr.SY_COMM_TODO);
		        queryBean.setAct(ServMgr.ACT_DELETE);
		        queryBean.setDeleteDatas(toDoList);
		        
		        OutBean outBean = ServMgr.act(queryBean);
		        resInt = outBean.getCount();
			}
		}
		return resInt;
	}
	
	/**
     * 
     * @param todoBean
     *            待办数据Bean
     * @param hostAddr
     *            主机地址
     * @return 访问待办的完整URL，可用于门户、RTX、邮件系统。
     */
    public static String createTodoUrl(Bean todoBean, String hostAddr) {
        if (StringUtils.isEmpty(hostAddr)) {
            hostAddr = Context.getSyConf(Constant.CONF_SYS_HOST_ADDR, "NULL");
        }
        StringBuilder result = new StringBuilder();
        if (todoBean.getStr("TODO_URL").indexOf(".showDialog.do") >= 0) {
            result.append("{'_PK_':'").append(todoBean.getStr("TODO_ID"));
            result.append("','servId':'").append("OA_SY_COMM_TODO");
            result.append("'}");
            String url = "";
            try {
                url = hostAddr + "/sy/comm/page/confirm.jsp?param="
                        + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
                return url;
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage() + ": " + url);
            }
        } else {
            String todoUrl = todoBean.getStr("TODO_URL");
            if (todoBean.getBoolean("_agtFLag")) { // 针对代他人办理情况
                todoUrl = todoUrl.substring(0, todoUrl.length() - 1);
                todoUrl = todoUrl + ",_AGENT_USER_:" + todoBean.getStr("OWNER_CODE") + "}";
            }
            String title = todoBean.getStr("TODO_TITLE");
            if (title.length() > 6) {
                title = title.substring(0, 6) + "...";
            }
            result.append("{'tTitle':'待办--").append(title);
            result.append("','url':'").append(todoBean.getStr("SERV_ID"));
            result.append(".card.do?pkCode=").append(todoBean.getStr("TODO_OBJECT_ID1"));
            // result.append("','menuFlag':4,'replaceUrl':'").append(todoUrl);
            result.append("&NI_ID=").append(todoBean.getStr(ToDoItem.TODO_OBJECT_ID2));
            result.append("'}");
            String url = "";
            try {
                url = hostAddr + "/sy/comm/page/page.jsp?openTab="
                        + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
                return url;
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage() + ": " + url);
            }
        }
    }
	
    /**
     * 办结指定用户的待办
     * @param userCode 指定用户ID
     * @param objectID1 服务ID
     * @param objectID2 被处理对象的ID。常用于保存分发ID。
     * @return 成功办结的数量
     */
    public static int endUserTodo(String userCode, String objectID1, String objectID2) {
        Bean whereBean = new Bean();
        whereBean.set("TODO_OBJECT_ID1", objectID1);
        whereBean.set("OWNER_CODE", userCode);
        whereBean.set("TODO_OBJECT_ID2", objectID2);

        return ends(whereBean);
    }
    
    /**
     * 清除指定数据的所有待办。
     * @param objectID1 对象ID1
     * @param objectID2 对象ID2
     * @return 成功办结的数量
     */
    public static int endAllUserTodo(String objectID1, String objectID2) {
        Bean whereBean = new Bean();
        whereBean.set("TODO_OBJECT_ID1", objectID1);
        whereBean.set("TODO_OBJECT_ID2", objectID2);
        
        return ends(whereBean);
    }

	/**
	 * 仅彻底删除一条代办
	 * 
	 * @param dataBean
	 *            参数对象
	 * @return resInt 成功数
	 * 
	 */
	public static int destroyById(Bean dataBean) {
		if (dataBean.getId().length() == 0) { // 主键信息
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR", "delete",
					"setId()"));
		}
//		int resInt = ServDao.destroys(ServMgr.SY_COMM_TODO, dataBean);
		
		ParamBean queryBean = new ParamBean(dataBean);
		queryBean.setServId(ServMgr.SY_COMM_TODO);
        queryBean.setAct(ServMgr.ACT_FINDS);
		
		List<Bean> datas = ServMgr.act(queryBean).getDataList();
		
		//通过服务操作数据        
        queryBean.setServId(ServMgr.SY_COMM_TODO);
        queryBean.setAct(ServMgr.ACT_DELETE);
        queryBean.setDeleteDatas(datas);
        OutBean outBean = ServMgr.act(queryBean);
        int resInt = outBean.getCount();
        
		return resInt;
	}

	/**
	 * 仅彻底删除多条代办
	 * 
	 * @param whereBean
	 *            参数对象
	 * @return resInt 成功数
	 */
	public static int destroys(Bean whereBean) {
		if (whereBean.size() == 0) { // 为空值判断
			throw new TipException(Context.getSyMsg("SY_TODO_ERROR",
					"destroys", "whereBean为空"));
		}
	//	int resInt = ServDao.destroys(ServMgr.SY_COMM_TODO, whereBean);
	      //通过服务操作数据        
        ParamBean queryBean = new ParamBean(whereBean);
        queryBean.setServId(ServMgr.SY_COMM_TODO);
        queryBean.setAct(ServMgr.ACT_FINDS);
        
		List<Bean> datas = ServMgr.act(queryBean).getDataList();
		
        queryBean.setAct(ServMgr.ACT_DELETE);
        queryBean.setDeleteDatas(datas);
        
        OutBean outBean = ServMgr.act(queryBean);
        int resInt = outBean.getCount();
        
		return resInt;
	}
	
	/**
	 * 彻底删除指定服务数据的所有待办，用于删除服务时。
	 * @param servId 服务ID
	 * @param dataId 数据ID
	 * @return 删除数据数量
	 */
    public static int destroys(String servId, String dataId) {
        Assert.hasLength(servId);
        Assert.hasLength(dataId);
        SqlBean sql = new SqlBean();
        sql.and("SERV_ID", servId);
        sql.and("TODO_OBJECT_ID1", dataId);

        return destroys(sql);
    }
	
	/**
     * 仅彻底删除多条代办
     * 
     * @param sqlBean
     *            参数对象
     * @return resInt 成功数
     */
    private static int destroys(SqlBean sqlBean) {
       // int resInt = ServDao.destroys(ServMgr.SY_COMM_TODO, sqlBean);
        //通过服务操作数据        
        ParamBean queryBean = new ParamBean(sqlBean);
        queryBean.setServId(ServMgr.SY_COMM_TODO);
        queryBean.setAct(ServMgr.ACT_FINDS);
        
        List<Bean> datas = ServMgr.act(queryBean).getDataList();
        
        //通过服务操作数据        
        queryBean.setDeleteDatas(datas);
        queryBean.setAct(ServMgr.ACT_DELETE);
        OutBean outBean = ServMgr.act(queryBean);
        int resInt = outBean.getCount();
        return resInt; 
    }
	
    /**
     * 获取指定用户的待办数量
     * @param userCode 用户编码
     * @param interfaceMenuIds 接口对应的菜单ID列表，通过接口单独获取
     * @return 待办数量
     */
    public static int getToDoCount(String userCode, String interfaceMenuIds) {
        return getToDoCountBean(userCode, interfaceMenuIds).getInt("TODO_COUNT");
    }
    
    /**
     * 获取指定用户的待办数量
     * @param userCode 用户编码
     * @param interfaceMenuIds 接口对应的菜单ID列表，通过接口单独获取
     * @return 待办数量信息：包含总待办数
     */
    public static Bean getToDoCountBean(String userCode, String interfaceMenuIds) {
        Bean todo = (Bean) CacheMgr.getInstance().get(userCode, CACHE_TODO);
        if (todo == null) {
            final Bean catCount = new Bean(); // 分类数量
            SqlBean sql = new SqlBean().selects("TODO_CATALOG, TODO_CODE, COUNT(TODO_ID) COUNT_")
                    .tables("SY_COMM_TODO").and("OWNER_CODE", userCode).groups("TODO_CATALOG, TODO_CODE");
            ServDao.findsCall(ServMgr.SY_COMM_TODO, sql, new RowHandler() {
                public void handle(List<Bean> columns, Bean data) {
                    int cat = data.getInt("TODO_CATALOG");
                    int oldCount = catCount.getInt(cat);
                    int newCount = data.getInt("COUNT_");
                    catCount.set(cat, newCount + oldCount); // 累加计算分类数量
                    catCount.set(data.get("TODO_CODE"), newCount); // 按服务编码设置数量给前端
                }
            });
            catCount.set(0, catCount.getInt(TODO_CATLOG_BAN) + catCount.getInt(TODO_CATLOG_YUE)
                    + catCount.getInt(TODO_CATLOG_MSG)); // 合计总待办数：待办 + 待阅 + 提醒
            todo = catCount;
            CacheMgr.getInstance().set(userCode, todo, CACHE_TODO);
        }
        if (!interfaceMenuIds.isEmpty()) {
            String[] menuIds = interfaceMenuIds.split(Constant.SEPARATOR);
            Bean moreTodo = todo.copyOf();
            List<Bean> todoList = todo.getList(Constant.RTN_DATA);
            List<Bean> moreTodoList = new ArrayList<Bean>(todoList.size() + menuIds.length);
            moreTodoList.addAll(todoList);
            for (String mId : menuIds) {
                Bean menu = DictMgr.getItem("SY_COMM_MENU_USER", mId);
                if (menu != null) {
                    try {
                        String[] todoInterface = menu.getStr("ALERT").split(Constant.SEPARATOR);
                        ITodoCount todoInst = (ITodoCount) Lang.createObject(ITodoCount.class, todoInterface[0]);
                        Bean todoBean = todoInst.getTodo(JsonUtils.toBean(ServUtils.replaceSysVars(todoInterface[1])));
                        moreTodoList.add(todoBean);
                        // 累加计算分类数量
                        moreTodo.set(menu.getStr("ID"), todoBean.getInt("TODO_COUNT"));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            moreTodo.set(Constant.RTN_DATA, moreTodoList);
            return moreTodo;
        } else {
            todo.set("TODO_COUNT", todo.getInt(0));
            return todo;
        }
    }
 /**
     * 获取指定用户的待办数量
     * @param userCode 用户编码
     * @return 待办数量信息：包含总待办数
     */
    public static Bean getToDoCountBeanMb(String userCode) {
        Bean todo = (Bean) CacheMgr.getInstance().get(userCode, CACHE_TODO);
        if (todo == null) {
            final Bean catCount = new Bean(); // 分类数量
            SqlBean sql = new SqlBean().selects("TODO_CATALOG, TODO_CODE, COUNT(TODO_ID) COUNT_")
                    .tables("SY_COMM_TODO")
                    .and("OWNER_CODE", userCode)
                    .groups("TODO_CATALOG, TODO_CODE");
            ServDao.findsCall(ServMgr.SY_COMM_TODO, sql, new RowHandler() {
                public void handle(List<Bean> columns, Bean data) {
                    int cat = data.getInt("TODO_CATALOG");
                    int oldCount = catCount.getInt(cat);
                    int newCount = data.getInt("COUNT_");
                    catCount.set(cat, newCount + oldCount); // 累加计算分类数量
                    catCount.set(data.get("TODO_CODE"), newCount); // 按服务编码设置数量给前端
                }
            });
            catCount.set(0, catCount.getInt(TODO_CATLOG_BAN));
            todo = catCount;
            CacheMgr.getInstance().set(userCode, todo, CACHE_TODO);
        }
        return todo;
    }
		
	/**
	 * 
	 * @param userCode 用户编码
	 * @param start 从多少条开始
	 * @param count 取多少条
	 * @return 待办列表
	 */
	public static List<Bean> getUserTodos(String userCode, int start, int count) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT todo_id, todo_code, todo_title, " 
		              + "todo_code_name,todo_send_time, send_user_code,todo_object_id1,todo_url");
		strSql.append(" FROM cm_todo where owner_code = '" + userCode + "'");
		strSql.append(" ORDER BY s_emergency DESC, todo_send_time DESC");
		
		List<Bean> todoList = Transaction.getExecutor().query(strSql.toString(), start, count);
		
		return todoList;
	}
	
	/**
     * 
     * @param todoBean 待办数据Bean
     * @param hostAddr 主机地址
     * @param existJiangang 是否存在兼岗
     * @param sysFlag 是否跨系统
     * @return 访问待办的完整URL，可用于门户、RTX、邮件系统。
     */
    public static String createTodoUrl(Bean todoBean, String hostAddr, boolean existJiangang, boolean sysFlag) {
        // 处理本人和委托的兼岗问题
        String todoUser = "";
        String ownerCode = todoBean.getStr("OWNER_CODE");
        boolean agtentFlag = todoBean.getBoolean("_agtFLag");
        if (existJiangang) {
            if (!agtentFlag) {
                todoUser = ownerCode;
            }
        }
        if (StringUtils.isEmpty(hostAddr)) {
            hostAddr = Context.getSyConf(Constant.CONF_SYS_HOST_ADDR, "NULL");
        }
        StringBuilder result = new StringBuilder();
        if (todoBean.getStr("TODO_URL").indexOf(".showDialog.do") >= 0) {
            result.append("{'_PK_':'").append(todoBean.getStr("TODO_ID"));
            result.append("','servId':'").append("OA_SY_COMM_TODO");
            result.append("'}");
            String url = "";
            try {
                url = hostAddr + "/sy/comm/page/confirm.jsp?param="
                        + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
                return url;
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage() + ": " + url);
            }
        } else {
            String todoUrl = todoBean.getStr("TODO_URL");
            todoUrl = todoUrl.replaceAll("\\.byid\\.do", "\\.card\\.do");
            if (agtentFlag) { // 针对代他人办理情况
                todoUrl = todoUrl.substring(0, todoUrl.length() - 1);
                todoUrl = todoUrl + ",_AGENT_USER_:" + todoBean.getStr("OWNER_CODE") + "}";
            }
            
            todoUrl = todoUrl + "&pkCode=" + todoBean.getStr("TODO_OBJECT_ID1");
            String title = todoBean.getStr("TODO_TITLE");
            if (title.length() > 6) {
                title = title.substring(0, 6) + "..";
            }
            result.append("{'tTitle':'待办--").append(title);
            result.append("','url':'").append(todoUrl);
            if (sysFlag) { // 增加是否为跨系统地址 1 为 是
                result.append("','_sysFlag_':'1");
            }
            if (agtentFlag) {
                result.append("','replaceUrl':'").append(todoUrl.replaceAll("\\.card\\.do", "\\.byid\\.do"));
            }
            result.append("'}");
            String url = "";
            try {
				url = hostAddr + "/sy/comm/page/page.jsp?openTab="
						 + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
            //url=URLEncoder.encode(url,"utf-8");
			return url;
        }
    }
	
	
	/**
	 * 
	 * @param todoBean 待办数据Bean
	 * @param hostAddr 主机地址
	 * @param existJiangang 是否存在兼岗
	 * @return 访问待办的完整URL，可用于门户、RTX、邮件系统。
	 */
    public static String createTodoUrl(Bean todoBean, String hostAddr, boolean existJiangang) {
        // 处理本人和委托的兼岗问题
        String todoUser = "";
        String ownerCode = todoBean.getStr("OWNER_CODE");
        boolean agtentFlag = todoBean.getBoolean("_agtFLag");
        if (existJiangang) {
            if (!agtentFlag) {
                todoUser = ownerCode;
            }
        }
        if (StringUtils.isEmpty(hostAddr)) {
            hostAddr = Context.getSyConf(Constant.CONF_SYS_HOST_ADDR, "NULL");
        }
        StringBuilder result = new StringBuilder();
        if (todoBean.getStr("TODO_URL").indexOf(".showDialog.do") >= 0) {
            result.append("{'_PK_':'").append(todoBean.getStr("TODO_ID"));
            result.append("','servId':'").append("OA_SY_COMM_TODO");
            result.append("'}");
            String url = "";
            try {
                url = hostAddr + "/sy/comm/page/confirm.jsp?param="
                        + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
                if (!todoUser.isEmpty()) {
                    url += "&TODO_USER=" + Hex.encodeHexString(todoUser.getBytes("UTF-8"));
                }
                url += "&hexToStr=true";
                return url;
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage() + ": " + url);
            }
        } else {
            String todoUrl = todoBean.getStr("TODO_URL");
            if (agtentFlag) { // 针对代他人办理情况
                todoUrl = todoUrl.substring(0, todoUrl.length() - 1);
                todoUrl = todoUrl + ",_AGENT_USER_:" + todoBean.getStr("OWNER_CODE") + "}";
            }
            String titleString = todoBean.getStr("TODO_TITLE");
            if (StringUtils.isNotEmpty(titleString) && titleString.length() > 5) {
                titleString = titleString.substring(0, 4) + "…";
            }
            result.append("{'tTitle':'待办--").append(titleString);
            result.append("','url':'").append(todoBean.getStr("SERV_ID"));
            result.append(".card.do?pkCode=").append(todoBean.getStr("TODO_OBJECT_ID1"));
            result.append("','menuFlag':4,'replaceUrl':'").append(todoUrl);
            result.append("'}");
            String url = "";
            try {
                url = hostAddr + "/sy/comm/page/page.jsp?openTab="
                        + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
                if (!todoUser.isEmpty()) {
                    url += "&TODO_USER=" + Hex.encodeHexString(todoUser.getBytes("UTF-8"));
                }
                url += "&hexToStr=true";
                return url;
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage() + ": " + url);
            }
        }
    }

	public static String createBNTodoUrl(Bean todoBean, String hostAddr, boolean existJiangang, boolean sysFlag) {
		// 处理本人和委托的兼岗问题
        String todoUser = "";
        String ownerCode = todoBean.getStr("OWNER_CODE");
        boolean agtentFlag = todoBean.getBoolean("_agtFLag");
        if (existJiangang) {
            if (!agtentFlag) {
                todoUser = ownerCode;
            }
        }
        if (StringUtils.isEmpty(hostAddr)) {
            hostAddr = Context.getSyConf(Constant.CONF_SYS_HOST_ADDR, "NULL");
        }
        StringBuilder result = new StringBuilder();
        if (todoBean.getStr("TODO_URL").indexOf(".showDialog.do") >= 0) {
            result.append("{'_PK_':'").append(todoBean.getStr("TODO_ID"));
            result.append("','servId':'").append("OA_SY_COMM_TODO");
            result.append("'}");
            String url = "";
            try {
                url = hostAddr + "/sy/comm/page/confirm.jsp?param="
                        + Hex.encodeHexString(result.toString().getBytes("UTF-8"));
                return url;
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage() + ": " + url);
            }
        } else {
            String todoUrl = todoBean.getStr("TODO_URL");
            todoUrl = todoUrl.replaceAll("\\.byid\\.do", "\\.card\\.do");
            if (agtentFlag) { // 针对代他人办理情况
                todoUrl = todoUrl.substring(0, todoUrl.length() - 1);
                todoUrl = todoUrl + ",_AGENT_USER_:" + todoBean.getStr("OWNER_CODE") + "}";
            }
            String url = "";
            todoUrl = todoUrl + "&pkCode=" + todoBean.getStr("TODO_OBJECT_ID1");
            String title = todoBean.getStr("TODO_TITLE");
            if (title.length() > 6) {
                title = title.substring(0, 6) + "..";
            }
            url = "{'tTitle':'待办--" + title + "','url':'" + todoUrl;
            result.append("{'tTitle':'待办--").append(title);
            result.append("','url':'").append(todoUrl);
            if (sysFlag) { // 增加是否为跨系统地址 1 为 是
            	url = url + "','_sysFlag_':'1" ;
                result.append("','_sysFlag_':'1");
            }
            if (agtentFlag) {
            	url = url + "','replaceUrl':'" + todoUrl.replaceAll("\\.card\\.do", "\\.byid\\.do");
                result.append("','replaceUrl':'").append(todoUrl.replaceAll("\\.card\\.do", "\\.byid\\.do"));
            }
            url = url + "'}";
            result.append("'}");
            
            try {
            	url = hostAddr + "/sy/comm/page/page.jsp?openTab="
   					 + URLEncoder.encode(url,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return url;
        }
	}
}
