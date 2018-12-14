package org.forrisco.core.item;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = SubItem.TABLE)
@Table(name = SubItem.TABLE)

public class SubItem extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_subitem";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String name;

	@Column(nullable = true, length=4000)
	private String description;

	@SkipSerialization
	@ManyToOne(targetEntity=Item.class, optional=false, fetch=FetchType.EAGER)
	private Item item;
	
	@Transient
	private List<FieldSubItem> fieldSubItem;
	
	
	public List<FieldSubItem> getFieldSubItem() {
		return fieldSubItem;
	}

	public void setFieldSubItem(List<FieldSubItem> fieldSubItem) {
		this.fieldSubItem = fieldSubItem;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}