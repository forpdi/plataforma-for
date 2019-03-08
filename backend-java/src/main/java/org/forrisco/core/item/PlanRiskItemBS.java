package org.forrisco.core.item;

import javax.enterprise.context.RequestScoped;

import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Juliano Afonso
 */

@RequestScoped
public class PlanRiskItemBS extends HibernateBusiness {
		
	// ------------LISTAGENS------------//
	
	/**
	 * Lista os itens de um plano de risco.
	 * @param PlanRisk
	 * 			plano de risco no qual se deseja obter os itens.
	 * 
	 * @return PaginatedList<PlanRiskItem>
	 * 			Lista de itens.
	 */
	public PaginatedList<PlanRiskItem> listItensByPlanRisk(PlanRisk planRisk) {
	
		PaginatedList<PlanRiskItem> results = new PaginatedList<PlanRiskItem>();
		
		Criteria criteria = this.dao.newCriteria(PlanRiskItem.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("planRisk", planRisk))
				.addOrder(Order.asc("id"));
		
		Criteria count = this.dao.newCriteria(PlanRiskItem.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRisk", planRisk))
				.setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRiskItem.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	/**
	 * Lista os campos de um item.
	 * @param PlanRiskItemField
	 * 			item no qual se deseja obter os campos.
	 * 
	 * @return PaginatedList<PlanRiskItemField>
	 * 			Lista de campos.
	 */
	public PaginatedList<PlanRiskItemField> listFieldsByPlanRiskItem(PlanRiskItem planRiskItem) {
		
		PaginatedList<PlanRiskItemField> results = new PaginatedList<PlanRiskItemField>();
		
		Criteria criteria = this.dao.newCriteria(PlanRiskItemField.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRiskItem", planRiskItem));
		
		Criteria count = this.dao.newCriteria(PlanRiskItemField.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRiskItem", planRiskItem))
				.setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRiskItemField.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
		
	}
	
	/**
	 * Lista os subitens de um item.
	 * @param PlanRiskItem
	 * 			item no qual se deseja obter os subitens.
	 * 
	 * @return PaginatedList<PlanRiskItemField>
	 * 			Lista de subitens.
	 */
	public PaginatedList<PlanRiskSubItem> listSubItemByItem(PlanRiskItem planRiskItem) {
		
		PaginatedList<PlanRiskSubItem> results = new PaginatedList<PlanRiskSubItem>();
		
		Criteria criteria = this.dao.newCriteria(PlanRiskSubItem.class)
				.add(Restrictions.eqOrIsNull("deleted", false))
				.add(Restrictions.eq("planRiskItem", planRiskItem));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRiskSubItem.class));
		return results;
	}

	
	/**
	 * Lista de campos de um subitem
	 * 
	 * @param planRiskSubItem
	 * @return
	 */
	
	public PaginatedList<PlanRiskSubItemField> listSubFieldsBySubItem(PlanRiskSubItem planRiskSubItem) {
		
		PaginatedList<PlanRiskSubItemField> results = new PaginatedList<PlanRiskSubItemField>();
		
		Criteria criteria = this.dao.newCriteria(PlanRiskSubItemField.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("planRiskSubItem", planRiskSubItem))
				.addOrder(Order.asc("id"));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRiskSubItemField.class));
		return results;
	}
	
	// ------------SAVES------------//
	
	/**
	 * Salva no banco de dados um novo item
	 * 
	 * @param plaRiskItem instância do item a ser salvo
	 */
	public void save(PlanRiskItem plaRiskItem) {
		plaRiskItem.setId(null);
		plaRiskItem.setDeleted(false);
		
		this.persist(plaRiskItem);
	}
	
	/**
	 * Salva no banco de dados um novo campo
	 * 
	 * @param planRiskItemField, instância do PlanRiskItemField a ser salvo
	 */
	public void save(PlanRiskItemField planRiskItemField) {
		planRiskItemField.setId(null);
		planRiskItemField.setDeleted(false);
		this.persist(planRiskItemField);
	}
	
	/**
	 * Salva no banco de dados um novo subitem
	 * 
	 * @param plaRiskItem instância do subitem a ser salvo
	 */
	public void save(PlanRiskSubItem plaRiskSubItem) {
		plaRiskSubItem.setId(null);
		plaRiskSubItem.setDeleted(false);
		
		this.persist(plaRiskSubItem);
	}
	
	/**
	 * Salva no banco de dados um campo de um  subitem
	 * 
	 * @param plaRiskItem instância do campo a ser salvo
	 */
	public void save(PlanRiskSubItemField plaRiskSubItemField) {
		plaRiskSubItemField.setId(null);
		plaRiskSubItemField.setDeleted(false);
		
		this.persist(plaRiskSubItemField);
	}
	
	// ------------DELETES------------//
	/**
	 * Deleta do banco de dados um item
	 * 
	 * @param PlanRiskItem,
	 *            instância do planRiskItem a ser deletado
	 */
	public void delete(PlanRiskItem planRiskItem) {
		planRiskItem.setDeleted(true);
		this.persist(planRiskItem);
	}
	
	/**
	 * Deleta do banco de dados um campo
	 * 
	 * @param PlanRiskItemField,
	 *            instância do PlanRiskItemField a ser deletado
	 */
	public void delete(PlanRiskItemField planRiskItemField) {
		planRiskItemField.setDeleted(true);
		this.persist(planRiskItemField);
	}
	
	/**
	 * Deleta do banco de dados um subitem
	 * 
	 * @param PlanRiskSubItem,
	 *            instância do PlanRiskSubItem a ser deletado
	 */
	
	public void delete(PlanRiskSubItem planRiskSubItem) {
		planRiskSubItem.setDeleted(true);
		this.persist(planRiskSubItem);
	}
	
	/**
	 * Deleta uma serie de subitens
	 * 
	 * @param PlanRiskSubItem,
	 *            instância do PlanRiskSubItem a ser deletado
	 */
	public void deleteSubItens(PlanRiskItem planRiskItem) {
		
		PaginatedList<PlanRiskSubItem> subitens = this.listSubItemByItem(planRiskItem);
		
		for(int j = 0; j < subitens.getList().size(); j ++) {
			this.delete(subitens.getList().get(j));
		}
	}
	
	/**
	 * Deleta do banco de dados um subcampo
	 * 
	 * @param PlanRiskSubItemField,
	 *            instância do PlanRiskSubItemField a ser deletado
	 */
	
	public void delete(PlanRiskSubItemField planRiskSubItemField) {
		planRiskSubItemField.setDeleted(true);
		this.persist(planRiskSubItemField);
	}

	
	/*public void deleteSubitens(PlanRiskItem planRiskItem) {
		
		PaginatedList<PlanRiskSubItem> subitens = this.listSubItemByItem(planRiskItem);
		
		for(int i = 0; i < subitens.getList().size(); i++) {
			this.deleteSubitem(subitens.getList().get(i));
		}
	}*/
	
	public PlanRiskItem retrievePlanRiskItembyId(long id) {
		
		Criteria criteria = this.dao.newCriteria(PlanRiskItem.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("id", id));
		
	return	(PlanRiskItem) criteria.uniqueResult();
}
}
