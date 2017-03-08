
import AppLogo from "forpdi/img/logoLogin.png";

import React from "react";

export default React.createClass({
	render() {
		return (<div>
			<div className="fpdi-error-404">				
				<div className="row">
					<div className="col-xs-12 text-center">
						<div className="fpdi-login-header">
							<img className="fpdi-login-brand" src={AppLogo} alt="ForPDI Logo" />
							<h3 className="fpdi-login-subtitle">Plataforma Aberta para Gestão e Acompanhamento do<br/>Plano de Desenvolvimento Institucional - PDI</h3>
						</div>
					</div>
				</div>
				<div className="container-fluid text-center">
					<h1>Oops. Não encontramos a página que você está procurando.</h1>
					<p>Você pode ter digitado algum endereço errado ou a página foi movida deste lugar.</p>
					<a className="btn btn-primary" href="#/">Voltar para a página inicial</a>
				</div>
			</div>
		</div>);
	}
});
