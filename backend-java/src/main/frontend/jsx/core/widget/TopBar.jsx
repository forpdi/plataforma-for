
import React from "react";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import Modal from "forpdi/jsx/core/widget/Modal.jsx";

import Logo from 'forpdi/img/logo.png';

export default React.createClass({
	getInitialState() {
		return {
			user: UserSession.get("user"),
			logged: !!UserSession.get("logged")
		};
	},
	componentWillMount() {
		var me = this;
		UserSession.on("login", session => {
			me.setState({
				user: session.get("user"),
				logged: true
			});
		}, me);
		UserSession.on("logout", session => {
			me.setState({
				user: null,
				logged: false
			});
		}, me);
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
    onLogout() {
        UserSession.dispatch({
            action: UserSession.ACTION_LOGOUT
        });
    },
    onSearch(evt, id) {
    	evt.preventDefault();
    	console.log("Searching:",this.refs['search'].value);
    },

    openFeedback(evt) {
    	evt.preventDefault();
    	Modal.feedbackPost();
    },
    openReportProblem(evt) {
    	evt.preventDefault();
    	Modal.reportProblem();
    },

	render() {
		if (!this.state.logged) {
			return <div style={{display: 'none'}} />;
		}
		return (<nav className="navbar navbar-default navbar-fixed-top">
			<div className="container-fluid">
				<div className="navbar-header">
					<button type="button"
						className="navbar-toggle collapsed"
						data-toggle="collapse"
						data-target="#mainNavbar"
						aria-expanded="false">
							<span className="mdi mdi-account" />
					</button>
					<a className="navbar-brand" href="#/">
						<img alt="ForPDI Logo" src={Logo} />
					</a>
				</div>
				<div className="collapse navbar-collapse" id="mainNavbar">
					<ul className="nav navbar-nav navbar-right">
						<li className="hidden">
				            <form onSubmit={this.onSearch} className="fpdi-search-form navbar-form">
				                <div className="form-group form-group-sm has-feedback">
				            		<label htmlFor="navbar-global-search" className="sr-only">Search</label>
				            		<input
				            			type="text"
			            				className="form-control"
			            				name="search"
			            				ref="search"
			            				id="navbar-global-search"
			            				placeholder="Buscar desafios, metas ou ações..."
		            				/>
				            		<span className="mdi mdi-magnify form-control-feedback"></span>
				            	</div>
				            </form>
						</li>
						<li className="dropdown fpdi-dropdown-profile hidden-xs">
							<a
								className="dropdown-toggle"
								data-toggle="dropdown"
								role="button"
								aria-haspopup="true"
								aria-expanded="false">
									<span className="mdi mdi-account"/>
									{this.state.user.name} <span
										className="caret" />
							</a>
							<ul className="dropdown-menu">
								{this.state.user.accessLevel >= 100 ? (
									<li>
										<a href="#/sysadmin">
											Administração
										</a>
									</li>
								):""}
								<li>
									<a href="#/profile">
										Editar perfil
									</a>
								</li>
								<li>
									<a onClick={this.openFeedback}>
										<span className="glyphicon glyphicon-exclamation-sign hidden"></span>
										Enviar feedback
									</a>
								</li>
								<li>
									<a onClick={this.openReportProblem}>
										<span className="glyphicon glyphicon-exclamation-sign hidden"></span>
										Reportar problema
									</a>
								</li>
								<li>
									<a onClick={this.onLogout}>
										<span className="glyphicon glyphicon-log-out hidden"></span>
										Sair
									</a>
								</li>
							</ul>
						</li>

						{this.state.user.accessLevel >= 100 ? (
							<li className="visible-xs">
								<a href="#/sysadmin">
									Administração
								</a>
							</li>
						):""}
						<li className="visible-xs">
							<a href="#/profile">
								Editar perfil
							</a>
						</li>
						<li className="visible-xs">
							<a onClick={this.openFeedback}>
								Enviar feedback
							</a>
						</li>
						<li className="visible-xs">
							<a onClick={this.openReportProblem}>
								Reportar problema
							</a>
						</li>
						<li className="visible-xs">
							<a onClick={this.onLogout}>
								Sair
							</a>
						</li>
					</ul>
				</div>
			</div>
		</nav>);
	  }
	});
