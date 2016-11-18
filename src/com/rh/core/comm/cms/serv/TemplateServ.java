package com.rh.core.comm.cms.serv;

import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.comm.cms.mgr.TemplateMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 模板Service，用于处理模板代码
 * @author chensheng
 */
public class TemplateServ extends CacheableServ {

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        if (null == paramBean.getId() || 0 == paramBean.getId().length()) {
            return;
        }
        @SuppressWarnings("static-access")
        String str = TemplateMgr.getInstance().getTmplString(outBean);
        outBean.set("TMPL_SOURCE", str);
    }

    @SuppressWarnings("static-access")
    @Override
    protected void afterSave(ParamBean paramBean, OutBean dataBean) {

        TemplateMgr.getInstance().createTmpl(paramBean, dataBean);
    }

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_CMS_TMPL;
    }
    
}
