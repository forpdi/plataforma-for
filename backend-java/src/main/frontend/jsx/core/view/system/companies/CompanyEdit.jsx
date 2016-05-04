
import React from "react";
import {Link} from 'react-router';

import CompanyStore from "forpdi/jsx/core/store/Company.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object
	},
	getInitialState() {
		return {
			loading: !!this.props.params.modelId,
			modelId: this.props.params.modelId,
			model: null,
			fields: !this.props.params.modelId ? this.getFields():null
		};
	},
	getFields(model) {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			label: "Nome",
			value: model ? model.get("name"):null
		},{
			name: "logo",
			type: "text",
			placeholder: "",
			label: "URL da Logomarca",
			value: model ? model.get("logo"):null
		},{
			name: 'description',
			type: 'textarea',
			placeholder: '',
			label: "Descrição",
			value: model ? model.get("description"):null
		}];
	},
	componentDidMount() {
		var me = this;
		CompanyStore.on("sync", (model) => {
			me.context.router.push("/system/companies");
		}, me);
		CompanyStore.on("retrieve", (model) => {
			me.setState({
				loading: false,
				model: model,
				fields: me.getFields(model)
			});
		}, me);

		if (this.state.loading) {
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
	},
	componentWillUnmount() {
		CompanyStore.off(null, null, this);
	},
	
	onSubmit(data) {
		var me = this;
		if (me.props.params.modelId) {
			me.state.model.set(data);
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_UPDATE,
				data: me.state.model
			});
		} else {
			CompanyStore.dispatch({
				action: CompanyStore.ACTION_CREATE,
				data: data
			});
		}
	},

	render() {
		return (<div className="col-sm-offset-3 col-sm-6 animated fadeIn">
			{this.state.loading ? <LoadingGauge />:<div>
				<h1>
					{this.state.model ? "Editar instituição":"Adicionar nova instituição"}
				</h1>
				<VerticalForm
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={CompanyStore}
					submitLabel="Salvar"
				/>
			</div>}
		</div>);
	}
});
