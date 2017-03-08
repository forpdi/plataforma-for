package org.forpdi.planning.jobs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;

import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.types.ResponsibleField;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.factory.SessionManager;
import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

/**
 *
 * Tarefa para gerar metas em segundo plano.
 * 
 * @author Pedro Mutter
 * @author Renato Oliveira
 *
 */
@ApplicationScoped
@Scheduled(fixedRate=5000, concurrent=false)
public class GoalsGenerationTask implements Task {

	private ConcurrentLinkedQueue<GoalDTO> queue;
	private static final Logger LOG = Logger.getLogger(GoalsGenerationTask.class);
	
	public GoalsGenerationTask() {
		this.queue = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Adiciona uma configuração de meta à fila e executa a tarefa
	 * 
	 * @param goal
	 */
	public void add(GoalDTO goal) {
		this.queue.add(goal);
	}

	/**
	 * Execução da tarefa, que instancia os objetos nescessários e chama a
	 * função para gerar metas.
	 */
	@Override
	public synchronized void execute() {
		if (this.queue.isEmpty()) {
			return;
		}
		
		SessionFactory factory;
		SessionManager mngr;

		factory = CDI.current().select(SessionFactory.class).get();
		mngr = new SessionManager(factory);
		HibernateDAO dao = new HibernateDAO(mngr);
		try {
			while (!this.queue.isEmpty()) {
				this.generate(this.queue.poll(), dao);
			}
		} catch (Throwable ex) {
			LOG.error("Unexpected error at goal generate task.", ex);
		}
		mngr.closeSession();
	}

	/**
	 * Método que verifica a fila, se houver configurações de metas, ele gera
	 * metas de acordo com essa configuração.
	 * 
	 * @param dao
	 */
	private void generate(GoalDTO goal, HibernateDAO dao) {
		try {
			Date begin = goal.getBeginDate();
			Date end = goal.getBeginDate();
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(begin);
			begin = gc.getTime();
			while (begin.before(goal.getEndDate())) {
				if (goal.getPeriodicity().equals("Diária")) {
					gc.setTime(begin);
					gc.add(Calendar.DAY_OF_MONTH, 1);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Semanal")) {
					gc.setTime(begin);
					gc.add(Calendar.WEEK_OF_YEAR, 1);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Quinzenal")) {
					gc.setTime(begin);
					gc.add(Calendar.DAY_OF_MONTH, 15);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Mensal")) {
					gc.setTime(begin);
					gc.add(Calendar.MONTH, 1);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Bimestral")) {
					gc.setTime(begin);
					gc.add(Calendar.MONTH, 2);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Trimestral")) {
					gc.setTime(begin);
					gc.add(Calendar.MONTH, 3);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Semestral")) {
					gc.setTime(begin);
					gc.add(Calendar.MONTH, 6);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Anual")) {
					gc.setTime(begin);
					gc.add(Calendar.YEAR, 1);
					end = gc.getTime();
				} else if (goal.getPeriodicity().equals("Bienal")) {
					gc.setTime(begin);
					gc.add(Calendar.YEAR, 2);
					end = gc.getTime();
				}
				if (end.after(goal.getEndDate()))
					end = goal.getEndDate();
				begin = end;

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				StructureLevelInstance levelInstance = new StructureLevelInstance();
				levelInstance.setName(goal.getName() + " - " + df.format(end));
				levelInstance.setParent(goal.getParent());
				levelInstance.setLevel(goal.getLevel());
				levelInstance.setPlan(goal.getPlan());
				levelInstance.setCreation(new Date());
				levelInstance.setModification(new Date());
				dao.persist(levelInstance);
				AttributeInstance attributeInstance = new AttributeInstance();
				attributeInstance.setAttribute(goal.getLevel().getAttributes().get(1));
				attributeInstance.setLevelInstance(levelInstance);
				attributeInstance.setValue(goal.getDescription());
				dao.persist(attributeInstance);
				for (Attribute attribute : goal.getLevel().getAttributes()) {
					attributeInstance = new AttributeInstance();
					attributeInstance.setAttribute(attribute);
					attributeInstance.setLevelInstance(levelInstance);
					if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
						attributeInstance.setValue(goal.getResponsible());
						dao.persist(attributeInstance);
					} else if (attribute.isFinishDate()) {
						attributeInstance.setValue(df.format(end));
						attributeInstance.setValueAsDate(end);
						dao.persist(attributeInstance);
					} else if (attribute.isExpectedField()) {
						attributeInstance.setValueAsNumber(goal.getExpected());
						attributeInstance.setValue(String.valueOf(goal.getExpected()));
						dao.persist(attributeInstance);
					} else if (attribute.isMinimumField()) {
						attributeInstance.setValueAsNumber(goal.getMinimum());
						attributeInstance.setValue(String.valueOf(goal.getMinimum()));
						dao.persist(attributeInstance);
					} else if (attribute.isMaximumField()) {
						attributeInstance.setValueAsNumber(goal.getMaximum());
						attributeInstance.setValue(String.valueOf(goal.getMaximum()));
						dao.persist(attributeInstance);
					} else {
						attributeInstance = null;
					}
					if (attribute.getAttributeInstances() == null)
						attribute.setAttributeInstances(new ArrayList<AttributeInstance>());
					attribute.getAttributeInstances().add(attributeInstance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
