package org.forpdi.core.user.authz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.caelum.vraptor.boilerplate.SimpleEntity;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = Role.TABLE)
@Table(name = Role.TABLE)
public class Role extends SimpleEntity {
	public static final String TABLE = "fpdi_role";
	private static final long serialVersionUID = 1L;

	@Column(nullable=false,length=255)
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
