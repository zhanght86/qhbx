package com.rh.core.wfe.remind;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.remind.RemindMgr;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.comm.todo.TodoUtils.ToDoItem;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.RequestUtils;

/**
 * 处理催督办服务的监听类
 * 
 * @author cuihf
 * 
 */
public class RemindServ extends CommonServ {

    /**
     * 服务ID
     */
    public static final String SERV_ID = "SY_WFE_REMIND";

    /**
     * 状态：未送出
     */
    public static final int STATE_NOSEND = 0;

    /**
     * 状态：未反馈
     */
    public static final int STATE_SENT = 1;

    /**
     * 状态：已反馈
     */
    public static final int STATE_FEEDBACK = 2;

    /**
     * 状态：已办结
     */
    public static final int STATE_FINISH = 3;

    /**
     * 根据代字、年度取得最大流水号
     * @param paramBean 参数信息
     * @return 流水号
     */
    public OutBean getMaxCode(ParamBean paramBean) {
        String servId = paramBean.getServId();
        Bean cdBean = new Bean();
        cdBean.set(WfeRemindItem.REMD_CODE, paramBean.getStr(WfeRemindItem.REMD_CODE));
        cdBean.set(WfeRemindItem.REMD_YEAR, paramBean.getStr(WfeRemindItem.REMD_YEAR));
        Bean codeBean = RemindUtils.getMaxCode(servId, cdBean, WfeRemindItem.REMD_NUM);
        return new OutBean(codeBean);
    }

    /**
     * 发送催办单
     * @param paramBean 参数信息
     * @return 送交信息
     */
    public OutBean sendTodo(ParamBean paramBean) {
        String servId = paramBean.getServId();
        OutBean cdBean = this.byid(paramBean);
        TodoBean todoBean = new TodoBean();
        todoBean.setSender(cdBean.getStr(WfeRemindItem.S_USER));
        todoBean.setTitle(cdBean.getStr(WfeRemindItem.REMD_TITLE));
        todoBean.setOwner(cdBean.getStr(WfeRemindItem.ACPT_USER));
        todoBean.setCode(servId);
        
        String dataCode = cdBean.getStr(WfeRemindItem.REMD_CODE) + "(" + cdBean.getStr(WfeRemindItem.REMD_YEAR) 
                          + ")" + cdBean.getStr(WfeRemindItem.REMD_NUM) + "号";
        /*todoBean.setDataCode(dataCode);*/
        
        todoBean.setObjectId1(paramBean.getStr(WfeRemindItem.REMD_ID));
        todoBean.setUrl(servId + ".byid.do?data={_PK_:" + paramBean.getStr(WfeRemindItem.REMD_ID) + "}");
        TodoUtils.insert(todoBean, (paramBean.getInt("IF_SENDSMS")==1));
        cdBean.set(WfeRemindItem.REMD_STATUS, new Integer(STATE_SENT));
        ServDao.update(servId, cdBean);
        // this.modify(cdBean);
        cdBean.setOk();
        return cdBean;
    }

    /**
     * 办结催办单
     * @param paramBean 参数信息
     * @return 办结信息
     */
    public OutBean finish(ParamBean paramBean) {
        String servId = paramBean.getServId();
        OutBean cdBean = this.byid(paramBean);
        if (cdBean.getId().length() <= 0) {
            throw new TipException(Context.getSyMsg("SY_DATA_NOT_EXIST", paramBean.getId()));
        }
        cdBean.set(WfeRemindItem.REMD_STATUS, new Integer(STATE_FINISH));
        ServDao.update(servId, cdBean);
        Bean todoBean = new Bean();
        todoBean.set("TODO_OBJECT_ID1", paramBean.getStr(WfeRemindItem.REMD_ID));
        todoBean.set("TODO_CODE", servId);
        TodoUtils.ends(todoBean);
        cdBean.setOk();
        return cdBean;
    }

    /**
     * 取消办结催办单
     * @param paramBean 参数信息
     * @return 取消办结信息
     */
    public OutBean unfinish(ParamBean paramBean) {
        OutBean cdBean = this.byid(paramBean);
        if (cdBean.getId().length() <= 0) {
            throw new TipException(Context.getSyMsg("SY_DATA_NOT_EXIST", paramBean.getId()));
        }
        cdBean.set(WfeRemindItem.REMD_STATUS, new Integer(STATE_NOSEND));
        ServDao.update(paramBean.getServId(), cdBean);
        cdBean.setOk();
        return cdBean;
    }

    @Override
    public OutBean byid(ParamBean paramBean) {
        OutBean outBean = super.byid(paramBean);
        if (outBean.isEmpty(WfeRemindItem.REMD_TITLE)) {
            appendServDataInfo(paramBean, outBean);
            appendOtherData(paramBean, outBean);
        }
        return outBean;
    }

    /**
     * 设置办理期限和被催办用户。ACPT_USER ,DEADLINE
     * @param paramBean 参数Bean
     * @param outBean 输出Bean
     */
    private void appendOtherData(ParamBean paramBean, OutBean outBean) {
        if (paramBean.isNotEmpty("DEADLINE")) {
            outBean.set(WfeRemindItem.DEADLINE, paramBean.getStr("DEADLINE"));
        }
        
        if (paramBean.isNotEmpty("ACPT_USER")) {
        	UserBean user = UserMgr.getUser(paramBean.getStr("ACPT_USER"));
            outBean.set(WfeRemindItem.ACPT_USER, user.getCode());
            outBean.set(WfeRemindItem.ACPT_USER + "_NAME", user.getName());
            outBean.set(WfeRemindItem.ACPT_USER + "_V", user.getName());
            outBean.set("ACPT_PHONE", user.getOfficePhone());
            outBean.set("USER_NAME", user.getName());
        }
    }

    /**
     * DATA_ID ,SERV_ID ,
     * @param paramBean 参数Bean
     * @param outBean 输出Bean
     */
    private void appendServDataInfo(ParamBean paramBean, OutBean outBean) {
        if (paramBean.isEmpty("DATA_ID") || paramBean.isEmpty("SERV_ID")) {
            return;
        }

        String dataId = paramBean.getStr("DATA_ID");
        String servId = paramBean.getStr("SERV_ID");
        ServDefBean servDef = ServUtils.getServDef(servId);
        Bean dataBean = ServDao.find(servId, dataId);
        if (dataBean == null) {
            return;
        }
        
        outBean.set("DATA_ID", dataId);
        outBean.set("SERV_ID", servId);

        String title = "";
        if (servDef.getDataTitle().length() > 0) {
            title = ServUtils.replaceValues(servDef.getDataTitle(), servId, dataBean);
            outBean.set(WfeRemindItem.REMD_TITLE, title);
        }

        if (servDef.getDataCode().length() > 0) {
            final String code = ServUtils.replaceValues(servDef.getDataCode(), servDef.getId(), dataBean);
            outBean.set(WfeRemindItem.REMD_REASON, title + " " + code);
        }

    }

    @Override
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        super.afterDelete(paramBean, outBean);

        //删除催办的同时，删除催办单待办
        String servId = paramBean.getServId();
        List<Bean> list = outBean.getDataList();

        for (Bean bean : list) {
            TodoUtils.destroys(servId, bean.getId());
        }
    }
    /**
     * 设置待办为特急、邮件提醒进行催办
     * @param paramBean
     * @return
     */
    public OutBean remindByTodo(ParamBean paramBean) {
    	OutBean outBean = new OutBean();
    	//查找待办sql
    	StringBuffer sb = new StringBuffer("");
    	//服务id
    	//sb.append(" and SERV_ID='" + paramBean.getStr("SERV_ID") + "'");
    	//办理用户code
    	sb.append(" and OWNER_CODE='" + paramBean.getStr("ACPT_USER") + "'");
    	//节点实例id
    	sb.append(" and TODO_OBJECT_ID2='" + paramBean.getStr("NI_ID") + "'");
    	//数据id
    	sb.append(" and TODO_OBJECT_ID1='" + paramBean.getStr("DATA_ID") + "'");
    	//获取待办
    	List<Bean> todoList = ServDao.finds(ServMgr.SY_COMM_TODO, sb.toString());
    	if (todoList == null || todoList.size() <=0 || todoList.size() > 1) {
    		outBean.setError("未找到待办信息");
    		return outBean;
    	}
    	//修改待办信息
    	Bean todoBean = todoList.get(0);
    	//如果催办过，则不进行处理，只发邮件提醒
    	boolean canUpdate = false;
    	if (!"30".equals(todoBean.getStr("S_EMERGENCY"))) {
    		//修改为特急
        	todoBean.set("S_EMERGENCY", 30);
        	canUpdate = true;
    	}
    	if (todoBean.getStr("TODO_TITLE").indexOf("催办：") < 0) {
        	//待办标题前增加“请办理：”字样
        	todoBean.set("TODO_TITLE", "催办：" + todoBean.getStr("TODO_TITLE"));
        	canUpdate = true;
    	}
    	if (canUpdate) {
        	ServDao.update(ServMgr.SY_COMM_TODO, todoBean);
    	}
    	//发送邮件提醒
    	Bean remindBean = new Bean();
        StringBuilder msg = new StringBuilder();
        msg.append("(催办)您有一项待办事务：");
        msg.append(todoBean.getStr(ToDoItem.TODO_TITLE));
        
        // 外网添加系统配置，外网url向内网推送
        String sysHost = Context.getSyConf("SY_REMOTE_TODO_URL", "");
        if (sysHost.length() == 0) {
            sysHost = RequestUtils.getSysHost();
        }
        String remoteUrl = TodoUtils.createTodoUrl(todoBean, sysHost, false);
        if (todoBean.isNotEmpty("TODO_URL")) {
        	msg.append(" [<a href='").append(remoteUrl).append("'>详情</a>]");
        }
        
        remindBean.set("REM_TITLE", todoBean.getStr(ToDoItem.TODO_TITLE));
        remindBean.set("REM_CONTENT", msg.toString());
        remindBean.set("S_USER", todoBean.getStr(ToDoItem.SEND_USER_CODE));
        remindBean.set("EXECUTE_TIME", "");
        remindBean.set("TYPE", "EMAIL");
        remindBean.set("S_EMGRENCY", todoBean.getInt(ToDoItem.S_EMERGENCY));
        remindBean.set("SERV_ID", todoBean.getStr(ToDoItem.SERV_ID));
        remindBean.set("SERV_SRC_ID", todoBean.getStr(ToDoItem.SERV_ID));
        remindBean.set("DATA_ID", todoBean.getStr(ToDoItem.TODO_OBJECT_ID2));//添加外部URL
        remindBean.set("REMOTE_URL", remoteUrl);
        RemindMgr.add(remindBean, todoBean.getStr(ToDoItem.OWNER_CODE));
    	outBean.setOk("成功催办");
    	return outBean;
    }
}
