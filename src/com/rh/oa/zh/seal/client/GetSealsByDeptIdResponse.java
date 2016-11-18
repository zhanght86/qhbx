
package com.rh.oa.zh.seal.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getSealsByDeptIdReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "getSealsByDeptIdReturn"
})
@XmlRootElement(name = "getSealsByDeptIdResponse")
public class GetSealsByDeptIdResponse {

    @XmlElement(required = true,namespace = "http://ws.dj.com")
    protected String getSealsByDeptIdReturn;

    /**
     * Gets the value of the getSealsByDeptIdReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetSealsByDeptIdReturn() {
        return getSealsByDeptIdReturn;
    }

    /**
     * Sets the value of the getSealsByDeptIdReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetSealsByDeptIdReturn(String value) {
        this.getSealsByDeptIdReturn = value;
    }

}
