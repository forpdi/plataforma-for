
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de acompanhamentoOrcamentarioAcaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="acompanhamentoOrcamentarioAcaoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="periodoOrdem" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subFuncao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigoTipoInclusaoAcao" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="snPendencia" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dataHoraAlteracao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="acompanhamentosLocalizadores">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="acompanhamentoLocalizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoOrcamentarioLocalizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acompanhamentoOrcamentarioAcaoDTO", propOrder = {
    "periodoOrdem",
    "exercicio",
    "codigoMomento",
    "esfera",
    "unidadeOrcamentaria",
    "funcao",
    "subFuncao",
    "programa",
    "acao",
    "codigoTipoInclusaoAcao",
    "snPendencia",
    "dataHoraAlteracao",
    "acompanhamentosLocalizadores"
})
public class AcompanhamentoOrcamentarioAcaoDTO
    extends BaseDTO
{

    protected int periodoOrdem;
    protected int exercicio;
    protected int codigoMomento;
    @XmlElement(required = true)
    protected String esfera;
    @XmlElement(required = true)
    protected String unidadeOrcamentaria;
    @XmlElement(required = true)
    protected String funcao;
    @XmlElement(required = true)
    protected String subFuncao;
    @XmlElement(required = true)
    protected String programa;
    @XmlElement(required = true)
    protected String acao;
    protected int codigoTipoInclusaoAcao;
    protected Boolean snPendencia;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraAlteracao;
    @XmlElement(required = true)
    protected AcompanhamentoOrcamentarioAcaoDTO.AcompanhamentosLocalizadores acompanhamentosLocalizadores;

    /**
     * Obtém o valor da propriedade periodoOrdem.
     * 
     */
    public int getPeriodoOrdem() {
        return periodoOrdem;
    }

    /**
     * Define o valor da propriedade periodoOrdem.
     * 
     */
    public void setPeriodoOrdem(int value) {
        this.periodoOrdem = value;
    }

    /**
     * Obtém o valor da propriedade exercicio.
     * 
     */
    public int getExercicio() {
        return exercicio;
    }

    /**
     * Define o valor da propriedade exercicio.
     * 
     */
    public void setExercicio(int value) {
        this.exercicio = value;
    }

    /**
     * Obtém o valor da propriedade codigoMomento.
     * 
     */
    public int getCodigoMomento() {
        return codigoMomento;
    }

    /**
     * Define o valor da propriedade codigoMomento.
     * 
     */
    public void setCodigoMomento(int value) {
        this.codigoMomento = value;
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
     * Obtém o valor da propriedade subFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubFuncao() {
        return subFuncao;
    }

    /**
     * Define o valor da propriedade subFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubFuncao(String value) {
        this.subFuncao = value;
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
     * Obtém o valor da propriedade codigoTipoInclusaoAcao.
     * 
     */
    public int getCodigoTipoInclusaoAcao() {
        return codigoTipoInclusaoAcao;
    }

    /**
     * Define o valor da propriedade codigoTipoInclusaoAcao.
     * 
     */
    public void setCodigoTipoInclusaoAcao(int value) {
        this.codigoTipoInclusaoAcao = value;
    }

    /**
     * Obtém o valor da propriedade snPendencia.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnPendencia() {
        return snPendencia;
    }

    /**
     * Define o valor da propriedade snPendencia.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnPendencia(Boolean value) {
        this.snPendencia = value;
    }

    /**
     * Obtém o valor da propriedade dataHoraAlteracao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraAlteracao() {
        return dataHoraAlteracao;
    }

    /**
     * Define o valor da propriedade dataHoraAlteracao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraAlteracao(XMLGregorianCalendar value) {
        this.dataHoraAlteracao = value;
    }

    /**
     * Obtém o valor da propriedade acompanhamentosLocalizadores.
     * 
     * @return
     *     possible object is
     *     {@link AcompanhamentoOrcamentarioAcaoDTO.AcompanhamentosLocalizadores }
     *     
     */
    public AcompanhamentoOrcamentarioAcaoDTO.AcompanhamentosLocalizadores getAcompanhamentosLocalizadores() {
        return acompanhamentosLocalizadores;
    }

    /**
     * Define o valor da propriedade acompanhamentosLocalizadores.
     * 
     * @param value
     *     allowed object is
     *     {@link AcompanhamentoOrcamentarioAcaoDTO.AcompanhamentosLocalizadores }
     *     
     */
    public void setAcompanhamentosLocalizadores(AcompanhamentoOrcamentarioAcaoDTO.AcompanhamentosLocalizadores value) {
        this.acompanhamentosLocalizadores = value;
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
     *         &lt;element name="acompanhamentoLocalizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoOrcamentarioLocalizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "acompanhamentoLocalizador"
    })
    public static class AcompanhamentosLocalizadores {

        protected List<AcompanhamentoOrcamentarioLocalizadorDTO> acompanhamentoLocalizador;

        /**
         * Gets the value of the acompanhamentoLocalizador property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acompanhamentoLocalizador property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcompanhamentoLocalizador().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AcompanhamentoOrcamentarioLocalizadorDTO }
         * 
         * 
         */
        public List<AcompanhamentoOrcamentarioLocalizadorDTO> getAcompanhamentoLocalizador() {
            if (acompanhamentoLocalizador == null) {
                acompanhamentoLocalizador = new ArrayList<AcompanhamentoOrcamentarioLocalizadorDTO>();
            }
            return this.acompanhamentoLocalizador;
        }

    }

}
