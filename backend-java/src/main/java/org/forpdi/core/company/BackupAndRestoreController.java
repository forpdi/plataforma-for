package org.forpdi.core.company;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.forpdi.core.event.Current;
import org.forpdi.core.properties.SystemConfigs;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.core.user.authz.permission.ExportDataPermission;
import org.forpdi.core.user.authz.permission.RestoreDataPermission;
import org.forpdi.system.Archive;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.AbstractController;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.observer.upload.UploadSizeLimit;
import br.com.caelum.vraptor.observer.upload.UploadedFile;

@Controller
public class BackupAndRestoreController extends AbstractController  {

	@Inject private BackupAndRestoreHelper dbbackup;
	@Inject @Current private CompanyDomain domain;
	
	private static UploadedFile file = null;
	
	/**
	 * Backup das tabelas
	 * 
	 *@param id 
	 *		id plano macro
	 *
	 */
	@Get("/company/export")
	@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {ExportDataPermission.class})
	public void export() {
		try {
			LOGGER.infof("Starting export company '%s'...", this.domain.getCompany().getName());
			this.response.setHeader("Content-Disposition", String.format("attachment; filename=plans-%d-%s.fbk",
					domain.getCompany().getId(), LocalDateTime.now().toString()));
			this.response.setContentType("application/octet-stream");
		
			this.dbbackup.export(this.domain.getCompany(), this.response.getOutputStream());
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			this.result.nothing();
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
	@Post("/company/fbkupload")
	@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {RestoreDataPermission.class})
	@UploadSizeLimit(fileSizeLimit = 100 * 1024 * 1024, sizeLimit = 100 * 1024 * 1024)
	public void  fbkupload(UploadedFile file) {
		if (file == null) {
			this.fail("upload falhou");
		}else if (BackupAndRestoreController.file != null) {
			this.fail("processo de importação já em andamento");
		} else { 
			try {
				BackupAndRestoreController.file=file;
				this.success("upload completo.");
			} catch (IllegalArgumentException ex) {
				this.fail(ex.getMessage());
			} catch (Throwable ex) {
				LOGGER.error("IO error", ex);
				this.fail("Erro inesperado: " + ex.getMessage());
			}
		}
	}
	

	/**
	 * Restaura tabelas a partir de um arquivo
	 *         
	 */
	@Post("api/company/restore")
	@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {RestoreDataPermission.class})
	public void  restore() {
		if (BackupAndRestoreController.file == null) {
			this.fail("arquivo não especificado");
			return;
		}
		
		if(this.domain == null || this.domain.getCompany() == null) {
			BackupAndRestoreController.file = null;
			this.fail("Instituição não definida");
			return;
		}
		
		
		try {		
			LOGGER.infof("Starting restoration for company '%s'...", this.domain.getCompany().getName());
			dbbackup.restore(BackupAndRestoreController.file);
			LOGGER.infof("Done restoration for company '%s'.", this.domain.getCompany().getName());
			
			BackupAndRestoreController.file = null;
			
			this.success("Dados importados com sucesso.");
		} catch (Throwable ex) {
			BackupAndRestoreController.file = null;
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Estado do upload atual
	 * 
	 */
	@Get("api/company/state")
	//@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {RestoreDataPermission.class})
	public void  state() {
		try {
			String  porcent =String.valueOf(dbbackup.getPorcentagem());
			
			if (dbbackup.getPorcentagem()==100) {
				dbbackup.resetQuantity();
			}
			this.success(porcent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			//this.fail("Erro inesperado: " + ex.getMessage());
			this.success("-1");
		}
	}
	
	
	/**
	 * Upload de arquivo local.
	 * 
	 * @param void.
	 * @return String Url do novo link do arquivo.
	 */
	@Post("api/file/uploadlocal")
	@NoCache
	@UploadSizeLimit(fileSizeLimit=5 * 1024 * 1024)
	public void  uploadFile(UploadedFile file) {
		try {
			if (file == null){
				this.fail("upload falhou");
				return;
			}

			Archive archive =this.dbbackup.listFilebyName(file.getFileName());
			if(archive != null){
				this.success(archive);
				return;
			}

			archive = new Archive();
			archive.setName(file.getFileName());
			File targetFile = new File(SystemConfigs.getConfig("store.files")+archive.getName());
			FileUtils.copyInputStreamToFile(file.getFile(), targetFile);

			this.dbbackup.persist(archive);
			this.success(archive);

		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			 this.fail(ex.getMessage());
		}
	}

	/**
	 * Leitura de arquivo local.
	 * 
	 * @param Id do arquivo.
	 * 
	 */
	@Get("api/file/{id}")
	@NoCache
	public void readFile(@NotNull Long id) {
		
		Archive file =this.dbbackup.exists(id, Archive.class);
		
		if(file ==null){
			this.fail("(No such file or directory)");
			return;
		}
		LOGGER.error("arquivo: "+SystemConfigs.getConfig("store.files")+file.getName());
		
		try{
			File initialFile = new File(SystemConfigs.getConfig("store.files")+file.getName());
			
			byte[] bytes = new byte[(int)initialFile.length()];
			
			if(initialFile.exists()){
				FileInputStream fis = new FileInputStream(initialFile);
				fis.read(bytes);
				fis.close();
				response.getOutputStream().write(bytes);
				response.getOutputStream().close();
				this.result.nothing();
			}else {
				this.fail("(No such file or directory):"+SystemConfigs.getConfig("store.files")+file.getName() );
				this.fail("arquivo: "+SystemConfigs.getConfig("store.files")+file.getName());
			}
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail("(No such file or directory): "+SystemConfigs.getConfig("store.files")+file.getName() );
		}
	}

}