package org.forpdi.planning.fields.budget;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

public class BudgetDTO extends SimpleLogicalDeletableEntity{
	private static final long serialVersionUID = 1L;
	
	private Budget budget;
	private Double planned;
	private Double conducted;
	private Double committed;
	
	public BudgetDTO(){}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
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

	public void setCommitted(Double commited) {
		this.committed = commited;
	}
	
	
}
