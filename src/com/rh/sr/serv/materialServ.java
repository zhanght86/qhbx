package com.rh.sr.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

public class materialServ extends CommonServ{
    public void after(ParamBean paramBean) {
        // TODO Auto-generated method stub
        
    }
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        System.out.println(paramBean.getId());
    }
}
