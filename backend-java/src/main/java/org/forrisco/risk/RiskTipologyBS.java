package org.forrisco.risk;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.core.company.Company;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@RequestScoped
public class RiskTipologyBS extends HibernateBusiness {

	@Inject protected HibernateDAO dao;
	
	void save(RiskTipology tipology) {
		this.dao.save(tipology);
	}
	
	PaginatedList<RiskTipology> getAll(Company company) {
		Criteria criteria = dao.newCriteria(RiskTipology.class)
				.add(Restrictions.eq("company", company));
		
		List<RiskTipology> tipologies = this.dao.findByCriteria(criteria, RiskTipology.class);
		PaginatedList<RiskTipology> paginatedTipologies = new PaginatedList(tipologies, (long) tipologies.size()); 

		return paginatedTipologies;
	}
}
