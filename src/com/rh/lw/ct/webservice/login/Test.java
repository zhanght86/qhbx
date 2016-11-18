package com.rh.lw.ct.webservice.login;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

/**
 * 测试登录接口
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
        SY_ORG_LOGINServiceLocator login = new SY_ORG_LOGINServiceLocator();
        login.setSY_ORG_LOGINPortEndpointAddress("http://65.0.1.21:8080/SY_ORG_LOGIN.ws");
        String retStr = login.getSY_ORG_LOGINPort().login("zhbx", "admin", "123456");
        System.out.println(retStr);
    }
    

}
