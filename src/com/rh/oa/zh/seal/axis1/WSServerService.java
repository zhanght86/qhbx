/**
 * WSServerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.oa.zh.seal.axis1;

public interface WSServerService extends javax.xml.rpc.Service {
    public java.lang.String getWSServerAddress();

    public WSServer getWSServer() throws javax.xml.rpc.ServiceException;

    public WSServer getWSServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
