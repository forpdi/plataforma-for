package org.forpdi.dashboard.manager;

import java.io.Serializable;

public class IndicatorHistory implements Serializable, Comparable<IndicatorHistory> {
	private static final long serialVersionUID = 1L;

	private Double value;
	private String period;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public int compareTo(IndicatorHistory o) {
		if(o.getPeriod().equals(this.getPeriod()))
			return 0;		
		
		return (Double.valueOf(o.getPeriod()) < Double.valueOf(this.getPeriod()) ? 1 : -1);
	}

}
