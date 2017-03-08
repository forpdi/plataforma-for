
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de informacaoCaptacaoPLOADTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="informacaoCaptacaoPLOADTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoMomentoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomentoJanelaAtual" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomentoLocalizador" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoMomentoPropostaAtual" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoTipoDetalhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="funcional" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorUnicoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="identificadorUnicoLocalizador" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="podeCaptar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="porQueNaoPodeCaptar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propostaValida" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="temJanela" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="temProposta" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informacaoCaptacaoPLOADTO", propOrder = {
    "codigoMomentoAcao",
    "codigoMomentoJanelaAtual",
    "codigoMomentoLocalizador",
    "codigoMomentoPropostaAtual",
    "codigoTipoDetalhamento",
    "exercicio",
    "funcional",
    "identificadorUnicoAcao",
    "identificadorUnicoLocalizador",
    "podeCaptar",
    "porQueNaoPodeCaptar",
    "propostaValida",
    "temJanela",
    "temProposta"
})
public class InformacaoCaptacaoPLOADTO {

    protected Integer codigoMomentoAcao;
    protected Integer codigoMomentoJanelaAtual;
    protected Integer codigoMomentoLocalizador;
    protected Integer codigoMomentoPropostaAtual;
    protected String codigoTipoDetalhamento;
    protected Integer exercicio;
    protected String funcional;
    protected Integer identificadorUnicoAcao;
    protected Integer identificadorUnicoLocalizador;
    protected Boolean podeCaptar;
    protected String porQueNaoPodeCaptar;
    protected Boolean propostaValida;
    protected Boolean temJanela;
    protected Boolean temProposta;

    /**
     * Obtém o valor da propriedade codigoMomentoAcao.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoMomentoAcao() {
        return codigoMomentoAcao;
    }

    /**
     * Define o valor da propriedade codigoMomentoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoMomentoAcao(Integer value) {
        this.codigoMomentoAcao = value;
    }

    /**
     * Obtém o valor da propriedade codigoMomentoJanelaAtual.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoMomentoJanelaAtual() {
        return codigoMomentoJanelaAtual;
    }

    /**
     * Define o valor da propriedade codigoMomentoJanelaAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoMomentoJanelaAtual(Integer value) {
        this.codigoMomentoJanelaAtual = value;
    }

    /**
     * Obtém o valor da propriedade codigoMomentoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoMomentoLocalizador() {
        return codigoMomentoLocalizador;
    }

    /**
     * Define o valor da propriedade codigoMomentoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoMomentoLocalizador(Integer value) {
        this.codigoMomentoLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade codigoMomentoPropostaAtual.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoMomentoPropostaAtual() {
        return codigoMomentoPropostaAtual;
    }

    /**
     * Define o valor da propriedade codigoMomentoPropostaAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoMomentoPropostaAtual(Integer value) {
        this.codigoMomentoPropostaAtual = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoDetalhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTipoDetalhamento() {
        return codigoTipoDetalhamento;
    }

    /**
     * Define o valor da propriedade codigoTipoDetalhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTipoDetalhamento(String value) {
        this.codigoTipoDetalhamento = value;
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
     * Obtém o valor da propriedade funcional.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuncional() {
        return funcional;
    }

    /**
     * Define o valor da propriedade funcional.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuncional(String value) {
        this.funcional = value;
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
     * Obtém o valor da propriedade identificadorUnicoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorUnicoLocalizador() {
        return identificadorUnicoLocalizador;
    }

    /**
     * Define o valor da propriedade identificadorUnicoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorUnicoLocalizador(Integer value) {
        this.identificadorUnicoLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade podeCaptar.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPodeCaptar() {
        return podeCaptar;
    }

    /**
     * Define o valor da propriedade podeCaptar.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPodeCaptar(Boolean value) {
        this.podeCaptar = value;
    }

    /**
     * Obtém o valor da propriedade porQueNaoPodeCaptar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPorQueNaoPodeCaptar() {
        return porQueNaoPodeCaptar;
    }

    /**
     * Define o valor da propriedade porQueNaoPodeCaptar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPorQueNaoPodeCaptar(String value) {
        this.porQueNaoPodeCaptar = value;
    }

    /**
     * Obtém o valor da propriedade propostaValida.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPropostaValida() {
        return propostaValida;
    }

    /**
     * Define o valor da propriedade propostaValida.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPropostaValida(Boolean value) {
        this.propostaValida = value;
    }

    /**
     * Obtém o valor da propriedade temJanela.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTemJanela() {
        return temJanela;
    }

    /**
     * Define o valor da propriedade temJanela.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTemJanela(Boolean value) {
        this.temJanela = value;
    }

    /**
     * Obtém o valor da propriedade temProposta.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTemProposta() {
        return temProposta;
    }

    /**
     * Define o valor da propriedade temProposta.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTemProposta(Boolean value) {
        this.temProposta = value;
    }

}
