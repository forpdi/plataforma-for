package org.forrisco.risk;

public enum CloseToMaturityPeriod {
	//horas
	DIARIO (7),
	//dias
	SEMANAL (2),
	QUINZENAL (7),
	MENSAL (7),
	BIMESTRAL (21),
	TRIMESTRAL (21),
	SEMESTRAL (30),
	ANUAL (30);
	
	private double value;
	
	private CloseToMaturityPeriod(int id) {
		this.value = id;
	}

	public double getValue() {
		return value;
	}
}
