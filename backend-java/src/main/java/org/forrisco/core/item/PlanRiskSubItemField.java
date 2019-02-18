package org.forrisco.core.item;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Juliano Afonso
 * 
 */

@Entity(name = PlanRiskSubItemField.TABLE)
@Table(name = PlanRiskSubItemField.TABLE)

public class PlanRiskSubItemField extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_plan_risk_sub_item_field";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true, length=4000)
	private String description;
	
	@ManyToOne(targetEntity=PlanRiskSubItem.class, optional=false, fetch=FetchType.EAGER)
	private PlanRiskSubItem planRiskSubItem;
	
	@Transient
	private List<PlanRiskSubItemField> planRiskSubItemField;
	
	@Transient
	private Long PlanRiskSubItemId;

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

	public PlanRiskSubItem getPlanRiskSubItem() {
		return planRiskSubItem;
	}

	public void setPlanRiskSubItem(PlanRiskSubItem planRiskSubItem) {
		this.planRiskSubItem = planRiskSubItem;
	}

	public List<PlanRiskSubItemField> getPlanRiskSubItemField() {
		return planRiskSubItemField;
	}

	public void setPlanRiskSubItemField(List<PlanRiskSubItemField> planRiskSubItemField) {
		this.planRiskSubItemField = planRiskSubItemField;
	}

	public Long getPlanRiskSubItemId() {
		return PlanRiskSubItemId;
	}

	public void setPlanRiskSubItemId(Long planRiskSubItemId) {
		PlanRiskSubItemId = planRiskSubItemId;
	}

}
