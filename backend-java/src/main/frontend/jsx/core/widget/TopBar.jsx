
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
		return (<div className="fpdi-top-bar">
			<span className="fpdi-fill" />
			<span className="mdi mdi-account-circle" style={{marginRight: '5px'}} />
			<span>{this.state.user.name}</span>
			<span title="Notificações" className="mdi mdi-bell" />
			<a onClick={this.onLogout}>
				<span title="Sair" className="mdi mdi-logout" />
			</a>
		</div>);
	  }
	});
