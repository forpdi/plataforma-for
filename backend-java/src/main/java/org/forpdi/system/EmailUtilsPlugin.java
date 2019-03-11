package org.forpdi.system;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.forpdi.core.properties.SystemConfigs;

import br.com.caelum.vraptor.boilerplate.util.EmailUtils;

public class EmailUtilsPlugin {
	
	/**
	 * Envia email de notificações
	 * @param toEmail
	 * 			Email
	 * @param toName
	 * 			Nome
	 * @param subject
	 * 			Assunto
	 * @param msg
	 * 		Mensagem
	 * @return
	 * @throws EmailException
	 */
	public static String sendSimpleEmail(String toEmail, String toName, String subject, String msg) throws EmailException {
		Email email = EmailUtils.getSimpleEmail();
		email.setAuthentication(SystemConfigs.getConfig("smtp.user"), SystemConfigs.getConfig("smtp.password"));
		email.setFrom(SystemConfigs.getConfig("smtp.from.email"), SystemConfigs.getConfig("smtp.from.name"));
		email.setHostName(SystemConfigs.getConfig("smtp.host"));
		email.setSmtpPort(Integer.parseInt(SystemConfigs.getConfig("smtp.port")));
		email.setSslSmtpPort(SystemConfigs.getConfig("smtp.port"));
		email.addTo(toEmail, toName);
		email.setSubject(subject);
		email.setMsg(msg);
		email.setSSLOnConnect("true".equals(SystemConfigs.getConfig("smtp.ssl")));
		email.setStartTLSEnabled("true".equals(SystemConfigs.getConfig("smtp.tls")));
		//email.setStartTLSRequired(true);
		return email.send();
	}
	
	/**
	 * Eviar email com imagens e outros contéudos
	 * @param toEmail
	 * 			Email
	 * @param toName
	 * 			Nome
	 * @param subject
	 * 			Assunto
	 * @param msg
	 * 			Mensagem
	 * @return
	 * @throws EmailException
	 */
	public static String sendHtmlEmail(String toEmail, String toName, String subject, String msg, String attach) throws EmailException {
		MultiPartEmail email = EmailUtils.getHtmlEmail();
		email.setAuthentication(SystemConfigs.getConfig("smtp.user"), SystemConfigs.getConfig("smtp.password"));
		email.setFrom(SystemConfigs.getConfig("smtp.from.email"), SystemConfigs.getConfig("smtp.from.name"));
		email.setHostName(SystemConfigs.getConfig("smtp.host"));
		email.setSmtpPort(Integer.parseInt(SystemConfigs.getConfig("smtp.port")));
		email.setSslSmtpPort(SystemConfigs.getConfig("smtp.port"));
		email.addTo(toEmail, toName);
		email.setSubject(subject);
		email.setMsg(msg);
		email.setSSLOnConnect("true".equals(SystemConfigs.getConfig("smtp.ssl")));
		email.setStartTLSEnabled("true".equals(SystemConfigs.getConfig("smtp.tls")));
		//email.setStartTLSRequired(true);

		// Create the attachment
		if(attach !=null) {
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(SystemConfigs.getConfig("store.files")+attach);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription(attach);
			attachment.setName(attach);
			email.attach(attachment);
		}
		
		return email.send();
	}
}
