package org.forpdi.planning.fields;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.mail.EmailException;
import org.forpdi.core.company.Company;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationSetting;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.core.user.auth.UserSession;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.document.DocumentAttribute;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.fields.attachment.Attachment;
import org.forpdi.planning.fields.budget.Budget;
import org.forpdi.planning.fields.budget.BudgetDTO;
import org.forpdi.planning.fields.budget.BudgetElement;
import org.forpdi.planning.fields.schedule.Schedule;
import org.forpdi.planning.fields.schedule.ScheduleInstance;
import org.forpdi.planning.fields.schedule.ScheduleStructure;
import org.forpdi.planning.fields.schedule.ScheduleValues;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.fields.table.TableInstance;
import org.forpdi.planning.fields.table.TableStructure;
import org.forpdi.planning.fields.table.TableValues;
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

@RequestScoped
public class FieldsBS extends HibernateBusiness {

	@Inject
	private NotificationBS nbs;
	@Inject
	private UserBS ubs;
	@Inject
	private UserSession userSession;
	@Inject
	@Current
	private CompanyDomain domain;
	private static final int PAGESIZE = 5;

	/**
	 * Salva campo tipo orçamento.
	 * 
	 * @param budget
	 *            Orçamento a ser salvo.
	 * @return void.
	 */
	public void saveBudget(Budget budget) {
		budget.setDeleted(false);
		this.persist(budget);
	}

	/**
	 * Update no campo tipo orçamento.
	 * 
	 * @param budget
	 *            Orçamento para realizar update.
	 * @return void.
	 */
	public void update(Budget budget) {

		budget.setDeleted(false);
		budget.setCreation(new Date());
		this.persist(budget);
	}

	/**
	 * Verificar existência de Ação orçamentária.
	 * 
	 * @param subAction
	 *            Nome Ação Orçamentária.
	 * @return Budget Orçamento que possui Ação orçamentária.
	 */
	public Budget budgetExistsBySubAction(String subAction) {
		Criteria criteria = this.dao.newCriteria(Budget.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("subAction", subAction));
		return (Budget) criteria.uniqueResult();
	}

	/**
	 * Buscar orçamento.
	 * 
	 * @param id
	 *            Id do orçamento para buscar.
	 * @return Budget Orçamento.
	 */
	public Budget budgetExistsById(Long id) {
		Criteria criteria = this.dao.newCriteria(Budget.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (Budget) criteria.uniqueResult();
	}

	/**
	 * Deletar orçamento.
	 * 
	 * @param budget
	 *            Orçamento para deletar.
	 * @return void.
	 */
	public void deleteBudget(Budget budget) {
		budget.setDeleted(true);
		this.persist(budget);
	}

	/**
	 * Buscar cronograma.
	 * 
	 * @param id
	 *            Id do cronograma para buscar.
	 * @return Cronograma.
	 */
	public Schedule retrieveSchedule(Long id) {
		Criteria criteria = this.dao.newCriteria(Schedule.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (Schedule) criteria.uniqueResult();
	}

	/**
	 * Buscar instância do cronograma.
	 * 
	 * @param id
	 *            Id para buscar instância.
	 * @return Instância do cronograma.
	 */
	public ScheduleInstance retrieveScheduleInstance(Long id) {
		Criteria criteria = this.dao.newCriteria(ScheduleInstance.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (ScheduleInstance) criteria.uniqueResult();
	}

	/**
	 * Retorna número de instâncias do campo cronograma.
	 * 
	 * @param schedule
	 *            Cronograma para buscar o número de instâncias.
	 * 
	 * @return Long Número de instâncias.
	 */
	public Long getScheduleInstanceNumber(Schedule schedule) {
		Criteria criteria = this.dao.newCriteria(ScheduleInstance.class).setProjection(Projections.max("number"))
				.add(Restrictions.eq("deleted", false)).add(Restrictions.eq("schedule", schedule));
		Number number = (Number) criteria.uniqueResult();
		if (number == null)
			number = 0;
		return number.longValue() + 1;
	}

	/**
	 * Buscar toda estrutura (todos os campos) do campo cronograma.
	 * 
	 * @param id
	 *            Id do cronograma para buscar os campos.
	 * @return ScheduleStructure Estrutura do cronograma
	 * 
	 */
	public ScheduleStructure retrieveScheduleStructure(Long id) {
		Criteria criteria = this.dao.newCriteria(ScheduleStructure.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (ScheduleStructure) criteria.uniqueResult();
	}

	/**
	 * Buscar os valores do campo cronograma.
	 * 
	 * @param scheduleStructure
	 *            Estrutura do campo cronograma.
	 * @param scheduleInstance
	 *            Instância do cronograma.
	 * @return
	 */
	public ScheduleValues retrieveScheduleValues(ScheduleStructure scheduleStructure,
			ScheduleInstance scheduleInstance) {
		Criteria criteria = this.dao.newCriteria(ScheduleValues.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("scheduleStructure", scheduleStructure))
				.add(Restrictions.eq("scheduleInstance", scheduleInstance));
		return (ScheduleValues) criteria.uniqueResult();
	}

	/**
	 * Buscar lista de instâncias do cronograma de um atributo.
	 * 
	 * @param attributeId
	 *            Id do atributo para buscar sua instância.
	 * @param isDocument
	 * 
	 * @return List<ScheduleInstance> Lista instâncias do cronograma.
	 */

	public List<ScheduleInstance> retrieveScheduleInstanceByAttribute(Long attributeId, boolean isDocument) {
		Criteria criteria = this.dao.newCriteria(ScheduleInstance.class);
		criteria.createAlias("schedule", "schedule", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("schedule.deleted", false));
		criteria.add(Restrictions.eq("schedule.attributeId", attributeId));
		criteria.add(Restrictions.eq("schedule.isDocument", isDocument));
		List<ScheduleInstance> scheduleInstance = this.dao.findByCriteria(criteria, ScheduleInstance.class);
		return scheduleInstance;
	}

	/**
	 * Buscar campo na tabela de campos.
	 * 
	 * @param id
	 *            Id do campo para buscar na tabela.
	 * @return TableFields Campo buscado.
	 */
	public TableFields retrieveTableFields(Long id) {
		Criteria criteria = this.dao.newCriteria(TableFields.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (TableFields) criteria.uniqueResult();
	}

	/**
	 * Buscar instância na tabela de campos.
	 * 
	 * @param id
	 *            Id do campo para buscar na tabela.
	 * @return TableInstance Instância do campo na tabela.
	 */
	public TableInstance retrieveTableInstance(Long id) {
		Criteria criteria = this.dao.newCriteria(TableInstance.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (TableInstance) criteria.uniqueResult();
	}

	/**
	 * Buscar estrutura na tabela de campos.
	 * 
	 * @param id
	 *            Id da estrutura para ser buscado.
	 * @return TableStructure Estrutura buscada.
	 */
	public TableStructure retrieveTableStructure(Long id) {
		Criteria criteria = this.dao.newCriteria(TableStructure.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (TableStructure) criteria.uniqueResult();
	}

	/**
	 * Buscar valores na tabela de campos.
	 * 
	 * @param tableStructure
	 *            Estrutura para buscar os valores.
	 * @param tableInstance
	 *            Instância da tabela de campos.
	 * @return TableValues Valores da tabela de campos.
	 * 
	 */
	public TableValues retrieveTableValues(TableStructure tableStructure, TableInstance tableInstance) {
		Criteria criteria = this.dao.newCriteria(TableValues.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("tableStructure", tableStructure))
				.add(Restrictions.eq("tableInstance", tableInstance));
		return (TableValues) criteria.uniqueResult();
	}

	/**
	 * Salvar valores na instância do cronograma.
	 * 
	 * @param scheduleInstance
	 *            Instância do cronograma.
	 * @param scheduleValuesList
	 *            Lista de valores do cronograma.
	 */
	public void saveScheduleValues(ScheduleInstance scheduleInstance, List<ScheduleValues> scheduleValuesList) {
		for (ScheduleValues scheduleValues : scheduleValuesList) {
			ScheduleStructure scheduleStructure = this
					.retrieveScheduleStructure(scheduleValues.getScheduleStructure().getId());
			ScheduleValues existentScheduleValues = this.retrieveScheduleValues(scheduleStructure, scheduleInstance);
			if (existentScheduleValues == null)
				existentScheduleValues = new ScheduleValues();
			existentScheduleValues.setScheduleStructure(scheduleStructure);
			existentScheduleValues.setScheduleInstance(scheduleInstance);
			existentScheduleValues.setValue(scheduleValues.getValue());
			this.persist(existentScheduleValues);
		}
	}

	/**
	 * Salvar valores na instância na tabela de campos.
	 * 
	 * @param tableInstance.
	 *            Instância da tabela de campos.
	 * @param tableValuesList
	 *            Lista de valores.
	 * @return void.
	 */
	public void saveTableValues(TableInstance tableInstance, List<TableValues> tableValuesList) {
		for (TableValues tableValues : tableValuesList) {
			TableStructure tableStructure = this.retrieveTableStructure(tableValues.getTableStructure().getId());
			TableValues existentTableValues = this.retrieveTableValues(tableStructure, tableInstance);
			if (existentTableValues == null)
				existentTableValues = new TableValues();
			existentTableValues.setTableStructure(tableStructure);
			existentTableValues.setTableInstance(tableInstance);
			existentTableValues.setValue(tableValues.getValue());
			this.persist(existentTableValues);
		}
	}

	/**
	 * Deletar instância do cronograma.
	 * 
	 * @param scheduleInstance
	 *            Instância para ser deletada.
	 * @return void.
	 */
	public void deleteScheduleInstance(ScheduleInstance scheduleInstance) {
		scheduleInstance.setDeleted(true);
		this.persist(scheduleInstance);
	}

	/**
	 * Deletar instância da tabela de campos
	 * 
	 * @param tableInstance
	 *            Instância para ser deletada.
	 * @return void.
	 */
	public void deleteTableInstance(TableInstance tableInstance) {
		tableInstance.setDeleted(true);
		this.persist(tableInstance);
	}

	/**
	 * Buscar lista de orçamentos.
	 * 
	 * @param instance
	 *            Estrutura para buscar os orçamentos.
	 * @return PaginatedList<Budget> Lista de orçamentos.
	 */
	public PaginatedList<Budget> listBudgetsByInstance(StructureLevelInstance instance) {
		PaginatedList<Budget> list = new PaginatedList<Budget>();
		Criteria criteria = this.dao.newCriteria(Budget.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("levelInstance", instance)).addOrder(Order.asc("id"));
		Criteria counting = this.dao.newCriteria(Budget.class).setProjection(Projections.countDistinct("id"))
				.add(Restrictions.eq("levelInstance", instance)).add(Restrictions.eq("deleted", false));
		list.setList(this.dao.findByCriteria(criteria, Budget.class));
		list.setTotal((Long) counting.uniqueResult());
		return list;
	}

	/**
	 * Buscar cronograma à partir de um atributo.
	 * 
	 * @param attributeId
	 *            Atributo para buscar o cronograma.
	 * @param isDocument
	 *            Campo para informar se o campo pertence ao documento ou plano.
	 * @return Schedule Cronograma.
	 */
	public Schedule scheduleByAttribute(Long attributeId, boolean isDocument) {
		Criteria criteria = this.dao.newCriteria(Schedule.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("attributeId", attributeId)).add(Restrictions.eq("isDocument", isDocument))
				.addOrder(Order.asc("id"));
		Schedule schedule = (Schedule) criteria.uniqueResult();
		return schedule;
	}

	/**
	 * Buscar campos na tabela à partir de um atributo.
	 * 
	 * @param attributeId
	 *            Atributo para buscar os campos.
	 * @param isDocument
	 *            Verificar se o campo pertence ao documento ou plano.
	 * @return TableFields Campo da tabela.
	 */
	public TableFields tableFieldsByAttribute(Long attributeId, boolean isDocument, boolean deleted) {
		Criteria criteria = this.dao.newCriteria(TableFields.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("attributeId", attributeId)).add(Restrictions.eq("isDocument", isDocument))
				.addOrder(Order.asc("id"));
		TableFields tableFields = (TableFields) criteria.uniqueResult();
		return tableFields;
	}
	public TableFields tableFieldsByAttribute(Long attributeId, boolean isDocument) {
		Criteria criteria = this.dao.newCriteria(TableFields.class)//.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("attributeId", attributeId)).add(Restrictions.eq("isDocument", isDocument))
				.addOrder(Order.asc("id"));
		TableFields tableFields = (TableFields) criteria.uniqueResult();
		return tableFields;
	}


	/**
	 * Buscar instância na tabela à partir de um atributo.
	 * 
	 * @param attributeId
	 *            Atributo para buscar os campos.
	 * @param isDocument
	 *            Verificar se o campo pertence ao documento ou plano.
	 * @return List<TableInstance> Lista de instâncias.
	 */
	public List<TableInstance> retrieveTableInstanceByAttribute(Long attributeId, boolean isDocument) {
		Criteria criteria = this.dao.newCriteria(TableInstance.class);
		criteria.createAlias("tableFields", "tableFields", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("tableFields.deleted", false));
		criteria.add(Restrictions.eq("tableFields.attributeId", attributeId));
		criteria.add(Restrictions.eq("tableFields.isDocument", isDocument));
		List<TableInstance> tableInstances = this.dao.findByCriteria(criteria, TableInstance.class);
		return tableInstances;
	}

	/**
	 * Buscar lista de orçamentos à partir de estrutura.
	 * 
	 * @param levelInstance
	 *            Estrutura para buscar orçamento.
	 * @return Lista de orçamentos.
	 */
	public List<BudgetDTO> getBudgets(StructureLevelInstance levelInstance) {
		PaginatedList<Budget> budgetList = this.listBudgetsByInstance(levelInstance);
		List<BudgetDTO> budgetItemlist = new ArrayList<BudgetDTO>();
		BudgetElement data;
		for (int i = 0; i < budgetList.getTotal(); i++) {
			BudgetDTO item = new BudgetDTO();
			data = this.retrieveBudgetElement(budgetList.getList().get(i).getSubAction());
			item.setBudget(budgetList.getList().get(i));
			item.setBudgetLoa(data.getBudgetLoa());
			item.setBalanceAvailable(data.getBalanceAvailable());
			budgetItemlist.add(item);
		}
		return budgetItemlist;
	}

	/**
	 * Retornar planos de ação à partir de estrutura.
	 * 
	 * @param instance
	 *            Estrutura para buscar plano de ação.
	 * @return PaginatedList<ActionPlan> Lista de planos de ação.
	 */
	public PaginatedList<ActionPlan> listActionPlansByInstance(StructureLevelInstance instance) {
		PaginatedList<ActionPlan> list = new PaginatedList<ActionPlan>();
		Criteria criteria = this.dao.newCriteria(ActionPlan.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("levelInstance", instance)).addOrder(Order.asc("description"));
		;
		list.setList(this.dao.findByCriteria(criteria, ActionPlan.class));
		return list;
	}

	/**
	 * Retornar página planos de ação à partir de estrutura.
	 * 
	 * @param instance
	 *            Estrutura para buscar plano de ação.
	 * @return PaginatedList<ActionPlan> Lista de planos de ação.
	 */
	public PaginatedList<ActionPlan> listActionPlansByInstancePagined(StructureLevelInstance instance, Integer page,
			Integer pageSize,String dtFiltro) throws ParseException {
		if (page == null || page < 1) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}

		

		PaginatedList<ActionPlan> list = new PaginatedList<ActionPlan>();
		Criteria criteria = this.dao.newCriteria(ActionPlan.class).setFirstResult((page - 1) * pageSize)
				.setMaxResults(pageSize).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("levelInstance", instance));
	
		
		Criteria counting = this.dao.newCriteria(ActionPlan.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("levelInstance", instance));
				
		//filtrando por ano
		if(dtFiltro != null && !dtFiltro.isEmpty()){
			int ano = Integer.parseInt(dtFiltro);
			Date dtIni = (new GregorianCalendar(ano,0,1)).getTime();
			Date dtFim = (new GregorianCalendar(ano,11,31)).getTime();

			criteria.add(Restrictions.between("end",dtIni,dtFim ))
					.add(Restrictions.between("begin",dtIni,dtFim ));

			counting.add(Restrictions.between("end",dtIni,dtFim ))
			.add(Restrictions.between("begin",dtIni,dtFim ));
		
		}

		criteria.addOrder(Order.asc("description"));
		counting.setProjection(Projections.countDistinct("id"));

		list.setList(this.dao.findByCriteria(criteria, ActionPlan.class));
		list.setTotal((Long) counting.uniqueResult());

		return list;
	}

	/**
	 * Retornar planos de ação.
	 * 
	 * @return PaginatedList<ActionPlan> Lista de planos de ação.
	 */
	public PaginatedList<ActionPlan> listActionPlans() {
		PaginatedList<ActionPlan> list = new PaginatedList<ActionPlan>();
		Criteria criteria = this.dao.newCriteria(ActionPlan.class).add(Restrictions.eq("deleted", false))
				.addOrder(Order.asc("id"));
		
		list.setList(this.dao.findByCriteria(criteria, ActionPlan.class));
		return list;
	}

	/**
	 * Buscar plano de ação pela id.
	 * 
	 * @param id
	 *            Id para buscar plano de ação.
	 * @return ActionPlan Plano de ação.
	 */
	public ActionPlan actionPlanExistsById(Long id) {
		Criteria criteria = this.dao.newCriteria(ActionPlan.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("id", id));
		return (ActionPlan) criteria.uniqueResult();
	}

	/**
	 * Deleter plano de ação.
	 * 
	 * @param actionPlan
	 *            Plano de ação para ser deletado.
	 * @return void.
	 */
	public void deleteActionPlan(ActionPlan actionPlan) {
		actionPlan.setDeleted(true);
		this.persist(actionPlan);
	}

	/**
	 * Buscar Ação orçamentária.
	 * 
	 * @param subAction
	 *            Ação orçamentária.
	 * @return BudgetSimulationDB Ação orçamentária.
	 */
	public BudgetElement retrieveBudgetElement(String subAction) {
		Criteria criteria = this.dao.newCriteria(BudgetElement.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("subAction", subAction))
				.add(Restrictions.eq("company",this.domain.getCompany()));
		return (BudgetElement) criteria.uniqueResult();
	}

	/**
	 * Retornar lista de ação orçamentária.
	 * 
	 * @return BudgetElement Lista de elementos orçamentários.
	 */
	public PaginatedList<BudgetElement> listBudget(Company company) {
		PaginatedList<BudgetElement> list = new PaginatedList<BudgetElement>();
		Criteria criteria = this.dao.newCriteria(BudgetElement.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("company", company));

		list.setList(this.dao.findByCriteria(criteria, BudgetElement.class));
		return list;

	}

	/**
	 * Simular valores do campos da ação orçamentária.
	 * 
	 * @param subAction
	 *            Ação orçamentária para simular os valores.
	 * @return BudgetSimulationDB Ação orçamentária com os valores simulados.
	 */
	public BudgetElement saveRandomBudgetSimulation(String subAction) {
		BudgetElement simulation = new BudgetElement();
		simulation.setSubAction(subAction);
		simulation.setDeleted(false);
		Random r = new Random();
		Double randomPlanned = 80000 + (150000 - 80000) * r.nextDouble();
		Double randomCommitted = 80000 + (130000 - 80000) * r.nextDouble();
		Double randomConducted = 80000 + (120000 - 80000) * r.nextDouble();
		//simulation.setCommitted(randomCommitted);
		//simulation.setConducted(randomConducted);
		//simulation.setPlanned(randomPlanned);

		this.persist(simulation);
		return simulation;

	}

	/**
	 * Salvar plano de ação.
	 * 
	 * @param actionPlan
	 *            Ação orçamentária.
	 * @return void.
	 * @throws EmailException
	 */
	public void saveActionPlan(ActionPlan actionPlan, String url) throws EmailException {
		/*
		 * ActionPlan exist =
		 * this.actionPlanExistsByDescription(actionPlan.getDescription()); if
		 * (exist != null && exist.getId() != actionPlan.getId()) { throw new
		 * IllegalArgumentException(
		 * "Já existe um plano de ação cadastrado com este nome."); }
		 */
		actionPlan.setDeleted(false);
		if (actionPlan.isChecked()) {
			User responsible = this.ubs.retrieveResponsible(actionPlan.getLevelInstance());
			if (responsible != null) {
				CompanyUser companyUser = this.ubs.retrieveCompanyUser(responsible, this.domain.getCompany());
				if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
						.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL.getSetting()) {
					this.nbs.sendNotification(NotificationType.ACTION_PLAN_CLOSED, actionPlan.getDescription(),
							actionPlan.getLevelInstance().getName(), responsible.getId(), url);
				} else if (companyUser.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
						.getSetting()) {
					this.nbs.sendNotification(NotificationType.ACTION_PLAN_CLOSED, actionPlan.getDescription(),
							actionPlan.getLevelInstance().getName(), responsible.getId(), url);
					this.nbs.sendNotificationEmail(NotificationType.ACTION_PLAN_CLOSED, actionPlan.getDescription(),
							actionPlan.getLevelInstance().getName(), responsible, url);
				}
			}
		}
		this.persist(actionPlan);
	}

	/**
	 * Buscar plano de ação à partir da descrição.
	 * 
	 * @param actionDescription
	 *            Nome do plano de ação.
	 * @return ActionPlan Plano de ação buscado.
	 */
	/* verificacao foi retirada, pois nao se verificou necessidade no momento */
	public ActionPlan actionPlanExistsByDescription(String actionDescription) {
		Criteria criteria = this.dao.newCriteria(ActionPlan.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("description", actionDescription));
		return (ActionPlan) criteria.uniqueResult();
	}

	/**
	 * Recuperar campos.
	 * 
	 * @param attributeId
	 *            Id para buscar os campos.
	 * @return PaginatedList<OptionsField> Lista com os campos.
	 */
	public PaginatedList<OptionsField> getOptionsField(Long attributeId, boolean deleted) {
		PaginatedList<OptionsField> list = new PaginatedList<>();
		Criteria criteria = this.dao.newCriteria(OptionsField.class).add(Restrictions.eq("deleted", deleted))
				.add(Restrictions.eq("attributeId", attributeId)).addOrder(Order.asc("id"));
		;
		list.setList(this.dao.findByCriteria(criteria, OptionsField.class));
		return list;
	}
	public List<OptionsField> listOptionsFieldsByAttrsAndDocAttrs(List<Attribute> attributes, List<DocumentAttribute> documentAttributes) {
		if (GeneralUtils.isEmpty(attributes) && GeneralUtils.isEmpty(documentAttributes)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(OptionsField.class);
		Disjunction orClause = Restrictions.disjunction();
		if (!GeneralUtils.isEmpty(attributes)) {
			orClause.add(Restrictions.conjunction()
				.add(Restrictions.eq("isDocument", false))
				.add(Restrictions.in("attributeId",
					attributes.stream().map((attr) -> attr.getId()).collect(Collectors.toList())
				))
			);
		}
		if (!GeneralUtils.isEmpty(documentAttributes)) {
			orClause.add(Restrictions.conjunction()
				.add(Restrictions.eq("isDocument", true))
				.add(Restrictions.in("attributeId",
					documentAttributes.stream().map((attr) -> attr.getId()).collect(Collectors.toList())
				))
			);
		}
		criteria.add(orClause);
		return this.dao.findByCriteria(criteria, OptionsField.class);
	}
	
	public List<Schedule> listSchedulesByAttrsAndDocAttrs(List<Attribute> attributes, List<DocumentAttribute> documentAttributes) {
		if (GeneralUtils.isEmpty(attributes) && GeneralUtils.isEmpty(documentAttributes)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(Schedule.class);
		Disjunction orClause = Restrictions.disjunction();
		if (!GeneralUtils.isEmpty(attributes)) {
			orClause.add(Restrictions.conjunction()
				.add(Restrictions.eq("isDocument", false))
				.add(Restrictions.in("attributeId",
					attributes.stream().map((attr) -> attr.getId()).collect(Collectors.toList())
				))
			);
		}
		if (!GeneralUtils.isEmpty(documentAttributes)) {
			orClause.add(Restrictions.conjunction()
				.add(Restrictions.eq("isDocument", true))
				.add(Restrictions.in("attributeId",
					documentAttributes.stream().map((attr) -> attr.getId()).collect(Collectors.toList())
				))
			);
		}
		criteria.add(orClause);
		return this.dao.findByCriteria(criteria, Schedule.class);
	}
	public List<TableFields> listTableFieldsByAttrsAndDocAttrs(List<Attribute> attributes, List<DocumentAttribute> documentAttributes) {
		if (GeneralUtils.isEmpty(attributes) && GeneralUtils.isEmpty(documentAttributes)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(TableFields.class);
		Disjunction orClause = Restrictions.disjunction();
		if (!GeneralUtils.isEmpty(attributes)) {
			orClause.add(Restrictions.conjunction()
				.add(Restrictions.eq("isDocument", false))
				.add(Restrictions.in("attributeId",
					attributes.stream().map((attr) -> attr.getId()).collect(Collectors.toList())
				))
			);
		}
		if (!GeneralUtils.isEmpty(documentAttributes)) {
			orClause.add(Restrictions.conjunction()
				.add(Restrictions.eq("isDocument", true))
				.add(Restrictions.in("attributeId",
					documentAttributes.stream().map((attr) -> attr.getId()).collect(Collectors.toList())
				))
			);
		}
		criteria.add(orClause);
		return this.dao.findByCriteria(criteria, TableFields.class);
	}

	/**
	 * Recuperar coluna dos campos.
	 * 
	 * @param attributeId
	 *            Id para buscar campo.
	 * @param columnId
	 *            Coluna para buscar campo.
	 * @param isDocument
	 *            Verifica se campo pertence ao documento ou plano.
	 * @return Lista com os campos.
	 */
	public List<OptionsField> getOptionsForColumn(Long attributeId, Long columnId, boolean isDocument) {
		Criteria criteria = this.dao.newCriteria(OptionsField.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("attributeId", attributeId)).add(Restrictions.eq("columnId", columnId))
				.add(Restrictions.eq("isDocument", isDocument)).addOrder(Order.asc("id"));
		return this.dao.findByCriteria(criteria, OptionsField.class);
	}

	/**
	 * Listar esturura dos campos.
	 * 
	 * @param fields
	 *            Campo para listar à estrutura.
	 * @return List<TableStructure> Lista com a estururas dos campos.
	 */
	public List<TableStructure> listTableStructureByFields(TableFields fields) {
		Criteria criteria = this.dao.newCriteria(TableStructure.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("tableFields", fields));
		return this.dao.findByCriteria(criteria, TableStructure.class);
	}

	/**
	 * Lista com as instância dos campos.
	 * 
	 * @param fields
	 *            Campos para listar à estrutura.
	 * @return List<TableInstance> Lista com a estururas dos campos.
	 */
	public List<TableInstance> listTableInstanceByFields(TableFields fields, boolean deleted) {
		Criteria criteria = this.dao.newCriteria(TableInstance.class).add(Restrictions.eq("deleted", deleted))
				.add(Restrictions.eq("tableFields", fields));
		return this.dao.findByCriteria(criteria, TableInstance.class);
	}
	public List<TableInstance> listTableInstanceByFields(TableFields fields) {
		Criteria criteria = this.dao.newCriteria(TableInstance.class)//.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("tableFields", fields));
		return this.dao.findByCriteria(criteria, TableInstance.class);
	}

	/**
	 * Lista com os valores da esturutura dos campos
	 * 
	 * @param instance
	 * @return List<TableValues> Lista com os valores da tabela.
	 */
	public List<TableValues> listTableValuesByInstance(TableInstance instance, boolean deleted) {
		Criteria criteria = this.dao.newCriteria(TableValues.class).add(Restrictions.eq("deleted", deleted))
				.add(Restrictions.eq("tableInstance", instance));
		return this.dao.findByCriteria(criteria, TableValues.class);
	}
	public List<TableValues> listTableValuesByInstance(TableInstance instance) {
		Criteria criteria = this.dao.newCriteria(TableValues.class)//.add(Restrictions.eq("deleted", deleted))
				.add(Restrictions.eq("tableInstance", instance));
		return this.dao.findByCriteria(criteria, TableValues.class);
	}

	/**
	 * Listar os valores do campo tabela
	 * 
	 * @param structure
	 *            Estrutura para listar os campos.
	 * @return List<TableValues> Lista dos valores com os campos de tabela.
	 */
	public List<TableValues> listTableValuesByStructure(TableStructure structure, boolean deleted) {
		Criteria criteria = this.dao.newCriteria(TableValues.class).add(Restrictions.eq("deleted", deleted))
				.add(Restrictions.eq("tableStructure", structure));
		return this.dao.findByCriteria(criteria, TableValues.class);
	}
	public List<TableValues> listTableValuesByStructure(TableStructure structure) {
		Criteria criteria = this.dao.newCriteria(TableValues.class)//.add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("tableStructure", structure));
		return this.dao.findByCriteria(criteria, TableValues.class);
	}


	/**
	 * Lista com as estruturas do campos do cronograma.
	 * 
	 * @param schedule
	 *            Cronograma para listar as estruturas.
	 * @return List<ScheduleStructure> Lista com as estruturas do cronograma.
	 */
	public List<ScheduleStructure> listScheduleStructure(Schedule schedule) {
		Criteria criteria = this.dao.newCriteria(ScheduleStructure.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("schedule", schedule));
		return this.dao.findByCriteria(criteria, ScheduleStructure.class);
	}

	/**
	 * Lista com as instâncias do cronograma
	 * 
	 * @param schedule
	 *            Cronograma para preencher lista com suas instâncias.
	 * @return List<ScheduleInstance> Lista com à estrutura das instâncias.
	 */
	public List<ScheduleInstance> listScheduleInstance(Schedule schedule) {
		Criteria criteria = this.dao.newCriteria(ScheduleInstance.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("schedule", schedule));
		return this.dao.findByCriteria(criteria, ScheduleInstance.class);
	}

	/**
	 * Lista com as instâncias das esturutras.
	 * 
	 * @param instance
	 *            Instância das estruturas.
	 * @return
	 */
	public List<ScheduleValues> lsitScheduleValues(ScheduleInstance instance) {
		Criteria criteria = this.dao.newCriteria(ScheduleValues.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("scheduleInstance", instance));
		return this.dao.findByCriteria(criteria, ScheduleValues.class);
	}

	/**
	 * Lista com os valores do campos da estrutura.
	 * 
	 * @param structure
	 *            Estrutura para listar os valores.
	 * @return List<ScheduleValues> Lista com os valores do cronograma.
	 */
	public List<ScheduleValues> lsitScheduleValues(ScheduleStructure structure) {
		Criteria criteria = this.dao.newCriteria(ScheduleValues.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("scheduleStructure", structure));
		return this.dao.findByCriteria(criteria, ScheduleValues.class);
	}

	/**
	 * Salvar dados de um anexo no banco de dados
	 * 
	 * @param attachment,
	 *            anexo a ser salvo
	 * @return Attachment
	 */
	public Attachment saveAttachment(Attachment attachment) {
		attachment.setDeleted(false);
		attachment.setCreation(new Date());
		attachment.setAuthor(this.userSession.getUser());
		this.persist(attachment);

		return attachment;
	}

	/**
	 * Listar os anexos de um dado nível
	 * 
	 * @param levelInstance,
	 *            nível a ser buscado os anexos
	 * @return PaginatedList<Attachment>
	 */
	public PaginatedList<Attachment> listAllAttachment(StructureLevelInstance levelInstance, Integer page,
			Integer pageSize) {
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = PAGESIZE;
		}
		PaginatedList<Attachment> result = new PaginatedList<>();
		Criteria criteria = this.dao.newCriteria(Attachment.class);
		criteria.add(Restrictions.eq("levelInstance", levelInstance));
		criteria.add(Restrictions.eq("deleted", false));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		Criteria counting = this.dao.newCriteria(Attachment.class);
		counting.add(Restrictions.eq("levelInstance", levelInstance));
		counting.add(Restrictions.eq("deleted", false));
		counting.setProjection(Projections.countDistinct("id"));

		result.setList(this.dao.findByCriteria(criteria, Attachment.class));
		result.setTotal((Long) counting.uniqueResult());

		return result;
	}

	/**
	 * Recuperar um anexo pelo seu id
	 * 
	 * @param id
	 * @return
	 */
	public Attachment retrieveById(Long id) {
		Criteria criteria = this.dao.newCriteria(Attachment.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("deleted", false));

		return (Attachment) criteria.uniqueResult();
	}

	/**
	 * Excluir um dado anexo
	 * 
	 * @param attachment
	 */
	public boolean deleteAttachment(Attachment attachment) {
		if (attachment == null) {
			return false;
		}

		attachment.setDeleted(true);
		this.persist(attachment);
		return true;
	}

	/**
	 * Editar descrição do anexo
	 * 
	 * @param attachment
	 * @return
	 */
	public boolean updateAttachment(Attachment attachment) {
		Attachment exist = this.retrieveById(attachment.getId());
		if (exist == null) {
			return false;
		}

		exist.setDescription(attachment.getDescription());
		this.persist(exist);

		return true;
	}

	/**
	 * Excluir lista de anexos
	 * 
	 * @param list
	 */
	public void deleteAttachmentList(List<Attachment> list) {
		for (Attachment attachment : list) {
			this.deleteAttachment(attachment);
		}
	}
	
	public List<ScheduleInstance> listAllScheduleInstancesBySchedules(List<Schedule> schedules) {
		if (GeneralUtils.isEmpty(schedules)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(ScheduleInstance.class);
		criteria.add(Restrictions.in("schedule", schedules));
		return this.dao.findByCriteria(criteria, ScheduleInstance.class);
	}

	public List<ScheduleStructure> listAllScheduleStructuresBySchedules(List<Schedule> schedules) {
		if (GeneralUtils.isEmpty(schedules)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(ScheduleStructure.class);
		criteria.add(Restrictions.in("schedule", schedules));
		return this.dao.findByCriteria(criteria, ScheduleStructure.class);
	}

	public List<TableInstance> listAllTableInstancesByTableFields(List<TableFields> tableFields) {
		if (GeneralUtils.isEmpty(tableFields)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(TableInstance.class);
		criteria.add(Restrictions.in("tableFields", tableFields));
		return this.dao.findByCriteria(criteria, TableInstance.class);
	}

	public List<TableStructure> listAllTableStructuresByTableFields(List<TableFields> tableFields) {
		if (GeneralUtils.isEmpty(tableFields)) {
			return Collections.emptyList();
		}
		Criteria criteria = this.dao.newCriteria(TableStructure.class);
		criteria.add(Restrictions.in("tableFields", tableFields));
		return this.dao.findByCriteria(criteria, TableStructure.class);
	}

	public List<ScheduleValues> listAllScheduleValuesByInstancesAndStructures(List<ScheduleInstance> instances, List<ScheduleStructure> structures) {
		Criteria criteria = this.dao.newCriteria(ScheduleValues.class);
		if (!GeneralUtils.isEmpty(instances))
			criteria.add(Restrictions.in("scheduleInstance", instances));
		if (!GeneralUtils.isEmpty(structures))
			criteria.add(Restrictions.in("scheduleStructure", structures));
		return this.dao.findByCriteria(criteria, ScheduleValues.class);
	}

	public List<TableValues> listAllTableValuesByInstancesAndStructures(List<TableInstance> instances, List<TableStructure> structures) {
		Criteria criteria = this.dao.newCriteria(TableValues.class);
		if (!GeneralUtils.isEmpty(instances))
			criteria.add(Restrictions.in("tableInstance", instances));
		if (!GeneralUtils.isEmpty(structures))
			criteria.add(Restrictions.in("tableStructure", structures));
		return this.dao.findByCriteria(criteria, TableValues.class);
	}
	

}
