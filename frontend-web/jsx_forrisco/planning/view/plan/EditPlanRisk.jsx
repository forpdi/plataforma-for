import React from "react";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import Messages from "@/core/util/Messages";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import _ from "underscore";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem";

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
			planRiskFields: []
		};
	},

	componentDidMount() {
		PlanRiskStore.on('retrivedplanrisk', response => {

			var fields = [];
			fields.push({
				name: "name",
				type: "text",
				//required: true,
				maxLength: 240,
				placeholder: "Novo Plano de Gestão de Riscos",
				label: Messages.getEditable("label.name", "fpdi-nav-label"),
				value: response.attributes.name,
			}, {
				name: "description",
				type: "textarea",
				placeholder: "Descrição da Política",
				maxLength: 9900,
				label: Messages.getEditable("label.descriptionPolicy", "fpdi-nav-label"),
				value: response.attributes.description,
			}, {
				name: "linkedPolicy",
				type: "select",
				options: [{
					id: response.attributes.policy.id,
					label: response.attributes.policy.name
				}],
				className: "form-control-h",
				required: true,
				displayField: 'label',
				valueField: 'id',
				label: Messages.getEditable("label.linkPlanPolicy", "fpdi-nav-label"),
				value: response.attributes.policy.name
			});

			this.setState({
				planRiskFields: fields
			});

			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, response.attributes.policy.name);
			});
		});
	},

	componentWillUnmount() {
		//PlanRiskStore.off('retrivedplanrisk');
	},

	getFields() {

	},

	handleSubmit(event) {
		event.preventDefault();
		const formData = new FormData(event.target);

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_EDIT_PLANRISK,
			data: {
				planRisk: {
					id: this.props.params.planRiskId,	//ID do plano a ser editado
					name: formData.get('name'),
					description: formData.get('description'),
					policy: {
						id: formData.get('linkedPolicy')
					}
				}
			}
		});

		PlanRiskStore.on('editPlanRisk', response => {
			this.context.toastr.addAlertSuccess("Plano de Risco editado com sucesso");
			this.context.router.push("/forrisco/plan-risk/" + response.data.id + "/item/" + this.props.params.itemId + '/info');
			PlanRiskItemStore.off('editPlanRisk');
		})
	},

	render() {
		return (
			<div>
				<h1 className="marginLeft115">Editar Plano de Risco</h1>
				<div className="fpdi-card padding40">
					<form onSubmit={this.handleSubmit} ref={"planRiskEditForm"}>

						{
							this.state.planRiskFields.map((field, index) => {
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
