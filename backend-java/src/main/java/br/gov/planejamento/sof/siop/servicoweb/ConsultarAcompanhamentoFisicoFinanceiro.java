
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de consultarAcompanhamentoFisicoFinanceiro complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="consultarAcompanhamentoFisicoFinanceiro">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="periodo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="momentoId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tipoCaptacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paginacao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}paginacaoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarAcompanhamentoFisicoFinanceiro", propOrder = {
    "credencial",
    "exercicio",
    "periodo",
    "momentoId",
    "tipoCaptacao",
    "paginacao"
})
public class ConsultarAcompanhamentoFisicoFinanceiro {

    protected CredencialDTO credencial;
    protected Integer exercicio;
    protected Integer periodo;
    protected Integer momentoId;
    protected String tipoCaptacao;
    protected PaginacaoDTO paginacao;

    /**
     * Obtém o valor da propriedade credencial.
     * 
     * @return
     *     possible object is
     *     {@link CredencialDTO }
     *     
     */
    public CredencialDTO getCredencial() {
        return credencial;
    }

    /**
     * Define o valor da propriedade credencial.
     * 
     * @param value
     *     allowed object is
     *     {@link CredencialDTO }
     *     
     */
    public void setCredencial(CredencialDTO value) {
        this.credencial = value;
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
     * Obtém o valor da propriedade periodo.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPeriodo() {
        return periodo;
    }

    /**
     * Define o valor da propriedade periodo.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPeriodo(Integer value) {
        this.periodo = value;
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
     * Obtém o valor da propriedade paginacao.
     * 
     * @return
     *     possible object is
     *     {@link PaginacaoDTO }
     *     
     */
    public PaginacaoDTO getPaginacao() {
        return paginacao;
    }

    /**
     * Define o valor da propriedade paginacao.
     * 
     * @param value
     *     allowed object is
     *     {@link PaginacaoDTO }
     *     
     */
    public void setPaginacao(PaginacaoDTO value) {
        this.paginacao = value;
    }

}
