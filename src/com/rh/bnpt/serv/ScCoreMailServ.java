package com.rh.bnpt.serv;

/*import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;*/

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.Lang;
import com.rh.core.comm.zhidao.ZhidaoServ;

/**
 * 首创CoreMail集成处理类
 * 
 * @author ZJW
 * 
 */
public class ScCoreMailServ {
	/**
	 * 获取当前用户邮件信息 目前包括（未读邮件数、用户Sid）
	 * 
	 * @throws Exception
	 *             异常
	 * @return 若返回null则出错
	 */
	/*public OutBean getMailInfos() throws Exception {

		String userEmail = "admin@capitalwater.cn";
		// 判断是否在测试阶段，如果在测试阶段则邮件只固定为admin测试用户的，否则取当前用户的邮件地址
		if (!Context.getSyConf("SC_ISTESTSYS_FLAG", true)) {
			// 当前用户
			UserBean usBean = Context.getUserBean();
			userEmail = usBean.getEmail();
		}
		APIContext ret;
		IClient cli = null;
		OutBean outBean = new OutBean();
		// coremail的服务IP
		String coreMailServIP = Context.getSyConf("SC_CORE_MAIL_SERVICE_IP", "");
		// coremail的服务端口
		int coreMailServPort = Context.getSyConf("SC_CORE_MAIL_SERVICE_PORT", 6195);
		Socket socket = new Socket(coreMailServIP, coreMailServPort);
		cli = APIContext.getClient(socket);
		try {
			ret = cli.userExist(userEmail);
			if (ret.getRetCode() == APIContext.RC_NORMAL) {
				outBean.set("userMailExist", "yes");
				ret = cli.userLoginEx(userEmail, "face=XJS");
				if (ret.getRetCode() == APIContext.RC_NORMAL) {
					String[] result = ret.getResult().split("&");
					String sid = "";
					for (String rst : result) {
						if (rst.contains("sid")) {
							sid = rst.split("=")[1];
						}
					}
					outBean.set("sessionId", sid);
				}
				ret = cli.getAttrsEx(userEmail, "mbox_newmsgcnt");
				if (ret.getRetCode() == APIContext.RC_NORMAL) {
					outBean.set("noReadCount", Integer.parseInt((ret.getResult().split("=")[1])));
				}
			} else {
				outBean.set("userMailExist", "no");
			}
			outBean.set("msg", "OK");
		} catch (IOException e) { // 当发生异常时，将未读邮件置为0
			outBean.set("msg", "erro");
			outBean.set("noReadCount", "0");
		} finally {
			cli.close();
		}
		return outBean;
	}
*/
	/**
	 * 替换HTML标签
	 * 
	 * @author chujie
	 * 
	 * @param paramBean
	 *            传入的参数ParamBean
	 * @return outBean
	 */
	public OutBean replaceHtmlTag(ParamBean paramBean) {
		OutBean bean = new OutBean();
		int size = paramBean.getInt("size");
		if (size == 0) {
			size = 80;
		}
		String replacedStr = Lang.getSummary(paramBean.getStr("newsBody"), size);
		return bean.set("newsBody", replacedStr);
	}
	/**
	 * 获取用户 提问 回答 关注 数量
	 * @return 返回值
	 */
	public OutBean getUserActivity() {
		// 当前用户
		UserBean userBean = Context.getUserBean();
		ParamBean param = new ParamBean();
		param.set("userId", userBean.getId());
		//提问数量
		int questionCount = new ZhidaoServ().getAskCount(param).getCount();
		//回答数量
	    int answerCount = new ZhidaoServ().getAnswerCount(param).getCount();
	    //关注数量
	    int followCount = new ZhidaoServ().getFollowList(param).getCount();

	    OutBean outBean = new OutBean();
		outBean.put("questionCount", questionCount);
		outBean.put("answerCount", answerCount);
		outBean.put("followCount", followCount);
		return outBean;
	}
}
