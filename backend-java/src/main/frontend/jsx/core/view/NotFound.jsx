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
							<img className="fpdi-login-brand" src={AppLogo} alt={Messages.get("label.forPdiLogo")} />
							<h3 className="fpdi-login-subtitle">{Messages.getEditable("label.login.titleComplement","fpdi-nav-label")}<br/>{Messages.getEditable("label.login.title","fpdi-nav-label")}</h3>
						</div>
					</div>
				</div>
				<div className="container-fluid text-center">
					<h1>{Messages.getEditable("label.notFound","fpdi-nav-label")}</h1>
					<p>{Messages.getEditable("label.addressNotExist","fpdi-nav-label")}</p>
					<a className="btn btn-primary" href="#/">{Messages.getEditable("label.returnToHomePage","fpdi-nav-label")}</a>
				</div>
			</div>
		</div>);
	}
});
