import React from "react";
import string from 'string';
import {Link} from "react-router";
import NotificationUser from "forpdi/jsx/core/view/user/NotificationUser.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
    getInitialState() {
        return {
            notifications: 0,
            user: UserSession.get("user")
        }
    },


    componentDidMount() {
        var me = this;
        
        UserSession.on("retrieve-limitedNotifications", (model) => {
            this.setState ({
                notifications: model.data    
            });
        }, me);
    },

    componentWillUnmount() {
        UserSession.off(null, null, this);
    },

   

    render() {
        return (
            <div className = "row">
                <div className="col-sm-6">
                    <NotificationUser notifications={this.state.notifications}/>
                    {(this.state.notifications && this.state.notifications.length > 0) ?  
                        <div className="textAlignCenter">
                            <Link  to = {"/users/profilerUser/" +this.state.user.id}>
                                {Messages.getEditable("label.seeAll","fpdi-nav-label")}
                            </Link>
                        </div>
                    : ""}
                </div>
            </div>
        );
    }
});