
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de programaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="programaDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoMacroDesafio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoTipoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estrategiaImplementacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="horizonteTemporalContinuo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="identificadorUnico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="justificativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objetivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objetivoGoverno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objetivoSetorial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="problema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicoAlvo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snExclusaoLogica" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeResponsavel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "programaDTO", propOrder = {
    "codigoMacroDesafio",
    "codigoMomento",
    "codigoOrgao",
    "codigoPrograma",
    "codigoTipoPrograma",
    "estrategiaImplementacao",
    "exercicio",
    "horizonteTemporalContinuo",
    "identificadorUnico",
    "justificativa",
    "objetivo",
    "objetivoGoverno",
    "objetivoSetorial",
    "problema",
    "publicoAlvo",
    "snExclusaoLogica",
    "titulo",
    "unidadeResponsavel"
})
public class ProgramaDTO {

    protected Integer codigoMacroDesafio;
    protected Integer codigoMomento;
    protected String codigoOrgao;
    protected String codigoPrograma;
    protected String codigoTipoPrograma;
    protected String estrategiaImplementacao;
    protected Integer exercicio;
    protected Integer horizonteTemporalContinuo;
    protected Integer identificadorUnico;
    protected String justificativa;
    protected String objetivo;
    protected String objetivoGoverno;
    protected String objetivoSetorial;
    protected String problema;
    protected String publicoAlvo;
    protected Boolean snExclusaoLogica;
    protected String titulo;
    protected String unidadeResponsavel;

    /**
     * Obtém o valor da propriedade codigoMacroDesafio.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoMacroDesafio() {
        return codigoMacroDesafio;
    }

    /**
     * Define o valor da propriedade codigoMacroDesafio.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoMacroDesafio(Integer value) {
        this.codigoMacroDesafio = value;
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
     * Obtém o valor da propriedade codigoPrograma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    /**
     * Define o valor da propriedade codigoPrograma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPrograma(String value) {
        this.codigoPrograma = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoPrograma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTipoPrograma() {
        return codigoTipoPrograma;
    }

    /**
     * Define o valor da propriedade codigoTipoPrograma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTipoPrograma(String value) {
        this.codigoTipoPrograma = value;
    }

    /**
     * Obtém o valor da propriedade estrategiaImplementacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstrategiaImplementacao() {
        return estrategiaImplementacao;
    }

    /**
     * Define o valor da propriedade estrategiaImplementacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstrategiaImplementacao(String value) {
        this.estrategiaImplementacao = value;
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
     * Obtém o valor da propriedade horizonteTemporalContinuo.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHorizonteTemporalContinuo() {
        return horizonteTemporalContinuo;
    }

    /**
     * Define o valor da propriedade horizonteTemporalContinuo.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHorizonteTemporalContinuo(Integer value) {
        this.horizonteTemporalContinuo = value;
    }

    /**
     * Obtém o valor da propriedade identificadorUnico.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorUnico() {
        return identificadorUnico;
    }

    /**
     * Define o valor da propriedade identificadorUnico.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorUnico(Integer value) {
        this.identificadorUnico = value;
    }

    /**
     * Obtém o valor da propriedade justificativa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificativa() {
        return justificativa;
    }

    /**
     * Define o valor da propriedade justificativa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificativa(String value) {
        this.justificativa = value;
    }

    /**
     * Obtém o valor da propriedade objetivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjetivo() {
        return objetivo;
    }

    /**
     * Define o valor da propriedade objetivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjetivo(String value) {
        this.objetivo = value;
    }

    /**
     * Obtém o valor da propriedade objetivoGoverno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjetivoGoverno() {
        return objetivoGoverno;
    }

    /**
     * Define o valor da propriedade objetivoGoverno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjetivoGoverno(String value) {
        this.objetivoGoverno = value;
    }

    /**
     * Obtém o valor da propriedade objetivoSetorial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjetivoSetorial() {
        return objetivoSetorial;
    }

    /**
     * Define o valor da propriedade objetivoSetorial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjetivoSetorial(String value) {
        this.objetivoSetorial = value;
    }

    /**
     * Obtém o valor da propriedade problema.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProblema() {
        return problema;
    }

    /**
     * Define o valor da propriedade problema.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProblema(String value) {
        this.problema = value;
    }

    /**
     * Obtém o valor da propriedade publicoAlvo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicoAlvo() {
        return publicoAlvo;
    }

    /**
     * Define o valor da propriedade publicoAlvo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicoAlvo(String value) {
        this.publicoAlvo = value;
    }

    /**
     * Obtém o valor da propriedade snExclusaoLogica.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnExclusaoLogica() {
        return snExclusaoLogica;
    }

    /**
     * Define o valor da propriedade snExclusaoLogica.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnExclusaoLogica(Boolean value) {
        this.snExclusaoLogica = value;
    }

    /**
     * Obtém o valor da propriedade titulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o valor da propriedade titulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

    /**
     * Obtém o valor da propriedade unidadeResponsavel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadeResponsavel() {
        return unidadeResponsavel;
    }

    /**
     * Define o valor da propriedade unidadeResponsavel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadeResponsavel(String value) {
        this.unidadeResponsavel = value;
    }

}
