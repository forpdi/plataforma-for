package org.forpdi.planning.structure;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;
import br.com.caelum.vraptor.observer.upload.UploadedFile;

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class StructureController extends AbstractController {
	@Inject private StructureBS bs;

	@Post(BASEPATH+"/structure/import")
	@NoCache
	@Permissioned(AccessLevels.COMPANY_ADMIN)
	public void importStructure(UploadedFile file) {
		try {
			if (file == null) {
				this.fail("Nenhum arquivo enviado.");
			} else {
				File temp = File.createTempFile("fpdi", "readedStructure-import");
				FileOutputStream copyStream = new FileOutputStream(temp);
				GeneralUtils.streamingPipe(file.getFile(), copyStream);
				copyStream.close();
				Structure structure = this.bs.importStructure(temp);
				temp.delete();
				this.success(structure);
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Post(BASEPATH+"/structure")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.COMPANY_ADMIN)
	public void saveStructure(@NotNull @Valid Structure structure) {
		try {
			structure.setId(null);
			this.bs.persist(structure);
			this.success(structure);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Put(BASEPATH+"/structure")
	@Consumes
	@NoCache
	@Permissioned(AccessLevels.COMPANY_ADMIN)
	public void updateStructure(@NotNull @Valid Structure structure) {
		try {
			Structure existent = this.bs.exists(structure.getId(), Structure.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDescription(structure.getDescription());
			existent.setName(structure.getName());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Get(BASEPATH+"/structure/{id}")
	@NoCache
	@Permissioned
	public void retrieveStructure(Long id){
		try {
			Structure structure = this.bs.exists(id, Structure.class);
			if (structure == null) {
				this.fail("A empresa solicitada não foi encontrada.");
			} else {
				structure.setLevels(this.bs.listLevels(structure).getList());
				for (StructureLevel level : structure.getLevels()) {
					level.setAttributes(this.bs.listAttributes(level).getList());
				}
				this.success(structure);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}
	
	@Get(BASEPATH+"/structure")
	@NoCache
	@Permissioned
	public void listStructures(Integer page){
		if (page == null)
			page = 0;
		try {
			PaginatedList<Structure> structures = this.bs.list(page);
			this.success(structures);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}

	@Delete(BASEPATH+"/structure/{id}")
	@NoCache
	@Permissioned(AccessLevels.COMPANY_ADMIN)
	public void delete(@NotNull Long id) {
		try {
			Structure existent = this.bs.exists(id, Structure.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}
			existent.setDeleted(true);
			this.bs.persist(existent);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

}
