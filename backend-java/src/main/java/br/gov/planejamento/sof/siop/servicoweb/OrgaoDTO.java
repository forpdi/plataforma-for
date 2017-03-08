
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de orgaoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="orgaoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrgaoPai" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descricaoAbreviada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="orgaoId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="orgaoSiorg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snAtivo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tipoOrgao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orgaoDTO", propOrder = {
    "codigoOrgao",
    "codigoOrgaoPai",
    "descricao",
    "descricaoAbreviada",
    "exercicio",
    "orgaoId",
    "orgaoSiorg",
    "snAtivo",
    "tipoOrgao"
})
public class OrgaoDTO {

    protected String codigoOrgao;
    protected String codigoOrgaoPai;
    protected String descricao;
    protected String descricaoAbreviada;
    protected Integer exercicio;
    protected Integer orgaoId;
    protected String orgaoSiorg;
    protected Boolean snAtivo;
    protected String tipoOrgao;

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
     * Obtém o valor da propriedade codigoOrgaoPai.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrgaoPai() {
        return codigoOrgaoPai;
    }

    /**
     * Define o valor da propriedade codigoOrgaoPai.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrgaoPai(String value) {
        this.codigoOrgaoPai = value;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

    /**
     * Obtém o valor da propriedade descricaoAbreviada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricaoAbreviada() {
        return descricaoAbreviada;
    }

    /**
     * Define o valor da propriedade descricaoAbreviada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricaoAbreviada(String value) {
        this.descricaoAbreviada = value;
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
     * Obtém o valor da propriedade orgaoId.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrgaoId() {
        return orgaoId;
    }

    /**
     * Define o valor da propriedade orgaoId.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrgaoId(Integer value) {
        this.orgaoId = value;
    }

    /**
     * Obtém o valor da propriedade orgaoSiorg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgaoSiorg() {
        return orgaoSiorg;
    }

    /**
     * Define o valor da propriedade orgaoSiorg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgaoSiorg(String value) {
        this.orgaoSiorg = value;
    }

    /**
     * Obtém o valor da propriedade snAtivo.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSnAtivo() {
        return snAtivo;
    }

    /**
     * Define o valor da propriedade snAtivo.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSnAtivo(Boolean value) {
        this.snAtivo = value;
    }

    /**
     * Obtém o valor da propriedade tipoOrgao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOrgao() {
        return tipoOrgao;
    }

    /**
     * Define o valor da propriedade tipoOrgao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOrgao(String value) {
        this.tipoOrgao = value;
    }

}
