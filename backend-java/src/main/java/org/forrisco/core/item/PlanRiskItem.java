package org.forrisco.core.item;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forrisco.core.plan.PlanRisk;
import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Juliano Afonso
 * 
 */
@Entity(name = PlanRiskItem.TABLE)
@Table(name = PlanRiskItem.TABLE)

public class PlanRiskItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_plan_risk_item";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, length=255)
	private String name;
	
	@Column(nullable = true, length=4000)
	private String description;
	
	@SkipSerialization
	@ManyToOne(targetEntity=PlanRisk.class, optional=false, fetch=FetchType.EAGER)
	private PlanRisk planRisk;
	
	@Transient
	private List<PlanRiskItemField> planRiskItemField;
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PlanRisk getPlanRisk() {
		return planRisk;
	}

	public void setPlanRisk(PlanRisk planRisk) {
		this.planRisk = planRisk;
	}

	public List<PlanRiskItemField> getPlanRiskItemField() {
		return planRiskItemField;
	}

	public void setPlanRiskItemField(List<PlanRiskItemField> planRiskItemField) {
		this.planRiskItemField = planRiskItemField;
	}

	public void setName(String name) {
		this.name = name;
	}
}
