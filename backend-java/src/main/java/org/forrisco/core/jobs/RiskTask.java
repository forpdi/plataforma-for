package org.forrisco.core.jobs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forpdi.core.jobs.NotificationEmail;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.User;
import org.forrisco.risk.Monitor;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskBS;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.jboss.logging.Logger;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.Unbound;

import br.com.caelum.vraptor.boilerplate.Business;
import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
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
@Scheduled(fixedRate = 3600000)
public class RiskTask  implements Task {
	
	@Inject @Unbound RequestContext requestContext;
	@Inject private NotificationBS notificationBS;
	@Inject private RiskBS riskBS;
	
	    
	/**
	 * Método que é executado a cada hora,
	 * 
	 */
	@Override

	public void execute() {
		requestContext.activate();
		ExpiredRisks();
		requestContext.deactivate();
	}

	
	
	/**
	 *
	 *  verificando se algum risco mudou de "Em dia" para "Próximo a vencer"
	 * 
	 */
	private void ExpiredRisks() {
		Logger LOG = Logger.getLogger(EmailSenderTask.class);
		LOG.infof("Verificando monitoramento dos Riscos...");
		
		PaginatedList<Risk> list= this.riskBS.listRiskState(1);
		for(Risk risk: list.getList()) {
			
			Date maturity = this.riskBS.CloseToMaturityDate(risk);
			if(new Date().getTime()- maturity.getTime()< 1000*60*60 
				&& new Date().getTime()- maturity.getTime()>0) {//intervalo de uma hora
		
				//enviar email de notificação de monitoramento vencido
				String texto="["+risk.getName()+"]";
				CompanyDomain companyDomain = riskBS.companyDomainByRisk(risk);
				String url=companyDomain.getBaseUrl()+"/#/forrisco/plan-risk/"+risk.getUnit().getPlanRisk().getId()+"/unit/"+risk.getUnit().getId()+"/risk/"+risk.getId()+"/monitor";
				
				try {				
					//falta adicionar notificacao p/ os gerentes e administradores do sistema
					//calcular corretamente o valor de próximo a vencer
					this.notificationBS.sendNotificationEmail(NotificationType.FORRISCO_RISK_CLOSE_TO_MATURITY, texto, "aux", risk.getUser(), url, companyDomain);
				} catch (Throwable ex) {
					LOG.errorf(ex, "Unexpected error occurred.");
				}
			}
		}
	}
	

}
