
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de financeiroDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="financeiroDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoPlanoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expansaoConcedida" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expansaoSolicitada" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorPlanoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="naturezaDespesa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultadoPrimarioAtual" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultadoPrimarioLei" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "financeiroDTO", propOrder = {
    "codigoPlanoOrcamentario",
    "expansaoConcedida",
    "expansaoSolicitada",
    "fonte",
    "idOC",
    "idUso",
    "identificadorPlanoOrcamentario",
    "naturezaDespesa",
    "resultadoPrimarioAtual",
    "resultadoPrimarioLei",
    "valor"
})
@XmlSeeAlso({
    FinanceiroEmendasDTO.class
})
public class FinanceiroDTO
    extends BaseDTO
{

    protected String codigoPlanoOrcamentario;
    protected Long expansaoConcedida;
    protected Long expansaoSolicitada;
    protected String fonte;
    protected String idOC;
    protected String idUso;
    protected Integer identificadorPlanoOrcamentario;
    protected String naturezaDespesa;
    protected String resultadoPrimarioAtual;
    protected String resultadoPrimarioLei;
    protected Long valor;

    /**
     * Obtém o valor da propriedade codigoPlanoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPlanoOrcamentario() {
        return codigoPlanoOrcamentario;
    }

    /**
     * Define o valor da propriedade codigoPlanoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPlanoOrcamentario(String value) {
        this.codigoPlanoOrcamentario = value;
    }

    /**
     * Obtém o valor da propriedade expansaoConcedida.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpansaoConcedida() {
        return expansaoConcedida;
    }

    /**
     * Define o valor da propriedade expansaoConcedida.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpansaoConcedida(Long value) {
        this.expansaoConcedida = value;
    }

    /**
     * Obtém o valor da propriedade expansaoSolicitada.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpansaoSolicitada() {
        return expansaoSolicitada;
    }

    /**
     * Define o valor da propriedade expansaoSolicitada.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpansaoSolicitada(Long value) {
        this.expansaoSolicitada = value;
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
     * Obtém o valor da propriedade idOC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOC() {
        return idOC;
    }

    /**
     * Define o valor da propriedade idOC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOC(String value) {
        this.idOC = value;
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
     * Obtém o valor da propriedade identificadorPlanoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorPlanoOrcamentario() {
        return identificadorPlanoOrcamentario;
    }

    /**
     * Define o valor da propriedade identificadorPlanoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorPlanoOrcamentario(Integer value) {
        this.identificadorPlanoOrcamentario = value;
    }

    /**
     * Obtém o valor da propriedade naturezaDespesa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaturezaDespesa() {
        return naturezaDespesa;
    }

    /**
     * Define o valor da propriedade naturezaDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaturezaDespesa(String value) {
        this.naturezaDespesa = value;
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
     * Obtém o valor da propriedade valor.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getValor() {
        return valor;
    }

    /**
     * Define o valor da propriedade valor.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setValor(Long value) {
        this.valor = value;
    }

}
