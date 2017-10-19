package org.forpdi.planning.fields.budget;


import java.util.Date;

import javax.enterprise.context.RequestScoped;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@RequestScoped
public class BudgetBS extends HibernateBusiness {
	
	/**
	 * Salva um elemento orçamentário.
	 * 
	 * @param budget
	 *    		Elemento orçamentario a ser salvo.
	 * @return void.
	 */
	public void saveBudgetElement(BudgetElement budgetElement) {
		budgetElement.setDeleted(false);
		this.persist(budgetElement);
	}
	
	
	/**
	 * Retornar lista de elementos orçamentários.
	 * 
	 * @return BudgetSimulationDB Lista de ação orçamentária.
	 */
	public PaginatedList<BudgetElement> listBudgetSimulation() {
		PaginatedList<BudgetElement> list = new PaginatedList<BudgetElement>();
		Criteria criteria = this.dao.newCriteria(BudgetElement.class).add(Restrictions.eq("deleted", false));

		list.setList(this.dao.findByCriteria(criteria, BudgetElement.class));
		
		return list;

	}
	
	/**
	 * Buscar Elemento Orçamentário.
	 * 
	 * @param id
	 *            Id do elemento orçamentário.
	 * @return Elemento orçamentário.
	 */
	public BudgetElement budgetElementExistsById(Long id) {
		Criteria criteria = this.dao.newCriteria(BudgetElement.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		
		return (BudgetElement) criteria.uniqueResult();
	}
	
	/**
	 * Update no elemento orçamentário.
	 * 
	 * @param budgetElement
	 *            Elemento orçamentário para realizar o update.
	 * @return void.
	 */
	public void update(BudgetElement budgetElement) {
		budgetElement.setDeleted(false);
		this.persist(budgetElement);
	}

}
