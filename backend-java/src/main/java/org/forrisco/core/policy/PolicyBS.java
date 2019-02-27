package org.forrisco.core.policy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.forpdi.core.company.Company;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.FieldSubItem;
import org.forrisco.core.item.Item;
import org.forrisco.core.item.SubItem;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.risk.RiskLevel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class PolicyBS extends HibernateBusiness {
	
	
	private static final int PAGESIZE = 50;
	
	
	
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
	
	/**
	 * Deleta do banco de dados uma política
	 * 
	 * @param Policy,
	 *            política a ser deletada
	 */
	public void delete(Policy policy) {
		policy.setDeleted(true);
		this.persist(policy);
	}
	
	/**
	 * Lista as políticas de uma instituição.
	 * @param company
	 * 			Companhia da qual se deseja obter aspolíticas.
	 * @param archived
	 * 			Filtro para arquivado ou não (true ou false). 
	 * @param page
	 * 			Número da página da lista a ser acessada. 
	 * @return PaginatedList<Policy>
	 * 			Lista de políticas.
	 */
	public PaginatedList<Policy> listPolicies(Company company, boolean archived, Integer page) {
		PaginatedList<Policy> results = new PaginatedList<Policy>();
		Criteria criteria = this.dao.newCriteria(Policy.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("archived", archived))
				.add(Restrictions.eq("company", company))
				.addOrder(Order.asc("id"));
		
		Criteria count = this.dao.newCriteria(Policy.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("archived", archived))
				.add(Restrictions.eq("company", company))
				.setProjection(Projections.countDistinct("id"));
		
		if(page != null){
			criteria.setMaxResults(PAGESIZE).setFirstResult(page * PAGESIZE);
		}
		
		results.setList(this.dao.findByCriteria(criteria, Policy.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Lista os planos com esta política.
	 * 
	 * @param Policy
	 * 			política da qual se deseja obter os planos.
	 * 
	 * @return PaginatedList<PlanRisk>
	 * 			Lista os planos de risco.
	 */
	public  PaginatedList<PlanRisk> listPlanbyPolicy(Policy policy) {
		PaginatedList<PlanRisk> results = new PaginatedList<PlanRisk>();
		
		Criteria criteria = this.dao.newCriteria(PlanRisk.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("policy", policy))
				.addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(PlanRisk.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));
		
		
		results.setList(this.dao.findByCriteria(criteria, PlanRisk.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Lista os planos com esta política.
	 * 
	 * @param Policy
	 * 			política da qual se deseja obter os planos.
	 * 
	 * @return PaginatedList<RiskLevel>
	 * 			Lista os graus de risco desta política.
	 */
	public PaginatedList<RiskLevel> listRiskLevelbyPolicy(Policy policy) {

		PaginatedList<RiskLevel> results = new PaginatedList<RiskLevel>();
		
		Criteria criteria = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("policy", policy))
				.addOrder(Order.asc("id"));
		
		Criteria count = this.dao.newCriteria(RiskLevel.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy))
				.setProjection(Projections.countDistinct("id"));
		
		
		results.setList(this.dao.findByCriteria(criteria, RiskLevel.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}



	/**
	 * Listar os itens/subitens pelo termo da busca.
	 * 
	 * @param policy
	 *           	Política.
	 * @param terms
	 *            Termo da busca.
	 * @param itensSelect
	 *            Lista de itens.
	 * @param ordResult
	 *            Ordem.
	 * @return results Lista de instâncias dos itens/subitens.
	 */
	public List<Item> listItemTerms(Policy policy, String terms, Long[] itensSelect, int ordResult) {
		if (terms == null || terms.isEmpty()) {
			return new ArrayList<Item>();
		}
		if(itensSelect != null && itensSelect.length == 0) {
			return new ArrayList<Item>();
		}
		
		Criterion name = Restrictions.like("name", "%" + terms + "%").ignoreCase();
		Criterion description = Restrictions.like("description", "%" + terms + "%").ignoreCase();
		LogicalExpression orExp = Restrictions.or(name, description);
		
		Criteria criteria = this.dao.newCriteria(Item.class)
				.add(Restrictions.eq("policy", policy))
				.add(Restrictions.eq("deleted", false));	
		
		if (itensSelect != null) {
			Disjunction or = Restrictions.disjunction();
			for (int i = 0; i < itensSelect.length; i++) {
				or.add(Restrictions.eq("id", itensSelect[i]));
			}
			criteria.add(or);
		}

		Map<Long,Item> itens = new HashMap<Long,Item>();
		List<Item> list = this.dao.findByCriteria(criteria, Item.class);

		for(int i=0; i < list.size(); i++) {
			Criteria crit = this.dao.newCriteria(FieldItem.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("item", list.get(i)))
		 		.add(orExp);
			List<FieldItem> fields = this.dao.findByCriteria(crit, FieldItem.class);

			for(int j=0; j < fields.size(); j++) {
				fields.get(j).getItem().setDescription(fields.get(j).getName());
				itens.put(fields.get(j).getItem().getId(),fields.get(j).getItem());
			}
		}

		criteria.add(orExp);
		list = this.dao.findByCriteria(criteria, Item.class);
		
		for(int i=0; i< list.size(); i++) {
			itens.put(list.get(i).getId(),list.get(i));
		}
			
		return new ArrayList<Item>(itens.values());
	}

	public List<SubItem> listSubitemTerms(Policy policy, String terms, Long[] subitensSelect, int ordResult) {
		if (terms == null || terms.isEmpty()) {
			return new ArrayList<SubItem>();
		}
		if(subitensSelect != null && subitensSelect.length == 0) {
			return new ArrayList<SubItem>();
		}
		
		Criterion name = Restrictions.like("name", "%" + terms + "%").ignoreCase();
		Criterion description = Restrictions.like("description", "%" + terms + "%").ignoreCase();
		LogicalExpression orExp = Restrictions.or(name,description);
		
		Criteria criteria = this.dao.newCriteria(SubItem.class)
				.createAlias("item", "item", JoinType.INNER_JOIN)
				.add(Restrictions.eq("item.policy", policy))
				.add(Restrictions.eq("deleted", false));
		
		if (subitensSelect != null) {
			Disjunction or = Restrictions.disjunction();
			for (int i = 0; i < subitensSelect.length; i++) {
				or.add(Restrictions.eq("id", subitensSelect[i]));
			}
			criteria.add(or);
		}

		Map<Long,SubItem> subitens = new HashMap<Long,SubItem>();
		List<SubItem> list = this.dao.findByCriteria(criteria, SubItem.class);
		
		for(int i=0; i < list.size(); i++) {
			Criteria crit = this.dao.newCriteria(FieldSubItem.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("subitem", list.get(i)))
		 		.add(orExp);
			List<FieldSubItem> fields = this.dao.findByCriteria(crit, FieldSubItem.class);
		
			for(int j=0; j < fields.size(); j++) {
				list.get(j).setItemId(list.get(j).getItem().getId());
				subitens.put(fields.get(j).getSubitem().getId(),fields.get(j).getSubitem());
			}
		}
		
		criteria.add(orExp);
		list = this.dao.findByCriteria(criteria, SubItem.class);
		
		for(int i=0; i< list.size(); i++) {
			list.get(i).setItemId(list.get(i).getItem().getId());
			subitens.put(list.get(i).getId(),list.get(i));
		}
			
		return new ArrayList<SubItem>(subitens.values());
	}
}