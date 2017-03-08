package org.forpdi.planning.attribute;

import org.forpdi.core.abstractions.IdentifiableComponent;

public abstract class AttributeType extends IdentifiableComponent {

	public abstract String getWidget();
	public abstract AttributeTypeWrapper getWrapper();

}
