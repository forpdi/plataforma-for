
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoAcaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoAcaoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localizador" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoLocalizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="snEmpreendimentoPPIPAC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoAcaoDTO", propOrder = {
    "codigoAcao",
    "localizador",
    "snEmpreendimentoPPIPAC"
})
public class ExecucaoAcaoDTO
    extends BaseDTO
{

    protected String codigoAcao;
    @XmlElement(nillable = true)
    protected List<ExecucaoLocalizadorDTO> localizador;
    protected Boolean snEmpreendimentoPPIPAC;

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
     * {@link ExecucaoLocalizadorDTO }
     * 
     * 
     */
    public List<ExecucaoLocalizadorDTO> getLocalizador() {
        if (localizador == null) {
            localizador = new ArrayList<ExecucaoLocalizadorDTO>();
        }
        return this.localizador;
    }

    /**
     * Obtém o valor da propriedade snEmpreendimentoPPIPAC.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnEmpreendimentoPPIPAC() {
        return snEmpreendimentoPPIPAC;
    }

    /**
     * Define o valor da propriedade snEmpreendimentoPPIPAC.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnEmpreendimentoPPIPAC(Boolean value) {
        this.snEmpreendimentoPPIPAC = value;
    }

}
