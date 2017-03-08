
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de execucaoReceitaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="execucaoReceitaDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoNaturezaReceita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dotacaoAtual" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="dotacaoInicial" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="execucaoMensal" type="{http://servicoweb.siop.sof.planejamento.gov.br/}execucaoMensalDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "execucaoReceitaDTO", propOrder = {
    "codigoNaturezaReceita",
    "dotacaoAtual",
    "dotacaoInicial",
    "execucaoMensal"
})
public class ExecucaoReceitaDTO {

    protected String codigoNaturezaReceita;
    protected Long dotacaoAtual;
    protected Long dotacaoInicial;
    @XmlElement(nillable = true)
    protected List<ExecucaoMensalDTO> execucaoMensal;

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
     * Obtém o valor da propriedade dotacaoAtual.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDotacaoAtual() {
        return dotacaoAtual;
    }

    /**
     * Define o valor da propriedade dotacaoAtual.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDotacaoAtual(Long value) {
        this.dotacaoAtual = value;
    }

    /**
     * Obtém o valor da propriedade dotacaoInicial.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDotacaoInicial() {
        return dotacaoInicial;
    }

    /**
     * Define o valor da propriedade dotacaoInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDotacaoInicial(Long value) {
        this.dotacaoInicial = value;
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

}
