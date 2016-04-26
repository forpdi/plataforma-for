package org.forpdi.core.company;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.user.User;
import org.forpdi.core.user.authz.AccessLevels;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = CompanyUser.TABLE)
@Table(name = CompanyUser.TABLE)
public class CompanyUser implements Serializable {
	public static final String TABLE = "fpdi_company_user";
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(targetEntity=Company.class, fetch=FetchType.EAGER, optional=false)
	private Company company;

	@Id
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER, optional=false)
	private User user;
	
	private int accessLevel = AccessLevels.AUTHENTICATED.getLevel();

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

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
}
