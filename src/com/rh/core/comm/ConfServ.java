/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.comm;

import com.rh.core.base.db.Transaction;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;

/**
 * 系统配置扩展类。用于保存完成后及时提交事务，确保字典能够取到最新的数据。
 * 
 * @author jerry li
 */
public class ConfServ extends CommonServ {
    
    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        if (outBean.isOkOrWarn()) {
            Transaction.commit();
            ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
            servDef.clearDictCache(outBean.getStr("S_CMPY")); 
        }
    }
    @Override
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        if (outBean.getCount() > 0) { //删除成功的数量大于0
            Transaction.commit();
        }
    }
}
