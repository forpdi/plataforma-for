package org.forrisco.core.unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.system.CriteriaCompanyFilter;
import org.forrisco.core.item.Item;
import org.forrisco.core.item.SubItem;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.Incident;
import org.forrisco.risk.Monitor;
import org.forrisco.risk.MonitorHistory;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskHistory;
import org.forrisco.risk.RiskLevel;
import org.forrisco.core.process.Process;
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
public class UnitBS extends HibernateBusiness {

	
	
	@Inject
	private CriteriaCompanyFilter filter;
	
	
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

	public PaginatedList<Unit> listSubunitbyUnit(Unit unit) {

		PaginatedList<Unit> results = new PaginatedList<Unit>();

		Criteria criteria = this.dao.newCriteria(Unit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("parent", unit));

//		Criteria count = this.dao.newCriteria(Unit.class)
//				.add(Restrictions.eq("deleted", false))
//				.add(Restrictions.eq("parent", unit))
//				.setProjection(Projections.countDistinct("id"));

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
				.add(Restrictions.eq("planRisk", planrisk));

		List<Unit> units = this.dao.findByCriteria(criteria, Unit.class);
		results.setList(units);
		results.setTotal((long) units.size());

		return results;
	}
	
	
	

	/**
	 * Retorna o estado de cada risco a partir da data 
	 * 
	 * @param String
	 *            periodicidade
	 * 
	 * @param Data
	 *            Data para comparação da peridicidade
	 * 
	 * @return int estado atual do monitoramento
	 */
	public int riskState(String periodicity, Date date) {
		int state = 0; // não iniciado

		if (date == null) {
			return state;
		}

		Date now = new Date();

		double diffInSec = (now.getTime() - date.getTime()) / 1000;
		double diffDays = diffInSec / (60 * 60 * 24);

		switch (periodicity) {
		case "diária":
			if (diffDays < 0.85) {
				state = 1;
			} // em dia
			else if (diffDays < 1) {
				state = 2;
			} // próximos a vencer
			else {
				state = 3;
			} // atrasado
			break;

		case "semanal":
			if (diffDays < 6) {
				state = 1;
			} else if (diffDays < 7) {
				state = 2;
			} else {
				state = 3;
			}
			break;

		case "quinzenal":
			if (diffDays < 12) {
				state = 1;
			} else if (diffDays < 15) {
				state = 2;
			} else {
				state = 3;
			}
			break;

		case "mensal":
			if (diffDays < 24) {
				state = 1;
			} else if (diffDays < 30) {
				state = 2;
			} else {
				state = 3;
			}
			break;

		case "bimestral":
			if (diffDays < 48) {
				state = 1;
			} else if (diffDays < 60) {
				state = 2;
			} else {
				state = 3;
			}
			break;

		case "trimestral":
			if (diffDays < 72) {
				state = 1;
			} else if (diffDays < 90) {
				state = 2;
			} else {
				state = 3;
			}
			break;

		case "semestral":
			if (diffDays < 144) {
				state = 1;
			} else if (diffDays < 180) {
				state = 2;
			} else {
				state = 3;
			}
			break;

		case "anual":
			if (diffDays < 288) {
				state = 1;
			} else if (diffDays < 360) {
				state = 2;
			} else {
				state = 3;
			}
			break;
		}

		return state;
	}

	/**
	 * Retorna o ultimo monitoramento de um risco
	 * 
	 * @param Risk
	 *            instância de um risco
	 *
	 * @return Monitor instância de um monitoramento
	 */
	public Monitor lastMonitorbyRisk(Risk risk) {

		Criteria criteria = this.dao.newCriteria(Monitor.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.addOrder(Order.desc("begin"));

		criteria.setMaxResults(1);
		Monitor monitor = (Monitor) criteria.uniqueResult();

		return monitor;
	}

	
	public PaginatedList<Process> listProcess() {
		
		PaginatedList<Process> results = new PaginatedList<Process>();

		Criteria criteria = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false));

		List<Process> list = this.filter.filterAndList(criteria, Process.class,"company");
		
		results.setList(list);
		results.setTotal( (long) list.size());

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
		int month = new Date().getMonth();

		for (Unit unit : this.listUnitsbyPlanRisk(plan).getList()) {
			map.clear();
			
			Criteria criteria = this.dao.newCriteria(Risk.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("unit", unit));

			for (Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {
				//String level =  risk.getRiskLevel().getLevel();
				RiskLevel level =  risk.getRiskLevel();
				
				if(!map.containsKey(level)) {
					map.put(level,0);
				}
				map.put(level, map.get(level) + 1);
			}
				
			for ( RiskLevel level : map.keySet() ) {
			
				//for (int j = month + 1; j <= 12; j++) {
	
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
					}
	
					riskhistory.setUnit(unit);
					riskhistory.setMonth(month);
					riskhistory.setYear(year);
					riskhistory.setRiskLevel(level);
					riskhistory.setQuantity(map.get(level));
					
					this.persist(riskhistory);
	
				//}
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
			for (int i = 0; i < 4; i++) {
				map.put(i, 0);
			}
			Criteria criteria = this.dao.newCriteria(Risk.class)
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("unit", unit));

			
			for (Risk risk : this.dao.findByCriteria(criteria, Risk.class)) {
				Monitor monitor =this.lastMonitorbyRisk(risk);
				int state = this.riskState(risk.getPeriodicity(), monitor != null ? monitor.getBegin():null);
				map.put(state, map.get(state) + 1);
			}

			for (int i = 0; i < map.size(); i++) {
				Integer quantity = map.get(i);
				String state = null;

				switch (i) {
				case 0:
					state = "não iniciado";
					break;
				case 1:
					state = "em dia";
					break;
				case 2:
					state = "próximo a vencer";
					break;
				case 3:
					state = "atrasado";
					break;
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

	public List<Item> listItemTerms(PlanRisk plan, String terms, Long[] itensSelect, int ordResult) {
		 //TODO Auto-generated method stub
		return null;
	}

	public List<SubItem> listSubitemTerms(PlanRisk plan, String terms, Long[] subitensSelect, int ordResult) {
		 //TODO Auto-generated method stub
		return null;
	}
}