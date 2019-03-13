package org.forrisco.risk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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

	
	@Inject protected HibernateDAO dao;
	@Inject protected HttpServletRequest request;
	


	/**
	 * Salva no banco de dados um novo risco
	 * 
	 * @param Risk,
	 *            instância de um risco a ser salvo
	 */
	public void saveRisk(Risk risk) {
		risk.setDeleted(false);
		this.persist(risk);	
	}
	
	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void saveRiskLevel(Policy policy) {
		
		String[][] str =  policy.getRisk_level();
		
		for(int i = 0; i<str[0].length;i++) {
			if(str[1][i] !=null) {
				RiskLevel rk= new RiskLevel();		
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
	 * @param PreventiveAction,
	 *            instância de uma ação preventiva a ser salva
	 */
	public void saveAction(PreventiveAction action) {
		action.setDeleted(false);
//		action.setAccomplished(false);
		this.persist(action);
	}
	
	/**
	 * Salva no banco de dados um novo monitoramento
	 * 
	 * @param Monitor,
	 *            instância de um monitoramento a ser salva
	 */
	public void saveMonitor(Monitor monitor) {
		monitor.setDeleted(false);
		this.persist(monitor);
	}

	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param RiskLevel,
	 *            instância de um grau de risco a ser salvo
	 */
	public void saveRiskLevel(RiskLevel risklevel) {
		risklevel.setDeleted(false);
		this.persist(risklevel);	
	}

	/**
	 * Salva no banco de dados incidente
	 * 
	 * @param Incident,
	 *            instância de um incident a ser salvo
	 */
	public void saveIncident(Incident incident) {
		incident.setDeleted(false);
		this.persist(incident);	
	}

	/**
	 * Salva no banco de dados contigenciamento
	 * 
	 * @param Contigency,
	 *            instância de um contigenciamento a ser salvo
	 */
	public void saveContingency(Contingency contingency) {
		contingency.setDeleted(false);
		this.persist(contingency);	
	}
	
	/**
	 * Salvar uma lista de atividades
	 * 
	 * @param PaginatedList<RiskActivity>
	 *            instância de uma lista de atividades de risco
	 */
	public void saveActivities(Risk risk) {
		
		if(risk.getActivities().getList() == null) {
			return;
		}
		
		for(RiskActivity activity : risk.getActivities().getList()) {
			activity.setId(null);
			activity.setRisk(risk);
			activity.setDeleted(false);
			
			Process process =this.dao.exists(activity.getProcess().getId(), Process.class);
			Unit unit =this.dao.exists(process.getUnit().getId(), Unit.class);
			
			activity.setProcess(process);
			
			if(activity.getName() == null) {
				activity.setName("");
			}

			
			//pegar link correto da unidade que contem o processo
			activity.setLinkFPDI("#/forrisco/plan-risk/"+unit.getPlan().getId()+"/unit/"+unit.getId()+"/info");
			this.dao.persist(activity);
		}
	}

	/**
	 * Salvar uma lista de objetivos de processos
	 * 
	 * @param PaginatedList<RiskProcess>
	 *            instância de uma lista de processos de unidade/instituição
	 */
	public void saveProcesses(Risk risk) {
		
		if(risk.getProcess().getList() == null) {
			return;
		}
		
		for(RiskProcess riskprocess : risk.getProcess().getList()) {
			riskprocess.setId(null);
			riskprocess.setRisk(risk);
			riskprocess.setDeleted(false);
			
			Unit unit =this.dao.exists(risk.getUnit().getId(), Unit.class);
			
			Process process =this.dao.exists(riskprocess.getProcess().getId(), Process.class);
			
			riskprocess.setName(process.getObjective()+ " - " + process.getName());
			riskprocess.setProcess(process);
			
			//pegar link correto da unidade que contem o processo
			riskprocess.setLinkFPDI("#/forrisco/plan-risk/"+unit.getPlan().getId()+"/unit/"+unit.getId()+"/info");
			this.dao.persist(riskprocess);
		}
	}

	/**
	 * Salvar uma lista de objetivos estratégicos
	 * 
	 * @param PaginatedList<RiskStrategy>
	 *            instância de uma lista de objetivos estratégicos
	 * @throws Exception 
	 */
	public void saveStrategies(Risk risk) throws Exception {
		
		if(risk.getStrategies().getList() == null) {
			return;
		}
		
		for(RiskStrategy strategy : risk.getStrategies().getList()) {
			strategy.setId(null);
			strategy.setRisk(risk);
			strategy.setDeleted(false);
			
			StructureLevelInstance structure =this.dao.exists(strategy.getStructure().getId(), StructureLevelInstance.class);
			if(structure ==null) {
				throw new Exception("Objetivo do PDI não foi encontrado");
			}
			
			Plan plan = this.dao.exists(structure.getPlan().getId(), Plan.class);

			if(plan ==null) {
				throw new Exception("Plano do PDI não foi encontrado");
			}
			
			//pegar link correto do Objetivo na Plataforma ForPDI.
			strategy.setLinkFPDI("#/plan/"+plan.getParent().getId()+"/details/subplan/level/"+strategy.getStructure().getId());
			strategy.setName(structure.getName());
			this.dao.persist(strategy);
		}
	}

	
	
	

	
	
	
	/**
	 *Deleta do banco de dados  graus de risco
	 * 
	 * @param Item,
	 *			instância do item a ser deletado
	 */
	public void delete(RiskLevel risklevel) {
		risklevel.setDeleted(true);
		this.persist(risklevel);
	}

	/**
	 *Deleta do banco de dados uma ação de prevenção
	 * 
	 * @param PreventiveAction,
	 *			instância da a ação ser deletado
	 */
	public void delete(PreventiveAction action) {
		action.setDeleted(true);
		this.persist(action);
	}
	
	/**
	 *Deleta do banco de dados um monitoramento
	 * 
	 * @param Monitor,
	 *			instância do monitoramento ser deletado
	 */
	public void delete(Monitor monitor) {
		monitor.setDeleted(true);
		this.persist(monitor);
	}
	
	/**
	 *Deleta do banco de dados um incidente
	 * 
	 * @param Incident,
	 *			instância do incidente a ser deletado
	 */
	public void delete(Incident incident) {
		incident.setDeleted(true);
		this.persist(incident);
	}
	
	/**
	 *Deleta do banco de dados um incidente
	 * 
	 * @param Incident,
	 *			instância do incidente a ser deletado
	 */
	public void delete(Contingency contingency) {
		contingency.setDeleted(true);
		this.persist(contingency);
	}
	
	/**
	 * Deleta do banco de dados uma atividade do processo
	 * 
	 * @param RiskActivity,
	 *			instância da atividade do processo
	 */
	private void delete(RiskActivity act) {
		act.setDeleted(true);
		this.persist(act);
	}
	
	/**
	 * Deleta do banco de dados um processo do objetivo
	 * 
	 * @param RiskProcess,
	 *			instância do processo
	 */
	private void delete(RiskProcess pro) {
		pro.setDeleted(true);
		this.persist(pro);
	}
	
	/**
	 * Deleta do banco de dados um objetivo estratégico
	 * 
	 * @param RiskStrategy,
	 *			instância do objetivo
	 */
	private void delete(RiskStrategy str) {
		str.setDeleted(true);
		this.persist(str);
	}
	
	/**
	 * Deleta do banco de dados as atividades,
	 *	os processos e os objetivos estratégicos de um risco
	 * 
	 * @param Risk,
	 *			instância do risco
	 */
	public void deleteAPS(Risk oldrisk) {
		
		Risk risk = this.dao.exists(oldrisk.getId(), Risk.class);
		if (risk == null) {
			return;
		}  
		
		PaginatedList<RiskActivity> activity= this.listRiskActivity(risk);
		PaginatedList<RiskProcess> process= this.listRiskProcess(risk);
		PaginatedList<RiskStrategy> strategy= this.listRiskStrategy(risk);
		
		
		for(RiskActivity act : activity.getList()) {
			this.delete(act);
		}
		
		for(RiskProcess pro : process.getList()) {
			this.delete(pro);
		}
		
		for(RiskStrategy str : strategy.getList()) {
			this.delete(str);
		}
	}
	
	
	/**
	 * Deleta do banco de dados um risco,
	 * 
	 * @param Risk,
	 *			instância do risco
	 */
	public void delete(Risk risk) {
		risk.setDeleted(true);
		this.persist(risk);
	}
		
	/**
	 * Retorna um risco 
	 * 
	 * @param id
	 * 		Id do risco
	 */
	public Risk listRiskById(Long id) {
		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
			criteria.setMaxResults(1);
			
		return (Risk) criteria.uniqueResult();
	}
	
	/**
	 * Retorna uma lista de grau de risco a partir da política
	 * 
	 * política não salva no banco (acho que da para usar a outra função)
	 * @param policy
	 * 
	 */
	public PaginatedList<RiskLevel> listRiskLevel(Policy policy) {
		List<RiskLevel> array = new  ArrayList<RiskLevel>();
		PaginatedList<RiskLevel> list = new  PaginatedList<RiskLevel>();
		String[][] str =  policy.getRisk_level();
		
		for(int i = 0; i<str[0].length;i++) {
			if(str[1][i] !=null) {
				RiskLevel rk= new RiskLevel();		
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
	 * @param policy
	 * 			instância da política
	 * @return
	 */
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
	
	/**
	 * Retorna riscos de uma unidade
	 * 
	 * @param Unit,
	 *            instância da unidade
	 *            
	 */
	public PaginatedList<Risk> listRiskbyUnit(Unit unit) {
		PaginatedList<Risk> results = new PaginatedList<Risk>();
		
		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
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
	 * @param List<Unit>,
	 *            lista da unidades
	 *            
	 */
	public PaginatedList<Risk> listRiskbyUnitList(List<Unit> units) {
		PaginatedList<Risk> results = new PaginatedList<Risk>();
		
		Criteria criteria = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.in("unit", units));
		
		List<Risk> risks = this.dao.findByCriteria(criteria, Risk.class);
		results.setList(risks);
		results.setTotal((long) risks.size());
		
		return results;
	}

	//recuperar o risklevel do risco
	//risco -> unidade -> plano -> politica -> risco level
	/**
	 * Retorna o grau de risco a partir da probabilidade e impacto
	 * 
	 * @param ProcessUnit,
	 *            instância processunit
	 * 
	 * @return List<Risk>
	 * @throws Exception 
	 */
	public RiskLevel getRiskLevelbyRisk(Risk risk, Policy policy) throws Exception {
		
		if(policy == null) {
			Unit unit = this.exists(risk.getUnit().getId(), Unit.class);
			
			if(unit==null) {return null;}
			
			PlanRisk planRisk = this.exists(unit.getPlan().getId(), PlanRisk.class);
			
			if(planRisk==null) {return null;}
			
			policy = this.exists(planRisk.getPolicy().getId(), Policy.class);
			
			if(policy==null) {return null;}
		}
		
		
		
		int total=policy.getNline()*policy.getNcolumn()+policy.getNline()+policy.getNcolumn();
		String[][]matrix = getMatrixVector(policy);
		
		int linha=0;
		int coluna=0;
		for(int i=0;i<total;i++){
			if(i%(policy.getNline()+1) ==0){
				
				if(risk.getProbability().equals(matrix[i][0])){
					i=total;
				}
				linha+=1;
			}
		}
		
		for(int i=total-policy.getNcolumn();i<total;i++){
			if(risk.getImpact().equals(matrix[i][0])){
				i=total;
			}
			coluna+=1;
		}
		
		int position =((linha-1)*(policy.getNcolumn()+1))+coluna;
		
		if(position>=total) {
			throw new Exception("falha ao carregar grau de risco");
		}
		
		String result=matrix[position][0];
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.add(Restrictions.eq("level", result));
		
		return (RiskLevel)criteria.uniqueResult();
		
	}
	
	
	/**
	 * Retorna as ativiades do processo de um risco
	 * 
	 * @param Risk
	 *            instância de um risco
	 * 
	 * @return PaginatedList<RiskActivity>
	 */
	public PaginatedList<RiskActivity> listActivityByRisk(Risk risk) {
		
		PaginatedList<RiskActivity> results = new PaginatedList<RiskActivity>();
		
		Criteria criteria = this.dao.newCriteria(RiskActivity.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskActivity.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskActivity.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
		
	}

	
	/**
	 * Retorna as ações preventivas a partir de um risco
	 * 
	 * @param Risk
	 *            instância de um risco
	 * 
	 * @return PaginatedList<PreventiveAction>
	 */
	public PaginatedList<PreventiveAction> listActionbyRisk(Risk risk) {
		
		PaginatedList<PreventiveAction> results = new PaginatedList<PreventiveAction>();
		
		Criteria criteria = this.dao.newCriteria(PreventiveAction.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

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
	 * @param Risk
	 *            instância de um risco
	 * 
	 * @return PaginatedList<Monitor>
	 */
	public PaginatedList<Monitor> listMonitorbyRisk(Risk risk) {
		
		PaginatedList<Monitor> results = new PaginatedList<Monitor>();
		
		Criteria criteria = this.dao.newCriteria(Monitor.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

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
	 * @param Risk
	 *            instância de um risco
	 * 
	 * @return PaginatedList<Incident>
	 */
	public PaginatedList<Incident> listIncidentsbyRisk(Risk risk){
		
		PaginatedList<Incident> results = new PaginatedList<Incident>();
		
		Criteria criteria = this.dao.newCriteria(Incident.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(Incident.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, Incident.class));
		results.setTotal((Long) count.uniqueResult());
		
		for(Incident incident : results.getList()) {
			
			incident.setUnitId(incident.getRisk().getUnit().getId());
		}
		
		return results;
	}
	
	
	/**
	 * Retorna os incidentes a partir de vários riscos
	 * 
	 * @param PaginatedList<Risk>
	 *            instância de um risco
	 * 
	 * @return PaginatedList<Incident>
	 */
	public PaginatedList<Incident> listIncidentsbyRisk(PaginatedList<Risk> risks){
		
		List<Incident> inc = new ArrayList<>();
		
		for(Risk risk: risks.getList()) {
			PaginatedList<Incident> incident = listIncidentsbyRisk(risk);
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
	 * @param Risk
	 *            instância de um risco
	 * 
	 * @return PaginatedList<Contingency>
	 */
	public PaginatedList<Contingency> listContingenciesbyRisk(Risk risk) {
		
		PaginatedList<Contingency> results = new PaginatedList<Contingency>();
		
		Criteria criteria = this.dao.newCriteria(Contingency.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

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
	 * @param risk
	 * 			instância do Risk
	 * @return 
	 * 		PaginatedList<RiskStrategy> lista de objetivos estrategicos
	 */
	public PaginatedList<RiskStrategy> listRiskStrategy(Risk risk) {
		PaginatedList<RiskStrategy> results = new PaginatedList<RiskStrategy>();
		
		Criteria criteria = this.dao.newCriteria(RiskStrategy.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskStrategy.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskStrategy.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	/**
	 * Retorna os objetivos de processo do risco
	 * 
	 * @param risk
	 * 			instância do Risk
	 * @return 
	 * 		PaginatedList<RiskProcess> lista de objetivos de processo
	 */
	public PaginatedList<RiskProcess> listRiskProcess(Risk risk) {
		PaginatedList<RiskProcess> results = new PaginatedList<RiskProcess>();
		
		Criteria criteria = this.dao.newCriteria(RiskProcess.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskProcess.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskProcess.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	/**
	 * Retorna as atividades do processo do risco
	 * 
	 * @param risk
	 * 			instância do Risk
	 * @return 
	 * 		PaginatedList<RiskActivity> lista de atividades do processo 
	 */
	public PaginatedList<RiskActivity> listRiskActivity(Risk risk) {
		PaginatedList<RiskActivity> results = new PaginatedList<RiskActivity>();
		
		Criteria criteria = this.dao.newCriteria(RiskActivity.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk));

		Criteria count = this.dao.newCriteria(RiskActivity.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskActivity.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}


	
	
	
	
	
	
	
	
	

	public PaginatedList<RiskHistory> listHistoryByUnits(PaginatedList<Unit> units) {
		
		PaginatedList<RiskHistory> results = new PaginatedList<RiskHistory>();
		List<RiskHistory> list = new ArrayList<>();
		
		for(Unit unit : units.getList()) {
			Criteria criteria = this.dao.newCriteria(RiskHistory.class)
				.add(Restrictions.eq("unit", unit))
				.add(Restrictions.eq("deleted", false))
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
		
		for(Unit unit : units.getList()) {
			Criteria criteria = this.dao.newCriteria(MonitorHistory.class)
				.add(Restrictions.eq("unit", unit))
				.add(Restrictions.eq("deleted", false))
				.addOrder(Order.asc("id"));
			
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
		int total=policy.getNline()*policy.getNcolumn()+policy.getNline()+policy.getNcolumn();
		String[] aux=policy.getMatrix().split(";");
		String[][] matrix = new String[total][3];
		
		for(int i=0; i< aux.length;i++){
			matrix[i][0]=aux[i].split("\\[.*\\]")[1];
			Pattern pattern = Pattern.compile("\\[.*\\]");
			Matcher matcher = pattern.matcher(aux[i]);
			if(matcher.find()){
				matrix[i][1]=matcher.group(0).split(",")[0].split("\\[")[1];
				matrix[i][2]=matcher.group(0).split(",")[1].split("\\]")[0];
			}
		}
		return matrix;
	}

	
	/**
	 * Atualiza a probabilidade e impacto do risco
	 * 
	 * @param Risk
	 *            instância de um risco
	 * @param Map<String, String>   
	 * 				map de probabilidade e risco novos e antigos
	 */
	public void updateRiskPI(Risk risk, Map<String, String> impact_probability) {
		
		PaginatedList<Monitor> monitors = this.listMonitorbyRisk(risk);
		
		//atualiza no monitor
		for(Monitor monit : monitors.getList()) {
			String newprob = impact_probability.get(monit.getProbability());
			String newimp = impact_probability.get(monit.getImpact());

			monit.setImpact(newimp);
			monit.setProbability(newprob);
			
			this.persist(monit);
		}
		
		//atualiza no risco
		
		String newprob = impact_probability.get(risk.getProbability());
		String newimp = impact_probability.get(risk.getImpact());
		
		risk.setImpact(newimp);
		risk.setProbability(newprob);
		
		this.persist(risk);
		
	}

	/**
	 * Verifica se um processo tem algum risco vinculado
	 * 
	 * @param process
	 *            instância de um processo
	 * @param boolean   
	 * 				booleano para verificação
	 */
	public boolean hasLinkedRiskProcess(Process process) {
		Criteria criteria = this.dao.newCriteria(RiskProcess.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("process", process)
		);
		criteria.setMaxResults(1);
		
		return criteria.uniqueResult() != null;
	}
	
	/**
	 * Verifica se um processo tem alguma atividade vinculada
	 * 
	 * @param process
	 *            instância de um processo
	 * @param boolean   
	 * 				booleano para verificação
	 */
	public boolean hasLinkedRiskActivity(Process process) {
		Criteria criteria = this.dao.newCriteria(RiskActivity.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("process", process)
		);
		criteria.setMaxResults(1);
		return criteria.uniqueResult() != null;
	}

	
}