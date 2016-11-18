/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;

import com.rh.core.base.Bean;
import com.rh.core.base.BeanUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;

/**
 * 继承自CommonServ服务，作为搜索部分的通用父服务，额外提供搜索定义服务需要的访问原始页面的功能。
 * 
 * @author Jerry Li
 */
public class CommonSearchServ extends CommonServ {
    
    /**
     * 提供基于主键的原始页面查询服务
     * @param paramBean    参数Bean
     * @return 查询结果
     */
    public OutBean srcId(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        String servId = paramBean.getServId();
        Bean servDef = ServUtils.getServDef(servId);
        if (servDef.getInt("SERV_SEARCH_FLAG") == Constant.YES_INT) { // 设置启用了全文搜索
            Bean searchDef = (Bean) servDef.get("SY_SERV_SEARCH");
            String url = searchDef.getStr("SEARCH_SRC_URL");
            if (url.length() > 0) {
                Bean data = ServDao.find(servId, paramBean.getId());
                if (data != null) {
                    url = searchDef.getStr("SEARCH_DOMAIN") + BeanUtils.replaceValues(url, data);
                    outBean.setToRedirect(url);
                }
            }
        }
        return outBean;
    }

}
