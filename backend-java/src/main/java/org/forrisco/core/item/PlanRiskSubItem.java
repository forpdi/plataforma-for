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
@Entity(name = PlanRiskSubItem.TABLE)
@Table(name = PlanRiskSubItem.TABLE)
public class PlanRiskSubItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_plan_risk_sub_item";
	private static final long serialVersionUID = 1L;
	
	@SkipSerialization
	@ManyToOne(targetEntity=PlanRiskItem.class, optional=false, fetch=FetchType.EAGER)
	private PlanRiskItem planRiskItem;
	
	@Column(nullable = false, length=400)
	private String name;
	
	@Column(nullable = true, length=10000)
	private String description;
	
	@Transient
	private List<PlanRiskSubItemField> planRiskSubItemField;
	
	@Transient
	private Long itemId;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public PlanRiskItem getPlanRiskItem() {
		return planRiskItem;
	}

	public void setPlanRiskItem(PlanRiskItem planRiskItem) {
		this.planRiskItem = planRiskItem;
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

	public List<PlanRiskSubItemField> getPlanRiskSubItemField() {
		return planRiskSubItemField;
	}

	public void setPlanRiskSubItemField(List<PlanRiskSubItemField> planRiskSubItemField) {
		this.planRiskSubItemField = planRiskSubItemField;
	}
}
