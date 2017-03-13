package org.forpdi.planning.fields;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.fields.attachment.Attachment;
import org.forpdi.planning.fields.budget.Budget;
import org.forpdi.planning.fields.budget.BudgetDTO;
import org.forpdi.planning.fields.budget.BudgetSimulationDB;
import org.forpdi.planning.fields.schedule.Schedule;
import org.forpdi.planning.fields.schedule.ScheduleInstance;
import org.forpdi.planning.fields.schedule.ScheduleStructure;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.fields.table.TableInstance;
import org.forpdi.planning.fields.table.TableStructure;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@Controller
public class FieldsController extends AbstractController {

	@Inject
	private FieldsBS bs;
	@Inject
	private StructureBS structureBs;

	/**
	 * Salvar um novo orçamento no banco de dados, relacionando a uma instancia
	 * de nível.
	 * 
	 * @param name,
	 *            nome do orçamento
	 * @param subAction,
	 *            sub ação orçamentaria
	 * @param instanceId,
	 *            id da instancia de nível
	 * 
	 * @return item, dto do orçamento salvo, com os valores conforme a sub ação
	 */
	@Post(BASEPATH + "/field/budget")
	@Consumes
	@NoCache
	@Permissioned
	public void save(@NotEmpty String name, @NotEmpty String subAction, @NotNull Long instanceId) {
		try {
			StructureLevelInstance instance = this.structureBs.retrieveLevelInstance(instanceId);
			if (instance == null) {
				this.fail("Estrutura inválida!");
				return;
			}
			Budget budget = new Budget();
			budget.setLevelInstance(instance);
			budget.setName(name);
			budget.setSubAction(subAction);
			BudgetSimulationDB simulation = this.bs.retrieveBudgetSimulation(subAction);
			if (simulation == null) {
				this.fail("Sub-ação inválida!");
				return;
			}
			this.bs.saveBudget(budget);
			BudgetDTO item = new BudgetDTO();
			item.setBudget(budget);
			item.setCommitted(simulation.getCommitted());
			item.setConducted(simulation.getConducted());
			item.setPlanned(simulation.getPlanned());
			this.success(item);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualizar um orçamento existente no banco de dados.
	 * 
	 * @param name,
	 *            novo nome do orçamento
	 * @param subAction,
	 *            nova sub ação orçametaria
	 * @param id,
	 *            referente ao orçamento existente
	 * 
	 * @return item, dto do orçamento atualizado
	 */
	@Post(BASEPATH + "/field/budget/update")
	@Consumes
	@NoCache
	@Permissioned
	public void update(@NotEmpty String name, @NotEmpty String subAction, @NotNull Long id) {
		try {
			Budget budget = this.bs.budgetExistsById(id);
			if (budget == null) {
				LOGGER.error("Orçamento inexistente para ser editado.");
				this.fail("Oçamento inválido.");
			}

			BudgetSimulationDB simulation = this.bs.retrieveBudgetSimulation(subAction);
			if (simulation == null) {
				LOGGER.error("Não existe ação orçamentária!");
				this.fail("Não existe ação orçamentária!");
			} else {
				budget.setName(name);
				budget.setSubAction(subAction);
				this.bs.update(budget);
				BudgetDTO item = new BudgetDTO();
				item.setBudget(budget);
				item.setCommitted(simulation.getCommitted());
				item.setConducted(simulation.getConducted());
				item.setPlanned(simulation.getPlanned());

				this.success(item);
			}

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Excluir um orçamento existente no banco de dados
	 * 
	 * @param id,
	 *            referente ao orçamento a ser excluido
	 * @return budget, orçamento excluído
	 */
	@Post(BASEPATH + "/field/budget/delete")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteBdget(@NotNull Long id) {
		try {
			Budget budget = this.bs.budgetExistsById(id);
			this.bs.deleteBudget(budget);
			this.success(budget);
		} catch (Throwable e) {
			LOGGER.error("Erro ao deletar o orçamento", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar um novo cronograma.
	 * 
	 * @param scheduleInstance,
	 *            instancia do cronograma a ser salva
	 * @param scheduleId,
	 *            id do atributo cronograma
	 * @param beginDate,
	 *            data de início
	 * @param endDate,
	 *            data de fim
	 * @return existentScheduleInstance, instancia do cronograma que foi salvo
	 */
	@Post(BASEPATH + "/field/schedule")
	@Consumes
	@NoCache
	@Permissioned
	public void saveSchedule(ScheduleInstance scheduleInstance, Long scheduleId, String beginDate, String endDate) {
		try {
			if (beginDate != null && endDate != null) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				scheduleInstance.setBegin((Date) formatter.parse(beginDate));
				scheduleInstance.setEnd((Date) formatter.parse(endDate));
			} else {
				this.fail("As datas devem ser preenchidas!");
			}
			Schedule schedule = this.bs.retrieveSchedule(scheduleId);
			if (schedule == null) {
				this.fail("Cronograma inválido!");
				return;
			}
			ScheduleInstance existentScheduleInstance = new ScheduleInstance();
			if (scheduleInstance.getId() != null && scheduleInstance.getId() != 0)
				existentScheduleInstance = this.bs.retrieveScheduleInstance(scheduleInstance.getId());
			else
				existentScheduleInstance.setNumber(this.bs.getScheduleInstanceNumber(schedule));
			if (scheduleInstance.getDescription().length() > 4000) {
				this.fail("Texto muito longo para descrição (Limite 4000 caracteres)");
				return;
			}
			existentScheduleInstance.setDescription(scheduleInstance.getDescription());
			existentScheduleInstance.setBegin(scheduleInstance.getBegin());
			existentScheduleInstance.setEnd(scheduleInstance.getEnd());
			existentScheduleInstance.setPeriodicity(scheduleInstance.getPeriodicity());
			existentScheduleInstance.setSchedule(schedule);
			this.bs.persist(existentScheduleInstance);
			this.bs.saveScheduleValues(existentScheduleInstance, scheduleInstance.getScheduleValues());
			existentScheduleInstance.setScheduleValues(scheduleInstance.getScheduleValues());
			this.success(existentScheduleInstance);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Deletar um cronograma do banco de dados
	 * 
	 * @param id,
	 *            referente ao cronograma a ser excluído
	 * @return scheduleInstance, que foi excluído do banco de dados
	 */
	@Post(BASEPATH + "/field/schedule/delete")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteScheduleInstance(@NotNull Long id) {
		try {
			ScheduleInstance scheduleInstance = this.bs.retrieveScheduleInstance(id);
			this.bs.deleteScheduleInstance(scheduleInstance);
			this.success(scheduleInstance);
		} catch (Throwable e) {
			LOGGER.error("Erro ao deletar o orçamento", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar um novo campo de um dado atributo tabela
	 * 
	 * @param tableInstance,
	 *            instancia da tabela que será inserido o campo
	 * @param tableFieldsId,
	 *            id do campo da tabela
	 * @return existentTableInstance, instancia do campo da tabela que foi salvo
	 *         no banco
	 */
	@Post(BASEPATH + "/field/tableFields")
	@Consumes
	@NoCache
	@Permissioned
	public void saveTableFields(TableInstance tableInstance, Long tableFieldsId) {
		try {
			TableFields tableFields = this.bs.retrieveTableFields(tableFieldsId);
			if (tableFields == null) {
				this.fail("Tabela inválida!");
				return;
			}
			TableInstance existentTableInstance = new TableInstance();
			if (tableInstance.getId() != null && tableInstance.getId() != 0)
				existentTableInstance = this.bs.retrieveTableInstance(tableInstance.getId());
			existentTableInstance.setTableFields(tableFields);
			this.bs.persist(existentTableInstance);
			this.bs.saveTableValues(existentTableInstance, tableInstance.getTableValues());
			existentTableInstance.setTableValues(tableInstance.getTableValues());
			existentTableInstance.setTableFieldsId(tableFieldsId);
			this.success(existentTableInstance);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar colunas de um atributo tabela
	 * 
	 * @param tableFields,
	 *            tabela a ser inserida essa coluna
	 * @return tableFields
	 */
	@Post(BASEPATH + "/field/tableFields/structures")
	@Consumes
	@NoCache
	@Permissioned
	public void saveTableColumns(TableFields tableFields) {
		try {
			if (tableFields == null) {
				this.fail("Tabela não existente.");
				return;
			}
			for (TableStructure structure : tableFields.getTableStructures()) {
				TableStructure structure2 = new TableStructure();
				structure2.setDeleted(false);
				structure2.setInTotal(structure.isInTotal());
				structure2.setLabel(structure.getLabel());
				structure2.setType(structure.getType());
				structure2.setTableFields(tableFields);
				this.bs.persist(structure2);
				structure.setId(structure2.getId());
			}
			this.success(tableFields);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Excluir uma instancia da tabela
	 * 
	 * @param id,
	 *            referente a instance da tabela a ser excluida
	 * @return tableInstance, instancia da tabela que foi excluida
	 */
	@Post(BASEPATH + "/field/tableFields/delete")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteTableInstance(@NotNull Long id) {
		try {
			TableInstance tableInstance = this.bs.retrieveTableInstance(id);
			tableInstance.setTableFieldsId(tableInstance.getTableFields().getId());
			this.bs.deleteTableInstance(tableInstance);
			this.success(tableInstance);
		} catch (Throwable e) {
			LOGGER.error("Erro ao deletar o orçamento", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar um novo plano de ação no banco de dados
	 * 
	 * @param actionPlan,
	 *            plano de ação
	 * @param levelInstanceId,
	 *            id da instancia do nível
	 * @param begin,
	 *            data de inicio em String
	 * @param end,
	 *            data de fim em String
	 * @return actionPlan
	 */
	@Post(BASEPATH + "/field/actionplan")
	@Consumes
	@NoCache
	@Permissioned
	public void saveAction(ActionPlan actionPlan, Long levelInstanceId, String begin, String end, String url) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		try {
			actionPlan.setBegin(formato.parse(begin));
			actionPlan.setEnd(formato.parse(end));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			LOGGER.error("Erro ao converter data do action plan", e1);
			e1.printStackTrace();
		}

		StructureLevelInstance levelInstance = this.structureBs.retrieveLevelInstance(levelInstanceId);
		if (levelInstance == null) {
			this.fail("Estrutura inválida!");
			return;
		}
		actionPlan.setLevelInstance(levelInstance);

		try {
			this.bs.saveActionPlan(actionPlan, url);
			this.success(actionPlan);
		} catch (Throwable e) {
			LOGGER.error("Erro ao salvar o plano de ação no banco", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Excluir um plano de ação do banco de dados
	 * 
	 * @param id,
	 *            referente ao plano de ação a ser excluido
	 * @return actionPlan, plano de ação que foi excluido
	 */
	@Post(BASEPATH + "/field/actionplan/delete")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteActionPlan(@NotNull Long id) {
		try {
			ActionPlan actionPlan = this.bs.actionPlanExistsById(id);
			this.bs.deleteActionPlan(actionPlan);
			this.success(actionPlan);
		} catch (Throwable e) {
			LOGGER.error("Erro ao deletar o plano de ação", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualizar um plano de ação
	 * 
	 * @param id,
	 *            referente ao plano de ação a ser atualizado
	 * @param begin,
	 *            nova data de inicio
	 * @param end,
	 *            nova data de fim
	 * @param checked,
	 *            novo atributo marcado
	 * @param responsible,
	 *            novo responsável
	 * @param description,
	 *            nova descrição
	 * 
	 * @return exist, plano de ação que foi atualizado
	 */
	@Post(BASEPATH + "/field/actionplan/update")
	@Consumes
	@NoCache
	@Permissioned
	public void updateActionPlan(@NotNull Long id, @NotNull String begin, @NotNull String end, @NotNull Boolean checked,
			@NotNull String responsible, @NotNull String description) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			ActionPlan exist = this.bs.actionPlanExistsById(id);
			if (exist == null) {
				this.fail("Não foi possível editar. Ação inexistente.");
				return;
			}
			exist.setId(id);
			exist.setBegin(format.parse(begin));
			exist.setChecked(checked);
			exist.setDescription(description);
			exist.setEnd(format.parse(end));
			exist.setResponsible(responsible);
			this.bs.saveActionPlan(exist, null);
			this.success(exist);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualizar o atributo checado do plano de ação
	 * 
	 * @param id,
	 *            referente ao plano de ação
	 * @param checked,
	 *            atributo checado
	 * 
	 * @return exist, plano de ação atualizado
	 */
	@Post(BASEPATH + "/field/actionplan/update/checkbox")
	@Consumes
	@NoCache
	@Permissioned
	public void checkboxUpdate(@NotNull Long id, @NotNull Boolean checked, String url) {
		try {
			ActionPlan exist = this.bs.actionPlanExistsById(id);

			exist.setChecked(checked);

			this.bs.saveActionPlan(exist, url);
			this.success(exist);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um ao tentar atualizar o checkbox: " + e.getMessage());
		}
	}

	/**
	 * Listar todos os orçamentos de simulação
	 * 
	 * @return list, todos as simulações de orçamento
	 */
	@Get(BASEPATH + "/field/budget/simulation")
	@NoCache
	@Permissioned
	public void listBudgetAction() {
		try {
			PaginatedList<BudgetSimulationDB> list = this.bs.listBudgetSimulation();
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Listar orçamentos referentes à uma instancia de nível
	 * 
	 * @param levelId,
	 *            id da instancia de nível que possui os orçamentos
	 * @return list, lista com os orçamentos daquele instancia de nível
	 */
	@Get(BASEPATH + "/field/budget")
	@NoCache
	@Permissioned
	public void listBudgetByLevelInstance(Long levelId) {
		try {
			StructureLevelInstance levelInstance = this.structureBs.retrieveLevelInstance(levelId);
			PaginatedList<Budget> list = this.bs.listBudgetsByInstance(levelInstance);
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Salvar estrutura de cronograma
	 * 
	 * @param schedule,
	 *            cronograma a ser referido a estrutura
	 * @return schedule
	 */
	@Post(BASEPATH + "/field/schedule/structures")
	@Consumes
	@NoCache
	@Permissioned
	public void saveScheduleStructures(@NotNull Schedule schedule) {
		try {
			if (schedule == null) {
				this.fail("Cronograma não existente.");
				return;
			}
			for (ScheduleStructure structure : schedule.getScheduleStructures()) {
				ScheduleStructure structure2 = new ScheduleStructure();
				structure2.setDeleted(false);
				structure2.setLabel(structure.getLabel());
				structure2.setSchedule(schedule);
				structure2.setType(structure.getType());
				this.bs.persist(structure2);
			}
			this.success(schedule);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Lista com os atributos do plano de ação para serem mostrados
	 * @param id
	 * 			Id do plano
	 * @param page
	 * 			Número da página
	 * @param pageSize
	 * 			Tamanho da página (quantidade de registros por busca)
	 */
	@Get(BASEPATH + "/field/actionplan/listActionPlanAttribute")
	@Consumes
	@NoCache
	@Permissioned
	public void listActionPlanAttribute(Long id, Integer page, Integer pageSize) {

		PaginatedList<ActionPlan> actionPlansPaginated = new PaginatedList<ActionPlan>();
		ArrayList<ActionPlan> actionPlans = new ArrayList<>();

		try {

			StructureLevelInstance levelInstance = this.structureBs.retrieveLevelInstance(id);
			if (levelInstance == null) {
				this.fail("Estrutura incorreta!");
			} else {
				List<Attribute> attributeList = this.structureBs.retrieveLevelAttributes(levelInstance.getLevel());
				PaginatedList<Attribute> attributeListPagined = new PaginatedList<Attribute>();
				attributeListPagined.setList(attributeList);

				attributeListPagined = this.structureBs.setActionPlansAttributes(levelInstance, attributeListPagined,
						page, pageSize);

				for (Attribute attribute : attributeListPagined.getList()) {
					if (attribute.getActionPlans() != null) {
						for (ActionPlan actionPlan : attribute.getActionPlans()) {
							actionPlans.add(actionPlan);
						}
					}
				}

				actionPlansPaginated.setList(actionPlans);
				actionPlansPaginated.setTotal(attributeListPagined.getTotal());

				this.success(actionPlansPaginated.getList(), actionPlansPaginated.getTotal());

			}

		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Salvar anexo
	 * 
	 * @param attachment,
	 *            anexo a ser salvo
	 */
	@Post(BASEPATH + "/attachment")
	@Consumes
	@NoCache
	@Permissioned
	public void saveAttachment(@NotNull Attachment attachment) {
		try {
			this.bs.saveAttachment(attachment);
			this.success(attachment);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Listar anexos de um dado nível
	 * 
	 * @param levelInstance
	 */
	@Get(BASEPATH + "/attachment")
	@NoCache
	@Permissioned
	public void listAttachmentByLevelInstance(Long id, Integer page, Integer pageSize) {
		try {
			StructureLevelInstance levelInstance = this.structureBs.retrieveLevelInstance(id);
			PaginatedList<Attachment> result = this.bs.listAllAttachment(levelInstance, page, pageSize);
			this.success(result);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Deletar anexo de um dado nível
	 * 
	 * @param id,
	 *            referente ao anexo a ser excluido
	 */
	@Post(BASEPATH + "/attachment/delete")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteAttachment(@NotNull Long id) {
		try {
			Attachment attachment = this.bs.retrieveById(id);
			if (this.bs.deleteAttachment(attachment)) {
				this.success(attachment);
			} else {
				this.fail("Não foi possível excluir o anexo.");
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Atualizar o anexo
	 * 
	 * @param attachement,
	 *            anexo a ser atualizado.
	 */
	@Post(BASEPATH + "/attachment/update")
	@Consumes
	@NoCache
	@Permissioned
	public void updateAttachment(@NotNull Attachment attachment) {
		try {
			if (this.bs.updateAttachment(attachment)) {
				this.success(attachment);
			} else {
				this.fail("Não foi possível atualizar o anexo.");
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Deletar lista de anexos
	 * 
	 * @param attachmentList
	 */
	@Post(BASEPATH + "/attachment/deleteList")
	@Consumes
	@NoCache
	@Permissioned
	public void deleteListOfAttachment(@NotNull PaginatedList<Double> attachmentList) {
		try {
			List<Attachment> list = new ArrayList<>();
			for (Double id : attachmentList.getList()) {
				Attachment attachment = this.bs.retrieveById(id.longValue());
				list.add(attachment);
			}
			this.bs.deleteAttachmentList(list);
			this.success(attachmentList);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

}
