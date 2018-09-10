package org.forpdi.planning.attribute;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forpdi.core.user.User;
import org.forpdi.planning.fields.OptionsField;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.fields.budget.BudgetDTO;
import org.forpdi.planning.fields.schedule.Schedule;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.structure.StructureLevel;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = Attribute.TABLE)
@Table(name = Attribute.TABLE)
public class Attribute extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_attribute";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 255)
	private String label;

	@Column(nullable = true, length = 10000)
	private String description;

	@Column(nullable = false, length = 255)
	private String type;

	@Column(nullable = false)
	private boolean required = false;

	@Column(nullable = false)
	private boolean visibleInTables = true;
	
	@Column(nullable = false)
	private boolean finishDate = false;
	
	@Column(nullable = false)
	private boolean expectedField = false;
	
	
	@Column(nullable = false)
	private boolean justificationField = false;
	
	
	@Column(nullable = false)
	private boolean minimumField = false;
	
	@Column(nullable = false)
	private boolean maximumField = false;
	
	@Column(nullable = false)
	private boolean reachedField = false;
	
	@Column(nullable = false)
	private boolean polarityField = false;
	
	@Column(nullable = false)
	private boolean formatField = false;
	
	@Column(nullable = false)
	private boolean periodicityField = false;
	
	@Column(nullable = false)
	private boolean beginField = false;
	
	@Column(nullable = false)
	private boolean endField = false;

	@Column(nullable = false)
	private boolean bscField = false;
	
	@SkipSerialization
	@ManyToOne(targetEntity = StructureLevel.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevel level;

	
	// Transient fields
	@Transient
	private AttributeInstance attributeInstance;
	@Transient
	private List<AttributeInstance> attributeInstances;
	@Transient
	private List<BudgetDTO> budgets;
	@Transient
	private List<ActionPlan> actionPlans;
	@Transient
	private Schedule schedule;
	@Transient
	private TableFields tableFields;
	@Transient
	private List<OptionsField> optionsField;
	@Transient
	private List<User> users;
	@Transient
	private Long exportStructureLevelId;
	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public StructureLevel getLevel() {
		return level;
	}

	public void setLevel(StructureLevel level) {
		this.level = level;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isVisibleInTables() {
		return visibleInTables;
	}

	public void setVisibleInTables(boolean visibleInTables) {
		this.visibleInTables = visibleInTables;
	}

	public boolean isFinishDate() {
		return finishDate;
	}

	public void setFinishDate(boolean finishDate) {
		this.finishDate = finishDate;
	}

	public boolean isExpectedField() {
		return expectedField;
	}
	

	public void setExpectedField(boolean expectedField) {
		this.expectedField = expectedField;
	}
	
	public boolean isJustificationField () {
		return justificationField;
	}
	
	public void setJustificationField (boolean justificationField) {
		this.justificationField = justificationField;
	}

	public boolean isMinimumField() {
		return minimumField;
	}

	public void setMinimumField(boolean minimumField) {
		this.minimumField = minimumField;
	}

	public boolean isMaximumField() {
		return maximumField;
	}

	public void setMaximumField(boolean maximumField) {
		this.maximumField = maximumField;
	}

	public boolean isReachedField() {
		return reachedField;
	}

	public void setReachedField(boolean reachedField) {
		this.reachedField = reachedField;
	}

	public boolean isPolarityField() {
		return polarityField;
	}

	public void setPolarityField(boolean polarityField) {
		this.polarityField = polarityField;
	}

	public boolean isFormatField() {
		return formatField;
	}

	public void setFormatField(boolean formatField) {
		this.formatField = formatField;
	}

	public boolean isPeriodicityField() {
		return periodicityField;
	}

	public void setPeriodicityField(boolean periodicityField) {
		this.periodicityField = periodicityField;
	}

	public boolean isBeginField() {
		return beginField;
	}

	public void setBeginField(boolean beginField) {
		this.beginField = beginField;
	}

	public boolean isEndField() {
		return endField;
	}

	public void setEndField(boolean endField) {
		this.endField = endField;
	}

	public boolean isBscField() {
		return bscField;
	}

	public void setBscField(boolean bscField) {
		this.bscField = bscField;
	}

	public AttributeInstance getAttributeInstance() {
		return attributeInstance;
	}

	public void setAttributeInstance(AttributeInstance attributeInstance) {
		this.attributeInstance = attributeInstance;
	}

	public List<AttributeInstance> getAttributeInstances() {
		return attributeInstances;
	}

	public void setAttributeInstances(List<AttributeInstance> attributeInstances) {
		this.attributeInstances = attributeInstances;
	}

	public List<BudgetDTO> getBudgets() {
		return budgets;
	}

	public void setBudgets(List<BudgetDTO> budgets) {
		this.budgets = budgets;
	}

	public List<ActionPlan> getActionPlans() {
		return actionPlans;
	}

	public void setActionPlans(List<ActionPlan> actionPlans) {
		this.actionPlans = actionPlans;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public TableFields getTableFields() {
		return tableFields;
	}

	public void setTableFields(TableFields tableFields) {
		this.tableFields = tableFields;
	}

	public List<OptionsField> getOptionsField() {
		return optionsField;
	}

	public void setOptionLabels(List<OptionsField> optionsField) {
		this.optionsField = optionsField;
	}

	public void setOptionsField(List<OptionsField> optionsField) {
		this.optionsField = optionsField;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Attribute [label=" + label + ", description=" + description + ", type=" + type + ", required="
				+ required + ", visibleInTables=" + visibleInTables + ", finishDate=" + finishDate + ", expectedField="
				+ expectedField + ", justificationField=" + justificationField + ", minimumField=" + minimumField
				+ ", maximumField=" + maximumField + ", reachedField=" + reachedField + ", level=" + level
				+ ", attributeInstance=" + attributeInstance + ", attributeInstances=" + attributeInstances
				+ ", budgets=" + budgets + ", actionPlans=" + actionPlans + ", schedule=" + schedule + ", tableFields="
				+ tableFields + ", optionsField=" + optionsField + "]";
	}
	
	
	public Long getExportStructureLevelId() {
		return exportStructureLevelId;
	}

	public void setExportStructureLevelId(Long exportStructureLevelId) {
		this.exportStructureLevelId = exportStructureLevelId;
	}
	
}