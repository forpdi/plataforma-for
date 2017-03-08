
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de receitaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="receitaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="naturezaReceita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receitaDTO", propOrder = {
    "naturezaReceita",
    "valor"
})
public class ReceitaDTO
    extends BaseDTO
{

    protected String naturezaReceita;
    protected Long valor;

    /**
     * Obtém o valor da propriedade naturezaReceita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaturezaReceita() {
        return naturezaReceita;
    }

    /**
     * Define o valor da propriedade naturezaReceita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaturezaReceita(String value) {
        this.naturezaReceita = value;
    }

    /**
     * Obtém o valor da propriedade valor.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getValor() {
        return valor;
    }

    /**
     * Define o valor da propriedade valor.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setValor(Long value) {
        this.valor = value;
    }

}
