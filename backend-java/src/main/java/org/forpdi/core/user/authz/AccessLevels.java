package org.forpdi.core.user.authz;

public enum AccessLevels {

	SYSTEM_ADMIN(100),
	COMPANY_ADMIN(50),
	MANAGER(30),
	COLABORATOR(15),
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
