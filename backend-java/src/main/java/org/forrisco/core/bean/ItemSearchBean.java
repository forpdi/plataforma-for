package org.forrisco.core.bean;

import java.io.Serializable;

/**
 * @autor Erick Alves
 */

public class ItemSearchBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long parentId;
	private String name;
	private String description;
	private String level;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}
