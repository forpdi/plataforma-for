package org.forrisco.core.unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;



import org.forpdi.core.user.User;
import org.forrisco.core.plan.PlanRisk;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Unit.TABLE)
@Table(name = Unit.TABLE)

public class Unit extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_unit";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = false, length=255)
	private String abbreviation;
	
	@Column(nullable = true, length=4000)
	private String description;
	
	@SkipSerialization
	@OneToOne(targetEntity=Unit.class, optional=false, fetch=FetchType.EAGER)
	private Unit parent;
	
	@SkipSerialization
	@ManyToOne(targetEntity=User.class, optional=false, fetch=FetchType.EAGER)
	private User user;

	@SkipSerialization
	@ManyToOne(targetEntity=PlanRisk.class, optional=false, fetch=FetchType.EAGER)
	private PlanRisk planRisk;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Unit getParent() {
		return parent;
	}

	public void setParent(Unit parent) {
		this.parent = parent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PlanRisk getPlan() {
		return planRisk;
	}

	public void setPlan(PlanRisk plan) {
		this.planRisk = plan;
	}

}