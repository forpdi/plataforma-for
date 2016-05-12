
import React from "react";
import {Link} from "react-router";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

import AppLogo from "forpdi/img/logo.png";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	getInitialState() {
		return {
			fields: [{
				name: "name",
				type: "text",
				placeholder: "",
				label: Messages.get("label.name")
			},{
				name: "cpf",
				type: "cpf",
				placeholder: "",
				label: Messages.get("label.cpf")
			},{
				name: "cellphone",
				type: "tel",
				placeholder: "",
				label: Messages.get("label.cellphone")
			},{
				name: "phone",
				type: "tel",
				placeholder: "",
				label: Messages.get("label.phone")
			},{
				name: "department",
				type: "text",
				placeholder: "",
				label: Messages.get("label.department")
			},{
				name: "birthdate",
				type: "date",
				placeholder: "",
				label: Messages.get("label.birthdate")
			},{
				name: "password",
				type: "password",
				placeholder: "",
				label: Messages.get("label.password")
			},{
				name: "passwordconfirm",
				type: "password",
				placeholder: "",
				label: Messages.get("label.passwordconfirm")
			},{
				name: "token",
				type: "hidden",
				placeholder: "",
				value: this.props.params.token
			}],
			loading: true,
			valid: false
		};
	},
	onSubmit(data) {
		UserSession.dispatch({
			action: UserSession.ACTION_REGISTER_USER,
			data: data
		});
	},
	componentDidMount() {
		var me = this;
		UserSession.on("register", model => {
			Modal.alert("Sucesso", "O seu cadastro foi concluído com sucesso.");
			location.assign("#/");
		}, me);
		UserSession.on("registertoken", valid => {
			me.setState({
				valid: valid,
				loading: false
			});
		}, me);

		UserSession.dispatch({
			action: UserSession.ACTION_CHECK_REGISTER_TOKEN,
			data: this.props.params.token
		});
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (
			<div className="container-fluid">
				<div className="row">
					{this.state.valid ?
						<div className="col-md-4 col-md-offset-4">
							<div className="fpdi-login-header">
								<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
								<h1>Finalize seu cadastro</h1>
								<p>Informe seus dados para finalizar seu cadastro.</p>
							</div>
							<div className="fpdi-card">
								<div className="fpdi-login">
									<div className="fpdi-login-body">
										<VerticalForm
											onSubmit={this.onSubmit}
											fields={this.state.fields}
											store={UserSession}
											submitLabel="Redefinir senha"
											blockButtons={true}
										/>
									</div>
								</div>
							</div>
						</div>
						:
						<div className="col-md-4 col-md-offset-4">
							<div className="fpdi-login-header">
								<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
								<h1>Esta página não está mais disponível.</h1>
								<p><a className="btn btn-sm btn-primary" href="#/">Retornar à página inicial</a></p>
							</div>
						</div>
					}
				</div>
			</div>
		);
	}
});
