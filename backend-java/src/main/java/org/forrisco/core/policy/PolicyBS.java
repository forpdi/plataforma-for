package org.forrisco.core.policy;


import javax.enterprise.context.RequestScoped;

import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.Item;
import org.forrisco.risk.RiskLevel;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class PolicyBS extends HibernateBusiness {
	
	/**
	 * Salva no banco de dados uma nova política
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void save(Policy policy) {
		policy.setDeleted(false);
		this.persist(policy);
	}
	
	/**
	 * Salva no banco de dados uma novo grau de risco
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void saveRiskLevel(Policy policy) {
		
		String[][] str =  policy.getRisk_leve();
		
		for(int i = 0; i<str[0].length;i++) {
			if(str[1][i] !=null) {
				RiskLevel rk= new RiskLevel();		
				rk.setId(null);
				rk.setColor(Integer.parseInt(str[1][i]));
				rk.setLevel(str[0][i]);
				rk.setPolicy(policy);
				this.persist(rk);	
			}
		}
	}

	/**
	 * Salva no banco de dados um item
	 * 
	 * @param item,
	 *            instância da política a ser salva
	 */
	public void save(Item item) {
		this.persist(item);
	}

	/**
	 * Salva no banco de dados um campo de item
	 * 
	 * @param Fielditem,
	 *            instância da política a ser salva
	 */
	public void save(FieldItem it) {
		this.persist(it);
	}
	
}