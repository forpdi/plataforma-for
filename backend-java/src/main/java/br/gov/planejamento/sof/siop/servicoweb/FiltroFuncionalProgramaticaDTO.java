
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de filtroFuncionalProgramaticaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="filtroFuncionalProgramaticaDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoAcao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoEsfera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoLocalizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPrograma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoSubFuncao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoTipoInclusaoAcao" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoTipoInclusaoLocalizador" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codigoUO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tipoInclusaoIds" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filtroFuncionalProgramaticaDTO", propOrder = {
    "codigoAcao",
    "codigoEsfera",
    "codigoFuncao",
    "codigoLocalizador",
    "codigoPrograma",
    "codigoSubFuncao",
    "codigoTipoInclusaoAcao",
    "codigoTipoInclusaoLocalizador",
    "codigoUO",
    "exercicio",
    "tipoInclusaoIds"
})
public class FiltroFuncionalProgramaticaDTO {

    protected String codigoAcao;
    protected String codigoEsfera;
    protected String codigoFuncao;
    protected String codigoLocalizador;
    protected String codigoPrograma;
    protected String codigoSubFuncao;
    protected Integer codigoTipoInclusaoAcao;
    protected Integer codigoTipoInclusaoLocalizador;
    protected String codigoUO;
    protected Integer exercicio;
    @XmlElement(nillable = true)
    protected List<Integer> tipoInclusaoIds;

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
     * Obtém o valor da propriedade codigoEsfera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoEsfera() {
        return codigoEsfera;
    }

    /**
     * Define o valor da propriedade codigoEsfera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoEsfera(String value) {
        this.codigoEsfera = value;
    }

    /**
     * Obtém o valor da propriedade codigoFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoFuncao() {
        return codigoFuncao;
    }

    /**
     * Define o valor da propriedade codigoFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoFuncao(String value) {
        this.codigoFuncao = value;
    }

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
     * Obtém o valor da propriedade codigoPrograma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    /**
     * Define o valor da propriedade codigoPrograma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPrograma(String value) {
        this.codigoPrograma = value;
    }

    /**
     * Obtém o valor da propriedade codigoSubFuncao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSubFuncao() {
        return codigoSubFuncao;
    }

    /**
     * Define o valor da propriedade codigoSubFuncao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSubFuncao(String value) {
        this.codigoSubFuncao = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoInclusaoAcao.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoTipoInclusaoAcao() {
        return codigoTipoInclusaoAcao;
    }

    /**
     * Define o valor da propriedade codigoTipoInclusaoAcao.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoTipoInclusaoAcao(Integer value) {
        this.codigoTipoInclusaoAcao = value;
    }

    /**
     * Obtém o valor da propriedade codigoTipoInclusaoLocalizador.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodigoTipoInclusaoLocalizador() {
        return codigoTipoInclusaoLocalizador;
    }

    /**
     * Define o valor da propriedade codigoTipoInclusaoLocalizador.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodigoTipoInclusaoLocalizador(Integer value) {
        this.codigoTipoInclusaoLocalizador = value;
    }

    /**
     * Obtém o valor da propriedade codigoUO.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUO() {
        return codigoUO;
    }

    /**
     * Define o valor da propriedade codigoUO.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUO(String value) {
        this.codigoUO = value;
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
     * Gets the value of the tipoInclusaoIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tipoInclusaoIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTipoInclusaoIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getTipoInclusaoIds() {
        if (tipoInclusaoIds == null) {
            tipoInclusaoIds = new ArrayList<Integer>();
        }
        return this.tipoInclusaoIds;
    }

}
