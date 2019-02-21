import React from "react";
import PlanRiskItemField from "forpdi/jsx_forrisco/planning/widget/planrisk/item/PlanRiskItemField.jsx";
import AttributeTypes from "@/planning/enum/AttributeTypes";
import _ from "underscore";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Modal from "@/core/widget/Modal";
import Messages from "@/core/util/Messages";
import caos from "forpdi/jsx_forrisco/planning/view/plan/item/PlanRiskRegistryItem";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		permissions: React.PropTypes.array.isRequired,
	},

	getInitialState() {
		return {
			cancelLabel: "Cancelar",
			submitLabel: "Salvar",
			vizualization: false,
			formFields: []
		};
	},

	componentDidMount() {
		this.getFields();
	},

	getFields() {
		var formFields = [{
			name: 'description',
			type: AttributeTypes.TEXT_FIELD,
			placeholder: "Título do Item",
			maxLength: 100,
			label: "Título",
			required: true,
			value: this.props.itemTitle,
			editInstance: false
		}];

		this.props.fieldsValues.map(editableFields => {
			formFields.push(editableFields);
		});

		this.setState({
			formFields: formFields
		});
	},

	deleteFields(id) {
		Modal.confirmCustom( () => {
			Modal.hide();
			this.state.formFields.map( (fieldItem, index) => {
				if (id === index) {
					this.state.formFields.splice(index, 1)
				}
			});

			this.setState({
				formFields: this.state.formFields
			})
		}, Messages.get("label.msg.deleteField"),()=>{Modal.hide()});
	},

	editFields(id, bool){

		this.state.formFields.map( (fieldItem, index) => {
			if (id === index){
				fieldItem.editInstance = bool
			}
		});

		this.setState({
			formFields: this.state.formFields
		})
	},

	onSubmit() {
		event.preventDefault();
	},

	render() {
		return (
			<div>
				<form onSubmit={this.onSubmit} ref="editPlanRiskItemForm">
					{
						this.state.formFields.map((field, index) => {
							// Se for um campo (Area de texto ou IMG) do Item
							if (field.fieldContent) {

								if(field.editInstance !== true) {
									return (
										<div key={index}>
											<PlanRiskItemField
												vizualization={this.props.onEdit}
												getFields={this.getFields}
												deleteFields={this.deleteFields}
												editFields={this.editFields}
												index={index}
												field={field}
												isEditable={false}
											/>
										</div>
									)
								}

								if(field.editInstance === true) {
									return (
										<div key={index}>
											<PlanRiskItemField
												vizualization={this.props.onEdit}
												getFields={this.getFields}
												deleteFields={this.deleteFields}
												editFields={this.editFields}
												index={index}
												field={field}
												isEditable={false}
											/>
										</div>
									)
								}
							}

							//Título do Item
							return (
								<div key={index}>
									<VerticalInput key={index} fieldDef={field}/>
								</div>
							)
						})
					}

					<div className="form-group">
						<button type="submit" className="btn btn-sm btn-success">{this.state.submitLabel}</button>
						<button className="btn btn-sm btn-default"
								onClick={this.onCancel}>{this.state.cancelLabel}</button>
					</div>

				</form>
			</div>
		)


	}
})
