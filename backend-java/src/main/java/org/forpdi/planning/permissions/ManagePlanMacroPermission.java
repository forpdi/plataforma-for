package org.forpdi.planning.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManagePlanMacroPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Planos";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Criar plano macro, Editar informações do plano macro, Duplicar plano macro, Arquivar plano macro";
	}
}
