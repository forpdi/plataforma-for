package org.forrisco.core.plan;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.abstractions.AbstractController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.system.PDFgenerate;
import org.forrisco.core.bean.ItemSearchBean;
import org.forrisco.core.item.PlanRiskItem;
import org.forrisco.core.item.PlanRiskItemField;
import org.forrisco.core.item.PlanRiskSubItem;
import org.forrisco.core.plan.permissions.ManagePlanRiskPermission;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.item.PlanRiskItemBS;
import org.forrisco.core.process.Process;
import org.forrisco.core.process.ProcessBS;
import org.forrisco.core.unit.Unit;
import org.forrisco.core.unit.UnitBS;

import com.itextpdf.text.DocumentException;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;


/**
 * @author Juliano Afonso
 * 
 */
@Controller
public class PlanRiskController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private PlanRiskBS planRiskBS;
	@Inject private PlanRiskItemBS planRiskItemBS;
	@Inject private UnitBS unitBS;
	@Inject private ProcessBS processBS;
	@Inject private PDFgenerate pdf;
	
	protected static final String PATH = BASEPATH +"/planrisk";
	
	/**
	 * Salvar Novo Plano
	 * 
	 * @return void
	 */
	
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanRiskPermission.class })
	public void savePlan(@NotNull @Valid  PlanRisk planRisk) {
		try {
			
			Policy policy = this.planRiskBS.exists(planRisk.getPolicy().getId(), Policy.class);
			
			if (policy == null || policy.isDeleted()) {
				this.fail("O plano de risco solicitada não foi encontrado.");
				return;
			}
			if ((planRisk.getValidityBegin() == null && planRisk.getValidityEnd() != null) ||
					(planRisk.getValidityEnd() == null && planRisk.getValidityBegin() != null)) {
				this.fail("Não é permitido preencher somente uma das datas do prazo de vigência");
				return;
			}
			if (planRisk.getValidityBegin() != null && planRisk.getValidityEnd() != null
					&& planRisk.getValidityEnd().before(planRisk.getValidityBegin())) {
				this.fail("A data de início do prazo de vigência não deve ser superior à data de término");
				return;
			}
			
			planRisk.setId(null);
			planRisk.setPolicy(policy);
			//planRisk.setName(planRisk.getName());
			//planRisk.setDescription(planRisk.getDescription());
			
			planRiskBS.save(planRisk);
			
			this.success(planRisk);
			
		} catch (Throwable e) {
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Retorna o lista de todos os planos não arquivados.
	 * 
	 * @param page
	 */
	@Get( PATH + "/unarchivedplanrisk")
	@NoCache
	@Permissioned
	public void listPlanRiskUnarchived(Integer page) {
		if (page == null) page = 0;
		try {
			if (this.domain != null) {
			PaginatedList<PlanRisk> planRisks = this.planRiskBS.listPlanRisk(this.domain.getCompany(),page);
				this.success(planRisks);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna o plano de risco.
	 * 
	 * @param id
	 * Id do plano de risco.
	 * 
	 * @return PlanRisk Retorna o plano de risco de acordo com o id passado.
	 */
	@Get( PATH + "/{id}")
	@NoCache
	@Permissioned
	public void retrievePlan(Long id) {
		try {
			PlanRisk planRisk = this.planRiskBS.exists(id, PlanRisk.class);
			if (planRisk == null) {
				this.fail("O plano de risco solicitada não foi encontrado.");
			} else {
				this.success(planRisk);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Retorna o plano atualizado.
	 * 
	 * @param PlanRisk planRisk plano de risco a ser atualizado.
	 * 
	 * @return PlanRisk Retorno o plano de risco atualizado.
	 * */
	@Post(PATH + "/update")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanRiskPermission.class })
	public void editPlanRisk(@NotNull @Valid PlanRisk planRisk) {
		try {
			PlanRisk existent = this.planRiskBS.exists(planRisk.getId(), PlanRisk.class);

			if (GeneralUtils.isInvalid(existent)) {
				this.fail("Plano de risco não encontrado");
				return;
			}

			if (existent.getPolicy() == null) {
				this.fail("Plano sem política associada");
			}

			if ((planRisk.getValidityBegin() == null && planRisk.getValidityEnd() != null) ||
					(planRisk.getValidityEnd() == null && planRisk.getValidityBegin() != null)) {
				this.fail("Não é permitido preencher somente uma das datas do prazo de vigência");
				return;
			}
			if (planRisk.getValidityBegin() != null && planRisk.getValidityEnd() != null
					&& planRisk.getValidityEnd().before(planRisk.getValidityBegin())) {
				this.fail("A data de início do prazo de vigência não deve ser superior à data de término");
				return;
			}

			existent.setPolicy(planRisk.getPolicy());
			existent.setName(planRisk.getName());
			existent.setDescription(planRisk.getDescription());
			existent.setValidityBegin(planRisk.getValidityBegin());
			existent.setValidityEnd(planRisk.getValidityEnd());

			this.planRiskBS.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	@Delete(PATH + "/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanRiskPermission.class })
	public void deletePlanRisk(Long id) {
		try {
			PlanRisk planRisk = this.planRiskBS.exists(id, PlanRisk.class);
			
			if (GeneralUtils.isInvalid(planRisk)) {
				this.result.notFound();
				this.fail("Plano de risco inválido.");
			}
			
			
			//verificar unidades
			PaginatedList<Unit> units= this.unitBS.listUnitsbyPlanRisk(planRisk);
			
			for(Unit unit:units.getList()) {
				if(!this.unitBS.deletableUnit(unit)) {	
					this.fail("O plano possui unidades que não podem ser deletadas." );
					return;
				}
			}
					
			//deletar unidades
			for(Unit unit:units.getList()) {
				//deletar processos desta unidade
				PaginatedList<Process> processes = this.processBS.listProcessByUnit(unit);
				for(Process process :processes.getList()) {
					this.processBS.deleteProcess(process);
				}
				this.unitBS.delete(unit);
			}
			
			
			
			//deletar itens
			PaginatedList<PlanRiskItem> planRiskItem = this.planRiskItemBS.listItensByPlanRisk(planRisk);
			
			for(PlanRiskItem item : planRiskItem.getList()) {
				
				PaginatedList<PlanRiskItemField> fields = this.planRiskItemBS.listFieldsByPlanRiskItem(item);
				
				for(PlanRiskItemField field : fields.getList()) {
					this.planRiskItemBS.delete(field);
				}
				
				this.planRiskItemBS.deleteSubItens(item);
				this.planRiskItemBS.delete(item);
			}
			
			//deletar plano
			this.planRiskBS.delete(planRisk);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	
	/**
	 * Cria arquivo pdf  para exportar relatório  
	 * 
	 * 
	 * @param title
	 * @param author
	 * @param pre
	 * @param item
	 * @param subitem
	 * @throws DocumentException 
	 * @throws IOException 
	 * 
	 */
	@Get(PATH + "/exportReport")
	@NoCache
	@Permissioned
	public void exportreport(String title, String author, boolean pre,Long planId, String itens, String subitens) throws IOException, DocumentException{
		try {
		
			File pdf = this.pdf.exportPlanRiskReport(title, author,planId, itens,subitens);

			OutputStream out;
			FileInputStream fis= new FileInputStream(pdf);
			this.response.reset();
			this.response.setHeader("Content-Type", "application/pdf");
			this.response.setHeader("Content-Disposition", "inline; filename=\"" + title + ".pdf\"");
			out = this.response.getOutputStream();
			
			IOUtils.copy(fis, out);
			out.close();
			fis.close();
			pdf.delete();
			this.result.nothing();
			
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail(ex.getMessage());
		}
	}
	
	/**
	 * Cria arquivo pdf  para exportar relatório  
	 * 
	 * 
	 * @param title
	 * @param author
	 * @param pre
	 * @param item
	 * @param subitem
	 * @throws DocumentException 
	 * @throws IOException 
	 * 
	 */
	@Get(PATH + "/exportBoardReport")
	@NoCache
	@Permissioned
	public void exportBoardReport(String title, String author, boolean pre,Long planId, String selecao) throws IOException, DocumentException{
		try {
		
			File pdf = this.pdf.exportBoardReport(title, author,planId, selecao);

			OutputStream out;
			FileInputStream fis= new FileInputStream(pdf);
			this.response.reset();
			this.response.setHeader("Content-Type", "application/pdf");
			this.response.setHeader("Content-Disposition", "inline; filename=\"" + title + ".pdf\"");
			out = this.response.getOutputStream();
			
			IOUtils.copy(fis, out);
			out.close();
			fis.close();
			pdf.delete();
			this.result.nothing();
			
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail(ex.getMessage());
		}
	}
	
	
	
	@Get(PATH + "/search")
	@NoCache
	@Permissioned
	public void listItensTerms(Long planRiskId, Integer page, String terms, int ordResult, Long limit) {
		if (page == null)
			page = 0;
		
		try {
			PlanRisk planRisk = this.planRiskBS.exists(planRiskId, PlanRisk.class);
			
			boolean generalInformationSearched = this.planRiskBS.termsSearchedInGeneralInformation(planRisk, terms, null);
			List<PlanRiskItem> itens = this.planRiskBS.listItemTerms(planRisk, terms, null, ordResult);
			List<PlanRiskSubItem> subitens = this.planRiskBS.listSubitemTerms(planRisk, terms, null, ordResult);

			PaginatedList<ItemSearchBean> result = this.planRiskBS.termResult(generalInformationSearched, planRisk, itens, subitens, page, limit);
			
			
			this.success(result);
 		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param planRiskId
	 * @param page
	 * @param terms
	 * @param itensSelect
	 * @param subitensSelect
	 * @param ordResult
	 * @param limit
	 */
	@Get(PATH + "/searchByKey")
	@NoCache
	@Permissioned
	public void listItensTerms(Long planRiskId, Integer page, String terms, Long itensSelect[], Long subitensSelect[], int ordResult, Long limit) {
		if (page == null)
			page = 0;
		
		try {
			PlanRisk planRisk = this.planRiskBS.exists(planRiskId, PlanRisk.class);
			
			boolean includeGeneralInformation = this.planRiskBS.termsSearchedInGeneralInformation(planRisk, terms, itensSelect);
			List<PlanRiskItem> itens = this.planRiskBS.listItemTerms(planRisk, terms, itensSelect, ordResult);
			List<PlanRiskSubItem> subitens = this.planRiskBS.listSubitemTerms(planRisk, terms, subitensSelect, ordResult);

			PaginatedList<ItemSearchBean> result = this.planRiskBS.termResult(includeGeneralInformation, planRisk, itens, subitens, page, limit);
			
			this.success(result);

 		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
}
