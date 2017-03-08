
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoAcompanhamentoOrcamentarioDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoAcompanhamentoOrcamentarioDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="acompanhamentosAcoes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="acompanhamentoAcao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoOrcamentarioAcaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="alertas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="alerta" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="pendencias" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="pendencia" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "retornoAcompanhamentoOrcamentarioDTO", propOrder = {
    "acompanhamentosAcoes",
    "alertas",
    "pendencias"
})
public class RetornoAcompanhamentoOrcamentarioDTO
    extends RetornoDTO
{

    @XmlElement(required = true)
    protected RetornoAcompanhamentoOrcamentarioDTO.AcompanhamentosAcoes acompanhamentosAcoes;
    protected RetornoAcompanhamentoOrcamentarioDTO.Alertas alertas;
    protected RetornoAcompanhamentoOrcamentarioDTO.Pendencias pendencias;

    /**
     * Obtém o valor da propriedade acompanhamentosAcoes.
     * 
     * @return
     *     possible object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO.AcompanhamentosAcoes }
     *     
     */
    public RetornoAcompanhamentoOrcamentarioDTO.AcompanhamentosAcoes getAcompanhamentosAcoes() {
        return acompanhamentosAcoes;
    }

    /**
     * Define o valor da propriedade acompanhamentosAcoes.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO.AcompanhamentosAcoes }
     *     
     */
    public void setAcompanhamentosAcoes(RetornoAcompanhamentoOrcamentarioDTO.AcompanhamentosAcoes value) {
        this.acompanhamentosAcoes = value;
    }

    /**
     * Obtém o valor da propriedade alertas.
     * 
     * @return
     *     possible object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO.Alertas }
     *     
     */
    public RetornoAcompanhamentoOrcamentarioDTO.Alertas getAlertas() {
        return alertas;
    }

    /**
     * Define o valor da propriedade alertas.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO.Alertas }
     *     
     */
    public void setAlertas(RetornoAcompanhamentoOrcamentarioDTO.Alertas value) {
        this.alertas = value;
    }

    /**
     * Obtém o valor da propriedade pendencias.
     * 
     * @return
     *     possible object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO.Pendencias }
     *     
     */
    public RetornoAcompanhamentoOrcamentarioDTO.Pendencias getPendencias() {
        return pendencias;
    }

    /**
     * Define o valor da propriedade pendencias.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoAcompanhamentoOrcamentarioDTO.Pendencias }
     *     
     */
    public void setPendencias(RetornoAcompanhamentoOrcamentarioDTO.Pendencias value) {
        this.pendencias = value;
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
     *         &lt;element name="acompanhamentoAcao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acompanhamentoOrcamentarioAcaoDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "acompanhamentoAcao"
    })
    public static class AcompanhamentosAcoes {

        protected List<AcompanhamentoOrcamentarioAcaoDTO> acompanhamentoAcao;

        /**
         * Gets the value of the acompanhamentoAcao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acompanhamentoAcao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcompanhamentoAcao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AcompanhamentoOrcamentarioAcaoDTO }
         * 
         * 
         */
        public List<AcompanhamentoOrcamentarioAcaoDTO> getAcompanhamentoAcao() {
            if (acompanhamentoAcao == null) {
                acompanhamentoAcao = new ArrayList<AcompanhamentoOrcamentarioAcaoDTO>();
            }
            return this.acompanhamentoAcao;
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
     *         &lt;element name="alerta" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
        "alerta"
    })
    public static class Alertas {

        protected List<String> alerta;

        /**
         * Gets the value of the alerta property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the alerta property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAlerta().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getAlerta() {
            if (alerta == null) {
                alerta = new ArrayList<String>();
            }
            return this.alerta;
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
     *         &lt;element name="pendencia" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
        "pendencia"
    })
    public static class Pendencias {

        protected List<String> pendencia;

        /**
         * Gets the value of the pendencia property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the pendencia property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPendencia().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPendencia() {
            if (pendencia == null) {
                pendencia = new ArrayList<String>();
            }
            return this.pendencia;
        }

    }

}
