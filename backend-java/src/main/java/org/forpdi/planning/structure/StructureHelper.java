package org.forpdi.planning.structure;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.types.enums.CalculationType;
import org.forpdi.planning.bean.PerformanceBean;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanDetailed;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;

/**
 * Classe com implementações de métodos para auxílios nas classes de negócio
 * e para as jobs assíncronas. Contempla regras relacionadas às estruturas e
 * aos níveis de informação (StructureLevel).
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@RequestScoped
public class StructureHelper {
	private final HibernateDAO dao;
	
	@Inject
	private StructureBS structureBS;
	
	/** @deprecated CDI-eyes only */
	protected StructureHelper() {
		this(null);
	}
	
	@Inject
	public StructureHelper(HibernateDAO dao) {
		this.dao = dao;
	}
	
	
	/**
	 * Buscar uma instância de um level pelo id.
	 */
	public StructureLevelInstance retrieveLevelInstance(Long id) {
		StructureLevelInstance levelInstance = this.dao.exists(id, StructureLevelInstance.class);
		this.fillIndicators(levelInstance);
		return levelInstance;
	}
	
	/**
	 * Busca detalhada de uma instância de um level.
	 */
	public StructureLevelInstanceDetailed getLevelInstanceDetailed(StructureLevelInstance levelInstance, AttributeInstance finishDate) {
		int month = finishDate.getValueAsDate().getMonth()+1;
		int year = finishDate.getValueAsDate().getYear()+1900;
		Criteria criteria = this.dao.newCriteria(StructureLevelInstanceDetailed.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("levelInstance", levelInstance));
		criteria.add(Restrictions.eq("month", month));
		criteria.add(Restrictions.eq("year", year));
		StructureLevelInstanceDetailed levelInstanceDetailed = (StructureLevelInstanceDetailed) criteria.uniqueResult();
		
		if (levelInstanceDetailed == null)
			levelInstanceDetailed = new StructureLevelInstanceDetailed();
		levelInstanceDetailed.setLevelInstance(levelInstance);
		levelInstanceDetailed.setMonth(month);
		levelInstanceDetailed.setYear(year);
		levelInstanceDetailed.setLevelValue(levelInstance.getLevelValue());
		levelInstanceDetailed.setLevelMinimum(levelInstance.getLevelMinimum());
		levelInstanceDetailed.setLevelMaximum(levelInstance.getLevelMaximum());
		return levelInstanceDetailed;
	}
	
	/**
	 * Busca detalhada de um plano.
	 */
	public PlanDetailed getPlanDetailed(Plan plan, AttributeInstance finishDate) {
		int month = finishDate.getValueAsDate().getMonth()+1;
		int year = finishDate.getValueAsDate().getYear()+1900;
		Criteria criteria = this.dao.newCriteria(PlanDetailed.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("plan", plan));
		criteria.add(Restrictions.eq("month", month));
		criteria.add(Restrictions.eq("year", year));
		PlanDetailed planDetailed = (PlanDetailed) criteria.uniqueResult();
		
		if (planDetailed == null)
			planDetailed = new PlanDetailed();
		planDetailed.setPlan(plan);
		planDetailed.setMonth(month);
		planDetailed.setYear(year);
		planDetailed.setPerformance(plan.getPerformance());
		planDetailed.setMinimumAverage(plan.getMinimumAverage());
		planDetailed.setMaximumAverage(plan.getMaximumAverage());
		return planDetailed;
	}
	
	/** Calcula a média do valor no nível abaixo. */
	public PerformanceBean calculateLevelValue(StructureLevelInstance levelInstance) {
		Criteria criteria =
			this.dao.newCriteria(StructureLevelInstance.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("parent", levelInstance.getId()))
			.add(Restrictions.isNotNull("levelValue"))
			.setProjection(
				Projections.projectionList()
				.add(Projections.avg("levelValue"), "performance")
				.add(Projections.avg("levelMinimum"), "minimumAverage")
				.add(Projections.avg("levelMaximum"), "maximumAverage")
			)
			.setResultTransformer(new AliasToBeanResultTransformer(PerformanceBean.class))
		;
		return (PerformanceBean) criteria.uniqueResult();
	}
	
	/** Calcula a média do valor no nível indicador abaixo. */
	public PerformanceBean calculateIndicatorLevelValue(StructureLevelInstance levelInstance) {
		Date date = new Date();
		Criteria criteria = this.dao.newCriteria(AttributeInstance.class);
		criteria.createAlias("attribute", "attribute", JoinType.INNER_JOIN);
		criteria.createAlias("levelInstance", "levelInstance", JoinType.INNER_JOIN);
		criteria.add(Restrictions.isNotNull("valueAsDate"));
		criteria.add(Restrictions.eq("attribute.finishDate", true));
		criteria.add(Restrictions.eq("levelInstance.deleted", false));
		criteria.add(Restrictions.eq("levelInstance.parent", levelInstance.getId()));
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.le("valueAsDate", date));
		or.add(Restrictions.isNotNull("levelInstance.levelValue"));
		criteria.add(or);
		
		List<AttributeInstance> list = this.dao.findByCriteria(criteria, AttributeInstance.class);
		Double performance = 0.0;
		Double minimumAverage = 0.0;
		Double maximumAverage = 0.0;
		for (AttributeInstance attrInstance : list) {
			if (attrInstance.getLevelInstance().getLevelValue() != null)
				performance = performance+attrInstance.getLevelInstance().getLevelValue();
			if (attrInstance.getLevelInstance().getLevelMinimum() != null)
				minimumAverage = minimumAverage+attrInstance.getLevelInstance().getLevelMinimum();
			if (attrInstance.getLevelInstance().getLevelMaximum() != null)
			maximumAverage = maximumAverage+attrInstance.getLevelInstance().getLevelMaximum();
		}
		PerformanceBean performanceBean = new PerformanceBean();
		performanceBean.setPerformance(performance/list.size());
		performanceBean.setMinimumAverage(minimumAverage/list.size());
		performanceBean.setMaximumAverage(maximumAverage/list.size());
		
		return performanceBean;
	}
	
	/** Calcula a média do valor no nível abaixo detalhado. */
	public PerformanceBean calculateLevelValueDetailed(StructureLevelInstance levelInstance, AttributeInstance finishDate) {
		int month = finishDate.getValueAsDate().getMonth()+1;
		int year = finishDate.getValueAsDate().getYear()+1900;
		Criteria criteria =
			this.dao.newCriteria(StructureLevelInstanceDetailed.class)
			.createAlias("levelInstance", "levelInstance", JoinType.INNER_JOIN)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.isNotNull("levelValue"))
			.add(Restrictions.eq("month", month))
			.add(Restrictions.eq("year", year))
			.add(Restrictions.eq("levelInstance.deleted", false))
			.add(Restrictions.eq("levelInstance.parent", levelInstance.getId()))
			.setProjection(
				Projections.projectionList()
				.add(Projections.avg("levelValue"), "performance")
				.add(Projections.avg("levelMinimum"), "minimumAverage")
				.add(Projections.avg("levelMaximum"), "maximumAverage")
			)
			.setResultTransformer(new AliasToBeanResultTransformer(PerformanceBean.class))
		;
		return (PerformanceBean) criteria.uniqueResult();
	}
	
	/** Calcula a média do valor do primeiro nível do plano. */
	public PerformanceBean calculatePlanPerformance(Plan plan) {
		Criteria criteria =
			this.dao.newCriteria(StructureLevelInstance.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("plan", plan))
			.add(Restrictions.isNull("parent"))
			.add(Restrictions.isNotNull("levelValue"))
			.setProjection(
				Projections.projectionList()
				.add(Projections.avg("levelValue"), "performance")
				.add(Projections.avg("levelMinimum"), "minimumAverage")
				.add(Projections.avg("levelMaximum"), "maximumAverage")
			)
			.setResultTransformer(new AliasToBeanResultTransformer(PerformanceBean.class))
		;
		return (PerformanceBean) criteria.uniqueResult();
	}
	
	/** Calcula a média do valor do primeiro nível do plano. */
	public PerformanceBean calculatePlanPerformanceDetailed(Plan plan, AttributeInstance finishDate) {
		int month = finishDate.getValueAsDate().getMonth()+1;
		int year = finishDate.getValueAsDate().getYear()+1900;
		Criteria criteria =
			this.dao.newCriteria(StructureLevelInstanceDetailed.class)
			.createAlias("levelInstance", "levelInstance", JoinType.INNER_JOIN)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.isNotNull("levelValue"))
			.add(Restrictions.eq("month", month))
			.add(Restrictions.eq("year", year))
			.add(Restrictions.eq("levelInstance.deleted", false))
			.add(Restrictions.eq("levelInstance.plan", plan))
			.add(Restrictions.isNull("levelInstance.parent"))
			.setProjection(
				Projections.projectionList()
				.add(Projections.avg("levelValue"), "performance")
				.add(Projections.avg("levelMinimum"), "minimumAverage")
				.add(Projections.avg("levelMaximum"), "maximumAverage")
			)
			.setResultTransformer(new AliasToBeanResultTransformer(PerformanceBean.class))
		;
		return (PerformanceBean) criteria.uniqueResult();
	}


	/** Preenche a lista de indicadores de um nível indicador. */
	public void fillIndicators(StructureLevelInstance levelInstance) {
		if (levelInstance != null && levelInstance.isAggregate()) {
			levelInstance.setIndicatorList(this.listIndicators(levelInstance));
		}
	}
	
	/** Listar instâncias dos indicadores agregados pela instância de um
	 *  nível de indicador. */
	public List<AggregateIndicator> listIndicators(StructureLevelInstance indicator) {
		Criteria criteria = this.dao.newCriteria(AggregateIndicator.class);
		criteria.createAlias("aggregate", "aggregate", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("aggregate.deleted", false));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("indicator", indicator));

		return this.dao.findByCriteria(criteria, AggregateIndicator.class);
	}
	
	public List<AggregateIndicator> getAggregatedToIndicators(StructureLevelInstance aggregated) {
		Criteria criteria = this.dao.newCriteria(AggregateIndicator.class);
		criteria.createAlias("indicator", "indicator", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("aggregate", aggregated));
		criteria.add(Restrictions.eq("indicator.deleted", false));
		criteria.add(Restrictions.eq("deleted", false));

		return this.dao.findByCriteria(criteria, AggregateIndicator.class);
	}
	
	/** Realiza o cálculo e atualiza o valor de nível (valor agregado)
	 * de acordo com o tipo de cálculo definido para o nível. */
	public void updateAggregatedLevelValue(StructureLevelInstance levelInstance) {
		Double total = 0.0;
		Double totalMinimum = 0.0;
		Double totalMaximum = 0.0;
		if (levelInstance.getCalculation() == CalculationType.NORMAL_AVG) {
			for (AggregateIndicator aggregates : levelInstance.getIndicatorList()) {
				if (aggregates.getAggregate().getLevelValue() != null) {
					total += aggregates.getAggregate().getLevelValue();
					totalMinimum += aggregates.getAggregate().getLevelMinimum();
					totalMaximum += aggregates.getAggregate().getLevelMaximum();
				}
			}
			if (levelInstance.getIndicatorList().size() > 0) {
				levelInstance.setLevelValue(total / levelInstance.getIndicatorList().size());
				levelInstance.setLevelMinimum(totalMinimum / levelInstance.getIndicatorList().size());
				levelInstance.setLevelMaximum(totalMaximum / levelInstance.getIndicatorList().size());
			} else {
				levelInstance.setLevelValue(total);
				levelInstance.setLevelMinimum(totalMinimum);
				levelInstance.setLevelMaximum(totalMaximum);
			}
		} else if (levelInstance.getCalculation() == CalculationType.WEIGHTED_AVG) {
			for (AggregateIndicator aggregates : levelInstance.getIndicatorList()) {
				if (aggregates.getAggregate().getLevelValue() != null) {
					total += aggregates.getAggregate().getLevelValue() * aggregates.getPercentage();
					totalMinimum += aggregates.getAggregate().getLevelMinimum() * aggregates.getPercentage();
					totalMaximum += aggregates.getAggregate().getLevelMaximum() * aggregates.getPercentage();
				}
			}
			if (levelInstance.getIndicatorList().size() > 0) {
				levelInstance.setLevelValue(total / 100);
				levelInstance.setLevelMinimum(totalMinimum / 100);
				levelInstance.setLevelMaximum(totalMaximum / 100);
			} else {
				levelInstance.setLevelValue(total);
				levelInstance.setLevelMinimum(totalMinimum);
				levelInstance.setLevelMaximum(totalMaximum);
			}
		} else if (levelInstance.getCalculation() == CalculationType.SUM) {
			for (AggregateIndicator aggregates : levelInstance.getIndicatorList()) {
				if (aggregates.getAggregate().getLevelValue() != null) {
					total += aggregates.getAggregate().getLevelValue();
					totalMinimum += aggregates.getAggregate().getLevelMinimum();
					totalMaximum += aggregates.getAggregate().getLevelMaximum();
				}
			}
			levelInstance.setLevelValue(total);
			levelInstance.setLevelMinimum(totalMinimum);
			levelInstance.setLevelMaximum(totalMaximum);
		}
		this.dao.persist(levelInstance);
	}

	/**
	 * Recebe uma lista de StructureLevelInstance e seta os atributos
	 * 
	 * @param structureLevelInstance 
	 */
	public void setAttributes(final List<StructureLevelInstance> structureLevelInstance) {
		List<AttributeInstance> attrInstances = this.structureBS.listAllAttributeInstanceByLevelInstances(structureLevelInstance);
		Map<Long, List<AttributeInstance>> strucAttrInstanceMap = new HashMap<>();
		for (AttributeInstance attrInstance : attrInstances) {
			List<AttributeInstance> attrInstanceList = strucAttrInstanceMap.get(attrInstance.getLevelInstance().getId());
			if (attrInstanceList == null) {
				attrInstanceList = new LinkedList<>();
				strucAttrInstanceMap.put(attrInstance.getLevelInstance().getId(), attrInstanceList);
			}
			attrInstanceList.add(attrInstance);
		}
		for (StructureLevelInstance goal : structureLevelInstance) {
			goal.setAttributeInstanceList(strucAttrInstanceMap.get(goal.getId()));			
		}
	}
}
