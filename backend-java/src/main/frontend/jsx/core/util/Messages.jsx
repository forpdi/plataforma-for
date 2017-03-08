
import $ from 'jquery';

var msg = {
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
	}
};

if (typeof EnvInfo === 'object') {
	msg._messages = EnvInfo.messages;
} else {
	$.ajax({
		url: BACKEND_URL+"messages",
		method: "GET",
		dataType: 'json',
		success(data) {
			msg._messages = data;
		},
		failure(opts) {
			console.error("Failure when loading system messages asynchrounously:\n", opts);
		}
	});
}

export default msg;
