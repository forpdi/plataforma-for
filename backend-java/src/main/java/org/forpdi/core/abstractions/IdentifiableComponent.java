package org.forpdi.core.abstractions;

import java.util.UUID;

import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * Classe para abstrair componentes que serão gerenciados por uma ComponentFactory.
 * @author Renato R. R. de Oliveira &lt;renatorro@comp.ufla.br&gt;
 *
 */
public abstract class IdentifiableComponent {

	protected final String id;
	
	protected IdentifiableComponent() {
		String id = generateId(this.getClass());
		if (GeneralUtils.isEmpty(id))
			this.id = UUID.randomUUID().toString();
		else
			this.id = id;
	}
	
	public static String generateId(Class<?> clazz) {
		return clazz.getCanonicalName();
	}
	
	public abstract String getDisplayName();
	
	public final String getId() {
		return this.id;
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		IdentifiableComponent other = (IdentifiableComponent) obj;
		if (GeneralUtils.isEmpty(this.getId())) {
			throw new IllegalArgumentException("A company theme must have a non-empty ID.");
		}
		return this.getId().equals(other.getId());
	}
	
}
