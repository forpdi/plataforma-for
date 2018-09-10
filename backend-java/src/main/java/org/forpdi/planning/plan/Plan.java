package org.forpdi.planning.plan;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.forpdi.planning.structure.Structure;
import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = Plan.TABLE)
@Table(
	name = Plan.TABLE
)
public class Plan extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_plans";
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();
	
	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true, length=10000)
	private String description;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date begin;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date end;

	@ManyToOne(targetEntity=Structure.class, optional=false, fetch=FetchType.EAGER)
	private Structure structure;

	@ManyToOne(targetEntity=PlanMacro.class, optional=false, fetch=FetchType.EAGER)
	private PlanMacro parent;

	private boolean archived = false;
	
	private Double performance;
	private Double minimumAverage;
	private Double maximumAverage;
	
	// Transient fields
	@Transient private boolean updated = false;
	@Transient private List<StructureLevelInstance> levelInstances;
	@Transient private Double auxValue;
	@Transient private List<PlanDetailed> planDetailedList;
	@Transient private boolean haveSons;
	@Transient private Long exportStructureId;
	@Transient private Long exportPlanMacroId;
	

	public boolean isHaveSons() {
		return haveSons;
	}

	public void setHaveSons(boolean haveSons) {
		this.haveSons = haveSons;
	}

	public Double getPerformance() {
		return performance;
	}

	public void setPerformance(Double performance) {
		this.performance = performance;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

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

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public PlanMacro getParent() {
		return parent;
	}

	public void setParent(PlanMacro parent) {
		this.parent = parent;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public List<StructureLevelInstance> getLevelInstances() {
		return levelInstances;
	}

	public void setLevelInstances(List<StructureLevelInstance> levelInstances) {
		this.levelInstances = levelInstances;
	}

	public Double getAuxValue() {
		return auxValue;
	}

	public void setAuxValue(Double auxValue) {
		this.auxValue = auxValue;
	}

	@Override
	public String toString() {
		return "Plan [creation=" + creation + ", name=" + name + ", description=" + description + ", begin=" + begin
				+ ", end=" + end + ", structure=" + structure.toString() + ", parent=" + parent.toString() + ", archived=" + archived
				+ ", updated=" + updated + ", auxValue=" + auxValue + "]";
	}

	public Double getMinimumAverage() {
		return minimumAverage;
	}

	public void setMinimumAverage(Double minimumAverage) {
		this.minimumAverage = minimumAverage;
	}

	public Double getMaximumAverage() {
		return maximumAverage;
	}

	public void setMaximumAverage(Double maximumAverage) {
		this.maximumAverage = maximumAverage;
	}

	public List<PlanDetailed> getPlanDetailedList() {
		return planDetailedList;
	}

	public void setPlanDetailedList(List<PlanDetailed> planDetailedList) {
		this.planDetailedList = planDetailedList;
	}

	public Long getExportStructureId() {
		return exportStructureId;
	}

	public void setExportStructureId(Long exportStructureId) {
		this.exportStructureId = exportStructureId;
	}
	
	public Long getExportPlanMacroId() {
		return exportPlanMacroId;
	}

	public void setExportPlanMacroId(Long exportPlanMacroId) {
		this.exportPlanMacroId = exportPlanMacroId;
	}

}