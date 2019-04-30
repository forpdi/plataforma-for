package org.forpdi.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Archive.TABLE)
@Table(name = Archive.TABLE)

public class Archive extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_archive";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 255)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
