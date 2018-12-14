package org.forrisco.core.policy.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManagePlanRiskPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Política";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Criar política, Editar informações da política, Duplicar política, Arquivar política";
	}
}
