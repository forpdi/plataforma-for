package org.forrisco.core.unit.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManageUnitPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Unidades";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Criar unidade e subunidade, Editar informações de unidade e subunidade, Excluir unidade e subunidade";
	}
}
