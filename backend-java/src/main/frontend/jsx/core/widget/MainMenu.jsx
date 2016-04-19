
import React from "react";
import {Link} from "react-router";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import WhiteLogo from 'forpdi/img/logo.png';

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
  render() {
    if (!this.state.logged) {
        return <div style={{display: 'none'}} />;
    }
    return (<div className='fpdi-app-sidebar hidden'>
        <div className="fpdi-sidebar-brand">
    	   <img alt="ForPDI Logo" src={WhiteLogo} />
        </div>
	    <ul className="nav nav-stacked">
			<li>
                <a href="#/challenges">
                    <span className="fpdi-nav-icon mdi mdi-format-list-bulleted"
                    	></span> Planos
                </a>
            </li>
			<li>
                <a href="#/sysadmin">
                    <span className="fpdi-nav-icon mdi mdi-settings"
                    	></span> Administração
                </a>
            </li>
		</ul>
   	</div>);
  }
});
