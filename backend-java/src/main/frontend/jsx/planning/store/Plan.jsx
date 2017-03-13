
import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"plan";

var PlanModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		{/*if (string(attrs.name).isEmpty()) {
			errors.push("O nome do plano é obrigatório.");
		} */}
		if (string(attrs.begin).isEmpty()) {
			errors.push("A data de início do plano é obrigatória.");
		}
		if (string(attrs.end).isEmpty()) {
			errors.push("A data de término do plano é obrigatória.");
		}

		if (errors.length > 0)
			return errors
	}
});

var PlanStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'plan-create',
	ACTION_DESTROY: 'plan-destroy',
	ACTION_FIND: 'plan-find',
	ACTION_FIND_TERMS:'plan-findTerms',
	ACTION_RETRIEVE: 'plan-retrieve',
	ACTION_RETRIEVE_PERFORMANCE: 'plan-retrievePerformance',
	ACTION_UPDATE: 'plan-update',
	ACTION_CUSTOM_UPDATE: 'plan-customUpdate',
	dispatchAcceptRegex: /^plan-[a-zA-Z0-9]+$/,

	url: URL,
	model: PlanModel,

	customUpdate(data) {
		var me = this;
		$.ajax({
			url: me.url+"/update",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("planUpdated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("planUpdated", opts);
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
				me.trigger("planFind", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("planFind", opts);
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
	}

});

export default new PlanStore();
