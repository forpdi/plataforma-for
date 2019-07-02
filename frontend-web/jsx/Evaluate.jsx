
import React from "react";
import { Link } from 'react-router';

import CompanyDomainStore from "forpdi/jsx/core/store/CompanyDomain.jsx";
import CompanyStore from "forpdi/jsx/core/store/Company.jsx";

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import AppLogo from "forpdi/img/plataforma-for-logo.svg";

import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
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
			fieldsUser: null,
			fieldsDomain: null,
			fieldsCompany: null,
			companies: null,
			themes: null
		};
	},

	getFieldsUser() {
		return [{
			name: "nome",
			type: "text",
			placeholder: "",
			maxLength: 255,
			required: true,
			label: "nome",
		},
		{
			name: "e-mail",
			type: "text",
			placeholder: "",
			maxLength: 255,
			required: true,
			label: "e-mail",
		},
		{
			name: "telefone",
			type: "tel",
			placeholder: "",
			maxLength: 15,
			required: true,
			label: "telefone",
		}, {
			name: "cpf",
			type: "cpf",
			placeholder: "",
			maxLength: 12,
			required: true,
			label: "CPF",
		},

		]
	},



	getFieldsCompany() {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			maxLength: 255,
			required: true,
			label: Messages.getEditable("label.name", "fpdi-nav-label"),
			//value: model ? model.get("name") : null
		}, {
			name: "logo",
			type: "text",
			maxLength: 255,
			placeholder: "",
			label: Messages.getEditable("label.system.companyLogo", "fpdi-nav-label"),
			//value: model ? model.get("logo") : null,
			onChange: this.isImg
		}, {
			name: 'description',
			type: 'textarea',
			maxLength: 10000,
			placeholder: '',
			label: Messages.getEditable("label.description", "fpdi-nav-label"),
			//value: model ? model.get("description") : null

		}, {
			name: 'localization',
			type: 'text',
			maxLength: 255,
			placeholder: 'Cidade/Estado',
			required: true,
			label: Messages.getEditable("label.cityState", "fpdi-nav-label"),
			//value: model ? model.get("localization") : null
		}, {
			name: "showDashboard",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.enableCommunityDashboard", "fpdi-nav-label"),
			//value: model ? model.get("showDashboard") : true,
			onChange: this.fieldsDashboardBoardCommunity
		}, {
			name: "showMaturity",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showMaturityDateToCommunity", "fpdi-nav-label"),
			//value: model ? model.get("showMaturity") : true
		}, {
			name: "showBudgetElement",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showBudgetElement", "fpdi-nav-label"),
			//value: model ? model.get("showBudgetElement") : true
		}]
	},

	getFieldsDomain() {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			maxLength: 255,
			required: true,
			label: Messages.getEditable("label.name", "fpdi-nav-label"),
			//value: model ? model.get("name") : null
		}, {
			name: "logo",
			type: "text",
			maxLength: 255,
			placeholder: "",
			label: Messages.getEditable("label.system.companyLogo", "fpdi-nav-label"),
			//value: model ? model.get("logo") : null,
			onChange: this.isImg
		}, {
			name: 'description',
			type: 'textarea',
			maxLength: 10000,
			placeholder: '',
			label: Messages.getEditable("label.description", "fpdi-nav-label"),
			//value: model ? model.get("description") : null

		}, {
			name: 'localization',
			type: 'text',
			maxLength: 255,
			placeholder: 'Cidade/Estado',
			required: true,
			label: Messages.getEditable("label.cityState", "fpdi-nav-label"),
			//value: model ? model.get("localization") : null
		}, {
			name: "showDashboard",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.enableCommunityDashboard", "fpdi-nav-label"),
			//value: model ? model.get("showDashboard") : true,
			onChange: this.fieldsDashboardBoardCommunity
		}, {
			name: "showMaturity",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showMaturityDateToCommunity", "fpdi-nav-label"),
			//value: model ? model.get("showMaturity") : true
		}, {
			name: "showBudgetElement",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showBudgetElement", "fpdi-nav-label"),
			//value: model ? model.get("showBudgetElement") : true
		}, {
			name: "host",
			type: "text",
			placeholder: "",
			maxLength: 128,
			label: Messages.getEditable("label.host", "fpdi-nav-label"),
			required: true,
			helpBox: '.teste.forpdi.org',
			value: this.state.model ? this.state.model.get("host") : null,
			className: "form-control-h",
			style: { "text-align": "-webkit-right", "height": "33px", "border-radius": "3px", "border": "1px solid #ccc" }
		},
		{
			name: "baseUrl",
			type: "url",
			placeholder: "",
			maxLength: 255,
			label: Messages.getEditable("label.baseUrl", "fpdi-nav-label"),
			required: true,
			helpBox: "Ex: http://app.teste.forpdi.org/",
			value: this.state.model ? this.state.model.get("baseUrl") : null,
		}, , {
			name: 'company',
			type: 'select',
			placeholder: Messages.get("label.selectInstitution"),
			label: Messages.getEditable("label.institution", "fpdi-nav-label"),
			required: true,
			value: this.state.model ? this.state.model.get("company").id : null,
			displayField: 'name',
			valueField: 'id',
			options: this.state.companies
		}];
	},
	/*updateLoadingState() {
		this.setState({
			fields: this.getFields(),
			loading:
				!this.state.companies
				|| !this.state.themes
				|| (this.props.params.modelId && !this.state.model)
		});
	},*/

	componentDidMount() {
		var me = this;

		CompanyStore.on("themes", (themes) => {
			me.setState({
				themes: themes
			});
		}, me);

		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND_THEMES,
			data: null
		});

		this.setState({
			//fields: this.getFields(),
			//fields: this.getFields(),
			fieldsUser: this.getFieldsUser(),
			fieldsDomain: this.getFieldsDomain(),
			fieldsCompany: this.getFieldsCompany(),
			loading: false
		})
	},

	componentWillUnmount() {
		CompanyDomainStore.off(null, null, this);
		CompanyStore.off(null, null, this);
	},

	onSubmit(data) {


		console.log("submit")
		return


		var me = this;
		data.company = { id: data.company };

		var msg = Validate.validationCompanyDomainEdit(data, this.refs.CompanyDomainEditForm);

		if (msg != "") {
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
			this.context.toastr.addAlertSuccess(Messages.get("notification.domain.update") + " " + Messages.get("notification.pageRefreshRequest"));
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


	cancelWrapper(evt) {
		evt.preventDefault();
		this.context.router.push('/login')
	},
	submitWrapper(evt) {
		evt.preventDefault();


		console.log(this.refs)


		var data = {};
		var user = {};
		var company = {};
		var domain = {};




		user['nome'] = this.refs['user-0'].refs['field-nome'].value
		user['email'] = this.refs['user-1'].refs['field-e-mail'].value
		user['telefone'] = this.refs['user-2'].refs['field-telefone'].input.value
		user['cpf'] = this.refs['user-3'].refs['field-cpf'].input.value


		console.log(JSON.stringify({ user:user }))

		company['name'] = this.refs['company-0'].refs['field-name'].value
		company['logo'] = this.refs['company-1'].refs['field-logo'].value
		company['description'] = this.refs['company-2'].refs['field-description'].value
		company['localization'] = this.refs['company-3'].refs['field-localization'].value
		company['showDashboard'] = this.refs['company-4'].refs['field-showDashboard'].value
		company['showMaturity'] = this.refs['company-5'].refs['field-showMaturity'].value
		company['showBudgetElement'] = this.refs['company-6'].refs['field-showBudgetElement'].value

		console.log(JSON.stringify({ company:company }))



		domain['host'] = this.refs['host'].refs['field-host'].value+'.teste.forpdi.org'
		domain['theme'] = this.refs['theme'].refs['field-theme'].value

		console.log(JSON.stringify({ domain:domain }))


		data['user']=user
		data["company"] = company
		data["domain"] = domain


		console.log(JSON.stringify({ data:data }))

		return data;
	},

	render() {
		return (<div className="col-sm-offset-3 col-sm-6 animated fadeIn">

			<div className="fpdi-login-header">
				<img
					className="fpdi-login-brand"
					src={AppLogo}
					alt={Messages.getEditable("label.forRiscoLogo", "fpdi-nav-label")}
				/>
				<center>
					<h3 className="frisco-login-subtitle">
						{Messages.get("label.login.titlePlatformComplement")}<br />
						{/* Messages.getEditable("label.login.title","fpdi-nav-label") */}
					</h3>
				</center>
			</div>

			{this.state.loading ? <LoadingGauge /> : <div>
				<br />

				<div className="panel panel-default">
					<div className="panel-body">


						<h1>
							Dados pessoais
				</h1>

						{this.state.fieldsUser.map((field, idx) => {
							return (<VerticalInput
								name={field.name}
								formId={this.props.id}
								fieldDef={field}
								key={field.value ? idx : field.name}
								onConfirm={this.submitWrapper}
								formAlertErrorFixedHeight={true}
								ref={"user-" + idx}
							/>);
						})}

						<br />

						<h1>
							Adicione uma nova instituição
				</h1>

						{this.state.fieldsCompany.map((field, idx) => {
							return (<VerticalInput
								name={field.name}
								formId={this.props.id}
								fieldDef={field}
								key={field.value ? idx : field.name}
								onConfirm={this.submitWrapper}
								ref={field.ref}
								formAlertErrorFixedHeight={true}
								ref={"company-" + idx}
							/>);
						})}

						<br />
						<h1>
							Adicionar novo domínio
				</h1>

						<HorizontalInput
							formId={this.props.id}
							fieldDef={{
								name: "host",
								type: "text",
								placeholder: "",
								maxLength: 128,
								label: Messages.getEditable("label.host", "fpdi-nav-label"),
								required: true,
								helpBox: '.teste.forpdi.org',
								value: this.state.model ? this.state.model.get("host") : null,
								className: "form-control-h",
								style: { "text-align": "-webkit-right", "height": "33px", "border-radius": "3px", "border": "1px solid #ccc" }
							}}
							ref={"host"}
							key={"host"}

						/>

						<VerticalInput
							formId={this.props.id}
							fieldDef={{
								name: 'theme',
								type: 'select',
								placeholder: Messages.get("label.selectTheme"),
								label: Messages.getEditable("label.theme", "fpdi-nav-label"),
								required: true,
								value: this.state.model ? this.state.model.get("theme") : null,
								displayField: 'label',
								valueField: 'id',
								options: this.state.themes
							}}
							ref={"theme"}
							key={"theme"}
						/>

						<div className="form-group text-left">
							<input type="submit" className="btn btn-sm btn-success" ref="btn-submit" value={"Enviar"} onClick={this.submitWrapper} />
							<input type="submit" className="btn btn-sm btn-defaul" ref="btn-submit" value={"Cancelar"} onClick={this.cancelWrapper} />
							{/*<Link className="btn btn-sm btn-default" to={"/login/"}>Cancelar</Link>*/}
						</div>
					</div>
				</div>
			</div>}
		</div>);
	}
});
