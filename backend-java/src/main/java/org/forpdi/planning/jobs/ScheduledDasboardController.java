package org.forpdi.planning.jobs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.dashboard.DashboardBS;
import org.forpdi.planning.attribute.types.enums.Periodicity;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.quartz.SchedulerException;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

/**
 * Controller para eventos agendados do painel de bordo.
 * 
 * @author Pedro Mutter
 *
 * 
 */

@Controller
public class ScheduledDasboardController extends AbstractController {

	@Inject
	private StructureBS sbs;
	@Inject
	private DashboardBS bs;
	@Inject
	private Result result;

	/**
	 * Tarefa para salvar os valores atuais dos indicadores, executada todo dia
	 * às 13:00
	 * 
	 * @throws SchedulerException
	 */
	@Post(BASEPATH + "/dashboard/jobs/indicatorHistory")
	// (second, minute, hour, day of month, month, day(s) of week)
	@Scheduled(concurrent = false, cron = "0 0 13 * * ?")
	public void saveIndicatorHistory() throws SchedulerException {
		try {
			PaginatedList<StructureLevelInstance> instances = this.sbs.listAllIndicators();
			for (StructureLevelInstance instance : instances.getList()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				if (instance.getNextSave() == null || calendar.getTime().after(instance.getNextSave())
						|| calendar.getTime().equals(instance.getNextSave())) {
					Periodicity peri = this.sbs.getPeriodicityByInstance(instance);
					if (peri == null) {
						LOGGER.info("Histórico do indicador " + instance.getName()
								+ " não pode ser salvo, periodicidade nula.");
					} else {
						this.bs.saveIndicatorHistory(instance, peri);
						LOGGER.info("Histórico do indicador " + instance.getName()
								+ " salvo com sucesso, próximo registro em "
								+ new SimpleDateFormat("dd/MM/yyyy").format(instance.getNextSave()));
					}
				}
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
		} finally {
			this.result.nothing();
		}
	}

}
