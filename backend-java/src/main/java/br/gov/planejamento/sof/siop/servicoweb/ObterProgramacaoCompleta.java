
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de obterProgramacaoCompleta complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterProgramacaoCompleta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="retornarOrgaos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarProgramas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarIndicadores" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarObjetivos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarIniciativas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarAcoes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarLocalizadores" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarMetas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarRegionalizacoes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarPlanosOrcamentarios" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarAgendaSam" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarMedidasInstitucionaisNormativas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
@XmlType(name = "obterProgramacaoCompleta", propOrder = {
    "credencial",
    "exercicio",
    "codigoMomento",
    "retornarOrgaos",
    "retornarProgramas",
    "retornarIndicadores",
    "retornarObjetivos",
    "retornarIniciativas",
    "retornarAcoes",
    "retornarLocalizadores",
    "retornarMetas",
    "retornarRegionalizacoes",
    "retornarPlanosOrcamentarios",
    "retornarAgendaSam",
    "retornarMedidasInstitucionaisNormativas",
    "dataHoraReferencia"
})
public class ObterProgramacaoCompleta {

    protected CredencialDTO credencial;
    protected Integer exercicio;
    protected Integer codigoMomento;
    protected boolean retornarOrgaos;
    protected boolean retornarProgramas;
    protected boolean retornarIndicadores;
    protected boolean retornarObjetivos;
    protected boolean retornarIniciativas;
    protected boolean retornarAcoes;
    protected boolean retornarLocalizadores;
    protected boolean retornarMetas;
    protected boolean retornarRegionalizacoes;
    protected boolean retornarPlanosOrcamentarios;
    protected boolean retornarAgendaSam;
    protected boolean retornarMedidasInstitucionaisNormativas;
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
     * Obtém o valor da propriedade retornarOrgaos.
     * 
     */
    public boolean isRetornarOrgaos() {
        return retornarOrgaos;
    }

    /**
     * Define o valor da propriedade retornarOrgaos.
     * 
     */
    public void setRetornarOrgaos(boolean value) {
        this.retornarOrgaos = value;
    }

    /**
     * Obtém o valor da propriedade retornarProgramas.
     * 
     */
    public boolean isRetornarProgramas() {
        return retornarProgramas;
    }

    /**
     * Define o valor da propriedade retornarProgramas.
     * 
     */
    public void setRetornarProgramas(boolean value) {
        this.retornarProgramas = value;
    }

    /**
     * Obtém o valor da propriedade retornarIndicadores.
     * 
     */
    public boolean isRetornarIndicadores() {
        return retornarIndicadores;
    }

    /**
     * Define o valor da propriedade retornarIndicadores.
     * 
     */
    public void setRetornarIndicadores(boolean value) {
        this.retornarIndicadores = value;
    }

    /**
     * Obtém o valor da propriedade retornarObjetivos.
     * 
     */
    public boolean isRetornarObjetivos() {
        return retornarObjetivos;
    }

    /**
     * Define o valor da propriedade retornarObjetivos.
     * 
     */
    public void setRetornarObjetivos(boolean value) {
        this.retornarObjetivos = value;
    }

    /**
     * Obtém o valor da propriedade retornarIniciativas.
     * 
     */
    public boolean isRetornarIniciativas() {
        return retornarIniciativas;
    }

    /**
     * Define o valor da propriedade retornarIniciativas.
     * 
     */
    public void setRetornarIniciativas(boolean value) {
        this.retornarIniciativas = value;
    }

    /**
     * Obtém o valor da propriedade retornarAcoes.
     * 
     */
    public boolean isRetornarAcoes() {
        return retornarAcoes;
    }

    /**
     * Define o valor da propriedade retornarAcoes.
     * 
     */
    public void setRetornarAcoes(boolean value) {
        this.retornarAcoes = value;
    }

    /**
     * Obtém o valor da propriedade retornarLocalizadores.
     * 
     */
    public boolean isRetornarLocalizadores() {
        return retornarLocalizadores;
    }

    /**
     * Define o valor da propriedade retornarLocalizadores.
     * 
     */
    public void setRetornarLocalizadores(boolean value) {
        this.retornarLocalizadores = value;
    }

    /**
     * Obtém o valor da propriedade retornarMetas.
     * 
     */
    public boolean isRetornarMetas() {
        return retornarMetas;
    }

    /**
     * Define o valor da propriedade retornarMetas.
     * 
     */
    public void setRetornarMetas(boolean value) {
        this.retornarMetas = value;
    }

    /**
     * Obtém o valor da propriedade retornarRegionalizacoes.
     * 
     */
    public boolean isRetornarRegionalizacoes() {
        return retornarRegionalizacoes;
    }

    /**
     * Define o valor da propriedade retornarRegionalizacoes.
     * 
     */
    public void setRetornarRegionalizacoes(boolean value) {
        this.retornarRegionalizacoes = value;
    }

    /**
     * Obtém o valor da propriedade retornarPlanosOrcamentarios.
     * 
     */
    public boolean isRetornarPlanosOrcamentarios() {
        return retornarPlanosOrcamentarios;
    }

    /**
     * Define o valor da propriedade retornarPlanosOrcamentarios.
     * 
     */
    public void setRetornarPlanosOrcamentarios(boolean value) {
        this.retornarPlanosOrcamentarios = value;
    }

    /**
     * Obtém o valor da propriedade retornarAgendaSam.
     * 
     */
    public boolean isRetornarAgendaSam() {
        return retornarAgendaSam;
    }

    /**
     * Define o valor da propriedade retornarAgendaSam.
     * 
     */
    public void setRetornarAgendaSam(boolean value) {
        this.retornarAgendaSam = value;
    }

    /**
     * Obtém o valor da propriedade retornarMedidasInstitucionaisNormativas.
     * 
     */
    public boolean isRetornarMedidasInstitucionaisNormativas() {
        return retornarMedidasInstitucionaisNormativas;
    }

    /**
     * Define o valor da propriedade retornarMedidasInstitucionaisNormativas.
     * 
     */
    public void setRetornarMedidasInstitucionaisNormativas(boolean value) {
        this.retornarMedidasInstitucionaisNormativas = value;
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
