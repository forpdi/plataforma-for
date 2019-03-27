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
	ACTION_FIND_BY_UNIT: 'risk-findByUnit',
	ACTION_FIND_BY_SUBUNITS: 'risk-findBySubunits',
	ACTION_MAIN_MENU_STATE: "risk-mainMenuState",
	ACTION_DELETE: "risk-delete",
	ACTION_NEWRISK: "risk-newRisk",
	dispatchAcceptRegex: /^risk-[a-zA-Z0-9]+$/,
	ACTION_CUSTOM_UPDATE: "risk-customUpdate",
	ACTION_FIND_BY_PLAN: 'risk-findByPlan',
	ACTION_FIND_INCIDENTS_BY_PLAN: "risk-findIncdentsByPlan",
	ACTION_FIND_MONITORS_BY_PLAN: "risk-findMonitorsByPlan",
	ACTION_FIND_HISTORY_BY_UNIT:"risk-findHistoryByUnit",
	ACTION_FIND_MONITOR_HISTORY_BY_UNIT:"risk-findMonitorHistoryByUnit",
	ACTION_FIND_RISK: "risk-findRisk",
	ACTION_LIST_PREVENTIVE_ACTIONS:"risk-listPreventiveActions",
	ACTION_NEW_PREVENTIVE_ACTION:"risk-newPreventiveAction",
	ACTION_DELETE_PREVENTIVE_ACTION:"risk-deletePreventiveAction",
	ACTION_UPDATE_PREVENTIVE_ACTION:"risk-updatePreventiveAction",
	ACTION_LIST_MONITOR: "risk-listMonitor",
	ACTION_NEW_MONITOR: "risk-newMonitor",
	ACTION_DELETE_MONITOR: "risk-deleteMonitor",
	ACTION_UPDATE_MONITOR: "risk-updateMonitor",
	ACTION_LIST_INCIDENT: "risk-listIncident",
	ACTION_NEW_INCIDENT: "risk-newIncident",
	ACTION_DELETE_INCIDENT: "risk-deleteIncident",
	ACTION_UPDATE_INCIDENT: "risk-updateIncident",
	ACTION_LIST_CONTINGENCY: "risk-listContingency",
	ACTION_NEW_CONTINGENCY: "risk-newContingency",
	ACTION_DELETE_CONTINGENCY: "risk-deleteContingency",
	ACTION_UPDATE_CONTINGENCY: "risk-updateContingency",
	ACTION_RETRIEVE_ACTIVITIES: "risk-retrieveActivities",
	ACTION_LIST_RISKS_BY_PI: "risk-listRisksByPI",
	ACTION_PAGINATE_INCIDENTS: 'risk-paginateIncidents',
	ACTION_FIND_INCIDENTS_BY_UNIT: 'unit-findIncidentsByUnit',
	url: URL,
	model: RiskModel,

	findRisk(data){
		var me = this;
		$.ajax({
			url: me.url+"/"+data,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("findRisk", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("findRisk", opts);
			}
		});
	},


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

	findByUnit(data, payload){
		var me = this;
		$.ajax({
			url: `${me.url}/listbyunit/${data.unitId}`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("riskbyunit", model, payload);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskbyunit", opts);
			}
		});
	},

	findBySubunits(data, node) {
		var me = this;
		$.ajax({
			url: `${me.url}/listbysubunits/${data.unit.id}`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("riskbysubunits", model, node);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskbysubunits", opts);
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
				me.trigger("riskDelete", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskDelete", opts);
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
	findHistoryByUnit(data){
		var me = this;
		$.ajax({
			url: me.url+"/history",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {unitId: data.unit, planId: data.plan},
			success(model) {
				me.trigger("historyByUnit", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("historyByUnit", opts);
			}
		});
	},
	findIncdentsByPlan(data){
		var me = this;
		$.ajax({
			url: me.url+"/incidents",
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
			url: me.url+"/monitors",
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



	findMonitorHistoryByUnit(data){
		var me = this;
		$.ajax({
			url: me.url+"/monitorHistory",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {unitId: data.unit, planId: data.plan},
			success(model) {
				me.trigger("monitorHistoryByUnit", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("monitorHistoryByUnit", opts);
			}
		});
	},

	listMonitor(data) {
		var me = this;
		const { riskId, page, pageSize } = data;
		$.ajax({
			url: me.url + "/monitor",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: { riskId, page, pageSize },
			success(model) {
				me.trigger("monitorListed", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("monitorListed", opts);
			}
		});
	},

	newMonitor(data) {
		var me = this;
		$.ajax({
			url: me.url + '/monitornew',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				monitor: data.monitor,
			}),
			success(model) {
				me.trigger("monitorCreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("monitorCreated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	deleteMonitor(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/monitor/${data.monitorId}`,
			method: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("monitorDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("monitorDeleted", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	updateMonitor(data) {
		var me = this;
		$.ajax({
			url: me.url + '/monitor/update',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				monitor: data.monitor,
			}),
			success(model) {
				me.trigger("monitorUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("monitorUpdated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},


	listPreventiveActions(data) {
		var me = this;
		const { riskId, page, pageSize } = data;
		$.ajax({
			url: me.url + "/action",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: { riskId, page, pageSize },
			success(model) {
				me.trigger("preventiveActionsListed", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("preventiveActionsListed", opts);
			}
		});
	},

	newPreventiveAction(data) {
		var me = this;
		$.ajax({
			url: me.url + '/actionnew',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				action: data.action,
			}),
			success(model) {
				me.trigger("preventiveActionCreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("preventiveActionCreated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	deletePreventiveAction(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/action/${data.actionId}`,
			method: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("preventiveActionDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("preventiveActionDeleted", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	updatePreventiveAction(data) {
		var me = this;
		$.ajax({
			url: me.url + '/action/update',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				action: data.action,
			}),
			success(model) {
				me.trigger("preventiveActionUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("preventiveActionUpdated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},





	listIncident(data) {
		var me = this;
		const { riskId, page, pageSize } = data;
		$.ajax({
			url: me.url + "/incident",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: { riskId, page, pageSize },
			success(model) {
				me.trigger("incidentListed", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("incidentListed", opts);
			}
		});
	},

	newIncident(data) {
		var me = this;
		$.ajax({
			url: me.url + '/incidentnew',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				incident: data.incident,
			}),
			success(model) {
				me.trigger("incidentCreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("incidentCreated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	deleteIncident(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/incident/${data.incidentId}`,
			method: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("incidentDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("incidentDeleted", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	updateIncident(data) {
		var me = this;
		$.ajax({
			url: me.url + '/incident/update',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				incident: data.incident,
			}),
			success(model) {
				me.trigger("incidentUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("incidentUpdated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	listContingency(data) {
		var me = this;
		const { riskId, page, pageSize } = data;
		$.ajax({
			url: me.url + "/contingency",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: { riskId, page, pageSize },
			success(model) {
				me.trigger("contingencyListed", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("contingencyListed", opts);
			}
		});
	},

	newContingency(data) {
		var me = this;
		$.ajax({
			url: me.url + '/contingencynew',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				contingency: data.contingency,
			}),
			success(model) {
				me.trigger("contingencyCreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("contingencyCreated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	deleteContingency(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/contingency/${data.contingencyId}`,
			method: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("contingencyDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("contingencyDeleted", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	updateContingency(data) {
		var me = this;
		$.ajax({
			url: me.url + '/contingency/update',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				contingency: data.contingency,
			}),
			success(model) {
				me.trigger("contingencyUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("contingencyUpdated", {
					msg: opts.responseJSON.message,
				});
				me.handleRequestErrors([], opts);
			}
		});
	},

	retrieveActivities(data) {
		var me = this;
		$.ajax({
			url: me.url + "/activity",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {riskId: data},
			success(model) {
				me.trigger("retrieveActivities", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("retrieveActivities", opts);
			}
		});
	},

	listRisksByPI(data) {
		var me = this;
		$.ajax({
			url: me.url + "/listByPI",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: data,
			success(model) {
				me.trigger("riskByPI", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("riskByPI", opts);
			}
		});
	},

	paginateIncidents(data) {
		var me = this;
		$.ajax({
			url: me.url + "/incidentsPaginated",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success(response) {
				me.trigger("paginatedIncidents", response);
			},
			error(opts, status, errorMsg) {
				me.trigger("paginatedIncidents", opts);
			}
		});
	},

	findIncidentsByUnit() {
		var me = this;
		$.ajax({
			url: me.url + "/incidentByUnit",
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

	mainMenuState(data){
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});

export default new RiskStore();
