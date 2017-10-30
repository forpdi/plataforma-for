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
public class BudgetController  extends AbstractController {
	
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
	public void save(@NotEmpty String subAction,@NotEmpty String budgetLoa,@NotNull Long companyId) {
		
		try {
			Company company = this.companyBs.exists(companyId, Company.class);
			if (company == null) {
				this.fail("Empresa inválida!");
				return;
			}
			BudgetElement budget = this.bs.budgetElementExistsBySubActionAndCompany(subAction, company);
			if(budget != null){
				this.fail("Nome de ação orcamentária já existente!");
				return;
			}
			
			String number = budgetLoa;
			String numberFormated = number.replaceAll(",",".");
			Double budgetLoaD = Double.parseDouble(numberFormated);
			
			BudgetElement budgetElement = new BudgetElement();
			budgetElement.setCompany(company);
			budgetElement.setSubAction(subAction);
			budgetElement.setBudgetLoa(budgetLoaD);
			budgetElement.setBalanceAvailable(budgetLoaD);
			
			this.bs.saveBudgetElement(budgetElement);
			this.success(budgetElement);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Listar todos elementos orçamentários de uma company
	 * 
	 * @param companyId
	 * 			Id da company
	 * 
	 * @return list, todos as simulações de orçamento
	 */
	@Get(BASEPATH + "/budget/element/list/{companyId}")
	@NoCache
	@Permissioned
	public void listBudgetAction(Long companyId) {

		try {
			
			Company company = this.bs.exists(companyId, Company.class);
			
			if (company == null) {
				this.fail("A empresa solicitada não foi encontrada.");
				return;
			}
			
			PaginatedList<BudgetElement> list = this.bs.listBudgetElement(company);
			
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Atualizar um Elemento orçamentário existente no banco de dados.
	 * 
	 * @param name,
	 *            novo nome do orçamento
	 * @param subAction,
	 *            nova sub ação orçametaria
	 * @param id,
	 *            referente ao orçamento existente
	 * 
	 * @return item, dto do orçamento atualizado
	 */
	@Post(BASEPATH + "/budget/element/update")
	@Consumes
	@NoCache
	@Permissioned
	public void update(@NotNull Long idBudgetElement,@NotEmpty String subAction,@NotNull Double budgetLoa) {
		try {
			BudgetElement budgetElement = this.bs.budgetElementExistsById(idBudgetElement);
			
			if (budgetElement == null) {
				this.fail("Orçamento inválido.");
				return;
			}
			
			if (budgetLoa == null) {
				this.fail("Orçamento Loa inválido.");
				return;
			}
			BudgetElement budgetElementNewName = this.bs.budgetElementExistsBySubActionAndCompany(subAction, budgetElement.getCompany());
			if(budgetElementNewName != null && budgetElementNewName.getId() != budgetElement.getId()){
				this.fail("Nome de ação orcamentária já existente!");
				return;
			}
			
			double diferenca =  budgetElement.getBudgetLoa() - budgetElement.getBalanceAvailable();
			Double committedTotal = 0d;
			
			if (budgetLoa  < diferenca) {
				this.fail("Orçamento loa não permitido.");
				return;
			} else {
				PaginatedList<Budget> list = this.bs.listBudgetsByBudgetElement(budgetElement);
				
				for (Budget budget: list.getList()) {
					committedTotal += budget.getCommitted();
				}
				
			}
			
			Double budgetLoaTotal = budgetLoa - committedTotal;
			
			budgetElement.setSubAction(subAction);
			budgetElement.setBudgetLoa(budgetLoa);
			budgetElement.setBalanceAvailable(budgetLoaTotal);
					
			this.bs.update(budgetElement);
			
			this.success(budgetElement);
		

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
		
	}
	
	
	/**
	 * Excluir um elemento existente no banco de dados
	 * 
	 * @param id,
	 *            referente ao orçamento a ser excluido
	 * @return budget, orçamento excluído
	 */
	@Post(BASEPATH + "/budget/element/delete")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteBdget(@NotNull Long id) {
		try {
			BudgetElement budgetElement = this.bs.budgetElementExistsById(id);
			
			if (budgetElement.getLinkedObjects() > 0) {
				this.fail("Não foi possivél deletar, elemento orçamentario possui orçamento relacionado.");
				return;
			}
			
			this.bs.deleteBudget(budgetElement);
			this.success(budgetElement);
		} catch (Throwable e) {
			LOGGER.error("Erro ao deletar o orçamento", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	

}
