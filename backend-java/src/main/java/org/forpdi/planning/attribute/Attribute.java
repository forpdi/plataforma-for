package org.forpdi.planning.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	@Column(nullable = false, length=255)
	private String label;

	@Column(nullable = true, length=10000)
	private String description;
	
	@Column(nullable=false, length=255)
	private String type;
	
	@Column(nullable=false)
	private boolean required = false;
	
	@Column(nullable=false)
	private boolean visibleInTables = true;
	
	@SkipSerialization
	@ManyToOne(targetEntity=StructureLevel.class, optional=false, fetch=FetchType.EAGER)
	private StructureLevel level;

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
	
}
