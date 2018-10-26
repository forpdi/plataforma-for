package org.forpdi.planning.fields.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = ScheduleStructure.TABLE)
@Table(name = ScheduleStructure.TABLE)
public class ScheduleStructure extends SimpleLogicalDeletableEntity{
	public static final String TABLE = "fpdi_schedule_structure";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String label;
	
	@Column(nullable = false, length=255)
	private String type;
	
	@SkipSerialization
	@ManyToOne(targetEntity=Schedule.class, optional=false, fetch=FetchType.EAGER)
	private Schedule schedule;

	@Transient private Long exportScheduleId;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
	public Long getExportScheduleId() {
		return exportScheduleId;
	}

	public void setExportScheduleId(Long exportScheduleId) {
		this.exportScheduleId = exportScheduleId;
	}
	
}
