package org.forrisco.core.plan;


import javax.enterprise.context.RequestScoped;

import org.forrisco.core.policy.Policy;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@RequestScoped
public class PlanRiskBS extends HibernateBusiness {
	
	/**
	 * Salva no banco de dados um novo item
	 * 
	 * @param PlanRisk,
	 *            instância do item a ser salvo
	 */
	public void save(PlanRisk planRisk) {
		planRisk.setId(null);
		planRisk.setDeleted(false);
		this.persist(planRisk);
	}
	/**
	 * Lista os planos de risco
	 * 
	 * @param page
	 * @return
	 */
	
	public PaginatedList<PlanRisk> listPlanRisk(Integer page) {
		
		PaginatedList<PlanRisk> results = new PaginatedList<PlanRisk>();
		
		Criteria criteria = this.dao.newCriteria(PlanRisk.class)
				.add(Restrictions.eq("deleted", false))
				.addOrder(Order.asc("name"));						
		
		Criteria count = this.dao.newCriteria(PlanRisk.class)
				.add(Restrictions.eq("deleted", false))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, PlanRisk.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	
	public Policy listPolicybyPlanRisk (PlanRisk plan) {
			
		Criteria criteria = this.dao.newCriteria(Policy.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("id",plan.getPolicy().getId()));		
		
		Policy result = (Policy) criteria.uniqueResult();
		return result;
	}

	public void delete(PlanRisk planRisk) {
		planRisk.setDeleted(true);
		this.persist(planRisk);
	}

}
