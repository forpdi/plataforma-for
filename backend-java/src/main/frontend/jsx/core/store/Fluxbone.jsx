
import {Dispatcher} from "flux";
import _ from 'underscore';
import Backbone from "backbone";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

var Model = Backbone.Model.extend({
	parse(response, opts) {
		if (response.data) {
			return response.data;
		}
		return response;
	}
});

var Store = Backbone.Collection.extend({
	BACKEND_URL: BACKEND_URL,
	$dispatcherActionRegex: /[a-zA-Z0-9]+$/,
	$dispatcher: new Dispatcher(),
	dispatchAcceptRegex: /^[a-zA-Z0-9]*-?[a-zA-Z0-9]+$/,
	dispatch(payload) {
		this.$dispatcher.dispatch(payload);
	},
	initialize() {
		this.dispatchToken = this.$dispatcher.register(this.dispatchCallback.bind(this));
		this.on("error", this.handleRequestErrors, this);
	},
	parse(response, opts) {
		this.total = response.total;
	    return response.data;
	},
	dispatchCallback(payload) {
		if (payload.action && payload.action.match(this.dispatchAcceptRegex)) {
			var method = this.$dispatcherActionRegex.exec(payload.action)[0];
			if (method) {
				var cb = this[method];
				if (typeof cb == 'function') {
					try {
						this[method](payload.data);
					} catch (err) {
						console.error("Store: Dispatching",payload.action, "failed:",err);
					}
				} else {
					console.warn("Store: Dispatch action is not a function:",(typeof cb),method);
				}
			} else {
				console.warn("Store: Invalid dispatch action:",payload.action);
			}
		}
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
		} else if (opts.status == 403) {
			// Probably lost the session
			UserSession.dispatch({
				action: UserSession.ACTION_LOGOUT
			});
		} else {
			this.trigger("fail", "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText);
		}
	},
	retrieve(id) {
		var me = this,
			model = me.get(id);
		if (model) {
			_.defer(function() {
				me.trigger("retrieve", model);
			});
		} else {
			model = new me.model();
			model.fetch({
				url: me.url + "/" + id,
				success(model, response, options) {
					me.trigger("retrieve", model);
				},
				error(model, response, options) {
					me.handleRequestErrors([], options.xhr);
				}
			});
		}
	},
	update(model) {
		if (!model) {
			console.error("Store: You must pass a model on the payload to request an update.");
		} else {
			if (!this.get(model.get("id"))) {
				this.add(model);
			}
			model.save();
		}
	},

	/**
		# Destroy (backbone docs)
		
		Destroys the model on the server by delegating an HTTP DELETE request to Backbone.sync. Returns a jqXHR object,
		or false if the model isNew. Accepts success and error callbacks in the options hash, which will be passed
		(model, response, options). Triggers a "destroy" event on the model, which will bubble up through any collections
		that contain it, a "request" event as it begins the Ajax request to the server, and a "sync" event, after the
		server has successfully acknowledged the model's deletion. Pass {wait: true} if you'd like to wait for the server
		to respond before removing the model from the collection.
		
		```javascript
		book.destroy({success: function(model, response) {
		  ...
		}});
		```
		Events: request, destroy, fail, sync
	*/
	destroy(model) {
		var me = this;
		if (!model) {
			console.error("Store: You must pass a model on the payload to request a destroy.");
		} else {
			model.destroy({
				wait: true,
				url: me.url + "/" + model.get("id"),
				error: (model,response,opts) => {
					me.handleRequestErrors([], response);
				}
			});
		}
	},

	find(data) {
		var me = this;
		me.fetch({
			data: data
		});
	},

	updateField(params) {
		if (!params) {
			console.error("Store: You must pass data on the payload to request a field update.");
		} else {
			var me = this;
			$.ajax({
				method: "POST",
				url: me.url+"/update/field",
				dataType: 'json',
				data: params,
				success(data, status, opts) {
					me.trigger("fieldupdated", data);
				},
				error(opts, status, errorMsg) {
					me.handleRequestErrors([], opts);
				}
			});
		}
	},
});

export default {
	BACKEND_URL: BACKEND_URL,
	Model: Model,
	Store: Store
};
