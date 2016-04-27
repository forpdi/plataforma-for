package org.forpdi.core.bean;

import java.io.Serializable;
import java.util.List;

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
	private List<String> permissions;
	private int accessLevel;
	
	public SessionInfo(UserSession session) {
		UserAccessToken accessToken = session.getToken();
		this.user = accessToken.getUser();
		this.token = accessToken.getToken();
		this.ttl = (accessToken.getTtl()*1000L) - (System.currentTimeMillis()-accessToken.getCreation().getTime());
		this.permissions = session.getPermissions();
		this.accessLevel = session.getAccessLevel();
		
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

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	
}
