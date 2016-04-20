package org.forpdi.core.user;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.bean.SessionInfo;
import org.forpdi.core.permission.Permissioned;
import org.forpdi.core.session.UserAccessToken;
import org.forpdi.core.session.UserSession;
import org.forpdi.system.IndexController;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
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
	public void save(@NotNull @Valid User user) {
		try {
			user.setId(null);
			this.bs.save(user);
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
	
	@Get("/api/user")
	@NoCache
	@Permissioned(UserRole.SYSTEM_ADMIN)
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
	@Permissioned(UserRole.SYSTEM_ADMIN)
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
