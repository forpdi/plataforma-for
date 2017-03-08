
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoDespesaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoDespesaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoIdUso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoModalidade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoNatureza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoRP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dotacaoAtual" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="dotacaoInicial" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="execucaoMensal" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoMensalDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fisicoAtual" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fisicoInicial" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoDespesaDTO", propOrder = {
    "codigoIdUso",
    "codigoModalidade",
    "codigoNatureza",
    "codigoRP",
    "dotacaoAtual",
    "dotacaoInicial",
    "execucaoMensal",
    "fisicoAtual",
    "fisicoInicial"
})
public class ExecucaoDespesaDTO
    extends BaseDTO
{

    protected String codigoIdUso;
    protected String codigoModalidade;
    protected String codigoNatureza;
    protected String codigoRP;
    protected Long dotacaoAtual;
    protected Long dotacaoInicial;
    @XmlElement(nillable = true)
    protected List<ExecucaoMensalDTO> execucaoMensal;
    protected Long fisicoAtual;
    protected Long fisicoInicial;

    /**
     * Obtém o valor da propriedade codigoIdUso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoIdUso() {
        return codigoIdUso;
    }

    /**
     * Define o valor da propriedade codigoIdUso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoIdUso(String value) {
        this.codigoIdUso = value;
    }

    /**
     * Obtém o valor da propriedade codigoModalidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoModalidade() {
        return codigoModalidade;
    }

    /**
     * Define o valor da propriedade codigoModalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoModalidade(String value) {
        this.codigoModalidade = value;
    }

    /**
     * Obtém o valor da propriedade codigoNatureza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoNatureza() {
        return codigoNatureza;
    }

    /**
     * Define o valor da propriedade codigoNatureza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoNatureza(String value) {
        this.codigoNatureza = value;
    }

    /**
     * Obtém o valor da propriedade codigoRP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoRP() {
        return codigoRP;
    }

    /**
     * Define o valor da propriedade codigoRP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoRP(String value) {
        this.codigoRP = value;
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
     * Obtém o valor da propriedade dotacaoInicial.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDotacaoInicial() {
        return dotacaoInicial;
    }

    /**
     * Define o valor da propriedade dotacaoInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDotacaoInicial(Long value) {
        this.dotacaoInicial = value;
    }

    /**
     * Gets the value of the execucaoMensal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the execucaoMensal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExecucaoMensal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecucaoMensalDTO }
     * 
     * 
     */
    public List<ExecucaoMensalDTO> getExecucaoMensal() {
        if (execucaoMensal == null) {
            execucaoMensal = new ArrayList<ExecucaoMensalDTO>();
        }
        return this.execucaoMensal;
    }

    /**
     * Obtém o valor da propriedade fisicoAtual.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFisicoAtual() {
        return fisicoAtual;
    }

    /**
     * Define o valor da propriedade fisicoAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFisicoAtual(Long value) {
        this.fisicoAtual = value;
    }

    /**
     * Obtém o valor da propriedade fisicoInicial.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFisicoInicial() {
        return fisicoInicial;
    }

    /**
     * Define o valor da propriedade fisicoInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFisicoInicial(Long value) {
        this.fisicoInicial = value;
    }

}
