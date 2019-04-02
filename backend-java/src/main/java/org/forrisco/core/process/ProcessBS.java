package org.forrisco.core.process;



import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.forrisco.core.plan.PlanRisk;
import org.forrisco.core.process.ProcessUnit;
import org.forrisco.core.unit.Unit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;


/**
 * @author Matheus Nascimento
 */
@RequestScoped
public class ProcessBS extends HibernateBusiness {
	
	
	
	/**
	 * Salvar um novo processo
	 * 
	 * @param Process,
	 *			instância do processo
	 * @throws Exception 
	 *            
	 */
	public void save(Process process) throws Exception {
		process.setDeleted(false);
		
		if(process.getFileLink() != null && process.getFile()==null||
				process.getFileLink() ==null && process.getFile() !=null) {
			throw new Exception("Link do arquivo inconsistente");
		}
		this.persist(process);	
	}
	
	/**
	 * Salvar um novo processounit
	 * 
	 * @param ProcessUnit,
	 *			instância do processounit
	 *            
	 */
	public void save(ProcessUnit processunit) {
		processunit.setDeleted(false);
		this.persist(processunit);	
	}
	
	
	/**
	 * Recuperar process de uma unidade
	 * 
	 * @param Unit,
	 *            instância da unidade
	 *
	 */
	public PaginatedList<Process> listProcessByUnit(Unit unit) {
		
		PaginatedList<Process> results = new PaginatedList<Process>();
		List<Process> list = new LinkedList<Process>();
		
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));

		for(ProcessUnit processUnit : this.dao.findByCriteria(criteria, ProcessUnit.class)){
			Process process = processUnit.getProcess();
			List<Unit> relatedUnits = this.listRelatedUnits(process,unit.getPlanRisk());
			process.setRelatedUnits(relatedUnits);
			list.add(process);
		}
		results.setList(list);
		results.setTotal((long) list.size());
	 
		return results;
	}

	public PaginatedList<Process> listProcessByUnit(Unit unit, Integer page, Integer pageSize) {
		PaginatedList<Process> results = new PaginatedList<Process>();
		List<Process> list = new LinkedList<Process>();
		
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit))
				.setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize);

		Criteria count = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit))
				.setProjection(Projections.countDistinct("id"));
		
		for(ProcessUnit processUnit : this.dao.findByCriteria(criteria, ProcessUnit.class)){
			Process process = processUnit.getProcess();
			List<Unit> relatedUnits = this.listRelatedUnits(process,unit.getPlanRisk());
			process.setRelatedUnits(relatedUnits);
			list.add(process);
		}
		results.setList(list);
		results.setTotal((Long) count.uniqueResult());
	 
		return results;
	}

	public List<Unit> listRelatedUnits(Process process, PlanRisk planRisk) {
		List<Unit> units = new LinkedList<Unit>();
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.createAlias("unit", "unit")
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("process", process))
				.add(Restrictions.eq("unit.planRisk", planRisk));
			
		for (ProcessUnit processUnit : this.dao.findByCriteria(criteria, ProcessUnit.class)) {
			Unit unit = processUnit.getUnit();
			//unit.setUser(null);
			units.add(unit);
		}
		return units;
	}

	public List<ProcessUnit> getProcessUnitsByProcess(Process process) {
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("process", process));
		return this.dao.findByCriteria(criteria, ProcessUnit.class);
	}	
	
	/**
	 * Retorna todos os processo da instituição
	 * 
	 *@Param CompanyDomain instituição
	 *
	 * @return PaginatedList<Process>  lista de processos
	 */
	public PaginatedList<Process> listProcessByCompany(Company company) {
		
		Criteria criteria = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("company", company));

		Criteria count = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("company", company))
				.setProjection(Projections.countDistinct("id"));
		
		PaginatedList<Process> results = new PaginatedList<Process>();
		results.setList(this.dao.findByCriteria(criteria, Process.class));
		results.setTotal((Long) count.uniqueResult());
	 
		return results;
	}
	
	/**
	 * Retorna todos os processo do plano de risco
	 * 
	 * @Param PlanRisk plano
	 *
	 * @return PaginatedList<Process>  lista de processos
	 */
	public PaginatedList<Process> listProcessByPlan(PlanRisk planRisk) {
		
		Criteria criteria = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false))
				.createAlias("unitCreator","unit")
				.add(Restrictions.eq("unit.planRisk",planRisk));

		Criteria count = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false))
				.createAlias("unitCreator","unit")
				.add(Restrictions.eq("unit.planRisk",planRisk))
				.setProjection(Projections.countDistinct("id"));
		
		PaginatedList<Process> results = new PaginatedList<Process>();
		results.setList(this.dao.findByCriteria(criteria, Process.class));
		results.setTotal((Long) count.uniqueResult());
		
		return results;
	}


	
	

	public void deleteProcess(Process process) {
			
		process.setDeleted(true);
		this.persist(process);
		List<ProcessUnit> processUnits = this.getProcessUnitsByProcess(process);
		for (ProcessUnit processUnit : processUnits) {
			processUnit.setDeleted(true);
			this.persist(processUnit);
		}

	}




}
