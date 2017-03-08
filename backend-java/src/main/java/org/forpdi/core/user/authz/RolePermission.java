package org.forpdi.core.user.authz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Renato R. R. de Oliveira
 * 
 */
@Entity(name = RolePermission.TABLE)
@Table(name = RolePermission.TABLE)
public class RolePermission implements Serializable {
	public static final String TABLE = "fpdi_role_permission";
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(targetEntity=Role.class, fetch=FetchType.EAGER, optional=false)
	private Role role;

	@Id
	@Column(nullable=false, length=255)
	private String permission;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}


}
