package org.forrisco.risk.objective;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.planning.structure.StructureLevelInstance;
import org.forrisco.risk.Risk;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = RiskStrategy.TABLE)
@Table(name = RiskStrategy.TABLE)

public class RiskStrategy extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_risk_strategy";
	private static final long serialVersionUID = 1L;

	@SkipSerialization
	@JoinColumn(name="risk_id") 
	@ManyToOne(targetEntity=Risk.class, optional=false, fetch=FetchType.EAGER)
	private Risk risk;

	@SkipSerialization
	@JoinColumn(name="structure_id") 
	@ManyToOne(targetEntity=StructureLevelInstance.class, optional=false, fetch=FetchType.EAGER)
	private StructureLevelInstance structure;
	
	@Column(nullable=false, length=1000)
	private String linkFPDI;

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	public String getLinkFPDI() {
		return linkFPDI;
	}

	public void setLinkFPDI(String linkFPDI) {
		this.linkFPDI = linkFPDI;
	}


	public StructureLevelInstance getStructure() {
		return structure;
	}

	public void setStructure(StructureLevelInstance strategy) {
		this.structure = strategy;
	}
	
	
}