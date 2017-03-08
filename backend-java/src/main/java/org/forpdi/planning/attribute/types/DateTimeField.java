package org.forpdi.planning.attribute.types;

import org.forpdi.planning.attribute.AttributeType;
import org.forpdi.planning.attribute.AttributeTypeWrapper;

public class DateTimeField extends AttributeType {

	private AttributeTypeWrapper wrapper = new DateField.DateWrapper();

	public final String WIDGET_NAME = "DateTimeField";
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
		return "Data e Hora";
	}

}
