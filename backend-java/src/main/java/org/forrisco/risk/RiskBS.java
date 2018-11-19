package org.forrisco.risk;

import javax.enterprise.context.RequestScoped;
import br.com.caelum.vraptor.boilerplate.HibernateBusiness;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class RiskBS extends HibernateBusiness {
	
	/**
	 * Salva no banco de dados um novo risco
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void save(Risk risk) {
		risk.setDeleted(false);
		this.persist(risk);
	}

}