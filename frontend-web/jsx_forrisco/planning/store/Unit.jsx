import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"unit";

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
	ACTION_RETRIEVE: 'unit-retrieve',
	ACTION_UPDATE: 'unit-update',
	ACTION_ARCHIVE: "unit-archive",
	ACTION_UNARCHIVE: "unit-unarchive",
	ACTION_FIND_ARCHIVED: 'unit-findArchived',
	ACTION_FIND_UNARCHIVED: 'unit-findUnarchived',
	ACTION_MAIN_MENU_STATE: "unit-mainMenuState",
	ACTION_DELETE: "unit-delete",
	ACTION_NEWUNIT: "unit-newUnit",
	dispatchAcceptRegex: /^unit-[a-zA-Z0-9]+$/,
	ACTION_CUSTOM_UPDATE: "unit-customUpdate",
	ACTION_FIND_BY_PLAN: "unit-findByPlan",
	//ACTION_FIND_INCIDENTS_BY_PLAN: "unit-findIncdents",
	url: URL,
	model: unitModel,

	findArchived(data){
		var me = this;
		$.ajax({
			url: me.url+"/archivedunit",
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

	findByPlan(data){
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {planId: data},
			success(model) {
				me.trigger("unitbyplan", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitbyplan", opts);
			}
		});
	},

	findUnarchived(data){
		var me = this;
		$.ajax({
			url: me.url+"/unarchivedunit",
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

	newUnit(data){
		var me = this;
		$.ajax({
			url: me.url+'/new',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				unit: data
			}),
			success(model) {
				me.trigger("unitcreated", model);
				//me.trigger("unitcreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unitcreated",{msg:opts.responseJSON.message,data:{id:null}})
				me.handleRequestErrors([], opts);
			}
		});
	},

	archive(data){
		var me = this;
		$.ajax({
			url: me.url+"/archive",
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

	unarchive(data){
		var me = this;
		$.ajax({
			url: me.url+"/unarchive",
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

	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/"+data,
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
			url: me.url+"/update",
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
				me.trigger("unitUpdated",{msg:opts.responseJSON.message,data:{id:null}})
			}
		});
	},

	mainMenuState(data){
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});

export default new UnitStore();
