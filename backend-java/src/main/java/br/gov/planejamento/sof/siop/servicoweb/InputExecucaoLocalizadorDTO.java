
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de inputExecucaoLocalizadorDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="inputExecucaoLocalizadorDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoLocalizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="execucaoMensal" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoMensalDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="mensagem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inputExecucaoLocalizadorDTO", propOrder = {
    "codigoLocalizador",
    "execucaoMensal",
    "mensagem"
})
public class InputExecucaoLocalizadorDTO
    extends BaseDTO
{

    protected String codigoLocalizador;
    @XmlElement(nillable = true)
    protected List<ExecucaoMensalDTO> execucaoMensal;
    protected String mensagem;

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
     * {@link ExecucaoMensalDTO }
     * 
     * 
     */
    public List<ExecucaoMensalDTO> getExecucaoMensal() {
        if (execucaoMensal == null) {
            execucaoMensal = new ArrayList<ExecucaoMensalDTO>();
        }
        return this.execucaoMensal;
    }

    /**
     * Obtém o valor da propriedade mensagem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Define o valor da propriedade mensagem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagem(String value) {
        this.mensagem = value;
    }

}
