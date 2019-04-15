package org.forrisco.risk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;


import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.apache.commons.mail.EmailException;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.objective.RiskActivity;
import org.forrisco.risk.objective.RiskProcess;
import org.forrisco.risk.objective.RiskStrategy;
import org.forrisco.core.process.Process;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class RiskBS extends HibernateBusiness {

	@Inject
	protected HibernateDAO dao;
	@Inject
	protected HttpServletRequest request;
	@Inject
	protected NotificationBS notificationBS;

	private static final int PAGESIZE = 10;

	/**
	 * Salva no banco de dados um novo risco
	 * 
	 * @param Risk, instância de um risco a ser salvo
	 */
	public void saveRisk(Risk risk) {
		risk.setDeleted(false);
		this.persist(risk);
	}

	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param policy, instância da política a ser salva
	 */
	public void saveRiskLevel(Policy policy) {

		String[][] str = policy.getRisk_level();

		for (int i = 0; i < str[0].length; i++) {
			if (str[1][i] != null) {
				RiskLevel rk = new RiskLevel();
				rk.setId(null);
				rk.setColor(Integer.parseInt(str[1][i]));
				rk.setLevel(str[0][i]);
				rk.setPolicy(policy);
				this.persist(rk);
			}
		}
	}

	/**
	 * Salva no banco de dados uma nova ação preventiva
	 * 
	 * @param PreventiveAction, instância de uma ação preventiva a ser salva
	 */
	public void saveAction(PreventiveAction action) {
		action.setDeleted(false);
//		action.setAccomplished(false);
		this.persist(action);
	}

	/**
	 * Salva no banco de dados um novo monitoramento
	 * 
	 * @param Monitor, instância de um monitoramento a ser salva
	 */
	public void saveMonitor(Monitor monitor) {
		monitor.setDeleted(false);
		this.persist(monitor);
	}

	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param RiskLevel, instância de um grau de risco a ser salvo
	 */
	public void saveRiskLevel(RiskLevel risklevel) {
		risklevel.setDeleted(false);
		this.persist(risklevel);
	}

	/**
	 * Salva no banco de dados incidente
	 * 
	 * @param Incident, instância de um incident a ser salvo
	 */
	public void saveIncident(Incident incident) {
		incident.setDeleted(false);
		this.persist(incident);
	}

	/**
	 * Salva no banco de dados contigenciamento
	 * 
	 * @param Contigency, instância de um contigenciamento a ser salvo
	 */
	public void saveContingency(Contingency contingency) {
		contingency.setDeleted(false);
		this.persist(contingency);
	}

	/**
	 * Salvar uma lista de atividades
	 * 
	 * @param PaginatedList<RiskActivity> instância de uma lista de atividades de
	 *        risco
	 */
	public void saveActivities(Risk risk) {

		if (risk.getActivities().getList() == null) {
			return;
		}

		for (RiskActivity activity : risk.getActivities().getList()) {
			activity.setId(null);
			activity.setRisk(risk);
			activity.setDeleted(false);

			Process process = this.dao.exists(activity.getProcess().getId(), Process.class);

			activity.setProcess(process);

			if (activity.getName() == null) {
				activity.setName("");
			}

			// pegar link correto da unidade que contem o processo
			if(process !=null && !process.isDeleted() && process.getUnitCreator() != null) {
				activity.setLinkFPDI("#/forrisco/plan-risk/" + process.getUnitCreator().getPlanRisk().getId() + "/unit/"
						+ process.getUnitCreator().getId() + "/info");
			}
			this.dao.persist(activity);
		}
	}

	/**
	 * Salvar uma lista de objetivos de processos
	 * 
	 * @param PaginatedList<RiskProcess> instância de uma lista de processos de
	 *        unidade/instituição
	 */
	public void saveProcesses(Risk risk) {

		if (risk.getProcess().getList() == null) {
			return;
		}

		for (RiskProcess riskprocess : risk.getProcess().getList()) {
			riskprocess.setId(null);
			riskprocess.setRisk(risk);
			riskprocess.setDeleted(false);

			Process process = this.dao.exists(riskprocess.getProcess().getId(), Process.class);

			riskprocess.setProcess(process);

			// pegar link correto da unidade que contem o processo
			riskprocess.setLinkFPDI("#/forrisco/plan-risk/" + process.getUnitCreator().getPlanRisk().getId() + "/unit/"
					+ process.getUnitCreator().getId() + "/info");
			this.dao.persist(riskprocess);
		}
	}

	/**
	 * Salvar uma lista de objetivos estratégicos
	 * 
	 * @param PaginatedList<RiskStrategy> instância de uma lista de objetivos
	 *        estratégicos
	 * @throws Exception
	 */
	public void saveStrategies(Risk risk) throws Exception {

		if (risk.getStrategies().getList() == null) {
			return;
		}

		for (RiskStrategy strategy : risk.getStrategies().getList()) {
			strategy.setId(null);
			strategy.setRisk(risk);
			strategy.setDeleted(false);

			StructureLevelInstance structure = this.dao.exists(strategy.getStructure().getId(),
					StructureLevelInstance.class);
			if (structure == null) {
				throw new Exception("Objetivo do PDI não foi encontrado");
			}

			Plan plan = this.dao.exists(structure.getPlan().getId(), Plan.class);

			if (plan == null) {
				throw new Exception("Plano do PDI não foi encontrado");
			}

			// pegar link correto do Objetivo na Plataforma ForPDI.
			strategy.setLinkFPDI(
					"#/plan/" + plan.getParent().getId() + "/details/subplan/level/" + strategy.getStructure().getId());
			strategy.setName(structure.getName());
			strategy.setStructure(structure);
			this.dao.persist(strategy);
		}
	}

	/**
	 * Deleta do banco de dados graus de risco
	 * 
	 * @param Item, instância do item a ser deletado
	 */
	public void delete(RiskLevel risklevel) {
		risklevel.setDeleted(true);
		this.persist(risklevel);
	}

	/**
	 * Deleta do banco de dados uma ação de prevenção
	 * 
	 * @param PreventiveAction, instância da a ação ser deletado
	 */
	public void delete(PreventiveAction action) {
		action.setDeleted(true);
		this.persist(action);
	}

	/**
	 * Deleta do banco de dados um monitoramento
	 * 
	 * @param Monitor, instância do monitoramento ser deletado
	 */
	public void delete(Monitor monitor) {
		monitor.setDeleted(true);
		this.persist(monitor);
	}

	/**
	 * Deleta do banco de dados um incidente
	 * 
	 * @param Incident, instância do incidente a ser deletado
	 */
	public void delete(Incident incident) {
		incident.setDeleted(true);
		this.persist(incident);
	}

	/**
	 * Deleta do banco de dados um incidente
	 * 
	 * @param Incident, instância do incidente a ser deletado
	 */
	public void delete(Contingency contingency) {
		contingency.setDeleted(true);
		this.persist(contingency);
	}

	/**
	 * Deleta do banco de dados uma atividade do processo
	 * 
	 * @param RiskActivity, instância da atividade do processo
	 */
	private void delete(RiskActivity act) {
		act.setDeleted(true);
		this.persist(act);
	}

	/**
	 * Deleta do banco de dados um processo do objetivo
	 * 
	 * @param RiskProcess, instância do processo
	 */
	private void delete(RiskProcess pro) {
		pro.setDeleted(true);
		this.persist(pro);
	}

	/**
	 * Deleta do banco de dados um objetivo estratégico
	 * 
	 * @param RiskStrategy, instância do objetivo
	 */
	private void delete(RiskStrategy str) {
		str.setDeleted(true);
		this.persist(str);
	}

	/**
	 * Deleta do banco de dados as atividades, os processos e os objetivos
	 * estratégicos de um risco
	 * 
	 * @param Risk, instância do risco
	 */
	public void deleteAPS(Risk oldrisk) {

		Risk risk = this.dao.exists(oldrisk.getId(), Risk.class);
		if (risk == null) {
			return;
		}

		PaginatedList<RiskActivity> activity = this.listRiskActivity(risk);
		PaginatedList<RiskProcess> process = this.listRiskProcess(risk);
		PaginatedList<RiskStrategy> strategy = this.listRiskStrategy(risk);

		for (RiskActivity act : activity.getList()) {
			this.delete(act);
		}

		for (RiskProcess pro : process.getList()) {
			this.delete(pro);
		}

		for (RiskStrategy str : strategy.getList()) {
			this.delete(str);
		}
	}

	/**
	 * Deleta do banco de dados um risco,
	 * 
	 * @param Risk, instância do risco
	 */
	public void delete(Risk risk) {
		risk.setDeleted(true);
		this.persist(risk);
	}

	/**
	 * Retorna um risco
	 * 
	 * @param id Id do risco
	 */
	public Risk listRiskById(Long id) {
		Criteria criteria = this.dao.newCriteria(Risk.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		criteria.setMaxResults(1);

		return (Risk) criteria.uniqueResult();
	}

	/**
	 * Retorna uma lista de grau de risco a partir da política
	 * 
	 * política não salva no banco (acho que da para usar a outra função)
	 * 
	 * @param policy
	 * 
	 */
	public PaginatedList<RiskLevel> listRiskLevel(Policy policy) {
		List<RiskLevel> array = new ArrayList<RiskLevel>();
		PaginatedList<RiskLevel> list = new PaginatedList<RiskLevel>();
		String[][] str = policy.getRisk_level();

		for (int i = 0; i < str[0].length; i++) {
			if (str[1][i] != null) {
				RiskLevel rk = new RiskLevel();
				rk.setId(null);
				rk.setColor(Integer.parseInt(str[1][i]));
				rk.setLevel(str[0][i]);
				rk.setPolicy(policy);
				array.add(rk);
			}
		}

		list.setList(array);
		list.setTotal(Long.valueOf(array.size()));
		return list;
	}

	/**
	 * Retorna os graus de risco a partir da política
	 * 
	 * @param policy instância da política
	 * @return
	 */
	public PaginatedList<RiskLevel> listRiskLevelByPolicy(Policy policy) {
		PaginatedList<RiskLevel> results = new PaginatedList<RiskLevel>();

		Criteria criteria = this.dao.newCriteria(RiskLevel.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy));

		Criteria count = this.dao.newCriteria(RiskLevel.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy)).setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskLevel.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Retorna riscos de uma unidade
	 * 
	 * @param Unit, instância da unidade
	 * 
	 */
	public PaginatedList<Risk> listRiskByUnit(Unit unit) {
		PaginatedList<Risk> results = new PaginatedList<Risk>();

		Criteria criteria = this.dao.newCriteria(Risk.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));

//		Criteria count = this.dao.newCriteria(Risk.class)
//				.add(Restrictions.eq("deleted", false))
//				.add(Restrictions.eq("unit", unit))
//				.setProjection(Projections.countDistinct("id"));

		List<Risk> risks = this.dao.findByCriteria(criteria, Risk.class);
		results.setList(risks);
		results.setTotal((long) risks.size());

		return results;
	}

	/**
	 * Retorna riscos de uma lista de unidades
	 * 
	 * @param List<Unit>, lista da unidades
	 * 
	 */
	public PaginatedList<Risk> listRiskByUnitList(List<Unit> units) {
		PaginatedList<Risk> results = new PaginatedList<Risk>();

		Criteria criteria = this.dao.newCriteria(Risk.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.in("unit", units));

		List<Risk> risks = this.dao.findByCriteria(criteria, Risk.class);
		results.setList(risks);
		results.setTotal((long) risks.size());

		return results;
	}


	/**
	 * Retorna os riscos a partir de um impácto e uma probabilidade
	 * 
	 * @param impact
	 * @param probability
	 * @param page
	 * @param pageSize
	 * @return Riscos paginados
	 */
	
	public PaginatedList<Risk> listRiskByPI(PlanRisk planRisk, String impact, String probability, Integer page, Integer pageSize) {


		if (page == null || page < 1) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}

		PaginatedList<Risk> results = new PaginatedList<Risk>();

		Criteria criteria = this.dao.newCriteria(Risk.class).setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize).addOrder(Order.asc("name"))
				.createAlias("unit","unit")
				.add(Restrictions.eq("unit.planRisk",planRisk))
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("impact", impact))
				.add(Restrictions.eq("probability", probability));
		
		Criteria counting = this.dao.newCriteria(Risk.class)
				.createAlias("unit","unit")
				.add(Restrictions.eq("unit.planRisk",planRisk))
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("impact", impact))
				.add(Restrictions.eq("probability", probability))
				.setProjection(Projections.countDistinct("id"));

		List<Risk> risks = this.dao.findByCriteria(criteria, Risk.class);
		results.setList(risks);
		results.setTotal((Long) counting.uniqueResult());

		return results;
	}

	public PaginatedList<Incident> pagitaneIncidents(List<Long> incidentsId, Integer page, Integer pageSize) {
		if (page == null || page < 1) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = 5;
		}

		PaginatedList<Incident> results = new PaginatedList<Incident>();
		
		Criteria criteria = this.dao.newCriteria(Incident.class);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize).addOrder(Order.asc("description"));
		criteria.add(Restrictions.in("id", incidentsId));
		
		
		Criteria counting = this.dao.newCriteria(Incident.class);
		counting.add(Restrictions.in("id", incidentsId));
		counting.setProjection(Projections.countDistinct("id"));

		List<Incident> findCriteria = this.dao.findByCriteria(criteria, Incident.class);
		
		results.setList(findCriteria);
		results.setTotal((Long) counting.uniqueResult());
				  
		return results;
	}

	// recuperar o risklevel do risco
	// risco -> unidade -> plano -> politica -> risco level
	/**
	 * Retorna o grau de risco a partir da probabilidade e impacto
	 * 
	 * @param ProcessUnit, instância processunit
	 * 
	 * @return List<Risk>
	 * @throws Exception
	 */
	public RiskLevel getRiskLevelByRisk(Risk risk, Policy policy) throws Exception {

		if (policy == null) {
			Unit unit = this.exists(risk.getUnit().getId(), Unit.class);

			if (unit == null) {
				return null;
			}

			PlanRisk planRisk = this.exists(unit.getPlanRisk().getId(), PlanRisk.class);

			if (planRisk == null) {
				return null;
			}

			policy = this.exists(planRisk.getPolicy().getId(), Policy.class);

			if (policy == null) {
				return null;
			}
		}

		int total = policy.getNline() * policy.getNcolumn() + policy.getNline() + policy.getNcolumn();
		String[][] matrix = getMatrixVector(policy);

		int linha = 0;
		int coluna = 0;
		for (int i = 0; i < total; i++) {
			if (i % (policy.getNline() + 1) == 0) {

				if (risk.getProbability().equals(matrix[i][0])) {
					i = total;
				}
				linha += 1;
			}
		}

		for (int i = total - policy.getNcolumn(); i < total; i++) {
			if (risk.getImpact().equals(matrix[i][0])) {
				i = total;
			}
			coluna += 1;
		}

		int position = ((linha - 1) * (policy.getNcolumn() + 1)) + coluna;

		if (position >= total) {
			throw new Exception("falha ao carregar grau de risco");
		}

		String result = matrix[position][0];

		Criteria criteria = this.dao.newCriteria(RiskLevel.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy)).add(Restrictions.eq("level", result));

		return (RiskLevel) criteria.uniqueResult();

	}

	/**
	 * Retorna as ativiades do processo de um risco
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<RiskActivity>
	 */
	public PaginatedList<RiskActivity> listActivityByRisk(Risk risk) {

		PaginatedList<RiskActivity> results = new PaginatedList<RiskActivity>();

		Criteria criteria = this.dao.newCriteria(RiskActivity.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskActivity.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskActivity.class));
		results.setTotal((Long) count.uniqueResult());
		return results;

	}

	/**
	 * Retorna as ações preventivas a partir de um risco
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<PreventiveAction>
	 */
	public PaginatedList<PreventiveAction> listActionByRisk(Risk risk) {

		PaginatedList<PreventiveAction> results = new PaginatedList<PreventiveAction>();

		Criteria criteria = this.dao.newCriteria(PreventiveAction.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		List<PreventiveAction> list = this.dao.findByCriteria(criteria, PreventiveAction.class);
		results.setList(list);
		results.setTotal((long) list.size());
		return results;
	}

	/**
	 * Retorna as ações preventivas a partir de um risco com paginacao
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<PreventiveAction>
	 */
	public PaginatedList<PreventiveAction> listActionByRisk(Risk risk, Integer page, Integer pageSize) {

		PaginatedList<PreventiveAction> results = new PaginatedList<PreventiveAction>();

		Criteria criteria = this.dao.newCriteria(PreventiveAction.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize);

		Criteria count = this.dao.newCriteria(PreventiveAction.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, PreventiveAction.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Retorna os monitoramentos a partir de um risco
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<Monitor>
	 */
	public PaginatedList<Monitor> listMonitorByRisk(Risk risk) {

		PaginatedList<Monitor> results = new PaginatedList<Monitor>();

		Criteria criteria = this.dao.newCriteria(Monitor.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		List<Monitor> list = this.dao.findByCriteria(criteria, Monitor.class);
		results.setList(list);
		results.setTotal((long) list.size());
		return results;
	}

	/**
	 * Retorna os monitoramentos a partir de um risco com paginacao
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<Monitor>
	 */
	public PaginatedList<Monitor> listMonitorByRisk(Risk risk, Integer page, Integer pageSize) {

		PaginatedList<Monitor> results = new PaginatedList<Monitor>();

		Criteria criteria = this.dao.newCriteria(Monitor.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize);

		Criteria count = this.dao.newCriteria(Monitor.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, Monitor.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	/**
	 * Retorna os incidentes a partir de um risco
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<Incident>
	 */
	public PaginatedList<Incident> listIncidentsByRisk(Risk risk) {

		PaginatedList<Incident> results = new PaginatedList<Incident>();

		Criteria criteria = this.dao.newCriteria(Incident.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		List<Incident> list = this.dao.findByCriteria(criteria, Incident.class);
		results.setList(list);
		results.setTotal((long) list.size());

		for (Incident incident : results.getList()) {

			incident.setUnitId(incident.getRisk().getUnit().getId());
		}

		return results;
	}

	/**
	 * Retorna os incidentes a partir de um risco com paginacao
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<Incident>
	 */
	public PaginatedList<Incident> listIncidentsByRisk(Risk risk, Integer page, Integer pageSize) {

		PaginatedList<Incident> results = new PaginatedList<Incident>();

		Criteria criteria = this.dao.newCriteria(Incident.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize);

		Criteria count = this.dao.newCriteria(Incident.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, Incident.class));
		results.setTotal((Long) count.uniqueResult());

		for (Incident incident : results.getList()) {

			incident.setUnitId(incident.getRisk().getUnit().getId());
		}

		return results;
	}

	/**
	 * Retorna os incidentes a partir de vários riscos
	 * 
	 * @param PaginatedList<Risk> instância de um risco
	 * 
	 * @return PaginatedList<Incident>
	 */
	public PaginatedList<Incident> listIncidentsByRisk(PaginatedList<Risk> risks) {

		List<Incident> inc = new ArrayList<>();

		for (Risk risk : risks.getList()) {
			PaginatedList<Incident> incident = listIncidentsByRisk(risk);
			inc.addAll(incident.getList());
		}

		PaginatedList<Incident> incident = new PaginatedList<Incident>();

		incident.setList(inc);
		incident.setTotal((long) inc.size());

		return incident;
	}

	/**
	 * Retorna os contingenciamentos a partir de um risco
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<Contingency>
	 */
	public PaginatedList<Contingency> listContingenciesByRisk(Risk risk) {

		PaginatedList<Contingency> results = new PaginatedList<Contingency>();

		Criteria criteria = this.dao.newCriteria(Contingency.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		List<Contingency> list = this.dao.findByCriteria(criteria, Contingency.class);
		results.setList(list);
		results.setTotal((long) list.size());
		return results;
	}

	/**
	 * Retorna os contingenciamentos a partir de um risco com paginacao
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<Contingency>
	 */
	public PaginatedList<Contingency> listContingenciesByRisk(Risk risk, Integer page, Integer pageSize) {

		PaginatedList<Contingency> results = new PaginatedList<Contingency>();

		Criteria criteria = this.dao.newCriteria(Contingency.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize);

		Criteria count = this.dao.newCriteria(Contingency.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, Contingency.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Retorna os objetivos estrategicos vinculados ao risco
	 * 
	 * @param risk instância do Risk
	 * @return PaginatedList<RiskStrategy> lista de objetivos estrategicos
	 */
	public PaginatedList<RiskStrategy> listRiskStrategy(Risk risk) {
		PaginatedList<RiskStrategy> results = new PaginatedList<RiskStrategy>();

		Criteria criteria = this.dao.newCriteria(RiskStrategy.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskStrategy.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskStrategy.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Retorna os objetivos de processo do risco
	 * 
	 * @param risk instância do Risk
	 * @return PaginatedList<RiskProcess> lista de objetivos de processo
	 */
	public PaginatedList<RiskProcess> listRiskProcess(Risk risk) {
		PaginatedList<RiskProcess> results = new PaginatedList<RiskProcess>();

		Criteria criteria = this.dao.newCriteria(RiskProcess.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskProcess.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskProcess.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Retorna as atividades do processo do risco
	 * 
	 * @param risk instância do Risk
	 * @return PaginatedList<RiskActivity> lista de atividades do processo
	 */
	public PaginatedList<RiskActivity> listRiskActivity(Risk risk) {
		PaginatedList<RiskActivity> results = new PaginatedList<RiskActivity>();

		Criteria criteria = this.dao.newCriteria(RiskActivity.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskActivity.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskActivity.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	public PaginatedList<RiskHistory> listHistoryByUnits(PaginatedList<Unit> units, boolean threat) {

		PaginatedList<RiskHistory> results = new PaginatedList<RiskHistory>();
		List<RiskHistory> list = new ArrayList<>();

		for (Unit unit : units.getList()) {
			Criteria criteria = this.dao.newCriteria(RiskHistory.class)
					.add(Restrictions.eq("unit", unit))
					.add(Restrictions.eq("deleted", false))
					.add(Restrictions.eq("threat", threat))
					.addOrder(Order.asc("id"));

			list.addAll(this.dao.findByCriteria(criteria, RiskHistory.class));
		}

		results.setList(list);
		results.setTotal((long) list.size());
		return results;

	}

	public PaginatedList<MonitorHistory> listMonitorHistoryByUnits(PaginatedList<Unit> units) {

		PaginatedList<MonitorHistory> results = new PaginatedList<MonitorHistory>();
		List<MonitorHistory> list = new ArrayList<>();

		for (Unit unit : units.getList()) {
			Criteria criteria = this.dao.newCriteria(MonitorHistory.class).add(Restrictions.eq("unit", unit))
					.add(Restrictions.eq("deleted", false)).addOrder(Order.asc("id"));

			list.addAll(this.dao.findByCriteria(criteria, MonitorHistory.class));
		}

		results.setList(list);
		results.setTotal((long) list.size());
		return results;
	}

	/**
	 * Transforma a string matrix em vetor
	 * 
	 * 
	 * @param policy
	 * @return
	 */
	public String[][] getMatrixVector(Policy policy) {
		int total = policy.getNline() * policy.getNcolumn() + policy.getNline() + policy.getNcolumn();
		String[] aux = policy.getMatrix().split(";");
		String[][] matrix = new String[total][3];

		for (int i = 0; i < aux.length; i++) {
			matrix[i][0] = aux[i].split("\\[.*\\]")[1];
			Pattern pattern = Pattern.compile("\\[.*\\]");
			Matcher matcher = pattern.matcher(aux[i]);
			if (matcher.find()) {
				matrix[i][1] = matcher.group(0).split(",")[0].split("\\[")[1];
				matrix[i][2] = matcher.group(0).split(",")[1].split("\\]")[0];
			}
		}
		return matrix;
	}

	/**
	 * Atualiza a probabilidade e impacto do risco
	 * 
	 * @param Risk instância de um risco
	 * @param      Map<String, String> map de probabilidade e risco novos e antigos
	 */
	public void updateRiskPI(Risk risk, Map<String, String> impact_probability) {

		PaginatedList<Monitor> monitors = this.listMonitorByRisk(risk);

		// atualiza no monitor
		for (Monitor monit : monitors.getList()) {
			String newprob = impact_probability.get(monit.getProbability());
			String newimp = impact_probability.get(monit.getImpact());

			monit.setImpact(newimp);
			monit.setProbability(newprob);

			this.persist(monit);
		}

		// atualiza no risco

		String newprob = impact_probability.get(risk.getProbability());
		String newimp = impact_probability.get(risk.getImpact());

		risk.setImpact(newimp);
		risk.setProbability(newprob);

		this.persist(risk);

	}

	/**
	 * Verifica se um processo tem algum risco vinculado
	 * 
	 * @param process instância de um processo
	 * @param         boolean booleano para verificação
	 */
	public boolean hasLinkedRiskProcess(Process process) {
		Criteria criteria = this.dao.newCriteria(RiskProcess.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("process", process));
		criteria.setMaxResults(1);

		return criteria.uniqueResult() != null;
	}

	/**
	 * Verifica se um processo tem alguma atividade vinculada
	 * 
	 * @param process instância de um processo
	 * @param         boolean booleano para verificação
	 */
	public boolean hasLinkedRiskActivity(Process process) {
		Criteria criteria = this.dao.newCriteria(RiskActivity.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("process", process));
		criteria.setMaxResults(1);
		return criteria.uniqueResult() != null;
	}

	/**
	 * Retorna as ações preventivas
	 * 
	 * @param Risk instância de um risco
	 * 
	 * @return PaginatedList<PreventiveAction>
	 */
	public PaginatedList<PreventiveAction> listPreventiveActionByRisk(Risk risk) {
		PaginatedList<PreventiveAction> results = new PaginatedList<PreventiveAction>();

		Criteria criteria = this.dao.newCriteria(PreventiveAction.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(PreventiveAction.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, PreventiveAction.class));
		results.setTotal((Long) count.uniqueResult());
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
		int state = 0; // em dia

		Date now = new Date();

		double diffInSec = (now.getTime() - date.getTime()) / 1000;
		double diffDays = diffInSec / (60 * 60 * 24);
		switch (periodicity.toLowerCase()) {
		case "diária":
			if(diffDays > 1) {
				state = 2;
			}else if(diffDays*24 > CloseToMaturityPeriod.DIARIO.getValue()) {
				state=1;
			}
			break;
			
		case "semanal":
			if(diffDays > 7){	
				state = 2;
			}else if (diffDays >7-CloseToMaturityPeriod.SEMANAL.getValue()) {
				state = 1;
			} 
			break;

		case "quinzenal":
			if(diffDays > 15){	
				state = 2;
			}else if (diffDays >15-CloseToMaturityPeriod.QUINZENAL.getValue()) {
				state = 1;
			} 
			break;

		case "mensal":
			if(diffDays > 30){	
				state = 2;
			}else if (diffDays >30-CloseToMaturityPeriod.MENSAL.getValue()) {
				state = 1;
			} 
			break;

		case "bimestral":
			if(diffDays > 60){
				state = 2;
			}else if (diffDays >60-CloseToMaturityPeriod.BIMESTRAL.getValue()) {
				state = 1;
			} 
			break;

		case "trimestral":
			if(diffDays > 90){
				state = 2;
			}else if (diffDays >90-CloseToMaturityPeriod.TRIMESTRAL.getValue()) {
				state = 1;
			} 
			break;

		case "semestral":
			if(diffDays > 180){
				state = 2;
			}else if (diffDays >180-CloseToMaturityPeriod.SEMESTRAL.getValue()) {
				state = 1;
			} 
			break;

		case "anual":
			if(diffDays > 360){
				state = 2;
			}else if (diffDays >CloseToMaturityPeriod.ANUAL.getValue()) {
				state = 1;
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

	
	
	public PaginatedList<Risk> listRiskState(int state) {
		
		PaginatedList<Risk> results = new PaginatedList<Risk>();
		
		
		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false));
		
		List<Risk> risks= this.dao.findByCriteria(criteria, Risk.class);
		List<Risk> list= new ArrayList<>();
		
				 for(Risk risk : risks) {
					 Monitor monitor = lastMonitorbyRisk(risk);
						Date date = risk.getBegin();

						if(monitor != null) {
							date = monitor.getBegin();
						}

					 if(new RiskBS().riskState(risk.getPeriodicity(),date)==state){
						 list.add(risk);
					 }
				 }
		
		results.setList(list);
		results.setTotal( (long) list.size());
		return results;
	}

	public Date CloseToMaturityDate(Risk risk) {
		//calcular corretamente o valor de próximo a vencer
		
		Monitor lastMonitor=this.lastMonitorbyRisk(risk);
		
		long time= lastMonitor!=null?lastMonitor.getBegin().getTime() : risk.getBegin().getTime();
		long hour = 1000*60*60;
		long day = hour*24;
		
		switch (risk.getPeriodicity().toLowerCase()) {
			case "diária":
				time+=hour*(24 - CloseToMaturityPeriod.DIARIO.getValue());
				break;

			case "semanal":
				time+=day*(7 - CloseToMaturityPeriod.SEMANAL.getValue());
				break;

			case "quinzenal":
				time+=day*(15 - CloseToMaturityPeriod.QUINZENAL.getValue());
				break;

			case "mensal":
				time+=day*(30 - CloseToMaturityPeriod.MENSAL.getValue());
				break;

			case "bimestral":
				time+=day*(60 - CloseToMaturityPeriod.BIMESTRAL.getValue());
				break;

			case "trimestral":
				time+=day*(90 - CloseToMaturityPeriod.TRIMESTRAL.getValue());
				break;

			case "semestral":
				time+=day*(180 - CloseToMaturityPeriod.SEMESTRAL.getValue());
				break;

			case "anual":
				time+=day*(360 - CloseToMaturityPeriod.ANUAL.getValue());
				break;
		}
		
		return new Date (time);
	}

	public CompanyDomain companyDomainByRisk(Risk risk) {
		
		PlanRisk  planRisk= this.dao.exists(risk.getUnit().getPlanRisk().getId(), PlanRisk.class);
		
		Company copmany = this.dao.exists(planRisk.getPolicy().getCompany().getId(), Company.class);
		
		Criteria criteria = this.dao.newCriteria(CompanyDomain.class)
				.add(Restrictions.eq("company", copmany));
		
		return (CompanyDomain) criteria.list().get(0);
	}
	

	public void sendUserLinkedToRiskNoktification(Risk risk, Unit unit, String baseUrl) throws EmailException {
		String url = baseUrl + "/#/forrisco/plan-risk/" + unit.getPlanRisk().getId() + "/unit/" +
				unit.getId() + "/risk/" + risk.getId() + "/info";

		String text = String.format("Você foi vinculado como responsável pelo risco %s no ForRisco", risk.getName());

		this.notificationBS.sendNotification(NotificationType.FORRISCO_USER_LINKED_TO_RISK,
				text, null, risk.getUser().getId(), url);
		this.notificationBS.sendNotificationEmail(NotificationType.FORRISCO_USER_LINKED_TO_RISK,
				text, "", risk.getUser(), url);
	}
}