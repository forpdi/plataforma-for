package org.forpdi.core.evaluation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.forpdi.core.user.User;

import br.com.caelum.vraptor.boilerplate.SimpleEntity;


/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Audit.TABLE)
@Table(name = Audit.TABLE)
public class Audit extends SimpleEntity {
	public static final String TABLE = "fpdi_audit";
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

}