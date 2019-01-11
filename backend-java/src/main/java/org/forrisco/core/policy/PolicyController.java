package org.forrisco.core.policy;


import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.core.policy.permissions.ManagePolicyPermission;
import org.forrisco.core.unit.Unit;
//import org.forrisco.core.unit.UnitBS;
import org.forrisco.risk.Risk;
import org.forrisco.risk.RiskBS;
import org.forrisco.risk.RiskLevel;
import org.forrisco.core.item.FieldItem;
import org.forrisco.core.item.Item;
import org.forrisco.core.item.ItemBS;
import org.forrisco.core.plan.PlanRisk;

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
public class PolicyController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private PolicyBS policyBS;
	@Inject private ItemBS itemBS;
	@Inject private RiskBS riskBS;
	//@Inject private UnitBS unitBS;
	
	protected static final String PATH =  BASEPATH +"/policy";
	
	
	/**
	 * Salvar Nova Política
	 * 
	 * @return void
	 */
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void savePolicy(@NotNull @Valid  Policy policy){
		
		try {
			if(this.domain == null) {
				this.fail("Instituição não definida");
				return;
			}
			policy.setCompany(this.domain.getCompany());
			policy.setId(null);
			this.policyBS.save(policy);
			this.riskBS.saveRiskLevel(policy);
			this.success(policy);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}


	/**
	 * 
	 * Lista as políticas arquivadas
	 * 
	 * @param page
	 *            Número da página da listagem a ser acessada.
	 * @return PaginatedList<PlanMacro> Lista de planos macro arquivados da
	 *         companhia.
	 */
	@Get( PATH + "/archivedpolicy")
	@NoCache
	public void listMacrosArchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
				PaginatedList<Policy> policy = this.policyBS.listPolicies(this.domain.getCompany(), true, page);
				this.success(policy);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	@Get( PATH + "/unarchivedpolicy")
	@NoCache
	public void listPolicyUnarchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
			PaginatedList<Policy> policies = this.policyBS.listPolicies(this.domain.getCompany(), false, page);
				this.success(policies);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}


	/**
	 * Retorna política.
	 * 
	 * @param id
	 *            Id da política a ser retornado.
	 * @return Policy Retorna a política de acordo com o id passado.
	 */
	@Get( PATH + "/{id}")
	@NoCache
	@Permissioned
	public void retrievePolicy(Long id) {
		try {
			Policy policy = this.policyBS.exists(id, Policy.class);
			if (policy == null) {
				this.fail("A política solicitada não foi encontrado.");
			} else {
				this.success(policy);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Exclui política.
	 * 
	 * @param id
	 *            Id da política ser excluído.
	 *
	 */
	@Delete( PATH + "/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deletePolicy(@NotNull Long id) {
		try {
			
			Policy policy = this.policyBS.exists(id, Policy.class);
			if (GeneralUtils.isInvalid(policy)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<PlanRisk> plans = this.policyBS.listPlanbyPolicy(policy);
			PaginatedList<RiskLevel>  riskLevels= this.policyBS.listRiskLevelbyPolicy(policy);
			
			if(plans.getTotal() >0) {
				this.fail("Impossível excluir política com Planos de Risco vinculados");
			}else {

				PaginatedList<Item> itens = this.itemBS.listItensByPolicy(policy);

				for(Item item : itens.getList()){

					PaginatedList<FieldItem> fields = this.itemBS.listFieldsByItem(item);
					for(FieldItem field : fields.getList()){
						this.itemBS.delete(field);
					}
					
					this.itemBS.deleteSubitens(item);

					this.itemBS.delete(item);
				}

				for(RiskLevel riskLevel : riskLevels.getList()){
					this.riskBS.delete(riskLevel);
				}

				//policy.setDeleted(true);
				this.policyBS.delete(policy);
				this.success();
			}

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Edita política.
	 * 
	 * @param policy
	 *            política a ser alterado com os novos campos.
	 */
	@Post( PATH + "/update")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void Policy(@NotNull @Valid Policy policy) {
		try {
			Policy existent = this.policyBS.exists(policy.getId(), Policy.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.fail("Política não encontrada");
				return;
			}

			if(existent.getCompany()==null) {
				this.fail("Política sem Istituição associada");
				return;
			}

			PaginatedList<RiskLevel> existentLevels = this.policyBS.listRiskLevelbyPolicy(existent);
			
			// se mudar linhas/colunas
			// ou alterar quantidade de graus de risco
			// e tiver unidade vinculada
			/** depois verificar se está recuperando certo plano de risco/unidade/risco**/
			/*if(policy.getNcolumn() != existent.getNcolumn() 
					|| policy.getNline() != existent.getNline()
					|| policy.getLevel() != existentLevels.getTotal()) {
			
				PaginatedList<PlanRisk> plans = this.policyBS.listPlanbyPolicy(policy);
				for(int i=0; i<plans.getTotal(); i++){
					PaginatedList<Unit> units = this.unitBS.listUnitbyPlan(plans.getList().get(i));
					for(int j=0;j<units.getTotal();j++) {
						PaginatedList<Risk> risks = this.riskBS.listRiskbyUnit(units.getList().get(i));
						if (risks.getTotal()>0) {
							this.fail("Impossível modificar política com Risco(s) vinculado(s)");
							return;
						}
					}	
				}
			} */
		
			//rótulos podem sempre ser mudados

			//atualizar graus de risco
			for(int i=0; i<existentLevels.getTotal(); i++){
				this.riskBS.delete(existentLevels.getList().get(i));
			}
			
			PaginatedList<RiskLevel> policyLevels = this.riskBS.listRiskLevel(policy);
			
			for(int i=0;i<policyLevels.getTotal() ;i++) {
				policyLevels.getList().get(i).setPolicy(existent);
				this.riskBS.saveRiskLevel(policyLevels.getList().get(i));
			}
			//this.riskBS.saveRiskLevel(existent);

			existent.setDescription(policy.getDescription());
			existent.setName(policy.getName());
			existent.setImpact(policy.getImpact());
			existent.setProbability(policy.getProbability());
			existent.setMatrix(policy.getMatrix());
			existent.setNline(policy.getNline());
			existent.setNcolumn(policy.getNcolumn());
			
			this.policyBS.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}

	@Post( PATH + "/archive")
	public void function5() {
		LOGGER.warn("5");
	}

	@Post( PATH + "/unarchive")
	public void function6() {
		LOGGER.warn("6");
	}

	
	@Post( PATH + "/duplicate")
	public void function1() {
		LOGGER.warn("2");
	}


}