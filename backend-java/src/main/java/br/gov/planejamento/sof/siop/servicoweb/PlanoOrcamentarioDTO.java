
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de planoOrcamentarioDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="planoOrcamentarioDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoIndicadorPlanoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoProduto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoUnidadeMedida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataHoraAlteracao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="detalhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="identificadorUnico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="identificadorUnicoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="planoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snAtual" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "planoOrcamentarioDTO", propOrder = {
    "codigoIndicadorPlanoOrcamentario",
    "codigoMomento",
    "codigoProduto",
    "codigoUnidadeMedida",
    "dataHoraAlteracao",
    "detalhamento",
    "exercicio",
    "identificadorUnico",
    "identificadorUnicoAcao",
    "planoOrcamentario",
    "snAtual",
    "titulo"
})
public class PlanoOrcamentarioDTO
    extends BaseDTO
{

    protected String codigoIndicadorPlanoOrcamentario;
    protected Integer codigoMomento;
    protected Integer codigoProduto;
    protected String codigoUnidadeMedida;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraAlteracao;
    protected String detalhamento;
    protected Integer exercicio;
    protected Integer identificadorUnico;
    protected Integer identificadorUnicoAcao;
    protected String planoOrcamentario;
    protected Boolean snAtual;
    protected String titulo;

    /**
     * Obtém o valor da propriedade codigoIndicadorPlanoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoIndicadorPlanoOrcamentario() {
        return codigoIndicadorPlanoOrcamentario;
    }

    /**
     * Define o valor da propriedade codigoIndicadorPlanoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoIndicadorPlanoOrcamentario(String value) {
        this.codigoIndicadorPlanoOrcamentario = value;
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
     * Obtém o valor da propriedade codigoProduto.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoProduto() {
        return codigoProduto;
    }

    /**
     * Define o valor da propriedade codigoProduto.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoProduto(Integer value) {
        this.codigoProduto = value;
    }

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
     * Obtém o valor da propriedade detalhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalhamento() {
        return detalhamento;
    }

    /**
     * Define o valor da propriedade detalhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalhamento(String value) {
        this.detalhamento = value;
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
     * Obtém o valor da propriedade identificadorUnicoAcao.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorUnicoAcao() {
        return identificadorUnicoAcao;
    }

    /**
     * Define o valor da propriedade identificadorUnicoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorUnicoAcao(Integer value) {
        this.identificadorUnicoAcao = value;
    }

    /**
     * Obtém o valor da propriedade planoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanoOrcamentario() {
        return planoOrcamentario;
    }

    /**
     * Define o valor da propriedade planoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanoOrcamentario(String value) {
        this.planoOrcamentario = value;
    }

    /**
     * Obtém o valor da propriedade snAtual.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAtual() {
        return snAtual;
    }

    /**
     * Define o valor da propriedade snAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAtual(Boolean value) {
        this.snAtual = value;
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

}
