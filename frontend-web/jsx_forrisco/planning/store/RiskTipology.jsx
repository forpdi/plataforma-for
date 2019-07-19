import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"tipology";

var TipologyModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (errors.length > 0)
			return errors
	}
});

var TipologyStore = Fluxbone.Store.extend({
  ACTION_CREATE: 'tipology-newTipology',
  ACTION_RETRIEVE_ALL_TIPOLOGIES: 'tipology-retrieveAllTipologies',
  dispatchAcceptRegex: /^tipology-[a-zA-Z0-9]+$/,
  url: URL,
  model: TipologyModel,

	newTipology(data) {
		var me = this;
		$.ajax({
			url: me.url+"/new",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({ name: data }),
			success(response) {
				me.trigger("tipologyCreated", response);
			},
			error(opts, status, errorMsg) {
				me.trigger("tipologyCreated", opts);
			}
		});
	},

	retrieveAllTipologies() {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			// dataType: 'json',
			// contentType: 'application/json',
			success(response) {
				me.trigger("allTipologies", response);
			},
			error(opts, status, errorMsg) {
				me.trigger("allTipologies", opts);
			}
		});
	},
});

export default new TipologyStore();
