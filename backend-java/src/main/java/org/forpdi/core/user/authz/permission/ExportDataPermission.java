package org.forpdi.core.user.authz.permission;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ExportDataPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Exportar Dados";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {
		return "Permite o usuário exportar todos os dados dos planos da instituição em formato "
			+ "passível de restauração em outras instalações do sistema.";
	}
}
