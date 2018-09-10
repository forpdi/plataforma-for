package org.forpdi.planning.attribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = AggregateIndicator.TABLE)
@Table(name = AggregateIndicator.TABLE, uniqueConstraints = @UniqueConstraint(columnNames = { "id" }), indexes = {
		@Index(columnList = "aggregate_id,indicator_id")})
public class AggregateIndicator extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_aggregate_indicator";
	private static final long serialVersionUID = 1L;

	@SkipSerialization
	@ManyToOne(targetEntity = StructureLevelInstance.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevelInstance indicator;

	@ManyToOne(targetEntity = StructureLevelInstance.class, optional = false, fetch = FetchType.EAGER)
	private StructureLevelInstance aggregate;

	@Column(nullable = false)
	private Double percentage;
	
	@Transient private Long exportAggregateId;
	
	@Transient private Long exportIndicatorId;

	public StructureLevelInstance getIndicator() {
		return indicator;
	}

	public void setIndicator(StructureLevelInstance indicator) {
		this.indicator = indicator;
	}

	public StructureLevelInstance getAggregate() {
		return aggregate;
	}

	public void setAggregate(StructureLevelInstance aggregate) {
		this.aggregate = aggregate;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "AggregateIndicator [indicator=" + indicator.toString() + ", aggregate=" + aggregate.toString() + ", percentage=" + percentage
				+ "]";
	}
	
	public Long getExportAggregateId() {
		return exportAggregateId;
	}

	public void setExportAggregateId(Long exportAggregateId) {
		this.exportAggregateId = exportAggregateId;
	}

	public Long getExportIndicatorId() {
		return exportIndicatorId;
	}

	public void setExportIndicatorId(Long exportIndicatorId) {
		this.exportIndicatorId = exportIndicatorId;
	}

}