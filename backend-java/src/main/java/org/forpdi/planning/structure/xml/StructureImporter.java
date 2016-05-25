package org.forpdi.planning.structure.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.AbstractAttribute;
import org.forpdi.planning.attribute.Attribute;
import org.forpdi.planning.structure.Structure;
import org.forpdi.planning.structure.StructureLevel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import br.com.caelum.vraptor.boilerplate.HibernateDAO.TransactionalOperation;

public class StructureImporter implements TransactionalOperation {
	
	private SAXReader dom4jReader = new SAXReader();
	
	protected StructureBeans.Structure readedStructure;
	private Structure savedStructure;
	
	public StructureImporter(InputStream xmlIS, String schema) throws Exception {
		StringBuffer xmlFile = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(xmlIS, Charset.forName("UTF-8")));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            xmlFile.append(line);
        }
        String xml = xmlFile.toString();

        /* TODO Publicar schema para validação.
		Validator validator = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
			.newSchema(new StreamSource(new CharArrayReader(schema.toCharArray()))).newValidator();
		Source source = new StreamSource(new CharArrayReader(xml.toCharArray()));
		validator.validate(source);
		*/

        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        // ask extending class to parse
        
        this.dom4jReader.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException ex) throws SAXException {
				System.out.println(ex.getMessage());
			}
			
			@Override
			public void fatalError(SAXParseException ex) throws SAXException {
				System.err.println(ex.getMessage());
			}
			
			@Override
			public void error(SAXParseException ex) throws SAXException {
				System.err.println(ex.getMessage());
			}
		});
        this.readedStructure = parseInternal(bais);
        bais.close();
	}
	
	@Override
	public void execute(Session session) throws HibernateException {
		AttributeTypeMap types = new AttributeTypeMap();
		Structure structure = new Structure();
		structure.setDeleted(false);
		structure.setDescription(this.readedStructure.description);
		structure.setName(this.readedStructure.name);
		session.persist(structure);
		
		for (StructureBeans.StructureLevel rawLevel : this.readedStructure.levels) {
			StructureLevel level = new StructureLevel();
			level.setDeleted(false);
			level.setDescription(rawLevel.description);
			level.setName(rawLevel.name);
			level.setSequence(rawLevel.sequence);
			level.setLeaf(this.readedStructure.levels.size() == (rawLevel.sequence+1));
			level.setStructure(structure);
			session.persist(level);
			
			for (StructureBeans.Attribute rawAttr : rawLevel.attributes) {
				Attribute attr = new Attribute();
				attr.setDeleted(false);
				attr.setDescription(rawAttr.description);
				attr.setLabel(rawAttr.label);
				attr.setRequired(rawAttr.required);
				attr.setVisibleInTables(rawAttr.visibleInTables);
				attr.setLevel(level);
				attr.setType(types.get(rawAttr.type));
				session.persist(attr);
			}
		}
		
		this.savedStructure = structure;
	}
	
	public Structure getImportedStructure() {
		return this.savedStructure;
	}
	
	public StructureBeans.Structure parseInternal(InputStream is) throws DocumentException {
		StructureBeans.Structure struct = new StructureBeans.Structure();
        Document document = this.dom4jReader.read(is);

        Element root = document.getRootElement();
        if (!root.getQName().getName().equals("structure")) {
            throw new RuntimeException("Wrong element: " + root.getQName());
        }

        Iterator<Node> children = root.elementIterator();
        while (children.hasNext()) {
            Node n = children.next();
            String childName = n.getName();
            if (childName == null)
                continue;
            if (childName.equals("structureName")) {
            	struct.name = ((Element) n).getTextTrim();
            } else if (childName.equals("structureDescription")) {
            	struct.description = ((Element) n).getTextTrim();
            } else if (childName.equals("structureLevels")) {
            	struct.levels = this.parseLevels((Element) n);
            }
        }
        return struct;
    }
	
	private List<StructureBeans.StructureLevel> parseLevels(Element el) {
		List<StructureBeans.StructureLevel> levels = new LinkedList<StructureBeans.StructureLevel>();
		
		Iterator<Node> children = el.elementIterator();
        int sequence = 0;
		while (children.hasNext()) {
            Node n = children.next();
            String childName = n.getName();
            if (childName == null)
                continue;
            if (childName.equals("level")) {
            	StructureBeans.StructureLevel level = this.parseLevel((Element) n);
            	level.sequence = sequence;
            	levels.add(level);
                sequence++;
            }
        }
		
		return levels;
	}
	private StructureBeans.StructureLevel parseLevel(Element el) {
		StructureBeans.StructureLevel level = new StructureBeans.StructureLevel();
		
		Iterator<Node> children = el.elementIterator();
		while (children.hasNext()) {
            Node n = children.next();
            String childName = n.getName();
            if (childName == null)
                continue;
            if (childName.equals("name")) {
            	level.name = ((Element) n).getTextTrim();
            } else if (childName.equals("description")) {
            	level.description = ((Element) n).getTextTrim();
            } else if (childName.equals("attributes")) {
            	level.attributes = this.parseAttributes((Element) n);
            }
        }
		
		return level;
	}
	
	private List<StructureBeans.Attribute> parseAttributes(Element el) {
		List<StructureBeans.Attribute> attrs = new LinkedList<StructureBeans.Attribute>();
		
		Iterator<Node> children = el.elementIterator();
        while (children.hasNext()) {
            Node n = children.next();
            String childName = n.getName();
            if (childName == null)
                continue;
            if (childName.equals("attribute")) {
            	StructureBeans.Attribute attr = this.parseAttribute((Element) n);
            	attrs.add(attr);
            }
        }
		
		return attrs;
	}
	private StructureBeans.Attribute parseAttribute(Element el) {
		StructureBeans.Attribute attr = new StructureBeans.Attribute();
		
		Iterator<Node> xmlAttrs = el.attributeIterator();
		while (xmlAttrs.hasNext()) {
			Node n = xmlAttrs.next();
            String childName = n.getName();
            if (childName == null)
                continue;
            if (childName.equals("required")) {
            	attr.required = ((AbstractAttribute) n).getValue().equals("true");
            } else if (childName.equals("visibleInTables")) {
                attr.visibleInTables = ((AbstractAttribute) n).getValue().equals("true");
            }
		}
		Iterator<Node> children = el.elementIterator();
		while (children.hasNext()) {
            Node n = children.next();
            String childName = n.getName();
            if (childName == null)
                continue;
            if (childName.equals("label")) {
            	attr.label = ((Element) n).getTextTrim();
            } else if (childName.equals("description")) {
            	attr.description = ((Element) n).getTextTrim();
            } else if (childName.equals("type")) {
            	attr.type = ((Element) n).getTextTrim();
            }
        }
		
		return attr;
	}

}
