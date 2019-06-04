package org.forpdi.core.company;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.properties.CoreMessages;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.core.user.authz.permission.EditMessagesPermission;
import org.hibernate.validator.constraints.NotEmpty;

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
	@Inject
	private CompanyBS bs;

	/**
	 * Configura os temas no sistema.
	 * 
	 * @return void
	 */
	@Get("/api/company/themes")
	@Cached(Cached.ONE_WEEK)
	public void listThemes() {
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setContentType("application/json");
			OutputStreamWriter writer = new OutputStreamWriter(this.response.getOutputStream(),
					Charset.forName("UTF-8"));
			writer.write(CompanyThemeFactory.getInstance().toJSON());
			writer.flush();
			writer.close();
			this.result.nothing();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Salva uma nova companhia no banco de dados.
	 * 
	 * @param company,
	 *            nova companhia a ser persistida no banco de dados.
	 * @return company, a nova companhia que acabou de ser persistida no banco
	 *         de dados.
	 */
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
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualiza uma companhia no banco de dados.
	 * 
	 * @param company, companhia a ser atualizada no banco de dados.
	 * @return existent, companhia que acabou de ser atualizada no banco de
	 *         dados.
	 */
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
			existent.setShowDashboard(company.isShowDashboard());
			existent.setShowMaturity(company.isShowMaturity());
			existent.setShowBudgetElement(company.isShowBudgetElement());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Retorna uma companhia de acordo com seu id.
	 * 
	 * @param id da companhia desejada.
	 * @return company, companhia desejada que possui o dado id.
	 */
	@Get("/api/company/{id}")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void retrieveCompany(Long id) {
		try {
			Company company = this.bs.exists(id, Company.class);
			if (company == null) {
				this.fail("A empresa solicitada não foi encontrada.");
			} else {
				this.success(company);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Lista as companhias limitados a uma dada página.
	 * @param page, página desejada da listagem de companhias.
	 * @return companies, lista de companhias da dada página.
	 */
	@Get("/api/company")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void listCompanies(Integer page) {
		if (page == null)
			page = 0;
		try {
			PaginatedList<Company> companies = this.bs.list(page);
			this.success(companies);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	/**
	 * Deleta uma companhia específica, conforme um dado id.
	 * @param id da companhia a ser deletada.
	 * @return void
	 */
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
			
			List<CompanyDomain> companyDomain = this.bs.retrieveCompanyByDomain(existent);
			
			if (companyDomain.size() == 0) {
				existent.setDeleted(true);
				this.bs.persist(existent);
				this.success(existent);
			} else {
				this.fail("Esta instituição não pode ser deletada, pois existe um domínio associado á ela.");
				return;
			}
			
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar um novo domínio no banco de dados.
	 * @param domain, novo domínio a ser salvo.
	 * @return domain, novo domínio que acabou de ser salvo.
	 */
	@Post("/api/companydomain")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void saveDomain(@NotNull @Valid CompanyDomain domain) {
		try {
			domain.setId(null);
			if (domain.getCompany() == null) {
				this.fail("Companhia não informada");
				return;
			}
			Company company = this.bs.retrieveCompanyById(domain.getCompany().getId());
			if (company == null) {
				this.fail("Companhia não encontrada");
				return;				
			}
			CompanyDomain existent = this.bs.retrieveByHost(domain.getHost());
			if (existent == null) {
				this.bs.persist(domain);
				this.success(domain);
			} else {
				this.fail("Já existe um domínio com este HOST.");
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Domínio já existente");
		}
	}

	/**
	 * Atualizar um domínio no banco de dados.
	 * @param domain, domínio a atualizado.
	 * @return existent, domínio atualizado.
	 */
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
			
			CompanyDomain cd = this.bs.retrieveByHost(domain.getHost());
			if (cd !=null && !cd.getHost().equals(existent.getHost())) {
				this.fail("Já existe um domínio com este host registrado");
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
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Retorna um domínio do banco de dados, conforme um dado id.
	 * @param id, referente ao domínio desejado.
	 * @return domain, domínio desejado.
	 */
	@Get("/api/companydomain/{id}")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void retrieveCompanyDomain(Long id) {
		try {
			CompanyDomain domain = this.bs.exists(id, CompanyDomain.class);
			if (domain == null) {
				this.fail("O domínio solicitado não foi encontrado.");
			} else {
				this.success(domain);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Lista os domínios, limitados a uma dada página.
	 * @param page, página desejada dos domínios.
	 * @return domains, lista dos domínios da dada página.
	 */
	@Get("/api/companydomain")
	@NoCache
	@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void listCompanyDomains(Integer page) {
		if (page == null)
			page = 0;
		try {
			PaginatedList<CompanyDomain> domains = this.bs.listDomains(page);
			this.success(domains);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Excluir um domínio, especificado pelo id.
	 * @param id, referente ao domínio a ser deletado.
	 * @return void.
	 */
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
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	@Post("/api/company/messages")
	@NoCache
	@Consumes
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { EditMessagesPermission.class,
			org.forrisco.core.authz.permissions.EditMessagesPermission.class })
	public void updateMessageOverlay(@NotEmpty String key, @NotEmpty String value) {
		try {
			CompanyDomain domain = this.bs.currentDomain();
			if (domain == null) {
				this.result.notFound();
			} else {
				this.bs.updateMessageOverlay(domain.getCompany(), key, value);
				this.success(true);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	@Get("/api/company/messages")
	public void getMessages() {
		CompanyDomain domain = this.bs.currentDomain();
		CoreMessages msg = new CoreMessages(CoreMessages.DEFAULT_LOCALE);
		if (domain != null) {
			Map<String, String> messagesOverlays = this.bs.retrieveMessagesOverlay(domain.getCompany());
			msg.setOverlay(messagesOverlays);
		}
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.addHeader("Content-Type", "application/json"); 
			this.response.getWriter().print(msg.getJSONMessages());
		} catch (IOException ex) {
			LOGGER.error("Unexpected runtime error", ex);
		}
		this.result.nothing();
	}

}
