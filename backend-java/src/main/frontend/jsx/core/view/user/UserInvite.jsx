
import React from "react";
import {Link} from 'react-router';

import UserStore from "forpdi/jsx/core/store/User.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

import Messages from 'forpdi/jsx/core/util/Messages.jsx';

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object
	},
	getInitialState() {
		return {
			fields: [{
				name: "name",
				type: "text",
				placeholder: "",
				label: Messages.get("label.name")
			},{
				name: "email",
				type: "email",
				placeholder: "",
				label: Messages.get("label.email")
			}]
		};
	},
	componentDidMount() {
		var me = this;
		UserStore.on("sync", (model) => {
			Modal.alert("Sucesso", "Usuário convidado com sucesso. Um e-mail foi enviado para que ele conclua o cadastro.");
			me.context.router.push("/users");
		}, me);

	},
	componentWillUnmount() {
		UserStore.off(null, null, this);
	},
	
	onSubmit(data) {
		var me = this;
		UserStore.dispatch({
			action: UserStore.ACTION_SIGNUP,
			data: data
		});
	},

	render() {
		return (<div className="row">
			<div className="col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10 fpdi-card animated fadeIn">
				<h1>
					Convidar usuário
				</h1>
				<p>Digite o nome e e-mail do novo usuário. Um e-mail com um link para finalizar o cadastro será enviado para ele.</p>
				<VerticalForm
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={UserStore}
					submitLabel="Enviar convite"
				/>
			</div>
		</div>);
	}
});
