/**
 * WSServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.oa.zh.seal.axis1;

public interface WSServer extends java.rmi.Remote {
    public String getSealsByDeptId(java.lang.String deptId) throws java.rmi.RemoteException;
    public String synUpdateDept(java.lang.String deptStr) throws java.rmi.RemoteException;
    public String synSaveDept(java.lang.String deptStr) throws java.rmi.RemoteException;
    public String synDeleteDept(java.lang.String deptStr) throws java.rmi.RemoteException;
    public String sysDeleteUser(java.lang.String userName) throws java.rmi.RemoteException;
    public String sysUpdateUser(java.lang.String oldUserName, java.lang.String newUserName, java.lang.String newDeptId, java.lang.String newRoleId) throws java.rmi.RemoteException;
    public String sysSaveUser(java.lang.String userName, java.lang.String trueName, java.lang.String deptId, java.lang.String roleIds) throws java.rmi.RemoteException;
    public String lssuedDocPrint(java.lang.String str) throws java.rmi.RemoteException;
    public String butttonLssuedDoc(java.lang.String deptId, java.lang.String addNum, java.lang.String wyh, java.lang.String reason) throws java.rmi.RemoteException;
    public String delSealedFile(java.lang.String file_no) throws java.rmi.RemoteException;
}
