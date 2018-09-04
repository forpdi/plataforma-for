package org.forpdi.core.jobs;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.forpdi.core.company.Company;
import org.forpdi.dashboard.manager.LevelInstanceHistory;
import org.forpdi.planning.attribute.AggregateIndicator;
import org.forpdi.planning.document.Document;
import org.forpdi.planning.document.DocumentAttribute;
import org.forpdi.planning.document.DocumentSection;
import org.forpdi.planning.fields.actionplan.ActionPlan;
import org.forpdi.planning.plan.Plan;
import org.forpdi.planning.plan.PlanDetailed;
import org.forpdi.planning.plan.PlanMacro;
import org.forpdi.planning.structure.Structure;
import org.forpdi.planning.structure.StructureLevel;
import org.forpdi.planning.structure.StructureLevelInstance;
import org.forpdi.planning.structure.StructureLevelInstanceDetailed;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.SimpleLogicalDeletableEntity;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.serialization.gson.GsonSerializerBuilder;

public class DatabaseBackup extends HibernateBusiness  {

	
	@Inject private GsonSerializerBuilder gsonBuilder;
	

	/**
	* consulta o banco e salva as informações em um arquivo
	*
	* @throws IOException 
	*
	*/
    public Download execute() throws IOException {
    
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		
		Compact(zos, PlanMacro.class);
    	Compact(zos, Structure.class);
    	Compact(zos, Plan.class);
    	Compact(zos, Document.class);
    	Compact(zos, StructureLevel.class);
    	

    	
    	
    	Compact(zos, LevelInstanceHistory.class);
    	Compact(zos, AggregateIndicator.class);
    	Compact(zos, DocumentAttribute.class);
    	Compact(zos, DocumentSection.class);
    	Compact(zos, ActionPlan.class);

    	Compact(zos, PlanDetailed.class);
    	
    	Compact(zos, StructureLevelInstance.class);
    	Compact(zos, StructureLevelInstanceDetailed.class);
    	
    	zos.close();
		 
    	return new ByteArrayDownload(bos.toByteArray(), "application/octet-stream", "arquivo.forpdi");
    }
		 


    /**
	 * Adiciona Todos os registros do banco desta classe nos arquivo zip
	 * 
	 * @param ZipOutputStream
	 * 		arquivo a ser adicionado ao zip
	 * 
	 * @param <clazz>
	 * 		classe dos objetos a serem recuperados do banco
	 * 
	 * 
     * @throws IOException 
	 *
	 */
    private void Compact(ZipOutputStream zos, Class<?> clazz) throws IOException {
    	
    	Gson gson = gsonBuilder.create();
    	String classe=clazz.getSimpleName();
    	JsonArray jsonArray = new JsonArray() ;
    	JsonObject jsonObject;
    	String str;
    	
    	List<Object> object = retrieve(clazz);
    	
    	if(!object.isEmpty()) {
    		
    		switch(clazz.getSimpleName()) {
    			case "PlanMacro" :
    				for (Object p : object ) {
    					ZipAdd(zos, classe,  gson.toJson(p));	
    		    	}
    				return;
    				
       			case "Structure" :
       				
					//jsonArray.add(gson.toJson(object));
    				//ZipAdd(zos, classe,  gson.toJson(object));
    				//return;
					jsonArray.add(Makejson(object, classe, (Long[]) null));
    				break;
					
    			case "Document" :    				
    				//juntar Doc com o plan_id
    				for (Object doc : object ) {
    					jsonArray.add(Makejson(doc, classe, ((Document) doc).getPlan().getId()));
    				}

    				break;
    				
    			case "Plan"	:
    				//juntar Plan com o parent_id, structure_id
    				for (Object plan : object ) {
    					jsonArray.add(Makejson(plan, classe, ((Plan) plan).getStructure().getId(),((Plan) plan).getParent().getId()));
    				}
    				break;   				
 
    			case "StructureLevel" :	
    				//juntar StructureLevel com o structure_id
       				for (Object strlevel : object ) {
    		    		jsonArray.add(Makejson(strlevel, classe, ((StructureLevel) strlevel).getStructure().getId()));
       				}
    				break;
    				
 
    			default: return;
    		
    		}
    		
			jsonObject = new JsonObject() ;
			jsonObject.add("json", jsonArray);
    		str=jsonObject.toString();
    		
    		ZipAdd(zos,clazz.getSimpleName(), str);	
    		
    	}
	}
    
    
    
	/**
	* adiciona dados ao banco a partir do arquivo
	* 
	* @param file
	*		arquivo a ser importado
	* 
	* @throws IOException 
	* @throws org.json.simple.parser.ParseException 
	* 
	*/
    public void restore(UploadedFile file) throws IOException,  org.json.simple.parser.ParseException {
    	
    	List<File>  files = Decompact(file.getFile());
    	Gson gson = gsonBuilder.create();
     	
    	Map<Long,Long> map_id_planomacro= new HashMap<Long,Long>();
    	Map<Long,Long> map_id_documento= new HashMap<Long,Long>();
    	Map<Long,Long> map_id_structure= new HashMap<Long,Long>();
    	Map<Long,Long> map_id_structure_level= new HashMap<Long,Long>();
    	Map<Long,Long> map_id_plan= new HashMap<Long,Long>();
    	
    	for(File f : files) {
    		
    		String register = IOUtils.toString(new FileInputStream(f), StandardCharsets.ISO_8859_1);
    		Long id_old;
    		String name=f.getName().split("_")[0];
    		JSONParser parser = new JSONParser();
    		//JSONObject obj;
    		JSONArray array;
    		
    		//company passado na hora da importação
			Company company = new Company(); 
			company.setId((long) 1);


    		switch(name) {
    			case "PlanMacro" :		
    				PlanMacro pm= gson.fromJson(register, PlanMacro.class);
    				id_old=pm.getId();
    				pm.setId(null);
    				pm.setCompany(company);	
    				this.dao.persist(pm);
	    			map_id_planomacro.put(id_old, pm.getId());
	    			break;
	    							
				case "Structure" :
					JSONObject jsonobj = (JSONObject) parser.parse(register);
					array = (JSONArray)jsonobj.get("json");
					array=(JSONArray) ((JSONObject)array.get(0)).get("Structure");
					
					for(int i=0; i<array.size(); i++) {
						Structure st = gson.fromJson(array.get(i).toString(), Structure.class);
						id_old=st.getId();
						st.setCompany(company);
						st.setId(null);
						this.dao.persist(st);
						map_id_structure.put(id_old, st.getId());
					}
					
				break;
				
				case "Plan" :
					Readjson(register, Plan.class, map_id_plan, map_id_structure, map_id_planomacro);
					break;
				
				case "StructureLevel" :   				
    				Readjson(register, StructureLevel.class, map_id_structure_level, map_id_structure, null);
    				break;
    				
    			case "Document" :
    				Readjson(register, Document.class, map_id_documento, map_id_planomacro, null);
    				break;

    			default: 
    				break;
    		}
    	}
    }

    
    
    /**
	* adiciona dados ao banco a partir do json
	* 
	* @param register
	*		json do registro
	*
	* @param clazz
	* 		classe a ser lida
	* 
	* @param map_atual
	* 		mapa do id da classe que está sendo importada
	* 
	* @param map_fkey_1
	* 		mapa do id da primeira foreign key desta classe
	* 
	* @param map_fkey_2
	* 		mapa do id da segunda foreign key desta classe
	* 
	* @throws org.json.simple.parser.ParseException 
	* 
	*/
	private void Readjson( String register, Class<?> clazz, Map<Long,Long> map_atual, Map<Long,Long> map_fkey_1, Map<Long, Long> map_fkey_2) throws org.json.simple.parser.ParseException {
		
		Gson gson = gsonBuilder.create();
		JSONParser parser = new JSONParser();
		JSONObject jsonobj = (JSONObject) parser.parse(register);
		JSONArray array = (JSONArray)jsonobj.get("json");
		
		for(int i=0; i<array.size(); i++) {
			
			JSONObject jo=(JSONObject) array.get(i);
			
			Object obj = gson.fromJson(jo.get(clazz.getSimpleName()).toString(), clazz);
			long id_old = Long.parseLong(jo.get("foreign_key_1").toString());
			Criteria criteria;
			
		
			switch(clazz.getSimpleName()){
				case "Document" :
					criteria= this.dao.newCriteria(PlanMacro.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old)));
					(((Document) obj)).setPlan((PlanMacro) criteria.uniqueResult());
					break;
					
				case "StructureLevel" :
					criteria= this.dao.newCriteria(Structure.class);
					criteria.add(Restrictions.eq("id", map_fkey_1.get(id_old)));
					(((StructureLevel) obj)).setStructure((Structure) criteria.uniqueResult());
					break;
				
				case "Plan"	:
					criteria= this.dao.newCriteria(Structure.class);
					criteria.add(Restrictions.eq("id",map_fkey_1.get(id_old)));
					(((Plan) obj)).setStructure((Structure) criteria.uniqueResult());
					
					criteria= this.dao.newCriteria(PlanMacro.class);
					criteria.add(Restrictions.eq("id", map_fkey_2.get(id_old)));
					(((Plan) obj)).setParent((PlanMacro) criteria.uniqueResult());
					break;
			}
			
			((SimpleLogicalDeletableEntity) clazz.cast(obj)).setId(null);
			
			this.dao.persist((Serializable) clazz.cast(obj));
			
			map_atual.put(id_old, ((SimpleLogicalDeletableEntity) clazz.cast(obj)).getId());
		}
	}
    

    /**
	* Escreve json adicionando chaves estrangeiras
	* 
	* @param object 
	* 		objeto do banco
	* 
	* @param clazz
	* 		nome da classe
	* 
	* @param f_id
	* 		valor das chaves estrangeiras
	*
	* @return JsonObject
	* 		objeto json
	*/
    private JsonObject Makejson(Object object, String clazz, Long... f_id) {
    	
    	Gson gson = gsonBuilder.create();
		JsonObject jsonObject = new JsonObject() ;
		JsonElement jsonElement1= gson.toJsonTree(object);
		jsonObject.add(clazz, jsonElement1);
			
		if(f_id!=null) {
			for(int i=0 ; i < f_id.length; i++) {
				JsonElement jsonElement = gson.toJsonTree(f_id[i]);
				jsonObject.add("foreign_key_"+String.valueOf(i+1), jsonElement);	
			}
		}
			
		return jsonObject;

	}



	/**
	 * Recuperar Todos os arquivo do zip
	 * 
	 * @param InputStream
	 * 		arquivo do upload
	 * 
	 * @return List<File>
	 * 			lista de arquivos do zip
	 * 
     * @throws IOException 
	 *
	 */
    private List<File> Decompact(InputStream inputStream) throws IOException {
    	
    	List<File> files = new ArrayList<File>();
         byte[] buffer = new byte[1024];
         ZipInputStream zis = new ZipInputStream(inputStream);
         ZipEntry zipEntry = zis.getNextEntry();
         while(zipEntry != null){
             String fileName = zipEntry.getName().split("_")[0];
             File newFile = File.createTempFile(fileName+"_", ".temp");
             FileOutputStream fos = new FileOutputStream(newFile);
             int len;
             while ((len = zis.read(buffer)) > 0) {
                 fos.write(buffer, 0, len);
             }
             fos.close();
             files.add(newFile);
             zipEntry = zis.getNextEntry();
         }
         zis.closeEntry();
         zis.close();
         
		return files;	
    }
    
    
    
	/**
   	 * Adiciona arquivo no zip
   	 * 
   	 * @param ZipOutputStream
   	 * 		arquivo zip
   	 * 
   	 * @param clazz
   	 * 		nome da classe
   	 * 
   	 * @param str
   	 * 		json a ser salvo
   	 * 
     * @throws IOException 
   	 *
   	 */
	private void ZipAdd(ZipOutputStream zos, String clazz, String str) throws IOException {
		
		File tempFile = File.createTempFile(clazz+"_", ".temp");
		FileOutputStream fos = new FileOutputStream (tempFile);
		fos.write(str.getBytes());
		fos.close();
		
    	byte[] buffer = new byte[1024];
    	FileInputStream fis = new FileInputStream(tempFile);
    	
    	zos.putNextEntry(new ZipEntry(tempFile.getName()));
    	int length;
    	while ((length = fis.read(buffer)) > 0){
    		zos.write(buffer, 0, length);
    	}
		zos.closeEntry();
		fis.close();
	}



	/**
	 * Lista Modelo
	 * 
	 * @param List<clazz>
	 * 		Classe do modelo no banco
	 * 
	 * @return String
	 *		resultado da busca
	 */
    private  <clazz> List<clazz> retrieve(Class clazz) {
			List<clazz> modelo =  this.dao.newCriteria(clazz).list();
			return modelo;
	}
	
}




