
import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import AppLogo from "forpdi/img/logo.png";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	getInitialState() {
		return {
			fields: [{
				name: "email",
				type: "email",
				placeholder: "",
				label: "E-mail"
			}]
		};
	},
	onSubmit(data) {
		UserSession.dispatch({
			action: UserSession.ACTION_RECOVER_PASSWORD,
			data: data
		});
	},
	componentWillMount() {
		UserSession.on("recoverpassword", model => {
			Modal.info("E-mail de recuperação de senha enviado com sucesso.");
			location.assign("#/");
		}, this);
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
	render() {
		return (
			<div className="container-fluid">
				<div className="row">
					<div className="col-md-4 col-md-offset-4 text-center">
						<div className="fpdi-login-header">
							<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
							<h1>Recupere sua senha</h1>
							<p>Informe o endereço de e-mail cadastrado que enviaremos um link para você recuperar sua senha de acesso.</p>
						</div>
					</div>
				</div>
				<div className="row">
					<div className="col-md-4 col-md-offset-4">
						<div className="fpdi-card">
							<div className="fpdi-login">
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
		);
	}
});
