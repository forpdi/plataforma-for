
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de acompanhamentoOrcamentarioLocalizadorDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="acompanhamentoOrcamentarioLocalizadorDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigoTipoInclusaoLocalizador" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="meta" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="reprogramado" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="realizadoLOA" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="dataApuracaoLOA" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="dotacaoAtual" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="limite" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="empenhado" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="liquidado" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="realizadoRAP" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="dataApuracaoRAP" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="rapInscritoLiquido" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="rapLiquidadoAPagar" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="rapPago" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="justificativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="analisesLocalizador">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="analiseLocalizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}analiseAcompanhamentoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="comentariosRegionalizacao" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="comentarioRegionalizacao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}analiseAcompanhamentoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="acompanhamentosPlanoOrcamentario" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="acompanhamentoPlanoOrcamentario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoPlanoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acompanhamentoOrcamentarioLocalizadorDTO", propOrder = {
    "localizador",
    "codigoTipoInclusaoLocalizador",
    "meta",
    "reprogramado",
    "realizadoLOA",
    "dataApuracaoLOA",
    "dotacaoAtual",
    "limite",
    "empenhado",
    "liquidado",
    "realizadoRAP",
    "dataApuracaoRAP",
    "rapInscritoLiquido",
    "rapLiquidadoAPagar",
    "rapPago",
    "justificativa",
    "analisesLocalizador",
    "comentariosRegionalizacao",
    "acompanhamentosPlanoOrcamentario"
})
public class AcompanhamentoOrcamentarioLocalizadorDTO
    extends BaseDTO
{

    @XmlElement(required = true)
    protected String localizador;
    protected int codigoTipoInclusaoLocalizador;
    protected Long meta;
    protected long reprogramado;
    protected long realizadoLOA;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataApuracaoLOA;
    protected Long dotacaoAtual;
    protected long limite;
    protected Double empenhado;
    protected Double liquidado;
    protected long realizadoRAP;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataApuracaoRAP;
    protected Double rapInscritoLiquido;
    protected Double rapLiquidadoAPagar;
    protected Double rapPago;
    protected String justificativa;
    @XmlElement(required = true)
    protected AcompanhamentoOrcamentarioLocalizadorDTO.AnalisesLocalizador analisesLocalizador;
    protected AcompanhamentoOrcamentarioLocalizadorDTO.ComentariosRegionalizacao comentariosRegionalizacao;
    protected AcompanhamentoOrcamentarioLocalizadorDTO.AcompanhamentosPlanoOrcamentario acompanhamentosPlanoOrcamentario;

    /**
     * Obtém o valor da propriedade localizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalizador() {
        return localizador;
    }

    /**
     * Define o valor da propriedade localizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalizador(String value) {
        this.localizador = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoInclusaoLocalizador.
     * 
     */
    public int getCodigoTipoInclusaoLocalizador() {
        return codigoTipoInclusaoLocalizador;
    }

    /**
     * Define o valor da propriedade codigoTipoInclusaoLocalizador.
     * 
     */
    public void setCodigoTipoInclusaoLocalizador(int value) {
        this.codigoTipoInclusaoLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade meta.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMeta() {
        return meta;
    }

    /**
     * Define o valor da propriedade meta.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMeta(Long value) {
        this.meta = value;
    }

    /**
     * Obtém o valor da propriedade reprogramado.
     * 
     */
    public long getReprogramado() {
        return reprogramado;
    }

    /**
     * Define o valor da propriedade reprogramado.
     * 
     */
    public void setReprogramado(long value) {
        this.reprogramado = value;
    }

    /**
     * Obtém o valor da propriedade realizadoLOA.
     * 
     */
    public long getRealizadoLOA() {
        return realizadoLOA;
    }

    /**
     * Define o valor da propriedade realizadoLOA.
     * 
     */
    public void setRealizadoLOA(long value) {
        this.realizadoLOA = value;
    }

    /**
     * Obtém o valor da propriedade dataApuracaoLOA.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApuracaoLOA() {
        return dataApuracaoLOA;
    }

    /**
     * Define o valor da propriedade dataApuracaoLOA.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApuracaoLOA(XMLGregorianCalendar value) {
        this.dataApuracaoLOA = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoAtual.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDotacaoAtual() {
        return dotacaoAtual;
    }

    /**
     * Define o valor da propriedade dotacaoAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDotacaoAtual(Long value) {
        this.dotacaoAtual = value;
    }

    /**
     * Obtém o valor da propriedade limite.
     * 
     */
    public long getLimite() {
        return limite;
    }

    /**
     * Define o valor da propriedade limite.
     * 
     */
    public void setLimite(long value) {
        this.limite = value;
    }

    /**
     * Obtém o valor da propriedade empenhado.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getEmpenhado() {
        return empenhado;
    }

    /**
     * Define o valor da propriedade empenhado.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setEmpenhado(Double value) {
        this.empenhado = value;
    }

    /**
     * Obtém o valor da propriedade liquidado.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLiquidado() {
        return liquidado;
    }

    /**
     * Define o valor da propriedade liquidado.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLiquidado(Double value) {
        this.liquidado = value;
    }

    /**
     * Obtém o valor da propriedade realizadoRAP.
     * 
     */
    public long getRealizadoRAP() {
        return realizadoRAP;
    }

    /**
     * Define o valor da propriedade realizadoRAP.
     * 
     */
    public void setRealizadoRAP(long value) {
        this.realizadoRAP = value;
    }

    /**
     * Obtém o valor da propriedade dataApuracaoRAP.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApuracaoRAP() {
        return dataApuracaoRAP;
    }

    /**
     * Define o valor da propriedade dataApuracaoRAP.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApuracaoRAP(XMLGregorianCalendar value) {
        this.dataApuracaoRAP = value;
    }

    /**
     * Obtém o valor da propriedade rapInscritoLiquido.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRapInscritoLiquido() {
        return rapInscritoLiquido;
    }

    /**
     * Define o valor da propriedade rapInscritoLiquido.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRapInscritoLiquido(Double value) {
        this.rapInscritoLiquido = value;
    }

    /**
     * Obtém o valor da propriedade rapLiquidadoAPagar.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRapLiquidadoAPagar() {
        return rapLiquidadoAPagar;
    }

    /**
     * Define o valor da propriedade rapLiquidadoAPagar.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRapLiquidadoAPagar(Double value) {
        this.rapLiquidadoAPagar = value;
    }

    /**
     * Obtém o valor da propriedade rapPago.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRapPago() {
        return rapPago;
    }

    /**
     * Define o valor da propriedade rapPago.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRapPago(Double value) {
        this.rapPago = value;
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
     * Obtém o valor da propriedade analisesLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link AcompanhamentoOrcamentarioLocalizadorDTO.AnalisesLocalizador }
     *     
     */
    public AcompanhamentoOrcamentarioLocalizadorDTO.AnalisesLocalizador getAnalisesLocalizador() {
        return analisesLocalizador;
    }

    /**
     * Define o valor da propriedade analisesLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link AcompanhamentoOrcamentarioLocalizadorDTO.AnalisesLocalizador }
     *     
     */
    public void setAnalisesLocalizador(AcompanhamentoOrcamentarioLocalizadorDTO.AnalisesLocalizador value) {
        this.analisesLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade comentariosRegionalizacao.
     * 
     * @return
     *     possible object is
     *     {@link AcompanhamentoOrcamentarioLocalizadorDTO.ComentariosRegionalizacao }
     *     
     */
    public AcompanhamentoOrcamentarioLocalizadorDTO.ComentariosRegionalizacao getComentariosRegionalizacao() {
        return comentariosRegionalizacao;
    }

    /**
     * Define o valor da propriedade comentariosRegionalizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link AcompanhamentoOrcamentarioLocalizadorDTO.ComentariosRegionalizacao }
     *     
     */
    public void setComentariosRegionalizacao(AcompanhamentoOrcamentarioLocalizadorDTO.ComentariosRegionalizacao value) {
        this.comentariosRegionalizacao = value;
    }

    /**
     * Obtém o valor da propriedade acompanhamentosPlanoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link AcompanhamentoOrcamentarioLocalizadorDTO.AcompanhamentosPlanoOrcamentario }
     *     
     */
    public AcompanhamentoOrcamentarioLocalizadorDTO.AcompanhamentosPlanoOrcamentario getAcompanhamentosPlanoOrcamentario() {
        return acompanhamentosPlanoOrcamentario;
    }

    /**
     * Define o valor da propriedade acompanhamentosPlanoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link AcompanhamentoOrcamentarioLocalizadorDTO.AcompanhamentosPlanoOrcamentario }
     *     
     */
    public void setAcompanhamentosPlanoOrcamentario(AcompanhamentoOrcamentarioLocalizadorDTO.AcompanhamentosPlanoOrcamentario value) {
        this.acompanhamentosPlanoOrcamentario = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="acompanhamentoPlanoOrcamentario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoPlanoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "acompanhamentoPlanoOrcamentario"
    })
    public static class AcompanhamentosPlanoOrcamentario {

        protected List<AcompanhamentoPlanoOrcamentarioDTO> acompanhamentoPlanoOrcamentario;

        /**
         * Gets the value of the acompanhamentoPlanoOrcamentario property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acompanhamentoPlanoOrcamentario property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcompanhamentoPlanoOrcamentario().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AcompanhamentoPlanoOrcamentarioDTO }
         * 
         * 
         */
        public List<AcompanhamentoPlanoOrcamentarioDTO> getAcompanhamentoPlanoOrcamentario() {
            if (acompanhamentoPlanoOrcamentario == null) {
                acompanhamentoPlanoOrcamentario = new ArrayList<AcompanhamentoPlanoOrcamentarioDTO>();
            }
            return this.acompanhamentoPlanoOrcamentario;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="analiseLocalizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}analiseAcompanhamentoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "analiseLocalizador"
    })
    public static class AnalisesLocalizador {

        protected List<AnaliseAcompanhamentoOrcamentarioDTO> analiseLocalizador;

        /**
         * Gets the value of the analiseLocalizador property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the analiseLocalizador property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnaliseLocalizador().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AnaliseAcompanhamentoOrcamentarioDTO }
         * 
         * 
         */
        public List<AnaliseAcompanhamentoOrcamentarioDTO> getAnaliseLocalizador() {
            if (analiseLocalizador == null) {
                analiseLocalizador = new ArrayList<AnaliseAcompanhamentoOrcamentarioDTO>();
            }
            return this.analiseLocalizador;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="comentarioRegionalizacao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}analiseAcompanhamentoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "comentarioRegionalizacao"
    })
    public static class ComentariosRegionalizacao {

        protected List<AnaliseAcompanhamentoOrcamentarioDTO> comentarioRegionalizacao;

        /**
         * Gets the value of the comentarioRegionalizacao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the comentarioRegionalizacao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getComentarioRegionalizacao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AnaliseAcompanhamentoOrcamentarioDTO }
         * 
         * 
         */
        public List<AnaliseAcompanhamentoOrcamentarioDTO> getComentarioRegionalizacao() {
            if (comentarioRegionalizacao == null) {
                comentarioRegionalizacao = new ArrayList<AnaliseAcompanhamentoOrcamentarioDTO>();
            }
            return this.comentarioRegionalizacao;
        }

    }

}
