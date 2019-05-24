package org.forpdi.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.dashboard.admin.GoalsInfoTable;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.filters.PeformanceFilterType;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;


/**
 * Classe auxiliar na geracao da tabela de informacoes das metas
 *  
 * @author Erick Alves
 *
 */
@RequestScoped
public class GoalsInfoTableHelper {
	private HibernateDAO dao;
	
	@Inject
	private DashboardBS dashboardBS;
	
	@Inject
	private StructureHelper structureHelper;
	
	@Inject 
	private AttributeHelper attrHelper;
	
	@Inject
	public GoalsInfoTableHelper(HibernateDAO dao) {
		this.dao = dao;
	}
	
	/** @deprecated CDI-eyes only */
	protected GoalsInfoTableHelper() {
		this(null);
	}
	
	/**
	 * @param idsSet conjunto com os ids dos level instances que serão processados
	 * @param macro plano macro pra filtro
	 * @param plan plano pra filtro
	 * @param indicator indicador pra filtro
	 * @param type tipo de performance pra filtro
	 * @return lista de metas filtradas
	 */
	public List<StructureLevelInstance> goalsFilter(Set<Long> idsSet, PlanMacro macro, Plan plan,
			StructureLevelInstance indicator, PeformanceFilterType type) {
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class)
			.createAlias("levelInstance", "levelInstance")
			.createAlias("levelInstance.level", "level")
			.createAlias("levelInstance.plan", "plan")
			.createAlias("plan.parent", "macro")
			.add(Restrictions.in("levelInstance.id", idsSet))
			.add(Restrictions.eq("level.goal", true))
			.add(Restrictions.eq("deleted", false))
			.setProjection(Projections.projectionList()
				.add(Projections.property("levelInstance"))
				.add(Projections.groupProperty("levelInstance.id"))
			);
		if (macro != null) {
			criteria.add(Restrictions.eq("plan.parent", macro));
		}
		if (plan != null) {
			criteria.add(Restrictions.eq("levelInstance.plan", plan));
		}
		if (indicator != null) {
			criteria.add(Restrictions.eq("levelInstance.parent", indicator.getId()));
		}
		@SuppressWarnings("unchecked")
		List<Object[]> list = criteria.list();
		List<StructureLevelInstance> goals = new ArrayList<>(list.size());
		for (Object[] object : list) {
			StructureLevelInstance structureLevelInstance = (StructureLevelInstance) object[0];
			goals.add(structureLevelInstance);
		}
		
		this.structureHelper.setAttributes(goals);
		
		List<StructureLevelInstance> goalsFiltered = new LinkedList<>();
		if (type == null) {
			goalsFiltered = goals;
		} else {	
			Map<Long, AttributeInstance> polarityMap = this.attrHelper.generatePolarityMap(goals);
			for (StructureLevelInstance goal : goals) {
				Double max = null, min = null, exp = null, reach = null;
				Date finish = null;
				for (AttributeInstance attr : goal.getAttributeInstanceList()) {
					if (attr.getAttribute().isExpectedField()) {
						exp = attr.getValueAsNumber();
					} else if (attr.getAttribute().isMaximumField()) {
						max = attr.getValueAsNumber();
					} else if (attr.getAttribute().isMinimumField()) {
						min = attr.getValueAsNumber();
					} else if (attr.getAttribute().isReachedField()) {
						reach = attr.getValueAsNumber();
					} else if (attr.getAttribute().isFinishDate()) {
						finish = attr.getValueAsDate();
					}
				}
				AttributeInstance polarity = polarityMap.get(goal.getId());
				Date today = new Date();
				switch (type) {
				case BELOW_MINIMUM:
					if ((reach == null && finish != null && finish.before(today))
							|| (reach != null && min != null && this.dashboardBS.polarityComparison(polarity, min, reach))) {
						goalsFiltered.add(goal);
					}
					break;
				case BELOW_EXPECTED:
					if (reach != null && this.dashboardBS.polarityComparison(polarity, exp, reach)
							&& (this.dashboardBS.polarityComparison(polarity, reach, min) || Double.compare(reach, min) == 0)) {
						goalsFiltered.add(goal);
					}
					break;
				case ENOUGH:
					if (((this.dashboardBS.polarityComparison(polarity, max, reach)
							&& (this.dashboardBS.polarityComparison(polarity, reach, exp) || Double.compare(reach, exp) == 0))
							|| (reach != null && max != null && (Double.compare(reach, exp) == 0)))) {
						goalsFiltered.add(goal);
					}
					break;
				case ABOVE_MAXIMUM:
					if (this.dashboardBS.polarityComparison(polarity, reach, max)
							|| (reach != null && max != null && (Double.compare(reach, max) == 0))) {
						goalsFiltered.add(goal);
					}
					break;
				case NOT_STARTED:
					if (reach == null && ((min == null && exp == null && max == null) || (finish.after(today)))) {
						goalsFiltered.add(goal);
					}
					break;
				default:
					goalsFiltered = goals;
					break;
				}
			}
		}
		return goalsFiltered;
	}

	/**
	 * Recebe uma lista de metas e seta os indicadores em levelParent
	 * 
	 * @param goals 
	 */
	public void setIndicators(final List<StructureLevelInstance> goals) {
		if (goals.size() == 0) {
			return;
		}
		List<Long> parentIds = new ArrayList<>(goals.size());
		for (StructureLevelInstance goal : goals) {
			parentIds.add(goal.getParent());
		}
		Criteria criteria = this.dao.newCriteria(StructureLevelInstance.class)
			.add(Restrictions.in("id", parentIds));
		List<StructureLevelInstance> parents = this.dao.findByCriteria(criteria, StructureLevelInstance.class);
		Map<Long, StructureLevelInstance> parentsMap = new HashMap<>(goals.size());
		for (StructureLevelInstance parent : parents) {
			parentsMap.put(parent.getId(), parent);
		}
		for (StructureLevelInstance goal : goals) {
			goal.setLevelParent(parentsMap.get(goal.getParent()));
		}
	}
	
	/**
	 * Recebe uma lista de metas e seta os objetivos em no parent de levelParent
	 * 
	 * @param goals 
	 */
	public void setObjectives(final List<StructureLevelInstance> goals) {
		if (goals.size() == 0) {
			return;
		}
		List<Long> parentIds = new ArrayList<>(goals.size());
		for (StructureLevelInstance goal : goals) {
			parentIds.add(goal.getLevelParent().getParent());
		}
		Criteria criteria = this.dao.newCriteria(StructureLevelInstance.class)
			.add(Restrictions.in("id", parentIds));
		List<StructureLevelInstance> parents = this.dao.findByCriteria(criteria, StructureLevelInstance.class);
		Map<Long, StructureLevelInstance> parentsMap = new HashMap<>(goals.size());
		for (StructureLevelInstance parent : parents) {
			parentsMap.put(parent.getId(), parent);
		}
		for (StructureLevelInstance goal : goals) {
			goal.getLevelParent()
				.setLevelParent(parentsMap.get(goal.getLevelParent().getParent()));
		}
	}

	public ArrayList<GoalsInfoTable> generateGoalsInfo(List<StructureLevelInstance> goals) {
		ArrayList<GoalsInfoTable> goalsInfo = new ArrayList<>(goals.size());
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for (StructureLevelInstance goal : goals) {
			GoalsInfoTable goalInfo = new GoalsInfoTable();
			goalInfo.setGoalName(goal.getName());
			goalInfo.setIndicatorName(goal.getLevelParent().getName());
			goalInfo.setObjectiveName(goal.getLevelParent().getLevelParent().getName());
			goalInfo.setLastModification(fmt.format(goal.getModification()));
			goalInfo.setDeadLineStatus(goal.getDeadlineStatus());
			goalInfo.setProgressStatus(goal.getProgressStatus());
			for (AttributeInstance attrInstance : goal.getAttributeInstanceList()) {
				Attribute attr = attrInstance.getAttribute();
				if (attr.isFinishDate()) {
					goalInfo.setFinishDate(attrInstance.getValueAsDate());
					Date today = new Date();
					Long diferenca = goalInfo.getFinishDate().getTime() - today.getTime();
					diferenca = ((diferenca / 1000) / 60 / 60 / 24);
					if (diferenca >= 0 || goal.isClosed()) {
						goalInfo.setGoalStatus("Em dia.");
					} else {
						if (((-1) * diferenca) <= 1) {
							goalInfo.setGoalStatus("Atraso em " + ((-1) * diferenca) + " dia.");
						} else {
							goalInfo.setGoalStatus("Atraso em " + ((-1) * diferenca) + " dias.");
						}
					}
				} else if (attr.isReachedField()) {
					goalInfo.setReached(attrInstance.getValueAsNumber());
				} else if (attr.isExpectedField()) {
					goalInfo.setExpected(attrInstance.getValueAsNumber());
				}
			}
			goalsInfo.add(goalInfo);
		}
		return goalsInfo;
	}

}
