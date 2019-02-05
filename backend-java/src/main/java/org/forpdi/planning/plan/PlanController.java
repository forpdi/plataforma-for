package org.forpdi.planning.plan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.document.Document;
import org.forpdi.planning.document.DocumentBS;
import org.forpdi.planning.permissions.ManagePlanMacroPermission;
import org.forpdi.planning.permissions.ManagePlanPermission;
import org.forpdi.planning.structure.Structure;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevelInstance;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class PlanController extends AbstractController {

	@Inject
	private PlanBS bs;
	@Inject
	private StructureBS sbs;
	@Inject
	private UserBS ubs;
	@Inject
	private DocumentBS dbs;
	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private NotificationBS notificationBS;

	/**
	 * Salva plano macro.
	 * 
	 * @param plan
	 *            Plano macro a ser salvo.
	 * @return PlanMacro Plano macro salvo.
	 */
	@Post(BASEPATH + "/planmacro")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanMacroPermission.class })
	public void saveMacro(@NotNull @Valid PlanMacro plan) {
		try {
			plan.setId(null);
			plan.setCompany(this.domain.getCompany());
			this.bs.savePlanMacro(plan);
			String url = domain.getBaseUrl() + "/#/plan/" + plan.getId();

			// this.notificationBS.sendNotification(NotificationType.PLAN_MACRO_CREATED,
			// plan.getName(), null, null, url);

			this.success(plan);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Edita plano macro.
	 * 
	 * @param plan
	 *            Plano macro a ser alterado com os novos valores.
	 * @return PlanMacro Retorna plano macro alterado.
	 */
	@Put(BASEPATH + "/planmacro")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanMacroPermission.class })
	public void updateMacro(@NotNull @Valid PlanMacro plan) {
		try {
			PlanMacro existent = this.bs.exists(plan.getId(), PlanMacro.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDescription(plan.getDescription());
			existent.setName(plan.getName());
			existent.setBegin(plan.getBegin());
			existent.setEnd(plan.getEnd());
			existent.setDocumented(plan.isDocumented());
			
			if(this.bs.listPlansForPlanMacro(existent).isEmpty()){
				existent.setHaveSons(false);	
			} else {
				existent.setHaveSons(true);
			}
			
			Document document = this.dbs.retrieveByPlan(existent);
			if (document != null) {
				document.setTitle("Documento - " + plan.getName());
				this.bs.persist(document);
			}
			
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Retorna plano macro.
	 * 
	 * @param id
	 *            Id do plano macro a ser retornado.
	 * @return PlanMacro Retorna o plano macro de acordo com o id passado.
	 */
	@Get(BASEPATH + "/planmacro/{id}")
	@NoCache
	@Permissioned
	public void retrieveMacro(Long id) {
		try {
			PlanMacro plan = this.bs.exists(id, PlanMacro.class);
			if (plan == null) {
				this.fail("O plano solicitado não foi encontrado.");
			} else {
				if(this.bs.listPlansForPlanMacro(plan).size() > 0)
					plan.setHaveSons(true);
				else
					plan.setHaveSons(false);
				this.success(plan);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Lista os planos macro da companhia.
	 * 
	 * @param page
	 *            Número da página da listagem a ser acessada.
	 * @return PaginatedList<PlanMacro> Lista de planos macro da companhia.
	 */
	@Get(BASEPATH + "/planmacro")
	@NoCache
	public void listMacros(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
				PaginatedList<PlanMacro> plans = this.bs.listMacros(this.domain.getCompany(), false, page);
				for(PlanMacro p : plans.getList()){
					if(this.bs.listPlansForPlanMacro(p).size() > 0)
						p.setHaveSons(true);
					else
						p.setHaveSons(false);
				}
				this.success(plans);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Lista os planos macro arquivados companhia.
	 * 
	 * @param page
	 *            Número da página da listagem a ser acessada.
	 * @return PaginatedList<PlanMacro> Lista de planos macro arquivados da
	 *         companhia.
	 */
	@Get(BASEPATH + "/planmacro/archivedplanmacro")
	@NoCache
	public void listMacrosArchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
				PaginatedList<PlanMacro> plans = this.bs.listMacros(this.domain.getCompany(), true, page);
				this.success(plans);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Lista os planos macro não arquivados companhia.
	 * 
	 * @param page
	 *            Número da página da listagem a ser acessada.
	 * @return PaginatedList<PlanMacro> Lista de planos macro arquivados da
	 *         companhia.
	 */
	@Get(BASEPATH + "/planmacro/unarchivedplanmacro")
	@NoCache
	public void listMacrosUnarchived(Integer page) {
		if (page == null)
			page = 0;
		try {
			if (this.domain != null) {
				PaginatedList<PlanMacro> plans = this.bs.listMacros(this.domain.getCompany(), false, page);
				this.success(plans);
			} else {
				this.fail("Não possui domínio!");
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Exclui plano macro.
	 * 
	 * @param id
	 *            Id do plano macro a ser excluído.
	 * @return
	 */
	@Delete(BASEPATH + "/planmacro/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanMacroPermission.class })
	public void deleteMacro(@NotNull Long id) {
		try {
			PlanMacro existent = this.bs.exists(id, PlanMacro.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDeleted(true);
			this.bs.persist(existent);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Cria um novo plano de metas.
	 * 
	 * @param plan
	 *            Plano de metas a ser criado.
	 * @return Plan Retorna plano de metas criado.
	 */
	@Post(BASEPATH + "/plan")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void savePlan(@NotNull @Valid Plan plan) {
		try {
			plan.setId(null);
			Structure structure = this.bs.exists(plan.getStructure().getId(), Structure.class);
			structure.setLevels(this.sbs.listStructureLevels(structure));
			plan.setStructure(structure);
			this.bs.persist(plan);
			PlanMacro macro = this.bs.retrievePlanMacroById(plan.getParent().getId());
			String url = domain.getBaseUrl() + "/#/plan/" + macro.getId() + "/details/subplan/" + plan.getId();
			// this.notificationBS.sendNotification(NotificationType.PLAN_CREATED,
			// plan.getName(), macro.getName(),null, url);
			this.success(plan);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Edita plano de metas.
	 * 
	 * @param plan
	 *            Plano de metas a ser alterado com os novos valores.
	 * @return Plan Retorna o plano alterado.
	 */
	@Put(BASEPATH + "/plan")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void update(@NotNull @Valid Plan plan) {
		try {
			Plan existent = this.bs.exists(plan.getId(), Plan.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDescription(plan.getDescription());
			existent.setName(plan.getName());
			existent.setBegin(plan.getBegin());
			existent.setEnd(plan.getEnd());
			this.bs.persist(existent);
			existent.getStructure().setLevels(this.sbs.listStructureLevels(existent.getStructure()));
			existent.setUpdated(true);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualiza plano de metas.
	 * 
	 * @param id
	 *            Id do plano de metas a ser alterado.
	 * @param name
	 *            Novo nome do plano de metas.
	 * @param description
	 *            Nova descrição do plano de metas.
	 * @param beginDate
	 *            Nova data de início do plano de metas.
	 * @param endDate
	 *            Nova data de fim do plano de metas.
	 * @return Plan Retorna o plano de metas alterado.
	 */
	@Post(BASEPATH + "/plan/update")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void updatePlan(Long id, String name, String description, String beginDate, String endDate) {
		try {
			Plan existent = this.bs.exists(id, Plan.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			if (beginDate != null && endDate != null) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				existent.setBegin((Date) formatter.parse(beginDate));
				existent.setEnd((Date) formatter.parse(endDate));
			} else {
				this.fail("As datas devem ser preenchidas!");
			}
			existent.setName(name);
			existent.setDescription(description);
			this.bs.persist(existent);
			existent.getStructure().setLevels(this.sbs.listStructureLevels(existent.getStructure()));
			existent.setUpdated(true);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Retorna plano de metas pelo id passado por parâmetro.
	 * 
	 * @param id
	 *            Id do plano de metas a ser retornado.
	 * @return Plan Retorna o plano de metas.
	 */
	@Get(BASEPATH + "/plan/{id}")
	@NoCache
	@Permissioned
	public void retrievePlan(Long id) {
		try {
			Plan plan = this.bs.exists(id, Plan.class);
			if (plan == null) {
				this.fail("O subplano solicitado não foi encontrado.");
			} else {
				if(this.sbs.listLevelsInstance(plan, null).getList().size() > 0){
					plan.setHaveSons(true);
				}else{
					plan.setHaveSons(false);
				}
				this.success(plan);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	@Get(BASEPATH + "/plan/performance")
	@NoCache
	@Permissioned
	public void retrievePlansPerformance(Long parentId) {
		try {
			PlanMacro macro = this.bs.exists(parentId, PlanMacro.class);
			List<Plan> list = this.bs.listPlansForPlanMacro(macro);
			List<Plan> result = new ArrayList<Plan>(list.size());
			
			List<PlanDetailed> planDetailedList = new ArrayList<PlanDetailed>();
			for (Plan p : list) {
				Structure s = p.getStructure();
				s.setLevels(this.sbs.listStructureLevels(s));
				p.setStructure(s);
				
				planDetailedList = this.bs.listPlansDetailed(p);
				p.setPlanDetailedList(new ArrayList<PlanDetailed>());
				for (int i=0; i<12; i++) {
					PlanDetailed planDetailed = null;
					for (PlanDetailed pDetailed : planDetailedList) {
						pDetailed.setPlan(null);
						if (pDetailed.getMonth() == i+1) {
							planDetailed = pDetailed;
						}
					}
					p.getPlanDetailedList().add(planDetailed);
				}
				
				result.add(p);
			}
			
			this.success(result, (long) result.size());
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Retorna os planos de metas com seus níveis de um plano macro.
	 * 
	 * @param parentId
	 *            Id do plano macro pai.
	 * @param page
	 *            Número da página da lista de plano de metas.
	 * @return List<Plan> Retorna lista de plano de metas com seu níveis.
	 */
	@Get(BASEPATH + "/plan")
	@NoCache
	public void listPlans(Long parentId, Integer page) {
		if (page == null)
			page = 0;
		try {
			PlanMacro macro = this.bs.exists(parentId, PlanMacro.class);
			PaginatedList<Plan> plans = this.bs.listPlans(macro, false, page, null, null, 0);
			List<Plan> list = plans.getList();
			List<Plan> result = new ArrayList<Plan>(list.size());
			for (Plan p : list) {
				Structure s = p.getStructure();
				s.setLevels(this.sbs.listStructureLevels(s));
				p.setStructure(s);
				if(this.sbs.listLevelsInstance(p, null).getList().size() > 0){
					p.setHaveSons(true);
				}else{
					p.setHaveSons(false);
				}
				result.add(p);
			}
			
			plans.setList(result);

			this.success(plans);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar Planos e seus níveis segundo uma chave de busca.
	 * 
	 * @param parentId
	 *            Id do plano macro pai.
	 * @param page
	 *            Número da página da lista de plano de metas.
	 * @param terms
	 *            Termo de busca.
	 * @param subPlansSelect
	 *            Conjunto de planos a serem buscados.
	 * @param levelsSelect
	 *            Conjunto de níveis que podem ser buscados.
	 * @param dataInit
	 *            Filtro de data inicial.
	 * @param dataEnd
	 *            Filtro de data final.
	 * @param ordResult
	 *            Ordenação do resultado, 1 para crescente e 2 para decrescente.
	 * @return PaginatedList<Plan> Retorna lista de planos de metas de acordo
	 *         com os filtros.
	 */
	@Get(BASEPATH + "/plan/findTerms")
	@NoCache
	@Permissioned
	public void listPlansTerms(Long parentId, Integer page, String terms, Long subPlansSelect[], Long levelsSelect[],
			String dataInit, String dataEnd, int ordResult, Long limit) {
		if (page == null)
			page = 0;
		try {
			PlanMacro macro = this.bs.exists(parentId, PlanMacro.class);
			PaginatedList<User> users = this.ubs.listUsersBySearch(terms);
			
			List<StructureLevelInstance> listLevelInstancesByResponsible = this.sbs
					.listLevelInstancesByResponsible(users.getList());

			List<StructureLevelInstance> levelInstanceList = this.sbs.listLevelsInstanceTerms(macro, terms,
					subPlansSelect, levelsSelect, ordResult);

			List<AttributeInstance> attributeInstanceList = this.sbs.listAttributesTerms(macro, terms, subPlansSelect,
					levelsSelect, ordResult);
			
			int firstResult = 0;
			int maxResult = 0;
			int count = 0;
			int add = 0;
			if (limit != null) {
				firstResult = (int) ((page - 1) * limit);
				maxResult = limit.intValue();
			}
			
			for (AttributeInstance attributeInstance : attributeInstanceList) {
				boolean exist = false;
				for (StructureLevelInstance levelInstance : levelInstanceList) {
					if (attributeInstance.getLevelInstance().getId() == levelInstance.getId()) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					levelInstanceList.add(this.sbs.retrieveLevelInstance(attributeInstance.getLevelInstance().getId()));
				}
			}
			
			PaginatedList<StructureLevelInstance> levelInstances = new PaginatedList<StructureLevelInstance>();
			levelInstances.setList(new ArrayList<StructureLevelInstance>());
			for (StructureLevelInstance levelInstance : levelInstanceList) {
				if (limit != null) {
					if (count >= firstResult && add < maxResult) {
						levelInstances.getList().add(levelInstance);
						count++;
						add++;
					} else {
						count++;
					}
				} else {
					levelInstances.getList().add(levelInstance);
				}
			}

			for (StructureLevelInstance levelInstance : listLevelInstancesByResponsible) {
				if (limit != null) {
					if (count >= firstResult && add < maxResult) {
						levelInstances.getList().add(levelInstance);
						count++;
						add++;
					} else {
						count++;
					}
				} else {
					levelInstances.getList().add(levelInstance);
				}
			}

			
			levelInstances.setTotal((long) count);
			this.success(levelInstances);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Exclui plano de metas.
	 * 
	 * @param id
	 *            Id do plano de metas a ser excluído.
	 * @return
	 */
	@Delete(BASEPATH + "/plan/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void deletePlan(@NotNull Long id) {
		try {
			Plan existent = this.bs.exists(id, Plan.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			if(this.sbs.listLevelsInstance(existent, null).getList().size()>0){
				this.fail("Impossível excluir plano de metas com níveis filhos");
			}else{			
				existent.setDeleted(true);
				List<StructureLevelInstance> planStructures = this.sbs.listLevelInstanceByPlan(existent);
				for(StructureLevelInstance structure : planStructures){
					structure.setDeleted(true);
					this.sbs.persist(structure);
				}
				this.bs.persist(existent);
				this.success();
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Duplica plano macro.
	 * 
	 * @param macro
	 *            Plano macro a ser duplicado.
	 * @param keepPlanLevel
	 *            Flag para manter estrutura do plano.
	 * @param keepPlanContent
	 *            Flag para manter conteúdo do plano.
	 * @param keepDocSection
	 *            Flag para manter seções do documento.
	 * @param keepDocContent
	 *            Flag para manter conteúdo das seções do documento.
	 * @return PlanMacro Retorna o plano macro duplicado.
	 */
	@Post(BASEPATH + "/planmacro/duplicate")
	@Consumes
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePlanMacroPermission.class })
	public void duplicate(@NotNull PlanMacro macro, Boolean keepPlanLevel, Boolean keepPlanContent,
			Boolean keepDocSection, Boolean keepDocContent) {
		try {
			PlanMacro existent = this.bs.retrievePlanMacroById(macro.getId());
			if (existent == null) {
				this.fail("Plano inexistente.");
				return;
			} else {
				macro.setCompany(existent.getCompany());
				macro.setId(null);
				macro.setDocumented(existent.isDocumented());
				macro = this.bs.duplicatePlanMacro(macro);
				
				if (macro != null) {
					
					if (keepPlanLevel) {
						if (this.bs.duplicatePlanLevel(existent, macro, sbs, keepPlanContent)) {
							LOGGER.info("Níveis do plano duplicados com sucesso.");
						} else {
							this.fail("Não foi possível duplicar os níveis do plano.");
						}
					}
					if (keepDocSection) {
						if (this.dbs.duplicateDocument(existent, macro, keepDocContent)) {
							LOGGER.info("Seções do documento duplicados com sucesso.");
						} else {
							this.fail("Não foi possível duplicar as seções do documento.");
						}
					}
				} else {
					this.fail("Não foi possível duplicar o plano.");
				}

				/*
				 * CompanyUser companyUser =
				 * this.ubs.retrieveCompanyUser(this.userSession.getUser(),
				 * this.domain.getCompany()); if
				 * (companyUser.getNotificationSetting() ==
				 * NotificationSetting.DEFAULT.getSetting() || companyUser
				 * .getNotificationSetting() ==
				 * NotificationSetting.DO_NOT_RECEIVE_EMAIL.getSetting()) {
				 * this.notificationBS.sendNotification(NotificationType.
				 * PLAN_MACRO_CREATED, macro.getName(), null, null, url); } else
				 * if (companyUser.getNotificationSetting() ==
				 * NotificationSetting.RECEIVE_ALL_BY_EMAIL .getSetting()) {
				 * this.notificationBS.sendNotification(NotificationType.
				 * PLAN_MACRO_CREATED, macro.getName(), null, null, url);
				 * this.notificationBS.sendNotificationEmail(NotificationType.
				 * PLAN_MACRO_CREATED, macro.getName(), null, null, url); }
				 */
				this.success(macro);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Arquiva plano macro.
	 * 
	 * @param id
	 *            Id do plano macro a ser arquivado.
	 * @return
	 * 
	 */
	@Post(BASEPATH + "/planmacro/archive")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void archivePlan(Long id) {
		try {
			PlanMacro plan = this.bs.exists(id, PlanMacro.class);
			if (GeneralUtils.isInvalid(plan)) {
				this.result.notFound();
				return;
			}
			plan.setArchived(true);
			this.bs.persist(plan);
			this.success(plan);

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Desarquiva plano macro.
	 * 
	 * @param id
	 *            Id do plano macro a ser desarquivado.
	 * @return
	 * 
	 */
	@Post(BASEPATH + "/planmacro/unarchive")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void unarchivePlan(Long id) {
		try {
			PlanMacro plan = this.bs.exists(id, PlanMacro.class);
			if (GeneralUtils.isInvalid(plan)) {
				this.result.notFound();
				return;
			}
			plan.setArchived(false);
			this.bs.persist(plan);
			this.success(plan);

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Agenda um recálculo das médias dos planos.
	 * 
	 * @param id
	 *            Id do plano macro.
	 * @return
	 * 
	 */
	@Post(BASEPATH + "/planmacro/{id}/schedule-recalculation")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManagePlanPermission.class })
	public void scheduleRecalculation(Long id) {
		try {
			PlanMacro plan = this.bs.exists(id, PlanMacro.class);
			if (GeneralUtils.isInvalid(plan)) {
				this.result.notFound();
				return;
			}
			this.bs.scheduleRecalculation(plan);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

}
