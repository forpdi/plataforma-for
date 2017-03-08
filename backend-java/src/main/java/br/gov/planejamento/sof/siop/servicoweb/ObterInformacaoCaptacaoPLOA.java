
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de obterInformacaoCaptacaoPLOA complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterInformacaoCaptacaoPLOA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="parametro" type="{http://servicoweb.siop.sof.planejamento.gov.br/}parametroInformacaoCaptacaoPLOA" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obterInformacaoCaptacaoPLOA", propOrder = {
    "credencial",
    "parametro"
})
public class ObterInformacaoCaptacaoPLOA {

    protected CredencialDTO credencial;
    protected ParametroInformacaoCaptacaoPLOA parametro;

    /**
     * Obtém o valor da propriedade credencial.
     * 
     * @return
     *     possible object is
     *     {@link CredencialDTO }
     *     
     */
    public CredencialDTO getCredencial() {
        return credencial;
    }

    /**
     * Define o valor da propriedade credencial.
     * 
     * @param value
     *     allowed object is
     *     {@link CredencialDTO }
     *     
     */
    public void setCredencial(CredencialDTO value) {
        this.credencial = value;
    }

    /**
     * Obtém o valor da propriedade parametro.
     * 
     * @return
     *     possible object is
     *     {@link ParametroInformacaoCaptacaoPLOA }
     *     
     */
    public ParametroInformacaoCaptacaoPLOA getParametro() {
        return parametro;
    }

    /**
     * Define o valor da propriedade parametro.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametroInformacaoCaptacaoPLOA }
     *     
     */
    public void setParametro(ParametroInformacaoCaptacaoPLOA value) {
        this.parametro = value;
    }

}
