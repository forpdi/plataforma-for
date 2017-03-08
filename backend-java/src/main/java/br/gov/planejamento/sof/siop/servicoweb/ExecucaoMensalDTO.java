
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoMensalDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoMensalDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="financeiro" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fisico" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="mensagem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mes" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoMensalDTO", propOrder = {
    "financeiro",
    "fisico",
    "mensagem",
    "mes"
})
public class ExecucaoMensalDTO
    extends BaseDTO
{

    protected Long financeiro;
    protected Long fisico;
    protected String mensagem;
    protected int mes;

    /**
     * Obtém o valor da propriedade financeiro.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFinanceiro() {
        return financeiro;
    }

    /**
     * Define o valor da propriedade financeiro.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFinanceiro(Long value) {
        this.financeiro = value;
    }

    /**
     * Obtém o valor da propriedade fisico.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFisico() {
        return fisico;
    }

    /**
     * Define o valor da propriedade fisico.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFisico(Long value) {
        this.fisico = value;
    }

    /**
     * Obtém o valor da propriedade mensagem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Define o valor da propriedade mensagem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagem(String value) {
        this.mensagem = value;
    }

    /**
     * Obtém o valor da propriedade mes.
     * 
     */
    public int getMes() {
        return mes;
    }

    /**
     * Define o valor da propriedade mes.
     * 
     */
    public void setMes(int value) {
        this.mes = value;
    }

}
