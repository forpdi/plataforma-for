package org.forrisco.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forpdi.core.user.User;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Monitor.TABLE)
@Table(name = Monitor.TABLE)

public class Monitor extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_monitor";
	private static final long serialVersionUID = 1L;

	@SkipSerialization
	@ManyToOne(targetEntity=User.class, optional=false, fetch=FetchType.EAGER)
	private User user;

	@SkipSerialization
	@ManyToOne(targetEntity=Risk.class, optional=false, fetch=FetchType.EAGER)
	private Risk risk;

	@Column(nullable=false, length=4000)
	private String report;

	@Column(nullable=false, length=40)
	private String probability;

	@Column(nullable=false, length=40)
	private String impact;

	@Column(nullable=false)
	private Date begin;
	
	@Transient
	private Long riskId;
	
	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Risk getRisk() {
		return risk;
	}

	public void setRisk(Risk risk) {
		this.risk = risk;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
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

	public Long getRiskId() {
		return riskId;
	}

	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}

}