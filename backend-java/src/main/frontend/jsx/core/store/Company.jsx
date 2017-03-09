
import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"company";

var CompanyModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (!attrs.name || (attrs.name == '')) {
			errors.push("O nome da instituição é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var CompanyStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'company-create',
	ACTION_DESTROY: 'company-destroy',
	ACTION_FIND: 'company-find',
	ACTION_RETRIEVE: 'company-retrieve',
	ACTION_UPDATE: 'company-update',
	ACTION_FIND_THEMES: 'company-findThemes',
	ACTION_REMOVE_COMPANY:'company-removeCompany',
	dispatchAcceptRegex: /^company-[a-zA-Z0-9]+$/,
	

	url: URL,
	model: CompanyModel,

	removeCompany(id) {

		var me = this;
		$.ajax({
			method: "DELETE",
			url: me.url+"/"+id,
			dataType: 'json',
			success(data) {
				me.trigger("remove", data);
			},
			error: (model,response,opts) => {
				me.trigger("remove",model);
			}
		});
	},

	findThemes(data) {
		var me = this;
		if (typeof me._themes === 'undefined') {
			$.ajax({
				url: me.url+"/themes",
				method: 'GET',
				dataType: 'json',
				success(data) {
					me._themes = data;
					me.trigger("themes", me._themes);
				}
			});
		} else {
			_.defer(() => {
				me.trigger("themes", me._themes);
			});
		}
	}

});

export default new CompanyStore();
