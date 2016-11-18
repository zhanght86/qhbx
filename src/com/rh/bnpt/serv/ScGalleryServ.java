package com.rh.bnpt.serv;

import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.comm.news.serv.NewsServ;
import com.rh.core.serv.ParamBean;

/**
 * 添加权限过滤
 * @author jason
 *
 */
public class ScGalleryServ extends NewsServ {

	@Override
	protected void beforeQuery(ParamBean paramBean) {
		super.beforeQuery(paramBean);
		paramBean.setQueryExtWhere(paramBean.getQueryExtWhere() + NewsMgr.getAclSql());
	}
}
