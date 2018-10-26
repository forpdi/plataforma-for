package org.forpdi.planning.fields.schedule;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

@Entity(name = Schedule.TABLE)
@Table(name = Schedule.TABLE)
public class Schedule extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_schedule";
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private boolean periodicityEnable;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", targetEntity = ScheduleStructure.class)
	@Cascade(CascadeType.ALL)
	private List<ScheduleStructure> scheduleStructures;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", targetEntity = ScheduleInstance.class)
	@Cascade(CascadeType.ALL)
	@Where(clause = "deleted=0")
	private List<ScheduleInstance> scheduleInstances;

	@Column(nullable = false)
	private Long attributeId;

	@Column(nullable = true)
	private boolean isDocument = false;
	
	@Transient
	private Long exportAttributeId;

	public boolean isPeriodicityEnable() {
		return periodicityEnable;
	}

	public void setPeriodicityEnable(boolean periodicityEnable) {
		this.periodicityEnable = periodicityEnable;
	}

	public List<ScheduleStructure> getScheduleStructures() {
		return scheduleStructures;
	}

	public void setScheduleStructures(List<ScheduleStructure> scheduleStructures) {
		this.scheduleStructures = scheduleStructures;
	}

	public List<ScheduleInstance> getScheduleInstances() {
		return scheduleInstances;
	}

	public void setScheduleInstances(List<ScheduleInstance> scheduleInstances) {
		this.scheduleInstances = scheduleInstances;
	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public boolean isDocument() {
		return isDocument;
	}

	public void setIsDocument(boolean isDocument) {
		this.isDocument = isDocument;
	}
	
	public Long getExportAttributeId() {
		return exportAttributeId;
	}

	public void setExportAttributeId(Long exportAttributeId) {
		this.exportAttributeId = exportAttributeId;
	}

}
