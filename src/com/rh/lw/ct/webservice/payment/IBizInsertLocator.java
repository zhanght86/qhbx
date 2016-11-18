/**
 * IBizInsertLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.rh.lw.ct.webservice.payment;

public class IBizInsertLocator extends org.apache.axis.client.Service implements com.rh.lw.ct.webservice.payment.IBizInsert {

    public IBizInsertLocator() {
    }


    public IBizInsertLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IBizInsertLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IBizInsertPort
    private java.lang.String IBizInsertPort_address = "http://65.0.9.102:8001/iTMS_TEST/WebService/IBizInsert";

    public java.lang.String getIBizInsertPortAddress() {
        return IBizInsertPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IBizInsertPortWSDDServiceName = "IBizInsertPort";

    public java.lang.String getIBizInsertPortWSDDServiceName() {
        return IBizInsertPortWSDDServiceName;
    }

    public void setIBizInsertPortWSDDServiceName(java.lang.String name) {
        IBizInsertPortWSDDServiceName = name;
    }

    public com.rh.lw.ct.webservice.payment.IBizInsertPortType getIBizInsertPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IBizInsertPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIBizInsertPort(endpoint);
    }

    public com.rh.lw.ct.webservice.payment.IBizInsertPortType getIBizInsertPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.rh.lw.ct.webservice.payment.IBizInsertSoapBindingStub _stub = new com.rh.lw.ct.webservice.payment.IBizInsertSoapBindingStub(portAddress, this);
            _stub.setPortName(getIBizInsertPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIBizInsertPortEndpointAddress(java.lang.String address) {
        IBizInsertPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.rh.lw.ct.webservice.payment.IBizInsertPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.rh.lw.ct.webservice.payment.IBizInsertSoapBindingStub _stub = new com.rh.lw.ct.webservice.payment.IBizInsertSoapBindingStub(new java.net.URL(IBizInsertPort_address), this);
                _stub.setPortName(getIBizInsertPortWSDDServiceName());
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
        if ("IBizInsertPort".equals(inputPortName)) {
            return getIBizInsertPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://com/iss/interfacecore/ws/weblogicservice", "IBizInsert");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://com/iss/interfacecore/ws/weblogicservice", "IBizInsertPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IBizInsertPort".equals(portName)) {
            setIBizInsertPortEndpointAddress(address);
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
