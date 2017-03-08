
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de obterTabelasApoio complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="obterTabelasApoio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="exercicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="retornarMomentos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarEsferas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarTiposInclusao" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retonarFuncoes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarSubFuncoes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarTiposAcao" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarProdutos" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarUnidadesMedida" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarRegioes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarPerfis" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarTiposPrograma" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarMacroDesafios" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarUnidadesMedidaIndicador" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarPeriodicidades" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="retornarBasesGeograficas" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="dataHoraReferencia" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obterTabelasApoio", propOrder = {
    "credencial",
    "exercicio",
    "retornarMomentos",
    "retornarEsferas",
    "retornarTiposInclusao",
    "retonarFuncoes",
    "retornarSubFuncoes",
    "retornarTiposAcao",
    "retornarProdutos",
    "retornarUnidadesMedida",
    "retornarRegioes",
    "retornarPerfis",
    "retornarTiposPrograma",
    "retornarMacroDesafios",
    "retornarUnidadesMedidaIndicador",
    "retornarPeriodicidades",
    "retornarBasesGeograficas",
    "dataHoraReferencia"
})
public class ObterTabelasApoio {

    protected CredencialDTO credencial;
    protected Integer exercicio;
    protected boolean retornarMomentos;
    protected boolean retornarEsferas;
    protected boolean retornarTiposInclusao;
    protected boolean retonarFuncoes;
    protected boolean retornarSubFuncoes;
    protected boolean retornarTiposAcao;
    protected boolean retornarProdutos;
    protected boolean retornarUnidadesMedida;
    protected boolean retornarRegioes;
    protected boolean retornarPerfis;
    protected boolean retornarTiposPrograma;
    protected boolean retornarMacroDesafios;
    protected boolean retornarUnidadesMedidaIndicador;
    protected boolean retornarPeriodicidades;
    protected boolean retornarBasesGeograficas;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataHoraReferencia;

    /**
     * Obtém o valor da propriedade credencial.
     * 
     * @return
     *     possible object is
     *     {@link CredencialDTO }
     *     
     */
    public CredencialDTO getCredencial() {
        return credencial;
    }

    /**
     * Define o valor da propriedade credencial.
     * 
     * @param value
     *     allowed object is
     *     {@link CredencialDTO }
     *     
     */
    public void setCredencial(CredencialDTO value) {
        this.credencial = value;
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
     * Obtém o valor da propriedade retornarMomentos.
     * 
     */
    public boolean isRetornarMomentos() {
        return retornarMomentos;
    }

    /**
     * Define o valor da propriedade retornarMomentos.
     * 
     */
    public void setRetornarMomentos(boolean value) {
        this.retornarMomentos = value;
    }

    /**
     * Obtém o valor da propriedade retornarEsferas.
     * 
     */
    public boolean isRetornarEsferas() {
        return retornarEsferas;
    }

    /**
     * Define o valor da propriedade retornarEsferas.
     * 
     */
    public void setRetornarEsferas(boolean value) {
        this.retornarEsferas = value;
    }

    /**
     * Obtém o valor da propriedade retornarTiposInclusao.
     * 
     */
    public boolean isRetornarTiposInclusao() {
        return retornarTiposInclusao;
    }

    /**
     * Define o valor da propriedade retornarTiposInclusao.
     * 
     */
    public void setRetornarTiposInclusao(boolean value) {
        this.retornarTiposInclusao = value;
    }

    /**
     * Obtém o valor da propriedade retonarFuncoes.
     * 
     */
    public boolean isRetonarFuncoes() {
        return retonarFuncoes;
    }

    /**
     * Define o valor da propriedade retonarFuncoes.
     * 
     */
    public void setRetonarFuncoes(boolean value) {
        this.retonarFuncoes = value;
    }

    /**
     * Obtém o valor da propriedade retornarSubFuncoes.
     * 
     */
    public boolean isRetornarSubFuncoes() {
        return retornarSubFuncoes;
    }

    /**
     * Define o valor da propriedade retornarSubFuncoes.
     * 
     */
    public void setRetornarSubFuncoes(boolean value) {
        this.retornarSubFuncoes = value;
    }

    /**
     * Obtém o valor da propriedade retornarTiposAcao.
     * 
     */
    public boolean isRetornarTiposAcao() {
        return retornarTiposAcao;
    }

    /**
     * Define o valor da propriedade retornarTiposAcao.
     * 
     */
    public void setRetornarTiposAcao(boolean value) {
        this.retornarTiposAcao = value;
    }

    /**
     * Obtém o valor da propriedade retornarProdutos.
     * 
     */
    public boolean isRetornarProdutos() {
        return retornarProdutos;
    }

    /**
     * Define o valor da propriedade retornarProdutos.
     * 
     */
    public void setRetornarProdutos(boolean value) {
        this.retornarProdutos = value;
    }

    /**
     * Obtém o valor da propriedade retornarUnidadesMedida.
     * 
     */
    public boolean isRetornarUnidadesMedida() {
        return retornarUnidadesMedida;
    }

    /**
     * Define o valor da propriedade retornarUnidadesMedida.
     * 
     */
    public void setRetornarUnidadesMedida(boolean value) {
        this.retornarUnidadesMedida = value;
    }

    /**
     * Obtém o valor da propriedade retornarRegioes.
     * 
     */
    public boolean isRetornarRegioes() {
        return retornarRegioes;
    }

    /**
     * Define o valor da propriedade retornarRegioes.
     * 
     */
    public void setRetornarRegioes(boolean value) {
        this.retornarRegioes = value;
    }

    /**
     * Obtém o valor da propriedade retornarPerfis.
     * 
     */
    public boolean isRetornarPerfis() {
        return retornarPerfis;
    }

    /**
     * Define o valor da propriedade retornarPerfis.
     * 
     */
    public void setRetornarPerfis(boolean value) {
        this.retornarPerfis = value;
    }

    /**
     * Obtém o valor da propriedade retornarTiposPrograma.
     * 
     */
    public boolean isRetornarTiposPrograma() {
        return retornarTiposPrograma;
    }

    /**
     * Define o valor da propriedade retornarTiposPrograma.
     * 
     */
    public void setRetornarTiposPrograma(boolean value) {
        this.retornarTiposPrograma = value;
    }

    /**
     * Obtém o valor da propriedade retornarMacroDesafios.
     * 
     */
    public boolean isRetornarMacroDesafios() {
        return retornarMacroDesafios;
    }

    /**
     * Define o valor da propriedade retornarMacroDesafios.
     * 
     */
    public void setRetornarMacroDesafios(boolean value) {
        this.retornarMacroDesafios = value;
    }

    /**
     * Obtém o valor da propriedade retornarUnidadesMedidaIndicador.
     * 
     */
    public boolean isRetornarUnidadesMedidaIndicador() {
        return retornarUnidadesMedidaIndicador;
    }

    /**
     * Define o valor da propriedade retornarUnidadesMedidaIndicador.
     * 
     */
    public void setRetornarUnidadesMedidaIndicador(boolean value) {
        this.retornarUnidadesMedidaIndicador = value;
    }

    /**
     * Obtém o valor da propriedade retornarPeriodicidades.
     * 
     */
    public boolean isRetornarPeriodicidades() {
        return retornarPeriodicidades;
    }

    /**
     * Define o valor da propriedade retornarPeriodicidades.
     * 
     */
    public void setRetornarPeriodicidades(boolean value) {
        this.retornarPeriodicidades = value;
    }

    /**
     * Obtém o valor da propriedade retornarBasesGeograficas.
     * 
     */
    public boolean isRetornarBasesGeograficas() {
        return retornarBasesGeograficas;
    }

    /**
     * Define o valor da propriedade retornarBasesGeograficas.
     * 
     */
    public void setRetornarBasesGeograficas(boolean value) {
        this.retornarBasesGeograficas = value;
    }

    /**
     * Obtém o valor da propriedade dataHoraReferencia.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHoraReferencia() {
        return dataHoraReferencia;
    }

    /**
     * Define o valor da propriedade dataHoraReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHoraReferencia(XMLGregorianCalendar value) {
        this.dataHoraReferencia = value;
    }

}
