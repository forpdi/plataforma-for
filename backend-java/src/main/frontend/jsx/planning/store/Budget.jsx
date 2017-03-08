import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"field";

var BudgetModel = Fluxbone.Model.extend({
	url: URL+"/budget",
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
	ACTION_GET_BUDGET_SIMULATION: 'budget-getBudgetSimulation',
	ACTION_DELETE: 'budget-delete',
	ACTION_CUSTOM_UPDATE: 'budget-customUpdate',
	dispatchAcceptRegex: /^budget-[a-zA-Z0-9]+$/,

	url: URL+"/budget",
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

	getBudgetSimulation(data){
		var me = this;
		$.ajax({
			url: me.url+"/simulation",
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
	}

});

export default new BudgetStore();
