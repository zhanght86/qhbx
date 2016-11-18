package com.rh.bn.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

public class UsageInfoServ extends CommonServ{
    public void beforeSave(ParamBean paramBean){
        
    }
    public OutBean saveSeal(ParamBean paramBean){
        
        ParamBean sealInfoBean = new ParamBean();
        sealInfoBean.setId(paramBean.getId());
        sealInfoBean.set("SEAL_STATE", "2");
        ServDao.update(paramBean.getServId(),sealInfoBean);
       /* paramBean.set("TYPE", "2");
        paramBean.setServId(paramBean.getStr("SERV_ID"));
        paramBean.setId(paramBean.getStr("DATA_ID"));
        new BnUtils().inSealCircle(paramBean);*/
        return new OutBean().setOk("盖章成功");
    }
}
