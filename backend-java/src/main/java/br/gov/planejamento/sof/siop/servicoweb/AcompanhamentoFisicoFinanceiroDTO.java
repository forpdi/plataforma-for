
package br.gov.planejamento.sof.siop.servicoweb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de acompanhamentoFisicoFinanceiroDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="acompanhamentoFisicoFinanceiroDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoLocalizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoProduto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoProdutoPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoSiorg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoSubFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoUnidadeMedida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoUnidadeMedidaPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoUo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dotacaoAtual" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotacaoAtualPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotacaoInicial" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dotacaoInicialPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="liquidado" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="liquidadoPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="liquidadoRAP" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="liquidadoRAPPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="momento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="momentoId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="orgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgaoSiorg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pago" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="pagoPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="periodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="produto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="produtoPO" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaAtual" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaAtualPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaLOA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaLOAPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadoLOA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadoPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadoRAP" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="reprogramadoFinanceiro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="reprogramadoFisico" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="subfuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoCaptacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeMedida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeMedidaPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acompanhamentoFisicoFinanceiroDTO", propOrder = {
    "acao",
    "codigoPO",
    "descricaoFuncao",
    "descricaoLocalizador",
    "descricaoOrgao",
    "descricaoProduto",
    "descricaoProdutoPO",
    "descricaoSiorg",
    "descricaoSubFuncao",
    "descricaoUnidadeMedida",
    "descricaoUnidadeMedidaPO",
    "descricaoUo",
    "dotacaoAtual",
    "dotacaoAtualPO",
    "dotacaoInicial",
    "dotacaoInicialPO",
    "esfera",
    "exercicio",
    "funcao",
    "liquidado",
    "liquidadoPO",
    "liquidadoRAP",
    "liquidadoRAPPO",
    "localizador",
    "momento",
    "momentoId",
    "orgao",
    "orgaoSiorg",
    "pago",
    "pagoPO",
    "periodo",
    "produto",
    "produtoPO",
    "programa",
    "quantidadeMetaAtual",
    "quantidadeMetaAtualPO",
    "quantidadeMetaLOA",
    "quantidadeMetaLOAPO",
    "realizadoLOA",
    "realizadoPO",
    "realizadoRAP",
    "reprogramadoFinanceiro",
    "reprogramadoFisico",
    "subfuncao",
    "tipoCaptacao",
    "tituloAcao",
    "tituloPO",
    "tituloPrograma",
    "unidadeMedida",
    "unidadeMedidaPO",
    "uo"
})
public class AcompanhamentoFisicoFinanceiroDTO {

    protected String acao;
    protected String codigoPO;
    protected String descricaoFuncao;
    protected String descricaoLocalizador;
    protected String descricaoOrgao;
    protected String descricaoProduto;
    protected String descricaoProdutoPO;
    protected String descricaoSiorg;
    protected String descricaoSubFuncao;
    protected String descricaoUnidadeMedida;
    protected String descricaoUnidadeMedidaPO;
    protected String descricaoUo;
    protected BigDecimal dotacaoAtual;
    protected BigDecimal dotacaoAtualPO;
    protected BigDecimal dotacaoInicial;
    protected BigDecimal dotacaoInicialPO;
    protected String esfera;
    protected Integer exercicio;
    protected String funcao;
    protected BigDecimal liquidado;
    protected BigDecimal liquidadoPO;
    protected BigDecimal liquidadoRAP;
    protected BigDecimal liquidadoRAPPO;
    protected String localizador;
    protected String momento;
    protected Integer momentoId;
    protected String orgao;
    protected String orgaoSiorg;
    protected BigDecimal pago;
    protected BigDecimal pagoPO;
    protected String periodo;
    protected Integer produto;
    protected Integer produtoPO;
    protected String programa;
    protected BigDecimal quantidadeMetaAtual;
    protected BigDecimal quantidadeMetaAtualPO;
    protected BigDecimal quantidadeMetaLOA;
    protected BigDecimal quantidadeMetaLOAPO;
    protected BigDecimal realizadoLOA;
    protected BigDecimal realizadoPO;
    protected BigDecimal realizadoRAP;
    protected BigDecimal reprogramadoFinanceiro;
    protected BigDecimal reprogramadoFisico;
    protected String subfuncao;
    protected String tipoCaptacao;
    protected String tituloAcao;
    protected String tituloPO;
    protected String tituloPrograma;
    protected String unidadeMedida;
    protected String unidadeMedidaPO;
    protected String uo;

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
     * Obtém o valor da propriedade codigoPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPO() {
        return codigoPO;
    }

    /**
     * Define o valor da propriedade codigoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPO(String value) {
        this.codigoPO = value;
    }

    /**
     * Obtém o valor da propriedade descricaoFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoFuncao() {
        return descricaoFuncao;
    }

    /**
     * Define o valor da propriedade descricaoFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoFuncao(String value) {
        this.descricaoFuncao = value;
    }

    /**
     * Obtém o valor da propriedade descricaoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoLocalizador() {
        return descricaoLocalizador;
    }

    /**
     * Define o valor da propriedade descricaoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoLocalizador(String value) {
        this.descricaoLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade descricaoOrgao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoOrgao() {
        return descricaoOrgao;
    }

    /**
     * Define o valor da propriedade descricaoOrgao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoOrgao(String value) {
        this.descricaoOrgao = value;
    }

    /**
     * Obtém o valor da propriedade descricaoProduto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    /**
     * Define o valor da propriedade descricaoProduto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoProduto(String value) {
        this.descricaoProduto = value;
    }

    /**
     * Obtém o valor da propriedade descricaoProdutoPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoProdutoPO() {
        return descricaoProdutoPO;
    }

    /**
     * Define o valor da propriedade descricaoProdutoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoProdutoPO(String value) {
        this.descricaoProdutoPO = value;
    }

    /**
     * Obtém o valor da propriedade descricaoSiorg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoSiorg() {
        return descricaoSiorg;
    }

    /**
     * Define o valor da propriedade descricaoSiorg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoSiorg(String value) {
        this.descricaoSiorg = value;
    }

    /**
     * Obtém o valor da propriedade descricaoSubFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoSubFuncao() {
        return descricaoSubFuncao;
    }

    /**
     * Define o valor da propriedade descricaoSubFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoSubFuncao(String value) {
        this.descricaoSubFuncao = value;
    }

    /**
     * Obtém o valor da propriedade descricaoUnidadeMedida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoUnidadeMedida() {
        return descricaoUnidadeMedida;
    }

    /**
     * Define o valor da propriedade descricaoUnidadeMedida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoUnidadeMedida(String value) {
        this.descricaoUnidadeMedida = value;
    }

    /**
     * Obtém o valor da propriedade descricaoUnidadeMedidaPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoUnidadeMedidaPO() {
        return descricaoUnidadeMedidaPO;
    }

    /**
     * Define o valor da propriedade descricaoUnidadeMedidaPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoUnidadeMedidaPO(String value) {
        this.descricaoUnidadeMedidaPO = value;
    }

    /**
     * Obtém o valor da propriedade descricaoUo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoUo() {
        return descricaoUo;
    }

    /**
     * Define o valor da propriedade descricaoUo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoUo(String value) {
        this.descricaoUo = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoAtual.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotacaoAtual() {
        return dotacaoAtual;
    }

    /**
     * Define o valor da propriedade dotacaoAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotacaoAtual(BigDecimal value) {
        this.dotacaoAtual = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoAtualPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotacaoAtualPO() {
        return dotacaoAtualPO;
    }

    /**
     * Define o valor da propriedade dotacaoAtualPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotacaoAtualPO(BigDecimal value) {
        this.dotacaoAtualPO = value;
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
     * Obtém o valor da propriedade dotacaoInicialPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDotacaoInicialPO() {
        return dotacaoInicialPO;
    }

    /**
     * Define o valor da propriedade dotacaoInicialPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDotacaoInicialPO(BigDecimal value) {
        this.dotacaoInicialPO = value;
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
     * Obtém o valor da propriedade liquidado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiquidado() {
        return liquidado;
    }

    /**
     * Define o valor da propriedade liquidado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiquidado(BigDecimal value) {
        this.liquidado = value;
    }

    /**
     * Obtém o valor da propriedade liquidadoPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiquidadoPO() {
        return liquidadoPO;
    }

    /**
     * Define o valor da propriedade liquidadoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiquidadoPO(BigDecimal value) {
        this.liquidadoPO = value;
    }

    /**
     * Obtém o valor da propriedade liquidadoRAP.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiquidadoRAP() {
        return liquidadoRAP;
    }

    /**
     * Define o valor da propriedade liquidadoRAP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiquidadoRAP(BigDecimal value) {
        this.liquidadoRAP = value;
    }

    /**
     * Obtém o valor da propriedade liquidadoRAPPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLiquidadoRAPPO() {
        return liquidadoRAPPO;
    }

    /**
     * Define o valor da propriedade liquidadoRAPPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLiquidadoRAPPO(BigDecimal value) {
        this.liquidadoRAPPO = value;
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
     * Obtém o valor da propriedade momento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMomento() {
        return momento;
    }

    /**
     * Define o valor da propriedade momento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMomento(String value) {
        this.momento = value;
    }

    /**
     * Obtém o valor da propriedade momentoId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMomentoId() {
        return momentoId;
    }

    /**
     * Define o valor da propriedade momentoId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMomentoId(Integer value) {
        this.momentoId = value;
    }

    /**
     * Obtém o valor da propriedade orgao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgao() {
        return orgao;
    }

    /**
     * Define o valor da propriedade orgao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgao(String value) {
        this.orgao = value;
    }

    /**
     * Obtém o valor da propriedade orgaoSiorg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgaoSiorg() {
        return orgaoSiorg;
    }

    /**
     * Define o valor da propriedade orgaoSiorg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgaoSiorg(String value) {
        this.orgaoSiorg = value;
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
     * Obtém o valor da propriedade pagoPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPagoPO() {
        return pagoPO;
    }

    /**
     * Define o valor da propriedade pagoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPagoPO(BigDecimal value) {
        this.pagoPO = value;
    }

    /**
     * Obtém o valor da propriedade periodo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Define o valor da propriedade periodo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodo(String value) {
        this.periodo = value;
    }

    /**
     * Obtém o valor da propriedade produto.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProduto() {
        return produto;
    }

    /**
     * Define o valor da propriedade produto.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProduto(Integer value) {
        this.produto = value;
    }

    /**
     * Obtém o valor da propriedade produtoPO.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProdutoPO() {
        return produtoPO;
    }

    /**
     * Define o valor da propriedade produtoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProdutoPO(Integer value) {
        this.produtoPO = value;
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
     * Obtém o valor da propriedade quantidadeMetaAtual.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantidadeMetaAtual() {
        return quantidadeMetaAtual;
    }

    /**
     * Define o valor da propriedade quantidadeMetaAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantidadeMetaAtual(BigDecimal value) {
        this.quantidadeMetaAtual = value;
    }

    /**
     * Obtém o valor da propriedade quantidadeMetaAtualPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantidadeMetaAtualPO() {
        return quantidadeMetaAtualPO;
    }

    /**
     * Define o valor da propriedade quantidadeMetaAtualPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantidadeMetaAtualPO(BigDecimal value) {
        this.quantidadeMetaAtualPO = value;
    }

    /**
     * Obtém o valor da propriedade quantidadeMetaLOA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantidadeMetaLOA() {
        return quantidadeMetaLOA;
    }

    /**
     * Define o valor da propriedade quantidadeMetaLOA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantidadeMetaLOA(BigDecimal value) {
        this.quantidadeMetaLOA = value;
    }

    /**
     * Obtém o valor da propriedade quantidadeMetaLOAPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantidadeMetaLOAPO() {
        return quantidadeMetaLOAPO;
    }

    /**
     * Define o valor da propriedade quantidadeMetaLOAPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantidadeMetaLOAPO(BigDecimal value) {
        this.quantidadeMetaLOAPO = value;
    }

    /**
     * Obtém o valor da propriedade realizadoLOA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadoLOA() {
        return realizadoLOA;
    }

    /**
     * Define o valor da propriedade realizadoLOA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadoLOA(BigDecimal value) {
        this.realizadoLOA = value;
    }

    /**
     * Obtém o valor da propriedade realizadoPO.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadoPO() {
        return realizadoPO;
    }

    /**
     * Define o valor da propriedade realizadoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadoPO(BigDecimal value) {
        this.realizadoPO = value;
    }

    /**
     * Obtém o valor da propriedade realizadoRAP.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadoRAP() {
        return realizadoRAP;
    }

    /**
     * Define o valor da propriedade realizadoRAP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadoRAP(BigDecimal value) {
        this.realizadoRAP = value;
    }

    /**
     * Obtém o valor da propriedade reprogramadoFinanceiro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReprogramadoFinanceiro() {
        return reprogramadoFinanceiro;
    }

    /**
     * Define o valor da propriedade reprogramadoFinanceiro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReprogramadoFinanceiro(BigDecimal value) {
        this.reprogramadoFinanceiro = value;
    }

    /**
     * Obtém o valor da propriedade reprogramadoFisico.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReprogramadoFisico() {
        return reprogramadoFisico;
    }

    /**
     * Define o valor da propriedade reprogramadoFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReprogramadoFisico(BigDecimal value) {
        this.reprogramadoFisico = value;
    }

    /**
     * Obtém o valor da propriedade subfuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubfuncao() {
        return subfuncao;
    }

    /**
     * Define o valor da propriedade subfuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubfuncao(String value) {
        this.subfuncao = value;
    }

    /**
     * Obtém o valor da propriedade tipoCaptacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCaptacao() {
        return tipoCaptacao;
    }

    /**
     * Define o valor da propriedade tipoCaptacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCaptacao(String value) {
        this.tipoCaptacao = value;
    }

    /**
     * Obtém o valor da propriedade tituloAcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloAcao() {
        return tituloAcao;
    }

    /**
     * Define o valor da propriedade tituloAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloAcao(String value) {
        this.tituloAcao = value;
    }

    /**
     * Obtém o valor da propriedade tituloPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloPO() {
        return tituloPO;
    }

    /**
     * Define o valor da propriedade tituloPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloPO(String value) {
        this.tituloPO = value;
    }

    /**
     * Obtém o valor da propriedade tituloPrograma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloPrograma() {
        return tituloPrograma;
    }

    /**
     * Define o valor da propriedade tituloPrograma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloPrograma(String value) {
        this.tituloPrograma = value;
    }

    /**
     * Obtém o valor da propriedade unidadeMedida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    /**
     * Define o valor da propriedade unidadeMedida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadeMedida(String value) {
        this.unidadeMedida = value;
    }

    /**
     * Obtém o valor da propriedade unidadeMedidaPO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadeMedidaPO() {
        return unidadeMedidaPO;
    }

    /**
     * Define o valor da propriedade unidadeMedidaPO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadeMedidaPO(String value) {
        this.unidadeMedidaPO = value;
    }

    /**
     * Obtém o valor da propriedade uo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUo() {
        return uo;
    }

    /**
     * Define o valor da propriedade uo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUo(String value) {
        this.uo = value;
    }

}
