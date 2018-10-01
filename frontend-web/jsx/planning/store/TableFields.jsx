import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"field";

var TableModel = Fluxbone.Model.extend({
	url: URL+"/tableFields",
	validate(attrs, options) {
		var errors = [];

		if (errors.length > 0)
			return errors;
	}
});

var TableStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'table-create',
	ACTION_DESTROY: 'table-destroy',
	ACTION_FIND: 'table-find',
	ACTION_RETRIEVE: 'table-retrieve',
	ACTION_UPDATE: 'table-update',
	ACTION_CUSTOM_SAVE: 'table-customSave',
	ACTION_DELETE: 'table-delete',
	ACTION_SAVE_STRUCTURES: 'table-saveStructures',
	dispatchAcceptRegex: /^table-[a-zA-Z0-9]+$/,

	url: URL+"/tableFields",
	model: TableModel,

	customSave(data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				tableInstance: data.tableInstance,
				tableFieldsId: data.tableFieldsId
			}),
			success(model) {
				if (data.tableInstance.id)
					me.trigger("tableUpdated", model);
				else
					me.trigger("tableSaved", model);
			},
			error(opts, status, errorMsg) {
				if (data.tableInstance.id)
					me.trigger("tableUpdated", opts);
				else
					me.trigger("tableSaved", opts);
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
				me.trigger("tableDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.trigger("tableDeleted", opts);
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
				tableFields: data.tableFields
			}),
			success(response) {				
				me.trigger("tablesavestructures", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	}
});

export default new TableStore();
