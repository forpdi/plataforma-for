package org.forpdi.planning.fields;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;

@Entity(name = OptionsField.TABLE)
@Table(name = OptionsField.TABLE)
public class OptionsField extends SimpleLogicalDeletableEntity {
	public static final String TABLE = "fpdi_options_field";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true)
	private String label;
	
	@Column(nullable = false)
	private Long attributeId;
	
	@Column(nullable = true)
	private Long columnId;
	
	@Column(nullable = true)
	private boolean isDocument = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creation = new Date();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public boolean isDocument() {
		return isDocument;
	}

	public void setDocument(boolean isDocument) {
		this.isDocument = isDocument;
	}	

}
