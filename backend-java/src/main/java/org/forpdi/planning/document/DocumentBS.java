package org.forpdi.planning.document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.properties.SystemConfigs;
import org.forpdi.core.user.User;
import org.forpdi.core.user.UserBS;
import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.attribute.AttributeHelper;
import org.forpdi.planning.attribute.AttributeInstance;
import org.forpdi.planning.attribute.AttributeTypeFactory;
import org.forpdi.planning.attribute.AttributeTypeWrapper;
import org.forpdi.planning.attribute.types.ActionPlanField;
import org.forpdi.planning.attribute.types.BudgetField;
import org.forpdi.planning.attribute.types.Currency;
import org.forpdi.planning.attribute.types.NumberField;
import org.forpdi.planning.attribute.types.Percentage;
import org.forpdi.planning.attribute.types.ResponsibleField;
import org.forpdi.planning.attribute.types.ScheduleField;
import org.forpdi.planning.attribute.types.SelectField;
import org.forpdi.planning.attribute.types.SelectPlan;
import org.forpdi.planning.attribute.types.StrategicObjective;
import org.forpdi.planning.attribute.types.TableField;
import org.forpdi.planning.attribute.types.TextArea;
import org.forpdi.planning.attribute.types.enums.FormatValue;
import org.forpdi.planning.fields.FieldsBS;
import org.forpdi.planning.fields.OptionsField;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.fields.budget.BudgetDTO;
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
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.StructureBS;
import org.forpdi.planning.structure.StructureHelper;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@RequestScoped
public class DocumentBS extends HibernateBusiness {

	@Inject
	private FieldsBS fieldsBS;
	@Inject
	private PlanBS planBS;
	@Inject
	private StructureBS structureBS;
	@Inject
	private AttributeHelper attrHelper;
	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private UserBS userBS;
	@Inject
	StructureHelper structHelper;

	/**
	 * Retorna o documento referente ao plano passado por parâmetro.
	 * 
	 * @param plan
	 *            O plano do qual se pretende obter o documento.
	 * @return Document O documento referente ao plano passado por parâmetro
	 */
	public Document retrieveByPlan(PlanMacro plan) {

		Criteria criteria = this.dao.newCriteria(Document.class);
		criteria.add(Restrictions.eq("plan", plan)).add(Restrictions.eq("deleted", false));
		Document document = (Document) criteria.uniqueResult();

		return document;
	}

	/**
	 * Cria uma seção no documento
	 * 
	 * @param document
	 *            O documento no qual se deseja inserir a seção.
	 * @param name
	 *            O nome da seção a ser adicionada.
	 * @param parentId
	 *            O id da seção pai, caso haja. Caso contrário deve-se passar
	 *            null.
	 * @return DocumentSection A seção criada.
	 */
	public DocumentSection createSection(Document document, String name, Long parentId) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("document", document)).add(Restrictions.eq("deleted", false))
				.setProjection(Projections.max("sequence"));
		DocumentSection parent = null;
		if (parentId == null || parentId <= 0) {
			criteria.add(Restrictions.isNull("parent"));
		} else {
			criteria.add(Restrictions.eq("parent.id", parentId));
			parent = this.exists(parentId, DocumentSection.class);
		}
		Integer lastSeq = (Integer) criteria.uniqueResult();
		if (lastSeq == null || lastSeq < 0L) {
			lastSeq = 0;
		}

		DocumentSection sec = new DocumentSection();
		sec.setName(name);
		sec.setDeleted(false);
		sec.setDocument(document);
		sec.setSequence(lastSeq + 1);
		sec.setParent(parent);
		sec.setLeaf(true);
		this.persist(sec);
		if (parent != null) {
			parent.setLeaf(false);
			this.persist(parent);
		}
		return sec;
	}

	/**
	 * Cria um atributo em uma seção do documento.
	 * 
	 * @param section
	 *            A seção na qual se deseja adicionar o atributo.
	 * @param name
	 *            O nome do atributo.
	 * @param type
	 *            O tipo do atributo.
	 * @param periodicity
	 *            A periodicidade do atributo, se houver.
	 * @return DocumentAttribute O atributo criado.
	 */
	public DocumentAttribute createAttribute(DocumentSection section, String name, String type, Boolean periodicity) {
		Criteria criteria = this.dao.newCriteria(DocumentAttribute.class);
		criteria.add(Restrictions.eq("section", section)).add(Restrictions.eq("deleted", false))
				.setProjection(Projections.max("sequence"));
		Integer lastSeq = (Integer) criteria.uniqueResult();
		if (lastSeq == null || lastSeq < 0L) {
			lastSeq = 0;
		}

		DocumentAttribute attr = new DocumentAttribute();
		attr.setName(name);
		attr.setType(type);
		attr.setDeleted(false);
		attr.setSection(section);
		attr.setSequence(lastSeq + 1);

		this.persist(attr);
		if (attr.getType().equals(ScheduleField.class.getCanonicalName())) {
			Schedule schedule = new Schedule();
			schedule.setAttributeId(attr.getId());
			schedule.setDeleted(false);
			schedule.setIsDocument(true);
			schedule.setPeriodicityEnable(periodicity);
			this.persist(schedule);
			attr.setSchedule(schedule);
		} else if (attr.getType().equals(TableField.class.getCanonicalName())) {
			TableFields tableFields = new TableFields();
			tableFields.setAttributeId(attr.getId());
			tableFields.setDeleted(false);
			tableFields.setIsDocument(true);
			this.persist(tableFields);
			attr.setTableFields(tableFields);
		} else {
			AttributeTypeWrapper attributeTypeWrapper = AttributeTypeFactory.getInstance().get(attr.getType())
					.getWrapper();
			if (attr.getValueAsDate() != null)
				attr.setValue(attributeTypeWrapper.fromDatabaseDate(attr.getValueAsDate()));
			else if (attr.getValueAsNumber() != null)
				attr.setValue(attributeTypeWrapper.fromDatabaseNumerical(attr.getValueAsNumber()));
			else
				attr.setValue(attributeTypeWrapper.fromDatabase(attr.getValue()));

			// Carrega todos os planos de metas para exibicao no select box nos
			// documentos
			if (attr.getType().equals(SelectPlan.class.getCanonicalName())) {
				PaginatedList<Plan> plans = this.planBS.listPlans(section.getDocument().getPlan(), false, 0, null, null,
						0);
				attr.setSelectPlans(plans.getList());
			}
		}
		return attr;
	}

	/**
	 * Lista seções de um documento para exibir na tela.
	 * 
	 * @param document
	 *            Documento do qual se deseja obter as seções.
	 * @param parentId
	 *            Id da seção pai, no caso de listar subseções.
	 * @return List<DocumentSectionDTO> Lista de seções de um documento para
	 *         exibir na tela.
	 */
	public List<DocumentSectionDTO> listSectionDTOsByDocument(Document document, Long parentId) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("document", document)).add(Restrictions.eq("deleted", false))
				.addOrder(Order.desc("preTextSection")).addOrder(Order.asc("sequence"));
		if (parentId == null || parentId <= 0L) {
			criteria.add(Restrictions.isNull("parent"));
		} else {
			criteria.add(Restrictions.eq("parent.id", parentId));
		}

		List<DocumentSection> raw = this.dao.findByCriteria(criteria, DocumentSection.class);
		if (raw == null || raw.size() <= 0)
			return Collections.emptyList();
		ArrayList<DocumentSectionDTO> list = new ArrayList<DocumentSectionDTO>(raw.size());
		for (DocumentSection section : raw) {
			list.add(new DocumentSectionDTO(section, this));
		}
		return list;
	}

	/**
	 * Recupera uma seção do documento pelo id
	 * 
	 * @param sectionId
	 *            Id da seção do documento
	 * @return documentSection Seção do documento
	 */
	public DocumentSection retrieveSectionById(Long sectionId) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("id", sectionId)).add(Restrictions.eq("deleted", false));
		DocumentSection documentSection = (DocumentSection) criteria.uniqueResult();
		return documentSection;
	}

	/**
	 * Retorna o número de atributos da seção
	 * 
	 * @param documentSection
	 *            Seção do documento
	 * @return
	 */
	public Long countAttributesPerSection(DocumentSection documentSection) {
		Criteria count = this.dao.newCriteria(DocumentAttribute.class);
		count.add(Restrictions.eq("section", documentSection)).add(Restrictions.eq("deleted", false))
				.setProjection(Projections.countDistinct("id"));

		return (Long) count.uniqueResult();
	}

	/**
	 * Retorna a lista de atributos de uma seção do documento
	 * 
	 * @param documentSection
	 *            Seção do documento
	 * @param planId
	 *            Id do Plano
	 * @return List<DocumentAttribute> list Lista de atributos
	 */
	public List<DocumentAttribute> listAttributesBySection(DocumentSection documentSection, Long planId) {
		Criteria criteria = this.dao.newCriteria(DocumentAttribute.class);
		criteria.add(Restrictions.eq("section", documentSection)).add(Restrictions.eq("deleted", false))
				.addOrder(Order.asc("sequence"));

		List<DocumentAttribute> list = this.dao.findByCriteria(criteria, DocumentAttribute.class);

		for (DocumentAttribute documentAttribute : list) {
			if (documentAttribute.getType().equals(ScheduleField.class.getCanonicalName())) {
				Schedule schedule = this.fieldsBS.scheduleByAttribute(documentAttribute.getId(), true);
				documentAttribute.setSchedule(schedule);
			} else if (documentAttribute.getType().equals(TableField.class.getCanonicalName())) {
				TableFields tableFields = this.fieldsBS.tableFieldsByAttribute(documentAttribute.getId(), true);
				if (tableFields == null)
					continue;
				for (TableStructure tableStructure : tableFields.getTableStructures()) {
					if (tableStructure.getType().equals(SelectField.class.getCanonicalName())) {
						tableStructure.setOptionsField(this.fieldsBS.getOptionsForColumn(documentAttribute.getId(),
								tableStructure.getId(), true));
					}
				}
				documentAttribute.setTableFields(tableFields);
			} else if (documentAttribute.getType().equals(StrategicObjective.class.getCanonicalName())) {
				if (planId != null) {

					Plan plan = this.planBS.retrieveById(planId);
					PaginatedList<Plan> plans = new PaginatedList<Plan>();
					ArrayList<Plan> plansAux = new ArrayList<Plan>();
					plansAux.add(plan);
					plans.setList(plansAux);
					plans.setTotal((long) plansAux.size());

					PaginatedList<StructureLevelInstance> levelInstances = this.structureBS.listObjective(plans);
					for (int i = 0; i < levelInstances.getList().size(); i++) {
						levelInstances.getList().get(i).getLevel().setAttributes(
								this.structureBS.retrieveLevelSonsAttributes(levelInstances.getList().get(i)));
					}
					documentAttribute.setStrategicObjectives(levelInstances.getList());
				} else {
					Long planMacroId = documentSection.getDocument().getPlan().getId();
					PlanMacro planMacro = this.planBS.retrievePlanMacroById(planMacroId);
					PaginatedList<Plan> plans = this.planBS.listPlans(planMacro, false, 0, null, null, 1);
					if (!plans.getList().isEmpty()) {
						PaginatedList<StructureLevelInstance> levelInstances = this.structureBS.listObjective(plans);
						for (int i = 0; i < levelInstances.getList().size(); i++) {
							levelInstances.getList().get(i).getLevel().setAttributes(
									this.structureBS.retrieveLevelSonsAttributes(levelInstances.getList().get(i)));
						}
						documentAttribute.setStrategicObjectives(levelInstances.getList());
					}

				}
				PaginatedList<Plan> plans = this.planBS.listPlans(documentSection.getDocument().getPlan(), false, 0,
						null, null, 0);

				documentAttribute.setSelectPlans(plans.getList());

			} else {
				AttributeTypeWrapper attributeTypeWrapper = AttributeTypeFactory.getInstance()
						.get(documentAttribute.getType()).getWrapper();
				if (documentAttribute.getValueAsDate() != null)
					documentAttribute
							.setValue(attributeTypeWrapper.fromDatabaseDate(documentAttribute.getValueAsDate()));
				else if (documentAttribute.getValueAsNumber() != null)
					documentAttribute
							.setValue(attributeTypeWrapper.fromDatabaseNumerical(documentAttribute.getValueAsNumber()));
				else
					documentAttribute.setValue(attributeTypeWrapper.fromDatabase(documentAttribute.getValue()));

				// Carrega todos os planos de metas para exibicao no select box
				// nos
				// documentos
				if (documentAttribute.getType().equals(SelectPlan.class.getCanonicalName())) {

					PaginatedList<Plan> plans = this.planBS.listPlans(documentSection.getDocument().getPlan(), false, 0,
							null, null, 0);

					documentAttribute.setSelectPlans(plans.getList());
				}
			}
		}

		return list;
	}

	
	/**
	 * Retorna a lista de atributos de uma seção do documento
	 * 
	 * @param documentSection
	 *            Seção do documento
	 * @return List<DocumentAttribute> list Lista de atributos
	 */
	public List<DocumentAttribute> listAllAttributesBySection(DocumentSection documentSection) {
		Criteria criteria = this.dao.newCriteria(DocumentAttribute.class);
		criteria.add(Restrictions.eq("section", documentSection)).addOrder(Order.asc("sequence"));

		return criteria.list();
	}
	
	
	/**
	 * Retorna um atributo do documento a partir do id passado por parâmetro.
	 * 
	 * @param attributeId
	 *            Id do atributo desejado.
	 * @return DocumentAttribute Atributo do documento.
	 */
	public DocumentAttribute retrieveDocumentAttribute(Long attributeId) {
		Criteria criteria = this.dao.newCriteria(DocumentAttribute.class);
		criteria.add(Restrictions.eq("id", attributeId)).add(Restrictions.eq("deleted", false));
		DocumentAttribute documentAttribute = (DocumentAttribute) criteria.uniqueResult();
		return documentAttribute;
	}

	/**
	 * Salva atributos do documento considerando seu tipos (Numerical ou Date).
	 * 
	 * @param documentAttributes
	 *            Lista de atributos a serem salvos.
	 * @return List<DocumentAttribute> Lista de atributos do documento com
	 *         ValueAsNumber ou ValueAsDate preenchidos de acordo com seu tipo.
	 */
	public List<DocumentAttribute> saveAttributes(List<DocumentAttribute> documentAttributes) {
		List<DocumentAttribute> attributesList = new ArrayList<DocumentAttribute>();
		for (DocumentAttribute documentAttribute : documentAttributes) {
			DocumentAttribute existentDocumentAttribute = this.retrieveDocumentAttribute(documentAttribute.getId());
			if (AttributeTypeFactory.getInstance().get(existentDocumentAttribute.getType()) != null) {
				AttributeTypeWrapper attributeTypeWrapper = AttributeTypeFactory.getInstance()
						.get(existentDocumentAttribute.getType()).getWrapper();
				existentDocumentAttribute.setValue(attributeTypeWrapper.toDatabase(documentAttribute.getValue()));
				if (attributeTypeWrapper.isNumerical())
					existentDocumentAttribute
							.setValueAsNumber(attributeTypeWrapper.toDatabaseNumerical(documentAttribute.getValue()));
				if (attributeTypeWrapper.isDate())
					existentDocumentAttribute
							.setValueAsDate(attributeTypeWrapper.toDatabaseDate(documentAttribute.getValue()));
			}
			if (documentAttribute.getValue() == null) {
				existentDocumentAttribute.setValue(null);
			}
			this.persist(existentDocumentAttribute);
			attributesList.add(existentDocumentAttribute);
		}
		return attributesList;
	}

	/**
	 * Lista todas as seções de um documento.
	 * 
	 * @param doc
	 *            Documento a partir do qual se deseja listar as seções.
	 * @return List<DocumentSection> Lista de seções do documento.
	 */
	public List<DocumentSection> listSectionsByDocument(Document doc) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("document", doc)).add(Restrictions.eq("deleted", false));
		List<DocumentSection> list = this.dao.findByCriteria(criteria, DocumentSection.class);

		return list;
	}
	
	/**
	 * Lista todas as seções de um documento. Inclusive deletados
	 * 
	 * @param doc
	 *            Documento a partir do qual se deseja listar as seções.
	 * @return List<DocumentSection> Lista de seções do documento.
	 */
	public List<DocumentSection> listAllSectionsByDocument(Document doc) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("document", doc));
		List<DocumentSection> list = this.dao.findByCriteria(criteria, DocumentSection.class);

		return list;
	}

	/**
	 * Lista seções raiz do documento passado por parâmetro.
	 * 
	 * @param doc
	 *            Documento a partir do qual se deseja listar as seções.
	 * @return PaginatedList<DocumentSection> Lista de seções do documento.
	 */
	public PaginatedList<DocumentSection> listRootSectionsByDocument(Document doc) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("document", doc)).add(Restrictions.isNull("parent"))
				.add(Restrictions.eq("deleted", false));
		PaginatedList<DocumentSection> list = new PaginatedList<DocumentSection>();
		list.setList(this.dao.findByCriteria(criteria, DocumentSection.class));

		return list;
	}

	/**
	 * Lista as seções filhas de uma seção passada por parâmetro.
	 * 
	 * @param section
	 *            Seção a partir da qual se deseja obter as seções filhas.
	 * @return List<DocumentSection> Lista das seções filhas da seção passada
	 *         por parâmetro.
	 */
	public List<DocumentSection> listSectionsSons(DocumentSection section) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("parent", section)).add(Restrictions.eq("deleted", false));
		List<DocumentSection> list = this.dao.findByCriteria(criteria, DocumentSection.class);

		return list;
	}

	/**
	 * Preencher atributo que indica se as seções passadas por parâmetro estão
	 * preenchidas ou não.
	 * 
	 * @param sections
	 *            Lista de seções para preencher esse atributo.
	 * @param planId
	 *            Id do plano macro do documento.
	 */
	public void setSectionsFilled(List<DocumentSection> sections, Long planId) {
		for (DocumentSection section : sections) {
			section.setFilled(false);
			section.setDocumentAttributes(this.listAttributesBySection(section, planId));
			for (int i = 0; !section.isFilled() && i < section.getDocumentAttributes().size(); i++) {
				if (section.getDocumentAttributes().get(i).getValue() != null
						&& !section.getDocumentAttributes().get(i).getValue().equals(""))
					section.setFilled(true);
				if (section.getDocumentAttributes().get(i).getType().equals(TableField.class.getCanonicalName())) {
					List<TableInstance> tableInstances = this.fieldsBS
							.retrieveTableInstanceByAttribute(section.getDocumentAttributes().get(i).getId(), true);
					if (tableInstances != null && tableInstances.size() > 0)
						section.setFilled(true);
				} else if (section.getDocumentAttributes().get(i).getType()
						.equals(ScheduleField.class.getCanonicalName())) {
					List<ScheduleInstance> scheduleInstances = this.fieldsBS
							.retrieveScheduleInstanceByAttribute(section.getDocumentAttributes().get(i).getId(), true);
					if (scheduleInstances != null && scheduleInstances.size() > 0)
						section.setFilled(true);
				}
			}

			section.setSectionsSons(this.listSectionsSons(section));
			for (DocumentSection son : section.getSectionsSons()) {
				son.setDocumentAttributes(this.listAttributesBySection(son, planId));
				for (int i = 0; !section.isFilled() && i < son.getDocumentAttributes().size(); i++) {
					if (son.getDocumentAttributes().get(i).getValue() != null
							&& !son.getDocumentAttributes().get(i).getValue().equals(""))
						section.setFilled(true);
					if (son.getDocumentAttributes().get(i).getType().equals(TableField.class.getCanonicalName())) {
						List<TableInstance> tableInstances = this.fieldsBS
								.retrieveTableInstanceByAttribute(son.getDocumentAttributes().get(i).getId(), true);
						if (tableInstances != null && tableInstances.size() > 0)
							section.setFilled(true);
					} else if (son.getDocumentAttributes().get(i).getType()
							.equals(ScheduleField.class.getCanonicalName())) {
						List<ScheduleInstance> scheduleInstances = this.fieldsBS
								.retrieveScheduleInstanceByAttribute(son.getDocumentAttributes().get(i).getId(), true);
						if (scheduleInstances != null && scheduleInstances.size() > 0)
							section.setFilled(true);
					}
				}
			}
		}
	}

	/**
	 * Retorna documento do plano macro passado por parâmetro.
	 * 
	 * @param planMacro
	 *            Plano macro do qual se deseja obter o documento.
	 * @return Document Documento do plano macro passado por parâmetro.
	 */
	public Document retrieveDocumentByPlan(PlanMacro planMacro) {
		Criteria criteria = this.dao.newCriteria(Document.class);
		criteria.add(Restrictions.eq("plan", planMacro)).add(Restrictions.eq("deleted", false));
		Document document = (Document) criteria.uniqueResult();

		return document;
	}

	/**
	 * Duplicar documento.
	 * 
	 * @param macro
	 *            Plano macro do documento a ser duplicado.
	 * @param macroCopy
	 *            Plano macro do documento que receberá a cópia.
	 * @param keepContent
	 *            Recebe true para manter o conteúdo, caso contrário recebe
	 *            false.
	 * @return boolean Se o documento foi duplicado retorna true, caso contrário
	 *         retorna false.
	 */
	public boolean duplicateDocument(PlanMacro macro, PlanMacro macroCopy, Boolean keepContent) {
		try {
			Document doc = this.retrieveByPlan(macro);
			Document docCopy = new Document();
			docCopy.setCreation(new Date());
			docCopy.setDeleted(false);
			docCopy.setDescription(doc.getDescription());
			docCopy.setId(null);
			docCopy.setPlan(macroCopy);
			docCopy.setTitle("Documento - " + macroCopy.getName());
			this.persist(docCopy);
			List<DocumentSection> sections = this.listSectionsByDocument(doc);
			Map<Long, DocumentSection> map = new HashMap<>();
			for (DocumentSection section : sections) {
				DocumentSection copy = new DocumentSection();
				copy.setDocument(docCopy);
				copy.setLeaf(section.isLeaf());
				copy.setName(section.getName());
				if (section.getParent() == null)
					copy.setParent(null);
				else
					copy.setParent(map.get(section.getParent().getId()));
				copy.setSequence(section.getSequence());
				copy.setId(null);
				this.persist(copy);
				map.put(section.getId(), copy);
				this.duplicateDocumentSectionAttribute(section, copy, keepContent);

			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Unexpected runtime error", e);
			return false;
		}
	}

	/**
	 * Duplicar uma seção.
	 * 
	 * @param section
	 *            Seção a ser duplicada.
	 * @param copy
	 *            Seção que receberá a cópia.
	 * @param keepContent
	 *            Recebe true para manter o conteúdo, caso contrário recebe
	 *            false.
	 * @return boolean Se a seção foi duplicada retorna true, caso contrário
	 *         retorna false.
	 */
	public boolean duplicateDocumentSectionAttribute(DocumentSection section, DocumentSection copy,
			Boolean keepContent) {
		try {
			List<DocumentAttribute> attributes = this.listAttributesBySection(section, null);
			for (DocumentAttribute attr : attributes) {
				DocumentAttribute attrCopy = new DocumentAttribute();
				attrCopy.setName(attr.getName());
				attrCopy.setRequired(attr.isRequired());
				attrCopy.setSchedule(attr.getSchedule());
				attrCopy.setSection(copy);
				attrCopy.setSelectPlans(attr.getSelectPlans());
				attrCopy.setSequence(attr.getSequence());
				attrCopy.setTableFields(attr.getTableFields());
				attrCopy.setType(attr.getType());
				if (keepContent) {
					attrCopy.setValue(attr.getValue());
					attrCopy.setValueAsDate(attr.getValueAsDate());
					attrCopy.setValueAsNumber(attr.getValueAsNumber());
				}
				attrCopy.setId(null);
				this.persist(attrCopy);
				if (attr.getType().equals(TableField.class.getCanonicalName())) {
					this.duplicateTableField(attr, attrCopy, keepContent);
				} else if (attr.getType().equals(ScheduleField.class.getCanonicalName())) {
					this.duplicateScheduleField(attr, attrCopy, keepContent);
				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Unexpected runtime error", e);
			return false;
		}
	}

	/**
	 * Lista as seções filhas de uma seção passada por parâmetro.
	 * 
	 * @param section
	 *            Seção a partir da qual se deseja obter as seções filhas.
	 * @return List<DocumentSection> Lista das seções filhas da seção passada
	 *         por parâmetro.
	 */
	public List<DocumentSection> listSectionChild(DocumentSection section) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("parent", section)).add(Restrictions.eq("deleted", false));

		return this.dao.findByCriteria(criteria, DocumentSection.class);
	}

	/**
	 * Duplicar atributo do tipo tabela.
	 * 
	 * @param attr
	 *            Atributo a ser duplicado.
	 * @param attrCopy
	 *            Atributo que receberá a cópia.
	 * @param keepContent
	 *            Recebe true para manter o conteúdo, caso contrário recebe
	 *            false.
	 */
	public void duplicateTableField(DocumentAttribute attr, DocumentAttribute attrCopy, Boolean keepContent) {
		TableFields fields = this.fieldsBS.tableFieldsByAttribute(attr.getId(), true);
		TableFields fieldsCopy = new TableFields();
		fieldsCopy.setAttributeId(attrCopy.getId());
		fieldsCopy.setDeleted(false);
		fieldsCopy.setIsDocument(true);
		this.persist(fieldsCopy);
		List<TableStructure> structures = this.fieldsBS.listTableStructureByFields(fields);
		Map<Long, TableStructure> structureMap = new HashMap<>();
		for (TableStructure structure : structures) {
			TableStructure structureCopy = new TableStructure();
			structureCopy.setDeleted(false);
			structureCopy.setInTotal(structure.isInTotal());
			structureCopy.setLabel(structure.getLabel());
			structureCopy.setTableFields(fieldsCopy);
			structureCopy.setType(structure.getType());
			this.persist(structureCopy);
			if (structure.getType().equals(SelectField.class.getCanonicalName())) {
				List<OptionsField> options = this.fieldsBS.getOptionsForColumn(attr.getId(), structure.getId(), true);
				for (OptionsField option : options) {
					OptionsField optionCopy = new OptionsField();
					optionCopy.setAttributeId(attrCopy.getId());
					optionCopy.setColumnId(structureCopy.getId());
					optionCopy.setCreation(new Date());
					optionCopy.setDeleted(false);
					optionCopy.setDocument(true);
					optionCopy.setLabel(option.getLabel());
					this.persist(optionCopy);
				}
			}
			structureMap.put(structure.getId(), structureCopy);
		}
		if (keepContent) {
			List<TableInstance> instances = this.fieldsBS.listTableInstanceByFields(fields);
			for (TableInstance instance : instances) {
				TableInstance instanceCopy = new TableInstance();
				instanceCopy.setCreation(new Date());
				instanceCopy.setDeleted(false);
				instanceCopy.setTableFields(fieldsCopy);
				this.persist(instanceCopy);
				List<TableValues> values = this.fieldsBS.listTableValuesByInstance(instance);
				for (TableValues value : values) {
					TableValues valueCopy = new TableValues();
					valueCopy.setDeleted(false);
					valueCopy.setTableInstance(instanceCopy);
					valueCopy.setTableStructure(structureMap.get(value.getTableStructure().getId()));
					valueCopy.setValue(value.getValue());
					valueCopy.setValueAsDate(value.getValueAsDate());
					valueCopy.setValueAsNumber(value.getValueAsNumber());
					this.persist(valueCopy);
				}
			}
		}
	}

	/**
	 * Duplicar atributo do tipo cronograma.
	 * 
	 * @param attr
	 *            Atributo a ser duplicado.
	 * @param attrCopy
	 *            Atributo que receberá a cópia.
	 * @param keepContent
	 *            Recebe true para manter o conteúdo, caso contrário recebe
	 *            false.
	 */
	public void duplicateScheduleField(DocumentAttribute attr, DocumentAttribute attrCopy, Boolean keepContent) {
		Schedule schedule = this.fieldsBS.scheduleByAttribute(attr.getId(), true);
		Schedule scheduleCopy = new Schedule();
		scheduleCopy.setAttributeId(attrCopy.getId());
		scheduleCopy.setDeleted(false);
		scheduleCopy.setIsDocument(true);
		scheduleCopy.setPeriodicityEnable(schedule.isPeriodicityEnable());
		this.persist(scheduleCopy);
		List<ScheduleStructure> structures = this.fieldsBS.listScheduleStructure(schedule);
		Map<Long, ScheduleStructure> structureMap = new HashMap<>();
		for (ScheduleStructure structure : structures) {
			ScheduleStructure structureCopy = new ScheduleStructure();
			structureCopy.setDeleted(false);
			structureCopy.setLabel(structure.getLabel());
			structureCopy.setSchedule(scheduleCopy);
			structureCopy.setType(structure.getType());
			this.persist(structureCopy);
			structureMap.put(structure.getId(), structureCopy);
		}
		if (keepContent) {
			List<ScheduleInstance> instances = this.fieldsBS.listScheduleInstance(schedule);
			for (ScheduleInstance instance : instances) {
				ScheduleInstance instanceCopy = new ScheduleInstance();
				instanceCopy.setBegin(instance.getBegin());
				instanceCopy.setCreation(new Date());
				instanceCopy.setDeleted(false);
				instanceCopy.setDescription(instance.getDescription());
				instanceCopy.setEnd(instance.getEnd());
				instanceCopy.setNumber(instance.getNumber());
				instanceCopy.setPeriodicity(instance.getPeriodicity());
				instanceCopy.setSchedule(scheduleCopy);
				this.persist(instanceCopy);
				List<ScheduleValues> values = this.fieldsBS.lsitScheduleValues(instance);
				for (ScheduleValues value : values) {
					ScheduleValues valueCopy = new ScheduleValues();
					valueCopy.setDeleted(false);
					valueCopy.setScheduleInstance(instanceCopy);
					valueCopy.setScheduleStructure(structureMap.get(value.getScheduleStructure().getId()));
					valueCopy.setValue(value.getValue());
					valueCopy.setValueAsDate(value.getValueAsDate());
					valueCopy.setValueAsNumber(value.getValueAsNumber());
					this.persist(valueCopy);
				}
			}
		}
	}

		
	/**
	 * Lista com os valores dos campos
	 * 
	 * @param instance
	 * @return List<ScheduleValues> Lista com os valores da tabela.
	 */
	public List<ScheduleValues> listScheduleValuesByInstance(ScheduleInstance instance) {
		Criteria criteria = this.dao.newCriteria(ScheduleValues.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("scheduleInstance", instance));
		return this.dao.findByCriteria(criteria, ScheduleValues.class);
	}

	/**
	 * Listar os valores do campo tabela
	 * 
	 * @param structure
	 *            Estrutura para listar os campos.
	 * @return List<TableValues> Lista dos valores com os campos de tabela.
	 */
	public List<ScheduleValues> listScheduleValuesByStructure(ScheduleStructure structure) {
		Criteria criteria = this.dao.newCriteria(ScheduleValues.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("scheduleStructure", structure));
		return this.dao.findByCriteria(criteria, ScheduleValues.class);
	}

	/**
	 * Buscar campos na tabela à partir de um atributo.
	 * 
	 * @param attributeId
	 *            Atributo para buscar os campos.
	 * @param isDocument
	 *            Verificar se o campo pertence ao documento ou plano.
	 * @return Schedule Campo da tabela.
	 */
	public Schedule scheduleByAttribute(Long attributeId, boolean isDocument) {
		Criteria criteria = this.dao.newCriteria(Schedule.class).add(Restrictions.eq("deleted", false))
				.add(Restrictions.eq("attributeId", attributeId)).add(Restrictions.eq("isDocument", isDocument))
				.addOrder(Order.asc("id"));
		Schedule schedule = (Schedule) criteria.uniqueResult();
		return schedule;
	}

	public List<AggregateIndicator> listAggIndbyLevelInstance(StructureLevelInstance levelInstance) {
		Criteria criteria = this.dao.newCriteria(AggregateIndicator.class);
		criteria.add(Restrictions.eq("deleted", false));
		criteria.add(Restrictions.eq("indicator", levelInstance));
		List<AggregateIndicator> levelList = this.dao.findByCriteria(criteria, AggregateIndicator.class);
		
		return levelList;
	}

}