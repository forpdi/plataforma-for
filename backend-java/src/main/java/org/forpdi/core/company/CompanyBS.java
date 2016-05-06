package org.forpdi.core.company;

import javax.enterprise.context.RequestScoped;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * @author Renato R. R. de Oliveira
 */
@RequestScoped
public class CompanyBS extends HibernateBusiness {
	
	public CompanyDomain currentDomain() {
		Criteria criteria = 
			this.dao.newCriteria(CompanyDomain.class)
			.add(Restrictions.eq("host", this.request.getHeader("Host")))
		;
		return (CompanyDomain) criteria.uniqueResult();
	}
	
	public void save(Company company) {
		company.setDeleted(false);
		this.persist(company);
	}

	public PaginatedList<Company> list(int page) {
		PaginatedList<Company> results = new PaginatedList<Company>();
		Criteria criteria =
			this.dao.newCriteria(Company.class)
			.setFirstResult(page*10).setMaxResults(12)
			.add(Restrictions.eq("deleted", false))
			.addOrder(Order.asc("name"))
		;
		Criteria counting =
			this.dao.newCriteria(Company.class)
			.setProjection(Projections.countDistinct("id"))
			.add(Restrictions.eq("deleted", false))
		;
		results.setList(this.dao.findByCriteria(criteria, Company.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}

	public PaginatedList<CompanyDomain> listDomains(int page) {
		PaginatedList<CompanyDomain> results = new PaginatedList<CompanyDomain>();
		Criteria criteria =
			this.dao.newCriteria(CompanyDomain.class)
			.setFirstResult(page*10).setMaxResults(12)
			.createAlias("company", "company", JoinType.INNER_JOIN)
			.addOrder(Order.asc("company.name"))
			.addOrder(Order.asc("host"))
		;
		Criteria counting =
			this.dao.newCriteria(CompanyDomain.class)
			.setProjection(Projections.countDistinct("id"))
		;
		results.setList(this.dao.findByCriteria(criteria, CompanyDomain.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}
}
