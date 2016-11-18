package com.rh.bnpt.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 与Hr系统对接 服务接口
 * @author jason
 *
 */
public class ScHrWebServ extends CommonServ {
	private String soapaction = "http://10.10.11.126:8081";
	private String allurl = soapaction + "/services/dataExInterFace?wsdl";
	/**
	 * 从HR系统获取user的年假记录
	 * @param param 入参
	 * @return null
	 */
	public OutBean getAnnualRecords(ParamBean param) {
//		UserBean user = Context.getUserBean();
//		String userId = user.getCode();
		// TODO  调用hr接口
		String content = "本年度 0";
		
		OutBean out = new OutBean();
				out.set("APPLY_RECORDS", content);
		return out;
	}
}
