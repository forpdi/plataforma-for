package org.forpdi.planning.structure.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.forpdi.planning.structure.StructureBS;
import org.junit.Assert;
import org.junit.Test;

public class StructureImporterTest {

	@Test
	public void testStructureXMLParsing() throws Exception {
		InputStream xmlIN = new FileInputStream(
				"/home/renato/development/forpdi/forpdi-repo/backend-java/src/main/resources/readedStructure-unifal.xml");
		Assert.assertNotNull("Input stream do XML", xmlIN);
		StructureImporter importer = new StructureImporter(xmlIN, StructureBS.SCHEMA);
		Assert.assertNotNull("Structure not null.", importer.readedStructure);
		PrintStream out = System.out;
		out.printf("{\nStructure:\n\tname:%s\n\tdescription:%s\n\tlevels:[\n", importer.readedStructure.name,
				importer.readedStructure.description);
		for (StructureBeans.StructureLevel level : importer.readedStructure.levels) {
			out.printf("\t{\n\t\tname:%s\n\t\tdescription:%s\n\t\tattributes:[\n", level.name, level.description);
			for (StructureBeans.Attribute attr : level.attributes) {
				out.printf("\t\t{\n\t\t\tlabel:%s\n\t\t\tdescription:%s\n\t\t\ttype:%s\n", attr.label, attr.description,
						attr.type);
				out.printf("\t\t\trequired:%s\n\t\t\tvisibleInTables:%s\n\t\t}\n", attr.required ? "true" : "false",
						attr.visibleInTables ? "true" : "false");
			}
			out.printf("\t\t]\n\t}\n");
		}
		out.printf("\t]\n}\n");
		xmlIN.close();
	}
}
