package org.forrisco.core.policy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.company.Company;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Policy.TABLE)
@Table(name = Policy.TABLE)

public class Policy extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_policy";
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=true, length=4000)
	private String description;
	
	@Column(nullable=false)
	private int nline;
	
	@Column(nullable=false)
	private int ncolumn;

	@Column(nullable=true, length=4000)
	private String probability;

	@Column(nullable=true, length=4000)
	private String impact;

	@SkipSerialization
	@ManyToOne(targetEntity=Company.class, optional=false, fetch=FetchType.EAGER)
	private Company company;

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

	public int getNline() {
		return nline;
	}

	public void setNline(int nline) {
		this.nline = nline;
	}

	public int getNcolumn() {
		return ncolumn;
	}

	public void setNcolumn(int ncolumn) {
		this.ncolumn = ncolumn;
	}

	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
