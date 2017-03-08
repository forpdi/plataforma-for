package org.forpdi.planning.permissions;

import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permission;

public class ManagePlanPermission extends Permission {

	@Override
	public String getDisplayName() {
		return "Gerenciar Planos de Metas";
	}

	@Override
	public int getRequiredAccessLevel() {
		return AccessLevels.MANAGER.getLevel();
	}

	@Override
	public String getDescription() {
		return "Criar planos de meta, Criar instância dos níveis na árvore, Excluir instâncias dos níveis, Inserir valores em todos os "
				+ "níveis, Editar valores em todos os níveis, Consultar valores em todos os níveis, "
				+ "Atribuir responsáveis nos níveis, Gerar metas, Concluir metas";
	}
}
