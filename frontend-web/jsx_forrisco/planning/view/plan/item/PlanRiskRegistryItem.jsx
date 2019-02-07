import React from 'react';
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import PlanRiskItemField from "forpdi/jsx_forrisco/planning/widget/planrisk/item/PlanRiskItemField.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";


export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			cancelLabel: "Cancelar",
			submitLabel: "Salvar",
			vizualization: false,
			formFields: []
		}
	},

	componentDidMount() {
		this.getFields();
	},

	getFields() {

		/*Título do Item*/
		var formFields = [{
			name: 'description',
			type: AttributeTypes.TEXT_FIELD,
			placeholder: "Título do Item",
			maxLength: 100,
			label: "Título",
			required: true,
		}];

		this.setState({
			formFields: formFields
		});
	},

	toggleFields() {
		this.setState({
			vizualization: !this.state.vizualization,
		});
	},

	onSubmit(event) {
		event.preventDefault();
		const formData = new FormData(event.target);

		if (formData.get('description') === '') {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		this.state.formFields.shift(); //Remove o Título da lista de Campos

		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_SAVE_ITENS,
			data: {
				name:  formData.get('description'),
				description: formData.get('description'),
				planRisk: {
					id: this.props.params.planRiskId
				},
				planRiskItemField: this.state.formFields
			}
		});
	},

	render() {
		return (
			<div>
				<form onSubmit={this.onSubmit} ref="newPlanRiskItemForm">
					<div className="fpdi-card fpdi-card-full floatLeft">
						{
							this.state.formFields.map((field, index) => {
								if (field.type === AttributeTypes.TEXT_AREA_FIELD || field.type ===  AttributeTypes.ATTACHMENT_FIELD) {
									return (
										<div>
											<PlanRiskItemField
												vizualization={this.state.vizualization}
												getFields={this.getFields}
												toggle={this.toggleFields}
												field={field}/>
										</div>
									)
								}

								return (
									<div>
										<VerticalInput key={index} fieldDef={field}/>
									</div>
								)
							})
						}

						{
							/*<FieldItemInput/>*/
							this.state.vizualization ?

								<PlanRiskItemField
									fields={this.state.formFields}
									vizualization={this.state.vizualization}
									getFields={this.getFields}
									toggle={this.toggleFields}/>
								:

								/* Botão de dicionar Novo Campo */
								<button onClick={this.toggleFields} className="btn btn-sm btn-neutral marginTop20">
									<span className="mdi mdi-plus"/> {Messages.get("label.addNewField")}
								</button>
						}

						<br/><br/><br/>
						<div className="form-group">
							<button type="submit" className="btn btn-sm btn-success">{this.state.submitLabel}</button>
							<button className="btn btn-sm btn-default">{this.state.cancelLabel}</button>
						</div>
					</div>
				</form>
			</div>
		)
	}
})
//

