
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"contact";

var ContactModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if ((!attrs.subject || (attrs.subject == '')) && (!attrs.screen || (attrs.screen == ''))) {
			errors.push("Por favor, selecione uma opção.");
		}
		if (!attrs.description || (attrs.description == '')) {
			errors.push("Por favor, preencha a descrição.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var ContactStore = Fluxbone.Store.extend({
	ACTION_CREATE_FEEDBACK: 'contact-createFeedback',
	ACTION_CREATE_PROBLEM: 'contact-createProblem',
	ACTION_UPDATE_FIELD: 'contact-updateField',
	ACTION_FIND: 'contact-find',
	dispatchAcceptRegex: /^contact-[a-zA-Z0-9]+$/,

	url: URL,
	model: ContactModel,

	createFeedback(data) {
		var me = this;
		me.create(data, {
			url: me.url+"/feedback"
		});
	},
	createProblem(data) {
		var me = this;
		me.create(data, {
			url: me.url+"/problem"
		});
	}

});

export default new ContactStore();
