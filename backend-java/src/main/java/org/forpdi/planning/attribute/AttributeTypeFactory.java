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
		this.register(new BudgetField());
		this.register(new ActionPlanField());
		this.register(new ScheduleField());
		this.register(new TableField());
		this.register(new TotalField());
		this.register(new SelectField());
		this.register(new SelectPlan());
		this.register(new ResponsibleField());
		this.register(new AttachmentField());
	}
	
}
