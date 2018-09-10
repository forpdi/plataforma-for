package org.forpdi.planning.plan;


import java.time.LocalDateTime;

import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.upload.UploadSizeLimit;
import br.com.caelum.vraptor.observer.upload.UploadedFile;



@Controller
public class BackupAndRestoreController extends AbstractController  {


	@Inject
	private BackupAndRestoreHelper dbbackup;
	
	
	
	/**
	 * Backup das tabelas
	 *          
	 */
	@Get("/plan/{id}/export")
	public Download export(Long id) {
		try {
			byte[] exportData = dbbackup.export(id);
			//this.success("sucess");
			return new ByteArrayDownload(exportData, "application/octet-stream",
				String.format("plan-%d-%s.fbk", id, LocalDateTime.now().toString()));
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
	@Post("/plan/restore")
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
