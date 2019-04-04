import React from "react";
import _ from "underscore";
import moment from 'moment'

import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import Messages from "@/core/util/Messages";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";


export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		roles: React.PropTypes.object.isRequired,
		planRisk: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			submitLabel: "Salvar",
			cancelLabel: "Cancelar",
			planRiskFields: [],
			policyOptions: [{id: null, label: ""}],
			validityBegin: null,
			validityEnd: null,
			policy: null,
			isLoading: true,
		};
	},

	componentDidMount() {

		PolicyStore.on("unarchivedpolicylisted", response => {
			const policies = [];

			response.data.map(policy => {
				policies.push({id: policy.id, label: policy.name})
			});
			this.setState({policyOptions: policies});

			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
				data: this.props.params.planRiskId
			});

		}, this);

		PlanRiskStore.on('retrivedplanrisk', response => {
			var fields = [];

			fields.push({
				name: "name",
				type: "text",
				ref: "newfield-name",
				required: true,
				maxLength: 240,
				placeholder: "Nome do Plano de Gestão de Riscos",
				label: Messages.getEditable("label.name", "fpdi-nav-label"),
				value: response.attributes.name,
			}, {
				name: "description",
				type: "textarea",
				placeholder: "Descrição da Política",
				maxLength: 9900,
				label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
				value: response.attributes.description,
			});

			this.setState({
				planRiskFields: fields,
				validityBegin: response.attributes.validityBegin,
				validityEnd: response.attributes.validityEnd,
				policy: response.attributes.policy,
				isLoading: false,
			});

			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, response.attributes.policy.name);
			});
		}, this);

		PlanRiskStore.on('editPlanRisk', response => {
			if (response.data) {
				this.context.toastr.addAlertSuccess("Plano de Risco editado com sucesso");
				this.context.router.push("/forrisco/plan-risk/" + response.data.id + "/item/overview");
				PlanRiskStore.dispatch({
					action: PlanRiskStore.ACTION_FIND_UNARCHIVED_FOR_MENU
				});
				this.context.tabPanel.removeTabByPath(this.props.location.pathname);
			} else {
				var msg = response.msg ? response.msg.responseJSON.message : "Erro ao criar Plano"
				this.context.toastr.addAlertError(msg);
			}
		});

		this.refreshComponent();
	},

	componentWillReceiveProps(newProps) {
		if (this.props.params.planRiskId !== newProps.params.planRiskId) {
			this.refreshComponent()
		}
	},

	refreshComponent() {
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_UNARCHIVED
		});
	},

	componentWillUnmount() {
		PlanRiskStore.off(null, null, this);
		PolicyStore.off(null, null, this);
	},

	handleSubmit(event) {
		event.preventDefault();
		const formData = new FormData(event.target);

		if (formData.get('name') === '') {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		var validityBegin = moment(this.state.validityBegin, 'DD/MM/YYYY').toDate();
		var validityEnd = moment(this.state.validityEnd, 'DD/MM/YYYY').toDate();
		if(validityBegin > validityEnd) {
			this.context.toastr.addAlertError("A data de início do prazo de vigência não deve ser superior à data de término");
			return false;
		}


		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_EDIT_PLANRISK,
			data: {
				planRisk: {
					id: this.props.params.planRiskId,	//ID do plano a ser editado
					name: formData.get('name'),
					description: formData.get('description'),
					validityBegin: this.state.validityBegin,
					validityEnd: this.state.validityEnd,
					policy: {
						id: formData.get('linkedPolicy')
					},
				}
			}
		});
	},

	onCancel() {
		this.context.tabPanel.removeTabByPath(this.props.location.pathname);
		this.context.router.push(
			"/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/overview"
		);
	},

	getVigencia() {
		var fields = [];
		fields.push(
			{
				name: "risk_vigencia_begin",
				type: "date",
				required: true,
				maxLength: 30,
				placeholder: "Data de início",
				value: this.state.validityBegin,
				onChange: (date) =>
					this.setState({ validityBegin: date ? date.format('DD/MM/YYYY') : null }),
			},
			{
				name: "risk_vigencia_end",
				type: "date",
				required: true,
				maxLength: 30,
				placeholder: "Data de término",
				value: this.state.validityEnd,
				onChange: (date) =>
					this.setState({ validityEnd: date ? date.format('DD/MM/YYYY') : null }),
			}
		);

		return fields;
	},

	getPolicyField() {
		return {
			name: "linkedPolicy",
			type: "select",
			className: "form-control-h",
			required: true,
			displayField: 'label',
			valueField: 'id',
			label: Messages.getEditable("label.linkPlanPolicy", "fpdi-nav-label"),
			value: this.state.policy.name,
			options: this.state.policyOptions,
		};
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		return (
			<div>
				<h1 className="marginLeft115">Editar Plano de Risco</h1>
				<div className="fpdi-card">
					<form onSubmit={this.handleSubmit} ref={"planRiskEditForm"}>

						{
							this.state.planRiskFields.map((field, index) => {
								return (
									<VerticalInput key={index} fieldDef={field}/>
								);
							})
						}

						<label htmlFor={this.state.fieldId} className="fpdi-text-label">
							Prazo de vigência
						</label>
						<br />
						{
							this.getVigencia().map((field, idx) =>
								<HorizontalInput
									name={field.name}
									formId={this.props.id}
									fieldDef={field}
									key={field.value ? idx : field.name}
									onConfirm={this.submitWrapper}
									ref={field.ref}
								/>
							)
						}

						<VerticalInput fieldDef={this.getPolicyField()}/>

						<div className="fpdi-editable-data-input-group">
							<button type="submit" className="btn btn-success">{this.state.submitLabel}</button>
							<button type="button" className="btn btn-default"
									onClick={this.onCancel}>{this.state.cancelLabel}</button>
						</div>
					</form>
				</div>
			</div>
		)
	}
})
