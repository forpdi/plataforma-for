package org.forpdi.core.user.authz;

import java.util.LinkedList;

public final class PermissionFactory {

	private static final PermissionFactory instance = new PermissionFactory();
	
	public static PermissionFactory getInstance() {
		return instance;
	}
	
	private final LinkedList<Permission> permissions = new LinkedList<Permission>();
	
	private PermissionFactory() {
		this.permissions.add(new SystemAdminPermission());
	}
	
	public int register(Permission perm) {
		if (perm == null) {
			throw new IllegalArgumentException("Null permission object passed.");
		}
		if (permissions.contains(perm)) {
			throw new IllegalArgumentException("Duplicate permission registering: "+perm.getId());
		}
		this.permissions.add(perm);
		return this.permissions.size()-1;
	}
	
	public Permission getPermission(int index) {
		return this.permissions.get(index);
	}
	
	public Permission getPermission(String permId) {
		for (Permission perm : permissions) {
			if (perm.getId().equals(permId)) {
				return perm;
			}
		}
		return null;
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
