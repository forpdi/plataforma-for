package org.forpdi.planning.attribute.types;

import java.text.NumberFormat;

import javax.validation.UnexpectedTypeException;

import org.forpdi.core.properties.CoreMessages;
import org.forpdi.planning.attribute.AttributeType;
import org.forpdi.planning.attribute.AttributeTypeWrapper;

public class Percentage extends AttributeType {

	private AttributeTypeWrapper wrapper = new Wrapper();

	public final String WIDGET_NAME = "Percentage";
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
		return "Porcentagem";
	}

	public static class Wrapper extends NumberField.Wrapper {
		
		NumberFormat formatter = NumberFormat.getPercentInstance(CoreMessages.DEFAULT_LOCALE);
		
		@Override
		public String fromDatabaseNumerical(Double databaseValue) {
			return this.formatter.format(databaseValue);
		}

		@Override
		public Double toDatabaseNumerical(String viewValue) {
			try {
				return this.formatter.parse(viewValue).doubleValue();
			} catch(Exception ex) {
				throw new UnexpectedTypeException(ex);
			}
		}

	}
}
