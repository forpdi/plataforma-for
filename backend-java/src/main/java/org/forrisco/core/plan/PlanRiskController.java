package org.forrisco.core.plan;

import org.forpdi.core.abstractions.AbstractController;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.core.item.Item;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.policy.PolicyBS;
import org.forrisco.core.policy.permissions.ManagePolicyPermission;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Juliano Afonso
 * 
 */
@Controller
public class PlanRiskController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private Policy policy;
	@Inject private PlanRiskBS planRiskBS;
	@Inject private PlanRisk planRisk;
	 
	protected static final String PATH = BASEPATH +"/planrisk";
	
	/**
	 * Salvar Novo Plano
	 * 
	 * @return void
	 */
	
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	
	public void savePlan(@NotNull @Valid  PlanRisk planRisk) {
		try {
			planRisk.setId(null);
			planRisk.setName(planRisk.getName());
			planRisk.setDescription(planRisk.getDescription());
			
			planRiskBS.save(planRisk);
			
			this.success(planRisk.getId());
			
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Retorna o lista de todos os planos não arquivados.
	 * 
	 * @param page
	 */
	@Get( PATH + "/unarchivedplanrisk")
	@NoCache
	public void listPlanRiskUnarchived(Integer page) {
		if (page == null) page = 0;
		try {
			if (this.domain != null) {
			PaginatedList<PlanRisk> planRisks = this.planRiskBS.listPlanRisk(page);
				this.success(planRisks);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna o plano de risco.
	 * 
	 * @param id
	 * Id do plano de risco.
	 * 
	 * @return PlanRisk Retorna o plano de risco de acordo com o id passado.
	 */
	@Get( PATH + "/{id}")
	@NoCache
	@Permissioned
	public void retrievePlan(Long id) {
		try {
			PlanRisk planRisk = this.planRiskBS.exists(id, PlanRisk.class);
			if (planRisk == null) {
				this.fail("O plano de risco solicitada não foi encontrado.");
			} else {
				this.success(planRisk);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

}
