
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de inputExecucaoFuncaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="inputExecucaoFuncaoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subFuncao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoSubFuncaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inputExecucaoFuncaoDTO", propOrder = {
    "codigoFuncao",
    "subFuncao"
})
public class InputExecucaoFuncaoDTO
    extends BaseDTO
{

    protected String codigoFuncao;
    @XmlElement(nillable = true)
    protected List<InputExecucaoSubFuncaoDTO> subFuncao;

    /**
     * Obtém o valor da propriedade codigoFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoFuncao() {
        return codigoFuncao;
    }

    /**
     * Define o valor da propriedade codigoFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoFuncao(String value) {
        this.codigoFuncao = value;
    }

    /**
     * Gets the value of the subFuncao property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subFuncao property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubFuncao().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputExecucaoSubFuncaoDTO }
     * 
     * 
     */
    public List<InputExecucaoSubFuncaoDTO> getSubFuncao() {
        if (subFuncao == null) {
            subFuncao = new ArrayList<InputExecucaoSubFuncaoDTO>();
        }
        return this.subFuncao;
    }

}
