package org.forpdi.planning.fields.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

@Entity(name = BudgetSimulationDB.TABLE)
@Table(name = BudgetSimulationDB.TABLE)
public class BudgetSimulationDB extends SimpleLogicalDeletableEntity{	
	public static final String TABLE = "fpdi_budget_simulation";
	private static final long serialVersionUID = 1L;
		
	@Column(nullable=false, unique=true, length=4)
	private String subAction;
	
	@Column(nullable=false)
	private double planned;
	
	@Column(nullable=false)
	private double conducted;
	
	@Column(nullable=false)
	private double committed;

	public String getSubAction() {
		return subAction;
	}

	public void setSubAction(String subAction) {
		this.subAction = subAction;
	}

	public double getPlanned() {
		return planned;
	}

	public void setPlanned(double planned) {
		this.planned = planned;
	}

	public double getConducted() {
		return conducted;
	}

	public void setConducted(double conducted) {
		this.conducted = conducted;
	}

	public double getCommitted() {
		return committed;
	}

	public void setCommitted(double committed) {
		this.committed = committed;
	}
	
	

}
