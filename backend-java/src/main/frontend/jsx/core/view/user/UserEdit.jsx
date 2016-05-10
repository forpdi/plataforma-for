
import React from "react";
import {Link} from 'react-router';

import UserStore from "forpdi/jsx/core/store/User.jsx";
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
			loading: true,
			modelId: this.props.params.modelId,
			model: null,
			fields: null,
			companies: null,
			themes: null
		};
	},
	getFields() {
		return [{
			name: "host",
			type: "text",
			placeholder: "",
			label: "Host",
			helpBox: 'Ex: app.forpdi.org',
			value: this.state.model ? this.state.model.get("host"):null
		},{
			name: "baseUrl",
			type: "url",
			placeholder: "",
			label: "URL Base",
			helpBox: "Ex: http://app.forpdi.org/",
			value: this.state.model ? this.state.model.get("baseUrl"):null
		},{
			name: 'theme',
			type: 'select',
			placeholder: '-- Selecione um Tema --',
			label: "Tema",
			value: this.state.model ? this.state.model.get("theme"):null,
			displayField: 'label',
			valueField: 'id',
			options: this.state.themes
		},{
			name: 'company',
			type: 'select',
			placeholder: '-- Selecione uma Instituição --',
			label: "Instituição",
			value: this.state.model ? this.state.model.get("company").id:null,
			displayField: 'name',
			valueField: 'id',
			options: this.state.companies
		}];
	},
	updateLoadingState() {
		this.setState({
			fields: this.getFields(),
			loading:
				!this.state.companies
				|| !this.state.themes
				|| (this.props.params.modelId && !this.state.model)
		});
	},
	componentDidMount() {
		var me = this;
		UserStore.on("sync", (model) => {
			me.context.router.push("/users");
		}, me);
		UserStore.on("retrieve", (model) => {
			me.setState({
				model: model
			});
			me.updateLoadingState();
		}, me);

		CompanyStore.on("sync", (store) => {
			me.setState({
				companies: store.models
			});
			me.updateLoadingState();
		}, me);
		CompanyStore.on("themes", (themes) => {
			me.setState({
				themes: themes
			});
			me.updateLoadingState();
		}, me);

		if (this.props.params.modelId) {
			UserStore.dispatch({
				action: UserStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND,
			data: null
		});
		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND_THEMES,
			data: null
		});
	},
	componentWillUnmount() {
		UserStore.off(null, null, this);
		CompanyStore.off(null, null, this);
	},
	
	onSubmit(data) {
		var me = this;
		if (me.props.params.modelId) {
			me.state.model.set(data);
			UserStore.dispatch({
				action: UserStore.ACTION_UPDATE,
				data: me.state.model
			});
		} else {
			UserStore.dispatch({
				action: UserStore.ACTION_CREATE,
				data: data
			});
		}
	},

	render() {
		return (<div className="col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10 fpdi-card animated fadeIn">
			{this.state.loading ? <LoadingGauge />:<div>
				<h1>
					Editar usuário
				</h1>
				<VerticalForm
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={UserStore}
					submitLabel="Salvar"
				/>
			</div>}
		</div>);
	}
});
