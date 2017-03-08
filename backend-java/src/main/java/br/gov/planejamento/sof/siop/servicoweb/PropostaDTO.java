
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de propostaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="propostaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoEsfera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoLocalizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoSubFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoTipoDetalhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoTipoInclusaoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoTipoInclusaoLocalizador" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="expansaoFisicaConcedida" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expansaoFisicaSolicitada" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="financeiros" type="{http://servicoweb.siop.sof.planejamento.gov.br/}financeiroDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="identificadorUnicoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="justificativa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="justificativaExpansaoConcedida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="justificativaExpansaoSolicitada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metaPlanoOrcamentario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}metaPlanoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="quantidadeFisico" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="receitas" type="{http://servicoweb.siop.sof.planejamento.gov.br/}receitaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="snAtual" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="valorFisico" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propostaDTO", propOrder = {
    "codigoAcao",
    "codigoEsfera",
    "codigoFuncao",
    "codigoLocalizador",
    "codigoMomento",
    "codigoOrgao",
    "codigoPrograma",
    "codigoSubFuncao",
    "codigoTipoDetalhamento",
    "codigoTipoInclusaoAcao",
    "codigoTipoInclusaoLocalizador",
    "exercicio",
    "expansaoFisicaConcedida",
    "expansaoFisicaSolicitada",
    "financeiros",
    "identificadorUnicoAcao",
    "justificativa",
    "justificativaExpansaoConcedida",
    "justificativaExpansaoSolicitada",
    "metaPlanoOrcamentario",
    "quantidadeFisico",
    "receitas",
    "snAtual",
    "valorFisico"
})
public class PropostaDTO
    extends BaseDTO
{

    protected String codigoAcao;
    protected String codigoEsfera;
    protected String codigoFuncao;
    protected String codigoLocalizador;
    protected Integer codigoMomento;
    protected String codigoOrgao;
    protected String codigoPrograma;
    protected String codigoSubFuncao;
    protected String codigoTipoDetalhamento;
    protected Integer codigoTipoInclusaoAcao;
    protected Integer codigoTipoInclusaoLocalizador;
    protected Integer exercicio;
    protected Long expansaoFisicaConcedida;
    protected Long expansaoFisicaSolicitada;
    @XmlElement(nillable = true)
    protected List<FinanceiroDTO> financeiros;
    protected Integer identificadorUnicoAcao;
    protected String justificativa;
    protected String justificativaExpansaoConcedida;
    protected String justificativaExpansaoSolicitada;
    @XmlElement(nillable = true)
    protected List<MetaPlanoOrcamentarioDTO> metaPlanoOrcamentario;
    protected Long quantidadeFisico;
    @XmlElement(nillable = true)
    protected List<ReceitaDTO> receitas;
    protected Boolean snAtual;
    protected Long valorFisico;

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
     * Obtém o valor da propriedade codigoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoLocalizador() {
        return codigoLocalizador;
    }

    /**
     * Define o valor da propriedade codigoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoLocalizador(String value) {
        this.codigoLocalizador = value;
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
     * Obtém o valor da propriedade codigoTipoInclusaoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoTipoInclusaoLocalizador() {
        return codigoTipoInclusaoLocalizador;
    }

    /**
     * Define o valor da propriedade codigoTipoInclusaoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoTipoInclusaoLocalizador(Integer value) {
        this.codigoTipoInclusaoLocalizador = value;
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
     * Obtém o valor da propriedade expansaoFisicaConcedida.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpansaoFisicaConcedida() {
        return expansaoFisicaConcedida;
    }

    /**
     * Define o valor da propriedade expansaoFisicaConcedida.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpansaoFisicaConcedida(Long value) {
        this.expansaoFisicaConcedida = value;
    }

    /**
     * Obtém o valor da propriedade expansaoFisicaSolicitada.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpansaoFisicaSolicitada() {
        return expansaoFisicaSolicitada;
    }

    /**
     * Define o valor da propriedade expansaoFisicaSolicitada.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpansaoFisicaSolicitada(Long value) {
        this.expansaoFisicaSolicitada = value;
    }

    /**
     * Gets the value of the financeiros property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the financeiros property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinanceiros().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FinanceiroDTO }
     * 
     * 
     */
    public List<FinanceiroDTO> getFinanceiros() {
        if (financeiros == null) {
            financeiros = new ArrayList<FinanceiroDTO>();
        }
        return this.financeiros;
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
     * Obtém o valor da propriedade justificativa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificativa() {
        return justificativa;
    }

    /**
     * Define o valor da propriedade justificativa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificativa(String value) {
        this.justificativa = value;
    }

    /**
     * Obtém o valor da propriedade justificativaExpansaoConcedida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificativaExpansaoConcedida() {
        return justificativaExpansaoConcedida;
    }

    /**
     * Define o valor da propriedade justificativaExpansaoConcedida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificativaExpansaoConcedida(String value) {
        this.justificativaExpansaoConcedida = value;
    }

    /**
     * Obtém o valor da propriedade justificativaExpansaoSolicitada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificativaExpansaoSolicitada() {
        return justificativaExpansaoSolicitada;
    }

    /**
     * Define o valor da propriedade justificativaExpansaoSolicitada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificativaExpansaoSolicitada(String value) {
        this.justificativaExpansaoSolicitada = value;
    }

    /**
     * Gets the value of the metaPlanoOrcamentario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metaPlanoOrcamentario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetaPlanoOrcamentario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetaPlanoOrcamentarioDTO }
     * 
     * 
     */
    public List<MetaPlanoOrcamentarioDTO> getMetaPlanoOrcamentario() {
        if (metaPlanoOrcamentario == null) {
            metaPlanoOrcamentario = new ArrayList<MetaPlanoOrcamentarioDTO>();
        }
        return this.metaPlanoOrcamentario;
    }

    /**
     * Obtém o valor da propriedade quantidadeFisico.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getQuantidadeFisico() {
        return quantidadeFisico;
    }

    /**
     * Define o valor da propriedade quantidadeFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setQuantidadeFisico(Long value) {
        this.quantidadeFisico = value;
    }

    /**
     * Gets the value of the receitas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receitas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceitas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceitaDTO }
     * 
     * 
     */
    public List<ReceitaDTO> getReceitas() {
        if (receitas == null) {
            receitas = new ArrayList<ReceitaDTO>();
        }
        return this.receitas;
    }

    /**
     * Obtém o valor da propriedade snAtual.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAtual() {
        return snAtual;
    }

    /**
     * Define o valor da propriedade snAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAtual(Boolean value) {
        this.snAtual = value;
    }

    /**
     * Obtém o valor da propriedade valorFisico.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getValorFisico() {
        return valorFisico;
    }

    /**
     * Define o valor da propriedade valorFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setValorFisico(Long value) {
        this.valorFisico = value;
    }

}
