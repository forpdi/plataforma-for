
package br.gov.planejamento.sof.siop.servicoweb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoOrcamentariaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoOrcamentariaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acompanhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="anoExercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="anoReferencia" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="autorizado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="bloqueadoRemanejamento" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="bloqueadoSOF" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="categoriaEconomica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="credito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="creditoContidoSOF" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="detalheAcompanhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detalheAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disponivel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotAntecipada" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotAtual" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotInicial" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotInicialSiafi" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotacaoAntecipada" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotacaoInicial" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotacaoOriginal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="elementoDespesa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="empALiquidar" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="empLiqInscrRapNp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="empLiquidado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="empenhadoALiquidar" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="executadoPorInscricaoDeRAP" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="grupoNaturezaDespesa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorAcompanhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indisponivel" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mes" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="modalidadeAplicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="natureza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroptres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pago" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="planoInterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="planoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="projetoLei" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapAPagarNaoProcessado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapAPagarProcessado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapCanceladosNaoProcessados" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapCanceladosProcessados" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapExerciciosAnteriores" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapInscritoNaoProcessado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapInscritoProcessado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapNaoProcessadoALiquidar" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapNaoProcessadoBloqueado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapNaoProcessadoLiquidadoAPagar" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapPagoNaoProcessado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rapPagoProcessado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="resultadoPrimarioAtual" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultadoPrimarioLei" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rpAtual" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rpLei" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subElementoDespesa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tematica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tematicaPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoApropriacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoApropriacaoPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoCredito" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeGestoraResponsavel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoOrcamentariaDTO", propOrder = {
    "acao",
    "acompanhamento",
    "acompanhamentoPO",
    "anoExercicio",
    "anoReferencia",
    "autorizado",
    "bloqueadoRemanejamento",
    "bloqueadoSOF",
    "categoriaEconomica",
    "credito",
    "creditoContidoSOF",
    "detalheAcompanhamento",
    "detalheAcompanhamentoPO",
    "disponivel",
    "dotAntecipada",
    "dotAtual",
    "dotInicial",
    "dotInicialSiafi",
    "dotacaoAntecipada",
    "dotacaoInicial",
    "dotacaoOriginal",
    "elementoDespesa",
    "empALiquidar",
    "empLiqInscrRapNp",
    "empLiquidado",
    "empenhadoALiquidar",
    "esfera",
    "executadoPorInscricaoDeRAP",
    "fonte",
    "funcao",
    "grupoNaturezaDespesa",
    "idOc",
    "idUso",
    "identificadorAcompanhamento",
    "identificadorAcompanhamentoPO",
    "indisponivel",
    "localizador",
    "mes",
    "modalidadeAplicacao",
    "natureza",
    "numeroptres",
    "origem",
    "pago",
    "planoInterno",
    "planoOrcamentario",
    "programa",
    "projetoLei",
    "rapAPagarNaoProcessado",
    "rapAPagarProcessado",
    "rapCanceladosNaoProcessados",
    "rapCanceladosProcessados",
    "rapExerciciosAnteriores",
    "rapInscritoNaoProcessado",
    "rapInscritoProcessado",
    "rapNaoProcessadoALiquidar",
    "rapNaoProcessadoBloqueado",
    "rapNaoProcessadoLiquidadoAPagar",
    "rapPagoNaoProcessado",
    "rapPagoProcessado",
    "resultadoPrimarioAtual",
    "resultadoPrimarioLei",
    "rpAtual",
    "rpLei",
    "subElementoDespesa",
    "subFuncao",
    "tematica",
    "tematicaPO",
    "tipoApropriacao",
    "tipoApropriacaoPO",
    "tipoCredito",
    "unidadeGestoraResponsavel",
    "unidadeOrcamentaria"
})
public class ExecucaoOrcamentariaDTO
    extends BaseDTO
{

    protected String acao;
    protected String acompanhamento;
    protected String acompanhamentoPO;
    protected Integer anoExercicio;
    protected Integer anoReferencia;
    protected BigDecimal autorizado;
    protected BigDecimal bloqueadoRemanejamento;
    protected BigDecimal bloqueadoSOF;
    protected String categoriaEconomica;
    protected String credito;
    protected BigDecimal creditoContidoSOF;
    protected String detalheAcompanhamento;
    protected String detalheAcompanhamentoPO;
    protected BigDecimal disponivel;
    protected BigDecimal dotAntecipada;
    protected BigDecimal dotAtual;
    protected BigDecimal dotInicial;
    protected BigDecimal dotInicialSiafi;
    protected BigDecimal dotacaoAntecipada;
    protected BigDecimal dotacaoInicial;
    protected BigDecimal dotacaoOriginal;
    protected String elementoDespesa;
    protected BigDecimal empALiquidar;
    protected String empLiqInscrRapNp;
    protected BigDecimal empLiquidado;
    protected BigDecimal empenhadoALiquidar;
    protected String esfera;
    protected BigDecimal executadoPorInscricaoDeRAP;
    protected String fonte;
    protected String funcao;
    protected String grupoNaturezaDespesa;
    protected String idOc;
    protected String idUso;
    protected String identificadorAcompanhamento;
    protected String identificadorAcompanhamentoPO;
    protected BigDecimal indisponivel;
    protected String localizador;
    protected Integer mes;
    protected String modalidadeAplicacao;
    protected String natureza;
    protected String numeroptres;
    protected String origem;
    protected BigDecimal pago;
    protected String planoInterno;
    protected String planoOrcamentario;
    protected String programa;
    protected BigDecimal projetoLei;
    protected BigDecimal rapAPagarNaoProcessado;
    protected BigDecimal rapAPagarProcessado;
    protected BigDecimal rapCanceladosNaoProcessados;
    protected BigDecimal rapCanceladosProcessados;
    protected BigDecimal rapExerciciosAnteriores;
    protected BigDecimal rapInscritoNaoProcessado;
    protected BigDecimal rapInscritoProcessado;
    protected BigDecimal rapNaoProcessadoALiquidar;
    protected BigDecimal rapNaoProcessadoBloqueado;
    protected BigDecimal rapNaoProcessadoLiquidadoAPagar;
    protected BigDecimal rapPagoNaoProcessado;
    protected BigDecimal rapPagoProcessado;
    protected String resultadoPrimarioAtual;
    protected String resultadoPrimarioLei;
    protected String rpAtual;
    protected String rpLei;
    protected String subElementoDespesa;
    protected String subFuncao;
    protected String tematica;
    protected String tematicaPO;
    protected String tipoApropriacao;
    protected String tipoApropriacaoPO;
    protected String tipoCredito;
    protected String unidadeGestoraResponsavel;
    protected String unidadeOrcamentaria;

    /**
     * Obtém o valor da propriedade acao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcao() {
        return acao;
    }

    /**
     * Define o valor da propriedade acao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcao(String value) {
        this.acao = value;
    }

    /**
     * Obtém o valor da propriedade acompanhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcompanhamento() {
        return acompanhamento;
    }

    /**
     * Define o valor da propriedade acompanhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcompanhamento(String value) {
        this.acompanhamento = value;
    }

    /**
     * Obtém o valor da propriedade acompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcompanhamentoPO() {
        return acompanhamentoPO;
    }

    /**
     * Define o valor da propriedade acompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcompanhamentoPO(String value) {
        this.acompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade anoExercicio.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnoExercicio() {
        return anoExercicio;
    }

    /**
     * Define o valor da propriedade anoExercicio.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnoExercicio(Integer value) {
        this.anoExercicio = value;
    }

    /**
     * Obtém o valor da propriedade anoReferencia.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnoReferencia() {
        return anoReferencia;
    }

    /**
     * Define o valor da propriedade anoReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnoReferencia(Integer value) {
        this.anoReferencia = value;
    }

    /**
     * Obtém o valor da propriedade autorizado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAutorizado() {
        return autorizado;
    }

    /**
     * Define o valor da propriedade autorizado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAutorizado(BigDecimal value) {
        this.autorizado = value;
    }

    /**
     * Obtém o valor da propriedade bloqueadoRemanejamento.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBloqueadoRemanejamento() {
        return bloqueadoRemanejamento;
    }

    /**
     * Define o valor da propriedade bloqueadoRemanejamento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBloqueadoRemanejamento(BigDecimal value) {
        this.bloqueadoRemanejamento = value;
    }

    /**
     * Obtém o valor da propriedade bloqueadoSOF.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBloqueadoSOF() {
        return bloqueadoSOF;
    }

    /**
     * Define o valor da propriedade bloqueadoSOF.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBloqueadoSOF(BigDecimal value) {
        this.bloqueadoSOF = value;
    }

    /**
     * Obtém o valor da propriedade categoriaEconomica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoriaEconomica() {
        return categoriaEconomica;
    }

    /**
     * Define o valor da propriedade categoriaEconomica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoriaEconomica(String value) {
        this.categoriaEconomica = value;
    }

    /**
     * Obtém o valor da propriedade credito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredito() {
        return credito;
    }

    /**
     * Define o valor da propriedade credito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredito(String value) {
        this.credito = value;
    }

    /**
     * Obtém o valor da propriedade creditoContidoSOF.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCreditoContidoSOF() {
        return creditoContidoSOF;
    }

    /**
     * Define o valor da propriedade creditoContidoSOF.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCreditoContidoSOF(BigDecimal value) {
        this.creditoContidoSOF = value;
    }

    /**
     * Obtém o valor da propriedade detalheAcompanhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalheAcompanhamento() {
        return detalheAcompanhamento;
    }

    /**
     * Define o valor da propriedade detalheAcompanhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalheAcompanhamento(String value) {
        this.detalheAcompanhamento = value;
    }

    /**
     * Obtém o valor da propriedade detalheAcompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalheAcompanhamentoPO() {
        return detalheAcompanhamentoPO;
    }

    /**
     * Define o valor da propriedade detalheAcompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalheAcompanhamentoPO(String value) {
        this.detalheAcompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade disponivel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDisponivel() {
        return disponivel;
    }

    /**
     * Define o valor da propriedade disponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDisponivel(BigDecimal value) {
        this.disponivel = value;
    }

    /**
     * Obtém o valor da propriedade dotAntecipada.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotAntecipada() {
        return dotAntecipada;
    }

    /**
     * Define o valor da propriedade dotAntecipada.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotAntecipada(BigDecimal value) {
        this.dotAntecipada = value;
    }

    /**
     * Obtém o valor da propriedade dotAtual.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotAtual() {
        return dotAtual;
    }

    /**
     * Define o valor da propriedade dotAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotAtual(BigDecimal value) {
        this.dotAtual = value;
    }

    /**
     * Obtém o valor da propriedade dotInicial.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotInicial() {
        return dotInicial;
    }

    /**
     * Define o valor da propriedade dotInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotInicial(BigDecimal value) {
        this.dotInicial = value;
    }

    /**
     * Obtém o valor da propriedade dotInicialSiafi.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotInicialSiafi() {
        return dotInicialSiafi;
    }

    /**
     * Define o valor da propriedade dotInicialSiafi.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotInicialSiafi(BigDecimal value) {
        this.dotInicialSiafi = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoAntecipada.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotacaoAntecipada() {
        return dotacaoAntecipada;
    }

    /**
     * Define o valor da propriedade dotacaoAntecipada.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotacaoAntecipada(BigDecimal value) {
        this.dotacaoAntecipada = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoInicial.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotacaoInicial() {
        return dotacaoInicial;
    }

    /**
     * Define o valor da propriedade dotacaoInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotacaoInicial(BigDecimal value) {
        this.dotacaoInicial = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoOriginal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotacaoOriginal() {
        return dotacaoOriginal;
    }

    /**
     * Define o valor da propriedade dotacaoOriginal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotacaoOriginal(BigDecimal value) {
        this.dotacaoOriginal = value;
    }

    /**
     * Obtém o valor da propriedade elementoDespesa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElementoDespesa() {
        return elementoDespesa;
    }

    /**
     * Define o valor da propriedade elementoDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElementoDespesa(String value) {
        this.elementoDespesa = value;
    }

    /**
     * Obtém o valor da propriedade empALiquidar.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEmpALiquidar() {
        return empALiquidar;
    }

    /**
     * Define o valor da propriedade empALiquidar.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEmpALiquidar(BigDecimal value) {
        this.empALiquidar = value;
    }

    /**
     * Obtém o valor da propriedade empLiqInscrRapNp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmpLiqInscrRapNp() {
        return empLiqInscrRapNp;
    }

    /**
     * Define o valor da propriedade empLiqInscrRapNp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmpLiqInscrRapNp(String value) {
        this.empLiqInscrRapNp = value;
    }

    /**
     * Obtém o valor da propriedade empLiquidado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEmpLiquidado() {
        return empLiquidado;
    }

    /**
     * Define o valor da propriedade empLiquidado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEmpLiquidado(BigDecimal value) {
        this.empLiquidado = value;
    }

    /**
     * Obtém o valor da propriedade empenhadoALiquidar.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEmpenhadoALiquidar() {
        return empenhadoALiquidar;
    }

    /**
     * Define o valor da propriedade empenhadoALiquidar.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEmpenhadoALiquidar(BigDecimal value) {
        this.empenhadoALiquidar = value;
    }

    /**
     * Obtém o valor da propriedade esfera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsfera() {
        return esfera;
    }

    /**
     * Define o valor da propriedade esfera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsfera(String value) {
        this.esfera = value;
    }

    /**
     * Obtém o valor da propriedade executadoPorInscricaoDeRAP.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getExecutadoPorInscricaoDeRAP() {
        return executadoPorInscricaoDeRAP;
    }

    /**
     * Define o valor da propriedade executadoPorInscricaoDeRAP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setExecutadoPorInscricaoDeRAP(BigDecimal value) {
        this.executadoPorInscricaoDeRAP = value;
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
     * Obtém o valor da propriedade funcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuncao() {
        return funcao;
    }

    /**
     * Define o valor da propriedade funcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuncao(String value) {
        this.funcao = value;
    }

    /**
     * Obtém o valor da propriedade grupoNaturezaDespesa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrupoNaturezaDespesa() {
        return grupoNaturezaDespesa;
    }

    /**
     * Define o valor da propriedade grupoNaturezaDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrupoNaturezaDespesa(String value) {
        this.grupoNaturezaDespesa = value;
    }

    /**
     * Obtém o valor da propriedade idOc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOc() {
        return idOc;
    }

    /**
     * Define o valor da propriedade idOc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOc(String value) {
        this.idOc = value;
    }

    /**
     * Obtém o valor da propriedade idUso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUso() {
        return idUso;
    }

    /**
     * Define o valor da propriedade idUso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUso(String value) {
        this.idUso = value;
    }

    /**
     * Obtém o valor da propriedade identificadorAcompanhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorAcompanhamento() {
        return identificadorAcompanhamento;
    }

    /**
     * Define o valor da propriedade identificadorAcompanhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorAcompanhamento(String value) {
        this.identificadorAcompanhamento = value;
    }

    /**
     * Obtém o valor da propriedade identificadorAcompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorAcompanhamentoPO() {
        return identificadorAcompanhamentoPO;
    }

    /**
     * Define o valor da propriedade identificadorAcompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorAcompanhamentoPO(String value) {
        this.identificadorAcompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade indisponivel.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIndisponivel() {
        return indisponivel;
    }

    /**
     * Define o valor da propriedade indisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIndisponivel(BigDecimal value) {
        this.indisponivel = value;
    }

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
     * Obtém o valor da propriedade mes.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMes() {
        return mes;
    }

    /**
     * Define o valor da propriedade mes.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMes(Integer value) {
        this.mes = value;
    }

    /**
     * Obtém o valor da propriedade modalidadeAplicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModalidadeAplicacao() {
        return modalidadeAplicacao;
    }

    /**
     * Define o valor da propriedade modalidadeAplicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModalidadeAplicacao(String value) {
        this.modalidadeAplicacao = value;
    }

    /**
     * Obtém o valor da propriedade natureza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureza() {
        return natureza;
    }

    /**
     * Define o valor da propriedade natureza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureza(String value) {
        this.natureza = value;
    }

    /**
     * Obtém o valor da propriedade numeroptres.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroptres() {
        return numeroptres;
    }

    /**
     * Define o valor da propriedade numeroptres.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroptres(String value) {
        this.numeroptres = value;
    }

    /**
     * Obtém o valor da propriedade origem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigem() {
        return origem;
    }

    /**
     * Define o valor da propriedade origem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigem(String value) {
        this.origem = value;
    }

    /**
     * Obtém o valor da propriedade pago.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPago() {
        return pago;
    }

    /**
     * Define o valor da propriedade pago.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPago(BigDecimal value) {
        this.pago = value;
    }

    /**
     * Obtém o valor da propriedade planoInterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanoInterno() {
        return planoInterno;
    }

    /**
     * Define o valor da propriedade planoInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanoInterno(String value) {
        this.planoInterno = value;
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
     * Obtém o valor da propriedade programa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrograma() {
        return programa;
    }

    /**
     * Define o valor da propriedade programa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrograma(String value) {
        this.programa = value;
    }

    /**
     * Obtém o valor da propriedade projetoLei.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getProjetoLei() {
        return projetoLei;
    }

    /**
     * Define o valor da propriedade projetoLei.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setProjetoLei(BigDecimal value) {
        this.projetoLei = value;
    }

    /**
     * Obtém o valor da propriedade rapAPagarNaoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapAPagarNaoProcessado() {
        return rapAPagarNaoProcessado;
    }

    /**
     * Define o valor da propriedade rapAPagarNaoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapAPagarNaoProcessado(BigDecimal value) {
        this.rapAPagarNaoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapAPagarProcessado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapAPagarProcessado() {
        return rapAPagarProcessado;
    }

    /**
     * Define o valor da propriedade rapAPagarProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapAPagarProcessado(BigDecimal value) {
        this.rapAPagarProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapCanceladosNaoProcessados.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapCanceladosNaoProcessados() {
        return rapCanceladosNaoProcessados;
    }

    /**
     * Define o valor da propriedade rapCanceladosNaoProcessados.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapCanceladosNaoProcessados(BigDecimal value) {
        this.rapCanceladosNaoProcessados = value;
    }

    /**
     * Obtém o valor da propriedade rapCanceladosProcessados.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapCanceladosProcessados() {
        return rapCanceladosProcessados;
    }

    /**
     * Define o valor da propriedade rapCanceladosProcessados.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapCanceladosProcessados(BigDecimal value) {
        this.rapCanceladosProcessados = value;
    }

    /**
     * Obtém o valor da propriedade rapExerciciosAnteriores.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapExerciciosAnteriores() {
        return rapExerciciosAnteriores;
    }

    /**
     * Define o valor da propriedade rapExerciciosAnteriores.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapExerciciosAnteriores(BigDecimal value) {
        this.rapExerciciosAnteriores = value;
    }

    /**
     * Obtém o valor da propriedade rapInscritoNaoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapInscritoNaoProcessado() {
        return rapInscritoNaoProcessado;
    }

    /**
     * Define o valor da propriedade rapInscritoNaoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapInscritoNaoProcessado(BigDecimal value) {
        this.rapInscritoNaoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapInscritoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapInscritoProcessado() {
        return rapInscritoProcessado;
    }

    /**
     * Define o valor da propriedade rapInscritoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapInscritoProcessado(BigDecimal value) {
        this.rapInscritoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapNaoProcessadoALiquidar.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapNaoProcessadoALiquidar() {
        return rapNaoProcessadoALiquidar;
    }

    /**
     * Define o valor da propriedade rapNaoProcessadoALiquidar.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapNaoProcessadoALiquidar(BigDecimal value) {
        this.rapNaoProcessadoALiquidar = value;
    }

    /**
     * Obtém o valor da propriedade rapNaoProcessadoBloqueado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapNaoProcessadoBloqueado() {
        return rapNaoProcessadoBloqueado;
    }

    /**
     * Define o valor da propriedade rapNaoProcessadoBloqueado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapNaoProcessadoBloqueado(BigDecimal value) {
        this.rapNaoProcessadoBloqueado = value;
    }

    /**
     * Obtém o valor da propriedade rapNaoProcessadoLiquidadoAPagar.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapNaoProcessadoLiquidadoAPagar() {
        return rapNaoProcessadoLiquidadoAPagar;
    }

    /**
     * Define o valor da propriedade rapNaoProcessadoLiquidadoAPagar.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapNaoProcessadoLiquidadoAPagar(BigDecimal value) {
        this.rapNaoProcessadoLiquidadoAPagar = value;
    }

    /**
     * Obtém o valor da propriedade rapPagoNaoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapPagoNaoProcessado() {
        return rapPagoNaoProcessado;
    }

    /**
     * Define o valor da propriedade rapPagoNaoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapPagoNaoProcessado(BigDecimal value) {
        this.rapPagoNaoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapPagoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRapPagoProcessado() {
        return rapPagoProcessado;
    }

    /**
     * Define o valor da propriedade rapPagoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRapPagoProcessado(BigDecimal value) {
        this.rapPagoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade resultadoPrimarioAtual.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultadoPrimarioAtual() {
        return resultadoPrimarioAtual;
    }

    /**
     * Define o valor da propriedade resultadoPrimarioAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultadoPrimarioAtual(String value) {
        this.resultadoPrimarioAtual = value;
    }

    /**
     * Obtém o valor da propriedade resultadoPrimarioLei.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultadoPrimarioLei() {
        return resultadoPrimarioLei;
    }

    /**
     * Define o valor da propriedade resultadoPrimarioLei.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultadoPrimarioLei(String value) {
        this.resultadoPrimarioLei = value;
    }

    /**
     * Obtém o valor da propriedade rpAtual.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRpAtual() {
        return rpAtual;
    }

    /**
     * Define o valor da propriedade rpAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRpAtual(String value) {
        this.rpAtual = value;
    }

    /**
     * Obtém o valor da propriedade rpLei.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRpLei() {
        return rpLei;
    }

    /**
     * Define o valor da propriedade rpLei.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRpLei(String value) {
        this.rpLei = value;
    }

    /**
     * Obtém o valor da propriedade subElementoDespesa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubElementoDespesa() {
        return subElementoDespesa;
    }

    /**
     * Define o valor da propriedade subElementoDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubElementoDespesa(String value) {
        this.subElementoDespesa = value;
    }

    /**
     * Obtém o valor da propriedade subFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubFuncao() {
        return subFuncao;
    }

    /**
     * Define o valor da propriedade subFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubFuncao(String value) {
        this.subFuncao = value;
    }

    /**
     * Obtém o valor da propriedade tematica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTematica() {
        return tematica;
    }

    /**
     * Define o valor da propriedade tematica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTematica(String value) {
        this.tematica = value;
    }

    /**
     * Obtém o valor da propriedade tematicaPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTematicaPO() {
        return tematicaPO;
    }

    /**
     * Define o valor da propriedade tematicaPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTematicaPO(String value) {
        this.tematicaPO = value;
    }

    /**
     * Obtém o valor da propriedade tipoApropriacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoApropriacao() {
        return tipoApropriacao;
    }

    /**
     * Define o valor da propriedade tipoApropriacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoApropriacao(String value) {
        this.tipoApropriacao = value;
    }

    /**
     * Obtém o valor da propriedade tipoApropriacaoPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoApropriacaoPO() {
        return tipoApropriacaoPO;
    }

    /**
     * Define o valor da propriedade tipoApropriacaoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoApropriacaoPO(String value) {
        this.tipoApropriacaoPO = value;
    }

    /**
     * Obtém o valor da propriedade tipoCredito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCredito() {
        return tipoCredito;
    }

    /**
     * Define o valor da propriedade tipoCredito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCredito(String value) {
        this.tipoCredito = value;
    }

    /**
     * Obtém o valor da propriedade unidadeGestoraResponsavel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadeGestoraResponsavel() {
        return unidadeGestoraResponsavel;
    }

    /**
     * Define o valor da propriedade unidadeGestoraResponsavel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadeGestoraResponsavel(String value) {
        this.unidadeGestoraResponsavel = value;
    }

    /**
     * Obtém o valor da propriedade unidadeOrcamentaria.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadeOrcamentaria() {
        return unidadeOrcamentaria;
    }

    /**
     * Define o valor da propriedade unidadeOrcamentaria.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadeOrcamentaria(String value) {
        this.unidadeOrcamentaria = value;
    }

}
