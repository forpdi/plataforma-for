package org.forpdi.planning.attribute.types;

import org.forpdi.planning.attribute.AttributeType;
import org.forpdi.planning.attribute.AttributeTypeWrapper;

public class BudgetField extends AttributeType{

	private AttributeTypeWrapper wrapper = new Wrapper();

	public final String WIDGET_NAME = "BudgetField";
	
	@Override
	public String getWidget() {
		return WIDGET_NAME;
	}

	@Override
	public AttributeTypeWrapper getWrapper() {
		return wrapper;
	}

	@Override
	public String getDisplayName() {		
		return "Orçamento";
	}
	
	public static class Wrapper extends TextField.Wrapper {
		
		@Override
		public String fromDatabase(String databaseValue) {
			return null;
		}
		
		@Override
		public String toDatabase(String viewValue) {
			return null;
		}
		
	}
	
}
