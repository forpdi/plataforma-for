
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
	dispatchAcceptRegex: /^company-[a-zA-Z0-9]+$/,

	url: URL,
	model: CompanyModel

});

export default new CompanyStore();
