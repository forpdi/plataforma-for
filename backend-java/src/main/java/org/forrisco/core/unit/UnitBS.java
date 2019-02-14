package org.forrisco.core.unit;


import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.Incident;
import org.forrisco.risk.Monitor;
import org.forrisco.risk.Risk;
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
	public PaginatedList<Unit> listUnitsbyPlanRisk(PlanRisk planrisk) {
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


	public PaginatedList<Monitor> listMonitorbyUnit(Unit unit) {
		PaginatedList<Monitor>  monitors = new PaginatedList<Monitor>();
		List<Monitor>  list = new ArrayList<Monitor>();
		Long total=(long) 0;
		
		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));
		
		
		for(Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {
			
			Criteria crit = this.dao.newCriteria(Monitor.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("risk", risk));
			
			Criteria count = this.dao.newCriteria(Monitor.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("risk", risk))
					.setProjection(Projections.countDistinct("id"));
			
			list.addAll(this.dao.findByCriteria(crit, Monitor.class));
				
			total += (Long) count.uniqueResult();

		}
		
		for(Monitor monitor: list) {
			monitor.setRiskId(monitor.getRisk().getId());
		}
		
		
		
		
		monitors.setList(list);
		monitors.setTotal(total);
		
		return monitors;
	}
	
	
	public PaginatedList<Incident> listIncidentsbyUnit(Unit unit) {
		PaginatedList<Incident>  incidents = new PaginatedList<Incident>();
		List<Incident>  list = new ArrayList<Incident>();
		Long total=(long) 0;
		
		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));
		
		for(Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {
			
			Criteria crit = this.dao.newCriteria(Incident.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("risk", risk));
			
			Criteria count = this.dao.newCriteria(Incident.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("risk", risk))
					.setProjection(Projections.countDistinct("id"));
			
			list.addAll(this.dao.findByCriteria(crit, Incident.class));
				
			total += (Long) count.uniqueResult();

		}
		
		for(Incident incident: list) {
			incident.setUnitId(unit.getId());
		}
	
		incidents.setList(list);
		incidents.setTotal(total);
		
		return incidents;
	}
	
	

}