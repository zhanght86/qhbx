package com.rh.oa.zh.seal;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 
 * @author yangjy
 * 
 */
public class SealServ extends CommonServ {

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        // 部门编号
        List<Bean> list = SealMgr.findSealList(SealMgr.getODeptNum(paramBean));
        outBean.setData(list);
    }

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        String extWhere = paramBean.getStr("_extWhere");
        paramBean.set("_extWhere", " and 1=0");

        paramBean.set("S_ODEPT", extWhere);
        super.beforeQuery(paramBean);
    }

}
