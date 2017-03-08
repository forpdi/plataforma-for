package org.forpdi.dashboard.admin;

import java.io.Serializable;

public class PlanDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer numberOfObjectives;
	private Integer numberOfIndicators;
	private Integer numberOfGoals;
	private Integer numberOfIndicatorsThematicAxis;
	private Double goalsDelayedPerCent;
	private Integer numberOfBudgets;
	
	public PlanDetails(){
		this.numberOfBudgets = 0;
		this.numberOfGoals = 0;
		this.numberOfIndicators =0;
		this.numberOfObjectives = 0;
		this.goalsDelayedPerCent = 0.0;
		this.numberOfIndicatorsThematicAxis = 0;
	}
	
	public Integer getNumberOfObjectives() {
		return numberOfObjectives;
	}
	public void setNumberOfObjectives(Integer numberOfObjectives) {
		this.numberOfObjectives = numberOfObjectives;
	}
	public Integer getNumberOfIndicators() {
		return numberOfIndicators;
	}
	public void setNumberOfIndicators(Integer numberOfIndicators) {
		this.numberOfIndicators = numberOfIndicators;
	}
	public Integer getNumberOfGoals() {
		return numberOfGoals;
	}
	public void setNumberOfGoals(Integer numberOfGoals) {
		this.numberOfGoals = numberOfGoals;
	}
	public Double getGoalsDelayedPerCent() {
		return goalsDelayedPerCent;
	}
	public void setGoalsDelayedPerCent(Double goalsDelayedPerCent) {
		this.goalsDelayedPerCent = goalsDelayedPerCent;
	}
	public Integer getNumberOfBudgets() {
		return numberOfBudgets;
	}
	public void setNumberOfBudgets(Integer numberOfBudgets) {
		this.numberOfBudgets = numberOfBudgets;
	}
	
	public Integer getNumberOfThematicAxis() {
		return numberOfIndicatorsThematicAxis;
	}
	public void setNumberOfThematicAxis(Integer numberOfIndicatorsThematicAxis) {
		this.numberOfIndicatorsThematicAxis = numberOfIndicatorsThematicAxis;
	}
	
	
}
