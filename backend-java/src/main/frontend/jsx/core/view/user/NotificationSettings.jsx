import React from 'react';
import UserStore from "forpdi/jsx/core/store/User.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var onClickOutside = require('react-onclickoutside');

export default onClickOutside(React.createClass({

	getInitialState() {
        return {
        model:this.props.model
        };
    },

    componentWillReceiveProps(newProps){
        if(this.isMounted()) {
            this.setState({
                model: newProps.model,
            });
        }
    },

    setNotificationConfig(){		
		var me = this;
		var notificationSettingOption;
		var update = false;

		if (document.getElementById('notificationSetting1').checked && this.state.model.notificationSettings != 1) {
		  notificationSettingOption = document.getElementById('notificationSetting1').value;
		  update = true;
		}else if (document.getElementById('notificationSetting2').checked && this.state.model.notificationSettings != 2) {
		  notificationSettingOption = document.getElementById('notificationSetting2').value;
		  update = true;
		}else if (document.getElementById('notificationSetting3').checked && this.state.model.notificationSettings != 3) {
		  notificationSettingOption = document.getElementById('notificationSetting3').value;
		  update = true;
		}
		if(update) {
			UserStore.dispatch({
				action: UserStore.ACTION_UPDATE_NOTIFICATION_SETTINGS,
				data: {
					id: me.props.params.modelId,
					notificationSetting: notificationSettingOption
				}
			});
			var model = this.state.model;
			model.notificationSettings = notificationSettingOption;

			this.setState({
				model: model
			})
		}
		
	},

    render() {
        return (
            <div className="dropdown floatRight">
                <a
                    id="notifications-settings-menu"
                    className="dropdown-toggle"
                    data-toggle="dropdown"
                    aria-haspopup="true"
                    aria-expanded="true"
                    title={Messages.get("label.settings")}
                    >
                        <span className="mdi mdi-settings cursorPointer floatRight"/>
                </a>
                <div className="dropdown-menu dropdown-menu-right width250" aria-labelledby="notifications-settings-menu">
                    <p>{Messages.get("notification.receiving")}</p>
                    <div className="radio" id="notificationSettingRadio" onClick={this.setNotificationConfig}>
                        <div key={'field-opt-1'}><label><input
                            type="radio"
                            name="notificationSetting"
                            id="notificationSetting1"
                            defaultChecked={this.state.model.notificationSettings == 1 ? true : false}
                            value="1"
                            />{Messages.get("notification.standard")}
                            <span className="fpdi-required">&nbsp;</span>
                        </label></div>
                        <div key={'field-opt-2'}><label><input
                            type="radio"
                            name="notificationSetting"
                            id="notificationSetting2"
                            defaultChecked={this.state.model.notificationSettings == 2 ? true : false}
                            value="2"
                            />{Messages.get("notification.emailType")}
                        </label></div>
                        <div key={'field-opt-3'}><label><input
                            type="radio"
                            name="notificationSetting"
                            id="notificationSetting3"
                            defaultChecked={this.state.model.notificationSettings == 3 ? true : false}
                            value="3"
                            />{Messages.get("notification.noEmailType")}
                        </label></div>
                    </div>
                    <p className="notificationSettingsDropdown"><span className="fpdi-required"></span> {Messages.get("notification.typeInformation")}</p>
                </div>
            </div>
        );
    }
}));