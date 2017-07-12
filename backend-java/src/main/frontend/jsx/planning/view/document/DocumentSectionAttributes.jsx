import S from 'string';
import React from "react";
import _ from 'underscore';
import {Link} from 'react-router';
import DocumentStore from "forpdi/jsx/planning/store/Document.jsx";
import ScheduleStore from "forpdi/jsx/planning/store/Schedule.jsx";
import StructureStore from 'forpdi/jsx/planning/store/Structure.jsx';
import TableStore from "forpdi/jsx/planning/store/TableFields.jsx";
import AttributeForm from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import TableFieldCreator from "forpdi/jsx/planning/view/document/TableFieldCreator.jsx";
import ScheduleFieldCreator from "forpdi/jsx/planning/view/document/ScheduleFieldCreator.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

//import Toastr from 'toastr';

var Validate = Validation.validate;

var VerticalForm = AttributeForm.VerticalForm;

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object.isRequired,
		planMacro: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {				
		return {
			loading: true,
			fields: [],
			model: null,
			subTitle: "",
			types: null,
			newField: false,
			tabPath: this.props.location.pathname,
			newFieldType: "",
			buttonsHide: false,
			tableColumns: [],
			vizualization: true
		};
	},

	getFields(model) {	
		var fields = [];
		if (model.documentAttributes) {			
			fields = model.documentAttributes.map((attr,idx) =>{				
				return {
					id: attr.id,
					name: "attribute-"+idx,
					type: attr.type,
					required: attr.required,
					placeholder: "",
					label: attr.name,
					value: attr.value,
					schedule: attr.schedule,
					extraSchedule: (attr.schedule ? this.saveInstanceSchedule : undefined),
					tableFields: attr.tableFields,
					selectPlans: attr.selectPlans,
					extraTableFields: (attr.tableFields ? this.saveInstanceTable : undefined),
					strategicObjectives: attr.strategicObjectives
				};
			})
		}
		return fields;
	},
	componentDidMount() {
		var me = this;		
		me.getSectionAttributes(me.props.params.sectionId, me.props.params.planId);

		DocumentStore.on('sectionAttributesSaved', (model) => {
			me.state.model = model;
			me.setState({
				subTitle: model.data.name,
				vizualization: true
			})			
			me.getSectionAttributes(model.data.id, me.props.params.planId);
			me.context.toastr.addAlertSuccess(Messages.get("label.success.informationSaved"));
		}, me);

		DocumentStore.on("sectionAttributesRetrieved", (model) => {		
			me.context.tabPanel.addTab(me.state.tabPath, model.name);
			me.setState({
				model: model,
				subTitle: model.name,
				fields: me.getFields(model),
				loading: false
			});
		}, me);

		DocumentStore.on("attributecreated", (attr) => {
			if(me.isMounted()){
				if(attr.type == AttributeTypes.TABLE_FIELD){				
					TableStore.dispatch({
						action: TableStore.ACTION_SAVE_STRUCTURES,
						data: {
							tableFields: {
								id: attr.tableFields.id,
								tableStructures: me.state.tableColumns,
								attributeId: attr.id
							}					
						}
					});
					me.setState({recentAttr: attr});
				} else if(attr.type == AttributeTypes.SCHEDULE_FIELD){				
					ScheduleStore.dispatch({
						action: ScheduleStore.ACTION_SAVE_STRUCTURES,
						data: {
							schedule: {
								id: attr.schedule.id,
								scheduleStructures: me.state.tableColumns
							}					
						}
					});
					me.setState({recentAttr: attr});
				} else {
					me.state.model.documentAttributes.push(attr);
					me.state.fields.push({
						name: "attribute-"+me.state.fields.length,
						id: attr.id,
						type: attr.type,
						required: attr.required,
						placeholder: "",
						label: attr.name,
						value: attr.value,
						schedule: attr.schedule,
						extraSchedule: (attr.schedule ? me.saveInstanceSchedule : undefined),
						tableFields: attr.tableFields,
						selectPlans: attr.selectPlans,
						extraTableFields: (attr.tableFields ? me.saveInstanceTable : undefined)
					});
					//Toastr.remove();
					//Toastr.success("Campo \""+attr.name+"\" adicionado com sucesso.");
					var nameCreated = attr.name.substr(0,50).concat("...");
					me.context.toastr.addAlertSuccess(Messages.get("label.field") + nameCreated + Messages.get("label.success.added"));
					me.tweakNewField();
				}
			}
		});

		DocumentStore.on("sectiondeleted", (model) => {			
			me.context.toastr.addAlertSuccess(Messages.get("label.section") + model.name + Messages.get("label.success.deleted"));
			me.context.tabPanel.removeTabByPath(me.props.location.pathname);
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_RETRIEVE,
				data: me.context.planMacro.id
			});
		});

		DocumentStore.on("attributedeleted", (model) => {
			me.context.toastr.addAlertSuccess(Messages.get("label.field") + nameCreated + Messages.get("label.success.deleted"));					
			me.getSectionAttributes(me.props.params.sectionId, me.props.params.planId);
		});

		

		StructureStore.on("attributetypes", (models) => {			
			var types = [];		

			models.map((type) => {				
				if(type.id == AttributeTypes.TEXT_AREA_FIELD ||
					type.id == AttributeTypes.SELECT_PLAN_FIELD ||
					type.id == AttributeTypes.TABLE_FIELD){
					types.push(type);
				}
			});
			me.setState({
				types: types
			});
		}, me);

		StructureStore.dispatch({
			action: StructureStore.ACTION_FIND_ATTRIBUTE_TYPES
		});

		TableStore.on("tablesavestructures", (model) => {			
			model.tableInstances = [];			
			me.state.fields.push({
					name: "attribute-"+me.state.fields.length,
					type: me.state.recentAttr.type,
					required: me.state.recentAttr.required,
					placeholder: "",
					label: me.state.recentAttr.name,
					value: me.state.recentAttr.value,
					schedule: me.state.recentAttr.schedule,
					extraSchedule: (me.state.recentAttr.schedule ? me.saveInstanceSchedule : undefined),
					tableFields: model,
					selectPlans: me.state.recentAttr.selectPlans,
					extraTableFields: (model ? me.saveInstanceTable : undefined),
			});			
			//Toastr.remove();
			//Toastr.success("Campo \""+me.state.recentAttr.name+"\" adicionado com sucesso.");
			me.context.toastr.addAlertSuccess(Messages.get("label.field") + me.state.recentAttr.name + Messages.get("label.success.added"));
			me.tweakNewField();
		});

		ScheduleStore.on("schedulestructurescreated", (model) => {			
			model.scheduleInstances = [];
			model.scheduleStructures = me.state.tableColumns;			
			me.state.fields.push({
					name: "attribute-"+me.state.fields.length,
					type: me.state.recentAttr.type,
					required: me.state.recentAttr.required,
					placeholder: "",
					label: me.state.recentAttr.name,
					value: me.state.recentAttr.value,
					schedule: model,
					extraSchedule: (me.state.recentAttr.schedule ? me.saveInstanceSchedule : undefined),
					tableFields: me.state.recentAttr.tableFields,
					selectPlans: me.state.recentAttr.selectPlans,
					extraTableFields: undefined
			});			
			//Toastr.remove();
			//Toastr.success("Campo \""+me.state.recentAttr.name+"\" adicionado com sucesso.");
			me.context.toastr.addAlertSuccess(Messages.get("label.field") + me.state.recentAttr.name + Messages.get("label.success.added"));
			me.tweakNewField();
		});
	},

	componentWillUnmount() {
		DocumentStore.off(null, null, this);
		ScheduleStore.off(null, null, this);
		StructureStore.off(null, null, this);
		TableStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {	
		var el = document.getElementsByClassName("fpdi-app-content")[0];		//pegando o elemento que contém os atributos
		el.scrollTop = 0;														//voltando seu scroll para o topo
		if (newProps.location.pathname != this.state.tabPath) {
			this.setState({
				tabPath: newProps.location.pathname,
				loading: true,
				vizualization: true,
				newField: false
			});			
			this.getSectionAttributes(newProps.params.sectionId, newProps.params.planId, this.props.params.planId);
			
		}
	},

	getSectionAttributes(sectionId, planId){		
		DocumentStore.dispatch({
			action: DocumentStore.ACTION_RETRIEVE_SECTIONATTRIBUTES,
			data: {
				sectionId: sectionId,
				planId: planId
			}
		});
	},

	saveInstanceSchedule(scheduleInstance){	
		ScheduleStore.dispatch({
			action: ScheduleStore.ACTION_CUSTOM_SAVE,
			data: {
				scheduleInstance: {
					id: scheduleInstance.id,
					number: scheduleInstance.number,
					description: scheduleInstance.description,
					periodicity: scheduleInstance.periodicity,
					scheduleValues: scheduleInstance.scheduleValues
				},
				scheduleId: scheduleInstance.scheduleId,
				beginDate: scheduleInstance.begin,
				endDate: scheduleInstance.end
			},
			opts: {
				wait: true
			}
		});
	},

	saveInstanceTable(tableInstance){		
		
		var numberTypes = [AttributeTypes.CURRENCY_FIELD, AttributeTypes.NUMBER_FIELD, AttributeTypes.PERCENTAGE_FIELD];

		tableInstance.tableValues.map((model, idx) => {
			if(numberTypes.includes(model.tableStructure.type)){
				tableInstance.tableValues[idx].value = model.value.replace(",",".");
			}
		});

		TableStore.dispatch({
			action: TableStore.ACTION_CUSTOM_SAVE,
			data: {
				tableInstance: {
					id: tableInstance.id,
					tableValues: tableInstance.tableValues
				},
				tableFieldsId: tableInstance.tableFieldsId
			},
			opts: {
				wait: true
			}
		});
	},

	onSubmit(data) {

		var me = this;
		var documentAttributes = [];
		var msg = Messages.get("label.msg.errorsForm");
		var boolMsg = false;
		
		var index = 0;

		var validation = Validate.validateSectionTitle(this.refs["fieldName"],this.refs['formAlertTitleSection']);
		

		for (var i=0; i<Object.keys(data).length; i++) {
			var tr = data[Object.keys(data)[i]];
			if(tr != null || tr != undefined) {
				if(tr.replace("<p>","").replace("</p>","").replace("<br>","").replace("&nbsp;","").trim() ==""){
					tr = null;
				}
				/*if(tr == null && this.state.model.documentAttributes[i].required == true){
					boolMsg = true;
					msg += this.state.fields[i].label+'<br/>';
				}*/
				if(!boolMsg){
					while (this.state.model.documentAttributes[index].type == AttributeTypes.SCHEDULE_FIELD
							|| this.state.model.documentAttributes[index].type == AttributeTypes.TABLE_FIELD) {
						index = index+1;
					}
					documentAttributes.push({
						id: this.state.model.documentAttributes[index].id,
						value: tr
					});
					index = index+1;
				}
			}
		}
		if(boolMsg){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
			return;
		}


		if (validation) {
			this.context.toastr.addAlertError(msg);
			return;
		}



		var documentSection = {
			id: this.state.model.id,
			name: this.refs["fieldName"].value,
			documentAttributes: documentAttributes
		};		
		DocumentStore.dispatch({
			action: DocumentStore.ACTION_SAVE_SECTIONATTRIBUTES,
			data: { 
				documentSection: documentSection,
				refs: me.refs
			}
		});
	},

	saveNewField(extra, periodicity, evt) {
		var validation = Validate.validationNewFieldDocument(this.refs);		
		if (validation.errorField) {
			this.context.toastr.addAlertError(Messages.get("label.error.form"));
		}
		else {
			this.setState({
				tableColumns: extra			
			});
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_CREATE_SECTION_ATTRIBUTE,
				data: {
					section: this.props.params.sectionId,
					name: validation.name.s,
					type: validation.type.s
				}
			});
		}
	},

	tweakNewField() {
		this.setState({
			newField: !this.state.newField,
			newFieldType: null
		});
	},

	deleteSection(){
		var msg = Messages.get("label.msg.deleteSection");
		Modal.confirmCustom(() => {
			Modal.hide();
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_DELETE_SECTION,
				data: {
					sectionId: this.props.params.sectionId
				}
			});
		},msg,()=>{Modal.hide()});
	},

	deleteAttribute(id){
		var msg = Messages.get("label.msg.deleteField");
		Modal.confirmCustom(() => {
			Modal.hide();
			this.setState({
				loading: true
			})
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_DELETE_ATTRIBUTE,
				data: {
					id: id
				}
			});
		},msg,()=>{Modal.hide()});
	},
	onSelectFieldType(){
		var me = this;

		me.setState({
				newFieldType: document.getElementById("selectFieldType").value
			});
		if(document.getElementById("selectFieldType").value == AttributeTypes.TABLE_FIELD  ||
			document.getElementById("selectFieldType").value == AttributeTypes.SCHEDULE_FIELD){
			me.setState({
				buttonsHide: true
			});
		}else{
			me.setState({
				buttonsHide: false
			});
		}
	},

	onCancel () {
		this.setState({
			vizualization: !this.state.vizualization
		})
	},

	editAttribute(name, idx){
		var field = this.state.model.documentAttributes[idx] || this.state.fields[idx];
		field.name = name;
		
		DocumentStore.dispatch({
			action: DocumentStore.ACTION_EDIT_ATTRIBUTE,
			data: {
				attr: field
			}
		});
	},

	editingAttributes () {
		this.setState({
			vizualization: !this.state.vizualization
		});
	},

	render() {	
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		//_.defer(() => {
		//	$("[data-toggle=tooltip]").tooltip();
		//});	
		return( 
		<div>
			<div>
				<h1>
					{this.state.subTitle}

					{((this.context.roles.MANAGER || _.contains(this.context.permissions, 
					    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get('archived')) ?
						(this.state.vizualization == true ? <i className="mdi mdi-pencil cursorPointer deleteIcon" onClick={this.editingAttributes} title={Messages.get("label.title.editInformation")}/> : "")
					: ""}
					{((this.context.roles.MANAGER || _.contains(this.context.permissions, 
					    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get('archived')) && this.state.model.leaf ?
						<i type="submit" className="mdi mdi-delete cursorPointer deleteIcon" onClick={this.deleteSection} 
						title="Excluir seção" ></i> 
					: ""}
				</h1>

				{!this.state.vizualization ? 
					<div className="panel panel-default panel-margins">
						<div className="panel-heading attribute-input-opts">
							<b className="budget-title">Título</b>
						</div>
						<div>
							<input
								className="form-control"
								maxLength='100'
								onKeyPress={this.onKeyUp}
								onPaste={this.onKeyUp}
								name='name'
								defaultValue={this.state.model.name}
								id='fieldName'
								ref='fieldName'
								placeholder={Messages.get("label.title.section")}
								onKeyPress={this.onKeyUp}
							/>
							<div ref="formAlertTitleSection" className="formAlertError"></div>
						
						</div>
					</div>
				: ""}
				
				<VerticalForm
					onSubmit={this.onSubmit}
					vizualization={this.state.vizualization}
					fields={this.state.fields}
					store={DocumentStore}
					alterable={true}
					undeletable={true}
					onCancel={this.onCancel}
					deleteFunc={this.deleteAttribute}
					editFunc={this.editAttribute}
					submitLabel={Messages.get("label.submitLabel")}
					isDocument={true}/>
				{this.state.newField ?
					<div className="form-group form-group-sm marginTop20">
						<div className="row">
							<div className="col-sm-6 col-md-4">
								<input
									type="text"
									spellCheck={false} 
									className="form-control"
									ref="newfield-name"
									placeholder={Messages.get("label.field.name")} 
									maxLength="255"
									/>
							</div>
							<div className="col-sm-6 col-md-4">
								<select
									type="text"
									spellCheck={false} 
									className="form-control"
									ref="newfield-type"
									placeholder={Messages.get("label.field.type")}
									onChange={this.onSelectFieldType}
									id="selectFieldType">
										<option value="">{Messages.get("label.selectTypeField")}</option>
										{this.state.types.map((type, idx) => {
											return <option value={type.id} key={"attr-type-"+idx}>{type.label}</option>
										})}
								</select>
							</div>
							{this.state.buttonsHide?"":
								<div className="col-sm-12 col-md-4" >
									<span className="mdi mdi-check btn btn-sm btn-success" onClick={this.saveNewField} title={Messages.get("label.submitLabel")}/>
					            	<span>&nbsp;</span>
					            	<span className="mdi mdi-close btn btn-sm btn-danger" onClick={this.tweakNewField} title={Messages.get("label.cancel")}/>
								</div>
							}
						</div>
						<div className="row">
							<div className="col-sm-6 col-md-4">
								<div className="formAlertError" ref="formAlertErrorName"></div>
							</div>
							<div className="col-sm-6 col-md-4">
								<div className="formAlertError" ref="formAlertErrorType"></div>
							</div>
						</div>
						<div>
							{this.state.newFieldType == AttributeTypes.TABLE_FIELD ?
								<TableFieldCreator cancelFunc={this.tweakNewField} confirmFunc={this.saveNewField} deleteFunc={this.deleteAttribute}/>
							: <div></div>}
							{this.state.newFieldType == AttributeTypes.SCHEDULE_FIELD ?
								<ScheduleFieldCreator types={this.state.types} cancelFunc={this.tweakNewField} 
								confirmFunc={this.saveNewField}/>
							: <div></div>}
						</div>
					</div>
					:
					(((this.context.roles.MANAGER || _.contains(this.context.permissions, 
					    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get('archived') && !this.state.model.preTextSection) ?
						<button onClick={this.tweakNewField} id="addIconDocument" className="btn btn-sm btn-neutral marginTop20">
							<span className="mdi mdi-plus" /> {Messages.get("label.addNewField")}
						</button>
					:"")
				}
			</div>			
	    </div>);
	}		
});
