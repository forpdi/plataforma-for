package org.forpdi.planning.structure;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.types.enums.CalculationType;
import org.forpdi.planning.bean.PerformanceBean;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanDetailed;
import org.hibernate.Criteria;
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
		if (levelInstance.getCalculation() == CalculationType.NORMAL_AVG) {
			Double total = 0.0;
			for (AggregateIndicator aggregates : levelInstance.getIndicatorList()) {
				if (aggregates.getAggregate().getLevelValue() != null)
					total += aggregates.getAggregate().getLevelValue();
			}
			if (levelInstance.getIndicatorList().size() > 0)
				levelInstance.setLevelValue(total / levelInstance.getIndicatorList().size());
			else
				levelInstance.setLevelValue(total);
		} else if (levelInstance.getCalculation() == CalculationType.WEIGHTED_AVG) {
			Double total = 0.0;
			for (AggregateIndicator aggregates : levelInstance.getIndicatorList()) {
				if (aggregates.getAggregate().getLevelValue() != null)
					total += aggregates.getAggregate().getLevelValue() * aggregates.getPercentage();
			}
			if (levelInstance.getIndicatorList().size() > 0)
				levelInstance.setLevelValue(total / 100);
			else
				levelInstance.setLevelValue(total);
		} else if (levelInstance.getCalculation() == CalculationType.SUM) {
			Double total = 0.0;
			for (AggregateIndicator aggregates : levelInstance.getIndicatorList()) {
				if (aggregates.getAggregate().getLevelValue() != null)
					total += aggregates.getAggregate().getLevelValue();
			}
			levelInstance.setLevelValue(total);
		}
		this.dao.persist(levelInstance);
	}

}
