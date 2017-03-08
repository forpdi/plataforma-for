package org.forpdi.planning.attribute.types.enums;

import java.io.Serializable;

public enum CalculationType implements Serializable{
	NORMAL_AVG(0), WEIGHTED_AVG(1), SUM(2);
	private final Integer value;
	
	private CalculationType(Integer value) {
		this.value = value;
	}
	
	public Integer getValue(){
		return value;
	}
}
