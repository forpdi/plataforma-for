package org.forpdi.dashboard.admin;

import java.io.Serializable;

public class GoalsInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer inDay;
	private Double inDayPercentage;
	private Integer late;
	private Double latePercentage;
	private Integer belowMininum;
	private Double belowMinimumPercentage;
	private Integer belowExpected;
	private Double belowExpectedPercentage;
	private Integer reached;
	private Double reachedPercentage;
	private Integer aboveExpected;
	private Double aboveExpectedPercentage;
	private Integer notStarted;
	private Double notStartedPercentage;
	private Integer finished;
	private Double finishedPercentage;
	private Integer closeToMaturity;
	private Double closeToMaturityPercentage;

	public GoalsInfo() {
		inDay = 0;
		inDayPercentage = 0.0;
		late = 0;
		latePercentage = 0.0;
		belowMininum = 0;
		belowMinimumPercentage = 0.0;
		belowExpected = 0;
		belowExpectedPercentage = 0.0;
		reached = 0;
		reachedPercentage = 0.0;
		aboveExpected = 0;
		aboveExpectedPercentage = 0.0;
		notStarted = 0;
		notStartedPercentage = 0.0;
		finished = 0;
		finishedPercentage = 0.0;
		closeToMaturity = 0;
		closeToMaturityPercentage = 0.0;
	}

	public Integer getFinished() {
		return finished;
	}

	public void setFinished(Integer finished) {
		this.finished = finished;
	}

	public Double getFinishedPercentage() {
		return finishedPercentage;
	}

	public void setFinishedPercentage(Double finishedPercentage) {
		this.finishedPercentage = finishedPercentage;
	}

	public Integer getCloseToMaturity() {
		return closeToMaturity;
	}

	public void setCloseToMaturity(Integer closeToMaturity) {
		this.closeToMaturity = closeToMaturity;
	}

	public Double getCloseToMaturityPercentage() {
		return closeToMaturityPercentage;
	}

	public void setCloseToMaturityPercentage(Double closeToMaturityPercentage) {
		this.closeToMaturityPercentage = closeToMaturityPercentage;
	}

	public Integer getNotStarted() {
		return notStarted;
	}

	public void setNotStarted(Integer notStarted) {
		this.notStarted = notStarted;
	}

	public Double getNotStartedPercentage() {
		return notStartedPercentage;
	}

	public void setNotStartedPercentage(Double notStartedPercentage) {
		this.notStartedPercentage = notStartedPercentage;
	}

	public Integer getInDay() {
		return inDay;
	}

	public void setInDay(Integer inDay) {
		this.inDay = inDay;
	}

	public Double getInDayPercentage() {
		return inDayPercentage;
	}

	public void setInDayPercentage(Double inDayPercentage) {
		this.inDayPercentage = inDayPercentage;
	}

	public Integer getLate() {
		return late;
	}

	public void setLate(Integer late) {
		this.late = late;
	}

	public Double getLatePercentage() {
		return latePercentage;
	}

	public void setLatePercentage(Double latePercentage) {
		this.latePercentage = latePercentage;
	}

	public Integer getBelowMininum() {
		return belowMininum;
	}

	public void setBelowMininum(Integer belowMininum) {
		this.belowMininum = belowMininum;
	}

	public Double getBelowMinimumPercentage() {
		return belowMinimumPercentage;
	}

	public void setBelowMinimumPercentage(Double belowMinimumPercentage) {
		this.belowMinimumPercentage = belowMinimumPercentage;
	}

	public Integer getBelowExpected() {
		return belowExpected;
	}

	public void setBelowExpected(Integer belowExpected) {
		this.belowExpected = belowExpected;
	}

	public Double getBelowExpectedPercentage() {
		return belowExpectedPercentage;
	}

	public void setBelowExpectedPercentage(Double belowExpectedPercentage) {
		this.belowExpectedPercentage = belowExpectedPercentage;
	}

	public Integer getReached() {
		return reached;
	}

	public void setReached(Integer reached) {
		this.reached = reached;
	}

	public Double getReachedPercentage() {
		return reachedPercentage;
	}

	public void setReachedPercentage(Double reachedPercentage) {
		this.reachedPercentage = reachedPercentage;
	}

	public Integer getAboveExpected() {
		return aboveExpected;
	}

	public void setAboveExpected(Integer aboveExpected) {
		this.aboveExpected = aboveExpected;
	}

	public Double getAboveExpectedPercentage() {
		return aboveExpectedPercentage;
	}

	public void setAboveExpectedPercentage(Double aboveExpectedPercentage) {
		this.aboveExpectedPercentage = aboveExpectedPercentage;
	}

}
