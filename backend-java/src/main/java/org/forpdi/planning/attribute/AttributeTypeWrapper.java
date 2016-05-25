package org.forpdi.planning.attribute;

public interface AttributeTypeWrapper {
	public String fromDatabase(String databaseValue);
	public String fromDatabaseNumerical(Double databaseValue);
	public String toDatabase(String viewValue);
	public Double toDatabaseNumerical(String viewValue);

	public boolean isNumerical();
	
	public String prefix();
	public String suffix();
}
