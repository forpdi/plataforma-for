package org.forpdi.core.user.authz;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.forpdi.core.user.auth.UserSession;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.boilerplate.bean.Response;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.serialization.gson.GsonSerializerBuilder;

@Intercepts
@AcceptsWithAnnotations(Permissioned.class)
public class PermissionInterceptor implements Interceptor {

	@Inject private Result result;
	@Inject private HttpServletResponse response;
	@Inject private GsonSerializerBuilder gsonBuilder;
	@Inject private UserSession session;
	
	private void unauthorized() {
		this.response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		this.response.setContentType("application/json");
		try {
			gsonBuilder.create().toJson(
				new Response<Object>(false, "Você não tem permissão para acessar este recurso.",
					"401 Acesso não autorizado.", null), this.response.getWriter());
			this.response.getWriter().close();
		} catch (IOException ex) {}
		this.result.nothing();
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method, Object controllerInstance)
			throws InterceptionException {
		if (this.session.isLogged()) {
			Permissioned ann = method.getMethod().getAnnotation(Permissioned.class);
			if (ann.value().getLevel() > session.getAccessLevel()) {
				if (ann.permissions().length <= 0) {
					this.unauthorized();
				} else {
					for (Class<? extends Permission> perm : ann.permissions()) {
						if (session.getPermissions().contains(perm.getCanonicalName())) {
							stack.next(method, controllerInstance);
							return;
						}
					}
					this.unauthorized();
				}
			} else {
				stack.next(method, controllerInstance);
			}
		} else {
			this.unauthorized();
		}
	}

	@Override
	public boolean accepts(ControllerMethod method) {
		return method.containsAnnotation(Permissioned.class);
	}

}
