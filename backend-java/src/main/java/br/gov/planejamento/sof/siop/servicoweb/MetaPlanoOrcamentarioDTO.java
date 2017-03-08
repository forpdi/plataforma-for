
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de metaPlanoOrcamentarioDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="metaPlanoOrcamentarioDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="expansaoFisicaConcedida" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expansaoFisicaSolicitada" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="identificadorUnicoPlanoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="quantidadeFisico" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metaPlanoOrcamentarioDTO", propOrder = {
    "expansaoFisicaConcedida",
    "expansaoFisicaSolicitada",
    "identificadorUnicoPlanoOrcamentario",
    "quantidadeFisico"
})
public class MetaPlanoOrcamentarioDTO
    extends BaseDTO
{

    protected Long expansaoFisicaConcedida;
    protected Long expansaoFisicaSolicitada;
    protected Integer identificadorUnicoPlanoOrcamentario;
    protected Long quantidadeFisico;

    /**
     * Obtém o valor da propriedade expansaoFisicaConcedida.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpansaoFisicaConcedida() {
        return expansaoFisicaConcedida;
    }

    /**
     * Define o valor da propriedade expansaoFisicaConcedida.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpansaoFisicaConcedida(Long value) {
        this.expansaoFisicaConcedida = value;
    }

    /**
     * Obtém o valor da propriedade expansaoFisicaSolicitada.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getExpansaoFisicaSolicitada() {
        return expansaoFisicaSolicitada;
    }

    /**
     * Define o valor da propriedade expansaoFisicaSolicitada.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setExpansaoFisicaSolicitada(Long value) {
        this.expansaoFisicaSolicitada = value;
    }

    /**
     * Obtém o valor da propriedade identificadorUnicoPlanoOrcamentario.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdentificadorUnicoPlanoOrcamentario() {
        return identificadorUnicoPlanoOrcamentario;
    }

    /**
     * Define o valor da propriedade identificadorUnicoPlanoOrcamentario.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdentificadorUnicoPlanoOrcamentario(Integer value) {
        this.identificadorUnicoPlanoOrcamentario = value;
    }

    /**
     * Obtém o valor da propriedade quantidadeFisico.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getQuantidadeFisico() {
        return quantidadeFisico;
    }

    /**
     * Define o valor da propriedade quantidadeFisico.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setQuantidadeFisico(Long value) {
        this.quantidadeFisico = value;
    }

}
