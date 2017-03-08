
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de analiseAcompanhamentoOrcamentarioDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="analiseAcompanhamentoOrcamentarioDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="analise" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comentarioId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nomeUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="periodoOrdem" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ultimaModificacao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "analiseAcompanhamentoOrcamentarioDTO", propOrder = {
    "analise",
    "comentarioId",
    "nomeUsuario",
    "periodoOrdem",
    "ultimaModificacao"
})
public class AnaliseAcompanhamentoOrcamentarioDTO
    extends BaseDTO
{

    protected String analise;
    protected Integer comentarioId;
    protected String nomeUsuario;
    protected Integer periodoOrdem;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar ultimaModificacao;

    /**
     * Obtém o valor da propriedade analise.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalise() {
        return analise;
    }

    /**
     * Define o valor da propriedade analise.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalise(String value) {
        this.analise = value;
    }

    /**
     * Obtém o valor da propriedade comentarioId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getComentarioId() {
        return comentarioId;
    }

    /**
     * Define o valor da propriedade comentarioId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setComentarioId(Integer value) {
        this.comentarioId = value;
    }

    /**
     * Obtém o valor da propriedade nomeUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    /**
     * Define o valor da propriedade nomeUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeUsuario(String value) {
        this.nomeUsuario = value;
    }

    /**
     * Obtém o valor da propriedade periodoOrdem.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPeriodoOrdem() {
        return periodoOrdem;
    }

    /**
     * Define o valor da propriedade periodoOrdem.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPeriodoOrdem(Integer value) {
        this.periodoOrdem = value;
    }

    /**
     * Obtém o valor da propriedade ultimaModificacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUltimaModificacao() {
        return ultimaModificacao;
    }

    /**
     * Define o valor da propriedade ultimaModificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUltimaModificacao(XMLGregorianCalendar value) {
        this.ultimaModificacao = value;
    }

}
