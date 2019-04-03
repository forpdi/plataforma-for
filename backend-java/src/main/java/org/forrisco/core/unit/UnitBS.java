package org.forrisco.core.unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.core.jobs.EmailSenderTask;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.Incident;
import org.forrisco.risk.Monitor;
import org.forrisco.risk.MonitorHistory;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskBS;
import org.forrisco.risk.RiskHistory;
import org.forrisco.risk.RiskLevel;
import org.forrisco.core.process.Process;
import org.forrisco.core.process.ProcessBS;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;

import com.google.gson.GsonBuilder;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class UnitBS extends HibernateBusiness {

	
	
	@Inject
	private RiskBS riskBS;
	@Inject 
	private ProcessBS processBS;
	
	
	/**
	 * Salvar uma nova unidade
	 * 
	 * @param Unit,
	 *            instância da unidade
	 * 
	 */
	public void save(Unit unit) {
		unit.setDeleted(false);
		this.persist(unit);
	}

	public PaginatedList<Unit> listSubunitByUnit(Unit unit) {
		PaginatedList<Unit> results = new PaginatedList<Unit>();

		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("parent", unit));

		List<Unit> subunits = this.dao.findByCriteria(criteria, Unit.class);
		results.setList(subunits);
		results.setTotal((long) subunits.size());

		return results;
	}

	
	
	
	/**
	 * Deleta uma unidade
	 * 
	 * @param Unit,
	 *            instância da unidade
	 * 
	 */
	public void delete(Unit unit) {
		unit.setDeleted(true);
		this.persist(unit);
	}

	public PaginatedList<Monitor> listMonitorbyUnit(Unit unit) {
		PaginatedList<Monitor> monitors = new PaginatedList<Monitor>();
		List<Monitor> list = new ArrayList<Monitor>();
		Long total = (long) 0;

		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));

		for (Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {

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

		for (Monitor monitor : list) {
			monitor.setRiskId(monitor.getRisk().getId());
		}

		monitors.setList(list);
		monitors.setTotal(total);

		return monitors;
	}
	
	

	public PaginatedList<Incident> listIncidentsbyUnit(Unit unit) {
		PaginatedList<Incident> incidents = new PaginatedList<Incident>();
		List<Incident> list = new ArrayList<Incident>();
		Long total = (long) 0;

		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));

		for (Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {

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

		for (Incident incident : list) {
			incident.setUnitId(unit.getId());
		}

		incidents.setList(list);
		incidents.setTotal(total);

		return incidents;
	}

	

	/**
	 * Retorna uma unidade 
	 * 
	 * @param id
	 * 		Id da unidade
	 */
	public Unit retrieveUnitById(Long id) {
		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
			criteria.setMaxResults(1);
			
		return (Unit) criteria.uniqueResult();
	}

	/**
	 * Recuperar unidades e subunidades de um plano
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
	 * Recuperar unidades de um plano
	 * 
	 * @param PlanRisk,
	 *            instância da plano de risco
	 * 
	 */
	public PaginatedList<Unit> listOnlyUnitsbyPlanRisk(PlanRisk planrisk) {
		PaginatedList<Unit> results = new PaginatedList<Unit>();

		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.isNull("parent"))
				.add(Restrictions.eq("planRisk", planrisk))
				.addOrder(Order.asc("id"));

		List<Unit> units = this.dao.findByCriteria(criteria, Unit.class);
		results.setList(units);
		results.setTotal((long) units.size());

		return results;
	}
	
	
	
	/**
	 * Atualiza o historico de riscos
	 * 
	 * @param plan
	 *            Plano de Risco
	 *
	 */
	public void updateHistory(PlanRisk plan) {
		if (plan == null) {
			return;
		}

		Map<RiskLevel, Integer> map = new HashMap<RiskLevel, Integer>();
		int year = new Date().getYear() + 1900;
		int month = new Date().getMonth()+1;
		
		
		PaginatedList<Unit> units = this.listUnitsbyPlanRisk(plan);
		for (Unit unit : units.getList()) {
			map.clear();
			
			Criteria criteria = this.dao.newCriteria(Risk.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("unit", unit));

			List<Risk> risks = this.dao.findByCriteria(criteria, Risk.class);
			
			for (Risk risk : risks) {
				RiskLevel level =  risk.getRiskLevel();
				
				if(!map.containsKey(level)) {
					map.put(level,0);
				}
				map.put(level, map.get(level) + 1);
			}

			for ( RiskLevel level : map.keySet() ) {
				criteria = this.dao.newCriteria(RiskHistory.class)
						.add(Restrictions.eq("deleted", false))
						.add(Restrictions.eq("month", month))
						.add(Restrictions.eq("year", year))
						.add(Restrictions.eq("riskLevel", level))
						.add(Restrictions.eq("unit", unit));

				criteria.setMaxResults(1);
				RiskHistory riskhistory = (RiskHistory) criteria.uniqueResult();

				if (riskhistory == null) {
					riskhistory = new RiskHistory();
					riskhistory.setUnit(unit);
					riskhistory.setMonth(month);
					riskhistory.setYear(year);
					riskhistory.setRiskLevel(level);
				}

				riskhistory.setQuantity(map.get(level));
				
				this.persist(riskhistory);
			}
		}
	}

	/**
	 * Atualiza o historico de monitoramentos
	 * 
	 * @param plan
	 *            Plano de Risco com os monitoramentos
	 *
	 */
	public void updateMonitorHistory(PlanRisk plan) {
		if (plan == null) {
			return;
		}

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int year = new Date().getYear() + 1900;
		int month = new Date().getMonth();

		for (Unit unit : this.listUnitsbyPlanRisk(plan).getList()) {
			for (int i = 0; i < 3; i++) {
				map.put(i, 0);
			}
			Criteria criteria = this.dao.newCriteria(Risk.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("unit", unit));

			
			for (Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {
				Monitor monitor = this.riskBS.lastMonitorbyRisk(risk);
				int state = this.riskBS.riskState(risk.getPeriodicity(), monitor != null ? monitor.getBegin() : risk.getBegin());
				map.put(state, map.get(state) + 1);
			}

			for (int i = 0; i < map.size(); i++) {
				Integer quantity = map.get(i);
				String state = null;

				switch (i) {
					case 0:
						state = "em dia";
						break;
					case 1:
						state = "próximo a vencer";
						break;
					default:
						state = "atrasado";
				}

				//for (int j = month + 1; j <= 12; j++) {

				criteria = this.dao.newCriteria(MonitorHistory.class)
						.add(Restrictions.eq("deleted", false))
						.add(Restrictions.eq("month", month))
						.add(Restrictions.eq("year", year))
						.add(Restrictions.eq("estado", state))
						.add(Restrictions.eq("unit", unit));

				criteria.setMaxResults(1);
				MonitorHistory hmonitor = (MonitorHistory) criteria.uniqueResult();

				if (hmonitor == null) {
					hmonitor = new MonitorHistory();
				}

				hmonitor.setUnit(unit);
				hmonitor.setMonth(month);
				hmonitor.setYear(year);
				hmonitor.setQuantity(quantity);
				hmonitor.setEstado(state);

				this.persist(hmonitor);

				//}
			}
		}
	}

	public List<Unit> listUnitTerms(PlanRisk planRisk, String terms, Long[] itensSelect, int ordResult) {
		if (terms == null || terms.isEmpty()) {
			return new ArrayList<Unit>();
		}
		if(itensSelect != null && itensSelect.length == 0) {
			return new ArrayList<Unit>();
		}
		
		ArrayList<Unit> result = new ArrayList<Unit>();
		
		SnowballStemmer snowballStemmer = new portugueseStemmer();
		snowballStemmer.setCurrent(terms);
		snowballStemmer.stem();
		String stemTerms = snowballStemmer.getCurrent();
		
		Criterion name = Restrictions.like("name", "%" + stemTerms + "%").ignoreCase();
		Criterion description = Restrictions.like("description", "%" + stemTerms + "%").ignoreCase();
		Criterion abbreviation = Restrictions.like("abbreviation", "%" + stemTerms + "%").ignoreCase();
		LogicalExpression orExp = Restrictions.or(name, description);
		LogicalExpression Exp= Restrictions.or(orExp, abbreviation);
		
		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("planRisk", planRisk))
				.add(Restrictions.eq("deleted", false));
		
		if (itensSelect != null) {
			Disjunction or = Restrictions.disjunction();
			for (int i = 0; i < itensSelect.length; i++) {
				or.add(Restrictions.eq("id", itensSelect[i]));
			}
			criteria.add(or);
		}
		
		Map<Long, Unit> unit = new HashMap<Long, Unit>(); //<SubUnit, Unit>
		Map<Long, Risk> risk = new HashMap<Long, Risk>(); //<Risk>
		List<Unit> list = this.dao.findByCriteria(criteria, Unit.class);
		
		
		for(int i = 0; i < list.size(); i++) {
			Criteria crit = this.dao.newCriteria(Unit.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("id", list.get(i).getId()))
			 		.add(Exp);
			 
			List<Unit> subUnits =  this.dao.findByCriteria(crit, Unit.class);
			
			for(int j = 0; j < subUnits.size(); j++) {
				unit.put(subUnits.get(j).getId(), subUnits.get(j));
			}
		}
		
		result.addAll(unit.values());
		
		for(int i = 0; i < list.size(); i++) {
		
			Criterion code = Restrictions.like("code", "%" + terms + "%").ignoreCase();
			LogicalExpression orExp2 = Restrictions.or(name, code);
			
			
			Criteria crit= this.dao.newCriteria(Risk.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("unit", list.get(i)))
					.add(orExp2);
			
			
			List<Risk> risks = this.dao.findByCriteria(crit, Risk.class);
			
			for(int j = 0; j < risks.size(); j++) {
				risk.put(risks.get(j).getId(), risks.get(j));
			}
		}
		
		for(Risk r: risk.values()) {
			Unit u= new Unit();
			u.setName(r.getName());
			u.setDescription(r.getCode());
			u.setId(r.getUnit().getId());
			u.setRiskSearchId(r.getId());
			result.add(u);
		}

		
		return result;
	}
	
	/**
	 * Verifica se uma unidade é deletável.
	 * 
	 * @param Unit
	 * 			unidade a ser verificada.
	 */
	public boolean deletableUnit(Unit unit) {

		// verifica se possui subunidades vinculadas
		if (unit.getParent() == null) {
			PaginatedList<Unit> subunits = this.listSubunitByUnit(unit);
			EmailSenderTask.LOG.info((new GsonBuilder().setPrettyPrinting().create().toJson(subunits)));
			if (subunits.getTotal() > 0) {
				return false;
			}
		}

		// verifica se possui riscos vinculados
		PaginatedList<Risk> risks = this.riskBS.listRiskByUnit(unit);
		if (risks.getTotal() > 0) {
			return false;
		}
		
		//verifica se possui processos vinculados com algum risco de outra unidade?
		//um processo pode estar vinculado a um risco de outra unidade? aparentemente sim
		PaginatedList<Process> processes = this.processBS.listProcessByUnit(unit);
		for(Process process :processes.getList()) {
			
			if (this.riskBS.hasLinkedRiskProcess(process) && process.getUnitCreator().getId().equals(unit.getId())) {
				return false;
			}
			if (this.riskBS.hasLinkedRiskActivity(process) && process.getUnitCreator().getId().equals(unit.getId())) {
				return false;
			}
		}
		
		return true;
	}

}