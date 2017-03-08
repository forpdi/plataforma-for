package org.forpdi.planning.document;

import java.io.Serializable;
import java.math.BigInteger;

public class DocumentSectionFilledDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger id;
	private String name;
	private BigInteger filled;
	
	public DocumentSectionFilledDTO() {
		super();
	}
	public DocumentSectionFilledDTO(BigInteger id, String name, BigInteger filled) {
		super();
		this.id = id;
		this.name = name;
		this.filled = filled;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigInteger getFilled() {
		return filled;
	}
	public void setFilled(BigInteger filled) {
		this.filled = filled;
	}
	
	@Override
	public String toString() {
		return "DocumentSectionFilledDTO [id=" + id + ", name=" + name + ", filled=" + filled + "]";
	}
	
	
	
	
}
