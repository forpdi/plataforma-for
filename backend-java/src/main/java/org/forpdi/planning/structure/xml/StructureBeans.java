package org.forpdi.planning.structure.xml;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import org.dom4j.Element;

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
		public boolean leaf;
		public boolean goal;
		public boolean indicator;
		public boolean objective;
		@Transient public Element child;
	}
	
	public static class Attribute implements Serializable {
		private static final long serialVersionUID = 1L;
		public String label;
		public String description;
		public String type;
		public boolean periodicity;
		public List<OptionsField> optionsField;
		public List<ScheduleValue> scheduleValues;
		public boolean required;
		public boolean visibleInTables;
		public boolean finishDate;
		public boolean expectedField;
		public boolean minimumField;
		public boolean maximumField;
		public boolean reachedField;
		public boolean justificationField;
		public boolean polarityField;
		public boolean formatField;
		public boolean periodicityField;
		public boolean beginField;
		public boolean endField;
		public boolean bscField;
	}
	
	public static class ScheduleValue implements Serializable {
		private static final long serialVersionUID = 1L;
		public String label;
		public String type;
	}
	
	public static class OptionsField implements Serializable {
		private static final long serialVersionUID = 1L;
		public String label;
		public Attribute attribute;
	}
}
