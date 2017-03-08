
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de acaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="acaoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificadorUnico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoProduto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoTipoInclusaoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="baseLegal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="finalidade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeResponsavel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detalhamentoImplementacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formaAcompanhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificacaoSazonalidade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insumosUtilizados" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoObjetivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoIniciativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoSubFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoEsfera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoTipoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoUnidadeMedida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="especificacaoProduto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="beneficiario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snDireta" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snDescentralizada" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snLinhaCredito" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snTransferenciaObrigatoria" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snTransferenciaVoluntaria" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snExclusaoLogica" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snRegionalizarNaExecucao" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snAquisicaoInsumoEstrategico" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snParticipacaoSocial" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snAcompanhamentoOpcional" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="localizadores">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="localizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}localizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acaoDTO", propOrder = {
    "identificadorUnico",
    "exercicio",
    "codigoMomento",
    "codigoProduto",
    "codigoTipoInclusaoAcao",
    "codigoAcao",
    "titulo",
    "baseLegal",
    "finalidade",
    "descricao",
    "unidadeResponsavel",
    "detalhamentoImplementacao",
    "formaAcompanhamento",
    "identificacaoSazonalidade",
    "insumosUtilizados",
    "codigoPrograma",
    "codigoObjetivo",
    "codigoIniciativa",
    "codigoFuncao",
    "codigoSubFuncao",
    "codigoOrgao",
    "codigoEsfera",
    "codigoTipoAcao",
    "codigoUnidadeMedida",
    "especificacaoProduto",
    "beneficiario",
    "snDireta",
    "snDescentralizada",
    "snLinhaCredito",
    "snTransferenciaObrigatoria",
    "snTransferenciaVoluntaria",
    "snExclusaoLogica",
    "snRegionalizarNaExecucao",
    "snAquisicaoInsumoEstrategico",
    "snParticipacaoSocial",
    "snAcompanhamentoOpcional",
    "localizadores"
})
public class AcaoDTO {

    protected Integer identificadorUnico;
    protected Integer exercicio;
    protected Integer codigoMomento;
    protected Integer codigoProduto;
    protected Integer codigoTipoInclusaoAcao;
    protected String codigoAcao;
    protected String titulo;
    protected String baseLegal;
    protected String finalidade;
    protected String descricao;
    protected String unidadeResponsavel;
    protected String detalhamentoImplementacao;
    protected String formaAcompanhamento;
    protected String identificacaoSazonalidade;
    protected String insumosUtilizados;
    protected String codigoPrograma;
    protected String codigoObjetivo;
    protected String codigoIniciativa;
    protected String codigoFuncao;
    protected String codigoSubFuncao;
    protected String codigoOrgao;
    protected String codigoEsfera;
    protected String codigoTipoAcao;
    protected String codigoUnidadeMedida;
    protected String especificacaoProduto;
    protected String beneficiario;
    protected Boolean snDireta;
    protected Boolean snDescentralizada;
    protected Boolean snLinhaCredito;
    protected Boolean snTransferenciaObrigatoria;
    protected Boolean snTransferenciaVoluntaria;
    protected Boolean snExclusaoLogica;
    protected Boolean snRegionalizarNaExecucao;
    protected Boolean snAquisicaoInsumoEstrategico;
    protected Boolean snParticipacaoSocial;
    protected Boolean snAcompanhamentoOpcional;
    @XmlElement(required = true)
    protected AcaoDTO.Localizadores localizadores;

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
     * Obtém o valor da propriedade codigoTipoInclusaoAcao.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoTipoInclusaoAcao() {
        return codigoTipoInclusaoAcao;
    }

    /**
     * Define o valor da propriedade codigoTipoInclusaoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoTipoInclusaoAcao(Integer value) {
        this.codigoTipoInclusaoAcao = value;
    }

    /**
     * Obtém o valor da propriedade codigoAcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAcao() {
        return codigoAcao;
    }

    /**
     * Define o valor da propriedade codigoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAcao(String value) {
        this.codigoAcao = value;
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
     * Obtém o valor da propriedade baseLegal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseLegal() {
        return baseLegal;
    }

    /**
     * Define o valor da propriedade baseLegal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseLegal(String value) {
        this.baseLegal = value;
    }

    /**
     * Obtém o valor da propriedade finalidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalidade() {
        return finalidade;
    }

    /**
     * Define o valor da propriedade finalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalidade(String value) {
        this.finalidade = value;
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

    /**
     * Obtém o valor da propriedade detalhamentoImplementacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalhamentoImplementacao() {
        return detalhamentoImplementacao;
    }

    /**
     * Define o valor da propriedade detalhamentoImplementacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalhamentoImplementacao(String value) {
        this.detalhamentoImplementacao = value;
    }

    /**
     * Obtém o valor da propriedade formaAcompanhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormaAcompanhamento() {
        return formaAcompanhamento;
    }

    /**
     * Define o valor da propriedade formaAcompanhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormaAcompanhamento(String value) {
        this.formaAcompanhamento = value;
    }

    /**
     * Obtém o valor da propriedade identificacaoSazonalidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacaoSazonalidade() {
        return identificacaoSazonalidade;
    }

    /**
     * Define o valor da propriedade identificacaoSazonalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacaoSazonalidade(String value) {
        this.identificacaoSazonalidade = value;
    }

    /**
     * Obtém o valor da propriedade insumosUtilizados.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsumosUtilizados() {
        return insumosUtilizados;
    }

    /**
     * Define o valor da propriedade insumosUtilizados.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsumosUtilizados(String value) {
        this.insumosUtilizados = value;
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
     * Obtém o valor da propriedade codigoObjetivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoObjetivo() {
        return codigoObjetivo;
    }

    /**
     * Define o valor da propriedade codigoObjetivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoObjetivo(String value) {
        this.codigoObjetivo = value;
    }

    /**
     * Obtém o valor da propriedade codigoIniciativa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoIniciativa() {
        return codigoIniciativa;
    }

    /**
     * Define o valor da propriedade codigoIniciativa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoIniciativa(String value) {
        this.codigoIniciativa = value;
    }

    /**
     * Obtém o valor da propriedade codigoFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoFuncao() {
        return codigoFuncao;
    }

    /**
     * Define o valor da propriedade codigoFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoFuncao(String value) {
        this.codigoFuncao = value;
    }

    /**
     * Obtém o valor da propriedade codigoSubFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSubFuncao() {
        return codigoSubFuncao;
    }

    /**
     * Define o valor da propriedade codigoSubFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSubFuncao(String value) {
        this.codigoSubFuncao = value;
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
     * Obtém o valor da propriedade codigoEsfera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoEsfera() {
        return codigoEsfera;
    }

    /**
     * Define o valor da propriedade codigoEsfera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoEsfera(String value) {
        this.codigoEsfera = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoAcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTipoAcao() {
        return codigoTipoAcao;
    }

    /**
     * Define o valor da propriedade codigoTipoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTipoAcao(String value) {
        this.codigoTipoAcao = value;
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
     * Obtém o valor da propriedade especificacaoProduto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEspecificacaoProduto() {
        return especificacaoProduto;
    }

    /**
     * Define o valor da propriedade especificacaoProduto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEspecificacaoProduto(String value) {
        this.especificacaoProduto = value;
    }

    /**
     * Obtém o valor da propriedade beneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiario() {
        return beneficiario;
    }

    /**
     * Define o valor da propriedade beneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiario(String value) {
        this.beneficiario = value;
    }

    /**
     * Obtém o valor da propriedade snDireta.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnDireta() {
        return snDireta;
    }

    /**
     * Define o valor da propriedade snDireta.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnDireta(Boolean value) {
        this.snDireta = value;
    }

    /**
     * Obtém o valor da propriedade snDescentralizada.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnDescentralizada() {
        return snDescentralizada;
    }

    /**
     * Define o valor da propriedade snDescentralizada.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnDescentralizada(Boolean value) {
        this.snDescentralizada = value;
    }

    /**
     * Obtém o valor da propriedade snLinhaCredito.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnLinhaCredito() {
        return snLinhaCredito;
    }

    /**
     * Define o valor da propriedade snLinhaCredito.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnLinhaCredito(Boolean value) {
        this.snLinhaCredito = value;
    }

    /**
     * Obtém o valor da propriedade snTransferenciaObrigatoria.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnTransferenciaObrigatoria() {
        return snTransferenciaObrigatoria;
    }

    /**
     * Define o valor da propriedade snTransferenciaObrigatoria.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnTransferenciaObrigatoria(Boolean value) {
        this.snTransferenciaObrigatoria = value;
    }

    /**
     * Obtém o valor da propriedade snTransferenciaVoluntaria.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnTransferenciaVoluntaria() {
        return snTransferenciaVoluntaria;
    }

    /**
     * Define o valor da propriedade snTransferenciaVoluntaria.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnTransferenciaVoluntaria(Boolean value) {
        this.snTransferenciaVoluntaria = value;
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
     * Obtém o valor da propriedade snRegionalizarNaExecucao.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnRegionalizarNaExecucao() {
        return snRegionalizarNaExecucao;
    }

    /**
     * Define o valor da propriedade snRegionalizarNaExecucao.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnRegionalizarNaExecucao(Boolean value) {
        this.snRegionalizarNaExecucao = value;
    }

    /**
     * Obtém o valor da propriedade snAquisicaoInsumoEstrategico.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAquisicaoInsumoEstrategico() {
        return snAquisicaoInsumoEstrategico;
    }

    /**
     * Define o valor da propriedade snAquisicaoInsumoEstrategico.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAquisicaoInsumoEstrategico(Boolean value) {
        this.snAquisicaoInsumoEstrategico = value;
    }

    /**
     * Obtém o valor da propriedade snParticipacaoSocial.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnParticipacaoSocial() {
        return snParticipacaoSocial;
    }

    /**
     * Define o valor da propriedade snParticipacaoSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnParticipacaoSocial(Boolean value) {
        this.snParticipacaoSocial = value;
    }

    /**
     * Obtém o valor da propriedade snAcompanhamentoOpcional.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAcompanhamentoOpcional() {
        return snAcompanhamentoOpcional;
    }

    /**
     * Define o valor da propriedade snAcompanhamentoOpcional.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAcompanhamentoOpcional(Boolean value) {
        this.snAcompanhamentoOpcional = value;
    }

    /**
     * Obtém o valor da propriedade localizadores.
     * 
     * @return
     *     possible object is
     *     {@link AcaoDTO.Localizadores }
     *     
     */
    public AcaoDTO.Localizadores getLocalizadores() {
        return localizadores;
    }

    /**
     * Define o valor da propriedade localizadores.
     * 
     * @param value
     *     allowed object is
     *     {@link AcaoDTO.Localizadores }
     *     
     */
    public void setLocalizadores(AcaoDTO.Localizadores value) {
        this.localizadores = value;
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
     *         &lt;element name="localizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}localizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "localizador"
    })
    public static class Localizadores {

        protected List<LocalizadorDTO> localizador;

        /**
         * Gets the value of the localizador property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the localizador property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLocalizador().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link LocalizadorDTO }
         * 
         * 
         */
        public List<LocalizadorDTO> getLocalizador() {
            if (localizador == null) {
                localizador = new ArrayList<LocalizadorDTO>();
            }
            return this.localizador;
        }

    }

}
