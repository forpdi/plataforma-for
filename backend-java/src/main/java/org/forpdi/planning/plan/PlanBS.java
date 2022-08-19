package org.forpdi.planning.plan;

import java.time.Year;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.core.company.Company;
import org.forpdi.planning.document.bootstrap.ForpladHelper;
import org.forpdi.planning.jobs.OnLevelInstanceUpdateTask;
import org.forpdi.planning.structure.Structure;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@RequestScoped
public class PlanBS extends HibernateBusiness {

	private static final int PAGESIZE = 50;
	
	@Inject private OnLevelInstanceUpdateTask levelInstanceUpdateTask;

	/**
	 * Cria plano macro.
	 * @param plan
	 * 			Plano macro a ser criado.
	 */
	public void savePlanMacro(PlanMacro plan) {
		this.persist(plan);
		ForpladHelper documentCreator = new ForpladHelper(this.dao);
		documentCreator.initializeDocument(plan);
	}
	
	/**
	 * Lista os planos macro de uma companhia.
	 * @param company
	 * 			Companhia da qual se deseja obter os planos macro.
	 * @param archived
	 * 			Filtro para arquivado ou não (true ou false). 
	 * @param page
	 * 			Número da página da lista a ser acessada. 
	 * @return PaginatedList<PlanMacro>
	 * 			Lista de planos macro.
	 */
	public PaginatedList<PlanMacro> listMacros(Company company, boolean archived, Integer page) {
		PaginatedList<PlanMacro> results = new PaginatedList<PlanMacro>();
		Criteria criteria = this.dao.newCriteria(PlanMacro.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("archived", archived)).add(Restrictions.eq("company", company))
				.addOrder(Order.desc("begin")).addOrder(Order.desc("end")).addOrder(Order.asc("name"));
		Criteria count = this.dao.newCriteria(PlanMacro.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("archived", archived)).add(Restrictions.eq("company", company))
				.setProjection(Projections.countDistinct("id"));
		
		if(page != null){
			criteria.setMaxResults(PAGESIZE).setFirstResult(page * PAGESIZE);
		}
		
		results.setList(this.dao.findByCriteria(criteria, PlanMacro.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	
	/**
	 * Lista os planos macro de uma companhia.
	 * @param company
	 * 			Companhia da qual se deseja obter os planos macro.
	 * 
	 * @return List<PlanMacro>
	 * 			Lista de planos macro.
	 */
	public List<PlanMacro> listAllMacros(Company company) {
		Criteria criteria = this.dao.newCriteria(PlanMacro.class)
			.add(Restrictions.eq("company", company))
		;
		return this.dao.findByCriteria(criteria, PlanMacro.class);
	}
	

	/**
	 * Retorna umna lista de planos macro pelos ids.
	 * @param ids
	 * 			Ids do planos macro a serem retornados
	 * @return plans
	 * 			Planos macro.
	 */
	public List<PlanMacro> listPlanMacrosByIds(List<Long> ids) {
		if (GeneralUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		
		Criteria criteria = this.dao.newCriteria(PlanMacro.class);
		criteria.add(Restrictions.in("id", ids))
			.add(Restrictions.eq("deleted", false));
		
		return dao.findByCriteria(criteria, PlanMacro.class);
	}

	/**
	 * Lista planos de meta de um plano macro.
	 * @param macro
	 * 			Plano macro do qual se deseja obter os planos de meta.
	 * @param archived
	 * 			Filtro para arquivado ou não (true ou false). 
	 * @param page
	 * 			Número da página da lista a ser acessada. 
	 * @param terms
	 * 			Termos da pesquisa para filtrar. Utilizado no nome e descrição dos planos.
	 * @param subPlansSelect
	 * 			Subconjunto de planos a ser pesquisados.
	 * @param ordResult
	 * 			Ordenação, 1 para crescente e 2 para decrescente.
	 * @return PaginatedList<Plan>
	 * 			Lista de planos de metas.
	 */
	public PaginatedList<Plan> listPlans(PlanMacro macro, boolean archived, int page, String terms,
			Long subPlansSelect[], int ordResult) {
		PaginatedList<Plan> results = new PaginatedList<Plan>();
		Criteria criteria = this.dao.newCriteria(Plan.class);
		if (page > 0) {
			criteria.setFirstResult(page * PAGESIZE).setMaxResults(PAGESIZE);
		}
		criteria.add(Restrictions.eq("deleted", false)).add(Restrictions.eq("archived", archived))
				.add(Restrictions.eq("parent", macro)).addOrder(Order.desc("begin")).addOrder(Order.desc("end"))
				.addOrder(Order.asc("name"));	
		
		Criteria count = this.dao.newCriteria(Plan.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("archived", archived)).add(Restrictions.eq("parent", macro))
				.setProjection(Projections.countDistinct("id"));

		if (terms != null) {
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.like("name", "%" + terms + "%").ignoreCase());
			or.add(Restrictions.like("description", "%" + terms + "%").ignoreCase());
			criteria.add(or);

			or = Restrictions.disjunction();
			or.add(Restrictions.like("name", "%" + terms + "%").ignoreCase());
			or.add(Restrictions.like("description", "%" + terms + "%").ignoreCase());
			count.add(or);
		}
		if (subPlansSelect != null) {
			Disjunction or = Restrictions.disjunction();
			for (int i = 0; i < subPlansSelect.length; i++) {
				or.add(Restrictions.eq("id", subPlansSelect[i]));
			}
			criteria.add(or);
		}
		if (ordResult == 1) {
			criteria.addOrder(Order.asc("creation"));
		} else if (ordResult == 2) {
			criteria.addOrder(Order.desc("creation"));
		}

		results.setList(this.dao.findByCriteria(criteria, Plan.class));
		results.setTotal((Long) count.uniqueResult());
		return results;
	}
	
	public List<Plan> listAllPlansForPlansMacro(List<PlanMacro> plansMacro) {
		if (GeneralUtils.isEmpty(plansMacro)) {
			return Collections.emptyList();
		}
		if (GeneralUtils.isEmpty(plansMacro))
			return Collections.emptyList();
		Criteria criteria = this.dao.newCriteria(Plan.class)
			.add(Restrictions.in("parent", plansMacro))
		;
		return this.dao.findByCriteria(criteria, Plan.class);
	}
	
	public List<Plan> listPlansForPlanMacro(PlanMacro macro) {
		Criteria criteria =
			this.dao.newCriteria(Plan.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("archived", false))
			.add(Restrictions.eq("parent", macro))
			.addOrder(Order.desc("begin"))
			.addOrder(Order.desc("end"))
			.addOrder(Order.asc("name"))
		;	
		return this.dao.findByCriteria(criteria, Plan.class);
	}

	/**
	 * Retorna o plano de metas pelo id.
	 * @param id
	 * 			Id do plano de metas a ser retornado.
	 * @return Plan
	 * 			Plano de metas.
	 */
	public Plan retrieveById(Long id) {
		Criteria criteria = this.dao.newCriteria(Plan.class);
		criteria.add(Restrictions.eq("id", id));
		Plan plan = (Plan) criteria.uniqueResult();
		return plan;
	}

	/**
	 * Retorna o plano macro pelo id.
	 * @param id
	 * 			Id do plano macro a ser retornado.
	 * @return Plan
	 * 			Plano macro.
	 */
	public PlanMacro retrievePlanMacroById(Long id) {
		Criteria criteria = this.dao.newCriteria(PlanMacro.class);
		criteria.add(Restrictions.eq("id", id));
		return (PlanMacro) criteria.uniqueResult();
	}
	
	/**
	 * Duplicar plano macro.
	 * @param planMacro
	 * 			Plano macro a ser duplicado.
	 * @return PlanMacro
	 * 			Cópia do plano macro passado por parâmetro.
	 */
	public PlanMacro duplicatePlanMacro(PlanMacro planMacro) {
		try {
			this.persist(planMacro);
			return planMacro;
		} catch (Exception e) {
			LOGGER.error("Unexpected runtime error", e);
			return null;
		}
	}
	
	/**
	 * Duplicar level do plano.
	 * @param macro
	 * 			Plano macro existente a ser duplicado.
	 * @param copy
	 * 			Plano macro que receberá a cópia.
	 * @param sbs
	 * 			Level a ser duplicado.
	 * @param keepContent
	 * 			Flag para determinar se o conteúdo será duplicado também.
	 * @return boolean
	 * 			Retorna true se o level foi duplicado com sucesso, caso contrário retorna false.
	 */
	public boolean duplicatePlanLevel(PlanMacro macro, PlanMacro copy, StructureBS sbs, Boolean keepContent) {
		try {
			PaginatedList<Plan> list = this.listPlans(macro, false, 0, null, null, 1);
			for (Plan plan : list.getList()) {
				Plan clone = new Plan();
				clone.setBegin(plan.getBegin());
				clone.setDescription(plan.getDescription());
				clone.setEnd(plan.getEnd());
				clone.setName(plan.getName());
				Structure structure = this.exists(plan.getStructure().getId(), Structure.class);
				structure.setLevels(sbs.listStructureLevels(structure));
				clone.setStructure(structure);
				clone.setParent(copy);
				clone.setId(null);
				clone.setDeleted(false);
				clone.setUpdated(plan.isUpdated());
				clone.setArchived(false);
				clone.setUpdated(plan.isUpdated());
				clone.setAuxValue(plan.getAuxValue());
				this.persist(clone);

				if(sbs.duplicatePlanLevelInstance(plan, clone, keepContent)){
					LOGGER.info("Plano "+plan.getName()+" duplicado com sucesso");
				} else {
					LOGGER.info("Não foi possível duplicar o plano "+plan.getName());
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Unexpected runtime error", e);
			return false;
		}
	}
	
	/**
	 * Agenda um recálculo das médias dos planos.
	 * @param planMacro
	 * 			Plano macro existente.
	 * @return
	 * 			
	 */
	public void scheduleRecalculation(PlanMacro planMacro) {
		Criteria criteria =
			this.dao.newCriteria(StructureLevelInstance.class)
			.createAlias("plan", "plan", JoinType.INNER_JOIN)
			.createAlias("level", "level", JoinType.INNER_JOIN)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("level.goal", true))
			.add(Restrictions.eq("plan.parent", planMacro))
		;
		List<StructureLevelInstance> goals = this.dao.findByCriteria(criteria, StructureLevelInstance.class);
		if (!GeneralUtils.isEmpty(goals)) {
			this.levelInstanceUpdateTask.add(goals);
		}
	}
	
	/**
	 * Listar os planos detalhados
	 * 
	 * @param plan
	 *            Plano de metas.
	 * @return query Lista dos planos.
	 */
	public List<PlanDetailed> listPlansDetailed(Plan plan) {
		Criteria criteria = this.dao.newCriteria(PlanDetailed.class)
				.add(Restrictions.eq("plan", plan))
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("year", Year.now().getValue()))
		;
		return this.dao.findByCriteria(criteria, PlanDetailed.class);
	}
	
	/**
	 * Listar os planos detalhados
	 * 
	 * @param plans
	 *            Plano de metas.
	 * @return query Lista dos planos.
	 */
	public List<PlanDetailed> listAllPlansDetailed(List<Plan> plans) {
		if (GeneralUtils.isEmpty(plans)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(PlanDetailed.class);
		criteria.add(Restrictions.in("plan", plans));
		return this.dao.findByCriteria(criteria, PlanDetailed.class);
	}

}