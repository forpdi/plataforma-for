package org.forrisco.risk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.user.User;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = PreventiveAction.TABLE)
@Table(name = PreventiveAction.TABLE)

public class PreventiveAction extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_preventive_action";
	private static final long serialVersionUID = 1L;

	@ManyToOne(targetEntity = Risk.class, optional = false, fetch = FetchType.EAGER)
	private Risk risk;

	@ManyToOne(targetEntity = User.class, optional = false, fetch = FetchType.EAGER)
	private User user;

	@Column(nullable = false, length = 4000)
	private String action;

	@Column(nullable = false)
	private boolean accomplished;

	public PreventiveAction() {
	}

	public PreventiveAction(PreventiveAction action) {
		this.user = action.getUser();
		this.action = action.getAction();
		this.accomplished = action.isAccomplished();
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isAccomplished() {
		return accomplished;
	}

	public void setAccomplished(boolean accomplished) {
		this.accomplished = accomplished;
	}

}