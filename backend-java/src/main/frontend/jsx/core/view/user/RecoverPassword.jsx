
import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import AppLogo from "forpdi/img/logoLogin.png";


var VerticalForm = Form.VerticalForm;

var ReactToastr = require("react-toastr");
var {ToastContainer} = ReactToastr; 
var ToastMessageFactory = React.createFactory(ReactToastr.ToastMessage);

export default React.createClass({
	getInitialState() {
		return {
			fields: [{
				name: "email",
				type: "email",
				required:true,
				placeholder: "",
				label: Messages.get("label.email")
			}]
		};
	},
	onSubmit(data) {
		if(data.email != ""){
			UserSession.dispatch({
				action: UserSession.ACTION_RECOVER_PASSWORD,
				data: data
			});
		}else{
			this.addAlertError(Messages.get("label.error.emptyEmail"));
		}
	},
	componentWillMount() {
		UserSession.on("recoverpassword", model => {	
			Modal.confirm(
				Messages.get("label.attention"), 
				Messages.get("label.recoveryPasswordRecovery")+ " " + model.message+"." + "  " + Messages.get("label.recoveryPasswordRecoverySpam"),
			() => {
				Modal.hide();
				location.assign("#/");		
			});		
		}, this);
		UserSession.on("fail", msg => {			
			this.addAlertError(msg);	
		}, this);
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},

	addAlertError(msg) {
		this.refs.container.clear(); 
		this.refs.container.error(
			msg,null, {
				timeOut: 5000,
				extendedTimeOut: 10000,
				closeButton:true,
				showAnimation: null,
				hideAnimation: 'flash'
			}
		);
	},
	addAlertSuccess(msg) {
		this.refs.container.clear();
		this.refs.container.success(
			msg,null, {
				timeOut: 5000,
				extendedTimeOut: 10000,
				closeButton:true,
				showAnimation: null,
				hideAnimation: 'flash'
			}
		);
	},

	render() {
		return (
			<div className="container-fluid">
				<ToastContainer ref="container"					
							className="toast-top-center" />
				<div className="row">
					<div className="col-xs-12 text-center">
						<div className="fpdi-login-header">
							<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
							<h3 className="fpdi-login-subtitle">{Messages.get("label.login.titleComplement")}<br/>{Messages.get("label.login.title")}</h3>
						</div>
					</div>
				</div>

		    <div className="row">
				<div className="col-md-4 col-md-offset-4">	
				<div className="fpdi-card-login">		
					<div className="panel panel-default">
					  <div className="panel-heading"><p className="fpdi-login-title"> {Messages.get("label.title.recoverPassword")}</p></div>
					  <div className="panel-body">

					  <p className="fpdi-recover-password-title">{Messages.get("label.emailRecoveryPassword")}</p>
									<div className="fpdi-login-body">
										<VerticalForm
											onSubmit={this.onSubmit}
											fields={this.state.fields}
											store={UserSession}
											submitLabel={Messages.get("label.submit.passwordRecoveryEmail")}
											blockButtons={true}
										/>
									</div>
					  </div>
					</div>
				</div>
				</div>
			</div>

			</div>
		);
	}
});
