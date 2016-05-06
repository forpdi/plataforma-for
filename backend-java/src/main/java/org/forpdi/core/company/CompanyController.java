package org.forpdi.core.company;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.boilerplate.Cached;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class CompanyController extends AbstractController {
	@Inject private CompanyBS bs;

	@Get("/api/company/themes")
	@Cached(Cached.ONE_WEEK)
	public void listThemes(){
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setContentType("application/json");
			this.response.getOutputStream().print(CompanyThemeFactory.getInstance().toJSON());
			this.result.nothing();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}

	@Post("/api/company")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void saveCompany(@NotNull @Valid Company company) {
		try {
			company.setId(null);
			this.bs.save(company);
			this.success(company);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Put("/api/company")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void updateCompany(@NotNull @Valid Company company) {
		try {
			Company existent = this.bs.exists(company.getId(), Company.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDescription(company.getDescription());
			existent.setLogo(company.getLogo());
			existent.setName(company.getName());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Get("/api/company/{id}")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void retrieveCompany(Long id){
		try {
			Company company = this.bs.exists(id, Company.class);
			if (company == null) {
				this.fail("A empresa solicitada não foi encontrada.");
			} else {
				this.success(company);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}
	
	@Get("/api/company")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void listCompanies(Integer page){
		if (page == null)
			page = 0;
		try {
			PaginatedList<Company> companies = this.bs.list(page);
			this.success(companies);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}

	@Delete("/api/company/{id}")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void delete(@NotNull Long id) {
		try {
			Company existent = this.bs.exists(id, Company.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDeleted(true);
			this.bs.persist(existent);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Post("/api/companydomain")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void saveDomain(@NotNull @Valid CompanyDomain domain) {
		try {
			domain.setId(null);
			this.bs.persist(domain);
			this.success(domain);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Put("/api/companydomain")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void updateDomain(@NotNull @Valid CompanyDomain domain) {
		try {
			CompanyDomain existent = this.bs.exists(domain.getId(), CompanyDomain.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setHost(domain.getHost());
			existent.setBaseUrl(domain.getBaseUrl());
			existent.setTheme(domain.getTheme());
			if (!GeneralUtils.isInvalid(domain.getCompany())
				&& !existent.getCompany().getId().equals(domain.getCompany().getId())) {
					existent.setCompany(domain.getCompany());
			}
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Get("/api/companydomain/{id}")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void retrieveCompanyDomain(Long id){
		try {
			CompanyDomain domain = this.bs.exists(id, CompanyDomain.class);
			if (domain == null) {
				this.fail("O domínio solicitado não foi encontrado.");
			} else {
				this.success(domain);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}
	
	@Get("/api/companydomain")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void listCompanyDomains(Integer page){
		if (page == null)
			page = 0;
		try {
			PaginatedList<CompanyDomain> domains = this.bs.listDomains(page);
			this.success(domains);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}

	@Delete("/api/companydomain/{id}")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void deleteDomain(@NotNull Long id) {
		try {
			CompanyDomain existent = this.bs.exists(id, CompanyDomain.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			this.bs.remove(existent);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
}
