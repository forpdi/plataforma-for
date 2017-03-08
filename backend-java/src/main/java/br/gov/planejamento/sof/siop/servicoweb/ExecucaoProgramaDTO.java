
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoProgramaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoProgramaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="acao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoAcaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codigoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoProgramaDTO", propOrder = {
    "acao",
    "codigoPrograma"
})
public class ExecucaoProgramaDTO
    extends BaseDTO
{

    @XmlElement(nillable = true)
    protected List<ExecucaoAcaoDTO> acao;
    protected String codigoPrograma;

    /**
     * Gets the value of the acao property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acao property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcao().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecucaoAcaoDTO }
     * 
     * 
     */
    public List<ExecucaoAcaoDTO> getAcao() {
        if (acao == null) {
            acao = new ArrayList<ExecucaoAcaoDTO>();
        }
        return this.acao;
    }

    /**
     * Obtém o valor da propriedade codigoPrograma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    /**
     * Define o valor da propriedade codigoPrograma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPrograma(String value) {
        this.codigoPrograma = value;
    }

}
