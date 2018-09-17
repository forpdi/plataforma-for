package org.forpdi.core.company;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

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
	 * Recupera uma instância do objeto CompanyDomain utilizando um host
	 * específico
	 * 
	 * @param host
	 *             à ser utilizado na query
	 * @return Domínio que utiliza o host epecificado
	 */
	
	public CompanyDomain retrieveCompanyByDomain (Company company) {
		Criteria criteria  = this.dao.newCriteria(CompanyDomain.class).add(Restrictions.eq("company",company));
			
		return (CompanyDomain) criteria.uniqueResult();
	}
	
	/**
	 * Recupera uma instância do objeto CompanyDomain utilizando um id
	 * 
	 * @param id
	 *             à ser utilizado na query
	 * @return Domínio que utiliza o host epecificado
	 */
	
	public Company retrieveCompanyById (Long id) {
		Criteria criteria  = this.dao.newCriteria(Company.class).add(Restrictions.eq("id",id));

		return (Company) criteria.uniqueResult();
	}

	public List<CompanyMessage> retrieveMessages(Company company) {
		Criteria criteria = this.dao.newCriteria(CompanyMessage.class);
		criteria.add(Restrictions.eq("company", company));
		return this.dao.findByCriteria(criteria, CompanyMessage.class);
	}
	
	public Map<String, String> retrieveMessagesOverlay(Company company) {
		List<CompanyMessage> messages = this.retrieveMessages(company);
		Map<String, String> overlay = new HashMap<String, String>();
		if (!GeneralUtils.isEmpty(messages)) {
			for (CompanyMessage message : messages) {
				overlay.put(message.getMessageKey(), message.getMessageValue());
			}
		}
		return overlay;
	}

	public void updateMessageOverlay(Company company, String key, String value) {
		Criteria criteria = this.dao.newCriteria(CompanyMessage.class);
		criteria.add(Restrictions.eq("company", company));
		criteria.add(Restrictions.eq("messageKey", key));
		CompanyMessage message = (CompanyMessage) criteria.uniqueResult();
		if (message == null) {
			message = new CompanyMessage();
			message.setCompany(company);
			message.setMessageKey(key);
		}
		message.setLastUpdated(new Date());
		message.setMessageValue(value);
		this.dao.persist(message);
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
		Criteria criteria = this.dao.newCriteria(Company.class).add(Restrictions.eq("deleted", false)).addOrder(Order.asc("name"));
		if (page > 0)
			criteria.setFirstResult((page-1) * PAGESIZE).setMaxResults(PAGESIZE);
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
		Criteria criteria = this.dao.newCriteria(CompanyDomain.class)
				.createAlias("company", "company", JoinType.INNER_JOIN)
				.addOrder(Order.asc("company.name")).addOrder(Order.asc("host"));
		if (page > 0)
			criteria.setFirstResult((page-1) * PAGESIZE).setMaxResults(PAGESIZE);
		Criteria counting = this.dao.newCriteria(CompanyDomain.class).setProjection(Projections.countDistinct("id"));
		results.setList(this.dao.findByCriteria(criteria, CompanyDomain.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}
}