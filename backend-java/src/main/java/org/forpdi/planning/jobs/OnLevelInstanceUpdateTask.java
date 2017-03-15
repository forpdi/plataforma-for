package org.forpdi.planning.jobs;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;

import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.bean.PerformanceBean;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanDetailed;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevel;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forpdi.planning.structure.StructureLevelInstanceDetailed;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.factory.SessionManager;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;
import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

/**
 * Realiza cálculos e atualizações necessárias quando uma
 * instância de nível tem algum atributo atualizado.
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@ApplicationScoped
@Scheduled(fixedRate = 60000, concurrent=false)
public class OnLevelInstanceUpdateTask implements Task {

	private ConcurrentLinkedQueue<StructureLevelInstance> queue;
	private static final Logger LOG = Logger.getLogger(OnLevelInstanceUpdateTask.class);

	public OnLevelInstanceUpdateTask() {
		queue = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Adiciona uma instância de nível para ser processada pela task.
	 * 
	 * @param goal
	 */
	public void add(StructureLevelInstance level) {
		queue.add(level);
	}
	public void add(List<StructureLevelInstance> levels) {
		queue.addAll(levels);
	}
	
	/**
	 * Método que é chamado toda vez em que a job é executada.
	 */
	@Override
	public synchronized void execute() {
		if (this.queue.isEmpty()) {
			return;
		}
		LOG.infof("Processing level instance queue with %d level instances...", this.queue.size());
		
		SessionFactory factory = CDI.current().select(SessionFactory.class).get();
		SessionManager mngr = new SessionManager(factory);
		HibernateDAO dao = new HibernateDAO(mngr);
		
		int count = 0;
		try {
			while (!this.queue.isEmpty()) {
				this.updateLevelInstanceValues(this.queue.poll(), dao);
				count++;
			}
		} catch (Throwable ex) {
			LOG.errorf(ex, "Exceção ao executar a task.");
		} finally {
			mngr.closeSession();
		}
		LOG.infof("Finished updating %d level instances.", count);
	}
	
	/** Realiza cálculos e atualizações para a instância de nível atual e
	 * enfileira a instância de nível pai para ser atualizada por esta própria
	 * task, a grosso modo, chama "recursivamente" para os pais. */
	private void updateLevelInstanceValues(StructureLevelInstance levelInstance, HibernateDAO dao) {
		StructureHelper structHelper = new StructureHelper(dao);
		AttributeHelper attrHelper = new AttributeHelper(dao);
		
		levelInstance = structHelper.retrieveLevelInstance(levelInstance.getId());
		StructureLevel level = levelInstance.getLevel();
		
		if (!level.isGoal()) {
			if (level.isIndicator() && levelInstance.isAggregate()) {
				structHelper.updateAggregatedLevelValue(levelInstance);
			} else if (level.isIndicator()) {
				PerformanceBean performance = structHelper.calculateIndicatorLevelValue(levelInstance);
				levelInstance.setLevelValue(performance.getPerformance());
				levelInstance.setLevelMinimum(performance.getMinimumAverage());
				levelInstance.setLevelMaximum(performance.getMaximumAverage());
				dao.persist(levelInstance);
			} else {
				PerformanceBean performance = structHelper.calculateLevelValue(levelInstance);
				levelInstance.setLevelValue(performance.getPerformance());
				levelInstance.setLevelMinimum(performance.getMinimumAverage());
				levelInstance.setLevelMaximum(performance.getMaximumAverage());
				dao.persist(levelInstance);
			}
		} else {
			AttributeInstance reachedAttribute = attrHelper.retrieveReachedFieldAttribute(levelInstance);
			Double reached = null;
			Double expected = null;
			Double minimum = null;
			Double maximum = null;
			if (reachedAttribute == null) {
				LOG.errorf("Goal level instance without reached attribute? level instance id: %d", levelInstance.getId());
			} else {
				expected = attrHelper.retrieveExpectedFieldAttribute(levelInstance).getValueAsNumber();
				minimum = attrHelper.retrieveMinimumFieldAttribute(levelInstance).getValueAsNumber();
				maximum = attrHelper.retrieveMaximumFieldAttribute(levelInstance).getValueAsNumber();
				reached = reachedAttribute.getValueAsNumber();
			}
			
			if (expected != null && reached != null) {
				AttributeInstance polarity = attrHelper.retrievePolarityAttributeInstance(levelInstance.getParent());
				if (polarity == null || polarity.getValue().equals("Maior-melhor")) {
					if (expected == 0) {
						levelInstance.setLevelValue(100.0);
						levelInstance.setLevelMinimum(100.0);
						levelInstance.setLevelMaximum(100.0);
					} else {
						levelInstance.setLevelValue(reached * 100.0 / expected);
						levelInstance.setLevelMinimum(minimum * 100.0 / expected);
						levelInstance.setLevelMaximum(maximum * 100.0 / expected);
					}
				} else {
					if (expected == 0) {
						levelInstance.setLevelValue(100.0 - reached >= 0.0 ? 100.0 - reached : 0.0);
						levelInstance.setLevelMinimum(100.0 + minimum - reached >= 0.0 ? (100.0 + minimum - reached) : 0.0);
						levelInstance.setLevelMaximum(100.0 + maximum - reached >= 0.0 ? (100.0 + maximum - reached) : 0.0);
					} else {
						levelInstance.setLevelValue(100.0 * ((2.0 * expected) - reached) / expected);
						levelInstance.setLevelMinimum(100.0 * ((2.0 * expected) - minimum) / expected);
						levelInstance.setLevelMaximum(100.0 * ((2.0 * expected) - maximum) / expected);
					}
				}
				
				AttributeInstance finishDate = attrHelper.retrieveFinishDateFieldAttribute(levelInstance);
				if (finishDate != null) {
					StructureLevelInstanceDetailed levelInstanceDetailed = structHelper.getLevelInstanceDetailed(levelInstance, finishDate);
					dao.persist(levelInstanceDetailed);
					
					StructureLevelInstance parentLevelInstance = levelInstance;
					while (parentLevelInstance.getParent() != null) {
						parentLevelInstance = structHelper.retrieveLevelInstance(parentLevelInstance.getParent());
						PerformanceBean performance = structHelper.calculateLevelValueDetailed(parentLevelInstance, finishDate);
						parentLevelInstance.setLevelValue(performance.getPerformance());
						parentLevelInstance.setLevelMinimum(performance.getMinimumAverage());
						parentLevelInstance.setLevelMaximum(performance.getMaximumAverage());
						levelInstanceDetailed = structHelper.getLevelInstanceDetailed(parentLevelInstance, finishDate);
						dao.persist(levelInstanceDetailed);
					}
					
					Plan plan = levelInstance.getPlan();
					PerformanceBean performance = structHelper.calculatePlanPerformanceDetailed(plan, finishDate);
					plan.setPerformance(performance.getPerformance());
					plan.setMinimumAverage(performance.getMinimumAverage());
					plan.setMaximumAverage(performance.getMaximumAverage());
					PlanDetailed planDetailed = structHelper.getPlanDetailed(plan, finishDate);
					dao.persist(planDetailed);
				}
			} else {
				levelInstance.setLevelValue(null);
			}
			dao.persist(levelInstance);
		}
		
		if (levelInstance.getParent() != null) {
			StructureLevelInstance parent = dao.exists(levelInstance.getParent(), StructureLevelInstance.class);
			if (parent != null)
				this.add(parent);
			else
				LOG.errorf("An inconsistency on level instance with id %d, it refers to an unexistent parent id %d",
						levelInstance.getId(), levelInstance.getParent());
		} else {
			Plan plan = levelInstance.getPlan();
			PerformanceBean performance = structHelper.calculatePlanPerformance(plan);
			plan.setPerformance(performance.getPerformance());
			plan.setMinimumAverage(performance.getMinimumAverage());
			plan.setMaximumAverage(performance.getMaximumAverage());
			dao.persist(plan);
		}
		
		if (level.isIndicator()) {
			List<AggregateIndicator> indicators = structHelper.getAggregatedToIndicators(levelInstance);
			if (!GeneralUtils.isEmpty(indicators)) {
				for (AggregateIndicator indicator : indicators) {
					this.add(indicator.getIndicator());
				}
			}
		}
		
		/*
		StructureLevelInstance indicatorLevelInstance = null;
		if (levelInstance.getParent() != null) {
			boolean haveParent = true;
			while (haveParent) {
				levelInstance = this.retrieveLevelInstance(levelInstance.getParent());
				if (levelInstance.getParent() == null)
					haveParent = false;
				List<StructureLevelInstance> structureLevelInstances = this
						.retrieveLevelInstanceSons(levelInstance.getId());
				if (structureLevelInstances != null) {
					Double total = 0.0;
					for (StructureLevelInstance structureLevelInstance : structureLevelInstances) {
						if (structureLevelInstance.getLevelValue() != null)
							total += structureLevelInstance.getLevelValue();
					}
					if (structureLevelInstances.size() > 0) {
						int size = structureLevelInstances.size();
						if (levelInstance.getLevel().isIndicator()) {
							for (StructureLevelInstance structureLevelInstance : structureLevelInstances) {
								structureLevelInstance.getLevel()
										.setAttributes(this.retrieveLevelAttributes(structureLevelInstance.getLevel()));
								for (Attribute attribute : structureLevelInstance.getLevel().getAttributes()) {
									if (attribute.isFinishDate()) {
										AttributeInstance attributeInstance = this
												.retrieveAttributeInstance(structureLevelInstance, attribute);
										if (structureLevelInstance.getLevelValue() == null && (attributeInstance == null
												|| attributeInstance.getValueAsDate().after(new Date())))
											size--;
									}
								}
							}
							if (size != 0)
								levelInstance.setLevelValue(total / size);
						} else {
							for (StructureLevelInstance structureLevelInstance : structureLevelInstances) {
								if (structureLevelInstance.getLevelValue() == null)
									size--;
								else
									levelInstance.setLevelValue(null);
							}
							if (size != 0)
								levelInstance.setLevelValue(total / size);
							else
								levelInstance.setLevelValue(null);
						}
					} else {
						levelInstance.setLevelValue(null);
					}
					this.persist(levelInstance);
				}

				if (levelInstance.isAggregate()) {
					List<BigInteger> indicators = this.listAggregateIndicatorsByAggregate(levelInstance);
					for (BigInteger indicatorId : indicators) {
						StructureLevelInstance indicator = this.retrieveLevelInstance(indicatorId.longValue());
						this.setAggregateIndicatorValue(indicator);
					}
				}
				
				if (levelInstance.getLevel().isIndicator() && !levelInstance.isAggregate()) {
					indicatorLevelInstance = levelInstance;
				}
			}
			
			if (indicatorLevelInstance != null) {
				List<BigInteger> listAggregatesIndicatorsId = this.listAggregateIndicatorsByAggregate(indicatorLevelInstance);
				if (listAggregatesIndicatorsId.size() > 0) {
					for (BigInteger indAggId : listAggregatesIndicatorsId) {
						this.setAggregateIndicatorValue(this.retrieveLevelInstance(indAggId.longValue()));
					}
				}
			}
		}*/
	}
	
}
