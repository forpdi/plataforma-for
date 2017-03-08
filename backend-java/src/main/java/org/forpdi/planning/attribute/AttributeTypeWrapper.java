package org.forpdi.planning.attribute;

import java.util.Date;

public interface AttributeTypeWrapper {
	public String fromDatabase(String databaseValue);
	public String fromDatabaseNumerical(Double databaseValue);
	public String fromDatabaseDate(Date databaseValue);
	public String toDatabase(String viewValue);
	public Double toDatabaseNumerical(String viewValue);
	public Date toDatabaseDate(String viewValue);

	public boolean isNumerical();
	public boolean isDate();
	
	public String prefix();
	public String suffix();
}
