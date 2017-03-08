import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"field";

var ScheduleModel = Fluxbone.Model.extend({
	url: URL+"/schedule",
	validate(attrs, options) {
		var errors = [];

		if (errors.length > 0)
			return errors;
	}
});

var ScheduleStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'schedule-create',
	ACTION_DESTROY: 'schedule-destroy',
	ACTION_FIND: 'schedule-find',
	ACTION_RETRIEVE: 'schedule-retrieve',
	ACTION_UPDATE: 'schedule-update',
	ACTION_CUSTOM_SAVE: 'schedule-customSave',
	ACTION_DELETE: 'schedule-delete',
	ACTION_SAVE_STRUCTURES: 'schedule-saveStructures',
	dispatchAcceptRegex: /^schedule-[a-zA-Z0-9]+$/,

	url: URL+"/schedule",
	model: ScheduleModel,

	customSave(data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				scheduleInstance: data.scheduleInstance,
				scheduleId: data.scheduleId,
				beginDate: data.beginDate,
				endDate: data.endDate
			}),
			success(model) {
				if (data.scheduleInstance.id)
					me.trigger("scheduleUpdated", model);
				else
					me.trigger("scheduleSaved", model);
			},
			error(opts, status, errorMsg) {
				if (data.scheduleInstance.id)
					me.trigger("scheduleUpdated", opts);
				else
					me.trigger("scheduleSaved", opts);
			}
		});
	},
	delete(data){
		var me = this;
		$.ajax({
			url: me.url+"/delete",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(model) {
				me.trigger("scheduleDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("scheduleDeleted", opts);
			}
		});
	},
	saveStructures(data){
		var me = this;
		$.ajax({
			url: me.url+"/structures",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				schedule: data.schedule
			}),
			success(model) {
				me.trigger("schedulestructurescreated", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	}

});

export default new ScheduleStore();
