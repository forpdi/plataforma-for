import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"planmacro";

var PlanMacroModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (string(attrs.name).isEmpty()) {
			errors.push("O nome do plano é obrigatório.");
		}
		if (string(attrs.begin).isEmpty()) {
			errors.push("A data de início do plano é obrigatória.");
		}
		if (string(attrs.end).isEmpty()) {
			errors.push("A data de término do plano é obrigatória.");
		}

		var begin = new Date(string(attrs.begin).substring(6,11), string(attrs.begin).substring(3,5)-1, string(attrs.begin).substring(0,2));
		var end = new Date(string(attrs.end).substring(6,11), string(attrs.end).substring(3,5)-1, string(attrs.end).substring(0,2));
		var validDates = true;
		if (begin.getFullYear() > end.getFullYear()) {
			validDates = false;
		} else if (begin.getFullYear() == end.getFullYear() && begin.getMonth() > end.getMonth()) {
			validDates = false;
		} else if (begin.getFullYear() == end.getFullYear() && begin.getMonth() == end.getMonth() && begin.getDate() > end.getDate()) {
			validDates = false;
		}
		if (!validDates) {
			errors.push("A data de término deve ser maior que a data de início do plano.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var PlanMacroStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'planmacro-create',
	ACTION_DESTROY: 'planmacro-destroy',
	ACTION_FIND: 'planmacro-find',
	ACTION_RETRIEVE: 'planmacro-retrieve',
	ACTION_UPDATE: 'planmacro-update',
	ACTION_DUPLICATE: "planmacro-duplicate",
	ACTION_NEWPLAN: "planmacro-newPlan",
	ACTION_ARCHIVE: "planmacro-archive",
	ACTION_UNARCHIVE: "planmacro-unarchive",
	ACTION_FIND_ARCHIVED: 'planmacro-findArchived',
	ACTION_FIND_UNARCHIVED: 'planmacro-findUnarchived',
	ACTION_SCHEDULE_RECALCULATION: "planmacro-scheduleRecalculation",
	ACTION_MAIN_MENU_STATE: "planmacro-mainMenuState",
	ACTION_DELETE: "planmacro-delete",
	dispatchAcceptRegex: /^planmacro-[a-zA-Z0-9]+$/,

	url: URL,
	model: PlanMacroModel,

	duplicate(data){		
		var me = this;
		$.ajax({
			url: me.url+"/duplicate",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				macro: data.macro,
				keepPlanLevel: data.keepPlanLevel,
				keepPlanContent: data.keepPlanContent,
				keepDocSection: data.keepDocSection,
				keepDocContent: data.keepDocContent
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
			url: me.url+"/archivedplanmacro",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("archivedplanmacrolisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("archivedplanmacrolisted", opts);
			}
		});
	},

	findUnarchived(data){	
		var me = this;
		$.ajax({
			url: me.url+"/unarchivedplanmacro",
			method: 'GET',
			dataType: 'json',
			contentType: 'application/json',
			success(model) {
				me.trigger("unarchivedplanmacrolisted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("unarchivedplanmacrolisted", opts);
			}
		});
	},

	newPlan(data){		
		var me = this;
		$.ajax({
			url: me.url,
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				plan: data
			}),
			success(model) {
				me.trigger("planmacrocreated", model);
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
				me.trigger("planmacroarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("planmacroarchived", opts);
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
				me.trigger("planmacrounarchived", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("planmacrounarchived", opts);
			}
		});
	},

	delete(data){		
		var me = this;
		$.ajax({
			url: me.url+"/"+data.id,
			method: 'DELETE',
			success(model) {
				me.trigger("plan-deleted", model, data);
			},
			error(opts, status, errorMsg) {
				me.trigger("plan-deleted", opts);
			}
		});
	},

	scheduleRecalculation(id){		
		var me = this;
		$.ajax({
			url: me.url+"/"+id+"/schedule-recalculation",
			method: 'POST',
			dataType: 'json',
			success(model) {
				me.trigger("recalculation-scheduled", model);
			},
			error(opts, response, errorMsg) {
				me.handleRequestErrors([], response);
			}
		});
	},

	mainMenuState(data){
		var me = this;
		me.trigger("getmainmenustate", data);
	}
});

export default new PlanMacroStore();
