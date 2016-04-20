package org.forpdi.core.event;

import org.forpdi.core.session.UserAccessToken;

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
