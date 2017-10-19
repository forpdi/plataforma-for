import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"field";
var URL_BUDGET = Fluxbone.BACKEND_URL+"budget"

var BudgetModel = Fluxbone.Model.extend({
	url: URL+"/budget",
	url_budget :URL_BUDGET + "/element",
	validate(attrs, options) {
		var errors = [];

		if (string(attrs.name).isEmpty()) {
			errors.push("O nome do orçamento é obrigatório.");
		}
		if (string(attrs.subAction).isEmpty()) {
			errors.push("A subação do orçamento é obrigatória.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var BudgetStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'budget-create',
	ACTION_DESTROY: 'budget-destroy',
	ACTION_FIND: 'budget-find',
	ACTION_RETRIEVE: 'budget-retrieve',
	ACTION_UPDATE: 'budget-update',
	ACTION_GET_BUDGET: 'budget-getBudget',
	ACTION_DELETE: 'budget-delete',
	ACTION_CUSTOM_UPDATE: 'budget-customUpdate',
	dispatchAcceptRegex: /^budget-[a-zA-Z0-9]+$/,
	ACTION_CREATE_BUDGET_ELEMENT: 'budget-createBudgetElement',
	ACTION_GET_BUDGET_ELEMENT: 'budget-getBudgetElement',

	url: URL+"/budget",
	url_budget :URL_BUDGET + "/element",
	model: BudgetModel,

	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/delete",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("budgetDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("budgetDeleted", opts);
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
				me.trigger("budgetUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("budgetUpdated", opts);
			}
		});
	},

	getBudget(data){
		var me = this;
		$.ajax({
			url: me.url+"/budget",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("budgetRetrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	createBudgetElement(data) {
		var me = this;
		$.ajax({
			url: me.url_budget +"/create",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				subAction: data.subAction,
				budgetLoa: data.budgetLoa,
				companyId: data.companyId,
			}),
			success(model) {
				me.trigger("budgetElementSavedSuccess", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getBudgetElement(data){
		var me = this;
		$.ajax({
			url: me.url_budget +"/list",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("budgetElementRetrivied", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	}

});

export default new BudgetStore();
