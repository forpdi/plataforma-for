package org.forpdi.planning.document;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para enviar dados das seções para a tela.
 * 
 * @author Renato Oliveira
 *
 */
public class DocumentSectionDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long documentId;
	private Long parentId;
	private int sequence;
	private long attributesAmount = 0L;
	private boolean leaf = false;
	private String name;
	private List<DocumentSectionDTO> children;
	private boolean preTextSection; 

	public DocumentSectionDTO(DocumentSection section, DocumentBS bs) {
		this.id = section.getId();
		this.documentId = section.getDocument().getId();
		this.sequence = section.getSequence();
		this.name = section.getName();
		this.preTextSection = section.isPreTextSection();
		this.attributesAmount = bs.countAttributesPerSection(section);
		
		if (section.getParent() != null) {
			this.parentId = section.getParent().getId();
			this.children = null;
			this.leaf = true;
		} else {
			this.children = bs.listSectionDTOsByDocument(section.getDocument(), section.getId());
			this.leaf = false;
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public long getAttributesAmount() {
		return attributesAmount;
	}
	public void setAttributesAmount(long attributesAmount) {
		this.attributesAmount = attributesAmount;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DocumentSectionDTO> getChildren() {
		return children;
	}
	public void setChildren(List<DocumentSectionDTO> children) {
		this.children = children;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	
	public boolean isPreTextSection() {
		return preTextSection;
	}

	public void setPreTextSection(boolean preTextSection) {
		this.preTextSection = preTextSection;
	}
}
