
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de consultarExecucoes complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="consultarExecucoes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="execucao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoEstataisDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarExecucoes", propOrder = {
    "credencial",
    "execucao"
})
public class ConsultarExecucoes {

    protected CredencialDTO credencial;
    protected ExecucaoEstataisDTO execucao;

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
     * Obtém o valor da propriedade execucao.
     * 
     * @return
     *     possible object is
     *     {@link ExecucaoEstataisDTO }
     *     
     */
    public ExecucaoEstataisDTO getExecucao() {
        return execucao;
    }

    /**
     * Define o valor da propriedade execucao.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecucaoEstataisDTO }
     *     
     */
    public void setExecucao(ExecucaoEstataisDTO value) {
        this.execucao = value;
    }

}
