package org.forrisco.core.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.jobs.EmailSenderTask;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserRecoverRequest;
import org.forrisco.core.unit.Unit;
import org.forrisco.risk.RiskBS;

import com.google.gson.GsonBuilder;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
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
	@Inject private NotificationBS notificationBS;
	
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
				for (Unit singleunit : process.getRelatedUnits()) {
					
					Unit relatedUnit = this.processBS.exists(singleunit.getId(), Unit.class);
					
					if (relatedUnit != null && !relatedUnit.isDeleted()) {

						processUnit = new ProcessUnit();
						processUnit.setProcess(process);
						processUnit.setUnit(relatedUnit);
						this.processBS.save(processUnit);
						
						User user=relatedUnit.getUser();
						
						//enviar email de notificação de criação de processo(unidade relacionada)
						String texto="Prezado(a) "+user.getName()+
								", A sua unidade ["+relatedUnit.getName()+"] foi relacionada ao Processo ["+process.getName()+
								"] com o objetivo ["+process.getObjective()+"] da unidade ["+unit.getName()+"]. O responsável"+
								" por essa unidade é o(a) "+unit.getUser().getName()+
								". Segue em anexo o Processo na qual a sua unidade foi relacionada.";						
						
						String url=this.domain.getBaseUrl()+"/#/forrisco/plan-risk/"+String.valueOf(relatedUnit.getPlan().getId())+"/unit/"+String.valueOf(relatedUnit.getId())+"/info";
		
						try {
								this.notificationBS.sendAttachedNotificationEmail(NotificationType.FORRISCO_PROCESS_CREATED, texto, "aux", user, url, process.getFile());
						} catch (Throwable ex) {
							LOGGER.errorf(ex, "Unexpected error occurred.");
							this.fail(ex.getMessage());
						}
							
					}
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
	 * Alterar um processo
	 * 
	 * @param Process
	 *            processo a ser alterado no banco de dados
	 */
	@Put(PATH)
	@Consumes
	@NoCache
	public void updateProcess(Process process) {
		try {
			Process existent = this.processBS.exists(process.getId(), Process.class);
			if (existent == null) {
				this.fail("Processo não encontrado.");
				return;
			}
			List<ProcessUnit> processUnitsExistent = this.processBS.getProcessUnitsByProcess(process);
			// mapeia todas as processUnits
			Map<Long, ProcessUnit> processUnitsExistentMap = new HashMap<>();
			for (ProcessUnit processUnit : processUnitsExistent) {
				processUnitsExistentMap.put(processUnit.getUnit().getId(), processUnit);
			}
			List<Unit> relatedUnits = process.getRelatedUnits();
			List<ProcessUnit> newProcessUnits = new LinkedList<>();
			// verifica as unidades que foram vinculadas e as que foram desvinculadas 
			for (Unit unit : relatedUnits) {
				ProcessUnit processUnit = processUnitsExistentMap.get(unit.getId());
				if (processUnit == null) {
					processUnit = new ProcessUnit();
					processUnit.setProcess(process);
					processUnit.setUnit(unit);
					newProcessUnits.add(processUnit);
				} else if (processUnit.isDeleted()) {
					processUnit.setDeleted(false);
					newProcessUnits.add(processUnit);
				} else {
					processUnitsExistentMap.remove(unit.getId());
				}
			}
			// persiste as processUnits
			for (ProcessUnit processUnit : newProcessUnits) {
				this.processBS.persist(processUnit);
			}
			for (ProcessUnit processUnit : processUnitsExistentMap.values()) {
				EmailSenderTask.LOG.info(new GsonBuilder().setPrettyPrinting().create().toJson(processUnit));
				if (processUnit.getUnit().getId() != process.getUnit().getId()) {
					processUnit.setDeleted(true);
					this.processBS.persist(processUnit);
				}
			}
			// atualiza e persiste o processo
			existent.setFileLink(process.getFileLink());
			//existent.setFileName(process.getFileName());
			existent.setFile(process.getFile());
			existent.setName(process.getName());
			existent.setObjective(process.getObjective());
			this.processBS.persist(existent);
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
