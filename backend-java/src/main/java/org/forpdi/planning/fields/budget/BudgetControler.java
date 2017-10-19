package org.forpdi.planning.fields.budget;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyBS;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@Controller
public class BudgetControler  extends AbstractController {
	
	@Inject
	private BudgetBS bs;
	@Inject
	private CompanyBS companyBs;
	
	/**
	 * Salvar um novo elemento orçamentário, relacionando a uma instancia company
	 *
	 * @param subAction,
	 *            sub ação orçamentaria
	 * @param instanceId,
	 *            id da instancia de nível
	 * 
	 * @return item, dto do orçamento salvo, com os valores conforme a sub ação
	 */
	@Post(BASEPATH + "/budget/element/create")
	@Consumes
	@NoCache
	@Permissioned
	public void save(@NotEmpty String subAction, @NotNull Double budgetLoa,@NotNull Long companyId) {
		try {
			Company company = this.companyBs.exists(companyId, Company.class);
			if (company == null) {
				this.fail("Empresa inválida!");
				return;
			}

			BudgetElement budgetElement = new BudgetElement();
			budgetElement.setCompany(company);
			budgetElement.setSubAction(subAction);
			budgetElement.setBudgetLoa(budgetLoa);
			budgetElement.setBalanceAvailable(budgetLoa);
			
			this.bs.saveBudgetElement(budgetElement);
			this.success(budgetElement);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Listar todos os orçamentos de simulação
	 * 
	 * @return list, todos as simulações de orçamento
	 */
	@Get(BASEPATH + "/budget/element/list")
	@NoCache
	@Permissioned
	public void listBudgetAction() {
		try {
			PaginatedList<BudgetElement> list = this.bs.listBudgetSimulation();
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	

}
