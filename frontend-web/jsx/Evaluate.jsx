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
import Toastr from 'toastr';

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
		}, {
			name: "newPassword",
			type: "password",
			placeholder: "",
			label: Messages.getEditable("label.newPassword", "fpdi-nav-label"),
			value: "",
			required: true
		}, {
			name: "newPasswordTwo",
			type: "password",
			placeholder: "",
			label: Messages.getEditable("label.newPasswordTwo", "fpdi-nav-label"),
			value: "",
			required: true
		}]
	},



	getFieldsCompany() {
		return [{
			name: "name",
			type: "text",
			placeholder: "",
			maxLength: 255,
			required: true,
			label: Messages.getEditable("label.name", "fpdi-nav-label"),
		}, {
			name: "logo",
			type: "text",
			maxLength: 255,
			placeholder: "",
			label: Messages.getEditable("label.system.companyLogo", "fpdi-nav-label"),
			onChange: this.isImg
		}, {
			name: 'description',
			type: 'textarea',
			maxLength: 10000,
			placeholder: '',
			label: Messages.getEditable("label.description", "fpdi-nav-label"),
		}, {
			name: 'localization',
			type: 'text',
			maxLength: 255,
			placeholder: 'Cidade/Estado',
			required: true,
			label: Messages.getEditable("label.cityState", "fpdi-nav-label"),
		}, {
			name: "showDashboard",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.enableCommunityDashboard", "fpdi-nav-label"),
			onChange: this.fieldsDashboardBoardCommunity
		}, {
			name: "showMaturity",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showMaturityDateToCommunity", "fpdi-nav-label"),
		}, {
			name: "showBudgetElement",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showBudgetElement", "fpdi-nav-label"),
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
		}, {
			name: "logo",
			type: "text",
			maxLength: 255,
			placeholder: "",
			label: Messages.getEditable("label.system.companyLogo", "fpdi-nav-label"),
			onChange: this.isImg
		}, {
			name: 'description',
			type: 'textarea',
			maxLength: 10000,
			placeholder: '',
			label: Messages.getEditable("label.description", "fpdi-nav-label"),
		}, {
			name: 'localization',
			type: 'text',
			maxLength: 255,
			placeholder: 'Cidade/Estado',
			required: true,
			label: Messages.getEditable("label.cityState", "fpdi-nav-label"),
		}, {
			name: "showDashboard",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.enableCommunityDashboard", "fpdi-nav-label"),
			onChange: this.fieldsDashboardBoardCommunity
		}, {
			name: "showMaturity",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showMaturityDateToCommunity", "fpdi-nav-label"),
		}, {
			name: "showBudgetElement",
			type: "checkbox",
			placeholder: "",
			required: true,
			label: Messages.getEditable("label.showBudgetElement", "fpdi-nav-label"),
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

	componentDidMount() {
		var me = this;

		CompanyStore.on("themes", (themes) => {
			me.setState({
				themes: themes
			});
		}, me);

		CompanyStore.on("evaluate", (model) => {
			if (model.success) {
				var msg = Messages.get("notification.policy.save");
				Toastr.success(msg)
				window.location = model.message
			} else {
				var msg = model.responseJSON ? model.responseJSON.message : "Erro ao criar Ambiente de Teste"
				Toastr.error(msg)
			}
		}, me)

		CompanyStore.dispatch({
			action: CompanyStore.ACTION_FIND_THEMES,
			data: null
		});

		this.setState({
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

	cancelWrapper(evt) {
		evt.preventDefault();
		this.context.router.push('/login')
	},

	submitWrapper(evt) {
		evt.preventDefault();

		var data = {};
		var user = {};
		var company = {};
		var domain = {};

		user['name'] = this.refs['user-0'].refs['field-nome'].value
		user['email'] = this.refs['user-1'].refs['field-e-mail'].value
		user['cellphone'] = this.refs['user-2'].refs['field-telefone'].input.value
		user['cpf'] = this.refs['user-3'].refs['field-cpf'].input.value
		user['password'] = this.refs['user-4'].refs['field-newPassword'].value
		user['passwordconfirm'] = this.refs['user-5'].refs['field-newPasswordTwo'].value

		company['name'] = this.refs['company-0'].refs['field-name'].value
		company['logo'] = this.refs['company-1'].refs['field-logo'].value
		company['description'] = this.refs['company-2'].refs['field-description'].value
		company['localization'] = this.refs['company-3'].refs['field-localization'].value
		company['showDashboard'] = this.refs['company-4'].refs['field-showDashboard'].checked
		company['showMaturity'] = this.refs['company-5'].refs['field-showMaturity'].checked
		company['showBudgetElement'] = this.refs['company-6'].refs['field-showBudgetElement'].checked

		domain['host'] = this.refs['host'].refs['field-host'].value + '.teste.forpdi.org'
		domain['theme'] = this.refs['theme'].refs['field-theme'].value

		data['user'] = user
		data["company"] = company
		data["domain"] = domain


		var errorUser = this.validationProfileUser(user);
		var errorCompany = this.validationCompany(company);
		var errorDomain = this.validationDomain(domain);

		if (errorUser) {
			Toastr.error(Messages.get("label.errorSavingdata"));
			return
		}
		if (errorCompany) {
			Toastr.error(Messages.get("label.errorSavingdata"));
			return
		}
		if (errorDomain) {
			Toastr.error(Messages.get("label.errorSavingdata"));
			return
		}

		CompanyStore.dispatch({
			action: CompanyStore.ACTION_EVALUATION,
			data:
			{
				user: user,
				company: company,
				domain: domain
			}
		});
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
							Adicione um novo domínio
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
						</div>
					</div>
				</div>
			</div>}
		</div>);
	},

	validationProfileUser(data) {
		var errorField = false;
		var msg;

		var name = this.refs['user-0']
		var email = this.refs['user-1']
		var phone = this.refs['user-2']
		var cpf = this.refs['user-3']
		var newPassword = this.refs['user-4']
		var newPasswordTwo = this.refs['user-5']

		if (data.name.trim() == "") {
			name.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
			name.refs['field-nome'].className += " borderError";
			errorField = true;
		} else {
			name.refs["formAlertError"].innerHTML = "";
			name.refs['field-nome'].className = "form-control";
		}

		if (data.cellphone.split("_")[0].length < 13) {
			msg = Messages.get("label.form.error");
			phone.refs['field-telefone'].input.className = "form-control borderError";
			phone.refs["formAlertError"].innerHTML = Messages.get("label.invalidPhoneNumber");
			errorField = true;
		} else {
			phone.refs['field-telefone'].input.className = "form-control";
			phone.refs["formAlertError"].innerHTML = "";
		}

		if (data.cpf == "" || !!data.cpf.match(/^(\s)+$/)) {
			cpf.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
			cpf.refs["field-cpf"].input.className = "form-control borderError";
			errorField = true;
		} else if (!this.validarCPF(data.cpf)) {
			cpf.refs["formAlertError"].innerHTML = Messages.get("label.cpfInvalid");
			cpf.refs["field-cpf"].input.className = "form-control borderError";
			errorField = true;
		} else {
			if (cpf.refs["field-cpf"].input.className && cpf.refs["field-cpf"].input.className.indexOf('borderError')) {
				cpf.refs["field-cpf"].input.className = "form-control";
				cpf.refs["formAlertError"].innerHTML = "";
			}
		}

		if (data.email != undefined) {
			if (data.email.trim() == "") {
				email.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
				email.refs["field-e-mail"].className += " borderError";
				errorField = true;
			} else if (!this.emailIsValid(data.email.trim())) {
				email.refs["formAlertError"].innerHTML = Messages.get("label.emailInvalid");
				email.refs["field-e-mail"].className += " borderError";
				errorField = true;
			} else {
				email.refs["formAlertError"].innerHTML = "";
				email.refs["field-e-mail"].className = "form-control";
			}
		}

		if (data.password != undefined && data.passwordconfirm != undefined && !errorField) {

			if (data.password.trim() != "" && data.passwordconfirm.trim() != "") {
				if (data.password.trim().localeCompare(data.passwordconfirm.trim()) != 0) {
					newPassword.refs["formAlertError"].innerHTML = Messages.get("label.passwordNotMatch");
					newPasswordTwo.refs["formAlertError"].innerHTML = Messages.get("label.passwordNotMatch");
					newPasswordTwo.refs["field-newPasswordTwo"].className += " borderError";
					newPassword.refs["field-newPassword"].className += " borderError";
					errorField = true;

				} else {
					newPassword.refs["formAlertError"].innerHTML = "";
					newPasswordTwo.refs["formAlertError"].innerHTML = "";
					newPassword.refs["field-newPassword"].className = "form-control";
					newPasswordTwo.refs["field-newPasswordTwo"].className = "form-control";
				}

			}

		} else if (data.password != undefined) {
			if (data.password == "" || !!data.password.match(/^(\s)+$/)) {
				msg = Messages.get("label.form.error");
				newPassword.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
				newPassword.refs["field-newPassword"].className += " borderError";
				errorField = true;
			} else {
				if (newPassword.refs["field-newPassword"].className && newPassword.refs["field-newPassword"].className.indexOf('borderError')) {
					newPassword.refs["field-newPassword"].className = "form-control";
					newPassword.refs["formAlertError"].innerHTML = "";
				}
			}
			if (data.password.length < 5 || !!data.password.match(/^(\s)+$/)) {
				msg = Messages.get("label.form.error");
				newPassword.refs["formAlertError"].innerHTML = Messages.get("label.passwordMinimumFiveCaracteres");
				newPassword.refs["field-newPassword"].className += " borderError";
				errorField = true;
			} else {
				if (newPassword.refs["field-newPassword"].className && newPassword.refs["field-newPassword"].className.indexOf('borderError')) {
					newPassword.refs["field-newPassword"].className = "form-control";
					newPassword.refs["formAlertError"].innerHTML = "";
				}
			}
			if (data.passwordconfirm == "" || !!data.passwordconfirm.match(/^(\s)+$/)) {
				msg = Messages.get("label.form.error");
				newPasswordTwo.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
				newPasswordTwo.refs["field-newPasswordTwo"].className += " borderError";
				errorField = true;
			} else {
				if (data.password.trim().localeCompare(data.passwordconfirm.trim()) != 0) {
					newPassword.refs["formAlertError"].innerHTML = Messages.get("label.passwordNotMatch");
					newPasswordTwo.refs["formAlertError"].innerHTML = Messages.get("label.passwordNotMatch");
					newPassword.refs["field-newPassword"].className += " borderError";
					newPasswordTwo.refs["field-newPasswordTwo"].className += " borderError";
					errorField = true;
				}
				else if (newPasswordTwo.refs["field-newPasswordTwo"].className && newPasswordTwo.refs["field-newPasswordTwo"].className.indexOf('borderError')) {
					newPasswordTwo.refs["field-newPasswordTwo"].className = "form-control";
					newPasswordTwo.refs["formAlertError"].innerHTML = "";
				}
			}
		}

		return errorField;
	},

	validationCompany(data) {
		var msg;

		var company = this.refs['company-0']
		var localization = this.refs['company-3']

		if (data.name.trim() == "") {
			msg = Messages.get("label.form.error");
			company.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			company.refs["field-name"].className += " borderError";
		} else {
				company.refs["field-name"].className = "form-control";
				company.refs.formAlertError.innerHTML = "";
		}
		if (data.localization.trim() == "") {
			msg = Messages.get("label.form.error");
			localization.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
			localization.refs["field-localization"].className += " borderError";
		} else {
				localization.refs["field-localization"].className = "form-control";
				localization.refs["formAlertError"].innerHTML = "";
		}


		return msg
	},
	validationDomain(data) {
		var msg;

		var host = this.refs['host']
		var theme = this.refs['theme']


		if (data.host.split('.')[0] == "" || !!data.host.match(/^(\s)+$/)) {
			msg = Messages.get("label.form.error");
			host.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
			host.refs["field-host"].className += " borderError";
		} else {
			host.refs["field-host"].className = "form-control-h";
			host.refs["formAlertError"].innerHTML = "";
		}

		if (data.theme == "" || !!data.theme.match(/^(\s)+$/)) {
			msg = Messages.get("label.form.error");
			theme.refs["formAlertError"].innerHTML = Messages.get("label.alert.fieldEmpty");
			theme.refs["field-theme"].className += " borderError";
		} else {
			theme.refs["field-theme"].className = "form-control";
			theme.refs["formAlertError"].innerHTML = "";
		}

		return msg
	},

	validarCPF(cpf) {
		cpf = cpf.replace(/\./g, '').replace('-', '')
		if (cpf.length != 11 || cpf.replace(eval('/' + cpf.charAt(1) + '/g'), '') == '') {
			return false;
		} else {

			var d;
			var c;
			for (var n = 9; n < 11; n++) {
				for (d = 0, c = 0; c < n; c++)
					d += cpf.charAt(c) * ((n + 1) - c);
				d = ((10 * d) % 11) % 10;
				if (cpf.charAt(c) != d)
					return false;
			}
			return true;
		}
	},
	emailIsValid(email) {
		var user = email.substr(0, email.indexOf('@'));
		var domain = email.substr(email.indexOf('@') + 1, email.length);

		if ((user.length >= 1) && (domain.length >= 3) && (user.search("@") == -1) &&
			(domain.search("@") == -1) && (user.search(" ") == -1) && (domain.search(" ") == -1) &&
			(domain.search(".") != -1) && (domain.indexOf(".") >= 1) && (domain.lastIndexOf(".") < domain.length - 1)) {
			return true;
		}
		else {
			return false;
		}
	}
});
