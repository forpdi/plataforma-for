
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de inputExecucaoAcaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="inputExecucaoAcaoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoLocalizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inputExecucaoAcaoDTO", propOrder = {
    "codigoAcao",
    "localizador"
})
public class InputExecucaoAcaoDTO
    extends BaseDTO
{

    protected String codigoAcao;
    @XmlElement(nillable = true)
    protected List<InputExecucaoLocalizadorDTO> localizador;

    /**
     * Obtém o valor da propriedade codigoAcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAcao() {
        return codigoAcao;
    }

    /**
     * Define o valor da propriedade codigoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAcao(String value) {
        this.codigoAcao = value;
    }

    /**
     * Gets the value of the localizador property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localizador property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalizador().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputExecucaoLocalizadorDTO }
     * 
     * 
     */
    public List<InputExecucaoLocalizadorDTO> getLocalizador() {
        if (localizador == null) {
            localizador = new ArrayList<InputExecucaoLocalizadorDTO>();
        }
        return this.localizador;
    }

}
