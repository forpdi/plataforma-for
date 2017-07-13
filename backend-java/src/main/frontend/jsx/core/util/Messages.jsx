
import React from 'react';
import $ from 'jquery';
import UserSession from 'forpdi/jsx/core/store/UserSession.jsx';

const PropTypes = React.PropTypes;

const EditableMessage = React.createClass({
	propTypes: {
		className: PropTypes.string,
		key: PropTypes.string.required,
		text: PropTypes.string.required,
	},
	onContextMenu(event) {
		event.preventDefault();
		console.log(event);
	},
	render() {
		return (<span className={this.props.className} onContextMenu={this.onContextMenu}>
			{this.props.text}*
		</span>);
	},
});

var Messages = {
	_messages: null,
	get(key) {
		if (typeof this._messages === 'undefined') {
			console.error("The system messages are not loaded.");
			return "!!!ERROR!!!";
		}
		if (typeof this._messages[key] === 'undefined') {
			console.warn("Undefined message key:",key);
			return "???"+key+"???";
		}
		return this._messages[key];
	},
	getEditable(key, containerClassName) {
		var msg = this.get(key);
		return <EditableMessage key={key} text={msg} className={containerClassName} />;
	},
};

if (typeof EnvInfo === 'object') {
	Messages._messages = EnvInfo.messages;
} else {
	$.ajax({
		url: BACKEND_URL+"messages",
		method: "GET",
		dataType: 'json',
		success(data) {
			Messages._messages = data;
		},
		failure(opts) {
			console.error("Failure when loading system messages asynchrounously:\n", opts);
		}
	});
}

export default Messages;
