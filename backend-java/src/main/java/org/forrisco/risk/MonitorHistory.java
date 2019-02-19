package org.forrisco.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.forrisco.core.unit.Unit;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;


/**
 * @author Matheus Nascimento
 * 
 */
@Entity(name = MonitorHistory.TABLE)
@Table(name = MonitorHistory.TABLE)
public class MonitorHistory extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "frisco_monitor_history";
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(targetEntity=Unit.class, optional=false, fetch=FetchType.EAGER)
	private Unit unit;
	
	@Column(nullable=false, length=200)
	private String estado;
	
	@Column(nullable=false)
	private int month;

	@Column(nullable=false)
	private int year;

	@Column(nullable=false)
	private int quantity;

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	
}
