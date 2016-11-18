package com.rh.bn.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

public class TypeServ extends CommonServ{
    public OutBean queryType(ParamBean paramBean){
        String strWhere = "";
        if(paramBean.getStr("SEAL_TYPE1").length()!=0){
            strWhere += " and SEAL_TYPE1 = '" +paramBean.getStr("SEAL_TYPE1")+ "'";
        }
        List<Bean> typeBeans = ServDao.finds("", strWhere);
        return new OutBean().setOk();
    }
}
