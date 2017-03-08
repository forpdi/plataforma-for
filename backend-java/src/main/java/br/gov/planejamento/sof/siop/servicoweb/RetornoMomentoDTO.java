
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoMomentoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoMomentoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="momento" type="{http://servicoweb.siop.sof.planejamento.gov.br/}momentoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoMomentoDTO", propOrder = {
    "momento"
})
public class RetornoMomentoDTO
    extends RetornoDTO
{

    protected MomentoDTO momento;

    /**
     * Obtém o valor da propriedade momento.
     * 
     * @return
     *     possible object is
     *     {@link MomentoDTO }
     *     
     */
    public MomentoDTO getMomento() {
        return momento;
    }

    /**
     * Define o valor da propriedade momento.
     * 
     * @param value
     *     allowed object is
     *     {@link MomentoDTO }
     *     
     */
    public void setMomento(MomentoDTO value) {
        this.momento = value;
    }

}
