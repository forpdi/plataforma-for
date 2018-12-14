package org.forrisco.risk;

import javax.enterprise.context.RequestScoped;

import org.forrisco.core.policy.Policy;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class RiskBS extends HibernateBusiness {
	
	/**
	 * Salva no banco de dados um novo risco
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void save(Risk risk) {
		risk.setDeleted(false);
		this.persist(risk);
	}

	public PaginatedList<RiskLevel> listRiskLevelbyPolicy(Policy policy) {
		PaginatedList<RiskLevel> results = new PaginatedList<RiskLevel>();
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy));

		Criteria count = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskLevel.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}


}