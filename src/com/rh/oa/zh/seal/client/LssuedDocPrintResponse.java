package com.rh.oa.zh.seal.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lssuedDocPrintReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lssuedDocPrintReturn"
})
@XmlRootElement(name = "lssuedDocPrintResponse")
public class LssuedDocPrintResponse {

    @XmlElement(required = true, namespace = "http://ws.dj.com")
    protected String lssuedDocPrintReturn;

    /**
     * Gets the value of the lssuedDocPrintReturn property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLssuedDocPrintReturn() {
        return lssuedDocPrintReturn;
    }

    /**
     * Sets the value of the lssuedDocPrintReturn property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setLssuedDocPrintReturn(String value) {
        this.lssuedDocPrintReturn = value;
    }

}
