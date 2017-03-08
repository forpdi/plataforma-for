
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de retornoProgramacaoQualitativoDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="retornoProgramacaoQualitativoDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}retornoDTO">
 *       &lt;sequence>
 *         &lt;element name="acoesDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}acaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="agendasSamDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}agendaSamDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="indicadoresDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}indicadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="iniciativasDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}iniciativaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="localizadoresDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}localizadorDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="medidasInstitucionaisNormativasDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}medidaInstitucionalNormativaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="metasDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}metaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="objetivosDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}objetivoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="orgaosDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}orgaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="planosOrcamentariosDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}planoOrcamentarioDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="programasDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}programaDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="regionalizacoesDTO" type="{http://servicoweb.siop.sof.planejamento.gov.br/}regionalizacaoDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retornoProgramacaoQualitativoDTO", propOrder = {
    "acoesDTO",
    "agendasSamDTO",
    "indicadoresDTO",
    "iniciativasDTO",
    "localizadoresDTO",
    "medidasInstitucionaisNormativasDTO",
    "metasDTO",
    "objetivosDTO",
    "orgaosDTO",
    "planosOrcamentariosDTO",
    "programasDTO",
    "regionalizacoesDTO"
})
public class RetornoProgramacaoQualitativoDTO
    extends RetornoDTO
{

    @XmlElement(nillable = true)
    protected List<AcaoDTO> acoesDTO;
    @XmlElement(nillable = true)
    protected List<AgendaSamDTO> agendasSamDTO;
    @XmlElement(nillable = true)
    protected List<IndicadorDTO> indicadoresDTO;
    @XmlElement(nillable = true)
    protected List<IniciativaDTO> iniciativasDTO;
    @XmlElement(nillable = true)
    protected List<LocalizadorDTO> localizadoresDTO;
    @XmlElement(nillable = true)
    protected List<MedidaInstitucionalNormativaDTO> medidasInstitucionaisNormativasDTO;
    @XmlElement(nillable = true)
    protected List<MetaDTO> metasDTO;
    @XmlElement(nillable = true)
    protected List<ObjetivoDTO> objetivosDTO;
    @XmlElement(nillable = true)
    protected List<OrgaoDTO> orgaosDTO;
    @XmlElement(nillable = true)
    protected List<PlanoOrcamentarioDTO> planosOrcamentariosDTO;
    @XmlElement(nillable = true)
    protected List<ProgramaDTO> programasDTO;
    @XmlElement(nillable = true)
    protected List<RegionalizacaoDTO> regionalizacoesDTO;

    /**
     * Gets the value of the acoesDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acoesDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcoesDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcaoDTO }
     * 
     * 
     */
    public List<AcaoDTO> getAcoesDTO() {
        if (acoesDTO == null) {
            acoesDTO = new ArrayList<AcaoDTO>();
        }
        return this.acoesDTO;
    }

    /**
     * Gets the value of the agendasSamDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agendasSamDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgendasSamDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgendaSamDTO }
     * 
     * 
     */
    public List<AgendaSamDTO> getAgendasSamDTO() {
        if (agendasSamDTO == null) {
            agendasSamDTO = new ArrayList<AgendaSamDTO>();
        }
        return this.agendasSamDTO;
    }

    /**
     * Gets the value of the indicadoresDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indicadoresDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndicadoresDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndicadorDTO }
     * 
     * 
     */
    public List<IndicadorDTO> getIndicadoresDTO() {
        if (indicadoresDTO == null) {
            indicadoresDTO = new ArrayList<IndicadorDTO>();
        }
        return this.indicadoresDTO;
    }

    /**
     * Gets the value of the iniciativasDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iniciativasDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIniciativasDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IniciativaDTO }
     * 
     * 
     */
    public List<IniciativaDTO> getIniciativasDTO() {
        if (iniciativasDTO == null) {
            iniciativasDTO = new ArrayList<IniciativaDTO>();
        }
        return this.iniciativasDTO;
    }

    /**
     * Gets the value of the localizadoresDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localizadoresDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalizadoresDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalizadorDTO }
     * 
     * 
     */
    public List<LocalizadorDTO> getLocalizadoresDTO() {
        if (localizadoresDTO == null) {
            localizadoresDTO = new ArrayList<LocalizadorDTO>();
        }
        return this.localizadoresDTO;
    }

    /**
     * Gets the value of the medidasInstitucionaisNormativasDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the medidasInstitucionaisNormativasDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMedidasInstitucionaisNormativasDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MedidaInstitucionalNormativaDTO }
     * 
     * 
     */
    public List<MedidaInstitucionalNormativaDTO> getMedidasInstitucionaisNormativasDTO() {
        if (medidasInstitucionaisNormativasDTO == null) {
            medidasInstitucionaisNormativasDTO = new ArrayList<MedidaInstitucionalNormativaDTO>();
        }
        return this.medidasInstitucionaisNormativasDTO;
    }

    /**
     * Gets the value of the metasDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metasDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetasDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetaDTO }
     * 
     * 
     */
    public List<MetaDTO> getMetasDTO() {
        if (metasDTO == null) {
            metasDTO = new ArrayList<MetaDTO>();
        }
        return this.metasDTO;
    }

    /**
     * Gets the value of the objetivosDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objetivosDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjetivosDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjetivoDTO }
     * 
     * 
     */
    public List<ObjetivoDTO> getObjetivosDTO() {
        if (objetivosDTO == null) {
            objetivosDTO = new ArrayList<ObjetivoDTO>();
        }
        return this.objetivosDTO;
    }

    /**
     * Gets the value of the orgaosDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orgaosDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrgaosDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrgaoDTO }
     * 
     * 
     */
    public List<OrgaoDTO> getOrgaosDTO() {
        if (orgaosDTO == null) {
            orgaosDTO = new ArrayList<OrgaoDTO>();
        }
        return this.orgaosDTO;
    }

    /**
     * Gets the value of the planosOrcamentariosDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the planosOrcamentariosDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlanosOrcamentariosDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PlanoOrcamentarioDTO }
     * 
     * 
     */
    public List<PlanoOrcamentarioDTO> getPlanosOrcamentariosDTO() {
        if (planosOrcamentariosDTO == null) {
            planosOrcamentariosDTO = new ArrayList<PlanoOrcamentarioDTO>();
        }
        return this.planosOrcamentariosDTO;
    }

    /**
     * Gets the value of the programasDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programasDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProgramasDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProgramaDTO }
     * 
     * 
     */
    public List<ProgramaDTO> getProgramasDTO() {
        if (programasDTO == null) {
            programasDTO = new ArrayList<ProgramaDTO>();
        }
        return this.programasDTO;
    }

    /**
     * Gets the value of the regionalizacoesDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regionalizacoesDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegionalizacoesDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegionalizacaoDTO }
     * 
     * 
     */
    public List<RegionalizacaoDTO> getRegionalizacoesDTO() {
        if (regionalizacoesDTO == null) {
            regionalizacoesDTO = new ArrayList<RegionalizacaoDTO>();
        }
        return this.regionalizacoesDTO;
    }

}
