//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.04 at 03:12:21 AM EST 
//


package nlp.hwz.dict.pojo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the nlp.hwz.dict.pojo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: nlp.hwz.dict.pojo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Sense }
     * 
     */
    public Sense createSense() {
        return new Sense();
    }

    /**
     * Create an instance of {@link Dictmap }
     * 
     */
    public Dictmap createDictmap() {
        return new Dictmap();
    }

    /**
     * Create an instance of {@link Lexelt }
     * 
     */
    public Lexelt createLexelt() {
        return new Lexelt();
    }

}