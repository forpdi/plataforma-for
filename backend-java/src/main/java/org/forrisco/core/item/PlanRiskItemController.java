package org.forrisco.core.item;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.policy.Policy;

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
 */

@Controller
public class PlanRiskItemController extends AbstractController {

	@Inject private PlanRiskItemBS planRiskItemBS;
	@Inject private PlanRisk planRisk;
	
	protected static final String PATH = BASEPATH +"/planrisk/item";
	
	/**
	 * Retorna itens.
	 * 
	 * @param PlanRisk Id do plano de risco a ser retornado.
	 * @return <List> item
	 */
	@Get(PATH + "")
	@NoCache
	public void listItens(@NotNull Long planRiskId) {
		try {
			PlanRisk planRisk = this.planRiskItemBS.exists(planRiskId, PlanRisk.class);
			
			if(planRisk == null) {
				this.fail("Plano de Risco não encontrado");
			}
			
			PaginatedList<PlanRiskItem> itens = this.planRiskItemBS.listItensByPlanRisk(planRisk);
			this.success(itens);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Salva um item
	 *  
	 * @return void
	 */
	@Post(PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void saveItem(@NotNull @Valid PlanRiskItem planRiskItem) {
		try {
			
			if(planRiskItem.getPlanRisk() == null) {
				this.fail("Plano de Risco não encontrado");
			}
			
			planRiskItem.setId(null);
			this.planRiskItemBS.save(planRiskItem);
			this.success(planRiskItem);
			
			for(int i = 0; i < planRiskItem.getPlanRiskItemField().size(); i++) {
				PlanRiskItemField planRiskItemField = planRiskItem.getPlanRiskItemField().get(i);
				planRiskItemField.setPlanRiskItem(planRiskItem);
				this.planRiskItemBS.save(planRiskItemField);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Salva um subitem
	 *  
	 * @return void
	 */
	@Post(PATH + "/new/subitem")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void saveSubItem(@NotNull @Valid PlanRiskSubItem planRiskSubItem) {
		try {
			
			if(planRiskSubItem.getPlanRiskItem() == null) {
				this.fail("Item Vinculado não encontrado não encontrado");
			}
			
			planRiskSubItem.setId(null);
			this.planRiskItemBS.save(planRiskSubItem);
			this.success(planRiskSubItem);
			
			for(int i = 0; i < planRiskSubItem.getPlanRiskSubItemField().size(); i++) {
				PlanRiskSubItemField planRiskSubItemField = planRiskSubItem.getPlanRiskSubItemField().get(i);
				planRiskSubItemField.setPlanRiskSubItem(planRiskSubItem);
				this.planRiskItemBS.save(planRiskSubItemField);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Duplica um item
	 *  
	 *  @Param PlanRiskItem um item com o id do item ogirinal
	 *  				 e o id no plano a ser salvo o item duplicado
	 * @return void
	 */
	@Post(PATH + "/duplicate")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void duplicateItem(@NotNull @Valid PlanRiskItem planRiskItem) {
		try {
			
			PlanRisk planRisk = this.planRiskItemBS.exists(planRiskItem.getPlanRisk().getId(), PlanRisk.class);
			
			if(planRisk == null || planRisk.isDeleted()) {
				this.fail("Plano de Risco não encontrado");
			}
			
			
			//item
			PlanRiskItem item = this.planRiskItemBS.exists(planRiskItem.getId(), PlanRiskItem.class);
			
			if(item == null || item.isDeleted()) {
				this.fail("Item não encontrado");
				return;
			}

			planRiskItem.setId(null);
			planRiskItem.setDescription(item.getDescription());
			planRiskItem.setName(item.getName());
			planRiskItem.setPlanRisk(planRisk);
			
			this.planRiskItemBS.save(planRiskItem);
			
			
			PaginatedList<PlanRiskItemField> fields = this.planRiskItemBS.listFieldsByPlanRiskItem(item);
			PaginatedList<PlanRiskSubItem> subitens = this.planRiskItemBS.listSubItemByItem(item);
			
			
			//fields
			for(PlanRiskItemField field :fields.getList()) {
				
				PlanRiskItemField f= new PlanRiskItemField();
				f.setPlanRiskItem(planRiskItem);
				f.setDescription(field.getDescription());
				f.setFileLink(field.getFileLink());
				f.setName(field.getName());
				f.setText(field.isText());
				f.setValue(field.getValue());
				f.setId(null);
				this.planRiskItemBS.save(f);
			}
			
			//subitens
			for(PlanRiskSubItem subitem :subitens.getList()) {
				
				PlanRiskSubItem sub = new PlanRiskSubItem();
				
				sub.setId(null);
				sub.setPlanRiskItem(planRiskItem);
				sub.setDescription(subitem.getDescription());
				sub.setName(subitem.getName());	
				this.planRiskItemBS.save(sub);
				
				//subfields
				 PaginatedList<PlanRiskSubItemField> subfields = this.planRiskItemBS.listSubFieldsBySubItem(subitem);
				
				for( PlanRiskSubItemField subfield : subfields.getList()) {
					
					PlanRiskSubItemField sf= new PlanRiskSubItemField();
					sf.setPlanRiskSubItem(sub);
					sf.setDescription(subfield.getDescription());
					sf.setFileLink(subfield.getFileLink());
					sf.setName(subfield.getName());
					sf.setText(subfield.isText());
					sf.setValue(subfield.getValue());
					sf.setId(null);
					this.planRiskItemBS.save(sf);
				}
				
			}
			
			this.success(planRiskItem);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna as informações e os Campos de um Subitem
	 * @param id do item a ser consultado
	 *  
	 * @return void
	 */
	@Get(PATH + "/sub-itens/{id}")
	@NoCache
	public void listSubitens(Long id) {
		try {
			PlanRiskItem planRiskItem = this.planRiskItemBS.exists(id, PlanRiskItem.class);
			
			if (planRiskItem == null) {
				this.fail("O Item solicitado não foi encontrado.");
			} else {
				PaginatedList<PlanRiskSubItem> itens = this.planRiskItemBS.listSubItemByItem(planRiskItem);
				this.success(itens);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna as informações e os Campos de todos os Subitens
	 * @param id do item a ser consultado
	 *  
	 * @return void
	 */
	@Get(PATH + "/allsub-itens/{id}")
	@NoCache
	public void listAllSubitens(Long id) {
		try {
			PlanRisk planRisk = this.planRiskItemBS.exists(id, PlanRisk.class);
			
			if (planRisk == null) {
				this.fail("O Plano solicitado não foi encontrado.");
			} else {
				
				PaginatedList<PlanRiskItem> itens = this.planRiskItemBS.listItensByPlanRisk(planRisk);
				PaginatedList<PlanRiskSubItem> subitens = new PaginatedList<>();
				List<PlanRiskSubItem> list= new ArrayList<>();
				
				for(PlanRiskItem item : itens.getList()) {
					PaginatedList<PlanRiskSubItem> subitem = this.planRiskItemBS.listSubItemByItem(item);
					list.addAll(subitem.getList());
				}
			
				subitens.setList(list);
				subitens.setTotal((long) list.size());
				this.success(subitens);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna as informaçõesde um Item
	 * @param id do item a ser detalhado
	 *  
	 * @return void
	 */
	@Get(PATH + "/{id}")
	@NoCache
	public void detailItem(Long id) {
		try {
			PlanRiskItem planRiskItem = this.planRiskItemBS.exists(id, PlanRiskItem.class);
			
			if (planRiskItem == null) {
				this.fail("O Item solicitado não foi encontrado.");
			} else {
				planRiskItem.setPlanRiskItemField(this.planRiskItemBS.listFieldsByPlanRiskItem(planRiskItem).getList());
				this.success(planRiskItem);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Retorna as informaçõesde um subitem
	 * @param id do subitem a ser detalhado
	 *  
	 * @return void
	 */
	@Get(PATH + "/subitem/{id}")
	@NoCache
	public void detailSubItem(@NotNull Long id) {
		try {
			PlanRiskSubItem planRiskSubItem = this.planRiskItemBS.exists(id, PlanRiskSubItem.class);
			
			if (planRiskSubItem == null) {
				this.fail("O Subitem solicitado não foi encontrado.");
			} else {
				planRiskSubItem.setPlanRiskSubItemField(this.planRiskItemBS.listSubFieldsBySubItem(planRiskSubItem).getList());
				this.success(planRiskSubItem);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Atualiza campos e título do item
	 * @param planRiskItem
	 */
	@Post( PATH + "/update")
	@Consumes
	@NoCache
	public void updatePlanRiskItem(@NotNull @Valid PlanRiskItem planRiskItem) {
		try {
			PlanRiskItem existent = planRiskItemBS.exists(planRiskItem.getId(), PlanRiskItem.class);
			
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			
			if(existent.getPlanRisk() == null) {
				this.fail("Item sem plano de risco associado");	
			}
			
			PaginatedList<PlanRiskItemField> fields = this.planRiskItemBS.listFieldsByPlanRiskItem(planRiskItem);
			
			for(int i = 0; i < fields.getList().size(); i++) {
				this.planRiskItemBS.delete(fields.getList().get(i));
			}
			
			for(int i = 0; i < planRiskItem.getPlanRiskItemField().size(); i++) {
				PlanRiskItemField planRiskItemField = planRiskItem.getPlanRiskItemField().get(i);
				
				planRiskItemField.setPlanRiskItem(existent);
				this.planRiskItemBS.save(planRiskItemField);
			}
			
			existent.setDescription(planRiskItem.getDescription());
			existent.setName(planRiskItem.getName());
			this.planRiskItemBS.persist(existent);
			this.success(existent);
			
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Atualiza subitem eseus campos
	 * @param planRiskSubItem
	 */
	@Post( PATH + "/update-subitem")
	@Consumes
	@NoCache
	public void updatePlanRiskSubItem(@NotNull @Valid PlanRiskSubItem planRiskSubItem) {
		try {
			PlanRiskSubItem existent = planRiskItemBS.exists(planRiskSubItem.getId(), PlanRiskSubItem.class);
			
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			
			if(existent.getPlanRiskItem() == null) {
				this.fail("Subitem sem item associado");	
			}
			
			PaginatedList<PlanRiskSubItemField> fields = this.planRiskItemBS.listSubFieldsBySubItem(planRiskSubItem);
			
			for(int i = 0; i < fields.getList().size(); i++) {
				this.planRiskItemBS.delete(fields.getList().get(i));
			}
			
			for(int i = 0; i < planRiskSubItem.getPlanRiskSubItemField().size(); i++) {
				PlanRiskSubItemField planRiskItemField = planRiskSubItem.getPlanRiskSubItemField().get(i);
				
				planRiskItemField.setPlanRiskSubItem(existent);
				this.planRiskItemBS.save(planRiskItemField);
			}
			
			existent.setDescription(planRiskSubItem.getDescription());
			existent.setName(planRiskSubItem.getName());
			this.planRiskItemBS.persist(existent);
			this.success(existent);
			
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Deleta um item do plano de risco
	 * @param id
	 */
	@Delete(PATH + "/{id}")
	@NoCache
	public void deletePlanRiskItem(@NotNull Long id) {
		try {
			PlanRiskItem planRiskItem = this.planRiskItemBS.exists(id, PlanRiskItem.class);
			
			if (GeneralUtils.isInvalid(planRiskItem)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<PlanRiskItemField> fields = this.planRiskItemBS.listFieldsByPlanRiskItem(planRiskItem);
			
			for(int i = 0; i < fields.getList().size(); i ++) {
				PlanRiskItemField planRiskItemField = fields.getList().get(i);
				
				this.planRiskItemBS.deleteSubItens(planRiskItem);  //Deleta os SubItens
				this.planRiskItemBS.delete(planRiskItemField);     //Delete os campos do item
			}
			
			this.planRiskItemBS.delete(planRiskItem); //Delete o Item
			this.success(planRiskItem);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Deleta um subitem do plano de risco
	 * @param id
	 */
	@Delete(PATH + "/delete-subitem/{id}")
	@NoCache
	public void deletePlanRiskSubItem(@NotNull Long id) {
		try {
			PlanRiskSubItem planRiskSubItem = this.planRiskItemBS.exists(id, PlanRiskSubItem.class);
			
			if (GeneralUtils.isInvalid(planRiskSubItem)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<PlanRiskSubItemField> fields = this.planRiskItemBS.listSubFieldsBySubItem(planRiskSubItem);
			
			for(int i = 0; i < fields.getList().size(); i ++) {
				PlanRiskSubItemField result = fields.getList().get(i);
				this.planRiskItemBS.delete(result);     //Delete os campos do subitem
			}
			
			this.planRiskItemBS.delete(planRiskSubItem); //Delete o subitem
			this.success(planRiskSubItem);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	@Get( PATH + "/allsubitens/{id}")
	@NoCache
	//@Permissioned
	public void retrieveAllSubitem(@NotNull Long id) {
		try {
			PlanRisk planRisk = this.planRiskItemBS.exists(id, PlanRisk.class);
			
			if (planRisk == null) {
				this.fail("O plano de risco solicitado não foi encontrado.");
				return;
			}

			PaginatedList<PlanRiskItem> itens = this.planRiskItemBS.listItensByPlanRisk(planRisk);
			PaginatedList<PlanRiskSubItem> subitens = new PaginatedList<>();
			List<PlanRiskSubItem> list= new ArrayList<>();
			
			for(PlanRiskItem item :itens.getList()) {
				PaginatedList<PlanRiskSubItem> subitem = this.planRiskItemBS.listSubItemByItem(item);
				list.addAll(subitem.getList());
			}
		
			subitens.setList(list);
			subitens.setTotal((long) list.size());
			this.success(subitens);

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
}
