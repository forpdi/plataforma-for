
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoEstataisDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoEstataisDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigosUnidadesOrcamentarias">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="codigoUnidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mes" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoEstataisDTO", propOrder = {
    "codigoOrgao",
    "codigosUnidadesOrcamentarias",
    "exercicio",
    "mes"
})
public class ExecucaoEstataisDTO
    extends BaseDTO
{

    protected String codigoOrgao;
    @XmlElement(required = true)
    protected ExecucaoEstataisDTO.CodigosUnidadesOrcamentarias codigosUnidadesOrcamentarias;
    protected Integer exercicio;
    protected Integer mes;

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
     * Obtém o valor da propriedade codigosUnidadesOrcamentarias.
     * 
     * @return
     *     possible object is
     *     {@link ExecucaoEstataisDTO.CodigosUnidadesOrcamentarias }
     *     
     */
    public ExecucaoEstataisDTO.CodigosUnidadesOrcamentarias getCodigosUnidadesOrcamentarias() {
        return codigosUnidadesOrcamentarias;
    }

    /**
     * Define o valor da propriedade codigosUnidadesOrcamentarias.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecucaoEstataisDTO.CodigosUnidadesOrcamentarias }
     *     
     */
    public void setCodigosUnidadesOrcamentarias(ExecucaoEstataisDTO.CodigosUnidadesOrcamentarias value) {
        this.codigosUnidadesOrcamentarias = value;
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
     * Obtém o valor da propriedade mes.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMes() {
        return mes;
    }

    /**
     * Define o valor da propriedade mes.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMes(Integer value) {
        this.mes = value;
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
     *         &lt;element name="codigoUnidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
        "codigoUnidadeOrcamentaria"
    })
    public static class CodigosUnidadesOrcamentarias {

        protected List<String> codigoUnidadeOrcamentaria;

        /**
         * Gets the value of the codigoUnidadeOrcamentaria property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the codigoUnidadeOrcamentaria property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCodigoUnidadeOrcamentaria().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getCodigoUnidadeOrcamentaria() {
            if (codigoUnidadeOrcamentaria == null) {
                codigoUnidadeOrcamentaria = new ArrayList<String>();
            }
            return this.codigoUnidadeOrcamentaria;
        }

    }

}
