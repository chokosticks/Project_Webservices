//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.26 at 09:04:51 AM CET 
//


package matching;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchedElementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MatchedElementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OutputElement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="InputElement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Score" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchedElementType", propOrder = {
    "outputElement",
    "inputElement",
    "score"
})
public class MatchedElementType {

    @XmlElement(name = "OutputElement", required = true)
    protected String outputElement;
    @XmlElement(name = "InputElement", required = true)
    protected String inputElement;
    @XmlElement(name = "Score")
    protected double score;

    /**
     * Gets the value of the outputElement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputElement() {
        return outputElement;
    }

    /**
     * Sets the value of the outputElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputElement(String value) {
        this.outputElement = value;
    }

    /**
     * Gets the value of the inputElement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputElement() {
        return inputElement;
    }

    /**
     * Sets the value of the inputElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputElement(String value) {
        this.inputElement = value;
    }

    /**
     * Gets the value of the score property.
     * 
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     */
    public void setScore(double value) {
        this.score = value;
    }

}