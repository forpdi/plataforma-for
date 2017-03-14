package org.forpdi.planning.filters;

public enum PeformanceFilterType {
	BELOW_MINIMUM(1), BELOW_EXPECTED(2), ENOUGH(3), ABOVE_MAXIMUM(4), NOT_STARTED(5);

	private int value;

	PeformanceFilterType(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}

	/**
	 * Retorna string que representa um status
	 * @param filter
	 * 		Id fo filter
	 * @return
	 */
	public static PeformanceFilterType valueOf(Integer filter) {
		switch (filter) {
		case 1:
			return BELOW_MINIMUM;
		case 2:
			return BELOW_EXPECTED;
		case 3:
			return ENOUGH;
		case 4:
			return ABOVE_MAXIMUM;
		case 5:
			return NOT_STARTED;
		default:
			return null;
		}		
	}
}
