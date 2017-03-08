
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de financiamentoExtraOrcamentarioDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="financiamentoExtraOrcamentarioDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoFinanciamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoOrgaoSiorg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custoTotal" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="dataInicio" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dataTermino" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="fonteFinanciamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorIniciativa" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="identificadorObjetivo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="identificadorUnico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="outraFonteFinanciamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="produto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snProjeto" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="valorAno1Ppa" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="valorAno2Ppa" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="valorAno3Ppa" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="valorAno4Ppa" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="valorTotal" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "financiamentoExtraOrcamentarioDTO", propOrder = {
    "codigoFinanciamento",
    "codigoMomento",
    "codigoOrgaoSiorg",
    "custoTotal",
    "dataInicio",
    "dataTermino",
    "descricao",
    "exercicio",
    "fonteFinanciamento",
    "identificadorIniciativa",
    "identificadorObjetivo",
    "identificadorUnico",
    "outraFonteFinanciamento",
    "produto",
    "snProjeto",
    "valorAno1Ppa",
    "valorAno2Ppa",
    "valorAno3Ppa",
    "valorAno4Ppa",
    "valorTotal"
})
public class FinanciamentoExtraOrcamentarioDTO
    extends BaseDTO
{

    protected String codigoFinanciamento;
    protected Integer codigoMomento;
    protected String codigoOrgaoSiorg;
    protected Double custoTotal;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInicio;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataTermino;
    protected String descricao;
    protected Integer exercicio;
    protected String fonteFinanciamento;
    protected Integer identificadorIniciativa;
    protected Integer identificadorObjetivo;
    protected Integer identificadorUnico;
    protected String outraFonteFinanciamento;
    protected String produto;
    protected Boolean snProjeto;
    protected Double valorAno1Ppa;
    protected Double valorAno2Ppa;
    protected Double valorAno3Ppa;
    protected Double valorAno4Ppa;
    protected Double valorTotal;

    /**
     * Obtém o valor da propriedade codigoFinanciamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoFinanciamento() {
        return codigoFinanciamento;
    }

    /**
     * Define o valor da propriedade codigoFinanciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoFinanciamento(String value) {
        this.codigoFinanciamento = value;
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
     * Obtém o valor da propriedade codigoOrgaoSiorg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrgaoSiorg() {
        return codigoOrgaoSiorg;
    }

    /**
     * Define o valor da propriedade codigoOrgaoSiorg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrgaoSiorg(String value) {
        this.codigoOrgaoSiorg = value;
    }

    /**
     * Obtém o valor da propriedade custoTotal.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getCustoTotal() {
        return custoTotal;
    }

    /**
     * Define o valor da propriedade custoTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setCustoTotal(Double value) {
        this.custoTotal = value;
    }

    /**
     * Obtém o valor da propriedade dataInicio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInicio() {
        return dataInicio;
    }

    /**
     * Define o valor da propriedade dataInicio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInicio(XMLGregorianCalendar value) {
        this.dataInicio = value;
    }

    /**
     * Obtém o valor da propriedade dataTermino.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataTermino() {
        return dataTermino;
    }

    /**
     * Define o valor da propriedade dataTermino.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataTermino(XMLGregorianCalendar value) {
        this.dataTermino = value;
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
     * Obtém o valor da propriedade fonteFinanciamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFonteFinanciamento() {
        return fonteFinanciamento;
    }

    /**
     * Define o valor da propriedade fonteFinanciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFonteFinanciamento(String value) {
        this.fonteFinanciamento = value;
    }

    /**
     * Obtém o valor da propriedade identificadorIniciativa.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorIniciativa() {
        return identificadorIniciativa;
    }

    /**
     * Define o valor da propriedade identificadorIniciativa.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorIniciativa(Integer value) {
        this.identificadorIniciativa = value;
    }

    /**
     * Obtém o valor da propriedade identificadorObjetivo.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorObjetivo() {
        return identificadorObjetivo;
    }

    /**
     * Define o valor da propriedade identificadorObjetivo.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorObjetivo(Integer value) {
        this.identificadorObjetivo = value;
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
     * Obtém o valor da propriedade outraFonteFinanciamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutraFonteFinanciamento() {
        return outraFonteFinanciamento;
    }

    /**
     * Define o valor da propriedade outraFonteFinanciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutraFonteFinanciamento(String value) {
        this.outraFonteFinanciamento = value;
    }

    /**
     * Obtém o valor da propriedade produto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduto() {
        return produto;
    }

    /**
     * Define o valor da propriedade produto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduto(String value) {
        this.produto = value;
    }

    /**
     * Obtém o valor da propriedade snProjeto.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnProjeto() {
        return snProjeto;
    }

    /**
     * Define o valor da propriedade snProjeto.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnProjeto(Boolean value) {
        this.snProjeto = value;
    }

    /**
     * Obtém o valor da propriedade valorAno1Ppa.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValorAno1Ppa() {
        return valorAno1Ppa;
    }

    /**
     * Define o valor da propriedade valorAno1Ppa.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValorAno1Ppa(Double value) {
        this.valorAno1Ppa = value;
    }

    /**
     * Obtém o valor da propriedade valorAno2Ppa.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValorAno2Ppa() {
        return valorAno2Ppa;
    }

    /**
     * Define o valor da propriedade valorAno2Ppa.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValorAno2Ppa(Double value) {
        this.valorAno2Ppa = value;
    }

    /**
     * Obtém o valor da propriedade valorAno3Ppa.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValorAno3Ppa() {
        return valorAno3Ppa;
    }

    /**
     * Define o valor da propriedade valorAno3Ppa.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValorAno3Ppa(Double value) {
        this.valorAno3Ppa = value;
    }

    /**
     * Obtém o valor da propriedade valorAno4Ppa.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValorAno4Ppa() {
        return valorAno4Ppa;
    }

    /**
     * Define o valor da propriedade valorAno4Ppa.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValorAno4Ppa(Double value) {
        this.valorAno4Ppa = value;
    }

    /**
     * Obtém o valor da propriedade valorTotal.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValorTotal() {
        return valorTotal;
    }

    /**
     * Define o valor da propriedade valorTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValorTotal(Double value) {
        this.valorTotal = value;
    }

}
