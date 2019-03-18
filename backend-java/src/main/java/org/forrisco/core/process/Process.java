package org.forrisco.core.process;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forpdi.core.company.Company;
import org.forpdi.system.Archive;
import org.forrisco.core.unit.Unit;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Process.TABLE)
@Table(name = Process.TABLE)

public class Process extends SimpleLogicalDeletableEntity implements Serializable {
	public static final String TABLE = "frisco_process";
	private static final long serialVersionUID = 1L;

	@SkipSerialization
	@ManyToOne(targetEntity=Company.class,  fetch=FetchType.EAGER)
	private Company company;
	
	//@SkipSerialization
	@ManyToOne(targetEntity=Archive.class,  fetch=FetchType.EAGER)
	private Archive file;
	
	@ManyToOne(targetEntity=Unit.class,  fetch=FetchType.EAGER)
	private Unit unitCreator;
	
	
	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=4000)
	private String objective;
	
	@Column(nullable=false, length=4000) 
	private String fileLink;
		
	
	
	
	@Transient
	private Unit unit;
	
	@Transient
	private List<Unit> relatedUnits;
	
	
	public Process() {}
	
	public Process(Process process) {
		this.file =process.getFile();
		this.name =process.getName();
		this.objective =process.getObjective();
		this.fileLink =process.getFileLink();
		this.company= process.getCompany();
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getFileLink() {
		return fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Unit> getRelatedUnits() {
		return relatedUnits;
	}

	public void setRelatedUnits(List<Unit> relatedUnits) {
		this.relatedUnits = relatedUnits;
	}

	public Archive getFile() {
		return file;
	}

	public void setFile(Archive file) {
		this.file = file;
	}

	public Unit getUnitCreator() {
		return unitCreator;
	}

	public void setUnitCreator(Unit unitCreator) {
		this.unitCreator = unitCreator;
	}
	
	
}
