package org.forrisco.risk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.company.Company;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

/**
 * @author Felippe Kipman
 */
@Entity(name = RiskTipology.TABLE)
@Table(name = RiskTipology.TABLE)
public class RiskTipology extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_risk_tipology";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 4000)
	private String name;

	@ManyToOne(targetEntity = Company.class, fetch = FetchType.EAGER)
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