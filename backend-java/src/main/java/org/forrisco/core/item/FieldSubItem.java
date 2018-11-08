package org.forrisco.core.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forrisco.core.item.SubItem;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = FieldSubItem.TABLE)
@Table(name = FieldSubItem.TABLE)

public class FieldSubItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_field_subitem";
	private static final long serialVersionUID = 1L;
	
	@SkipSerialization
	@ManyToOne(targetEntity=SubItem.class, optional=false, fetch=FetchType.EAGER)
	private SubItem subitem;
	
	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true, length=4000)
	private String description;

	@Column(nullable = false)
	private boolean isText;
	
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

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	public boolean isFileLink() {
		return fileLink;
	}

	public void setFileLink(boolean fileLink) {
		this.fileLink = fileLink;
	}

	@Column(nullable = false, length=255)
	private boolean fileLink;

}