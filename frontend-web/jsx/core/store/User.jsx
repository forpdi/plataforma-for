
import Fluxbone from "forpdi/jsx/core/store/Fluxbone.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";


var URL = Fluxbone.BACKEND_URL+"user";

var UserModel = Fluxbone.Model.extend({
	url: URL,
	validate(attrs, options) {
		var errors = [];
		
		if (attrs.name == undefined || attrs.name == "") {
			errors.push("O nome é obrigatório.");
		}
		if (attrs.email == undefined || attrs.email == "") {
			errors.push("O e-mail é obrigatório.");
		}

		if (errors.length > 0)
			return errors;
	}
});

var UserStore = Fluxbone.Store.extend({
	ACTION_SIGNUP: 'user-create',
	ACTION_FIND: 'user-find',
	ACTION_RETRIEVE: 'user-retrieve',
	ACTION_RETRIEVE_USER: 'user-retrieveUser',
	ACTION_REMOVE: 'user-removeFromCompany',
	ACTION_BLOCK: 'user-block',
	ACTION_UNBLOCK: 'user-unblock',
	ACTION_UPDATE: 'user-update',
	ACTION_UPDATE_USER:'user-updateEdit',
	ACTION_UPDATE_PICTURE: 'user-updatePicture',
	ACTION_UPDATE_PICTURE_EDIT_USER: 'user-updatePictureEditUser',
	ACTION_UPDATE_FIELD: 'user-updateField',
	ACTION_RESEND_INVITATION: 'user-resendInvitation',
	ACTION_LIST_PERMISSIONS: 'user-listPermissions',
	ACTION_SAVE_PERMISSIONS: 'user-savePermissions',
	ACTION_USER_PROFILE: 'user-retrieveUserProfile',
	ACTION_UPDATE_NOTIFICATION_SETTINGS: 'user-updateNotificationSettings',
	ACTION_IMPORT_USERS: 'user-importUsers',
	ACTION_REGISTER: 'user-registerUser',
	dispatchAcceptRegex: /^user-[a-zA-Z0-9]+$/,

	url: URL,
	model: UserModel,

	removeFromCompany(id) {
		var me = this;
		$.ajax({
			method: "DELETE",
			url: me.url+"/"+id,
			dataType: 'json',
			success(data) {
				if(data.data){
					me.remove(id);	
				}				
				me.trigger("remove", data);
			},
			error: (model,response,opts) => {
				me.handleRequestErrors([], response);
			}
		});
	},

	block(id) {
		var me = this;
		$.ajax({
			method: "POST",
			url: me.url+"/"+id+"/block",
			dataType: 'json',
			success(data) {
				me.trigger("block", data);
			},
			error: (model,response,opts) => {
				me.handleRequestErrors([], response);
			}
		});
	},

	unblock(id) {
		var me = this;
		$.ajax({
			method: "POST",
			url: me.url+"/"+id+"/unblock",
			dataType: 'json',
			success(data) {
				me.trigger("unblock", data);
			},
			error: (model,response,opts) => {
				me.handleRequestErrors([], response);
			}
		});
	},

	resendInvitation(id) {
		var me = this;
		$.ajax({
			method: "POST",
			url: me.url+"/"+id+"/reinvite",
			dataType: 'json',
			success(data) {
				me.trigger("resend-invitation");
			},
			error: (model,response,opts) => {
				me.handleRequestErrors([], response);
			}
		});
	},

	update(data){
		var me = this;
		$.ajax({
			url: me.url+"/profile",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				user: data.user,
				passwordCurrent: data.currentPassword,
				passwordNew: data.newPassword,
				passwordNewTo: data.newPasswordTwo
			}),
			success(response) {
				me.trigger("useredit",response);
			},
			error(opts, status, errorMsg) {
				me.trigger("useredit",opts);
			}
		});
	},

	updateEdit(data) {
		var me = this;
		$.ajax({
			url: me.url+"/editUser",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				user: data.user,
				passwordNew: data.newPassword
			}),
			success(response) {
				me.trigger("editUser",response);
			},
			error(opts, status, errorMsg) {
				me.trigger("editUser",opts);
			}
		});
	},

	updatePicture(data){	
		var me = this;
		$.ajax({
			url: me.url+"/picture",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				user: data.user,
				url: data.uri
			}),
			success(response) {
				me.trigger("update-picture",response);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	updatePictureEditUser(data){
		var me = this;
		$.ajax({
			url: me.url+"/pictureEditUser",
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify({
				user: data.user,
				url: data.uri
			}),
			success(response) {
				me.trigger("update-picture-user",response);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	retrieveUserProfile(data) {
		var me = this;
		
		$.ajax({
			url: me.url+"/profileUser/"+data,
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			success(model) {
				me.trigger("retrieve-user-profile", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},
	
	retrieveUser (data) {
		var me = this;
		$.ajax({
			url: me.url,
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data:data,
			success(model) {
				me.trigger("retrieve-user", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},


	listPermissions(data){
		var me = this;		
		$.ajax({
			url: me.url+"/permissions",
			method: 'GET',
			dataType: 'json',
			contentType: 'json',
			data: {
				userId: data.userId
			},
			success(model) {
				me.trigger("retrieve-permissions", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	savePermissions(data){
		var me = this;
		$.ajax({
			url: me.url+"/permissions",
			method: 'POST',
			dataType: 'json',
			contentType: 'json',
			data: JSON.stringify({
				list: data.list,
				total: data.total
			}),
			success(model) {
				me.trigger("permissions-saved", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	updateNotificationSettings(data){

		var me = this;
		$.ajax({
			url: me.url+ "/updateNotificationSettings",
			method: 'POST',
			dataType: 'json',
			contentType: 'json',
			data: JSON.stringify({
				id: data.id,
				notificationSetting: data.notificationSetting
			}),
			success(model) {
				me.trigger("notifications-settings-updated", model);
			},
			error(opts, status, errorMsg) {
				me.handleRequestErrors([], opts);
			}
		});
	},

	importUsers(data) {
		var me = this;
		$.ajax({
			method: "POST",
			url: me.url+"/importUsers",
			dataType: 'json',
			contentType: 'json',
			data: JSON.stringify({
				nameList: data.nameList,
				emailList: data.emailList,
				accessList: data.accessList
			}),
			success(data) {
				me.trigger("users-imported", data);
			},
			error: (model,response,opts) => {
				me.handleRequestErrors([], response);
			}
		});
	},

	registerUser(data) {
		var me = this;
		$.ajax({
			method: "POST",
			url: me.url+"/register",
			dataType: 'json',
			contentType: 'json',
			data: JSON.stringify(data),
			success(model) {
				me.trigger("user-registred", model);
			},
			error: (model,response,opts) => {
				me.handleRequestErrors([], response);
			}
		});
	},

});

export default new UserStore();
