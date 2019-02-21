package org.forrisco.core.policy.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManagePlanRiskPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Plano de Risco";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Criar plano de risco, Editar informações do plano de risco, Duplicar plano de risco, Arquivar plano de risco";
	}
}
