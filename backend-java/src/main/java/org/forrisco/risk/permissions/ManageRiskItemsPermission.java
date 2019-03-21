package org.forrisco.risk.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManageRiskItemsPermission extends Permission {
	@Override
	public String getDisplayName() {
		return "Gerenciar Monitoramento, Incidentes e Contingenciamento";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COLABORATOR.getLevel();
	}

	@Override
	public String getDescription() {
		return "Cadastrar monitorimento, Editar monitoramento, Excluir monitoramento, Cadastrar incidentes, Editar incidentes, "
				+ "Excluir incidentes, Cadastrar contingenciamento, Editar contingenciamento, Excluir contingenciamento";
	}
}
