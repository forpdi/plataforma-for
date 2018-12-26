package org.forrisco.core.process;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.forrisco.core.unit.Unit;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = ProcessUnit.TABLE)
@Table(name = ProcessUnit.TABLE)

public class ProcessUnit extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_process_unit";
	private static final long serialVersionUID = 1L;

	//@Id
	@SkipSerialization
	@JoinColumn(name="process_id")
	@ManyToOne(targetEntity=Process.class,  fetch=FetchType.EAGER)
	///private Set<Process> process = new HashSet<>();
	private Process process;
	
	//@Id
	@SkipSerialization
	@JoinColumn(name="unit_id") 
	@ManyToOne(targetEntity=Unit.class,  fetch=FetchType.EAGER)
	///private Set<Unit> unit = new HashSet<>();
	private Unit unit;
	
	public void setProcess(Process process) {
		this.process = process;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Process getProcess() {
		return process;
	}

	public Unit getUnit() {
		return unit;
	}
	
}
