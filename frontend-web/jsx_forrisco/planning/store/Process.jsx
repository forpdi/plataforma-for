import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = `${Fluxbone.BACKEND_URL}process`;

var processModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];
		if (string(attrs.name).isEmpty()) {
			errors.push("O nome do processo é obrigatório.");
		}
		if (errors.length > 0)
			return errors;
	}
});

var ProcessStore = Fluxbone.Store.extend({
	ACTION_LIST_BY_UNIT: 'process-listProcessByUnit',
	ACTION_LIST_BY_PLAN: 'process-listProcessByPlan',
	ACTION_CREATE: 'process-newProcess',
	ACTION_DELETE: 'process-deleteProcess',
	ACTION_UPDATE: 'process-updateProcess',
	dispatchAcceptRegex: /^process-[a-zA-Z0-9]+$/,
	url: URL,
	model: processModel,

	listProcessByUnit(data) {
		var me = this;
		var { id, page, pageSize } = data;
		$.ajax({
			url: `${me.url}/${id}`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: { page, pageSize },
			success(model) {
				me.trigger("processListedByUnit", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("processListedByUnit", opts);
			}
		});
	},

	listProcessByPlan(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/allByPlan`,
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: {planId: data},
			success(model) {
				me.trigger("listProcessByPlan", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("listProcessByPlan", opts);
			}
		});
	},

	newProcess(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/new`,
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data.process),
			success(model) {
				me.trigger("processCreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("processCreated", opts);
			}
		});
	},

	deleteProcess(data) {
		var me = this;
		$.ajax({
			url: `${me.url}/${data.processId}`,
			method: 'DELETE',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("processDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("processDeleted", opts);
			}
		});
	},

	updateProcess(data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'PUT',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data.process),
			success(model) {
				me.trigger("processUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("processUpdated", opts);
			}
		});
	},

	mainMenuState(data){
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});


export default new ProcessStore();
