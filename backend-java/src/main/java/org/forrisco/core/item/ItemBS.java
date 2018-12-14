package org.forrisco.core.item;


import javax.enterprise.context.RequestScoped;

import org.forrisco.core.policy.Policy;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class ItemBS extends HibernateBusiness {
	
	
	/**
	 * Salva no banco de dados um novo item
	 * 
	 * @param Item,
	 *            instância do item a ser salvo
	 */
	public void save(Item item) {
		item.setId(null);
		item.setDeleted(false);
		this.persist(item);
	}
	
	/**
	 * Salva no banco de dados um novo subitem
	 * 
	 * @param Item,
	 *            instância do subitem a ser salvo
	 */
	public void save(SubItem subitem) {
		subitem.setId(null);
		subitem.setDeleted(false);
		this.persist(subitem);
	}
	
	/**
	 * Salva no banco de dados um novo campo
	 * 
	 * @param FieldItem,
	 *            instância do fieldItem a ser salvo
	 */
	public void save(FieldItem fieldItem) {
		fieldItem.setId(null);
		fieldItem.setDeleted(false);
		this.persist(fieldItem);
	}
	
	/**
	 * Salva no banco de dados um novo subcampo
	 * 
	 * @param FieldSubItem,
	 *            instância do fieldSubItem a ser salvo
	 */
	public void save(FieldSubItem subfield) {
		subfield.setId(null);
		subfield.setDeleted(false);
		this.persist(subfield);
	}
	
	
	
	/**
	 *Deleta do banco de dados um item
	 * 
	 * @param Item,
	 *            instância do item a ser deletado
	 */
	public void delete(Item item) {
		item.setDeleted(true);
		this.persist(item);
	}
	
	/**
	 *Deleta do banco de dados um subitem
	 * 
	 * @param SubItem,
	 *            instância do sbitem a ser deletado
	 */
	public void delete(SubItem subitem) {
		subitem.setDeleted(true);
		this.persist(subitem);
	}
	
	
	/**
	 *Deleta do banco de dados um campo
	 * 
	 * @param FieldItem,
	 *            instância do fielditem a ser deletado
	 */
	public void delete(FieldItem fieldItem) {
		fieldItem.setDeleted(true);
		this.persist(fieldItem);
	}

	/**
	 *Deleta do banco de dados um subcampo
	 * 
	 * @param FieldSubItem,
	 *            instância do fieldsubitem a ser deletado
	 */
	public void delete(FieldSubItem subfield) {
		subfield.setDeleted(true);
		this.persist(subfield);
	}

	
	/**
	 * Lista os itens de uma política.
	 * @param Policy
	 * 			política da qual se deseja obter os itens.
	 * 
	 * @return PaginatedList<Item>
	 * 			Lista de itens.
	 */
	public PaginatedList<Item> listItensByPolicy(Policy policy) {
	
		PaginatedList<Item> results = new PaginatedList<Item>();
		
		Criteria criteria = this.dao.newCriteria(Item.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("policy", policy)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(Item.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, Item.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	/**
	 * Lista as 'inormações gerais' de uma política.
	 * @param Policy
	 * 			política da qual se deseja obter o item.
	 * 
	 * @return Item
	 * 	
	 */
	public Item listInfoByPolicy(Policy policy) {
		Item result = new Item();
		
		Criteria criteria = this.dao.newCriteria(Item.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("policy", policy)).addOrder(Order.asc("name"))
				.add(Restrictions.eq("name", "Informações gerais"));
		
		/*Criteria count = this.dao.newCriteria(Item.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("policy", policy)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, Item.class));
		results.setTotal((Long) count.uniqueResult());*/
		
		result=(Item) criteria.uniqueResult();
		return result;
	}
	
	
	/**
	 * Lista campos de um item.
	 * 
	 * @param Item
	 * 			Item para recuperar os campos.
	 * 
	 * @return PaginatedList<FieldItem>
	 * 	
	 */
	public  PaginatedList<FieldItem> listFieldsByItem(Item item) {
		
		PaginatedList<FieldItem> results = new PaginatedList<FieldItem>();
		
		Criteria criteria = this.dao.newCriteria(FieldItem.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("item", item)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(FieldItem.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("item", item)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, FieldItem.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	
	/**
	 * Lista os subitens de um item.
	 * @param Item
	 * 			item do qual se deseja obter os subitens.
	 * 
	 * @return PaginatedList<SubItem>
	 * 			Lista de subitens.
	 */
	public PaginatedList<SubItem> listSubItensByItem(Item item) {
		PaginatedList<SubItem> results = new PaginatedList<SubItem>();
		
		Criteria criteria = this.dao.newCriteria(SubItem.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("item", item)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(SubItem.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("item", item)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, SubItem.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

	/**
	 * Lista campos de um subitem.
	 * 
	 * @param SubItem
	 * 			SubItem para recuperar os campos.
	 * 
	 * @return PaginatedList<FieldSubItem>
	 * 	
	 */
	public PaginatedList<FieldSubItem> listFieldsBySubItem(SubItem subitem) {
		
		PaginatedList<FieldSubItem> results = new PaginatedList<FieldSubItem>();
		
		Criteria criteria = this.dao.newCriteria(FieldSubItem.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("subitem", subitem)).addOrder(Order.asc("name"));
		
		Criteria count = this.dao.newCriteria(FieldSubItem.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("subitem", subitem)).setProjection(Projections.countDistinct("id"));
		
		results.setList(this.dao.findByCriteria(criteria, FieldSubItem.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}

}
