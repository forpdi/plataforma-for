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
		
		//listItensByPlanRisk
	
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
		
		Criteria criteria = this.dao.newCriteria(PlanRiskItem.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("planRisk", planRisk)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(PlanRiskItem.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRisk", planRisk)).setProjection(Projections.countDistinct("id"));
		
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
	public PaginatedList<PlanRiskItemField> listItensByPlanRiskField(PlanRiskItem planRiskItem) {
		
		PaginatedList<PlanRiskItemField> results = new PaginatedList<PlanRiskItemField>();
		
		Criteria criteria = this.dao.newCriteria(PlanRiskItemField.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRiskItem", planRiskItem));
		
		//Criteria count = this.dao.newCriteria(PlanRiskItem.class).add(Restrictions.eq("deleted", false))
				//.add(Restrictions.eq("planRiskItem", planRiskItem)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRiskItemField.class));
		//results.setTotal((Long) count.uniqueResult());
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
		
		Criteria criteria = this.dao.newCriteria(PlanRiskSubItem.class).add(Restrictions.eqOrIsNull("deleted", false))
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
	
	public PaginatedList<PlanRiskSubItem> listSubFieldsBySubItem(PlanRiskSubItem planRiskSubItem) {
		
		PaginatedList<PlanRiskSubItem> results = new PaginatedList<PlanRiskSubItem>();
		
		Criteria criteria = this.dao.newCriteria(FieldSubItem.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("subitem", planRiskSubItem)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(FieldSubItem.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("subitem", planRiskSubItem)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRiskSubItem.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
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
	

	
	public void deleteSubitens(PlanRiskItem planRiskItem) {
		
		PaginatedList<PlanRiskSubItem> subitens = this.listSubItemByItem(planRiskItem);
		
		for(int i = 0; i < subitens.getList().size(); i++) {
			this.deleteSubitem(subitens.getList().get(i));
		}
	}
	
	
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
	
	public void deleteSubitem(PlanRiskSubItem planRiskSubItem) {
		
		PaginatedList<PlanRiskSubItem> subfields = this.listSubFieldsBySubItem(planRiskSubItem);
		
		for(int i = 0; i < subfields.getList().size(); i++) {
			this.delete(subfields.getList().get(i));
		}
		
		this.delete(planRiskSubItem);
	}

}
