package com.rh.bnpt.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.serv.AddressServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;

/**
 * 用户通讯录服务类
 * 
 * @author Jerry Li
 * 
 */
public class ScAddressServ extends AddressServ {


	/**
	 * 查询之前的拦截方法，由子类重载
	 * 
	 * @param paramBean
	 *            参数信息
	 */
	@SuppressWarnings("unchecked")
	protected void beforeQuery(ParamBean paramBean) {
		String odeptCode = Context.getUserBean().getODeptCode();
		if (paramBean.isEmpty("_treeWhere") || "[]".equals(paramBean.getStr("_treeWhere"))) {
			if(!"24".equals(odeptCode)){
				paramBean.setQueryExtWhere(paramBean.getQueryExtWhere()+" and ODEPT_CODE='"+odeptCode+"'");
			}
		}else{
			ServDefBean serv = ServUtils.getServDef(paramBean.getServId());
			Bean dyBean = new Bean();
			dyBean.set("@com.rh.core.comm.addressbook.AddrDeptDict", "SY_ORG_DEPT_SUB");
			String treeWhere = getTreeWhere(serv,(List<Bean>) paramBean.get("_treeWhere"),dyBean);
			String extWhere = "";
			if("".equals(treeWhere)){
				extWhere = " and DEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT where 1=1  and (CMPY_CODE='2' )  and CODE_PATH like '24^%' and ODEPT_CODE = '24')";
			}else{
				extWhere = treeWhere;
			}
			paramBean.remove("_treeWhere");
			paramBean.setQueryExtWhere(paramBean.getQueryExtWhere()+extWhere);
		}
	}
	
	/**
     * 得到前端提交的查询过滤条件，支持字典名称和编码的自动转换
     * @param servDef 服务定义
     * @param dictList 导航选中信息
     * @return 过滤条件
     */
    public String getTreeWhere(ServDefBean servDef, List<Bean> dictList,Bean dyDictBean) {
        StringBuilder sb = new StringBuilder();
        for (Bean item : dictList) {
            String itemCode = item.getStr("DICT_ITEM");
            String value = item.getStr("DICT_VALUE");
            boolean withSubs = item.get("DICT_SUBS", true);
            Bean itemDef = servDef.getItem(itemCode);
            
            if (itemDef != null && !itemDef.isEmpty("DICT_ID")) {
            	Bean dict;
            	if(itemDef.getStr("DICT_ID").startsWith("@")){
            		dict = DictMgr.getDict(dyDictBean.getStr(itemDef.getStr("DICT_ID")));
            	}else{
            		dict = DictMgr.getDict(itemDef.getStr("DICT_ID"));
            	}
                if (withSubs && DictMgr.isSingleTree(dict) && dict.isNotEmpty("DICT_F_PATH")) { // 查询所有子
                    String where = DictMgr.getDictSubSql(dict, value);
                    if (where.length() > 0) {
                        sb.append(" and ").append(itemCode).append(" in (").append(where).append(")");
                    }
                } else {
                    sb.append(" and ").append(itemCode).append("='").append(value).append("'");
                }
            } else {
                sb.append(" and ").append(itemCode).append("='").append(value).append("'");
            }
        }
        return sb.toString();
    }
}
