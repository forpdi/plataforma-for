package org.forrisco.core.process;



import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.forpdi.core.company.CompanyDomain;
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
	 *            
	 */
	public void save(Process process) {
		process.setDeleted(false);
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
	public PaginatedList<Process> listProcessbyUnit(Unit unit) {
		
		PaginatedList<Process> results = new PaginatedList<Process>();
		List<Process> list = new LinkedList<Process>();
		
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("unit", unit));

//		Criteria count = this.dao.newCriteria(ProcessUnit.class)
//				.add(Restrictions.eq("deleted", false))
//				.add(Restrictions.eq("unit", unit))
//				.setProjection(Projections.countDistinct("id"));
		
		for(ProcessUnit processUnit : this.dao.findByCriteria(criteria, ProcessUnit.class)){
			Process process = processUnit.getProcess();
			List<Unit> relatedUnits = this.listRelatedUnits(process);
			process.setRelatedUnits(relatedUnits);
			list.add(process);
		}
		results.setList(list);
		results.setTotal((long) list.size());
	 
		return results;
	}

	public List<Unit> listRelatedUnits(Process process) {
		List<Unit> units = new LinkedList<Unit>();
		Criteria criteria = this.dao.newCriteria(ProcessUnit.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("process", process));
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
	
	public PaginatedList<Process> listProcessbyCompany(CompanyDomain domain) {
		
		Criteria criteria = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("company", domain));

		Criteria count = this.dao.newCriteria(Process.class)
				.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("company", domain))
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
