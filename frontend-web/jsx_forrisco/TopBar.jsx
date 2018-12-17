import React from "react";
import {Link} from "react-router";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import NotificationTopBar from "forpdi/jsx/core/view/user/NotificationTopBar.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import Logo from 'forpdi/img/logo.png';
import forRiscoLogo from 'forpdi/img/forrisco-logo.png';
import _ from 'underscore';
import string from 'string';
import LinkInformation from "forpdi/jsx/LinkInformation.jsx";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		accessLevels: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			user: UserSession.get("user"),
			logged: !!UserSession.get("logged"),
			model:null,
			notifications:0
		};
	},


	componentWillMount() {
		var me = this;
		UserSession.on("login", session => {
			me.setState({
				user: session.get("user"),
				logged: true,
				model: session.get("user")
			});
			this.verifyNotificationTask();
		}, me);

		UserStore.on("retrieve-user-profile", (store) => {
			me.setState({
				model:store.data,
				logged: true
			});
		}, me);


		UserSession.on("logout", session => {
			me.setState({
				user: null,
				logged: false,
				notifications: 0
			});
		}, me);

		UserStore.on("useredit", (model) => {
			if (model.data) {
				UserStore.dispatch({
					action: UserStore.ACTION_USER_PROFILE,
					data: this.state.user.id
				});
			}
		},me);

		UserSession.on("notificationsVerifyed", (model) => {
			if (model.data != null && model.data != undefined) {
				me.setState({
					notifications: model.data
				});
			}
		},me);

		UserSession.on("retrieve-notifications", (model) => {
			this.setState({
				notifications: 0
			});
		},me);

		UserSession.on("retrieve-showMoreNotifications", (model) => {
			this.setState({
				notifications: 0
			});
		},me);

		UserStore.on("update-picture", (model) => {
			this.setState({
				user: model.data,
			});
		},me);

		this.verifyNotificationTask();
	},

	verifyNotificationTask() {
		if (EnvInfo.company != null) {
			UserSession.dispatch({
            	action: UserSession.ACTION_VERIFY_NOTIFICATION
        	});
			if (this.state.user != null) {
				var func = _.bind(this.verifyNotificationTask);
				_.delay(func, 60000);
			}
		}


	},

	componentWillUnmount() {
		UserSession.off(null, null, this);
	},
    onLogout() {
    	Modal.confirmCustom(
			() => {
				UserSession.dispatch({
           	 		action: UserSession.ACTION_LOGOUT
        		});

				Modal.hide();
			},
			Messages.get("label.logoffConfirmation"),
			() => {Modal.hide()}
		);

    },
    onSearch(evt, id) {
    	evt.preventDefault();
    	//console.log("Searching:",this.refs['search'].value);
    },

    openFeedback(evt) {
    	evt.preventDefault();
    	Modal.feedbackPost();
    },
    openReportProblem(evt) {
    	evt.preventDefault();
    	Modal.reportProblem();
    },

    openViewNotifications () {
    	var newNotifications = 0;
    	if (this.state.notifications > 7)
    		newNotifications = this.state.notifications -7;
		this.setState({
			notifications: newNotifications
		});

    	if (EnvInfo.company != null) {
    		UserSession.dispatch({
            	action: UserSession.ACTION_LIST_NOTIFICATIONS,
        		data: {
        			limit: 7,
        			topBar: true
        		}
        	});
		}
    },

	render() {

		if (!this.state.logged) {
			return <div style={{display: 'none'}} />;
		}
		return (<div className="fpdi-top-bar">
	        <Link to='/home' className="fpdi-top-bar-brand">
				{(EnvInfo.company && EnvInfo.company.logo == "" )?(<img alt={Messages.get("label.forPdiLogo")} src={Logo}/>):(<img alt={Messages.get("label.forPdiLogo")} src={EnvInfo.company ? EnvInfo.company.logo:Logo} />)}
	        </Link>
			<Link to='/forrisco/home' className="fpdi-top-bar-brand">
				{<img alt={Messages.get("label.forRiscoLogo")} src={forRiscoLogo} />}
	        </Link>
			<span className="fpdi-fill" />
			{/*<span className="mdi mdi-account-circle" style={{marginRight: '5px'}} />
			<span>{this.state.user.name}</span>*/}
			<Link  to = {"/users/profilerUser/" +this.state.user.id}>
				<span><img className="fpdi-userPicture" src={(!this.state.user.picture || this.state.user.picture == "" ) ?
					"http://cloud.progolden.com.br/file/8352" : this.state.user.picture} /></span>
				{this.state.model == null ? (this.state.user.name.length>25)?(string(this.state.user.name).trim().substr(0,25).concat("...").toString()):(this.state.user.name)   : (this.state.model.name.length>25)?(string(this.state.model.name).trim().substr(0,25).concat("...").toString()):(this.state.model.name)}
			</Link>
			<span title="Notificações" className="mdi mdi-magnify hidden" />

			<div className="dropdown">
					<a
						id="top-bar-notification"
						className={this.state.notifications == 0 ? " hidden " : "fpdi-notifications-number"}
						data-toggle="dropdown"
						aria-haspopup="true"
						aria-expanded="true"
						title="Notificações"
						onClick={this.openViewNotifications}
					>
						{this.state.notifications < 10 ? "0"+this.state.notifications : this.state.notifications}
					</a>

					<a
						id="top-bar-notification"
						className="dropdown-toggle mdi mdi-bell fpdi-notifications icon-link"
						data-toggle="dropdown"
						aria-haspopup="true"
						aria-expanded="true"
						title="Notificações"
						onClick={this.openViewNotifications}
					>
					</a>
					<ul className="dropdown-menu-notification dropdown-menu dropdown-menu-right-notification" aria-labelledby="top-bar-notification">

						<div className = "container">
							<NotificationTopBar/>
						</div>

					</ul>
			</div>
			<div className="dropdown">
				<a
					id="top-bar-notification"
					className="mdi mdi-help-circle dropdown-toggle"
					data-toggle="dropdown"
					aria-haspopup="true"
					aria-expanded="true"
					title="Informações"
					>
				</a>
				<div className="dropdown-menu dropdown-menu-right">
					<LinkInformation />
			  	</div>

			</div>


			<div className="dropdown">
				<a
					id="top-bar-main-menu"
					className="dropdown-toggle"
					data-toggle="dropdown"
					aria-haspopup="true"
					aria-expanded="true"
					title="Menu de Opções"
					>
						<span className="sr-only">{Messages.get("label.optionMenu")}</span>
						<span id="align-top-bar-menu" className="mdi mdi-menu icon-link" />
				</a>
				<ul className="dropdown-menu dropdown-menu-right" aria-labelledby="top-bar-main-menu">
					<li>
						<Link  to = {"/users/profilerUser/" +this.state.user.id}>
							<span className="mdi mdi-account-circle icon-link"/> {Messages.getEditable("label.myProfile","fpdi-nav-label")}
						</Link>
					</li>

					{(this.context.roles.MANAGER  || _.contains(this.context.permissions,
					"org.forpdi.core.user.authz.permission.ManageUsersPermission") || _.contains(this.context.permissions,
					"org.forpdi.core.user.authz.permission.ViewUsersPermission")) && EnvInfo.company ? <li>
						<Link to="/users">
		                	<span className="mdi mdi-account-multiple icon-link"
		                    	/> {Messages.getEditable("label.users","fpdi-nav-label")}
		            	</Link>
		            </li>:""}

					{/* Aba CONFIGURAÇOES COMENTADA
					<li>
						{(false)?(<Link to="/settings">
			                <span className="mdi mdi-settings"
			                    /> {Messages.get("label.settings")}

			            </Link>
					</li> */}


			        {this.context.roles.SYSADMIN ? <li>
						<Link to="/system/companies">
			                <span className="mdi mdi-chemical-weapon icon-link"
			                    /> {Messages.getEditable("label.system","fpdi-nav-label")}
			            </Link>
					</li>:""}

					{this.context.roles.SYSADMIN ? <li>
						<Link to="/structures">
			                <span className="mdi mdi-note-text icon-link"
			                    /> {Messages.getEditable("label.structures","fpdi-nav-label")}
			            </Link>
					</li>:""}



					<li role="separator" className="divider"></li>
					<li>
						<a onClick={this.onLogout}>
							<span className="mdi mdi-logout icon-link" /> {Messages.getEditable("label.logoff","fpdi-nav-label")}
						</a>
					</li>
				</ul>
			</div>
		</div>);
	  }
	});
