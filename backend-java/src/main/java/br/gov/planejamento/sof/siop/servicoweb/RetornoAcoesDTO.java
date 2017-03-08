
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoAcoesDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoAcoesDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="registros" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="acoes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="acao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acaoDTO" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "retornoAcoesDTO", propOrder = {
    "registros",
    "acoes"
})
public class RetornoAcoesDTO
    extends RetornoDTO
{

    @XmlElement(nillable = true)
    protected List<AcaoDTO> registros;
    @XmlElement(required = true)
    protected RetornoAcoesDTO.Acoes acoes;

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
     * {@link AcaoDTO }
     * 
     * 
     */
    public List<AcaoDTO> getRegistros() {
        if (registros == null) {
            registros = new ArrayList<AcaoDTO>();
        }
        return this.registros;
    }

    /**
     * Obtém o valor da propriedade acoes.
     * 
     * @return
     *     possible object is
     *     {@link RetornoAcoesDTO.Acoes }
     *     
     */
    public RetornoAcoesDTO.Acoes getAcoes() {
        return acoes;
    }

    /**
     * Define o valor da propriedade acoes.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoAcoesDTO.Acoes }
     *     
     */
    public void setAcoes(RetornoAcoesDTO.Acoes value) {
        this.acoes = value;
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
     *         &lt;element name="acao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acaoDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "acao"
    })
    public static class Acoes {

        protected List<AcaoDTO> acao;

        /**
         * Gets the value of the acao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AcaoDTO }
         * 
         * 
         */
        public List<AcaoDTO> getAcao() {
            if (acao == null) {
                acao = new ArrayList<AcaoDTO>();
            }
            return this.acao;
        }

    }

}
