package org.forpdi.planning.structure;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.forpdi.planning.attribute.Attribute;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = StructureLevel.TABLE)
@Table(
	name = StructureLevel.TABLE,
	uniqueConstraints=@UniqueConstraint(columnNames={"id","sequence"}),
	indexes = {
		@Index(columnList="sequence"),
		@Index(columnList="structure_id,sequence")
	}
)
public class StructureLevel extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_structure_level";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true, length=10000)
	private String description;
	
	@Column(nullable = false)
	private int sequence;

	@Column(nullable = false)
	private boolean leaf = false;

	@SkipSerialization
	@ManyToOne(targetEntity=Structure.class, optional=false, fetch=FetchType.EAGER)
	private Structure structure;
	
	@Column(nullable=false)
	private boolean goal = false;
	
	@Column(nullable=false)
	private boolean indicator = false;
	
	@Column(nullable=false)
	private boolean objective = false;

	@Transient
	private List<Attribute> attributes;
	
	@Transient
	private Long exportStructureId;
	
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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public boolean isGoal() {
		return goal;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
	}

	public boolean isIndicator() {
		return indicator;
	}

	public void setIndicator(boolean indicator) {
		this.indicator = indicator;
	}
	
	public boolean isObjective() {
		return objective;
	}

	public void setObjective(boolean objective) {
		this.objective = objective;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public Long getExportStructureId() {
		return exportStructureId;
	}

	public void setExportStructureId(Long exportStructureId) {
		this.exportStructureId = exportStructureId;
	}

}