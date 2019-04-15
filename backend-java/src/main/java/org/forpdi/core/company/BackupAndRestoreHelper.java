 	package org.forpdi.core.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.event.Current;
import org.forpdi.core.exception.RestoreException;
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
import org.forpdi.system.Archive;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
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
				attr.setExportDocumentSectionId(attr.getSection().getId());
				attr.setSection(null);
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
		final Company company = this.domain.getCompany();
		if (company == null) {
			throw new IllegalArgumentException("Você precisa criar uma instituição e um domínio antes de importar dados.");
		}
		
		final Map<String, File> files = this.uncompress(file.getFile());
		final Map<Long, BudgetElement> budgetElements = new LinkedHashMap<>();
		final Map<Long, PlanMacro> plansMacro = new LinkedHashMap<>();
		final Map<Long, Document> documents = new LinkedHashMap<>();
		final Map<Long, DocumentSection> documentSections = new LinkedHashMap<>();
		final Map<Long, DocumentAttribute> documentAttributes = new LinkedHashMap<>();
		final Map<Long, Structure> structures = new LinkedHashMap<>();
		final Map<Long, StructureLevel> structureLevels = new LinkedHashMap<>();
		final Map<Long, Attribute> attributes = new LinkedHashMap<>();
		final Map<Long, Plan> plans = new LinkedHashMap<>();
		final Map<Long, StructureLevelInstance> structureLevelInstances = new LinkedHashMap<>();
		final Map<Long, Schedule> schedules = new LinkedHashMap<>();
		final Map<Long, ScheduleInstance> scheduleInstances = new LinkedHashMap<>();
		final Map<Long, ScheduleStructure> scheduleStructures = new LinkedHashMap<>();
		final Map<Long, TableFields> tableFields = new LinkedHashMap<>();
		final Map<Long, TableInstance> tableInstances = new LinkedHashMap<>();
		final Map<Long, TableStructure> tableStructures = new LinkedHashMap<>();

		this.dao.execute((session) -> {
			String content;
			
			// Importando elementos orçamentários
			content = this.readFromFile(files, BudgetElement.class);
			List<BudgetElement> budgetElementsList = this.gson.fromJson(content, new TypeToken<List<BudgetElement>>() {}.getType());
			if (!GeneralUtils.isEmpty(budgetElementsList)) {
				budgetElementsList.forEach((budgetElement) -> {
					final Long oldId = budgetElement.getId();
					budgetElement.setId(null);
					budgetElement.setCompany(company);
					session.persist(budgetElement);
					budgetElements.put(oldId, budgetElement);
				});
			}

			// Importando mensagens customizadas
			content = this.readFromFile(files, CompanyMessage.class);
			List<CompanyMessage> companyMessagesList = this.gson.fromJson(content, new TypeToken<List<CompanyMessage>>() {}.getType());
			if (!GeneralUtils.isEmpty(companyMessagesList)) {
				companyMessagesList.forEach((companyMessage) -> {
					this.companyBS.updateMessageOverlay(session, company, companyMessage.getMessageKey(), companyMessage.getMessageValue());
				});
			}
			
			// Importando os planos macro
			content = this.readFromFile(files, PlanMacro.class);
			List<PlanMacro> plansMacroList = this.gson.fromJson(content, new TypeToken<List<PlanMacro>>() {}.getType());
			if (!GeneralUtils.isEmpty(plansMacroList)) {
				plansMacroList.forEach((planMacro) -> {
					final Long oldId = planMacro.getId();
					planMacro.setId(null);
					planMacro.setCompany(company);
					session.persist(planMacro);
					plansMacro.put(oldId, planMacro);
				});
			}

			// Importando os documentos
			content = this.readFromFile(files, Document.class);
			List<Document> documentsList = this.gson.fromJson(content, new TypeToken<List<Document>>() {}.getType());
			if (!GeneralUtils.isEmpty(documentsList)) {
				documentsList.forEach((document) -> {
					final Long oldId = document.getId();
					document.setId(null);
					document.setPlan(plansMacro.get(document.getExportPlanMacroId()));
					session.persist(document);
					documents.put(oldId, document);
				});
			}

			// Importando as seções dos documentos
			content = this.readFromFile(files, DocumentSection.class);
			List<DocumentSection> documentSectionsList = this.gson.fromJson(content, new TypeToken<List<DocumentSection>>() {}.getType());
			if (!GeneralUtils.isEmpty(documentSectionsList)) {
				documentSectionsList.forEach((documentSection) -> {
					final Long oldId = documentSection.getId();
					documentSection.setId(null);
					documentSection.setDocument(documents.get(documentSection.getExportDocumentId()));
					session.persist(documentSection);
					documentSections.put(oldId, documentSection);
				});
				documentSectionsList.forEach((documentSection) -> {
					documentSection.setParent(documentSections.get(documentSection.getExportDocumentSectionId()));
					session.persist(documentSection);
				});
			}

			// Importando os atributos dos documentos
			content = this.readFromFile(files, DocumentAttribute.class);
			List<DocumentAttribute> documentAttributesList = this.gson.fromJson(content, new TypeToken<List<DocumentAttribute>>() {}.getType());
			if (!GeneralUtils.isEmpty(documentAttributesList)) {
				documentAttributesList.forEach((documentAttribute) -> {
					final Long oldId = documentAttribute.getId();
					documentAttribute.setId(null);
					documentAttribute.setSection(documentSections.get(documentAttribute.getExportDocumentSectionId()));
					session.persist(documentAttribute);
					documentAttributes.put(oldId, documentAttribute);
				});
			}

			// Importando as estruturas
			content = this.readFromFile(files, Structure.class);
			List<Structure> structuresList = this.gson.fromJson(content, new TypeToken<List<Structure>>() {}.getType());
			if (!GeneralUtils.isEmpty(structuresList)) {
				structuresList.forEach((structure) -> {
					final Long oldId = structure.getId();
					structure.setId(null);
					structure.setCompany(company);
					session.persist(structure);
					structures.put(oldId, structure);
				});
			}

			// Importando os níveis de estruturas
			content = this.readFromFile(files, StructureLevel.class);
			List<StructureLevel> structureLevelsList = this.gson.fromJson(content, new TypeToken<List<StructureLevel>>() {}.getType());
			if (!GeneralUtils.isEmpty(structureLevelsList)) {
				structureLevelsList.forEach((structureLevel) -> {
					final Long oldId = structureLevel.getId();
					structureLevel.setId(null);
					structureLevel.setStructure(structures.get(structureLevel.getExportStructureId()));
					session.persist(structureLevel);
					structureLevels.put(oldId, structureLevel);
				});
			}

			// Importando os atributos
			content = this.readFromFile(files, Attribute.class);
			List<Attribute> attributesList = this.gson.fromJson(content, new TypeToken<List<Attribute>>() {}.getType());
			if (!GeneralUtils.isEmpty(attributesList)) {
				attributesList.forEach((attribute) -> {
					final Long oldId = attribute.getId();
					attribute.setId(null);
					attribute.setLevel(structureLevels.get(attribute.getExportStructureLevelId()));
					session.persist(attribute);
					attributes.put(oldId, attribute);
				});
			}

			// Importando os planos
			content = this.readFromFile(files, Plan.class);
			List<Plan> plansList = this.gson.fromJson(content, new TypeToken<List<Plan>>() {}.getType());
			if (!GeneralUtils.isEmpty(plansList)) {
				plansList.forEach((plan) -> {
					final Long oldId = plan.getId();
					plan.setId(null);
					plan.setParent(plansMacro.get(plan.getExportPlanMacroId()));
					plan.setStructure(structures.get(plan.getExportStructureId()));
					session.persist(plan);
					plans.put(oldId, plan);
				});
			}

			// Importando os detalhes dos planos
			content = this.readFromFile(files, PlanDetailed.class);
			List<PlanDetailed> plansDetailedList = this.gson.fromJson(content, new TypeToken<List<PlanDetailed>>() {}.getType());
			if (!GeneralUtils.isEmpty(plansDetailedList)) {
				plansDetailedList.forEach((planDetailed) -> {
					planDetailed.setId(null);
					planDetailed.setPlan(plans.get(planDetailed.getExportPlanId()));
					session.persist(planDetailed);
				});
			}

			// Importando as instâncias de nível de estrutura
			content = this.readFromFile(files, StructureLevelInstance.class);
			List<StructureLevelInstance> structureLevelInstancesList = this.gson.fromJson(content, new TypeToken<List<StructureLevelInstance>>() {}.getType());
			if (!GeneralUtils.isEmpty(structureLevelInstancesList)) {
				structureLevelInstancesList.forEach((levelInstance) -> {
					final Long oldId = levelInstance.getId();
					levelInstance.setId(null);
					levelInstance.setLevel(structureLevels.get(levelInstance.getExportLevelId()));
					levelInstance.setPlan(plans.get(levelInstance.getExportPlanId()));
					
					if(levelInstance.getParent() != null) {
						StructureLevelInstance sli = structureLevelInstances.get(levelInstance.getParent());
						levelInstance.setParent(sli.getId());
					}
						
					session.persist(levelInstance);
					structureLevelInstances.put(oldId, levelInstance);
				});
			}

			// Importando os detalhes das instâncias de nível de estrutura
			content = this.readFromFile(files, StructureLevelInstanceDetailed.class);
			List<StructureLevelInstanceDetailed> structureLevelInstancesDetailedList = this.gson.fromJson(content, new TypeToken<List<StructureLevelInstanceDetailed>>() {}.getType());
			if (!GeneralUtils.isEmpty(structureLevelInstancesDetailedList)) {
				structureLevelInstancesDetailedList.forEach((levelInstanceDetailed) -> {
					levelInstanceDetailed.setId(null);
					levelInstanceDetailed.setLevelInstance(structureLevelInstances.get(levelInstanceDetailed.getExportStructureLevelInstanceId()));
					session.persist(levelInstanceDetailed);
				});
			}

			// Importando o histórico das instâncias de nível de estrutura
			content = this.readFromFile(files, LevelInstanceHistory.class);
			List<LevelInstanceHistory> levelInstancesHistoryList = this.gson.fromJson(content, new TypeToken<List<LevelInstanceHistory>>() {}.getType());
			if (!GeneralUtils.isEmpty(levelInstancesHistoryList)) {
				levelInstancesHistoryList.forEach((levelInstanceHistory) -> {
					levelInstanceHistory.setId(null);
					levelInstanceHistory.setLevelInstance(structureLevelInstances.get(levelInstanceHistory.getExportStructureLevelInstanceId()));
					session.persist(levelInstanceHistory);
				});
			}

			// Importando os planos de ação
			content = this.readFromFile(files, ActionPlan.class);
			List<ActionPlan> actionPlansList = this.gson.fromJson(content, new TypeToken<List<ActionPlan>>() {}.getType());
			if (!GeneralUtils.isEmpty(actionPlansList)) {
				actionPlansList.forEach((actionPlan) -> {
					actionPlan.setId(null);
					actionPlan.setLevelInstance(structureLevelInstances.get(actionPlan.getExportStructureLevelInstanceId()));
					session.persist(actionPlan);
				});
			}

			// Importando os indicadores agregados
			content = this.readFromFile(files, AggregateIndicator.class);
			List<AggregateIndicator> aggregateIndicatorsList = this.gson.fromJson(content, new TypeToken<List<AggregateIndicator>>() {}.getType());
			if (!GeneralUtils.isEmpty(aggregateIndicatorsList)) {
				aggregateIndicatorsList.forEach((aggregateIndicator) -> {
					aggregateIndicator.setId(null);
					aggregateIndicator.setAggregate(structureLevelInstances.get(aggregateIndicator.getExportAggregateId()));
					aggregateIndicator.setIndicator(structureLevelInstances.get(aggregateIndicator.getExportIndicatorId()));
					session.persist(aggregateIndicator);
				});
			}

			// Importando os anexos
			content = this.readFromFile(files, Attachment.class);
			List<Attachment> attachmentsList = this.gson.fromJson(content, new TypeToken<List<Attachment>>() {}.getType());
			if (!GeneralUtils.isEmpty(attachmentsList)) {
				attachmentsList.forEach((attachment) -> {
					attachment.setId(null);
					attachment.setLevelInstance(structureLevelInstances.get(attachment.getExportStructureLevelInstanceId()));
					final String authorEmail = attachment.getExportAuthorMail();
					final User author = this.userBS.existsByEmail(authorEmail);
					if (author != null) {
						attachment.setAuthor(author);
					} else {
						LOGGER.warnf("Ignoring unexistent attachment author: %s", authorEmail);
					}
					session.persist(attachment);
				});
			}
	
			// Importando os orçamentos
			content = this.readFromFile(files, Budget.class);
			List<Budget> budgetsList = this.gson.fromJson(content, new TypeToken<List<Budget>>() {}.getType());
			if (!GeneralUtils.isEmpty(budgetsList)) {
				budgetsList.forEach((budget) -> {
					budget.setId(null);
					budget.setBudgetElement(budgetElements.get(budget.getExportBudgetElementId()));
					budget.setLevelInstance(structureLevelInstances.get(budget.getExportStructureLevelInstanceId()));
					session.persist(budget);
				});
			}

			// Importando as instâncias de atributos
			content = this.readFromFile(files, AttributeInstance.class);
			List<AttributeInstance> attributeInstancesList = this.gson.fromJson(content, new TypeToken<List<AttributeInstance>>() {}.getType());
			if (!GeneralUtils.isEmpty(attributeInstancesList)) {
				attributeInstancesList.forEach((attributeInstance) -> {
					attributeInstance.setId(null);
					attributeInstance.setAttribute(attributes.get(attributeInstance.getExportAttributeId()));
					attributeInstance.setLevelInstance(structureLevelInstances.get(attributeInstance.getExportStructureLevelInstanceId()));
					if ("Responsável".equals(attributeInstance.getAttribute().getLabel())) {
						User user = this.userBS.existsByEmail(attributeInstance.getLevelInstance().getExportResponsibleMail());
						if (user != null) {
							attributeInstance.setValue(user.getId().toString());
							attributeInstance.setValueAsNumber(user.getId().doubleValue());
						} else {
							LOGGER.warnf("Usuário responsável não encontrado: %s", attributeInstance.getLevelInstance().getExportResponsibleMail());
						}
					}
					session.persist(attributeInstance);
				});
			}

			// Importando os campos de opções
			content = this.readFromFile(files, OptionsField.class);
			List<OptionsField> optionsFieldsList = this.gson.fromJson(content, new TypeToken<List<OptionsField>>() {}.getType());
			if (!GeneralUtils.isEmpty(optionsFieldsList)) {
				optionsFieldsList.forEach((optionsField) -> {
					optionsField.setId(null);
					if (optionsField.isDocument()) {
						optionsField.setAttributeId(documentAttributes.get(optionsField.getAttributeId()).getId());
					} else {
						optionsField.setAttributeId(attributes.get(optionsField.getAttributeId()).getId());
					}
					session.persist(optionsField);
				});
			}

			// Importando os cronogramas
			content = this.readFromFile(files, Schedule.class);
			List<Schedule> schedulesList = this.gson.fromJson(content, new TypeToken<List<Schedule>>() {}.getType());
			if (!GeneralUtils.isEmpty(schedulesList)) {
				schedulesList.forEach((schedule) -> {
					final Long oldId = schedule.getId();
					schedule.setId(null);
					if (schedule.isDocument()) {
						schedule.setAttributeId(documentAttributes.get(schedule.getAttributeId()).getId());
					} else {
						schedule.setAttributeId(attributes.get(schedule.getAttributeId()).getId());
					}
					session.persist(schedule);
					schedules.put(oldId, schedule);
				});
			}

			// Importando as instâncias de cronogramas
			content = this.readFromFile(files, ScheduleInstance.class);
			List<ScheduleInstance> scheduleInstancesList = this.gson.fromJson(content, new TypeToken<List<ScheduleInstance>>() {}.getType());
			if (!GeneralUtils.isEmpty(scheduleInstancesList)) {
				scheduleInstancesList.forEach((scheduleInstance) -> {
					final Long oldId = scheduleInstance.getId();
					scheduleInstance.setId(null);
					scheduleInstance.setSchedule(schedules.get(scheduleInstance.getExportScheduleId()));
					session.persist(scheduleInstance);
					scheduleInstances.put(oldId, scheduleInstance);
				});
			}

			// Importando as estruturas de cronogramas
			content = this.readFromFile(files, ScheduleStructure.class);
			List<ScheduleStructure> scheduleStructuresList = this.gson.fromJson(content, new TypeToken<List<ScheduleStructure>>() {}.getType());
			if (!GeneralUtils.isEmpty(scheduleStructuresList)) {
				scheduleStructuresList.forEach((scheduleStructure) -> {
					final Long oldId = scheduleStructure.getId();
					scheduleStructure.setId(null);
					scheduleStructure.setSchedule(schedules.get(scheduleStructure.getExportScheduleId()));
					session.persist(scheduleStructure);
					scheduleStructures.put(oldId, scheduleStructure);
				});
			}

			// Importando os valores de cronogramas
			content = this.readFromFile(files, ScheduleValues.class);
			List<ScheduleValues> scheduleValuesList = this.gson.fromJson(content, new TypeToken<List<ScheduleValues>>() {}.getType());
			if (!GeneralUtils.isEmpty(scheduleValuesList)) {
				scheduleValuesList.forEach((scheduleValue) -> {
					scheduleValue.setId(null);
					scheduleValue.setScheduleInstance(scheduleInstances.get(scheduleValue.getExportScheduleInstanceId()));
					scheduleValue.setScheduleStructure(scheduleStructures.get(scheduleValue.getExportScheduleStructureId()));
					session.persist(scheduleValue);
				});
			}

			// Importando as tabelas
			content = this.readFromFile(files, TableFields.class);
			List<TableFields> tableFieldsList = this.gson.fromJson(content, new TypeToken<List<TableFields>>() {}.getType());
			if (!GeneralUtils.isEmpty(tableFieldsList)) {
				tableFieldsList.forEach((tableField) -> {
					final Long oldId = tableField.getId();
					tableField.setId(null);
					if (tableField.isDocument()) {
						tableField.setAttributeId(documentAttributes.get(tableField.getAttributeId()).getId());
					} else {
						tableField.setAttributeId(attributes.get(tableField.getAttributeId()).getId());
					}
					session.persist(tableField);
					tableFields.put(oldId, tableField);
				});
			}

			// Importando as instâncias de tabelas
			content = this.readFromFile(files, TableInstance.class);
			List<TableInstance> tableInstancesList = this.gson.fromJson(content, new TypeToken<List<TableInstance>>() {}.getType());
			if (!GeneralUtils.isEmpty(tableInstancesList)) {
				tableInstancesList.forEach((tableInstance) -> {
					final Long oldId = tableInstance.getId();
					tableInstance.setId(null);
					tableInstance.setTableFields(tableFields.get(tableInstance.getExportTableFieldsId()));
					session.persist(tableInstance);
					tableInstances.put(oldId, tableInstance);
				});
			}

			// Importando as estruturas de tabelas
			content = this.readFromFile(files, TableStructure.class);
			List<TableStructure> tableStructuresList = this.gson.fromJson(content, new TypeToken<List<TableStructure>>() {}.getType());
			if (!GeneralUtils.isEmpty(tableStructuresList)) {
				tableStructuresList.forEach((tableStructure) -> {
					final Long oldId = tableStructure.getId();
					tableStructure.setId(null);
					tableStructure.setTableFields(tableFields.get(tableStructure.getExportTableFieldsId()));
					session.persist(tableStructure);
					tableStructures.put(oldId, tableStructure);
				});
			}

			// Importando os valores de tabelas
			content = this.readFromFile(files, TableValues.class);
			List<TableValues> tableValuesList = this.gson.fromJson(content, new TypeToken<List<TableValues>>() {}.getType());
			if (!GeneralUtils.isEmpty(tableValuesList)) {
				tableValuesList.forEach((tableValue) -> {
					tableValue.setId(null);
					tableValue.setTableInstance(tableInstances.get(tableValue.getExportTableInstanceId()));
					tableValue.setTableStructure(tableStructures.get(tableValue.getExportTableStructureId()));
					session.persist(tableValue);
				});
			}

		});
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
	private Map<String, File> uncompress(InputStream inputStream) throws IOException {
		Map<String, File> files = new LinkedHashMap<>(30);
		ZipInputStream zis = new ZipInputStream(inputStream);
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			final String filename = zipEntry.getName();
			LOGGER.infof("Reading zip entry: %s", filename);
			final String className = filename.split("\\.")[0];
			final File newFile = File.createTempFile(className, ".json");
			final FileOutputStream fos = new FileOutputStream(newFile);
			IOUtils.copy(zis, fos);
			fos.close();
			files.put(className, newFile);
			zis.closeEntry();
			zipEntry = zis.getNextEntry();
		}
		zis.close();

		return files;
	}
	
	private String readFromFile(Map<String, File> files, Class<?> clazz) {
		final File file = files.get(clazz.getSimpleName());
		if (file == null) {
			LOGGER.infof("Nenhum arquivo de dados para entidade: %s", clazz.getSimpleName());
			return "[]";
		}
		LOGGER.infof("Importing entity: %s", clazz.getSimpleName());
		try {
			final FileInputStream fis = new FileInputStream(file);
			final String content = IOUtils.toString(fis, StandardCharsets.UTF_8);
			fis.close();
			return content;
		} catch(IOException e) {
			throw new RestoreException(e);
		}
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

	public Archive listFilebyName(String fileName) {

		Criteria criteria = this.dao.newCriteria(Archive.class).add(Restrictions.eq("deleted", false))								
				.add(Restrictions.eq("name", fileName));
		criteria.setMaxResults(1);
		
		Archive file=(Archive)criteria.uniqueResult();// this.dao.findByCriteria(criteria, Archive.class);
		
		return file;
	}
}