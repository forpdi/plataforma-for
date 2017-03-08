
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de obterFinanciamentoExtraOrcamentarioPorIniciativaResponse complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterFinanciamentoExtraOrcamentarioPorIniciativaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoFinanciamentoExtraOrcamentarioDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obterFinanciamentoExtraOrcamentarioPorIniciativaResponse", propOrder = {
    "_return"
})
public class ObterFinanciamentoExtraOrcamentarioPorIniciativaResponse {

    @XmlElement(name = "return")
    protected RetornoFinanciamentoExtraOrcamentarioDTO _return;

    /**
     * Obtém o valor da propriedade return.
     * 
     * @return
     *     possible object is
     *     {@link RetornoFinanciamentoExtraOrcamentarioDTO }
     *     
     */
    public RetornoFinanciamentoExtraOrcamentarioDTO getReturn() {
        return _return;
    }

    /**
     * Define o valor da propriedade return.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoFinanciamentoExtraOrcamentarioDTO }
     *     
     */
    public void setReturn(RetornoFinanciamentoExtraOrcamentarioDTO value) {
        this._return = value;
    }

}
