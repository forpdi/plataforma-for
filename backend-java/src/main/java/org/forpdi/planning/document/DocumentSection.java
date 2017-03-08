package org.forpdi.planning.document;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity(name = DocumentSection.TABLE)
@Table(
	name = DocumentSection.TABLE,
	indexes = {
		@Index(columnList="sequence")
	}
)
public class DocumentSection extends SimpleLogicalDeletableEntity   {
	public static final String TABLE = "fpdi_document_section";
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, length=255)
	private String name;
	
	@ManyToOne(targetEntity=Document.class, optional=false, fetch=FetchType.EAGER)
	@SkipSerialization
	private Document document;
	
	@ManyToOne(targetEntity=DocumentSection.class, optional=true, fetch=FetchType.EAGER)
	@SkipSerialization
	private DocumentSection parent;
	
	private boolean leaf = false;
	
	private int sequence;
	
	@Transient
	private List<DocumentAttribute> documentAttributes;
	
	@Transient
	private List<DocumentSection> sectionsSons;
	
	@Transient
	private boolean filled;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public DocumentSection getParent() {
		return parent;
	}
	public void setParent(DocumentSection parent) {
		this.parent = parent;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public List<DocumentAttribute> getDocumentAttributes() {
		return documentAttributes;
	}
	public void setDocumentAttributes(List<DocumentAttribute> documentAttributes) {
		this.documentAttributes = documentAttributes;
	}
	public List<DocumentSection> getSectionsSons() {
		return sectionsSons;
	}
	public void setSectionsSons(List<DocumentSection> sectionsSons) {
		this.sectionsSons = sectionsSons;
	}
	public boolean isFilled() {
		return filled;
	}
	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	
}
