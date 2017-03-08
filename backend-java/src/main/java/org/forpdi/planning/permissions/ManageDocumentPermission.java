package org.forpdi.planning.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManageDocumentPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Documento";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.MANAGER.getLevel();
	}

	@Override

	public String getDescription() {		
		return "Criar novas seções e subseções, Excluir seções e subseções, Editar seções e subseções,"
				+ " Criar novos campos, Excluir campos, Editar campos, Inserir valores nos campos,"
				+ " Editar valores dos campos, Exportar o documento";
		}
}
