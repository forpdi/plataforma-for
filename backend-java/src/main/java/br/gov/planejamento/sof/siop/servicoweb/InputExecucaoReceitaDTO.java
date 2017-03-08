
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de inputExecucaoReceitaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="inputExecucaoReceitaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoNaturezaReceita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="execucaoMensal" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoReceitaMensalDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inputExecucaoReceitaDTO", propOrder = {
    "codigoNaturezaReceita",
    "execucaoMensal"
})
public class InputExecucaoReceitaDTO
    extends BaseDTO
{

    protected String codigoNaturezaReceita;
    @XmlElement(nillable = true)
    protected List<ExecucaoReceitaMensalDTO> execucaoMensal;

    /**
     * Obtém o valor da propriedade codigoNaturezaReceita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoNaturezaReceita() {
        return codigoNaturezaReceita;
    }

    /**
     * Define o valor da propriedade codigoNaturezaReceita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoNaturezaReceita(String value) {
        this.codigoNaturezaReceita = value;
    }

    /**
     * Gets the value of the execucaoMensal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the execucaoMensal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExecucaoMensal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecucaoReceitaMensalDTO }
     * 
     * 
     */
    public List<ExecucaoReceitaMensalDTO> getExecucaoMensal() {
        if (execucaoMensal == null) {
            execucaoMensal = new ArrayList<ExecucaoReceitaMensalDTO>();
        }
        return this.execucaoMensal;
    }

}
