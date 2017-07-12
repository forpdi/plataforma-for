import React from "react";
import _ from 'underscore';
import AttributeForm from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
//import Toastr from 'toastr';

var VerticalForm = AttributeForm.VerticalForm;
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';

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

	cleanTypes(){
		var types = [];
		this.props.types.map((type) => {			
			if(type.id != AttributeTypes.BUDGET_FIELD &&
				type.id != AttributeTypes.ACTION_PLAN_FIELD &&
				type.id != AttributeTypes.SCHEDULE_FIELD &&
				type.id != AttributeTypes.TABLE_FIELD &&
				type.id != AttributeTypes.TOTAL_FIELD &&				
				type.id !=  AttributeTypes.SELECT_PLAN_FIELD){
					types.push(type);
			}
		});
		this.setState({
			types: types
		});
	},

	componentDidMount() {
		var me = this;
		this.cleanTypes();
		this.state.columns.push({
			label: "#",
			type: AttributeTypes.NUMBER_FIELD,
			typeLabel: "Número"
		});
		this.state.columns.push({
			label: Messages.get("label.activity"),
			type: AttributeTypes.TEXT_FIELD,
			typeLabel: "Campo de Texto"
		});
		this.state.columns.push({
			label: Messages.get("label.begin"),
			type: AttributeTypes.DATE,
			typeLabel: "Data"
		});
		this.state.columns.push({
			label: Messages.get("label.end"),
			type: AttributeTypes.DATE,
			typeLabel: "Data"
		});
	},

	insertColumn(){		
		var name = this.refs["new-column-name"].value;
		var type = this.refs["new-column-type"].value;
		
		if(name == undefined || name == ""  || type == undefined || type == ""){
			//Toastr.remove();
			//Toastr.error("Preencha o nome e o tipo da nova coluna.");
			this.context.toastr.addAlertError(Messages.get("label.error.nameTypeColumnEmpty"));			
			return;
		}

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
	},

	cancel(){
		this.props.cancelFunc();
	},

	save(){
		this.state.columns.splice(0,4);
		this.props.confirmFunc(this.state.columns,this.refs["enable-periodicity"].checked);
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
					<strong>{Messages.get("label.scheduleSetting")}</strong>
				</div>
				<div className="panel-body">
					<div className="col-sm-4 col-md-3">
						<label htmlFor="new-column-name">{Messages.get("label.periodicity")}</label>
					    <input
							type="checkbox"
							className="periodicity-checkbox"							
							ref="enable-periodicity"
							defaultValue={false}/>
					</div>				
					<div className="col-sm-4 col-md-3">
						<label htmlFor="new-column-name">{Messages.get("label.columnName")}</label>
					    <input
							type="text"
							spellCheck={false} 
							className="form-control"
							ref="new-column-name"
							placeholder={Messages.get("label.field.name")}
							id="new-column-name" />
					</div>
					<div className="col-sm-4 col-md-3">
						<label htmlFor="select-new-column-type">{Messages.get("label.type")}</label>
					    <select
							type="text"
							spellCheck={false} 
							className="form-control"
							ref="new-column-type"
							placeholder={Messages.get("label.typeLegend")}
							id="select-new-column-type">
							<option value="" data-placement="right" title={Messages.get("label.title.selectTypeField")}>{Messages.get("label.selectTypeField")}</option>
							{this.state.types.map((type, idx) => {
								return <option value={type.id} key={"attr-type-"+idx} data-placement="right" title={type.label}>
									{type.label}</option>
							})}
						</select>
					</div>
					<div className="col-sm-4 col-md-3">
						<label htmlFor="add-column">&nbsp;</label>
						<button className="btn btn-sm btn-info form-control" id="add-column" onClick={this.insertColumn}>
							{Messages.get("label.insertColumn")}
						</button>
					</div>
					<br/>
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
					  								<span className="mdi mdi-check btn btn-sm btn-success padding2" onClick={this.confirmEdit.bind(this,idx)} title={Messages.get("label.submitLabel")}/>
									            	<span>&nbsp;</span>
									            	<span className="mdi mdi-close btn btn-sm btn-danger padding2" onClick={this.cancelEdit} title={Messages.get("label.cancel")}/>
					  							</th>
					  							: 
					  							<th className="textAlignCenter" key={"col-"+idx}>
								  					{column.label}
								  					<span>&nbsp;</span>
								  					{idx > 3 ? 
									  					<div>
							  								<span className="mdi mdi-pencil cursorPointer" onClick={this.editColumn.bind(this,idx)} title={Messages.get("label.title.editInformation")}></span>
							  								<span className="mdi mdi-delete cursorPointer" onClick={this.deleteColumn.bind(this,idx)} title={Messages.get("label.delete")}></span>
							  							</div>
							  						:
							  							<div/>
							  						}
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
						<span className="mdi mdi-check btn btn-sm btn-success" onClick={this.save} title={Messages.get("label.submitLabel")}/>
		            	<span>&nbsp;</span>
		            	<span className="mdi mdi-close btn btn-sm btn-danger" onClick={this.cancel} title={Messages.get("label.cancel")}/>
					</div>											
				</div>
			</div>
		)
	}

});