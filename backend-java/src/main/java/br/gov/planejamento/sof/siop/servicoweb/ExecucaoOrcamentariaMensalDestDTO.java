
package br.gov.planejamento.sof.siop.servicoweb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoOrcamentariaMensalDestDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoOrcamentariaMensalDestDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoSubfuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estatalDependente" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="estatalIndependente" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lei" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="leiMaisCreditos" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ppipac" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="realizadaAbril" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaAgosto" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaDezembro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaFevereiro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaJaneiro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaJulho" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaJunho" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaMaio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaMarco" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaNovembro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaOutubro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadaSetembro" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="regiao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subfuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloLocalizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "execucaoOrcamentariaMensalDestDTO", propOrder = {
    "acao",
    "descricaoFuncao",
    "descricaoSubfuncao",
    "esfera",
    "estatalDependente",
    "estatalIndependente",
    "exercicio",
    "funcao",
    "lei",
    "leiMaisCreditos",
    "localizador",
    "ppipac",
    "programa",
    "realizadaAbril",
    "realizadaAgosto",
    "realizadaDezembro",
    "realizadaFevereiro",
    "realizadaJaneiro",
    "realizadaJulho",
    "realizadaJunho",
    "realizadaMaio",
    "realizadaMarco",
    "realizadaNovembro",
    "realizadaOutubro",
    "realizadaSetembro",
    "regiao",
    "subfuncao",
    "tituloAcao",
    "tituloLocalizador",
    "tituloPrograma",
    "uf",
    "unidadeOrcamentaria"
})
public class ExecucaoOrcamentariaMensalDestDTO
    extends BaseDTO
{

    protected String acao;
    protected String descricaoFuncao;
    protected String descricaoSubfuncao;
    protected String esfera;
    protected Boolean estatalDependente;
    protected Boolean estatalIndependente;
    protected Integer exercicio;
    protected String funcao;
    protected BigDecimal lei;
    protected BigDecimal leiMaisCreditos;
    protected String localizador;
    protected Boolean ppipac;
    protected String programa;
    protected BigDecimal realizadaAbril;
    protected BigDecimal realizadaAgosto;
    protected BigDecimal realizadaDezembro;
    protected BigDecimal realizadaFevereiro;
    protected BigDecimal realizadaJaneiro;
    protected BigDecimal realizadaJulho;
    protected BigDecimal realizadaJunho;
    protected BigDecimal realizadaMaio;
    protected BigDecimal realizadaMarco;
    protected BigDecimal realizadaNovembro;
    protected BigDecimal realizadaOutubro;
    protected BigDecimal realizadaSetembro;
    protected String regiao;
    protected String subfuncao;
    protected String tituloAcao;
    protected String tituloLocalizador;
    protected String tituloPrograma;
    protected String uf;
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
     * Obtém o valor da propriedade descricaoSubfuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoSubfuncao() {
        return descricaoSubfuncao;
    }

    /**
     * Define o valor da propriedade descricaoSubfuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoSubfuncao(String value) {
        this.descricaoSubfuncao = value;
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
     * Obtém o valor da propriedade estatalIndependente.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstatalIndependente() {
        return estatalIndependente;
    }

    /**
     * Define o valor da propriedade estatalIndependente.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstatalIndependente(Boolean value) {
        this.estatalIndependente = value;
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
     * Obtém o valor da propriedade lei.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLei() {
        return lei;
    }

    /**
     * Define o valor da propriedade lei.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLei(BigDecimal value) {
        this.lei = value;
    }

    /**
     * Obtém o valor da propriedade leiMaisCreditos.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLeiMaisCreditos() {
        return leiMaisCreditos;
    }

    /**
     * Define o valor da propriedade leiMaisCreditos.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLeiMaisCreditos(BigDecimal value) {
        this.leiMaisCreditos = value;
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
     * Obtém o valor da propriedade ppipac.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPpipac() {
        return ppipac;
    }

    /**
     * Define o valor da propriedade ppipac.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPpipac(Boolean value) {
        this.ppipac = value;
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
     * Obtém o valor da propriedade realizadaAbril.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaAbril() {
        return realizadaAbril;
    }

    /**
     * Define o valor da propriedade realizadaAbril.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaAbril(BigDecimal value) {
        this.realizadaAbril = value;
    }

    /**
     * Obtém o valor da propriedade realizadaAgosto.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaAgosto() {
        return realizadaAgosto;
    }

    /**
     * Define o valor da propriedade realizadaAgosto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaAgosto(BigDecimal value) {
        this.realizadaAgosto = value;
    }

    /**
     * Obtém o valor da propriedade realizadaDezembro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaDezembro() {
        return realizadaDezembro;
    }

    /**
     * Define o valor da propriedade realizadaDezembro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaDezembro(BigDecimal value) {
        this.realizadaDezembro = value;
    }

    /**
     * Obtém o valor da propriedade realizadaFevereiro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaFevereiro() {
        return realizadaFevereiro;
    }

    /**
     * Define o valor da propriedade realizadaFevereiro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaFevereiro(BigDecimal value) {
        this.realizadaFevereiro = value;
    }

    /**
     * Obtém o valor da propriedade realizadaJaneiro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaJaneiro() {
        return realizadaJaneiro;
    }

    /**
     * Define o valor da propriedade realizadaJaneiro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaJaneiro(BigDecimal value) {
        this.realizadaJaneiro = value;
    }

    /**
     * Obtém o valor da propriedade realizadaJulho.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaJulho() {
        return realizadaJulho;
    }

    /**
     * Define o valor da propriedade realizadaJulho.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaJulho(BigDecimal value) {
        this.realizadaJulho = value;
    }

    /**
     * Obtém o valor da propriedade realizadaJunho.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaJunho() {
        return realizadaJunho;
    }

    /**
     * Define o valor da propriedade realizadaJunho.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaJunho(BigDecimal value) {
        this.realizadaJunho = value;
    }

    /**
     * Obtém o valor da propriedade realizadaMaio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaMaio() {
        return realizadaMaio;
    }

    /**
     * Define o valor da propriedade realizadaMaio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaMaio(BigDecimal value) {
        this.realizadaMaio = value;
    }

    /**
     * Obtém o valor da propriedade realizadaMarco.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaMarco() {
        return realizadaMarco;
    }

    /**
     * Define o valor da propriedade realizadaMarco.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaMarco(BigDecimal value) {
        this.realizadaMarco = value;
    }

    /**
     * Obtém o valor da propriedade realizadaNovembro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaNovembro() {
        return realizadaNovembro;
    }

    /**
     * Define o valor da propriedade realizadaNovembro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaNovembro(BigDecimal value) {
        this.realizadaNovembro = value;
    }

    /**
     * Obtém o valor da propriedade realizadaOutubro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaOutubro() {
        return realizadaOutubro;
    }

    /**
     * Define o valor da propriedade realizadaOutubro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaOutubro(BigDecimal value) {
        this.realizadaOutubro = value;
    }

    /**
     * Obtém o valor da propriedade realizadaSetembro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRealizadaSetembro() {
        return realizadaSetembro;
    }

    /**
     * Define o valor da propriedade realizadaSetembro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRealizadaSetembro(BigDecimal value) {
        this.realizadaSetembro = value;
    }

    /**
     * Obtém o valor da propriedade regiao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegiao() {
        return regiao;
    }

    /**
     * Define o valor da propriedade regiao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegiao(String value) {
        this.regiao = value;
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
     * Obtém o valor da propriedade tituloLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloLocalizador() {
        return tituloLocalizador;
    }

    /**
     * Define o valor da propriedade tituloLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloLocalizador(String value) {
        this.tituloLocalizador = value;
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
     * Obtém o valor da propriedade uf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUf() {
        return uf;
    }

    /**
     * Define o valor da propriedade uf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUf(String value) {
        this.uf = value;
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
