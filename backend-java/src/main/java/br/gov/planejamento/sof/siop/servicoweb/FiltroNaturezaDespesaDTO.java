
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de filtroNaturezaDespesaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="filtroNaturezaDespesaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="codigoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoDetalhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="momentoId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filtroNaturezaDespesaDTO", propOrder = {
    "codigoAcao",
    "codigoDetalhamento",
    "codigoOrgao",
    "exercicio",
    "momentoId"
})
public class FiltroNaturezaDespesaDTO
    extends BaseDTO
{

    protected String codigoAcao;
    protected String codigoDetalhamento;
    protected String codigoOrgao;
    protected Integer exercicio;
    protected int momentoId;

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
     * Obtém o valor da propriedade codigoDetalhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDetalhamento() {
        return codigoDetalhamento;
    }

    /**
     * Define o valor da propriedade codigoDetalhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDetalhamento(String value) {
        this.codigoDetalhamento = value;
    }

    /**
     * Obtém o valor da propriedade codigoOrgao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrgao() {
        return codigoOrgao;
    }

    /**
     * Define o valor da propriedade codigoOrgao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrgao(String value) {
        this.codigoOrgao = value;
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
     * Obtém o valor da propriedade momentoId.
     * 
     */
    public int getMomentoId() {
        return momentoId;
    }

    /**
     * Define o valor da propriedade momentoId.
     * 
     */
    public void setMomentoId(int value) {
        this.momentoId = value;
    }

}
