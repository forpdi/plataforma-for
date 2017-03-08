package org.forpdi.planning.document.bootstrap;

import org.forpdi.planning.attribute.types.NumberField;
import org.forpdi.planning.attribute.types.ScheduleField;
import org.forpdi.planning.attribute.types.SelectField;
import org.forpdi.planning.attribute.types.SelectPlan;
import org.forpdi.planning.attribute.types.StrategicObjective;
import org.forpdi.planning.attribute.types.TableField;
import org.forpdi.planning.attribute.types.TextArea;
import org.forpdi.planning.attribute.types.TextField;
import org.forpdi.planning.attribute.types.TotalField;
import org.forpdi.planning.document.Document;
import org.forpdi.planning.document.DocumentAttribute;
import org.forpdi.planning.document.DocumentSection;
import org.forpdi.planning.fields.OptionsField;
import org.forpdi.planning.fields.schedule.Schedule;
import org.forpdi.planning.fields.table.TableFields;
import org.forpdi.planning.fields.table.TableStructure;
import org.forpdi.planning.plan.PlanMacro;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.HibernateDAO.TransactionalOperation;

/**
 * Helper que cria o documento estipulado pelo documento de referência do FORPLAD.
 * Ele cria o documento dentro de um plano macro.
 * 
 * @see https://docs.google.com/document/d/1IJjCoGLbR4bsVM_o-tlyf_5H1efAF24WcaIvoaEouq0/edit#heading=h.fr3x6z3vdjm
 * @author Renato Oliveira
 *
 */
public class ForpladHelper {
	
	private final HibernateDAO dao;
	
	public ForpladHelper(HibernateDAO dao) {
		this.dao = dao;
	}
	
	public Document initializeDocument(PlanMacro plan) {
		if (plan == null) {
			throw new IllegalArgumentException("You must provide a PlanMacro to initialize a Forplad Document.");
		}
		
		Document document = this.retrieveByPlan(plan);
		if (document != null) {
			throw new IllegalArgumentException("The provided PlanMacro already has a Document.");
		}
		
		DocumentCreator creator = new DocumentCreator(plan);
		this.dao.execute(creator);
		
		return creator.document;
	}
	
	public Document retrieveByPlan(PlanMacro plan) {
		Criteria criteria =
			this.dao.newCriteria(Document.class)
			.add(Restrictions.eq("plan", plan))
		;
		return (Document) criteria.uniqueResult();
	}
	
	/**
	 * Classe privada que insere a estrutura do documento
	 * FORPLAD em uma única transação do banco de dados.
	 * 
	 * @author Renato Oliveira
	 *
	 */
	protected class DocumentCreator implements TransactionalOperation {

		public final Document document;
		
		public DocumentCreator(PlanMacro plan) {
			this.document = new Document();
			this.document.setPlan(plan);
			this.document.setDescription("O PDI, elaborado para um período de cinco anos, é o documento"
					+ " que identifica a IES no que diz respeito à sua filosofia de trabalho, à missão a que se propõe, "
					+ "às diretrizes pedagógicas que orientam suas ações, à sua estrutura organizacional "
					+ "e às atividades acadêmicas que desenvolve e/ou que pretende desenvolver");
			this.document.setTitle("Documento - " + plan.getName());
		}
		
		@Override
		public void execute(Session session) throws HibernateException {
			DocumentSection section, subsection;
			DocumentAttribute attr;
			Schedule schedule;
			TableFields tableFields;
			TableStructure tableStructure;
			OptionsField optionsField;
			
			session.persist(this.document);
			
			// Seção 1 - Apresentação
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Apresentação");
			section.setSequence(1); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			attr = new DocumentAttribute();
			attr.setName("Apresentação do PDI");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			
			// Seção 2 - Método
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Método");
			section.setSequence(2); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição do método utilizado para a elaboração do PDI");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			
			// Seção 3 - Documentos de referência
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Documentos de referência");
			section.setSequence(3); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			attr = new DocumentAttribute();
			attr.setName("Documentos de referência mais utilizados para a elaboração do PDI");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			
			// Seção 4 - Resultados do PDI anterior
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Resultados do PDI anterior");
			section.setSequence(4); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			attr = new DocumentAttribute();
			attr.setName("Resultados do PDI anterior");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			
			// Seção 5 - Perfil institucional
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Perfil institucional");
			section.setSequence(5); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 5.1 - Histórico da IES
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Histórico da IES");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Análise do histórico de todos os campus da IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 5.2 - Finalidade
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Finalidade");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Finalidade da IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 5.3 - Missão
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Missão, visão e valores");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Missão");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			attr = new DocumentAttribute();
			attr.setName("Visão");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			attr = new DocumentAttribute();
			attr.setName("Valores");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(3); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Objetivos estratégicos");
			attr.setSection(subsection);
			attr.setType(StrategicObjective.generateId(StrategicObjective.class));
			attr.setSequence(4); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			

			// Subseção 5.4 - Área(s) de atuação acadêmica
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Área(s) de atuação acadêmica");
			subsection.setSequence(4); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Áreas de atuação acadêmica");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			
			// Seção 6 - Projeto Pedagógico Institucional (PPI)
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Projeto Pedagógico Institucional (PPI)");
			section.setSequence(6); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 6.1 - Inserção regional
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Inserção regional");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.2 - Princípios filosóficos e técnico-metodológicos gerais
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Princípios filosóficos e técnico-metodológicos gerais");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição dos princípios que norteiam as práticas acadêmicas da IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.3 - Organização didático-pedagógica da instituição
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Organização didático-pedagógica da instituição");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Organização didático-pedagógica da instituição");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.4 - Políticas de ensino
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Políticas de ensino");
			subsection.setSequence(4); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição das políticas de ensino");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.5 - Políticas de extensão
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Políticas de extensão");
			subsection.setSequence(5); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição das políticas de extensão");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.6 - Políticas de pesquisa
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Políticas de pesquisa");
			subsection.setSequence(6); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição das políticas de pesquisa adotadas pela IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.7 - Políticas de gestão
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Políticas de gestão");
			subsection.setSequence(7); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição das políticas de gestão adotadas pela IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 6.8 - Responsabilidade Social da IES
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Responsabilidade social da IES");
			subsection.setSequence(8); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição da contribuição à inclusão social e ao desenvolvimento econômico e social da região");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 7 - Cronograma de implantação e desenvolvimento da instituição e dos cursos
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Cronograma de implantação e desenvolvimento da instituição e dos cursos");
			section.setSequence(7); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 7.1 - Descrição da situação atual
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Descrição da situação atual");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição da situação atual");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Cursos ofertados pela IES");
			attr.setSection(subsection);
			attr.setType(TableField.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			tableFields = new TableFields();
			tableFields.setAttributeId(attr.getId());
			tableFields.setIsDocument(true);
			session.persist(tableFields);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Nome do Curso");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Habilitação");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			//Valores da habilitação
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Tecnólogo");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Bacharelado");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Licenciatura");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Especialização");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("MBA");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Mestrado");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Doutorado");
			session.persist(optionsField);
			// Novo campo da tabela
			tableStructure = new TableStructure();
			tableStructure.setLabel("Modalidade");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			//Valores da habilitação
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Presencial");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("À distância");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Semipresencial");
			session.persist(optionsField);
			// Novo campo da tabela
			tableStructure = new TableStructure();
			tableStructure.setLabel("Regime de matrícula");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			//Valores da habilitação
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Anual");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Semestral");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Bienal");
			session.persist(optionsField);
			//Fim valores do selectfield
			tableStructure = new TableStructure();
			tableStructure.setLabel("Nº de turmas");
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Nº de Alunos por turma");
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Turno de funcionamento");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			// Valores do select box
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Integral");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Diurno");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Noturno");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Vespertino");
			session.persist(optionsField);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Situação Atual");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			
			attr = new DocumentAttribute();
			attr.setName("Cursos que serão expandidos ou abertos no futuro");
			attr.setSection(subsection);
			attr.setType(TableField.class.getCanonicalName());
			attr.setSequence(3); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			tableFields = new TableFields();
			tableFields.setAttributeId(attr.getId());
			tableFields.setIsDocument(true);
			session.persist(tableFields);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Nome do Curso");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Habilitação");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			//Valores da habilitação
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Tecnólogo");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Bacharelado");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Licenciatura");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Especialização");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("MBA");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Mestrado");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Doutorado");
			session.persist(optionsField);
			// Novo campo da tabela
			tableStructure = new TableStructure();
			tableStructure.setLabel("Modalidade");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			//Valores da habilitação
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Presencial");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("À distância");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Semipresencial");
			session.persist(optionsField);
			// Novo campo da tabela
			tableStructure = new TableStructure();
			tableStructure.setLabel("Regime de matrícula");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			//Valores da habilitação
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Anual");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Semestral");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Bienal");
			session.persist(optionsField);
			/*tableStructure = new TableStructure();
			tableStructure.setLabel("Habilitação");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Modalidade");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Regime de matrícula");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);*/
			tableStructure.setLabel("Turno de funcionamento");
			tableStructure.setType(SelectField.class.getCanonicalName());
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			// Valores do select box
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Integral");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Diurno");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Noturno");
			session.persist(optionsField);
			optionsField = new OptionsField();
			optionsField.setAttributeId(attr.getId());
			optionsField.setColumnId(tableStructure.getId());
			optionsField.setDocument(true);
			optionsField.setLabel("Vespertino");
			session.persist(optionsField);
			/*tableStructure = new TableStructure();
			tableStructure.setLabel("Turno de funcionamento");
			tableStructure.setType(TextField.generateId(TextField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);*/
			tableStructure = new TableStructure();
			tableStructure.setLabel("Nº de vagas autorizadas");
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Nº de vagas a solicitar");
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Ano previsto para solicitação");
			tableStructure.setType(TextField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			
			attr = new DocumentAttribute();
			attr.setName("Cronograma de expansão");
			attr.setSection(subsection);
			attr.setType(ScheduleField.class.getCanonicalName());
			attr.setSequence(4); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			schedule = new Schedule();
			schedule.setPeriodicityEnable(false);
			schedule.setAttributeId(attr.getId());
			schedule.setIsDocument(true);
			session.persist(schedule);

			// Subseção 7.2 - Objetivos e metas
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Objetivos e metas");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição do plano de implantação e desenvolvimento da instituição e dos cursos");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Selecione o plano de metas correspondente");
			attr.setSection(subsection);
			attr.setType(SelectPlan.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 8 - Perfil do corpo docente
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Perfil do corpo docente");
			section.setSequence(8); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 8.1 - Composição
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Composição");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Evolução no quadro permanente de docentes por classe/ano");
			attr.setSection(subsection);
			attr.setType(TableField.class.getCanonicalName());
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			tableFields = new TableFields();
			tableFields.setAttributeId(attr.getId());
			tableFields.setIsDocument(true);
			session.persist(tableFields);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Ano");
			tableStructure.setInTotal(false);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Auxiliar");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Assistente");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Adjunto");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Associado");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Titular");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Total");
			tableStructure.setType(TotalField.generateId(TotalField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			
			attr = new DocumentAttribute();
			attr.setName("Evolução no quadro permanente de docentes por titulação");
			attr.setSection(subsection);
			attr.setType(TableField.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			tableFields = new TableFields();
			tableFields.setAttributeId(attr.getId());
			tableFields.setIsDocument(true);
			session.persist(tableFields);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Ano");
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Graduação");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Especialização");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Mestrado");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Doutorado");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Pós-doutorado");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Total");
			tableStructure.setType(TotalField.generateId(TotalField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			
			attr = new DocumentAttribute();
			attr.setName("Evolução no quadro permanente de docentes por regime de trabalho");
			attr.setSection(subsection);
			attr.setType(TableField.class.getCanonicalName());
			attr.setSequence(3); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			tableFields = new TableFields();
			tableFields.setAttributeId(attr.getId());
			tableFields.setIsDocument(true);
			session.persist(tableFields);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Ano");
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Dedicação exclusiva");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("40h");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("20h");
			tableStructure.setInTotal(true);
			tableStructure.setType(NumberField.generateId(NumberField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);
			tableStructure = new TableStructure();
			tableStructure.setLabel("Total");
			tableStructure.setType(TotalField.generateId(TotalField.class));
			tableStructure.setTableFields(tableFields);
			session.persist(tableStructure);

			// Subseção 8.2 - Plano de carreira
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Plano de carreira");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Plano de carreira");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 8.3 - Critérios de seleção e contratação
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Critérios de seleção e contratação");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Critérios de seleção e contratação");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 8.4 - Procedimentos para substituição (definitiva e eventual) dos professores do quadro
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Procedimentos para substituição (definitiva e eventual) dos professores do quadro");
			subsection.setSequence(4); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Procedimentos adotados pela IES, visando recomposição e substituição de professores do quadro");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 8.5 - Cronograma
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Cronograma e plano de expansão do corpo docente");
			subsection.setSequence(5); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Cronograma e plano de expansão do corpo docente");
			attr.setSection(subsection);
			attr.setType(ScheduleField.class.getCanonicalName());
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			schedule = new Schedule();
			schedule.setPeriodicityEnable(false);
			schedule.setAttributeId(attr.getId());
			schedule.setIsDocument(true);
			session.persist(schedule);
			
			// Subseção 8.6 - Objetivos e metas
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Objetivos e metas");
			subsection.setSequence(6); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição do plano de expansão do corpo docente");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Selecione o plano de metas correspondente");
			attr.setSection(subsection);
			attr.setType(SelectPlan.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 9 - Perfil do corpo técnico administrativo
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Perfil do corpo técnico administrativo");
			section.setSequence(9); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 9.1 - Composição
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Composição");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Composição");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 9.2 - Plano de carreira
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Plano de carreira");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Plano de carreira");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 9.3 - Critérios de seleção e contratação
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Critérios de seleção e contratação");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Apresentar os critérios de seleção e contratação de técnicos utilizados pela IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 9.4 - Procedimentos para substituição (definitiva e eventual) dos professores do quadro
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Procedimentos para substituição (definitiva e eventual) dos técnicos do quadro");
			subsection.setSequence(4); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição dos procedimentos adotados pela IES, visando recomposição e substituição de técnicos do quadro");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 9.5 - Cronograma e plano de expansão do corpo técnico administrativo
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Cronograma e plano de expansão do corpo técnico administrativo");
			subsection.setSequence(5); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Cronograma");
			attr.setSection(subsection);
			attr.setType(ScheduleField.class.getCanonicalName());
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			schedule = new Schedule();
			schedule.setPeriodicityEnable(false);
			schedule.setAttributeId(attr.getId());
			schedule.setIsDocument(true);
			session.persist(schedule);
			
			// Subseção 9.6 - Objetivos e metas
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Objetivos e metas");
			subsection.setSequence(6); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição do plano de expansão do corpo técnico administrativo");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Selecione o plano de metas correspondente");
			attr.setSection(subsection);
			attr.setType(SelectPlan.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 10 - Organização administrativa da IES
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Organização administrativa da IES");
			section.setSequence(10); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 10.1 - Estrutura Organizacional, Instâncias de Decisão e Organograma Institucional e Acadêmico
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Estrutura organizacional, instâncias de decisão e organograma institucional e acadêmico");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrever a estrutura organizacional da IES");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 10.2 - Órgãos Colegiados
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Órgãos Colegiados");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrever as competências e composição de cada órgão colegiado");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 10.3 - Órgãos de apoio às atividades acadêmicas
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Órgãos de apoio às atividades acadêmicas");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrever as competências e composição de cada órgão de apoio");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 11 - Políticas de atendimento aos discentes
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Políticas de atendimento aos discentes");
			section.setSequence(11); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 11.1 - Programas de apoio pedagógico e financeiro (bolsas)
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Programas de apoio pedagógico e financeiro (bolsas)");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição de todos os programas de apoio pedagógico e financeiro");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 11.2 - Estímulos à permanência
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Estímulos à permanência");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Estímulos à permanência");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 11.3 - Organização estudantil
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Organização estudantil");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição dos espaços para participação e convivência estudantil, bem como dos órgãos de representatividade discente");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 11.4 - Acompanhamento dos egressos
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Acompanhamento dos egressos");
			subsection.setSequence(4); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição das formas de relação com os egressos");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 11.5 - Objetivos e metas
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Objetivos e metas");
			subsection.setSequence(5); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição do plano de atendimento aos discentes");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Selecione o plano de metas correspondente");
			attr.setSection(subsection);
			attr.setType(SelectPlan.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Seção 12 - Infraestrutura
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Infraestrutura");
			section.setSequence(12); // Número da seção para ordenar na exibição.
			section.setLeaf(false);
			session.persist(section);
			
			// Subseção 12.1 - Infraestrutura física
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Infraestrutura física");
			subsection.setSequence(1); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Detalhar infraestrutura física");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);

			// Subseção 12.2 - Biblioteca
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Biblioteca");
			subsection.setSequence(2); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Informações sobre a biblioteca");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Cronograma de expansão do acervo");
			attr.setSection(subsection);
			attr.setType(ScheduleField.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			schedule = new Schedule();
			schedule.setPeriodicityEnable(false);
			schedule.setAttributeId(attr.getId());
			schedule.setIsDocument(true);
			session.persist(schedule);
			
			// Subseção 12.3 - Laboratórios
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Laboratórios");
			subsection.setSequence(3); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Informações sobre os laboratórios");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 12.4 - Recursos tecnológicos e de áudio visual
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Recursos tecnológicos e de áudio visual");
			subsection.setSequence(4); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrever os recursos presentes na IES, e o planejamento de melhorias nessa área");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 12.5 - Plano de promoção de acessibilidade e de atendimento diferenciado a portadores de necessidades especiais
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Plano de promoção de acessibilidade e de atendimento diferenciado a portadores de necessidades especiais");
			subsection.setSequence(5); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Ações planejadas para promover acessibilidade aos portadores de necessidades especiais");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Subseção 12.6 - Cronograma de expansão da infra-estrutura para o período de vigência do PDI
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Cronograma de expansão da infraestrutura para o período de vigência do PDI");
			subsection.setSequence(6); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Cronograma de implementação das ações voltadas para a infraestrutura");
			attr.setSection(subsection);
			attr.setType(ScheduleField.class.getCanonicalName());
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			schedule = new Schedule();
			schedule.setPeriodicityEnable(false);
			schedule.setAttributeId(attr.getId());
			schedule.setIsDocument(true);
			session.persist(schedule);
			
			// Subseção 12.7 - Objetivos e metas
			subsection = new DocumentSection();
			subsection.setDocument(document);
			subsection.setName("Objetivos e metas");
			subsection.setSequence(7); // Número da subseção para ordenar na exibição.
			subsection.setParent(section);
			subsection.setLeaf(true);
			session.persist(subsection);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição do plano de expansão da infraestrutura");
			attr.setSection(subsection);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Selecione o plano de metas correspondente");
			attr.setSection(subsection);
			attr.setType(SelectPlan.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 13 - Avaliação e acompanhamento do desenvolvimento institucional
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Avaliação e acompanhamento do desenvolvimento institucional");
			section.setSequence(13); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			
			attr = new DocumentAttribute();
			attr.setName("Descrever os procedimentos utilizados para a realização da autoavaliação da IES");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 14 - Gestão financeira e orçamentária
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Gestão financeira e orçamentária");
			section.setSequence(14); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			
			attr = new DocumentAttribute();
			attr.setName("Apresentar ações relacionadas à melhoria da gestão financeira da IES, bem como demonstrar a sustentabilidade financeira");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 15 - Processo de monitoramento, controle e revisão do PDI
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Processo de monitoramento, controle e revisão do PDI");
			section.setSequence(15); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
						
			attr = new DocumentAttribute();
			attr.setName("Apresentar o planejamento das atividades de controle que ocorrerão durante a vigência do PDI");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			attr = new DocumentAttribute();
			attr.setName("Cronograma de execução");
			attr.setSection(section);
			attr.setType(ScheduleField.class.getCanonicalName());
			attr.setSequence(2); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			schedule = new Schedule();
			schedule.setPeriodicityEnable(false);
			schedule.setAttributeId(attr.getId());
			schedule.setIsDocument(true);
			session.persist(schedule);
			
			// Seção 16 - Plano para gestão de riscos
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Plano para gestão de riscos");
			section.setSequence(16); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
						
			attr = new DocumentAttribute();
			attr.setName("Análise de riscos e o plano em caso de ocorrência");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 17 - Conclusão
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Conclusão");
			section.setSequence(17); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			
			attr = new DocumentAttribute();
			attr.setName("Conclusão do documento de PDI");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 18 - Anexos
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Anexos");
			section.setSequence(18); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			
			attr = new DocumentAttribute();
			attr.setName("Descrição dos anexos");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
			
			// Seção 19 - Apêndice
			section = new DocumentSection();
			section.setDocument(document);
			section.setName("Apêndice");
			section.setSequence(19); // Número da seção para ordenar na exibição.
			section.setLeaf(true);
			session.persist(section);
			
			attr = new DocumentAttribute();
			attr.setName("Descrição dos apêndices");
			attr.setSection(section);
			attr.setType(TextArea.generateId(TextArea.class));
			attr.setSequence(1); // Ordem do atributo dentro da seção
			attr.setRequired(false);
			session.persist(attr);
						
		}
		
	}
}
