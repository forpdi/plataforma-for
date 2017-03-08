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

	private static final int PAGESIZE = 12;

	/**
	 * Recupera a instância do objeto CompanyDomain que está ativo no momento
	 * 
	 * @return Domínio ativo no momento
	 */
	public CompanyDomain currentDomain() {
		Criteria criteria = this.dao.newCriteria(CompanyDomain.class)
				.add(Restrictions.eq("host", this.request.getHeader("Host")));
		return (CompanyDomain) criteria.uniqueResult();
	}

	/**
	 * Recupera uma instância do objeto CompanyDomain utilizando um host
	 * específico
	 * 
	 * @param host
	 *             à ser utilizado na query
	 * @return Domínio que utiliza o host epecificado
	 */
	public CompanyDomain retrieveByHost(String host) {
		Criteria criteria = this.dao.newCriteria(CompanyDomain.class).add(Restrictions.eq("host", host));
		return (CompanyDomain) criteria.uniqueResult();
	}

	/**
	 * Salva no banco de dados uma nova companhia
	 * 
	 * @param company,
	 *            instância da companhia a ser salva
	 * @return void
	 */
	public void save(Company company) {
		company.setDeleted(false);
		this.persist(company);
	}

	/**
	 * Lista as companhias limitados a uma dada página
	 * 
	 * @param page,
	 *            número da página a ser listada
	 * @return PaginatedList<Company>, lista de companhias
	 */
	public PaginatedList<Company> list(int page) {
		PaginatedList<Company> results = new PaginatedList<Company>();
		Criteria criteria = this.dao.newCriteria(Company.class).setFirstResult(page * PAGESIZE).setMaxResults(PAGESIZE)
				.add(Restrictions.eq("deleted", false)).addOrder(Order.asc("name"));
		Criteria counting = this.dao.newCriteria(Company.class).setProjection(Projections.countDistinct("id"))
				.add(Restrictions.eq("deleted", false));
		results.setList(this.dao.findByCriteria(criteria, Company.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}

	/**
	 * Lista os domínios limitados a uma dada página
	 * 
	 * @param page,
	 *            número da página a ser listada
	 * @return PaginatedList<CompanyDomain>, lista de domínios
	 */
	public PaginatedList<CompanyDomain> listDomains(int page) {
		PaginatedList<CompanyDomain> results = new PaginatedList<CompanyDomain>();
		Criteria criteria = this.dao.newCriteria(CompanyDomain.class).setFirstResult(page * PAGESIZE)
				.setMaxResults(PAGESIZE).createAlias("company", "company", JoinType.INNER_JOIN)
				.addOrder(Order.asc("company.name")).addOrder(Order.asc("host"));
		Criteria counting = this.dao.newCriteria(CompanyDomain.class).setProjection(Projections.countDistinct("id"));
		results.setList(this.dao.findByCriteria(criteria, CompanyDomain.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}
}
