import React from "react";
import {Link} from 'react-router';
import _ from 'underscore';

import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
    getInitialState() {
        return {
            notifications:[]
        }
    },

    componentWillReceiveProps(newProps){
		if (newProps.notifications) {
			this.setState({
				notifications: newProps.notifications
			});
		}
    },

    componentDidMount() {
        var me = this;
        UserSession.on("retrieve-notifications", (model) => {
            me.setState ({
                notifications:model.data
            });
        }, me);

        UserSession.on("retrieve-showMoreNotifications", (model) => {
            var notifications = me.state.notifications;
            for (var i=0; i<model.data.length; i++) {
                var exist = false;
                for (var j=0; j<notifications.length; j++) {
                    if (notifications[j].id == model.data[i].id)
                        exist = true;
                }
                if (!exist)
                    notifications.push(model.data[i]);
            }
            me.setState({
                notifications: notifications
            });
        }, me);

    },

    componentWillUnmount() {
        UserSession.off(null, null, this);
    },

    render() {
        return (
            <div>
                {
					this.state.notifications && this.state.notifications.length > 0
					?
                    _.map(this.state.notifications, (item, idx) => (
						<div key={"notification-"+idx} className = {item.vizualized && !item.vizualizeNow ? "" : "backgroundNotification"}>
							<div className = "row paddingNotification">
								{
									item.url
									?
									<Link to={item.url.split("#")[1]}>
										<div className="col-md-1">
											<img alt="Notifications-Picture" src={item.picture}/>
										</div>
										<div className="col-md-8">
											<p id = "p-notificationUser" dangerouslySetInnerHTML={{__html:item.description}}/>
										</div>
										<div className="col-md-3">
											<p id = "p-notificationUser">
												<i className="mdi mdi-clock-notification mdi-calendar-clock" id = "notificationIcons">
													<span id = "p-time-notifications" className="fpdi-notificationDate">
														{item.creation.split(" ")[0]}
													</span>
												</i>
											</p>
										</div>
									</Link>
									:
									<div>
										<div className="col-md-1">
											<img alt="Notifications-Picture" src={item.picture}/>
										</div>
										<div className="col-md-8">
											<p id = "p-notificationUser" dangerouslySetInnerHTML={{__html:item.description}}/>
										</div>
										<div className="col-md-3">
											<p id = "p-notificationUser">
												<i className="mdi mdiclock-notification mdi-calendar-clock" id = "notificationIcons">
													<span id = "p-time-notifications" className="fpdi-notificationDate">
														{item.creation.split(" ")[0]}
													</span>
												</i>
											</p>
										</div>
									</div>
								}
							</div>
						</div>
					))
					:
					<div className="fpdi-noNotifications">
						{Messages.getEditable("notification.noNotifications","fpdi-nav-label")}
					</div>
				}
            </div>
        );
    }
});
