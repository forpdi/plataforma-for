package org.forrisco.risk;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.forrisco.core.item.Item;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.process.ProcessUnit;
import org.forrisco.core.unit.Unit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class RiskBS extends HibernateBusiness {
	

	
	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param policy,
	 *            instância da política a ser salva
	 */
	public void saveRiskLevel(Policy policy) {
		
		String[][] str =  policy.getRisk_level();
		
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
	 * Retorna uma lista de grau de risco a partir da política
	 * 
	 * @param policy
	 * 
	 */
	public PaginatedList<RiskLevel> listRiskLevel(Policy policy) {
		List<RiskLevel> array = new  ArrayList<RiskLevel>();
		PaginatedList<RiskLevel> list = new  PaginatedList<RiskLevel>();
		String[][] str =  policy.getRisk_level();
		
		for(int i = 0; i<str[0].length;i++) {
			if(str[1][i] !=null) {
				RiskLevel rk= new RiskLevel();		
				rk.setId(null);
				rk.setColor(Integer.parseInt(str[1][i]));
				rk.setLevel(str[0][i]);
				rk.setPolicy(policy);
				array.add(rk);
			}
		}
		
		list.setList(array);
		list.setTotal(Long.valueOf(array.size()));
		return list;
	}
	
	
	/**
	 * Salva no banco de dados graus de risco
	 * 
	 * @param RiskLevel,
	 *            instância de um grau de risco a ser salvo
	 */
	public void saveRiskLevel(RiskLevel risklevel) {
		risklevel.setDeleted(false);
		this.persist(risklevel);	
	}
	
		
	/**
	 *Deleta do banco de dados  graus de risco
	 * 
	 * @param Item,
	 *			instância do item a ser deletado
	 */
	public void delete(RiskLevel risklevel) {
		risklevel.setDeleted(true);
		this.persist(risklevel);
	}



	public PaginatedList<RiskLevel> listRiskLevelbyPolicy(Policy policy) {
		PaginatedList<RiskLevel> results = new PaginatedList<RiskLevel>();
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy));

		Criteria count = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));

		results.setList(this.dao.findByCriteria(criteria, RiskLevel.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	
	
	/**
	 * Recuperar riscos de uma unidade
	 * 
	 * @param Unit,
	 *            instância da unidade
	 *            
	 */
	public PaginatedList<Risk> listRiskbyUnit(Unit unit) {
		PaginatedList<ProcessUnit> results = new PaginatedList<ProcessUnit>();
		
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));

		Criteria count = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit))
				.setProjection(Projections.countDistinct("id"));
		
		

		results.setList(this.dao.findByCriteria(criteria, ProcessUnit.class));
		results.setTotal((Long) count.uniqueResult());
		
		LOGGER.error("Risk >> "+results);
		
		//return results;
		return null;
	}

}