package org.forpdi.planning.structure.xml;

import java.util.HashMap;

import org.forpdi.planning.attribute.AttributeTypeFactory;

public class AttributeTypeMap {

	private final HashMap<String, String> map = new HashMap<String, String>();
	
	public AttributeTypeMap() {
		AttributeTypeFactory.getInstance().each((type) -> {
			String[] splitted = type.getId().split("\\.");
			String name = splitted[splitted.length-1];
			map.put(name, type.getId());
		});
	}
	
	public String get(String name) {
		return map.get(name);
	}
}
