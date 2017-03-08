import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"attachment";

var AttachmentModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (string(attrs.fileLink).isEmpty()) {
			errors.push("O link do arquivo é obrigatório.");
		}
		if (string(attrs.name).isEmpty()) {
			errors.push("O nome do arquivo é obrigatório.");
		}
		if (!attrs.levelInstance) {
			errors.push("O nível do arquivo é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var AttachmentStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'attachment-create',
	ACTION_DESTROY: 'attachment-destroy',
	ACTION_FIND: 'attachment-getAttachment',
	ACTION_RETRIEVE: 'attachment-retrieve',
	ACTION_UPDATE: 'attachment-update',
	ACTION_DELETE: 'attachment-delete',
	ACTION_UPDATE_DESCRIPTION: 'attachment-updateDescription',
	ACTION_DELETE_SELECTED: 'attachment-deleteSelected',	
	dispatchAcceptRegex: /^attachment-[a-zA-Z0-9]+$/,

	url: URL,
	model: AttachmentModel,

	delete(data){
		var me = this;		
		$.ajax({
			url: me.url+"/delete",
			method: 'POST',
			dataType: 'json',			
			data: data,
			success(model) {
				me.trigger("attachmentDeleted", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	getAttachment(data){
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: data,
			success(model) {
				me.trigger("attachmentList", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	updateDescription(data){
		var me = this;		
		$.ajax({
			url: me.url+"/update",
			method: 'POST',
			dataType: 'json',
			contentType: 'json',
			data: JSON.stringify({
				attachment: {
					id: data.id,
					description: data.description
				}
			}),
			success(model) {
				me.trigger("attachmentUpdated", model);
			},
			error(opts, status, errorMsg) {				
				me.handleRequestErrors([], opts);
			}
		});
	},

	deleteSelected(data){
		var me = this;		
		$.ajax({
			url: me.url+"/deleteList",
			method: 'POST',
			dataType: 'json',
			contentType: 'json',
			data: JSON.stringify({
				attachmentList: {
					list: data.list,
					total: data.total
				}
			}),
			success(model) {
				me.trigger("attachmentDeleted", model);
			},
			error(opts, status, errorMsg) {				
				me.handleRequestErrors([], opts);
			}
		});
	}

});

export default new AttachmentStore();
