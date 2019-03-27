package org.forrisco.core.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = FieldItem.TABLE)
@Table(name = FieldItem.TABLE)

public class FieldItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_item_field";
	private static final long serialVersionUID = 1L;

	@SkipSerialization
	@ManyToOne(targetEntity=Item.class, optional=false, fetch=FetchType.EAGER)
	private Item item;

	@Column(nullable = false, length=400)
	private String name;

	@Column(nullable = true, columnDefinition="longtext")
	private String description;

	@Column(nullable = false)
	private boolean isText;

	@Column(length=400)
	private String fileLink;
	
	@Transient
	private String value;
	
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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

	public String getFileLink() {
		return fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

}