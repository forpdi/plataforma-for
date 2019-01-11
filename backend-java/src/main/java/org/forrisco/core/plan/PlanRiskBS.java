package org.forrisco.core.plan;

import javax.enterprise.context.RequestScoped;

import org.forrisco.core.item.Item;
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
	
	public PaginatedList<PlanRisk> listPlanRisk(Integer page) {
		
		PaginatedList<PlanRisk> results = new PaginatedList<PlanRisk>();
		
		Criteria criteria = this.dao.newCriteria(PlanRisk.class).add(Restrictions.eq("deleted", false)).addOrder(Order.asc("name"));						
				//.add(Restrictions.eq("policy", policy)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(PlanRisk.class).add(Restrictions.eq("deleted", false)).setProjection(Projections.countDistinct("id"));
				//.add(Restrictions.eq("policy", policy)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, PlanRisk.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

}
