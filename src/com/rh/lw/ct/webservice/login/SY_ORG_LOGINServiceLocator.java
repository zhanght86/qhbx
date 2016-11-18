/**
 * SY_ORG_LOGINServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.lw.ct.webservice.login;

public class SY_ORG_LOGINServiceLocator extends org.apache.axis.client.Service implements com.rh.lw.ct.webservice.login.SY_ORG_LOGINService {

    public SY_ORG_LOGINServiceLocator() {
    }


    public SY_ORG_LOGINServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SY_ORG_LOGINServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SY_ORG_LOGINPort
    private java.lang.String SY_ORG_LOGINPort_address = "http://localhost:8083/SY_ORG_LOGIN.ws";

    public java.lang.String getSY_ORG_LOGINPortAddress() {
        return SY_ORG_LOGINPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SY_ORG_LOGINPortWSDDServiceName = "SY_ORG_LOGINPort";

    public java.lang.String getSY_ORG_LOGINPortWSDDServiceName() {
        return SY_ORG_LOGINPortWSDDServiceName;
    }

    public void setSY_ORG_LOGINPortWSDDServiceName(java.lang.String name) {
        SY_ORG_LOGINPortWSDDServiceName = name;
    }

    public com.rh.lw.ct.webservice.login.SY_ORG_LOGIN getSY_ORG_LOGINPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SY_ORG_LOGINPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSY_ORG_LOGINPort(endpoint);
    }

    public com.rh.lw.ct.webservice.login.SY_ORG_LOGIN getSY_ORG_LOGINPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.rh.lw.ct.webservice.login.SY_ORG_LOGINPortBindingStub _stub = new com.rh.lw.ct.webservice.login.SY_ORG_LOGINPortBindingStub(portAddress, this);
            _stub.setPortName(getSY_ORG_LOGINPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSY_ORG_LOGINPortEndpointAddress(java.lang.String address) {
        SY_ORG_LOGINPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.rh.lw.ct.webservice.login.SY_ORG_LOGIN.class.isAssignableFrom(serviceEndpointInterface)) {
                com.rh.lw.ct.webservice.login.SY_ORG_LOGINPortBindingStub _stub = new com.rh.lw.ct.webservice.login.SY_ORG_LOGINPortBindingStub(new java.net.URL(SY_ORG_LOGINPort_address), this);
                _stub.setPortName(getSY_ORG_LOGINPortWSDDServiceName());
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
        if ("SY_ORG_LOGINPort".equals(inputPortName)) {
            return getSY_ORG_LOGINPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.ruaho.com/", "SY_ORG_LOGINService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.ruaho.com/", "SY_ORG_LOGINPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SY_ORG_LOGINPort".equals(portName)) {
            setSY_ORG_LOGINPortEndpointAddress(address);
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
