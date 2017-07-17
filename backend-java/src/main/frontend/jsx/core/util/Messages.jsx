
import React from 'react';
import ReactDOM from 'react-dom';
import $ from 'jquery';
import UserSession from 'forpdi/jsx/core/store/UserSession.jsx';

const PropTypes = React.PropTypes;

const EditMessagePopover = React.createClass({
	getInitialState() {
		return {
			hidden: true,
			text: '',
			messageKey: null,
			top: -500,
			left: -200,
		};
	},
	show(key, text, x, y) {
		console.log(arguments);
		this.setState({
			hidden: false,
			text: text,
			messageKey: key,
			top: y,
			left: x,
		});
	},
	submit(event) {
		event.preventDefault();
		console.log(event);
	},
	render() {
		if (this.state.hidden) {
			return <div />;
		}
		console.log("Rerender",this.state);
		return (<div ref='main' className="fpdi-message-edit-popup"
			style={{top: this.state.top+"px", left: this.state.left+"px"}}>
			<p>Editar este texto:</p>
			<form>
				<div className="form-group">
					<input className="form-control" defaultValue={this.state.text} ref="text" />
				</div>
				<div className="form-group row">
					<div className="col col-sm-6">
						<button type="submit" className="btn btn-sm btn-block btn-success">
							<span className="mdi mdi-check" />
						</button>
					</div>
					<div className="col col-sm-6">
						<button className="btn btn-sm btn-block btn-danger">
							<span className="mdi mdi-close" />
						</button>
					</div>
				</div>
			</form>
		</div>);
	},
});

let el = document.getElementById("message-edit-container");
if (!el) {
	el = document.createElement("div");
	el.id = "message-edit-container";
	el.class = "fpdi-message-edit";
	document.getElementsByTagName("body")[0].appendChild(el);
}
const EditCt = ReactDOM.render(<EditMessagePopover />, el);

const EditableMessage = React.createClass({
	propTypes: {
		className: PropTypes.string,
		messageKey: PropTypes.string.isRequired,
		text: PropTypes.string.isRequired,
	},
	onContextMenu(event) {
		event.preventDefault();
		EditCt.show(this.props.messageKey, this.props.text,
			event.clientX, event.clientY);
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
		return <EditableMessage messageKey={key} text={msg} className={containerClassName} />;
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
