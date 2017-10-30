package org.forpdi.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.dashboard.admin.GeneralBudgets;
import org.forpdi.dashboard.admin.GoalsInfoTable;
import org.forpdi.dashboard.admin.PlanDetails;
import org.forpdi.dashboard.manager.IndicatorHistory;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.fields.FieldsBS;
import org.forpdi.planning.fields.budget.Budget;
import org.forpdi.planning.fields.budget.BudgetDTO;
import org.forpdi.planning.filters.PeformanceFilterType;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@Controller
public class DashboardController extends AbstractController {

	@Inject
	private StructureBS sbs;
	@Inject
	private PlanBS planBS;
	@Inject
	private DashboardBS bs;
	@Inject
	private FieldsBS fbs;
	@Inject
	private AttributeHelper attrHelper;
	@Inject
	private StructureHelper structHelper;

	/**
	 * Usuário recupera informações sobre os objetivos
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @return info informações sobre os objetivos
	 * 
	 */
	@Get(BASEPATH + "/dashboard/admin/goals")
	@NoCache
	@Permissioned
	public void adminGoalsInfo(Long macro, Long plan) {
		try {
			PaginatedList<StructureLevelInstance> list = this.sbs.listGoals(this.planBS.retrievePlanMacroById(macro),
					this.planBS.retrieveById(plan), null);
			this.success(this.bs.retrieveAdminGoalsInfo(list.getList()));
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera eixo estratégico
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @return plan2 Plano contendo a lista de eixos
	 * 
	 */
	@Get(BASEPATH + "/dashboard/admin/performanceStrategicAxis")
	@NoCache
	@Permissioned
	public void performanceStrategicAxis(Long macro, Long plan, Integer page, Integer pageSize) {
		try {
			PaginatedList<StructureLevelInstance> list;

			if (plan == null && macro == null) {
				list = this.sbs.listStrategicAxis(null, null, page, pageSize);
			} else if (plan == null) {
				PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
				list = this.sbs.listStrategicAxis(planMacro, null, page, pageSize);
			} else {
				list = this.sbs.listStrategicAxis(null, this.planBS.retrieveById(plan), page, pageSize);
			}
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera objetivos a partir de eixo temático
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param thematicAxis
	 *            ID do eixo temático a ter os dados recuperados
	 * @return plan2 Plano contendo a lista de eixos
	 * 
	 */
	@Get(BASEPATH + "/dashboard/admin/objectivesByThematicAxis")
	@NoCache
	@Permissioned
	public void objectivesByThematicAxis(Long macro, Long plan, Long thematicAxis) {
		try {
			PaginatedList<StructureLevelInstance> list;
			Plan plan2 = new Plan();
			StructureLevelInstance thematicAxisLevel = this.sbs.retrieveLevelInstance(thematicAxis);

			if (plan == null && macro == null) {
				list = this.sbs.listObjectivesByThematicAxis(null, null, thematicAxisLevel);
			} else if (plan == null) {
				PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
				list = this.sbs.listObjectivesByThematicAxis(planMacro, null, thematicAxisLevel);
			} else {
				list = this.sbs.listObjectivesByThematicAxis(null, this.planBS.retrieveById(plan), thematicAxisLevel);
			}
			plan2.setLevelInstances(list.getList());
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera detalhes, como número de objetivos e metas, de um plano
	 * de metas
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @return planDetails detalhes recuperados
	 * 
	 */
	@Get(BASEPATH + "/dashboard/admin/planDetails")
	@NoCache
	@Permissioned
	public void adminPlanDetails(Long macro, Long plan) {
		try {
			PlanDetails planDetails = new PlanDetails();
			PaginatedList<StructureLevelInstance> goalList;
			PaginatedList<StructureLevelInstance> indicatorsList;
			PaginatedList<StructureLevelInstance> objectivesList;
			PaginatedList<Budget> budgetList;
			PlanMacro planMacro = null;
			Plan plan2 = null;
			if (plan == null && macro == null) {
				indicatorsList = this.sbs.listAllIndicators();
			} else if (plan == null) {
				planMacro = this.planBS.retrievePlanMacroById(macro);
				indicatorsList = this.sbs.listIndicators(planMacro);
			} else {
				plan2 = this.planBS.retrieveById(plan);
				indicatorsList = this.sbs.listIndicators(plan2);
			}

			goalList = this.sbs.listGoals(this.planBS.retrievePlanMacroById(macro), this.planBS.retrieveById(plan),
					null);
			objectivesList = this.sbs.listObjective(planMacro, plan2);
			budgetList = this.sbs.listBudgets(planMacro, plan2);
			if (goalList.getTotal() > 0) {
				Double goalsLatePercent = ((double) this.bs.getNumberOfGoalsLate(goalList.getList()) * 100)
						/ goalList.getTotal();
				planDetails.setGoalsDelayedPerCent(goalsLatePercent);
			} else {
				planDetails.setGoalsDelayedPerCent(0.0);
			}
			planDetails.setNumberOfBudgets(budgetList.getTotal().intValue());
			planDetails.setNumberOfObjectives(objectivesList.getTotal().intValue());
			planDetails.setNumberOfIndicators(indicatorsList.getTotal().intValue());
			planDetails.setNumberOfGoals(goalList.getTotal().intValue());
			this.success(planDetails);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());

		}
	}

	
	/**
	 * Usuário recupera lista de orçamentos
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param objective
	 *            ID do objetivo a ter os dados recuperados
	 * @param subAction
	 *            ação orçamentária desejada
	 * @return budgets lista de orçamentos recuperados
	 * 
	 */
		@Get(BASEPATH + "/dashboard/admin/budget")
		@NoCache
		@Permissioned
	public void listBudgets(Long macro, Long plan, Long objective, String subAction) {
		try {
			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			Plan plan2 = this.planBS.retrieveById(plan);
			StructureLevelInstance obj = this.sbs.retrieveLevelInstance(objective);
			List<Budget> list = this.bs.listBudgets(planMacro, plan2, obj, subAction);
			GeneralBudgets budgets = new GeneralBudgets();
			Double realized = 0.0;
			Double committed = 0.0;
			Double planned = 0.0;
			boolean isId = false;
			
			List <Long> listIdBudgetsElement = new ArrayList<Long>(); 
			if (list != null && !list.isEmpty()) {
				listIdBudgetsElement.add(list.get(0).getBudgetElement().getId());
				planned += list.get(0).getBudgetElement().getBudgetLoa();
			}
			
			for (Budget budget : list) {
				if (budget.getCommitted() != null) {
					committed += budget.getCommitted();
				}
				
				if (budget.getRealized() != null) {
					realized += budget.getRealized();
				}
				
				for (Long id : listIdBudgetsElement) {
					if (id != budget.getBudgetElement().getId()) {
						isId = true;
					}	
				}
				
				if (isId) {
					listIdBudgetsElement.add(budget.getBudgetElement().getId());
					planned += budget.getBudgetElement().getBudgetLoa();
				}
				
				isId = false;		
			}
			budgets.setCommitted(committed);
			budgets.setConducted(realized);
			budgets.setPlanned(planned);
			this.success(budgets);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());

		}
	}


	/**
	 * Usuário recupera lista de indicadores e suas informações
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param objective
	 *            ID do objetivo a ter os dados recuperados
	 * @return indicatorsList Lista de indicadores recuperados
	 * 
	 */
	@Get(BASEPATH + "/dashboard/admin/indicatorsInformation")
	@NoCache
	@Permissioned
	public void indicatorsInformation(Long macro, Long plan, Long objective, Integer page, Integer pageSize) {
		try {
			PaginatedList<StructureLevelInstance> indicatorsList;
			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			Plan plan2 = this.planBS.retrieveById(plan);
			StructureLevelInstance objectiveLevel = this.sbs.retrieveLevelInstance(objective);
			indicatorsList = this.sbs.listIndicators(planMacro, plan2, objectiveLevel, page, pageSize);

			for (int i = 0; i < indicatorsList.getList().size(); i++) {
				this.structHelper.fillIndicators(indicatorsList.getList().get(i));
			}

			this.success(indicatorsList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera lista de informações sobre metas, como responsável e
	 * status
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param indicator
	 *            ID do indicador a ter os dados recuperados
	 * @return goalsList Lista com as informações recuperadas
	 * 
	 */
	@Get(BASEPATH + "/dashboard/manager/goalsInfoTable")
	@NoCache
	@Permissioned
	public void goalsInfoTable(Long macro, Long plan, Long indicator, Integer page, Integer pageSize, Integer filter) {
		try {
			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			Plan plan2 = this.planBS.retrieveById(plan);
			StructureLevelInstance indicatorLevel = this.sbs.retrieveLevelInstance(indicator);
			PaginatedList<GoalsInfoTable> goalsList = this.bs.getGoalsInfoTable(planMacro, plan2, indicatorLevel, page,
					pageSize, PeformanceFilterType.valueOf(filter));

			this.success(goalsList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera lista de objetivos e suas informações, como nome e data
	 * de criação
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @return objectivesList Lista com as informações e objetivos recuperados
	 * 
	 */
	@Get(BASEPATH + "/dashboard/admin/objectivesInformation")
	@NoCache
	@Permissioned
	public void objectivesInformation(Long macro, Long plan) {
		try {
			PaginatedList<StructureLevelInstance> objectivesList;

			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			Plan plan2 = this.planBS.retrieveById(plan);
			objectivesList = this.sbs.listObjectives(planMacro, plan2);
			this.success(objectivesList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera lista de metas e informações sobre metas, como nome e
	 * data de criação
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param indicator
	 *            ID do indicador a ter os dados recuperados
	 * @return goalsList Lista com as informações e metas recuperados
	 * 
	 */
	@Get(BASEPATH + "/dashboard/colaborator/goalsInformation")
	@NoCache
	@Permissioned
	public void goalsInformation(Long macro, Long plan, Long indicator, Integer page, Integer pageSize) {
		try {
			PaginatedList<StructureLevelInstance> goalsList;
			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			Plan plan2 = this.planBS.retrieveById(plan);
			StructureLevelInstance indicatorLevel = this.sbs.retrieveLevelInstance(indicator);
			goalsList = this.sbs.listGoals(planMacro, plan2, indicatorLevel, page, pageSize);
			List<StructureLevelInstance> list = new ArrayList<StructureLevelInstance>();
			for (StructureLevelInstance sli : goalsList.getList()) {
				sli.setAttributeInstanceList(new ArrayList<AttributeInstance>());
				sli.setAttributeList(this.sbs.retrieveLevelAttributes(sli.getLevel()));
				AttributeInstance polarity = this.attrHelper.retrievePolarityAttributeInstance(sli.getParent());
				if (polarity == null)
					sli.setPolarity("Maior-melhor");
				else
					sli.setPolarity(polarity.getValue());
				for (Attribute attr : sli.getAttributeList()) {
					AttributeInstance attrInst = this.attrHelper.retrieveAttributeInstance(sli, attr);
					if (attrInst != null)
						sli.getAttributeInstanceList().add(attrInst);
					else
						sli.getAttributeInstanceList().add(new AttributeInstance());
				}
				list.add(sli);
			}
			goalsList.setList(list);
			this.success(goalsList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Recuperação de uma lista com o histórico de um indicador
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param indicator
	 *            ID do indicador a ter os dados recuperados
	 * @return list lista contendo o histórico do indicador requerido
	 * 
	 */
	@Get(BASEPATH + "/dashboard/manager/indicatorsHistory")
	@NoCache
	@Permissioned
	public void listIndicatorsHistory(Long macro, Long plan, Long indicator, Integer page, Integer pageSize) {
		try {
			PaginatedList<IndicatorHistory> list = this.bs.listIndicatorHistory(macro, plan, indicator, page, pageSize);

			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Usuário recupera lista de filhos de uma instancia para o gráfico
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param levelInstance
	 *            ID do nível
	 * @return plan2 Lista com os filhos da instancia requerida
	 * 
	 */
	@Get(BASEPATH + "/dashboard/community/levelsonsgraph")
	@NoCache
	public void listLevelSonsForGraph(Long macro, Long plan, Long levelInstance, Integer page, Integer pageSize) {
		try {
			PaginatedList<StructureLevelInstance> list;
			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			if (plan == null)
				list = this.sbs.retrieveLevelSonsForGraph(planMacro, null, null, page, pageSize);
			else
				list = this.sbs.retrieveLevelSonsForGraph(null, this.planBS.retrieveById(plan), levelInstance, page,
						pageSize);

			if (list.getTotal() > 0) {
				for (int i = 0; i < list.getList().size(); i++) {
					this.structHelper.fillIndicators(list.getList().get(i));
				}
			}

			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Recuperação de metas e informações para o dashboard da comunidade
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @param levelInstance
	 *            ID do nível
	 * @return goalsList Lista com as metas recuperadas
	 * 
	 */
	@Get(BASEPATH + "/dashboard/community/communityinfotable")
	@NoCache
	public void communityInfoTable(Long macro, Long plan, Long levelInstance, Integer page, Integer pageSize) {
		try {
			PaginatedList<GoalsInfoTable> goalsList;
			PlanMacro planMacro = this.planBS.retrievePlanMacroById(macro);
			if (plan == null)
				goalsList = this.bs.getCommunityInfoTable(planMacro, null, null, page, pageSize);
			else
				goalsList = this.bs.getCommunityInfoTable(null, this.planBS.retrieveById(plan), levelInstance, page,
						pageSize);

			this.success(goalsList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Recuperação de detalhes sobre um plano de metas
	 * 
	 * @param macro
	 *            ID do plano macro a ter os dados recuperados
	 * @param plan
	 *            ID do plano de metas a ter os dados recuperados
	 * @return planDetails dados do plano de metas recuperado, como número de
	 *         indicadores e objetivos
	 * 
	 */
	@Get(BASEPATH + "/dashboard/community/planDetails")
	@NoCache
	public void communityPlanDetails(Long macro, Long plan) {
		try {
			PlanDetails planDetails = new PlanDetails();
			PaginatedList<StructureLevelInstance> goalList;
			PaginatedList<StructureLevelInstance> indicatorsList;
			PaginatedList<StructureLevelInstance> objectivesList;
			PaginatedList<StructureLevelInstance> thematicAxisList;
			PaginatedList<Budget> budgetList;
			PlanMacro planMacro = null;
			Plan plan2 = null;
			if (plan == null && macro == null) {
				indicatorsList = this.sbs.listAllIndicators();
			} else if (plan == null) {
				planMacro = this.planBS.retrievePlanMacroById(macro);
				indicatorsList = this.sbs.listIndicators(planMacro);
			} else {
				plan2 = this.planBS.retrieveById(plan);
				indicatorsList = this.sbs.listIndicators(plan2);
			}

			goalList = this.sbs.listGoalsWithoutResponsible(this.planBS.retrievePlanMacroById(macro),
					this.planBS.retrieveById(plan), null);
			objectivesList = this.sbs.listObjectiveWithoutResponsible(planMacro, plan2);
			thematicAxisList = this.sbs.listStrategicAxis(planMacro, plan2);

			budgetList = this.sbs.listBudgets(planMacro, plan2);
			if (goalList.getTotal() > 0) {
				Double goalsReachedPercent = ((double) this.bs.getNumberOfGoalsReached(goalList.getList()) * 100)
						/ goalList.getTotal();
				planDetails.setGoalsDelayedPerCent(goalsReachedPercent);
			} else {
				planDetails.setGoalsDelayedPerCent(0.0);
			}
			planDetails.setNumberOfBudgets(budgetList.getTotal().intValue());
			planDetails.setNumberOfObjectives(objectivesList.getTotal().intValue());
			planDetails.setNumberOfIndicators(indicatorsList.getTotal().intValue());
			planDetails.setNumberOfGoals(goalList.getTotal().intValue());
			planDetails.setNumberOfThematicAxis(thematicAxisList.getTotal().intValue());

			this.success(planDetails);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());

		}
	}

	/**
	 * Usuário recupera lista de metas e informações sobre metas, como nome e
	 * data de criação para o gráfico no indicador
	 * 
	 * @param indicator
	 *            ID do indicador a ter os dados recuperados
	 * @return goalsList Lista com as informações e metas recuperados
	 * 
	 */
	@Get(BASEPATH + "/dashboard/graphforindicator")
	@NoCache
	@Permissioned
	public void graphForIndicator(Long indicator, Integer page, Integer pageSize) {
		try {
			PaginatedList<StructureLevelInstance> goalsList;
			StructureLevelInstance indicatorLevel = this.sbs.retrieveLevelInstance(indicator);
			goalsList = this.sbs.listGoalsByIndicatorWithoutResponsible(indicatorLevel, page, pageSize);
			List<StructureLevelInstance> list = new ArrayList<StructureLevelInstance>();
			for (StructureLevelInstance sli : goalsList.getList()) {
				sli.setAttributeInstanceList(new ArrayList<AttributeInstance>());
				sli.setAttributeList(this.sbs.retrieveLevelAttributes(sli.getLevel()));
				AttributeInstance polarity = this.attrHelper.retrievePolarityAttributeInstance(sli.getParent());
				if (polarity == null)
					sli.setPolarity("Maior-melhor");
				else
					sli.setPolarity(polarity.getValue());
				for (Attribute attr : sli.getAttributeList()) {
					AttributeInstance attrInst = this.attrHelper.retrieveAttributeInstance(sli, attr);
					if (attrInst != null)
						sli.getAttributeInstanceList().add(attrInst);
					else
						sli.getAttributeInstanceList().add(new AttributeInstance());
				}
				list.add(sli);
			}
			goalsList.setList(list);
			this.success(goalsList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

}
