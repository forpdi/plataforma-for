
import _ from "underscore";
import React from "react";
import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import ContactStore from "forpdi/jsx/core/store/Contact.jsx";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	getDefaultProps() {
		return {};
	},
	getInitialState(args) {
		return {
			fields: [{
				name: "screen",
				type: "select",
				options: [
					{'screen': "Dashboard"},
					{'screen': "Cadastro de Desafios"},
					{'screen': "Planos"},
					{'screen': "Configurações"}
				],
				valueField: 'screen',
				displayField: 'screen',
				placeholder: "-- Selecione uma tela --",
				label: "Tela onde o problema ocorreu"
			},{
				name: "description",
				type: "textarea",
				rows: 4,
				placeholder: "",
				label: "Descreva melhor o problema"
			}]
		};
	},
	onSubmit(data) {
		ContactStore.dispatch({
			action: ContactStore.ACTION_CREATE_PROBLEM,
			data: data
		});
	},
	componentDidMount() {
		var me = this;
		ContactStore.on("sync", model => {
			_.defer(() => {
				ContactStore.remove(model);
				if (typeof me.props.onSubmit === 'function')
					me.props.onSubmit();
			});
		}, me);
	},
	componentWillUnmount() {
		ContactStore.off(null, null, this);
	},
	render() {
		return (
			<div ref="form-ct">
				<VerticalForm
					onCancel={this.props.onCancel}
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={ContactStore}
					cancelLabel="Descartar"
					submitLabel="Enviar"
				/>
			</div>
		);
	}
});
