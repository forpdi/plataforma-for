
import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/widget/form/Form.jsx";
import UserStore from "forpdi/jsx/store/User.jsx";

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
		UserStore.dispatch({
			action: UserStore.ACTION_RECOVER_PASSWORD,
			data: data
		});
	},
	componentWillMount() {
		UserStore.on("sync", model => {
			location.assign("#/");
		}, this);
	},
	componentWillUnmount() {
		UserStore.off(null, null, this);
	},
	render() {
		return (
			<div className="container-fluid">
				<div className="row">
					<div className="col-md-4 col-md-offset-4 text-center">
						<div className="cmp-login-header">
							<img className="cmp-login-brand" src={AppLogo} alt="ForPDI Logo" />
							<h1>Recupere sua senha</h1>
							<p>Informe o endereço de e-mail cadastrado que enviaremos um link para você recuperar sua senha de acesso.</p>
						</div>
					</div>
				</div>
				<div className="row">
					<div className="col-md-4 col-md-offset-4">
						<div className="cmp-card">
							<div className="cmp-login">
								<div className="cmp-login-body">
									<VerticalForm
										onSubmit={this.onSubmit}
										fields={this.state.fields}
										store={UserStore}
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
