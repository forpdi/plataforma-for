package org.forrisco.core.item;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Juliano Afonso
 */

@Controller
public class PlanRiskItemController extends AbstractController {

	@Inject private PlanRiskItemBS planRiskItemBS;
	@Inject private PlanRisk planRisk;
	
	protected static final String PATH = BASEPATH +"/planrisk/item";
	
	/**
	 * Retorna itens.
	 * 
	 * @param PlanRisk Id do plano de risco a ser retornado.
	 * @return <List> item
	 */
	@Get(PATH + "")
	@NoCache
	public void listItens(@NotNull Long planRiskId) {
		try {
			PlanRisk planRisk = this.planRiskItemBS.exists(planRiskId, PlanRisk.class);
			PaginatedList<PlanRiskItem> itens = this.planRiskItemBS.listItensByPlanRisk(planRisk);
			this.success(itens);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Salva um item
	 *  
	 * @return void
	 */
	@Post(PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void saveItem(@NotNull @Valid PlanRiskItem planRiskItem) {
		try {
			
			if(planRiskItem.getPlanRisk() == null) {
				this.fail("Plano de Risco não encontrado");
			}
			
			planRiskItem.setId(null);
			this.planRiskItemBS.save(planRiskItem);
			this.success(planRiskItem);
			//PaginatedList<PlanRiskItemField> fields = this.planRiskItemBS.listItensByPlanRiskField(planRiskItem);
			
			for(int i = 0; i < planRiskItem.getPlanRiskItemField().size(); i++) {
				PlanRiskItemField planRiskItemField = planRiskItem.getPlanRiskItemField().get(i);
				planRiskItemField.setPlanRiskItem(planRiskItem);
				this.planRiskItemBS.save(planRiskItemField);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Salva um subitem
	 *  
	 * @return void
	 */
	@Post(PATH + "/new/subitem")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void saveSubItem(@NotNull @Valid PlanRiskSubItem planRiskSubItem) {
		try {
			
			if(planRiskSubItem.getPlanRiskItem() == null) {
				this.fail("Item Vinculado não encontrado não encontrado");
			}
			
			planRiskSubItem.setId(null);
			this.planRiskItemBS.save(planRiskSubItem);
			this.success(planRiskSubItem);
			
			for(int i = 0; i < planRiskSubItem.getPlanRiskSubItemField().size(); i++) {
				PlanRiskSubItemField planRiskSubItemField = planRiskSubItem.getPlanRiskSubItemField().get(i);
				planRiskSubItemField.setPlanRiskSubItem(planRiskSubItem);
				this.planRiskItemBS.save(planRiskSubItemField);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna as informações e os Campos de um Item
	 * @param id do item a ser consultado
	 *  
	 * @return void
	 */
	@Get(PATH + "/sub-itens/{id}")
	@NoCache
	public void lisFields(Long id) {
		try {
			PlanRiskItem planRiskItem = this.planRiskItemBS.exists(id, PlanRiskItem.class);
			
			if (planRiskItem == null) {
				this.fail("O Item solicitado não foi encontrado.");
			} else {
				PaginatedList<PlanRiskSubItem> itens = this.planRiskItemBS.listSubItemByItem(planRiskItem);
				this.success(itens);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna as informaçõesde um Item
	 * @param id do item a ser detalhado
	 *  
	 * @return void
	 */
	@Get(PATH + "/{id}")
	@NoCache
	public void detailItem(Long id) {
		try {
			PlanRiskItem planRiskItem = this.planRiskItemBS.exists(id, PlanRiskItem.class);
			
			if (planRiskItem == null) {
				this.fail("O Item solicitado não foi encontrado.");
			} else {
				planRiskItem.setPlanRiskItemField(this.planRiskItemBS.listItensByPlanRiskField(planRiskItem).getList());
				this.success(planRiskItem);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Retorna as informaçõesde um subitem
	 * @param id do subitem a ser detalhado
	 *  
	 * @return void
	 */
	@Get(PATH + "/subitem/{id}")
	@NoCache
	public void detailSubItem(@NotNull Long id) {
		try {
			PlanRiskSubItem planRiskSubItem = this.planRiskItemBS.exists(id, PlanRiskSubItem.class);
			
			if (planRiskSubItem == null) {
				this.fail("O Subitem solicitado não foi encontrado.");
			} else {
				planRiskSubItem.setPlanRiskSubItemField(this.planRiskItemBS.listSubFieldsBySubItem(planRiskSubItem).getList());
				this.success(planRiskSubItem);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
}
