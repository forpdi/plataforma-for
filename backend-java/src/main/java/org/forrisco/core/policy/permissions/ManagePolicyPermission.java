package org.forrisco.core.policy.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManagePolicyPermission extends Permission {

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
		return "Criar política, Editar informações da política, Excluir política, Cadastrar itens e subitens de uma política, "
				+ "Editar itens e subitens de uma política, Excluir itens e subitens de uma política";
	}
}
