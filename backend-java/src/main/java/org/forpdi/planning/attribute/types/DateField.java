package org.forpdi.planning.attribute.types;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.forpdi.planning.attribute.AttributeType;
import org.forpdi.planning.attribute.AttributeTypeWrapper;

public class DateField extends AttributeType {

	private AttributeTypeWrapper wrapper = new DateWrapper();

	public final String WIDGET_NAME = "DateField";

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
		return "Data";
	}

	public static class DateWrapper extends TextField.Wrapper {
		@Override
		public boolean isDate() {
			return true;
		}

		@Override
		public String fromDatabaseDate(Date databaseValue) {
			String newDate = new SimpleDateFormat("dd/MM/yyyy").format(databaseValue);
			return newDate;
		}

		@Override
		public Date toDatabaseDate(String viewValue) {
			String[] dateString = viewValue.split("/");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(dateString[2]));
			cal.set(Calendar.MONTH, Integer.parseInt(dateString[1]));
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString[0]));

			Date date = cal.getTime();

			return date;
		}
	}

}
