
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de obterMedidaInstitucionalPorIniciativaResponse complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterMedidaInstitucionalPorIniciativaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoMedidaInstitucionalNormativaDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obterMedidaInstitucionalPorIniciativaResponse", propOrder = {
    "_return"
})
public class ObterMedidaInstitucionalPorIniciativaResponse {

    @XmlElement(name = "return")
    protected RetornoMedidaInstitucionalNormativaDTO _return;

    /**
     * Obtém o valor da propriedade return.
     * 
     * @return
     *     possible object is
     *     {@link RetornoMedidaInstitucionalNormativaDTO }
     *     
     */
    public RetornoMedidaInstitucionalNormativaDTO getReturn() {
        return _return;
    }

    /**
     * Define o valor da propriedade return.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoMedidaInstitucionalNormativaDTO }
     *     
     */
    public void setReturn(RetornoMedidaInstitucionalNormativaDTO value) {
        this._return = value;
    }

}
