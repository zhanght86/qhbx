package com.rh.oa.aud;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * 稽核审计服务类
 * 
 * @author wangchen
 */
public class AuditServ extends CommonServ {

    @Override
    public OutBean delete(ParamBean paramBean) {
        String audIds = paramBean.getId();
        String[] audIdArr = audIds.split(Constant.SEPARATOR);
        ParamBean delBean = new ParamBean();
        delBean.set("S_FLAG", Constant.NO_INT);
        delBean.setAddFlag(false);
        delBean.setAct(ServMgr.ACT_SAVE);
        delBean.setServId(paramBean.getServId());
        for (String id : audIdArr) {
            delBean.setId(id);
            ServMgr.act(delBean);
        }
        return new OutBean().setOk("删除成功");
    }
    
}