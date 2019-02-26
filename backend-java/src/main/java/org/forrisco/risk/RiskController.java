package org.forrisco.risk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forpdi.core.user.User;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.unit.Unit;
import org.forrisco.core.unit.UnitBS;
import org.forrisco.risk.objective.RiskActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Matheus Nascimento
 */
@Controller
public class RiskController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private RiskBS riskBS;
	@Inject private UnitBS unitBS ;

	protected static final String PATH =  BASEPATH +"/risk";
	
	
	/**
	 * Salvar Novo Risco
	 * 
	 * @Param Risk
	 * 			instancia de um novo risco
	 */
	@Post(PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Risk risk){
		try {			
			Unit unit = this.riskBS.exists(risk.getUnit().getId(), Unit.class);
			if (unit == null) {
				this.fail("A unidade solicitada não foi encontrada.");
				return;
			}
			
			User user = this.riskBS.exists(risk.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			
			risk.setRiskLevel(this.riskBS.getRiskLevelbyRisk(risk,null));
			
			if (risk.getRiskLevel() == null) {
				this.fail("Grau de Risco solicitado não foi encontrado.");
				return;
			}
			
			this.riskBS.saveActivities(risk);
			this.riskBS.saveProcesses(risk);
			this.riskBS.saveStrategies(risk);
			
			risk.setId(null);
			risk.setBegin(new Date());
			this.riskBS.saveRisk(risk);
			

			
			this.success(risk);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Salvar Nova ação de prevenção
	 * 
	 * @Param PreventiveAction
	 * 			instancia de uma nova ação preventiva
	 */
	@Post(PATH + "/actionnew")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid PreventiveAction action){
		try {
			Risk risk = this.riskBS.exists(action.getRisk().getId(), Risk.class);
			if (risk == null) {
				this.fail("Risco solicitado não foi encontrado.");
				return;
			}
			
			User user = this.riskBS.exists(action.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			
			action.setId(null);
			this.riskBS.saveAction(action);
			this.success(action);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Salvar Novo monitoramento
	 * 
	 * @Param Monitor
	 * 			instancia de uma novo monitoramento
	 */
	@Post(PATH + "/monitornew")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Monitor monitor){
//		EmailSenderTask.LOG.info((new GsonBuilder().setPrettyPrinting().create().toJson(monitor)));
		try {
			Risk risk = this.riskBS.exists(monitor.getRisk().getId(), Risk.class);
			if (risk == null) {
				this.fail("Risco solicitado não foi encontrado.");
				return;
			}
			
			User user = this.riskBS.exists(monitor.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			
			monitor.setId(null);
//			monitor.setBegin(new Date());
			
			this.riskBS.saveMonitor(monitor);
			
			risk.setImpact(monitor.getImpact());
			risk.setProbability(monitor.getProbability());
			risk.setRiskLevel(this.riskBS.getRiskLevelbyRisk(risk,null));
			this.riskBS.persist(risk);
		
			this.success(monitor);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Salvar Novo incidente
	 * 
	 * @Param Incident
	 * 			instancia de uma novo incidente
	 */
	@Post(PATH + "/incidentnew")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Incident incident){
		try {
			Risk risk = this.riskBS.exists(incident.getRisk().getId(), Risk.class);
			if (risk == null) {
				this.fail("Risco solicitado não foi encontrado.");
				return;
			}
			
			User user = this.riskBS.exists(incident.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			if (IncidentType.getById(incident.getType()) == null) {
				this.fail("Tipo de incidente inválido.");
				return;
			}
			
			incident.setId(null);
//			incident.setBegin(new Date());
			
			this.riskBS.saveIncident(incident);
			
			this.success(incident);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Salvar Novo Contingenciamento
	 * 
	 * @Param Contingency
	 * 			instancia de uma novo contingenciamento
	 */
	@Post(PATH + "/contingencynew")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Contingency contingency){
		try {
			Risk risk = this.riskBS.exists(contingency.getRisk().getId(), Risk.class);
			if (risk == null) {
				this.fail("Risco solicitado não foi encontrado.");
				return;
			}
			
			User user = this.riskBS.exists(contingency.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			
			contingency.setId(null);
			this.riskBS.saveContingency(contingency);
			
			this.success(contingency);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	
	
	
	
	
	/**
	 * Retorna risco.
	 * 
	 * @param id
	 *			Id do risco a ser retornado.
	 * @return Risk Retorna o risco de acordo com o id passado.
	 */
	@Get( PATH + "/{id}")
	@NoCache
	//@Permissioned
	public void listUnit(Long id) {
		try {
			Risk risk = this.riskBS.exists(id, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
			} else {
				
			/*RiskActivity act = new RiskActivity();
				act.setProcess(process);
				save(Risk)
				*/
				
				risk.setStrategies(this.riskBS.listRiskStrategy(risk));
				risk.setProcesses(this.riskBS.listRiskProcess(risk));
				risk.setActivities(this.riskBS.listRiskActivity(risk));
				
				
				this.success(risk);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	
	/**
	 * Retorna unidades.
	 * 
	 * @param PLanRisk
	 *            Id do plano de risco.
	 * @return <PaginatedList> Unit
	 */
	@Get( PATH + "")
	@NoCache
	public void listRisksbyPlan(@NotNull Long planId) {
		try {			
			PlanRisk plan = this.riskBS.exists(planId, PlanRisk.class);
			PaginatedList<Unit> units= this.unitBS.listUnitsbyPlanRisk(plan);
			
			List<Risk> list = new ArrayList<>();
			
			 for(Unit unit : units.getList()) {
				 PaginatedList<Risk> risks= this.riskBS.listRiskbyUnit(unit);
				 list.addAll(risks.getList());
			 }
			 
			 
			 PaginatedList<Risk> risks = new  PaginatedList<Risk>();
			
			risks.setList(list);
			risks.setTotal((long) list.size());
			
			this.success(risks);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
		
	/**
	 * Retorna ações de prevenção.
	 * 
	 * @param id
	 *			Id do risco.
	 * @return <PaginedList> PreventiveAcion
	 * 			 Retorna lista de ações de prevenção do risco.
	 */
	@Get( PATH + "/action")
	@NoCache
	//@Permissioned
	public void listActions(@NotNull Long riskId) {
		try {
			Risk risk = this.riskBS.exists(riskId, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			PaginatedList<PreventiveAction> actions = this.riskBS.listActionbyRisk(risk);
			
			this.success(actions);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna monitoramentos.
	 * 
	 * @param planId
	 *			Id do plano.
	 * @return <PaginedList> Monitor
	 * 			 Retorna lista de monitoramentos do risco.
	 */
	@Get( PATH + "/monitor")
	@NoCache
	//@Permissioned
	public void listMonitors(@NotNull Long planId) {
		try {
			PlanRisk plan = this.riskBS.exists(planId, PlanRisk.class);
			if (plan == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			PaginatedList<Unit> units= this.unitBS.listUnitsbyPlanRisk(plan);
			
			List<Risk> risks = new ArrayList<>();
			List<Monitor> monitors = new ArrayList<>();
		
			
			 for(Unit unit : units.getList()) {
				 PaginatedList<Risk> list= this.riskBS.listRiskbyUnit(unit);
				 risks.addAll(list.getList());
			 }
			 
			 for(Risk risk : risks) {
				 PaginatedList<Monitor> list = this.riskBS.listMonitorbyRisk(risk);
				 monitors.addAll(list.getList());
			 }
			 
			 for(Monitor monitor : monitors) {
				 monitor.setUnitId(monitor.getRisk().getUnit().getId());
			 }
	
			PaginatedList<Monitor> monitor = new  PaginatedList<Monitor>();
			
			monitor.setList(monitors);
			monitor.setTotal((long) monitors.size());
			
			this.success(monitor);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna incidentes.
	 * 
	 * @param planId
	 *			Id do plano.
	 * @return <PaginedList> Incident
	 * 			 Retorna lista de incidentes do risco.
	 */
	@Get( PATH + "/incident")
	@NoCache
	//@Permissioned
	public void listIncidents(@NotNull Long planId) {
		try {
			PlanRisk plan = this.riskBS.exists(planId, PlanRisk.class);
			if (plan == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			PaginatedList<Unit> units= this.unitBS.listUnitsbyPlanRisk(plan);
			
			List<Risk> risks = new ArrayList<>();
			List<Incident> incidents = new ArrayList<>();
			
			 for(Unit unit : units.getList()) {
				 PaginatedList<Risk> list= this.riskBS.listRiskbyUnit(unit);
				 risks.addAll(list.getList());
			 }
			 
			 for(Risk risk : risks) {
				 PaginatedList<Incident> list = this.riskBS.listIncidentsbyRisk(risk);
				 incidents.addAll(list.getList());
			 }
			 
			 for(Incident incident : incidents) {
				 incident.setUnitId(incident.getRisk().getUnit().getId());
			 }
	
			PaginatedList<Incident> incident = new  PaginatedList<Incident>();
			
			incident.setList(incidents);
			incident.setTotal((long) incidents.size());
			
			this.success(incident);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	

	/**
	 * Retorna contingeciamentos.
	 * 
	 * @param id
	 *			Id do risco.
	 * @return <PaginedList> Contingency
	 * 			 Retorna lista de contingenciamentos do risco.
	 */
	@Get( PATH + "/contingency")
	@NoCache
	//@Permissioned
	public void listContingencies(@NotNull Long riskId) {
		try {
			Risk risk = this.riskBS.exists(riskId, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			PaginatedList<Contingency> contingencies = this.riskBS.listContingenciesbyRisk(risk);
			
			this.success(contingencies);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna historico.
	 * 
	 * @param planId
	 *			Id do plano de risco.
	 *
	 * @param unitId
	 *			Id da unidade.
	 *
	 * @return <PaginedList> RiskHistory
	 * 			 Retorna lista de historicos de riscos
	 */
	@Get( PATH + "/history")
	@NoCache
	//@Permissioned
	public void listHistory(@NotNull Long planId, Long unitId) {
		try {
			PaginatedList<Unit> units= new PaginatedList<Unit>();
			PlanRisk plan = this.riskBS.exists(planId, PlanRisk.class);
			if (plan == null) {
				this.fail("O plano de risco solicitado não foi encontrado.");
				return;
			} 
			
			this.unitBS.updateHistory(plan);
			
			
			if(unitId== -1) {
				units = this.unitBS.listUnitsbyPlanRisk(plan);
			}else {
				Unit unit = this.riskBS.exists(unitId, Unit.class);
				
				if (unit == null) {
					this.fail("O risco solicitado não foi encontrado.");
					return;
				}
				
				List<Unit> list = new ArrayList<Unit>();
				list.add(unit);
				units.setList(list);
			}
			
			PaginatedList<RiskHistory> history = this.riskBS.listHistoryByUnits(units);
						
			this.success(history);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna atividades do processo.
	 * 
	 * @param RiskId
	 *			Id do risco.
	 *
	 *
	 * @return <PaginedList> RiskActivity
	 * 			 Retorna lista de atividades do processo
	 */
	@Get( PATH + "/activity")
	@NoCache
	//@Permissioned
	public void lisActivity(@NotNull Long riskId) {
		try {
			Risk risk = this.riskBS.exists(riskId, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			PaginatedList<RiskActivity> activities = this.riskBS.listActivityByRisk(risk);
			
			this.success(activities);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna historico.
	 * 
	 * @param planId
	 *			Id do plano de risco.
	 *
	 * @param unitId
	 *			Id da unidade.
	 *
	 * @return <PaginedList> RiskHistory
	 * 			 Retorna lista de historicos de riscos
	 */
	@Get( PATH + "/monitorHistory")
	@NoCache
	//@Permissioned
	public void listMonitorHistory(@NotNull Long planId, Long unitId) {
		try {
			PaginatedList<Unit> units= new PaginatedList<Unit>();
			
			PlanRisk plan = this.riskBS.exists(planId, PlanRisk.class);
			if (plan == null) {
				this.fail("O plano de risco solicitado não foi encontrado.");
				return;
			} 
			
			this.unitBS.updateMonitorHistory(plan);
			
			
			if(unitId== -1) {
				units = this.unitBS.listUnitsbyPlanRisk(plan);
			}else {
				Unit unit = this.riskBS.exists(unitId, Unit.class);
				
				if (unit == null) {
					this.fail("O risco solicitado não foi encontrado.");
					return;
				}
				
				List<Unit> list = new ArrayList<Unit>();
				list.add(unit);
				units.setList(list);
			}
			
			PaginatedList<MonitorHistory> history = this.riskBS.listMonitorHistoryByUnits(units);
						
			this.success(history);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Atualiza risco.
	 * 
	 * @param id
	 *            Id da ação a ser atualizada.
	 * @throws Exception 
	 */
	@Post( PATH + "/update")
	@NoCache
	@Consumes
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void updateRisk(@NotNull Risk risk) throws Exception {
		try {
			Risk oldrisk = this.riskBS.exists(risk.getId(), Risk.class);
			if (oldrisk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			User user = this.riskBS.exists(risk.getUser().getId(), User.class);
			if (user == null) {
				this.fail("O usuário solicitado não foi encontrado.");
				return;
			} 
			
			Unit unit = this.riskBS.exists(risk.getUnit().getId(), Unit.class);
			if (unit == null) {
				this.fail("A unidade solicitada não foi encontrada.");
				return;
			}
			
			oldrisk.setUnit(unit);
			oldrisk.setUser(user);
			oldrisk.setImpact(risk.getImpact());
			oldrisk.setProbability(risk.getProbability());
			oldrisk.setPeriodicity(risk.getPeriodicity());
			oldrisk.setCode(risk.getCode());
			oldrisk.setLinkFPDI(risk.getLinkFPDI());
			oldrisk.setName(risk.getName());
			oldrisk.setReason(risk.getReason());
			oldrisk.setResult(risk.getResult());
			oldrisk.setTipology(risk.getTipology());
			oldrisk.setType(risk.getType());
			oldrisk.setRisk_act_process(risk.isRisk_act_process());
			oldrisk.setRisk_obj_process(risk.isRisk_obj_process());
			oldrisk.setRisk_pdi(risk.isRisk_pdi());
			
			
			oldrisk.setRiskLevel(this.riskBS.getRiskLevelbyRisk(oldrisk,null));

			if (oldrisk.getRiskLevel() == null) {
				this.fail("Grau de Risco solicitado não foi encontrado.");
				return;
			}
			
			this.riskBS.deleteAPS(oldrisk);
			
			this.riskBS.saveActivities(risk);
			this.riskBS.saveProcesses(risk);
			this.riskBS.saveStrategies(risk);
			
			this.riskBS.saveRisk(oldrisk);
			
			this.success(oldrisk);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	
	/**
	 * Atualiza ação preventiva.
	 * 
	 * @param id
	 *            Id da ação a ser atualizada.
	 */
	@Post( PATH + "/action/update")
	@NoCache
	@Consumes
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void updateAction(@NotNull PreventiveAction action) {
		try {
			PreventiveAction oldaction = this.riskBS.exists(action.getId(), PreventiveAction.class);
			if (oldaction == null) {
				this.fail("A ação solicitado não foi encontrada.");
				return;
			} 
			
			User user = this.riskBS.exists(action.getUser().getId(), User.class);
			if (user == null) {
				this.fail("O usuário solicitado não foi encontrado.");
				return;
			} 
			
			oldaction.setAccomplished(action.isAccomplished());
			oldaction.setAction(action.getAction());
			oldaction.setUser(user);
			this.riskBS.saveAction(oldaction);
			
			this.success(oldaction);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Atualiza monitoramento.
	 * 
	 * @param id
	 *            Id do monitoramento a ser atualizado.
	 */
	@Post( PATH + "/monitor/update")
	@NoCache
	@Consumes
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void updateAction(@NotNull Monitor monitor) {
		try {
			Monitor oldmonitor = this.riskBS.exists(monitor.getId(), Monitor.class);
			if (oldmonitor == null) {
				this.fail("O monitoramento solicitado não foi encontrado.");
				return;
			} 
			
			User user = this.riskBS.exists(monitor.getUser().getId(), User.class);
			if (user == null) {
				this.fail("O usuário solicitado não foi encontrado.");
				return;
			} 
			
			Risk risk = this.riskBS.exists(oldmonitor.getRisk().getId(), Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			oldmonitor.setImpact(monitor.getImpact());
			oldmonitor.setProbability(monitor.getProbability());
			oldmonitor.setReport(monitor.getReport());
			oldmonitor.setUser(user);
			this.riskBS.saveMonitor(oldmonitor);
			
			
			risk.setImpact(monitor.getImpact());
			risk.setProbability(monitor.getProbability());
			risk.setRiskLevel(this.riskBS.getRiskLevelbyRisk(risk,null));
			this.riskBS.persist(risk);
			
			this.success(oldmonitor);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Atualiza incidente.
	 * 
	 * @param id
	 *            Id do incidente a ser atualizado.
	 */
	@Post( PATH + "/incident/update")
	@NoCache
	@Consumes
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void updateIncident(@NotNull Incident incident) {
		try {
			Incident oldincident = this.riskBS.exists(incident.getId(), Incident.class);
			if (oldincident == null) {
				this.fail("A ação solicitado não foi encontrada.");
				return;
			} 
			
			User user = this.riskBS.exists(incident.getUser().getId(), User.class);
			if (user == null) {
				this.fail("O usuário solicitado não foi encontrado.");
				return;
			} 
			if (IncidentType.getById(incident.getType()) == null) {
				this.fail("Tipo de incidente inválido.");
				return;
			}
			
			oldincident.setAction(incident.getAction());
			oldincident.setDescription(incident.getDescription());
			oldincident.setUser(user);
			oldincident.setType(incident.getType());
			oldincident.setBegin(incident.getBegin());
			
			this.riskBS.saveIncident(oldincident);
			
			this.success(oldincident);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Atualiza contingenciamento.
	 * 
	 * @param id
	 *            Id do contingenciamento a ser atualizado.
	 */
	@Post( PATH + "/contingency/update")
	@NoCache
	@Consumes
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void updateContingency(@NotNull Contingency contingency) {
		try {
			Contingency oldcontingency = this.riskBS.exists(contingency.getId(), Contingency.class);
			if (oldcontingency == null) {
				this.fail("A ação solicitado não foi encontrada.");
				return;
			} 
			
			User user = this.riskBS.exists(contingency.getUser().getId(), User.class);
			if (user == null) {
				this.fail("O usuário solicitado não foi encontrado.");
				return;
			} 
			
			oldcontingency.setAction(contingency.getAction());
			oldcontingency.setUser(user);
			
			this.riskBS.saveContingency(oldcontingency);
			
			this.success(oldcontingency);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Exclui um risco.
	 * 
	 * @param id
	 *            Id do risco.
	 */
	@Delete( PATH + "/{actionId}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deleteRisk(@NotNull Long riskId) {
		try {
			Risk risk = this.riskBS.exists(riskId, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			
			this.riskBS.deleteAPS(risk);
			
			/*TODO
			this.delete(monitors);
			this.delete(incident);
			this.delete(contingency);
			*/
			
			this.riskBS.delete(risk);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Exclui ação preventiva.
	 * 
	 * @param id
	 *            Id da ação a ser excluida.
	 */
	@Delete( PATH + "/action/{actionId}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deleteAction(@NotNull Long actionId) {
		try {
			PreventiveAction action = this.riskBS.exists(actionId, PreventiveAction.class);
			if (action == null) {
				this.fail("A ação solicitada não foi encontrado.");
				return;
			} 
			
			riskBS.delete(action);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Exclui monitoramento.
	 * 
	 * @param id
	 *            Id do monitoramento a ser excluido.
	 */
	@Delete( PATH + "/monitor/{monitorId}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deleteMonitor(@NotNull Long monitorId) {
		try {
			Monitor monitor = this.riskBS.exists(monitorId, Monitor.class);
			if (monitor == null) {
				this.fail("O monitoramento solicitado não foi encontrado.");
				return;
			} 
			
			riskBS.delete(monitor);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Exclui incidente.
	 * 
	 * @param id
	 *            Id do incidente a ser excluido.
	 */
	@Delete( PATH + "/incident/{incidentId}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deleteIncident(@NotNull Long incidentId) {
		try {
			Incident incident = this.riskBS.exists(incidentId, Incident.class);
			if (incident == null) {
				this.fail("O incidente solicitado não foi encontrado.");
				return;
			} 
			
			riskBS.delete(incident);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Exclui contingenciamento.
	 * 
	 * @param id
	 *            Id do contingenciamento a ser excluido.
	 */
	@Delete( PATH + "/contingency/{contingencyId}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deleteContigency(@NotNull Long contingencyId) {
		try {
			Contingency contingency = this.riskBS.exists(contingencyId, Contingency.class);
			if (contingency == null) {
				this.fail("O contingenciamento solicitado não foi encontrado.");
				return;
			} 
			
			riskBS.delete(contingency);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
}