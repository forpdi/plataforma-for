package org.forrisco.risk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forpdi.core.user.User;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = Risk.TABLE)
@Table(name = Risk.TABLE)

public class Risk extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_risk";
	private static final long serialVersionUID = 1L;

	@SkipSerialization
	@ManyToOne(targetEntity=User.class, optional=false, fetch=FetchType.EAGER)
	private User user;

	//@SkipSerialization
	//@ManyToOne
	//@Id "unit_id"
	//@Id="frisco_process_id"
	//private Unit unit;

	@SkipSerialization
	@ManyToOne(targetEntity=RiskLevel.class, fetch=FetchType.EAGER)
	private RiskLevel riskLevel;
	
	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=255)
	private String code;

	@Column(nullable=false, length=4000)
	private String reason;

	@Column(nullable=false, length=4000)
	private String result;

	@Column(nullable=false)
	private boolean risk_pdi;

	@Column(nullable=false)
	private boolean risk_obj_process;

	@Column(nullable=false)
	private boolean risk_act_process;

	@Column(nullable=false)
	private int pdi;

	@Column(nullable=false)
	private int obj_process;

	@Column(nullable=false)
	private int act_process;

	@Column(nullable=false)
	private int probability;

	@Column(nullable=false)
	private int impact;

	@Column(nullable=false)
	private int periodicity;

	@Column(nullable=false)
	private int tipology;

	@Column(nullable=false)
	private int type;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isRisk_pdi() {
		return risk_pdi;
	}

	public void setRisk_pdi(boolean risk_pdi) {
		this.risk_pdi = risk_pdi;
	}

	public boolean isRisk_obj_process() {
		return risk_obj_process;
	}

	public void setRisk_obj_process(boolean risk_obj_process) {
		this.risk_obj_process = risk_obj_process;
	}

	public boolean isRisk_act_process() {
		return risk_act_process;
	}

	public void setRisk_act_process(boolean risk_act_process) {
		this.risk_act_process = risk_act_process;
	}

	public int getPdi() {
		return pdi;
	}

	public void setPdi(int pdi) {
		this.pdi = pdi;
	}

	public int getObj_process() {
		return obj_process;
	}

	public void setObj_process(int obj_process) {
		this.obj_process = obj_process;
	}

	public int getAct_process() {
		return act_process;
	}

	public void setAct_process(int act_process) {
		this.act_process = act_process;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public int getImpact() {
		return impact;
	}

	public void setImpact(int impact) {
		this.impact = impact;
	}

	public int getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(int periodicity) {
		this.periodicity = periodicity;
	}

	public int getTipology() {
		return tipology;
	}

	public void setTipology(int tipology) {
		this.tipology = tipology;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public org.forpdi.core.user.User getUser() {
		return user;
	}

	public void setUser(org.forpdi.core.user.User user) {
		this.user = user;
	}

	public RiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

}