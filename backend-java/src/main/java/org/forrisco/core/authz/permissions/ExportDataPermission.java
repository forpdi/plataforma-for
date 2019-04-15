package org.forrisco.core.authz.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ExportDataPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Exportar Dados";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COLABORATOR.getLevel();
	}

	@Override
	public String getDescription() {
		return "Exportar dados dos planos de risco, Exportar dados de unidades, Exportar dados dos riscos, Exportar dados do painel de bordo";
	}
}
