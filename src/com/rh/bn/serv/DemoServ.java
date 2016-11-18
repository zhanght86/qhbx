package com.rh.bn.serv;

import java.util.List;

import com.rh.bnpt.util.BnPtUtils;
import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

public class DemoServ extends CommonServ{
    public void updatePYValue(ParamBean paramBean){
        List<Bean> userBeans = ServDao.finds("SY_ORG_USER", "");
        for (Bean userBean : userBeans) {
            String PYValue = BnPtUtils.trans2PinYin(userBean.getStr("USER_NAME"));
            ServDao.update("SY_ORG_USER", userBean.set("USER_SPELLING", PYValue));
        }
    }
}
