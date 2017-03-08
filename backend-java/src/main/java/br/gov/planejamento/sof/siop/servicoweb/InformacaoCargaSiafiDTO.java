
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de informacaoCargaSiafiDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="informacaoCargaSiafiDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataCompetencia" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ultimaCarga" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ultimoMesFechado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataFechamentoUltimoMes" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informacaoCargaSiafiDTO", propOrder = {
    "tipo",
    "dataCompetencia",
    "ultimaCarga",
    "ultimoMesFechado",
    "dataFechamentoUltimoMes"
})
public class InformacaoCargaSiafiDTO {

    protected String tipo;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataCompetencia;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar ultimaCarga;
    protected String ultimoMesFechado;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataFechamentoUltimoMes;

    /**
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Obtém o valor da propriedade dataCompetencia.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCompetencia() {
        return dataCompetencia;
    }

    /**
     * Define o valor da propriedade dataCompetencia.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCompetencia(XMLGregorianCalendar value) {
        this.dataCompetencia = value;
    }

    /**
     * Obtém o valor da propriedade ultimaCarga.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUltimaCarga() {
        return ultimaCarga;
    }

    /**
     * Define o valor da propriedade ultimaCarga.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUltimaCarga(XMLGregorianCalendar value) {
        this.ultimaCarga = value;
    }

    /**
     * Obtém o valor da propriedade ultimoMesFechado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUltimoMesFechado() {
        return ultimoMesFechado;
    }

    /**
     * Define o valor da propriedade ultimoMesFechado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUltimoMesFechado(String value) {
        this.ultimoMesFechado = value;
    }

    /**
     * Obtém o valor da propriedade dataFechamentoUltimoMes.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFechamentoUltimoMes() {
        return dataFechamentoUltimoMes;
    }

    /**
     * Define o valor da propriedade dataFechamentoUltimoMes.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFechamentoUltimoMes(XMLGregorianCalendar value) {
        this.dataFechamentoUltimoMes = value;
    }

}
