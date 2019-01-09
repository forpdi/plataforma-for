package org.forrisco.core.item;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forrisco.core.policy.Policy;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Item.TABLE)
@Table(name = Item.TABLE)

public class Item extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_item";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String name;
	
	@Column(nullable = true, length=4000)
	private String description;

	@SkipSerialization
	@ManyToOne(targetEntity=Policy.class, optional=false, fetch=FetchType.EAGER)
	private Policy policy;
	
	@Transient
	private List<FieldItem> fieldItem;

	public List<FieldItem> getFieldItem() {
		return fieldItem;
	}

	public void setFieldItem(List<FieldItem> fieldItem) {
		this.fieldItem = fieldItem;
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

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

}