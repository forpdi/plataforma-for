package org.forpdi.dashboard.admin;

import java.io.Serializable;

public class GeneralBudgets implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double planned;
	private Double conducted;
	private Double committed;
	
	public GeneralBudgets(){
		planned = 0.0;
		committed = 0.0;
		conducted = 0.0;
	}

	public Double getPlanned() {
		return planned;
	}

	public void setPlanned(Double planned) {
		this.planned = planned;
	}

	public Double getConducted() {
		return conducted;
	}

	public void setConducted(Double conducted) {
		this.conducted = conducted;
	}

	public Double getCommitted() {
		return committed;
	}

	public void setCommitted(Double committed) {
		this.committed = committed;
	}

}
