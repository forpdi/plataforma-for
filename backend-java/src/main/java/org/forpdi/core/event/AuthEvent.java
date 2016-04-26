package org.forpdi.core.event;

import org.forpdi.core.user.auth.UserAccessToken;

/**
 * 
 * @author Renato R. R. de Oliveira
 *
 */
public class AuthEvent {

	private UserAccessToken token;

	public AuthEvent(UserAccessToken token) {
		this.token = token;
	}
	
	public UserAccessToken getToken() {
		return token;
	}

	public void setToken(UserAccessToken token) {
		this.token = token;
	}
}
