package org.forpdi.core.user.authz.permission;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class RestoreDataPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Restaurar/Importar Dados";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {
		return "Permite o usuário restaurar todos os dados dos planos de uma instituição a partir de um "
			+ "arquivo exportado do próprio sistema.";
	}
}
