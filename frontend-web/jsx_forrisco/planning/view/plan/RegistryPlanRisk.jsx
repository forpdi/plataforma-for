import React from "react";
import moment from 'moment'

import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import Messages from "@/core/util/Messages";

export default React.createClass({

	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {
		return {
			planRiskModel: null,
			submitLabel: "Salvar",
			cancelLabel: "Cancelar",
			validityBegin: null,
			validityEnd: null,
			policyId: null,
			plansLength: null,
			policies: [{
				id: null,
				label: ''
			}],
			loading:true,
		};
	},

	componentDidMount() {
		var policiData = [];
		var resultSelect = PolicyStore.on("unarchivedpolicylisted", (response) => {
			if (response.status !== true) {
				this.setState({domainError: true});
			}

			if(response.total ==0){
				this.context.router.push("/forrisco/policy/new")
			}

			if (response.success === true) {
				response.data.map((attr) => {
					policiData.push({
						id: attr.id,
						label: attr.name
					});
				});

				this.setState({
					policies: policiData, domainError: false,
					loading:false
				});
			}

			resultSelect.off("unarchivedpolicylisted");
		});

		PlanRiskStore.on("listedunarchivedplanrisk", (response) => {
			this.setState({
				plansLength: response.total
			});
		});

		PlanRiskStore.on("plariskcreated", (response) => {
			if (response.data) {
				this.context.toastr.addAlertSuccess(Messages.get("notification.plan.sav"));
				this.context.router.push("forrisco/plan-risk/" + response.data.id + "/");
				PlanRiskStore.dispatch({
					action: PlanRiskStore.ACTION_FIND_UNARCHIVED_FOR_MENU
				});
			} else {
				var msg = response.msg ? response.msg.responseJSON.message : "Erro ao criar Plano"
				this.context.toastr.addAlertError(msg);
			}
		});

		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_UNARCHIVED,
		});

		PolicyStore.dispatch({
			action: PlanRiskStore.ACTION_FIND_UNARCHIVED,
		});
	},

	componentWillUnmount() {
		PolicyStore.off(null, null, this);
		PlanRiskStore.off(null, null, this);
	},

	getFields() {
		var fields = [];
		fields.push({
			name: "name",
			type: "text",
			required: true,
			maxLength: 240,
			placeholder: "Novo Plano de Gestão de Riscos",
			label: Messages.getEditable("label.name", "fpdi-nav-label"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.name : null,
		}, {
			name: "description",
			type: "textarea",
			placeholder: "Descrição do Plano de Risco",
			maxLength: 9900,
			label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.description : null,
		});

		return fields;
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

	getPoliciesField() {
		return {
			name: "linkedPolicy",
			type: "select",
			options: this.state.policies,
			className: "form-control-h",
			required: true,
			displayField: 'label',
			valueField: 'id',
			placeholder: "Selecione a Política",
			label: Messages.getEditable("label.linkPlanPolicy", "fpdi-nav-label"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.description : null,
		};
	},

	handleSubmit(event) {
		event.preventDefault();
		const formData = new FormData(event.target);

		if (formData.get('name') === '') {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		if (formData.get('linkedPolicy') === '') {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		if ((!this.state.validityBegin && this.state.validityEnd) ||
				(this.state.validityBegin && !this.state.validityEnd)) {
			this.context.toastr.addAlertError("Não é permitido preencher somente uma das datas do prazo de vigência");
			return false;
		}
		var validityBegin = moment(this.state.validityBegin, 'DD/MM/YYYY').toDate();
		var validityEnd = moment(this.state.validityEnd, 'DD/MM/YYYY').toDate();
		if(validityBegin > validityEnd) {
			this.context.toastr.addAlertError("A data de início do prazo de vigência não deve ser superior à data de término");
			return false;
		}

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_NEWPLANRISK,
			data: {
				name: formData.get('name'),
				description: formData.get('description'),
				validityBegin: this.state.validityBegin,
				validityEnd: this.state.validityEnd,
				policy: {
					id: formData.get('linkedPolicy')
				}
			}
		});
	},

	onCancel() {
		if (this.state.plansLength > 0 || this.state.policies.length === 0) {
			this.context.router.push("/forrisco/home/");
		}

		if (this.state.policies.length && this.state.policies.length === 1) {
			this.context.router.push("/forrisco/policy/" + this.state.policies[0].id + "/")
		}
	},

	render() {

		if (this.state.loading === true) {
			return <LoadingGauge />;
		}
		return (
			<div>
				<div className="fpdi-card fpdi-card-full">
					<h1>{Messages.getEditable("label.newPlanRisco", "fpdi-nav-label")}</h1>
					<form onSubmit={this.handleSubmit}>

						{
							this.getFields().map((field, index) => {
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

						<VerticalInput fieldDef={this.getPoliciesField()}/>

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
});
