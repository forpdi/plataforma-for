import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"item";

var ItemModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		{/*if (string(attrs.name).isEmpty()) {
			errors.push("O nome do itemo é obrigatório.");
		} */}
		/*if (string(attrs.begin).isEmpty()) {
			errors.push("A data de início do item é obrigatória.");
		}
		if (string(attrs.end).isEmpty()) {
			errors.push("A data de término do item é obrigatória.");
		}*/

		if (errors.length > 0)
			return errors
	}
});

var ItemStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'item-create',
	ACTION_CREATE_FIELD: 'item-createField',
	ACTION_NEW_ITEM: 'item-newItem',
	ACTION_DESTROY: 'item-destroy',
	ACTION_FIND: 'item-find',
	ACTION_FIND_TERMS:'item-findTerms',
	ACTION_RETRIEVE_ITEM: 'item-retrieveItem',
	ACTION_RETRIEVE_ITENS: 'item-retrieveItens',
	ACTION_RETRIEVE_FIELD: 'item-retrieveField',
	ACTION_RETRIEVE_PERFORMANCE: 'item-retrievePerformance',
	ACTION_UPDATE: 'item-update',
	ACTION_CUSTOM_UPDATE: 'item-customUpdate',
	ACTION_DELETE_PLAN: 'item-deleteItem',
	ACTION_DELETE: "item-delete",
	ACTION_RETRIEVE_SUBITEM: "item-retrieveSubitem",
	ACTION_RETRIEVE_SUBITENS: "item-retrieveSubitens",
	ACTION_RETRIEVE_ALLSUBITENS: "item-retrieveAllSubitens",
	ACTION_NEW_SUBITEM: 'item-newSubItem',
	ACTION_CREATE_SUBFIELD: 'item-createSubfield',
	ACTION_RETRIEVE_SUBFIELD: 'item-retrieveSubField',
	ACTION_DELETE_SUB: 'item-deleteSubitem',
	ACTION_CUSTOM_UPDATE_SUB: 'item-customUpdateSub',
	dispatchAcceptRegex: /^item-[a-zA-Z0-9]+$/,
	url: URL,
	model: ItemModel,


	newItem(data) {
		var me = this;
		$.ajax({
			url: me.url+"/new",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				item: data
			}),
			success(model) {
				me.trigger("newItem", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("newItem", opts);
			}
		});
	},

	newSubItem(data) {
		var me = this;
		$.ajax({
			url: me.url+"/subnew",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				subitem: data
			}),
			success(model) {
				me.trigger("newSubItem", model);

			},
			error(opts, status, errorMsg) {
				me.trigger("newSubItem", opts);
			}
		});
	},

	retrieveItem(id) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url + "/" + id,
			success(model, response, options) {
				me.trigger("retrieveItem", model);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	retrieveItens(id) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url,
			data: {policyId: id},
			success(model, response, options) {
				me.trigger("retrieveItens", response);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	retrieveSubitem(id) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url + "/subitem/" + id,
			success(model, response, options) {
				me.trigger("retrieveSubitem", response);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	retrieveSubitens(id,pigback) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url + "/subitens/" + id,
			success(model, response, options) {
				me.trigger("retrieveSubitens", response,pigback);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	retrieveAllSubitens(id,pigback) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url + "/allsubitens/" + id,
			success(model, response, options) {
				me.trigger("retrieveAllSubitens", response,pigback);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
				me.trigger("retrieveAllSubitens", model,pigback);
			}
		});
	},

	createField(data) {
		var me = this;
		$.ajax({
			url: me.url+"/field",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				fieldItem: data
			}),
			success(model) {
				me.trigger("itemField", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemField", opts);
			}
		});
	},

	createSubfield(data) {
		var me = this;
		$.ajax({
			url: me.url+"/subfield",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				fieldSubItem: data
			}),
			success(model) {
				me.trigger("itemField", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemField", null);
			}
		});
	},

	retrieveField(id) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url + "/field/" + id,
			success(model, response, options) {
				me.trigger("retrieveField", model);
			},
			error(model, response, options) {
				me.trigger("retrieveField", null);
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	retrieveSubField(id) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: me.url + "/subfield/" + id,
			success(model, response, options) {
				me.trigger("retrieveSubField", response);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	customUpdate(data) {
		var me = this;
		$.ajax({
			url: me.url+"/update",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				item: data
			}),
			success(model) {
				me.trigger("itemUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemUpdated", null);
			}
		});
	},

	customUpdateSub(data) {
		var me = this;
		$.ajax({
			url: me.url+"/subitem/update",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				subitem: data
			}),
			success(model) {
				me.trigger("subitemUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("subitemUpdated", null);
			}
		});
	},

	findTerms(data) {
		var me = this;
		$.ajax({
			url: me.url+"/findTerms",
			method: 'GET',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("itemFind", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemFind", opts);
			}
		});
	},

	retrievePerformance(params) {
		var me = this;
		$.ajax({
			method: "GET",
			url: me.url+"/performance",
			dataType: 'json',
			data: params,
			success(response, status, opts) {
				me.trigger("retrieve-performance", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},


	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/"+data,
			method: 'DELETE',
			success(model) {
				me.trigger("itemDeleted", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("itemDeleted", opts);
			}
		});
	},


	deleteSubitem(data){
		var me = this;
		$.ajax({
			url: me.url+"/subitem/"+data,
			method: 'DELETE',
			success(model) {
				me.trigger("subitemDeleted", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("subitemDeleted", opts);
			}
		});
	},

});

export default new ItemStore();
