
import S from 'string';
import React from "react";
import _ from 'underscore';
import {Link} from 'react-router';

import StructureStore from 'forpdi/jsx/planning/store/Structure.jsx';
import TableStore from "forpdi/jsx/planning/store/TableFields.jsx";

import AttributeForm from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

var NewFieldValidation = Validation.validate;
var VerticalForm = AttributeForm.VerticalForm;

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	
	getInitialState() {
		return{
			types: [],
			hide: false,
			columns: []
		}	
	},

	cleanTypes(types){
		var	allowed = [];
		types.map((type) => {			
			if(type.id != AttributeTypes.BUDGET_FIELD &&
				type.id != AttributeTypes.ACTION_PLAN_FIELD &&
				type.id != AttributeTypes.SCHEDULE_FIELD &&
				type.id != AttributeTypes.TABLE_FIELD &&
				type.id != AttributeTypes.TOTAL_FIELD &&				
				type.id != AttributeTypes.SELECT_PLAN_FIELD &&
				type.id != AttributeTypes.TEXT_AREA_FIELD &&
				type.id != AttributeTypes.DATE_TIME_FIELD &&
				type.id != AttributeTypes.SELECT_FIELD &&
				type.id != AttributeTypes.ATTACHMENT_FIELD){
					allowed.push(type);
			}
		});
		
		return allowed;
	},

	componentWillReceiveProps(newProps){
		
	},

	componentWillUnmount() {
		TableStore.off(null, null, this);		
		StructureStore.off(null, null, this);
	},

	componentDidMount() {
		var me = this;
		StructureStore.on("attributetypes", (models) => {			
			var types = [];
			me.setState({
				types: me.cleanTypes(models)
			});
		}, me);
		StructureStore.dispatch({
			action: StructureStore.ACTION_FIND_ATTRIBUTE_TYPES
		});
	},

	insertColumn(){
		if(!NewFieldValidation.tableNewFieldValidate(this.refs)){
			//Verifica a selectbox
			if(this.refs["new-column-type"].value == ""){
				this.refs["new-column-type"].className = "form-control borderError";
			}else{
				this.refs["new-column-type"].className = "form-control";
			}
			//Verifica o campo nome
			if(this.refs["new-column-name"].value == ""){
				this.refs["new-column-name"].className = "form-control borderError";
			}else{
				this.refs["new-column-name"].className = "form-control";
			}
			//Retorna o erro
			this.context.toastr.addAlertError("Preencha o nome e o tipo da nova coluna.");
			return;
		}

		var type = this.refs["new-column-type"].value.trim();
		var name = this.refs["new-column-name"].value.trim();
		var label;
		this.state.types.map((item) =>{
			if(item.id == type){
				label = item.label;
			}
		});

		var columns = this.state.columns;
		columns.push({
			label: name,
			type: type,
			typeLabel: label
		});

		this.setState({
			columns: columns
		});

		this.refs["new-column-type"].value = "";
		this.refs["new-column-type"].className = "form-control";
		this.refs["new-column-name"].value = "";
		this.refs["new-column-name"].className = "form-control";
	},

	cancel(){
		this.props.cancelFunc();
	},

	save(){		
		if(this.state.columns == undefined || this.state.columns.length == 0){
			this.context.toastr.addAlertError("Você deve inserir ao menos uma coluna!");
			return;
		}
		this.props.confirmFunc(this.state.columns);
	},

	deleteColumn(idx){
		var columns = this.state.columns;
		columns.splice(idx,1);
		this.setState({
			columns: columns
		});
	},

	editColumn(idx){
		this.setState({
			editingColumn: idx
		});
	},

	cancelEdit(){
		this.setState({
			editingColumn: null
		});
	},

	confirmEdit(idx){
		var label = this.refs["edit-field-"+idx].value;
		var columns = this.state.columns;
		columns[idx].label = label;
		this.setState({
			columns: columns,
			editingColumn: null
		});
	},

	render(){
		return(
			<div className="panel panel-default panel-margins">
				<div className="panel-heading displayFlex">
					<strong>Configuração da tabela</strong>
				</div>
				<div className="panel-body">
					<div className="row">					
						<div className="col-sm-6 col-md-4">
							<label htmlFor="new-column-name">Nome da coluna:</label>
						    <input
								type="text"
								spellCheck={false} 
								className="form-control"
								ref="new-column-name"
								placeholder="Nome do campo"
								id="new-column-name" />
						</div>
						<div className="col-sm-6 col-md-4">
							<label htmlFor="select-new-column-type">Tipo:</label>
						    <select
								type="text"
								spellCheck={false} 
								className="form-control"
								ref="new-column-type"
								placeholder="Tipo"
								id="select-new-column-type">
								<option value="" data-placement="right" title="Selecione o tipo do campo">
									-- Selecione o tipo do campo --
								</option>
								{this.state.types.map((type, idx) => {
									return <option value={type.id} key={"attr-type-"+idx} data-placement="right" title={type.label}>
										{type.label}</option>
								})}
							</select>
						</div>
						<div className="col-sm-6 col-md-4">
							<label htmlFor="add-column">&nbsp;</label>
							<button className="btn btn-sm btn-info form-control" id="add-column" onClick={this.insertColumn}>
								Inserir coluna
							</button>
						</div>
					</div>					
				  	<div className="panel panel-default panel-margins">					  	
					  	<table className="budget-field-table table">
					  		<thead>
					  			<tr>
					  				{this.state.columns.map((column,idx) => {					  					
					  					return(
					  						(idx == this.state.editingColumn ? 
					  							<th className="textAlignCenter" key={"col-"+idx}>
					  								<input defaultValue={column.label} ref={"edit-field-"+idx}/>
					  								<span>&nbsp;</span>
					  								<span className="mdi mdi-check btn btn-sm btn-success padding2" 
					  								onClick={this.confirmEdit.bind(this,idx)} title="Salvar"/>
									            	<span>&nbsp;</span>
									            	<span className="mdi mdi-close btn btn-sm btn-danger padding2" 
									            	onClick={this.cancelEdit} title="Cancelar"/>
					  							</th>
					  							: 
					  							<th className="textAlignCenter" key={"col-"+idx}>
								  					{column.label}
								  					<span>&nbsp;</span>
						  							<span className="mdi mdi-pencil cursorPointer" onClick={this.editColumn.bind(this,idx)}
						  							 title="Editar informações"></span>
						  							<span className="mdi mdi-delete cursorPointer" onClick={this.deleteColumn.bind(this,idx)}
						  							 title="Excluir"></span>
						  						</th>
						  					)
					  					)
					  				})}					  				
					  			</tr>
					  		</thead>
					  		<tbody>
					  			<tr>
					  				{this.state.columns.map((column,idx) => {
					  					return(
					  						<td className="textAlignCenter" key={"col-"+idx}>{column.typeLabel}</td>
					  					)
					  				})}						  				
					  			</tr>
					  		</tbody>
					  	</table>					 	
					</div>	 						  
					<div className="col-sm-12 col-md-4" >
						<span className="mdi mdi-check btn btn-sm btn-success" onClick={this.save} title="Salvar"/>
		            	<span>&nbsp;</span>
		            	<span className="mdi mdi-close btn btn-sm btn-danger" onClick={this.cancel} title="Cancelar"/>
					</div>											
				</div>
			</div>
		)
	}

});