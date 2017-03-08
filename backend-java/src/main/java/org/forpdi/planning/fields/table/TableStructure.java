package org.forpdi.planning.fields.table;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.forpdi.planning.fields.OptionsField;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = TableStructure.TABLE)
@Table(name = TableStructure.TABLE)
public class TableStructure extends SimpleLogicalDeletableEntity{
	public static final String TABLE = "fpdi_table_structure";
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length=255)
	private String label;
	
	@Column(nullable = false, length=255)
	private String type;
	
	@Column(nullable = true)
	private boolean isInTotal = false;
	
	@SkipSerialization
	@ManyToOne(targetEntity=TableFields.class, optional=false, fetch=FetchType.EAGER)
	private TableFields tableFields;
	
	@Transient
	private List<OptionsField> optionsField;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TableFields getTableFields() {
		return tableFields;
	}

	public void setTableFields(TableFields tableFields) {
		this.tableFields = tableFields;
	}

	public List<OptionsField> getOptionsField() {
		return optionsField;
	}

	public void setOptionsField(List<OptionsField> optionsField) {
		this.optionsField = optionsField;
	}

	public boolean isInTotal() {
		return isInTotal;
	}

	public void setInTotal(boolean isInTotal) {
		this.isInTotal = isInTotal;
	}

}
