/**
 * LW_CT_PAYMENTServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.lw.ct.webservice.checkpay;

public class LW_CT_PAYMENTServiceLocator extends org.apache.axis.client.Service implements com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENTService {

    public LW_CT_PAYMENTServiceLocator() {
    }


    public LW_CT_PAYMENTServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LW_CT_PAYMENTServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LW_CT_PAYMENTPort
    private java.lang.String LW_CT_PAYMENTPort_address = "http://localhost:8082/LW_CT_PAYMENT.ws";

    public java.lang.String getLW_CT_PAYMENTPortAddress() {
        return LW_CT_PAYMENTPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LW_CT_PAYMENTPortWSDDServiceName = "LW_CT_PAYMENTPort";

    public java.lang.String getLW_CT_PAYMENTPortWSDDServiceName() {
        return LW_CT_PAYMENTPortWSDDServiceName;
    }

    public void setLW_CT_PAYMENTPortWSDDServiceName(java.lang.String name) {
        LW_CT_PAYMENTPortWSDDServiceName = name;
    }

    public com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENT getLW_CT_PAYMENTPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LW_CT_PAYMENTPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLW_CT_PAYMENTPort(endpoint);
    }

    public com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENT getLW_CT_PAYMENTPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENTPortBindingStub _stub = new com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENTPortBindingStub(portAddress, this);
            _stub.setPortName(getLW_CT_PAYMENTPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLW_CT_PAYMENTPortEndpointAddress(java.lang.String address) {
        LW_CT_PAYMENTPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENT.class.isAssignableFrom(serviceEndpointInterface)) {
                com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENTPortBindingStub _stub = new com.rh.lw.ct.webservice.checkpay.LW_CT_PAYMENTPortBindingStub(new java.net.URL(LW_CT_PAYMENTPort_address), this);
                _stub.setPortName(getLW_CT_PAYMENTPortWSDDServiceName());
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
        if ("LW_CT_PAYMENTPort".equals(inputPortName)) {
            return getLW_CT_PAYMENTPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.ruaho.com/", "LW_CT_PAYMENTService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.ruaho.com/", "LW_CT_PAYMENTPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LW_CT_PAYMENTPort".equals(portName)) {
            setLW_CT_PAYMENTPortEndpointAddress(address);
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
