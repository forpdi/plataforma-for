
package br.gov.planejamento.sof.siop.servicoweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de baseDTO complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="baseDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseDTO")
@XmlSeeAlso({
    ExecucaoReceitaMensalDTO.class,
    ExecucaoLocalizadorDTO.class,
    ExecucaoMensalDTO.class,
    ExecucaoUnidadeOrcamentariaDTO.class,
    CredencialDTO.class,
    ExecucaoEstataisDTO.class,
    ExecucaoSubFuncaoDTO.class,
    ExecucaoFuncaoDTO.class,
    InputExecucaoProgramaDTO.class,
    ExecucaoOrgaoDTO.class,
    ExecucaoProgramaDTO.class,
    InputExecucaoFuncaoDTO.class,
    ExecucaoDespesaDTO.class,
    InputExecucaoEstataisDTO.class,
    InputExecucaoLocalizadorDTO.class,
    InputExecucaoUnidadeOrcamentariaDTO.class,
    InputExecucaoAcaoDTO.class,
    ExecucaoAcaoDTO.class,
    InputExecucaoOrgaoDTO.class,
    InputExecucaoSubFuncaoDTO.class,
    InputExecucaoReceitaDTO.class
})
public abstract class BaseDTO {


}
