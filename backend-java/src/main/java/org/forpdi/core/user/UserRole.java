package org.forpdi.core.user;

public enum UserRole {
	NORMAL(1),
	TEST_USER(2),
	SYSTEM_ADMIN(100);
	
	private UserRole(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	private final int accessLevel;

	public int getAccessLevel() {
		return accessLevel;
	}
}
