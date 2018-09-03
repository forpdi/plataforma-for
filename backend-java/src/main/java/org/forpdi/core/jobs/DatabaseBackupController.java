package org.forpdi.core.jobs;


import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.upload.UploadSizeLimit;
import br.com.caelum.vraptor.observer.upload.UploadedFile;



@Controller
public class DatabaseBackupController extends AbstractController  {


	@Inject
	private DatabaseBackup dbbackup;
	
	
	
	/**
	 * Backup das tabelas
	 *          
	 */
	@Get("/structure/backup")
	public Download DoBackup() {
		try {
			Download dw = dbbackup.execute();
			//this.success("sucess");
			return dw;
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
			return null;
		}
	}
	
	
	/**
	 * Restaura tabelas a partir de um arquivo
	 *          
	 */
	@Post("/structure/restore")
	@UploadSizeLimit(fileSizeLimit=5 * 1024 * 1024)
	public void  DoRestore(UploadedFile file) {
		try {
			if (file==null) {
				this.fail("Nenhum arquivo selecionado");
				return;
			}
			dbbackup.restore(file);
			this.success("sucess");
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
}
