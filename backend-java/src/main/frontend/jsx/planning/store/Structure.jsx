
import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"structure";

var StructureModel = Fluxbone.Model.extend({
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

var StructureStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'structure-create',
	ACTION_DESTROY: 'structure-destroy',
	ACTION_FIND: 'structure-find',
	ACTION_RETRIEVE: 'structure-retrieve',
	ACTION_UPDATE: 'structure-update',
	dispatchAcceptRegex: /^structure-[a-zA-Z0-9]+$/,

	url: URL,
	model: StructureModel

});

export default new StructureStore();
