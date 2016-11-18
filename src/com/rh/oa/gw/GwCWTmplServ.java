package com.rh.oa.gw;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 成文模板服务类
 *
 */
public class GwCWTmplServ extends CommonServ {
    
	/**
	 * 服务名称
	 */
	public static final String OA_GW_CODE_CW_TMPL_SERV = "OA_GW_CODE_CW_TMPL";
	
	/**
	 * 根据机关代字，查询成文模板的列表
	 * @param paramBean 参数Bean
	 * @return 成文模板列表
	 */
	public Bean getCwTmplListByCode(Bean paramBean) {
    	String gwYearCode = paramBean.getStr("GW_YEAR_CODE");
		
    	SqlBean queryBean = new SqlBean();
    	queryBean.and("CODE_NAME", gwYearCode);
    	queryBean.and("S_ODEPT", Context.getUserBean().getODeptCode());
    	
    	
    	List<Bean> cwList = ServDao.finds(OA_GW_CODE_CW_TMPL_SERV, queryBean);
		
    	
    	Bean rtnBean = new Bean();
    	rtnBean.set("cwList", cwList);
    	
    	return rtnBean;
    }
}
