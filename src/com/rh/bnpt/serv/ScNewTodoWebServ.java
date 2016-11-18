package com.rh.bnpt.serv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rh.client.RHClient;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.util.XmlUtils;

/**
 * OA系统webservice接口类
 * 
 * @author ZJW
 */
public class ScNewTodoWebServ {
	private static final String OA_SY_COMM_TODO = "OA_TODO_PT_SERV";
	private static final String OA_TODO_GET_USER = Context.getSyConf("OA_TODO_GET_USER", "admin");
	private static final String OA_TODO_GET_USPASS = Context.getSyConf("OA_TODO_GET_USPASS", "123456");
	private String serverURL = "";
	private RHClient wsClient = null;

	/**
	 * 构造函数
	 */
	public ScNewTodoWebServ() {
		serverURL = Context.getSyConf("OA_HOST_ADDR", "").trim();
		wsClient = new RHClient(serverURL);
	}

	// 当前用户

	/**
	 * 获取新OA系统待办
	 * 
	 * @return 待办
	 * @throws Exception
	 *             异常
	 */
	public List<Bean> getNewOaTodos() throws Exception {
		if ("".equals(serverURL)) {
			return null;
		}
		wsClient.login("2", OA_TODO_GET_USER, OA_TODO_GET_USPASS);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", Context.getUserBean().getId());
		map = this.invokeWith(OA_SY_COMM_TODO, "getTodos", map);
		Bean outBean = XmlUtils.toBean(map.get("return").toString());
		String data = outBean.getStr("_DATA_");
		List<Bean> todos = null;
		if (!"".equals(data)) {
			todos = outBean.getList("_DATA_");
		}
		return todos;
	}

	/**
	 * 获取新OA系统待阅
	 * 
	 * @return 待办
	 * @throws Exception
	 *             异常
	 */
	public List<Bean> getNewOaReads() throws Exception {
		if ("".equals(serverURL)) {
			return null;
		}
		wsClient.login("2", OA_TODO_GET_USER, OA_TODO_GET_USPASS);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", Context.getUserBean().getId());
		map = this.invokeWith(OA_SY_COMM_TODO, "getReads", map);
		Bean outBean = XmlUtils.toBean(map.get("return").toString());
		String data = outBean.getStr("_DATA_");
		List<Bean> reads = null;
		if (!"".equals(data)) {
			reads = outBean.getList("_DATA_");
		}
		return reads;
	}

	/**
	 * 获取新OA系统委托
	 * 
	 * @return 委托
	 * @throws Exception
	 *             异常
	 */
	public List<Bean> getNewOaWts() throws Exception {
		if ("".equals(serverURL)) {
			return null;
		}
		wsClient.login("2", OA_TODO_GET_USER, OA_TODO_GET_USPASS);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", Context.getUserBean().getId());
		Bean outBean = XmlUtils.toBean(this.invokeWith(OA_SY_COMM_TODO, "getWts", map).get("return").toString());
		List<Bean> wts = null;
		String data = outBean.getStr("_DATA_");
		if (!"".equals(data)) {
			wts = outBean.getList("_DATA_");
		}
		return wts;
	}

	/**
	 * 调用方法
	 * 
	 * @param service
	 *            服务
	 * @param method
	 *            方法
	 * @param param
	 *            参数
	 * @throws Exception
	 *             例外
	 */
	public void invoke(String service, String method, Map<String, Object> param) throws Exception {
		this.wsClient.invoke(this.serverURL + "/" + service + ".ws", method, param);
	}

	/**
	 * 调用方法
	 * 
	 * @param service
	 *            服务
	 * @param method
	 *            方法
	 * @param param
	 *            参数
	 * @return 结果
	 * @throws Exception
	 *             例外
	 */
	public Map<String, Object> invokeWith(String service, String method, Map<String, Object> param) throws Exception {
		return this.wsClient.invoke(this.serverURL + "/" + service + ".ws", method, param);
	}
}
