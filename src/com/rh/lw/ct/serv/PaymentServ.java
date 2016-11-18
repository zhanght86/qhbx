package com.rh.lw.ct.serv;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.ConfMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;
import com.rh.lw.ct.mgr.PaymentMgr;

/**
 * 付款计划
 * @author chensheng
 *
 */
public class PaymentServ extends CommonServ {
    
    /**
     * 调用资金系统接口
     * @param paramBean 参数
     * @return 返回结果
     */
    public OutBean pay(ParamBean paramBean) {
        String address = "http://65.0.9.102:8001/iTMS_TEST/WebService/IBizInsert?WSDL";
        address = ConfMgr.getConf("OA_PAYMENT_ADDRESS", address);
        Bean payBean = PaymentMgr.createPayBean(paramBean); // 支付要素Bean
        Bean msgBean = PaymentMgr.createMsgBean(payBean); // 支付报文Bean
        OutBean outBean = PaymentMgr.doPay(paramBean.getId(), address, msgBean);
        if (outBean.isOk()) { // 上传支付报文成功，设置支付人和支付时间
            String userCode = Context.getUserBean().getId(); // 当前人作为支付人
            String dateTimeTS = DateUtils.getDatetimeTS(); // 支付时间
            ServDao.update(paramBean.getServId(), 
                    new Bean().set("PM_USER", userCode).set("PM_TIME", dateTimeTS));
        }
        return outBean;
    }
    
    /**
     * 提供给资金系统调用，回写支付状态
     * @param paramBean 参数
     * @return 返回
     */
    public OutBean checkPay(ParamBean paramBean) {
        return PaymentMgr.doCheckPay(paramBean);
    }
    
}
