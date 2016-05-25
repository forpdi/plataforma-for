package org.forpdi.planning.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.enterprise.context.RequestScoped;

import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.structure.xml.StructureImporter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.caelum.vraptor.boilerplate.HibernateBusiness;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;

/**
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@RequestScoped
public class StructureBS extends HibernateBusiness {

	private static final int PAGESIZE = 12;

	public static final String SCHEMA;
	static {
		try {
			InputStream xsd = StructureBS.class.getResourceAsStream("/forpdi_structure_1.0.xsd");
			if (xsd == null) {
				throw new RuntimeException("The import Schema couldn't be opened.");
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(xsd, Charset.forName("UTF-8")));
			String line = null;
			StringBuilder str = new StringBuilder();
			while ((line = in.readLine()) != null) {
				str.append(line).append("\n");
			}
			SCHEMA = str.toString();
			xsd.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public Structure importStructure(File xmlFile) throws Exception {
		InputStream xml = new FileInputStream(xmlFile);
		StructureImporter importer = new StructureImporter(xml, SCHEMA);
		xml.close();
		this.dao.execute(importer);
		return importer.getImportedStructure();
	}
	
	public PaginatedList<Structure> list(int page) {
		PaginatedList<Structure> results = new PaginatedList<Structure>();
		Criteria criteria =
			this.dao.newCriteria(Structure.class)
			.setFirstResult(page*PAGESIZE).setMaxResults(PAGESIZE)
			.add(Restrictions.eq("deleted", false))
			.addOrder(Order.asc("name"))
		;
		Criteria counting =
			this.dao.newCriteria(Structure.class)
			.setProjection(Projections.countDistinct("id"))
			.add(Restrictions.eq("deleted", false))
		;
		results.setList(this.dao.findByCriteria(criteria, Structure.class));
		results.setTotal((Long) counting.uniqueResult());
		return results;
	}

	public PaginatedList<StructureLevel> listLevels(Structure structure) {
		PaginatedList<StructureLevel> results = new PaginatedList<StructureLevel>();
		Criteria criteria =
			this.dao.newCriteria(StructureLevel.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("structure", structure))
			.addOrder(Order.asc("sequence"))
		;
		results.setList(this.dao.findByCriteria(criteria, StructureLevel.class));
		results.setTotal((long) results.getList().size());
		return results;
	}

	public PaginatedList<Attribute> listAttributes(StructureLevel level) {
		PaginatedList<Attribute> results = new PaginatedList<Attribute>();
		Criteria criteria =
			this.dao.newCriteria(Attribute.class)
			.add(Restrictions.eq("deleted", false))
			.add(Restrictions.eq("level", level))
			.addOrder(Order.asc("label"))
		;
		results.setList(this.dao.findByCriteria(criteria, Attribute.class));
		results.setTotal((long) results.getList().size());
		return results;
	}

}
