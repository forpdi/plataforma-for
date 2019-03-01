import React from "react";
import PlanRiskItemField from "forpdi/jsx_forrisco/planning/widget/planrisk/item/PlanRiskItemField.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import AttributeTypes from "forpdi/jsx/planning/enum/AttributeTypes";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import Modal from "forpdi/jsx/core/widget/Modal";
import Messages from "forpdi/jsx/core/util/Messages";
import $ from "jquery";
import _ from "underscore";

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
			formFields: [],
			itemTitle: '',
			itemDescription: ''
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
			editInstance: false,
			onChange: this.setTitleValue,
		}];

		this.props.fieldsValues.map(editableFields => {
			formFields.push(editableFields);
		});

		this.setState({
			formFields: formFields,
			itemTitle: formFields[0].value,
			itemDescription: formFields[0].value
		});
	},

	setTitleValue() {
		var value = $('#field-description').val();
		this.setState({
			itemTitle: value,
			itemDescription: value
		})
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

	//Muda o Tipo do item
	editType(bool, id) {
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index) {
				fieldItem.isText = bool;
			}
		});

		this.setState({
			formFields: this.state.formFields
		});
	},

	//Ativa Instancia de Edição Pra um Item
	editFields(id, bool){
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index){
				fieldItem.editInstance = bool
			}
		});

		this.setState({
			formFields: this.state.formFields,
			vizualization: false
		})
	},

	//Edita o Título de um Item
	editFieldTitle(newTitle, id) {
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index){
				fieldItem.fieldName = newTitle
			}
		});

		this.setState({
			formFields: this.state.formFields
		})
	},

	//Edita a TextArea de um item
	editRichTextField(contenteValue, id) {
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index){
				fieldItem.fieldContent = contenteValue
			}
		});

		this.setState({
			formFields: this.state.formFields
		})
	},

	//Edita a img selecionada no campo
	editImg(img, id) {
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index) {
				fieldItem.fieldName = img.fieldName;
				fieldItem.fieldContent = img.fieldContent;
				fieldItem.isText = false;
				fieldItem.fileLink = img.fileLink
			}

			//this.state.formFields[index] = fieldItem;
		});

		this.setState({
			formFields: this.state.formFields
		});
	},

	//Confirma as edições no campo
	setFieldValue(field, id) {
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index) {
				fieldItem = field;
				fieldItem.editInstance = false;
			}

			this.state.formFields[index] = fieldItem;
		});

		this.setState({
			formFields: this.state.formFields
		});
	},

	toggleFields() {
		this.setState({
			vizualization: true,
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
			action: PlanRiskItemStore.ACTION_UPDATE_SUBITEM,
			data: {
				planRiskSubItem: {
					name: this.state.itemTitle,
					description: this.state.itemDescription,
					id: this.props.subitemId,
					planRiskItem: {
						id: this.props.itemId
					},
					planRiskSubItemField: submitFields
				},

			}
		});

		PlanRiskItemStore.on('subitemUpdated', response => {
			this.context.toastr.addAlertSuccess('Informações Atualizadas com Sucesso');
			this.context.router.push(
				"/forrisco/plan-risk/" + this.props.planRiskId + "/item/" + this.props.itemId + "/subitem/" + response.data.id
			);
			PlanRiskItemStore.off('subitemUpdated');
		});
	},

	onCancel() {
		this.context.router.push("/forrisco/plan-risk/" + this.props.planRiskId + "/item/" +  this.props.planRiskId + '/info');
	},

	render() {
		return (
			<div>
				<form onSubmit={this.onSubmit} ref="editPlanRiskItemForm">
					{
						this.state.formFields.map((field, index) => {
							// Se for um campo (Area de texto ou IMG) do Item
							if (field.fieldContent) {

								// Recupera todos os campos do Item
								if(field.editInstance !== true) {
									return (
										<div key={index}>
											<PlanRiskItemField
												getFields={this.getFields}
												deleteFields={this.deleteFields}
												editFields={this.editFields}
												editRichTextField={this.editRichTextField}
												setFieldValue={this.setFieldValue}
												editFieldTitle={this.editFieldTitle}
												editType={this.editType}
												editImg={this.editImg}
												index={index}
												field={field}
												isEditable={false}
											/>
										</div>
									)
								}

								//Entra na Instancia de Edição dos campos de um item
								//vizualization={this.props.onEdit}
								//vizualization={this.props.onEdit}
								if(field.editInstance === true) {
									return (
										<div key={index}>
											<PlanRiskItemField
												getFields={this.getFields}
												deleteFields={this.deleteFields}
												editFields={this.editFields}
												editRichTextField={this.editRichTextField}
												setFieldValue={this.setFieldValue}
												editFieldTitle={this.editFieldTitle}
												editType={this.editType}
												editImg={this.editImg}
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

					{
						//Adicionar novo campo aos campos já existentes
						this.state.vizualization === true ?
							<div>
								<PlanRiskItemField
									fields={this.state.formFields}
									vizualization={this.state.vizualization}
									setFieldValue={this.setFieldValue}
									editFields={this.editFields}
									getFields={this.getFields}
									toggle={this.toggleFields}/>
							</div>

							:

							/* Botão de dicionar Novo Campo */
							<div>
								<button onClick={this.toggleFields} className="btn btn-sm btn-neutral marginTop20">
									<span className="mdi mdi-plus"/> {Messages.get("label.addNewField")}
								</button>
							</div>

					}

					<br/><br/><br/>
					<div className="form-group">
						<button type="submit" className="btn btn-sm btn-success" onClick={this.onSubmit}>{this.state.submitLabel}</button>
						<button className="btn btn-sm btn-default"
								onClick={this.onCancel}>{this.state.cancelLabel}</button>
					</div>

				</form>
			</div>
		)


	}
})
