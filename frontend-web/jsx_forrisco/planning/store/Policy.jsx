import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"policy";

var PolicyModel = Fluxbone.Model.extend({
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

var PolicyStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'policy-create',
	ACTION_DESTROY: 'policy-destroy',
	ACTION_FIND: 'policy-find',
	ACTION_RETRIEVE: 'policy-retrieve',
	ACTION_UPDATE: 'policy-update',
	ACTION_DUPLICATE: "policy-duplicate",
	ACTION_ARCHIVE: "policy-archive",
	ACTION_UNARCHIVE: "policy-unarchive",
	ACTION_FIND_ARCHIVED: 'policy-findArchived',
	ACTION_FIND_UNARCHIVED: 'policy-findUnarchived',
	ACTION_MAIN_MENU_STATE: "policy-mainMenuState",
	ACTION_DELETE: "policy-delete",
	ACTION_NEWPOLICY: "policy-newPolicy",
	dispatchAcceptRegex: /^policy-[a-zA-Z0-9]+$/,

	url: URL,
	model: PolicyModel,

	duplicate(data){
		var me = this;
		$.ajax({
			url: me.url+"/duplicate",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				macro: data.macro
			}),
			success(model) {
				me.trigger("planmacroduplicated", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("planmacroduplicated", opts);
			}
		});
	},

	findArchived(data){
		var me = this;
		$.ajax({
			url: me.url+"/archivedpolicy",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("archivepolicylisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("archivedpolicylisted", opts);
			}
		});
	},


	findUnarchived(data){
		var me = this;
		$.ajax({
			url: me.url+"/unarchivedpolicy",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unarchivedpolicylisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unarchivedpolicylisted", opts);
			}
		});
	},

	newPolicy(data){
		var me = this;

		$.ajax({
			url: me.url+'/new',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				policy: data
			}),
			success(model) {
				me.trigger("policycreated", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], response);
			}
		});
	},

	archive(data){
		var me = this;
		$.ajax({
			url: me.url+"/archive",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				id: data.id
			}),
			success(model) {
				me.trigger("policyarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("policyarchived", opts);
			}
		});
	},

	unarchive(data){
		var me = this;
		$.ajax({
			url: me.url+"/unarchive",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				id: data.id
			}),
			success(model) {
				me.trigger("policyunarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("policyunarchived", opts);
			}
		});
	},

	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/"+data.id,
			method: 'DELETE',
			success(model) {
				me.trigger("policy-deleted", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("policy-deleted", opts);
			}
		});
	},

	mainMenuState(data){
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});

export default new PolicyStore();
