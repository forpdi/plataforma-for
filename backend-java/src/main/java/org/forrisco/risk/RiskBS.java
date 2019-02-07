package org.forrisco.risk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;

import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.objective.RiskActivity;
import org.forrisco.risk.objective.RiskProcess;
import org.forrisco.risk.objective.RiskStrategy;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ibm.icu.impl.duration.TimeUnit;

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
		action.setAccomplished(false);
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
		
		for(RiskActivity activity : risk.getActivities().getList()) {
			activity.setRisk(risk);
			activity.setDeleted(false);
			activity.setName( activity.getActivity() +" - "+ activity.getProcess().getName());
			activity.setLinkFPDI(activity.getLinkFPDI()+"/#/forrisco/plan/x/unit/"+activity.getRisk().getUnit().getId());//pegar link correto da unidade que contem o processo
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
		
		for(RiskProcess process : risk.getProcess().getList()) {
			process.setRisk(risk);
			process.setDeleted(false);
			process.setName(process.getProcess().getObjective()+ " - " + process.getProcess().getName());
			
			process.setLinkFPDI(process.getLinkFPDI()+"/#/forrisco/plan/x/unit/"+process.getRisk().getUnit().getId());//pegar link correto da unidade que contem o processo
			this.dao.persist(process);
		}
	}

	/**
	 * Salvar uma lista de objetivos estratégicos
	 * 
	 * @param PaginatedList<RiskStrategy>
	 *            instância de uma lista de objetivos estratégicos
	 */
	public void saveStrategies(Risk risk) {
		
		for(RiskStrategy strategy : risk.getStrategies().getList()) {
			strategy.setRisk(risk);
			strategy.setDeleted(false);
			
			Plan plan = this.dao.exists(this.dao.exists(strategy.getStructure().getId(), StructureLevelInstance.class).getId(), Plan.class);
			
			strategy.setLinkFPDI(strategy.getLinkFPDI()+"/#/plan/"+plan.getParent().getId()+"/details/subplan/level/"+strategy.getStructure().getId());
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
	 * Retorna uma lista de grau de risco a partir da política
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
		
		Criteria count = this.dao.newCriteria(Risk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, Risk.class));
		results.setTotal((Long) count.uniqueResult());
		
		return results;
	}
		/*PaginatedList<Risk> results = new PaginatedList<Risk>();
		List<Risk> risks= new ArrayList<Risk>();
		
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));
		
		for( ProcessUnit pu : this.dao.findByCriteria(criteria, ProcessUnit.class)){
			risks.addAll(this.listRiskbyProcessUnit(pu));
		}
		
		results.setList(risks);
		results.setTotal((long) risks.size());*
	


	**
	 * Retorna lista de riscos a partir de processunit
	 * 
	 * @param ProcessUnit,
	 *            instância processunit
	 * 
	 * @return List<Risk>
	 *
	private List<Risk> listRiskbyProcessUnit(ProcessUnit pu) {
		Criteria criteria = this.dao.newCriteria(Risk.class)
		.add(Restrictions.eq("deleted", false))
		.add(Restrictions.eq("pu", pu));
		
		return this.dao.findByCriteria(criteria, Risk.class);
	}*/

	//recuperar o risklevel do risco
	//risco -> unidade -> plano -> politica -> risco level
	/**
	 * Retorna o grau de risco a partir da probabilidade e impacto
	 * 
	 * @param ProcessUnit,
	 *            instância processunit
	 * 
	 * @return List<Risk>
	 */
	public RiskLevel getRiskLevelbyRisk(Risk risk, Policy policy) {
		
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
			if(i%policy.getNline() ==0){
				
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
		
		//String result=matrix[((linha)*policy.getNcolumn()-1)+coluna][0];
		
		String result=matrix[((linha-1)*policy.getNcolumn())+coluna][0];
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.add(Restrictions.eq("level", result));
		
		return (RiskLevel)criteria.uniqueResult();
		
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

	public Monitor lastMonitorbyRisk(Risk risk) {
			
		Criteria criteria = this.dao.newCriteria(Monitor.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).addOrder(Order.desc("begin"));

		criteria.setMaxResults(1);
		Monitor monitor = (Monitor) criteria.uniqueResult();
		
		return monitor;
	}

	public int riskState(String periodicity, Monitor monitor) {
		int state=0; //não iniciado
		
		if(monitor == null) {
			return state;
		}

		Date date = monitor.getBegin();
		Date now= new Date();
		
		double diffInSec = (now.getTime() - date.getTime())/1000;
		double diffDays=diffInSec/(60*60*24);
	

		switch(periodicity) {
			case "diária":
				if(diffDays<0.85){state=1;}//em dia
				else if(diffDays<1){state=2;}	//próximos a vencer 
				else{state=3;}	//atrasado
				break;
	
			case "semanal":
				if(diffDays<6){state=1;}
				else if(diffDays<7){state=2;}
				else{state=3;}
				break;
	
			case "quinzenal":
				if(diffDays<12){state=1;}
				else if(diffDays<15){state=2;}
				else{state=3;}
				break;
	
			case "mensal":
				if(diffDays<24){state=1;}
				else if(diffDays<30){state=2;}
				else{state=3;}
				break;
	
			case "bimestral":
				if(diffDays<48){state=1;}
				else if(diffDays<60){state=2;}
				else{state=3;}
				break;
	
			case "trimestral":
				if(diffDays<72){state=1;}
				else if(diffDays<90){state=2;}
				else{state=3;}
				break;
	
			case "semestral":
				if(diffDays<144){state=1;}
				else if(diffDays<180){state=2;}
				else{state=3;}
				break;
	
			case "anual":
				if(diffDays<288){state=1;}
				else if(diffDays<360){state=2;}
				else{state=3;}
				break;
		}
		
		return state;
	}


}