package org.forpdi.planning.fields.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = TableValues.TABLE)
@Table(name = TableValues.TABLE)
public class TableValues extends SimpleLogicalDeletableEntity{
	public static final String TABLE = "fpdi_table_values";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true, length=4000)
	private String value;
	
	@Column(nullable = true)
	private Double valueAsNumber;

	@Column(nullable = true)
	private Date valueAsDate;
	
	@SkipSerialization
	@ManyToOne(targetEntity=TableInstance.class, optional=false, fetch=FetchType.EAGER)
	private TableInstance tableInstance;
	
	@ManyToOne(targetEntity=TableStructure.class, optional=false, fetch=FetchType.EAGER)
	private TableStructure tableStructure;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getValueAsNumber() {
		return valueAsNumber;
	}

	public void setValueAsNumber(Double valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}

	public Date getValueAsDate() {
		return valueAsDate;
	}

	public void setValueAsDate(Date valueAsDate) {
		this.valueAsDate = valueAsDate;
	}

	public TableInstance getTableInstance() {
		return tableInstance;
	}

	public void setTableInstance(TableInstance tableInstance) {
		this.tableInstance = tableInstance;
	}

	public TableStructure getTableStructure() {
		return tableStructure;
	}

	public void setTableStructure(TableStructure tableStructure) {
		this.tableStructure = tableStructure;
	}
	
}
