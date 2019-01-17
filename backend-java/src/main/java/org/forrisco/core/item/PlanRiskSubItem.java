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
@Entity(name = PlanRiskSubItem.TABLE)
@Table(name = PlanRiskSubItem.TABLE)
public class PlanRiskSubItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_plan_risk_sub_item";
	private static final long serialVersionUID = 1L;
	
	@SkipSerialization
	@ManyToOne(targetEntity=PlanRiskItem.class, optional=false, fetch=FetchType.EAGER)
	private PlanRiskSubItem planRiskSubItem;
	
	@Column(nullable = false, length=255)
	private String name;
	
	@Column(nullable = true, length=4000)
	private String description;
	
	@Column(length=255)
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
