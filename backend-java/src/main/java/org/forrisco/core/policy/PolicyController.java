package org.forrisco.core.policy;


import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import  org.forrisco.core.policy.permissions.ManagePolicyPermission;
import org.forrisco.risk.RiskLevel;
import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.Item;
import org.forrisco.core.item.ItemBS;
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
	@Inject private PolicyBS bs;
	@Inject private ItemBS itemBS;
	
	protected static final String PATH =  BASEPATH +"/policy";
	
	/**
	 * Salvar Nova Política
	 * 
	 * @return void
	 */
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void savePolicy(@NotNull @Valid  Policy policy){
		
		try {
			if(this.domain == null) {
				this.fail("Instituição não definida");
			}
			policy.setCompany(this.domain.getCompany());
			policy.setId(null);
			this.bs.save(policy);
			this.bs.saveRiskLevel(policy);
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
	public void listMacrosArchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
				PaginatedList<Policy> policy = this.bs.listPolicies(this.domain.getCompany(), true, page);
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
	public void listPolicyUnarchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
			PaginatedList<Policy> policies = this.bs.listPolicies(this.domain.getCompany(), false, page);
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
			Policy policy = this.bs.exists(id, Policy.class);
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
	 * Exclui político.
	 * 
	 * @param id
	 *            Id da política ser excluído.
	 *
	 */
	@Delete( PATH + "/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deletePolicy(@NotNull Long id) {
		try {
			
			Policy policy = this.bs.exists(id, Policy.class);
			if (GeneralUtils.isInvalid(policy)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<PlanRisk> plans = this.bs.listPlanbyPolicy(policy);
			PaginatedList<RiskLevel>  riskLevels= this.bs.listRiskLevelbyPolicy(policy);
			
			if(plans.getTotal() >0) {
				this.fail("Impossível excluir política com Planos de Risco vinculados");
			}else {
				
				PaginatedList<Item> itens = this.itemBS.listItensByPolicy(policy);
				
				for(Item item : itens.getList()){
					
					PaginatedList<FieldItem> fields = this.itemBS.listFieldsByItem(item);
					for(FieldItem field : fields.getList()){
						field.setDeleted(true);
						this.itemBS.persist(field);
					}
					
					item.setDeleted(true);
					this.itemBS.persist(item);
				}
				
				for(RiskLevel riskLevel : riskLevels.getList()){
					riskLevel.setDeleted(true);
					this.itemBS.persist(riskLevel);
				}
				
				policy.setDeleted(true);
				this.bs.persist(policy);
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
			Policy existent = this.bs.exists(policy.getId(), Policy.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}

			if(existent.getCompany()==null) {
				this.fail("política sem Istituição associada");
				return;
			}

			//(deletar os grau de riscos emodificar a quantidade de linhas e colunas)
			PaginatedList<PlanRisk> plans = this.bs.listPlanbyPolicy(policy);
			

			//pegar os risco desses planos
			//caso não esteja vazio, retornar falha
			if(plans ==null) {
				this.fail("Impossível modificar política com Risco vinculados");
				return;
			}
			
			PaginatedList<RiskLevel>  riskLevels= this.bs.listRiskLevelbyPolicy(policy);
			
			if(plans.getTotal() >0) {
				
			}else {
				
			}
			
			//existent.setDescription(item.getDescription());
			//existent.setName(item.getName());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}

	@Post( PATH + "/archive")
	public void function5() {
		LOGGER.warn("5");
	}

	@Post( PATH + "/unarchive")
	public void function6() {
		LOGGER.warn("6");
	}

	
	@Post( PATH + "/duplicate")
	public void function1() {
		LOGGER.warn("2");
	}


}