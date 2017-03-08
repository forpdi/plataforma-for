
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de cadastrarAcompanhamentoOrcamentarioResponse complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="cadastrarAcompanhamentoOrcamentarioResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoAcompanhamentoOrcamentarioDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cadastrarAcompanhamentoOrcamentarioResponse", propOrder = {
    "_return"
})
public class CadastrarAcompanhamentoOrcamentarioResponse {

    @XmlElement(name = "return")
    protected RetornoAcompanhamentoOrcamentarioDTO _return;

    /**
     * Obtém o valor da propriedade return.
     * 
     * @return
     *     possible object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO }
     *     
     */
    public RetornoAcompanhamentoOrcamentarioDTO getReturn() {
        return _return;
    }

    /**
     * Define o valor da propriedade return.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO }
     *     
     */
    public void setReturn(RetornoAcompanhamentoOrcamentarioDTO value) {
        this._return = value;
    }

}
