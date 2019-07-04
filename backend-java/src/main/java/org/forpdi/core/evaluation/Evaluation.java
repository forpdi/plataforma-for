package org.forpdi.core.evaluation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.user.User;
import org.forpdi.planning.plan.PlanMacro;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Evaluation.TABLE)
@Table(name = Evaluation.TABLE)
public class Evaluation extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_evaluation";
	private static final long serialVersionUID = 1L;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private CompanyDomain companyDomain;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date creation = new Date();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CompanyDomain getCompanyDomain() {
		return companyDomain;
	}

	public void setCompanyDomain(CompanyDomain companyDomain) {
		this.companyDomain = companyDomain;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

}