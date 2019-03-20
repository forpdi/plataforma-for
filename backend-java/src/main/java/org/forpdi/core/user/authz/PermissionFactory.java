package org.forpdi.core.user.authz;

import org.forpdi.core.abstractions.ComponentFactory;
import org.forpdi.core.user.authz.permission.EditMessagesPermission;
import org.forpdi.core.user.authz.permission.ExportDataPermission;
import org.forpdi.core.user.authz.permission.ManageUsersPermission;
import org.forpdi.core.user.authz.permission.RestoreDataPermission;
import org.forpdi.core.user.authz.permission.ViewUsersPermission;
import org.forpdi.planning.permissions.ManageDocumentPermission;
import org.forpdi.planning.permissions.ManagePlanMacroPermission;
import org.forpdi.planning.permissions.ManagePlanPermission;
import org.forpdi.planning.permissions.UpdateGoalPermission;
import org.forrisco.core.plan.permissions.ManagePlanRiskPermission;
import org.forrisco.core.policy.permissions.ManagePolicyPermission;
import org.forrisco.core.process.ManageProcessPermission;
import org.forrisco.core.unit.permissions.EditUnitPermission;
import org.forrisco.core.unit.permissions.ManageUnitPermission;
import org.forrisco.risk.permissions.ManageRiskItemsPermission;
import org.forrisco.risk.permissions.ManageRiskPermission;

public final class PermissionFactory extends ComponentFactory<Permission> {

	private static final PermissionFactory instance = new PermissionFactory();

	public static PermissionFactory getInstance() {
		return instance;
	}

	private PermissionFactory() {
		// forpdi permissions
		// this.register(new SystemAdminPermission());
		
		// forpdi permissions
		this.register(new ManageUsersPermission());
		this.register(new ViewUsersPermission());
		this.register(new EditMessagesPermission());
		// this.register(new ManageStructurePermission());
		// FIXME Isso não pode estar aqui, viola as dependências dos módulos.
		this.register(new ExportDataPermission());
		this.register(new RestoreDataPermission());
		this.register(new ManagePlanMacroPermission());
		this.register(new ManageDocumentPermission());
		this.register(new ManagePlanPermission());
		this.register(new UpdateGoalPermission());
		
		// forrisco permissions
		this.register(new org.forrisco.core.authz.permissions.ManageUsersPermission());
		this.register(new org.forrisco.core.authz.permissions.ViewUsersPermission());
		this.register(new org.forrisco.core.authz.permissions.EditMessagesPermission());
		this.register(new org.forrisco.core.authz.permissions.ExportDataPermission());
		this.register(new ManagePolicyPermission());
		this.register(new ManagePlanRiskPermission());
		this.register(new ManageUnitPermission());
		this.register(new EditUnitPermission());
		this.register(new ManageProcessPermission());
		this.register(new ManageRiskPermission());
		this.register(new ManageRiskItemsPermission());
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

		@Override
		public String getDescription() {
			return "Adicionar Instituições, Editar Instituições, Remover Instituições, Adicionar Domínios,"
					+ "Editar Domínios, Remover Domínios";
		}
	}
}
