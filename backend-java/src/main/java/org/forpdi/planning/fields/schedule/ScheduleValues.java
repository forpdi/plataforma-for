package org.forpdi.planning.fields.schedule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = ScheduleValues.TABLE)
@Table(name = ScheduleValues.TABLE)
public class ScheduleValues extends SimpleLogicalDeletableEntity{
	public static final String TABLE = "fpdi_schedule_values";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true, length=4000)
	private String value;
	
	@Column(nullable = true)
	private Double valueAsNumber;

	@Column(nullable = true)
	private Date valueAsDate;
	
	@SkipSerialization
	@ManyToOne(targetEntity=ScheduleInstance.class, optional=false, fetch=FetchType.EAGER)
	private ScheduleInstance scheduleInstance;
	
	@ManyToOne(targetEntity=ScheduleStructure.class, optional=false, fetch=FetchType.EAGER)
	private ScheduleStructure scheduleStructure;

	@Transient private Long exportScheduleStructureId;
	
	@Transient private Long exportScheduleInstanceId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getValueAsNumber() {
		return valueAsNumber;
	}

	public void setValueAsNumber(Double valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}

	public Date getValueAsDate() {
		return valueAsDate;
	}

	public void setValueAsDate(Date valueAsDate) {
		this.valueAsDate = valueAsDate;
	}

	public ScheduleInstance getScheduleInstance() {
		return scheduleInstance;
	}

	public void setScheduleInstance(ScheduleInstance scheduleInstance) {
		this.scheduleInstance = scheduleInstance;
	}

	public ScheduleStructure getScheduleStructure() {
		return scheduleStructure;
	}

	public void setScheduleStructure(ScheduleStructure scheduleStructure) {
		this.scheduleStructure = scheduleStructure;
	}
	
	public Long getExportScheduleStructureId() {
		return exportScheduleStructureId;
	}

	public void setExportScheduleStructureId(Long exportScheduleStructureId) {
		this.exportScheduleStructureId = exportScheduleStructureId;
	}

	public Long getExportScheduleInstanceId() {
		return exportScheduleInstanceId;
	}

	public void setExportScheduleInstanceId(Long exportScheduleInstanceId) {
		this.exportScheduleInstanceId = exportScheduleInstanceId;
	}
	
}
