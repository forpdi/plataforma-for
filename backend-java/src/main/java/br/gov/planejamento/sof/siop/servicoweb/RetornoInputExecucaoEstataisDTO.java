
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoInputExecucaoEstataisDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoInputExecucaoEstataisDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="registros" type="{http://servicoweb.siop.sof.planejamento.gov.br/}inputExecucaoOrgaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoInputExecucaoEstataisDTO", propOrder = {
    "exercicio",
    "registros"
})
public class RetornoInputExecucaoEstataisDTO
    extends RetornoDTO
{

    protected int exercicio;
    @XmlElement(nillable = true)
    protected List<InputExecucaoOrgaoDTO> registros;

    /**
     * Obtém o valor da propriedade exercicio.
     * 
     */
    public int getExercicio() {
        return exercicio;
    }

    /**
     * Define o valor da propriedade exercicio.
     * 
     */
    public void setExercicio(int value) {
        this.exercicio = value;
    }

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
     * {@link InputExecucaoOrgaoDTO }
     * 
     * 
     */
    public List<InputExecucaoOrgaoDTO> getRegistros() {
        if (registros == null) {
            registros = new ArrayList<InputExecucaoOrgaoDTO>();
        }
        return this.registros;
    }

}
