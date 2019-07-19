package org.forrisco.core.unit.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class EditUnitPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Editar Unidades";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.MANAGER.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Editar informações de unidade e subunidade";
	}
}
