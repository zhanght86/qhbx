package com.rh.core.comm.news.serv;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jcifs.dcerpc.msrpc.netdfs;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.news.mgr.HtmlParser;
import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;

/**
 * 信息管理
 * @author zhanghailong
 */
public class InfosServ extends NewsServ {

	private static final int SHOW_NUM = 5;

	private static final String SY_COMM_INFOS_VIEW = "SY_COMM_INFOS_VIEW";
	private static final String SY_COMM_CMS_CHNL = "SY_COMM_CMS_CHNL";
	private static final String SY_COMM_INFOS_CHNL = "SY_COMM_INFOS_CHNL";

	/**
	 * 
	 * @return 取得信息发布管理员角色
	 */
	private String getInfosAdminRole() {
		return Context.getSyConf("CMS_INFOS_ADMIN_ROLE", "CJADMIN");
	}

	@Override
	protected void beforeQuery(ParamBean paramBean) {
		super.beforeQuery(paramBean);
		UserBean userBean = Context.getUserBean();
		// 接收信息发布菜单传的站点
		if (paramBean.isNotEmpty(NewsMgr.DICT_EXTWHERE)) {
			paramBean.setWhere(paramBean.getStr(NewsMgr.DICT_EXTWHERE));
		}
		if(paramBean.isNotEmpty("WHERE")){
			paramBean.setQueryExtWhere(paramBean.getQueryExtWhere()+paramBean.getStr("WHERE")+"'"+userBean.getODeptCode()+"'");
		}
		paramBean.setQueryExtWhere(paramBean.getQueryExtWhere() + NewsMgr.getAclSql());
		// 设置信息管理员用户权限
		
		if (userBean != null && userBean.existInRole(getInfosAdminRole())) {
			Context.getThread("_IS_SERV_DACL_ADMIN", true);
		} else {
			Context.setThread("_IS_SERV_DACL_ADMIN", false);
		}
	}

	@Override
	protected void afterDelete(ParamBean paramBean, OutBean outBean) {
		Bean sqlBean = new Bean().set("TODO_OBJECT_ID1", outBean.get("_DELIDS_"));
		TodoUtils.ends(sqlBean);
	}
	/**
	 * 获取当前用户
	 * @param paramBean
	 * @return
	 */
	public UserBean getUserBean(ParamBean paramBean){
		return Context.getUserBean();
	}

	public Bean getChnlBean(ParamBean paramBean){
		
		return getChannelPropertiesBean(paramBean.getStr("CHNL_ID"));
	}
	
	@Override
	protected void afterSave(ParamBean paramBean, OutBean outBean) {
		// 自动填写摘要、短标题
		String body = outBean.getStr("NEWS_BODY");
		body = new HtmlParser().parse(body);
		Bean upBean = new Bean();
		if (outBean.isEmpty("SITE_ID")) {
			// 获取栏目的站点信息
			String siteId = this.getSiteId(outBean.getStr("CHNL_ID"));
			upBean.set("SITE_ID", siteId).setId(outBean.getId());
		}

		// 是否有图
		Bean imgQuery = new Bean().set("DATA_ID", outBean.getId());
		
		Bean fengmianji=new Bean().set("DATA_ID", outBean.getId());
		imgQuery.set("FILE_CAT", "TUPIANJI");
		fengmianji.set("FILE_CAT","FENGMIANJI");
		int imgCount = ServDao.count(ServMgr.SY_COMM_FILE, imgQuery);
		int fengmianjiCount=ServDao.count(ServMgr.SY_COMM_FILE, fengmianji);
		
		if (imgCount > 0 || fengmianjiCount>0 ) {
			upBean.set("HAS_IMAGE", 1).setId(outBean.getId());
		}
		
		// 判断是否从其他栏目同步过来的新闻 added by tanyh 20161108
		if (paramBean.containsKey("IS_SYNC")
				&& "true".equals(paramBean.getStr("IS_SYNC"))) {
			// 是，则获取新闻所属栏目的类型，且如果没有设置封面图片，则使用默认的封面图片
			List<Bean> chnlTypeList = ServDao.finds("BN_NEWS_CHNL_TYPE", " and CHNL_ID='"
					+ outBean.getStr("CHNL_ID") + "' order by S_MTIME desc");
			if (chnlTypeList != null && chnlTypeList.size() > 0) {
				Bean chnlType = chnlTypeList.get(0);
				upBean.set("NEWS_TYPE", chnlType.getInt("CHNL_TYPE")).setId(
						outBean.getId());
				if (fengmianjiCount <= 0) {
					List<Bean> fengmianList = ServDao.finds(
							ServMgr.SY_COMM_FILE,
							(new Bean()).set("DATA_ID", chnlType.getId()).set(
									"FILE_CAT", "FENGMIANJI"));
					if (fengmianList != null && fengmianList.size() > 0) {
						Bean fengmian = fengmianList.get(0);
						Bean newFile = new Bean();
						newFile.set("DATA_ID", outBean.getId());
						ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
						if (servDef.getSrcId().length() > 0) {
							newFile.set("SERV_ID", servDef.getSrcId());
						} else {
							newFile.set("SERV_ID", paramBean.getServId());
						}
						// 复制封面图片
						FileMgr.copyFile(fengmian, newFile);

						upBean.set("HAS_IMAGE", 1).setId(outBean.getId());
					}
				}
			}
		}

		// 是否有附件
		Bean attachQuery = new Bean().set("DATA_ID", outBean.getId());
		attachQuery.set("FILE_CAT", "FUJIAN");
		int aCount = ServDao.count(ServMgr.SY_COMM_FILE, attachQuery);
		if (aCount > 0) {
			upBean.set("HAS_ATTACH", 1).setId(outBean.getId());
		}

		if (null != upBean.getId() && 0 < upBean.getId().length()) {
			ServDao.save("SY_COMM_INFOS", upBean);
		}
		
		
		// 保存信息权限
		saveScopeAcl(paramBean, outBean);
		// 保存信息发布人
		saveAddUserAcl(paramBean, outBean);
	}

	/**
	 * 保存信息发布人
	 * @param paramBean 参数信息
	 * @param outBean  输出信息          
	 */
	private void saveAddUserAcl(ParamBean paramBean, OutBean outBean) {

		if (!paramBean.getAddFlag()) {
			int oldScope = paramBean.getSaveOldData().getInt("NEWS_SCOPE");
			int newScope = paramBean.getSaveFullData().getInt("NEWS_SCOPE");
			if (oldScope == newScope) {
				return;
			}
		}
		ParamBean param = new ParamBean();
		param.set("OWNER_CODE", outBean.getStr("NEWS_USER"));
		param.set("DATA_ID", outBean.getId());
		param.set("ACL_TYPE", NewsAclMgr.ACL_TYPE_SCOPE);
		param.set("SERV_ID", outBean.getStr("serv"));
		ServMgr.act("SY_COMM_NEWS_ACL", "save", param);
	}

	/**
	 * 保存信息权限
	 * @param paramBean 参数信息       
	 * @param outBean 输出信息         
	 */
	private void saveScopeAcl(ParamBean paramBean, OutBean outBean) {

		if (!paramBean.getAddFlag()) {
			int oldScope = paramBean.getSaveOldData().getInt("NEWS_SCOPE");
			int newScope = paramBean.getSaveFullData().getInt("NEWS_SCOPE");
			if (oldScope == newScope) {
				return;
			}
		}

		// @TODO 清除老数据
		NewsAclMgr.delAclByType(outBean.getId(), NewsAclMgr.ACL_TYPE_SCOPE);

		// 保存之前判断是否给单条信息设置权限
		ParamBean param = new ParamBean();
		int newScope = outBean.getInt("NEWS_SCOPE");
		if (newScope == 1) {
			param.set("OWNER_CODE", NewsAclMgr.RPUB);
			param.set("DATA_ID", outBean.getId());
			param.set("ACL_TYPE", NewsAclMgr.ACL_TYPE_SCOPE);
			param.set("SERV_ID", outBean.getStr("serv"));
		} else if (newScope == 2) { // 本级公司
			param.set("OWNER_CODE", outBean.getStr("S_ODEPT"));
			param.set("DATA_ID", outBean.getId());
			param.set("ACL_TYPE", NewsAclMgr.ACL_TYPE_SCOPE);
			param.set("SERV_ID", outBean.getStr("serv"));
		} else if (newScope == 3) { // 本级部门
			param.set("OWNER_CODE", outBean.getStr("S_TDEPT"));
			param.set("DATA_ID", outBean.getId());
			param.set("ACL_TYPE", NewsAclMgr.ACL_TYPE_SCOPE);
			param.set("SERV_ID", outBean.getStr("serv"));
		} else if (newScope == 4) { // 业务条线
			// 本级部门的业务条线
			DeptBean deptBean = OrgMgr.getDept(outBean.getStr("S_TDEPT"));
			if (deptBean != null) {
				param.set("OWNER_CODE", deptBean.getLine());
			} else {
				param.set("OWNER_CODE", "");
			}
			param.set("DATA_ID", outBean.getId());
			param.set("ACL_TYPE", NewsAclMgr.ACL_TYPE_SCOPE);
			param.set("SERV_ID", outBean.getStr("serv"));
		} else if (newScope == 5) { // 角色
			String roleScope = outBean.getStr("NEWS_OWNER");
			if (!roleScope.equals("")) {
				String[] roleScopes = roleScope.split(",");
				for (int i = 0; i < roleScopes.length; i++) {
					ParamBean paramRole = new ParamBean();
					paramRole.set("OWNER_CODE", roleScopes[i]);
					paramRole.set("DATA_ID", outBean.getId());
					paramRole.set("ACL_TYPE", NewsAclMgr.ACL_TYPE_SCOPE);
					param.set("SERV_ID", outBean.getStr("serv"));
					ServMgr.act("SY_COMM_NEWS_ACL", "save", paramRole);
				}
			}
			return;
		}
		ServMgr.act("SY_COMM_NEWS_ACL", "save", param);

	}

	/**
	 * get site id by channel 
	 * @param chnlId - channel id     
	 * @return site id
	 */
	public String getSiteId(String chnlId) {
		Bean chnl = getChannelPropertiesBean(chnlId);
		return chnl.getStr("SITE_ID");
	}

	/**
	 * get channel bean from cache and database(cache first)
	 * 
	 * @param id - channel pk          
	 * @return - cache bean
	 */
	private Bean getChannelPropertiesBean(String id) {
		Bean chnl = ServDao.find(SY_COMM_INFOS_CHNL, new Bean().setId(id));
		return chnl;
	}
	/**
	 * 获取特定栏目的新闻
	 */
	public OutBean getNews(ParamBean paramBean){
		OutBean outBean = new OutBean();
		List<Bean> newsBeans = ServDao.finds("SY_COMM_INFOS_VIEW", " and CHNL_ID = '"+paramBean.getStr("CHNL_ID")+"'");
		if(newsBeans.size()>0){
			for (Bean newsBean : newsBeans) {
				outBean.set("NEWS_BODY", newsBean.get("NEWS_BODY"));
				outBean.set("NEWS_ID", newsBean.get("NEWS_ID"));
			}
		}else{
			outBean.set("NEWS_BODY", "该栏目下没有信息!!!");
		}
		return outBean;
	}
	/**
	 * 获取关于百年栏目下的所有子栏目
	 * @param paramBean
	 * @return
	 */
	public OutBean getBNChnlAll(ParamBean paramBean) {
		String chnlId = paramBean.getStr("CHNL_ID");
		String chnlPid = paramBean.getStr("CHNL_PID");
		String extWhere = "";
		if(chnlId!=null && chnlId.length()>0){
			extWhere=" AND CHNL_ID = '"+chnlId+"'";
		}
		List<Bean> chnlBeans = ServDao.finds("SY_COMM_INFOS_CHNL", " AND CHNL_ID <> '"+chnlPid+"' and CHNL_PATH like '%"+chnlPid+"%'");
		paramBean.setQueryExtWhere(extWhere);
		
		OutBean outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);
		outBean.set("listBeans", chnlBeans);
		return outBean;
	}
	/**
	 * 返回最新的信息
	 * @param paramBean  参数          
	 * @return 返回结果
	 */
	public OutBean getTopInfos(ParamBean paramBean) {
		String chnlId = paramBean.getStr("CHNL_ID");
		String count = paramBean.getStr("COUNT");
		String[] chnlIdArr = chnlId.split(",");
		int len = chnlIdArr.length;
		String extWhere = "";
		if (len > 0) {
			String inChnlSql = "";
			for (int index = 0; index < len; index++) {
				inChnlSql += chnlIdArr[index] + "','";
			}
			inChnlSql = inChnlSql.substring(0, inChnlSql.lastIndexOf(","));
			extWhere += " AND CHNL_ID in ('" + inChnlSql + ")";
		}
		if (count.equals("")) {
			paramBean.setShowNum(SHOW_NUM);
		} else {
			paramBean.setShowNum(Integer.parseInt(count));
		}

		paramBean.setQueryExtWhere(extWhere);
		paramBean.setOrder("NEWS_SORT asc,NEWS_TIME DESC");
		OutBean outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);
		outBean.set("CHNL_ID", chnlId);

		Bean bean = ServDao.find(SY_COMM_CMS_CHNL, new ParamBean().set("_PK_", chnlId));
		if (bean != null) {
			outBean.set("CHNL_NAME", bean.get("CHNL_NAME"));
		} else {
			outBean.set("CHNL_NAME", "栏目名称为空");
		}
		outBean.set("ALLNUM", outBean.getCount());
		return outBean;
	}
    /*************************************通知公告*********************/
	
	/**
	 * 返回通知公告的最新信息【省分公司专用】
	 * 总公司和控股公司获取通知公告的方式不变
	 * 根据指定层级的odept_code，获取该机构下的通知公告。
	 * 如果该机构下有多个通知公告栏目默认使用最新修改的通知公告 
	 * @param paramBean 显示记录数
	 * @return 最新显示的信息
	 */
	public OutBean getNoticeTop(ParamBean paramBean) {
		String count = paramBean.getStr("COUNT");
		OutBean chnlBean = getNoticeChnl();
		String chnlId = chnlBean.getStr("CHNL_ID");
		String chnlname = chnlBean.getStr("CHNL_NAME");
		if (count.equals("")) {
			paramBean.setShowNum(SHOW_NUM);
		} else {
			paramBean.setShowNum(Integer.parseInt(count));
		}        
        String viewExtWhere = " AND CHNL_ID = '" + chnlId + "'";
		paramBean.setQueryExtWhere(viewExtWhere);
		paramBean.setOrder("NEWS_SORT asc,NEWS_TIME DESC");
		OutBean outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);
		outBean.set("CHNL_ID", chnlId);
		outBean.set("CHNL_NAME", chnlname);
		return outBean;
	}
	/**
	 * 根据机构编码获取通知公告的栏目id
	 * @return 栏目id+栏目名称
	 */
	public OutBean getNoticeChnl() {
		 OutBean outBean = new OutBean();
		 int odeptlevel = Context.getUserBean().getODeptLevel();
         String odeptPath = Context.getUserBean().getODeptCodePath();
        //暂时使用于省分公司
        if (odeptlevel >= 3) {
         String extWhere = "";
        	//取三级的机构编码
         String odeptCode = OrgMgr.getOdeptlevelCode(odeptPath, 3);
         //PORTAL_DISPLAY为1表示在个人门户显示
         extWhere = "AND 1=1 AND ODEPT_CODE = '" + odeptCode + "' AND PORTAL_DISPLAY = 1 ";
         ParamBean param = new ParamBean();
         param.setWhere(extWhere);
         //有多个就取最新添加chnl_id
         param.setOrder("S_MTIME ASC");
         List<Bean> beanList = ServDao.finds(SY_COMM_CMS_CHNL, new Bean(param));
         for (Bean beans : beanList) {
        	 outBean.set("CHNL_ID", beans.getStr("CHNL_ID")); 
        	 outBean.set("CHNL_NAME", beans.getStr("CHNL_NAME"));	
          }
        } else {
            outBean.set("CHNL_ID", ""); 
       	    outBean.set("CHNL_NAME", "");
        }
       return outBean; 
	}
	
	
	/**
	 * 获得更多的通知公告信息做分页处理
	 * @param paramBean  参数   
	 * @return 返回结果
	 */
	public OutBean getMoreNotice(ParamBean paramBean) {
		OutBean outBean = new OutBean();
        OutBean chnlBean = getNoticeChnl();
		String chnlid = chnlBean.getStr("CHNL_ID");
		String chnlname = chnlBean.getStr("CHNL_NAME");
		String extWhere = "AND CHNL_ID='" + chnlid + "'";

		paramBean.setQueryExtWhere(extWhere);
		super.beforeQuery(paramBean);
		paramBean.set(Constant.PARAM_SERV_ID, SY_COMM_INFOS_VIEW);
		//添加单条信息权限过滤
		String fullWhere = getFullWhere(paramBean) + NewsMgr.getAclSql();

		int total = this.count(SY_COMM_INFOS_VIEW, new Bean().set(Constant.PARAM_WHERE, fullWhere));
		if (paramBean.contains("curtentpage")) {
			paramBean.setQueryPageNowPage(paramBean.getInt("curtentpage"));
		}
		paramBean.setQueryPageOrder("NEWS_SORT asc,NEWS_TIME DESC");
		paramBean.setQueryPageShowNum(15);
		paramBean.getQueryPage().set("ALLNUM", total);

		outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);

		outBean.set("CHNL_ID", chnlid);
		outBean.set("CHNL_NAME", chnlname);
		outBean.setToDispatcher("/sy/comm/infos/jsp/infosMore.jsp");
		outBean.set("_INFOS_PARAM_", outBean);
		outBean.set("TOTAL", total);
		return outBean;
	}

	/************************************end**************************/
	/**
	 * 返回最新的信息
	 * @param paramBean  参数       
	 * @return 返回结果
	 */
	public OutBean getTopZTInfos(ParamBean paramBean) {
		String chnlId = paramBean.getStr("CHNL_ID");
		String count = paramBean.getStr("COUNT");
		String[] chnlIdArr = chnlId.split(",");
		int len = chnlIdArr.length;
		String extWhere = "";
		if (len > 0) {
			String inChnlSql = "";
			for (int index = 0; index < len; index++) {
				inChnlSql += chnlIdArr[index] + "','";
			}
			inChnlSql = inChnlSql.substring(0, inChnlSql.lastIndexOf(","));
			extWhere += " AND CHNL_PID in ('" + inChnlSql + ")";
		}
		if (count.equals("")) {
			paramBean.setShowNum(SHOW_NUM);
		} else {
			paramBean.setShowNum(Integer.parseInt(count));
		}

		paramBean.setQueryExtWhere(extWhere);
		paramBean.setOrder("CHNL_TIME DESC");
		OutBean outBean = ServMgr.act(SY_COMM_CMS_CHNL, "query", paramBean);
		outBean.set("CHNL_PID", chnlId);
		return outBean;
	}

	/**
	 * 返回信息的更多做分页处理
	 * @param paramBean  参数   
	 * @return 返回结果
	 */
	public OutBean getMoreInfos(ParamBean paramBean) {
		OutBean outBean = new OutBean();

		String chnlid = paramBean.getStr("CHNL_ID");
		String extWhere = "AND CHNL_ID='" + chnlid + "'";

		paramBean.setQueryExtWhere(extWhere);
		super.beforeQuery(paramBean);
		paramBean.set(Constant.PARAM_SERV_ID, SY_COMM_INFOS_VIEW);
		String fullWhere = getFullWhere(paramBean) + NewsMgr.getAclSql();

		int total = this.count(SY_COMM_INFOS_VIEW, new Bean().set(Constant.PARAM_WHERE, fullWhere));
		if (paramBean.contains("curtentpage")) {
			paramBean.setQueryPageNowPage(paramBean.getInt("curtentpage"));
		}
		paramBean.setQueryPageOrder("NEWS_SORT asc,NEWS_TIME DESC");
		paramBean.setQueryPageShowNum(15);
		paramBean.getQueryPage().set("ALLNUM", total);

		outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);

		outBean.set("CHNL_ID", chnlid);
		Bean bean = ServDao.find(SY_COMM_CMS_CHNL, new ParamBean().set("_PK_", chnlid));
		outBean.set("CHNL_NAME", bean.get("CHNL_NAME"));

		outBean.setToDispatcher("/sy/comm/infos/jsp/infosMore.jsp");
		outBean.set("_INFOS_PARAM_", outBean);
		outBean.set("TOTAL", total);
		return outBean;
	}

	/**
	 * 根据paramBean参数组装的where条件获取数据的数量
	 * @param servId 服务Id          
	 * @param paramBean 参数信息    
	 * @return 符合条件的数据数量
	 */
	public int count(String servId, Bean paramBean) {
		ServDefBean servDef = ServUtils.getServDef(servId);
		paramBean.set(Constant.PARAM_SELECT, " count(*) COUNT_ ");
		String psql = Transaction.getBuilder().select(servDef, paramBean);
		List<Bean> crazy = Transaction.getExecutor().query(psql, paramBean.getList(Constant.PARAM_PRE_VALUES));
		int count = crazy.get(0).getInt("COUNT_");
		return count;
	}

	/**
	 * 返回专题的更多做分页处理
	 * @param paramBean 参数          
	 * @return 返回结果
	 */
	public OutBean getMoreZT(ParamBean paramBean) {
		OutBean outBean = new OutBean();

		String chnlid = paramBean.getStr("CHNL_PID");
		String extWhere = "AND CHNL_PID='" + chnlid + "'";

		paramBean.setQueryExtWhere(extWhere);
		super.beforeQuery(paramBean);
		paramBean.set(Constant.PARAM_SERV_ID, SY_COMM_INFOS_CHNL);
		String fullWhere = getFullWhere(paramBean);

		int total = ServDao.count(SY_COMM_INFOS_CHNL, new SqlBean().set(Constant.PARAM_WHERE, fullWhere));
		if (paramBean.contains("curtentpage")) {
			paramBean.setQueryPageNowPage(paramBean.getInt("curtentpage"));
		}
		paramBean.setQueryPageOrder("CHNL_TIME DESC");
		paramBean.setQueryPageShowNum(15);
		paramBean.getQueryPage().set("ALLNUM", total);

		//outBean = ServMgr.act(SY_COMM_INFOS_CHNL, "query", paramBean);
		outBean = ServMgr.act(SY_COMM_CMS_CHNL, "query", paramBean);

		outBean.setToDispatcher("/sy/comm/infos/jsp/infosZTMore.jsp");
		outBean.set("_INFOSZT_PARAM_", outBean);
		outBean.set("CHNL_PID", chnlid);
		outBean.set("CHNL_NAME", "专题更多");
		outBean.set("TOTAL", total);
		return outBean;
	}

	/**
	 * 添加点击量
	 * @param param  主键      
	 * @return 点击量
	 */
	public Bean increaseReadCounter(Bean param) {
		Bean queryBean = new Bean();
		queryBean.set("NEWS_ID", param.getId());
		Bean currentHis = new Bean();
		currentHis.set("NEWS_ID", param.getId());
		currentHis = ServDao.find(ServMgr.SY_COMM_INFOS, queryBean);
		if (null != currentHis) {
			int currentCounter = currentHis.get("COUNTER", 0);
			currentHis.set("COUNTER", currentCounter + 1);
			ServDao.save(ServMgr.SY_COMM_INFOS, currentHis);
		}
		return currentHis;
	}

	/**
	 * 获取各分公司新闻中心 
	 * @param param [PT_TYPE]
	 * @return 返回
	 */
	public OutBean getCpyTmpl(ParamBean param) {
		ParamBean params = new ParamBean();
		params.putAll(param);
		OutBean outBean = ServMgr.act("SY_COMM_TEMPL", "finds", params);
		return outBean;
	}
	
	/**
	 * 获取各子公司新闻中心 20150211
	 * @param param [PT_TYPE]
	 * @return 返回
	 */
	public OutBean getSubpyTmpl(ParamBean param) {
		ParamBean params = new ParamBean();
    	params.putAll(param);
    	params.setOrder("TEMPL_SORT desc");
		OutBean outBean = ServMgr.act("SY_COMM_TEMPL", "finds", params);
		return outBean;
	}

	/**
	 * 获得更多的新闻中心
	 * @param paramBean 入参           
	 * @return 返回
	 */
	public OutBean getMoreMhTmpl(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String pttype = paramBean.getStr("PT_TYPE");
		String extWhere = "AND PT_TYPE='" + pttype + "'";
		paramBean.setQueryExtWhere(extWhere);
		int total = ServDao.count("SY_COMM_TEMPL", new Bean().set(Constant.PARAM_WHERE, extWhere));

		if (paramBean.contains("curtentpage")) {
			paramBean.setQueryPageNowPage(paramBean.getInt("curtentpage"));
		}
		paramBean.setQueryPageOrder("TEMPL_SORT asc,S_MTIME DESC");
		paramBean.setQueryPageShowNum(15);
		paramBean.getQueryPage().set("ALLNUM", total);
		paramBean.setSelect("PT_ID,PT_TITLE,S_MTIME,PT_TYPE");
		outBean = ServMgr.act("SY_COMM_TEMPL", "query", paramBean);
		outBean.set("PT_TYPE", pttype);
		outBean.setToDispatcher("/sy/comm/infos/jsp/infosMHMore.jsp");
		outBean.set("_INFOS_PARAM_", outBean);
		outBean.set("TOTAL", total);
		return outBean;
	}

	/**
	 * 返回最新的信息,包括子栏目下的所有数据
	 * 
	 * @param paramBean
	 *            参数
	 * @return 返回结果
	 */
	public OutBean getTopInfosAll(ParamBean paramBean) {
		String chnlId = paramBean.getStr("CHNL_ID");
		String count = paramBean.getStr("COUNT");
		String extWhere = "";
		ParamBean query = new ParamBean();
		query.setSelect("CHNL_ID");
		query.setWhere(" AND CHNL_PATH like '%" + chnlId + "%'");
		OutBean chnlIdsBean = ServMgr.act(SY_COMM_CMS_CHNL, "query", query);
		List<Bean> list = chnlIdsBean.getDataList();
		if (!list.isEmpty()) {
			String inChnlSql = "";
			for (Bean bean : list) {
				String id = bean.getStr("CHNL_ID");
				inChnlSql += id + "','";
			}
			inChnlSql = inChnlSql.substring(0, inChnlSql.lastIndexOf(","));
			extWhere += " AND CHNL_ID in ('" + inChnlSql + ")";
		}
		if (count.equals("")) {
			paramBean.setShowNum(SHOW_NUM);
		} else {
			paramBean.setShowNum(Integer.parseInt(count));
		}

		paramBean.setQueryExtWhere(extWhere);
		/*paramBean.setOrder("NEWS_TIME_SORT DESC");*/
		OutBean outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);
		outBean.set("CHNL_ID", chnlId);

		Bean bean = ServDao.find(SY_COMM_CMS_CHNL, new ParamBean().set("_PK_", chnlId));
		outBean.set("CHNL_NAME", bean.get("CHNL_NAME"));
		return outBean;
	}

	/**
	 * 返回信息的更多做分页处理[带时间]
	 * 
	 * @param paramBean
	 *            参数
	 * @return 返回结果
	 */
	public OutBean getDateMoreInfos(ParamBean paramBean) {
		ParamBean copyParamBean = paramBean.copyOf();
		OutBean outBean = new OutBean();

		String chnlid = paramBean.getStr("CHNL_ID");
		String chnlName = paramBean.getStr("CHNL_NAME");
		String extWhere = "AND CHNL_ID='" + chnlid + "'";

		paramBean.setQueryExtWhere(extWhere);
		super.beforeQuery(paramBean);
		paramBean.set(Constant.PARAM_SERV_ID, SY_COMM_INFOS_VIEW);
		String fullWhere = getFullWhere(paramBean) + NewsMgr.getAclSql();

		if (paramBean.contains("curtentpage")) {
			paramBean.setQueryPageNowPage(paramBean.getInt("curtentpage"));
		}
		/*paramBean.setQueryPageOrder("NEWS_TIME_SORT DESC");*/
		paramBean.setQueryPageShowNum(15);
		if (paramBean.isNotEmpty("CUR_DATE")) {
			// 兼容时间的参数传入
			paramBean.setWhere("AND NEWS_TIME LIKE '" + paramBean.getStr("CUR_DATE") + "%'");
		}
		outBean = ServMgr.act(SY_COMM_INFOS_VIEW, "query", paramBean);
		outBean.set("CHNL_ID", chnlid);
		outBean.set("CHNL_NAME", chnlName);
		// 放年份
		outBean.set("SET_DATE", this.getNewTime(fullWhere));
		outBean.setToDispatcher("/sy/comm/infos/jsp/infosDateMore.jsp");

		// 放选中年份
		outBean.set("CUR_DATE", paramBean.getStr("CUR_DATE"));
		if (outBean.getCount() == 0) {
			copyParamBean.set("CUR_DATE", String.valueOf(Integer.parseInt(paramBean.getStr("CUR_DATE")) - 1));
			return getDateMoreInfos(copyParamBean);
		} else {
			return outBean;
		}
	}

	/**
	 * 获取不重复的年份
	 * 
	 * @param extwhere
	 *            外部的条件
	 * @return 不重复的年份
	 */
	public Set<String> getNewTime(String extwhere) {
		// Set<String> setData = new HashSet<String>();
		Set<String> setData = new TreeSet<String>();
		List<Bean> dataBean = ServDao.finds("SY_COMM_INFOS_VIEW", new ParamBean().setWhere(extwhere));
		for (Bean bean : dataBean) {
			setData.add(bean.getStr("NEWS_TIME").substring(0, 4));
		}
		return setData;
	}
	
	/**
	 * 获取可查看人
	 * @param paramBean 传入参数
	 */
	public OutBean getNewsOwner (ParamBean paramBean) {
		String newsOwnerIds[] = paramBean.getStr("NEWS_OWNER").split(Constant.SEPARATOR);
		OutBean outbean = new OutBean();
		List<Bean> beanList = new ArrayList<Bean>();
		for (String id : newsOwnerIds) {
			String name = "";
			name = DictMgr.getFullName("SY_ORG_DEPT_USER_ALL", id);
			if ("".equals(name) || name == null || name.equals(id)) {
				name = DictMgr.getFullName("SY_ORG_ROLE", id);
			}
			Bean dcitBean = new Bean().set("NAME", name).set("ID", id);
			beanList.add(dcitBean);
		}
		return  outbean.setData(beanList);
	}


}
