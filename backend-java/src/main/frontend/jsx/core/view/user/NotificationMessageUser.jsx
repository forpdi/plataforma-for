import React from "react";
import string from 'string';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";



export default React.createClass({
    getInitialState() {
        return {
            notifications:[]
        }
    },

   

    componentDidMount() {
        var me = this;

        UserSession.on("retrieve-messages", (model) => {
            me.setState ({
                notifications:model.data
            });
        }, me);

        UserSession.on("retrieve-showMoreMessages", (model) => {
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
       /* {console.log(this.state.notifications)} */  
        return (
           <div>
                {(this.state.notifications !=  null && this.state.notifications.length > 0) ?
                    this.state.notifications.map((item, idx) => {
                    return (
                            <p key={"message-"+idx}> <span id = "format-message-subject"> {item.subject} </span>
                                <span id = "format-message-date"> {item.creation} </span>
                                <br/>
                                <span id = "format-message"> {(item.message.length>70)?(string(item.message).trim().substr(0,70).concat("...").toString()):(item.message)} </span> 
                            </p>
                                                               
                    );
                })
                : <div className="fpdi-noNotifications">{Messages.getEditable("notification.noNotifications","fpdi-nav-label")}</div>}
            </div>
        );
    }
});
