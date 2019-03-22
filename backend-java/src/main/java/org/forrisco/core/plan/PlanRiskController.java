package org.forrisco.core.plan;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.abstractions.AbstractController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.system.PDFgenerate;
import org.forrisco.core.item.PlanRiskItem;
import org.forrisco.core.item.PlanRiskItemField;
import org.forrisco.core.item.PlanRiskSubItem;
import org.forrisco.core.item.PlanRiskItemBS;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.process.Process;
import org.forrisco.core.process.ProcessBS;
import org.forrisco.core.unit.Unit;
import org.forrisco.core.unit.UnitBS;
import org.forrisco.core.unit.UnitController;

import com.google.gson.GsonBuilder;
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
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	
	public void savePlan(@NotNull @Valid  PlanRisk planRisk) {
		try {
			
			Policy policy = this.planRiskBS.exists(planRisk.getPolicy().getId(), Policy.class);
			
			if (policy == null || policy.isDeleted()) {
				this.fail("O plano de risco solicitada não foi encontrado.");
				return;
			}
			
			
			
			planRisk.setId(null);
			planRisk.setPolicy(policy);
			//planRisk.setName(planRisk.getName());
			//planRisk.setDescription(planRisk.getDescription());
			
			planRiskBS.save(planRisk);
			
			this.success(planRisk);
			
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
			
			existent.setPolicy(planRisk.getPolicy());
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
			EmailSenderTask.LOG.info((new GsonBuilder().setPrettyPrinting().create().toJson(id)));
			PlanRisk planRisk = this.planRiskBS.exists(id, PlanRisk.class);
			
			if (GeneralUtils.isInvalid(planRisk)) {
				this.result.notFound();
				this.fail("Plano de risco inválido.");
			}
			
			
			//verificar unidades
			PaginatedList<Unit> units= this.unitBS.listUnitsbyPlanRisk(planRisk);
			
			EmailSenderTask.LOG.info((new GsonBuilder().setPrettyPrinting().create().toJson(units)));

			for(Unit unit:units.getList()) {
				if(!this.unitBS.deletableUnit(unit)) {	
					this.fail("O plano possui unidades que não podem ser deletadas." );
				}
			}
			
			EmailSenderTask.LOG.info((new GsonBuilder().setPrettyPrinting().create().toJson(units)));
			
					
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
			
			EmailSenderTask.LOG.info((new GsonBuilder().setPrettyPrinting().create().toJson(planRisk)));	
			//deletar plano
			this.planRiskBS.delete(planRisk);
			this.success();
			
		} catch (Throwable ex) {
			EmailSenderTask.LOG.error("Unexpected runtime error", ex);
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
	
	@Get(PATH + "/search")
	@NoCache
	@Permissioned
	public void listItensTerms(Long planRiskId, Integer page, String terms, int ordResult, Long limit) {
		if (page == null)
			page = 0;
		
		try {
			PlanRisk planRisk = this.planRiskBS.exists(planRiskId, PlanRisk.class);
			
	
			List<PlanRiskItem> itens = this.planRiskBS.listItemTerms(planRisk, terms, null, ordResult);
			List<PlanRiskSubItem> subitens = this.planRiskBS.listSubitemTerms(planRisk, terms, null, ordResult);

			PaginatedList<PlanRiskSubItem> result = TermResult( itens,subitens, page, limit);
			
			
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
			
			List<PlanRiskItem> itens = this.planRiskBS.listItemTerms(planRisk, terms, itensSelect, ordResult);
			List<PlanRiskSubItem> subitens = this.planRiskBS.listSubitemTerms(planRisk, terms, subitensSelect, ordResult);

			PaginatedList<PlanRiskSubItem> result = TermResult(itens,subitens, page, limit);
			
			this.success(result);

 		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	private PaginatedList<PlanRiskSubItem> TermResult(List<PlanRiskItem> itens, List<PlanRiskSubItem> subitens,  Integer page,  Long limit){
		int firstResult = 0;
		int maxResult = 0;
		int count = 0;
		int add = 0;
		if (limit != null) {
			firstResult = (int) ((page - 1) * limit);
			maxResult = limit.intValue();
		}
		
		for(PlanRiskItem item : itens) {
			PlanRiskSubItem subitem = new PlanRiskSubItem();
			subitem.setDescription(item.getDescription());
			subitem.setId(item.getId());
			subitem.setName(item.getName());
			//item.setSubitemParentId(subitem.getItem().getId());
			subitens.add(subitem);
		}
		
		List<PlanRiskSubItem> list = new ArrayList<>();
		for(PlanRiskSubItem subitem : subitens) {
			if (limit != null) {
				if (count >= firstResult && add < maxResult) {
					list.add(subitem);
					count++;
					add++;
				} else {
					count++;
				}
			} else {
				list.add(subitem);
			}
		}

		PaginatedList<PlanRiskSubItem> result = new PaginatedList<PlanRiskSubItem>();
		
		result.setList(list);
		result.setTotal((long)count);
		return result;
	}

	
}
