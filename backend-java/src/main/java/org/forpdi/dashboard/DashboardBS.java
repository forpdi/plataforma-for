package org.forpdi.dashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
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
import org.forpdi.planning.filters.PeformanceFilterType;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forpdi.system.CriteriaCompanyFilter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@RequestScoped
public class DashboardBS extends HibernateBusiness {

	@Inject
	private UserSession userSession;
	@Inject
	private StructureBS sbs;
	@Inject
	private PlanBS pbs;
	@Inject
	private AttributeHelper attrHelper;
	@Inject
	private CriteriaCompanyFilter filter;
	@Inject
	private GoalsInfoTableHelper infoTableHelper;

	private final Integer PAGESIZE = 5;

	/**
	 * Calcular informações gerais (Em dia, Atrasados, Próximos a vencer, Não
	 * iniciados, Abaixo do mínimo, Abaixo do esperado, Suficiente, Acima do máximo)
	 * das metas.
	 * 
	 * @param goals
	 *            Lista de metas para ser calculado as informações gerais.
	 * @return Informações gerais sobre as metas.
	 */
	public GoalsInfo retrieveAdminGoalsInfo(List<StructureLevelInstance> goals) {
		GoalsInfo info = new GoalsInfo();

		if (goals.size() > 0) {
			// recupera todas as AttributeInstance relacionadas as StructureLevelInstance
			List<AttributeInstance> attrInstances = this.sbs.listAllAttributeInstanceByLevelInstances(goals);

			// cria uma lista com as instancias pai de goals (istancias metas) de onde eh possivel recuperar a polaridade
			// cria um map com os ids de goals e dos pais para facilitar o aceeso posterior 
			List<Long> goalParentIds = new ArrayList<>(goals.size());
			Map<Long, Long> idParentMap = new HashMap<>();
			for (StructureLevelInstance goal : goals) {
				if (goal.getParent() != null) {
					goalParentIds.add(goal.getParent());
					idParentMap.put(goal.getParent(), goal.getId());
				}
			}
			
			// recupera todas AttributeInstance em que levelInstance possui o campo de polaridade
			 List<AttributeInstance> polarities = this.attrHelper.retrievePolaritiesByLevelInstanceIds(goalParentIds);
			 
			// cria um map para acessar a polaridade atraves do id do goal (meta)
			Map<Long, AttributeInstance> polarityMap = new HashMap<>();
			for (AttributeInstance polarity : polarities) {
				long structureLevelInstanceId = idParentMap.get(polarity.getLevelInstance().getId());
				polarityMap.put(structureLevelInstanceId, polarity);
			}
			
			int inDay = 0;
			int late = 0;
			int belowMin = 0;
			int belowExp = 0;
			int reached = 0;
			int aboveExp = 0;
			int notStarted = 0;
			int finished = 0;
			int closeToMat = 0;			
			// calcula goals info
			for (StructureLevelInstance goal : goals) {
				List<AttributeInstance> goalAttrInstances = new ArrayList<>();
				for(AttributeInstance attr : attrInstances) {
					if(attr.getLevelInstance().getId().equals(goal.getId())) {
						goalAttrInstances.add(attr);
					}
				}
				Date finish = new Date();
				Double expected = null;
				Double reach = null;
				Double max = null;
				Double min = null;
				for (AttributeInstance attrInstance : goalAttrInstances) {
					Attribute attr = attrInstance.getAttribute();
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
				
				// a polaridade era recuperada diretamente do bd criando multiplos acessos
				// AttributeInstance polarity = this.attrHelper.retrievePolarityAttributeInstance(goal.getParent());

				AttributeInstance polarity = polarityMap.get(goal.getId());
				Date today = new Date();
				if (reach == null && ((min == null && expected == null && max == null) || finish.after(today))) {
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
	 * Calcular informações gerais (Em dia, Atrasados, Próximos a vencer, Não
	 * iniciados, Abaixo do mínimo, Abaixo do esperado, Suficiente, Acima do máximo)
	 * das metas. Este método ja recebe uma lista de AttributeInstance, logo nao precisa acessar 
	 * o BD para gerar as informacoes, como no metodo retrieveAdminGoalsInfo  
	 * 
	 * @param attributeInstances
	 *            Lista de instancias de atributos para ser calculado as informações gerais.
	 * @return Informações gerais sobre as metas.
	 */
	public GoalsInfo retrieveAdminGoalsInfoByAttributeInstances(List<AttributeInstance> attributeInstances) {
		return null;
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
	 * Listar todas as metas pertencentes a um indicador.
	 * 
	 * @param macro
	 *            Plano Macro para buscar as metas.
	 * @param plan
	 *            Plano de Metas para buscar as metas.
	 * @param indicator
	 *            Indicador para buscar as metas.
	 * @return PaginatedList<GoalsInfoTable> Lista de Metas.
	 * @throws ParseException
	 */
	public PaginatedList<GoalsInfoTable> getGoalsInfoTable(PlanMacro macro, Plan plan, StructureLevelInstance indicator,
			Integer page, Integer pageSize, PeformanceFilterType type) throws ParseException {
		if (page == null || page < 1) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}
		// obtem todos os indices de StructureLevelInstance em que o usuario eh responsavel, inclusive os filhos
		Set<Long> allLevelInstanceIds = this.sbs.retrieveChildResponsibleIds();		
		// obtem uma lista com as metas filtradas de acordo com os parametros passados
		if (allLevelInstanceIds.isEmpty()) {
			return new PaginatedList<>(new ArrayList<>(0), 0L);
		}
		List<StructureLevelInstance> goals = this.infoTableHelper.goalsFilter(allLevelInstanceIds, macro, plan, indicator, type);
		// faz a paginacao
		long total = goals.size();
		int minIdx = (page - 1) * pageSize;
		int maxIdx = minIdx + pageSize;
		goals = goals.subList(minIdx, maxIdx < goals.size() ? maxIdx : goals.size());
		// seta os indicadores e objetivos das metas
		this.infoTableHelper.setIndicators(goals);
		this.infoTableHelper.setObjectives(goals);
		// gera uma lista com as informacoes das metas
		ArrayList<GoalsInfoTable> goalsInfo = this.infoTableHelper.generateGoalsInfo(goals);
		return new PaginatedList<>(goalsInfo, total);
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
			for (AttributeInstance a : this.sbs.listAttributeInstanceByLevel(s, false)) {
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
