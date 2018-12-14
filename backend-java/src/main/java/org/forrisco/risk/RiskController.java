package org.forrisco.risk;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.core.policy.Policy;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

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
	
	
	/**
	 * Retorna graus de risco.
	 * 
	 * @param id
	 *            Id da política.
	 * @return RiskLevel Retorna os graus da política (id) passada.
	 * 
	 */

	@Get("/api/policy/risklevel/{id}")
	@NoCache
	@Permissioned
	public void retrievePolicy(Long id) {
		try {
			Policy policy = this.bs.exists(id, Policy.class);
			if (policy == null) {
				this.fail("A política solicitada não foi encontrado.");
			} else {
				PaginatedList<RiskLevel> risklevels = this.bs.listRiskLevelbyPolicy(policy);
				this.success(risklevels);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	
	
}