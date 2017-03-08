
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de financeiroEmendasDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="financeiroEmendasDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}financeiroDTO">
 *       &lt;sequence>
 *         &lt;element name="emendas">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="emenda" type="{http://servicoweb.siop.sof.planejamento.gov.br/}emendaDTO" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "financeiroEmendasDTO", propOrder = {
    "emendas"
})
public class FinanceiroEmendasDTO
    extends FinanceiroDTO
{

    @XmlElement(required = true)
    protected FinanceiroEmendasDTO.Emendas emendas;

    /**
     * Obtém o valor da propriedade emendas.
     * 
     * @return
     *     possible object is
     *     {@link FinanceiroEmendasDTO.Emendas }
     *     
     */
    public FinanceiroEmendasDTO.Emendas getEmendas() {
        return emendas;
    }

    /**
     * Define o valor da propriedade emendas.
     * 
     * @param value
     *     allowed object is
     *     {@link FinanceiroEmendasDTO.Emendas }
     *     
     */
    public void setEmendas(FinanceiroEmendasDTO.Emendas value) {
        this.emendas = value;
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
     *         &lt;element name="emenda" type="{http://servicoweb.siop.sof.planejamento.gov.br/}emendaDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "emenda"
    })
    public static class Emendas {

        protected List<EmendaDTO> emenda;

        /**
         * Gets the value of the emenda property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the emenda property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEmenda().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link EmendaDTO }
         * 
         * 
         */
        public List<EmendaDTO> getEmenda() {
            if (emenda == null) {
                emenda = new ArrayList<EmendaDTO>();
            }
            return this.emenda;
        }

    }

}
