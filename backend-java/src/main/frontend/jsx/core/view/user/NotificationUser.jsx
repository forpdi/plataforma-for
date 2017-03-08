import React from "react";
import string from 'string';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";



export default React.createClass({
    getInitialState() {
        return {
            notifications:[]
        }
    },

    componentWillReceiveProps(newProps){
        if(this.isMounted()) {
            if (newProps.notifications) {
                this.setState({
                    notifications: newProps.notifications
                });
            }
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
                {(this.state.notifications && this.state.notifications.length > 0) ?
                    this.state.notifications.map((item, idx) => {
                        return (
                            <div key={"notification-"+idx} className = {item.vizualized == true && !item.vizualizeNow ? "" : "backgroundNotification"}>
                                <div className = "row paddingNotification">
                                    <a href={item.url}>
                                        <div className="col-md-1">
                                            <img alt="Notifications-Picture" src={item.picture}/>
                                        </div>

                                        <div className="col-md-8">
                                            <p id = "p-notificationUser" dangerouslySetInnerHTML={{__html:item.description}}/>
                                        </div>

                                        <div className="col-md-3">
                                            <p id = "p-notificationUser" > <i className="mdi mdi-clock-notification mdi-calendar-clock" id = "notificationIcons"> <span id = "p-time-notifications" className="fpdi-notificationDate"> {item.creation.split(" ")[0]} </span> </i>  </p>  

                                        </div>
                                    </a>
                                </div>
                            </div>
                        );
                    })
                : <div className="fpdi-noNotifications">{"Você ainda não possui nenhuma notificação registada no sistema."}</div>}
            </div>
        );
    }
});
