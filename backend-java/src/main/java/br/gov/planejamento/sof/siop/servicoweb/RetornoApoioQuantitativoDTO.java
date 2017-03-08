
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoApoioQuantitativoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoApoioQuantitativoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="idocs" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="idoc" type="{http://servicoweb.siop.sof.planejamento.gov.br/}idOcDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="idusos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="iduso" type="{http://servicoweb.siop.sof.planejamento.gov.br/}idUsoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="fontes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="fonte" type="{http://servicoweb.siop.sof.planejamento.gov.br/}fonteDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resultadosPrimarios" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="resultadoPrimario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}resultadoPrimarioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="naturezas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="natureza" type="{http://servicoweb.siop.sof.planejamento.gov.br/}naturezaDespesaDTO" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "retornoApoioQuantitativoDTO", propOrder = {
    "idocs",
    "idusos",
    "fontes",
    "resultadosPrimarios",
    "naturezas"
})
public class RetornoApoioQuantitativoDTO
    extends RetornoDTO
{

    protected RetornoApoioQuantitativoDTO.Idocs idocs;
    protected RetornoApoioQuantitativoDTO.Idusos idusos;
    protected RetornoApoioQuantitativoDTO.Fontes fontes;
    protected RetornoApoioQuantitativoDTO.ResultadosPrimarios resultadosPrimarios;
    protected RetornoApoioQuantitativoDTO.Naturezas naturezas;

    /**
     * Obtém o valor da propriedade idocs.
     * 
     * @return
     *     possible object is
     *     {@link RetornoApoioQuantitativoDTO.Idocs }
     *     
     */
    public RetornoApoioQuantitativoDTO.Idocs getIdocs() {
        return idocs;
    }

    /**
     * Define o valor da propriedade idocs.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoApoioQuantitativoDTO.Idocs }
     *     
     */
    public void setIdocs(RetornoApoioQuantitativoDTO.Idocs value) {
        this.idocs = value;
    }

    /**
     * Obtém o valor da propriedade idusos.
     * 
     * @return
     *     possible object is
     *     {@link RetornoApoioQuantitativoDTO.Idusos }
     *     
     */
    public RetornoApoioQuantitativoDTO.Idusos getIdusos() {
        return idusos;
    }

    /**
     * Define o valor da propriedade idusos.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoApoioQuantitativoDTO.Idusos }
     *     
     */
    public void setIdusos(RetornoApoioQuantitativoDTO.Idusos value) {
        this.idusos = value;
    }

    /**
     * Obtém o valor da propriedade fontes.
     * 
     * @return
     *     possible object is
     *     {@link RetornoApoioQuantitativoDTO.Fontes }
     *     
     */
    public RetornoApoioQuantitativoDTO.Fontes getFontes() {
        return fontes;
    }

    /**
     * Define o valor da propriedade fontes.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoApoioQuantitativoDTO.Fontes }
     *     
     */
    public void setFontes(RetornoApoioQuantitativoDTO.Fontes value) {
        this.fontes = value;
    }

    /**
     * Obtém o valor da propriedade resultadosPrimarios.
     * 
     * @return
     *     possible object is
     *     {@link RetornoApoioQuantitativoDTO.ResultadosPrimarios }
     *     
     */
    public RetornoApoioQuantitativoDTO.ResultadosPrimarios getResultadosPrimarios() {
        return resultadosPrimarios;
    }

    /**
     * Define o valor da propriedade resultadosPrimarios.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoApoioQuantitativoDTO.ResultadosPrimarios }
     *     
     */
    public void setResultadosPrimarios(RetornoApoioQuantitativoDTO.ResultadosPrimarios value) {
        this.resultadosPrimarios = value;
    }

    /**
     * Obtém o valor da propriedade naturezas.
     * 
     * @return
     *     possible object is
     *     {@link RetornoApoioQuantitativoDTO.Naturezas }
     *     
     */
    public RetornoApoioQuantitativoDTO.Naturezas getNaturezas() {
        return naturezas;
    }

    /**
     * Define o valor da propriedade naturezas.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoApoioQuantitativoDTO.Naturezas }
     *     
     */
    public void setNaturezas(RetornoApoioQuantitativoDTO.Naturezas value) {
        this.naturezas = value;
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
     *         &lt;element name="fonte" type="{http://servicoweb.siop.sof.planejamento.gov.br/}fonteDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "fonte"
    })
    public static class Fontes {

        protected List<FonteDTO> fonte;

        /**
         * Gets the value of the fonte property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the fonte property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFonte().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FonteDTO }
         * 
         * 
         */
        public List<FonteDTO> getFonte() {
            if (fonte == null) {
                fonte = new ArrayList<FonteDTO>();
            }
            return this.fonte;
        }

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
     *         &lt;element name="idoc" type="{http://servicoweb.siop.sof.planejamento.gov.br/}idOcDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "idoc"
    })
    public static class Idocs {

        protected List<IdOcDTO> idoc;

        /**
         * Gets the value of the idoc property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the idoc property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIdoc().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link IdOcDTO }
         * 
         * 
         */
        public List<IdOcDTO> getIdoc() {
            if (idoc == null) {
                idoc = new ArrayList<IdOcDTO>();
            }
            return this.idoc;
        }

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
     *         &lt;element name="iduso" type="{http://servicoweb.siop.sof.planejamento.gov.br/}idUsoDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "iduso"
    })
    public static class Idusos {

        protected List<IdUsoDTO> iduso;

        /**
         * Gets the value of the iduso property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the iduso property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIduso().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link IdUsoDTO }
         * 
         * 
         */
        public List<IdUsoDTO> getIduso() {
            if (iduso == null) {
                iduso = new ArrayList<IdUsoDTO>();
            }
            return this.iduso;
        }

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
     *         &lt;element name="natureza" type="{http://servicoweb.siop.sof.planejamento.gov.br/}naturezaDespesaDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "natureza"
    })
    public static class Naturezas {

        protected List<NaturezaDespesaDTO> natureza;

        /**
         * Gets the value of the natureza property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the natureza property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNatureza().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NaturezaDespesaDTO }
         * 
         * 
         */
        public List<NaturezaDespesaDTO> getNatureza() {
            if (natureza == null) {
                natureza = new ArrayList<NaturezaDespesaDTO>();
            }
            return this.natureza;
        }

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
     *         &lt;element name="resultadoPrimario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}resultadoPrimarioDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "resultadoPrimario"
    })
    public static class ResultadosPrimarios {

        protected List<ResultadoPrimarioDTO> resultadoPrimario;

        /**
         * Gets the value of the resultadoPrimario property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the resultadoPrimario property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResultadoPrimario().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ResultadoPrimarioDTO }
         * 
         * 
         */
        public List<ResultadoPrimarioDTO> getResultadoPrimario() {
            if (resultadoPrimario == null) {
                resultadoPrimario = new ArrayList<ResultadoPrimarioDTO>();
            }
            return this.resultadoPrimario;
        }

    }

}
