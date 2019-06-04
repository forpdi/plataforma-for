
import _ from "underscore";
import React from "react";
import Backbone from "backbone";
import {Dispatcher} from "flux";
import string from 'string';
import Toastr from 'toastr';

var ReactToastr = require("react-toastr");
var {ToastContainer} = ReactToastr;
var ToastMessageFactory = React.createFactory(ReactToastr.ToastMessage);
Toastr.options.positionClass = "toast-top-full-width";
Toastr.options.timeOut = 4000;
Toastr.options.extendedTimeOut = 8000;

var UserSession = Backbone.Model.extend({
	ACTION_REFRESH: 'refreshStatus',
	ACTION_LOGIN: 'login',
	ACTION_LOGOUT: 'logout',
	ACTION_RECOVER_PASSWORD: 'recoverPassword',
	ACTION_CHECK_REGISTER_TOKEN: 'checkRegisterToken',
	ACTION_REGISTER_USER: 'registerUser',
	ACTION_CHECK_RECOVER_TOKEN: 'checkRecoverToken',
	ACTION_RESET_PASSWORD: 'resetPassword',
	ACTION_UPDATE_PROFILE: 'updateProfile',
	ACTION_VERIFY_NOTIFICATION: 'verifyNotification',
	ACTION_LIST_NOTIFICATIONS: 'listNotifications',
	ACTION_SEND_MESSAGE:'sendMessage',
	ACTION_LIST_MESSAGES:'listMessages',

	BACKEND_URL: BACKEND_URL,
	url: BACKEND_URL + "user",
	$dispatcher: new Dispatcher(),
	dispatch(payload) {
		this.$dispatcher.dispatch(payload);
	},
	dispatchCallback(payload) {
		if (payload.action) {
			var method = this[payload.action];
			if (typeof method === 'function') {
				method.call(this, payload.data);
			} else {
				console.warn("UserSession: The action (method)",payload.action,"is not defined.");
			}
		} else {
			console.warn("UserSession: The dispatching action must be defined.\n",payload);
		}
	},
	initialize() {
		this.dispatchToken = this.$dispatcher.register(this.dispatchCallback.bind(this));
		if (localStorage.token && (localStorage.token != "")) {
			$.ajaxSetup({
				headers: {
					"Authorization": localStorage.token
				}
			});
			this.set({loading: true});
			this.refreshStatus(true);
		} else {
			this.set({loading: false});
		}
	},
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	parse(response, opts) {
		return response.data ? {
			logged: true,
			user: response.data.user,
			accessLevel: response.data.accessLevel,
			permissions: response.data.permissions
		}:{
			logged: false
		};
	},

	clearStorage() {
		localStorage.removeItem("token");
		localStorage.removeItem("userId");
	},
	putStorage(token, userId) {
		localStorage.token = token;
		localStorage.userId = userId;
	},

	refreshStatus(initial) {
		var me = this;
		$.ajax({
			method: "GET",
			url: BACKEND_URL + "user/session",
			dataType: 'json',
			success(data, status, opts) {
				if (data.success) {
					me.set({
						"logged": true,
						"user": data.data.user,
						"accessLevel": data.data.accessLevel,
						"permissions": data.data.permissions
					});
					me.trigger("login", me);
				} else {
					me.trigger("fail", data.message);
				}
				if (me.get("loading")) {
					me.set({loading: false});
					me.trigger("loaded", true);
				}
			},
			error(opts, status, errorMsg) {
				if (me.get("loading")) {
					me.set({loading: false});
					me.trigger("loaded", true);
				}
				me.clearStorage();
				location.assign("#/");
			}
		});
	},

	handleRequestErrors(collection, opts) {
		if (opts.status == 400) {
			this.trigger("fail", opts.responseJSON.message);
		} else if (opts.status == 409) {
			// Validation errors
			try {
				var resp = JSON.parse(opts.responseText);
			} catch (err) {
				resp = {
					message: "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText
				};
			}
			this.trigger("fail", resp.message);
		} else {
			console.error(opts.responseText);
			this.trigger("fail", `Unexpected server error: ${opts.status} ${opts.statusText}`);
		}
	},

	login(data) {
		var me = this,
			errors = [];

		if ((!(data.email) || (data.email == '') && (!(data.password) || (data.password == '')))) {
			errors.push("Por favor, digite seu nome de usuário e sua senha");
		}

		else {
			if (!(data.email) || (data.email == '')) {
				errors.push("Por favor, digite seu nome de usuário");
			}
			if (!(data.password) || (data.password == '')) {
				errors.push("Por favor, digite sua senha");
			}
		}

		if (errors.length > 0) {
			me.trigger("fail", errors);
		} else {
			$.ajax({
				method: "POST",
				url: BACKEND_URL + "user/login",
				dataType: 'json',
				data: JSON.stringify(data),
				contentType: 'application/json',
				processData: false,
				success(data, status, opts) {
					if (data.success) {
						$.ajaxSetup({
							headers: {
								"Authorization": data.data.token
							}
						});
						me.putStorage(data.data.token, data.data.user.id);
						me.set({
							"logged": true,
							"user": data.data.user,
							"accessLevel": data.data.accessLevel,
							"permissions": data.data.permissions
						});
						me.trigger("login", me);
					} else {
						me.trigger("fail", data.message);
					}
				},
				error(opts, status, errorMsg) {
					me.handleRequestErrors([], opts);
				}
			});
		}
	},
	logout(reloadPage) {
		var me = this;
		$.ajax({
			method: "POST",
			url: BACKEND_URL + "user/logout",
			dataType: 'json',
			success(data, status, opts) {
				me.set({logged: false, user: null});
				$.ajaxSetup({
					headers: {
						"Authorization": null
					}
				});
				me.clearStorage();
				me.trigger("logout");
				if (reloadPage) {
					location.reload();
				} else {
					location.assign("#/");
				}
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	recoverPassword(params) {
		var me = this;
		$.ajax({
			method: "POST",
			url: BACKEND_URL + "user/recover",
			dataType: 'json',
			data: JSON.stringify(params),
			contentType: 'application/json',
			processData: false,
			success(data, status, opts) {
				me.trigger("recoverpassword", data);
			},
			error(opts, status, errorMsg) {
				me.trigger("recoverpassword", opts.responseJSON);
			}
		});
	},

	checkRegisterToken(token) {
		var me = this;
		if (typeof token !== 'string') {
			console.warn("UserSession: You must provide a string token to be checked.\n",token);
			return;
		}
		$.ajax({
			method: "GET",
			url: BACKEND_URL + "user/register/"+token,
			dataType: 'json',
			success(data, status, opts) {
				me.trigger("registertoken", true);
			},
			error(opts, status, errorMsg) {
				me.trigger("registertoken", false);
			}
		});
	},
	registerUser(params) {
		var me = this,
			errors = []
		;

		if (string(params.name).isEmpty()) {
			errors.push("O nome é obrigatório.");
		}
		if (string(params.cpf).isEmpty()) {
			errors.push("O CPF é obrigatório.");
		}
		if (string(params.cellphone).isEmpty()) {
			errors.push("O celular é obrigatório.");
		}
		if (string(params.birthdate).isEmpty()) {
			errors.push("A data de nascimento é obrigatória.");
		}

		if (string(params.password).isEmpty()) {
			errors.push("A senha é obrigatória.");
		} else if (params.password !== params.passwordconfirm) {
			errors.push("As senhas digitadas não são iguais.");
		}

		if (errors.length > 0) {
			me.trigger("fail", errors);
			return;
		}

		var birthdate = params.birthdate, token = params.token;
		delete params.birthdate;
		delete params.token;
		$.ajax({
			method: "POST",
			url: BACKEND_URL + "user/register/"+token,
			dataType: 'json',
			data: JSON.stringify({
				user: params,
				birthdate: birthdate
			}),
			contentType: 'application/json',
			processData: false,
			success(data, status, opts) {
				me.trigger("register", data);
			},
			error(opts, status, errorMsg) {
				var error = JSON.parse(opts.responseText);
				if (error.message == "CPF ") {
					Toastr.remove();
					Toastr.error("CPF já cadastrado no sistema. Insira um CPF válido.");
					//me.trigger("fail", "CPF já cadastrado no sistema. Insira um CPF válido");
				} else if (error.message == "CPF CELULAR") {
					Toastr.remove();
					Toastr.error("CPF e Celular já cadastrados no sistema. Insira valores válidos");
					//me.trigger("fail", "CPF e Celular já cadastrados no sistema. Insira valores válidos");
				} else if (error.message == "CELULAR") {
					Toastr.remove();
					Toastr.error("Celular já cadastrado no sistema. Insira um número válido");
					//me.trigger("fail", "Celular já cadastrado no sistema. Insira um número válido");
				} else {
					me.handleRequestErrors([], opts);
				}
			}
		});
	},

	checkRecoverToken(token) {
		var me = this;
		if (typeof token !== 'string') {
			console.warn("UserSession: You must provide a string token to be checked.\n",token);
			return;
		}
		$.ajax({
			method: "GET",
			url: BACKEND_URL + "user/reset/"+token,
			dataType: 'json',
			success(data, status, opts) {
				me.trigger("recovertoken", true);
			},
			error(opts, status, errorMsg) {
				me.trigger("recovertoken", false);
			}
		});
	},
	resetPassword(params) {
		var me = this;
		if (params.password !== params.passwordconfirm) {
			me.trigger("fail", "As senhas digitadas não são iguais.");
			return;
		}
		$.ajax({
			method: "POST",
			url: BACKEND_URL + "user/reset/"+params.token,
			dataType: 'json',
			data: JSON.stringify({
				password: params.password
			}),
			contentType: 'application/json',
			processData: false,
			success(data, status, opts) {
				me.trigger("resetpassword", data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	verifyNotification(data) {
		var me = this;
		$.ajax({
			method: "GET",
			url: BACKEND_URL + "notification/verifynotifications",
			dataType: 'json',
			data: data,
			success(data, status, opts) {
				if (data.data == -1) {
					me.logout();	//caso o usuário tenha sido bloqueado enquanto estava logado ocorrerá o logout quando isto for constatado
				}
				me.trigger("notificationsVerifyed", data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	listNotifications(data){
		var me = this;
		$.ajax({
			url:  BACKEND_URL + "notification/notifications",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				if (data && data.topBar)
					me.trigger("retrieve-limitedNotifications", model);
				else if (data && data.page)
					me.trigger("retrieve-showMoreNotifications", model);
				else
					me.trigger("retrieve-notifications", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	sendMessage(data){
		var me = this;
		$.ajax({
			url:  BACKEND_URL + "structure/sendmessage",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				subject: data.subject,
				message: data.message,
				userId: data.userId
			}),
			success(response) {
				me.trigger("sendMessage",response);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	listMessages(data){
		var me = this;
		$.ajax({
			url: BACKEND_URL +"structure/listmessages",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: data,
			success(model) {
				if (data && data.page)
					me.trigger("retrieve-showMoreMessages", model);
				else
					me.trigger("retrieve-messages", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	}

});

export default new UserSession();
