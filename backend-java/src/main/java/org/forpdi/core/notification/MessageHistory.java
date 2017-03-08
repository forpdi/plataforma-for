package org.forpdi.core.notification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.forpdi.core.company.Company;
import org.forpdi.core.user.User;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Rodrigo de Freitas Santos
 * 
 */
@Entity(name = MessageHistory.TABLE)
@Table(name = MessageHistory.TABLE)
public class MessageHistory extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_message_history";
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=70)
	private String subject;
	
	@Column(nullable=false, length=255)
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date creation = new Date();
	
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER, optional=false)
	private User userSender;
	
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER, optional=false)
	private User userReceiver;
	
	@ManyToOne(targetEntity=Company.class, fetch=FetchType.EAGER, optional=false)
	private Company company;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public User getUserSender() {
		return userSender;
	}

	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}

	public User getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(User userReceiver) {
		this.userReceiver = userReceiver;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
