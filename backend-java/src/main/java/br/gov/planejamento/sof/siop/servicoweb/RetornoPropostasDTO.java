
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoPropostasDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoPropostasDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="numeroRegistros" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="proposta" type="{http://servicoweb.siop.sof.planejamento.gov.br/}propostaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="valorTotal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoPropostasDTO", propOrder = {
    "numeroRegistros",
    "proposta",
    "valorTotal"
})
public class RetornoPropostasDTO
    extends RetornoDTO
{

    protected Integer numeroRegistros;
    @XmlElement(nillable = true)
    protected List<PropostaDTO> proposta;
    protected Long valorTotal;

    /**
     * Obtém o valor da propriedade numeroRegistros.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroRegistros() {
        return numeroRegistros;
    }

    /**
     * Define o valor da propriedade numeroRegistros.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroRegistros(Integer value) {
        this.numeroRegistros = value;
    }

    /**
     * Gets the value of the proposta property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the proposta property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProposta().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropostaDTO }
     * 
     * 
     */
    public List<PropostaDTO> getProposta() {
        if (proposta == null) {
            proposta = new ArrayList<PropostaDTO>();
        }
        return this.proposta;
    }

    /**
     * Obtém o valor da propriedade valorTotal.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getValorTotal() {
        return valorTotal;
    }

    /**
     * Define o valor da propriedade valorTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setValorTotal(Long value) {
        this.valorTotal = value;
    }

}
