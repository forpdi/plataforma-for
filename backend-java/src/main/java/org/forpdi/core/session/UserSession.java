package org.forpdi.core.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import org.forpdi.core.user.User;

/**
 * @author Rodrigo Freitas
 *
 */
@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private User user;
	
	public void login(User user) {
		this.logout();
		this.user = user;
	}
	
	public void logout() {
		this.user = null;
	}
	
	public boolean isLogged() {
		return (this.user != null);
	}

	public User getUser() {
		return user;
	}
	
}
