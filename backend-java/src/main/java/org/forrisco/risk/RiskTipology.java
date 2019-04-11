package org.forrisco.risk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.forpdi.core.company.Company;
import org.forrisco.core.item.Item;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Felippe Kipman
 * 
 */
@Entity(name = RiskTipology.TABLE)
@Table(name = RiskTipology.TABLE)

public class RiskTipology extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_risk_tipology";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String name;
	
	@SkipSerialization
	@OneToOne(targetEntity=Company.class, optional=false, fetch=FetchType.EAGER)
	private Company company;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
