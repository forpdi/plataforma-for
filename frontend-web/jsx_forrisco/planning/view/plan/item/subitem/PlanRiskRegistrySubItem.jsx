import React from 'react';
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import PlanRiskItemField from "forpdi/jsx_forrisco/planning/widget/planrisk/item/PlanRiskItemField.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import _ from "underscore";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

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
		_.defer(() => {
			this.context.tabPanel.addTab(this.props.location.pathname, 'Novo Subitem');
		});
	},

	getFields() {

		/*Título do SubItem*/
		var formFields = [{
			name: 'description',
			type: AttributeTypes.TEXT_FIELD,
			placeholder: "Título do Subitem",
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
	
	editFields(id, bool){
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index){
				fieldItem.editInstance = bool
			}
		});

		this.setState({
			formFields: this.state.formFields,
			vizualization: !this.state.vizualization
		})
	},

	deleteFields(id) {
		Modal.confirmCustom(() => {
			Modal.hide();
			this.state.formFields.map((fieldItem, index) => {
				if (id === index) {
					this.state.formFields.splice(index, 1)
				}
			});

			this.setState({
				formFields: this.state.formFields
			})
		}, Messages.get("label.msg.deleteField"), () => {
			Modal.hide()
		});
	},

	onSubmit(event) {
		var submitFields = [];

		event.preventDefault();
		const formData = new FormData(event.target);

		if (formData.get('description') === '') {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
			return false;
		}

		this.state.formFields.shift(); 		 //Remove o Título da lista de Campos

		this.setState({
			formFields: this.state.formFields
		});

		this.state.formFields.map( (field, index) => {
			delete field.editInstance;

			submitFields.push({
				name: field.fieldName,
				type: field.type,
				description: field.fieldContent,
				fileLink: field.fileLink,
				isText: field.isText
			})
		});

		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_SAVE_SUBITENS,
			data: {
				name: formData.get('description'),
				description: formData.get('description'),
				planRiskItem: {
					id: this.props.params.itemId
				},
				planRiskSubItemField: submitFields
			}
		});

		PlanRiskItemStore.on('subItemSaved', response => {
			this.context.toastr.addAlertSuccess(Messages.get("label.successNewItem"));
			this.context.router.push(
				"/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/" + this.props.params.itemId + "/subitem/" + response.data.id
			);
		});
	},

	onCancel() {
		this.context.router.push("/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/" + this.props.params.planRiskId + '/info');
	},

	render() {
		return (
			<div>
				<form onSubmit={this.onSubmit} ref="newPlanRiskItemForm">
					<div className="fpdi-card fpdi-card-full floatLeft">
						{
							this.state.formFields.map((field, index) => {
								if (field.type === AttributeTypes.TEXT_AREA_FIELD || field.type === AttributeTypes.ATTACHMENT_FIELD) {
									return (
										<div key={index}>
											<PlanRiskItemField
												vizualization={this.state.vizualization}
												getFields={this.getFields}
												toggle={this.toggleFields}
												deleteFields={this.deleteFields}
												editFields={this.editFields}
												index={index}
												field={field}/>
										</div>
									)
								}

								return (
									<div key={index}>
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
									editFields={this.editFields}
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
							<button className="btn btn-sm btn-default"
									onClick={this.onCancel}>{this.state.cancelLabel}</button>
						</div>
					</div>
				</form>
			</div>
		)
	}
})
//

