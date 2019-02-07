import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"risk";

var RiskModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];
		if (string(attrs.name).isEmpty()) {
			errors.push("O nome do risco é obrigatório.");
		}
		if (errors.length > 0)
			return errors;
	}
});

var RiskStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'risk-create',
	ACTION_DESTROY: 'risk-destroy',
	ACTION_FIND: 'risk-find',
	ACTION_RETRIEVE: 'risk-retrieve',
	ACTION_UPDATE: 'risk-update',
	ACTION_ARCHIVE: "risk-archive",
	ACTION_UNARCHIVE: "risk-unarchive",
	ACTION_FIND_ARCHIVED: 'risk-findArchived',
	ACTION_FIND_UNARCHIVED: 'risk-findUnarchived',
	ACTION_MAIN_MENU_STATE: "risk-mainMenuState",
	ACTION_DELETE: "risk-delete",
	ACTION_NEWUNIT: "risk-newRisk",
	dispatchAcceptRegex: /^risk-[a-zA-Z0-9]+$/,
	ACTION_CUSTOM_UPDATE: "risk-customUpdate",
	ACTION_FIND_BY_PLAN: 'risk-findByPlan',
	ACTION_FIND_INCIDENTS_BY_PLAN: "risk-findIncdentsByPlan",
	ACTION_FIND_MONITORS_BY_PLAN: "risk-findMonitorsByPlan",
	url: URL,
	model: RiskModel,

	findArchived(data){
		var me = this;
		$.ajax({
			url: me.url+"/archivedrisk",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("archiverisklisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("archivedrisklisted", opts);
			}
		});
	},

	findByUnit(data){
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {unitId: data},
			success(model) {
				me.trigger("riskbyunit", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskbyunit", opts);
			}
		});
	},


	findUnarchived(data){
		var me = this;
		$.ajax({
			url: me.url+"/unarchivedrisk",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unarchivedrisklisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unarchivedrisklisted", opts);
			}
		});
	},

	newRisk(data){
		var me = this;
		$.ajax({
			url: me.url+'/new',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				risk: data
			}),
			success(model) {
				me.trigger("riskcreated", model);
				//me.trigger("riskcreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskcreated",{msg:opts.responseJSON.message,data:{id:null}})
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
				me.trigger("riskarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskarchived", opts);
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
				me.trigger("riskunarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskunarchived", opts);
			}
		});
	},

	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/"+data,
			method: 'DELETE',
			success(model) {
				me.trigger("riskDeleted", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskDeleted", opts);
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
				risk: data
			}),
			success(model) {
				me.trigger("riskUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskUpdated",{msg:opts.responseJSON.message,data:{id:null}})
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
				me.trigger("riskbyplan", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskbyplan", opts);
			}
		});
	},

	findIncdentsByPlan(data){
		var me = this;
		$.ajax({
			url: me.url+"/incident",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {planId: data},
			success(model) {
				me.trigger("incidentbByPlan", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("incidentbByPlan", opts);
			}
		});
	},

	findMonitorsByPlan(data){
		var me = this;
		$.ajax({
			url: me.url+"/monitor",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {planId: data},
			success(model) {
				me.trigger("monitorByPlan", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("monitorByPlan", opts);
			}
		});
	},

	mainMenuState(data){
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});

export default new RiskStore();
