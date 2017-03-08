
import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

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
				label: "E-mail"
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
			this.addAlertError("Campo de e-mail vazio!");
		}
	},
	componentWillMount() {
		UserSession.on("recoverpassword", model => {	
			Modal.confirm(
				"Atenção", 
				"Um e-mail de recuperação de senha foi enviado para "+model.message+"." + " Se o e-mail demorar a chegar, verifique o span.",
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
							<h3 className="fpdi-login-subtitle">Plataforma Aberta para Gestão e Acompanhamento do<br/>Plano de Desenvolvimento Institucional - PDI</h3>
						</div>
					</div>
				</div>

		    <div className="row">
				<div className="col-md-4 col-md-offset-4">	
				<div className="fpdi-card-login">		
					<div className="panel panel-default">
					  <div className="panel-heading"><p className="fpdi-login-title">Recupere sua senha</p></div>
					  <div className="panel-body">

					  <p className="fpdi-recover-password-title">Informe o endereço de e-mail cadastrado que enviaremos um link para você recuperar sua senha de acesso.</p>
									<div className="fpdi-login-body">
										<VerticalForm
											onSubmit={this.onSubmit}
											fields={this.state.fields}
											store={UserSession}
											submitLabel="Enviar email de recuperação"
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
