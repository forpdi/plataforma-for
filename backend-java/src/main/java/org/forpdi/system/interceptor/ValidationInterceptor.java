package org.forpdi.system.interceptor;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.forpdi.system.IndexController;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.validator.Validator;

/**
 * Interceptor that redirects all validation errors to JSON response page.
 * @author Renato R. R. de Oliveira
 *
 */
@RequestScoped
public class ValidationInterceptor {

	private static final Logger LOG = Logger.getLogger(ValidationInterceptor.class);
	
	@Inject private Validator validator;
	
	public void intercept(@Observes br.com.caelum.vraptor.events.MethodReady event) {
		LOG.debug("Redirecting if there are validation errors.");
		this.validator.onErrorForwardTo(IndexController.class).errors();
	}
	
}
