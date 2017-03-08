package org.forpdi.planning.permissions;

import java.io.Serializable;

public class PermissionDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String permission;
	private Boolean granted;
	private Integer accessLevel;
	private String description;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Boolean isGranted() {
		return granted;
	}
	public void setGranted(Boolean granted) {
		this.granted = granted;
	}
	
	
}
