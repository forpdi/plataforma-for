package org.forrisco.risk.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManageRiskPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Risco";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.MANAGER.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Criar riscos de unidades e subunidades, Editar informações de riscos de unidades e subunidades, "
				+ "Excluir riscos de unidades e subunidades, Cadastrar ações de prevenção dos riscos, Editar ações de prevenção dos riscos, Excluir ações de prevenção dos riscos";
	}
}
