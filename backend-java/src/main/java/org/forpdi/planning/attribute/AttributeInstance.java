package org.forpdi.planning.attribute;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Rodrigo de Freitas Santos
 * 
 */
@Entity(name = AttributeInstance.TABLE)
@Table(name = AttributeInstance.TABLE, uniqueConstraints = @UniqueConstraint(columnNames = { "id" }), indexes = {
		@Index(columnList = "attribute_id,levelInstance_id") })
public class AttributeInstance extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_attribute_instance";
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, length = 4000)
	private String value;

	@Column(nullable = true)
	private Double valueAsNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date valueAsDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();

	@SkipSerialization
	@ManyToOne(targetEntity = Attribute.class, optional = false, fetch = FetchType.EAGER)
	private Attribute attribute;

	@SkipSerialization
	@ManyToOne(targetEntity = StructureLevelInstance.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevelInstance levelInstance;
	
	@Transient
	private String formattedValue;

	public String getFormattedValue() {
		return formattedValue;
	}

	public void setFormattedValue(String formattedValue) {
		this.formattedValue = formattedValue;
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

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public StructureLevelInstance getLevelInstance() {
		return levelInstance;
	}

	public void setLevelInstance(StructureLevelInstance levelInstance) {
		this.levelInstance = levelInstance;
	}

	@Override
	public String toString() {
		return "AttributeInstance [value=" + value + ", valueAsNumber=" + valueAsNumber + ", valueAsDate=" + valueAsDate
				+ ", creation=" + creation + ", attribute=" + attribute.getId() + ", levelInstance=" + levelInstance.getName()
				+ ", formattedValue=" + formattedValue + "]";
	}

	
	
}
