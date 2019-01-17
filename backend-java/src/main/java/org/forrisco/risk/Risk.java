package org.forrisco.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forpdi.core.user.User;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.objective.RiskActivity;
import org.forrisco.risk.objective.RiskProcess;
import org.forrisco.risk.objective.RiskStrategy;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
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

	@SkipSerialization
	@ManyToOne(targetEntity=Unit.class, optional=false, fetch=FetchType.EAGER)
	private Unit unit;
	
	@SkipSerialization
	@ManyToOne(targetEntity=RiskLevel.class, optional=false, fetch=FetchType.EAGER)
	private RiskLevel riskLevel;
	
	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=255)
	private String code;

	@Column(nullable=false, length=4000)
	private String reason;

	@Column(nullable=false, length=4000)
	private String result;

	@Column(nullable=false , length=400)
	private String probability;

	@Column(nullable=false , length=400)
	private String impact;

	@Column(nullable=false , length=400)
	private String  periodicity;

	@Column(nullable=false , length=400)
	private String tipology;

	@Column(nullable=false , length=4010)
	private String type;
	
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
	private Date begin;
	
	@Column(nullable=true)
	private String linkFPDI;
	
	@Transient
	private PaginatedList<RiskActivity> activities;
	
	@Transient
	private PaginatedList<RiskProcess> processes;
	
	@Transient
	private PaginatedList<RiskStrategy> strategies;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
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
	public String getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	public String getTipology() {
		return tipology;
	}
	public void setTipology(String tipology) {
		this.tipology = tipology;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	public RiskLevel getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(RiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getLinkFPDI() {
		return linkFPDI;
	}
	public void setLinkFPDI(String linkFPDI) {
		this.linkFPDI = linkFPDI;
	}
	public PaginatedList<RiskActivity> getActivities() {
		return activities;
	}
	public void setActivities(PaginatedList<RiskActivity> activities) {
		this.activities = activities;
	}
	public PaginatedList<RiskProcess> getProcess() {
		return processes;
	}
	public void setProcess(PaginatedList<RiskProcess> process) {
		this.processes = process;
	}
	public PaginatedList<RiskStrategy> getStrategies() {
		return strategies;
	}
	public void setStrategies(PaginatedList<RiskStrategy> strategies) {
		this.strategies = strategies;
	}

}