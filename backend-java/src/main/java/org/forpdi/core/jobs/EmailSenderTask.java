package org.forpdi.core.jobs;

import java.util.LinkedList;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.mail.EmailException;
import org.forpdi.system.EmailUtilsPlugin;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

/**
 * Tarefa que é executada a cada minuto, para verificar se existe algum e-mail
 * pendente para ser enviado.
 * 
 * @author Pedro Mutter
 *
 * 
 */
@ApplicationScoped
@Scheduled(fixedRate = 60000)
public class EmailSenderTask implements Task {

	private static Logger LOG = Logger.getLogger(EmailSenderTask.class);
	private Queue<NotificationEmail> queue;

	public EmailSenderTask() {
		this.queue = new LinkedList<>();
	}

	/**
	 * Retorna a fila de e-mails pendentes à ser enviados.
	 * 
	 * @return queue, fila de e-mails.
	 */
	public Queue<NotificationEmail> getQueue() {
		return queue;
	}

	/**
	 * Adiciona um e-mail à fila, para ser enviado na próxima vez que a tarefa
	 * for executada.
	 * 
	 * @param email
	 */
	public synchronized void add(NotificationEmail email) {
		queue.add(email);
	}

	/**
	 * Método que é executado a cada minuto, verificando se existe e-mail na
	 * fila e os enviando.
	 */
	@Override
	public void execute() {
		LOG.infof("Executando envio de %d emails...", this.queue.size());
		while (!this.queue.isEmpty()) {
			try {
				NotificationEmail email = this.queue.poll();
				EmailUtilsPlugin.sendHtmlEmail(email.getEmail(), email.getName(), email.getSubject(), email.getBody());
			} catch (EmailException e) {
				LOG.error("Falha ao enviar e-mail.", e);
			}
		}
	}

}
