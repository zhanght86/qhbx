package com.rh.bn.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;

public class BnFileViewServ extends CommonServ{
	protected void beforeQuery(ParamBean paramBean) {
    	List<Bean> treeWhereBeans = paramBean.getList("_treeWhere");
    	for(Bean treeWhereBean : treeWhereBeans){
    		//导航树如果选择的是总公司，只显示总公司的印章，不显示总公司及以下
    		if("CHNL_ID".equals(treeWhereBean.getStr("DICT_ITEM"))){
    			String servId = paramBean.getServId();
    			ServDefBean serv = ServUtils.getServDef(servId);
    			StringBuilder where0 = new StringBuilder();
    			where0.append(ServUtils.getTreeWhere(serv, (List<Bean>) paramBean.get("_treeWhere")));
    			where0 = where0.replace(where0.indexOf(" like "), where0.indexOf(" like ")+6, " = ");
    			where0 = where0.replace(where0.indexOf("^%"), where0.indexOf("^%")+2, "^");
    			paramBean.set("_treeWhere", "");
    			paramBean.set("_WHERE_",where0);
    		}
    	}
    	super.beforeQuery(paramBean);
    }
}
