
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de incluirExecucao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="incluirExecucao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="input" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoEstataisDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "incluirExecucao", propOrder = {
    "credencial",
    "input"
})
public class IncluirExecucao {

    protected CredencialDTO credencial;
    protected InputExecucaoEstataisDTO input;

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
     * Obtém o valor da propriedade input.
     * 
     * @return
     *     possible object is
     *     {@link InputExecucaoEstataisDTO }
     *     
     */
    public InputExecucaoEstataisDTO getInput() {
        return input;
    }

    /**
     * Define o valor da propriedade input.
     * 
     * @param value
     *     allowed object is
     *     {@link InputExecucaoEstataisDTO }
     *     
     */
    public void setInput(InputExecucaoEstataisDTO value) {
        this.input = value;
    }

}
