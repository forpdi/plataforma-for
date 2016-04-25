package org.forpdi.core.permission;

public enum AccessLevels {

	SYSTEM_ADMIN(100),
	COMPANY_ADMIN(50),
	COLABORATOR(20),
	AUDITOR(10),
	AUTHENTICATED(5),
	NONE(0);
	
	private final int level;
	
	private AccessLevels(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}
