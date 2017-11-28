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
import com.itextpdf.text.pdf.PdfTemplate;
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
	 * @param sectionId
	 * 			Id da seção do documento
	 * @return documentSection 
	 * 			Seção do documento
	 */
	public DocumentSection retrieveSectionById(Long sectionId) {
		Criteria criteria = this.dao.newCriteria(DocumentSection.class);
		criteria.add(Restrictions.eq("id", sectionId)).add(Restrictions.eq("deleted", false));
		DocumentSection documentSection = (DocumentSection) criteria.uniqueResult();
		return documentSection;
	}
	
	
	/**
	 * Retorna o número de atributos da seção
	 * @param documentSection
	 * 		Seção do documento
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
	 * @param documentSection
	 * 			Seção do documento
	 * @param planId
	 * 			Id do Plano 
	 * @return List<DocumentAttribute> list
	 * 			Lista de atributos
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

	/*
	 * public PaginatedList<DocumentSection> listFilledDocumentSections(long
	 * documentId) { String sql =
	 * "SELECT doc_sec.id AS id, doc_sec.name AS name " //+
	 * "	CASE WHEN sub_cons.parent IS NULL THEN 0 ELSE 1 END AS filled " +
	 * "	FROM forpdi_db.fpdi_document_section AS doc_sec " +
	 * "	LEFT JOIN (SELECT doc_sec.id AS secId, CASE WHEN doc_sec.parent_id is null then doc_sec.id else "
	 * +
	 * "	doc_sec.parent_id end as parent FROM fpdi_document_attribute AS doc_att "
	 * +
	 * "	INNER JOIN fpdi_document_section AS doc_sec ON doc_att.section_id = doc_sec.id "
	 * + "	LEFT JOIN (SELECT doc_sec.id, doc_sec.name " +
	 * "	FROM forpdi_db.fpdi_document_section doc_sec) AS doc_sec_parent " +
	 * "	ON doc_sec.parent_id = doc_sec_parent.id " +
	 * "  LEFT JOIN fpdi_table_field AS tab_field ON doc_att.id = tab_field.attributeId "
	 * +
	 * "	LEFT JOIN fpdi_table_instance AS tab_inst ON tab_field.id = tab_inst.tableFields_id "
	 * +
	 * "	LEFT JOIN (SELECT tableFields_id AS tabFieldId, COUNT(id) AS qtdCols FROM "
	 * +
	 * "	forpdi_db.fpdi_table_structure GROUP BY tableFields_id) AS qtd_cols "
	 * + "	ON tab_field.id = qtd_cols.tabFieldId LEFT JOIN (SELECT " +
	 * "	tab_inst.tableFields_id AS tabFieldId, COUNT(tab_val.id) AS qtdRows "
	 * +
	 * "	FROM forpdi_db.fpdi_table_values AS tab_val INNER JOIN fpdi_table_instance "
	 * + "	AS tab_inst ON tab_val.tableInstance_id = tab_inst.id " +
	 * "	GROUP BY tab_inst.tableFields_id) AS qtd_rows ON tab_field.id = qtd_rows.tabFieldId "
	 * +
	 * "	LEFT JOIN fpdi_table_structure AS tab_struct ON tab_field.id = tab_struct.tableFields_id "
	 * +
	 * "	LEFT JOIN fpdi_table_values AS tab_val ON tab_struct.id = tab_val.tableStructure_id "
	 * +
	 * "	LEFT JOIN fpdi_schedule AS sch_field ON doc_att.id = sch_field.attributeId "
	 * +
	 * "	LEFT JOIN fpdi_schedule_instance AS sch_inst ON sch_field.id = sch_inst.schedule_id "
	 * +
	 * "	LEFT JOIN (SELECT count(sch_inst.id) schRows, sch.id FROM forpdi_db.fpdi_schedule_instance "
	 * +
	 * "	sch_inst INNER JOIN fpdi_schedule sch ON sch_inst.schedule_id = sch.id GROUP BY sch.id ) "
	 * +
	 * "	as sch_rows ON sch_inst.schedule_id = sch_rows.id LEFT JOIN fpdi_plan_macro AS plan_macro "
	 * +
	 * "	ON doc_att.value = plan_macro.id WHERE    ((doc_att.value IS NOT NULL AND doc_att.value <> '' AND doc_att.value <> '<p><br></p>') "
	 * +
	 * "	OR tab_val.value IS NOT NULL OR sch_inst.description IS NOT NULL)AND doc_att.deleted = 0 "
	 * +
	 * "	GROUP BY tab_val.id, sch_inst.id, doc_att.id ORDER BY doc_sec.id , doc_att.id , tab_inst.id , "
	 * +
	 * "	tab_val.id, sch_inst.id) AS sub_cons ON doc_sec.id = sub_cons.parent WHERE doc_sec.parent_id IS NULL "
	 * + "	AND doc_sec.deleted=0 AND doc_sec.document_id=" + documentId +
	 * " GROUP BY doc_sec.id";
	 */
	/*
	 * String sql = "SELECT doc_sec.id as id, doc_sec.name as name, \n"; sql +=
	 * "CASE WHEN replace(subconsulta.value, ',', '') IS NULL OR \n"; sql +=
	 * "replace(subconsulta.value, ',', '') = '' THEN false ELSE true END AS filled \n"
	 * ; sql += "FROM fpdi_document_section AS doc_sec \n"; sql +=
	 * "INNER JOIN (SELECT \n"; sql +=
	 * "CASE WHEN doc_sec.parent_id is null then doc_sec.id else \n"; sql +=
	 * "doc_sec.parent_id end as parent, \n"; sql +=
	 * "group_concat(doc_att.value) as value \n"; sql +=
	 * "FROM forpdi_db.fpdi_document_section as doc_sec \n"; sql +=
	 * "LEFT JOIN fpdi_document_attribute AS doc_att \n"; sql +=
	 * "ON doc_sec.id = doc_att.section_id \n"; sql += "WHERE document_id=" +
	 * documentId + " \n"; sql += "GROUP BY parent \n"; sql +=
	 * "ORDER BY parent \n"; sql += ") AS subconsulta \n"; sql +=
	 * "ON doc_sec.id = subconsulta.parent \n"; sql +=
	 * "WHERE doc_sec.parent_id IS NULL AND \n"; sql += "doc_sec.document_id ="
	 * + documentId + " AND doc_sec.deleted = 0;\n";
	 */
	/*
	 * SQLQuery query = this.dao.newSQLQuery(sql);
	 * PaginatedList<DocumentSection> result = new
	 * PaginatedList<DocumentSection>();
	 * //query.setResultTransformer(Transformers.aliasToBean(DocumentSection.
	 * class)); result.setList(query.list()); return result; }
	 */

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
	 * Exporta documento PDF
	 * 
	 * @param author
	 *            Autor do documento.
	 * @param title
	 *            Título do documento.
	 * @param lista
	 *            Lista de seções a serem exportadas.
	 * @return InputStream Fluxo de entrada do arquivo PDF.
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public InputStream exportDocument(String author, String title, String lista)
			throws IOException, DocumentException, SQLException, ClassNotFoundException {

		class TOCEvent extends PdfPageEventHelper {

			protected int counter = 0;
			protected List<SimpleEntry<String, SimpleEntry<String, Integer>>> toc = new ArrayList<>();

			String lastText = "";

			@Override
			public void onGenericTag(PdfWriter writer, com.itextpdf.text.Document document, Rectangle rect,
					String text) {
				if (text != lastText) {
					String name = "dest" + (counter++);
					int page = writer.getPageNumber();
					// LOGGER.info(text);
					toc.add(new SimpleEntry<String, SimpleEntry<String, Integer>>(text,
							new SimpleEntry<String, Integer>(name, page)));
				}
				lastText = text;
				// writer.addNamedDestination(name, page, new
				// PdfDestination(PdfDestination.FITH, rect.getTop()));

			}

			public List<SimpleEntry<String, SimpleEntry<String, Integer>>> getTOC() {
				return toc;
			}

		}

		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		com.itextpdf.text.Document coverDocument = new com.itextpdf.text.Document();
		com.itextpdf.text.Document preTextDocument = new com.itextpdf.text.Document();
		com.itextpdf.text.Document summaryDocument = new com.itextpdf.text.Document();

		ClassLoader classLoader = getClass().getClassLoader();
		String resourcesPath = new File(classLoader.getResource("/reports/pdf/example.pdf").getFile()).getPath();
		resourcesPath = "/tmp"; // corrigir para salvar com um caminho
		// dinamico
		resourcesPath = resourcesPath.replace("example.pdf", "");
		resourcesPath = resourcesPath.replace("%20", " ");
		File pdfFile = File.createTempFile("output.", ".pdf", new File(resourcesPath));

		String SRC = pdfFile.getAbsolutePath();
		String DEST = pdfFile.getAbsolutePath() + "mounted.pdf";
		String SUMMARY = pdfFile.getAbsolutePath() + "summary.pdf";
		String FINALSUMMARY = pdfFile.getAbsolutePath() + "finalSummary.pdf";
		String PRETEXT = pdfFile.getAbsolutePath() + "preText.pdf";
		String COVER = pdfFile.getAbsolutePath() + "cover.pdf";
		String FINAL = pdfFile.getAbsolutePath() + "final.pdf";

		File coverPdfFile = new File(COVER);
		File preTextPdfFile = new File(PRETEXT);
		File summaryPdfFile = new File(SUMMARY);
		File finalSummaryPdfFile = new File(FINALSUMMARY);
		File file = new File(DEST);
		File finalPdfFile = new File(FINAL);
		file.getParentFile().mkdirs();

		InputStream in = new FileInputStream(pdfFile);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		PdfWriter coverWriter = PdfWriter.getInstance(coverDocument, new FileOutputStream(coverPdfFile));
		PdfWriter preTextWriter = PdfWriter.getInstance(preTextDocument, new FileOutputStream(preTextPdfFile));
		PdfWriter summaryWriter = PdfWriter.getInstance(summaryDocument, new FileOutputStream(summaryPdfFile));

		TOCEvent event = new TOCEvent();
		writer.setPageEvent(event);

		// DEFINIÇÕES DE FONTE, MARGENS, ESPAÇAMENTO E CORES
		Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);

		Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
		Font tituloCapa = FontFactory.getFont(FontFactory.TIMES_BOLD, 14.0f);
		// Cor cinza - cabeçalho das tabelas
		CMYKColor headerBgColor = new CMYKColor(55, 45, 42, 7);

		// 0,8 cm acima e abaixo
		float paragraphSpacing = 22.6772f;
		// Parágrafo com 1,25 cm na primeira linha
		float firstLineIndent = 35.43307f;
		// 1,5 entrelinhas
		float interLineSpacing = texto.getCalculatedLeading(1.5f);
		// Formato A4 do documento
		coverDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		coverDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);

		coverDocument.open();

		// CABEÇALHO
		String imageUrl = domain.getCompany().getLogo();
		// LOGGER.info("|"+imageUrl+"|");
		if (!imageUrl.trim().isEmpty()) {
			Image image = Image.getInstance(new URL(imageUrl));
			// image.scaleAbsolute(150f, 150f);
			float scaler = ((coverDocument.getPageSize().getWidth() - coverDocument.leftMargin()
					- coverDocument.rightMargin()) / image.getWidth()) * 100;
			image.scalePercent(scaler * 0.4f);
			image.setAlignment(Element.ALIGN_CENTER);
			coverDocument.add(image);
		}

		Paragraph TITULO = new Paragraph(title, tituloCapa);
		// Paragraph AUTHOR = new Paragraph(author, texto);
		TITULO.setAlignment(Element.ALIGN_CENTER);
		TITULO.setSpacingBefore(paragraphSpacing * 8);

		// AUTHOR.setAlignment(Element.ALIGN_CENTER);

		coverDocument.add(TITULO);
		// document.add(AUTHOR);
		// document.add(YEAR);
		Phrase localizationPhrase = new Phrase(this.domain.getCompany().getLocalization(), titulo);
		// Phrase footer = new Phrase(String.valueOf(cal.get(Calendar.YEAR)),
		// titulo);
		PdfContentByte cb = coverWriter.getDirectContent();
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, localizationPhrase,
				(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
				coverDocument.bottom() + 30, 0);
		coverDocument.newPage();

		// FOLHA DE ROSTO
		Paragraph COMPANY = new Paragraph(domain.getCompany().getName(), tituloCapa);
		COMPANY.setAlignment(Element.ALIGN_CENTER);
		COMPANY.setSpacingBefore(paragraphSpacing);
		coverDocument.add(COMPANY);

		coverDocument.add(TITULO);

		Calendar cal = Calendar.getInstance();

		Phrase periodPhrase = new Phrase(
				String.valueOf(cal.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cal.get(Calendar.YEAR)), titulo);
		// footerRosto.add(periodPhrase);
		// footerRosto.add(footer);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, periodPhrase,
				(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
				coverDocument.bottom() + 15, 0);
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, localizationPhrase,
				(coverDocument.right() - coverDocument.left()) / 2 + coverDocument.leftMargin(),
				coverDocument.bottom() + 30, 0);
		coverDocument.newPage();
		coverDocument.close();

		String[] sections = lista.split(",");
		int secIndex = 0, subSecIndex = 0;

		boolean lastAttWasPlan = false;
		boolean lastSecWasPreText = false;

		DocumentSection summarySection = new DocumentSection();
		List<DocumentSection> summarySectionSons = new ArrayList<DocumentSection>();

		// table to store placeholder for all chapters and sections
		final Map<String, PdfTemplate> tocPlaceholder = new HashMap<>();

		// store the chapters and sections with their title here.
		final Map<String, Integer> pageByTitle = new HashMap<>();

		document.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		document.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
		document.open();

		preTextDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		preTextDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
		preTextDocument.open();

		boolean endPreText = false;
		boolean havePreText = false;
		boolean haveContent = false;

		for (int i = 0; i < sections.length; i++) {
			DocumentSection ds = this.retrieveSectionById(Long.parseLong(sections[i]));
			ds.setDocumentAttributes(this.listAttributesBySection(ds, ds.getDocument().getPlan().getId()));

			subSecIndex = 0;
			String secName = ds.getName();

			if (ds.isPreTextSection()) { // SEÇÕES PRÉ TEXTUAIS

				for (DocumentAttribute a : ds.getDocumentAttributes()) {
					if (a.getType().equals(TableField.class.getCanonicalName())) {
						havePreText = true;
						TableFields tf = fieldsBS.tableFieldsByAttribute(a.getId(), true);
						List<TableStructure> tabStructList = fieldsBS.listTableStructureByFields(tf);
						List<TableInstance> tabInstList = fieldsBS.listTableInstanceByFields(tf);
						if (!tabInstList.isEmpty()) {
							// String attName = a.getName();
							// if (!attName.equals(secName)) {

							Chunk c = new Chunk(secName, titulo);
							c.setGenericTag(secName);

							Paragraph attTitle = new Paragraph(c);
							attTitle.setAlignment(Element.ALIGN_CENTER);
							attTitle.setLeading(interLineSpacing);
							attTitle.setSpacingAfter(paragraphSpacing);
							attTitle.setSpacingBefore(paragraphSpacing);
							preTextDocument.add(attTitle);
							// }
							PdfPTable table;
							if (tabStructList
									.size() == 4) /**
													 * SOLUÇÃO DE CONTORNO PARA
													 * TRATAR O CASO ESPECIAL DO
													 * HISTÓRICO DE VERSÕES QUE
													 * É UMA SEÇÃO PRÉ-TEXTUAL
													 * MAS DEVE EXIBIR BORDA E
													 * CABEÇALHO, NECESSITA DE
													 * MELHORIA
													 */
								table = returnPdfPTable(tabStructList, tabInstList, false, false);
							else
								table = returnPdfPTable(tabStructList, tabInstList, true, true);
							preTextDocument.add(table);
							preTextDocument.newPage();
							lastAttWasPlan = false;
							lastSecWasPreText = true;
						}
					}
				}

			} else {
				haveContent = true;
				// SEÇÕES NUMERADAS
				/*
				 * if (lastSecWasPreText) { lastSecWasPreText = false;
				 * 
				 * Paragraph secTitle = new Paragraph("Sumário", titulo);
				 * secTitle.setLeading(interLineSpacing);
				 * secTitle.setSpacingAfter(paragraphSpacing);
				 * secTitle.setSpacingBefore(paragraphSpacing);
				 * secTitle.setAlignment(Element.ALIGN_CENTER);
				 * document.add(secTitle); int summaryIndex = 0; int
				 * summarySubSecIndex = 0; for (String secaoId : sections) {
				 * summarySection =
				 * this.retrieveSectionById(Long.parseLong(secaoId)); if
				 * (!summarySection.isPreTextSection()) {
				 * 
				 * summaryIndex++; secTitle = new Paragraph(summaryIndex + ". "
				 * + summarySection.getName(), titulo);
				 * secTitle.setLeading(interLineSpacing);
				 * document.add(secTitle);
				 * 
				 * List<DocumentSection> dsList =
				 * this.listSectionsSons(summarySection);
				 * this.setSectionsFilled(dsList,
				 * summarySection.getDocument().getPlan().getId());
				 * summarySubSecIndex = 0; for (DocumentSection sec : dsList) {
				 * 
				 * if (sec.isFilled()) { summarySubSecIndex++; secTitle = new
				 * Paragraph( summaryIndex + "." + summarySubSecIndex + ". " +
				 * sec.getName(), texto); secTitle.setLeading(interLineSpacing);
				 * secTitle.setFirstLineIndent(firstLineIndent / 2);
				 * document.add(secTitle); } }
				 * 
				 * } } document.newPage(); }
				 */

				secIndex++;

				// LOGGER.info(ds.getId() + ". " + ds.getName() + " - Size:
				// " +
				// ds.getDocumentAttributes().size());

				boolean secTitlePrinted = false;

				if (ds.getDocumentAttributes().size() == 0) {
					if (lastAttWasPlan) {
						document.setPageSize(PageSize.A4);
						document.newPage();
					}
					Chunk c = new Chunk(secIndex + ". " + secName, titulo);
					c.setGenericTag(secIndex + ". " + secName);
					Paragraph secTitle = new Paragraph(c);
					secTitle.setLeading(interLineSpacing);
					secTitle.setSpacingAfter(paragraphSpacing);
					secTitle.setSpacingBefore(paragraphSpacing);
					document.add(secTitle);
					secTitlePrinted = true;
					lastAttWasPlan = false;
				}

				for (DocumentAttribute a : ds.getDocumentAttributes()) {
					if (a.getType().equals(TextArea.class.getCanonicalName())) {
						if (a.getValue() != null && !a.getValue().equals("")) {
							if (lastAttWasPlan) {
								document.setPageSize(PageSize.A4);
								document.newPage();
							}
							if (!secTitlePrinted) {
								Chunk c = new Chunk(secIndex + ". " + secName, titulo);
								c.setGenericTag(secIndex + ". " + secName);
								Paragraph secTitle = new Paragraph(c);
								secTitle.setLeading(interLineSpacing);
								secTitle.setSpacingAfter(paragraphSpacing);
								secTitle.setSpacingBefore(paragraphSpacing);
								document.add(secTitle);
								secTitlePrinted = true;
							}
							String attName = a.getName();
							if (!attName.equals(secName)) {
								Paragraph attTitle = new Paragraph(attName, titulo);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
							// HTMLWorker htmlWorker = new
							// HTMLWorker(document);
							Map<String, String> pc2 = new HashMap<String, String>();
							pc2.put("line-height", "115%");
							pc2.put("margin-bottom", "6.0pt");
							pc2.put("text-align", "center");

							HashMap<String, String> spanc1 = new HashMap<String, String>();
							spanc1.put("text-justify", "inter-word");

							StyleSheet styles = new StyleSheet();
							styles.loadTagStyle("p", "text-indent", "1.25cm");

							String str = "<html>" + "<head>" + "</head><body style=\"text-indent: 1.25cm; \">"
									+ "<p style=\"text-indent: 1.25cm; \">";
							Queue<String> allMatches = new LinkedList<>();
							String value = a.getValue();
							if (a.getValue().contains("<img")) {
								Matcher m = Pattern.compile("<img [^>]*>").matcher(a.getValue());
								while (m.find()) {
									String match = m.group();
									allMatches.add(match);
									value = value.replace(match, "<p>||IMAGE||</p>");
								}
							}
							str += value + "</p></body></html>";

							resourcesPath = new File(classLoader.getResource("/reports/html/example.html").getFile())
									.getPath();
							resourcesPath = resourcesPath.replace("example.html", "");
							resourcesPath = resourcesPath.replace("%20", " ");
							resourcesPath = "/tmp"; // corrigir para usar
							// um caminho
							// dinamico
							File htmlFile = File.createTempFile("output.", ".html", new File(resourcesPath));
							FileWriter fw = new FileWriter(htmlFile.getPath(), true);
							BufferedWriter conexao = new BufferedWriter(fw);
							conexao.write(str);
							conexao.newLine();
							conexao.close();
							// LOGGER.info(htmlFile.getPath());

							List<Element> p = HTMLWorker.parseToList(new FileReader(htmlFile.getPath()), styles);
							for (int k = 0; k < p.size(); ++k) {
								if (p.get(k) instanceof Paragraph) {
									Paragraph att = (Paragraph) p.get(k);
									// LOGGER.info("------->"+att.getContent());
									if (att.getContent().contains("||IMAGE||")) {
										String img = allMatches.poll();
										if (img != null) {
											// LOGGER.info("IMG------->"+img);
											Image image = Image.getInstance(
													new URL(img.replaceAll("<img src=\"", "").replaceAll("\">", "")));
											float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
													- document.rightMargin()) / image.getWidth()) * 100;
											image.scalePercent(scaler * 0.4f);
											image.setAlignment(Element.ALIGN_CENTER);
											document.add(image);
										}
									} else {
										att.setFirstLineIndent(firstLineIndent);
										document.add(att);
									}
								} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
									com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
									att.setIndentationLeft(firstLineIndent);
									document.add(att);
								}
							}
							lastAttWasPlan = false;
							htmlFile.delete();
						}
					} else if (a.getType().equals(TableField.class.getCanonicalName())) {

						TableFields tf = fieldsBS.tableFieldsByAttribute(a.getId(), true);
						List<TableStructure> tabStructList = fieldsBS.listTableStructureByFields(tf);
						List<TableInstance> tabInstList = fieldsBS.listTableInstanceByFields(tf);
						if (!tabInstList.isEmpty()) {
							if (lastAttWasPlan) {
								document.setPageSize(PageSize.A4);
								document.newPage();
							}
							if (!secTitlePrinted) {
								Chunk c = new Chunk(secIndex + ". " + secName, titulo);
								c.setGenericTag(secIndex + ". " + secName);
								Paragraph secTitle = new Paragraph(c);
								secTitle.setLeading(interLineSpacing);
								secTitle.setSpacingAfter(paragraphSpacing);
								secTitle.setSpacingBefore(paragraphSpacing);
								document.add(secTitle);
								secTitlePrinted = true;
							}
							String attName = a.getName();
							if (!attName.equals(secName)) {
								Paragraph attTitle = new Paragraph(attName, texto);
								attTitle.setAlignment(Element.ALIGN_CENTER);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
							PdfPTable table = returnPdfPTable(tabStructList, tabInstList, false, false);
							document.add(table);
							lastAttWasPlan = false;
						}

					} else if (a.getType().equals(ScheduleField.class.getCanonicalName())) {
						List<ScheduleInstance> schInstList = this.fieldsBS
								.retrieveScheduleInstanceByAttribute(a.getId(), true);
						if (!schInstList.isEmpty()) {
							if (lastAttWasPlan) {
								document.setPageSize(PageSize.A4);
								document.newPage();
							}
							if (!secTitlePrinted) {
								Chunk c = new Chunk(secIndex + ". " + secName, titulo);
								c.setGenericTag(secIndex + ". " + secName);
								Paragraph secTitle = new Paragraph(c);
								secTitle.setLeading(interLineSpacing);
								secTitle.setSpacingAfter(paragraphSpacing);
								secTitle.setSpacingBefore(paragraphSpacing);
								document.add(secTitle);
								secTitlePrinted = true;
							}
							String attName = a.getName();
							if (!attName.equals(secName)) {
								Paragraph attTitle = new Paragraph(attName, texto);
								attTitle.setAlignment(Element.ALIGN_CENTER);
								attTitle.setLeading(interLineSpacing);
								attTitle.setSpacingAfter(paragraphSpacing);
								attTitle.setSpacingBefore(paragraphSpacing);
								document.add(attTitle);
							}
							PdfPTable table = new PdfPTable(3);
							table.getDefaultCell();
							PdfPCell c = new PdfPCell(new Paragraph("Atividade", texto));
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							c.setBackgroundColor(headerBgColor);
							table.addCell(c);
							c = new PdfPCell(new Paragraph("Início", texto));
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							c.setBackgroundColor(headerBgColor);

							table.addCell(c);
							c = new PdfPCell(new Paragraph("Fim", texto));
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							c.setBackgroundColor(headerBgColor);
							table.addCell(c);

							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							for (ScheduleInstance sch : schInstList) {
								table.addCell(new Paragraph(sch.getDescription(), texto));
								table.addCell(new Paragraph(sdf.format(sch.getBegin()), texto));
								table.addCell(new Paragraph(sdf.format(sch.getEnd()), texto));
							}
							document.add(table);
							lastAttWasPlan = false;
						}
					} else if (a.getType().equals(SelectPlan.class.getCanonicalName())) {
						if (a.getValue() != null) {
							Plan plan = planBS.retrieveById(Long.parseLong(a.getValue()));
							List<PdfPTable> planTableList = this.generatePDFplanTable(plan);
							boolean first = true;
							// LOGGER.info("2 - " + plan.getName());
							// LOGGER.info(secName + " " + secTitlePrinted);
							for (PdfPTable planTable : planTableList) {
								if (!lastAttWasPlan) {
									document.setPageSize(PageSize.A4.rotate());
								}
								document.newPage();
								if (first) {

									if (!secTitlePrinted) {
										Chunk c = new Chunk(secIndex + ". " + secName, titulo);
										c.setGenericTag(secIndex + ". " + secName);
										Paragraph secTitle = new Paragraph(c);
										document.add(secTitle);
										secTitlePrinted = true;
									}
									Paragraph attTitle = new Paragraph(plan.getName(), texto);
									attTitle.setAlignment(Element.ALIGN_CENTER);
									document.add(attTitle);
									first = false;
									lastAttWasPlan = true;
								}
								document.add(planTable);
							}
							// document.setPageSize(PageSize.A4);
							// document.newPage();
						}
					}
				}

				List<DocumentSection> dsList = this.listSectionsSons(ds);
				this.setSectionsFilled(dsList, ds.getDocument().getPlan().getId());

				// LOGGER.info("dsList.size: " + dsList.size());

				for (DocumentSection d : dsList) {
					if (d.isFilled()) {
						// LOGGER.info("filled: " + d.getName());
						String subSecName = d.getName();
						boolean subSecTitlePrinted = false;
						// if (!secName.equals(subSecName)) {
						/*
						 * subSecIndex++; Paragraph subSecTitle = new
						 * Paragraph(secIndex + "." + subSecIndex + ". " +
						 * subSecName, titulo);
						 * subSecTitle.setLeading(interLineSpacing);
						 * subSecTitle.setSpacingAfter(paragraphSpacing);
						 * subSecTitle.setSpacingBefore(paragraphSpacing);
						 * document.add(subSecTitle); subSecTitlePrinted = true;
						 */
						// }

						List<DocumentAttribute> attList = this.listAttributesBySection(d,
								d.getDocument().getPlan().getId());
						// LOGGER.info("attList.size: " + attList.size());
						for (DocumentAttribute a : attList) {
							if (a.getType().equals(TextArea.class.getCanonicalName())) {
								if (a.getValue() != null && !a.getValue().equals("")) {
									String attName = a.getName();
									if (lastAttWasPlan) {
										document.setPageSize(PageSize.A4);
										document.newPage();
									}
									if (!subSecTitlePrinted) {
										subSecIndex++;
										Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
										c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
										Paragraph subSecTitle = new Paragraph(c);
										subSecTitle.setLeading(interLineSpacing);
										subSecTitle.setSpacingAfter(paragraphSpacing);
										subSecTitle.setSpacingBefore(paragraphSpacing);
										document.add(subSecTitle);
										subSecTitlePrinted = true;
									}
									if (!attName.equals(subSecName)) {
										Paragraph attTitle = new Paragraph(attName, titulo);
										attTitle.setLeading(interLineSpacing);
										attTitle.setSpacingAfter(paragraphSpacing);
										attTitle.setSpacingBefore(paragraphSpacing);
										document.add(attTitle);
									}
									// HTMLWorker htmlWorker = new
									// HTMLWorker(document);
									Map<String, String> pc2 = new HashMap<String, String>();
									pc2.put("line-height", "115%");
									pc2.put("margin-bottom", "6.0pt");
									pc2.put("text-align", "center");

									HashMap<String, String> spanc1 = new HashMap<String, String>();
									spanc1.put("text-justify", "inter-word");

									StyleSheet styles = new StyleSheet();
									styles.loadTagStyle("p", "text-indent", "1.25cm");

									String str = "<html>" + "<head>" + "</head><body style=\"text-indent: 1.25cm; \">"
											+ "<p style=\"text-indent: 1.25cm; \">";
									Queue<String> allMatches = new LinkedList<>();
									String value = a.getValue();
									if (a.getValue().contains("<img")) {
										Matcher m = Pattern.compile("<img [^>]*>").matcher(a.getValue());
										while (m.find()) {
											String match = m.group();
											allMatches.add(match);
											value = value.replace(match, "<p>||IMAGE||</p>");
										}
									}
									str += value + "</p></body></html>";

									resourcesPath = new File(
											classLoader.getResource("/reports/html/example.html").getFile()).getPath();
									resourcesPath = resourcesPath.replace("example.html", "");
									resourcesPath = resourcesPath.replace("%20", " ");
									resourcesPath = "/tmp"; // corrigir
									// para
									// usar
									// um caminho
									// dinamico
									File htmlFile = File.createTempFile("output.", ".html", new File(resourcesPath));
									FileWriter fw = new FileWriter(htmlFile.getPath(), true);
									BufferedWriter conexao = new BufferedWriter(fw);
									conexao.write(str);
									conexao.newLine();
									conexao.close();

									List<Element> p = HTMLWorker.parseToList(new FileReader(htmlFile.getPath()),
											styles);
									for (int k = 0; k < p.size(); ++k) {
										if (p.get(k) instanceof Paragraph) {
											Paragraph att = (Paragraph) p.get(k);
											if (att.getContent().contains("||IMAGE||")) {
												String img = allMatches.poll();
												if (img != null) {
													Image image = Image.getInstance(new URL(
															img.replaceAll("<img src=\"", "").replaceAll("\">", "")));
													float scaler = ((document.getPageSize().getWidth()
															- document.leftMargin() - document.rightMargin())
															/ image.getWidth()) * 100;
													image.scalePercent(scaler * 0.4f);
													image.setAlignment(Element.ALIGN_CENTER);
													document.add(image);
												}
											} else {
												att.setFirstLineIndent(firstLineIndent);
												document.add(att);
											}
										} else if (p.get(k).getClass().getName().equals("com.itextpdf.text.List")) {
											com.itextpdf.text.List att = (com.itextpdf.text.List) p.get(k);
											att.setIndentationLeft(firstLineIndent);
											document.add(att);
										}
									}
									lastAttWasPlan = false;
									htmlFile.delete();
								}
							} else if (a.getType().equals(TableField.class.getCanonicalName())) {

								TableFields tf = fieldsBS.tableFieldsByAttribute(a.getId(), true);
								List<TableStructure> tabStructList = fieldsBS.listTableStructureByFields(tf);
								List<TableInstance> tabInstList = fieldsBS.listTableInstanceByFields(tf);
								if (!tabInstList.isEmpty()) {
									if (lastAttWasPlan) {
										document.setPageSize(PageSize.A4);
										document.newPage();
									}
									String attName = a.getName();
									if (!subSecTitlePrinted) {
										subSecIndex++;
										Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
										c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
										Paragraph subSecTitle = new Paragraph(c);
										subSecTitle.setLeading(interLineSpacing);
										subSecTitle.setSpacingAfter(paragraphSpacing);
										subSecTitle.setSpacingBefore(paragraphSpacing);
										document.add(subSecTitle);
										subSecTitlePrinted = true;
									}
									if (!attName.equals(subSecName)) {
										Paragraph attTitle = new Paragraph(attName, texto);
										attTitle.setAlignment(Element.ALIGN_CENTER);
										attTitle.setLeading(interLineSpacing);
										attTitle.setSpacingAfter(paragraphSpacing);
										attTitle.setSpacingBefore(paragraphSpacing);
										document.add(attTitle);
									}
									PdfPTable table = returnPdfPTable(tabStructList, tabInstList, false, false);
									document.add(table);
									lastAttWasPlan = false;
								}
							} else if (a.getType().equals(ScheduleField.class.getCanonicalName())) {
								List<ScheduleInstance> schInstList = this.fieldsBS
										.retrieveScheduleInstanceByAttribute(a.getId(), true);

								if (!schInstList.isEmpty()) {
									if (lastAttWasPlan) {
										document.setPageSize(PageSize.A4);
										document.newPage();
									}
									if (!subSecTitlePrinted) {
										subSecIndex++;
										Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName, titulo);
										c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
										Paragraph subSecTitle = new Paragraph(c);
										subSecTitle.setLeading(interLineSpacing);
										subSecTitle.setSpacingAfter(paragraphSpacing);
										subSecTitle.setSpacingBefore(paragraphSpacing);
										document.add(subSecTitle);
										subSecTitlePrinted = true;
									}
									String attName = a.getName();
									if (!attName.equals(subSecName)) {
										Paragraph attTitle = new Paragraph(attName, texto);
										attTitle.setAlignment(Element.ALIGN_CENTER);
										attTitle.setLeading(interLineSpacing);
										attTitle.setSpacingAfter(paragraphSpacing);
										attTitle.setSpacingBefore(paragraphSpacing);
										document.add(attTitle);
									}
									PdfPTable table = new PdfPTable(3);
									table.getDefaultCell();
									PdfPCell c = new PdfPCell(new Paragraph("Atividade", texto));
									c.setHorizontalAlignment(Element.ALIGN_CENTER);
									c.setBackgroundColor(headerBgColor);
									table.addCell(c);
									c = new PdfPCell(new Paragraph("Início", texto));
									c.setHorizontalAlignment(Element.ALIGN_CENTER);
									c.setBackgroundColor(headerBgColor);

									table.addCell(c);
									c = new PdfPCell(new Paragraph("Fim", texto));
									c.setHorizontalAlignment(Element.ALIGN_CENTER);
									c.setBackgroundColor(headerBgColor);
									table.addCell(c);

									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									for (ScheduleInstance sch : schInstList) {
										table.addCell(new Paragraph(sch.getDescription(), texto));
										table.addCell(new Paragraph(sdf.format(sch.getBegin()), texto));
										table.addCell(new Paragraph(sdf.format(sch.getEnd()), texto));
									}
									document.add(table);
									lastAttWasPlan = false;
								}
							} else if (a.getType().equals(SelectPlan.class.getCanonicalName())) {
								if (a.getValue() != null) {
									Plan plan = planBS.retrieveById(Long.parseLong(a.getValue()));
									// LOGGER.info("2 - " + plan.getName());
									List<PdfPTable> planTableList = this.generatePDFplanTable(plan);
									boolean first = true;
									for (PdfPTable planTable : planTableList) {
										if (!lastAttWasPlan) {
											document.setPageSize(PageSize.A4.rotate());
										}
										document.newPage();
										if (first) {
											if (!subSecTitlePrinted) {
												subSecIndex++;
												Chunk c = new Chunk(secIndex + "." + subSecIndex + ". " + subSecName,
														titulo);
												c.setGenericTag(secIndex + "." + subSecIndex + ". " + subSecName);
												Paragraph subSecTitle = new Paragraph(c);
												document.add(subSecTitle);
												subSecTitlePrinted = true;
											}
											Paragraph attTitle = new Paragraph(plan.getName(), texto);
											attTitle.setAlignment(Element.ALIGN_CENTER);
											document.add(attTitle);
											first = false;
											lastAttWasPlan = true;
										}
										document.add(planTable);
									}
									// document.setPageSize(PageSize.A4);
									// document.newPage();
								}
							}
						}
					}
				}
			}
		}
		if (havePreText)
			preTextDocument.close();
		if (haveContent)
			document.close();

		summaryDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		summaryDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
		summaryDocument.open();

		Paragraph summaryTitle = new Paragraph("Sumário", titulo);
		summaryTitle.setLeading(interLineSpacing);
		summaryTitle.setSpacingAfter(paragraphSpacing);
		summaryTitle.setSpacingBefore(paragraphSpacing);
		summaryDocument.add(summaryTitle);

		Chunk dottedLine = new Chunk(new DottedLineSeparator());
		List<SimpleEntry<String, SimpleEntry<String, Integer>>> entries = event.getTOC();
		Paragraph p;
		int summaryCountPages = 0;
		for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : entries) {
			// LOGGER.info(entry.getKey());
			Chunk chunk = new Chunk(entry.getKey(), titulo);
			SimpleEntry<String, Integer> value = entry.getValue();
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p = new Paragraph(chunk);
			p.add(dottedLine);
			chunk = new Chunk(String.valueOf(value.getValue()), titulo);
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p.add(chunk);
			summaryDocument.add(p);
		}
		summaryDocument.close();
		PdfReader summaryAux = new PdfReader(SUMMARY);
		PdfReader cover = new PdfReader(COVER);
		summaryCountPages = summaryAux.getNumberOfPages() + cover.getNumberOfPages();
		PdfReader preText;
		if (havePreText) {
			preText = new PdfReader(PRETEXT);
			summaryCountPages += preText.getNumberOfPages();
		}

		com.itextpdf.text.Document finalSummaryDocument = new com.itextpdf.text.Document();
		// Formato A4 do documento
		finalSummaryDocument.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		finalSummaryDocument.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);
		PdfWriter finalSummaryWriter = PdfWriter.getInstance(finalSummaryDocument,
				new FileOutputStream(finalSummaryPdfFile));
		finalSummaryDocument.open();

		finalSummaryDocument.add(summaryTitle);
		for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : entries) {
			// LOGGER.info(entry.getKey());
			Chunk chunk = new Chunk(entry.getKey(), titulo);
			SimpleEntry<String, Integer> value = entry.getValue();
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p = new Paragraph(chunk);
			p.add(dottedLine);
			chunk = new Chunk(String.valueOf(value.getValue() + summaryCountPages), titulo);
			chunk.setAction(PdfAction.gotoLocalPage(value.getKey(), false));
			p.add(chunk);
			finalSummaryDocument.add(p);
		}
		finalSummaryDocument.close();

		com.itextpdf.text.Document newDocument = new com.itextpdf.text.Document();

		PdfImportedPage page;
		int n;
		PdfCopy copy = new PdfCopy(newDocument, new FileOutputStream(DEST));
		newDocument.open();

		
		PdfReader summary = new PdfReader(FINALSUMMARY);
		PdfReader content;
		//int unnumberedPgsCount = summaryCountPages;
		// CAPA
		n = cover.getNumberOfPages();
		//unnumberedPgsCount += n;
		for (int i = 0; i < n;) {
			page = copy.getImportedPage(cover, ++i);
			copy.addPage(page);
		}
		if (havePreText) {
			preText = new PdfReader(PRETEXT);
			// SEÇÕES PRE TEXTUAIS
			n = preText.getNumberOfPages();
			//unnumberedPgsCount += n;
			for (int i = 0; i < n;) {
				page = copy.getImportedPage(preText, ++i);
				copy.addPage(page);
			}
		}
		if (haveContent) {
			// SUMÁRIO
			n = summary.getNumberOfPages();
			for (int i = 0; i < n;) {
				page = copy.getImportedPage(summary, ++i);
				copy.addPage(page);
			}
			content = new PdfReader(SRC);
			// CONTEÚDO
			n = content.getNumberOfPages();
			for (int i = 0; i < n;) {
				page = copy.getImportedPage(content, ++i);
				copy.addPage(page);
			}
		}

		newDocument.close();

		manipulatePdf(DEST, FINAL, newDocument, summaryCountPages);
		InputStream inpStr = new FileInputStream(finalPdfFile);
		return inpStr;
	}

	/**
	 * Gera tabelas PDF do plano de meta passado por parâmetro.
	 * 
	 * @param plan
	 *            Plano de metas para geração de PDF.
	 * @return List<PdfPTable> Lista de tabelas em PDF.
	 */
	public List<PdfPTable> generatePDFplanTable(Plan plan) {

		CMYKColor eixoHeaderBgColor = new CMYKColor(0, 0, 0, 70);
		// Cor cinza - cabeçalho objetivo do plano de metas
		CMYKColor objetivoHeaderBgColor = new CMYKColor(0, 0, 0, 50);
		// Cor cinza - conteudo objetivo do plano de metas
		CMYKColor objetivoRowBgColor = new CMYKColor(0, 0, 0, 20);
		// Cor branco - borda tabela plano de metas
		CMYKColor borderPlanColor = new CMYKColor(0, 0, 0, 0);
		// Cor roxo - cabeçalho indicador do plano de metas
		CMYKColor indicadorHeaderBgColor = new CMYKColor(62, 30, 0, 18);
		// Cor roxo - conteudo indicador do plano de metas
		CMYKColor indicadorRowBgColor = new CMYKColor(28, 14, 0, 9);
		Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);
		Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
		float paragraphSpacing = 22.6772f;
		PdfPTable table = new PdfPTable(5);
		// Plan plan = planBS.retrieveById(planId);
		List<StructureLevelInstance> list = structureBS.listRootLevelInstanceByPlan(plan);
		// ArrayList<Long> attInstList = new ArrayList<Long>();
		ArrayList<PdfPTable> tableList = new ArrayList<PdfPTable>();
		for (StructureLevelInstance s : list) {

			PaginatedList<StructureLevelInstance> structureLevelSons = new PaginatedList<>();
			structureLevelSons.setList(structureBS.retrieveLevelInstanceSons(s.getId()));
			s.setSons(structureLevelSons);

			List<Attribute> attributeList = structureBS.retrieveLevelAttributes(s.getLevel());
			attributeList = structureBS.setAttributesInstances(s, attributeList);

			if (!s.getSons().getList().isEmpty()) {
				for (StructureLevelInstance son : s.getSons().getList()) {
					if (son.getLevel().isObjective()) {// Objetivo
						String eixoLabel = s.getLevel().getName() + ": ";
						String eixoName = s.getName();
						table = new PdfPTable(5);
						table.setSpacingBefore(paragraphSpacing);
						table.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.setWidthPercentage(100.0f);

						Phrase phraseEixoLabel = new Phrase(eixoLabel, titulo);
						Phrase phraseEixoName = new Phrase(eixoName, texto);
						phraseEixoLabel.add(phraseEixoName);

						PdfPCell cell = new PdfPCell(phraseEixoLabel);

						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						// centraliza verticalmente
						Float fontSize = titulo.getSize();
						Float capHeight = titulo.getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);
						Float padding = 5f;
						cell.setPadding(padding);
						cell.setPaddingTop(capHeight - fontSize + padding);

						cell.setBackgroundColor(eixoHeaderBgColor);
						cell.setBorderColor(borderPlanColor);
						cell.setColspan(5);
						table.addCell(cell);

						String objetivoLabel = son.getLevel().getName() + ": ";
						String objetivoName = son.getName();
						Phrase phraseObjetivoLabel = new Phrase(objetivoLabel, titulo);
						Phrase phraseObjetivoName = new Phrase(objetivoName, texto);
						phraseObjetivoLabel.add(phraseObjetivoName);

						cell = new PdfPCell(phraseObjetivoLabel);

						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						// centraliza verticalmente
						cell.setPadding(padding);
						cell.setPaddingTop(capHeight - fontSize + padding);

						cell.setBackgroundColor(objetivoHeaderBgColor);
						cell.setBorderColor(borderPlanColor);
						cell.setColspan(5);
						table.addCell(cell);

						List<BudgetDTO> budgetList = new ArrayList<BudgetDTO>();
						int budgetListSize = 0;

						List<Attribute> sonAttributeList = structureBS.retrieveLevelAttributes(son.getLevel());
						sonAttributeList = structureBS.setAttributesInstances(son, sonAttributeList);
						if (!sonAttributeList.isEmpty()) {
							String bsc = "-";
							for (Attribute sonAttribute : sonAttributeList) {

								if (sonAttribute.isBscField()) { // Perspectiva
									AttributeInstance attInst = attrHelper.retrieveAttributeInstance(son, sonAttribute);
									if (attInst != null) {
										bsc = attInst.getValue();
									} else {
										bsc = "-";
									}
								}
								if (sonAttribute.getBudgets() != null && !sonAttribute.getBudgets().isEmpty()) { // Orçamento
									budgetList = sonAttribute.getBudgets();
									budgetListSize = budgetList.size();
								}
							}
							if (budgetListSize > 0) {
								// Perspectiva BSC
								cell = new PdfPCell(new Phrase("Perspectiva do BSC", titulo));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoHeaderBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Orçamento
								cell = new PdfPCell(new Phrase("Orçamento", titulo));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoHeaderBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(4);
								table.addCell(cell);

								// Perspectiva BSC - valor
								cell = new PdfPCell(new Phrase(bsc, texto));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								cell.setRowspan(budgetListSize + 1);
								table.addCell(cell);

								// Subação
								cell = new PdfPCell(new Phrase("Subação", titulo));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Planejado
								cell = new PdfPCell(new Phrase("Planejado", titulo));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Empenhado
								cell = new PdfPCell(new Phrase("Empenhado", titulo));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								// Realizado
								cell = new PdfPCell(new Phrase("Realizado", titulo));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								// centraliza verticalmente
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBackgroundColor(objetivoRowBgColor);
								cell.setBorderColor(borderPlanColor);
								cell.setColspan(1);
								table.addCell(cell);

								for (BudgetDTO b : budgetList) {
									// Subação - valor
									cell = new PdfPCell(new Phrase(b.getBudget().getSubAction(), texto));
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									// centraliza verticalmente
									cell.setPadding(padding);
									cell.setPaddingTop(capHeight - fontSize + padding);
									cell.setBackgroundColor(objetivoRowBgColor);
									cell.setBorderColor(borderPlanColor);
									cell.setColspan(1);
									table.addCell(cell);

									// Para formatar valores em R$
									Locale ptBr = new Locale("pt", "BR"); // Locale
																			// para
																			// o
																			// Brasil
									NumberFormat moedaFormat = NumberFormat.getCurrencyInstance(ptBr);

									// Planejado - valor
									cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudgetLoa()), texto));
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									// centraliza verticalmente
									cell.setPadding(padding);
									cell.setPaddingTop(capHeight - fontSize + padding);
									cell.setBackgroundColor(objetivoRowBgColor);
									cell.setBorderColor(borderPlanColor);
									cell.setColspan(1);
									table.addCell(cell);

									// Empenhado - valor
									cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudget().getCommitted()), texto));
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									// centraliza verticalmente
									cell.setPadding(padding);
									cell.setPaddingTop(capHeight - fontSize + padding);
									cell.setBackgroundColor(objetivoRowBgColor);
									cell.setBorderColor(borderPlanColor);
									cell.setColspan(1);
									table.addCell(cell);

									// Realizado - valor
									cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudget().getRealized()), texto));
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									// centraliza verticalmente
									cell.setPadding(padding);
									cell.setPaddingTop(capHeight - fontSize + padding);
									cell.setBackgroundColor(objetivoRowBgColor);
									cell.setBorderColor(borderPlanColor);
									cell.setColspan(1);
									table.addCell(cell);
								}
							}
						}

						StructureLevelInstance sonAux = son;
						PaginatedList<StructureLevelInstance> objSonsList = new PaginatedList<>();
						objSonsList.setList(structureBS.retrieveLevelInstanceSons(sonAux.getId()));
						sonAux.setSons(objSonsList);
						objSonsList = sonAux.getSons();

						if (objSonsList.getList().size() > 0) {
							// Indicadores
							cell = new PdfPCell(new Phrase("Indicadores", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(indicadorHeaderBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// Metas
							cell = new PdfPCell(new Phrase("Metas", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoHeaderBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(2);
							table.addCell(cell);

							// Esperado
							cell = new PdfPCell(new Phrase("Esperado", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoHeaderBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// Alcançado
							cell = new PdfPCell(new Phrase("Alcançado", titulo));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// centraliza verticalmente
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBackgroundColor(objetivoHeaderBgColor);
							cell.setBorderColor(borderPlanColor);
							cell.setColspan(1);
							table.addCell(cell);

							// String indicadorName = "";
							String calculo = "";
							Long responsavel = (long) -1;

							for (StructureLevelInstance indicatorSon : objSonsList.getList()) { // Indicadores
								if (indicatorSon.getLevel().isIndicator()) {
									List<Attribute> indicatorSonAttributeList = structureBS
											.retrieveLevelAttributes(indicatorSon.getLevel());
									indicatorSonAttributeList = structureBS.setAttributesInstances(indicatorSon,
											indicatorSonAttributeList);
									indicatorSon.getLevel().setAttributes(indicatorSonAttributeList);
									for (Attribute indicatorSonAttribute : indicatorSonAttributeList) {
										if (indicatorSonAttribute.getId() == 14) { // Cálculo
											AttributeInstance attInst = attrHelper.retrieveAttributeInstance(
													indicatorSon, structureBS.retrieveAttribute((long) 14));
											if (attInst != null) {
												calculo = indicatorSonAttribute.getAttributeInstance().getValue();
											} else {
												calculo = "-";
											}
										}
										if (indicatorSonAttribute.getId() == 7) { // responsável
											AttributeInstance attInst = attrHelper.retrieveAttributeInstance(
													indicatorSon, structureBS.retrieveAttribute((long) 7));
											if (attInst != null) {
												responsavel = Long.parseLong(
														indicatorSonAttribute.getAttributeInstance().getValue());
											} else {
												responsavel = (long) -1;
											}

										}
									}
									// LOGGER.info(calculo);
									// LOGGER.info(responsavel);
									PaginatedList<StructureLevelInstance> levelInstances = new PaginatedList<>();
									levelInstances
											.setList(this.structureBS.retrieveLevelInstanceSons(indicatorSon.getId()));

									// Indicador - valores

									User responsible = userBS.existsByUser(responsavel);
									// LOGGER.info(responsible.toString());
									Phrase indicador = new Phrase(indicatorSon.getName(), texto);
									indicador.add(new Phrase("\n\nCálculo: ", titulo));
									indicador.add(new Phrase(calculo, texto));
									indicador.add(new Phrase("\nResponsável: ", titulo));
									if (responsible != null) {
										indicador.add(new Phrase(responsible.getName(), texto));
									} else {
										indicador.add(new Phrase("-", texto));
									}

									cell = new PdfPCell(indicador);
									cell.setHorizontalAlignment(Element.ALIGN_CENTER);
									// centraliza verticalmente
									cell.setPadding(padding);
									cell.setPaddingTop(capHeight - fontSize + padding);
									cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
									cell.setBackgroundColor(indicadorRowBgColor);
									cell.setBorderColor(borderPlanColor);
									cell.setColspan(1);
									cell.setMinimumHeight(4 * fontSize);
									if (levelInstances.getList().size() > 0) {
										cell.setRowspan(levelInstances.getList().size());
									}
									table.addCell(cell);

									for (int index = 0; index < levelInstances.getList().size(); index++) {

										levelInstances.getList().get(index).getLevel().setAttributes(this.structureBS
												.retrieveLevelSonsAttributes(levelInstances.getList().get(index)));
									}

									StructureLevelInstance levelInstanceAux = new StructureLevelInstance();
									levelInstanceAux.setId(indicatorSon.getId());
									levelInstanceAux.setSons(levelInstances);

									// CABEÇALHO
									// METAS
									if (levelInstanceAux.getSons().getList().size() != 0) {
										ArrayList<String> expected = new ArrayList<String>();
										ArrayList<String> reached = new ArrayList<String>();

										HashMap<Long, ArrayList<String>> meta = new HashMap<Long, ArrayList<String>>();
										AttributeInstance formatAttr = this.attrHelper
												.retrieveFormatAttributeInstance(levelInstanceAux);
										FormatValue formatValue = FormatValue.forAttributeInstance(formatAttr);
										for (int goalIndex = 0; goalIndex < levelInstanceAux.getSons().getList()
												.size(); goalIndex++) {

											ArrayList<String> values = new ArrayList<String>();
											// LOGGER.info(levelInstanceAux.getSons().get(goalIndex).toString());

											List<AttributeInstance> attInst = structureBS.listAttributeInstanceByLevel(
													levelInstanceAux.getSons().getList().get(goalIndex));
											List<Attribute> attList = structureBS.listAttributesPDF(
													levelInstanceAux.getSons().getList().get(goalIndex).getLevel());

											for (Attribute a : attList) {
												if (a.isExpectedField()) { // esperado
													for (AttributeInstance at : attInst) {
														if (at.getAttribute().getId() == a.getId()) {

															at.setFormattedValue(formatValue.format(at.getValue()));
															expected.add(at.getFormattedValue());
															values.add(at.getFormattedValue());
														}
													}
												} else if (a.isReachedField()) { // realizado
													for (AttributeInstance at : attInst) {
														if (at.getAttribute().getId() == a.getId()) {
															at.setFormattedValue(formatValue.format(at.getValue()));
															reached.add(at.getFormattedValue());
															values.add(at.getFormattedValue());
														}
													}
												}
											}

											meta.put(levelInstanceAux.getSons().getList().get(goalIndex).getId(),
													values);
										}

										List<Long> keys = new ArrayList<Long>(meta.keySet());
										Collections.sort(keys);
										// LOGGER.info(keys);
										int i = 0;
										for (Long x : keys) {
											// LOGGER.info(meta.get(x));
											cell = new PdfPCell(
													new Phrase(levelInstances.getList().get(i).getName(), texto));
											i++;
											cell.setHorizontalAlignment(Element.ALIGN_CENTER);
											cell.setPadding(padding);
											cell.setPaddingTop(capHeight - fontSize + padding);
											cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
											cell.setBackgroundColor(objetivoRowBgColor);
											cell.setBorderColor(borderPlanColor);
											cell.setColspan(2);
											table.addCell(cell);

											if (meta.get(x).size() > 1) {
												cell = new PdfPCell(new Phrase(meta.get(x).get(1), texto));
												cell.setHorizontalAlignment(Element.ALIGN_CENTER);
												cell.setPadding(padding);
												cell.setPaddingTop(capHeight - fontSize + padding);
												cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
												cell.setBackgroundColor(objetivoRowBgColor);
												cell.setBorderColor(borderPlanColor);
												table.addCell(cell);

												cell = new PdfPCell(new Phrase(meta.get(x).get(0), texto));
												cell.setHorizontalAlignment(Element.ALIGN_CENTER);
												cell.setPadding(padding);
												cell.setPaddingTop(capHeight - fontSize + padding);
												cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
												cell.setBackgroundColor(objetivoRowBgColor);
												cell.setBorderColor(borderPlanColor);
												table.addCell(cell);
											} else if (meta.get(x).size() == 0) {
												cell = new PdfPCell(new Phrase("-", texto));
												cell.setHorizontalAlignment(Element.ALIGN_CENTER);
												cell.setPadding(padding);
												cell.setPaddingTop(capHeight - fontSize + padding);
												cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
												cell.setBackgroundColor(objetivoRowBgColor);
												cell.setBorderColor(borderPlanColor);
												table.addCell(cell);

												cell = new PdfPCell(new Phrase("-", texto));
												cell.setHorizontalAlignment(Element.ALIGN_CENTER);
												cell.setPadding(padding);
												cell.setPaddingTop(capHeight - fontSize + padding);
												cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
												cell.setBackgroundColor(objetivoRowBgColor);
												cell.setBorderColor(borderPlanColor);
												table.addCell(cell);

											} else {
												cell = new PdfPCell(new Phrase(meta.get(x).get(0), texto));
												cell.setHorizontalAlignment(Element.ALIGN_CENTER);
												cell.setPadding(padding);
												cell.setPaddingTop(capHeight - fontSize + padding);
												cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
												cell.setBackgroundColor(objetivoRowBgColor);
												cell.setBorderColor(borderPlanColor);
												table.addCell(cell);

												cell = new PdfPCell(new Phrase("-", texto));
												cell.setHorizontalAlignment(Element.ALIGN_CENTER);
												cell.setPadding(padding);
												cell.setPaddingTop(capHeight - fontSize + padding);
												cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
												cell.setBackgroundColor(objetivoRowBgColor);
												cell.setBorderColor(borderPlanColor);
												table.addCell(cell);
											}
										}
									} else {
										cell = new PdfPCell(new Phrase("-", texto));
										cell.setHorizontalAlignment(Element.ALIGN_CENTER);
										cell.setPadding(padding);
										cell.setPaddingTop(capHeight - fontSize + padding);
										cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
										cell.setBackgroundColor(objetivoRowBgColor);
										cell.setBorderColor(borderPlanColor);
										cell.setColspan(2);
										table.addCell(cell);
										cell.setColspan(1);
										table.addCell(cell);
										table.addCell(cell);

									}
								}
							}
						}
						tableList.add(table);
					}
				}
			}
		}
		return tableList;
	}

	/**
	 * Gera tabela PDF de atributo do tipo "TableField".
	 * 
	 * @param tabStructList
	 *            Lista de estruturas da tabela (Cabeçalhos).
	 * @param tabInstList
	 *            Lista de instâncias de tabela (Valores).
	 * @return PdfPTable Tabela do atributo em PDF.
	 * @throws DocumentException
	 */

	public PdfPTable returnPdfPTable(List<TableStructure> tabStructList, List<TableInstance> tabInstList,
			boolean hideHeaders, boolean hideBorders) throws DocumentException {
		PdfPTable table = new PdfPTable(tabStructList.size());
		table.setHorizontalAlignment(Element.ALIGN_CENTER);

		if (hideBorders) {
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		}
		table.setWidthPercentage(100);
		//int[] sizes = new int[tabStructList.size()];
		//int i = 0;

		// ajuste de widths
		/*
		 * for (TableStructure ts : tabStructList) { if (ts.getLabel().length()
		 * < 4) { sizes[i] = 6; } else { String[] split = ts.getLabel().split(
		 * " "); String maior = split[0]; for (int j = 0; j < split.length; j++)
		 * { if (split[j].length() > maior.length()) { maior = split[j]; } } if
		 * (split.length <= 3 && maior.length() < 6) { sizes[i] =
		 * ts.getLabel().length(); } else { sizes[i] = maior.length(); } } i++;
		 * }
		 */
		// table.setWidths(sizes);
		Font textoTabela = FontFactory.getFont(FontFactory.TIMES, 10.0f);
		if (!hideHeaders) {
			for (TableStructure ts : tabStructList) {
				PdfPCell c = new PdfPCell(new Paragraph(ts.getLabel(), textoTabela));
				CMYKColor bgColor = new CMYKColor(55, 45, 42, 7);
				c.setBackgroundColor(bgColor);
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c);
			}
			for (TableInstance ti : tabInstList) {
				List<TableValues> tabValuesList = fieldsBS.listTableValuesByInstance(ti);
				for (TableValues tv : tabValuesList) {
					if (tv.getTableStructure().getType().equals(Currency.class.getCanonicalName())) {
						table.addCell(new Paragraph(FormatValue.MONETARY.format(tv.getValue()), textoTabela));
					} else if (tv.getTableStructure().getType().equals(Percentage.class.getCanonicalName())) {
						table.addCell(new Paragraph(FormatValue.PERCENTAGE.format(tv.getValue()), textoTabela));
					} else if (tv.getTableStructure().getType().equals(NumberField.class.getCanonicalName())) {
						double integerTest = Double.valueOf(tv.getValue());
						if (integerTest == (int) integerTest) {
							table.addCell(new Paragraph(tv.getValue(), textoTabela));
						} else {
							table.addCell(
									new Paragraph(
											FormatValue.NUMERIC
													.format(tv.getValue()),
											textoTabela));
						}
					} else if (tv.getTableStructure().getType().equals(ResponsibleField.class.getCanonicalName())) {
						table.addCell(new Paragraph(this.userBS.existsByUser(Long.valueOf(tv.getValue())).getName(),
								textoTabela));
					} else {
						table.addCell(new Paragraph(tv.getValue(), textoTabela));
					}
				}
			}
		} else {
			if (tabStructList.size() == 2) {
				table.setWidths(new float[] { 1, 1 });
			}
			for (TableInstance ti : tabInstList) {
				List<TableValues> tabValuesList = fieldsBS.listTableValuesByInstance(ti);
				for (TableValues tv : tabValuesList) {
					PdfPCell c = new PdfPCell();
					c.setBorder(Rectangle.NO_BORDER);
					// c.setHorizontalAlignment(Element.ALIGN_CENTER);
					Paragraph cellContent = new Paragraph();
					if (tv.getTableStructure().getType().equals(Currency.class.getCanonicalName())) {
						cellContent = new Paragraph(FormatValue.MONETARY.format(tv.getValue()), textoTabela);

					} else if (tv.getTableStructure().getType().equals(Percentage.class.getCanonicalName())) {
						cellContent = new Paragraph(FormatValue.PERCENTAGE.format(tv.getValue()), textoTabela);
					} else if (tv.getTableStructure().getType().equals(ResponsibleField.class.getCanonicalName())) {
						cellContent = new Paragraph(this.userBS.existsByUser(Long.valueOf(tv.getValue())).getName(),
								textoTabela);
					} else {
						cellContent = new Paragraph(tv.getValue(), textoTabela);
					}
					// cellContent.setAlignment(Element.ALIGN_CENTER);
					c.addElement(cellContent);
					table.addCell(c);
				}
			}
		}
		return table;
	}
	/**
	 * Exportar para pdf atributos de um level
	 * @param levelId
	 * 			Id do level
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public InputStream exportLevelAttributes(Long levelId)
			throws MalformedURLException, IOException, DocumentException {
		// TODO Auto-generated method stub
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();

		ClassLoader classLoader = getClass().getClassLoader();
		String resourcesPath = new File(classLoader.getResource("/reports/pdf/example.pdf").getFile()).getPath();
		resourcesPath = "/tmp"; // corrigir para salvar com um caminho
		// dinamico
		resourcesPath = resourcesPath.replace("example.pdf", "");
		resourcesPath = resourcesPath.replace("%20", " ");
		File pdfFile = File.createTempFile("output.", ".pdf", new File(resourcesPath));
		InputStream in = new FileInputStream(pdfFile);
		@SuppressWarnings("unused")
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

		// DEFINIÇÕES DE FONTE, MARGENS, ESPAÇAMENTO E CORES
		Font texto = FontFactory.getFont(FontFactory.TIMES, 12.0f);
		Font textoItalico = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12.0f);

		Font titulo = FontFactory.getFont(FontFactory.TIMES_BOLD, 12.0f);
		//Font tituloCapa = FontFactory.getFont(FontFactory.TIMES_BOLD, 14.0f);
		// Cor cinza - cabeçalho das tabelas
		//CMYKColor headerBgColor = new CMYKColor(55, 45, 42, 7);

		// 0,8 cm acima e abaixo
		float paragraphSpacing = 22.6772f;
		// Parágrafo com 1,25 cm na primeira linha
		//float firstLineIndent = 35.43307f;
		// 1,5 entrelinhas
		//float interLineSpacing = texto.getCalculatedLeading(1.5f);
		// Formato A4 do documento
		document.setPageSize(PageSize.A4);
		// Margens Superior e esquerda: 3 cm Inferior e direita: 2 cm
		document.setMargins(85.0394f, 56.6929f, 85.0394f, 56.6929f);

		document.open();

		// CABEÇALHO
		String companyLogoUrl = domain.getCompany().getLogo();
		String fpdiLogoUrl = "http://cloud.progolden.com.br/file/8345";//new File(classLoader.getResource("logo.png").getFile()).getPath();
		if (!companyLogoUrl.trim().isEmpty()) {
			Image companyLogo = Image.getInstance(new URL(companyLogoUrl));
			Image fpdiLogo = Image.getInstance(fpdiLogoUrl);
			// image.scaleAbsolute(150f, 150f);
			float companyLogoScaler = ((document.getPageSize().getWidth() - document.leftMargin()
					- document.rightMargin()) / companyLogo.getWidth()) * 100;
			float fpdiLogoScaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin())
					/ fpdiLogo.getWidth()) * 100;
			companyLogo.scalePercent(companyLogoScaler * 0.25f);
			companyLogo.setAlignment(Element.ALIGN_LEFT);
			fpdiLogo.scalePercent(fpdiLogoScaler * 0.15f);
			fpdiLogo.setAlignment(Element.ALIGN_RIGHT);
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 1 });
			table.setSpacingAfter(paragraphSpacing);

			PdfPCell companyCell = new PdfPCell(companyLogo);
			PdfPCell fpdiCell = new PdfPCell(fpdiLogo);

			companyCell.setBorder(Rectangle.NO_BORDER);
			fpdiCell.setBorder(Rectangle.NO_BORDER);
			companyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			fpdiCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

			table.addCell(companyCell);
			table.addCell(fpdiCell);
			document.add(table);
		}

		StructureLevelInstance levelInstance = this.structHelper.retrieveLevelInstance(levelId);
		String levelInstanceName = levelInstance.getName();
		String levelInstanceType;
		if (levelInstance.getLevel().isIndicator()) {
			if (levelInstance.isAggregate()) {
				levelInstanceType = levelInstance.getLevel().getName() + " agregado";
			} else {
				levelInstanceType = levelInstance.getLevel().getName() + " simples";
			}
		} else {
			levelInstanceType = levelInstance.getLevel().getName();
		}
		String planName = levelInstance.getPlan().getName();
		String planMacroName = levelInstance.getPlan().getParent().getName();

		// PLANO MACRO
		Paragraph planMacroParagraph = new Paragraph(planMacroName, titulo);
		document.add(planMacroParagraph);
		

		// PLANO DE METAS
		Paragraph planParagraph = new Paragraph(planName, titulo);
		document.add(planParagraph);

		// DATA EXPORTAÇÃO
		SimpleDateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		// Paragraph dataExportacaoLabel = new Paragraph("Data da exportação: ",
		// titulo);
		Paragraph dataExportacaoValue = new Paragraph(brDateFormat.format(cal.getTime()), texto);
		dataExportacaoValue.setSpacingAfter(paragraphSpacing);
		document.add(dataExportacaoValue);

		// NOME DO NIVEL
		Paragraph levelInstanceNameParagraph = new Paragraph(levelInstanceName, titulo);
		document.add(levelInstanceNameParagraph);

		// TIPO DO NIVEL
		Paragraph levelInstanceTypeParagraph = new Paragraph(levelInstanceType, textoItalico);
		document.add(levelInstanceTypeParagraph);

		// RENDIMENTO DO NIVEL
		DecimalFormat decimalFormatDbl = new DecimalFormat("#,##0.00");
		Paragraph proceedsParagraph = new Paragraph();
		Phrase proceedsValue;
		Phrase proceedsLabel = new Phrase("Rendimento atual do nível: ", titulo);
		if (levelInstance.getLevelValue() == null) {
			proceedsValue = new Phrase("0,00%", texto);
		} else {

			proceedsValue = new Phrase(decimalFormatDbl.format(Double.valueOf(levelInstance.getLevelValue())) + "%",
					texto);
		}
		proceedsParagraph.add(proceedsLabel);
		proceedsParagraph.add(proceedsValue);
		proceedsParagraph.setSpacingAfter(paragraphSpacing);
		document.add(proceedsParagraph);

		List<Attribute> attrList = this.structureBS.retrieveLevelSonsAttributes(levelInstance);

		for (Attribute attribute : attrList) {
			// LOGGER.info(attribute.toString());
			if (attribute.isRequired() || attribute.getType().equals(BudgetField.class.getCanonicalName())
					|| attribute.isReachedField()) {
				// LOGGER.info(attribute.toString());

				Paragraph attributeParagraph = new Paragraph();
				Phrase attributeValue = new Phrase();
				Phrase attributeLabel = new Phrase(attribute.getLabel() + ": ", titulo);

				if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
					if (attribute.getAttributeInstances().get(0) != null) {

						User responsible = this.userBS
								.existsByUser(Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
						if (responsible != null) {
							attributeValue = new Phrase(responsible.getName(), texto);
							attributeParagraph.add(attributeLabel);
							attributeParagraph.add(attributeValue);
							document.add(attributeParagraph);
						}
					}
				} else if (attribute.getType().equals(ActionPlanField.class.getCanonicalName())) {

					PaginatedList<ActionPlan> actionPlanList = this.fieldsBS.listActionPlansByInstance(levelInstance);
					if (actionPlanList.getList() != null && !actionPlanList.getList().isEmpty()) {
						attributeParagraph.setSpacingBefore(paragraphSpacing);
						attributeLabel = new Phrase(attribute.getLabel(), titulo);
						attributeParagraph.add(attributeLabel);
						document.add(attributeParagraph);

						PdfPTable table = new PdfPTable(4);
						table.setSpacingBefore(paragraphSpacing / 2);
						table.setWidthPercentage(100);
						table.getDefaultCell();
						PdfPCell c = new PdfPCell(new Paragraph("Ação", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						// c.setBackgroundColor(headerBgColor);
						table.addCell(c);
						c = new PdfPCell(new Paragraph("Responsável", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						// c.setBackgroundColor(headerBgColor);
						table.addCell(c);

						c = new PdfPCell(new Paragraph("Início", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						// c.setBackgroundColor(headerBgColor);
						table.addCell(c);

						c = new PdfPCell(new Paragraph("Fim", texto));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						// c.setBackgroundColor(headerBgColor);
						table.addCell(c);

						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						for (ActionPlan acp : actionPlanList.getList()) {
							table.addCell(new Paragraph(acp.getDescription(), texto));
							table.addCell(new Paragraph(acp.getResponsible(), texto));
							table.addCell(new Paragraph(sdf.format(acp.getBegin()), texto));
							table.addCell(new Paragraph(sdf.format(acp.getEnd()), texto));
						}
						document.add(table);
					}
				} else if (attribute.getType().equals(BudgetField.class.getCanonicalName())) {
					List<BudgetDTO> budgetList = new ArrayList<BudgetDTO>();
					budgetList = this.fieldsBS.getBudgets(levelInstance);
					if (!budgetList.isEmpty()) {
						attributeParagraph.setSpacingBefore(paragraphSpacing);
						attributeLabel = new Phrase(attribute.getLabel(), titulo);
						attributeParagraph.add(attributeLabel);
						document.add(attributeParagraph);

						// Orçamento
						PdfPTable table = new PdfPTable(4);
						table.setSpacingBefore(paragraphSpacing / 2);
						table.setWidthPercentage(100);

						// Subação
						PdfPCell cell = new PdfPCell(new Phrase("Subação", texto));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);

						// Planejado
						cell = new PdfPCell(new Phrase("Planejado", texto));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);

						// Empenhado
						cell = new PdfPCell(new Phrase("Empenhado", texto));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);

						// Realizado
						cell = new PdfPCell(new Phrase("Realizado", texto));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);

						for (BudgetDTO b : budgetList) {
							// Subação - valor
							cell = new PdfPCell(new Phrase(b.getBudget().getSubAction(), texto));
							table.addCell(cell);

							// Para formatar valores em R$
							Locale ptBr = new Locale("pt", "BR"); // Locale
																	// para
																	// o
																	// Brasil
							NumberFormat moedaFormat = NumberFormat.getCurrencyInstance(ptBr);

							// Planejado - valor
							cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudgetLoa()), texto));
							table.addCell(cell);

							// Empenhado - valor
							cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudget().getCommitted()), texto));
							table.addCell(cell);

							// Realizado - valor
							cell = new PdfPCell(new Phrase(moedaFormat.format(b.getBudget().getRealized()), texto));
							table.addCell(cell);
						}
						document.add(table);
					}

				} else {
					if (levelInstance.getLevel().isGoal()) {
						AttributeInstance attinst = attribute.getAttributeInstances().get(0);
						if (attribute.isMaximumField() || attribute.isMinimumField() || attribute.isExpectedField()
								|| attribute.isReachedField()) {

							FormatValue formatValue = FormatValue.forAttributeInstance(
									this.attrHelper.retrieveFormatAttributeInstance(levelInstance.getParent()));
							if (attinst != null) {
								if (attinst.getValue() != null)
									attributeValue = new Phrase(formatValue.format(attinst.getValue()), texto);
								else
									attributeValue = new Phrase("-", texto);
							} else {
								attributeValue = new Phrase("-", texto);
							}
						} else {
							if (attinst != null) {
								attributeValue = new Phrase(attinst.getValue(), texto);
							}
						}
						if (attinst != null) {
							attributeParagraph.add(attributeLabel);
							attributeParagraph.add(attributeValue);
							document.add(attributeParagraph);
						}
					} else {
						if (attribute.getAttributeInstances().get(0) != null) {
							attributeValue = new Phrase(attribute.getAttributeInstances().get(0).getValue(), texto);
							attributeParagraph.add(attributeLabel);
							attributeParagraph.add(attributeValue);
							document.add(attributeParagraph);
						}
					}
				}

			}
		}

		if (levelInstance.isAggregate()) {
			Criteria criteria = this.dao.newCriteria(AggregateIndicator.class);
			criteria.add(Restrictions.eq("deleted", false));
			criteria.add(Restrictions.eq("indicator", levelInstance));
			List<AggregateIndicator> levelList = this.dao.findByCriteria(criteria, AggregateIndicator.class);

			if (!levelList.isEmpty()) {
				Paragraph aggParagraphLabel = new Paragraph();
				aggParagraphLabel.setSpacingBefore(paragraphSpacing);
				aggParagraphLabel.add(new Phrase("Indicadores", titulo));
				document.add(aggParagraphLabel);

				PdfPTable table = new PdfPTable(5);
				table.setSpacingBefore(paragraphSpacing / 2);
				table.setWidthPercentage(100);

				PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Responsável", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Início", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Fim", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Rendimento", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				for (AggregateIndicator son : levelList) {

					cell = new PdfPCell(new Phrase(son.getAggregate().getName(), texto));
					table.addCell(cell);

					List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son.getAggregate());
					User responsible = new User();
					Date beginDate = new Date();
					Date endDate = new Date();
					for (Attribute attribute : sonAttrList) {
						if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
							responsible = this.userBS
									.existsByUser(Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
						} else if (attribute.isBeginField()) {
							beginDate = attribute.getAttributeInstances().get(0).getValueAsDate();
						} else if (attribute.isEndField()) {
							endDate = attribute.getAttributeInstances().get(0).getValueAsDate();
						}
					}

					if (responsible != null)
						cell = new PdfPCell(new Phrase(responsible.getName(), texto));
					else
						cell = new PdfPCell(new Phrase("-", texto));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(brDateFormat.format(beginDate), texto));
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(brDateFormat.format(endDate), texto));
					table.addCell(cell);

					if (son.getAggregate().getLevelValue() != null)
						cell = new PdfPCell(new Phrase(
								decimalFormatDbl.format(Double.valueOf(son.getAggregate().getLevelValue())) + "%",
								texto));
					else
						cell = new PdfPCell(new Phrase("0,00%", texto));
					table.addCell(cell);
				}
				document.add(table);

			}

		}

		PaginatedList<StructureLevelInstance> sonsList = new PaginatedList<>();
		sonsList.setList(structureBS.retrieveLevelInstanceSons(levelInstance.getId()));
		levelInstance.setSons(sonsList);
		sonsList = levelInstance.getSons();
		Paragraph sonParagraphLabel = new Paragraph();
		// LOGGER.info(sonsList.getList().toString());
		if (!sonsList.getList().isEmpty()) {
			if (sonsList.getList().get(0).getLevel().isObjective()) {
				sonParagraphLabel.setSpacingBefore(paragraphSpacing);
				sonParagraphLabel.add(new Phrase("Objetivos", titulo));
				document.add(sonParagraphLabel);

				PdfPTable table = new PdfPTable(3);
				table.setSpacingBefore(paragraphSpacing / 2);
				table.setWidthPercentage(100);

				PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Responsável", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Rendimento", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				for (StructureLevelInstance son : sonsList.getList()) {
					cell = new PdfPCell(new Phrase(son.getName(), texto));
					table.addCell(cell);

					List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son);
					User responsible = new User();
					for (Attribute attribute : sonAttrList) {
						if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
							if (attribute.getAttributeInstances().get(0) != null) {
								responsible = this.userBS.existsByUser(
										Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
							} else {
								responsible = null;
							}
							break;
						}
					}
					if (responsible != null)
						cell = new PdfPCell(new Phrase(responsible.getName(), texto));
					else
						cell = new PdfPCell(new Phrase("-", texto));

					table.addCell(cell);

					if (son.getLevelValue() != null)
						cell = new PdfPCell(
								new Phrase(decimalFormatDbl.format(Double.valueOf(son.getLevelValue())) + "%", texto));
					else
						cell = new PdfPCell(new Phrase("0,00%", texto));
					table.addCell(cell);
				}
				document.add(table);
			} else if (sonsList.getList().get(0).getLevel().isIndicator()) {
				sonParagraphLabel.setSpacingBefore(paragraphSpacing);
				sonParagraphLabel.add(new Phrase("Indicadores", titulo));
				document.add(sonParagraphLabel);

				PdfPTable table = new PdfPTable(5);
				table.setSpacingBefore(paragraphSpacing / 2);
				table.setWidthPercentage(100);

				PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Responsável", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Início", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Fim", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Rendimento", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				for (StructureLevelInstance son : sonsList.getList()) {
					cell = new PdfPCell(new Phrase(son.getName(), texto));
					table.addCell(cell);

					List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son);
					User responsible = new User();
					Date beginDate = new Date();
					Date endDate = new Date();
					for (Attribute attribute : sonAttrList) {
						if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
							if (attribute.getAttributeInstances().get(0) != null) {
								responsible = this.userBS.existsByUser(
										Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
							} else {
								responsible = null;
							}
						} else if (attribute.isBeginField()) {
							if (attribute.getAttributeInstances().get(0) != null)
								beginDate = attribute.getAttributeInstances().get(0).getValueAsDate();
							else
								beginDate = null;
						} else if (attribute.isEndField()) {
							if (attribute.getAttributeInstances().get(0) != null)
								endDate = attribute.getAttributeInstances().get(0).getValueAsDate();
							else
								endDate = null;
						}
					}

					if (responsible != null)
						cell = new PdfPCell(new Phrase(responsible.getName(), texto));
					else
						cell = new PdfPCell(new Phrase("-", texto));
					table.addCell(cell);
					if (beginDate != null) {
						cell = new PdfPCell(new Phrase(brDateFormat.format(beginDate), texto));
					} else {
						cell = new PdfPCell(new Phrase("-", texto));
					}
					table.addCell(cell);

					if (endDate != null) {
						cell = new PdfPCell(new Phrase(brDateFormat.format(endDate), texto));
					} else {
						cell = new PdfPCell(new Phrase("-", texto));
					}
					table.addCell(cell);

					if (son.getLevelValue() != null)
						cell = new PdfPCell(
								new Phrase(decimalFormatDbl.format(Double.valueOf(son.getLevelValue())) + "%", texto));
					else
						cell = new PdfPCell(new Phrase("0,00%", texto));
					table.addCell(cell);

				}
				document.add(table);
			} else if (sonsList.getList().get(0).getLevel().isGoal()) {
				sonParagraphLabel.setSpacingBefore(paragraphSpacing);
				sonParagraphLabel.add(new Phrase("Metas", titulo));
				document.add(sonParagraphLabel);

				PdfPTable table = new PdfPTable(3);
				table.setSpacingBefore(paragraphSpacing / 2);
				table.setWidthPercentage(100);

				PdfPCell cell = new PdfPCell(new Phrase("Nome", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Responsável", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Desempenho", texto));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				for (StructureLevelInstance son : sonsList.getList()) {
					cell = new PdfPCell(new Phrase(son.getName(), texto));
					table.addCell(cell);

					List<Attribute> sonAttrList = this.structureBS.retrieveLevelSonsAttributes(son);
					User responsible = new User();
					for (Attribute attribute : sonAttrList) {
						if (attribute.getType().equals(ResponsibleField.class.getCanonicalName())) {
							if (attribute.getAttributeInstances().get(0) != null) {
								responsible = this.userBS.existsByUser(
										Long.parseLong(attribute.getAttributeInstances().get(0).getValue()));
							} else {
								responsible = null;
							}
							break;
						}
					}

					if (responsible != null)
						cell = new PdfPCell(new Phrase(responsible.getName(), texto));
					else
						cell = new PdfPCell(new Phrase("-", texto));

					table.addCell(cell);

					if (son.getLevelValue() != null)
						cell = new PdfPCell(
								new Phrase(decimalFormatDbl.format(Double.valueOf(son.getLevelValue())) + "%", texto));
					else
						cell = new PdfPCell(new Phrase("0,00%", texto));
					table.addCell(cell);
				}
				document.add(table);
			}
		}

		document.close();

		return in;
	}
	
	/**
	 * Realiza as funções necessárias para gerar o PDF
	 * @param src
	 * @param dest
	 * @param document
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void manipulatePdf(String src, String dest, com.itextpdf.text.Document document, int unnumbered)
			throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		int n = reader.getNumberOfPages();
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		PdfContentByte pagecontent;

		Font texto = FontFactory.getFont(FontFactory.TIMES, 10.0f);
		for (int i = 0; i < n;) {
			pagecontent = stamper.getOverContent(++i);
			if (i > unnumbered)
				ColumnText.showTextAligned(pagecontent, Element.ALIGN_RIGHT, new Phrase(String.format("%s", i), texto),
						// new Phrase(String.format("Página %s de %s", i, n),
						// texto),
						document.right(), document.bottom(), 0);
		}
		stamper.close();
		reader.close();
	}

}
