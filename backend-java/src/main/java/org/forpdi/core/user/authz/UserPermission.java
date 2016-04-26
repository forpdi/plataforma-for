package org.forpdi.core.user.authz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.company.Company;
import org.forpdi.core.user.User;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = UserPermission.TABLE)
@Table(name = UserPermission.TABLE)
public class UserPermission implements Serializable {
	public static final String TABLE = "fpdi_user_permission";
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(targetEntity=Company.class, fetch=FetchType.EAGER, optional=false)
	private Company company;

	@Id
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER, optional=false)
	private User user;
	
	@Id
	@Column(nullable=false, length=255)
	private String permission;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
