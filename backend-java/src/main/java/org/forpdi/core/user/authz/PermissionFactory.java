package org.forpdi.core.user.authz;

import org.forpdi.core.abstractions.ComponentFactory;

public final class PermissionFactory extends ComponentFactory<Permission> {

	private static final PermissionFactory instance = new PermissionFactory();
	
	public static PermissionFactory getInstance() {
		return instance;
	}
	
	private PermissionFactory() {
		this.register(new SystemAdminPermission());
	}
	
	public class SystemAdminPermission extends Permission {
		@Override
		public String getDisplayName() {
			return "Administração de Sistema";
		}
		@Override
		public int getRequiredAccessLevel() {
			return AccessLevels.SYSTEM_ADMIN.getLevel();
		}
	}
}
