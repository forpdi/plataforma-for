
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"companydomain";

var CompanyDomainModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (!attrs.host || (attrs.host == '')) {
			errors.push("O host é obrigatório.");
		}
		if (!attrs.baseUrl || (attrs.baseUrl == '')) {
			errors.push("A URL base é obrigatório.");
		}
		if (!attrs.theme || (attrs.theme == '')) {
			errors.push("O tema é obrigatório.");
		}
		if (!attrs.company || !attrs.company.id || (attrs.company.id == '')) {
			errors.push("A instituição é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var CompanyDomainStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'companydomain-create',
	ACTION_DESTROY: 'companydomain-destroy',
	ACTION_FIND: 'companydomain-find',
	ACTION_RETRIEVE: 'companydomain-retrieve',
	ACTION_UPDATE: 'companydomain-update',
	ACTION_SAVE: 'companydomain-save',
	ACTION_LIST_DOMAINS: 'companydomain-listDomains',
	ACTION_REMOVE_DOMAIN: 'companydomain-removeDomain',
	dispatchAcceptRegex: /^companydomain-[a-zA-Z0-9]+$/,

	url: URL,
	model: CompanyDomainModel,

	removeDomain(id) {
		var me = this;
		console.log(id)
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

	save(data) {
		var me = this;
		$.ajax({
			method: "POST",
			url: me.url,
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				domain: data
			}),
			success(model) {
				me.trigger("sync", model);
			},
			error: (model,response,opts) => {
				var json = JSON.parse(model.responseText);
				me.trigger("fail", json.message);
			}
		});
	},

	listDomains(data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("domains-listed", model, data);
			},
			error: (model,response,opts) => {
				me.trigger("remove",model);
			}
		});
	},

});

export default new CompanyDomainStore();
