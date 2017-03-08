
package br.gov.planejamento.sof.siop.servicoweb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de filtroExecucaoOrcamentariaDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="filtroExecucaoOrcamentariaDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://servicoweb.siop.sof.planejamento.gov.br/}baseDTO">
 *       &lt;sequence>
 *         &lt;element name="acoes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="acompanhamentosPO" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="acompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="anoExercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="anoReferencia" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="categoriasEconomicas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="categoriaEconomicas" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="detalhesAcompanhamentoPO" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="detalheAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="elementosDespesa" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="elementoDespesa" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="esferas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="estatalDependente" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="fontes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="funcoes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="gruposNatureza" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="grupoNatureza" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="identificadoresAcompanhamentoPO" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="identificadorAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="idocs" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="idoc" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="idusos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="iduso" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="localizadores" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="modalidadesAplicacao" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="modalidadeAplicacao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="naturezasDespesa" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="naturezaDespesa" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="planosInternos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="planoInterno" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="planosOrcamentarios" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="planoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="programas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resultadosPrimariosAtuais" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="resultadoPrimarioAtual" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resultadosPrimariosLei" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="resultadoPrimarioLei" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="subfuncoes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="subfuncao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="tematicasPO" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="tematicaPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="tiposCredito" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="tipoCredito" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="tiposApropriacaoPO" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="tipoApropriacaoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="unidadesOrcamentarias" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="unidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="unidadesGestorasResponsaveis" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="unidadeGestoraResponsavel" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filtroExecucaoOrcamentariaDTO", propOrder = {
    "acoes",
    "acompanhamentosPO",
    "anoExercicio",
    "anoReferencia",
    "categoriasEconomicas",
    "detalhesAcompanhamentoPO",
    "elementosDespesa",
    "esferas",
    "estatalDependente",
    "fontes",
    "funcoes",
    "gruposNatureza",
    "identificadoresAcompanhamentoPO",
    "idocs",
    "idusos",
    "localizadores",
    "modalidadesAplicacao",
    "naturezasDespesa",
    "planosInternos",
    "planosOrcamentarios",
    "programas",
    "resultadosPrimariosAtuais",
    "resultadosPrimariosLei",
    "subfuncoes",
    "tematicasPO",
    "tiposCredito",
    "tiposApropriacaoPO",
    "unidadesOrcamentarias",
    "unidadesGestorasResponsaveis"
})
public class FiltroExecucaoOrcamentariaDTO
    extends BaseDTO
{

    protected FiltroExecucaoOrcamentariaDTO.Acoes acoes;
    protected FiltroExecucaoOrcamentariaDTO.AcompanhamentosPO acompanhamentosPO;
    protected Integer anoExercicio;
    protected Integer anoReferencia;
    protected FiltroExecucaoOrcamentariaDTO.CategoriasEconomicas categoriasEconomicas;
    protected FiltroExecucaoOrcamentariaDTO.DetalhesAcompanhamentoPO detalhesAcompanhamentoPO;
    protected FiltroExecucaoOrcamentariaDTO.ElementosDespesa elementosDespesa;
    protected FiltroExecucaoOrcamentariaDTO.Esferas esferas;
    protected Boolean estatalDependente;
    protected FiltroExecucaoOrcamentariaDTO.Fontes fontes;
    protected FiltroExecucaoOrcamentariaDTO.Funcoes funcoes;
    protected FiltroExecucaoOrcamentariaDTO.GruposNatureza gruposNatureza;
    protected FiltroExecucaoOrcamentariaDTO.IdentificadoresAcompanhamentoPO identificadoresAcompanhamentoPO;
    protected FiltroExecucaoOrcamentariaDTO.Idocs idocs;
    protected FiltroExecucaoOrcamentariaDTO.Idusos idusos;
    protected FiltroExecucaoOrcamentariaDTO.Localizadores localizadores;
    protected FiltroExecucaoOrcamentariaDTO.ModalidadesAplicacao modalidadesAplicacao;
    protected FiltroExecucaoOrcamentariaDTO.NaturezasDespesa naturezasDespesa;
    protected FiltroExecucaoOrcamentariaDTO.PlanosInternos planosInternos;
    protected FiltroExecucaoOrcamentariaDTO.PlanosOrcamentarios planosOrcamentarios;
    protected FiltroExecucaoOrcamentariaDTO.Programas programas;
    protected FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosAtuais resultadosPrimariosAtuais;
    protected FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosLei resultadosPrimariosLei;
    protected FiltroExecucaoOrcamentariaDTO.Subfuncoes subfuncoes;
    protected FiltroExecucaoOrcamentariaDTO.TematicasPO tematicasPO;
    protected FiltroExecucaoOrcamentariaDTO.TiposCredito tiposCredito;
    protected FiltroExecucaoOrcamentariaDTO.TiposApropriacaoPO tiposApropriacaoPO;
    protected FiltroExecucaoOrcamentariaDTO.UnidadesOrcamentarias unidadesOrcamentarias;
    protected FiltroExecucaoOrcamentariaDTO.UnidadesGestorasResponsaveis unidadesGestorasResponsaveis;

    /**
     * Obtém o valor da propriedade acoes.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Acoes }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Acoes getAcoes() {
        return acoes;
    }

    /**
     * Define o valor da propriedade acoes.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Acoes }
     *     
     */
    public void setAcoes(FiltroExecucaoOrcamentariaDTO.Acoes value) {
        this.acoes = value;
    }

    /**
     * Obtém o valor da propriedade acompanhamentosPO.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.AcompanhamentosPO }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.AcompanhamentosPO getAcompanhamentosPO() {
        return acompanhamentosPO;
    }

    /**
     * Define o valor da propriedade acompanhamentosPO.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.AcompanhamentosPO }
     *     
     */
    public void setAcompanhamentosPO(FiltroExecucaoOrcamentariaDTO.AcompanhamentosPO value) {
        this.acompanhamentosPO = value;
    }

    /**
     * Obtém o valor da propriedade anoExercicio.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnoExercicio() {
        return anoExercicio;
    }

    /**
     * Define o valor da propriedade anoExercicio.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnoExercicio(Integer value) {
        this.anoExercicio = value;
    }

    /**
     * Obtém o valor da propriedade anoReferencia.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnoReferencia() {
        return anoReferencia;
    }

    /**
     * Define o valor da propriedade anoReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnoReferencia(Integer value) {
        this.anoReferencia = value;
    }

    /**
     * Obtém o valor da propriedade categoriasEconomicas.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.CategoriasEconomicas }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.CategoriasEconomicas getCategoriasEconomicas() {
        return categoriasEconomicas;
    }

    /**
     * Define o valor da propriedade categoriasEconomicas.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.CategoriasEconomicas }
     *     
     */
    public void setCategoriasEconomicas(FiltroExecucaoOrcamentariaDTO.CategoriasEconomicas value) {
        this.categoriasEconomicas = value;
    }

    /**
     * Obtém o valor da propriedade detalhesAcompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.DetalhesAcompanhamentoPO }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.DetalhesAcompanhamentoPO getDetalhesAcompanhamentoPO() {
        return detalhesAcompanhamentoPO;
    }

    /**
     * Define o valor da propriedade detalhesAcompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.DetalhesAcompanhamentoPO }
     *     
     */
    public void setDetalhesAcompanhamentoPO(FiltroExecucaoOrcamentariaDTO.DetalhesAcompanhamentoPO value) {
        this.detalhesAcompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade elementosDespesa.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ElementosDespesa }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.ElementosDespesa getElementosDespesa() {
        return elementosDespesa;
    }

    /**
     * Define o valor da propriedade elementosDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ElementosDespesa }
     *     
     */
    public void setElementosDespesa(FiltroExecucaoOrcamentariaDTO.ElementosDespesa value) {
        this.elementosDespesa = value;
    }

    /**
     * Obtém o valor da propriedade esferas.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Esferas }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Esferas getEsferas() {
        return esferas;
    }

    /**
     * Define o valor da propriedade esferas.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Esferas }
     *     
     */
    public void setEsferas(FiltroExecucaoOrcamentariaDTO.Esferas value) {
        this.esferas = value;
    }

    /**
     * Obtém o valor da propriedade estatalDependente.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstatalDependente() {
        return estatalDependente;
    }

    /**
     * Define o valor da propriedade estatalDependente.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstatalDependente(Boolean value) {
        this.estatalDependente = value;
    }

    /**
     * Obtém o valor da propriedade fontes.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Fontes }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Fontes getFontes() {
        return fontes;
    }

    /**
     * Define o valor da propriedade fontes.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Fontes }
     *     
     */
    public void setFontes(FiltroExecucaoOrcamentariaDTO.Fontes value) {
        this.fontes = value;
    }

    /**
     * Obtém o valor da propriedade funcoes.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Funcoes }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Funcoes getFuncoes() {
        return funcoes;
    }

    /**
     * Define o valor da propriedade funcoes.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Funcoes }
     *     
     */
    public void setFuncoes(FiltroExecucaoOrcamentariaDTO.Funcoes value) {
        this.funcoes = value;
    }

    /**
     * Obtém o valor da propriedade gruposNatureza.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.GruposNatureza }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.GruposNatureza getGruposNatureza() {
        return gruposNatureza;
    }

    /**
     * Define o valor da propriedade gruposNatureza.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.GruposNatureza }
     *     
     */
    public void setGruposNatureza(FiltroExecucaoOrcamentariaDTO.GruposNatureza value) {
        this.gruposNatureza = value;
    }

    /**
     * Obtém o valor da propriedade identificadoresAcompanhamentoPO.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.IdentificadoresAcompanhamentoPO }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.IdentificadoresAcompanhamentoPO getIdentificadoresAcompanhamentoPO() {
        return identificadoresAcompanhamentoPO;
    }

    /**
     * Define o valor da propriedade identificadoresAcompanhamentoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.IdentificadoresAcompanhamentoPO }
     *     
     */
    public void setIdentificadoresAcompanhamentoPO(FiltroExecucaoOrcamentariaDTO.IdentificadoresAcompanhamentoPO value) {
        this.identificadoresAcompanhamentoPO = value;
    }

    /**
     * Obtém o valor da propriedade idocs.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Idocs }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Idocs getIdocs() {
        return idocs;
    }

    /**
     * Define o valor da propriedade idocs.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Idocs }
     *     
     */
    public void setIdocs(FiltroExecucaoOrcamentariaDTO.Idocs value) {
        this.idocs = value;
    }

    /**
     * Obtém o valor da propriedade idusos.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Idusos }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Idusos getIdusos() {
        return idusos;
    }

    /**
     * Define o valor da propriedade idusos.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Idusos }
     *     
     */
    public void setIdusos(FiltroExecucaoOrcamentariaDTO.Idusos value) {
        this.idusos = value;
    }

    /**
     * Obtém o valor da propriedade localizadores.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Localizadores }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Localizadores getLocalizadores() {
        return localizadores;
    }

    /**
     * Define o valor da propriedade localizadores.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Localizadores }
     *     
     */
    public void setLocalizadores(FiltroExecucaoOrcamentariaDTO.Localizadores value) {
        this.localizadores = value;
    }

    /**
     * Obtém o valor da propriedade modalidadesAplicacao.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ModalidadesAplicacao }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.ModalidadesAplicacao getModalidadesAplicacao() {
        return modalidadesAplicacao;
    }

    /**
     * Define o valor da propriedade modalidadesAplicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ModalidadesAplicacao }
     *     
     */
    public void setModalidadesAplicacao(FiltroExecucaoOrcamentariaDTO.ModalidadesAplicacao value) {
        this.modalidadesAplicacao = value;
    }

    /**
     * Obtém o valor da propriedade naturezasDespesa.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.NaturezasDespesa }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.NaturezasDespesa getNaturezasDespesa() {
        return naturezasDespesa;
    }

    /**
     * Define o valor da propriedade naturezasDespesa.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.NaturezasDespesa }
     *     
     */
    public void setNaturezasDespesa(FiltroExecucaoOrcamentariaDTO.NaturezasDespesa value) {
        this.naturezasDespesa = value;
    }

    /**
     * Obtém o valor da propriedade planosInternos.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.PlanosInternos }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.PlanosInternos getPlanosInternos() {
        return planosInternos;
    }

    /**
     * Define o valor da propriedade planosInternos.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.PlanosInternos }
     *     
     */
    public void setPlanosInternos(FiltroExecucaoOrcamentariaDTO.PlanosInternos value) {
        this.planosInternos = value;
    }

    /**
     * Obtém o valor da propriedade planosOrcamentarios.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.PlanosOrcamentarios }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.PlanosOrcamentarios getPlanosOrcamentarios() {
        return planosOrcamentarios;
    }

    /**
     * Define o valor da propriedade planosOrcamentarios.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.PlanosOrcamentarios }
     *     
     */
    public void setPlanosOrcamentarios(FiltroExecucaoOrcamentariaDTO.PlanosOrcamentarios value) {
        this.planosOrcamentarios = value;
    }

    /**
     * Obtém o valor da propriedade programas.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Programas }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Programas getProgramas() {
        return programas;
    }

    /**
     * Define o valor da propriedade programas.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Programas }
     *     
     */
    public void setProgramas(FiltroExecucaoOrcamentariaDTO.Programas value) {
        this.programas = value;
    }

    /**
     * Obtém o valor da propriedade resultadosPrimariosAtuais.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosAtuais }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosAtuais getResultadosPrimariosAtuais() {
        return resultadosPrimariosAtuais;
    }

    /**
     * Define o valor da propriedade resultadosPrimariosAtuais.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosAtuais }
     *     
     */
    public void setResultadosPrimariosAtuais(FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosAtuais value) {
        this.resultadosPrimariosAtuais = value;
    }

    /**
     * Obtém o valor da propriedade resultadosPrimariosLei.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosLei }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosLei getResultadosPrimariosLei() {
        return resultadosPrimariosLei;
    }

    /**
     * Define o valor da propriedade resultadosPrimariosLei.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosLei }
     *     
     */
    public void setResultadosPrimariosLei(FiltroExecucaoOrcamentariaDTO.ResultadosPrimariosLei value) {
        this.resultadosPrimariosLei = value;
    }

    /**
     * Obtém o valor da propriedade subfuncoes.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Subfuncoes }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.Subfuncoes getSubfuncoes() {
        return subfuncoes;
    }

    /**
     * Define o valor da propriedade subfuncoes.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.Subfuncoes }
     *     
     */
    public void setSubfuncoes(FiltroExecucaoOrcamentariaDTO.Subfuncoes value) {
        this.subfuncoes = value;
    }

    /**
     * Obtém o valor da propriedade tematicasPO.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.TematicasPO }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.TematicasPO getTematicasPO() {
        return tematicasPO;
    }

    /**
     * Define o valor da propriedade tematicasPO.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.TematicasPO }
     *     
     */
    public void setTematicasPO(FiltroExecucaoOrcamentariaDTO.TematicasPO value) {
        this.tematicasPO = value;
    }

    /**
     * Obtém o valor da propriedade tiposCredito.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.TiposCredito }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.TiposCredito getTiposCredito() {
        return tiposCredito;
    }

    /**
     * Define o valor da propriedade tiposCredito.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.TiposCredito }
     *     
     */
    public void setTiposCredito(FiltroExecucaoOrcamentariaDTO.TiposCredito value) {
        this.tiposCredito = value;
    }

    /**
     * Obtém o valor da propriedade tiposApropriacaoPO.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.TiposApropriacaoPO }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.TiposApropriacaoPO getTiposApropriacaoPO() {
        return tiposApropriacaoPO;
    }

    /**
     * Define o valor da propriedade tiposApropriacaoPO.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.TiposApropriacaoPO }
     *     
     */
    public void setTiposApropriacaoPO(FiltroExecucaoOrcamentariaDTO.TiposApropriacaoPO value) {
        this.tiposApropriacaoPO = value;
    }

    /**
     * Obtém o valor da propriedade unidadesOrcamentarias.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.UnidadesOrcamentarias }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.UnidadesOrcamentarias getUnidadesOrcamentarias() {
        return unidadesOrcamentarias;
    }

    /**
     * Define o valor da propriedade unidadesOrcamentarias.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.UnidadesOrcamentarias }
     *     
     */
    public void setUnidadesOrcamentarias(FiltroExecucaoOrcamentariaDTO.UnidadesOrcamentarias value) {
        this.unidadesOrcamentarias = value;
    }

    /**
     * Obtém o valor da propriedade unidadesGestorasResponsaveis.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO.UnidadesGestorasResponsaveis }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO.UnidadesGestorasResponsaveis getUnidadesGestorasResponsaveis() {
        return unidadesGestorasResponsaveis;
    }

    /**
     * Define o valor da propriedade unidadesGestorasResponsaveis.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO.UnidadesGestorasResponsaveis }
     *     
     */
    public void setUnidadesGestorasResponsaveis(FiltroExecucaoOrcamentariaDTO.UnidadesGestorasResponsaveis value) {
        this.unidadesGestorasResponsaveis = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="acao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "acao"
    })
    public static class Acoes {

        protected List<String> acao;

        /**
         * Gets the value of the acao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getAcao() {
            if (acao == null) {
                acao = new ArrayList<String>();
            }
            return this.acao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="acompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "acompanhamentoPO"
    })
    public static class AcompanhamentosPO {

        protected List<String> acompanhamentoPO;

        /**
         * Gets the value of the acompanhamentoPO property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acompanhamentoPO property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcompanhamentoPO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getAcompanhamentoPO() {
            if (acompanhamentoPO == null) {
                acompanhamentoPO = new ArrayList<String>();
            }
            return this.acompanhamentoPO;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="categoriaEconomicas" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "categoriaEconomicas"
    })
    public static class CategoriasEconomicas {

        protected List<String> categoriaEconomicas;

        /**
         * Gets the value of the categoriaEconomicas property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the categoriaEconomicas property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategoriaEconomicas().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getCategoriaEconomicas() {
            if (categoriaEconomicas == null) {
                categoriaEconomicas = new ArrayList<String>();
            }
            return this.categoriaEconomicas;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="detalheAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "detalheAcompanhamentoPO"
    })
    public static class DetalhesAcompanhamentoPO {

        protected List<String> detalheAcompanhamentoPO;

        /**
         * Gets the value of the detalheAcompanhamentoPO property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the detalheAcompanhamentoPO property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDetalheAcompanhamentoPO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getDetalheAcompanhamentoPO() {
            if (detalheAcompanhamentoPO == null) {
                detalheAcompanhamentoPO = new ArrayList<String>();
            }
            return this.detalheAcompanhamentoPO;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="elementoDespesa" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "elementoDespesa"
    })
    public static class ElementosDespesa {

        protected List<String> elementoDespesa;

        /**
         * Gets the value of the elementoDespesa property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the elementoDespesa property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getElementoDespesa().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getElementoDespesa() {
            if (elementoDespesa == null) {
                elementoDespesa = new ArrayList<String>();
            }
            return this.elementoDespesa;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="esfera" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "esfera"
    })
    public static class Esferas {

        protected List<String> esfera;

        /**
         * Gets the value of the esfera property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the esfera property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEsfera().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getEsfera() {
            if (esfera == null) {
                esfera = new ArrayList<String>();
            }
            return this.esfera;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "fonte"
    })
    public static class Fontes {

        protected List<String> fonte;

        /**
         * Gets the value of the fonte property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the fonte property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFonte().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getFonte() {
            if (fonte == null) {
                fonte = new ArrayList<String>();
            }
            return this.fonte;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="funcao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "funcao"
    })
    public static class Funcoes {

        protected List<String> funcao;

        /**
         * Gets the value of the funcao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the funcao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFuncao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getFuncao() {
            if (funcao == null) {
                funcao = new ArrayList<String>();
            }
            return this.funcao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="grupoNatureza" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "grupoNatureza"
    })
    public static class GruposNatureza {

        protected List<String> grupoNatureza;

        /**
         * Gets the value of the grupoNatureza property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the grupoNatureza property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGrupoNatureza().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getGrupoNatureza() {
            if (grupoNatureza == null) {
                grupoNatureza = new ArrayList<String>();
            }
            return this.grupoNatureza;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="identificadorAcompanhamentoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "identificadorAcompanhamentoPO"
    })
    public static class IdentificadoresAcompanhamentoPO {

        protected List<String> identificadorAcompanhamentoPO;

        /**
         * Gets the value of the identificadorAcompanhamentoPO property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the identificadorAcompanhamentoPO property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIdentificadorAcompanhamentoPO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getIdentificadorAcompanhamentoPO() {
            if (identificadorAcompanhamentoPO == null) {
                identificadorAcompanhamentoPO = new ArrayList<String>();
            }
            return this.identificadorAcompanhamentoPO;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="idoc" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "idoc"
    })
    public static class Idocs {

        protected List<String> idoc;

        /**
         * Gets the value of the idoc property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the idoc property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIdoc().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getIdoc() {
            if (idoc == null) {
                idoc = new ArrayList<String>();
            }
            return this.idoc;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="iduso" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "iduso"
    })
    public static class Idusos {

        protected List<String> iduso;

        /**
         * Gets the value of the iduso property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the iduso property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIduso().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getIduso() {
            if (iduso == null) {
                iduso = new ArrayList<String>();
            }
            return this.iduso;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "localizador"
    })
    public static class Localizadores {

        protected List<String> localizador;

        /**
         * Gets the value of the localizador property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the localizador property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLocalizador().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getLocalizador() {
            if (localizador == null) {
                localizador = new ArrayList<String>();
            }
            return this.localizador;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="modalidadeAplicacao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "modalidadeAplicacao"
    })
    public static class ModalidadesAplicacao {

        protected List<String> modalidadeAplicacao;

        /**
         * Gets the value of the modalidadeAplicacao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the modalidadeAplicacao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getModalidadeAplicacao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getModalidadeAplicacao() {
            if (modalidadeAplicacao == null) {
                modalidadeAplicacao = new ArrayList<String>();
            }
            return this.modalidadeAplicacao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="naturezaDespesa" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "naturezaDespesa"
    })
    public static class NaturezasDespesa {

        protected List<String> naturezaDespesa;

        /**
         * Gets the value of the naturezaDespesa property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the naturezaDespesa property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNaturezaDespesa().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getNaturezaDespesa() {
            if (naturezaDespesa == null) {
                naturezaDespesa = new ArrayList<String>();
            }
            return this.naturezaDespesa;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="planoInterno" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "planoInterno"
    })
    public static class PlanosInternos {

        protected List<String> planoInterno;

        /**
         * Gets the value of the planoInterno property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the planoInterno property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPlanoInterno().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPlanoInterno() {
            if (planoInterno == null) {
                planoInterno = new ArrayList<String>();
            }
            return this.planoInterno;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="planoOrcamentario" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "planoOrcamentario"
    })
    public static class PlanosOrcamentarios {

        protected List<String> planoOrcamentario;

        /**
         * Gets the value of the planoOrcamentario property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the planoOrcamentario property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPlanoOrcamentario().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPlanoOrcamentario() {
            if (planoOrcamentario == null) {
                planoOrcamentario = new ArrayList<String>();
            }
            return this.planoOrcamentario;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="programa" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "programa"
    })
    public static class Programas {

        protected List<String> programa;

        /**
         * Gets the value of the programa property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the programa property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPrograma().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPrograma() {
            if (programa == null) {
                programa = new ArrayList<String>();
            }
            return this.programa;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="resultadoPrimarioAtual" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "resultadoPrimarioAtual"
    })
    public static class ResultadosPrimariosAtuais {

        protected List<String> resultadoPrimarioAtual;

        /**
         * Gets the value of the resultadoPrimarioAtual property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the resultadoPrimarioAtual property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResultadoPrimarioAtual().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getResultadoPrimarioAtual() {
            if (resultadoPrimarioAtual == null) {
                resultadoPrimarioAtual = new ArrayList<String>();
            }
            return this.resultadoPrimarioAtual;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="resultadoPrimarioLei" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "resultadoPrimarioLei"
    })
    public static class ResultadosPrimariosLei {

        protected List<String> resultadoPrimarioLei;

        /**
         * Gets the value of the resultadoPrimarioLei property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the resultadoPrimarioLei property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResultadoPrimarioLei().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getResultadoPrimarioLei() {
            if (resultadoPrimarioLei == null) {
                resultadoPrimarioLei = new ArrayList<String>();
            }
            return this.resultadoPrimarioLei;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="subfuncao" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "subfuncao"
    })
    public static class Subfuncoes {

        protected List<String> subfuncao;

        /**
         * Gets the value of the subfuncao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the subfuncao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSubfuncao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getSubfuncao() {
            if (subfuncao == null) {
                subfuncao = new ArrayList<String>();
            }
            return this.subfuncao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="tematicaPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tematicaPO"
    })
    public static class TematicasPO {

        protected List<String> tematicaPO;

        /**
         * Gets the value of the tematicaPO property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tematicaPO property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTematicaPO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getTematicaPO() {
            if (tematicaPO == null) {
                tematicaPO = new ArrayList<String>();
            }
            return this.tematicaPO;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="tipoApropriacaoPO" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoApropriacaoPO"
    })
    public static class TiposApropriacaoPO {

        protected List<String> tipoApropriacaoPO;

        /**
         * Gets the value of the tipoApropriacaoPO property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tipoApropriacaoPO property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTipoApropriacaoPO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getTipoApropriacaoPO() {
            if (tipoApropriacaoPO == null) {
                tipoApropriacaoPO = new ArrayList<String>();
            }
            return this.tipoApropriacaoPO;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="tipoCredito" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoCredito"
    })
    public static class TiposCredito {

        protected List<String> tipoCredito;

        /**
         * Gets the value of the tipoCredito property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tipoCredito property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTipoCredito().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getTipoCredito() {
            if (tipoCredito == null) {
                tipoCredito = new ArrayList<String>();
            }
            return this.tipoCredito;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="unidadeGestoraResponsavel" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "unidadeGestoraResponsavel"
    })
    public static class UnidadesGestorasResponsaveis {

        protected List<String> unidadeGestoraResponsavel;

        /**
         * Gets the value of the unidadeGestoraResponsavel property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the unidadeGestoraResponsavel property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUnidadeGestoraResponsavel().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getUnidadeGestoraResponsavel() {
            if (unidadeGestoraResponsavel == null) {
                unidadeGestoraResponsavel = new ArrayList<String>();
            }
            return this.unidadeGestoraResponsavel;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="unidadeOrcamentaria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "unidadeOrcamentaria"
    })
    public static class UnidadesOrcamentarias {

        protected List<String> unidadeOrcamentaria;

        /**
         * Gets the value of the unidadeOrcamentaria property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the unidadeOrcamentaria property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUnidadeOrcamentaria().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getUnidadeOrcamentaria() {
            if (unidadeOrcamentaria == null) {
                unidadeOrcamentaria = new ArrayList<String>();
            }
            return this.unidadeOrcamentaria;
        }

    }

}
