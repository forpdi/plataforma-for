package org.forrisco.core.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;



import org.forpdi.core.user.User;
import org.forrisco.core.plan.Plan;
import org.forrisco.core.policy.Policy;

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

	@Column(nullable = false, length=255)
	private String abbreviation;
	
	@Column(nullable = true, length=4000)
	private String description;

	@SkipSerialization
	@OneToOne(targetEntity=Item.class, optional=false, fetch=FetchType.EAGER)
	private Item item;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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