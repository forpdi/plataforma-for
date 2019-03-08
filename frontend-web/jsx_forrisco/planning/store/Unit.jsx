import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL + "unit";

var unitModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];
		if (string(attrs.name).isEmpty()) {
			errors.push("O nome da unidade é obrigatório.");
		}
		if (errors.length > 0)
			return errors;
	}
});

var UnitStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'unit-create',
	ACTION_DESTROY: 'unit-destroy',
	ACTION_FIND: 'unit-find',
	ACTION_RETRIEVE_UNIT: 'unit-retrieveUnit',
	ACTION_UPDATE_UNIT: 'unit-updateUnit',
	ACTION_ARCHIVE: "unit-archive",
	ACTION_UNARCHIVE: "unit-unarchive",
	ACTION_FIND_ARCHIVED: 'unit-findArchived',
	ACTION_FIND_UNARCHIVED: 'unit-findUnarchived',
	ACTION_MAIN_MENU_STATE: "unit-mainMenuState",
	ACTION_DELETE_UNIT: "unit-deleteUnit",
	ACTION_NEW_UNIT: "unit-newUnit",
	ACTION_NEW_SUBUNIT: "unit-newSubunit",
	dispatchAcceptRegex: /^unit-[a-zA-Z0-9]+$/,
	ACTION_CUSTOM_UPDATE: "unit-customUpdate",
	ACTION_FIND_BY_PLAN: "unit-findByPlan",
	ACTION_LIST_SUBUNIT: "unit-listSubunits",
	ACTION_LIST_SUBUNIT_BY_PLAN: "unit-listSubunitsByPlan",

	ACTION_RETRIEVE_PROCESSES: "unit-retrieveProcess",
	ACTION_FIND_TERMS: 'unit-findTerms',
	ACTION_FINDALL_TERMS: 'unit-findAllTerms',
	//ACTION_FIND_INCIDENTS_BY_PLAN: "unit-findIncdents",
	url: URL,
	model: unitModel,

	findArchived(data) {
		var me = this;
		$.ajax({
			url: me.url + "/archivedunit",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("archiveunitlisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("archivedunitlisted", opts);
			}
		});
	},

	findByPlan(data, info){
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {planId: data},
			success(model) {
				me.trigger("unitbyplan", model, info);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitbyplan", opts);
			}
		});
	},

	findUnarchived(data) {
		var me = this;
		$.ajax({
			url: me.url + "/unarchivedunit",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unarchivedunitlisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unarchivedunitlisted", opts);
			}
		});
	},

	newUnit(data) {
		var me = this;
		$.ajax({
			url: me.url + '/new',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				unit: data
			}),
			success(model) {
				me.trigger("unitcreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitcreated", {msg: opts.responseJSON.message, data: {id: null}})
				me.handleRequestErrors([], opts);
			}
		});
	},

	archive(data) {
		var me = this;
		$.ajax({
			url: me.url + "/archive",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				id: data.id
			}),
			success(model) {
				me.trigger("unitarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitarchived", opts);
			}
		});
	},

	unarchive(data) {
		var me = this;
		$.ajax({
			url: me.url + "/unarchive",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				id: data.id
			}),
			success(model) {
				me.trigger("unitunarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitunarchived", opts);
			}
		});
	},

	delete(data) {
		var me = this;
		$.ajax({
			url: me.url + "/" + data,
			method: 'DELETE',
			success(model) {
				me.trigger("unitDeleted", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitDeleted", opts);
			}
		});
	},

	customUpdate(data) {
		var me = this;
		$.ajax({
			url: me.url + "/update",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				unit: data
			}),
			success(model) {
				me.trigger("unitUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitUpdated", {msg: opts.responseJSON.message, data: {id: null}})
			}
		});
	},

	retrieveUnit(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/${data.unitId}`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unitRetrieved", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitRetrieved", opts);
			}
		});
	},

	updateUnit(data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'PUT',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				unit: data.unit,
			}),
			success(model) {
				me.trigger("unitUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitUpdated", opts);
			}
		});
	},

	deleteUnit(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/${data.id}`,
			method: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unitDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitDeleted", opts);
			}
		});
	},

	newSubunit(data) {
		var me = this;
		$.ajax({
			url: me.url + '/subnew',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				unit: data
			}),
			success(model) {
				me.trigger("subunitCreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("subunitCreated", {msg: opts.responseJSON.message, data: {id: null}})
				me.handleRequestErrors([], opts);
			}
		});
	},

	listSubunits(data, node) {
		var me = this;
		$.ajax({
			url: `${me.url}/listsub/${data.unitId}`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("subunitsListed", model, node);
			},
			error(opts, status, errorMsg) {
				me.trigger("subunitsListed", opts);
			}
		});
	},

	listSubunitsByPlan(data, node) {
		var me = this;
		$.ajax({
			url: `${me.url}/listsub`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {planId: data},
			success(model) {
				me.trigger("allSubunitsListed", model, node);
			},
			error(opts, status, errorMsg) {
				me.trigger("allSubunitsListed", opts);
			}
		});
	},

	retrieveProcess() {
		var me = this;
		$.ajax({
			url: me.url + "/process",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("retrieveProcess", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("retrieveProcess", opts);
			}
		});
	},

	//Busca Avançada
	findTerms(data) {
		var me = this;
		$.ajax({
			url: me.url + "/findTerms",
			method: 'GET',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("findTerms", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("findTerms", opts);
			}
		});
	},

	findAllTerms(data) {
		var me = this;
		$.ajax({
			url: me.url + "/search",
			method: 'GET',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("findTerms", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("findTerms", opts);
			}
		});
	},

	mainMenuState(data) {
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});

export default new UnitStore();
