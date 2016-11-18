package com.rh.bnpt.serv;

import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;

/**
 * 门户样式 服务类
 * @author jason
 *
 */
public class ScPortalStyleServ extends CommonServ {

	/**
	 * 管理员更改样式风格时，更新所有人的风格
	 * @param param 
	 */
	public void update(ParamBean param) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("update SY_ORG_USER_STYLE set SS_STYLE_PORTAL ='"); 
		strSql.append(param.getStr("SS_STYLE_PORTAL"));
		strSql.append("'");
		Context.getExecutor().execute(strSql.toString());
		
//		String style = param.getStr("SS_STYLE_PORTAL");
//		String servId =  param.getServId();
//		
//		ParamBean queryBean = new ParamBean();
//		queryBean.setSelect("SS_ID");
//		List<Bean> list = ServDao.finds(servId, queryBean);
//		if (!list.isEmpty()) {
//			ParamBean tempBean = new ParamBean(servId, ServMgr.ACT_SAVE);
//			for (Bean bean : list) {
//				tempBean.setId(bean.getId());
//				tempBean.set("SS_STYLE_PORTAL", style);
//				ServMgr.act(tempBean);
//			}
//		}
	}
}
