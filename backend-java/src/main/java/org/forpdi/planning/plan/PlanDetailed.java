package org.forpdi.planning.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Rodrigo de Freitas Santos
 * 
 */
@Entity(name = PlanDetailed.TABLE)
@Table(name = PlanDetailed.TABLE)
public class PlanDetailed extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_plan_detailed";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int month;

	@Column(nullable = false)
	private int year;

	@ManyToOne(targetEntity = Plan.class, optional = false, fetch = FetchType.EAGER)
	private Plan plan;
	
	@Transient private Long exportPlanId;
	
	private Double performance;
	private Double minimumAverage;
	private Double maximumAverage;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public Double getPerformance() {
		return performance;
	}
	public void setPerformance(Double performance) {
		this.performance = performance;
	}
	public Double getMinimumAverage() {
		return minimumAverage;
	}
	public void setMinimumAverage(Double minimumAverage) {
		this.minimumAverage = minimumAverage;
	}
	public Double getMaximumAverage() {
		return maximumAverage;
	}
	public void setMaximumAverage(Double maximumAverage) {
		this.maximumAverage = maximumAverage;
	}
	public Long getExportPlanId() {
		return exportPlanId;
	}
	public void setExportPlanId(Long exportPlanId) {
		this.exportPlanId = exportPlanId;
	}
	
}

