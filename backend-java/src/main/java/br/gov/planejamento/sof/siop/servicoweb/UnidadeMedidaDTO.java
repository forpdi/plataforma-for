
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de unidadeMedidaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="unidadeMedidaDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoUnidadeMedida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataHoraAlteracao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="simbolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snAtivo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unidadeMedidaDTO", propOrder = {
    "codigoUnidadeMedida",
    "dataHoraAlteracao",
    "descricao",
    "simbolo",
    "snAtivo"
})
public class UnidadeMedidaDTO {

    protected String codigoUnidadeMedida;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraAlteracao;
    protected String descricao;
    protected String simbolo;
    protected Boolean snAtivo;

    /**
     * Obtém o valor da propriedade codigoUnidadeMedida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUnidadeMedida() {
        return codigoUnidadeMedida;
    }

    /**
     * Define o valor da propriedade codigoUnidadeMedida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUnidadeMedida(String value) {
        this.codigoUnidadeMedida = value;
    }

    /**
     * Obtém o valor da propriedade dataHoraAlteracao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraAlteracao() {
        return dataHoraAlteracao;
    }

    /**
     * Define o valor da propriedade dataHoraAlteracao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraAlteracao(XMLGregorianCalendar value) {
        this.dataHoraAlteracao = value;
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

    /**
     * Obtém o valor da propriedade simbolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Define o valor da propriedade simbolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimbolo(String value) {
        this.simbolo = value;
    }

    /**
     * Obtém o valor da propriedade snAtivo.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAtivo() {
        return snAtivo;
    }

    /**
     * Define o valor da propriedade snAtivo.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAtivo(Boolean value) {
        this.snAtivo = value;
    }

}
