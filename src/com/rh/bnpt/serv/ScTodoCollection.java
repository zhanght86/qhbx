package com.rh.bnpt.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.UserStateBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 获取门户及集成系统待办的集合
 * 
 * @author ZJW
 * 
 */
public class ScTodoCollection {

	private ScNewTodoWebServ newOaws = null;
	
	/** 用户状态服务 */
	private static final String SEPARATOR_CODE_AND_DATE = "#";
	/** 服务：OA受托列表服务 跨数据源服务 */
//	private static final String SC_OA_USER_TYPE_AGENT_TO = "SC_OA_USER_TYPE_AGENT_TO";
	private static final String SC_OA_USER_TYPE_AGENT_TO = "SY_ORG_USER_TYPE_AGENT_TO";
	
	
	/** 服务：OA委托类型设置 跨数据源服务 */
//	private static final String SC_OA_USER_AGT_TYPE = "SC_OA_USER_AGT_TYPE";
	private static final String SC_OA_USER_AGT_TYPE = "SY_ORG_USER_AGT_TYPE";
	
	/** 服务：OA用户状态 跨数据源服务 */
//	private static final String SC_OA_USER_STATE = "SC_OA_USER_STATE";
	private static final String SC_OA_USER_STATE = "SY_ORG_USER_STATE";
	

	private Comparator<Bean> comparator = new Comparator<Bean>() {
		public int compare(Bean b1, Bean b2) {
			return b2.getStr("TODO_SEND_TIME").compareTo(b1.getStr("TODO_SEND_TIME"));
		}
	};

	/**
	 * 取门户本身待办
	 * 
	 * @param userCode
	 *            用户编码
	 * @return list
	 */
	public ArrayList<Bean> getPtTodos(String userCode) {

//		String where = "and TODO_SEND_TIME >= '"+DateVar.getInst().get("@DATE_DIFF_DD30_N@")+"' and TODO_SEND_TIME < '"+DateUtils.getDatetime()+"'";
		ParamBean paramBean = new ParamBean();
		paramBean.setServId("SY_COMM_TODO");
		paramBean.setShowNum(ConfMgr.getConf("BN_SC_TODO_SHOWNUM", 50));
		paramBean.setOrder("S_EMERGENCY desc,TODO_SEND_TIME desc");
		paramBean.setAct("query");
		ArrayList<Bean> todoListtmp = (ArrayList<Bean>) ServMgr.act(paramBean).getDataList();
		UserBean user = null;
		ArrayList<Bean> todoList = new ArrayList<Bean>();
		for (Bean bean : todoListtmp) {
			String shortTitle = bean.get("TODO_TITLE", "");
			bean.set("SHORT_TITLE", shortTitle);
			if(bean.getStr("TODO_CODE_NAME").length()>5){
			    bean.set("TODO_CODE_NAME",bean.getStr("TODO_CODE_NAME").substring(0, 3)+"...");
			}
			bean.set("SYS_NAME", bean.getStr("TODO_CODE_NAME"));
			bean.set("SYS_CODE", "PT");

			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
		}
		return todoList;
	}

	/**
	 * 取门户本身待阅
	 * 
	 * @param userCode
	 *            用户编码
	 * @return list
	 */
	public ArrayList<Bean> getPtReads(String userCode) {

		String where = "and owner_code = '" + userCode + "' and TODO_CATALOG=2 ";
		ParamBean paramBean = new ParamBean();
		paramBean.setServId("SY_COMM_TODO");
		paramBean.setWhere(where);
		ArrayList<Bean> todoListtmp = (ArrayList<Bean>) ServDao.finds("SY_COMM_TODO", paramBean);
		UserBean user = null;
		ArrayList<Bean> todoList = new ArrayList<Bean>();
		for (Bean bean : todoListtmp) {
			String shortTitle = bean.get("TODO_TITLE", "");
			bean.set("SHORT_TITLE", shortTitle);
			bean.set("SYS_NAME", "门户待阅");
			bean.set("SYS_CODE", "PT");

			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
		}
		return todoList;
	}

	/**
	 * 取新OA系统待办
	 * 
	 * @param userCode
	 *            用户编码
	 * @return list
	 */
	public ArrayList<Bean> getNewOaTodos(String userCode) {
		String serverAddre = "";/*Context.getSyConf("OA_HOST_CAPITAL", "http://127.0.0.1:8081");*/ // OA服务器地址
		String where = "and owner_code = '" + userCode + "' and (TODO_CATALOG in(1,3) OR (TODO_CATALOG = '2' AND S_MTIME >= '2015-01-01')) ";
		ParamBean paramBean = new ParamBean();
		paramBean.setServId("SY_COMM_TODO");
		paramBean.setAct("finds");
		paramBean.setWhere(where);
		List<Bean> todoListTemp = ServMgr.act(paramBean).getDataList();
		UserBean user = null;
		ArrayList<Bean> todoList = new ArrayList<Bean>();
		for (Bean bean : todoListTemp) {
			String shortTitle = bean.get("TODO_TITLE", "");

			bean.set("SHORT_TITLE", shortTitle);
			bean.set("SYS_NAME", "OA待办");
			bean.set("SYS_CODE", "OA");

			bean.set("_agtFLag", false);
			bean.set("TODO_URL", TodoUtils.createTodoUrl(bean, serverAddre, false, true));

			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
		}
		return todoList;
	}

	/**
	 * 取新OA系统待阅
	 * 
	 * @param userCode
	 *            用户编码
	 * @return list
	 */
	public ArrayList<Bean> getNewOaReads(String userCode) {
		String serverAddre = Context.getSyConf("OA_HOST_CAPITAL", "http://portal.capitalwater.cn:8081"); // OA服务器地址
		String where = "and owner_code = '" + userCode + "' and TODO_CATALOG=2 and S_MTIME <= '2015-01-01' ";
		ParamBean paramBean = new ParamBean();
		paramBean.setServId("SY_COMM_TODO");
		paramBean.setAct("finds");
		paramBean.setWhere(where);
		List<Bean> todoListtmp = ServMgr.act(paramBean).getDataList();
		UserBean user = null;
		ArrayList<Bean> todoList = new ArrayList<Bean>();
		for (Bean bean : todoListtmp) {
			String shortTitle = bean.get("TODO_TITLE", "");

			bean.set("SHORT_TITLE", shortTitle);
			bean.set("SYS_NAME", "OA待阅");
			bean.set("SYS_CODE", "OA");

			bean.set("_agtFLag", false);
			bean.set("TODO_URL", TodoUtils.createTodoUrl(bean, serverAddre, false, true));

			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
		}
		return todoList;

	}

	/**
	 * 取老系统待办，待阅
	 * 
	 * @param userCode
	 *            用户编码
	 * @param catalog
	 * 			处理类型 0 为待办 其它为待阅
	 * @return 待办、待阅集合
	 */
	public ArrayList<Bean> getOldOaTodos(String userCode, String catalog) {
		int userCodeint = 0;
		try {
			userCodeint = Integer.parseInt(userCode);
		} catch (Exception e) { //因老系统用户为数值型数据，若userCode不能转换则表明老系统中不存在此用户，则直接返回空
			return new ArrayList<Bean>();
		}
		String where = "and TODO_DO_USERID = " + userCode
				+ " and TODO_CATALOG="
				+ catalog + " and DEL_FLAG = '0' and TODO_FLAG = '0' or (TODO_ROLE = '1'"
				+ " and TODO_DO_USERID in (SELECT ROLEID from assignment where rightid=" + userCodeint
				+ " and assignmenttype=1))";
		ParamBean paramBean = new ParamBean();
		paramBean.setServId("TBL_COMN_TODO");
		paramBean.setSelect("TODO_CAPTION TODO_TITLE,TODO_SEND_TIME,TODO_URL"
				+ ",TODO_SEND_USERID SEND_USER_CODE,CODE_ALIAS TODO_CODE_NAME,OBJECT_ID TODO_OBJECT_ID1");
		paramBean.setAct("finds");
		paramBean.setWhere(where);
		List<Bean> todoListtmp = ServMgr.act(paramBean).getDataList();
		UserBean user = null;
		ArrayList<Bean> todoList = new ArrayList<Bean>();
		for (Bean bean : todoListtmp) {
			String shortTitle = bean.get("TODO_TITLE", "");
			bean.set("SHORT_TITLE", shortTitle);
			if ("0".equals(catalog)) {
				bean.set("SYS_NAME", "旧OA待办");
			} else {
				bean.set("SYS_NAME", "旧OA待阅");
			}
			bean.set("SYS_CODE", "OLD_OA");
			bean.set("OWNER_CODE", "");
			bean.set("TODO_ID", "");
			bean.set("SERV_ID", "");
			bean.set("TODO_CATALOG", "");
			bean.set("TODO_CONTENT", "");

			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
		}
		return todoList;
		
	}

	/**
	 * 集成各系统的待办列表，包括oa兼职待办及门户待办
	 * 
	 * @return OutBean
	 */
	public OutBean getTodos() {
		OutBean outBean = new OutBean();
		ArrayList<Bean> ptTodos = null;
		ArrayList<Bean> oaTodos = null;
		ArrayList<Bean> todos = new ArrayList<Bean>();
		UserBean userBean = Context.getUserBean();
		if(userBean== null){
		    return outBean;
		}
		 String userCode = userBean.getCode();
	
		try {
			// newOaws = new ScNewTodoWebServ();
			ptTodos = getPtTodos(userCode);
			if (oaTodos != null) {
				todos.addAll(oaTodos);
			}
			/*ArrayList<Bean> oldOaTodos = getOldOaTodos(userCode, "0");
			if (oldOaTodos != null) {
				todos.addAll(oldOaTodos);
			}*/
			todos.addAll(ptTodos);
			Collections.sort(todos, comparator);
			outBean.setData(todos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outBean;
	}

	/**
	 * 集成各系统的待阅列表，包括oa待阅及门户待阅
	 * 
	 * @return OutBean
	 */
	public OutBean getReads() {

		OutBean outBean = new OutBean();
		ArrayList<Bean> ptReads = null;
		ArrayList<Bean> oaReads = null;
		ArrayList<Bean> reads = new ArrayList<Bean>();
		String userCode = Context.getUserBean().getCode();
		try {
			// newOaws = new ScNewTodoWebServ();
			ptReads = getPtReads(userCode);
			if (oaReads != null) {
				reads.addAll(oaReads);
			}
			/*ArrayList<Bean> oldOaReads = getOldOaTodos(userCode, "1");
			if (oldOaReads != null) {
				reads.addAll(oldOaReads);
			}*/
			reads.addAll(ptReads);
			Collections.sort(reads, comparator);
			outBean.setData(reads);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outBean;
	}

	/**
	 * 集成各系统的委托列表，OA委托
	 * 
	 * @return OutBean
	 */
	public OutBean getWtsOld() {
		newOaws = new ScNewTodoWebServ();
		OutBean outBean = new OutBean();
		ArrayList<Bean> wts = new ArrayList<Bean>();
		try {
			wts = (ArrayList<Bean>) newOaws.getNewOaWts();
			if (wts != null) {
				outBean.setData(wts);
			} else {
				outBean.setData(new ArrayList<Bean>());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outBean;
	}

	/**
	 * 获取OA委托信息
	 * 
	 * @return 数据Bean
	 */
	public OutBean getWts() {
		String serverAddre = Context.getSyConf("SYS_HOST_ADDR", ""); // 服务器地址
		OutBean outBean = new OutBean();
		String currentUser = Context.getUserBean().getId();
		// 得到委托人列表
		List<Bean> agtUserList = getAgtUser(currentUser);
		if (agtUserList.size() == 0) {
			// 无委托人则返回
			return outBean.setCount(0).setData(new ArrayList<Bean>());
		}
		// 构造每个委托人的委托待办数据
		List<Bean> agentList = new ArrayList<Bean>();
		ParamBean todoParam = new ParamBean();

		for (Bean agtUser : agtUserList) {
			String userCode = agtUser.getStr("aCode");
			ParamBean param = new ParamBean();
			param.setServId(SC_OA_USER_TYPE_AGENT_TO);
			param.setAct("finds");
			param.setWhere(" and FROM_USER_CODE='" + userCode + "' and TO_USER_CODE='" + currentUser
					+ "' and AGT_STATUS='1'");
			param.setSelect("AGT_TYPE_CODE");

			// 获取受托人的受托服务
			ArrayList<Bean> agtTypes = (ArrayList<Bean>) ServMgr.act(param).getDataList();

			String where = "";
			String todoWhere = "";
			for (Bean bean : agtTypes) {
				where = where + "'" + bean.getStr("AGT_TYPE_CODE") + "'" + ",";
			}
			if (where.length() > 0) {
				where = " and AGT_TYPE_CODE in(" + where.substring(0, where.length() - 1) + ")";
				ParamBean agtParam = new ParamBean();
				agtParam.setSelect("AGT_COND");
				agtParam.setWhere(where);
				
				agtParam.setServId(SC_OA_USER_AGT_TYPE);
				agtParam.setAct("finds");
				ArrayList<Bean> servs = (ArrayList<Bean>) ServMgr.act(agtParam).getDataList();

				for (Bean bean : servs) {
					String AGT_COND = bean.getStr("AGT_COND");
					if (!"_ALL_".equals(AGT_COND)) {
						todoWhere = todoWhere +AGT_COND + ",";
					}
				}
			}
			if (todoWhere.length() > 0) {
				todoWhere = "and TODO_BENCH_FLAG = 1 and TODO_CODE in (" + todoWhere.substring(0, todoWhere.length() - 1) + ")"
						+ " and OWNER_CODE = '" + userCode + "'";

				todoParam.setWhere(todoWhere);
			} else {
				
				todoWhere =  "and TODO_BENCH_FLAG = 1 and OWNER_CODE = '" + userCode + "'";

				todoParam.setWhere(todoWhere);
				
//				todoParam.setWhere(" and 1=2");
			}

//			todoParam.setServId("SC_OA_TODO"); //20160511 changyc
			todoParam.setServId("SY_COMM_TODO");
			todoParam.setAct("finds");
			
			// 查询委托人待办数据
			ArrayList<Bean> todoListtmp = (ArrayList<Bean>) ServMgr.act(todoParam).getDataList();

			// 处理每条记录的跳转url
			for (Bean bean : todoListtmp) {
				bean.set("_agtFLag", true); // 生成url时针对代他人办理情况设置标识
				bean.set("TODO_URL", TodoUtils.createBNTodoUrl(bean, serverAddre, false, true));
			}
			agentList.addAll(todoListtmp);
		}
		UserBean user = null;
		for (Bean bean : agentList) {
			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
			}
			bean.set("SHORT_TITLE", bean.get("TODO_TITLE", ""));
			bean.set("SYS_NAME", "OA委托");
			bean.set("SYS_CODE", "OAWT");
		}
		Collections.sort(agentList, comparator);
		return outBean.setCount(agentList.size()).setData(agentList);
	}

	/**
	 * 获取委托用户列表
	 * 
	 * @param mainUser
	 *            主用户
	 * @return 委托用户列表
	 */
	private List<Bean> getAgtUser(String mainUser) {
		Bean userState = getUserStateOrCreate(mainUser);
		List<Bean> subList = new ArrayList<Bean>();
		if (userState != null) {
			String[] subCodes = userState.getStr("SUB_CODES").split(Constant.SEPARATOR);
			if (userState.getStr("SUB_CODES").length() == 0) {
				return subList;
			}
			String today = DateUtils.getDate();
			for (String sub : subCodes) {
				String[] subArr = sub.split(SEPARATOR_CODE_AND_DATE);
				// 获取uCode
				String uCode = subArr[0];
				// 获取开始时间
				String sdt = subArr[1];
				// 获取结束时间
				String edt = subArr[2];
				if (DateUtils.isBefore(edt, today) || DateUtils.isBefore(today, sdt)) {
					continue;
				}
				Bean user = new Bean().set("aCode", uCode).set("aName", UserMgr.getUser(uCode).getName())
						.set("beginDate", sdt).set("endDate", edt);
				subList.add(user);
			}

		}
		return subList;
	}

	/**
	 * 获取用户状态，不存在创建或返回null
	 * 
	 * @param userCode
	 *            用户编码
	 * @return 最新的用户状态信息
	 */
	public static UserStateBean getUserStateOrCreate(String userCode) {
		ParamBean param = new ParamBean();
		param.setServId("SY_ORG_USER_STATE");
		param.setAct("finds");
		param.setId(userCode);
		Bean userState = null;
		List<Bean> userStates = ServMgr.act(param).getDataList();
		if (userStates != null && userStates.size() > 0) {
			userState = userStates.get(0);
		}
		if (userState != null) {
			return new UserStateBean(userState);
		} else {
			Bean userBean = ServDao.find(ServMgr.SY_ORG_USER_ALL, userCode);
			if (userBean != null) {
				ParamBean saveBean = new ParamBean();
				saveBean.setServId(SC_OA_USER_STATE);
				saveBean.set("USER_CODE", userCode);
				saveBean.setAct("save");
				return new UserStateBean(ServMgr.act(saveBean).getBean("_DATA_"));
			} else {
				return null;
			}
		}
	}
}
