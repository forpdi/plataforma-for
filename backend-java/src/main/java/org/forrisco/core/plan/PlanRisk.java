package org.forrisco.core.plan;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.forrisco.core.policy.Policy;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = PlanRisk.TABLE)
@Table(name = PlanRisk.TABLE)

public class PlanRisk extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_plan_risk";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=400)
	private String name;

	@Column(nullable = true, columnDefinition="longtext")
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true, name="validity_begin")
	private Date validityBegin;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true, name="validity_end")
	private Date validityEnd;

	//@SkipSerialization
	@ManyToOne(targetEntity=Policy.class, optional=false,  fetch=FetchType.EAGER)
	private Policy policy;

	private boolean archived = false;

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getValidityBegin() {
		return validityBegin;
	}

	public void setValidityBegin(Date validityBegin) {
		this.validityBegin = validityBegin;
	}

	public Date getValidityEnd() {
		return validityEnd;
	}

	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

}