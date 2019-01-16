import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL + "planrisk";

var PlanRiskModel = Fluxbone.Model.extend({
	url: URL,
});

var PlanRiskStore = Fluxbone.Store.extend({
	ACTION_NEWPLANRISK: 'planRisk-newPlanRisk',
	ACTION_RETRIEVE_PLANRISK: 'planRisk-retrievePlanRisk',
	ACTION_FIND_UNARCHIVED: 'planRisk-getAllUnarchived',
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

	retrievePlanRisk(id) {
		var me = this;
		var model = new me.model();
		model.fetch({
			url: this.url + '/' + id,
			success(model) {
				me.trigger("retrivedPlanRisk");
			},
			error(model, response, options) {
				me.handleRequestErrors([], options.xhr);
			}
		});
	}
});

export default new PlanRiskStore();
