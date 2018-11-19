package org.forrisco.core.policy;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.core.item.Item;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;

/**
 * @author Matheus Nascimento
 */
@Controller
public class PolicyController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private PolicyBS bs;
	
	/**
	 * Salvar Nova Política
	 * 
	 * @return void
	 */
	@Post("/api/policy/new")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	//
	public void savePolicy2(  Policy policy){
		//@NotNull @Valid
		policy.setCompany(this.domain.getCompany());

		if(policy.getMatrix() ==null) {
			policy.setMatrix("");
		}

		try {
			policy.setId(null);
			this.bs.save(policy);
			this.bs.saveRiskLevel(policy);
			this.success(policy);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	@Post("forrisco/policy/new")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	//
	public void savePolicy3(  Policy policy){
		//@NotNull @Valid
		policy.setCompany(this.domain.getCompany());

		if(policy.getMatrix() ==null) {
			policy.setMatrix("");
		}

		try {
			policy.setId(null);
			this.bs.save(policy);
			this.bs.saveRiskLevel(policy);
			this.success(policy);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	
	/**
	 * Salvar Novo Item
	 * 
	 * @return void
	 */
	@Post("/forrisco/item/new")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Item item){
		LOGGER.warn("1");
		/*this.bs.save(item);
		
		item.getFieldItem().forEach(it->{
			it.setId(null);
			it.setItem(item);
			this.bs.save(it);
		});*/
	}
	
	
	@Post("/forrisco/policy/duplicate")
	public void function1() {
		LOGGER.warn("2");
	}
	
	@Get("/forrisco/archivedpolicy")
	public void function2() {
		LOGGER.warn("3");
	}
	
	@Get("/forrisco/unarchivedpolicy")
	public void function3() {
		LOGGER.warn("4");
	}
	
	@Post("/forrisco/archive")
	public void function5() {
		LOGGER.warn("5");
	}
	
	@Post("/forrisco/unarchive")
	public void function6() {
		LOGGER.warn("6");
	}
	
	@Delete("/forrisco/{id}/")
	public void function7() {
		LOGGER.warn("7");
	}
	
	
	@Post("/forrisco/")
	public void function4() {
		LOGGER.warn("8");
	}
	
	

}