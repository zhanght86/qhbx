package com.rh.lw.ct.webservice.payment;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.rh.core.base.Bean;
import com.rh.lw.ct.mgr.PaymentMgr;

/**
 * 测试资金接口
 * @author chensheng
 *
 */
public class Test {

    /**
     * @param args 参数
     * @throws RemoteException 
     * @throws ServiceException 
     */
    public static void main(String[] args) throws RemoteException, ServiceException {
        String address = "http://65.0.9.102:8001/iTMS_TEST/WebService/IBizInsert?WSDL";
        Bean msgBean = PaymentMgr.createMsgBean(createPayBean());
        PaymentMgr.doPay("", address, msgBean);
    }
    
    /**
     * 构造测试支付要素Bean
     * @return 支付要素
     */
    public static Bean createPayBean() {
        
        Bean payBean = new Bean();
        payBean.set("C_ID", "EM23120145060335000909-1-16");    // 以支付计划的ID作为支付的唯一标识码
        payBean.set("BILLCODE", "EM23120145060335000909-1-1");  // 合同编号作为业务单据编号
        payBean.set("DATASOURCE", "HT"); // 假设'HT'是合同系统在资金系统内的标识
        payBean.set("TRANSFERDATE", "2013-10-13"); // 合同起草日期作为业务单据日期
        payBean.set("ORGCODE", "00"); // 起草机构作为业务申请单位编号
        payBean.set("PAYTYPE", 1); // 资金交割方式，需要确认，1：直联 2：非直联
        payBean.set("TRANSFERCODE", 9801); // 业务类型编号，需要确认
        payBean.set("PAYTYPE", 1); // 付款用途编号，需要确认
        payBean.set("BUDGETPROJECTCODE", 1); // 预算项目编号，需要确认
        payBean.set("RECACCOUNTNO", "277872801747"); // 签约对方签约对方开户行账号作为收款账户编号
        payBean.set("RECACCOUNTNAME", "测试"); // 签约对方姓名作为收款账户名称
        payBean.set("RECBANKNAME", "中行北京分行"); // 收款人开户行地址不能提供
        payBean.set("RECPROVINCE", "北京"); // 收款行地址（省）不能提供
        payBean.set("RECAREANAMEOFCITY", "北京"); // 收款行地址（市）不能提供
        payBean.set("BANKPAYTYPE", 1); // 付款对象类型（银行指令类型），需要确认   1：公对公 2：公对私
        payBean.set("CURRENCYCODE", "CNY"); // 结算币种，国际标准代码
        payBean.set("AMOUNT", 700.000000); // 付款计划的预付款金额作为付款金额
        payBean.set("TRANSFERTYPE", 1); // 业务类别，需要确认    1：业务结算 2：员工结算
        payBean.set("DATASTATUS", 0); // 数据状态，需要确认
        
        return payBean;
    }

}
