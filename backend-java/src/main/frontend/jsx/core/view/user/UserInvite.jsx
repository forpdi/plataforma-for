{/* TELA NÂO FAZ PARTE DO SISTEMA */}
import S from 'string';
import React from "react";
import {Link} from 'react-router';

import UserStore from "forpdi/jsx/core/store/User.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

import Messages from 'forpdi/jsx/core/util/Messages.jsx';
//import Toastr from 'toastr';

import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			fields: [{
				name: "name",
				type: "text",
				maxLength: 255,
				required:true,
				placeholder: "",
				label: Messages.get("label.name")
			},{
				name: "email",
				type: "email",
				maxLength: 255,
				required:true,
				placeholder: "",
				label: Messages.get("label.email")
			},{
				name: "accessLevel",
				type: "select",
				maxLength: 255,
				required: true,
				placeholder: Messages.get("label.permissionLevel"),
				label: Messages.get("label.permission"),
				displayField: 'name',
				valueField: 'accessLevel',
				options: AccessLevels.list.filter((level) => {
					return level.accessLevel <= this.context.accessLevel;
				})
			}]
		};
	},
	componentDidMount() {
		var me = this;
		if (!me.context.roles.ADMIN) {
			me.context.router.replace("/home");
			return;
		}
		UserStore.on("sync", (model) => {
			//Toastr.remove();
			//Toastr.success("Usuário convidado com sucesso. Um e-mail foi enviado para que ele conclua o cadastro");
			this.context.toastr.addAlertSuccess(Messages.get("label.success.userInvitation"));
			me.context.router.push("/users");
		}, me);
		UserStore.on("fail", msg =>{
			UserStore.dispatch({
				action: UserStore.ACTION_RETRIEVE
			});
		},me)
	},
	componentWillUnmount() {
		UserStore.off(null, null, this);
	},
	
	onSubmit(data) {
		var me = this;
		var msg = "";
		if (S(data.name).trim().isEmpty()) {
			msg = Messages.get("label.alert.fieldEmpty");
			this.refs.userInviteForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			this.refs.userInviteForm.refs.name.refs["field-name"].className += " borderError";
		} else {
			if(this.refs.userInviteForm.refs.name.refs["field-name"].className && this.refs.userInviteForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				this.refs.userInviteForm.refs.name.refs["field-name"].className = "form-control";
				this.refs.userInviteForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		}
		if (S(data.email).trim().isEmpty()) {
			msg =  Messages.get("label.alert.fieldEmpty");
			this.refs.userInviteForm.refs.email.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			this.refs.userInviteForm.refs.email.refs["field-email"].className += " borderError";
		} else {
			if(this.refs.userInviteForm.refs.email.refs["field-email"].className && this.refs.userInviteForm.refs.email.refs["field-email"].className.indexOf('borderError')){
				this.refs.userInviteForm.refs.email.refs["field-email"].className = "form-control";
				this.refs.userInviteForm.refs.email.refs.formAlertError.innerHTML = "";
			}
		}
		if(S(data.accessLevel).trim().isEmpty()) {
			msg =  Messages.get("label.alert.fieldEmpty");
			this.refs.userInviteForm.refs.accessLevel.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			this.refs.userInviteForm.refs.accessLevel.refs["field-accessLevel"].className += " borderError";
		} else {
			if(this.refs.userInviteForm.refs.accessLevel.refs["field-accessLevel"].className && this.refs.userInviteForm.refs.accessLevel.refs["field-accessLevel"].className.indexOf('borderError')){
				this.refs.userInviteForm.refs.accessLevel.refs["field-accessLevel"].className = "form-control";
				this.refs.userInviteForm.refs.accessLevel.refs.formAlertError.innerHTML = "";
			}
		}
		if (msg != ""){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
			return;
		}


		UserStore.dispatch({
			action: UserStore.ACTION_SIGNUP,
			data: data
		});
		return true;
	},

	render() {
		return (<div className="fpdi-card container animated fadeIn" style={{maxWidth: '600px'}}>
					<h1>
						{Messages.get("label.inviteUser")}
					</h1>
					<p>{Messages.get("label.msg.newEmailUser")}</p>
					<VerticalForm
						ref="userInviteForm"
						onSubmit={this.onSubmit}
						fields={this.state.fields}
						store={UserStore}
						submitLabel={Messages.get("label.submit.sendInvitation")}
					/>
		</div>);
	}
});
