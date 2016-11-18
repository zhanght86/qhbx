package com.rh.bn.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
/**
 * 印章作废服务类
 * @author Lidongdong
 *
 */
public class ScrapServ extends CommonServ{
    public void sealScrap(ParamBean paramBean) {
        String[] sealIds = paramBean.getStr("SEAL_ID").split(",");
        for(String sealId:sealIds){
            BnUtils.changeSealStatus(sealId, "3");
        }
    }
}
