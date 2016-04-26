package org.forpdi.core.user.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.persistence.Transient;

import org.forpdi.core.event.AuthEvent;
import org.forpdi.core.event.Login;
import org.forpdi.core.event.Logout;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.authz.UserPermission;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Rodrigo Freitas
 *
 */
@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private UserAccessToken accessToken;
	private List<String> permissions;
	private int accessLevel = 0;
	
	@Inject @Login
	@Transient
	private Event<AuthEvent> loginEvent;
	
	@Inject @Logout
	@Transient
	private Event<AuthEvent> logoutEvent;
	
	public void login(UserAccessToken token) {
		this.logout();
		this.accessToken = token;
		
		UserBS bs = CDI.current().select(UserBS.class).get();
		List<UserPermission> perms = bs.retrievePermissions(token.getUser());
		if (GeneralUtils.isEmpty(perms)) {
			this.permissions = new ArrayList<String>();
		} else {
			this.permissions = new ArrayList<String>(perms.size());
			for (UserPermission perm : perms) {
				this.permissions.add(perm.getPermission());
			}
		}
		
		this.accessLevel = bs.retrieveAccessLevel(token.getUser());
		this.loginEvent.fire(new AuthEvent(this.accessToken));
	}
	
	public void logout() {
		if (this.accessToken != null) {
			this.logoutEvent.fire(new AuthEvent(this.accessToken));
			this.accessToken = null;
			this.permissions = null;
			this.accessLevel = 0;
		}
	}
	
	public boolean isLogged() {
		return (this.accessToken != null);
	}

	public int getAccessLevel() {
		return this.accessLevel;
	}

	public List<String> getPermissions() {
		return this.permissions;
	}

	public User getUser() {
		return this.accessToken.getUser();
	}
	
	public UserAccessToken getToken() {
		return this.accessToken;
	}
	
}
