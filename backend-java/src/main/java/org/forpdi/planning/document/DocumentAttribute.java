package org.forpdi.planning.document;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forpdi.planning.fields.schedule.Schedule;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = DocumentAttribute.TABLE)
@Table(name = DocumentAttribute.TABLE, indexes = { @Index(columnList = "sequence") })
public class DocumentAttribute extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_document_attribute";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 255)
	private String name;

	@ManyToOne(targetEntity = DocumentSection.class, optional = false, fetch = FetchType.EAGER)
	@SkipSerialization
	private DocumentSection section;

	@Column(nullable = false)
	private String type;

	@Column(nullable = true, length = 12000)
	private String value;

	@Column(nullable = true)
	private Double valueAsNumber;

	@Column(nullable = true)
	private Date valueAsDate;

	private int sequence;

	@Column(nullable = false)
	private boolean required = false;

	@Transient
	private Schedule schedule;

	@Transient
	private TableFields tableFields;

	@Transient
	private List<Plan> selectPlans;
	
	@Transient
	private List<StructureLevelInstance> strategicObjectives;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DocumentSection getSection() {
		return section;
	}

	public void setSection(DocumentSection section) {
		this.section = section;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getValueAsNumber() {
		return valueAsNumber;
	}

	public void setValueAsNumber(Double valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}

	public Date getValueAsDate() {
		return valueAsDate;
	}

	public void setValueAsDate(Date valueAsDate) {
		this.valueAsDate = valueAsDate;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
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

	public List<Plan> getSelectPlans() {
		return selectPlans;
	}

	public void setSelectPlans(List<Plan> selectPlans) {
		this.selectPlans = selectPlans;
	}

	public List<StructureLevelInstance> getStrategicObjectives() {
		return strategicObjectives;
	}

	public void setStrategicObjectives(List<StructureLevelInstance> strategicObjectives) {
		this.strategicObjectives = strategicObjectives;
	}
	
}
