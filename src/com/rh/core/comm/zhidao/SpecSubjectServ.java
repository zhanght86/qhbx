package com.rh.core.comm.zhidao;

import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 专家领域
 * 
 */
public class SpecSubjectServ extends CacheableServ {
    
    /**
     *
     * TODO：
     * 保存结果后，同步专家领域字段
     * 
     * @param paramBean - 参数bean
     * @param outBean - 结果bean
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        
    }

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_ZHIDAO_SPEC_SUBJECT;
    }
  
}
