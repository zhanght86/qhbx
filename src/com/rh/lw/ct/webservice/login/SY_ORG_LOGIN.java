/**
 * SY_ORG_LOGIN.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.lw.ct.webservice.login;

public interface SY_ORG_LOGIN extends java.rmi.Remote {
    public java.lang.String login(java.lang.String cmpyCode, java.lang.String loginName, java.lang.String password) throws java.rmi.RemoteException, com.rh.lw.ct.webservice.login.Exception;
    public void wsLogout(java.lang.String SID) throws java.rmi.RemoteException, com.rh.lw.ct.webservice.login.Exception;
    public void isLogin(java.lang.String SID) throws java.rmi.RemoteException, com.rh.lw.ct.webservice.login.Exception;
}
