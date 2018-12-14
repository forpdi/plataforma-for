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
	
	/**
	 * Salvar Nova Política
	 * 
	 * @return void
	 */
	@Post("/api/policy/new")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void savePolicy2(@NotNull @Valid  Policy policy){
		
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
	@Get("/api/policy/archivedpolicy")
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

	@Get("/api/policy/unarchivedpolicy")
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

	@Get("/api/policy/{id}")
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
	@Delete("/api/policy/{id}")
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
	

	@Post("/api/policy/archive")
	public void function5() {
		LOGGER.warn("5");
	}

	@Post("/api/policy/unarchive")
	public void function6() {
		LOGGER.warn("6");
	}

	
	@Post("/api/policy/duplicate")
	public void function1() {
		LOGGER.warn("2");
	}


}