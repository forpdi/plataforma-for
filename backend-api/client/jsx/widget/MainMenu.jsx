
import React from "react";
import {Link} from "react-router";
import UserSession from "forpdi/jsx/store/UserSession.jsx";

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
    return (<div className='cmp-app-sidebar hidden'>
        <div className="cmp-sidebar-brand">
    	   <img alt="Salesfox Logo" src="img/brand-white.svg" />
        </div>
	    <ul className="nav nav-stacked">
			<li>
                <a href="#/challenges">
                    <span className="cmp-nav-icon mdi mdi-format-list-bulleted"
                    	></span> Desafios
                </a>
            </li>
			<li>
                <a href="#/sysadmin">
                    <span className="cmp-nav-icon mdi mdi-settings"
                    	></span> Administração
                </a>
            </li>
		</ul>
   	</div>);
  }
});
