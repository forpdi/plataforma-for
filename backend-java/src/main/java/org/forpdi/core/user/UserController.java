package org.forpdi.core.user;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.permission.Permissioned;
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

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class UserController extends AbstractController {
	@Inject private UserBS bs;
	@Inject private UserSession userSession;
	
	@Post("/user")
	@Consumes
	@NoCache
	public void save(@NotNull @Valid User user) {
		try {
			user.setId(null);
			this.bs.save(user);
			this.userSession.login(user);
			this.success(user);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}

	@Post("/user/profile")
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
			this.userSession.login(existent);
			this.success(existent);
		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Post("/user/login")
	@Consumes
	@NoCache
	public void login(
			@NotEmpty(message="email.notempty") String email,
			@NotEmpty(message="password.notempty") String password) {
		try {
			User user = this.bs.existsByEmailAndPassword(email, password);
			if (user == null) {
				this.fail("E-mail e/ou senha inválido(s).");
			} else {
				this.userSession.login(user);
				this.success(user);
			}
		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Post("/user/logout")
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
	
	@Get("/user/logout")
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
	
	@Get("/user/isLogged")
	@NoCache
	public void isLogged(){
		if (this.userSession.getUser() != null) {
			this.success(this.userSession.getUser());
		} else {
			this.fail();
		}
	}

	@Get("/user/session")
	@NoCache
	public void fetchSession(){
		try {
			this.success(this.userSession.getUser());
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: "+e.getMessage());
		}
	}
	
	@Get("/user")
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

	@Delete("/user/{id}")
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
