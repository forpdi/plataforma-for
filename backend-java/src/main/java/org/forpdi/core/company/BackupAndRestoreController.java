package org.forpdi.core.company;


import java.time.LocalDateTime;

import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.event.Current;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.upload.UploadSizeLimit;
import br.com.caelum.vraptor.observer.upload.UploadedFile;



@Controller
public class BackupAndRestoreController extends AbstractController  {


	@Inject private BackupAndRestoreHelper dbbackup;
	@Inject @Current private CompanyDomain domain;
	
	
	
	/**
	 * Backup das tabelas
	 * 
	 *@param id 
	 *		id plano macro
	 *
	 */
	@Get("/company/export")
	public Download export() {
		try {
			LOGGER.warn("Starting import Plan Macro");
			byte[] exportData = dbbackup.export(this.domain.getCompany());
			//this.success("sucess");
			LOGGER.warn("Stopping import Plan Macro");
			return new ByteArrayDownload(exportData, "application/octet-stream",
				String.format("plan-%d-%s.fbk", domain.getCompany().getId(), LocalDateTime.now().toString()));
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
			return null;
		}
	}
	
	
	/**
	 * Restaura tabelas a partir de um arquivo
	 *         
	 * @param file
	 * 		arquivo para restore
	 *  
	 * @param id
	 * 		id company
	 */
	@Post("/company/restore")
	@UploadSizeLimit(fileSizeLimit=5 * 1024 * 1024)
	public void  DoRestore(UploadedFile file) {
		try {
			dbbackup.restore(file);
			this.success("Dados importados com sucesso.");
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
}