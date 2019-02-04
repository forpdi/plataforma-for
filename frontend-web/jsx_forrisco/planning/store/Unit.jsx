import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL + "unit";

var UnitModel = Fluxbone.Model.extend({
	url: URL,
});

var UnitStore = Fluxbone.Store.extend({
	ACTION_NEWPLANRISK: 'unit-newUnit',
	ACTION_RETRIEVE_PLANRISK: 'unit-retrieveUnit',
	ACTION_FIND_UNARCHIVED: 'unit-getAllUnarchived',
	url: URL,
	model: UnitModel,

	newUnit(data) {
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

	retrieveUnit(data) {
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
	}
});

export default new UnitStore();
