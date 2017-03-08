
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de consultarExecucaoOrcamentaria complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="consultarExecucaoOrcamentaria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credencial" type="{http://servicoweb.siop.sof.planejamento.gov.br/}credencialDTO" minOccurs="0"/>
 *         &lt;element name="filtro" type="{http://servicoweb.siop.sof.planejamento.gov.br/}filtroExecucaoOrcamentariaDTO" minOccurs="0"/>
 *         &lt;element name="selecaoRetorno" type="{http://servicoweb.siop.sof.planejamento.gov.br/}selecaoRetornoExecucaoOrcamentariaDTO" minOccurs="0"/>
 *         &lt;element name="paginacao" type="{http://servicoweb.siop.sof.planejamento.gov.br/}paginacaoDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarExecucaoOrcamentaria", propOrder = {
    "credencial",
    "filtro",
    "selecaoRetorno",
    "paginacao"
})
public class ConsultarExecucaoOrcamentaria {

    protected CredencialDTO credencial;
    protected FiltroExecucaoOrcamentariaDTO filtro;
    protected SelecaoRetornoExecucaoOrcamentariaDTO selecaoRetorno;
    protected PaginacaoDTO paginacao;

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
     * Obtém o valor da propriedade filtro.
     * 
     * @return
     *     possible object is
     *     {@link FiltroExecucaoOrcamentariaDTO }
     *     
     */
    public FiltroExecucaoOrcamentariaDTO getFiltro() {
        return filtro;
    }

    /**
     * Define o valor da propriedade filtro.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroExecucaoOrcamentariaDTO }
     *     
     */
    public void setFiltro(FiltroExecucaoOrcamentariaDTO value) {
        this.filtro = value;
    }

    /**
     * Obtém o valor da propriedade selecaoRetorno.
     * 
     * @return
     *     possible object is
     *     {@link SelecaoRetornoExecucaoOrcamentariaDTO }
     *     
     */
    public SelecaoRetornoExecucaoOrcamentariaDTO getSelecaoRetorno() {
        return selecaoRetorno;
    }

    /**
     * Define o valor da propriedade selecaoRetorno.
     * 
     * @param value
     *     allowed object is
     *     {@link SelecaoRetornoExecucaoOrcamentariaDTO }
     *     
     */
    public void setSelecaoRetorno(SelecaoRetornoExecucaoOrcamentariaDTO value) {
        this.selecaoRetorno = value;
    }

    /**
     * Obtém o valor da propriedade paginacao.
     * 
     * @return
     *     possible object is
     *     {@link PaginacaoDTO }
     *     
     */
    public PaginacaoDTO getPaginacao() {
        return paginacao;
    }

    /**
     * Define o valor da propriedade paginacao.
     * 
     * @param value
     *     allowed object is
     *     {@link PaginacaoDTO }
     *     
     */
    public void setPaginacao(PaginacaoDTO value) {
        this.paginacao = value;
    }

}
