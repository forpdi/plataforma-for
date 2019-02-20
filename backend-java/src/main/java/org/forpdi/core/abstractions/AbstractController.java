package org.forpdi.core.abstractions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.forpdi.core.user.auth.UserSession;

/**
 * Especialização da classe abstrata para controladores com o objetivo de fornecer um prefixo
 * para os end points REST e injetar o objeto de sessão de usuário automaticamente nos
 * controladores do sistema. Todos os controladores devem estender dessa classe.
 * 
 * @author Renato R. R. de Oliveira <renatorro@comp.ufla.br>
 *
 */
public abstract class AbstractController extends br.com.caelum.vraptor.boilerplate.AbstractController {

	/** Prefixo dos end points REST. */
	protected static final String BASEPATH = "/api";
	
	/** Objecto da sessão do usuário. Injetado automaticamente. */
	@Inject protected UserSession userSession;
	
	@Inject protected HttpServletResponse httpResponse;
}
