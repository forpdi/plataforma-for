
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de selecaoRetornoExecucaoOrcamentariaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="selecaoRetornoExecucaoOrcamentariaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="acompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="anoExercicio" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="anoReferencia" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="autorizado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="bloqueadoRemanejamento" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="bloqueadoSOF" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="categoriaEconomica" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="creditoContidoSOF" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="detalheAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="disponivel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dotAtual" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dotInicialSiafi" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dotacaoAntecipada" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dotacaoInicial" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="elementoDespesa" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="empLiquidado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="empenhadoALiquidar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="estatalDependente" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="executadoPorInscricaoDeRAP" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="grupoNaturezaDespesa" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="identificadorAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="idoc" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="iduso" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="indisponivel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="modalidadeAplicacao" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="natureza" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="numeroptres" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="origem" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="pago" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="planoInterno" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="planoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="projetoLei" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapAPagarNaoProcessado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapAPagarProcessado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapCanceladosNaoProcessados" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapCanceladosProcessados" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapExerciciosAnteriores" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapInscritoNaoProcessado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapInscritoProcessado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapNaoProcessadoALiquidar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapNaoProcessadoBloqueado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapNaoProcessadoLiquidadoAPagar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapPagoNaoProcessado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rapPagoProcessado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="resultadoPrimarioAtual" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="resultadoPrimarioLei" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="subElementoDespesa" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="subFuncao" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tematicaPO" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tipoApropriacaoPO" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tipoCredito" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="unidadeGestoraResponsavel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="unidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selecaoRetornoExecucaoOrcamentariaDTO", propOrder = {
    "acao",
    "acompanhamentoPO",
    "anoExercicio",
    "anoReferencia",
    "autorizado",
    "bloqueadoRemanejamento",
    "bloqueadoSOF",
    "categoriaEconomica",
    "creditoContidoSOF",
    "detalheAcompanhamentoPO",
    "disponivel",
    "dotAtual",
    "dotInicialSiafi",
    "dotacaoAntecipada",
    "dotacaoInicial",
    "elementoDespesa",
    "empLiquidado",
    "empenhadoALiquidar",
    "esfera",
    "estatalDependente",
    "executadoPorInscricaoDeRAP",
    "fonte",
    "funcao",
    "grupoNaturezaDespesa",
    "identificadorAcompanhamentoPO",
    "idoc",
    "iduso",
    "indisponivel",
    "localizador",
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
    "subElementoDespesa",
    "subFuncao",
    "tematicaPO",
    "tipoApropriacaoPO",
    "tipoCredito",
    "unidadeGestoraResponsavel",
    "unidadeOrcamentaria"
})
public class SelecaoRetornoExecucaoOrcamentariaDTO
    extends BaseDTO
{

    protected Boolean acao;
    protected Boolean acompanhamentoPO;
    protected Boolean anoExercicio;
    protected boolean anoReferencia;
    protected Boolean autorizado;
    protected Boolean bloqueadoRemanejamento;
    protected Boolean bloqueadoSOF;
    protected Boolean categoriaEconomica;
    protected Boolean creditoContidoSOF;
    protected Boolean detalheAcompanhamentoPO;
    protected Boolean disponivel;
    protected Boolean dotAtual;
    protected Boolean dotInicialSiafi;
    protected Boolean dotacaoAntecipada;
    protected Boolean dotacaoInicial;
    protected Boolean elementoDespesa;
    protected Boolean empLiquidado;
    protected Boolean empenhadoALiquidar;
    protected Boolean esfera;
    protected Boolean estatalDependente;
    protected Boolean executadoPorInscricaoDeRAP;
    protected Boolean fonte;
    protected Boolean funcao;
    protected Boolean grupoNaturezaDespesa;
    protected Boolean identificadorAcompanhamentoPO;
    protected Boolean idoc;
    protected Boolean iduso;
    protected Boolean indisponivel;
    protected Boolean localizador;
    protected Boolean modalidadeAplicacao;
    protected Boolean natureza;
    protected Boolean numeroptres;
    protected Boolean origem;
    protected Boolean pago;
    protected Boolean planoInterno;
    protected Boolean planoOrcamentario;
    protected Boolean programa;
    protected Boolean projetoLei;
    protected Boolean rapAPagarNaoProcessado;
    protected Boolean rapAPagarProcessado;
    protected Boolean rapCanceladosNaoProcessados;
    protected Boolean rapCanceladosProcessados;
    protected Boolean rapExerciciosAnteriores;
    protected Boolean rapInscritoNaoProcessado;
    protected Boolean rapInscritoProcessado;
    protected Boolean rapNaoProcessadoALiquidar;
    protected Boolean rapNaoProcessadoBloqueado;
    protected Boolean rapNaoProcessadoLiquidadoAPagar;
    protected Boolean rapPagoNaoProcessado;
    protected Boolean rapPagoProcessado;
    protected Boolean resultadoPrimarioAtual;
    protected Boolean resultadoPrimarioLei;
    protected Boolean subElementoDespesa;
    protected Boolean subFuncao;
    protected Boolean tematicaPO;
    protected Boolean tipoApropriacaoPO;
    protected Boolean tipoCredito;
    protected Boolean unidadeGestoraResponsavel;
    protected Boolean unidadeOrcamentaria;

    /**
     * Obtém o valor da propriedade acao.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAcao() {
        return acao;
    }

    /**
     * Define o valor da propriedade acao.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAcao(Boolean value) {
        this.acao = value;
    }

    /**
     * Obtém o valor da propriedade acompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAcompanhamentoPO() {
        return acompanhamentoPO;
    }

    /**
     * Define o valor da propriedade acompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAcompanhamentoPO(Boolean value) {
        this.acompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade anoExercicio.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAnoExercicio() {
        return anoExercicio;
    }

    /**
     * Define o valor da propriedade anoExercicio.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAnoExercicio(Boolean value) {
        this.anoExercicio = value;
    }

    /**
     * Obtém o valor da propriedade anoReferencia.
     * 
     */
    public boolean isAnoReferencia() {
        return anoReferencia;
    }

    /**
     * Define o valor da propriedade anoReferencia.
     * 
     */
    public void setAnoReferencia(boolean value) {
        this.anoReferencia = value;
    }

    /**
     * Obtém o valor da propriedade autorizado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutorizado() {
        return autorizado;
    }

    /**
     * Define o valor da propriedade autorizado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutorizado(Boolean value) {
        this.autorizado = value;
    }

    /**
     * Obtém o valor da propriedade bloqueadoRemanejamento.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBloqueadoRemanejamento() {
        return bloqueadoRemanejamento;
    }

    /**
     * Define o valor da propriedade bloqueadoRemanejamento.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBloqueadoRemanejamento(Boolean value) {
        this.bloqueadoRemanejamento = value;
    }

    /**
     * Obtém o valor da propriedade bloqueadoSOF.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBloqueadoSOF() {
        return bloqueadoSOF;
    }

    /**
     * Define o valor da propriedade bloqueadoSOF.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBloqueadoSOF(Boolean value) {
        this.bloqueadoSOF = value;
    }

    /**
     * Obtém o valor da propriedade categoriaEconomica.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCategoriaEconomica() {
        return categoriaEconomica;
    }

    /**
     * Define o valor da propriedade categoriaEconomica.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCategoriaEconomica(Boolean value) {
        this.categoriaEconomica = value;
    }

    /**
     * Obtém o valor da propriedade creditoContidoSOF.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCreditoContidoSOF() {
        return creditoContidoSOF;
    }

    /**
     * Define o valor da propriedade creditoContidoSOF.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreditoContidoSOF(Boolean value) {
        this.creditoContidoSOF = value;
    }

    /**
     * Obtém o valor da propriedade detalheAcompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDetalheAcompanhamentoPO() {
        return detalheAcompanhamentoPO;
    }

    /**
     * Define o valor da propriedade detalheAcompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDetalheAcompanhamentoPO(Boolean value) {
        this.detalheAcompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade disponivel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Define o valor da propriedade disponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisponivel(Boolean value) {
        this.disponivel = value;
    }

    /**
     * Obtém o valor da propriedade dotAtual.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDotAtual() {
        return dotAtual;
    }

    /**
     * Define o valor da propriedade dotAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDotAtual(Boolean value) {
        this.dotAtual = value;
    }

    /**
     * Obtém o valor da propriedade dotInicialSiafi.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDotInicialSiafi() {
        return dotInicialSiafi;
    }

    /**
     * Define o valor da propriedade dotInicialSiafi.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDotInicialSiafi(Boolean value) {
        this.dotInicialSiafi = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoAntecipada.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDotacaoAntecipada() {
        return dotacaoAntecipada;
    }

    /**
     * Define o valor da propriedade dotacaoAntecipada.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDotacaoAntecipada(Boolean value) {
        this.dotacaoAntecipada = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoInicial.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDotacaoInicial() {
        return dotacaoInicial;
    }

    /**
     * Define o valor da propriedade dotacaoInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDotacaoInicial(Boolean value) {
        this.dotacaoInicial = value;
    }

    /**
     * Obtém o valor da propriedade elementoDespesa.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isElementoDespesa() {
        return elementoDespesa;
    }

    /**
     * Define o valor da propriedade elementoDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setElementoDespesa(Boolean value) {
        this.elementoDespesa = value;
    }

    /**
     * Obtém o valor da propriedade empLiquidado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmpLiquidado() {
        return empLiquidado;
    }

    /**
     * Define o valor da propriedade empLiquidado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmpLiquidado(Boolean value) {
        this.empLiquidado = value;
    }

    /**
     * Obtém o valor da propriedade empenhadoALiquidar.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmpenhadoALiquidar() {
        return empenhadoALiquidar;
    }

    /**
     * Define o valor da propriedade empenhadoALiquidar.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmpenhadoALiquidar(Boolean value) {
        this.empenhadoALiquidar = value;
    }

    /**
     * Obtém o valor da propriedade esfera.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsfera() {
        return esfera;
    }

    /**
     * Define o valor da propriedade esfera.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsfera(Boolean value) {
        this.esfera = value;
    }

    /**
     * Obtém o valor da propriedade estatalDependente.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstatalDependente() {
        return estatalDependente;
    }

    /**
     * Define o valor da propriedade estatalDependente.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstatalDependente(Boolean value) {
        this.estatalDependente = value;
    }

    /**
     * Obtém o valor da propriedade executadoPorInscricaoDeRAP.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExecutadoPorInscricaoDeRAP() {
        return executadoPorInscricaoDeRAP;
    }

    /**
     * Define o valor da propriedade executadoPorInscricaoDeRAP.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExecutadoPorInscricaoDeRAP(Boolean value) {
        this.executadoPorInscricaoDeRAP = value;
    }

    /**
     * Obtém o valor da propriedade fonte.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFonte() {
        return fonte;
    }

    /**
     * Define o valor da propriedade fonte.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFonte(Boolean value) {
        this.fonte = value;
    }

    /**
     * Obtém o valor da propriedade funcao.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFuncao() {
        return funcao;
    }

    /**
     * Define o valor da propriedade funcao.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFuncao(Boolean value) {
        this.funcao = value;
    }

    /**
     * Obtém o valor da propriedade grupoNaturezaDespesa.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGrupoNaturezaDespesa() {
        return grupoNaturezaDespesa;
    }

    /**
     * Define o valor da propriedade grupoNaturezaDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGrupoNaturezaDespesa(Boolean value) {
        this.grupoNaturezaDespesa = value;
    }

    /**
     * Obtém o valor da propriedade identificadorAcompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIdentificadorAcompanhamentoPO() {
        return identificadorAcompanhamentoPO;
    }

    /**
     * Define o valor da propriedade identificadorAcompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIdentificadorAcompanhamentoPO(Boolean value) {
        this.identificadorAcompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade idoc.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIdoc() {
        return idoc;
    }

    /**
     * Define o valor da propriedade idoc.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIdoc(Boolean value) {
        this.idoc = value;
    }

    /**
     * Obtém o valor da propriedade iduso.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIduso() {
        return iduso;
    }

    /**
     * Define o valor da propriedade iduso.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIduso(Boolean value) {
        this.iduso = value;
    }

    /**
     * Obtém o valor da propriedade indisponivel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIndisponivel() {
        return indisponivel;
    }

    /**
     * Define o valor da propriedade indisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIndisponivel(Boolean value) {
        this.indisponivel = value;
    }

    /**
     * Obtém o valor da propriedade localizador.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLocalizador() {
        return localizador;
    }

    /**
     * Define o valor da propriedade localizador.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLocalizador(Boolean value) {
        this.localizador = value;
    }

    /**
     * Obtém o valor da propriedade modalidadeAplicacao.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isModalidadeAplicacao() {
        return modalidadeAplicacao;
    }

    /**
     * Define o valor da propriedade modalidadeAplicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setModalidadeAplicacao(Boolean value) {
        this.modalidadeAplicacao = value;
    }

    /**
     * Obtém o valor da propriedade natureza.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNatureza() {
        return natureza;
    }

    /**
     * Define o valor da propriedade natureza.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNatureza(Boolean value) {
        this.natureza = value;
    }

    /**
     * Obtém o valor da propriedade numeroptres.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNumeroptres() {
        return numeroptres;
    }

    /**
     * Define o valor da propriedade numeroptres.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNumeroptres(Boolean value) {
        this.numeroptres = value;
    }

    /**
     * Obtém o valor da propriedade origem.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOrigem() {
        return origem;
    }

    /**
     * Define o valor da propriedade origem.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOrigem(Boolean value) {
        this.origem = value;
    }

    /**
     * Obtém o valor da propriedade pago.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPago() {
        return pago;
    }

    /**
     * Define o valor da propriedade pago.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPago(Boolean value) {
        this.pago = value;
    }

    /**
     * Obtém o valor da propriedade planoInterno.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPlanoInterno() {
        return planoInterno;
    }

    /**
     * Define o valor da propriedade planoInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPlanoInterno(Boolean value) {
        this.planoInterno = value;
    }

    /**
     * Obtém o valor da propriedade planoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPlanoOrcamentario() {
        return planoOrcamentario;
    }

    /**
     * Define o valor da propriedade planoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPlanoOrcamentario(Boolean value) {
        this.planoOrcamentario = value;
    }

    /**
     * Obtém o valor da propriedade programa.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrograma() {
        return programa;
    }

    /**
     * Define o valor da propriedade programa.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrograma(Boolean value) {
        this.programa = value;
    }

    /**
     * Obtém o valor da propriedade projetoLei.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isProjetoLei() {
        return projetoLei;
    }

    /**
     * Define o valor da propriedade projetoLei.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setProjetoLei(Boolean value) {
        this.projetoLei = value;
    }

    /**
     * Obtém o valor da propriedade rapAPagarNaoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapAPagarNaoProcessado() {
        return rapAPagarNaoProcessado;
    }

    /**
     * Define o valor da propriedade rapAPagarNaoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapAPagarNaoProcessado(Boolean value) {
        this.rapAPagarNaoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapAPagarProcessado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapAPagarProcessado() {
        return rapAPagarProcessado;
    }

    /**
     * Define o valor da propriedade rapAPagarProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapAPagarProcessado(Boolean value) {
        this.rapAPagarProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapCanceladosNaoProcessados.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapCanceladosNaoProcessados() {
        return rapCanceladosNaoProcessados;
    }

    /**
     * Define o valor da propriedade rapCanceladosNaoProcessados.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapCanceladosNaoProcessados(Boolean value) {
        this.rapCanceladosNaoProcessados = value;
    }

    /**
     * Obtém o valor da propriedade rapCanceladosProcessados.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapCanceladosProcessados() {
        return rapCanceladosProcessados;
    }

    /**
     * Define o valor da propriedade rapCanceladosProcessados.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapCanceladosProcessados(Boolean value) {
        this.rapCanceladosProcessados = value;
    }

    /**
     * Obtém o valor da propriedade rapExerciciosAnteriores.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapExerciciosAnteriores() {
        return rapExerciciosAnteriores;
    }

    /**
     * Define o valor da propriedade rapExerciciosAnteriores.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapExerciciosAnteriores(Boolean value) {
        this.rapExerciciosAnteriores = value;
    }

    /**
     * Obtém o valor da propriedade rapInscritoNaoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapInscritoNaoProcessado() {
        return rapInscritoNaoProcessado;
    }

    /**
     * Define o valor da propriedade rapInscritoNaoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapInscritoNaoProcessado(Boolean value) {
        this.rapInscritoNaoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapInscritoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapInscritoProcessado() {
        return rapInscritoProcessado;
    }

    /**
     * Define o valor da propriedade rapInscritoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapInscritoProcessado(Boolean value) {
        this.rapInscritoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapNaoProcessadoALiquidar.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapNaoProcessadoALiquidar() {
        return rapNaoProcessadoALiquidar;
    }

    /**
     * Define o valor da propriedade rapNaoProcessadoALiquidar.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapNaoProcessadoALiquidar(Boolean value) {
        this.rapNaoProcessadoALiquidar = value;
    }

    /**
     * Obtém o valor da propriedade rapNaoProcessadoBloqueado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapNaoProcessadoBloqueado() {
        return rapNaoProcessadoBloqueado;
    }

    /**
     * Define o valor da propriedade rapNaoProcessadoBloqueado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapNaoProcessadoBloqueado(Boolean value) {
        this.rapNaoProcessadoBloqueado = value;
    }

    /**
     * Obtém o valor da propriedade rapNaoProcessadoLiquidadoAPagar.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapNaoProcessadoLiquidadoAPagar() {
        return rapNaoProcessadoLiquidadoAPagar;
    }

    /**
     * Define o valor da propriedade rapNaoProcessadoLiquidadoAPagar.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapNaoProcessadoLiquidadoAPagar(Boolean value) {
        this.rapNaoProcessadoLiquidadoAPagar = value;
    }

    /**
     * Obtém o valor da propriedade rapPagoNaoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapPagoNaoProcessado() {
        return rapPagoNaoProcessado;
    }

    /**
     * Define o valor da propriedade rapPagoNaoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapPagoNaoProcessado(Boolean value) {
        this.rapPagoNaoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade rapPagoProcessado.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRapPagoProcessado() {
        return rapPagoProcessado;
    }

    /**
     * Define o valor da propriedade rapPagoProcessado.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRapPagoProcessado(Boolean value) {
        this.rapPagoProcessado = value;
    }

    /**
     * Obtém o valor da propriedade resultadoPrimarioAtual.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResultadoPrimarioAtual() {
        return resultadoPrimarioAtual;
    }

    /**
     * Define o valor da propriedade resultadoPrimarioAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResultadoPrimarioAtual(Boolean value) {
        this.resultadoPrimarioAtual = value;
    }

    /**
     * Obtém o valor da propriedade resultadoPrimarioLei.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResultadoPrimarioLei() {
        return resultadoPrimarioLei;
    }

    /**
     * Define o valor da propriedade resultadoPrimarioLei.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResultadoPrimarioLei(Boolean value) {
        this.resultadoPrimarioLei = value;
    }

    /**
     * Obtém o valor da propriedade subElementoDespesa.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSubElementoDespesa() {
        return subElementoDespesa;
    }

    /**
     * Define o valor da propriedade subElementoDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubElementoDespesa(Boolean value) {
        this.subElementoDespesa = value;
    }

    /**
     * Obtém o valor da propriedade subFuncao.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSubFuncao() {
        return subFuncao;
    }

    /**
     * Define o valor da propriedade subFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubFuncao(Boolean value) {
        this.subFuncao = value;
    }

    /**
     * Obtém o valor da propriedade tematicaPO.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTematicaPO() {
        return tematicaPO;
    }

    /**
     * Define o valor da propriedade tematicaPO.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTematicaPO(Boolean value) {
        this.tematicaPO = value;
    }

    /**
     * Obtém o valor da propriedade tipoApropriacaoPO.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTipoApropriacaoPO() {
        return tipoApropriacaoPO;
    }

    /**
     * Define o valor da propriedade tipoApropriacaoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTipoApropriacaoPO(Boolean value) {
        this.tipoApropriacaoPO = value;
    }

    /**
     * Obtém o valor da propriedade tipoCredito.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTipoCredito() {
        return tipoCredito;
    }

    /**
     * Define o valor da propriedade tipoCredito.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTipoCredito(Boolean value) {
        this.tipoCredito = value;
    }

    /**
     * Obtém o valor da propriedade unidadeGestoraResponsavel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUnidadeGestoraResponsavel() {
        return unidadeGestoraResponsavel;
    }

    /**
     * Define o valor da propriedade unidadeGestoraResponsavel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUnidadeGestoraResponsavel(Boolean value) {
        this.unidadeGestoraResponsavel = value;
    }

    /**
     * Obtém o valor da propriedade unidadeOrcamentaria.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUnidadeOrcamentaria() {
        return unidadeOrcamentaria;
    }

    /**
     * Define o valor da propriedade unidadeOrcamentaria.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUnidadeOrcamentaria(Boolean value) {
        this.unidadeOrcamentaria = value;
    }

}
