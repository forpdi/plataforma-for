import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Toastr from 'toastr';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import AppLogo from "forpdi/img/logoLogin.png";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	getInitialState() {
		return {
			fields: [{
				name: "password",
				type: "password",
				required:true,
				placeholder: "",
				label: Messages.get("label.password")
			},{
				name: "passwordconfirm",
				type: "password",
				required:true,
				placeholder: "",
				label: Messages.get("passwordConfirm")
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
			action: UserSession.ACTION_RESET_PASSWORD,
			data: data
		});
	},
	componentDidMount() {
		var me = this;
		UserSession.on("resetpassword", model => {
			Toastr.remove();
			Toastr.success(Messages.get("label.sucess.passwordReset"));	
			
			location.assign("#/");
		}, me);
		UserSession.on("recovertoken", valid => {
			me.setState({
				valid: valid,
				loading: false
			});
		}, me);

		UserSession.dispatch({
			action: UserSession.ACTION_CHECK_RECOVER_TOKEN,
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
					<div className="col-xs-12 text-center">
						<div className="fpdi-login-header">
							<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
							<h3 className="fpdi-login-subtitle">{Messages.get("label.login.titleComplement")}<br/>{Messages.get("label.login.title")}</h3>
						</div>
					</div>
				</div>

				<div className="row">
					{this.state.valid ?


							<div className="col-md-4 col-md-offset-4">
								<div className="fpdi-card-login">		
									<div className="panel panel-default">
									  <div className="panel-heading"><p className="fpdi-login-title">{Messages.get("label.resetPassword")}</p></div>
									  	<div className="panel-body">
									  		  <p className="fpdi-recover-password-title">Informe sua nova senha de acesso.</p>
										<div className="fpdi-login-body">
										<VerticalForm
											onSubmit={this.onSubmit}
											fields={this.state.fields}
											store={UserSession}
											submitLabel={Messages.get("label.submit.resetPassword")}
											blockButtons={true}
											cancelUrl={'/login'}
										/>
									</div>
										</div>
									</div>
								</div>
							</div>

						:
						<div className="col-md-4 col-md-offset-4">
							<div className="fpdi-login-header">
								<h1> {Messages.get("label.pageNotAvailable")}.</h1>
								<p><a className="btn btn-sm btn-primary" href="#/">{Messages.get("label.returnToHomePage")}</a></p>
							</div>
						</div>
					}
				</div>
			</div>
		);
	}
});
