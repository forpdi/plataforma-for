package org.forpdi.core.user;

import javax.inject.Inject;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.bean.SessionInfo;
import org.forpdi.core.user.auth.UserAccessToken;
import org.forpdi.core.user.auth.UserSession;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.system.IndexController;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.CryptManager;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class UserController extends AbstractController {
	@Inject private UserBS bs;
	@Inject private UserSession userSession;
	
	@Post("/api/user")
	@Consumes
	@NoCache
	@Permissioned
	public void save(@NotEmpty String name, @NotEmpty String email) {
		try {
			User user = this.bs.inviteUser(name, email);
			this.success(user);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Post("/api/user/profile")
	@Consumes
	@NoCache
	@Permissioned
	public void updateProfile(User user) {
		try {
			User existent = this.bs.exists(this.userSession.getUser().getId(), User.class);
			existent.setName(user.getName());
			existent.setCellphone(user.getCellphone());
			existent.setPhone(user.getPhone());
			existent.setDepartment(user.getDepartment());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Post("/api/user/login")
	@Consumes
	@NoCache
	public void login(
			@NotEmpty(message="email.notempty") String email,
			@NotEmpty(message="password.notempty") String password) {
		try {
			UserAccessToken token = this.bs.authenticate(email, password);
			if (token == null) {
				this.fail("E-mail e/ou senha inválido(s).");
			} else {
				this.userSession.login(token);
				this.success(new SessionInfo(this.userSession));
			}
		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Post("/api/user/logout")
	@NoCache
	public void logoutAjax() {
		try {
			// Ajax logout
			this.userSession.logout();
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Get("/api/user/logout")
	@NoCache
	public void logout() {
		try {
			this.userSession.logout();
			this.result.redirectTo(IndexController.class).index();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Get("/api/user/isLogged")
	@NoCache
	public void isLogged(){
		if (this.userSession.isLogged()) {
			this.success(this.userSession.getToken());
		} else {
			this.fail();
		}
	}

	@Get("/api/user/session")
	@NoCache
	public void fetchSession(){
		try {
			if (this.userSession.isLogged()) {
				this.success(new SessionInfo(this.userSession));
			} else {
				String auth = this.request.getHeader("Authorization");
				if (GeneralUtils.isEmpty(auth)) {
					this.fail("Not logged in.");
				} else {
					UserAccessToken token = this.bs.exists(auth, UserAccessToken.class);
					if (token == null) {
						this.fail("Invalid token.");
					} else {
						this.userSession.login(token);
						this.success(new SessionInfo(this.userSession));
					}
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Post("/api/user/recover")
	@Consumes
	@NoCache
	public void requestRecover(String email) {
		try {
			User user = this.bs.existsByEmail(email);
			if (user == null) {
				this.fail("Este e-mail não está cadastrado no sistema.");
			} else {
				UserRecoverRequest req = this.bs.requestRecover(user);
				LOGGER.debugf("Recover requested with token: %s", req.getToken());
				this.success();
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}

	@Post("/api/user/register/{token}")
	@Consumes("application/json")
	@NoCache
	public void registerUser(User user, String birthdate, String token) {
		try {
			User existent = this.bs.existsByInviteToken(token);
			if (GeneralUtils.isInvalid(existent)) {
				this.fail("Token de registro inválida.");
			} else {
				existent.setName(user.getName());
				existent.setCpf(user.getCpf());
				existent.setCellphone(user.getCellphone());
				existent.setPhone(user.getPhone());
				existent.setDepartment(user.getDepartment());
				existent.setBirthdate(GeneralUtils.parseDate(birthdate));
				existent.setPassword(CryptManager.passwordHash(user.getPassword()));
				existent.setActive(true);
				existent.setInviteToken(null);
				
				this.bs.persist(existent);
				this.success(existent);
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}

	@Get("/api/user/register/{token}")
	@NoCache
	public void canRegister(String token) {
		try {
			User user = this.bs.existsByInviteToken(token);
			if (user == null) {
				this.fail("Token de registro inválida.");
			} else {
				this.success();
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}
	
	@Post("/api/user/reset/{token}")
	@Consumes
	@NoCache
	public void resetUserPassword(String password, String token) {
		try {
			if (GeneralUtils.isEmpty(password)) {
				this.fail("A senha não pode ser vazia.");
			} else if (this.bs.resetPassword(password, token)) {
				this.success();
			} else {
				this.fail("Token de recuperação inválida ou expirada.");
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}

	@Get("/api/user/reset/{token}")
	@NoCache
	public void canReset(String token) {
		try {
			UserRecoverRequest req = this.bs.retrieveRecoverRequest(token);
			if (req == null) {
				this.fail("Token de recuperação inválida ou expirada.");
			} else {
				this.success();
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}
	
	@Get("/api/user")
	@NoCache
	@Permissioned(AccessLevels.COMPANY_ADMIN)
	public void listUsers(Integer page){
		if (page == null)
			page = 0;
		try {
			PaginatedList<User> users = this.bs.list(page);
			this.success(users.getList(), users.getTotal());
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}

	@Delete("/api/user/{id}")
	@NoCache
	@Permissioned(AccessLevels.COMPANY_ADMIN)
	public void blockUser(Long id){
		if (id == null) {
			this.result.notFound();
			return;
		}
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.result.notFound();
				return;
			}
			user.setDeleted(true);
			this.bs.persist(user);
			this.success();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: "+ex.getMessage());
		}
	}
	
}
