
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
 * <p>Classe Java de acompanhamentoPlanoOrcamentarioDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="acompanhamentoPlanoOrcamentarioDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="planoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="realizadoLOA" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="dataApuracaoLOA" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="analisesPlanoOrcamentario">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="analisePlanoOrcamentario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}analiseAcompanhamentoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "acompanhamentoPlanoOrcamentarioDTO", propOrder = {
    "planoOrcamentario",
    "realizadoLOA",
    "dataApuracaoLOA",
    "analisesPlanoOrcamentario"
})
public class AcompanhamentoPlanoOrcamentarioDTO
    extends BaseDTO
{

    @XmlElement(required = true)
    protected String planoOrcamentario;
    protected long realizadoLOA;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataApuracaoLOA;
    @XmlElement(required = true)
    protected AcompanhamentoPlanoOrcamentarioDTO.AnalisesPlanoOrcamentario analisesPlanoOrcamentario;

    /**
     * Obtém o valor da propriedade planoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanoOrcamentario() {
        return planoOrcamentario;
    }

    /**
     * Define o valor da propriedade planoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanoOrcamentario(String value) {
        this.planoOrcamentario = value;
    }

    /**
     * Obtém o valor da propriedade realizadoLOA.
     * 
     */
    public long getRealizadoLOA() {
        return realizadoLOA;
    }

    /**
     * Define o valor da propriedade realizadoLOA.
     * 
     */
    public void setRealizadoLOA(long value) {
        this.realizadoLOA = value;
    }

    /**
     * Obtém o valor da propriedade dataApuracaoLOA.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataApuracaoLOA() {
        return dataApuracaoLOA;
    }

    /**
     * Define o valor da propriedade dataApuracaoLOA.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataApuracaoLOA(XMLGregorianCalendar value) {
        this.dataApuracaoLOA = value;
    }

    /**
     * Obtém o valor da propriedade analisesPlanoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link AcompanhamentoPlanoOrcamentarioDTO.AnalisesPlanoOrcamentario }
     *     
     */
    public AcompanhamentoPlanoOrcamentarioDTO.AnalisesPlanoOrcamentario getAnalisesPlanoOrcamentario() {
        return analisesPlanoOrcamentario;
    }

    /**
     * Define o valor da propriedade analisesPlanoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link AcompanhamentoPlanoOrcamentarioDTO.AnalisesPlanoOrcamentario }
     *     
     */
    public void setAnalisesPlanoOrcamentario(AcompanhamentoPlanoOrcamentarioDTO.AnalisesPlanoOrcamentario value) {
        this.analisesPlanoOrcamentario = value;
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
     *         &lt;element name="analisePlanoOrcamentario" type="{http://servicoweb.siop.sof.planejamento.gov.br/}analiseAcompanhamentoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "analisePlanoOrcamentario"
    })
    public static class AnalisesPlanoOrcamentario {

        protected List<AnaliseAcompanhamentoOrcamentarioDTO> analisePlanoOrcamentario;

        /**
         * Gets the value of the analisePlanoOrcamentario property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the analisePlanoOrcamentario property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnalisePlanoOrcamentario().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AnaliseAcompanhamentoOrcamentarioDTO }
         * 
         * 
         */
        public List<AnaliseAcompanhamentoOrcamentarioDTO> getAnalisePlanoOrcamentario() {
            if (analisePlanoOrcamentario == null) {
                analisePlanoOrcamentario = new ArrayList<AnaliseAcompanhamentoOrcamentarioDTO>();
            }
            return this.analisePlanoOrcamentario;
        }

    }

}
