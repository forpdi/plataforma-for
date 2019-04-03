package org.forrisco.core.policy;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.system.PDFgenerate;
import org.forrisco.core.policy.permissions.ManagePolicyPermission;
import org.forrisco.core.unit.Unit;
import org.forrisco.core.unit.UnitBS;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskBS;
import org.forrisco.risk.RiskLevel;

import com.itextpdf.text.DocumentException;

import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.Item;
import org.forrisco.core.item.ItemBS;
import org.forrisco.core.item.SubItem;
import org.forrisco.core.plan.PlanRisk;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Matheus Nascimento
 */
@Controller
public class PolicyController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private PolicyBS policyBS;
	@Inject private ItemBS itemBS;
	@Inject private RiskBS riskBS;
	@Inject private UnitBS unitBS;
	@Inject private PDFgenerate pdf;
	
	protected static final String PATH =  BASEPATH +"/policy";
	
	
	/**
	 * Salvar Nova Política
	 * 
	 * @return void
	 */
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void savePolicy(@NotNull @Valid  Policy policy){
		
		try {
			if(this.domain == null) {
				this.fail("Instituição não definida");
				return;
			}
			policy.setCompany(this.domain.getCompany());
			policy.setId(null);
			this.policyBS.save(policy);
			this.riskBS.saveRiskLevel(policy);
			this.success(policy);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}


	/**
	 * 
	 * Lista as políticas arquivadas
	 * 
	 * @param page
	 *            Número da página da listagem a ser acessada.
	 * @return PaginatedList<PlanMacro> Lista de planos macro arquivados da
	 *         companhia.
	 */
	@Get( PATH + "/archivedpolicy")
	@NoCache
	@Permissioned
	public void listMacrosArchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
				PaginatedList<Policy> policy = this.policyBS.listPolicies(this.domain.getCompany(), true, page);
				this.success(policy);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	
	@Get( PATH + "/unarchivedpolicy")
	@NoCache
	@Permissioned
	public void listPolicyUnarchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
			PaginatedList<Policy> policies = this.policyBS.listPolicies(this.domain.getCompany(), false, page);
				this.success(policies);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}


	/**
	 * Retorna política.
	 * 
	 * @param id
	 *            Id da política a ser retornado.
	 * @return Policy Retorna a política de acordo com o id passado.
	 */
	@Get( PATH + "/{id}")
	@NoCache
	@Permissioned
	public void retrievePolicy(Long id) {
		try {
			Policy policy = this.policyBS.exists(id, Policy.class);
			if (policy == null) {
				this.fail("A política solicitada não foi encontrado.");
			} else {
				this.success(policy);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna graus de risco.
	 * 
	 * @param id
	 *            Id da política.
	 * @return RiskLevel Retorna os graus da política (id) passada.
	 * 
	 */
	@Get(PATH + "/risklevel/{id}")
	@NoCache
	@Permissioned
	public void retrieveRiskLevel(Long id) {
		try {
			Policy policy = this.riskBS.exists(id, Policy.class);
			if (policy == null) {
				this.fail("A política solicitada não foi encontrado.");
			} else {
				PaginatedList<RiskLevel> risklevels = this.riskBS.listRiskLevelByPolicy(policy);
				this.success(risklevels);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	

	/**
	 * Exclui política.
	 * 
	 * @param id
	 *            Id da política ser excluído.
	 *
	 */
	@Delete( PATH + "/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void deletePolicy(@NotNull Long id) {
		try {
			Policy policy = this.policyBS.exists(id, Policy.class);
			if (GeneralUtils.isInvalid(policy)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<PlanRisk> plans = this.policyBS.listPlanbyPolicy(policy);
			PaginatedList<RiskLevel>  riskLevels= this.policyBS.listRiskLevelbyPolicy(policy);
			
			if(plans.getTotal() >0) {
				this.fail("Impossível excluir política com Plano(s) de Risco vinculado(s)");
			}else {

				PaginatedList<Item> itens = this.itemBS.listItensByPolicy(policy);

				for(Item item : itens.getList()){

					PaginatedList<FieldItem> fields = this.itemBS.listFieldsByItem(item);
					for(FieldItem field : fields.getList()){
						this.itemBS.delete(field);
					}
					
					this.itemBS.deleteSubitens(item);

					this.itemBS.delete(item);
				}

				for(RiskLevel riskLevel : riskLevels.getList()){
					this.riskBS.delete(riskLevel);
				}

				//policy.setDeleted(true);
				this.policyBS.delete(policy);
				this.success();
			}

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Edita política.
	 * 
	 * @param policy
	 *            política a ser alterado com os novos campos.
	 */
	@Post( PATH + "/update")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void Policy(@NotNull @Valid Policy policy) {
		try {
			Policy existent = this.policyBS.exists(policy.getId(), Policy.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.fail("Política não encontrada");
				return;
			}

			if(existent.getCompany()==null) {
				this.fail("Política sem Istituição associada");
				return;
			}

			PaginatedList<RiskLevel> existentLevels = this.policyBS.listRiskLevelbyPolicy(existent);
			List<Risk> risks = new ArrayList<Risk>();
			
			//pegar os riscos associados
			PaginatedList<PlanRisk> plans = this.policyBS.listPlanbyPolicy(existent);
			for(PlanRisk plan : plans.getList()) {
				PaginatedList<Unit> units = this.unitBS.listUnitsbyPlanRisk(plan);
				for(Unit unit : units.getList()) {
					PaginatedList<Risk> localrisks = this.riskBS.listRiskByUnit(unit);
					risks.addAll(localrisks.getList());
				}
			}
			

			//para alterar quantidade de graus de risco/linhas/colunas
			//não podem existir riscos vinculados
			if(policy.getNcolumn() != existent.getNcolumn() 
				|| policy.getNline() != existent.getNline()
				|| policy.getLevels() != existentLevels.getTotal()) {
			
				if (!risks.isEmpty()) {
					this.fail("Impossível modificar política com Risco(s) vinculado(s)");
					return;
				}
			}
			
			//atualiza graus de risco
			//se a quantidade de os novos graus >= graus antigos 
			for(int i=0; i < (policy.getLevels() > existentLevels.getTotal() ? (policy.getLevels()): existentLevels.getTotal()) ;i++) {
				
				if(i < existentLevels.getTotal() && i < policy.getLevels() ) {
					if(!existentLevels.getList().get(i).getLevel().equals(policy.getRisk_level()[0][i]) 
						|| existentLevels.getList().get(i).getColor() != Integer.parseInt(policy.getRisk_level()[1][i])) {
					
						this.riskBS.delete(existentLevels.getList().get(i));
						
						RiskLevel level= new RiskLevel(existent,
										Integer.parseInt(policy.getRisk_level()[1][i]),
										policy.getRisk_level()[0][i]);

						this.riskBS.saveRiskLevel(level);
					}
				}else if(i >= existentLevels.getTotal()){
					RiskLevel level= new RiskLevel(existent,
							Integer.parseInt(policy.getRisk_level()[1][i]),
							policy.getRisk_level()[0][i]);

					this.riskBS.saveRiskLevel(level);
					
				}else{
					this.riskBS.delete(existentLevels.getList().get(i));
				}
			}
			
			
			String[][] matrix = this.riskBS.getMatrixVector(policy);
			String[][] matrix_old = this.riskBS.getMatrixVector(existent);
			Map <String,String> impact_probability = new HashMap<String,String>();
			
			//atualizar política
			existent.setDescription(policy.getDescription());
			existent.setName(policy.getName());
			existent.setImpact(policy.getImpact());
			existent.setProbability(policy.getProbability());
			existent.setMatrix(policy.getMatrix());
			existent.setNline(policy.getNline());
			existent.setNcolumn(policy.getNcolumn());
			existent.setPIDescriptions(policy.getPIDescriptions());
			this.policyBS.persist(existent);
			
			
			//rótulos podem sempre ser alterados
			//guardar probabilidades e impactos em um map
			///alterar rótulos dos riscos e monitoramentos
			if(!risks.isEmpty()){
				for(int i=0; i<policy.getNline()*policy.getNcolumn()+policy.getNline()+policy.getNcolumn();i++){
					impact_probability.put(matrix_old[i][0], matrix[i][0]);
				}
				
				for(int i=0; i<risks.size(); i++){
					this.riskBS.updateRiskPI(risks.get(i),impact_probability);
				}
				
				//aplicar novamento a função de risklevel para achar o grau de risco correspondente
				for(int i=0; i<risks.size(); i++){
					RiskLevel rl = this.riskBS.getRiskLevelByRisk(risks.get(i),existent);
					risks.get(i).setRiskLevel(rl);
					this.riskBS.saveRisk(risks.get(i));
				}
			}

			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Listar Planos e seus níveis segundo uma chave de busca.
	 * 
	 * @param parentId
	 *            Id da política.
	 * @param page
	 *            Número da página da lista de plano de metas.
	 * @param terms
	 *            Termo de busca.
	 * @param itensSelect
	 *            Conjunto de planos a serem buscados.
	 * @param subitensSelect
	 *            Conjunto de níveis que podem ser buscados.
	 * @param ordResult
	 *            Ordenação do resultado, 1 para crescente e 2 para decrescente.
	 *            
	 * @return PaginatedList<Plan> Retorna lista de planos de metas de acordo
	 *         com os filtros.
	 */
	@Get(PATH + "/findTerms")
	@NoCache
	@Permissioned
	public void listItensTerms(Long policyId, Integer page, String terms, Long itensSelect[], Long subitensSelect[], int ordResult, Long limit) {
		if (page == null)
			page = 0;
		
		try {
			Policy policy = this.policyBS.exists(policyId, Policy.class);
			
			List<Item> itens = this.policyBS.listItemTerms(policy, terms, itensSelect, ordResult);
			List<SubItem> subitens = this.policyBS.listSubitemTerms(policy, terms, subitensSelect, ordResult);

			PaginatedList<SubItem> result = TermResult(itens,subitens, page, limit);
			
			this.success(result);

 		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	
	@Get(PATH + "/findAllTerms")
	@NoCache
	@Permissioned
	public void listItensTerms(Long policyId, Integer page, String terms, int ordResult, Long limit) {
		if (page == null)
			page = 0;
		
		try {
			Policy policy = this.policyBS.exists(policyId, Policy.class);
			
			List<Item> itens = this.policyBS.listItemTerms(policy, terms, null, ordResult);
			List<SubItem> subitens = this.policyBS.listSubitemTerms(policy, terms, null, ordResult);

			PaginatedList<SubItem> result = TermResult( itens,subitens, page, limit);
			
			this.success(result);
 		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	private PaginatedList<SubItem> TermResult(List<Item> itens, List<SubItem> subitens,  Integer page,  Long limit){
		int firstResult = 0;
		int maxResult = 0;
		int count = 0;
		int add = 0;
		if (limit != null) {
			firstResult = (int) ((page - 1) * limit);
			maxResult = limit.intValue();
		}
		
		for(Item item : itens) {
			SubItem subitem = new SubItem();
			subitem.setDescription(item.getDescription());
			subitem.setId(item.getId());
			subitem.setName(item.getName());
			//item.setSubitemParentId(subitem.getItem().getId());
			subitens.add(subitem);
		}
		
		List<SubItem> list = new ArrayList<>();
		for(SubItem subitem : subitens) {
			if (limit != null) {
				if (count >= firstResult && add < maxResult) {
					list.add(subitem);
					count++;
					add++;
				} else {
					count++;
				}
			} else {
				list.add(subitem);
			}
		}

		PaginatedList<SubItem> result = new PaginatedList<SubItem>();
		
		result.setList(list);
		result.setTotal((long)count);
		return result;
	}

	
	/**
	 * Cria arquivo pdf  para exportar relatório  
	 * 
	 * 
	 * @param title
	 * @param author
	 * @param pre
	 * @param item
	 * @param subitem
	 * @throws DocumentException 
	 * @throws IOException 
	 * 
	 */
	@Get(PATH + "/exportReport")
	@NoCache
	@Permissioned
	public void exportreport(Long policyId, String title, String author, boolean pre, String itens, String subitens){
		try {
		
			File pdf = this.pdf.exportPolicyReport(policyId, title, author, itens, subitens);

			OutputStream out;
			FileInputStream fis= new FileInputStream(pdf);
			this.response.reset();
			this.response.setHeader("Content-Type", "application/pdf");
			this.response.setHeader("Content-Disposition", "inline; filename=\"" + title + ".pdf\"");
			out = this.response.getOutputStream();
			
			IOUtils.copy(fis, out);
			out.close();
			fis.close();
			pdf.delete();
			this.result.nothing();
			
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail(ex.getMessage());
		}
	}

}