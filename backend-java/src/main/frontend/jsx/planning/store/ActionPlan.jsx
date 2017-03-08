import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"field";

var ActionPlanModel = Fluxbone.Model.extend({
	url: URL+"/actionplan",
	validate(attrs, options) {
		var errors = [];

		if (string(attrs.name).isEmpty()) {
			errors.push("O nome do plano de ação é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var ActionPlanStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'actionPlan-create',
	ACTION_DESTROY: 'actionPlan-destroy',
	ACTION_FIND: 'actionPlan-find',
	ACTION_RETRIEVE: 'actionPlan-retrieve',
	ACTION_UPDATE: 'actionPlan-update',
	ACTION_DELETE: 'actionPlan-delete',
	ACTION_CUSTOM_CREATE: 'actionPlan-customCreate',
	ACTION_CUSTOM_UPDATE: 'actionPlan-customUpdate',
	ACTION_CHECKBOX_UPDATE: 'actionPlan-checkboxUpdate',
	ACTION_RETRIVE_ACTION_PLAN_ATTRIBUTE: 'actionPlan-retrieveActionPlanAttribute',
	dispatchAcceptRegex: /^actionPlan-[a-zA-Z0-9]+$/,


	url: URL+"/actionplan",
	model: ActionPlanModel,

	customCreate(data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				actionPlan: data.actionPlan,
				levelInstanceId: data.instanceId,
				begin: data.begin,
				end: data.end,
				url: data.url
			}),
			success(model) {
				me.trigger("actionPlanSavedSuccess", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("actionPlanSavedSuccess", opts);
			}
		});
	},

	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/delete",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("actionPlanDeletedSuccess", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("actionPlanDeletedError", opts);
			}
		});
	},

	customUpdate(data){
		var me = this;
		$.ajax({
			url: me.url+"/update",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("actionPlanEdited", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("actionPlanEdited", opts);
			}
		});
	},

	checkboxUpdate(data) {
		var me = this;
		$.ajax({
			url: me.url+"/update/checkbox",
			method: 'POST',
			dataType: 'json',
			data: ({
				id: data.id,
				checked: data.checked,
				url:data.url
			}),
			success(model) {
				me.trigger("actionPlanCompleted", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	retrieveActionPlanAttribute (data) {
		var me = this;
		$.ajax({
			url: me.url+"/listActionPlanAttribute",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("listActionPlanRetrieved", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});

	}

});

export default new ActionPlanStore();
