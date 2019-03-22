import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL + "planrisk";

var PlanRiskModel = Fluxbone.Model.extend({
	url: URL,
});

var PlanRiskStore = Fluxbone.Store.extend({
	ACTION_NEWPLANRISK: 'planRisk-newPlanRisk',
	ACTION_RETRIEVE_PLANRISK: 'planRisk-retrievePlanRisk',
	ACTION_FIND_UNARCHIVED_FOR_MENU: 'planRisk-getAllUnarchivedForMenu',
	ACTION_FIND_UNARCHIVED: 'planRisk-getAllUnarchived',
	ACTION_DELETE_PLANRISK: 'planRisk-deletePlanRisk',
	ACTION_EDIT_PLANRISK: 'planRisk-editPlanRisk',
	ACTION_SEARCH_TERMS: 'planRisk-searchTerms',
	ACTION_SEARCH_BY_KEY: 'planRisk-searchTermsByKey',
	//ACTION_DUPLICATE_PLANRISK: 'planRisk-duplicatePlanRisk',
	url: URL,
	model: PlanRiskModel,

	newPlanRisk(data) {
		var me = this;
		$.ajax({
			url: this.url + '/new',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success(model) {
				me.trigger("plariskcreated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("plariskcreated", {msg: opts})
				me.handleRequestErrors([], opts);
			}
		});
	},

	getAllUnarchivedForMenu() {
		var me = this;
		$.ajax({
			url: this.url + '/unarchivedplanrisk',
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unarchivedPlanRiskForMenu", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unarchivedPlanRiskForMenu", opts);
			}
		});
	},

	getAllUnarchived() {
		var me = this;
		$.ajax({
			url: this.url + '/unarchivedplanrisk',
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("listedunarchivedplanrisk", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("listedunarchivedplanrisk", opts);
			}
		});
	},

	retrievePlanRisk(data) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: this.url + '/' + data,
			success(model) {
				me.trigger("retrivedplanrisk", model);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	},

	deletePlanRisk(data) {
		console.log(data);
		var me = this;
		console.log(me);
		console.log(me.url);
		$.ajax({
			url: me.url + "/" + data,
			method: 'DELETE',
			success(model) {
				me.trigger("deletePlanRisk", model);
			},
			error(opts, status, errorMsg) {
				console.log(opts);
				var resp = JSON.parse(opts.responseText);
				me.trigger("deletePlanRisk", resp);
			}
		})
	},

	editPlanRisk(data) {
		console.log("edit plan risk");
		var me = this;
		console.log(me);
		console.log(me.url);
		$.ajax({
			url: me.url + '/update',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success(model) {
				me.trigger("editPlanRisk", model);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		})
	},

	/*duplicatePlanRisk(data) {
		var me = this;
		$.ajax({
			url: me.url + '/duplicate',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success(model) {
				me.trigger("duplicatePlanRisk", model);
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
				me.trigger("duplicatePlanRisk", model);
			}
		})
	},*/

	searchTerms(data) {
		var me = this;
		$.ajax({
			url: me.url + "/search",
			method: 'GET',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("searchTerms", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("searchTerms", opts);
			}
		});
	},

	searchTermsByKey(data) {
		var me = this;
		$.ajax({
			url: me.url + "/searchByKey",
			method: 'GET',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("searchTerms", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("searchTerms", opts);
			}
		});
	}
});

export default new PlanRiskStore();
