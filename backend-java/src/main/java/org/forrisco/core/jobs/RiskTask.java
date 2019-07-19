package org.forrisco.core.jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.User;
import org.forpdi.core.user.authz.AccessLevels;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.risk.CloseToMaturityPeriod;
import org.forrisco.risk.Monitor;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskBS;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;
import br.com.caelum.vraptor.boilerplate.factory.SessionManager;

/**
 * Tarefa que é executada a cada Hora para verificar o estado dos riscos
 * 
 * @author Matheus Nascimento
 * 
 */
@ApplicationScoped
@Scheduled(fixedRate = 3600000, concurrent = false)
public class RiskTask implements Task {

	@Inject
	private NotificationBS notificationBS;

	private HibernateDAO dao;

	/**
	 * Método que é executado a cada hora,
	 * 
	 */
	@Override
	public void execute() {

		if (this.dao == null) {
			SessionFactory factory;
			SessionManager mngr;

			factory = CDI.current().select(SessionFactory.class).get();
			mngr = new SessionManager(factory);
			this.dao = new HibernateDAO(mngr);
		}

		ExpiredRisks();

	}

	/**
	 *
	 * verificando se algum risco mudou de "Em dia" para "Próximo a vencer" na
	 * ultima hora
	 * 
	 */
	private void ExpiredRisks() {
		Logger LOG = Logger.getLogger(EmailSenderTask.class);
		LOG.infof("Verificando monitoramento dos Riscos...");

		try {

			PaginatedList<Risk> list = listRiskState(1);
			for (Risk risk : list.getList()) {

				Date maturity = CloseToMaturityDate(risk);
				if (new Date().getTime() - maturity.getTime() < 1000 * 60 * 60
						&& new Date().getTime() - maturity.getTime() > 0) {// intervalo da última hora

					// enviar email de notificação de monitoramento vencido
					String texto = risk.getName();
					CompanyDomain companyDomain = companyDomainByRisk(risk);
					String url = companyDomain.getBaseUrl() + "/#/forrisco/plan-risk/"
							+ risk.getUnit().getPlanRisk().getId() + "/unit/" + risk.getUnit().getId() + "/risk/"
							+ risk.getId() + "/monitor";

					// responsável pelo risco
					this.notificationBS.sendNotificationEmail(NotificationType.FORRISCO_RISK_CLOSE_TO_MATURITY, texto,
							"aux", risk.getUser(), url, companyDomain);

					// responsável pela unidade
					if (!risk.getUser().getId().equals(risk.getUnit().getUser().getId())) {
						this.notificationBS.sendNotificationEmail(NotificationType.FORRISCO_RISK_CLOSE_TO_MATURITY,
								texto, "aux", risk.getUnit().getUser(), url, companyDomain);
					}

					PaginatedList<User> admins = listByPermissionLevel(AccessLevels.COMPANY_ADMIN);

					for (User admin : admins.getList()) {
						// administradores
						if (!admin.getId().equals(risk.getUser().getId())
								&& !admin.getId().equals(risk.getUnit().getUser().getId())) {
							this.notificationBS.sendNotificationEmail(NotificationType.FORRISCO_RISK_CLOSE_TO_MATURITY,
									texto, "aux", admin, url, companyDomain);
						}
					}
				}
			}
		} catch (Throwable ex) {
			LOG.errorf(ex, "Unexpected error occurred while verifying monitors.");
		}
	}

	public Monitor lastMonitorbyRisk(Risk risk) {

		Criteria criteria = this.dao.newCriteria(Monitor.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("risk", risk)).addOrder(Order.desc("begin"));

		criteria.setMaxResults(1);
		Monitor monitor = (Monitor) criteria.uniqueResult();

		return monitor;
	}

	public PaginatedList<Risk> listRiskState(int state) {

		PaginatedList<Risk> results = new PaginatedList<Risk>();

		Criteria criteria = this.dao.newCriteria(Risk.class).add(Restrictions.eq("deleted", false));

		List<Risk> risks = this.dao.findByCriteria(criteria, Risk.class);
		List<Risk> list = new ArrayList<>();

		for (Risk risk : risks) {
			Monitor monitor = lastMonitorbyRisk(risk);
			Date date = risk.getBegin();

			if (monitor != null) {
				date = monitor.getBegin();
			}

			if (RiskBS.riskState(risk.getPeriodicity(), date) == state) {
				list.add(risk);
			}
		}

		try {
			this.dao.persist(null);
		} catch (Exception e) {
			Logger LOG = Logger.getLogger(EmailSenderTask.class);
			LOG.infof("Exception");
		}

		results.setList(list);
		results.setTotal((long) list.size());
		return results;
	}

	public Date CloseToMaturityDate(Risk risk) {
		// calcular corretamente o valor de próximo a vencer

		Monitor lastMonitor = lastMonitorbyRisk(risk);

		long time = lastMonitor != null ? lastMonitor.getBegin().getTime() : risk.getBegin().getTime();
		long hour = 1000 * 60 * 60;
		long day = hour * 24;

		switch (risk.getPeriodicity().toLowerCase()) {
		case "diária":
			time += hour * (24 - CloseToMaturityPeriod.DIARIO.getValue());
			break;

		case "semanal":
			time += day * (7 - CloseToMaturityPeriod.SEMANAL.getValue());
			break;

		case "quinzenal":
			time += day * (15 - CloseToMaturityPeriod.QUINZENAL.getValue());
			break;

		case "mensal":
			time += day * (30 - CloseToMaturityPeriod.MENSAL.getValue());
			break;

		case "bimestral":
			time += day * (60 - CloseToMaturityPeriod.BIMESTRAL.getValue());
			break;

		case "trimestral":
			time += day * (90 - CloseToMaturityPeriod.TRIMESTRAL.getValue());
			break;

		case "semestral":
			time += day * (180 - CloseToMaturityPeriod.SEMESTRAL.getValue());
			break;

		case "anual":
			time += day * (360 - CloseToMaturityPeriod.ANUAL.getValue());
			break;
		}

		return new Date(time);
	}

	public CompanyDomain companyDomainByRisk(Risk risk) {

		PlanRisk planRisk = this.dao.exists(risk.getUnit().getPlanRisk().getId(), PlanRisk.class);

		Company copmany = this.dao.exists(planRisk.getPolicy().getCompany().getId(), Company.class);

		Criteria criteria = this.dao.newCriteria(CompanyDomain.class).add(Restrictions.eq("company", copmany));

		return (CompanyDomain) criteria.list().get(0);
	}

	public PaginatedList<User> listByPermissionLevel(AccessLevels accessLevel) {
		PaginatedList<User> results = new PaginatedList<User>();

		Criteria criteria = this.dao.newCriteria(User.class);
		criteria.add(Restrictions.eq("accessLevel", accessLevel.getLevel()));
		criteria.add(Restrictions.eq("deleted", false));

		List<User> list = this.dao.findByCriteria(criteria, User.class);

		results.setList(list);
		results.setTotal((long) list.size());
		return results;
	}
}
