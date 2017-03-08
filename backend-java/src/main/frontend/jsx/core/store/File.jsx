import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";

var URL = Fluxbone.BACKEND_URL+"file";

var FileModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];

		if (errors.length > 0)
			return errors;
	}
});


var FileStore = Fluxbone.Store.extend({
	ACTION_CREATE: 'file-create',
	ACTION_FIND: 'file-find',
	ACTION_DESTROY: 'file-destroy',
	ACTION_RETRIEVE: 'file-retrieve',
	ACTION_UPLOAD: 'file-upload',	
	dispatchAcceptRegex: /^file-[a-zA-Z0-9]+$/,

	url: URL,
	model: FileModel,

	upload (){
		var me = this;
		$.ajax({
			method: "POST",
			url: BACKEND_URL + "upload",
			dataType: 'json',
			success(data, status, opts) {
				if (data.success) {			
					me.trigger("uploaded", me);
				} else {
					me.trigger("fail", data.message);
				}				
			},
			error(opts, status, errorMsg) {
				me.trigger("fail", data.message);
			}
		});
	}
	
});

export default new FileStore();
