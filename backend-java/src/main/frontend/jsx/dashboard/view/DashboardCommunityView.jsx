
import React from "react";
import {Link} from "react-router";

import AccessLevels from "forpdi/jsx/core/store/AccessLevels.json";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import DashboardCommunityPanel from "forpdi/jsx/dashboard/view/DashboardCommunityPanel.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Filter from "forpdi/jsx/dashboard/view/community/FilterCommunity.jsx";

import Logo from 'forpdi/img/logo.png';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

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
	getInitialState() {
		return {
			loading: UserSession.get("loading"),
			accessLevel: UserSession.get("logged") ? UserSession.get("accessLevel"):0,
			permissions: UserSession.get("logged") ? UserSession.get("permissions"):[]
		};
	},
	componentDidMount() {
		var me = this;
		UserSession.on("loaded", () => {me.setState({loading: false})}, me);
	},
	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (<main className='fpdi-app-container'>
			<div className="fpdi-top-bar">
				<div className="col-md-2 marginRight0">
			        <div className="fpdi-top-bar-brand">
						{(EnvInfo.company && EnvInfo.company.logo == "" )?(<img alt={Messages.get("label.forPdiLogo")} src={Logo}/>):(<img alt={Messages.get("label.forPdiLogo")} src={EnvInfo.company ? EnvInfo.company.logo:Logo} />)}
			        </div>
		        </div>
		        <div className="col-md-8 textAlignCenter marginRight0">
		        	<h1>{Messages.get("label.institutionalDevelopmentPlan")}</h1>
		        </div>
		        <div className="col-md-2"></div>
				
			</div>
			<div className="fpdi-app-body overFlowY">
				<ToastContainer ref="container"					
					className="toast-top-center" />
					  {EnvInfo.company.showDashboard ?
					  	(<div className="wFull">
					  			<DashboardCommunityPanel />
					  		
					  	</div>)
					  :
					  	(<div>
					  		<h1 className="dashboard-noCommunity">{Messages.get("label.unableCommunityDashboard")}</h1>
				  		</div>)}
			</div>
		</main>);
	}
});

