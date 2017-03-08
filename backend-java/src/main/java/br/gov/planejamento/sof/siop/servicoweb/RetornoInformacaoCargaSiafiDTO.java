
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoInformacaoCargaSiafiDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoInformacaoCargaSiafiDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="informacoesCargaSiafi">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="informacaoCargaSiafi" type="{http://servicoweb.siop.sof.planejamento.gov.br/}informacaoCargaSiafiDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoInformacaoCargaSiafiDTO", propOrder = {
    "informacoesCargaSiafi"
})
public class RetornoInformacaoCargaSiafiDTO
    extends RetornoDTO
{

    @XmlElement(required = true)
    protected RetornoInformacaoCargaSiafiDTO.InformacoesCargaSiafi informacoesCargaSiafi;

    /**
     * Obtém o valor da propriedade informacoesCargaSiafi.
     * 
     * @return
     *     possible object is
     *     {@link RetornoInformacaoCargaSiafiDTO.InformacoesCargaSiafi }
     *     
     */
    public RetornoInformacaoCargaSiafiDTO.InformacoesCargaSiafi getInformacoesCargaSiafi() {
        return informacoesCargaSiafi;
    }

    /**
     * Define o valor da propriedade informacoesCargaSiafi.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoInformacaoCargaSiafiDTO.InformacoesCargaSiafi }
     *     
     */
    public void setInformacoesCargaSiafi(RetornoInformacaoCargaSiafiDTO.InformacoesCargaSiafi value) {
        this.informacoesCargaSiafi = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="informacaoCargaSiafi" type="{http://servicoweb.siop.sof.planejamento.gov.br/}informacaoCargaSiafiDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "informacaoCargaSiafi"
    })
    public static class InformacoesCargaSiafi {

        protected List<InformacaoCargaSiafiDTO> informacaoCargaSiafi;

        /**
         * Gets the value of the informacaoCargaSiafi property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the informacaoCargaSiafi property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInformacaoCargaSiafi().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InformacaoCargaSiafiDTO }
         * 
         * 
         */
        public List<InformacaoCargaSiafiDTO> getInformacaoCargaSiafi() {
            if (informacaoCargaSiafi == null) {
                informacaoCargaSiafi = new ArrayList<InformacaoCargaSiafiDTO>();
            }
            return this.informacaoCargaSiafi;
        }

    }

}
