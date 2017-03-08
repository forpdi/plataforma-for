package org.forpdi.core.company;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.forpdi.core.event.Current;

/**
 * 
 * @author Renato R. R. de Oliveira
 *
 */
@RequestScoped
public class CurrentDomainProducer {

	@Inject private CompanyBS bs;
	private CompanyDomain domain;
	
	@Produces
	@Current
	public CompanyDomain getInstance() {
		if (domain == null) {
			domain = bs.currentDomain();
		}
		return domain;
	}
}
