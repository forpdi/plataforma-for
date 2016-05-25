package org.forpdi.core.user.authz;

import org.forpdi.core.abstractions.IdentifiableComponent;

public abstract class Permission extends IdentifiableComponent {

	public int getRequiredAccessLevel() {
		return AccessLevels.AUTHENTICATED.getLevel();
	}

}
