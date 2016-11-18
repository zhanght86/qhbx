package com.rh.core.comm.zhidao;

import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 获取专家分页信息
 * 
 */
public class SpecialistServ extends CacheableServ {

    /**
     * @param paramBean - 参数bean
     * @return outBean - 输出bean
     */
    public OutBean getSpecialist(ParamBean paramBean) {
        ZhidaoServ zhidaoServ = new ZhidaoServ();
        OutBean outBean = zhidaoServ.getSpecialistDetail(paramBean);
        return outBean;
    }

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_ZHIDAO_SPECIALIST;
    }
}
