package org.forrisco.risk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forrisco.core.policy.Policy;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = RiskLevel.TABLE)
@Table(name = RiskLevel.TABLE)

public class RiskLevel extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_risk_level";
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=255)
	private String level;

	@Column(nullable=false)
	private int color;
	
	@SkipSerialization
	@ManyToOne(targetEntity=Policy.class, optional=false, fetch=FetchType.EAGER)
	private Policy policy;
	
	
	public RiskLevel() {}
	
	public RiskLevel(Policy policy, int color, String level) {
		this.policy=policy;
		this.color=color;
		this.level=level;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int cor) {
			this.color = cor;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
}