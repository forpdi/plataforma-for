package org.forrisco.core.process;

import java.util.HashSet;
import java.util.Set;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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

	@Id
	@SkipSerialization
	@ManyToOne(targetEntity=Process.class,  fetch=FetchType.EAGER)
	private Set<Process> process = new HashSet<>();

	@Id
	@SkipSerialization
	@ManyToOne(targetEntity=Unit.class,  fetch=FetchType.EAGER)
	private Set<Unit> unit = new HashSet<>();

	public Set<Process> getProcess() {
		return process;
	}

	public void setProcess(Set<Process> process) {
		this.process = process;
	}

	public Set<Unit> getUnit() {
		return unit;
	}

	public void setUnit(Set<Unit> unit) {
		this.unit = unit;
	}
	
	
}
