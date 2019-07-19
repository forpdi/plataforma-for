
import React from "react";
import { ToastContainer } from 'react-toastr';

import ForPDITopBar from "forpdi/jsx/TopBar.jsx";
import ForRiscoTopBar from "forpdi/jsx_forrisco/TopBar.jsx";
import PlataformaForTopbar from "forpdi/jsx/PlataformaForTopbar.jsx";;

import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object.isRequired,
	},
	childContextTypes: {
		accessLevel: React.PropTypes.number,
		accessLevels: React.PropTypes.object,
		permissions: React.PropTypes.array,
		roles: React.PropTypes.object,
		toastr: React.PropTypes.object
	},
	getChildContext() {
		return {
			accessLevel: this.state.accessLevel,
			accessLevels: AccessLevels.enum,
			permissions: this.state.permissions,
			roles: {
				NORMAL:      this.state.accessLevel >= AccessLevels.enum.NORMAL,
				COLABORATOR: this.state.accessLevel >= AccessLevels.enum.COLABORATOR,
				MANAGER:     this.state.accessLevel >= AccessLevels.enum.MANAGER,
				ADMIN:       this.state.accessLevel >= AccessLevels.enum.ADMIN,
				SYSADMIN:    this.state.accessLevel >= AccessLevels.enum.SYSADMIN
			},
			toastr: this
		};
	},
	addAlertError(msg) {
		this.refs.container.clear();
		this.refs.container.error(
			msg,null, {
				timeOut: 5000,
				extendedTimeOut: 10000,
				closeButton:true,
				showAnimation: null,
				hideAnimation: 'flash'
			}
		);
	},
	addAlertSuccess(msg) {
		this.refs.container.clear();
		this.refs.container.success(
			msg,null, {
				timeOut: 5000,
				extendedTimeOut: 10000,
				closeButton:true,
				showAnimation: null,
				hideAnimation: 'flash'
			}
		);
	},
	getInitialState() {
		return {
			loading: UserSession.get("loading"),
			accessLevel: UserSession.get("logged") ? UserSession.get("accessLevel"):0,
			permissions: UserSession.get("logged") ? UserSession.get("permissions"):[]
		};
	},
	componentDidMount() {
		if (!this.state.loading && this.state.accessLevel === 0)
			this.context.router.push("/login");
		UserSession.on("login", () => {
			this.setState({
				accessLevel: UserSession.get("accessLevel") || 0,
				permissions: UserSession.get("permissions") || []
			});
		}, this);
		UserSession.on("logout", () => {
			this.setState({
				accessLevel: 0,
				permissions: []
			});
		}, this);
		UserSession.on("loaded", () => {
			this.setState({ loading: false });
		}, this);
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
	getTopbar() {
		if (this.props.location.pathname.includes("forrisco")) {
			return <ForRiscoTopBar />;
		} else if (this.props.location.pathname.includes("app-select")) {
			return <PlataformaForTopbar />;
		}
		return <ForPDITopBar />;
	},
	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (
			<main className='fpdi-app-container'>
				{ this.getTopbar() }
				<ToastContainer ref="container"
					className="toast-top-center" />
				{this.props.children}
			</main>
		);
	}
});

