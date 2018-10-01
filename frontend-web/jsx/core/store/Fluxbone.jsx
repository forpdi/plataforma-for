
import {Dispatcher} from "flux";
import _ from 'underscore';
import React from "react";
import Backbone from "backbone";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
//import Toastr from 'toastr';

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
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	initialize() {
		this.dispatchToken = this.$dispatcher.register(this.dispatchCallback.bind(this));
		this.on("error", this.handleRequestErrors, this);
		this.on("fail", (msg) => {
			//Toastr.remove();
			//Toastr.error(msg);
			//this.context.toastr.addAlertError(msg);			
		}, this);
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
						this[method](payload.data, payload.opts);
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
					//message: "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText
					message: "O servidor não conseguiu processar sua solicitação. Tente novamente mais tarde. Se o erro persistir contate o suporte."
				};
			}
			this.trigger("fail", resp.message);
			//this.context.toastr.addAlertError(resp.message);
		} else if (opts.status == 409) {
			// Validation errors
			try {
				var resp = JSON.parse(opts.responseText);
			} catch (err) {
				resp = {
					//message: "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText
					message: "O servidor não pode concluir sua ação devido a um conflito na execução do mesmo. Tente novamente mais tarde. Se o erro persistir contate o suporte."
				};
			}
			this.trigger("fail", resp.message);
			//this.context.toastr.addAlertError(resp.message);
		} else if (opts.status == 403) {
			// Probably lost the session
			UserSession.dispatch({
				action: UserSession.ACTION_LOGOUT
			});
			this.trigger("fail", "Não foi possível concluir sua ação. Sua sessão foi encerrada.");
			this.trigger("forbidden");
		} else if (opts.status == 401) {
			// Probably lost the session
			//this.trigger("fail", "Não foi possível concluir sua ação. Refaça o login no sistema e tente novamente. Se o erro persistir contate o suporte.");
			this.trigger("unauthorized");
		} else {
			//this.trigger("fail", "Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText);
			this.trigger("fail", "Não foi possível concluir sua ação. Tente novamente mais tarde. Se o erro persistir contate o suporte.");
			//this.addAlertError("Unexpected server error "+opts.status+" "+opts.statusText+": "+opts.responseText);
		}
	},

	/**
		# Create (backbone docs): collection.create(attributes, [options]) 

		Convenience to create a new instance of a model within a collection. Equivalent to instantiating a model with a hash of attributes,
		saving the model to the server, and adding the model to the set after being successfully created. Returns the new model. If client-side
		validation failed, the model will be unsaved, with validation errors. In order for this to work, you should set the model property of
		the collection. The create method can accept either an attributes hash or an existing, unsaved model object.

		Creating a model will cause an immediate "add" event to be triggered on the collection, a "request" event as the new model is sent to
		the server, as well as a "sync" event, once the server has responded with the successful creation of the model. Pass {wait: true} if
		you'd like to wait for the server before adding the new model to the collection.

		```javascript
		var Library = Backbone.Collection.extend({
		  model: Book
		});

		var nypl = new Library;

		var othello = nypl.create({
		  title: "Othello",
		  author: "William Shakespeare"
		});
		```

		Events: add, request, sync
	*/


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

	/**
		Events: fail, find
	*/
	find(params) {
		var me = this;
		$.ajax({
			method: "GET",
			url: me.url,
			dataType: 'json',
			data: params,
			success(data, status, opts) {
				me.add(data.data, {
					merge: true
				});
				me.trigger("find", me, data.data, opts);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	updateField(params) {
		if (!params) {
			console.error("Store: You must pass data on the payload to request a field update.");
		} else {
			var me = this;
			$.ajax({
				method: "POST",
				url: me.url+"/"+params.id+"/update/field",
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
