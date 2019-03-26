package org.forpdi.core.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.mail.EmailException;
import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forpdi.core.jobs.NotificationEmail;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.auth.UserSession;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.UserPermission;
import org.forpdi.core.user.authz.permission.ManageUsersPermission;
import org.forpdi.core.user.authz.permission.ViewUsersPermission;
import org.forpdi.planning.permissions.ManageDocumentPermission;
import org.forpdi.planning.permissions.ManagePlanMacroPermission;
import org.forpdi.planning.permissions.ManagePlanPermission;
import org.forpdi.planning.permissions.UpdateGoalPermission;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.FavoriteLevelInstance;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forpdi.system.Archive;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Rodrigo de Freitas Santos
 */
@RequestScoped
public class NotificationBS extends HibernateBusiness {

	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private UserSession userSession;
	@Inject
	private EmailSenderTask emailTask;
	@Inject
	private UserBS userBS;

	/**
	 * Salvar todas as notificações do sistema.
	 * 
	 * @param type
	 *            Tipo da notificação.
	 * @param text
	 *            Texto da notificação.
	 * @param aux
	 *            Nome do Plano de metas onde ocorreu a notificação.
	 * @param userId
	 *            Id do usuário para receber a notificação.
	 */
	public void sendNotification(NotificationType type, String text, String aux, Long userId, String url) {
		PaginatedList<User> users = new PaginatedList<User>();
		users.setList(new ArrayList<User>());
		if (userId != null) {
			User user = this.userBS.existsByUser(userId);
			users.getList().add(user);
		} else {
			users = this.userBS.listUsersByCompany();
		}
		url = url.replaceAll("//#", "/#");
		Notification notification = new Notification();
		notification.setPicture(type.getImageUrl());
		notification.setCompany(this.domain.getCompany());
		notification.setType(type.getValue());
		notification.setOnlyEmail(type.isOnlyEmail());
		notification.setUrl(url);
		this.setDescriptionForNotification(notification, type, text, aux);
		for (User user : users.getList()) {
			Notification not = new Notification();
			not.setPicture(notification.getPicture());
			not.setCompany(notification.getCompany());
			not.setType(notification.getType());
			not.setOnlyEmail(notification.isOnlyEmail());
			not.setDescription(notification.getDescription());
			not.setUrl(notification.getUrl());
			not.setUser(user);
			this.persist(not);
		}
	}

	/**
	 * Enviar notificação do sistema para o email do usuário.
	 * 
	 * @param type
	 *            Tipo da notificação.
	 * @param text
	 *            Texto da notificação.
	 * @param aux
	 *            Nome do Plano de metas onde ocorreu a notificação.
	 * @param user
	 *            Id do usuário para receber a notificação.
	 * @throws EmailException
	 */
	public void sendNotificationEmail(NotificationType type, String text, String aux, User user, String url)
			throws EmailException {
		if (url == null) {
			url = this.domain.getBaseUrl();
		}
		url = url.replaceAll("//#", "/#");
		EmailBuilder builder;
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setCompany(domain.getCompany());
		notification.setCreation(new Date());
		notification.setOnlyEmail(true);
		notification.setPicture(type.getImageUrl());
		notification.setVizualized(false);
		notification.setType(type.getValue());
		notification.setUrl(url);
		this.setDescriptionForNotification(notification, type, text, aux);
		if (type == NotificationType.WELCOME) {
			builder = new EmailBuilder(NotificationType.WELCOME);
		} else if (type == NotificationType.INVITE_USER) {
			builder = new EmailBuilder(NotificationType.INVITE_USER, user.getName(), text);
		} else if (type == NotificationType.RECOVER_PASSWORD) {
			builder = new EmailBuilder(NotificationType.RECOVER_PASSWORD, user.getName(), text);
		} else {
			builder = new EmailBuilder(type, notification.getDescription(), url);
		}
		this.persist(notification);
		this.emailTask
				.add(new NotificationEmail(user.getEmail(), user.getName(), builder.getSubject(), builder.getBody(), null));
	}
	
	/**
	 * Enviar notificação do sistema para o email do usuário com arquivo anexado.
	 * 
	 * @param type
	 *            Tipo da notificação.
	 * @param text
	 *            Texto da notificação.
	 * @param aux
	 *            Nome do Plano de metas onde ocorreu a notificação.
	 * @param user
	 *            Id do usuário para receber a notificação.
	 * @throws EmailException
	 */
	public void sendAttachedNotificationEmail(NotificationType type, String text, String aux, User user, String url, Archive attachment)
			throws EmailException {
		if (url == null) {
			url = this.domain.getBaseUrl();
		}
		url = url.replaceAll("//#", "/#");
		EmailBuilder builder;
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setCompany(domain.getCompany());
		notification.setCreation(new Date());
		notification.setOnlyEmail(true);
		notification.setPicture(type.getImageUrl());
		notification.setVizualized(false);
		notification.setType(type.getValue());
		notification.setUrl(url);
		this.setDescriptionForNotification(notification, type, text, aux);
		if (type == NotificationType.WELCOME) {
			builder = new EmailBuilder(NotificationType.WELCOME);
		} else if (type == NotificationType.INVITE_USER) {
			builder = new EmailBuilder(NotificationType.INVITE_USER, user.getName(), text);
		} else if (type == NotificationType.RECOVER_PASSWORD) {
			builder = new EmailBuilder(NotificationType.RECOVER_PASSWORD, user.getName(), text);
		} else {
			builder = new EmailBuilder(type, notification.getDescription(), url);
		}
		this.persist(notification);
		this.emailTask
				.add(new NotificationEmail(user.getEmail(), user.getName(), builder.getSubject(), builder.getBody(), attachment.getName()));
	}
	

	/**
	 * Enviar notificação do sistema para o email do usuário.
	 * 
	 * @param type
	 *            Tipo da notificação.
	 * @param text
	 *            Texto da notificação.
	 * @param aux
	 *            Nome do Plano de metas onde ocorreu a notificação.
	 * @param user
	 *            Id do usuário para receber a notificação.
	 * @throws EmailException
	 */
	public void sendNotificationEmail(NotificationType type, String text, String aux, User user, String url, CompanyDomain companyDomain)
			throws EmailException {
		if (url == null) {
			url = companyDomain.getBaseUrl();
		}
		url = url.replaceAll("//#", "/#");
		EmailBuilder builder;
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setCompany(companyDomain.getCompany());
		notification.setCreation(new Date());
		notification.setOnlyEmail(false);
		notification.setPicture(type.getImageUrl());
		notification.setVizualized(false);
		notification.setType(type.getValue());
		notification.setUrl(url);
		this.setDescriptionForNotification(notification, type, text, aux);
		if (type == NotificationType.WELCOME) {
			builder = new EmailBuilder(NotificationType.WELCOME);
		} else if (type == NotificationType.INVITE_USER) {
			builder = new EmailBuilder(NotificationType.INVITE_USER, user.getName(), text);
		} else if (type == NotificationType.RECOVER_PASSWORD) {
			builder = new EmailBuilder(NotificationType.RECOVER_PASSWORD, user.getName(), text);
		} else {
			builder = new EmailBuilder(type, notification.getDescription(), url);
		}
		this.persist(notification);
		this.emailTask
				.add(new NotificationEmail(user.getEmail(), user.getName(), builder.getSubject(), builder.getBody(), null));
	}
	
	
	
	/**
	 * Listar as permissões do usuário em uma instituição.
	 * 
	 * @param user
	 *            Usuário para listar as notifições.
	 * @param limit
	 *            Número maximo de notificações listadas.
	 * @param page
	 *            Número da pagina para listar as notificações.
	 * @return
	 */
	public PaginatedList<Notification> listNotificationByUserCompany(User user, Long limit, int page, boolean topBar) {
		PaginatedList<Notification> results = new PaginatedList<Notification>();
		Criteria criteria = this.dao.newCriteria(Notification.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("company", this.domain.getCompany()));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("onlyEmail", false));
		if (topBar == true)
			criteria.addOrder(Order.asc("vizualized"));
		criteria.addOrder(Order.desc("creation"));
		if (limit != null) {
			criteria.setFirstResult((int) ((page - 1) * limit));
			criteria.setMaxResults(limit.intValue());
		}
		List<Notification> notificationUser = this.dao.findByCriteria(criteria, Notification.class);

		Criteria counting = this.dao.newCriteria(Notification.class);
		counting.setProjection(Projections.countDistinct("id"));
		counting.add(Restrictions.eq("user", user));
		counting.add(Restrictions.eq("company", this.domain.getCompany()));
		counting.add(Restrictions.eq("deleted", false));
		counting.add(Restrictions.eq("onlyEmail", false));

		results.setList(notificationUser);
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}

	/**
	 * Retornar o número de notificações do usuário.
	 * 
	 * @param user
	 *            Usuário para retornar o número de notificações.
	 * @return Número de notificações do usuário.
	 */
	public Long countNotifications(User user) {
		Criteria criteria = this.dao.newCriteria(Notification.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("company", this.domain.getCompany()));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("onlyEmail", false));
		criteria.add(Restrictions.eq("vizualized", false));
		criteria.setProjection(Projections.countDistinct("id"));
		Long count = (Long) criteria.uniqueResult();
		return count;
	}

	/**
	 * Setar a descrição da notificação.
	 * 
	 * @param notification
	 *            Tipo da notificação para setar a descrição.
	 * @param type
	 *            Tipo da notificação.
	 * @param text
	 *            Texto da descrição.
	 * @param aux
	 *            Nome do Plano de metas na onde ocorreu a notificação.
	 */
	public void setDescriptionForNotification(Notification notification, NotificationType type, String text,
			String aux) {
		if (type == NotificationType.WELCOME) {
			notification.setDescription("Bem vindo ao ForPDI, seu cadastro foi realizado com sucesso.");
		} else if (type == NotificationType.ACCESSLEVEL_CHANGED) {
			notification
					.setDescription("<b>Seu tipo de conta foi alterado. Agora você é um </b>\"" + text + "\"<b>.</b>");
		} else if (type == NotificationType.PERMISSION_CHANGED) {
			notification.setDescription(
					"<b>Suas permissões foram alteradas. Agora você pode: </b>\"" + text + "\"<b>. Faça o login novamente para aplicar as alterações.</b>");
		} else if (type == NotificationType.PLAN_MACRO_CREATED) {
			notification.setDescription("<b>O plano </b>\"" + text + "\"<b> foi criado.</b>");
		} else if (type == NotificationType.PLAN_CREATED) {
			notification.setDescription(
					"<b>O plano de metas </b>\"" + text + "\"<b> foi criado no plano </b>\"" + aux + "\"<b>.</b>");
		} else if (type == NotificationType.ATTRIBUTED_RESPONSIBLE) {
			notification.setDescription(
					"<b>Você foi atribuído como responsável em: </b>\"" + aux + "\" - \"" + text + "\"<b>.</b>");
		} else if (type == NotificationType.GOAL_CLOSED) {
			notification.setDescription("<b>A meta </b>\"" + text + "\"<b> foi concluída.</b>");
		} 
		else if (type == NotificationType.GOAL_OPENED) {
			notification.setDescription("<b>A meta </b>\"" + text + "\"<b> foi reaberta.</b>");
		}else if (type == NotificationType.PLAN_CLOSE_TO_MATURITY) {
			notification.setDescription("<b>O plano de metas </b>\"" + text + "\"<b> do plano </b>\"" + aux
					+ "\"<b> está próximo da data de término.</b>");
		} else if (type == NotificationType.GOAL_CLOSE_TO_MATURITY) {
			if (!aux.isEmpty()) {
				aux = "<b> do indicador </b>\"" + aux + "\"";
			}
			notification.setDescription(
					"<b>A meta </b>\"" + text + "\"" + aux + "<b> está próxima da data de vencimento.</b>");
		} else if (type == NotificationType.LATE_GOAL) {
			if (!aux.isEmpty()) {
				aux = "<b> do indicador </b>\"" + aux + "\"";
			}
			notification.setDescription("<b>A meta </b>\"" + text + "\"" + aux + "<b> está atrasada.</b>");
		} else if (type == NotificationType.ACTION_PLAN_CLOSED) {
			notification.setDescription(
					"<b>A ação </b>\"" + text + "\"<b> do indicador </b>\"" + aux + "\"<b> foi concluída.</b>");
		} else if (type == NotificationType.LATE_ACTION_PLAN) {
			notification.setDescription(
					"<b>A ação </b>\"" + text + "\"<b> do indicador </b>\"" + aux + "\"<b> está atrasada.</b>");
		} else if (type == NotificationType.ACTION_PLAN_CLOSE_TO_MATURITY) {
			notification.setDescription("<b>A ação </b>\"" + text + "\"<b> do indicador </b>\"" + aux
					+ "\"<b> está próxima do vencimento.</b>");
		} else if (type == NotificationType.DATE_ATTRIBUTE_UPDATED) {
			notification.setDescription(
					"<b>A data de </b>\"" + aux + "\" - \"" + text + "\"<b> foi alterada. Verifique o novo prazo.</b>");
		} else if (type == NotificationType.FORRISCO_PROCESS_CREATED) {
			notification.setDescription("<b>"+ text +"</b>");
			
		} else if (type == NotificationType.FORRISCO_RISK_CLOSE_TO_MATURITY) {
			notification.setDescription("<b>O monitoramento do risco</b>\"" + text 
					+ "\"<b>no ForRisco está próximo a vencer. Crie um novo monitoramento no sistema para atualizar o risco.</b>");
		} else {
			notification.setDescription("");
		}
	}

	/**
	 * Retornar o nome do nível de acesso do usuário no sistema.
	 * 
	 * @param companyUser
	 *            Nível de acesso do usuário.
	 * @return String Nome do nível de acesso do usuário.
	 */
	public String getAccessLevelText(CompanyUser companyUser) {
		String text = "";
		if (companyUser.getAccessLevel() == AccessLevels.SYSTEM_ADMIN.getLevel()) {
			text = "Administrador do Sistema";
		} else if (companyUser.getAccessLevel() == AccessLevels.COMPANY_ADMIN.getLevel()) {
			text = "Administrador da Instituição: " + companyUser.getCompany().getName();
		} else if (companyUser.getAccessLevel() == AccessLevels.MANAGER.getLevel()) {
			text = "Gerente da Instituição: " + companyUser.getCompany().getName();
		} else if (companyUser.getAccessLevel() == AccessLevels.COLABORATOR.getLevel()) {
			text = "Colaborador da Instituição: " + companyUser.getCompany().getName();
		} else if (companyUser.getAccessLevel() == AccessLevels.AUDITOR.getLevel()) {
			text = "Auditor da Instituição: " + companyUser.getCompany().getName();
		} else if (companyUser.getAccessLevel() == AccessLevels.AUTHENTICATED.getLevel()) {
			text = "Usuário da Instituição: " + companyUser.getCompany().getName();
		}
		return text;
	}

	/**
	 * Retornar o nome da permissão do usuário no sistema.
	 * 
	 * @param permission
	 *            Permissão do usuário.
	 * @return String Nome da permissão do usuário.
	 */
	public String getPermissionText(UserPermission permission) {
		String text = "";
		if (permission.getPermission().equals(ManageUsersPermission.class.getCanonicalName())) {
			text = "Gerenciar Usuários na Instituição: " + permission.getCompany().getName();
		} else if (permission.getPermission().equals(ViewUsersPermission.class.getCanonicalName())) {
			text = "Visualizar Usuários na Instituição: " + permission.getCompany().getName();
		} else if (permission.getPermission().equals(ManagePlanMacroPermission.class.getCanonicalName())) {
			text = "Gerenciar Planos na Instituição: " + permission.getCompany().getName();
		} else if (permission.getPermission().equals(ManageDocumentPermission.class.getCanonicalName())) {
			text = "Gerenciar Documentos na Instituição: " + permission.getCompany().getName();
		} else if (permission.getPermission().equals(ManagePlanPermission.class.getCanonicalName())) {
			text = "Gerenciar Planos de Metas na Instituição: " + permission.getCompany().getName();
		} else if (permission.getPermission().equals(UpdateGoalPermission.class.getCanonicalName())) {
			text = "Atualizar Metas na Instituição: " + permission.getCompany().getName();
		}
		return text;
	}

	private class EmailBuilder {

		private String subject;
		private String body;

		public String getSubject() {
			return this.subject;
		}

		public String getBody() {
			return this.body;
		}

		/**
		 * Enviar email de notificação para o usuário.
		 * 
		 * @param type
		 *            Tipo da notificação.
		 * @param extras
		 *            Campo com as informações para gerar a mensagem do e-mail
		 *            da notificação.
		 */
		public EmailBuilder(NotificationType type, String... extras) {
			this.subject = this.getSubjectByType(type);
			switch (type) {
			case WELCOME:
				this.body = this.mountWelcomeEmail();
				break;
			case INVITE_USER:
				this.body = this.mountInviteUserEmail(extras[0], extras[1]);
				break;
			case RECOVER_PASSWORD:
				this.body = this.mountRecoverEmail(extras[0], extras[1]);
				break;
			default:
				this.body = this.mountNotificationEmail(extras[0], extras[1]);
				break;
			}
		}

		/**
		 * Retorna o nome do tipo da notificação.
		 * 
		 * @param type
		 *            Tipo da notificação.
		 * @return String Nome do tipo da notificação.
		 */
		private String getSubjectByType(NotificationType type) {
			switch (type) {
			case WELCOME:
				return "Bem-vindo ao ForPDI";
			case ACCESSLEVEL_CHANGED:
				return "Seu nível de acesso no ForPDI foi alterado";
			case PERMISSION_CHANGED:
				return "Suas permissões no ForPDI foram alteradas";
			case PLAN_MACRO_CREATED:
				return "Um novo plano foi criado na sua instituição do ForPDI";
			case PLAN_CREATED:
				return "Um novo plano de metas foi criado na sua instituição do ForPDI";
			case ATTRIBUTED_RESPONSIBLE:
				return "Você foi atribuido como responsável por um nível no ForPDI";
			case GOAL_CLOSED:
				return "Uma meta foi concluída no ForPDI";
			case GOAL_OPENED:
				return "Uma meta foi reaberta no ForPDI";
			case PLAN_CLOSE_TO_MATURITY:
				return "Um plano de metas está próximo a data de finalização no ForPDI";
			case GOAL_CLOSE_TO_MATURITY:
				return "Uma meta está próxima a data de vencimento no ForPDI";
			case LATE_GOAL:
				return "Uma meta está atrasada no ForPDI";
			case INVITE_USER:
				return "Complete seu cadastro no ForPDI";
			case RECOVER_PASSWORD:
				return "Recuperação de Senha de Acesso ao ForPDI";
			case ACTION_PLAN_CLOSED:
				return "Plano de ação foi concluído no ForPDI";
			case LATE_ACTION_PLAN:
				return "Um plano de ação está atrasado no ForPDI";
			case ACTION_PLAN_CLOSE_TO_MATURITY:
				return "Um plano de ação está próximo do vencimento no ForPDI";
			case DATE_ATTRIBUTE_UPDATED:
				return "Ocorreu uma alteração de data";
			case FORRISCO_PROCESS_CREATED:
				return "ForRisco - A sua unidade foi relacionada a um processo";
			case FORRISCO_RISK_CLOSE_TO_MATURITY:
				return "ForRisco - O risco está com monitoramento vencido";
			default:
				return "";
			}
		}

		/**
		 * Mensagem base do email para os vários tipos de notificação.
		 * 
		 * @param msg
		 *            Mensagem da notificação.
		 * @return Mensagem do e-mail para os vários tipos de notificação.
		 */
		private String mountNotificationEmail(String msg, String url) {
			return ("<meta charset='utf-8'>" + "<div>" + "<div>"
					+ "<div><p style='margin-bottom: 30px'>Se não for possível ver esse email, acesse o link: " + url
					+ " </p>	" + "<center style='margin-top: 50px'>"
					+ "<img src='http://cloud.progolden.com.br/file/8345' alt='ForPDI Logo' width='126' height='110'>"
					+ "<h3 style='font-family:sans-serif;font-size:1rem;color:#293a59;margin-top: 25px;'>"
					+ "Plataforma Aberta para Gest&atilde;o e Acompanhamento do <br>Plano de Desenvolvimento Institucional - PDI"
					+ "</h3>"
					+ "<center style='margin: 25px;width: 600px;height: 350px;border-top: 4px solid #0383D9;border-right: 1px solid #CCC;"
					+ "border-bottom: 1px solid #CCC;border-left: 1px solid #CCC;'>"
					+ "<h1 style='color: #0A4068;font-family: sans-serif;'>Notifica&ccedil;&atilde;o</h1>"
					+ "<p style='margin-bottom: 80px;margin-top: 90px;font-family: sans-serif;color: #9C9C9C;width: 90%;'>"
					+ msg + "</p>"
					+ "<p><a style='text-decoration: none;background-color: #1C486D;color: #FFF;padding-top: 8px;padding-bottom: 8px;"
					+ "padding-left: 20px;padding-right: 20px;border-radius: 7px;font-family: sans-serif;' href='" + url
					+ "'>" + "Acesse o ForPDI" + "</a></p>" + "</center>"
					+ "<center style='margin-top: 30px;font-family: sans-serif; font-size: 12px;'>"
					+ "<p style='color: #1C486D;font-weight: 600;'>ForPDI - Todos os direitos reservados</p>"
					+ "<a style='color: #9C9C9C;text-decoration: none;' href='www.forpdi.org'>www.forpdi.org</a>"
					+ "</center>" + "</center>" + "</div>" + "</div>" + "</div>");
		}

		/**
		 * Email de convite do usuário.
		 * 
		 * @param user
		 *            Nome do usuário convidado.
		 * @param url
		 *            Url para usuário completar cadastro no sistema.
		 * @return String Mensagem do e-mail de convite do usuário.
		 */
		private String mountInviteUserEmail(String user, String url) {
			return ("<meta charset='utf-8'>" + "<div>" + "<div>"
					+ "<div><p style='margin-bottom: 30px'>Se não for possível ver esse email, acesse o link: " + url
					+ " </p>	" + "<center style='margin-top: 50px'>"
					+ "<img src='http://cloud.progolden.com.br/file/8345' alt='ForPDI Logo' width='126' height='110'>"
					+ "<h3 style='font-family:sans-serif;font-size:1rem;color:#293a59;margin-top: 25px;'>"
					+ "Plataforma Aberta para Gest&atilde;o e Acompanhamento do <br>Plano de Desenvolvimento Institucional - PDI"
					+ "</h3>"
					+ "<center style='margin: 25px;width: 600px;height: 350px;border-top: 4px solid #0383D9;border-right: 1px solid"
					+ " #CCC;border-bottom: 1px solid #CCC;border-left: 1px solid #CCC;'>"
					+ "<h1 style='color: #0A4068;font-family: sans-serif;'>" + "Completar Cadastro" + "</h1>"
					+ "<p style='margin-top: 20px;font-family: sans-serif;color: #9C9C9C;margin-bottom: 30px;width: 90%;'>"
					+ "Ol&aacute; " + user
					+ ", <br>voc&ecirc; foi convidado para acessar o ForPDI<br> Para completar o cadastro "
					+ "clique no bot&atilde;o abaixo:" + "</p>"
					+ "<p><a style='text-decoration: none;background-color: #1C486D;color: #FFF;padding-top: 8px;padding-bottom: 8px;padding-left: 20px;"
					+ "padding-right: 20px;border-radius: 7px;font-family: sans-serif;' href='" + url + "'>"
					+ "Complete seu cadastro" + "</a></p>"
					+ "<p style='font-family: sans-serif;color: #9C9C9C;margin-top: 50px'>"
					+ "Ou cole em seu navegador de internet o link:" + "</p>"
					+ "<a style='font-family: sans-serif;color: #1C486D;font-size: 12px;word-wrap: break-word;'>" + url
					+ "</a>" + "<p style='font-family: sans-serif;color: #9C9C9C;'>"
					+ "Atenciosamente<br> Equipe do ForPDI" + "</p>" + "</center>"
					+ "<center style='margin-top: 30px;font-family: sans-serif; font-size: 12px;'>"
					+ "<p style='color: #1C486D;font-weight: 600;'>" + "ForPDI - Todos os direitos reservados" + "</p>"
					+ "<a style='color: #9C9C9C;text-decoration: none;' href='www.forpdi.org'>" + "www.forpdi.org"
					+ "</a>" + "</center>" + "</center>" + "</div>" + "</div>" + "</div>");
		}

		/**
		 * Mensagem de e-mail de notificação do tipo Bem-Vindo.
		 * 
		 * @return String Mensagem do e-mail de notificação do tipo Bem-Vindo.
		 */
		private String mountWelcomeEmail() {
			return ("<meta charset='utf-8'>" + "<div>" + "<div>"
					+ "<div><p style='margin-bottom: 30px'>Se não for possível ver esse email, acesse o link: "
					+ domain.getBaseUrl() + " </p>	" + "<center style='margin-top: 50px'>"
					+ "<img src='http://cloud.progolden.com.br/file/8345' alt='ForPDI Logo' width='126' height='110'>"
					+ "<h3 style='font-family:sans-serif;font-size:1rem;color:#293a59;margin-top: 25px;'>"
					+ "Plataforma Aberta para Gest&atilde;o e Acompanhamento do <br>Plano de Desenvolvimento Institucional - PDI"
					+ "</h3>"
					+ "<center style='margin: 25px;width: 600px;height: 400px;border-top: 4px solid #0383D9;border-right: 1px solid #CCC;"
					+ "border-bottom: 1px solid #CCC;border-left: 1px solid #CCC;'>"
					+ "<h1 style='color: #0A4068;font-family: sans-serif;'>Seja bem vindo(a) ao ForPDI</h1>"
					+ "<p style='margin-top: 20px;font-family: sans-serif;color: #9C9C9C;margin-bottom: 30px;font-size: 14px'>"
					+ "O ForPDI &eacute; uma plataforma aberta para gest&atilde;o e acompanhamento do Plano de<br> Desenvolvimento Institucional (PDI) de universidades federais e outras institui&ccedil;&otilde;es<br> p&uacute;blicas."
					+ "</p>" + "<center style='display: inline-flex;margin-bottom: 30px;'>"
					+ "<div style='width: 33%;padding: 5px;'>"
					+ "<img src='http://cloud.progolden.com.br/file/8347' style='width: 65px;'>"
					+ "<p style='color:#9C9C9C;font-family: sans-serif;font-size: 13px;'>"
					+ "Gerencie de uma forma f&aacute;cil e<br> intuitiva o Plano de<br> Desenvolvimento da sua<br> instiui&ccedil;&atilde;o."
					+ "</p>" + "</div>" + "<div style='width: 33%;padding: 5px;'>"
					+ "<img src='http://cloud.progolden.com.br/file/8348' style='width: 65px;'>"
					+ "<p style='color:#9C9C9C;font-family: sans-serif;font-size: 13px;'>"
					+ "Acompanhe pelo Painel de<br> bordo as estat&iacute;sticas de sua<br> instiui&ccedil;&atilde;o de uma forma<br> interativa."
					+ "</p>" + "</div>" + "<div style='width: 33%;padding: 5px;'>"
					+ "<img src='http://cloud.progolden.com.br/file/8349' style='width: 65px;'>"
					+ "<p style='color:#9C9C9C;font-family: sans-serif;font-size: 13px;'>"
					+ "Gere o documento do PDI<br> automaticamente de uma<br> forma totalmente<br> personalizada."
					+ "</p>" + "</div>" + "</center>"
					+ "<p><a style='text-decoration: none;background-color: #1C486D;color: #FFF;padding-top: 8px;padding-bottom: 8px;padding-left: 20px;padding-right: 20px;border-radius: 7px;font-family: sans-serif;' href='"
					+ domain.getBaseUrl() + "'>" + "Acesse o ForPDI" + "</a></p>" + "</center>"
					+ "<center style='margin-top: 30px;font-family: sans-serif; font-size: 12px;'>"
					+ "<p style='color: #1C486D;font-weight: 600;'>ForPDI - Todos os direitos reservados</p>"
					+ "<a style='color: #9C9C9C;text-decoration: none;' href='www.forpdi.org'>www.forpdi.org</a>"
					+ "</center>" + "</center>" + "</div>" + "</div>" + "</div>");
		}

		/**
		 * Mensagem de notificação de e-mail de recuperação de senha.
		 * 
		 * @param user
		 *            Usuário que solicitou a recuperação da senha.
		 * @param url
		 *            Url para o usuário recuperar sua senha.
		 * @return
		 */
		private String mountRecoverEmail(String user, String url) {
			return ("<meta charset='utf-8'>" + "<div>" + "<div>"
					+ "<div><p style='margin-bottom: 30px'>Se não for possível ver esse email, acesse o link: " + url
					+ " </p>	" + "<center style='margin-top: 50px'>"
					+ "<img src='http://cloud.progolden.com.br/file/8345' alt='ForPDI Logo' width='126' height='110'>"
					+ "<h3 style='font-family:sans-serif;font-size:1rem;color:#293a59;margin-top: 25px;'>"
					+ "Plataforma Aberta para Gest&atilde;o e Acompanhamento do <br>Plano de Desenvolvimento Institucional - PDI"
					+ "</h3>"
					+ "<center style='margin: 25px;width: 600px;height: 350px;border-top: 4px solid #0383D9;border-right: 1px solid #CCC;"
					+ "border-bottom: 1px solid #CCC;border-left: 1px solid #CCC;'>"
					+ "<h2 style='color: #0A4068;font-family: sans-serif;'>Recupera&ccedil;&atilde;o de Senha de Acesso ao ForPDI</h2>"
					+ "<p style='margin-top: 60px;font-family: sans-serif;color: #9C9C9C;margin-bottom: 80px;font-size: 14px;width: 90%;'>"
					+ "Ol&aacute; " + user
					+ ",<br> voc&ecirc; solicitou a recuperação de seus dados de acesso ao ForPDI.<br> Para alterar sua senha acesse:<br> <a href='"
					+ url + "'>" + url + "</a> <br><br><br>Atenciosamente, Equipe do ForPDI." + "</p>"
					+ "<p><a style='background-color: #1C486D;color: #FFF;padding-top: 8px;padding-bottom: 8px;padding-left: 20px;padding-right: 20px;border-radius: 7px;font-family: sans-serif;text-decoration: none' href='"
					+ url + "'>" + "Acesse o ForPDI" + "</a></p>" + "</center>"
					+ "<center style='margin-top: 30px;font-family: sans-serif; font-size: 12px;'>"
					+ "<p style='color: #1C486D;font-weight: 600;'>ForPDI - Todos os direitos reservados</p>"
					+ "<a style='color: #9C9C9C;text-decoration: none;' href='www.forpdi.org'>www.forpdi.org</a>"
					+ "</center>" + "</center>" + "</div>" + "</div>" + "</div>");
		}
	}

	/**
	 * Salvar uma mensagem enviada.
	 * 
	 * @param subject
	 *            Assunto da mensagem.
	 * @param message
	 *            Corpo da mensagem.
	 * @param user
	 *            Usuário que vai receber a mensagem.
	 * 
	 * @return messageHistory Mensagem que foi salva.
	 */
	public MessageHistory saveMessageHistory(String subject, String message, User user) {

		MessageHistory messageHistory = new MessageHistory();
		messageHistory.setSubject(subject);
		messageHistory.setMessage(message);
		messageHistory.setUserSender(this.userSession.getUser());
		messageHistory.setUserReceiver(user);
		messageHistory.setCompany(this.domain.getCompany());
		this.persist(messageHistory);
		return messageHistory;
	}

	/**
	 * Enviar uma mensagem.
	 * 
	 * @param messageHistory
	 *            Mensagem que será enviada.
	 * 
	 * @return messageHistory Mensagem que foi salva.
	 */
	public void sendMessage(MessageHistory messageHistory) throws EmailException {
		this.emailTask.add(new NotificationEmail(messageHistory.getUserReceiver().getEmail(),
				messageHistory.getUserReceiver().getName(), messageHistory.getSubject(),
				"<" + "p style=\"font-size:12pt;\">" + "Você recebeu uma mensagem de "
						+ this.userSession.getUser().getName() + "<br/><br/>" + messageHistory.getMessage() + "</p>", null));
		Notification notification = new Notification();
		notification.setUser(messageHistory.getUserReceiver());
		notification.setCompany(this.domain.getCompany());
		notification.setCreation(new Date());
		notification.setOnlyEmail(false);
		notification.setVizualized(false);
		notification.setPicture(NotificationType.SEND_MESSAGE.getImageUrl());
		notification.setType(NotificationType.SEND_MESSAGE.getValue());
		notification.setDescription("<b>Mensagem de " + messageHistory.getUserSender().getName() + ".</b> "
				+ messageHistory.getSubject() + ": " + messageHistory.getMessage());
		this.persist(notification);
	}

	/**
	 * Listar as mensagens enviadas para um usuário
	 * 
	 * @param user
	 *            Usuário que recebeu as mensagens.
	 * 
	 */
	public PaginatedList<MessageHistory> listMessageHistory(User user, Long limit, Integer page) {

		PaginatedList<MessageHistory> results = new PaginatedList<MessageHistory>();

		Criteria criteria = this.dao.newCriteria(MessageHistory.class);
		criteria.add(Restrictions.eq("userSender", this.userSession.getUser()));
		criteria.add(Restrictions.eq("userReceiver", user));
		criteria.add(Restrictions.eq("company", this.domain.getCompany()));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.addOrder(Order.desc("creation"));

		if (limit != null) {
			criteria.setFirstResult((int) ((page - 1) * limit));
			criteria.setMaxResults(limit.intValue());
		}

		List<MessageHistory> messageHistoryList = this.dao.findByCriteria(criteria, MessageHistory.class);

		Criteria counting = this.dao.newCriteria(MessageHistory.class);
		counting.setProjection(Projections.countDistinct("id"));
		counting.add(Restrictions.eq("userSender", this.userSession.getUser()));
		counting.add(Restrictions.eq("userReceiver", user));
		counting.add(Restrictions.eq("company", this.domain.getCompany()));
		counting.add(Restrictions.eq("deleted", false));

		results.setList(messageHistoryList);
		results.setTotal((Long) counting.uniqueResult());
		return results;

	}

}
