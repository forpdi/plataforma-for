package org.forpdi.dashboard.manager;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

@Entity(name = LevelInstanceHistory.TABLE)
@Table(name = LevelInstanceHistory.TABLE)
public class LevelInstanceHistory extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_level_instance_history";
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation;

	@Column(nullable = false)
	private Double value;

	@ManyToOne(targetEntity = StructureLevelInstance.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevelInstance levelInstance;

	@Transient
	private Long exportStructureLevelInstanceId;
	
	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public StructureLevelInstance getLevelInstance() {
		return levelInstance;
	}

	public void setLevelInstance(StructureLevelInstance indicator) {
		this.levelInstance = indicator;
	}

	public Long getExportStructureLevelInstanceId() {
		return exportStructureLevelInstanceId;
	}

	public void setExportStructureLevelInstanceId(Long exportStructureLevelInstanceId) {
		this.exportStructureLevelInstanceId = exportStructureLevelInstanceId;
	}
}