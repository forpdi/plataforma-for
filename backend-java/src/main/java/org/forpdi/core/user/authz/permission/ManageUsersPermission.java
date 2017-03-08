package org.forpdi.core.user.authz.permission;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManageUsersPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Usuários";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.COMPANY_ADMIN.getLevel();
	}

	@Override
	public String getDescription() {
		
		return "Cadastrar usuários, Listar usuários, Excluir usuário, Bloquear Usuários,"
				+ " Consultar informações do usuário, Editar usuário, Alterar permissões locais do usuário, Importar usuários";
	}
}
