package com.rh.yz.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

public class MainServ extends CommonServ{
    public void beforeSave(ParamBean paramBean){
        System.out.println(paramBean);
        
        
    }
}
