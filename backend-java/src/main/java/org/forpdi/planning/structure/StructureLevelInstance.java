package org.forpdi.planning.structure;

import java.util.Date;
import java.util.List;

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

import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.types.enums.CalculationType;
import org.forpdi.planning.plan.Plan;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Rodrigo de Freitas Santos
 * 
 */
@Entity(name = StructureLevelInstance.TABLE)
@Table(name = StructureLevelInstance.TABLE, uniqueConstraints = @UniqueConstraint(columnNames = { "id" }), indexes = {
		@Index(columnList = "level_id,plan_id") })
public class StructureLevelInstance extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_structure_level_instance";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 255)
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date modification = new Date();

	@ManyToOne(targetEntity = StructureLevel.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevel level;

	@ManyToOne(targetEntity = Plan.class, optional = false, fetch = FetchType.EAGER)
	private Plan plan;

	@Column(nullable = true)
	private Long parent;

	@Column(nullable = false)
	private boolean closed = false;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date closedDate;

	@Column(nullable = false)
	private boolean aggregate = false;

	private Double levelValue;
	private Double levelMinimum;
	private Double levelMaximum;

	@Column(nullable = true)
	private CalculationType calculation;

	@Column(nullable = true)
	private Date nextSave;

	@Transient
	private int deadlineStatus;

	@Transient
	private int progressStatus;

	@Transient
	private PaginatedList<StructureLevelInstance> sons;

	@Transient
	private boolean haveBudget;

	@Transient
	private String polarity;

	@Transient
	private List<AggregateIndicator> indicatorList;

	@Transient
	private StructureLevel levelSon;
	
	@Transient
	private boolean favoriteExistent;
	
	@Transient
	private int favoriteTotal;
	
	@Transient
	private String exportResponsibleMail;

	public Date getNextSave() {
		return nextSave;
	}

	public void setNextSave(Date nextSave) {
		this.nextSave = nextSave;
	}

	@Transient
	private List<AttributeInstance> attributeInstanceList;

	@Transient
	private List<Attribute> attributeList;

	@Transient
	private List<StructureLevelInstance> parents;
	
	@Transient
	private List<StructureLevelInstanceDetailed> levelInstanceDetailedList;
	
	@Transient
	private Long exportLevelId;
	
	@Transient
	private Long exportPlanId;

	@Transient
	private StructureLevelInstance levelParent;
	
	public List<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}

	public List<AttributeInstance> getAttributeInstanceList() {
		return attributeInstanceList;
	}

	public void setAttributeInstanceList(List<AttributeInstance> attributeInstanceList) {
		this.attributeInstanceList = attributeInstanceList;
	}

	public List<AggregateIndicator> getIndicatorList() {
		return indicatorList;
	}

	public void setIndicatorList(List<AggregateIndicator> indicatorList) {
		this.indicatorList = indicatorList;
	}

	public StructureLevel getLevelSon() {
		return levelSon;
	}

	public void setLevelSon(StructureLevel levelSon) {
		this.levelSon = levelSon;
	}

	public CalculationType getCalculation() {
		return calculation;
	}

	public void setCalculation(CalculationType calculation) {
		this.calculation = calculation;
	}

	public boolean isAggregate() {
		return aggregate;
	}

	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}

	public boolean isHaveBudget() {
		return haveBudget;
	}

	public void setHaveBudget(boolean haveBudget) {
		this.haveBudget = haveBudget;
	}

	public String isPolarity() {
		return polarity;
	}

	public void setPolarity(String polarity) {
		this.polarity = polarity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getModification() {
		return modification;
	}

	public void setModification(Date modification) {
		this.modification = modification;
	}

	public StructureLevel getLevel() {
		return level;
	}

	public void setLevel(StructureLevel level) {
		this.level = level;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public Double getLevelValue() {
		return levelValue;
	}

	public void setLevelValue(Double levelValue) {
		this.levelValue = levelValue;
	}

	public String getPolarity() {
		return polarity;
	}

	public int getDeadlineStatus() {
		return deadlineStatus;
	}

	public void setDeadlineStatus(int deadlineStatus) {
		this.deadlineStatus = deadlineStatus;
	}

	public int getProgressStatus() {
		return progressStatus;
	}

	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}

	public PaginatedList<StructureLevelInstance> getSons() {
		return sons;
	}

	public void setSons(PaginatedList<StructureLevelInstance> sons) {
		this.sons = sons;
	}

	public List<StructureLevelInstance> getParents() {
		return parents;
	}

	public void setParents(List<StructureLevelInstance> parents) {
		this.parents = parents;
	}

	public boolean isFavoriteExistent() {
		return favoriteExistent;
	}

	public void setFavoriteExistent(boolean favoriteExistent) {
		this.favoriteExistent = favoriteExistent;
	}

	public int getFavoriteTotal() {
		return favoriteTotal;
	}

	public void setFavoriteTotal(int favoriteTotal) {
		this.favoriteTotal = favoriteTotal;
	}

	@Override
	public String toString() {
		return "StructureLevelInstance [name=" + name + ", creation=" + creation + ", modification=" + modification
				+ ", level=" + level + ", plan=" + plan + ", parent=" + parent + ", closed=" + closed + ", closedDate="
				+ closedDate + ", deadlineStatus=" + deadlineStatus + ", progressStatus=" + progressStatus + ", sons="
				+ sons + ", haveBudget=" + haveBudget + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StructureLevelInstance && this.id == ((StructureLevelInstance) obj).id) {
			return true;
		}
		return false;
	}
	
	public Double getLevelMinimum() {
		return levelMinimum;
	}

	public void setLevelMinimum(Double levelMinimum) {
		this.levelMinimum = levelMinimum;
	}

	public Double getLevelMaximum() {
		return levelMaximum;
	}

	public void setLevelMaximum(Double levelMaximum) {
		this.levelMaximum = levelMaximum;
	}

	public List<StructureLevelInstanceDetailed> getLevelInstanceDetailedList() {
		return levelInstanceDetailedList;
	}

	public void setLevelInstanceDetailedList(List<StructureLevelInstanceDetailed> levelInstanceDetailedList) {
		this.levelInstanceDetailedList = levelInstanceDetailedList;
	}

	public Long getExportLevelId() {
		return exportLevelId;
	}

	public void setExportLevelId(Long exportLevelId) {
		this.exportLevelId = exportLevelId;
	}

	public Long getExportPlanId() {
		return exportPlanId;
	}

	public void setExportPlanId(Long exportPlanId) {
		this.exportPlanId = exportPlanId;
	}
	
	public String getExportResponsibleMail() {
		return exportResponsibleMail;
	}

	public void setExportResponsibleMail(String exportResponsibleMail) {
		this.exportResponsibleMail = exportResponsibleMail;
	}
	
	public StructureLevelInstance getLevelParent() {
		return levelParent;
	}

	public void setLevelParent(StructureLevelInstance levelParent) {
		this.levelParent = levelParent;
	}
}