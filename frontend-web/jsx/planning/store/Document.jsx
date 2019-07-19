
import _ from 'underscore';
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import string from "string";

var URL = Fluxbone.BACKEND_URL+"document";

var DocumentModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (string(attrs.title).isEmpty()) {
			errors.push("O título do documento é obrigatório.");
		}
		if (string(attrs.plan).isEmpty()) {
			errors.push("O plano do documento é obrigatória.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var DocumentStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'document-create',
	ACTION_DESTROY: 'document-destroy',
	ACTION_FIND: 'document-find',
	ACTION_RETRIEVE: 'document-retrieve',
	ACTION_UPDATE: 'document-update',
	ACTION_CREATE_SECTION: 'document-createSection',
	ACTION_RETRIEVE_SECTIONATTRIBUTES: 'document-retrieveSectionAttributes',
	ACTION_SAVE_SECTIONATTRIBUTES: 'document-saveSectionAttributes',
	ACTION_CREATE_SECTION_ATTRIBUTE: "document-createNewAttribute",
	ACTION_RETRIEVE_FILLED_SECTIONS: 'document-retrieveFilledSections',
	ACTION_DELETE_SECTION: 'document-deleteSection',
	ACTION_DELETE_ATTRIBUTE: 'document-deleteAttribute',
	ACTION_EXPORT_DOCUMENT: 'document-exportDocument',
	ACTION_EDIT_ATTRIBUTE: 'document-editAttribute',
	dispatchAcceptRegex: /^document-[a-zA-Z0-9]+$/,


	url: URL,
	model: DocumentModel,

	createSection(data, pigback) {
		var me = this;
		$.ajax({
			url: me.url+"/"+data.document+"/section",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(response) {
				me.trigger("sectioncreated", response.data, pigback);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	retrieveSectionAttributes(data) {
		var me = this;
		$.ajax({
			url: me.url+"/sectionattributes",
			method: 'GET',
			dataType: 'json',
			data: data,
			success(response) {
				me.trigger("sectionAttributesRetrieved", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	saveSectionAttributes(data) {
		var me = this;
		$.ajax({
			url: me.url+"/sectionattributes",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				documentSection: data.documentSection
			}),
			success(model) {
				me.trigger("sectionAttributesSaved", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},


	retrieveFilledSections(data) {
		var me = this;
		$.ajax({
			url: me.url+"/" + data.id + "/filledsections",
			method: 'GET',
			dataType: 'json',
			success(response) {
				me.trigger("filledSectionsRetrieved", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	createNewAttribute(data) {
		var me = this;
		$.ajax({
			url: me.url+"/section/"+data.section+"/attribute",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(response) {
				me.trigger("attributecreated", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	deleteSection(data){
		var me = this;
		$.ajax({
			url: me.url+"/section/delete",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(response) {
				me.trigger("sectiondeleted", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	deleteAttribute(data){
		var me = this;
		$.ajax({
			url: me.url+"/sectionattribute/delete",
			method: 'POST',
			dataType: 'json',
			data: data,
			success(response) {
				me.trigger("attributedeleted", response.data);
			},
			error(opts, status, errorMsg) {
				me.trigger("attributedeletedError", opts);
				me.handleRequestErrors([], opts);
			}
		});
	},

	editAttribute(data){
		var me = this;
		$.ajax({
			url: me.url+"/sectionattribute/edit",
			method: 'POST',
			dataType: 'json',
			data: {
				id: data.attr.id || (data.attr.tableFields != undefined ? data.attr.tableFields.attributeId : undefined),
				name: data.attr.name
			},
			success(response) {
				me.trigger("attributeedited", response.data);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	}

});

export default new DocumentStore();
