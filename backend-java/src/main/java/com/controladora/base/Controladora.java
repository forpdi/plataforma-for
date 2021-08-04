package com.controladora.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Controladora {

	
	public final static  String URL_UPLOAD_FILE = "ARQUIVO_UPLOAD_FORPDI";
	public final static  String URL_DOWNLOAD_FILE = "ARQUIVO_DOWNLOAD_FORPDI";
	private final Integer MAX_FILE_SIZE  = 2 * 1024 * 1024;
	private String caminhoArquivo = "";
	private String caminhoDownArquivo = "";
	
	
	public Controladora(ServletContext ct) {
		  caminhoArquivo = ct.getInitParameter(URL_UPLOAD_FILE);
		  caminhoDownArquivo = ct.getInitParameter(URL_DOWNLOAD_FILE); 

	}
	
	
	
	/**
	 * Metodo que remove um arquivo passado por parametro 
	 * @param nomeArquivo nome do arquivo que deseja remover
	 * @return true se o arquivo foi removido com sucesso ou nao 
	 * @throws IOException excessao no caso de erro de remocao do arquivo
	 */
	public boolean removeArquivo(String nomeArquivo) throws IOException {
		Path arquivo= Paths.get(caminhoArquivo, nomeArquivo);		
		return Files.deleteIfExists(arquivo);
		
	}
	
	
	/**
	 * Método que verifica se o arquivo esta correto.
	 * @param arq nome do arquivo
	 */
	private void verificaDir(File arq) {
		if(!arq.exists ()) {
			System.out.println("O Diretorio não existe no sistema: " + arq.getAbsolutePath());
			
			boolean test = arq.mkdirs();
			if(test) {
				System.out.println("Criação do diretorio de upload com sucesso");	
			}else {
				System.out.println("Cuidado diretorio não foi criado");				
			}
			
		}
	}
	
	/**
	 * Método que realizada o upload do arquivo na pasta definida como variavel de ambiente 
	 * @param input InputStrem que contem os dados do arquivo a ser armazenado 
	 * @param prefixo do arquivo que desejar gravar 
	 * @return url do arquivo armazenado 
	 */
	public String enviaArquivo(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, java.io.IOException, Exception{
	    
	      String result = "";
		  // verifica se esta requisição esta como envio de arquivo 
	      boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	  
	      if(isMultipart ) {
	    	  // criar a fabrica de enviado do arquivo 
	    	  DiskFileItemFactory factory = new DiskFileItemFactory();
	    	  File arquivo = new File(caminhoArquivo);
	    	  verificaDir(arquivo);
	    	  factory.setRepository(arquivo);
	    	  ServletFileUpload upload = new ServletFileUpload(factory);
	    	  upload.setFileSizeMax(MAX_FILE_SIZE);
	    	 	    		  List<FileItem> items = upload.parseRequest(request);
	    		  FileItem arquivoGravar = null;
	    		  String chave = "";
	    		  String prefixo = "";
	    		  for(FileItem item:items) {
	    			  if(!item.isFormField()) {
	    				  arquivoGravar = item; 
	    			  }else {
	    				  // nome do arquivo
	    				  if(item.getFieldName().equalsIgnoreCase("chave"))
	    					  chave = item.getString();
	    				  else if(item.getFieldName().equalsIgnoreCase("nome"))
	    					  prefixo = item.getString();
	    			  }
	    		
	    		  }
	    		  
	    		  // tentando gravar arquivo 
	    		  if(arquivoGravar != null) {                              // nao manter o tipo de arquivo para foto do perfil
	    			  String typeArquivo = (arquivoGravar.getName().indexOf(".")) >0 && !prefixo.equals("imageicon") ? 
	    					  arquivoGravar.getName().substring(arquivoGravar.getName().indexOf(".")):""; 
	    			  		  
	    			  String nome = prefixo + '-' + chave + typeArquivo ;
	    			  arquivoGravar.getContentType();
	    			  arquivoGravar.write(new File(arquivo, nome));
    			  	  result = caminhoDownArquivo + "/" + nome;
	    		  }
	    	  
	    	  
	      }
	      
	      return result;
	}
	
}
