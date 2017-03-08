
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoInformacaoCaptacaoPLOADTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoInformacaoCaptacaoPLOADTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="registros">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="registro" type="{http://servicoweb.siop.sof.planejamento.gov.br/}informacaoCaptacaoPLOADTO" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "retornoInformacaoCaptacaoPLOADTO", propOrder = {
    "registros"
})
public class RetornoInformacaoCaptacaoPLOADTO
    extends RetornoDTO
{

    @XmlElement(required = true)
    protected RetornoInformacaoCaptacaoPLOADTO.Registros registros;

    /**
     * Obtém o valor da propriedade registros.
     * 
     * @return
     *     possible object is
     *     {@link RetornoInformacaoCaptacaoPLOADTO.Registros }
     *     
     */
    public RetornoInformacaoCaptacaoPLOADTO.Registros getRegistros() {
        return registros;
    }

    /**
     * Define o valor da propriedade registros.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoInformacaoCaptacaoPLOADTO.Registros }
     *     
     */
    public void setRegistros(RetornoInformacaoCaptacaoPLOADTO.Registros value) {
        this.registros = value;
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
     *         &lt;element name="registro" type="{http://servicoweb.siop.sof.planejamento.gov.br/}informacaoCaptacaoPLOADTO" maxOccurs="unbounded" minOccurs="0"/>
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
        "registro"
    })
    public static class Registros {

        protected List<InformacaoCaptacaoPLOADTO> registro;

        /**
         * Gets the value of the registro property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the registro property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRegistro().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InformacaoCaptacaoPLOADTO }
         * 
         * 
         */
        public List<InformacaoCaptacaoPLOADTO> getRegistro() {
            if (registro == null) {
                registro = new ArrayList<InformacaoCaptacaoPLOADTO>();
            }
            return this.registro;
        }

    }

}
