package org.forpdi.planning.fields.budget;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.company.Company;
import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;


@Entity(name = BudgetElement.TABLE)
@Table(name = BudgetElement.TABLE)
public class BudgetElement extends SimpleLogicalDeletableEntity{	
	public static final String TABLE = "fpdi_budget_element";
	private static final long serialVersionUID = 1L;
		
	@Column(nullable=false, unique=true)
	private String subAction;
	
	@Column(nullable=false)
	private double budgetLoa;
	
	@Column(nullable=false)
	private double balanceAvailable;
	
	@Column(nullable=false)
	private Long linkedObjects = 0l;
	
	private Date creation = new Date();
	
	@ManyToOne(targetEntity=Company.class, optional=false, fetch=FetchType.EAGER)
	private Company company;

	public String getSubAction() {
		return subAction;
	}

	public void setSubAction(String subAction) {
		this.subAction = subAction;
	}

	public double getBudgetLoa() {
		return budgetLoa;
	}

	public void setBudgetLoa(double budgetLoa) {
		this.budgetLoa = budgetLoa;
	}

	public double getBalanceAvailable() {
		return balanceAvailable;
	}

	public void setBalanceAvailable(double balanceAvailable) {
		this.balanceAvailable = balanceAvailable;
	}

	public Long getLinkedObjects() {
		return linkedObjects;
	}

	public void setLinkedObjects(Long linkedObjects) {
		this.linkedObjects = linkedObjects;
	}
	
	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
}
