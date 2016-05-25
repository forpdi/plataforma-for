package org.forpdi.system;

import java.io.IOException;

import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyBS;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyThemeFactory;
import org.forpdi.core.properties.CoreMessages;
import org.forpdi.core.properties.SystemConfigs;

import com.google.gson.Gson;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.validator.MessageList;

@Controller
public class IndexController extends AbstractController {
	
	@Inject private CompanyBS companyBS;
	
	@Path(value="/", priority=Path.HIGHEST)
	@NoCache
	public void index() {
		
	}
	
	@Path(value="/validationerrors",priority=Path.LOWEST)
	public void errors() {
		MessageList errors = (MessageList) result.included().get("errors");
		this.fail(errors);
	}
	
	@Get("/environment")
	public void envInfo() {
		StringBuilder body = new StringBuilder();
		CompanyDomain domain = this.companyBS.currentDomain();
		Gson gson = this.gsonBuilder.create();
		CoreMessages msg = new CoreMessages(CoreMessages.DEFAULT_LOCALE);
		
		body.append("EnvInfo={");
		body.append("'baseUrl': '").append(SystemConfigs.getConfig("sys.baseurl")).append("'");
		body.append(",'themes': ").append(CompanyThemeFactory.getInstance().toJSON());
		if (domain == null) {
			body.append(",'company': null");
			body.append(",'themeCss': '")
				.append(CompanyThemeFactory.getDefaultTheme().getCSSFile())
				.append("'");
		} else {
			body.append(",'company': ").append(gson.toJson(domain.getCompany()));
			body.append(",'themeCss': '")
				.append(CompanyThemeFactory.getInstance().get(domain.getTheme()).getCSSFile())
				.append("'");
		}
		body.append(",'messages':").append(msg.getJSONMessages());
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
