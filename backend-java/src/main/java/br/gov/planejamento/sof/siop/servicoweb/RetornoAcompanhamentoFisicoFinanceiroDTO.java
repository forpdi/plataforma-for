
package br.gov.planejamento.sof.siop.servicoweb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de retornoAcompanhamentoFisicoFinanceiroDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoAcompanhamentoFisicoFinanceiroDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="dataHoraUltimaCargaSiafi" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="paginacao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}paginacaoDTO" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaAtual" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaLOA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="quantidadeMetaLOAPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadoLOA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadoPO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="realizadoRAP" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="registros" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoFisicoFinanceiroDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reprogramadoFisico" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoAcompanhamentoFisicoFinanceiroDTO", propOrder = {
    "dataHoraUltimaCargaSiafi",
    "paginacao",
    "quantidadeMetaAtual",
    "quantidadeMetaLOA",
    "quantidadeMetaLOAPO",
    "realizadoLOA",
    "realizadoPO",
    "realizadoRAP",
    "registros",
    "reprogramadoFisico"
})
public class RetornoAcompanhamentoFisicoFinanceiroDTO
    extends RetornoDTO
{

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraUltimaCargaSiafi;
    protected PaginacaoDTO paginacao;
    protected BigDecimal quantidadeMetaAtual;
    protected BigDecimal quantidadeMetaLOA;
    protected BigDecimal quantidadeMetaLOAPO;
    protected BigDecimal realizadoLOA;
    protected BigDecimal realizadoPO;
    protected BigDecimal realizadoRAP;
    @XmlElement(nillable = true)
    protected List<AcompanhamentoFisicoFinanceiroDTO> registros;
    protected BigDecimal reprogramadoFisico;

    /**
     * Obtém o valor da propriedade dataHoraUltimaCargaSiafi.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraUltimaCargaSiafi() {
        return dataHoraUltimaCargaSiafi;
    }

    /**
     * Define o valor da propriedade dataHoraUltimaCargaSiafi.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraUltimaCargaSiafi(XMLGregorianCalendar value) {
        this.dataHoraUltimaCargaSiafi = value;
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
     * Gets the value of the registros property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registros property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegistros().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcompanhamentoFisicoFinanceiroDTO }
     * 
     * 
     */
    public List<AcompanhamentoFisicoFinanceiroDTO> getRegistros() {
        if (registros == null) {
            registros = new ArrayList<AcompanhamentoFisicoFinanceiroDTO>();
        }
        return this.registros;
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

}
