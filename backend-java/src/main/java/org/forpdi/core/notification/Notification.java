package org.forpdi.core.notification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.forpdi.core.company.Company;
import org.forpdi.core.user.User;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

	/**
	 * @author Rodrigo de Freitas Santos
	 * 
	 */
	@Entity(name = Notification.TABLE)
	@Table(name = Notification.TABLE)
	public class Notification extends SimpleLogicalDeletableEntity {
		public static final String TABLE = "fpdi_notification";
		private static final long serialVersionUID = 1L;
		
		@Column(nullable=true, length=255)
		private String picture;
		
		@Column(nullable=false, length=4000)
		private String description;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(nullable=false)
		private Date creation = new Date();
		
		@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER, optional=false)
		private User user;
		
		@ManyToOne(targetEntity=Company.class, fetch=FetchType.EAGER, optional=false)
		private Company company;
		
		private boolean vizualized = false;
		
		private boolean onlyEmail = false;
		
		//NotificationType
		private Integer type;
		
		@Transient
		private boolean vizualizeNow;
		
		@Column(nullable=true, length=255)
		private String url;
		
		
		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Date getCreation() {
			return creation;
		}

		public void setCreation(Date creation) {
			this.creation = creation;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company company) {
			this.company = company;
		}

		public boolean isVizualized() {
			return vizualized;
		}

		public void setVizualized(boolean vizualized) {
			this.vizualized = vizualized;
		}

		public boolean isOnlyEmail() {
			return onlyEmail;
		}

		public void setOnlyEmail(boolean onlyEmail) {
			this.onlyEmail = onlyEmail;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public boolean isVizualizeNow() {
			return vizualizeNow;
		}

		public void setVizualizeNow(boolean vizualizeNow) {
			this.vizualizeNow = vizualizeNow;
		}
		
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
