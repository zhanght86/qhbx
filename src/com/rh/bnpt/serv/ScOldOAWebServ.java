package com.rh.bnpt.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.XmlUtils;

/**
 * 老OA系统webservice接口类
 * 
 * @author ZJW
 * 
 */
public class ScOldOAWebServ {

	private String soapaction = Context.getSyConf("SC_OA_SERVICE_URL", "");
	private String ns = "http://wsservice.zotn.com";
	private String allurl = soapaction + "/wsservice/PtService?wsdl";

	private Comparator<Bean> comparator = new Comparator<Bean>() {
		public int compare(Bean b1, Bean b2) {
			return b2.get("TODO_SEND_TIME", "").compareTo(b1.get("TODO_SEND_TIME", ""));
		}
	};

	/**
	 * 通过webservice取老OA系统待处理事项
	 * 
	 * @param param
	 *            获取老OA系统待办参数
	 * @return 返回ArrayList 待办list
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("finally")
	public ArrayList<Bean> getTodoDataList(ParamBean param) throws Exception {
		Service service = new Service();
		Call call = null;
		ArrayList<Bean> todos = new ArrayList<Bean>();
		if ("".equals(soapaction)) {
			return todos;
		}
		// 当前用户
//		UserBean usBean = Context.getUserBean();
		
		//如果参数中没有userBean，就使用当前用户的userBean，切换兼岗是的待办条数统计不准问题
		UserBean usBean = null;
		String jianGangUserId = param.getStr("JIANGANG_USER_ID");
		if (jianGangUserId != null && (!"".equals(jianGangUserId))) {
			usBean = UserMgr.getUser(jianGangUserId);
		} else {
			usBean = Context.getUserBean();
		}
		
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(allurl));
			call.setOperationName(new QName(soapaction, "getTodos"));
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(ns + "/getTodos");
			// 用户ID
			call.addParameter(new QName(ns, "userID"), XMLType.XSD_LONG, ParameterMode.IN);
			// 待处理类型
			call.addParameter(new QName(ns, "catalog"), XMLType.XSD_INT, ParameterMode.IN);
			call.addParameter(new QName(ns, "flag"), XMLType.XSD_BOOLEAN, ParameterMode.IN);
			call.setReturnType(new QName(ns, "getTodos"), String.class);
			String hrtemp = (String) call.invoke(new Object[] { new Long(usBean.getId()), param.getInt("catalog"),
					param.getBoolean("flag") });
			Bean bean = XmlUtils.toBean(hrtemp);
			Bean beanTemp;
			for (Object key : bean.keySet()) {
				beanTemp = (Bean) bean.get(key);
				String shorTitle = beanTemp.get("TODO_TITLE", "");
				int fixLen = Context.getSyConf("SC_TODO_FIX_LEN", 5);
				if (shorTitle.length() > fixLen) {
					shorTitle = shorTitle.substring(0, fixLen) + "...";
				}
				beanTemp.set("SHORT_TITLE", shorTitle);
				todos.add(beanTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return todos;
		}

	}

	/**
	 * 通过webservice取老OA系统委托事项
	 * 
	 * @param param
	 *            获取老OA系统待办参数
	 * @return 返回OutBean
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("finally")
	public OutBean getAgencyDatas(ParamBean param) throws Exception {
		Service service = new Service();
		Call call = null;
		ArrayList<Bean> todos = new ArrayList<Bean>();
		OutBean outBean = new OutBean();
		if ("".equals(soapaction)) {
			return outBean.setData(todos);
		}
		// 当前用户
		UserBean usBean = Context.getUserBean();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(allurl));
			call.setOperationName(new QName(soapaction, "getAgencyTodos"));
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(ns + "/getAgencyTodos");
			// 用户ID
			call.addParameter(new QName(ns, "userID"), XMLType.XSD_LONG, ParameterMode.IN);

			call.setReturnType(new QName(ns, "getAgencyTodos"), String.class);
			String hrtemp = (String) call.invoke(new Object[] { new Long(usBean.getId()) });
			Bean bean = XmlUtils.toBean(hrtemp);
			Bean beanTemp;
			for (Object key : bean.keySet()) {
				beanTemp = (Bean) bean.get(key);
				String shorTitle = beanTemp.get("TODO_TITLE", "");
				int fixLen = Context.getSyConf("SC_TODO_FIX_LEN", 5);
				if (shorTitle.length() > fixLen) {
					shorTitle = shorTitle.substring(0, fixLen) + "...";
				}
				beanTemp.set("SHORT_TITLE", shorTitle);
				todos.add(beanTemp);
			}
			Collections.sort(todos, comparator);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return outBean.setData(todos);
		}
	}
}
