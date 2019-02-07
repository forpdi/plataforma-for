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
				this.success(planRiskItemField);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
}
