package org.forpdi.planning.attribute.types;

import org.forpdi.planning.attribute.AttributeType;
import org.forpdi.planning.attribute.AttributeTypeWrapper;
import org.forpdi.planning.attribute.types.TotalField.Wrapper;

public class AttachmentField extends AttributeType{
	private AttributeTypeWrapper wrapper = new Wrapper();

	public final String WIDGET_NAME = "AttachmentField";

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
		return "Anexar arquivo";
	}
	
}
