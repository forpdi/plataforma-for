
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de retornoExecucaoOrcamentariaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoExecucaoOrcamentariaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="dataHoraUltimaCarga" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="execucoesOrcamentarias" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="execucaoOrcamentaria" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoOrcamentariaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="paginacao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}paginacaoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoExecucaoOrcamentariaDTO", propOrder = {
    "dataHoraUltimaCarga",
    "execucoesOrcamentarias",
    "paginacao"
})
public class RetornoExecucaoOrcamentariaDTO
    extends RetornoDTO
{

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraUltimaCarga;
    protected RetornoExecucaoOrcamentariaDTO.ExecucoesOrcamentarias execucoesOrcamentarias;
    protected PaginacaoDTO paginacao;

    /**
     * Obtém o valor da propriedade dataHoraUltimaCarga.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraUltimaCarga() {
        return dataHoraUltimaCarga;
    }

    /**
     * Define o valor da propriedade dataHoraUltimaCarga.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraUltimaCarga(XMLGregorianCalendar value) {
        this.dataHoraUltimaCarga = value;
    }

    /**
     * Obtém o valor da propriedade execucoesOrcamentarias.
     * 
     * @return
     *     possible object is
     *     {@link RetornoExecucaoOrcamentariaDTO.ExecucoesOrcamentarias }
     *     
     */
    public RetornoExecucaoOrcamentariaDTO.ExecucoesOrcamentarias getExecucoesOrcamentarias() {
        return execucoesOrcamentarias;
    }

    /**
     * Define o valor da propriedade execucoesOrcamentarias.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoExecucaoOrcamentariaDTO.ExecucoesOrcamentarias }
     *     
     */
    public void setExecucoesOrcamentarias(RetornoExecucaoOrcamentariaDTO.ExecucoesOrcamentarias value) {
        this.execucoesOrcamentarias = value;
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
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="execucaoOrcamentaria" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoOrcamentariaDTO" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "execucaoOrcamentaria"
    })
    public static class ExecucoesOrcamentarias {

        protected List<ExecucaoOrcamentariaDTO> execucaoOrcamentaria;

        /**
         * Gets the value of the execucaoOrcamentaria property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the execucaoOrcamentaria property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExecucaoOrcamentaria().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ExecucaoOrcamentariaDTO }
         * 
         * 
         */
        public List<ExecucaoOrcamentariaDTO> getExecucaoOrcamentaria() {
            if (execucaoOrcamentaria == null) {
                execucaoOrcamentaria = new ArrayList<ExecucaoOrcamentariaDTO>();
            }
            return this.execucaoOrcamentaria;
        }

    }

}
