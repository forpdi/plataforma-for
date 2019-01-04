package org.forpdi.core.company;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;


import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.event.Current;
import org.forpdi.core.properties.SystemConfigs;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.core.user.authz.permission.ExportDataPermission;
import org.forpdi.core.user.authz.permission.RestoreDataPermission;
import org.forpdi.system.Archive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.util.StoragerUtils;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.upload.UploadSizeLimit;
import br.com.caelum.vraptor.observer.upload.UploadedFile;



@Controller
public class BackupAndRestoreController extends AbstractController  {

	@Inject private BackupAndRestoreHelper dbbackup;
	@Inject @Current private CompanyDomain domain;
	
	private static UploadedFile file=null;
	
	/**
	 * Backup das tabelas
	 * 
	 *@param id 
	 *		id plano macro
	 *
	 */
	@Get("/company/export")
	@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {ExportDataPermission.class})
	public Download export() {
		try {
			LOGGER.infof("Starting export company '%s'...", this.domain.getCompany().getName());
			byte[] exportData = dbbackup.export(this.domain.getCompany());
			return new ByteArrayDownload(exportData, "application/octet-stream",
				String.format("plans-%d-%s.fbk", domain.getCompany().getId(), LocalDateTime.now().toString()));
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
	@Post("/company/fbkupload")
	@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {RestoreDataPermission.class})
	@UploadSizeLimit(fileSizeLimit=5 * 1024 * 1024)
	public void  fbkupload(UploadedFile file) {
		
		if (file == null) {
			this.fail("upload falhou");
			return;
		}else if (BackupAndRestoreController.file != null) {
			this.fail("processo de importação já em andamento");
			return;
		}else{ 
			try {
			
				BackupAndRestoreController.file=file;
				this.success("upload completo.");
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
		
		if(this.domain.getCompany() == null) {
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
	@Permissioned(value=AccessLevels.COMPANY_ADMIN, permissions= {RestoreDataPermission.class})
	public void  state() {
		try {
			String  porcent =String.valueOf(dbbackup.getPorcentagem());
			
			if (dbbackup.getPorcentagem()==100) {
				dbbackup.resetQuantity();
			}
			this.success(porcent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
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
			File targetFile = new File(SystemConfigs.getConfig("store.files")+"/"+archive.getName());
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
			this.fail("Arquivo não encontrado");
			return;
		}
		try{
			File initialFile = new File(SystemConfigs.getConfig("store.files")+file.getName());
			if(initialFile.exists()){
				FileInputStream fis = new FileInputStream(initialFile);
				IOUtils.copy(fis, response.getOutputStream());
				fis.close();
			}
			this.success(file);
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail(ex.getMessage());
		}
	}
}