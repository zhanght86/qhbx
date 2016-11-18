package com.rh.bnpt.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

/**
 * 获取门户及集成系统待办的集合
 * 
 * @author ZJW
 * 
 */
public class ScTodoCollectionBAK {

	private ScOldOAWebServ oldOaws = new ScOldOAWebServ();

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

		String where = "and owner_code = '" + userCode + "' and TODO_CATALOG=1 ";
		ParamBean paramBean = new ParamBean();
		paramBean.setServId("SY_COMM_TODO");
		paramBean.setWhere(where);
		ArrayList<Bean> todoListtmp = (ArrayList<Bean>) ServDao.finds("SY_COMM_TODO", paramBean);
		UserBean user = null;
		ArrayList<Bean> todoList = new ArrayList<Bean>();
		for (Bean bean : todoListtmp) {
			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
			String shortTitle = bean.get("TODO_TITLE", "");
			int fixLen = Context.getSyConf("SC_TODO_FIX_LEN", 5);
			if (shortTitle.length() > fixLen) {
				shortTitle = shortTitle.substring(0, fixLen) + "...";
			}
			bean.set("SHORT_TITLE", shortTitle);
			bean.set("SYS_NAME", "门户待办");
			bean.set("SYS_CODE", "PT");
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
			user = UserMgr.getUser(bean.get("SEND_USER_CODE", ""));
			if (user != null) {
				bean.set("SEND_USER_NAME", user.getName());
				todoList.add(bean);
			}
			String shortTitle = bean.get("TODO_TITLE", "");
			int fixLen = Context.getSyConf("SC_TODO_FIX_LEN", 5);
			if (shortTitle.length() > fixLen) {
				shortTitle = shortTitle.substring(0, fixLen) + "...";
			}
			bean.set("SHORT_TITLE", shortTitle);
			bean.set("SYS_NAME", "门户待阅");
			bean.set("SYS_CODE", "PT");
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
		ParamBean param = new ParamBean();
		param.set("catalog", 0);
		// true时取兼职待办
		param.set("flag", false);
		ArrayList<Bean> ptTodos = null;
		ArrayList<Bean> oaTodos = null;
		ArrayList<Bean> todos = new ArrayList<Bean>();
		try {
			oaTodos = oldOaws.getTodoDataList(param);
			ptTodos = getPtTodos(Context.getUserBean().getCode());
			todos.addAll(oaTodos);
			todos.addAll(ptTodos);
			Collections.sort(todos, comparator);
			outBean.setData(todos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outBean;
	}

	/**
	 * 集成各系统的待阅列表，包括oa兼职待阅及门户待阅
	 * 
	 * @return OutBean
	 */
	public OutBean getReads() {
		OutBean outBean = new OutBean();
		ParamBean param = new ParamBean();
		param.set("catalog", 1);
		param.set("flag", true);
		// ArrayList<Bean> ptTodos = null;
		ArrayList<Bean> oaTodos = null;
		ArrayList<Bean> todos = new ArrayList<Bean>();
		try {
			oaTodos = oldOaws.getTodoDataList(param);
			// ptTodos = getPtReads(Context.getUserBean().getCode());
			// todos.addAll(oaTodos);
			// todos.addAll(ptTodos);
			Collections.sort(oaTodos, comparator);
			outBean.setData(todos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outBean;
	}

	/**
	 * 取旧OA系统待办
	 * 
	 * @return outBean
	 * @throws Exception
	 *             异常
	 */
	public OutBean getWts() throws Exception {
		return oldOaws.getAgencyDatas(new ParamBean());
	}
}
