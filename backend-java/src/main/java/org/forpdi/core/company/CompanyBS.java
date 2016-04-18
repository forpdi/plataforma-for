package org.forpdi.core.company;

import javax.enterprise.context.RequestScoped;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Renato R. R. de Oliveira
 */
@RequestScoped
public class CompanyBS extends HibernateBusiness {
	
	public void save(Company company) {
		company.setDeleted(false);
		this.persist(company);
	}
	
	public PaginatedList<Company> list(int page) {
		PaginatedList<Company> results = new PaginatedList<Company>();
		Criteria criteria =
			this.dao.newCriteria(Company.class)
			.setFirstResult(page*10).setMaxResults(10)
			.addOrder(Order.asc("name"))
		;
		Criteria counting =
			this.dao.newCriteria(Company.class)
			.setProjection(Projections.countDistinct("id"))
		;
		results.setList(this.dao.findByCriteria(criteria, Company.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}
}
