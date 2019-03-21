package org.forpdi.core.notification;


import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.auth.UserSession;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Rafael S. Lima
 */
@Controller
public class NotificationController extends AbstractController {
	
	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private UserSession userSession;
	@Inject
	private NotificationBS bs;
	@Inject
	private UserBS userBS;
	
	/**
	 * Recuperação do número de notificações não lidas 
	 * @param void
	 * @return notifications
	 * 		número de notificações não lidas e verificação de necessidade de logout por bloqueio
	 * 
	 */
	@Get("/api/notification/verifynotifications")
	@NoCache
	public void verifyNotifications() {
		try {
			Long notifications = (long) 0; 
			if (this.userSession.isLogged() && this.userSession.getUser() != null) {
				CompanyUser companyUser = this.userBS.retrieveCompanyUser(this.userSession.getUser(), this.domain.getCompany());
				if(companyUser != null && companyUser.isBlocked()) {
					this.success(-1);
					return;
				}
				else {
					User existent = this.userBS.existsByUser(this.userSession.getUser().getId());
					notifications = this.bs.countNotifications(existent);
				}
			}
			this.success(notifications);
		} catch (Throwable ex){
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail();
		}
	}
	
	/**
	 * Recuperação das notificações do usuário 
	 * @param limit
	 * 		limite de notificações por página
	 * @param page
	 * 		número da página a ser listada
	 * @return list
	 * 		lista de notificações
	 * 
	 */
	@Get("/api/notification/notifications")
	@NoCache
	public void listNotifications(Long limit, Integer page, boolean topBar) {
		try {
			if (page == null)
				page = 1;
			User existent = this.bs.exists(this.userSession.getUser().getId(), User.class);
			PaginatedList<Notification> list = this.bs.listNotificationByUserCompany(existent, limit, page, topBar);
			for (Notification notification : list.getList()) {
				if (!notification.isVizualized()) {
					notification.setVizualized(true);
					notification.setVizualizeNow(true);
					this.bs.persist(notification);
				}
			}
			this.success(list);
		} catch (Throwable ex){
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail();
		}
	}
	
	/**
	 * Enviar uma mensagem para um usuário
	 * 
	 * @param subject
	 *            Assunto da mensagem.
	 * @param message
	 * 			  Corpo da mensagem.
	 * @param userId
	 * 			  Id do usuário que vai receber a mensagem.
	 *            
	 */
	@Post("/api/structure/sendmessage")
	@NoCache
	@Consumes
	public void sendMessage(String subject, String message, Long userId) {
		try {
			User user = this.userBS.existsByUser(userId);
			MessageHistory messageHistory = new MessageHistory();
			messageHistory = this.bs.saveMessageHistory(subject, message, user);
			this.bs.sendMessage(messageHistory);
			this.success(messageHistory);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Listar as mensagens enviadas para um usuário
	 * 
	 * @param userId
	 * 			  Id do usuário que recebeu as mensagens.
	 *            
	 */
	@Get(BASEPATH + "/structure/listmessages")
	@NoCache
	@Consumes
	public void listMessages(Long userId,Long limit, Integer page) {
		try {
			User user = this.userBS.existsByUser(userId);
			PaginatedList<MessageHistory> messageHistoryList = new PaginatedList<MessageHistory>();
			if (page == null) {
				page = 1;
			}
				
			messageHistoryList = this.bs.listMessageHistory(user,limit,page);
			this.success(messageHistoryList);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
		

}
