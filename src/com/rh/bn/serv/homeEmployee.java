package com.rh.bn.serv;


import org.apache.pdfbox.util.operator.SetCharSpacing;

import com.rh.core.comm.news.serv.InfosServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

public class homeEmployee extends InfosServ{
    public OutBean byid(ParamBean paramBean) {
        String[] params = {};
        if(paramBean.getId().length()!=0){
            params = paramBean.getId().split(",");
        }
        super.byid(paramBean);
        ParamBean param = new ParamBean();
        
        OutBean outBean = new OutBean();
        for (int i = 0; i < params.length; i++) {
            param.copyFrom(paramBean);
            param.setId(params[i]);
            param.setShowNum(6);
            outBean.set("_DATA_"+i,super.getTopInfos(param).getData());
            
        }
        return outBean;
    }
    
}
