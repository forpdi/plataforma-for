package org.forrisco.core.plan;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.abstractions.AbstractController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.system.PDFgenerate;
import org.forrisco.core.item.PlanRiskItem;
import org.forrisco.core.item.PlanRiskItemField;
import org.forrisco.core.item.PlanRiskItemBS;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.policy.PolicyBS;
import org.forrisco.core.policy.permissions.ManagePlanRiskPermission;
import org.forrisco.core.policy.permissions.ManagePolicyPermission;

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
	@Inject private Policy policy;
	@Inject private PlanRiskBS planRiskBS;
	@Inject private PlanRisk planRisk;
	@Inject private PlanRiskItemBS planRiskItemBS;
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
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	
	public void savePlan(@NotNull @Valid  PlanRisk planRisk) {
		try {
			planRisk.setId(null);
			planRisk.setName(planRisk.getName());
			planRisk.setDescription(planRisk.getDescription());
			
			planRiskBS.save(planRisk);
			
			this.success(planRisk.getId());
			
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
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
	@Permissioned
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
			
			existent.setName(planRisk.getName());
			existent.setDescription(planRisk.getDescription());
			
			this.planRiskBS.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	@Delete(PATH + "/{id}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanRiskPermission.class })
	public void deletePlanRisk(Long id) {
		try {
			PlanRisk planRisk = this.planRiskBS.exists(id, PlanRisk.class);
			
			if (GeneralUtils.isInvalid(planRisk)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<PlanRiskItem> planRiskItem = this.planRiskItemBS.listItensByPlanRisk(planRisk);
			
			for(PlanRiskItem item : planRiskItem.getList()) {
				
				PaginatedList<PlanRiskItemField> fields = this.planRiskItemBS.listFieldsByPlanRiskItem(item);
				
				for(PlanRiskItemField field : fields.getList()) {
					this.planRiskItemBS.delete(field);
				}
				
				this.planRiskItemBS.deleteSubItens(item);
				this.planRiskItemBS.delete(item);
			}
			
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
	//@Permissioned
	public void exportreport(Long planId, String title, String author, boolean pre, String selecao) throws IOException, DocumentException{
		//try {
		
			File pdf = this.pdf.exportPolicyReport(title, author, selecao,planId);

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
			
	/*	} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail(ex.getMessage());
		}*/
	}

	
}
