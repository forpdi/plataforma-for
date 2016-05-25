package org.forpdi.planning.structure;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = Structure.TABLE)
@Table(name = Structure.TABLE)
public class Structure extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_structure";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true, length=10000)
	private String description;

	@Transient
	private List<StructureLevel> levels;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<StructureLevel> getLevels() {
		return levels;
	}

	public void setLevels(List<StructureLevel> levels) {
		this.levels = levels;
	}
	
}
