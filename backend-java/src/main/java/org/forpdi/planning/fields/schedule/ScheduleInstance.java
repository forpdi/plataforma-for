package org.forpdi.planning.fields.schedule;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = ScheduleInstance.TABLE)
@Table(name = ScheduleInstance.TABLE)
public class ScheduleInstance extends SimpleLogicalDeletableEntity{
	public static final String TABLE = "fpdi_schedule_instance";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private Long number;
	
	@Column(nullable = false, length=4000)
	private String description;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date begin;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date end;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();
	
	@Column(nullable = true, length=255)
	private String periodicity;
	
	@SkipSerialization
	@ManyToOne(targetEntity=Schedule.class, optional=false, fetch=FetchType.EAGER)
	private Schedule schedule;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="scheduleInstance", targetEntity=ScheduleValues.class)
	@Cascade(CascadeType.ALL)
	private List<ScheduleValues> scheduleValues;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public List<ScheduleValues> getScheduleValues() {
		return scheduleValues;
	}

	public void setScheduleValues(List<ScheduleValues> scheduleValues) {
		this.scheduleValues = scheduleValues;
	}
	
}
