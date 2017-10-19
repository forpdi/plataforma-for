package org.forpdi.planning.fields.budget;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = Budget.TABLE)
@Table(name = Budget.TABLE)
public class Budget extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_budget";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = false)
	private String subAction;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = true)
	private Double committed; // empenhado

	@Column(nullable = true)
	private Double realized; // realizado

	private Date creation = new Date();

	@ManyToOne(targetEntity = BudgetElement.class, optional = false, fetch = FetchType.EAGER)
	private BudgetElement budgetElement;

	@SkipSerialization
	@ManyToOne(targetEntity = StructureLevelInstance.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevelInstance levelInstance;

	public String getSubAction() {
		return subAction;
	}

	public void setSubAction(String subAction) {
		this.subAction = subAction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCommitted() {
		return committed;
	}

	public void setCommitted(Double committed) {
		this.committed = committed;
	}

	public Double getRealized() {
		return realized;
	}

	public void setRealized(Double realized) {
		this.realized = realized;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public BudgetElement getBudgetElement() {
		return budgetElement;
	}

	public void setBudgetElement(BudgetElement budgetElement) {
		this.budgetElement = budgetElement;
	}

	public StructureLevelInstance getLevelInstance() {
		return levelInstance;
	}

	public void setLevelInstance(StructureLevelInstance levelInstance) {
		this.levelInstance = levelInstance;
	}

}
