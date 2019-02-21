package org.forpdi.core.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.time.DateUtils;
import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationSetting;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.fields.FieldsBS;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.quartz.SchedulerException;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

/**
 * Controller para eventos agendados de notificações.
 * 
 * @author Pedro Mutter
 * 
 * 
 *
 */
@Controller
public class ScheduledNotificationController extends AbstractController {

	@Inject
	private StructureBS sbs;
	@Inject
	private PlanBS pbs;
	@Inject
	private NotificationBS bs;
	@Inject
	private UserBS ubs;
	@Inject
	private FieldsBS fbs;
	@Inject
	@Current
	private CompanyDomain domain;

	/**
	 * Tarefa que verifica se as metas do sistema estão vencidas ou próximas a
	 * vencer, se sim é enviado uma notificação ao usuário, atraves do sistema e
	 * por e-mail.
	 * 
	 * @throws SchedulerException
	 */
	@Post(BASEPATH + "/notification/jobs/goalsInspection")
	// (second, minute, hour, day of month, month, day(s) of week)
	@Scheduled(concurrent = false, cron = "0 0 13 * * ?")
	public void inspectGoalsMaturity() throws SchedulerException {
		try {
			PaginatedList<StructureLevelInstance> goals = this.sbs.listGoals();
			for (StructureLevelInstance goal : goals.getList()) {
				if (goal.isClosed())
					continue;
				long idMacro = goal.getPlan().getParent().getId();
				List<AttributeInstance> attrList = this.sbs.listAttributeInstanceByLevel(goal,false);
				for (AttributeInstance attr : attrList) {
					if (attr.getAttribute().isFinishDate()) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(new Date());
						calendar.add(Calendar.DAY_OF_MONTH, 5);

						String url = domain.getBaseUrl() + "/#/plan/" + idMacro + "/details/subplan/level/"
								+ goal.getId();
						if (DateUtils.isSameDay(calendar.getTime(), attr.getValueAsDate())) {
							User responsible = this.ubs.retrieveResponsible(goal);
							CompanyUser companyUser = this.ubs.retrieveCompanyUser(responsible,
									this.domain.getCompany());
							if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting()
									|| companyUser.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
											.getSetting()) {
								this.bs.sendNotification(NotificationType.GOAL_CLOSE_TO_MATURITY, goal.getName(), "",
										responsible.getId(), url);
								this.bs.sendNotificationEmail(NotificationType.GOAL_CLOSE_TO_MATURITY, goal.getName(),
										"", responsible, url);
							} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
									.getSetting()) {
								this.bs.sendNotification(NotificationType.GOAL_CLOSE_TO_MATURITY, goal.getName(), "",
										responsible.getId(), url);
							}
							StructureLevelInstance parent = this.sbs.retrieveLevelInstance(goal.getParent());
							User parentResp = this.ubs.retrieveResponsible(parent);

							if (responsible.getId() != parentResp.getId()) {
								if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting()
										|| companyUser
												.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
														.getSetting()) {
									this.bs.sendNotification(NotificationType.GOAL_CLOSE_TO_MATURITY, goal.getName(),
											parent.getName(), parentResp.getId(), url);
									this.bs.sendNotificationEmail(NotificationType.GOAL_CLOSE_TO_MATURITY,
											goal.getName(), parent.getName(), parentResp, url);
								} else if (companyUser
										.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
												.getSetting()) {
									this.bs.sendNotification(NotificationType.GOAL_CLOSE_TO_MATURITY, goal.getName(),
											parent.getName(), parentResp.getId(), url);
								}
							}
						} else {
							Date today = new Date();
							if (today.after(attr.getValueAsDate())) {
								User responsible = this.ubs.retrieveResponsible(goal);
								CompanyUser companyUser = this.ubs.retrieveCompanyUser(responsible,
										this.domain.getCompany());
								if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting()
										|| companyUser
												.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
														.getSetting()) {
									this.bs.sendNotification(NotificationType.LATE_GOAL, goal.getName(), "",
											responsible.getId(), url);
									this.bs.sendNotificationEmail(NotificationType.LATE_GOAL, goal.getName(), "",
											responsible, url);
								} else if (companyUser
										.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
												.getSetting()) {
									this.bs.sendNotification(NotificationType.LATE_GOAL, goal.getName(), "",
											responsible.getId(), url);
								}
								StructureLevelInstance parent = this.sbs.retrieveLevelInstance(goal.getParent());
								User parentResp = this.ubs.retrieveResponsible(parent);
								companyUser = this.ubs.retrieveCompanyUser(parentResp,this.domain.getCompany());
								if (responsible.getId() != parentResp.getId()) {
									if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting()
											|| companyUser
													.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
															.getSetting()) {
										this.bs.sendNotification(NotificationType.LATE_GOAL, goal.getName(),
												parent.getName(), parentResp.getId(), url);
										this.bs.sendNotificationEmail(NotificationType.LATE_GOAL, goal.getName(),
												parent.getName(), parentResp, url);
									} else if (companyUser
											.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
													.getSetting()) {
										this.bs.sendNotification(NotificationType.LATE_GOAL, goal.getName(),
												parent.getName(), parentResp.getId(), url);
									}
								}
							}
						}
					}
				}
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
		} finally {
			this.result.nothing();
		}
	}

	/**
	 * Tarefa que verifica se os planos de metas estão próximas a vencer, se
	 * sim, uma notificação é enviado aos usuários pertencentes à instituição do
	 * plano de meta.
	 * 
	 * @throws SchedulerException
	 */
	@Post(BASEPATH + "/notification/jobs/plansInspection")
	// (second, minute, hour, day of month, month, day(s) of week)
	@Scheduled(concurrent = false, cron = "0 0 13 * * ?")
	public void inspectPlansMaturity() throws SchedulerException {
		try {
			PaginatedList<PlanMacro> macros = this.pbs.listMacros(this.domain.getCompany(), false, null);
			for (PlanMacro macro : macros.getList()) {
				PaginatedList<Plan> plans = this.pbs.listPlans(macro, false, 0, null, null, 1);
				for (Plan plan : plans.getList()) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DAY_OF_MONTH, 5);
					if (DateUtils.isSameDay(calendar.getTime(), plan.getEnd())) {
						String url = this.domain.getBaseUrl() + "/#/plan/" + plan.getParent().getId()
								+ "/details/subplan/" + plan.getId();
						this.bs.sendNotification(NotificationType.PLAN_CLOSE_TO_MATURITY, plan.getName(), "", null,
								url);
					}
				}
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
		} finally {
			this.result.nothing();
		}
	}

	/**
	 * Tarefa que verifica se os planos de ação estão próximas a vencer ou
	 * vencidos, se sim, uma notificação é enviado aos usuários pertencentes à
	 * instituição do plano de meta.
	 * 
	 * @throws SchedulerException
	 */
	@Post(BASEPATH + "/notification/jobs/actionPlansInspection")
	// (second, minute, hour, day of month, month, day(s) of week)
	@Scheduled(concurrent = false, cron = "0 0 13 * * ?")
	public void inspectActionPlanMaturity() throws SchedulerException {
		try {
			PaginatedList<ActionPlan> acList = this.fbs.listActionPlans();
			for (ActionPlan ap : acList.getList()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, 5);
				Date today = new Date();
				if (DateUtils.isSameDay(calendar.getTime(), ap.getEnd())) {
					String url = domain.getBaseUrl() + "/#/plan/" + ap.getLevelInstance().getPlan().getParent().getId()
							+ "/details/subplan/level/" + ap.getLevelInstance().getId();

					CompanyUser companyUser = this.ubs.retrieveCompanyUser(this.ubs.retrieveResponsible(ap.getLevelInstance()),
							this.domain.getCompany());
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL.getSetting()) {
						this.bs.sendNotification(NotificationType.ACTION_PLAN_CLOSE_TO_MATURITY, ap.getDescription(),
								ap.getLevelInstance().getName(),
								this.ubs.retrieveResponsible(ap.getLevelInstance()).getId(), url);
						this.bs.sendNotificationEmail(NotificationType.ACTION_PLAN_CLOSE_TO_MATURITY, ap.getDescription(),
								ap.getLevelInstance().getName(),
								this.ubs.retrieveResponsible(ap.getLevelInstance()), url);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
							.getSetting()) {
						this.bs.sendNotification(NotificationType.ACTION_PLAN_CLOSE_TO_MATURITY, ap.getDescription(),
								ap.getLevelInstance().getName(),
								this.ubs.retrieveResponsible(ap.getLevelInstance()).getId(), url);
					}
				} else if (today.after(ap.getEnd())) {
					String url = domain.getBaseUrl() + "/#/plan/" + ap.getLevelInstance().getPlan().getParent().getId()
							+ "/details/subplan/level/" + ap.getLevelInstance().getId();
					CompanyUser companyUser = this.ubs.retrieveCompanyUser(
							this.ubs.retrieveResponsible(ap.getLevelInstance()), this.domain.getCompany());
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL.getSetting()) {
						this.bs.sendNotification(NotificationType.LATE_ACTION_PLAN, ap.getDescription(),
								ap.getLevelInstance().getName(),
								this.ubs.retrieveResponsible(ap.getLevelInstance()).getId(), url);
						this.bs.sendNotificationEmail(NotificationType.LATE_ACTION_PLAN, ap.getDescription(),
								ap.getLevelInstance().getName(), this.ubs.retrieveResponsible(ap.getLevelInstance()),
								url);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
							.getSetting()) {
						this.bs.sendNotification(NotificationType.LATE_ACTION_PLAN, ap.getDescription(),
								ap.getLevelInstance().getName(),
								this.ubs.retrieveResponsible(ap.getLevelInstance()).getId(), url);
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
