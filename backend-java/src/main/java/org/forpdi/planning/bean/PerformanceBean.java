package org.forpdi.planning.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * Bean que armazena os dados de performance de um nível ou plano.
 * Contempla os valores alcançados, mínimos e máximos médios.
 * Nessa caso, o esperado é sempre 100% que representa o rendimento percentual.
 * @author Renato R. R. de Oliveira
 *
 */
@Embeddable
public class PerformanceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double performance;
	private Double minimumAverage;
	private Double maximumAverage;
	
	public Double getPerformance() {
		return performance;
	}
	public void setPerformance(Double performance) {
		this.performance = performance;
	}
	public Double getMinimumAverage() {
		return minimumAverage;
	}
	public void setMinimumAverage(Double minimumAverage) {
		this.minimumAverage = minimumAverage;
	}
	public Double getMaximumAverage() {
		return maximumAverage;
	}
	public void setMaximumAverage(Double maximumAverage) {
		this.maximumAverage = maximumAverage;
	}
	
}
