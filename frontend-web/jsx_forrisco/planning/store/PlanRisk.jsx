import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item";

var URL = Fluxbone.BACKEND_URL + "planrisk";

var PlanRiskModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];
		if (string(attrs.name).isEmpty()) {
			errors.push("O nome da política é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var PlanRiskStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'planRisk-create',
	ACTION_DESTROY: 'planRisk-destroy',
	ACTION_FIND: 'planRisk-find',
	ACTION_RETRIEVE: 'planRisk-retrieve',
	ACTION_UPDATE: 'planRisk-update',
	ACTION_NEWPLANRISK: 'planRisk-newPlanRisk',
	ACTION_FIND_UNARCHIVED: 'planRisk-findUnarchived',
	url: URL,
	model: PlanRiskModel,

	newPlanRisk(data) {
		$.ajax({
			url: this.url + '/new',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success(model) {
				this.trigger("plariskcreated", model);
			},
			error(opts, status, errorMsg) {
				this.trigger("plariskcreated", {msg: opts})
				this.handleRequestErrors([], opts);
			}
		});
	},


	findUnarchived(data) {
		$.ajax({
			url: this.url + '/unarchivedplanrisk',
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success(model) {
				this.trigger("unarchivedplanrisklisted", model);
			},
			error(opts, status, errorMsg) {
				this.trigger("unarchivedplanrisklisted", {msg: opts})
				this.handleRequestErrors([], opts);
			}
		});
	}
});

export default new PlanRiskStore();
