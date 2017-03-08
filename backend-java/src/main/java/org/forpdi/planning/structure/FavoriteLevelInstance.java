package org.forpdi.planning.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.user.User;

/**
 * @author Rodrigo de Freitas Santos
 * 
 */
@Entity(name = FavoriteLevelInstance.TABLE)
@Table(name = FavoriteLevelInstance.TABLE)
public class FavoriteLevelInstance implements Serializable {
	public static final String TABLE = "fpdi_favorite_level_instance";
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(targetEntity = StructureLevelInstance.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevelInstance levelInstance;
	
	@Id
	@ManyToOne(targetEntity = CompanyUser.class, optional = false, fetch = FetchType.EAGER)
	private CompanyUser companyUser;
	
	private boolean deleted = false;

	public StructureLevelInstance getLevelInstance() {
		return levelInstance;
	}

	public void setLevelInstance(StructureLevelInstance levelInstance) {
		this.levelInstance = levelInstance;
	}

	public CompanyUser getCompanyUser() {
		return companyUser;
	}

	public void setCompanyUser(CompanyUser companyUser) {
		this.companyUser = companyUser;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
