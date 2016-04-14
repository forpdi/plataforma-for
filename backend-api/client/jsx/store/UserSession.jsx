
import _ from "underscore";
import Backbone from "backbone";
import {Dispatcher} from "flux";

var UserSession = Backbone.Model.extend({
	ACTION_REFRESH: 'refreshStatus',
	ACTION_LOGIN: 'login',
	ACTION_LOGOUT: 'logout',
	ACTION_UPDATE_PROFILE: 'updateProfile',

	BACKEND_URL: BACKEND_URL,
	url: BACKEND_URL + "persons",
	$dispatcher: new Dispatcher(),
	dispatch(payload) {
		this.$dispatcher.dispatch(payload);
	},
	dispatchCallback(payload) {
		if (payload.action) {
			this[payload.action](payload.data);
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
			_.defer(() => { location.assign("#/") });
		}
	},

	parse(response, opts) {
		return response.data ? {
			logged: true,
			user: response.data
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
			url: BACKEND_URL + "persons/"+localStorage.userId,
			dataType: 'json',
			success(data, status, opts) {
				if (!data.error) {
					me.set({
						"logged": true,
						"user": data
					});
					me.trigger("login", me);
				} else {
					me.trigger("fail", data.error.message);
				}
				if (me.get("loading")) {
					me.set({loading: false});
					me.trigger("loaded", true);
				}
			},
			error(opts, status, errorMsg) {
				if (me.get("loading")) {
					me.set({loading: false});
				}
				me.clearStorage();
				location.assign("#/");
			}
		});
	},

	handleRequestErrors(collection, opts) {
		if (opts.status == 401) {
			this.trigger("fail", "Dados de login inválidos.");
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
			this.trigger("fail", "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseJSON.error.message);
		}
	},

	login(data) {
		var me = this,
			errors = [];
		if (!(data.email) || (data.email == '')) {
			errors.push("Digite seu e-mail.");
		}
		if (!(data.password) || (data.password == '')) {
			errors.push("Digite sua senha.");
		}

		if (errors.length > 0) {
			me.trigger("fail", errors);
		} else {
			$.ajax({
				method: "POST",
				url: BACKEND_URL + "persons/login",
				dataType: 'json',
				data: data,
				success(data, status, opts) {
					console.log(data);
					if (!data.error) {
						$.ajaxSetup({
							headers: {
								"Authorization": data.id
							}
						});
						me.putStorage(data.id, data.userId);
						me.refreshStatus();
					} else {
						me.trigger("fail", data.error.message);
					}
				},
				error(opts, status, errorMsg) {
					me.handleRequestErrors([], opts);
				}
			});
		}
	},
	logout() {
		var me = this;
		$.ajax({
			method: "POST",
			url: BACKEND_URL + "persons/logout",
			dataType: 'json',
			success(data, status, opts) {
				console.log(data);
				me.set({logged: false, user: null});
				$.ajaxSetup({
					headers: {
						"Authorization": null
					}
				});
				me.clearStorage();
				me.trigger("logout");
				location.assign("#/");
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},
	updateProfile(data) {
		var me = this,
			errors = [];
		if (!(data.name) || (data.name == '')) {
			errors.push("Digite seu nome.");
		}
		if (!(data.lastName) || (data.lastName == '')) {
			errors.push("Digite seu sobrenome.");
		}
		if (!(data.email) || (data.email == '')) {
			errors.push("Digite seu e-mail.");
		}

		if (errors.length > 0) {
			me.trigger("fail", errors);
		} else {
			$.ajax({
				method: "POST",
				url: BACKEND_URL + "user/profile",
				dataType: 'json',
				data: data,
				success(data, status, opts) {
					if (data.success) {
						me.set({
							"logged": true,
							"user": data.data
						});
						me.trigger("updated", me);
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
});

export default new UserSession();
