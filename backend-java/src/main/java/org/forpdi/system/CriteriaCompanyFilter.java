package org.forpdi.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;

@RequestScoped
public class CriteriaCompanyFilter extends HibernateBusiness{
	
	@Inject
	@Current
	private CompanyDomain domain;
	
	public <E extends Serializable> List<E> filterAndList(Criteria criteria, Class<E> clazz){
		if(domain == null)
			return new ArrayList<E>();
		
		criteria.add(Restrictions.eq("company", domain.getCompany()));
		return this.dao.findByCriteria(criteria, clazz);
	}
	
	public <E extends Serializable> List<E> filterAndList(Criteria criteria, Class<E> clazz, String alias){
		if(domain == null)
			return new ArrayList<E>();
		
		criteria.add(Restrictions.eq(alias, domain.getCompany()));
		return this.dao.findByCriteria(criteria, clazz);
	}
	
	public <E extends Serializable> E filterAndFind(Criteria criteria){
		if(domain == null)
			return null;
		
		criteria.add(Restrictions.eq("company", domain.getCompany()));
		return (E) criteria.uniqueResult();
	}
	
	public <E extends Serializable> E filterAndFind(Criteria criteria, String alias){
		if(domain == null)
			return null;
		criteria.add(Restrictions.eq(alias, domain.getCompany()));
		return (E) criteria.uniqueResult();
	}

}
