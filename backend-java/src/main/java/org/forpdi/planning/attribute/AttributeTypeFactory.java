package org.forpdi.planning.attribute;

import org.forpdi.core.abstractions.ComponentFactory;
import org.forpdi.planning.attribute.types.*;

public final class AttributeTypeFactory extends ComponentFactory<AttributeType> {

	private static AttributeTypeFactory instance = new AttributeTypeFactory();
	
	public static AttributeTypeFactory getInstance() {
		return instance;
	}
	
	private AttributeTypeFactory() {
		this.register(new TextArea());
		this.register(new TextField());
		this.register(new DateField());
		this.register(new DateTimeField());
		this.register(new Currency());
		this.register(new NumberField());
		this.register(new Percentage());
	}
	
}
