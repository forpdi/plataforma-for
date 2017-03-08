package org.forpdi.dashboard.admin;

import java.io.Serializable;
import java.util.Date;

public class GoalsInfoTable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String strategicAxisName;
	private String objectiveName;
	private String indicatorName;
	private String goalName;
	private String responsible;
	private String goalStatus;
	private String lastModification;
	private int deadLineStatus;
	private Date finishDate;
	private int progressStatus;
	private Double expected;
	private Double reached;
	private Long idResponsible;
	

	
	public Double getExpected() {
		return expected;
	}
	public void setExpected(Double expected) {
		this.expected = expected;
	}
	public Double getReached() {
		return reached;
	}
	public void setReached(Double reached) {
		this.reached = reached;
	}
	public int getProgressStatus() {
		return progressStatus;
	}
	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public int getDeadLineStatus() {
		return deadLineStatus;
	}
	public void setDeadLineStatus(int deadLineStatus) {
		this.deadLineStatus = deadLineStatus;
	}
	public String getStrategicAxisName() {
		return strategicAxisName;
	}
	public void setStrategicAxisName(String strategicAxisName) {
		this.strategicAxisName = strategicAxisName;
	}
	public String getObjectiveName() {
		return objectiveName;
	}
	public void setObjectiveName(String objectiveName) {
		this.objectiveName = objectiveName;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public String getGoalName() {
		return goalName;
	}
	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getGoalStatus() {
		return goalStatus;
	}
	public void setGoalStatus(String goalStatus) {
		this.goalStatus = goalStatus;
	}
	public String getLastModification() {
		return lastModification;
	}
	public void setLastModification(String lastModification) {
		this.lastModification = lastModification;
	}
	
	public Long getIdResponsible() {
		return idResponsible;
	}
	public void setIdResponsible(Long idResponsible) {
		this.idResponsible = idResponsible;
	}
	
	@Override
	public String toString() {
		return "GoalsInfoTable [objectiveName=" + objectiveName + ", indicatorName=" + indicatorName + ", goalName="
				+ goalName + ", responsible=" + responsible + ", IdResponsible=" + idResponsible + " + id goalStatus=" + goalStatus + ", lastModification="
				+ lastModification + ", deadLineStatus=" + deadLineStatus + ", finishDate=" + finishDate.toLocaleString() +"]";
	}
	
	
	
	
}
