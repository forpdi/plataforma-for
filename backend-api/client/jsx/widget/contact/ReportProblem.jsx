
import _ from "underscore";
import React from "react";
import Form from "forpdi/jsx/widget/form/Form.jsx";
import ContactStore from "forpdi/jsx/store/Contact.jsx";

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
					{'screen': "Cadastro de Ações"},
					{'screen': "Cadastro de Desafios"},
					{'screen': "Desafios"},
					{'screen': "Detalhes do Desafio"}
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
