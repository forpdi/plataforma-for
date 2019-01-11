import React from "react";
import Messages from "@/core/util/Messages";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Router from "react-router";

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
			policyId: null,
			policies: [{
				id: null,
				label: ''
			}],
		};
	},

	componentDidMount() {
		var policiData = [];
		var resultSelect = PolicyStore.on("unarchivedpolicylisted", (response) => {

			if (response.status !== true) {
				this.setState({domainError: true});
			}

			if (response.success === true) {
				response.data.map((attr) => {
					policiData.push({
						id: attr.id,
						label: attr.description
					});
				});

				this.setState({
					policies: policiData, domainError: false,
				});
			}

			resultSelect.off("unarchivedpolicylisted");
		});
	},

	componentWillMount() {
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_UNARCHIVED,
		});
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
			placeholder: "Descrição da Política",
			maxLength: 9900,
			label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.description : null,
		}, {
			name: "linkedPolicy",
			type: "select",
			options: this.state.policies,
			required: true,
			displayField: 'label',
			valueField: 'id',
			placeholder: "Selecone a Política",
			label: Messages.getEditable("label.linkPlanPolicy", "dashboard-select-box"),
			value: this.state.planRiskModel ? this.state.planRiskModel.attributes.description : null,
		});

		return fields;
	},

	handleChange(event) {

		this.setState({
			value: event.target.value
		})
	},

	handleSubmit(event) {
		event.preventDefault();
		const formData = new FormData(event.target);

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_NEWPLANRISK,
			data: {
				name: formData.get('name'),
				description: formData.get('description'),
				policy: {
					id: formData.get('linkedPolicy')
				}
			}
		});
	},

	onCancel() {
		if (this.state.policies.length && this.state.policies.length === 1) {
			this.context.router.push("/forrisco/policy/" + this.state.policies[0].id + "/")
		}
	},

	render() {
		return (
			<div>
				<h1 className="marginLeft115">{Messages.getEditable("label.newPlanRisco", "fpdi-nav-label")}</h1>
				<div className="fpdi-card padding40">
					<form onSubmit={this.handleSubmit}>
						{
							this.getFields().map((field, index) => {
								return (
									<VerticalInput key={index} fieldDef={field}/>
								);
							})
						}

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
;
