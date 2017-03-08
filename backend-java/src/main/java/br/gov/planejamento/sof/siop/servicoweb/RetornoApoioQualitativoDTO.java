
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoApoioQualitativoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoApoioQualitativoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="basesGeograficasDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}baseGeograficaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="esferasDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}esferaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="funcoesDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}funcaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="macroDesafiosDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}macroDesafioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="momentosDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}momentoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="perfisDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}perfilDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="periodicidadesDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}periodicidadeDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="produtosDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}produtoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="regioesDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}regiaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subFuncoesDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}subFuncaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tiposAcaoDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}tipoAcaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tiposInclusaoDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}tipoInclusaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tiposProgramaDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}tipoProgramaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="unidadesMedidaDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}unidadeMedidaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="unidadesMedidaIndicadorDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}unidadeMedidaIndicadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoApoioQualitativoDTO", propOrder = {
    "basesGeograficasDTO",
    "esferasDTO",
    "funcoesDTO",
    "macroDesafiosDTO",
    "momentosDTO",
    "perfisDTO",
    "periodicidadesDTO",
    "produtosDTO",
    "regioesDTO",
    "subFuncoesDTO",
    "tiposAcaoDTO",
    "tiposInclusaoDTO",
    "tiposProgramaDTO",
    "unidadesMedidaDTO",
    "unidadesMedidaIndicadorDTO"
})
public class RetornoApoioQualitativoDTO
    extends RetornoDTO
{

    @XmlElement(nillable = true)
    protected List<BaseGeograficaDTO> basesGeograficasDTO;
    @XmlElement(nillable = true)
    protected List<EsferaDTO> esferasDTO;
    @XmlElement(nillable = true)
    protected List<FuncaoDTO> funcoesDTO;
    @XmlElement(nillable = true)
    protected List<MacroDesafioDTO> macroDesafiosDTO;
    @XmlElement(nillable = true)
    protected List<MomentoDTO> momentosDTO;
    @XmlElement(nillable = true)
    protected List<PerfilDTO> perfisDTO;
    @XmlElement(nillable = true)
    protected List<PeriodicidadeDTO> periodicidadesDTO;
    @XmlElement(nillable = true)
    protected List<ProdutoDTO> produtosDTO;
    @XmlElement(nillable = true)
    protected List<RegiaoDTO> regioesDTO;
    @XmlElement(nillable = true)
    protected List<SubFuncaoDTO> subFuncoesDTO;
    @XmlElement(nillable = true)
    protected List<TipoAcaoDTO> tiposAcaoDTO;
    @XmlElement(nillable = true)
    protected List<TipoInclusaoDTO> tiposInclusaoDTO;
    @XmlElement(nillable = true)
    protected List<TipoProgramaDTO> tiposProgramaDTO;
    @XmlElement(nillable = true)
    protected List<UnidadeMedidaDTO> unidadesMedidaDTO;
    @XmlElement(nillable = true)
    protected List<UnidadeMedidaIndicadorDTO> unidadesMedidaIndicadorDTO;

    /**
     * Gets the value of the basesGeograficasDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the basesGeograficasDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBasesGeograficasDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BaseGeograficaDTO }
     * 
     * 
     */
    public List<BaseGeograficaDTO> getBasesGeograficasDTO() {
        if (basesGeograficasDTO == null) {
            basesGeograficasDTO = new ArrayList<BaseGeograficaDTO>();
        }
        return this.basesGeograficasDTO;
    }

    /**
     * Gets the value of the esferasDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the esferasDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEsferasDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EsferaDTO }
     * 
     * 
     */
    public List<EsferaDTO> getEsferasDTO() {
        if (esferasDTO == null) {
            esferasDTO = new ArrayList<EsferaDTO>();
        }
        return this.esferasDTO;
    }

    /**
     * Gets the value of the funcoesDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the funcoesDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFuncoesDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuncaoDTO }
     * 
     * 
     */
    public List<FuncaoDTO> getFuncoesDTO() {
        if (funcoesDTO == null) {
            funcoesDTO = new ArrayList<FuncaoDTO>();
        }
        return this.funcoesDTO;
    }

    /**
     * Gets the value of the macroDesafiosDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the macroDesafiosDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMacroDesafiosDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MacroDesafioDTO }
     * 
     * 
     */
    public List<MacroDesafioDTO> getMacroDesafiosDTO() {
        if (macroDesafiosDTO == null) {
            macroDesafiosDTO = new ArrayList<MacroDesafioDTO>();
        }
        return this.macroDesafiosDTO;
    }

    /**
     * Gets the value of the momentosDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the momentosDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMomentosDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MomentoDTO }
     * 
     * 
     */
    public List<MomentoDTO> getMomentosDTO() {
        if (momentosDTO == null) {
            momentosDTO = new ArrayList<MomentoDTO>();
        }
        return this.momentosDTO;
    }

    /**
     * Gets the value of the perfisDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the perfisDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerfisDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PerfilDTO }
     * 
     * 
     */
    public List<PerfilDTO> getPerfisDTO() {
        if (perfisDTO == null) {
            perfisDTO = new ArrayList<PerfilDTO>();
        }
        return this.perfisDTO;
    }

    /**
     * Gets the value of the periodicidadesDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the periodicidadesDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPeriodicidadesDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PeriodicidadeDTO }
     * 
     * 
     */
    public List<PeriodicidadeDTO> getPeriodicidadesDTO() {
        if (periodicidadesDTO == null) {
            periodicidadesDTO = new ArrayList<PeriodicidadeDTO>();
        }
        return this.periodicidadesDTO;
    }

    /**
     * Gets the value of the produtosDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the produtosDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProdutosDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProdutoDTO }
     * 
     * 
     */
    public List<ProdutoDTO> getProdutosDTO() {
        if (produtosDTO == null) {
            produtosDTO = new ArrayList<ProdutoDTO>();
        }
        return this.produtosDTO;
    }

    /**
     * Gets the value of the regioesDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regioesDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegioesDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegiaoDTO }
     * 
     * 
     */
    public List<RegiaoDTO> getRegioesDTO() {
        if (regioesDTO == null) {
            regioesDTO = new ArrayList<RegiaoDTO>();
        }
        return this.regioesDTO;
    }

    /**
     * Gets the value of the subFuncoesDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subFuncoesDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubFuncoesDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubFuncaoDTO }
     * 
     * 
     */
    public List<SubFuncaoDTO> getSubFuncoesDTO() {
        if (subFuncoesDTO == null) {
            subFuncoesDTO = new ArrayList<SubFuncaoDTO>();
        }
        return this.subFuncoesDTO;
    }

    /**
     * Gets the value of the tiposAcaoDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tiposAcaoDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTiposAcaoDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoAcaoDTO }
     * 
     * 
     */
    public List<TipoAcaoDTO> getTiposAcaoDTO() {
        if (tiposAcaoDTO == null) {
            tiposAcaoDTO = new ArrayList<TipoAcaoDTO>();
        }
        return this.tiposAcaoDTO;
    }

    /**
     * Gets the value of the tiposInclusaoDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tiposInclusaoDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTiposInclusaoDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoInclusaoDTO }
     * 
     * 
     */
    public List<TipoInclusaoDTO> getTiposInclusaoDTO() {
        if (tiposInclusaoDTO == null) {
            tiposInclusaoDTO = new ArrayList<TipoInclusaoDTO>();
        }
        return this.tiposInclusaoDTO;
    }

    /**
     * Gets the value of the tiposProgramaDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tiposProgramaDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTiposProgramaDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoProgramaDTO }
     * 
     * 
     */
    public List<TipoProgramaDTO> getTiposProgramaDTO() {
        if (tiposProgramaDTO == null) {
            tiposProgramaDTO = new ArrayList<TipoProgramaDTO>();
        }
        return this.tiposProgramaDTO;
    }

    /**
     * Gets the value of the unidadesMedidaDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unidadesMedidaDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnidadesMedidaDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnidadeMedidaDTO }
     * 
     * 
     */
    public List<UnidadeMedidaDTO> getUnidadesMedidaDTO() {
        if (unidadesMedidaDTO == null) {
            unidadesMedidaDTO = new ArrayList<UnidadeMedidaDTO>();
        }
        return this.unidadesMedidaDTO;
    }

    /**
     * Gets the value of the unidadesMedidaIndicadorDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unidadesMedidaIndicadorDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnidadesMedidaIndicadorDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnidadeMedidaIndicadorDTO }
     * 
     * 
     */
    public List<UnidadeMedidaIndicadorDTO> getUnidadesMedidaIndicadorDTO() {
        if (unidadesMedidaIndicadorDTO == null) {
            unidadesMedidaIndicadorDTO = new ArrayList<UnidadeMedidaIndicadorDTO>();
        }
        return this.unidadesMedidaIndicadorDTO;
    }

}
