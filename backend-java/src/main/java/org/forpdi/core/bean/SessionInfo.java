package org.forpdi.core.bean;

import java.io.Serializable;

import org.forpdi.core.user.User;
import org.forpdi.core.user.auth.UserAccessToken;
import org.forpdi.core.user.auth.UserSession;

/**
 * 
 * @author Renato R. R. de Oliveira
 *
 */
public class SessionInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String token;
	private long ttl;
	
	public SessionInfo(UserSession session) {
		UserAccessToken accessToken = session.getToken();
		this.user = accessToken.getUser();
		this.token = accessToken.getToken();
		this.ttl = (accessToken.getTtl()*1000L) - (System.currentTimeMillis()-accessToken.getCreation().getTime());
		
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	
	
}
