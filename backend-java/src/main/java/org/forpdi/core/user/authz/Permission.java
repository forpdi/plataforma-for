package org.forpdi.core.user.authz;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

public abstract class Permission {

	public abstract String getDisplayName();
	
	public int getRequiredAccessLevel() {
		return AccessLevels.AUTHENTICATED.getLevel();
	}

	public final String getId() {
		return this.getClass().getCanonicalName();
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Permission other = (Permission) obj;
		if (GeneralUtils.isEmpty(this.getId())) {
			throw new IllegalArgumentException("A company theme must have a non-empty ID.");
		}
		return this.getId().equals(other.getId());
	}
	
}
