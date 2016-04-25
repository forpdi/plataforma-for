package org.forpdi.core.company;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

public abstract class CompanyTheme {

	public abstract String getDisplayName();
	public abstract String getCSSFile();

	public String getId() {
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
		CompanyTheme other = (CompanyTheme) obj;
		if (GeneralUtils.isEmpty(this.getId())) {
			throw new IllegalArgumentException("A company theme must have a non-empty ID.");
		}
		return this.getId().equals(other.getId());
	}
	
}
