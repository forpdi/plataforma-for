package org.forpdi.core.user;

import java.net.UnknownHostException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.apache.commons.mail.EmailException;
import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.bean.SessionInfo;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.company.CompanyUser;
import org.forpdi.core.event.Current;
import org.forpdi.core.notification.NotificationBS;
import org.forpdi.core.notification.NotificationSetting;
import org.forpdi.core.notification.NotificationType;
import org.forpdi.core.user.auth.UserAccessToken;
import org.forpdi.core.user.auth.UserSession;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forpdi.core.user.authz.UserPermission;
import org.forpdi.core.user.authz.permission.ManageUsersPermission;
import org.forpdi.core.user.authz.permission.ViewUsersPermission;
import org.forpdi.planning.permissions.PermissionDTO;
import org.forpdi.planning.structure.StructureBS;
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
import br.com.caelum.vraptor.boilerplate.util.StoragerUtils;

/**
 * @author Renato R. R. de Oliveira
 */
@Controller
public class UserController extends AbstractController {

	@Inject
	@Current
	private CompanyDomain domain;
	@Inject
	private UserBS bs;
	@Inject
	private StructureBS sbs;
	@Inject
	private UserSession userSession;
	@Inject
	private HttpServletRequest request;
	@Inject
	private NotificationBS notificationBS;

	/**
	 * Salvar um usuário
	 * 
	 * @param name
	 *            Nome do usuário.
	 * @param email
	 *            Email do usuário.
	 * @param accessLevel
	 *            Nível de acesso do usuário.
	 * @return User Usuário salvo
	 */
	@Post("/api/user")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void save(@NotEmpty String name, @NotEmpty String email, Integer accessLevel) {
		try {
			/* int actualAccess = this.bs.retrieveAccessLevel(this.userSession.getUser());
				if (accessLevel > actualAccess) {
					this.forbidden();
					return;
				}
			*/
			User user = this.bs.inviteUser(name, email, accessLevel);

			this.success(user);
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("E-mail do usuário já foi cadastrado!");
		}
	}

	/**
	 * Atualizar um determinado campo do usuário.
	 * 
	 * @param id
	 *            Id do usuário que se deseja atualizar o campo.
	 * @param field
	 *            Campo que se deseja atualizar.
	 * @param value
	 *            Novo valor do campo.
	 * 
	 * @return void
	 */
	@Post("/api/user/{id}/update/field")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void updateUserField(@NotNull Long id, String field, String value) {
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.result.notFound();
				return;
			}

			if ("name".equals(field)) {
				user.setName(value);
			} else if ("department".equals(field)) {
				user.setDepartment(value);
			} else if ("phone".equals(field)) {
				user.setPhone(value);
			} else if ("cellphone".equals(field)) {
				user.setCellphone(value);
			} else if ("birthdate".equals(field)) {
				user.setBirthdate(GeneralUtils.parseDate(value));
			} else if ("accessLevel".equals(field)) {
				CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
				if (companyUser == null) {
					this.result.notFound();
					return;
				}
				companyUser.setAccessLevel(Integer.parseInt(value));
				this.bs.persist(companyUser);
			} else {
				this.fail("Campo inválido: " + field);
			}
			this.bs.persist(user);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Reenviar convite para usuário acessar o sistema.
	 * 
	 * @param id
	 *            Id do usuário.
	 * @return Mensagem de feedback.
	 */
	@Post("/api/user/{id}/reinvite")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void resendInvitation(@NotNull Long id) {
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.result.notFound();
				return;
			}
			this.bs.sendInvitationEmail(user);
			this.success("Convite reenviado com sucesso.");
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("E-mail do usuário já foi cadastrado!");
		}
	}

	/**
	 * Update em todos os campos do perfil do usuário.
	 * 
	 * @param user
	 *            Usuário que terá seus campos atualizados.
	 * @param passwordCurrent
	 *            Password atual do usuário.
	 * @param passwordNew
	 *            Novo passdoword do usuário.
	 * @param passwordNewTo
	 *            Confirmação do novo password.
	 * @return User Usuário com todos os campos do perfil atualizado.
	 */
	@Post("/api/user/profile")
	@Consumes
	@NoCache
	@Permissioned
	public void updateProfile(User user, String passwordCurrent, String passwordNew, String passwordNewTo) {

		User userRetrieveCpf = null;
		User userRetrieveEmail = null;
		User userRetrieveCellphone = null;

		userRetrieveCpf = bs.existsByCpf(user.getCpf());
		userRetrieveEmail = bs.existsByEmail(user.getEmail());
		userRetrieveCellphone = bs.existsByCellphone(user.getCellphone());

		if (userRetrieveCpf == null) {
			userRetrieveCpf = new User();
			userRetrieveCpf.setId(user.getId());
			userRetrieveCpf.setCpf("");
		}
		if (userRetrieveEmail == null) {
			userRetrieveEmail = new User();
			userRetrieveEmail.setEmail("");
			userRetrieveEmail.setId(user.getId());
		}

		if (userRetrieveCellphone == null) {
			userRetrieveCellphone = new User();
			userRetrieveCellphone.setCellphone("");
			userRetrieveCellphone.setId(user.getId());
		}

		if (userRetrieveCpf.getCpf().equals(user.getCpf()) || userRetrieveEmail.getEmail().equals(user.getEmail())
				|| userRetrieveCellphone.getCellphone().equals(user.getCellphone())) {

			if (userRetrieveCpf.getId() == user.getId() && userRetrieveEmail.getId() == user.getId()
					&& userRetrieveCellphone.getId() == user.getId()) {
				try {
					User existent = this.bs.exists(this.userSession.getUser().getId(), User.class);

					existent.setName(user.getName());
					existent.setCellphone(user.getCellphone());
					existent.setPhone(user.getPhone());
					existent.setDepartment(user.getDepartment());
					// alguns campos que podem ser editados
					existent.setCpf(user.getCpf());
					existent.setBirthdate(user.getBirthdate());
					existent.setEmail(user.getEmail());

					if (passwordCurrent != null && passwordNew != null && passwordNewTo != null) {
						User userExist = this.bs.authenticate(user.getEmail(), passwordCurrent);
						if (userExist != null) {
							if (passwordNew.equals(passwordNewTo)) {
								this.bs.changePassword(passwordNew, existent);
								this.success(existent);
							} else {
								this.fail("Você deve inserir a mesma senha duas vezes para confirmá-la.");
							}

						} else {
							this.fail("Sua senha atual está incorreta");
						}

					} else {
						this.bs.persist(existent);
						this.success(existent);
					}

				} catch (Throwable e) {
					LOGGER.error("Erro no login.", e);
					this.fail("Ocorreu um erro inesperado: " + e.getMessage());
				}

			} else {

				this.fail("Verifique se os campos E-MAIL, CPF ou CELULAR já estão cadastrados no sistema");

			}

		} else {
			try {
				User existent = this.bs.exists(this.userSession.getUser().getId(), User.class);
				String emailUser = existent.getEmail();
				existent.setName(user.getName());
				existent.setCellphone(user.getCellphone());
				existent.setPhone(user.getCellphone());
				existent.setDepartment(user.getDepartment());

				// alguns campos que podem ser editados
				existent.setCpf(user.getCpf());
				existent.setBirthdate(user.getBirthdate());
				existent.setEmail(user.getEmail());

				if (passwordCurrent != null && passwordNew != null && passwordNewTo != null) {
					User userExist = this.bs.authenticate(emailUser, passwordCurrent);
					if (userExist != null) {
						if (passwordNew.equals(passwordNewTo)) {
							this.bs.changePassword(passwordNew, existent);
							this.success(existent);
						} else {
							this.fail("Você deve inserir a mesma senha duas vezes para confirmá-la.");
						}

					} else {
						this.fail("Sua senha atual está incorreta");
					}

				} else {
					this.bs.persist(existent);
					this.success(existent);
				}

			} catch (Throwable e) {
				LOGGER.error("Erro no login.", e);
				this.fail("Ocorreu um erro inesperado: " + e.getMessage());
			}

		}

	}

	/**
	 * Usuário do tipo administrador realiza update em todos os campos de um
	 * usuário.
	 * 
	 * @param user
	 *            Usuário que terá seus campos atualizados.
	 * @param passwordNew
	 *            Novo password do usuário.
	 * @return User Usuário com todos os campos atualizados.
	 */
	@Post("/api/user/editUser")
	@Consumes
	@NoCache
	@Permissioned
	public void editUser(User user, String passwordNew) {
		boolean changeAccessLevel = false;
		User userRetrieveCpf = null;
		User userRetrieveEmail = null;
		User userRetrieveCellphone = null;

		userRetrieveCpf = bs.existsByCpf(user.getCpf());
		userRetrieveEmail = bs.existsByEmail(user.getEmail());
		userRetrieveCellphone = bs.existsByCellphone(user.getCellphone());

		if (userRetrieveCpf == null) {
			userRetrieveCpf = new User();
			userRetrieveCpf.setId(user.getId());
			userRetrieveCpf.setCpf("");
		}
		if (userRetrieveEmail == null) {
			userRetrieveEmail = new User();
			userRetrieveEmail.setEmail("");
			userRetrieveEmail.setId(user.getId());
		}

		if (userRetrieveCellphone == null) {
			userRetrieveCellphone = new User();
			userRetrieveCellphone.setCellphone("");
			userRetrieveCellphone.setId(user.getId());
		}

		if (userRetrieveCpf.getCpf().equals(user.getCpf()) || userRetrieveEmail.getEmail().equals(user.getEmail())
				|| userRetrieveCellphone.getCellphone().equals(user.getCellphone())) {

			if (userRetrieveCpf.getId() == user.getId() && userRetrieveEmail.getId() == user.getId()
					&& userRetrieveCellphone.getId() == user.getId()) {
				try {
					User existent = this.bs.existsByUser(user.getId());
					CompanyUser companyUser = this.bs.retrieveCompanyUser(existent, this.domain.getCompany());

					if (this.userSession.getAccessLevel() == AccessLevels.SYSTEM_ADMIN.getLevel() || this.userSession.getUser().getId() == existent.getId()) {
						existent.setEmail(user.getEmail());
						existent.setCpf(user.getCpf());
					}
					existent.setName(user.getName());
					existent.setBirthdate(user.getBirthdate());
					existent.setCellphone(user.getCellphone());
					existent.setPhone(user.getPhone());
					existent.setDepartment(user.getDepartment());
					existent.setAccessLevel(user.getAccessLevel());

					if (companyUser == null) {
						companyUser = new CompanyUser();
						companyUser.setUser(existent);
						companyUser.setCompany(this.domain.getCompany());
					}
					if (companyUser.getAccessLevel() != user.getAccessLevel())
						changeAccessLevel = true;
					companyUser.setAccessLevel(user.getAccessLevel());

					if (passwordNew != null && ("".equals(passwordNew.trim()) == false)) {
						this.bs.changePassword(passwordNew, existent);
					} else {
						this.bs.persist(existent);
					}
					this.bs.persist(companyUser);
					if (changeAccessLevel) {
						String url = domain.getBaseUrl() + "/#/users/profilerUser/" + user.getId();
						if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting()
								|| companyUser.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL
										.getSetting()) {
							this.notificationBS.sendNotification(NotificationType.ACCESSLEVEL_CHANGED,
									this.notificationBS.getAccessLevelText(companyUser), null, existent.getId(), url);
							this.notificationBS.sendNotificationEmail(NotificationType.ACCESSLEVEL_CHANGED,
									this.notificationBS.getAccessLevelText(companyUser), "", existent, url);
						} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
								.getSetting()) {
							this.notificationBS.sendNotification(NotificationType.ACCESSLEVEL_CHANGED,
									this.notificationBS.getAccessLevelText(companyUser), null, existent.getId(), url);

						}
					}
					this.success(existent);

				} catch (Throwable e) {
					LOGGER.error("Erro no login.", e);
					this.fail("Ocorreu um erro inesperado: " + e.getMessage());
				}

			} else {

				this.fail("Verifique se os campos E-MAIL, CPF ou CELULAR já estão cadastrados no sistema");

			}

		} else {

			try {
				User existent = this.bs.existsByUser(user.getId());
				CompanyUser companyUser = this.bs.retrieveCompanyUser(existent, this.domain.getCompany());

				existent.setName(user.getName());
				existent.setEmail(user.getEmail());
				existent.setCpf(user.getCpf());
				existent.setBirthdate(user.getBirthdate());
				existent.setCellphone(user.getCellphone());
				existent.setPhone(user.getPhone());
				existent.setDepartment(user.getDepartment());
				existent.setAccessLevel(user.getAccessLevel());
				this.bs.persist(existent);

				if (companyUser == null) {
					companyUser = new CompanyUser();
					companyUser.setUser(existent);
					companyUser.setCompany(this.domain.getCompany());
				}
				if (companyUser.getAccessLevel() != user.getAccessLevel())
					changeAccessLevel = true;
				companyUser.setAccessLevel(user.getAccessLevel());
				if (passwordNew != null && ("".equals(passwordNew.trim()) == false)) {
					this.bs.changePassword(passwordNew, existent);
				} else {
					this.bs.persist(existent);
				}
				this.bs.persist(companyUser);
				if (changeAccessLevel) {
					String url = domain.getBaseUrl() + "/#/users/profilerUser/" + user.getId();
					if (companyUser.getNotificationSetting() == NotificationSetting.DEFAULT.getSetting() || companyUser
							.getNotificationSetting() == NotificationSetting.RECEIVE_ALL_BY_EMAIL.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.ACCESSLEVEL_CHANGED,
								this.notificationBS.getAccessLevelText(companyUser), null, existent.getId(), url);
						this.notificationBS.sendNotificationEmail(NotificationType.ACCESSLEVEL_CHANGED,
								this.notificationBS.getAccessLevelText(companyUser), "", existent, url);
					} else if (companyUser.getNotificationSetting() == NotificationSetting.DO_NOT_RECEIVE_EMAIL
							.getSetting()) {
						this.notificationBS.sendNotification(NotificationType.ACCESSLEVEL_CHANGED,
								this.notificationBS.getAccessLevelText(companyUser), null, existent.getId(), url);
					}
				}
				this.success(existent);

			} catch (Throwable e) {
				LOGGER.error("Erro no login.", e);
				this.fail("Ocorreu um erro inesperado: " + e.getMessage());
			}

		}

	}

	/**
	 * Atualizar a imagem de perfil do usuário.
	 * 
	 * @param user
	 *            Usuário que terá sua imagem de perfil atualizada.
	 * @param url
	 *            Url que contém a foto de perfil do usuário.
	 * @return User Usuário com a foto de perfil atualizada.
	 */
	@Post("/api/user/picture")
	@Consumes
	@NoCache
	@Permissioned
	public void updatePictureUser(User user, String url) {

		try {
			User existent = this.bs.exists(this.userSession.getUser().getId(), User.class);
			if (url == null) {
				this.fail("Imagem Inválida.");
			} else {
				existent.setPicture(url);
				this.bs.persist(existent);
				this.success(existent);
			}

		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Usuário do tipo administrador realiza update na foto de perfil de um
	 * usuário.
	 * 
	 * @param user
	 *            Usuário que terá a foto de perfil atualizada.
	 * @param url
	 *            Url que contém a foto do perfil do usuário.
	 */
	@Post("/api/user/pictureEditUser")
	@Consumes
	@NoCache
	@Permissioned
	public void updatePictureEditUser(User user, String url) {
		try {
			User existent = this.bs.existsByUser(user.getId());
			if (url == null) {
				this.fail("Imagem Inválida.");
			} else {
				existent.setPicture(url);
				this.bs.persist(existent);
				this.success(existent);
			}

		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Realiza login do usuário.
	 * 
	 * @param email
	 *            Email do usuário.
	 * @param password
	 *            Password do usuário.
	 * @return SessionInfo Nova sessão com o usuário
	 */
	@Post("/api/user/login")
	@Consumes
	@NoCache
	public void login(@NotEmpty(message = "email.notempty") String email,
			@NotEmpty(message = "password.notempty") String password) {
		try {
			User user = this.bs.authenticate(email, password);
			if (user != null) {
				if (this.domain != null && user.getAccessLevel() < AccessLevels.SYSTEM_ADMIN.getLevel()) {
					CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
					if (companyUser != null && companyUser.isBlocked()) {
						this.fail("Este usuário foi bloqueado. Entre em contato com o administrador do sistema.");
					} else if (companyUser == null) {
						this.fail("Usuário não cadastrado nesse domínio.");
					} else {
						UserAccessToken token = this.bs.retrieveToken(companyUser.getUser());
						if (token == null) {
							this.fail("E-mail e/ou senha inválido(s).");
						} else {
							this.userSession.login(token);
							this.success(new SessionInfo(this.userSession));
						}
					}

				} else {
					if (this.domain == null && user.getAccessLevel() < AccessLevels.SYSTEM_ADMIN.getLevel()) {
						this.fail("Este endereço foi desativado. Entre em contato com o administrador da instituição para mais informações.");
					}
					else {
						UserAccessToken token = this.bs.retrieveToken(user);
						if (token == null) {
							this.fail("E-mail e/ou senha inválido(s).");
						} else {
							this.userSession.login(token);
							this.success(new SessionInfo(this.userSession));
						}
					}
				}

			} else {
				boolean deleted = this.bs.userIsDeleted(email);
				if (deleted && this.bs.existsByEmail(email) != null) {
					this.fail("Este usuário foi deletado. Entre em contato com o administrador do sistema.");
				} else {
					this.fail("E-mail e/ou senha inválido(s).");
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Erro no login.", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Efetua logout do usuário.
	 * 
	 * @param void.
	 * @return void.
	 * 
	 */
	@Post("/api/user/logout")
	@NoCache
	public void logoutAjax() {
		try {
			// Ajax logout
			this.userSession.logout();
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Efetua logout do usuário.
	 * 
	 * @param void.
	 * @return void.
	 */
	@Get("/api/user/logout")
	@NoCache
	public void logout() {
		try {
			this.userSession.logout();
			this.result.redirectTo(IndexController.class).index();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Verificar se usuário está logado.
	 * 
	 * @param void
	 * @return Token do usuário que está logado.
	 */
	@Get("/api/user/isLogged")
	@NoCache
	public void isLogged() {
		if (this.userSession.isLogged()) {
			this.success(this.userSession.getToken());
		} else {
			this.fail();
		}
	}

	/**
	 * Criar sessão.
	 * 
	 * @param void
	 * @return Sessão criada.
	 */
	@Get("/api/user/session")
	@NoCache
	public void fetchSession() {
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
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}

	/**
	 * Recuperar password do usuário.
	 * 
	 * @param email
	 *            E-mail do usuário para recuperar a senha.
	 * @throws EmailException
	 * @throws UnknownHostException
	 */
	@Post("/api/user/recover")
	@Consumes
	@NoCache
	public void requestRecover(String email) throws EmailException, UnknownHostException {
		try {
			User user = this.bs.existsByEmail(email);
			CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
			if (user == null) {
				this.fail("Este e-mail não está cadastrado no sistema.");
			} else {
				boolean userInactive = this.bs.userIsDeleted(email);
				if (userInactive) {
					this.fail("Este usuário foi deletado. Entre em contato com o administrador do sistema.");
				} else if (companyUser.isBlocked()) {
					this.fail("Este usuário foi bloqueado. Entre em contato com o administrador do sistema.");
				} else if (!companyUser.getUser().isActive()) {
					this.fail("Este usuário ainda não completou seu cadastro. Entre em contato com o administrador do sistema.");
				} else {
					UserRecoverRequest req = this.bs.requestRecover(user);

					String url = request.getRequestURL().toString().replaceAll("api/user/recover", "#/reset-password/")
							+ req.getToken();
					this.notificationBS.sendNotificationEmail(NotificationType.RECOVER_PASSWORD, url, "", user, null);
					this.success(email);
				}
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}

	/**
	 * Registrar usuário.
	 * 
	 * @param user
	 *            Usuário que será registrado.
	 * @param birthdate
	 *            Data de nascimento do usuário.
	 * @param token
	 *            Token de identificação do usuário.
	 */
	@Post("/api/user/register/{token}")
	@Consumes("application/json")
	@NoCache
	public void registerUser(User user, String birthdate, String token) {
		try {
			User existent = this.bs.existsByInviteToken(token);
			if (GeneralUtils.isInvalid(existent)) {
				this.fail("Token de registro inválida.");
			} else {
				User userRetrieveCpf = null;
				User userRetrieveCellphone = null;

				userRetrieveCpf = bs.existsByCpf(user.getCpf());
				userRetrieveCellphone = bs.existsByCellphone(user.getCellphone());

				boolean exists = false;
				String msgError = "";

				if (userRetrieveCpf != null) {
					exists = true;
					msgError = "CPF ";
				}
				if (userRetrieveCellphone != null) {
					exists = true;
					msgError += "CELULAR";
				}

				if (!exists) {
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
					CompanyUser companyUser = this.bs.retrieveCompanyUser(existent, this.domain.getCompany());
					this.notificationBS.sendNotificationEmail(NotificationType.WELCOME, "", "", existent, null);
					this.success(existent);
				} else {
					this.fail(msgError);
				}
			}
		} catch (Throwable ex) {
			LOGGER.errorf(ex, "Unexpected error occurred.");
			this.fail(ex.getMessage());
		}
	}

	/**
	 * Verificar validade de token de acesso do usuário.
	 * 
	 * @param token
	 *            Token para validação.
	 * @return Mensagem feedback do usuário.
	 */
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

	/**
	 * Alterar password.
	 * 
	 * @param password
	 *            Password novo.
	 * @param token
	 *            Token de identificação do usuário para trocar o password.
	 */
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

	/**
	 * Requisição para recuperar usuário.
	 * 
	 * @param token
	 *            Token de identificação do usuário a ser recuperado.
	 * @return UserRecoverRequest Requisição realizada.
	 * 
	 */
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

	/**
	 * Buscar usuário da instituição do domínio acessado.
	 * 
	 * @param id
	 *            Id do usuário.
	 * @return User usuário da instituição.
	 */
	@Get("/api/user/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageUsersPermission.class,
			ViewUsersPermission.class })
	public void retrieveUsers(@NotNull Long id) {
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null)
				this.result.notFound();
			else {
				CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
				if (companyUser == null) {
					this.fail(
							"Este usuário não está cadastrado nesse instituição. Convide-o para poder ver suas informações.");
					return;
				}
				user.setAccessLevel(companyUser.getAccessLevel());
				user.setBlocked(companyUser.isBlocked());
				this.success(user);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Buscar uma página com vários usuários de acordo com o número da página.
	 * 
	 * @param page
	 *            Número da página que se deseja recuperar os usuários.
	 * @return PaginatedList Lista dos usuários.
	 */
	@Get("/api/user")
	@NoCache
	@Permissioned(value = AccessLevels.MANAGER, permissions = { ManageUsersPermission.class,
			ViewUsersPermission.class })
	public void listUsers(Integer page, Integer pageSize) {
		if (page == null)
			page = 0;
		try {
			PaginatedList<User> users = this.bs.listFromCurrentCompany(page, pageSize);
			this.success(users.getList(), users.getTotal());
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Deletar usuário.
	 * 
	 * @param id
	 *            Id do usuário que será deletado.
	 * @return boolean true - usuário deletado com sucesso. false - usuário não
	 *         deletado.
	 */
	@Delete("/api/user/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void removeUser(Long id) {

		try {
			User user = this.bs.exists(id, User.class);

			if (user == null) {
				this.result.notFound();
				return;
			}
			if (!sbs.isUserResponsibleForSomeLevel(id, this.domain.getCompany())) {
				CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
				if (companyUser != null) {
					// companyUser.getUser().setDeleted(true);
					// companyUser.getUser().setInviteToken(null);
					this.bs.remove(companyUser);
				}
				this.success(true);
			} else {
				this.success(false);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Bloquear usuário.
	 * 
	 * @param id
	 *            Id do usuário que será bloquado.
	 * @return CompanyUser Instituição que o usuário está bloquado.
	 */
	@Post("/api/user/{id}/block")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void blockUser(Long id) {
		if (id == null) {
			this.result.notFound();
			return;
		}
		if (this.domain == null) {
			this.fail("Você só pode bloquear usuários estando em um domínio de uma instituição.");
			return;
		}
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.result.notFound();
				return;
			}

			CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
			if (companyUser != null) {
				companyUser.setBlocked(true);
				this.bs.persist(companyUser);
			}
			this.success();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Desbloquear usuário.
	 * 
	 * @param id
	 *            Id do usuário que será desbloqueado.
	 * @return companyUser Instituição que o usuário está desbloqueado.
	 */
	@Post("/api/user/{id}/unblock")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void unblockUser(Long id) {
		if (id == null) {
			this.result.notFound();
			return;
		}
		if (this.domain == null) {
			this.fail("Você só pode desbloquear usuários estando em um domínio de uma instituição.");
			return;
		}
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.result.notFound();
				return;
			}

			CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
			if (companyUser != null) {
				companyUser.setBlocked(false);
				this.bs.persist(companyUser);
			}
			this.success();
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Buscar todos os campos do usuário.
	 * 
	 * @param id
	 *            Id do usuário para buscar todos os seus campos.
	 * @return User Usuário com todos os seus campos.
	 */
	@Get("/api/user/profileUser/{id}")
	@NoCache
	@Permissioned
	@Consumes
	public void retrieveUserProfile(Long id) {
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.fail("A empresa solicitada não foi encontrada.");
			} else {
				if (user.getAccessLevel() != AccessLevels.SYSTEM_ADMIN.getLevel()) {
					CompanyUser compUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
					if (compUser != null) {
						user.setNotificationSettings(compUser.getNotificationSetting());
					}
				}
				this.success(user);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	/**
	 * Upload da foto do usuário.
	 * 
	 * @param void.
	 * @return String Url que representa a foto do usuário.
	 */
	@Post("api/file/upload")
	@NoCache
	public void uploadFile() {

		try {
			String fileUrl = StoragerUtils.pipeMultipartFile(this.request.getInputStream(),
					this.request.getContentType(), this.response);
			this.success(fileUrl);
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			// this.fail(ex.getMessage());
		} finally {
			this.result.nothing();
		}
	}

	/**
	 * Salvar as permissões do usuário.
	 * 
	 * @param permissions
	 *            Lista com as permissões a serem salvas.
	 * @return UserPermission Lista de permissões do usuário.
	 */
	@Post("/api/user/permissions")
	@NoCache
	@Permissioned
	@Consumes
	public void saveUserPermissions(PaginatedList<UserPermission> permissions) {
		try {
			for (UserPermission permission : permissions.getList()) {
				permission.setCompany(this.domain.getCompany());
				String url = domain.getBaseUrl() + "/#/users/profilerUser/" + permission.getUser().getId();
				this.bs.saveUserPermission(permission, url);
			}
			this.success(permissions);
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail();
		}
	}

	/**
	 * Listar as permições do sistema, se o usuário possuir essa permição local,
	 * o atributo "granted" vira como true
	 * 
	 * @param userId
	 *            id do usuário
	 * @return lista com as permições desse usuário
	 */
	@Get("/api/user/permissions")
	@NoCache
	@Permissioned
	@Consumes
	public void listPermissions(Long userId) {
		try {
			User user;
			if (userId == null) {
				user = userSession.getUser();
			} else {
				user = this.bs.existsByUser(userId);
			}
			PaginatedList<PermissionDTO> list = this.bs.listPermissionsByUser(user);
			this.success(list);
		} catch (Throwable ex) {
			LOGGER.error("Error while proxying the file upload.", ex);
			this.fail();
		}
	}

	/**
	 * Atualizar configuração de notificação.
	 * 
	 * @param id
	 *            Id do usuário que atualizará configurações de notificação.
	 * @param notificationSetting
	 *            Nova configuração de notificação.
	 * @return companyUser Instituição que o usuário está desbloqueado.
	 */
	@Post("/api/user/updateNotificationSettings")
	@NoCache
	@Permissioned
	@Consumes
	public void updateNotificationSettings(Long id, int notificationSetting) {
		if (id == null) {
			this.result.notFound();
			return;
		}
		if (this.domain == null) {
			this.fail("Você só pode atualizar configuração de notificação estando em um domínio de uma instituição.");
			return;
		}
		try {
			User user = this.bs.exists(id, User.class);
			if (user == null) {
				this.result.notFound();
				return;
			}

			CompanyUser companyUser = this.bs.retrieveCompanyUser(user, this.domain.getCompany());
			if (companyUser != null) {
				companyUser.setNotificationSetting(notificationSetting);
				;
				this.bs.persist(companyUser);
			}
			this.success(companyUser);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	@Post("/api/user/importUsers")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManageUsersPermission.class })
	public void importUsers(String nameList[], String emailList[], Integer accessList[]) {
		try {
			for (int i = 0; i < nameList.length; i++) {
				this.bs.inviteUser(nameList[i], emailList[i], accessList[i]);
			}
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("E-mail do usuário já foi cadastrado!");
		}
	}

}
