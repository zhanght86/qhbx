package com.rh.bn.serv;

import java.util.List;

import com.rh.bnpt.util.BnPtUtils;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
/**
 * 留言板服务类
 * @author Lidongdong
 *
 */
public class BoardServ extends CommonServ{
    /**
     * 发布
     * @param paramBean
     * @return
     */
    public OutBean approval(ParamBean paramBean){
        //留言状态  1:未审核;2:审核未通过;3:审核通过
        paramBean.set("BN_LY_FLAG", "3");
        paramBean.setAct("save");
        paramBean.set("AUDIT_USER", Context.getUserBean().getUser());
        ServMgr.act(paramBean);
        return new OutBean().setOk("审核通过");
    }
    public OutBean updatePYValue(ParamBean paramBean){
        List<Bean> userBeans = ServDao.finds("SY_ORG_USER", "");
        for (Bean userBean : userBeans) {
            String PYValue = BnPtUtils.trans2PinYin(userBean.getStr("USER_NAME"));
            ServDao.update("SY_ORG_USER", userBean.set("USER_SPELLING", PYValue));
        }
        return new OutBean();
    }
}
