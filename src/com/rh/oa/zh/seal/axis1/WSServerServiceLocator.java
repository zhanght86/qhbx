/**
 * WSServerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package com.rh.oa.zh.seal.axis1;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;

public class WSServerServiceLocator extends Service implements WSServerService {

    public WSServerServiceLocator() {
    }


    public WSServerServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public WSServerServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSServer
    private java.lang.String WSServer_address = "http://10.193.33.17/services/WSServer.jws";

    public java.lang.String getWSServerAddress() {
        return WSServer_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSServerWSDDServiceName = "WSServer";

    public java.lang.String getWSServerWSDDServiceName() {
        return WSServerWSDDServiceName;
    }

    public void setWSServerWSDDServiceName(java.lang.String name) {
        WSServerWSDDServiceName = name;
    }

    public WSServer getWSServer() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSServer_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSServer(endpoint);
    }

    public WSServer getWSServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WSServerSoapBindingStub _stub = new WSServerSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSServerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSServerEndpointAddress(java.lang.String address) {
        WSServer_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (WSServer.class.isAssignableFrom(serviceEndpointInterface)) {
                WSServerSoapBindingStub _stub = new WSServerSoapBindingStub(new java.net.URL(WSServer_address), this);
                _stub.setPortName(getWSServerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSServer".equals(inputPortName)) {
            return getWSServer();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://10.193.52.17:8080/services/WSServer.jws", "WSServerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://10.193.52.17:8080/services/WSServer.jws", "WSServer"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSServer".equals(portName)) {
            setWSServerEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
