package com.rh.oa.comm;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;

/**
 * 流程管理功能查询流程实例
 * @author yangjy
 *
 */
public class EntityMultQuery extends CommonServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        ServDefBean def = ServUtils.getServDef(paramBean.getServId());
        
        String selectItem = getListSelectFields(def);
        
        paramBean.setSelect(selectItem);
        paramBean.setGroupBy("DATA_ID");
        
        String search = paramBean.getQuerySearchWhere();
        if (search.indexOf("S_ODEPT") > 0) { 
            //使用Query_ODEPT字段代替S_ODEPT查询，提高速度。
            search = search.replaceAll("S_ODEPT", "QUERY_ODEPT");
            paramBean.setQuerySearchWhere(search);
        }
    }
    
    /**
     * @param servDefBean 服务定义
     * @return 从服务定义中取得列表查询Select字段 
     */
    private String getListSelectFields(ServDefBean servDefBean) {
        StringBuilder select = new StringBuilder();
        List<Bean> tableFields = servDefBean.getViewItems();
        int fieldCount = tableFields.size();
        for (int i = 0; i < fieldCount; i++) { // 获取全部字段数据
            Bean itemBean = tableFields.get(i);
            if (itemBean.getInt("ITEM_LIST_FLAG") != Constant.NO_INT) { // 除不显示的字段之外都要放到本地
                select.append("max(").append(itemBean.get("ITEM_CODE")).append(")");
                select.append(itemBean.get("ITEM_CODE")).append(",");
            }
        }

        if (select.length() > 0) {
            select.setLength(select.length() - 1);
            return select.toString();
        }

        return "";
    }

}
