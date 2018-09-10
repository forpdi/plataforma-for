package org.forpdi.planning.document;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.forpdi.planning.plan.PlanMacro;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

@Entity(name = Document.TABLE)
@Table(name = Document.TABLE)
public class Document extends SimpleLogicalDeletableEntity  {
	public static final String TABLE = "fpdi_document";
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();
	
	@Column(nullable = false, length=255)
	private String title;
	
	@Column(nullable=true, length=4000)
	private String description;
	

	@OneToOne(targetEntity=PlanMacro.class, optional=false, fetch=FetchType.EAGER)
	private PlanMacro plan;
	
	@Transient private Long exportPlanMacroId;

	public Long getExportPlanMacroId() {
		return exportPlanMacroId;
	}

	public void setExportPlanMacroId(Long exportPlanMacroId) {
		this.exportPlanMacroId = exportPlanMacroId;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PlanMacro getPlan() {
		return plan;
	}

	public void setPlan(PlanMacro plan) {
		this.plan = plan;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}