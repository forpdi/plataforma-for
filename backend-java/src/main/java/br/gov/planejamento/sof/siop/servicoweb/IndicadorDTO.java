
package br.gov.planejamento.sof.siop.servicoweb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de indicadorDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="indicadorDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoBaseGeografica" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoIndicador" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoPeriodicidade" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoUnidadeMedidaIndicador" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="dataApuracao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formula" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorUnico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="snApuracaoReferencia" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snExclusaoLogica" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="valorReferencia" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "indicadorDTO", propOrder = {
    "codigoBaseGeografica",
    "codigoIndicador",
    "codigoMomento",
    "codigoPeriodicidade",
    "codigoPrograma",
    "codigoUnidadeMedidaIndicador",
    "dataApuracao",
    "descricao",
    "exercicio",
    "fonte",
    "formula",
    "identificadorUnico",
    "snApuracaoReferencia",
    "snExclusaoLogica",
    "valorReferencia"
})
public class IndicadorDTO
    extends BaseDTO
{

    protected Integer codigoBaseGeografica;
    protected Integer codigoIndicador;
    protected Integer codigoMomento;
    protected Integer codigoPeriodicidade;
    protected String codigoPrograma;
    protected Integer codigoUnidadeMedidaIndicador;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataApuracao;
    protected String descricao;
    protected Integer exercicio;
    protected String fonte;
    protected String formula;
    protected Integer identificadorUnico;
    protected Boolean snApuracaoReferencia;
    protected Boolean snExclusaoLogica;
    protected BigDecimal valorReferencia;

    /**
     * Obtém o valor da propriedade codigoBaseGeografica.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoBaseGeografica() {
        return codigoBaseGeografica;
    }

    /**
     * Define o valor da propriedade codigoBaseGeografica.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoBaseGeografica(Integer value) {
        this.codigoBaseGeografica = value;
    }

    /**
     * Obtém o valor da propriedade codigoIndicador.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoIndicador() {
        return codigoIndicador;
    }

    /**
     * Define o valor da propriedade codigoIndicador.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoIndicador(Integer value) {
        this.codigoIndicador = value;
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
     * Obtém o valor da propriedade codigoPeriodicidade.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoPeriodicidade() {
        return codigoPeriodicidade;
    }

    /**
     * Define o valor da propriedade codigoPeriodicidade.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoPeriodicidade(Integer value) {
        this.codigoPeriodicidade = value;
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
     * Obtém o valor da propriedade codigoUnidadeMedidaIndicador.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoUnidadeMedidaIndicador() {
        return codigoUnidadeMedidaIndicador;
    }

    /**
     * Define o valor da propriedade codigoUnidadeMedidaIndicador.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoUnidadeMedidaIndicador(Integer value) {
        this.codigoUnidadeMedidaIndicador = value;
    }

    /**
     * Obtém o valor da propriedade dataApuracao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApuracao() {
        return dataApuracao;
    }

    /**
     * Define o valor da propriedade dataApuracao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApuracao(XMLGregorianCalendar value) {
        this.dataApuracao = value;
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
     * Obtém o valor da propriedade fonte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFonte() {
        return fonte;
    }

    /**
     * Define o valor da propriedade fonte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFonte(String value) {
        this.fonte = value;
    }

    /**
     * Obtém o valor da propriedade formula.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Define o valor da propriedade formula.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormula(String value) {
        this.formula = value;
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
     * Obtém o valor da propriedade snApuracaoReferencia.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnApuracaoReferencia() {
        return snApuracaoReferencia;
    }

    /**
     * Define o valor da propriedade snApuracaoReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnApuracaoReferencia(Boolean value) {
        this.snApuracaoReferencia = value;
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
     * Obtém o valor da propriedade valorReferencia.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorReferencia() {
        return valorReferencia;
    }

    /**
     * Define o valor da propriedade valorReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorReferencia(BigDecimal value) {
        this.valorReferencia = value;
    }

}
