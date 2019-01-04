package org.forrisco.risk;

import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.User;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.unit.Unit;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
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
	@Inject private RiskBS riskBS;

	protected static final String PATH =  BASEPATH +"/risk";
	
	
	/**
	 * Salvar Novo Risco
	 * 
	 * @Param Risk
	 * 			instancia de um novo risco
	 */
	@Post(PATH + "/new")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid Risk risk){
		try {
			Unit unit = this.riskBS.exists(risk.getUnit().getId(), Unit.class);
			if (unit == null) {
				this.fail("A unidade solicitada não foi encontrada.");
				return;
			}
			
			User user = this.riskBS.exists(risk.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			
			risk.setRiskLevel(this.riskBS.getRiskLevelbyRisk(risk));
			
			if (risk.getRiskLevel() == null) {
				this.fail("Grau de Risco solicitado não foi encontrado.");
				return;
			}
			
			risk.setId(null);
			risk.setBegin(new java.util.Date());
			this.riskBS.saveRisk(risk);
			this.success(risk);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Salvar Nova ação de prevenção
	 * 
	 * @Param PreventiveAction
	 * 			instancia de uma nova ação preventiva
	 */
	@Post(PATH + "/actionnew")
	@Consumes
	@NoCache
	//@Permissioned(AccessLevels.SYSTEM_ADMIN)
	public void save(@NotNull @Valid PreventiveAction action){
		try {
			Risk risk = this.riskBS.exists(action.getRisk().getId(), Risk.class);
			if (risk == null) {
				this.fail("Risco solicitado não foi encontrado.");
				return;
			}
			
			User user = this.riskBS.exists(action.getUser().getId(), User.class);
			if (user == null) {
				this.fail("Usuário solicitada não foi encontrado.");
				return;
			}
			
			action.setId(null);
			this.riskBS.saveAction(action);
			this.success(action);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	

	/**
	 * Retorna risco.
	 * 
	 * @param id
	 *			Id do risco a ser retornado.
	 * @return Risk Retorna o risco de acordo com o id passado.
	 */
	@Get( PATH + "/{id}")
	@NoCache
	//@Permissioned
	public void listUnit(Long id) {
		try {
			Risk risk = this.riskBS.exists(id, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
			} else {
				this.success(risk);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
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
	public void retrieveRiskLevel(Long id) {
		try {
			Policy policy = this.riskBS.exists(id, Policy.class);
			if (policy == null) {
				this.fail("A política solicitada não foi encontrado.");
			} else {
				PaginatedList<RiskLevel> risklevels = this.riskBS.listRiskLevelbyPolicy(policy);
				this.success(risklevels);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna ações de prevenção.
	 * 
	 * @param id
	 *			Id do risco.
	 * @return <PaginedList> PreventiveAcion
	 * 			 Retorna lista de ações de prevenção do risco.
	 */
	@Get( PATH + "/action")
	@NoCache
	//@Permissioned
	public void listActions(@NotNull Long riskId) {
		try {
			Risk risk = this.riskBS.exists(riskId, Risk.class);
			if (risk == null) {
				this.fail("O risco solicitado não foi encontrado.");
				return;
			} 
			
			PaginatedList<PreventiveAction> actions = this.riskBS.listActionbyRisk(risk);
			
			this.success(actions);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Atualiza ação preventiva.
	 * 
	 * @param id
	 *            Id da ação a ser atualizada.
	 */
	@Post( PATH + "/action/update")
	@NoCache
	@Consumes
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void updateAction(@NotNull PreventiveAction action) {
		try {
			PreventiveAction oldaction = this.riskBS.exists(action.getId(), PreventiveAction.class);
			if (oldaction == null) {
				this.fail("A ação solicitado não foi encontrada.");
				return;
			} 
			
			User user = this.riskBS.exists(action.getUser().getId(), User.class);
			if (user == null) {
				this.fail("A usuário solicitado não foi encontrado.");
				return;
			} 
			
			oldaction.setAccomplished(action.isAccomplished());
			oldaction.setAction(action.getAction());
			oldaction.setUser(action.getUser());
			this.riskBS.saveAction(oldaction);
			this.success();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Exclui ação preventiva.
	 * 
	 * @param id
	 *            Id da ação a ser excluida.
	 */
	@Delete( PATH + "/action/{actionId}")
	@NoCache
	//@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePolicyPermission.class })
	public void deleteAction(@NotNull Long actionId) {
		try {
			PreventiveAction action = this.riskBS.exists(actionId, PreventiveAction.class);
			if (action == null) {
				this.fail("A ação solicitado não foi encontrado.");
				return;
			} 
			
			riskBS.delete(action);
			this.success();
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
}