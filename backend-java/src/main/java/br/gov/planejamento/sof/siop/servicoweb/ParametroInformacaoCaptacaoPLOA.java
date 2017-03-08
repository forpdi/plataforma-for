
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de parametroInformacaoCaptacaoPLOA complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="parametroInformacaoCaptacaoPLOA">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codigoMomento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codigoTipoDetalhamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigoUnidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="captados" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="captaveis" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parametroInformacaoCaptacaoPLOA", propOrder = {
    "exercicio",
    "codigoMomento",
    "codigoTipoDetalhamento",
    "codigoOrgao",
    "codigoUnidadeOrcamentaria",
    "captados",
    "captaveis"
})
public class ParametroInformacaoCaptacaoPLOA
    extends BaseDTO
{

    protected int exercicio;
    protected int codigoMomento;
    protected String codigoTipoDetalhamento;
    @XmlElement(required = true)
    protected String codigoOrgao;
    @XmlElement(required = true)
    protected String codigoUnidadeOrcamentaria;
    protected Boolean captados;
    protected Boolean captaveis;

    /**
     * Obtém o valor da propriedade exercicio.
     * 
     */
    public int getExercicio() {
        return exercicio;
    }

    /**
     * Define o valor da propriedade exercicio.
     * 
     */
    public void setExercicio(int value) {
        this.exercicio = value;
    }

    /**
     * Obtém o valor da propriedade codigoMomento.
     * 
     */
    public int getCodigoMomento() {
        return codigoMomento;
    }

    /**
     * Define o valor da propriedade codigoMomento.
     * 
     */
    public void setCodigoMomento(int value) {
        this.codigoMomento = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoDetalhamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTipoDetalhamento() {
        return codigoTipoDetalhamento;
    }

    /**
     * Define o valor da propriedade codigoTipoDetalhamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTipoDetalhamento(String value) {
        this.codigoTipoDetalhamento = value;
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
     * Obtém o valor da propriedade codigoUnidadeOrcamentaria.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUnidadeOrcamentaria() {
        return codigoUnidadeOrcamentaria;
    }

    /**
     * Define o valor da propriedade codigoUnidadeOrcamentaria.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUnidadeOrcamentaria(String value) {
        this.codigoUnidadeOrcamentaria = value;
    }

    /**
     * Obtém o valor da propriedade captados.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCaptados() {
        return captados;
    }

    /**
     * Define o valor da propriedade captados.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCaptados(Boolean value) {
        this.captados = value;
    }

    /**
     * Obtém o valor da propriedade captaveis.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCaptaveis() {
        return captaveis;
    }

    /**
     * Define o valor da propriedade captaveis.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCaptaveis(Boolean value) {
        this.captaveis = value;
    }

}
