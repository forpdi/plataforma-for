package org.forrisco.core.plan.permissions;

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
		return "Criar plano de risco, Editar informações do plano de risco, Excluir plano de risco, Cadastrar itens e subitens de um plano de risco, "
				+ "Editar itens e subitens de um plano de risco, Excluir itens e subitens de um plano de risco";
	}
}
