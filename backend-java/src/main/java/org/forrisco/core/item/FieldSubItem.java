package org.forrisco.core.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forrisco.core.item.SubItem;
import org.hibernate.annotations.Type;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = FieldSubItem.TABLE)
@Table(name = FieldSubItem.TABLE)

public class FieldSubItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_subitem_field";
	private static final long serialVersionUID = 1L;
	
	@SkipSerialization
	@ManyToOne(targetEntity=SubItem.class, optional=false, fetch=FetchType.EAGER)
	private SubItem subitem;
	
	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true)
	@Type(type="text")
	private String description;

	@Column(nullable = false)
	private boolean isText;
	
	@Column(length=255)
	private String fileLink;
	
	@Transient
	private String value;
	
	public SubItem getSubitem() {
		return subitem;
	}

	public void setSubitem(SubItem subitem) {
		this.subitem = subitem;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public boolean isText() {
		return isText;
	}

	public void setText(boolean isText) {
		this.isText = isText;
	}

	public String isFileLink() {
		return fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}


}