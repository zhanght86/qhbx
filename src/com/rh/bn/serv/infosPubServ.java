package com.rh.bn.serv;

import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.comm.news.serv.InfosServ;
import com.rh.core.serv.ParamBean;

public class infosPubServ extends InfosServ{

	
	protected void beforeQuery(ParamBean paramBean) {
		String chnlwhere = paramBean.getStr("_treeWhere");
		if(chnlwhere.length() >2){
			int  start = chnlwhere.indexOf("DICT_VALUE");
			int  end = chnlwhere.indexOf("}", start);
			String chnlId = chnlwhere.substring(start+11, end);
		    String where33 = NewsMgr.getAclSql();
		    paramBean.setWhere(" and CHNL_ID = '"+chnlId+"'"+where33);
		}
		
	}
	
	
}
