package org.forpdi.dashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.auth.UserSession;
import org.forpdi.dashboard.admin.GoalsInfo;
import org.forpdi.dashboard.admin.GoalsInfoTable;
import org.forpdi.dashboard.manager.IndicatorHistory;
import org.forpdi.dashboard.manager.LevelInstanceHistory;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.types.ResponsibleField;
import org.forpdi.planning.attribute.types.enums.Periodicity;
import org.forpdi.planning.fields.budget.Budget;
import org.forpdi.planning.filters.PeformanceFilter;
import org.forpdi.planning.filters.PeformanceFilterType;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forpdi.system.CriteriaCompanyFilter;
import org.forpdi.system.factory.ApplicationSetup;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.sql.JoinType;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@RequestScoped
public class DashboardBS extends HibernateBusiness {

	@Inject
	private UserSession userSession;
	@Inject
	private StructureBS sbs;
	@Inject
	private UserBS usrbs;
	@Inject
	private PlanBS pbs;
	@Inject
	private AttributeHelper attrHelper;
	@Inject
	private StructureHelper structHelper;
	@Inject
	private CriteriaCompanyFilter filter;
	@Inject
	private PeformanceFilter peformanceFilter;

	private final Integer PAGESIZE = 5;

	/**
	 * Calcular informações gerais (Em dia, Atrasados, Próximos a vencer, Não
	 * iniciados, Abaixo do mínimo, Abaixo do esperado, Suficiente, Acima do
	 * máximo) das metas.
	 * 
	 * @param goals
	 *            Lista de metas para ser calculado as informações gerais.
	 * @return Informações gerais sobre as metas.
	 */
	public GoalsInfo retrieveAdminGoalsInfo(List<StructureLevelInstance> goals) {
		GoalsInfo info = new GoalsInfo();

		if (goals.size() > 0) {
			Integer inDay = 0;
			Integer late = 0;
			Integer belowMin = 0;
			Integer belowExp = 0;
			Integer reached = 0;
			Integer aboveExp = 0;
			Integer notStarted = 0;
			Integer finished = 0;
			Integer closeToMat = 0;
			for (StructureLevelInstance goal : goals) {
				List<AttributeInstance> attrInstances = this.sbs.listAttributeInstanceByLevel(goal,false);
				Date finish = new Date();
				Double expected = null;
				Double reach = null;
				Double max = null;
				Double min = null;
				for (AttributeInstance attrInstance : attrInstances) {
					Attribute attr = this.sbs.retrieveAttribute(attrInstance.getAttribute().getId());
					if (attr.isFinishDate()) {
						finish = attrInstance.getValueAsDate();
					} else if (attr.isExpectedField()) {
						expected = attrInstance.getValueAsNumber();
					} else if (attr.isReachedField()) {
						reach = attrInstance.getValueAsNumber();
					} else if (attr.isMinimumField()) {
						min = attrInstance.getValueAsNumber();
					} else if (attr.isMaximumField()) {
						max = attrInstance.getValueAsNumber();
					}
				}
				AttributeInstance polarity = this.attrHelper.retrievePolarityAttributeInstance(goal.getParent());
				Date today = new Date();
				if (reach == null && ((min == null && expected == null && max == null) || (finish.after(today)))) {
					notStarted++;
				} else {
					if (goal.isClosed()) {
						finished++;
					} else if (finish.before(today)) {
						late++;
					} else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(today);
						calendar.add(Calendar.DATE, 10);
						if (calendar.getTime().after(finish) || DateUtils.isSameDay(calendar.getTime(), finish)) {
							closeToMat++;
						} else {
							inDay++;
						}
					}
					if (this.polarityComparison(polarity, min, reach) || (reach == null && finish.before(today))) {
						belowMin++;
					} else if (this.polarityComparison(polarity, expected, reach)) {
						belowExp++;
					} else if (this.polarityComparison(polarity, max, reach)
							|| (reach != null && expected != null && (Double.compare(reach, expected) == 0))) {
						reached++;
					} else if (this.polarityComparison(polarity, reach, max)
							|| (reach != null && max != null && (Double.compare(reach, max) == 0))) {
						aboveExp++;
					}
				}
			}
			info.setInDay(inDay);
			info.setInDayPercentage((double) inDay * 100 / goals.size());
			info.setLate(late);
			info.setLatePercentage((double) late * 100 / goals.size());
			info.setBelowMininum(belowMin);
			info.setBelowMinimumPercentage((double) belowMin * 100 / goals.size());
			info.setBelowExpected(belowExp);
			info.setBelowExpectedPercentage((double) belowExp * 100 / goals.size());
			info.setReached(reached);
			info.setReachedPercentage((double) reached * 100 / goals.size());
			info.setAboveExpected(aboveExp);
			info.setAboveExpectedPercentage((double) aboveExp * 100 / goals.size());
			info.setNotStarted(notStarted);
			info.setNotStartedPercentage((double) notStarted * 100 / goals.size());
			info.setFinished(finished);
			info.setFinishedPercentage((double) finished * 100 / goals.size());
			info.setCloseToMaturity(closeToMat);
			info.setCloseToMaturityPercentage((double) closeToMat * 100 / goals.size());
		}

		return info;
	}

	/**
	 * Comparar a polaridade do indicador.
	 * 
	 * @param polarity
	 *            Polaridade a ser comparada (Maior - Melhor ou Menor - Melhor).
	 * @param x
	 *            Valor da polaridade que será comparado.
	 * @param y
	 *            Valor da polaridade que será comparado.
	 * @return Boolean true - Polaridade (Maior-melhor). false - Polaridade
	 *         (Menor-melhor).
	 */
	public Boolean polarityComparison(AttributeInstance polarity, Double x, Double y) {
		if (x == null || y == null)
			return false;

		if (polarity == null || polarity.getValue().equals("Maior-melhor")) {
			return x > y;
		} else if (polarity.getValue().equals("Menor-melhor")) {
			return x < y;
		}

		return null;
	}

	/**
	 * Calcular número de metas atrasadas.
	 * 
	 * @param goals
	 *            Metas.
	 * @return Integer Número de metas atrasadas.
	 */
	public synchronized Integer getNumberOfGoalsLate(List<StructureLevelInstance> goals) {
		GoalsInfo info = this.retrieveAdminGoalsInfo(goals);
		return info.getLate();
	}

	/**
	 * Calcular número de metas alcançadas.
	 * 
	 * @param goals
	 *            Metas.
	 * @return Integer Número de metas alcançadas.
	 */
	public Integer getNumberOfGoalsReached(List<StructureLevelInstance> goals) {
		GoalsInfo info = this.retrieveAdminGoalsInfo(goals);

		return (info.getReached() + info.getAboveExpected());

	}

	/**
	 * Retorna lista de orçamentos.
	 * 
	 * @param macro
	 *            Plano macro no qual será pesquisado a lista de orçamentos.
	 * @param plan
	 *            Plano no qual será pesquisado a lista de orçamentos.
	 * @param obj
	 *            Objetivos no qual será pesquisado a lista de orçamentos.
	 * @param subAction
	 *            Sub Ação Orçamentária no qual será pesquisado a lista de
	 *            orçamentos.
	 * @return List<Budget> Lista de orçamentos
	 */
	public List<Budget> listBudgets(PlanMacro macro, Plan plan, StructureLevelInstance obj, String subAction) {
		Criteria criteria = this.dao.newCriteria(Budget.class);
		criteria.createAlias("levelInstance", "levelInstance", JoinType.INNER_JOIN);
		criteria.createAlias("levelInstance.plan", "plan", JoinType.INNER_JOIN);
		criteria.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("macro.archived", false));
		criteria.add(Restrictions.eq("deleted", false));

		if (obj != null) {
			criteria.add(Restrictions.eq("levelInstance", obj));
			if (subAction != null) {
				criteria.add(Restrictions.eq("subAction", subAction));
			}
		} else if (plan != null) {
			criteria.add(Restrictions.eq("levelInstance.plan", plan));
		} else if (macro != null) {
			criteria.add(Restrictions.eq("plan.parent", macro));
		}

		List<Budget> budgets = this.filter.filterAndList(criteria, Budget.class, "macro.company");

		if (this.userSession.getAccessLevel() < 50) {
			List<Budget> list2 = new ArrayList<Budget>();
			for (Budget bud : budgets) {
				boolean lvlAdd = false;
				List<StructureLevelInstance> stLvInstList = this.sbs
						.retrieveLevelInstanceSons(bud.getLevelInstance().getId());
				for (StructureLevelInstance stLvInst : stLvInstList) {
					List<Attribute> attributeList = this.sbs.retrieveLevelAttributes(stLvInst.getLevel());
					for (Attribute attr : attributeList) {
						if (attr.getType().equals(ResponsibleField.class.getCanonicalName())) {
							AttributeInstance attrInst = this.attrHelper.retrieveAttributeInstance(stLvInst, attr);
							if (attrInst != null) {
								if (!lvlAdd
										&& Long.parseLong(attrInst.getValue()) == this.userSession.getUser().getId()) {
									list2.add(bud);
									lvlAdd = true;
								}
							}
						}
					}
				}

				StructureLevelInstance lvlI = bud.getLevelInstance();
				List<Attribute> attributeList = this.sbs.retrieveLevelAttributes(lvlI.getLevel());
				for (Attribute attr : attributeList) {
					if (attr.getType().equals(ResponsibleField.class.getCanonicalName())) {
						AttributeInstance attrInst = this.attrHelper.retrieveAttributeInstance(lvlI, attr);
						if (attrInst != null) {
							if (!lvlAdd && Long.parseLong(attrInst.getValue()) == this.userSession.getUser().getId()) {
								list2.add(bud);
								lvlAdd = true;
							}
						}
					}
				}
				while (lvlI.getParent() != null && !lvlAdd) {
					lvlI = this.sbs.retrieveLevelInstance(lvlI.getParent());
					attributeList = this.sbs.retrieveLevelAttributes(lvlI.getLevel());
					for (Attribute attr : attributeList) {
						if (attr.getType().equals(ResponsibleField.class.getCanonicalName())) {
							AttributeInstance attrInst = this.attrHelper.retrieveAttributeInstance(lvlI, attr);
							if (attrInst != null) {
								if (!lvlAdd
										&& Long.parseLong(attrInst.getValue()) == this.userSession.getUser().getId()) {
									list2.add(bud);
									lvlAdd = true;
								}
							}
						}
					}
				}
			}
			budgets = list2;
		}

		return budgets;
	}

	/**
	 * Salvar histórico do indicador.
	 * 
	 * @param instance
	 *            Indicador para salvar o histórico.
	 * @param periodicity
	 *            Periocidade (Tempo para salvar histórico do indicador).
	 * @return void.
	 */
	public void saveIndicatorHistory(StructureLevelInstance instance, Periodicity periodicity) {
		if (instance.getNextSave() != null) {
			LevelInstanceHistory history = new LevelInstanceHistory();
			history.setCreation(new Date());
			history.setDeleted(false);
			history.setLevelInstance(instance);
			if (instance.getLevelValue() == null)
				history.setValue(0.0);
			else
				history.setValue(instance.getLevelValue());
			this.dao.persist(history);
		}
		Calendar calendar = Calendar.getInstance();
		if (instance.getNextSave() == null)
			calendar.setTime(new Date());
		else
			calendar.setTime(instance.getNextSave());

		switch (periodicity.getValue()) {
		case 1:
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			break;
		case 2:
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			break;
		case 3:
			calendar.add(Calendar.WEEK_OF_YEAR, 2);
			break;
		case 4:
			calendar.add(Calendar.MONTH, 1);
			break;
		case 5:
			calendar.add(Calendar.MONTH, 2);
			break;
		case 6:
			calendar.add(Calendar.MONTH, 3);
			break;
		case 7:
			calendar.add(Calendar.MONTH, 6);
			break;
		case 8:
			calendar.add(Calendar.YEAR, 1);
			break;
		case 9:
			calendar.add(Calendar.YEAR, 2);
			break;
		}

		instance.setNextSave(calendar.getTime());
		this.dao.persist(instance);
	}

	/**
	 * Cria uma criteria com uma query para filtrar se o usuário da sessão é o
	 * responsável pelos níveis.
	 * 
	 * @return criteria
	 */
	public Criteria filterByResponsibleCriteria() {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class);
		criteria.createAlias("levelInstance", "levelInstance", JoinType.RIGHT_OUTER_JOIN);
		criteria.createAlias("attribute", "attribute", JoinType.INNER_JOIN);
		criteria.createAlias("levelInstance.level", "level", JoinType.INNER_JOIN);
		criteria.createAlias("levelInstance.plan", "plan", JoinType.INNER_JOIN);
		criteria.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("levelInstance.deleted", false));
		criteria.add(Restrictions.eq("attribute.type", ResponsibleField.class.getCanonicalName()));
		criteria.add(Restrictions.eq("value", this.userSession.getUser().getId().toString()));

		return criteria;
	}

	/**
	 * Retorna uma lista dos IDs dos níveis os quais ele é responsável e seus
	 * filhos
	 * 
	 * @return allIds, lista de IDs dos níveis
	 */
	public List<Long> retrieveChildResponsibleIds() {
		Criteria responsible = this.filterByResponsibleCriteria();
		responsible.setProjection(Projections.property("levelInstance.id"));
		List<Long> ids = this.dao.findByCriteria(responsible, Long.class);
		
		/*Disjunction or = Restrictions.disjunction();
		for (Long id : ids) {
			or.add(Restrictions.eq("parent", id));
		}*///child.add(or);
		
		if(ids.isEmpty()) {
			return new ArrayList<>();
		}
		
		Criteria child = this.dao.newCriteria(StructureLevelInstance.class);
		child.add(Restrictions.in("parent", ids));
		child.setProjection(Projections.property("id"));
		
		List<Long> ids2 = this.dao.findByCriteria(child, Long.class);
		//or = Restrictions.disjunction();
		/*for (Long id : ids2) {
			or.add(Restrictions.eq("parent", id));
		}*///child2.add(or);
	
		
		Criteria child2 = this.dao.newCriteria(StructureLevelInstance.class);
		child2.add(Restrictions.in("parent", ids2));
		child2.setProjection(Projections.property("id"));
		
		/*or = Restrictions.disjunction();
		for (Long id : ids3) {
			or.add(Restrictions.eq("parent", id));
		}*///child3.add(or);
		
		List<Long> ids3 = this.dao.findByCriteria(child2, Long.class);
		Criteria child3 = this.dao.newCriteria(StructureLevelInstance.class);		
		child3.add(Restrictions.in("parent", ids3));
		child3.setProjection(Projections.property("id"));
		
		List<Long> ids4 = this.dao.findByCriteria(child3, Long.class);

		List<Long> allIds = new ArrayList<>();
		allIds.addAll(ids);
		allIds.addAll(ids2);
		allIds.addAll(ids3);
		allIds.addAll(ids4);

		return allIds;
	}

	/**
	 * Listar todas as metas pertencentes a um indicador.
	 * 
	 * @param macro
	 *            Plano Macro para buscar as metas.
	 * @param plan
	 *            Plano de Metas para buscar as metas.
	 * @param indic
	 *            Indicador para buscar as metas.
	 * @return PaginatedList<GoalsInfoTable> Lista de Metas.
	 * @throws ParseException
	 */
	public PaginatedList<GoalsInfoTable> getGoalsInfoTable(PlanMacro macro, Plan plan, StructureLevelInstance indic,
			Integer page, Integer pageSize, PeformanceFilterType type) throws ParseException {
		if (page == null || page < 1) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}

		this.peformanceFilter.setType(type);
		List<Long> allIds = this.retrieveChildResponsibleIds();
		allIds = this.peformanceFilter.depurate(allIds);
		if (allIds.isEmpty()) {
			PaginatedList<GoalsInfoTable> result = new PaginatedList<>();
			result.setTotal(0L);
			return result;
		}

		Criteria criteria = this.dao.newCriteria(StructureLevelInstance.class);
		criteria.createAlias("level", "level", JoinType.INNER_JOIN);
		criteria.createAlias("plan", "plan", JoinType.INNER_JOIN);
		criteria.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("level.goal", true));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("macro.archived", false));
		Disjunction or = Restrictions.disjunction();
		for (Long id : allIds) {
			or.add(Restrictions.eq("id", id));
		}
		criteria.add(or);
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);

		Criteria counting = this.dao.newCriteria(StructureLevelInstance.class);
		counting.setProjection(Projections.countDistinct("id"));
		counting.createAlias("level", "level", JoinType.INNER_JOIN);
		counting.createAlias("plan", "plan", JoinType.INNER_JOIN);
		counting.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		counting.add(Restrictions.eq("level.goal", true));
		counting.add(Restrictions.eq("deleted", false));
		counting.add(Restrictions.eq("macro.archived", false));
		or = Restrictions.disjunction();
		for (Long id : allIds) {
			or.add(Restrictions.eq("id", id));
		}
		counting.add(or);
		if (plan != null) {
			criteria.add(Restrictions.eq("plan", plan));
			counting.add(Restrictions.eq("plan", plan));
		} else if (macro != null) {
			criteria.add(Restrictions.eq("plan.parent", macro));
			counting.add(Restrictions.eq("plan.parent", macro));
		}
		if (indic != null) {
			criteria.add(Restrictions.eq("parent", indic.getId()));
			counting.add(Restrictions.eq("parent", indic.getId()));
		}

		PaginatedList<GoalsInfoTable> result = new PaginatedList<>();
		List<StructureLevelInstance> levelInstances = this.filter.filterAndList(criteria, StructureLevelInstance.class,
				"macro.company");
		Long total = this.filter.filterAndFind(counting, "macro.company");
		for (int i = 0; i < levelInstances.size(); i++) {
			levelInstances.get(i).getLevel().setAttributes(this.sbs.retrieveLevelSonsAttributes(levelInstances.get(i)));
		}

		ArrayList<GoalsInfoTable> goalsInfoTable = new ArrayList<GoalsInfoTable>();

		for (StructureLevelInstance s : levelInstances) {
			List<Attribute> attributeList = this.sbs.retrieveLevelAttributes(s.getLevel());
			attributeList = this.sbs.setAttributesInstances(s, attributeList);
			s.getLevel().setAttributes(attributeList);
			GoalsInfoTable goalAux = new GoalsInfoTable();

			StructureLevelInstance indicator = this.structHelper.retrieveLevelInstance(s.getParent());

			goalAux.setIndicatorName(indicator.getName());
			AttributeInstance attributeInstance = this.attrHelper.retrievePolarityAttributeInstance(indicator);
			String polarity = null;
			if (attributeInstance != null)
				polarity = attributeInstance.getValue();
			this.sbs.setGoalStatus(levelInstances, polarity);

			StructureLevelInstance objective = this.sbs.retrieveLevelInstance(indicator.getParent());
			goalAux.setObjectiveName(objective.getName());
			goalAux.setGoalName(s.getName());

			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String dataString = fmt.format(s.getModification());
			goalAux.setLastModification(dataString);

			goalAux.setDeadLineStatus(s.getDeadlineStatus());
			goalAux.setProgressStatus(s.getProgressStatus());

			/*
			 * AttributeInstance attrInst = null;
			 * 
			 * for (Attribute atttr : attributeList) { if
			 * (atttr.getType().equals(ResponsibleField.class.getCanonicalName()
			 * )) { attrInst = this.sbs.retrieveAttributeInstance(s, atttr); } }
			 * if (attrInst != null) { User user =
			 * usrbs.existsByUser(Long.parseLong(attrInst.getValue()));
			 * goalAux.setResponsible(user.getName()); }
			 */
			// LOGGER.info(s.getName()+ " | DeadlineStatus:
			// "+s.getDeadlineStatus());
			// LOGGER.info("GoalsInfoTable: " + goalAux.toString());
			for (AttributeInstance a : this.sbs.listAttributeInstanceByLevel(s,false)) {
				Attribute attr = this.sbs.retrieveAttribute(a.getAttribute().getId());
				if (attr.getType().equals(ResponsibleField.class.getCanonicalName())) {
					User user = usrbs.existsByUser(Long.parseLong(a.getValue()));
					goalAux.setResponsible(user.getName());
					goalAux.setIdResponsible(user.getId());
				} else if (attr.isFinishDate()) {
					goalAux.setFinishDate(a.getValueAsDate());
					Date today = new Date();

					Long diferenca = goalAux.getFinishDate().getTime() - today.getTime();
					diferenca = ((diferenca / 1000) / 60 / 60 / 24);

					if (diferenca >= 0 || s.isClosed()) {
						goalAux.setGoalStatus("Em dia.");
					} else {

						if (((-1) * diferenca) <= 1) {
							goalAux.setGoalStatus("Atraso em " + ((-1) * diferenca) + " dia.");
						} else {
							goalAux.setGoalStatus("Atraso em " + ((-1) * diferenca) + " dias.");
						}
					}
				} else if (attr.isReachedField()) {
					goalAux.setReached(a.getValueAsNumber());
				} else if (attr.isExpectedField()) {
					goalAux.setExpected(a.getValueAsNumber());
				}
			}
			goalsInfoTable.add(goalAux);
		}

		result.setList(goalsInfoTable);
		result.setTotal(total);

		return result;
	}

	/**
	 * Listar histórico dos indicadores.
	 * 
	 * @param macro
	 *            Id do plano macro para listar os indicadores.
	 * @param plan
	 *            Id do plano para listar os indicadores.
	 * @param indicator
	 *            Id do indicador.
	 * @return List<IndicatorHistory> Lista do histórico dos indicadores.
	 */
	public PaginatedList<IndicatorHistory> listIndicatorHistory(Long macro, Long plan, Long indicator, Integer page,
			Integer pageSize) {
		if (page == null || page <= 0) {
			page = 1;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = PAGESIZE;
		}
		List<IndicatorHistory> list = new ArrayList<>();
		Criteria criteria = this.dao.newCriteria(LevelInstanceHistory.class);
		criteria.createAlias("levelInstance", "levelInstance", JoinType.INNER_JOIN);
		criteria.createAlias("levelInstance.plan", "plan", JoinType.INNER_JOIN);
		criteria.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("deleted", false));
		Criteria counting = this.dao.newCriteria(LevelInstanceHistory.class);
		counting.createAlias("levelInstance", "levelInstance", JoinType.INNER_JOIN);
		counting.createAlias("levelInstance.plan", "plan", JoinType.INNER_JOIN);
		counting.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		counting.add(Restrictions.eq("deleted", false));
		counting.setProjection(Projections.countDistinct("id"));
		if (indicator != null) {
			StructureLevelInstance instance = this.sbs.retrieveLevelInstance(indicator);
			criteria.add(Restrictions.eq("levelInstance", instance));
			criteria.setFirstResult((page - 1) * pageSize);
			criteria.setMaxResults(pageSize);
			counting.add(Restrictions.eq("levelInstance", instance));
		} else if (plan != null) {
			Plan plan2 = this.pbs.retrieveById(plan);
			criteria.add(Restrictions.eq("levelInstance.plan", plan2));
		} else if (macro != null) {
			PlanMacro macro2 = this.pbs.retrievePlanMacroById(macro);
			criteria.add(Restrictions.eq("plan.parent", macro2));
		}

		List<LevelInstanceHistory> historyList = this.filter.filterAndList(criteria, LevelInstanceHistory.class,
				"macro.company");
		Long total = this.filter.filterAndFind(counting, "macro.company");

		if (indicator == null) {
			Map<String, ArrayList<Double>> map = new HashMap<>();
			Calendar calendar = Calendar.getInstance();
			for (LevelInstanceHistory history : historyList) {
				calendar.setTime(history.getCreation());
				if (map.get(String.valueOf(calendar.get(Calendar.YEAR))) == null) {
					map.put(String.valueOf(calendar.get(Calendar.YEAR)), new ArrayList<Double>());
				}
				map.get(String.valueOf(calendar.get(Calendar.YEAR))).add(history.getValue());
			}

			for (Map.Entry<String, ArrayList<Double>> entry : map.entrySet()) {
				IndicatorHistory indicatorHistory = new IndicatorHistory();
				indicatorHistory.setPeriod(entry.getKey());
				Double value = 0.0;
				for (Double val : entry.getValue()) {
					value += val;
				}
				indicatorHistory.setValue(value / entry.getValue().size());
				list.add(indicatorHistory);
			}
			Collections.sort(list);
		} else {
			for (LevelInstanceHistory history : historyList) {
				IndicatorHistory indicatorHistory = new IndicatorHistory();
				indicatorHistory.setPeriod(new SimpleDateFormat("dd/MM/yyyy").format(history.getCreation()));
				indicatorHistory.setValue(history.getValue());
				list.add(indicatorHistory);
			}
		}

		PaginatedList<IndicatorHistory> result = new PaginatedList<>();
		result.setList(list);
		if (indicator == null) {
			result.setTotal((long) list.size());
		} else {
			result.setTotal(total);
		}
		return result;
	}

	/**
	 * Cria uma criteria para pegar os filhos de um dado nível
	 * 
	 * @param levelInstance
	 * @return Criteria
	 */
	public Criteria filterByInheritedParent(StructureLevelInstance levelInstance) {
		Criteria criteria = this.dao.newCriteria(StructureLevelInstance.class);
		criteria.add(Restrictions.eq("parent", levelInstance.getId()));
		if (levelInstance.getLevel().isIndicator()) {
			return criteria;
		}

		criteria.setProjection(Projections.property("id"));
		List<Long> ids = this.dao.findByCriteria(criteria, Long.class);
		Disjunction or = Restrictions.disjunction();
		for (Long id : ids) {
			or.add(Restrictions.eq("parent", id));
		}
		Criteria criteria2 = this.dao.newCriteria(StructureLevelInstance.class);
		or.add(Restrictions.eq("parent", levelInstance.getId()));
		criteria2.add(or);
		if (levelInstance.getLevel().isObjective()) {
			return criteria2;
		}

		criteria2.setProjection(Projections.property("id"));
		List<Long> ids2 = this.dao.findByCriteria(criteria2, Long.class);
		Disjunction or2 = Restrictions.disjunction();
		for (Long id : ids2) {
			or2.add(Restrictions.eq("parent", id));
		}
		Criteria criteria3 = this.dao.newCriteria(StructureLevelInstance.class);
		or2.add(or);
		criteria3.add(or2);

		return criteria3;

	}

	/**
	 * Listar todas as metas pertencentes a um indicador.
	 * 
	 * @param macro
	 *            Plano Macro para buscar as metas.
	 * @param plan
	 *            Plano de Metas para buscar as metas.
	 * @param indic
	 *            Indicador para buscar as metas.
	 * @return PaginatedList<GoalsInfoTable> Lista de Metas.
	 * @throws ParseException
	 */
	public PaginatedList<GoalsInfoTable> getCommunityInfoTable(PlanMacro macro, Plan plan, Long levelInstance,
			Integer page, Integer pageSize) throws ParseException {
		if (page == null || page < 1) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}
		StructureLevelInstance lvlInstance = null;
		if (levelInstance != null)
			lvlInstance = this.sbs.retrieveLevelInstance(levelInstance);

		Criteria criteria;
		Criteria couting;
		if (lvlInstance != null) {
			criteria = this.filterByInheritedParent(lvlInstance);
			couting = this.filterByInheritedParent(lvlInstance);
		} else {
			criteria = this.dao.newCriteria(StructureLevelInstance.class);
			couting = this.dao.newCriteria(StructureLevelInstance.class);
		}

		criteria.createAlias("level", "level", JoinType.INNER_JOIN);
		criteria.createAlias("plan", "plan", JoinType.INNER_JOIN);
		criteria.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("level.goal", true));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);

		couting.createAlias("level", "level", JoinType.INNER_JOIN);
		couting.createAlias("plan", "plan", JoinType.INNER_JOIN);
		couting.createAlias("plan.parent", "macro", JoinType.INNER_JOIN);
		couting.add(Restrictions.eq("level.goal", true));
		couting.add(Restrictions.eq("deleted", false));
		couting.setProjection(Projections.countDistinct("id"));
		if (macro != null) {
			criteria.add(Restrictions.eq("plan.parent", macro));
			couting.add(Restrictions.eq("plan.parent", macro));
		}
		if (plan != null) {
			criteria.add(Restrictions.eq("plan", plan));
			couting.add(Restrictions.eq("plan", plan));
		}

		PaginatedList<GoalsInfoTable> result = new PaginatedList<>();
		List<StructureLevelInstance> levelInstances = this.filter.filterAndList(criteria, StructureLevelInstance.class,
				"macro.company");
		Long total = this.filter.filterAndFind(couting, "macro.company");
		for (int i = 0; i < levelInstances.size(); i++) {
			levelInstances.get(i).getLevel().setAttributes(this.sbs.retrieveLevelSonsAttributes(levelInstances.get(i)));
		}

		ArrayList<GoalsInfoTable> goalsInfoTable = new ArrayList<GoalsInfoTable>();
		for (StructureLevelInstance s : levelInstances) {
			List<Attribute> attributeList = this.sbs.retrieveLevelAttributes(s.getLevel());
			attributeList = this.sbs.setAttributesInstances(s, attributeList);
			s.getLevel().setAttributes(attributeList);
			GoalsInfoTable goalAux = new GoalsInfoTable();

			StructureLevelInstance indicator = this.sbs.retrieveLevelInstance(s.getParent());
			goalAux.setIndicatorName(indicator.getName());

			StructureLevelInstance objective = this.sbs.retrieveLevelInstance(indicator.getParent());
			goalAux.setObjectiveName(objective.getName());

			StructureLevelInstance strategicAxis = this.sbs.retrieveLevelInstance(objective.getParent());
			goalAux.setStrategicAxisName(strategicAxis.getName());

			goalAux.setGoalName(s.getName());
			for (AttributeInstance a : this.sbs.listAttributeInstanceByLevel(s,false)) {
				Attribute attr = this.sbs.retrieveAttribute(a.getAttribute().getId());
				if (attr.isFinishDate() && s.getPlan().getParent().getCompany().isShowMaturity())
					goalAux.setFinishDate(a.getValueAsDate());
				else if (attr.isReachedField())
					goalAux.setReached(a.getValueAsNumber());
				else if (attr.isExpectedField())
					goalAux.setExpected(a.getValueAsNumber());
			}

			goalsInfoTable.add(goalAux);
		}

		result.setList(goalsInfoTable);
		result.setTotal(total);

		return result;
	}

}
