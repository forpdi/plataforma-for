
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de obterTabelasApoioQuantitativo complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterTabelasApoioQuantitativo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="retornarNaturezas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarIdOcs" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarIdUsos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarFontes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarRPs" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataHoraReferencia" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obterTabelasApoioQuantitativo", propOrder = {
    "credencial",
    "exercicio",
    "retornarNaturezas",
    "retornarIdOcs",
    "retornarIdUsos",
    "retornarFontes",
    "retornarRPs",
    "dataHoraReferencia"
})
public class ObterTabelasApoioQuantitativo {

    protected CredencialDTO credencial;
    protected Integer exercicio;
    protected boolean retornarNaturezas;
    protected boolean retornarIdOcs;
    protected boolean retornarIdUsos;
    protected boolean retornarFontes;
    protected boolean retornarRPs;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraReferencia;

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
     * Obtém o valor da propriedade exercicio.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExercicio() {
        return exercicio;
    }

    /**
     * Define o valor da propriedade exercicio.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExercicio(Integer value) {
        this.exercicio = value;
    }

    /**
     * Obtém o valor da propriedade retornarNaturezas.
     * 
     */
    public boolean isRetornarNaturezas() {
        return retornarNaturezas;
    }

    /**
     * Define o valor da propriedade retornarNaturezas.
     * 
     */
    public void setRetornarNaturezas(boolean value) {
        this.retornarNaturezas = value;
    }

    /**
     * Obtém o valor da propriedade retornarIdOcs.
     * 
     */
    public boolean isRetornarIdOcs() {
        return retornarIdOcs;
    }

    /**
     * Define o valor da propriedade retornarIdOcs.
     * 
     */
    public void setRetornarIdOcs(boolean value) {
        this.retornarIdOcs = value;
    }

    /**
     * Obtém o valor da propriedade retornarIdUsos.
     * 
     */
    public boolean isRetornarIdUsos() {
        return retornarIdUsos;
    }

    /**
     * Define o valor da propriedade retornarIdUsos.
     * 
     */
    public void setRetornarIdUsos(boolean value) {
        this.retornarIdUsos = value;
    }

    /**
     * Obtém o valor da propriedade retornarFontes.
     * 
     */
    public boolean isRetornarFontes() {
        return retornarFontes;
    }

    /**
     * Define o valor da propriedade retornarFontes.
     * 
     */
    public void setRetornarFontes(boolean value) {
        this.retornarFontes = value;
    }

    /**
     * Obtém o valor da propriedade retornarRPs.
     * 
     */
    public boolean isRetornarRPs() {
        return retornarRPs;
    }

    /**
     * Define o valor da propriedade retornarRPs.
     * 
     */
    public void setRetornarRPs(boolean value) {
        this.retornarRPs = value;
    }

    /**
     * Obtém o valor da propriedade dataHoraReferencia.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraReferencia() {
        return dataHoraReferencia;
    }

    /**
     * Define o valor da propriedade dataHoraReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraReferencia(XMLGregorianCalendar value) {
        this.dataHoraReferencia = value;
    }

}
