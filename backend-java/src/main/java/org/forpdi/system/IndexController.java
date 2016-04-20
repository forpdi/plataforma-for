package org.forpdi.system;

import java.io.IOException;

import com.google.gson.Gson;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.validator.MessageList;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.properties.SystemConfigs;

@Controller
public class IndexController extends AbstractController {
	
	@Path(value="/", priority=Path.HIGHEST)
	@NoCache
	public void index() {
		
	}
	
	@Path(value="/validationerrors",priority=Path.LOWEST)
	public void errors() {
		MessageList errors = (MessageList) result.included().get("errors");
		this.fail(errors);
	}
	
	@Get("/api/session")
	public void sessionInfo() {
		StringBuilder body = new StringBuilder();
		body.append("SessionInfo={");
		if (this.userSession.isLogged()) {
			Gson gson = this.gsonBuilder.create();
			String json = gson.toJson(this.userSession.getUser());
			body
				.append("'user': ")
				.append(json)
			;
		} else {
			body.append("'user': undefined");
		}
		body.append(",'baseUrl': '").append(SystemConfigs.getConfig("sys.baseurl")).append("'");
		body.append("};");
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.addHeader("Content-Type", "text/javascript"); 
			this.response.getWriter().print(body.toString());
		} catch (IOException ex) {
			LOGGER.error("Unexpected runtime error", ex);
		}
		this.result.nothing();
	}
}
