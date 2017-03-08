package org.forpdi.core.user.authz.permission;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ViewUsersPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Visualizar Usuários";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.MANAGER.getLevel();
	}

	@Override
	public String getDescription() {
		return "Listar usuários, Consultar informações de um usuário, Enviar mensagem para um usuário";
	}
}
