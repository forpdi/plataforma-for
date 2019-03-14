package org.forrisco.core.item;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forrisco.core.item.Item;
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
 * @author Matheus Nascimento
 */
@Controller
public class ItemController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private ItemBS itemBS;
	
	protected static final String PATH =  BASEPATH +"/item";
	
	
	/**
	 * Salvar Novo Item
	 * 
	 * @return void
	 */
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	public void saveItem(@NotNull @Valid Item item){
		try {	
			
			if(item.getPolicy()== null) {
				this.fail("Política não encontrada");
			}
			item.setId(null);
			this.itemBS.save(item);
			this.success(item);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	
	/**
	 * Salvar Novo Subitem
	 * 
	 * @return void
	 */
	@Post( PATH + "/subnew")
	@Consumes
	@NoCache
	public void saveSubitem(@NotNull @Valid SubItem subitem){
		try {
			
			if(subitem.getItem() == null) {
				this.fail("Item não encontrada");
			}
			
			this.itemBS.save(subitem);
			this.success(subitem);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}


	/**
	 * Salvar Novo FieldItem
	 * 
	 * @return void
	 */
	@Post( PATH + "/field")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void saveFieldItem(@NotNull @Valid FieldItem fieldItem){
		try {

			fieldItem.setId(null);
			this.itemBS.save(fieldItem);
			this.success(fieldItem);

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Salvar Novo FieldSubItem
	 * 
	 * @return void
	 */
	@Post( PATH + "/subfield")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void saveFieldSubItem(@NotNull @Valid FieldSubItem fieldSubItem){
		try {
			this.itemBS.save(fieldSubItem);
			this.success(fieldSubItem);

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna itens.
	 * 
	 * @param Policy
	 *            Id da política.
	 * @return <List> item
	 */
	@Get( PATH + "")
	@NoCache
	public void listItens(@NotNull Long policyId) {
		try {
			Policy policy = this.itemBS.exists(policyId, Policy.class);
			PaginatedList<Item> itens= this.itemBS.listItensByPolicy(policy);
			this.success(itens);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna item.
	 * 
	 * @param id
	 *			Id do item a ser retornado.
	 * @return Item Retorna o item de acordo com o id passado.
	 */

	@Get( PATH + "/{id}")
	@NoCache
	//@Permissioned
	public void retrieveItem(@NotNull Long id) {
		try {
			Item item = this.itemBS.exists(id, Item.class);
			if (item == null) {
				this.fail("O Item solicitado não foi encontrado.");
			} else {
				
			item.setFieldItem(this.itemBS.listFieldsByItem(item).getList());
						
			this.success(item);
			
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna SubItem.
	 * 
	 * @param id
	 *			Id do SubItem a ser retornado.
	 * @return SubItem 
	 * 			Retorna o subitem de acordo com o id passado.
	 */

	@Get( PATH + "/subitem/{id}")
	@NoCache
	//@Permissioned
	public void retrieveSubItem(@NotNull Long id) {
		try {
			SubItem subitem = this.itemBS.exists(id, SubItem.class);
			if (subitem == null) {
				this.fail("O SubItem solicitado não foi encontrado.");
			} else {
				
				subitem.setFieldSubItem(this.itemBS.listFieldsBySubItem(subitem).getList());
				
				this.success(subitem);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna subitens.
	 * 
	 * @param id
	 *            Id do item.
	 * @return Subitem Retorna os subitens de acordo com o id passado.
	 * 
	 */

	@Get( PATH + "/subitens/{id}")
	@NoCache
	//@Permissioned
	public void retrieveSubitem(@NotNull Long id) {
		try {
			Item item = this.itemBS.exists(id, Item.class);
			if (item == null) {
				this.fail("O item solicitado não foi encontrado.");
			} else {
				PaginatedList<SubItem> subitens= this.itemBS.listSubItensByItem(item);
				this.success(subitens);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
		
	
	/**
	 * Retorna campo.
	 * 
	 * @param id
	 *            Id do item.
	 */

	@Get( PATH + "/field/{id}")
	@NoCache
	//@Permissioned
	public void retrieveField(@NotNull Long id) {
		
		try {
			
			Item item = this.itemBS.exists(id, Item.class);
			if (item == null) {
				this.fail("O item solicitado não foi encontrado.");
			} else {
				
				PaginatedList<FieldItem> fields = this.itemBS.listFieldsByItem(item);
				this.success(fields);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna subcampo.
	 * 
	 * @param id
	 *            Id do subitem.
	 */

	@Get( PATH + "/subfield/{id}")
	@NoCache
	//@Permissioned
	public void retrieveSubField(@NotNull Long id) {
		
		try {
			
			SubItem subitem = this.itemBS.exists(id, SubItem.class);
			if (subitem == null) {
				this.fail("O subitem solicitado não foi encontrado.");
			} else {
				
				PaginatedList<FieldSubItem> fields = this.itemBS.listFieldsBySubItem(subitem);
				this.success(fields);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}



	

	/**
	 * Edita item.
	 * 
	 * @param item
	 *            Item a ser alterado com os novos campos.
	 */
	@Post( PATH + "/update")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void updateItem(@NotNull @Valid Item item) {
		try {
			Item existent = this.itemBS.exists(item.getId(), Item.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}

			if(existent.getPolicy()==null) {
				this.fail("Item sem política associada");
			}


			PaginatedList<FieldItem> fields = this.itemBS.listFieldsByItem(existent);
		
			for(int i=0; i<fields.getList().size();i++) {
				this.itemBS.delete(fields.getList().get(i));
			}

			
			for(int i=0; i<item.getFieldItem().size();i++) {
				FieldItem field =item.getFieldItem().get(i);
				
				field.setName(field.getValue());
				field.setItem(existent);
				this.itemBS.save(field);
			}
			
			existent.setDescription(item.getDescription());
			existent.setName(item.getName());
			this.itemBS.persist(existent);
			
			existent.setFieldItem(this.itemBS.listFieldsByItem(existent).getList());
			
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Edita subitem.
	 * 
	 * @param subitem
	 *            Item a ser alterado com os novos campos.
	 */
	@Post( PATH + "/subitem/update")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void updateSubitem(@NotNull @Valid SubItem subitem) {
		try {
			SubItem existent = this.itemBS.exists(subitem.getId(), SubItem.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}

			if(existent.getItem()==null) {
				this.fail("SubItem sem Item associado");
			}


			PaginatedList<FieldSubItem> subfields = this.itemBS.listFieldsBySubItem(existent);
		
			for(int i=0; i<subfields.getList().size();i++) {
				this.itemBS.delete(subfields.getList().get(i));
			}

			for(int i=0; i<subitem.getFieldSubItem().size();i++) {
				FieldSubItem subfield =subitem.getFieldSubItem().get(i);
				
				subfield.setName(subfield.getValue());
				subfield.setSubitem(existent);
				this.itemBS.save(subfield);
			}
			
			existent.setDescription(subitem.getDescription());
			existent.setName(subitem.getName());
			this.itemBS.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Exclui item.
	 * 
	 * @param id
	 *            Id do item a ser excluído.
	 *            
	 */
	@Delete( PATH + "/{id}")
	@NoCache
	//@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void deleteItem(@NotNull Long id) {
		try {
			Item item = this.itemBS.exists(id, Item.class);
			if (GeneralUtils.isInvalid(item)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<FieldItem> fields = this.itemBS.listFieldsByItem(item);
			
			for(int i=0;i<fields.getList().size();i++) {
				this.itemBS.delete(fields.getList().get(i));
			}
			
			this.itemBS.deleteSubitens(item);
			this.itemBS.delete(item);
			
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Exclui subitem.
	 * 
	 * @param id
	 *            Id do subitem a ser excluído.
	 *            
	 */
	@Delete( PATH + "/subitem/{id}")
	@NoCache
	//@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void deleteSubitem(@NotNull Long id) {
		try {
			SubItem subitem = this.itemBS.exists(id, SubItem.class);
			if (GeneralUtils.isInvalid(subitem)) {
				this.result.notFound();
				return;
			}
			
			this.itemBS.deleteSubitem(subitem);
			
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	

	
	
	/**
	 * Retorna todos subitens da política.
	 * 
	 * @param id
	 *            Id da política.
	 * @return Subitem Retorna os subitens de acordo com o id passado.
	 * 
	 */

	@Get( PATH + "/allsubitens/{id}")
	@NoCache
	//@Permissioned
	public void retrieveAllSubitem(@NotNull Long id) {
		try {
			Policy policy = this.itemBS.exists(id, Policy.class);
			if (policy == null) {
				this.fail("A política solicitada não foi encontrado.");
				return;
			}

			PaginatedList<Item> itens = this.itemBS.listItensByPolicy(policy);
			PaginatedList<SubItem> subitens = new PaginatedList<>();
			List<SubItem> list= new ArrayList<>();
			
			for(Item item :itens.getList()) {
				PaginatedList<SubItem> subitem = this.itemBS.listSubItensByItem(item);
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