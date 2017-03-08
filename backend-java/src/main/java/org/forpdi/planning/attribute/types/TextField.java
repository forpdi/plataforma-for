package org.forpdi.planning.attribute.types;

import java.util.Date;

import org.forpdi.planning.attribute.AttributeType;
import org.forpdi.planning.attribute.AttributeTypeWrapper;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

public class TextField extends AttributeType {

	private AttributeTypeWrapper wrapper = new Wrapper();

	public final String WIDGET_NAME = "TextField";
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
		return "Campo de Texto";
	}

	public static class Wrapper implements AttributeTypeWrapper {

		@Override
		public String fromDatabase(String databaseValue) {
			return databaseValue;
		}

		@Override
		public String fromDatabaseNumerical(Double databaseValue) {
			return null;
		}

		@Override
		public String toDatabase(String viewValue) {
			return GeneralUtils.isEmpty(viewValue) ? "":viewValue.trim();
		}
		@Override
		public Double toDatabaseNumerical(String viewValue) {
			return null;
		}

		@Override
		public String fromDatabaseDate(Date databaseValue) {
			return null;
		}

		@Override
		public Date toDatabaseDate(String viewValue) {
			return null;
		}
		
		@Override
		public boolean isNumerical() {
			return false;
		}
		@Override
		public boolean isDate() {
			return false;
		}
		
		@Override
		public String prefix() {
			return "";
		}
		@Override
		public String suffix() {
			return "";
		}

	}
}
