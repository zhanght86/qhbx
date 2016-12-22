package com.rh.core.comm.loginbkg;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;

public class LoginBkgServ extends CommonServ {

	/**
	 * 保存之前的拦截方法，由子类重载
	 * 
	 * @param paramBean
	 *            参数信息 可以通过paramBean获取数据库中的原始数据信息： Bean oldBean = getParamOldBean(paramBean);
	 *            可以通过方法paramBean.getFullData()获取数据库原始数据加上修改数据的完整的数据信息： Bean fullBean = paramBean.getFullData();
	 *            可以通过paramBean.getBoolean(Constant.PARAM_ADD_FLAG)是否为true判断是否为添加模式
	 */
	protected void beforeSave(ParamBean paramBean) {
		String id = paramBean.getId();
		String flag = paramBean.getStr("LOGIN_BKG_FLAG");
		if ("1".equals(flag)) {
			Bean setBean = new Bean();
			setBean.set("LOGIN_BKG_FLAG", "2");
			Bean whereBean = new Bean();
			whereBean.set(Constant.PARAM_WHERE, "and LOGIN_BKG_ID <> '" + id + "'");
			ServDao.updates("SY_LOGIN_BKG_MGR", setBean, whereBean);
		}
	}

	/**
	 * 获取登录背景图片地址
	 * 
	 * @return path
	 */
	public Bean getLoginBkgPic() {
		// String sqlWhere = " and LOGIN_BKG_FLAG='1'";
		String sqlWhere = " and (sysdate between to_date(LOGIN_USE_BTIME||'00:00:00','yyyy-MM-dd HH24:mi:ss') and to_date(LOGIN_USE_ETIME||'23:59:59','yyyy-MM-dd HH24:mi:ss'))"
				+ " and LOGIN_USE_BTIME is not null and LOGIN_USE_ETIME is not null";
		List<Bean> bkgPics = ServDao.finds("SY_LOGIN_BKG_MGR", sqlWhere);
		Bean bkgBean = new Bean();
		if (bkgPics.size() > 0) {
			bkgBean = bkgPics.get(0);
			// String pic = bkgBean.getStr("LOGIN_BKG_PATH");
			// return pic.split(",")[0];
			return bkgBean;
		} else {
			sqlWhere = " and LOGIN_USE_BTIME is null and LOGIN_USE_ETIME is null";
			bkgPics = ServDao.finds("SY_LOGIN_BKG_MGR", sqlWhere);
			if (bkgPics.size() > 0) {
				bkgBean = bkgPics.get(0);
				return bkgBean;
			} else {
				return null;
			}
		}
	}
}
