package com.rh.lw.ct.webservice.checkpay;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.rh.core.base.Bean;
import com.rh.core.util.XmlUtils;
import com.rh.lw.ct.webservice.login.SY_ORG_LOGINServiceLocator;

/**
 * 测试回传支付状态接口
 * @author chensheng
 * 
 */
public class Test {

    /**
     * @param args 参数
     * @throws ServiceException 异常
     * @throws RemoteException 异常
     */
    public static void main(String[] args)
            throws RemoteException, ServiceException {
        // 登录
        SY_ORG_LOGINServiceLocator login = new SY_ORG_LOGINServiceLocator();
        login.setSY_ORG_LOGINPortEndpointAddress("http://65.0.1.21:8080/SY_ORG_LOGIN.wsdl.ws");
        String sId = login.getSY_ORG_LOGINPort().login("zhbx", "zijin", "zijin1234");

        // 业务处理
        try {
            LW_CT_PAYMENTServiceLocator checkpay;
            checkpay = new LW_CT_PAYMENTServiceLocator();
            checkpay.setLW_CT_PAYMENTPortEndpointAddress("http://65.0.1.21:8080/LW_CT_PAYMENT.wsdl.ws");
            String xmlData = XmlUtils.toFullXml("platforminfo", createXMLBean());
            String retStr = checkpay.getLW_CT_PAYMENTPort().checkPay(sId, xmlData);
            System.out.println(retStr);
        } catch (Exception e) {
            // 退出
            login.getSY_ORG_LOGINPort().wsLogout(sId);
        }

    }

    /**
     * 构造测试XML
     * @return 返回
     */
    private static Bean createXMLBean() {
        Bean outBean = new Bean();

        Bean baseBean = new Bean();
        baseBean.set("TOTALNUM", 1);
        baseBean.set("BATCHNO", "0I-3iavKt9vVSFLu2MM6pk");
        baseBean.set("FAILNUM", 0);
        baseBean.set("RESPONSE", "2013-10-14 00:00:53");
        outBean.set("baseinfo", baseBean);

        Bean recordBean = new Bean();
        recordBean.set("C_ID", "0I-3iavKt9vVSFLu2MM6pk");
        recordBean.set("DATASTATUS", "1");
        recordBean.set("ERRDESCRIPTION", "");
        outBean.set("recordinfos", new Bean().set("recordinfo", recordBean));

        return outBean;
    }

}
