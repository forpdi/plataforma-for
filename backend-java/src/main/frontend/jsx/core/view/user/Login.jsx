
import React from "react";
import {Link} from "react-router";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
//import Toastr from 'toastr';

import Messages from "forpdi/jsx/core/util/Messages.jsx";

import AppLogo from "forpdi/img/logoLogin.png";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

var Validate = Validation.validate;

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			loaded: !UserSession.get("loading"),
			fields: [{
				name: "email",
				type: "email",
				placeholder: "",
				required:true,
				label: Messages.get("label.email")
			},{
				name: "password",
				type: "password",
				placeholder: "",
				required:true,
				label: Messages.get("label.password")
			}]
		};
	},
	onSubmit(data) {
		Validate.validationLogin(this.refs["login"]);

		UserSession.dispatch({
			action: UserSession.ACTION_LOGIN,
			data: data
		});
	},
	componentWillMount() {
		var me = this;
		UserSession.on("login", model => {
			var url = window.location.href.split("#");
			var path = url[1].split("?");
			if (path[0] == "/login")
				location.assign("#/home");
		}, me);
		UserSession.on("loaded", () => {
			me.setState({loaded: true});
		}, me);

	},
	componentDidMount() {
		if (!!UserSession.get("logged")) {
			location.assign("#/home");
		} else {
			this.setState({loaded: true});
		}
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
	render() {
		if (!this.state.loaded) {
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
				<div className="col-md-4 col-md-offset-4">	
					<div className="fpdi-card-login">		
						<div className="panel panel-default">
						  <div className="panel-heading"><p className="fpdi-login-title">{Messages.get("label.login")}</p></div>
							  <div className="panel-body">
									<div className="fpdi-login-body">
										<VerticalForm
											ref="login"
											onSubmit={this.onSubmit}
											fields={this.state.fields}
											store={UserSession}
											hideCanel={true}
											submitLabel={Messages.get("label.LogIn")}
											blockButtons={true}
											confirmKey={13}
											id="login-form"
										/>
									</div>
									
									<div className="fpdi-login-footer">
										<div className="row">
											<div className="col-md-12 text-center marginBottom10">
												<Link to="/recover-password">{Messages.get("label.recoverPassword")}</Link>
											</div>
										</div>
									</div>
							  </div>
						</div>
					</div>

					<div className="fpdi-browsers-info">
						{Messages.get("label.infoBrowsers")}<br/>
						<i>{Messages.get("label.browsers")}</i>
					</div>
				</div>
			</div>



			</div>
		);
	}
});
