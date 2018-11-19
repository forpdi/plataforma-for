
import React from "react";

import MainMenu from "forpdi/jsx_forrisco/MainMenu.jsx";
import TopBar from "forpdi/jsx_forrisco/TopBar.jsx";

import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Login from "forpdi/jsx/core/view/user/Login.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

var ReactToastr = require("react-toastr");
var {ToastContainer} = ReactToastr;
var ToastMessageFactory = React.createFactory(ReactToastr.ToastMessage);

export default React.createClass({
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
		var me = this;
		//$("[data-toggle=tooltip]").tooltip();
		UserSession.on("login", () => {
			me.setState({
				accessLevel: UserSession.get("accessLevel") || 0,
				permissions: UserSession.get("permissions") || []
			});
		}, me);
		UserSession.on("logout", () => {
			me.setState({
				accessLevel: 0,
				permissions: []
			});
		}, me);
		UserSession.on("loaded", () => {me.setState({loading: false})}, me);
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
	componentDidUpdate() {
		//$("[data-toggle=tooltip]").tooltip();
	},
	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (
			<main className='fpdi-app-container'>
				<TopBar />
				<div className="fpdi-app-body">
					<MainMenu {...this.props} />
					<ToastContainer ref="container"
						className="toast-top-center" />
					<div className="fpdi-app-content">
						  {this.state.accessLevel == 0 ? <Login /> : this.props.children}
					</div>
				</div>
			</main>
		);
	}
});
