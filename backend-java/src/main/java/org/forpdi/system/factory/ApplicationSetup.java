package org.forpdi.system.factory;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

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
import org.forpdi.core.user.User;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.planning.fields.budget.BudgetElement;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;

import br.com.caelum.vraptor.boilerplate.HibernateDAO;
import br.com.caelum.vraptor.boilerplate.factory.SessionFactoryProducer;
import br.com.caelum.vraptor.boilerplate.factory.SessionManager;
import br.com.caelum.vraptor.boilerplate.util.CryptManager;
import br.com.caelum.vraptor.boilerplate.util.EmailUtils;
import br.com.caelum.vraptor.boilerplate.util.StoragerUtils;

@ApplicationScoped
@Startup
public class ApplicationSetup {

	private static final Logger LOG = Logger.getLogger(ApplicationSetup.class);

	protected ApplicationSetup() {
	}

	public void initializeAtStartup(@Observes ServletContext context) {

	}

	@Inject
	public ApplicationSetup(SessionFactoryProducer factoryProducer) {
		factoryProducer.initialize("hibernate.cfg.xml");
		
		StoragerUtils.setStoragerConfig("https://cloud.progolden.com.br/", 
				"11", "db4a598baf30adf87c97065a4735c4f40edf56ba9a514dfdc5424f1ba647b885");

		CryptManager.updateKey(SystemConfigs.getConfig("crypt.key"));
		CryptManager.updateSalt("@2b_A", "7-!x$");

		EmailUtils.setDefaultFrom("noreply@forpdi.org", "ForPDI");
		EmailUtils.setSmtpSettings(
			SystemConfigs.getConfig("smtp.host"),
			Integer.parseInt(SystemConfigs.getConfig("smtp.port")),
			SystemConfigs.getConfig("smtp.user"),
			SystemConfigs.getConfig("smtp.password"),
			"true".equals(SystemConfigs.getConfig("smtp.ssl"))
		);

		SessionManager mngr = new SessionManager(factoryProducer.getInstance());
		HibernateDAO dao = new HibernateDAO(mngr);
		LOG.info("Overwriting SSL context to ignore invalid certificates...");
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
		} catch (GeneralSecurityException ex) {
			System.out.println("N�o consegui sobrescrever o SSLContext.");
			ex.printStackTrace();
		}

		Criteria criteria = dao.newCriteria(User.class).add(Restrictions.eq("email", "admin@forpdi.org"));
		User user = (User) criteria.uniqueResult();
		if (user == null) {
			user = new User();
			user.setCpf("00000000000");
			user.setEmail("admin@forpdi.org");
			user.setName("Administrador ForPDI");
			user.setPassword(CryptManager.passwordHash("12345"));
			user.setAccessLevel(AccessLevels.SYSTEM_ADMIN.getLevel());
			dao.persist(user);
		}

		criteria = dao.newCriteria(User.class).add(Restrictions.eq("email", "gustavo@forpdi.org"));
		User user1 = (User) criteria.uniqueResult();
		if (user1 == null) {
			user1 = new User();
			user1.setCpf("11111111111");
			user1.setEmail("gustavo@forpdi.org");
			user1.setName("Gustavo Melo");
			user1.setPassword(CryptManager.passwordHash("12345"));
			user1.setAccessLevel(AccessLevels.COMPANY_ADMIN.getLevel());
			dao.persist(user1);
		}

		criteria = dao.newCriteria(User.class).add(Restrictions.eq("email", "everton@forpdi.org"));
		User user2 = (User) criteria.uniqueResult();
		if (user2 == null) {
			user2 = new User();
			user2.setCpf("22222222222");
			user2.setEmail("everton@forpdi.org");
			user2.setName("Everton Almeida");
			user2.setPassword(CryptManager.passwordHash("12345"));
			user2.setAccessLevel(AccessLevels.COMPANY_ADMIN.getLevel());
			dao.persist(user2);
		}

		criteria = dao.newCriteria(User.class).add(Restrictions.eq("email", "guilherme@forpdi.org"));
		User user3 = (User) criteria.uniqueResult();
		if (user3 == null) {
			user3 = new User();
			user3.setCpf("33333333333");
			user3.setEmail("guilherme@forpdi.org");
			user3.setName("Guilherme Henrique");
			user3.setPassword(CryptManager.passwordHash("12345"));
			user1.setAccessLevel(AccessLevels.COMPANY_ADMIN.getLevel());
			dao.persist(user3);
		}
		

		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String resourcesPath = classLoader.getResource("budget_simulation.txt").getFile();
			resourcesPath = resourcesPath.replace("%20", " ");
			File file = new File(resourcesPath);
			
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String subAction = line.split(" ")[0];
				criteria = dao.newCriteria(BudgetElement.class).add(Restrictions.eq("subAction", subAction));
				BudgetElement simulation = (BudgetElement) criteria.uniqueResult();
				if (simulation == null) {
					simulation = new BudgetElement();
					simulation.setSubAction(subAction);
				}
				simulation.setDeleted(false);

				Double planned = Double.valueOf(line.split(" ")[1]);
				Double committed = Double.valueOf(line.split(" ")[2]);
				Double conducted = Double.valueOf(line.split(" ")[3]);
				//simulation.setCommitted(planned);
				//simulation.setConducted(committed);
				//simulation.setPlanned(conducted);
				dao.persist(simulation);
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("Application setup completed.");
		mngr.closeSession();
	}

	public static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
