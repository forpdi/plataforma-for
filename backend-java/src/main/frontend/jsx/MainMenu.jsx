
import React from "react";
import {Link} from "react-router";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

import Logo from 'forpdi/img/logo.png';

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
    return (<div className='fpdi-app-sidebar fpdi-tabs-stacked'>
        <div className="fpdi-sidebar-brand">
    	   <img alt="ForPDI Logo" src={EnvInfo.company ? EnvInfo.company.logo:Logo} />
        </div>
	    <div className="fpdi-tabs-nav">
			<Link to="/home" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-view-dashboard"
                    ></span> {Messages.get("label.dashboard")}
            </Link>
            <Link to="/plans" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-note-text"
                    ></span> {Messages.get("label.plans")}
            </Link>
            <Link to="/structures" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-grid"
                    ></span> {Messages.get("label.structures")}
            </Link>
            <Link to="/favorites" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-star"
                    ></span> {Messages.get("label.favorites")}
            </Link>
            <Link to="/users" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-account-multiple"
                    ></span> {Messages.get("label.users")}
            </Link>
            <Link to="/settings" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-settings"
                    ></span> {Messages.get("label.settings")}
            </Link>
            <Link to="/system" activeClassName="active">
                <span className="fpdi-nav-icon mdi mdi-chemical-weapon"
                    ></span> {Messages.get("label.system")}
            </Link>
		</div>
        <span className="fpdi-fill" />
   	</div>);
  }
});
