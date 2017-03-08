
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de inputExecucaoUnidadeOrcamentariaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="inputExecucaoUnidadeOrcamentariaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoUnidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="funcao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoFuncaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="receita" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoReceitaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inputExecucaoUnidadeOrcamentariaDTO", propOrder = {
    "codigoUnidadeOrcamentaria",
    "funcao",
    "receita"
})
public class InputExecucaoUnidadeOrcamentariaDTO
    extends BaseDTO
{

    protected String codigoUnidadeOrcamentaria;
    @XmlElement(nillable = true)
    protected List<InputExecucaoFuncaoDTO> funcao;
    @XmlElement(nillable = true)
    protected List<InputExecucaoReceitaDTO> receita;

    /**
     * Obtém o valor da propriedade codigoUnidadeOrcamentaria.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUnidadeOrcamentaria() {
        return codigoUnidadeOrcamentaria;
    }

    /**
     * Define o valor da propriedade codigoUnidadeOrcamentaria.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUnidadeOrcamentaria(String value) {
        this.codigoUnidadeOrcamentaria = value;
    }

    /**
     * Gets the value of the funcao property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the funcao property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFuncao().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputExecucaoFuncaoDTO }
     * 
     * 
     */
    public List<InputExecucaoFuncaoDTO> getFuncao() {
        if (funcao == null) {
            funcao = new ArrayList<InputExecucaoFuncaoDTO>();
        }
        return this.funcao;
    }

    /**
     * Gets the value of the receita property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receita property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceita().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputExecucaoReceitaDTO }
     * 
     * 
     */
    public List<InputExecucaoReceitaDTO> getReceita() {
        if (receita == null) {
            receita = new ArrayList<InputExecucaoReceitaDTO>();
        }
        return this.receita;
    }

}
