package org.forrisco.core.policy;


import javax.enterprise.context.RequestScoped;

import org.forpdi.core.company.Company;
import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.Item;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.risk.RiskLevel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class PolicyBS extends HibernateBusiness {
	
	
	private static final int PAGESIZE = 50;
	
	
	
	/**
	 * Salva no banco de dados uma nova política
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void save(Policy policy) {
		policy.setDeleted(false);
		this.persist(policy);
	}
	


	/**
	 * Salva no banco de dados um item
	 * 
	 * @param item,
	 *            instância da política a ser salva
	 */
	public void save(Item item) {
		this.persist(item);
	}
	

	/**
	 * Salva no banco de dados um campo de item
	 * 
	 * @param Fielditem,
	 *            instância da política a ser salva
	 */
	public void save(FieldItem it) {
		this.persist(it);
	}
	
	/**
	 * Deleta do banco de dados uma política
	 * 
	 * @param Policy,
	 *            política a ser deletada
	 */
	public void delete(Policy policy) {
		policy.setDeleted(true);
		this.persist(policy);
	}
	
	/**
	 * Lista as políticas de uma companhia.
	 * @param company
	 * 			Companhia da qual se deseja obter aspolíticas.
	 * @param archived
	 * 			Filtro para arquivado ou não (true ou false). 
	 * @param page
	 * 			Número da página da lista a ser acessada. 
	 * @return PaginatedList<Policy>
	 * 			Lista de políticas.
	 */
	public PaginatedList<Policy> listPolicies(Company company, boolean archived, Integer page) {
		PaginatedList<Policy> results = new PaginatedList<Policy>();
		Criteria criteria = this.dao.newCriteria(Policy.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("archived", archived)).add(Restrictions.eq("company", company))
				.addOrder(Order.asc("name"));
		Criteria count = this.dao.newCriteria(Policy.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("archived", archived)).add(Restrictions.eq("company", company))
				.setProjection(Projections.countDistinct("id"));
		
		if(page != null){
			criteria.setMaxResults(PAGESIZE).setFirstResult(page * PAGESIZE);
		}
		
		results.setList(this.dao.findByCriteria(criteria, Policy.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Lista os planos com esta política.
	 * 
	 * @param Policy
	 * 			política da qual se deseja obter os planos.
	 * 
	 * @return PaginatedList<PlanRisk>
	 * 			Lista os planos de risco.
	 */
	public  PaginatedList<PlanRisk> listPlanbyPolicy(Policy policy) {
		PaginatedList<PlanRisk> results = new PaginatedList<PlanRisk>();
		
		Criteria criteria = this.dao.newCriteria(PlanRisk.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("policy", policy))
				.addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(PlanRisk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));
		
		
		results.setList(this.dao.findByCriteria(criteria, PlanRisk.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Lista os planos com esta política.
	 * 
	 * @param Policy
	 * 			política da qual se deseja obter os planos.
	 * 
	 * @return PaginatedList<RiskLevel>
	 * 			Lista os graus de risco desta política.
	 */
	public PaginatedList<RiskLevel> listRiskLevelbyPolicy(Policy policy) {

		PaginatedList<RiskLevel> results = new PaginatedList<RiskLevel>();
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("policy", policy))
				.addOrder(Order.asc("id"));
		
		Criteria count = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));
		
		
		results.setList(this.dao.findByCriteria(criteria, RiskLevel.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
}