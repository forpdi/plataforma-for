
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de obterAcoesPorOrgao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterAcoesPorOrgao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoUo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "obterAcoesPorOrgao", propOrder = {
    "credencial",
    "exercicio",
    "codigoOrgao",
    "codigoUo",
    "codigoMomento",
    "dataHoraReferencia"
})
public class ObterAcoesPorOrgao {

    protected CredencialDTO credencial;
    protected Integer exercicio;
    protected String codigoOrgao;
    protected String codigoUo;
    protected Integer codigoMomento;
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
     * Obtém o valor da propriedade codigoOrgao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrgao() {
        return codigoOrgao;
    }

    /**
     * Define o valor da propriedade codigoOrgao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrgao(String value) {
        this.codigoOrgao = value;
    }

    /**
     * Obtém o valor da propriedade codigoUo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUo() {
        return codigoUo;
    }

    /**
     * Define o valor da propriedade codigoUo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUo(String value) {
        this.codigoUo = value;
    }

    /**
     * Obtém o valor da propriedade codigoMomento.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoMomento() {
        return codigoMomento;
    }

    /**
     * Define o valor da propriedade codigoMomento.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoMomento(Integer value) {
        this.codigoMomento = value;
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
