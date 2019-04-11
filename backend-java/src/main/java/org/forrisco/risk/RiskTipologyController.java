package org.forrisco.risk;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.risk.permissions.ManageRiskPermission;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.NoCache;

/**
 * @author Felippe Kipman
 */
@Controller
public class RiskTipologyController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private RiskTipologyBS bs;
	
	protected static final String PATH =  BASEPATH +"/tipology";

	@Post(PATH + "/new")
	@Consumes
	@NoCache
	public void save(@NotNull @Valid RiskTipology tipology) {
		try {
			Company company = this.domain.getCompany();
			
			if (company == null) {
				this.fail("Impossível criar tipologia sem uma compania!");
			}
			
			tipology.setCompany(company);
			
			bs.save(tipology);
			this.success();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());			
		}
	}

	@Get(PATH)
	@NoCache
	public void getAll() {
		try {
			Company company = this.domain.getCompany();
			
			if (company == null) {
				this.fail("Impossível buscar tipologias sem uma compania!");
			}
			
			this.success(bs.getAll(company));
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
}
