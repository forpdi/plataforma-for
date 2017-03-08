package org.forpdi.planning.attribute.types.enums;

public enum Periodicity {
	
	DAILY(1, "Diária"), WEEKLY(2, "Semanal"), FORTNIGHTLY(3, "Quinzenal"), MONTHLY(4, "Mensal"), BIMONTHLY(5,
			"Bimestral"), QUARTERLY(6, "Trimestral"), SEMIANNUAL(7, "Semestral"), ANNUAL(8, "Anual"), BIENNIAL(9, "Bienal");
	
	private Integer value;
	private String name;
	
	private Periodicity(Integer value, String name){
		this.value = value;
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
