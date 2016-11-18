package com.rh.bnpt.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ServDao;

public class ScNoOaConfigServ extends CommonServ {

	/**
	 * 根据用户所属机构配合"有OA机构配置(SC_COMM_SEARCH_CONFIG)"添加用户是否有ＯＡ的属性
	 * @return hasOaFlag属性结果
	 */
	public OutBean getHasOaFlag() {
		OutBean outBean = new OutBean();
		UserBean user = Context.getUserBean();
		String hasOaFlag = user.getStr("hasOaFlag");
		if ("".equals(hasOaFlag)) {
			String odeptCode = user.getODeptCode();
			List<Bean> ls = ServDao.finds("SC_COMM_SEARCH_CONFIG", " and CONF_HASOA_ODEPT ='" + odeptCode + "'");
			if(ls.size()>0){
				user.set("hasOaFlag", "1");
			}else{
				user.set("hasOaFlag", "2");
			}
			outBean.set("hasOaFlag", user.getStr("hasOaFlag"));
		} else {
			outBean.set("hasOaFlag", hasOaFlag);
		}
		return outBean;
	}
}
