
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
    componentDidMount() {
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
    return (<div className='fpdi-app-sidebar'>
        <div className="fpdi-sidebar-brand">
    	   <img alt="ForPDI Logo" src={WhiteLogo} />
        </div>
	    <ul className="nav nav-stacked">
			<li>
                <a href="#/home">
                    <span className="fpdi-nav-icon mdi mdi-view-dashboard"
                        ></span> Painel de Bordo
                </a>
            </li>
            <li>
                <a href="#/plans">
                    <span className="fpdi-nav-icon mdi mdi-note-text"
                    	></span> Planos
                </a>
            </li>
            <li>
                <a href="#/plans">
                    <span className="fpdi-nav-icon mdi mdi-star"
                        ></span> Favoritos
                </a>
            </li>
            <li>
                <a href="#/plans">
                    <span className="fpdi-nav-icon mdi mdi-account-multiple"
                        ></span> Usuários
                </a>
            </li>
			<li>
                <a href="#/settings">
                    <span className="fpdi-nav-icon mdi mdi-settings"
                    	></span> Configurações
                </a>
            </li>
            <li>
                <a href="#/system/general">
                    <span className="fpdi-nav-icon mdi mdi-chemical-weapon"
                        ></span> Sistema
                </a>
            </li>
		</ul>
        <span className="fpdi-fill" />
   	</div>);
  }
});
