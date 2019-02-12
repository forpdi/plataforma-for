 	package org.forpdi.core.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.dashboard.manager.LevelInstanceHistory;
import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.document.Document;
import org.forpdi.planning.document.DocumentAttribute;
import org.forpdi.planning.document.DocumentBS;
import org.forpdi.planning.document.DocumentSection;
import org.forpdi.planning.fields.FieldsBS;
import org.forpdi.planning.fields.OptionsField;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.fields.attachment.Attachment;
import org.forpdi.planning.fields.budget.Budget;
import org.forpdi.planning.fields.budget.BudgetBS;
import org.forpdi.planning.fields.budget.BudgetElement;
import org.forpdi.planning.fields.schedule.Schedule;
import org.forpdi.planning.fields.schedule.ScheduleInstance;
import org.forpdi.planning.fields.schedule.ScheduleStructure;
import org.forpdi.planning.fields.schedule.ScheduleValues;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.fields.table.TableInstance;
import org.forpdi.planning.fields.table.TableStructure;
import org.forpdi.planning.fields.table.TableValues;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanDetailed;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.Structure;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureLevel;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forpdi.planning.structure.StructureLevelInstanceDetailed;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.serialization.gson.GsonSerializerBuilder;

@RequestScoped
public class BackupAndRestoreHelper extends HibernateBusiness {

	private Gson gson;
	private static int quantity;
	private static int quantityTotal;

	@Inject private PlanBS planBS;
	@Inject private DocumentBS docBS;
	@Inject private StructureBS structureBS;
	@Inject private BudgetBS budgetBS;
	@Inject private CompanyBS companyBS;
	@Inject private UserBS userBS;
	@Inject	private FieldsBS fieldsBS;
	@Inject @Current private CompanyDomain domain;
	
	@Inject
	public BackupAndRestoreHelper(GsonSerializerBuilder gsonBuilder) {
		gsonBuilder.setWithoutRoot(true);
		gsonBuilder.indented();
		this.gson = gsonBuilder.create();
	}

	@Deprecated
	protected BackupAndRestoreHelper() {}

	/**
	 * consulta o banco e salva as informações em um arquivo
	 *
	 * @throws IOException
	 *
	 */
	public void export(Company company, OutputStream output) throws IOException {
		if (company == null || output == null) {
			throw new IllegalArgumentException("Company not found.");
		}
		
		final ZipOutputStream zos = new ZipOutputStream(output);
		zipAdd(zos, Company.class.getSimpleName(), this.gson.toJson(company));
		zos.flush();
		
		final HashMap<Long, Structure> structuresMap = new HashMap<>();
		
		//Exportando os elementos orçamentários
		final List<BudgetElement> budgetElements = this.budgetBS.listAllBudgetElementsByCompany(company);
		if(!GeneralUtils.isEmpty(budgetElements)) {
			for(final BudgetElement budgetElement : budgetElements) {
				budgetElement.setCompany(null);
			}
			zipAdd(zos, BudgetElement.class.getSimpleName(), this.gson.toJson(budgetElements));
			zos.flush();
		}
		
		// Exportando labels customizadas da company
		final List<CompanyMessage> companyMessage = companyBS.retrieveMessages(company);
		if(!GeneralUtils.isEmpty(companyMessage)) {
			companyMessage.forEach(it -> {
				it.setCompany(null);
			});
			zipAdd(zos, CompanyMessage.class.getSimpleName(), this.gson.toJson(companyMessage));
			zos.flush();
		}

		//Exportando os Planos Macro
		final List<PlanMacro> plansMacro = this.planBS.listAllMacros(company);
		if(!GeneralUtils.isEmpty(plansMacro)) {
			for(final PlanMacro planMacro : plansMacro) {
				planMacro.setCompany(null);
			}
			zipAdd(zos, PlanMacro.class.getSimpleName(), this.gson.toJson(plansMacro));
			zos.flush();
		}
		
		//Exportando os documentos do PDI
		final List<Document> documents = this.docBS.listAllByPlansMacro(plansMacro);
		if(!GeneralUtils.isEmpty(documents)) {
			for (final Document document : documents) {
				document.setExportPlanMacroId(document.getPlan().getId());
				document.setPlan(null);
			}
			zipAdd(zos, Document.class.getSimpleName(), this.gson.toJson(documents));
			zos.flush();
		}

		//Exportando as seções dos documentos
		final List<DocumentSection> documentSections = this.docBS.listAllSectionsByDocuments(documents);
		if(!GeneralUtils.isEmpty(documentSections)) {
			for (final DocumentSection documentSection : documentSections) {
				documentSection.setExportDocumentId(documentSection.getDocument().getId());
				documentSection.setDocument(null);
				if (documentSection.getParent() != null) {
					documentSection.setExportDocumentSectionId(documentSection.getParent().getId());
					documentSection.setParent(null);
				}
			}
			zipAdd(zos, DocumentSection.class.getSimpleName(), this.gson.toJson(documentSections));
			zos.flush();
		}

		//Exportando os atributos das seções dos documentos
		final List<DocumentAttribute> documentAttributes = this.docBS.listAllAttributesBySections(documentSections);
		if(!GeneralUtils.isEmpty(documentAttributes)) {
			for (final DocumentAttribute attr : documentAttributes) {
				attr.setExportDocumentSectionId(attr.getExportDocumentSectionId());
			}
			zipAdd(zos, DocumentAttribute.class.getSimpleName(), this.gson.toJson(documentAttributes));
			zos.flush();
		}
		
		//Exportando os planos de metas
		final List<Plan> plans = this.planBS.listAllPlansForPlansMacro(plansMacro);
		if(!GeneralUtils.isEmpty(plans)) {
			for (final Plan plan : plans) {
				final Structure structure = plan.getStructure();
				plan.setExportPlanMacroId(plan.getParent().getId());
				plan.setExportStructureId(structure.getId());
				plan.setParent(null);
				plan.setStructure(null);
				if (!structuresMap.containsKey(structure.getId())) {
					structuresMap.put(structure.getId(), structure);
				}
			}
			zipAdd(zos, Plan.class.getSimpleName(), this.gson.toJson(plans));
			zos.flush();
		}
		
		//Exporta plano detalhado
		final List<PlanDetailed> plansDetailed = this.planBS.listAllPlansDetailed(plans);
		if(!GeneralUtils.isEmpty(plansDetailed)) {
			for (final PlanDetailed pd : plansDetailed) {
				pd.setExportPlanId(pd.getPlan().getId());
				pd.setPlan(null);
			}
			zipAdd(zos, PlanDetailed.class.getSimpleName(), this.gson.toJson(plansDetailed));
			zos.flush();
		}
		
		// Exportando estruturas
		final List<Structure> structures = new ArrayList<>(structuresMap.values());
		if (!GeneralUtils.isEmpty(structures)) {
			for (final Structure structure : structures) {
				structure.setCompany(null);
			}
			zipAdd(zos, Structure.class.getSimpleName(), this.gson.toJson(structures));
			zos.flush();
		}
		
		// Exportando níveis de estruturas
		final List<StructureLevel> structureLevels = this.structureBS.listAllStructuresLevels(structures);
		if (!GeneralUtils.isEmpty(structureLevels)) {
			for (final StructureLevel structureLevel : structureLevels) {
				structureLevel.setExportStructureId(structureLevel.getStructure().getId());
				structureLevel.setStructure(null);
			}
			zipAdd(zos, StructureLevel.class.getSimpleName(), this.gson.toJson(structureLevels));
			zos.flush();
		}

		// Exportando atributos
		final List<Attribute> attributes = this.structureBS.listAllAttributes(structureLevels);
		if (!GeneralUtils.isEmpty(attributes)) {
			for (final Attribute attribute : attributes) {
				attribute.setExportStructureLevelId(attribute.getLevel().getId());
				attribute.setLevel(null);
			}
			zipAdd(zos, Attribute.class.getSimpleName(), this.gson.toJson(attributes));
			zos.flush();
		}
		
		//Exportando as estruturas level instance necessárias.
		final List<StructureLevelInstance> structureLevelInstances = this.structureBS.listAllLevelInstanceByPlans(plans);
		if(!GeneralUtils.isEmpty(structureLevelInstances)) {
			for (final StructureLevelInstance sli : structureLevelInstances) {
				sli.setExportLevelId(sli.getLevel().getId());
				sli.setExportPlanId(sli.getPlan().getId());
				sli.setLevel(null);
				sli.setPlan(null);
				
				final AttributeInstance responsible = this.structureBS.listResponsibleAttributeByLevel(sli);
				if (responsible != null) {
					User user = this.userBS.existsByUser(Long.parseLong(responsible.getValue()));
					sli.setExportResponsibleMail(user.getEmail());
				}
			}
			zipAdd(zos, StructureLevelInstance.class.getSimpleName(), this.gson.toJson(structureLevelInstances));
			zos.flush();
		}

		//Exportando os detalhes das estruturas level instance.
		final List<StructureLevelInstanceDetailed> structureLevelInstancesDetailed =
			this.structureBS.listAllLevelInstancesDetailedByLevelInstances(structureLevelInstances);
		if(!GeneralUtils.isEmpty(structureLevelInstancesDetailed)) {
			for (final StructureLevelInstanceDetailed slid : structureLevelInstancesDetailed) {
				slid.setExportStructureLevelInstanceId(slid.getLevelInstance().getId());
				slid.setLevelInstance(null);
			}
			zipAdd(zos, StructureLevelInstanceDetailed.class.getSimpleName(), this.gson.toJson(structureLevelInstancesDetailed));
			zos.flush();
		}

		//Exportando o histórico das estruturas level instance.
		final List<LevelInstanceHistory> levelInstancesHistory =
			this.structureBS.listAllLevelInstancesHistoryByLevelInstances(structureLevelInstances);
		if(!GeneralUtils.isEmpty(levelInstancesHistory)) {
			for (final LevelInstanceHistory lih : levelInstancesHistory) {
				lih.setExportStructureLevelInstanceId(lih.getLevelInstance().getId());
				lih.setLevelInstance(null);
			}
			zipAdd(zos, LevelInstanceHistory.class.getSimpleName(), this.gson.toJson(levelInstancesHistory));
			zos.flush();
		}

		//Exportando os planos de ação
		final List<ActionPlan> actionPlans = this.structureBS.listAllActionPlansByLevelInstances(structureLevelInstances);
		if(!GeneralUtils.isEmpty(actionPlans)) {
			for (final ActionPlan actionPlan : actionPlans) {
				actionPlan.setExportStructureLevelInstanceId(actionPlan.getLevelInstance().getId());
				actionPlan.setLevelInstance(null);
			}
			zipAdd(zos, ActionPlan.class.getSimpleName(), this.gson.toJson(actionPlans));
			zos.flush();
		}

		//Exportando os indicadores agregados
		final List<AggregateIndicator> aggregateIndicators = this.structureBS.listAllAggregateIndicatorsByLevelInstances(structureLevelInstances);
		if(!GeneralUtils.isEmpty(aggregateIndicators)) {
			for (final AggregateIndicator aggregateIndicator : aggregateIndicators) {
				aggregateIndicator.setExportAggregateId(aggregateIndicator.getAggregate().getId());
				aggregateIndicator.setExportIndicatorId(aggregateIndicator.getIndicator().getId());
				aggregateIndicator.setAggregate(null);
				aggregateIndicator.setIndicator(null);
			}
			zipAdd(zos, AggregateIndicator.class.getSimpleName(), this.gson.toJson(aggregateIndicators));
			zos.flush();
		}

		//Exportando os anexos
		final List<Attachment> attachments = this.structureBS.listAllAttachmentsByLevelInstances(structureLevelInstances);
		if(!GeneralUtils.isEmpty(attachments)) {
			for (final Attachment attachment : attachments) {
				attachment.setExportStructureLevelInstanceId(attachment.getLevelInstance().getId());
				attachment.setLevelInstance(null);
				if (attachment.getAuthor() != null) {
					attachment.setExportAuthorMail(attachment.getAuthor().getEmail());
				} else {
					attachment.setExportAuthorMail("");
				}
				attachment.setAuthor(null);
			}
			zipAdd(zos, Attachment.class.getSimpleName(), this.gson.toJson(attachments));
			zos.flush();
		}

		//Exportando os orçamentos
		final List<Budget> budgets = this.budgetBS.listAllBudgetsByElementsAndLevelInstances(budgetElements, structureLevelInstances);
		if(!GeneralUtils.isEmpty(budgets)) {
			for(final Budget budget : budgets) {
				budget.setExportBudgetElementId(budget.getBudgetElement().getId());
				budget.setExportStructureLevelInstanceId(budget.getLevelInstance().getId());
				budget.setBudgetElement(null);
				budget.setLevelInstance(null);
			}
			zipAdd(zos, Budget.class.getSimpleName(), this.gson.toJson(budgets));
			zos.flush();
		}
		
		//Exportando as instância de atributos
		final List<AttributeInstance> attributeInstances = this.structureBS.listAllAttributeInstanceByPlans(structureLevelInstances);
		if(!GeneralUtils.isEmpty(attributeInstances)) {
			for (final AttributeInstance attrInstance : attributeInstances) {
				attrInstance.setExportAttributeId(attrInstance.getAttribute().getId());
				attrInstance.setExportStructureLevelInstanceId(attrInstance.getLevelInstance().getId());
				attrInstance.setAttribute(null);
				attrInstance.setLevelInstance(null);
			}
			zipAdd(zos, AttributeInstance.class.getSimpleName(), this.gson.toJson(attributeInstances));
			zos.flush();
		}

		//Exportando as opções do select field
		final List<OptionsField> optionsFields = this.fieldsBS.listOptionsFieldsByAttrsAndDocAttrs(attributes, documentAttributes);
		if(!GeneralUtils.isEmpty(optionsFields)) {
			zipAdd(zos, OptionsField.class.getSimpleName(), this.gson.toJson(optionsFields));
			zos.flush();
		}

		//Exportando os cronogramas
		final List<Schedule> schedules = this.fieldsBS.listSchedulesByAttrsAndDocAttrs(attributes, documentAttributes);
		if(!GeneralUtils.isEmpty(schedules)) {
			for (Schedule schedule : schedules) {
				schedule.setScheduleInstances(null);
				schedule.setScheduleStructures(null);
			}
			zipAdd(zos, Schedule.class.getSimpleName(), this.gson.toJson(schedules));
			zos.flush();
		}
		
		//Exportando os instâncias de cronogramas
		final List<ScheduleInstance> scheduleInstances = this.fieldsBS.listAllScheduleInstancesBySchedules(schedules);
		if(!GeneralUtils.isEmpty(scheduleInstances)) {
			for (final ScheduleInstance scheduleInstance : scheduleInstances) {
				scheduleInstance.setExportScheduleId(scheduleInstance.getSchedule().getId());
				scheduleInstance.setSchedule(null);
			}
			zipAdd(zos, ScheduleInstance.class.getSimpleName(), this.gson.toJson(scheduleInstances));
			zos.flush();
		}
		
		//Exportando os estruturas de cronogramas
		final List<ScheduleStructure> scheduleStructures = this.fieldsBS.listAllScheduleStructuresBySchedules(schedules);
		if(!GeneralUtils.isEmpty(scheduleStructures)) {
			for (final ScheduleStructure scheduleStructure : scheduleStructures) {
				scheduleStructure.setExportScheduleId(scheduleStructure.getSchedule().getId());
				scheduleStructure.setSchedule(null);
			}
			zipAdd(zos, ScheduleStructure.class.getSimpleName(), this.gson.toJson(scheduleStructures));
			zos.flush();
		}

		//Exportando os valores de cronogramas
		if (!GeneralUtils.isEmpty(scheduleInstances) || !GeneralUtils.isEmpty(scheduleStructures)) {
			final List<ScheduleValues> scheduleValues = this.fieldsBS.listAllScheduleValuesByInstancesAndStructures(scheduleInstances, scheduleStructures);
			if(!GeneralUtils.isEmpty(scheduleValues)) {
				for (final ScheduleValues scheduleValue : scheduleValues) {
					scheduleValue.setExportScheduleInstanceId(scheduleValue.getScheduleInstance().getId());
					scheduleValue.setExportScheduleStructureId(scheduleValue.getScheduleStructure().getId());
					scheduleValue.setScheduleInstance(null);
					scheduleValue.setScheduleStructure(null);
				}
				zipAdd(zos, ScheduleValues.class.getSimpleName(), this.gson.toJson(scheduleValues));
				zos.flush();
			}
		}

		//Exportando as tabelas
		final List<TableFields> tableFields = this.fieldsBS.listTableFieldsByAttrsAndDocAttrs(attributes, documentAttributes);
		if(!GeneralUtils.isEmpty(tableFields)) {
			for (TableFields tableField : tableFields) {
				tableField.setTableInstances(null);
				tableField.setTableStructures(null);
			}
			zipAdd(zos, TableFields.class.getSimpleName(), this.gson.toJson(tableFields));
			zos.flush();
		}
		
		//Exportando os instâncias de tabelas
		final List<TableInstance> tableInstances = this.fieldsBS.listAllTableInstancesByTableFields(tableFields);
		if(!GeneralUtils.isEmpty(tableInstances)) {
			for (final TableInstance tableInstance : tableInstances) {
				tableInstance.setExportTableFieldsId(tableInstance.getTableFields().getId());
				tableInstance.setTableFields(null);
				tableInstance.setTableValues(null);
			}
			zipAdd(zos, TableInstance.class.getSimpleName(), this.gson.toJson(tableInstances));
			zos.flush();
		}

		//Exportando os estruturas de tabelas
		final List<TableStructure> tableStructures = this.fieldsBS.listAllTableStructuresByTableFields(tableFields);
		if(!GeneralUtils.isEmpty(tableStructures)) {
			for (final TableStructure tableStructure : tableStructures) {
				tableStructure.setExportTableFieldsId(tableStructure.getTableFields().getId());
				tableStructure.setTableFields(null);
			}
			zipAdd(zos, TableStructure.class.getSimpleName(), this.gson.toJson(tableStructures));
			zos.flush();
		}
		
		//Exportando os valores de tabelas
		if (!GeneralUtils.isEmpty(tableInstances) || !GeneralUtils.isEmpty(tableStructures)) {
			final List<TableValues> tableValues = this.fieldsBS.listAllTableValuesByInstancesAndStructures(tableInstances, tableStructures);
			if(!GeneralUtils.isEmpty(tableValues)) {
				for (final TableValues tableValue : tableValues) {
					tableValue.setExportTableInstanceId(tableValue.getTableInstance().getId());
					tableValue.setExportTableStructureId(tableValue.getTableStructure().getId());
					tableValue.setTableInstance(null);
					tableValue.setTableStructure(null);
				}
				zipAdd(zos, TableValues.class.getSimpleName(), this.gson.toJson(tableValues));
				zos.flush();
			}
		}
		
		zos.close();
	}


	/**
	 * adiciona dados ao banco a partir do arquivo
	 * 
	 * @param file arquivo a ser importado
	 * 
	 * @throws IOException
	 * 
	 * @throws ParseException
	 * 
	 */
	public void restore(UploadedFile file) throws IOException, ParseException {
		// company passado na hora da importação
		Company company = this.domain.getCompany();
		
		if (company == null) {
			throw new IllegalArgumentException("Company not found.");
		}
		
		List<File> files = decompact(file.getFile());
		
		//contar quantidade de registros que serão salvos;
		quantityTotal=quantityTotal(files);
		quantity=0;
		
		Map<Long, Long> map_id_company = new HashMap<Long, Long>();
		Map<Long, Long> map_id_plan_macro = new HashMap<Long, Long>();
		Map<Long, Long> map_id_structure = new HashMap<Long, Long>();
		Map<Long, Long> map_id_structure_level = new HashMap<Long, Long>();
		Map<Long, Long> map_id_structure_level_instance = new HashMap<Long, Long>();
		Map<Long, Long> map_id_plan = new HashMap<Long, Long>();
		Map<Long, Long> map_budget_element = new HashMap<Long, Long>();
		Map<Long, Long> map_id_attribute = new HashMap<Long, Long>();
		Map<Long, Long> map_id_document = new HashMap<Long, Long>();
		Map<Long, Long> map_id_document_section = new HashMap<Long, Long>();
		Map<Long, Long> map_id_document_attribute= new HashMap<Long, Long>();
		Map<Long, Long> map_id_table_field = new HashMap<Long, Long>();
		Map<Long, Long> map_id_table_instance = new HashMap<Long, Long>();
		Map<Long, Long> map_id_table_structure = new HashMap<Long, Long>();
		Map<Long, Long> map_id_schedule = new HashMap<Long, Long>();
		Map<Long, Long> map_id_schedule_instance = new HashMap<Long, Long>();
		Map<Long, Long> map_id_schedule_structure = new HashMap<Long, Long>();

		
		for (File f : files) {

			String register = IOUtils.toString(new FileInputStream(f), StandardCharsets.UTF_8);
			Long id_old;
			String name = f.getName().split(".json")[0];
			JSONParser parser = new JSONParser();
			JSONArray array;

			LOGGER.warn("Restoring " +name);
			switch (name) {
			
				case "Company" :
					Company c = gson.fromJson(register, Company.class);
					map_id_company.put(c.getId(), company.getId());
					break;
					
				case "CompanyMessage" :
					
					array = (JSONArray) parser.parse(register);
					for (int i = 0; i < array.size(); i++) {
						CompanyMessage cm = gson.fromJson(array.get(i).toString(), CompanyMessage.class);
						//long id_old_company =cm.getExportCompanyId();
						cm.setCompany(company);
						quantity+=1;
						
						try {
							this.dao.persist(cm);
						}catch(Exception e) {
							continue;
						}
					}
					break;
	
					
				case "PlanMacro":
					readJson(register, PlanMacro.class, map_id_plan_macro, map_id_company, null);
					break;
	
				case "Structure":
					array = (JSONArray) parser.parse(register);
					for (int i = 0; i < array.size(); i++) {
						Structure st = gson.fromJson(array.get(i).toString(), Structure.class);
						id_old = st.getId();
						st.setCompany(company);
						st.setId(null);
						this.dao.persist(st);
						map_id_structure.put(id_old, st.getId());
						quantity+=1;
					}
					break;
					
				case "BudgetElement" :
					array = (JSONArray) parser.parse(register);
					for (int i = 0; i < array.size(); i++) {
						BudgetElement be = gson.fromJson(array.get(i).toString(), BudgetElement.class);
						id_old = be.getId();
						be.setCompany(company);
						be.setId(null);
						this.dao.persist(be);
						map_budget_element.put(id_old, be.getId());
						quantity+=1;
					}
					break;
	
				case "Plan":
					readJson(register, Plan.class, map_id_plan, map_id_structure, map_id_plan_macro);
					break;
					
				case "PlanDetailed":
					readJson(register, PlanDetailed.class, null, map_id_plan, null);
					break;
	
				case "StructureLevel":
					readJson(register, StructureLevel.class, map_id_structure_level, map_id_structure, null);
					break;
					
				case "StructureLevelInstance":
					readJson(register, StructureLevelInstance.class, map_id_structure_level_instance, map_id_structure_level, map_id_plan);
					break;
	
				case "Document":
					readJson(register, Document.class, map_id_document, map_id_plan_macro, null);
					break;
					
				case "DocumentSection":
					readJson(register, DocumentSection.class, map_id_document_section, map_id_document_section, map_id_document);
					break;
					
				case "DocumentAttribute":
					readJson(register, DocumentAttribute.class, map_id_document_attribute, map_id_document_section, null);
					break;
							
				case "ActionPlan" :
					readJson(register, ActionPlan.class, null, map_id_structure_level_instance, null);
					break;
					
				case "LevelInstanceHistory" :
					readJson(register, LevelInstanceHistory.class, null, map_id_structure_level_instance, null);
					break;	
					
				case "StructureLevelInstanceDetailed" :
					readJson(register, StructureLevelInstanceDetailed.class, null, map_id_structure_level_instance, null);
					break;
					
				case "Budget" :
					readJson(register, Budget.class, null, map_id_structure_level_instance, map_budget_element);
					break;
					
				case "Attachment":
					readJson(register, Attachment.class, null, map_id_structure_level_instance, null);
					break;	
					
				case "Attribute" :
					readJson(register, Attribute.class, map_id_attribute, map_id_structure_level, null);
					break;	
					
				case "AttributeInstance" :
					readJson(register, AttributeInstance.class, null, map_id_structure_level_instance, map_id_attribute);
					break;
					
				case "AggregateIndicator" :
					readJson(register, AggregateIndicator.class, null, map_id_structure_level_instance, null);
					break;
					
				case "TableFields":
					readJson(register, TableFields.class, map_id_table_field, map_id_document_attribute, map_id_attribute);
					break;
				
				case "TableStructure":
					readJson(register, TableStructure.class, map_id_table_structure, map_id_table_field, null);
					break;
					
				case "TableInstance":
					readJson(register, TableInstance.class, map_id_table_instance, map_id_table_field, null);
					break;
					
				case "TableValues":
					readJson(register, TableValues.class, null, map_id_table_structure, map_id_table_instance);
					break;
				
				case "Schedule":
					readJson(register, Schedule.class, map_id_schedule, map_id_document_attribute, map_id_attribute);
					break;
				
				case "ScheduleStructure":
					readJson(register, ScheduleStructure.class, map_id_schedule_structure, map_id_schedule, null);
					break;
					
				case "ScheduleInstance":
					readJson(register, ScheduleInstance.class, map_id_schedule_instance, map_id_schedule, null);
					break;
					
				case "ScheduleValues":
					readJson(register, ScheduleValues.class, null, map_id_schedule_structure, map_id_schedule_instance);
					break;	
					
				case "OptionsField":
					readJson(register, OptionsField.class, null, map_id_document_attribute, map_id_attribute);
					break;
										
				default:
					break;
			}
		}
	}

	
	/**
	 * adiciona dados ao banco a partir do json
	 * 
	 * @param register   json do registro
	 *
	 * @param clazz      classe a ser lida
	 * 
	 * @param map_atual  mapa do id da classe que está sendo importada
	 * 
	 * @param map_fkey_1 mapa do id da primeira foreign key desta classe
	 * 
	 * @param map_fkey_2 mapa do id da segunda foreign key desta classe
	 * 
	 * @throws org.json.simple.parser.ParseException
	 * 
	 */
	private void readJson(String register, Class<?> clazz, Map<Long, Long>  map_atual , Map<Long, Long> map_fkey_1, Map<Long, Long> map_fkey_2) throws org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray) parser.parse(register);
		
		if(clazz.getSimpleName().equals("StructureLevelInstance")) {
	
			List<StructureLevelInstance> sli = new ArrayList<>(); 
			
			for (int i = 0; i < array.size(); i++) {
				JSONObject jo = (JSONObject) array.get(i);
				StructureLevelInstance obj = (StructureLevelInstance) gson.fromJson(jo.toString(), clazz);
				sli.add(obj);
			}
			
			Map<Long, StructureLevel> structureLevelMap = new HashMap<>();
			List<StructureLevel> sl = retrieve(StructureLevel.class);
			sl.stream().forEach(it->{
				structureLevelMap.put(it.getId(), it);
			});
			
			Map<Long, Plan> planMap = new HashMap<>();
			List<Plan> pl = retrieve(Plan.class);
			pl.stream().forEach(it->{
				planMap.put(it.getId(), it);
			});
			
			do {
				for( StructureLevelInstance s : sli) {
					if(s.getParent()==null || map_atual.containsKey(s.getParent())) {
						
						s.setLevel(structureLevelMap.get(map_fkey_1.get(s.getExportLevelId())));
						s.setPlan( planMap.get(map_fkey_2.get(s.getExportPlanId())));					
						long id_old=s.getId();
						
						((SimpleLogicalDeletableEntity) s).setId(null);

						if(map_atual.containsKey(s.getParent())) {
							s.setParent(map_atual.get(s.getParent()));
						}
						
						s.setId(null);
						this.dao.persist((Serializable) s);
						quantity+=1;
						
						if(map_atual !=null) {
							map_atual.put(id_old, ((SimpleLogicalDeletableEntity) s).getId());
						}				
						sli.remove(s);
						break;
					}
				}
			} while(!sli.isEmpty());
			return;
		}
		
		
		
		Map<Long, StructureLevelInstance> structureLevelInstanceMap = new HashMap<>();
		Map<Long, Attribute> attributeMap = new HashMap<>();
				
		if(clazz.getSimpleName().equals("AttributeInstance")) {
			
			List<StructureLevelInstance> sli = retrieve(StructureLevelInstance.class);
			sli.stream().forEach(it->{
				structureLevelInstanceMap.put(it.getId(), it);
			});
			
			List<Attribute> attr = retrieve(Attribute.class);
			attr.stream().forEach(it->{
				attributeMap.put(it.getId(), it);
			});
		}
		

		for (int i = 0; i < array.size(); i++) {

			JSONObject jo = (JSONObject) array.get(i);

			Object obj = gson.fromJson(jo.toString(), clazz);
			long id_old_company;
			long id_old = 0;
			long id_old_structure;
			long id_old_planmacro;
			long id_old_structure_level;
			long id_old_structure_level_instance;
			long id_old_plan;
			long id_old_document_section;
			long id_old_attribute;
			long id_old_instance;
			boolean isDocument;
			Criteria criteria;

			switch (clazz.getSimpleName()) {
					
				case "PlanMacro" :

					id_old_company = Long.parseLong(jo.get("exportCompanyId").toString());
					
					criteria = this.dao.newCriteria(Company.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_company)));
					((PlanMacro) obj).setCompany((Company) criteria.uniqueResult());
					id_old=((PlanMacro) obj).getId();
					break;
					
				case "Plan":
					
					id_old_structure = Long.parseLong(jo.get("exportStructureId").toString());
					id_old_planmacro = Long.parseLong(jo.get("exportPlanMacroId").toString());
					
					criteria = this.dao.newCriteria(Structure.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure)));
					((Plan) obj).setStructure((Structure) criteria.uniqueResult());
		
					criteria = this.dao.newCriteria(PlanMacro.class);
					criteria.add(Restrictions.eq("id", map_fkey_2.get(id_old_planmacro)));
					((Plan) obj).setParent((PlanMacro) criteria.uniqueResult());
					id_old=((Plan) obj).getId();
					break;	
					
				case "PlanDetailed":
		
					id_old_plan = Long.parseLong(jo.get("exportPlanId").toString());
		
					criteria = this.dao.newCriteria(Plan.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_plan)));
					((PlanDetailed) obj).setPlan((Plan) criteria.uniqueResult());
					break;	
					
				case "Document":

					id_old_planmacro = Long.parseLong(jo.get("exportPlanMacroId").toString());
					
					criteria = this.dao.newCriteria(PlanMacro.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_planmacro)));
					((Document) obj).setPlan((PlanMacro) criteria.uniqueResult());
					id_old=((Document) obj).getId();
					break;
					
				case "DocumentSection":
					
					id_old_document_section = Long.parseLong(jo.get("exportDocumentSectionId").toString());
					long id_old_document = Long.parseLong(jo.get("exportDocumentId").toString());
					
					criteria = this.dao.newCriteria(DocumentSection.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_document_section)));
					((DocumentSection) obj).setParent((DocumentSection) criteria.uniqueResult());
					
					criteria = this.dao.newCriteria(Document.class);
					criteria.add(Restrictions.eq("id", map_fkey_2.get(id_old_document)));
					((DocumentSection) obj).setDocument((Document) criteria.uniqueResult());
					id_old=((DocumentSection) obj).getId();
					break;
					
				case "DocumentAttribute":
					
					id_old_document_section = Long.parseLong(jo.get("exportDocumentSectionId").toString());
					criteria = this.dao.newCriteria(DocumentSection.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_document_section)));
					((DocumentAttribute) obj).setSection((DocumentSection) criteria.uniqueResult());
					id_old=((DocumentAttribute) obj).getId();
					break;
					
				case "StructureLevel":
					
					id_old_structure = Long.parseLong(jo.get("exportStructureId").toString());
	
					criteria = this.dao.newCriteria(Structure.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure)));
					((StructureLevel) obj).setStructure((Structure) criteria.uniqueResult());
					id_old=((StructureLevel) obj).getId();
					break;
					
				case "ActionPlan" :

					id_old_structure_level_instance = Long.parseLong(jo.get("exportStructureLevelInstanceId").toString());
					
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure_level_instance)));
					((ActionPlan) obj).setLevelInstance((StructureLevelInstance) criteria.uniqueResult());
					break;
					
				case "LevelInstanceHistory" :

					id_old_structure_level_instance = Long.parseLong(jo.get("exportStructureLevelInstanceId").toString());
					
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure_level_instance)));
					((LevelInstanceHistory) obj).setLevelInstance((StructureLevelInstance) criteria.uniqueResult());
					break;
					
				case "StructureLevelInstanceDetailed" :
					
					id_old_structure_level_instance = Long.parseLong(jo.get("exportStructureLevelInstanceId").toString());
					
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure_level_instance)));
					((StructureLevelInstanceDetailed) obj).setLevelInstance((StructureLevelInstance) criteria.uniqueResult());
					break;
					
				case "Budget" :
					
					id_old_structure_level_instance = Long.parseLong(jo.get("exportStructureLevelInstanceId").toString());
					long id_old_budget_element = Long.parseLong(jo.get("exportBudgetElementId").toString());
					
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure_level_instance)));
					((Budget) obj).setLevelInstance((StructureLevelInstance) criteria.uniqueResult());
	
					criteria = this.dao.newCriteria(BudgetElement.class);
					criteria.add(Restrictions.eq("id", map_fkey_2.get(id_old_budget_element)));
					((Budget) obj).setBudgetElement((BudgetElement) criteria.uniqueResult());
					break;
					
				case "Attachment" :

					id_old_structure_level_instance = Long.parseLong(jo.get("exportStructureLevelInstanceId").toString());
					
					//atualiza id do autor que possui o mesmo email neste banco, se houver
					User autor=userBS.existsByEmail(jo.get("exportAuthorMail").toString());
					/*if (autor == null) {
						quantity+=1;
						continue;
					}*/
					((Attachment) obj).setAuthor(autor);
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure_level_instance)));
					((Attachment) obj).setLevelInstance((StructureLevelInstance) criteria.uniqueResult());
					break;
					
				case "Attribute" :
					
					id_old_structure_level = Long.parseLong(jo.get("exportStructureLevelId").toString());
										
					criteria = this.dao.newCriteria(StructureLevel.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure_level)));
					((Attribute) obj).setLevel((StructureLevel) criteria.uniqueResult());
					id_old=((Attribute) obj).getId();
					break;
					
				case "AttributeInstance" :
					
					id_old_structure_level_instance = Long.parseLong(jo.get("exportStructureLevelInstanceId").toString());
					id_old_attribute = Long.parseLong(jo.get("exportAttributeId").toString());
					
					//atualiza id do responsável que possui o mesmo email neste banco 
					if(attributeMap.get(map_fkey_2.get(id_old_attribute)).getLabel().equals("Responsável")) {
						String email=structureLevelInstanceMap.get(map_fkey_1.get(id_old_structure_level_instance)).getExportResponsibleMail();
						User user=userBS.existsByEmail(email);
						if (user == null) {
							quantity+=1;
							continue;
						}
						((AttributeInstance) obj).setValue(String.valueOf(user.getId()));
					}
					
					((AttributeInstance) obj).setLevelInstance(structureLevelInstanceMap.get(map_fkey_1.get(id_old_structure_level_instance)));
					((AttributeInstance) obj).setAttribute(attributeMap.get(map_fkey_2.get(id_old_attribute)));
					break;

				case "AggregateIndicator" :
					
					long id_old_aggregated = Long.parseLong(jo.get("exportAggregateId").toString());
					long id_old_indicator = Long.parseLong(jo.get("exportIndicatorId").toString());
					
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					StructureLevelInstance id_new_aggregated = (StructureLevelInstance) criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_aggregated))).uniqueResult();
					((AggregateIndicator) obj).setAggregate(id_new_aggregated);
					
					criteria = this.dao.newCriteria(StructureLevelInstance.class);
					StructureLevelInstance id_new_indicator = (StructureLevelInstance)  criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_indicator))).uniqueResult();
					((AggregateIndicator) obj).setIndicator(id_new_indicator);			
					break;
					
				case "TableFields" :
					
					id_old_attribute = Long.parseLong(jo.get("exportAttributeId").toString());
					isDocument=	Boolean.parseBoolean(jo.get("isDocument").toString());
					
					if(isDocument) {
						((TableFields) obj).setAttributeId(map_fkey_1.get(id_old_attribute));
					}else {
						((TableFields) obj).setAttributeId(map_fkey_2.get(id_old_attribute));
					}
					
					id_old=((TableFields) obj).getId();
					break;
			
				case "TableStructure" :
					
					id_old_structure = Long.parseLong(jo.get("exportTableFieldsId").toString());
					
					criteria = this.dao.newCriteria(TableFields.class);
					TableFields id_new_tablefield = (TableFields) criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure))).uniqueResult();
					((TableStructure) obj).setTableFields(id_new_tablefield);
					id_old=((TableStructure) obj).getId();
					break;
					
				case "TableInstance" :
					
					id_old_instance = Long.parseLong(jo.get("exportTableFieldsId").toString());
					
					criteria = this.dao.newCriteria(TableFields.class);
					id_new_tablefield = (TableFields) criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_instance))).uniqueResult();
					((TableInstance) obj).setTableFields(id_new_tablefield);
					id_old=((TableInstance) obj).getId();
					break;
					
				case "TableValues" :
					
					id_old_structure = Long.parseLong(jo.get("exportTableStructureId").toString());
					id_old_instance = Long.parseLong(jo.get("exportTableInstanceId").toString());
					
					criteria = this.dao.newCriteria(TableStructure.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure)));
					((TableValues) obj).setTableStructure((TableStructure) criteria.uniqueResult());
		
					criteria = this.dao.newCriteria(TableInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_2.get(id_old_instance)));
					((TableValues) obj).setTableInstance((TableInstance) criteria.uniqueResult());
					id_old=((TableValues) obj).getId();
					
					if(((TableValues) obj).getTableInstance() ==null) {
						LOGGER.warn("erro");
					}
					
					if(((TableValues) obj).getTableStructure() ==null) {
						LOGGER.warn("erro");
					}
					break;	

				case "Schedule" :
					
					id_old_attribute = Long.parseLong(jo.get("exportAttributeId").toString());
					isDocument=	Boolean.parseBoolean(jo.get("isDocument").toString());
					
					if(isDocument) {
						((Schedule) obj).setAttributeId(map_fkey_1.get(id_old_attribute));
					}else {
						((Schedule) obj).setAttributeId(map_fkey_2.get(id_old_attribute));
					}
					
					id_old=((Schedule) obj).getId();
					break;
			
				case "ScheduleStructure" :
					
					id_old_structure = Long.parseLong(jo.get("exportScheduleId").toString());
					
					criteria = this.dao.newCriteria(Schedule.class);
					Schedule id_new_schedule = (Schedule) criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure))).uniqueResult();
					((ScheduleStructure) obj).setSchedule(id_new_schedule);
					id_old=((ScheduleStructure) obj).getId();
					break;
					
				case "ScheduleInstance" :
					
					id_old_instance = Long.parseLong(jo.get("exportScheduleId").toString());
					
					criteria = this.dao.newCriteria(Schedule.class);
					id_new_schedule = (Schedule) criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_instance))).uniqueResult();
					((ScheduleInstance) obj).setSchedule(id_new_schedule);
					id_old=((ScheduleInstance) obj).getId();
					break;
					
				case "ScheduleValues" :
					
					id_old_structure = Long.parseLong(jo.get("exportScheduleStructureId").toString());
					id_old_instance = Long.parseLong(jo.get("exportScheduleInstanceId").toString());
					
					criteria = this.dao.newCriteria(ScheduleStructure.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old_structure)));
					((ScheduleValues) obj).setScheduleStructure((ScheduleStructure) criteria.uniqueResult());
		
					criteria = this.dao.newCriteria(ScheduleInstance.class);
					criteria.add(Restrictions.eq("id", map_fkey_2.get(id_old_instance)));
					((ScheduleValues) obj).setScheduleInstance((ScheduleInstance) criteria.uniqueResult());
					id_old=((ScheduleValues) obj).getId();
					break;	
					
				case "OptionsField":
					
					id_old_attribute = Long.parseLong(jo.get("exportAttributeId").toString());
					isDocument=	Boolean.parseBoolean(jo.get("isDocument").toString());
					if(isDocument) {
						((OptionsField) obj).setAttributeId(map_fkey_1.get(id_old_attribute));
					}else {
						((OptionsField) obj).setAttributeId(map_fkey_2.get(id_old_attribute));
					}
					break;
			}

			((SimpleLogicalDeletableEntity) clazz.cast(obj)).setId(null);

			this.dao.persist((Serializable) clazz.cast(obj));
			
			if(map_atual !=null) {
				map_atual.put(id_old, ((SimpleLogicalDeletableEntity) clazz.cast(obj)).getId());
			}
			
			quantity+=1;
		}
	}
	

	/**
	 * Ler Todos os arquivo do zip
	 * 
	 * @param file arquivo do upload
	 * 
	 * @return int quantidade de registros
	 * 
	 * @throws IOException 
	 * @throws ParseException 
	 *
	 */
	public int quantityTotal(List<File> files) throws IOException, ParseException {
		
		int count=0;
		for (File f : files) {
		
			if(!f.getName().split(".json")[0].equals("Company")) {
				String register = IOUtils.toString(new FileInputStream(f), StandardCharsets.UTF_8);
				JSONParser parser = new JSONParser();
				JSONArray array;
			
				array = (JSONArray) parser.parse(register);
				for (int i = 0; i < array.size(); i++) {
					count+=1;
				}
			}
		}
			
		return count;
	}
	
	
	/**
	 * Recuperar Todos os arquivo do zip
	 * 
	 * @param InputStream arquivo do upload
	 * 
	 * @return List<File> lista de arquivos do zip
	 * 
	 * @throws IOException
	 *
	 */
	private List<File> decompact(InputStream inputStream) throws IOException {

		List<File> files = new ArrayList<File>();
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(inputStream);
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			String fileName = zipEntry.getName().split("_")[0];
			File newFile = File.createTempFile(fileName + "_", ".temp");
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			files.add(newFile);
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();

		return files;
	}
	
	/**
	 * Adiciona arquivo no zip
	 * 
	 * @param ZipOutputStream arquivo zip
	 * 
	 * @param className       nome da classe
	 * 
	 * @param content         json a ser salvo
	 * 
	 * @throws IOException
	 *
	 */
	private void zipAdd(ZipOutputStream zos, String className, String content) throws IOException {
		LOGGER.infof("Writing JSON for %s...", className);
		ZipEntry entry = new ZipEntry(String.format("%s.json", className));
		zos.putNextEntry(entry);
		zos.write(content.getBytes("UTF-8"));
		zos.closeEntry();
	}

	
	
	/**
	 * Lista Modelo
	 * 
	 * @param List<clazz> Classe do modelo no banco
	 * 
	 * @return String resultado da busca
	 */
	private <E extends Serializable> List<E> retrieve(Class<E> clazz) {
		Criteria criteria = this.dao.newCriteria(clazz);
		return this.dao.findByCriteria(criteria, clazz);
	}

	public int getQuantity() {
		return quantity;
	}
	
	public int getPorcentagem(){
		if(getQuantityTotal()==0) {
			return -1;
		}
		return Math.floorDiv(100*getQuantity(), getQuantityTotal());
	}

	public int getQuantityTotal(){
		return quantityTotal;
	}

	public void setQuantityTotal(int q){
		quantityTotal = q;
	}

	public void resetQuantity() {
		quantity=0;
		quantityTotal=0;
	}

}