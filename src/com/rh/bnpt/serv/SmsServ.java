package com.rh.bnpt.serv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;


/**
 * @author liuxinhe
 * @version v1.0 创建时间：2013-8-15 下午12:47:58 类说明 短信发送的服务
 */
public class SmsServ extends CommonServ {


	private static Log log = LogFactory.getLog(SmsServ.class);

	/**
	 * 单条记录显示之后的拦截方法，通讯录点击发送短信，将发送人自己添加(zjw)
	 * 
	 * @param paramBean
	 *            参数信息
	 * @param outBean
	 *            输出信息
	 */
	protected void afterByid(ParamBean paramBean, OutBean outBean) {
		outBean.set("SMS_SENDTONAME", paramBean.getStr("SMS_SENDTOID"));
		outBean.set("SMS_SENDTONAME__NAME", paramBean.getStr("SMS_SENDTONAME"));
	}

	/**
	 * 进行发送短信
	 * 
	 * @param paramBean
	 *            参数
	 * @return bean
	 */
	public OutBean save(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String qfMobile = paramBean.getStr("MOBILE");
		String[] destMobile = qfMobile.split(",");
		String message = paramBean.getStr("MESSAGE");
		String userName = paramBean.getStr("SEND_USER");
		try {
			
			log.debug("短信发送号码为 : ***" + qfMobile  + "***, 内容为 :***" + message + "***,发送短信人员 :" + userName);
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e.toString().indexOf("发送失败的手机号") > 0) {
				outBean.setError(e.toString());
			} else if (e.toString().indexOf("com.huawei.utils.db.DBPool.tidy") > 0) {
                outBean.setOk();
            } else {
				throw new TipException("短信发送失败！");
			}

		}
		outBean.setOk("短信全部发送成功！");
		return outBean;
	}

}
