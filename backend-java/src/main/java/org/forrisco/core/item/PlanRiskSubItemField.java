package org.forrisco.core.item;

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
	
	@SkipSerialization
	@ManyToOne(targetEntity=PlanRiskSubItem.class, optional=false, fetch=FetchType.EAGER)
	private PlanRiskSubItem planRiskSubItem;
	
	@Column(nullable = false, length=400)
	private String name;

	@Column(nullable = true, columnDefinition="longtext")
	private String description;
	
	@Column(nullable = false)
	private boolean isText;
	
	@Column(length=400)
	private String fileLink;
	
	@Transient
	private String value;

	public PlanRiskSubItem getPlanRiskSubItem() {
		return planRiskSubItem;
	}

	public void setPlanRiskSubItem(PlanRiskSubItem planRiskSubItem) {
		this.planRiskSubItem = planRiskSubItem;
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

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	public String getFileLink() {
		return fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



}
