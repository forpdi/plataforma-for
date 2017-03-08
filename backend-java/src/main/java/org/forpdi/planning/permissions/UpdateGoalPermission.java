package org.forpdi.planning.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class UpdateGoalPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Atualizar Metas";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COLABORATOR.getLevel();
	}

	@Override
	public String getDescription() {		

		return "Inserir valor alcançado, Concluir meta (Colaborador: apenas se for o responsável pela meta)";

	}
}
