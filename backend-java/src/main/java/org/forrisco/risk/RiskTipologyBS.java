package org.forrisco.risk;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.HibernateDAO;

@RequestScoped
public class RiskTipologyBS extends HibernateBusiness {

	@Inject protected HibernateDAO dao;
	
	void save(RiskTipology tipology) {
		this.dao.save(tipology);
	}
}
