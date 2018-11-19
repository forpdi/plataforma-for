package org.forrisco.risk;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;

/**
 * @author Matheus Nascimento
 */
@Controller
public class RiskController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private RiskBS bs;

	/**
	 * Salvar Novo Plan
	 * 
	 * @return void
	 */
	@Post("/forrisco/risk/new")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Risk risk){
		

		
		try {
			risk.setId(null);
			this.bs.save(risk);
			this.success(risk);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
}