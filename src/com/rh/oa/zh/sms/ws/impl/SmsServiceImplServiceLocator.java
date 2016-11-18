/**
 * SmsServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.oa.zh.sms.ws.impl;

public class SmsServiceImplServiceLocator extends org.apache.axis.client.Service implements com.rh.oa.zh.sms.ws.impl.SmsServiceImplService {

    public SmsServiceImplServiceLocator() {
    }


    public SmsServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SmsServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SmsServiceImplPort
    private java.lang.String SmsServiceImplPort_address = "http://65.0.9.10:7006/flexsmsInterface/sms/SmsNetApp";

    public java.lang.String getSmsServiceImplPortAddress() {
        return SmsServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SmsServiceImplPortWSDDServiceName = "SmsServiceImplPort";

    public java.lang.String getSmsServiceImplPortWSDDServiceName() {
        return SmsServiceImplPortWSDDServiceName;
    }

    public void setSmsServiceImplPortWSDDServiceName(java.lang.String name) {
        SmsServiceImplPortWSDDServiceName = name;
    }

    public com.rh.oa.zh.sms.ws.SmsService getSmsServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SmsServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSmsServiceImplPort(endpoint);
    }

    public com.rh.oa.zh.sms.ws.SmsService getSmsServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.rh.oa.zh.sms.ws.impl.SmsServiceImplPortBindingStub _stub = new com.rh.oa.zh.sms.ws.impl.SmsServiceImplPortBindingStub(portAddress, this);
            _stub.setPortName(getSmsServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSmsServiceImplPortEndpointAddress(java.lang.String address) {
        SmsServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.rh.oa.zh.sms.ws.SmsService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.rh.oa.zh.sms.ws.impl.SmsServiceImplPortBindingStub _stub = new com.rh.oa.zh.sms.ws.impl.SmsServiceImplPortBindingStub(new java.net.URL(SmsServiceImplPort_address), this);
                _stub.setPortName(getSmsServiceImplPortWSDDServiceName());
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
        if ("SmsServiceImplPort".equals(inputPortName)) {
            return getSmsServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.service.server.com/", "SmsServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.service.server.com/", "SmsServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SmsServiceImplPort".equals(portName)) {
            setSmsServiceImplPortEndpointAddress(address);
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
