package org.forpdi.system.factory;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletContext;

import org.forpdi.core.properties.SystemConfigs;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.factory.SessionFactoryProducer;
import br.com.caelum.vraptor.boilerplate.factory.SessionManager;
import br.com.caelum.vraptor.boilerplate.util.CryptManager;
import br.com.caelum.vraptor.boilerplate.util.EmailUtils;


@ApplicationScoped
@Startup
public class ApplicationSetup {

	private static final Logger LOG = Logger.getLogger(ApplicationSetup.class);
	
	protected ApplicationSetup() {}
	
	public void initializeAtStartup(@Observes ServletContext context) {
		
	}
	
	@Inject
	public ApplicationSetup(SessionFactoryProducer factoryProducer) {
		factoryProducer.initialize("hibernate.cfg.xml");
		
		CryptManager.updateKey(SystemConfigs.getConfig("crypt.key"));
		CryptManager.updateSalt("@2b_A", "7-!x$");
		
		EmailUtils.setDefaultFrom("noreply@forpdi.org", "ForPDI");
		// EmailUtils.setSmtpSettings(host, port, user, password, ssl);
		
		SessionManager mngr = new SessionManager(factoryProducer.getInstance());
		HibernateDAO dao = new HibernateDAO(mngr);
		LOG.info("Overwriting SSL context to ignore invalid certificates...");
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0],
					new TrustManager[] { new DefaultTrustManager() },
					new SecureRandom());
			SSLContext.setDefault(ctx);
		} catch (GeneralSecurityException ex) {
			System.out.println("N�o consegui sobrescrever o SSLContext.");
			ex.printStackTrace();
		}
		
		LOG.info("Application setup completed.");
		mngr.closeSession();
	}

	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
