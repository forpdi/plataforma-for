package org.forpdi.core.company;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.user.User;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.notification.NotificationSetting;;

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
	
	private boolean blocked = false;
	private int accessLevel = AccessLevels.AUTHENTICATED.getLevel();
	private int notificationSetting = NotificationSetting.DEFAULT.getSetting();	

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

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	public int getNotificationSetting() {
		return notificationSetting;
	}

	public void setNotificationSetting(int notificationSetting) {
		this.notificationSetting = notificationSetting;
	}

	@Override
	public String toString() {
		return "CompanyUser [company=" + company.getName() + ", user=" + user.getName() + ", blocked=" + blocked + ", accessLevel="
				+ accessLevel + ", notificationSetting=" + notificationSetting + "]";
	}
	
	
}
