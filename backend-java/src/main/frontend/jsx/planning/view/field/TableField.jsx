import React from "react";
import {Link} from 'react-router';

import _ from "underscore";
import UserStore from "forpdi/jsx/core/store/User.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import TableStore from "forpdi/jsx/planning/store/TableFields.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import AttributeInput from "forpdi/jsx/planning/widget/attributeForm/AttributeInput.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

var numeral = require('numeral');
var NewFieldValidation = Validation.validate;

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
        accessLevels: React.PropTypes.object.isRequired,
        permissions: React.PropTypes.array.isRequired,
        roles: React.PropTypes.object.isRequired,
        planMacro: React.PropTypes.object.isRequired
	},
	getDefaultProps() {
		return {
			fieldDef: {
				type: "text",
				name: "input-"+Date.now(),
				label: '',
				placeholder: '',
				value: null,
				onChange: null,
				helpBox: null,
				undeletable: false,
				alterable: false,
				editing: false,
			}
		};
	},

	getInitialState() {
		return {			
			tableFields: this.props.data,
			title: this.props.title,
			loading: false,
			totalField: null,
			totalCountFields: [],
			users: []
		};
	},

	newTable(evt){
    	this.setState({
    		adding: true,
    		hide: false
    	});    	
	},

	onKeyUp(evt){		
		var key = evt.which;
		if(key == 13) {
			evt.preventDefault();
			return;
		}
	},

	componentDidMount()	{
		this.setState({
    		adding: false,
    	});
    	TableStore.on('tableSaved', model => {
    		if (!model.responseText) {
	    		var tableInstance = model.data;
	    		if (this.state.tableFields.id == tableInstance.tableFieldsId){
					this.state.tableFields.tableInstances.push(tableInstance);
					this.setState({
						adding: false
					});
	    		}				
				
				this.context.toastr.addAlertSuccess("Informações salvas com sucesso");
			}
		}, this);
		TableStore.on("tableUpdated", model => {
			if (!model.responseText) {
				this.state.tableFields.tableInstances[this.state.idx] = model.data;
				this.setState({
					loading: false
				});

				this.context.toastr.addAlertSuccess("Edição realizada com sucesso");
			}
		},this);
		TableStore.on("tableDeleted", model => {			
			if (!model.responseText) {
				var tableInstance = model.data;
				if (this.state.tableFields.id == tableInstance.tableFieldsId)
	    			this.state.tableFields.tableInstances.splice(this.state.idx,1);
				this.setState({
					loading: false
				});
				
				this.context.toastr.addAlertSuccess("Exclusão realizada com sucesso");
			}
		},this);

		UserStore.on("retrieve-user", (model) => {
			this.setState({
				loading: false,
				users: model.data,
				totalUsers:model.total
			});
		},this)

		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 50
			}
		});
	},

	cancelNewTable(){
		this.setState({
    		adding: false
    	});
	},

	componentWillUnmount() {
		TableStore.off(null, null, this);
		UserStore.off(null, null, this);
	},

	acceptNewTable(){
		var tableValues = [];		
		if (this.state.tableFields.tableStructures.length > 0) {
			this.state.tableFields.tableStructures.map((model, idx) => {				
				tableValues.push({
					value: this.refs['tabValue' + idx].value || this.refs['tabValue'+idx].getValue() || this.refs['tabValue'+idx].props.fieldDef.value,
					tableStructure: {
						id: this.state.tableFields.tableStructures[idx].id,
						optionsField: model.optionsField && model.optionsField.length>0 ? 
									  model.optionsField : undefined, //insere as opções do selectBox no campo a ser salvo. Se remover isto o selectbox vira input
						type: model.type
					}
				});
			});
		}
		
		if(NewFieldValidation.tableNewInstanceValidate(tableValues)) { 
			var tableInstance = {
				tableValues: tableValues,
				tableFieldsId: this.state.tableFields.id
			};			
			this.props.newFunc(tableInstance);
		} else {
			this.state.tableFields.tableStructures.map((model, idx) => { 
				if(document.getElementById("field-" + model.label).value == ""){
					document.getElementById("field-" + model.label).className = "form-control borderError";
				} else{
					document.getElementById("field-" + model.label).className = "form-control";
				}
			});

			this.context.toastr.addAlertError("Por favor preencha todos os campos corretamente.");
		}
	},

	deleteTable(id, idx,evt){
		var msg = "Você tem certeza que deseja excluir ?";
		
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			this.setState({
				loading: true,
				idx: idx //index a ser deletado
			});
			TableStore.dispatch({
				action: TableStore.ACTION_DELETE,
				data: {
					id: id
				}
			});
		},msg,()=>{Modal.hide()});
	},

	acceptedEditTable(id,idx){
		var tableValues = [];
		var empty = false;
		this.state.tableFields.tableStructures.map((model, idx) => {			
			if(typeof this.refs['edit-tabValue'+idx].getValue == 'function')
				this.state.tableFields.tableInstances[this.state.editingIdx].tableValues[idx].value = this.refs['edit-tabValue'+idx].getValue();
			else 
			 	this.state.tableFields.tableInstances[this.state.editingIdx].tableValues[idx].value = this.refs['edit-tabValue'+idx].value;
			tableValues.push({
				value:  this.state.tableFields.tableInstances[this.state.editingIdx].tableValues[idx].value,
				tableStructure: {
					id: this.state.tableFields.tableStructures[idx].id,
					optionsField: model.optionsField && model.optionsField.length>0 ? 
								  model.optionsField : undefined, //insere as opções do selectBox no campo a ser salvo. Se remover isto o selectbox vira input
					type: model.type
				}
			});
		});
		if(!NewFieldValidation.tableNewInstanceValidate(tableValues)){
			this.context.toastr.addAlertError("Por favor preencha todos os campos corretamente.");
			return;
		}
		var tableInstance = {
			id: this.state.tableFields.tableInstances[this.state.editingIdx].id,
			tableValues: tableValues,
			tableFieldsId: this.state.tableFields.id
		};
		
		this.props.newFunc(tableInstance);
		this.rejectedEditTable();
	},

	hideFields() {
		this.setState({
			hide: !this.state.hide
		})
	},

	rejectedEditTable(oldTableFields,idx, id){
		this.setState({
			editingIdx: undefined
		});
	},

	editTable(id, idx, evt){
		this.setState({
			editingIdx: idx
		});
	},

	onChangeDateField(ref, data){		
		this.refs[ref].props.fieldDef.value = data.format('DD/MM/YYYY');
		this.refs[ref].forceUpdate();
	},

	renderEditing(){
		this.state.totalCountFields = [];		
		return(			
			<tr key='new-table'>
				{this.state.tableFields.tableStructures.length >0 ?
					this.state.tableFields.tableStructures.map((model, idx) => {
						if(model.isInTotal){
							this.state.totalCountFields.push(idx);
						}
						if(model.type == AttributeTypes.TOTAL_FIELD){
							this.state.editTotalField = idx;
						}
						var field = {
							id: model.id,
							label: "",
							name: model.label,
							placeholder: "",
							required: false,
							type: model.type,
							users: this.state.users,
							onChange: (model.type == AttributeTypes.DATE_FIELD ? 
											this.onChangeDateField.bind(this,"edit-tabValue"+idx) :
											model.isInTotal ? this.onEditTotalChange:
											 _.noop),
							value: this.state.tableFields.tableInstances[this.state.editingIdx].tableValues[idx].value,
							editModeValue: this.state.tableFields.tableInstances[this.state.editingIdx].tableValues[idx].value.replace(".",",")
						};
						
						return(
							<td key={"value-"+idx}>
								{model.optionsField && model.optionsField.length>0 ? (
									<select
										className="budget-field-table"
										name={"edit-tabValue"+idx}
										id={"edit-tabValue"+idx}
										ref={"edit-tabValue"+idx}
										defaultValue={this.state.tableFields.tableInstances[this.state.editingIdx].tableValues[idx].value}>
											<option key={'field-opt-'}/>
											{model.optionsField.map((opt,id) => {
												return (
													<option key={'field-opt-'+idx+'-'+id} value={opt.label} data-placement="right"
													 title={opt.label}>
														{opt.label}
													</option>
												);
											})}
									</select>
								) : (
								<AttributeInput
									id={"edit-tabValue"+idx}
									index={idx}
									fieldDef={field}
									ref={"edit-tabValue"+idx}
									key={field.name}
									undeletable={false}
									deleteFunc={_.noop}
									editFunc={_.noop}
									alterable={false}
									vizualization={false}
									isDocument={true}
									onClick={_.noop}/>
								)}
							</td>
						);
					})
				: ""}
				<td className="edit-budget-col cursorDefault">				
                    <div className='tableFieldIcons'>
                       		<span className='mdi mdi-check accepted-budget' onClick={this.acceptedEditTable} title="Salvar"></span>
                      		<span className='mdi mdi-close reject-budget' onClick={this.rejectedEditTable} title="Cancelar"></span>
                   	</div>
	            </td>
			</tr>
		);
	},

	onTotalChange(){
		var total = 0;
		var value = 0;
		this.state.totalCountFields.map((model,idx)=>{
			value = this.refs['tabValue'+model].getValue().replace(",",".");
			if(!isNaN(value)){								
				total += Number(value);
			}
		});		
		this.refs['tabValue'+this.state.totalField].props.fieldDef.value = total;
		this.refs['tabValue'+this.state.totalField].forceUpdate();
	},
	onEditTotalChange(id,i){
		var total = 0;
		var value = 0;
		this.state.totalCountFields.map((model,idx)=>{
			value = this.refs['edit-tabValue'+model].getValue().replace(",",".");
			if(!isNaN(value)){
				total += Number(value);
			}
		});
		this.refs['edit-tabValue'+this.state.editTotalField].props.fieldDef.value = total;	
		this.refs['edit-tabValue'+this.state.editTotalField].forceUpdate();
	},
	onlyNumber(evt){
 		var key = evt.which;
 		if(key == 13|| key != 46 && (key < 48 || key > 57)) {			
 			evt.preventDefault();
 			return;
 		}
 	},
	onlyNumberPaste(evt){
		var value = evt.clipboardData.getData('Text');
		if (!(!isNaN(parseFloat(value)) && isFinite(value))) {
			evt.preventDefault();
			return;
		}
	},
	renderNewTable(){	
		this.state.totalCountFields = [];
		return(			
			<tr key='new-table'>
				{this.state.tableFields.tableStructures.length >0 ?
					this.state.tableFields.tableStructures.map((model, idx) => {
						if(model.isInTotal){
							this.state.totalCountFields.push(idx);
						}
						if(model.type == AttributeTypes.TOTAL_FIELD){
							this.state.totalField = idx;
						}						
						var field = {
							id: model.id,
							label: "",
							name: model.label,
							placeholder: "",
							required: false,
							type: model.type,
							users: this.state.users,
							onChange: (model.type == AttributeTypes.DATE_FIELD ? 
											this.onChangeDateField.bind(this,"tabValue"+idx) :
											model.isInTotal ? this.onTotalChange :
											 _.noop)
						};
						return(
							<td key={"value-"+idx}>
								{model.optionsField && model.optionsField.length>0 ? (
									<select
										className="budget-field-table"
										name={"tabValue"+idx}
										id={"tabValue"+idx}
										ref={"tabValue"+idx}
										>
											<option key={'field-opt-'}></option>
											{model.optionsField.map((opt,id) => {
												return (<option key={'field-opt-'+idx+'-'+id} defaultValue={opt.label} 
													data-placement="right" title={opt.label}>
														{opt.label}</option>);
											})}
									</select>
								) : (
									<AttributeInput
										id={"tabValue"+idx}
										index={idx}
										fieldDef={field}
										ref={"tabValue"+idx}
										key={field.name}
										undeletable={false}
										deleteFunc={_.noop}
										editFunc={_.noop}
										alterable={false}
										vizualization={false}
										isDocument={true}
										onClick={_.noop}/>								
								)}
							</td>
						);
					})
				: ""}
				<td  className="edit-budget-col cursorDefault">
                    <div className='tableFieldIcons'>
                       		<span className='mdi mdi-check accepted-budget' onClick={this.acceptNewTable} title="Salvar"></span>
                      		<span className='mdi mdi-close reject-budget' onClick={this.cancelNewTable} title="Cancelar"></span>
                   	</div>
	            </td>
			</tr>
		);
	},

	delete(){
		this.props.deleteFunc(this.props.data.attributeId);
	},

	edit(){
		this.setState({
			editing: true
		});
	},

	cancelEditing(){
		this.setState({
			editing: false
		});
	},

	confirmEdit(){
		if(this.refs['edit-input'].value.trim() != ""){
			this.props.editFunc(this.refs['edit-input'].value, this.props.index);
			this.state.title = this.refs['edit-input'].value;
			this.cancelEditing();
		}else{
			this.context.toastr.addAlertError("Preencha o nome da tabela");
		}
	},

	renderEditTitle(){
		return(
			<div className="panel-heading attribute-input-opts">
			<div className="edit-section-attribute"> 
				<input defaultValue={this.props.fieldDef.label == "" ? this.state.title : this.props.fieldDef.label} className="edit-section-attribute-input" ref="edit-input"/>
				<div className=' displayFlex'>
                   	<span className='mdi mdi-check accepted-budget' onClick={this.confirmEdit} title="Salvar"></span>
                  	<span className='mdi mdi-close reject-budget' onClick={this.cancelEditing} title="Cancelar"></span>
               	</div>
			</div>
			</div>
		);
	},
	
	render(){
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return(
			<div className="panel panel-default panel-margins">
				{!!this.state.editing ? this.renderEditTitle() 
				:
				<div className="panel-heading displayFlex attribute-input-opts">
					<b className="budget-title" ref="titleSchedule">{this.state.title}</b>
					{!!this.props.isDocument ? (((this.context.roles.MANAGER || _.contains(this.context.permissions, 
													PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived")) ?
						<span>
							<span type="submit" className="mdi mdi-delete attribute-input-edit atribute-input-edit-schedule inner"
					 		title="Excluir campo" onClick={this.delete}/>

							<span className="mdi mdi-pencil attribute-input-edit atribute-input-edit-schedule inner" 
							title="Alterar campo" onClick={this.edit}/>

						</span> : "") : ""}
					{(this.state.adding)?
						"":
					<div className="budget-btns">
						{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
							PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived") ?
							<button type="button" className="btn btn-primary budget-new-btn" onClick={this.newTable}>Novo</button>
						:""}
						<span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
					</div>}
				</div>
				}
				{!this.state.hide  ? 
				<table className="budget-field-table table">	
					<thead>
						<tr>
							{this.state.tableFields.tableStructures.length >0 ?
								this.state.tableFields.tableStructures.map((model, idx) => {
									return(
										<th key={"structure-"+idx}>{model.label}</th>
									);
								})
							: ""}
							{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
								PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived") ?
								<th></th>
							: ""}
						</tr>
					</thead>
					<tbody>
						{this.state.adding ? this.renderNewTable() : undefined}
						{!this.state.hide ? this.state.tableFields.tableInstances.map((model, idx) => {
							if(idx == this.state.editingIdx){
								return(this.renderEditing());
							} else {								
								return(
									<tr key={"tableField-"+idx}>
										{model.tableValues.length >0 ?
											model.tableValues.map((mod, id) => {
												if(mod.tableStructure.type == AttributeTypes.RESPONSIBLE_FIELD){
													var el;
													this.state.users.map((user) => {
														if(user.id == mod.value){
															el = (
																<td key={"tableValue-"+id} id={'tableValue'+model.id+'-'+id} ref={'tableValue'+model.id+'-'+id}>
																	{user.name}
																</td>
															);
														}
													});
													return el;
												} else {
													return(
														<td key={"tableValue-"+id} id={'tableValue'+model.id+'-'+id} ref={'tableValue'+model.id+'-'+id}>
															{mod.tableStructure.type == AttributeTypes.CURRENCY_FIELD ? 
																"R$ "+numeral(mod.value).format('0,0.00') : 
																(mod.tableStructure.type == AttributeTypes.PERCENTAGE_FIELD ? 
																	numeral(mod.value).format('0,0.00') + "%" : 
																	(mod.tableStructure.type == AttributeTypes.NUMBER_FIELD ? mod.value.replace(".",",") :
																	 mod.value)
																)
															}
															
														</td>
													);
												}
											})
										: ""}

										{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
											PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived") ?
											<td id={'optionsTable'+model.id} ref={'optionsTable'+model.id} className=" tableFieldIcons edit-budget-col cursorDefault floatRight">
												<span className='mdi mdi-pencil cursorPointer' id={'edit-table-button-'+model.id} ref={'edit-table-button-'+model.id} onClick={this.editTable.bind(this,model.id,idx)} />
												<span className='mdi mdi-delete cursorPointer' id={'delete-table-button-'+model.id} ref={'delete-table-button-'+model.id} onClick={this.deleteTable.bind(this,model.id,idx)} />
											</td>
										:""}
									</tr>
								);
							}
						}) : ""} 
					</tbody>					
				</table>
				: ""}
			</div>
		);
	}

});
