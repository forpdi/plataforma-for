
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de naturezaDespesaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="naturezaDespesaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoNatureza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="elementoDescricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="elementoDescricaoAbreviada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="snAtiva" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snValorizavel" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="subElementoDescricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subElementoDescricaoAbreviada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "naturezaDespesaDTO", propOrder = {
    "codigoNatureza",
    "elementoDescricao",
    "elementoDescricaoAbreviada",
    "exercicio",
    "snAtiva",
    "snValorizavel",
    "subElementoDescricao",
    "subElementoDescricaoAbreviada"
})
public class NaturezaDespesaDTO
    extends BaseDTO
{

    protected String codigoNatureza;
    protected String elementoDescricao;
    protected String elementoDescricaoAbreviada;
    protected Integer exercicio;
    protected Boolean snAtiva;
    protected Boolean snValorizavel;
    protected String subElementoDescricao;
    protected String subElementoDescricaoAbreviada;

    /**
     * Obtém o valor da propriedade codigoNatureza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoNatureza() {
        return codigoNatureza;
    }

    /**
     * Define o valor da propriedade codigoNatureza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoNatureza(String value) {
        this.codigoNatureza = value;
    }

    /**
     * Obtém o valor da propriedade elementoDescricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElementoDescricao() {
        return elementoDescricao;
    }

    /**
     * Define o valor da propriedade elementoDescricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElementoDescricao(String value) {
        this.elementoDescricao = value;
    }

    /**
     * Obtém o valor da propriedade elementoDescricaoAbreviada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElementoDescricaoAbreviada() {
        return elementoDescricaoAbreviada;
    }

    /**
     * Define o valor da propriedade elementoDescricaoAbreviada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElementoDescricaoAbreviada(String value) {
        this.elementoDescricaoAbreviada = value;
    }

    /**
     * Obtém o valor da propriedade exercicio.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExercicio() {
        return exercicio;
    }

    /**
     * Define o valor da propriedade exercicio.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExercicio(Integer value) {
        this.exercicio = value;
    }

    /**
     * Obtém o valor da propriedade snAtiva.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAtiva() {
        return snAtiva;
    }

    /**
     * Define o valor da propriedade snAtiva.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAtiva(Boolean value) {
        this.snAtiva = value;
    }

    /**
     * Obtém o valor da propriedade snValorizavel.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnValorizavel() {
        return snValorizavel;
    }

    /**
     * Define o valor da propriedade snValorizavel.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnValorizavel(Boolean value) {
        this.snValorizavel = value;
    }

    /**
     * Obtém o valor da propriedade subElementoDescricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubElementoDescricao() {
        return subElementoDescricao;
    }

    /**
     * Define o valor da propriedade subElementoDescricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubElementoDescricao(String value) {
        this.subElementoDescricao = value;
    }

    /**
     * Obtém o valor da propriedade subElementoDescricaoAbreviada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubElementoDescricaoAbreviada() {
        return subElementoDescricaoAbreviada;
    }

    /**
     * Define o valor da propriedade subElementoDescricaoAbreviada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubElementoDescricaoAbreviada(String value) {
        this.subElementoDescricaoAbreviada = value;
    }

}
