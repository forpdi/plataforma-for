
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
				name: "subject",
				type: "select",
				options: [
					{'subject': "Dificuldade de Uso"},
					{'subject': "Dúvida"},
					{'subject': "Novas Funcionalidades"},
					{'subject': "Pagamento/Assinatura"},
					{'subject': "Perfil do Usuário"},
					{'subject': "Sugestão de Melhoria"}
				],
				valueField: 'subject',
				displayField: 'subject',
				placeholder: "Selecione um assunto para a mensagem...",
				label: "Assunto"
			},{
				name: "description",
				type: "textarea",
				rows: 4,
				placeholder: "",
				label: "Descreva melhor seu feedback"
			}]
		};
	},
	onSubmit(data) {
		ContactStore.dispatch({
			action: ContactStore.ACTION_CREATE_FEEDBACK,
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
					cancelLabel="Cancelar"
					submitLabel="Enviar mensagem"
				/>
			</div>
		);
	}
});
