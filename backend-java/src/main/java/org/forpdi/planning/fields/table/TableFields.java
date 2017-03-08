package org.forpdi.planning.fields.table;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

@Entity(name = TableFields.TABLE)
@Table(name = TableFields.TABLE)
public class TableFields extends SimpleLogicalDeletableEntity{
	public static final String TABLE = "fpdi_table_field";
	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="tableFields", targetEntity=TableStructure.class)
	@Cascade(CascadeType.ALL)
	private List<TableStructure> tableStructures;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="tableFields", targetEntity=TableInstance.class)
	@Cascade(CascadeType.ALL)
	@Where(clause="deleted=0")
	private List<TableInstance> tableInstances;
	
	@Column(nullable = false)
	private Long attributeId;
	
	@Column(nullable = true)
	private boolean isDocument = false;

	public List<TableStructure> getTableStructures() {
		return tableStructures;
	}

	public void setTableStructures(List<TableStructure> tableStructures) {
		this.tableStructures = tableStructures;
	}

	public List<TableInstance> getTableInstances() {
		return tableInstances;
	}

	public void setTableInstances(List<TableInstance> tableInstances) {
		this.tableInstances = tableInstances;
	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public boolean getIsDocument() {
		return isDocument;
	}

	public void setIsDocument(boolean isDocument) {
		this.isDocument = isDocument;
	}
	
}
