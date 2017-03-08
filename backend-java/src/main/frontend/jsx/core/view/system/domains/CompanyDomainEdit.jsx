
import React from "react";
import {Link} from 'react-router';

import CompanyDomainStore from "forpdi/jsx/core/store/CompanyDomain.jsx";
import CompanyStore from "forpdi/jsx/core/store/Company.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

//import Toastr from 'toastr';

var Validate = Validation.validate;
var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired
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
			maxLength: 128,
			label: "Host",
			required:true,
			helpBox: 'Ex: app.forpdi.org',
			value: this.state.model ? this.state.model.get("host"):null
		},{
			name: "baseUrl",
			type: "url",
			placeholder: "",
			maxLength: 255,
			label: "URL Base",
			required:true,
			helpBox: "Ex: http://app.forpdi.org/",
			value: this.state.model ? this.state.model.get("baseUrl"):null
		},{
			name: 'theme',
			type: 'select',
			placeholder: '-- Selecione um Tema --',
			label: "Tema",
			required:true,
			value: this.state.model ? this.state.model.get("theme"):null,
			displayField: 'label',
			valueField: 'id',
			options: this.state.themes
		},{
			name: 'company',
			type: 'select',
			placeholder: '-- Selecione uma Instituição --',
			label: "Instituição",
			required:true,
			value: this.state.model ? this.state.model.get("company").id:null,
			displayField: 'name',
			valueField: 'id',
			options: this.state.companies
		}];
	},
	updateLoadingState() {
		if (this.isMounted()) {
			this.setState({
				fields: this.getFields(),
				loading:
					!this.state.companies
					|| !this.state.themes
					|| (this.props.params.modelId && !this.state.model)
			});
		}
	},
	componentDidMount() {
		var me = this;
		CompanyDomainStore.on("sync", (model) => {
			me.context.router.push("/system/domains");
			//Toastr.remove();
			//Toastr.success(Messages.get("notification.domain.save"));
			this.context.toastr.addAlertSuccess(Messages.get("notification.domain.save"));
		}, me);
		CompanyDomainStore.on("retrieve", (model) => {
			if (me.isMounted()) {
				me.setState({
					model: model
				});
			}
			me.updateLoadingState();
		}, me);

		CompanyStore.on("find", (store) => {
			if (me.isMounted()) {
				me.setState({
					companies: store.models
				});
			}
			me.updateLoadingState();
		}, me);
		CompanyStore.on("themes", (themes) => {
			if (me.isMounted()) {
				me.setState({
					themes: themes
				});
			}
			me.updateLoadingState();
		}, me);

		if (this.props.params.modelId) {
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_RETRIEVE,
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
		CompanyDomainStore.off(null, null, this);
		CompanyStore.off(null, null, this);
	},
	
	onSubmit(data) {
		var me = this;
		data.company = {id: data.company};
		
		var msg = Validate.validationCompanyDomainEdit(data, this.refs.CompanyDomainEditForm);

		if(msg!= ""){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
			return;
		}
		if (me.props.params.modelId) {
			me.state.model.set(data);
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_UPDATE,
				data: me.state.model
			});
			//Toastr.remove();
			//Toastr.success(Messages.get("notification.domain.update"));
			this.context.toastr.addAlertSuccess(Messages.get("notification.domain.update"));
		} else {
			CompanyDomainStore.dispatch({
				action: CompanyDomainStore.ACTION_SAVE,
				data: data,
				opts: {
					wait: true
				}
			});

		}
	},

	render() {
		return (<div className="col-sm-offset-3 col-sm-6 animated fadeIn">
			{this.state.loading ? <LoadingGauge />:<div>
				<h1>
					{this.state.model ? "Editar domínio":"Adicionar novo domínio"}
				</h1>
				<VerticalForm
					ref="CompanyDomainEditForm"
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={CompanyDomainStore}
					submitLabel="Salvar"
				/>
			</div>}
		</div>);
	}
});
