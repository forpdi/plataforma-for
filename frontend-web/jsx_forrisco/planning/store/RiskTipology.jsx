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
				me.trigger("newTipology", response);
			},
			error(opts, status, errorMsg) {
				me.trigger("newTipology", opts);
			}
		});
	},
});

export default new TipologyStore();
