
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoLocalizadorDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoLocalizadorDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoLocalizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoRegiao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="despesa" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoDespesaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoLocalizadorDTO", propOrder = {
    "codigoLocalizador",
    "codigoRegiao",
    "despesa"
})
public class ExecucaoLocalizadorDTO
    extends BaseDTO
{

    protected String codigoLocalizador;
    protected String codigoRegiao;
    @XmlElement(nillable = true)
    protected List<ExecucaoDespesaDTO> despesa;

    /**
     * Obtém o valor da propriedade codigoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoLocalizador() {
        return codigoLocalizador;
    }

    /**
     * Define o valor da propriedade codigoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoLocalizador(String value) {
        this.codigoLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade codigoRegiao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoRegiao() {
        return codigoRegiao;
    }

    /**
     * Define o valor da propriedade codigoRegiao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoRegiao(String value) {
        this.codigoRegiao = value;
    }

    /**
     * Gets the value of the despesa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the despesa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDespesa().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecucaoDespesaDTO }
     * 
     * 
     */
    public List<ExecucaoDespesaDTO> getDespesa() {
        if (despesa == null) {
            despesa = new ArrayList<ExecucaoDespesaDTO>();
        }
        return this.despesa;
    }

}
