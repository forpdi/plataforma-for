package org.forrisco.core.process.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManageProcessPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Processos";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.MANAGER.getLevel();
	}

	@Override
	public String getDescription() {		
		return "Cadastrar processos em unidade e subunidade, Editar processos em unidade e subunidade, Excluir processos em unidade e subunidade";
	}
}
