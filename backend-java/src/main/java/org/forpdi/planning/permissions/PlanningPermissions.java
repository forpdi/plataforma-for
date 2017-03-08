package org.forpdi.planning.permissions;

import org.forpdi.core.user.authz.PermissionFactory;

public final class PlanningPermissions {
	// Registrando permissões do módulo de planejamento.
	static {
		PermissionFactory factory = PermissionFactory.getInstance();
		factory.register(new ManageStructurePermission());
		factory.register(new ManagePlanMacroPermission());
		factory.register(new ManageDocumentPermission());
		factory.register(new ManagePlanPermission());
		factory.register(new UpdateGoalPermission());
	}
}
