package org.forrisco.core.unit;


import javax.enterprise.context.RequestScoped;

import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.process.ProcessUnit;
import org.forrisco.core.unit.Unit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class UnitBS extends HibernateBusiness {
	
		
	/**
	 * Recuperar unidade de um plano
	 * 
	 * @param PlanRisk,
	 *            instância da plano de risco
	 *            
	 */
	public PaginatedList<Unit> listUnitbyPlan(PlanRisk planrisk) {
		PaginatedList<Unit> results = new PaginatedList<Unit>();
		
		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRisk", planrisk));

		Criteria count = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("planRisk", planrisk))
				.setProjection(Projections.countDistinct("id"));
		
		
		results.setList(this.dao.findByCriteria(criteria, Unit.class));
		results.setTotal((Long) count.uniqueResult());
	
		return results;
	}
	
	
	/**
	 * Salvar uma nova unidade
	 * 
	 * @param Unit,
	 *			instância da unidade
	 *            
	 */
	public void save(Unit unit){
		unit.setDeleted(false);
		this.persist(unit);	
	}


	public PaginatedList<Unit>  listSubunitbyUnit(Unit unit) {
		
		PaginatedList<Unit> results = new PaginatedList<Unit>();
		
		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("parent", unit));

		Criteria count = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("parent", unit))
				.setProjection(Projections.countDistinct("id"));
		
		
		results.setList(this.dao.findByCriteria(criteria, Unit.class));
		results.setTotal((Long) count.uniqueResult());
		
		return results;
	}

	/**
	 * Deleta uma unidade
	 * 
	 * @param Unit,
	 *			instância da unidade
	 *            
	 */
	public void delete(Unit unit) {
		unit.setDeleted(true);
		this.persist(unit);	
	}
	
	
	

}