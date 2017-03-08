package org.forpdi.planning.fields.actionplan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = ActionPlan.TABLE)
@Table(name = ActionPlan.TABLE)
public class ActionPlan extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_action_plan";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private boolean checked;
	
	@Column(nullable=true, length=4000)
	private String description;
	
	@SkipSerialization
	@ManyToOne(targetEntity=StructureLevelInstance.class, optional=false, fetch=FetchType.EAGER)
	private StructureLevelInstance levelInstance;
	
	@Column(nullable = false, length=4000)
	private String responsible;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date begin;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date end;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StructureLevelInstance getLevelInstance() {
		return levelInstance;
	}

	public void setLevelInstance(StructureLevelInstance levelInstance) {
		this.levelInstance = levelInstance;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}


}
