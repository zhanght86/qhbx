package com.rh.core.comm.cms.serv;

import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.serv.ParamBean;

public class DirectSubMenuServ extends InfosChannelServ {
	
	protected void beforeQuery (ParamBean paramBean) {
		String chnlwhere = paramBean.getStr("_treeWhere");
		// 进来直接显示 关于百年 等3个总栏目
		if( chnlwhere.length() == 2 ){
			String where = " and CHNL_NAME in( '关于百年' , '总公司门户栏目' , '分公司门户栏目')";
			paramBean.setWhere(where);
		}else if(chnlwhere.length() >2){
			int  start = chnlwhere.indexOf("DICT_VALUE");
			int  end = chnlwhere.indexOf("}", start);
			String chnlId = chnlwhere.substring(start+11, end);
		    paramBean.setWhere(" and CHNL_PID = '"+chnlId+"'");
		}
		
	}
	
	
}
