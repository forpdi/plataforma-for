package org.forpdi.core.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.Transient;

import org.forpdi.core.event.AuthEvent;
import org.forpdi.core.event.Login;
import org.forpdi.core.event.Logout;
import org.forpdi.core.user.User;

/**
 * @author Rodrigo Freitas
 *
 */
@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private UserAccessToken accessToken;
	
	@Inject @Login
	@Transient
	private Event<AuthEvent> loginEvent;
	
	@Inject @Logout
	@Transient
	private Event<AuthEvent> logoutEvent;
	
	public void login(UserAccessToken token) {
		this.logout();
		this.accessToken = token;
		this.loginEvent.fire(new AuthEvent(this.accessToken));
	}
	
	public void logout() {
		if (this.accessToken != null) {
			this.logoutEvent.fire(new AuthEvent(this.accessToken));
			this.accessToken = null;
		}
	}
	
	public boolean isLogged() {
		return (this.accessToken != null);
	}

	public User getUser() {
		return this.accessToken.getUser();
	}
	
	public UserAccessToken getToken() {
		return this.accessToken;
	}
	
}
