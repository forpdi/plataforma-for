package org.forpdi.planning.document;

import java.io.Serializable;
import java.util.List;
public class DocumentDTO implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Document document;
	private List<DocumentSectionDTO> sections;
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public List<DocumentSectionDTO> getSections() {
		return sections;
	}
	public void setSections(List<DocumentSectionDTO> sections) {
		this.sections = sections;
	}	

}
