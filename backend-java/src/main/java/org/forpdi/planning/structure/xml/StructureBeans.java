package org.forpdi.planning.structure.xml;

import java.io.Serializable;
import java.util.List;

public final class StructureBeans {
	
	public static class Structure implements Serializable {
		private static final long serialVersionUID = 1L;
		public String name;
		public String description;
		public List<StructureLevel> levels;
	}
	
	public static class StructureLevel implements Serializable {
		private static final long serialVersionUID = 1L;
		public String name;
		public String description;
		public int sequence;
		public List<Attribute> attributes;
	}
	
	public static class Attribute implements Serializable {
		private static final long serialVersionUID = 1L;
		public String label;
		public String description;
		public String type;
		public boolean required;
		public boolean visibleInTables;
	}
}
