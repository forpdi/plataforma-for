package org.forpdi.planning.jobs;

import java.util.Date;

import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.structure.StructureLevel;

public class GoalDTO {
	private Long parent;
	private Plan plan;
	private StructureLevel level;
	private String name;
	private String responsible;
	private String description;
	private double expected;
	private double minimum;
	private double maximum;
	private String periodicity;
	private Date beginDate;
	private Date endDate;
	
	public GoalDTO() {
		super();
	}
	
	public GoalDTO(Long parent, Plan plan, StructureLevel level, String name, String responsible, String description,
			double expected, double minimum, double maximum, String periodicity, Date beginDate, Date endDate) {
		super();
		this.parent = parent;
		this.plan = plan;
		this.level = level;
		this.name = name;
		this.responsible = responsible;
		this.description = description;
		this.expected = expected;
		this.minimum = minimum;
		this.maximum = maximum;
		this.periodicity = periodicity;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public StructureLevel getLevel() {
		return level;
	}

	public void setLevel(StructureLevel level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getExpected() {
		return expected;
	}

	public void setExpected(double expected) {
		this.expected = expected;
	}

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
