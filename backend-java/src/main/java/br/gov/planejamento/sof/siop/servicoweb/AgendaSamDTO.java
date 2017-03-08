
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de agendaSamDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="agendaSamDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="agendaSam" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoAgendaSam" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "agendaSamDTO", propOrder = {
    "agendaSam",
    "codigoAgendaSam",
    "descricao"
})
public class AgendaSamDTO
    extends BaseDTO
{

    protected String agendaSam;
    protected Integer codigoAgendaSam;
    protected String descricao;

    /**
     * Obtém o valor da propriedade agendaSam.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgendaSam() {
        return agendaSam;
    }

    /**
     * Define o valor da propriedade agendaSam.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgendaSam(String value) {
        this.agendaSam = value;
    }

    /**
     * Obtém o valor da propriedade codigoAgendaSam.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoAgendaSam() {
        return codigoAgendaSam;
    }

    /**
     * Define o valor da propriedade codigoAgendaSam.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoAgendaSam(Integer value) {
        this.codigoAgendaSam = value;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

}
