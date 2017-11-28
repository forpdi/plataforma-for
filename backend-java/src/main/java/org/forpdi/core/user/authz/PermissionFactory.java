package org.forpdi.core.user.authz;

import org.forpdi.core.abstractions.ComponentFactory;
import org.forpdi.core.user.authz.permission.EditMessagesPermission;
import org.forpdi.core.user.authz.permission.ManageUsersPermission;
import org.forpdi.core.user.authz.permission.ViewUsersPermission;
import org.forpdi.planning.permissions.ManageDocumentPermission;
import org.forpdi.planning.permissions.ManagePlanMacroPermission;
import org.forpdi.planning.permissions.ManagePlanPermission;
import org.forpdi.planning.permissions.UpdateGoalPermission;

public final class PermissionFactory extends ComponentFactory<Permission> {

	private static final PermissionFactory instance = new PermissionFactory();

	public static PermissionFactory getInstance() {
		return instance;
	}

	private PermissionFactory() {
		// this.register(new SystemAdminPermission());
		this.register(new ManageUsersPermission());
		this.register(new ViewUsersPermission());
		this.register(new EditMessagesPermission());
		// this.register(new ManageStructurePermission());
		// FIXME Isso não pode estar aqui, viola as dependências dos módulos.
		this.register(new ManagePlanMacroPermission());
		this.register(new ManageDocumentPermission());
		this.register(new ManagePlanPermission());
		this.register(new UpdateGoalPermission());
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
