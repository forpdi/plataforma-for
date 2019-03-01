package org.forrisco.core.process;

import java.util.List;

import javax.inject.Inject;


import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.RiskBS;

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
public class ProcessController extends AbstractController{

	@Inject @Current private CompanyDomain domain;
	@Inject private ProcessBS processBS;
	@Inject private RiskBS riskBS;
	
	protected static final String PATH =  BASEPATH +"/process";


	
	
	/**
	 * Salvar um processo em uma unidade.
	 * 
	 * @param Process
	 *            processo a ser salvo no banco de dados
	 * @return Process
	 */
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	public void saveProcess(Process process) {
		try {
 			Unit unit = this.processBS.exists(process.getUnit().getId(), Unit.class);
			if (unit == null) {
				this.fail("Unidade não encontrada.");
				return;
			}
			if(this.domain == null) {
				this.fail("Instituição não definida");
				return;
			}
			process.setCompany(this.domain.getCompany());
			
			this.processBS.save(process);

			ProcessUnit processUnit = new ProcessUnit();
			processUnit.setProcess(process);
			processUnit.setUnit(unit);
			this.processBS.save(processUnit);

			if (process.getRelatedUnits() != null) {
				for (Unit relatedUnit : process.getRelatedUnits()) {
					processUnit = new ProcessUnit();
					processUnit.setProcess(process);
					processUnit.setUnit(relatedUnit);
					this.processBS.save(processUnit);
				}
			}

			this.success(process);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna Processos de uma unidade.
	 * 
	 * @param Unit
	 *            Id da unidade.
	 * @return <PaginatedList> Process
	 */
	@Get( PATH + "/{id}")
	@NoCache
	public void listProcesses(Long id) {
		try {
			Unit unit = this.processBS.exists(id, Unit.class);
			PaginatedList<Process> process= this.processBS.listProcessbyUnit(unit);
			this.success(process);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Deleta um processos
	 * 
	 * @param id
	 *            Id do processo
	 */
	@Delete( PATH + "/{id}")
	@NoCache
	public void deleteProcesses(Long id) {
		try {
			Process process = this.processBS.exists(id, Process.class);
			if (process == null) {
				this.fail("Processo não encontrado.");
				return;
			}
			
			if (this.riskBS.hasLinkedRiskProcess(process)) {
				this.fail("Processo vinculado a um Risco. É necessário deletar a vinculação no Risco para depois excluir o processo.");
				return;
			}
			if (this.riskBS.hasLinkedRiskActivity(process)) {
				this.fail("Processo vinculado a um Risco. É necessário deletar a vinculação no Risco para depois excluir o processo.");
				return;
			}
			
			this.processBS.deleteProcess(process);
				
			this.success();

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}	

	/**
	 * Retorna Processos da instituição atual
	 * 
	 * @return <PaginatedList> Process
	 */
	@Get( PATH + "")
	@NoCache
	public void listAllProcesses() {
		try {
			if(this.domain == null) {
				this.fail("Instituição não definida");
				return;
			}
			PaginatedList<Process> process= this.processBS.listProcessbyCompany(this.domain);
			this.success(process);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
}
