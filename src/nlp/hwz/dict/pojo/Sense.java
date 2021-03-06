//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.04 at 03:12:21 AM EST 
//


package nlp.hwz.dict.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="gloss" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="synset" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="wn" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "sense")
public class Sense {

    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String gloss;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String id;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String source;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String synset;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String wn;

    /**
     * Gets the value of the gloss property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGloss() {
        return gloss;
    }

    /**
     * Sets the value of the gloss property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGloss(String value) {
        this.gloss = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the synset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSynset() {
        return synset;
    }

    /**
     * Sets the value of the synset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSynset(String value) {
        this.synset = value;
    }

    /**
     * Gets the value of the wn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWn() {
        return wn;
    }

    /**
     * Sets the value of the wn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWn(String value) {
        this.wn = value;
    }

}
