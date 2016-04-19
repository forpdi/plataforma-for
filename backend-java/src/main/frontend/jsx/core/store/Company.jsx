
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"company";

var CompanyModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (errors.length > 0)
			return errors;
	}
});

var CompanyStore = Fluxbone.Store.extend({
	ACTION_RETRIEVE: 'company-retrieve',
	ACTION_FIND: 'company-find',
	dispatchAcceptRegex: /^company-[a-zA-Z0-9]+$/,

	url: URL,
	model: CompanyModel

});

export default new CompanyStore();
