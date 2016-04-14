
import Backbone from "backbone";
import {Dispatcher} from "flux";

var UserSession = Backbone.Model.extend({
	ACTION_REFRESH: 'refreshStatus',
	ACTION_LOGIN: 'login',
	ACTION_LOGOUT: 'logout',
	ACTION_UPDATE_PROFILE: 'updateProfile',

	BACKEND_URL: BACKEND_URL,
	url: BACKEND_URL + "user/session",
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
		this.refreshStatus(true);
	},

	parse(response, opts) {
		return response.data ? {
			logged: true,
			user: response.data
		}:{
			logged: false
		};
	},

	refreshStatus(initial) {
		this.fetch({
			success(model, response, options) {
				if (model.get("logged")) {
					model.trigger("login", model);
				} else  {
					if (initial !== true)
						model.trigger("logout");
					location.assign("#/");
				}
			},
			error(model, response, options) {
				console.error("Failed to fetch user session: ", response);
			}
		});
	},

	handleRequestErrors(collection, opts) {
		if (opts.status == 400) {
			// Validation errors
			var resp;
			try {
				resp = JSON.parse(opts.responseText);
			} catch (err) {
				resp = {
					message: "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText
				};
			}
			this.trigger("fail", resp.message);
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
			this.trigger("fail", "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText);
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
				url: BACKEND_URL + "user/login",
				dataType: 'json',
				data: data,
				success(data, status, opts) {
					if (data.success) {
						me.set({
							"logged": true,
							"user": data.data
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
	logout() {
		var me = this;
		$.post(BACKEND_URL + "user/logout",(resp, status, xhr) => {
			me.set({logged: false});
			me.trigger("logout");
			location.assign("#/");
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
