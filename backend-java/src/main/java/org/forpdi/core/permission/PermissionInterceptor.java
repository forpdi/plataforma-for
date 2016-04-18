package org.forpdi.core.permission;

import javax.inject.Inject;

import org.forpdi.core.session.UserSession;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

@Intercepts
@AcceptsWithAnnotations(Permissioned.class)
public class PermissionInterceptor {

	@Inject private Result result;
	@Inject private UserSession session;
	
	@AroundCall
	public void intercept(SimpleInterceptorStack stack)
			throws InterceptionException {
		if (this.session.isLogged()) {
			stack.next();
		} else {
			this.result.use(Results.status()).forbidden("You don't have permission to access this resource.");
		}
	}

}
