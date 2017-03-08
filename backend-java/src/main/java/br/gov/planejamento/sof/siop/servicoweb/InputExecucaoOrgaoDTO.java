
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de inputExecucaoOrgaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="inputExecucaoOrgaoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadeOrcamentaria" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoUnidadeOrcamentariaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inputExecucaoOrgaoDTO", propOrder = {
    "codigoOrgao",
    "unidadeOrcamentaria"
})
public class InputExecucaoOrgaoDTO
    extends BaseDTO
{

    protected String codigoOrgao;
    @XmlElement(nillable = true)
    protected List<InputExecucaoUnidadeOrcamentariaDTO> unidadeOrcamentaria;

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
     * Gets the value of the unidadeOrcamentaria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unidadeOrcamentaria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnidadeOrcamentaria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputExecucaoUnidadeOrcamentariaDTO }
     * 
     * 
     */
    public List<InputExecucaoUnidadeOrcamentariaDTO> getUnidadeOrcamentaria() {
        if (unidadeOrcamentaria == null) {
            unidadeOrcamentaria = new ArrayList<InputExecucaoUnidadeOrcamentariaDTO>();
        }
        return this.unidadeOrcamentaria;
    }

}
