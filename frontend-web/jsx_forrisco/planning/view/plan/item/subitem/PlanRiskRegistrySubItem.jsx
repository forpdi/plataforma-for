import React from 'react';
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import PlanRiskItemField from "forpdi/jsx_forrisco/planning/widget/planrisk/item/PlanRiskItemField.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import _ from "underscore";


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
			formFields: [],
			title: Messages.getEditable("label.newSubitem","fpdi-nav-label"),
		}
	},

	componentDidMount() {
		this.getFields();

		_.defer(() => {
			this.context.tabPanel.addTab(this.props.location.pathname, 'Novo Item');
		});
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
			edit: false
		}];

		this.setState({
			formFields: formFields
		});
	},

	toggleFields() {
		this.state.formFields.map( (fieldItem, index) => {
			fieldItem.editInstance = false
		});
		this.setState({
			globalEditInstance: true,
			vizualization: true,
		});
	},

	editFields(id, bool){
		this.state.formFields.map( (fieldItem, index) => {
			if (id === index){
				fieldItem.editInstance = bool
			}
		});

		if(this.state.globalEditInstance === true) { //Se já estiver editando
			this.setState({
				formFields: this.state.formFields,
				vizualization: false
			})
		} else {
			this.setState({
				formFields: this.state.formFields,
				vizualization: false
			})
		}
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
		this.context.router.push(
			"/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/overview"
		);
	},

	render() {
		return (
			<div>
				<form onSubmit={this.onSubmit} ref="newPlanRiskItemForm">
					<div className="fpdi-card fpdi-card-full floatLeft">
						<h1>
							{this.state.title}
						</h1>
						{
							this.state.formFields.map((field, index) => {
								if (field.type === AttributeTypes.TEXT_AREA_FIELD || field.type ===  AttributeTypes.ATTACHMENT_FIELD) {
									return (
										<div key={index}>
											<PlanRiskItemField
												toggle={this.toggleFields}
												vizualization={this.state.vizualization}
												getFields={this.getFields}
												deleteFields={this.deleteFields}
												editFields={this.editFields}
												editRichTextField={this.editRichTextField}
												setFieldValue={this.setFieldValue}
												editFieldTitle={this.editFieldTitle}
												editType={this.editType}
												editImg={this.editImg}
												index={index}
												fields={this.state.formFields}
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
							this.state.vizualization === true ?

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
							<button className="btn btn-sm btn-default" onClick={this.onCancel}>{this.state.cancelLabel}</button>
						</div>
					</div>
				</form>
			</div>
		)
	}
})
//
