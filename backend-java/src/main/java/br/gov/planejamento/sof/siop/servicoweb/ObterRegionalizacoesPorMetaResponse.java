
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de obterRegionalizacoesPorMetaResponse complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterRegionalizacoesPorMetaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoRegionalizacoesDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obterRegionalizacoesPorMetaResponse", propOrder = {
    "_return"
})
public class ObterRegionalizacoesPorMetaResponse {

    @XmlElement(name = "return")
    protected RetornoRegionalizacoesDTO _return;

    /**
     * Obtém o valor da propriedade return.
     * 
     * @return
     *     possible object is
     *     {@link RetornoRegionalizacoesDTO }
     *     
     */
    public RetornoRegionalizacoesDTO getReturn() {
        return _return;
    }

    /**
     * Define o valor da propriedade return.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoRegionalizacoesDTO }
     *     
     */
    public void setReturn(RetornoRegionalizacoesDTO value) {
        this._return = value;
    }

}
