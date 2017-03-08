
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoFinanceiroEmendasDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoFinanceiroEmendasDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="financeiros">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="financeiroEmendas" type="{http://servicoweb.siop.sof.planejamento.gov.br/}financeiroEmendasDTO" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "retornoFinanceiroEmendasDTO", propOrder = {
    "financeiros"
})
public class RetornoFinanceiroEmendasDTO
    extends RetornoDTO
{

    @XmlElement(required = true)
    protected RetornoFinanceiroEmendasDTO.Financeiros financeiros;

    /**
     * Obtém o valor da propriedade financeiros.
     * 
     * @return
     *     possible object is
     *     {@link RetornoFinanceiroEmendasDTO.Financeiros }
     *     
     */
    public RetornoFinanceiroEmendasDTO.Financeiros getFinanceiros() {
        return financeiros;
    }

    /**
     * Define o valor da propriedade financeiros.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoFinanceiroEmendasDTO.Financeiros }
     *     
     */
    public void setFinanceiros(RetornoFinanceiroEmendasDTO.Financeiros value) {
        this.financeiros = value;
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
     *         &lt;element name="financeiroEmendas" type="{http://servicoweb.siop.sof.planejamento.gov.br/}financeiroEmendasDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "financeiroEmendas"
    })
    public static class Financeiros {

        protected List<FinanceiroEmendasDTO> financeiroEmendas;

        /**
         * Gets the value of the financeiroEmendas property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the financeiroEmendas property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFinanceiroEmendas().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FinanceiroEmendasDTO }
         * 
         * 
         */
        public List<FinanceiroEmendasDTO> getFinanceiroEmendas() {
            if (financeiroEmendas == null) {
                financeiroEmendas = new ArrayList<FinanceiroEmendasDTO>();
            }
            return this.financeiroEmendas;
        }

    }

}
