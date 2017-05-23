package org.forpdi.planning.document;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.planning.permissions.ManageDocumentPermission;
import org.forpdi.planning.plan.PlanBS;
import org.forpdi.planning.plan.PlanMacro;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

@Controller
public class DocumentController extends AbstractController {

	@Inject
	private DocumentBS bs;
	@Inject
	private PlanBS planBs;

	/**
	 * Retorna documento com suas seções para exibição no frontend.
	 * 
	 * @param planId
	 *            Id do plano macro.
	 * @return DocumentDTO Documento com suas seções para exibição no frontend
	 */
	@Get(BASEPATH + "/document/{planId}")
	@NoCache
	@Permissioned
	public void retrieveByPlan(Long planId) {
		try {
			PlanMacro plan = this.planBs.retrievePlanMacroById(planId);
			DocumentDTO dto = new DocumentDTO();
			Document document = this.bs.retrieveByPlan(plan);
			dto.setDocument(document);
			dto.setSections(this.bs.listSectionDTOsByDocument(document, null));
			this.success(dto);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

	/**
	 * Lista atributos de uma seção.
	 * 
	 * @param sectionId
	 *            Id da seção onde estão os atributos.
	 * @param planId
	 *            Id do plano de metas.
	 * @return DocumentSection Seção do documento com seus atributos.
	 */
	@Get(BASEPATH + "/document/sectionattributes")
	@NoCache
	@Permissioned
	public void getSectionAttributes(Long sectionId, Long planId) {
		try {
			DocumentSection documentSection = this.bs.retrieveSectionById(sectionId);
			if (documentSection == null) {
				this.fail("Seção inexistente!");
			} else {
				List<DocumentAttribute> list = this.bs.listAttributesBySection(documentSection, planId);
				documentSection.setDocumentAttributes(list);
				this.success(documentSection);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

	/**
	 * Salva atributos de uma seção do documento.
	 * 
	 * @param documentSection
	 *            Seção do documento com seus atributos.
	 * @return DocumentSection Seção do documento com seus atributos.
	 */
	@Post(BASEPATH + "/document/sectionattributes")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageDocumentPermission.class })
	public void saveSectionAttributes(DocumentSection documentSection) {
		try {
			DocumentSection section = this.bs.retrieveSectionById(documentSection.getId());
			if (section != null && !documentSection.getName().isEmpty()) {
				section.setName(documentSection.getName());
				section.setDocumentAttributes(this.bs.saveAttributes(documentSection.getDocumentAttributes()));
				this.bs.persist(section);
			}
			this.success(documentSection);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

	/**
	 * Cria seção no documento
	 * 
	 * @param documentId
	 *            Id documento no qual se deseja inserir a seção.
	 * @param name
	 *            O nome da seção a ser adicionada.
	 * @param parentId
	 *            O id da seção pai, caso haja. Caso contrário deve-se passar
	 *            null.
	 * @return DocumentSectionDTO A seção do documento para exibição no
	 *         frontend.
	 */
	@Post(BASEPATH + "/document/{documentId}/section")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageDocumentPermission.class })
	public void createSection(Long documentId, String name, Long parentId) {
		try {
			Document doc = this.bs.exists(documentId, Document.class);
			DocumentSection sec = this.bs.createSection(doc, name, parentId);
			this.success(new DocumentSectionDTO(sec, this.bs));
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Deletar seção do documento;
	 * 
	 * @param sectionId
	 *            Id da seção a ser excluída.
	 * @return DocumentSection Seção do documento que foi excluída.
	 */
	@Post(BASEPATH + "/document/section/delete")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageDocumentPermission.class })
	public void deleteSection(Long sectionId) {
		try {
			DocumentSection section = this.bs.retrieveSectionById(sectionId);
			section.setDeleted(true);
			this.bs.persist(section);
			if (section.getParent() != null) {
				DocumentSection parent = this.bs.retrieveSectionById(section.getParent().getId());
				if (this.bs.listSectionChild(parent).size() <= 0) {
					parent.setLeaf(true);
					this.bs.persist(parent);
				}
			}
			this.success(section);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

	/**
	 * Cria um atributo em uma seção do documento.
	 * 
	 * @param sectionId
	 *            Id da seção na qual se deseja adicionar o atributo.
	 * @param name
	 *            O nome do atributo.
	 * @param type
	 *            O tipo do atributo.
	 * @param periodicity
	 *            A periodicidade do atributo, se houver.
	 * @return DocumentAttribute O atributo criado.
	 */
	@Post(BASEPATH + "/document/section/{sectionId}/attribute")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageDocumentPermission.class })
	public void createAttribute(Long sectionId, String name, String type, Boolean periodicity) {
		try {
			DocumentSection sec = this.bs.exists(sectionId, DocumentSection.class);
			DocumentAttribute attr = this.bs.createAttribute(sec, name, type, periodicity);
			this.success(attr);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

	/**
	 * Lista seções do documento diferenciando se está preenchido ou não pelo
	 * atributo filled.
	 * 
	 * @param planId
	 *            Id do plano macro referente ao documento.
	 * @return PaginatedList<DocumentSection> Lista das seções com o atributo
	 *         filled preenchido.
	 */
	@Get(BASEPATH + "/document/{planId}/filledsections")
	@NoCache
	@Permissioned
	public void retrieveFilledSectionsByPlan(Long planId) {
		try {
			PlanMacro planMacro = this.planBs.retrievePlanMacroById(planId);
			Document document = this.bs.retrieveDocumentByPlan(planMacro);
			PaginatedList<DocumentSection> sections = this.bs.listRootSectionsByDocument(document);
			this.bs.setSectionsFilled(sections.getList(), planId);
			this.success(sections);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
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
	 * @return OutputStream Arquivo PDF.
	 */
	@Get(BASEPATH + "/document/exportdocument")
	@Consumes
	@NoCache
	@Permissioned
	public void exportDocument(String author, String title, String lista) {
		try {
			InputStream in = this.bs.exportDocument(author, title, lista);
			OutputStream out;

			this.response.reset();
			this.response.setHeader("Content-Type", "application/pdf");
			this.response.setHeader("Content-Disposition", "inline; filename=\"" + title + ".pdf\"");
			out = this.response.getOutputStream();
			IOUtils.copy(in, out);
			// out.flush();
			out.close();
			in.close();
			this.result.nothing();

		} catch (

		Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
		// return null;

	}

	/**
	 * Exportar para pdf os atriubos de um level
	 * @param levelId
	 * 			Id o level
	 */
	@Get(BASEPATH + "/document/exportLevelAttributes")
	@Consumes
	@NoCache
	@Permissioned
	public void exportLevelAttributes(Long levelId) {
		try {
			InputStream in = this.bs.exportLevelAttributes(levelId);
			OutputStream out;

			this.response.reset();
			this.response.setHeader("Content-Type", "application/pdf");
			this.response.setHeader("Content-Disposition", "inline; filename=\"exportLevel" + levelId + ".pdf\"");
			out = this.response.getOutputStream();
			IOUtils.copy(in, out);
			// out.flush();
			out.close();
			in.close();
			this.result.nothing();

		} catch (

		Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Deletar um atributo da seção
	 * 
	 * @param id
	 *            Id do atributo a ser excluído.
	 * @return DocumentAttribute Atributo excluído.
	 */
	@Post(BASEPATH + "/document/sectionattribute/delete")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageDocumentPermission.class })
	public void deleteSectionAttribute(Long id) {
		try {
			DocumentAttribute attr = this.bs.retrieveDocumentAttribute(id);
			attr.setDeleted(true);
			this.bs.persist(attr);
			this.success(attr);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

	/**
	 * Editar atributo da seção.
	 * 
	 * @param id
	 *            Id do atributo do documento a ser editado.
	 * @param name
	 *            Nome do atributo.
	 * @return DocumentAttribute Atributo editado.
	 */
	@Post(BASEPATH + "/document/sectionattribute/edit")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageDocumentPermission.class })
	public void editSectionAttribute(Long id, String name) {
		try {
			DocumentAttribute exist = this.bs.retrieveDocumentAttribute(id);
			if (exist == null) {
				this.fail("Atributo não existente");
				return;
			}
			exist.setName(name);
			this.bs.persist(exist);
			this.success(exist);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}

	}

}
