package org.forpdi.core.user.auth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.forpdi.core.user.User;

/**
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@Entity(name = UserAccessToken.TABLE)
@Table(name = UserAccessToken.TABLE)
public class UserAccessToken implements Serializable {
	public static final String TABLE = "fpdi_user_access_token";
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable=false, length=128)
	private String token;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date creation;
	
	@Column(nullable=false, length=64)
	private String creationIp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date expiration;
	
	@Column(nullable=false)
	private Long ttl = 86400000L * 7L;

	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER, optional=false)
	private User user;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getCreationIp() {
		return creationIp;
	}

	public void setCreationIp(String creationIp) {
		this.creationIp = creationIp;
	}

	public Long getTtl() {
		return ttl;
	}

	public void setTtl(Long ttl) {
		this.ttl = ttl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
}
