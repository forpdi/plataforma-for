import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import AppLogo from "forpdi/img/logoLogin.png";

export default React.createClass({
	render() {
		return (<div>
			<div className="fpdi-error-404">				
				<div className="row">
					<div className="col-xs-12 text-center">
						<div className="fpdi-login-header">
							<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
							<h3 className="fpdi-login-subtitle">{Messages.get("label.login.titleComplement")}<br/>{Messages.get("label.login.title")}</h3>
						</div>
					</div>
				</div>
				<div className="container-fluid text-center">
					<h1>{Messages.get("label.notFound")}</h1>
					<p>{Messages.get("label.addressNotExist")}</p>
					<a className="btn btn-primary" href="#/">{Messages.get("label.returnToHomePage")}</a>
				</div>
			</div>
		</div>);
	}
});
