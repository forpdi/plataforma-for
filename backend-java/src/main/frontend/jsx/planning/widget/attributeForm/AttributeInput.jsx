import _ from "underscore";
import React from "react";
import MaskedInput from 'react-maskedinput';
import RichText from 'forpdi/jsx/vendor/FPDIRichText.jsx';
import {Link} from 'react-router';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
import string from 'string';
import $ from 'jquery';
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import IndicatorType from "forpdi/jsx/planning/view/field/IndicatorTypeField.jsx";
import SelectPlan from "forpdi/jsx/planning/view/field/SelectPlanField.jsx";
import Responsible from "forpdi/jsx/planning/view/field/ResponsibleField.jsx";
import StrategicObjective from "forpdi/jsx/planning/view/field/StrategicObjectiveField.jsx";
import TextArea from "forpdi/jsx/planning/view/field/TextAreaField.jsx";
import SelectField from "forpdi/jsx/planning/view/field/SelectField.jsx";
import SelectStructure from "forpdi/jsx/planning/view/field/SelectStructureField.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
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
				editing: false
			}
		};
	},
	getInitialState() {
		return {
			fieldId: "field-"+this.props.fieldDef.name.replace(/\./g, "-"),
			strategicObjectivesPlansParam: -1
		};
	},
	getValue() {
		var el = this.refs[this.state.fieldId];

		if (el == undefined)
			return el;
		if (el.type == AttributeTypes.DATE_FIELD || 
				el.type ==  AttributeTypes.DATE_TIME_FIELD)
			return el.valueAsDate;
		if (el.type == AttributeTypes.CURRENCY_FIELD || 
				el.type == AttributeTypes.NUMBER_FIELD ||
				el.type == AttributeTypes.PERCENTAGE_FIELD ||
				el.type == AttributeTypes.TOTAL_FIELD)
			return el.valueAsNumber;
		if (this.props.fieldDef.type == AttributeTypes.SELECT_PLAN_FIELD) {
			if (this.props.fieldDef.selectPlans.length > 0) {
				for (var i=0; i<this.props.fieldDef.selectPlans.length; i++) {
					if(this.props.fieldDef.selectPlans[i].name == el.value) {
						return this.props.fieldDef.selectPlans[i].id.toString();
					}
				}
			} else {
				return null;
			}
		}
		if (el.mask) {
			return el.mask.getRawValue().trim();
		}
		return el.value;
	},
	getInputNode() {
		return this.refs[this.state.fieldId];
	},
	onKeyUp(evt){
		this.maxLengthMask();
		var key = evt.which;
		if(key == 13) {			
			evt.preventDefault();
			return;
		}
		if(this.refs[this.state.fieldId].value.length+1 > this.refs[this.state.fieldId].maxLength) {
			this.refs[this.state.fieldId].value = this.refs[this.state.fieldId].value.substr(0, this.refs[this.state.fieldId].maxLength-1);
		}
	},
	maxLengthMask(){
		if(this.refs[this.state.fieldId].value.length >= this.refs[this.state.fieldId].maxLength){
			this.context.toastr.addAlertError("Limite de "+this.refs[this.state.fieldId].maxLength+" caracteres atingido!");
		}
	},
	onlyNumber(evt){
 		var key = evt.which;
 		if(key == 13|| key != 44 && (key < 48 || key > 57)) {
 			evt.preventDefault();
 			return;
 		}
 	},
	
	onlyNumberPaste(evt){
		var value = evt.clipboardData.getData('Text');
		if (!(!isNaN(parseFloat(value)) && isFinite(value)) || parseFloat(value) < 0) {
			evt.preventDefault();
			return;
		}
	},

	componentDidMount() {		
		if (this.props.fieldDef.type == AttributeTypes.DATE_FIELD || 
				this.props.fieldDef.type == AttributeTypes.DATE_TIME_FIELD) {
			$(this.getInputNode()).daterangepicker({
				autoApply: true,
				autoUpdateInput: true,
				locale: {
		            format: 'DD/MM/YYYY'
		        },
		        opens: 'right',
		        drops: 'down',
		        showDropdowns: true,
		        singleDatePicker: true
			});
		}

		PlanMacroStore.on("getmainmenustate", (data) => {	
            this.setState({
                menuHidden: data
            });
        }, this);	
	},

	onStrategicObjectivesSelectPlanChange(){
      this.setState({
        strategicObjectivesPlansParam: this.refs.strategicObjectivesSelectPlan.value
      })
      
    },

	delete(){
		this.props.deleteFunc(this.props.id);
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
			this.cancelEditing();
		}else{
			this.context.toastr.addAlertError("Por favor, preencha o campo");
		}
	},

	renderEditing(){
		return(
			<div className="edit-section-attribute"> 
				<input defaultValue={this.props.fieldDef.label} className="edit-section-attribute-input" maxLength="255" ref="edit-input"/>
				<div className='displayFlex'>
                   	<span className='mdi mdi-check accepted-budget' onClick={this.confirmEdit} title="Salvar"></span>
                  	<span className='mdi mdi-close reject-budget' onClick={this.cancelEditing} title="Cancelar"></span>
               	</div>
			</div>
		);
	},

	renderLabel(param){
		return(
			<div>
				<b className="budget-title">{this.props.fieldDef.label}</b>
				{param}
				{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         				PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived")?
					(!!this.props.undeletable ? <span type="submit" className="mdi mdi-delete attribute-input-edit inner"
				 		title="Excluir campo" onClick={this.delete}/> : "")
				: ""}
				{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         				PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived")?
					(!!this.props.alterable ? <span className="mdi mdi-pencil attribute-input-edit inner" 
							title="Alterar campo" onClick={this.edit}/> : "")
				:""}
			</div>
		);
	},

	render() {
		var fieldEl;		
		if (this.props.vizualization) {
			if (this.props.fieldDef.type == AttributeTypes.INDICATOR_TYPE){	
				fieldEl = (
					<IndicatorType fieldDef={this.props.fieldDef} vizualization={this.props.vizualization} />
				);
			} else if (this.props.fieldDef.type == AttributeTypes.SELECT_STRUCTURE) {
				fieldEl = (
					<span className="pdi-normal-text">{this.props.fieldDef.valueLabel}</span>
				);
			} else if (this.props.fieldDef.type == AttributeTypes.RESPONSIBLE_FIELD) {
				fieldEl = (
					<Responsible fieldId={this.state.fieldId} fieldDef={this.props.fieldDef} vizualization={this.props.vizualization} />
				);
			} else if (this.props.fieldDef.type == AttributeTypes.SELECT_PLAN_FIELD) {
				fieldEl = (
					<SelectPlan fieldDef={this.props.fieldDef} vizualization={this.props.vizualization} />
				);
			} else if ((this.props.fieldDef.type == AttributeTypes.NUMBER_FIELD || 
				this.props.fieldDef.type == AttributeTypes.CURRENCY_FIELD ||
				this.props.fieldDef.type == AttributeTypes.PERCENTAGE_FIELD) && this.props.fieldDef.formattedValue){
				fieldEl = (
					<span className="pdi-normal-text">{this.props.fieldDef.formattedValue.trim().substr(0,4) == "null" ? "" : this.props.fieldDef.formattedValue}</span>
				);
			} else if (this.props.fieldDef.type == AttributeTypes.STRATEGIC_OBJECTIVE_FIELD) {
				fieldEl = (
					<StrategicObjective fieldId={this.state.fieldId} fieldDef={this.props.fieldDef} strategicObjectivesPlansParam={this.state.strategicObjectivesPlansParam} />
				);		
			} else {
 				fieldEl = (
 					<div><span className="pdi-normal-text" dangerouslySetInnerHTML={{__html: this.props.fieldDef.value}}/></div>
 				);
 			}
		} else if (this.props.fieldDef.type == AttributeTypes.STRATEGIC_OBJECTIVE_FIELD) {
			fieldEl = (
				<StrategicObjective fieldId={this.state.fieldId} fieldDef={this.props.fieldDef} strategicObjectivesPlansParam={this.state.strategicObjectivesPlansParam} />
			);
 		} else if (this.props.fieldDef.type == AttributeTypes.TEXT_AREA_FIELD) {
			if(this.props.isDocument){
				fieldEl = (
					<div>
						<RichText
							rows={this.props.fieldDef.rows}
							maxLength='10000'
							className="form-control minHeight170"
							placeholder={this.props.fieldDef.placeholder}
							name={this.props.fieldDef.name}
							defaultValue={this.props.fieldDef.value}
							id={this.state.fieldId}
							ref={this.state.fieldId}
							onChange={this.props.fieldDef.onChange || _.noop}
							onKeyPress={this.onKeyUp}
							onPaste={this.onKeyUp}
						/>
						<div className="textAreaMaxLenght documentText">
							<span>Máx. 10000 caracteres</span>
						</div>
					</div>
				);			
			} else {
				if (this.props.fieldDef.disabled) {
					fieldEl = (
						/* Div criada devido a a necessidade de retorno de apenas um elemento no render */
						<div>
							<textarea
								rows={this.props.fieldDef.rows}
								maxLength='4000'
								className="form-control minHeight170"
								placeholder={this.props.fieldDef.placeholder}
								name={this.props.fieldDef.name}
								defaultValue={this.props.fieldDef.value}
								id={this.state.fieldId}
								ref={this.state.fieldId}
								onChange={this.props.fieldDef.onChange || _.noop}
								onKeyPress={this.onKeyUp}
								onPaste={this.onKeyUp}
								disabled
								title={"Você não tem permissão para editar este campo."}
							/>
							<div className="textAreaMaxLenght">
								<span>Máx. 4000 caracteres</span>
							</div>
						</div>
					);
				} else {
					fieldEl = (
						/* Div criada devido a a necessidade de retorno de apenas um elemento no render */
						<div>
							<textarea
								rows={this.props.fieldDef.rows}
								maxLength='4000'
								className="form-control minHeight170"
								placeholder={this.props.fieldDef.placeholder}
								name={this.props.fieldDef.name}
								defaultValue={this.props.fieldDef.value}
								id={this.state.fieldId}
								ref={this.state.fieldId}
								onChange={this.props.fieldDef.onChange || _.noop}
								onKeyPress={this.onKeyUp}
								onPaste={this.onKeyUp}
							/>
							<div className="textAreaMaxLenght">
								<span>Máx. 4000 caracteres</span>
							</div>
						</div>
					);
				}
			}
		} else if (this.props.fieldDef.type == AttributeTypes.SELECT_FIELD) {
			fieldEl = (
				<select
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					id={this.state.fieldId}
					ref={this.state.fieldId}
					type={this.props.fieldDef.type}
					onChange={this.props.fieldDef.onChange || _.noop}
					defaultValue={this.props.fieldDef.value}
					>
						{this.props.fieldDef.optionsField ? this.props.fieldDef.optionsField.map((opt,idx) => {
							return (<option key={'field-opt-'+this.state.fieldId+"-"+idx} defaultValue={opt.get ? opt.label : ""}
								data-placement="right" title={opt.label}>
									{opt.label}</option>);
						}):''}
				</select>
			);	
		} else if (this.props.fieldDef.type == AttributeTypes.RESPONSIBLE_FIELD) {
			if (!this.props.fieldDef.users || this.props.fieldDef.users.length <= 0) {
				fieldEl = (
					<input
						className="form-control"
						placeholder="Não existe usuário cadastrado"
						name={this.props.fieldDef.name}
						id={this.state.fieldId}
						ref={this.state.fieldId}
						type={this.props.fieldDef.type}
						disabled
						title="É necessário realizar o cadastro de usuários para prosseguir"
						>
					</input>
				);
			} else if (this.props.fieldDef.disabled) {
				fieldEl = (
					<select
						className="form-control"
						placeholder={this.props.fieldDef.placeholder}
						name={this.props.fieldDef.name}
						id={this.state.fieldId}
						ref={this.state.fieldId}
						type={this.props.fieldDef.type}
						onChange={this.props.fieldDef.onChange || _.noop}
						defaultValue={this.props.fieldDef.value}
						disabled
						title={"Você não tem permissão para editar este campo."}
						>
							{this.props.fieldDef.users ? this.props.fieldDef.users.map((user,idx) => {
								return (<option key={'field-opt-'+this.state.fieldId+"-"+idx} value={user.id}
									data-placement="right" title={user.name}>
										{user.name}</option>);
							}):''}
					</select>
				);
			} else {				
				fieldEl = (
					<select
						className="form-control"
						placeholder={this.props.fieldDef.placeholder}
						name={this.props.fieldDef.name}
						id={this.state.fieldId}
						ref={this.state.fieldId}
						type={this.props.fieldDef.type}
						onChange={this.props.fieldDef.onChange || _.noop}
						defaultValue={this.props.fieldDef.value}
						>
							{this.props.fieldDef.users ? this.props.fieldDef.users.map((user,idx) => {
								return (<option key={'field-opt-'+this.state.fieldId+"-"+idx} value={user.id}
									data-placement="right" title={user.name}>
										{user.name}</option>);
							}):''}
					</select>
				);
			}
		} else if (this.props.fieldDef.type == AttributeTypes.SELECT_PLAN_FIELD) {
			if (this.props.fieldDef.selectPlans.length > 0) {
				fieldEl = (
					<select
						className="form-control"
						placeholder={this.props.fieldDef.placeholder}
						name={this.props.fieldDef.name}
						id={this.state.fieldId}
						ref={this.state.fieldId}
						type={this.props.fieldDef.type}
						onChange={this.props.fieldDef.onChange || _.noop}
						defaultValue={this.props.fieldDef.value}
						>
							{this.props.fieldDef.selectPlans.map((opt,idx) => {
								return (<option key={'field-opt-'+this.state.fieldId+"-"+idx} value={opt.id}
									data-placement="right" title={opt.name}>
									 {(opt.name.length>92)?(string(opt.name).trim().substr(0, 89).concat("...").toString()):(opt.name)}
									</option>);
							})}
					</select>
				);
			} else {
				fieldEl = (
					<select
						disabled
						className="form-control"
						name={this.props.fieldDef.name}
						id={this.state.fieldId}
						ref={this.state.fieldId}
						type={this.props.fieldDef.type}
						>
						<option key='field-opt-' data-placement="right" title="Nenhum plano de metas cadastrado ainda">
							Nenhum plano de metas cadastrado ainda
						</option>
					</select>
				);
			}
		} else if (this.props.fieldDef.type == AttributeTypes.RADIO || this.props.fieldDef.type == AttributeTypes.INDICATOR_TYPE) {			
			fieldEl = (
				<div>
					<div className="row">
						{this.props.fieldDef.options.map((option, idx) => {		
							return (															
								<div className="fpdi-indicator-type-ctn col-sm-2" key={this.props.fieldDef.name+"-option-"+idx}>
									<label onClick={this.props.fieldDef.onClick}>
										<input
											className="col-sm-6"
											type="radio"
											name={this.props.fieldDef.name+idx}
											defaultValue={option[this.props.fieldDef.valueField]}
											defaultChecked={option[this.props.fieldDef.valueField]}	
											ref={this.props.fieldDef.name+"-option-"+idx}
											onChange={this.props.fieldDef.onChange || _.noop}
											onKeyPress={this.onKeyUp}
											onPaste={this.onKeyUp}
											onClick={this.props.fieldDef.onClick}/>
										<label className="fpdi-indicator-type-label col-sm-6" id={"label-"+idx}>{option[this.props.fieldDef.displayField]}</label>
									</label>
								</div>							
							);
						})}						
					</div>
					{this.props.fieldDef.extraRender ? this.props.fieldDef.extraRender() : ""}
				</div>
			);
		} else if (this.props.fieldDef.type == AttributeTypes.SELECT_STRUCTURE) {		
			fieldEl = (
				<select
					className="form-control"
					placeholder={this.props.fieldDef.placeholder}
					name={this.props.fieldDef.name}
					id={this.state.fieldId}
					ref={this.props.fieldDef.name}
					onChange={this.props.fieldDef.onChange}
					defaultValue={this.props.fieldDef.value}
					>
					{this.props.fieldDef.options.map((opt,idx) => {
						return (<option key={'field-opt-'+opt.id+"-"+idx} value={opt.id}
							data-placement="right" title={opt.name}>
								 {(opt.get("name").length>20)?(string(opt.get("name")).trim().substr(0, 15).concat("...").toString()):(opt.get("name"))}
								</option>);
					})}
				</select>
			);
		} else if (this.props.fieldDef.type == AttributeTypes.DATE_FIELD || this.props.fieldDef.type == AttributeTypes.DATE_TIME_FIELD) {
			if (this.props.fieldDef.disabled) {
				fieldEl = (<div><DatePicker
					className="form-control"
					type="datepicker"
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					ref={this.state.fieldId}
					placeholderText="DD/MM/AAAA" 
					dateFormat="DD/MM/YYYY"
					id={this.state.fieldId}
					selected={(this.props.fieldDef.value)?(moment(this.props.fieldDef.value, "DD/MM/YYYY")):(null)}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
					maxLength='255'
					showYearDropdown  
					disabled
					title={"Você não tem permissão para editar este campo."}
				/></div>);
			} else {
				fieldEl = (<div><DatePicker
					className="form-control"
					type="datepicker"
					name={this.props.fieldDef.name}
					defaultValue={this.props.fieldDef.value}
					ref={this.state.fieldId}
					placeholderText="DD/MM/AAAA" 
					dateFormat="DD/MM/YYYY"
					id={this.state.fieldId}
					selected={(this.props.fieldDef.value)?(moment(this.props.fieldDef.value, "DD/MM/YYYY")):(null)}
					onChange={this.props.fieldDef.onChange || _.noop}
					onKeyPress={this.onKeyUp}
					onPaste={this.onKeyUp}
					maxLength='255'
					showYearDropdown
				/></div>);
			}
		} else if (this.props.fieldDef.type == AttributeTypes.NUMBER_FIELD || 
			this.props.fieldDef.type == AttributeTypes.PERCENTAGE_FIELD ||
			this.props.fieldDef.type == AttributeTypes.CURRENCY_FIELD) {		
			if (this.props.fieldDef.disabled) {	
	 		 	fieldEl = (<input
	 				onKeyPress={this.onlyNumber}
					onPaste={this.onlyNumberPaste}	
					className="budget-field-table"
	 			//	type='number'
	 				step='any'
					min="0" //caso seja necessário campo numérico negativo, remover aqui!
	 				name={this.props.fieldDef.name}
	 				defaultValue={this.props.fieldDef.editModeValue}
	 				id={this.state.fieldId}
	 				ref={this.state.fieldId}
	 				placeholder={this.props.fieldDef.placeholder}
	 				onChange={this.props.fieldDef.onChange || _.noop}
	 				disabled
	 				title={"Você não tem permissão para editar este campo."}
	 			/>);
	 		 } else {
	 		 	fieldEl = (<input
	 				onKeyPress={this.onlyNumber}
					onPaste={this.onlyNumberPaste}
	 			//	className="budget-field-table"
	 				className="form-control"
	 				step='any'
	 			//	type='number'
					min="0" //caso seja necessário campo numérico negativo, remover aqui!
	 				name={this.props.fieldDef.name}
	 				defaultValue={this.props.fieldDef.editModeValue}
	 				id={this.state.fieldId}
	 				ref={this.state.fieldId}
	 				placeholder={this.props.fieldDef.placeholder}
	 				onChange={this.props.fieldDef.onChange || _.noop}
	 			/>);
	 		 }
		} else if (this.props.fieldDef.type == AttributeTypes.TOTAL_FIELD){
			fieldEl = (<input
 				onKeyPress={this.onlyNumber}
				onPaste={this.onlyNumberPaste}
 				className="form-control"
 				type='number'
 				name={this.props.fieldDef.name}
 				value={this.props.fieldDef.value || 0}
 				id={this.state.fieldId}
 				ref={this.state.fieldId}
 				placeholder={this.props.fieldDef.placeholder}
 				onChange={this.props.fieldDef.onChange || _.noop} 				
				disabled={true}
 			/>);
		} else {
			fieldEl = (<input
				//className="budget-field-table"
				className="form-control"
				type={this.props.fieldDef.type}
				maxLength='100'
				onKeyPress={this.onKeyUp}
				onPaste={this.onKeyUp}
				name={this.props.fieldDef.name}
				defaultValue={this.props.fieldDef.value}
				id={this.state.fieldId}
				ref={this.state.fieldId}
				placeholder={this.props.fieldDef.placeholder}
				onChange={this.props.fieldDef.onChange || _.noop}
				onKeyPress={this.onKeyUp}
				maxLength='255'
				disabled={this.props.fieldDef.disabled}
				title={this.props.fieldDef.disabled ? "Você não tem permissão para editar este campo." : ""}
			/>);
		}
			
		var strategicObjectivesPlans = "";
		if (this.props.fieldDef.type == AttributeTypes.STRATEGIC_OBJECTIVE_FIELD) {
			if (this.props.fieldDef.selectPlans.length > 0) {
				strategicObjectivesPlans = (
					<select
						className="marginLeft10"
						placeholder={this.props.fieldDef.placeholder}
						name={this.props.fieldDef.name}
						id={this.props.fieldId}
						ref="strategicObjectivesSelectPlan"
						type={this.props.fieldDef.type}
						onChange={this.onStrategicObjectivesSelectPlanChange}
						defaultValue={this.props.fieldDef.value}
						>
						<option value={-1} data-placement="right" title="Todos os planos de metas">Todos os planos de metas </option>
							{this.props.fieldDef.selectPlans.map((opt,idx) => {
								return (<option key={'field-opt-'+this.props.fieldId+"-"+idx} value={opt.id} data-placement="right" title={opt.name}>
									 {(opt.name.length>20)?(string(opt.name).trim().substr(0, 15).concat("...").toString()):(opt.name)}
								</option>);
							})}
					</select>
				) 
			} else {
				strategicObjectivesPlans = (
					<select
						disabled
						className="marginLeft10"
						name={this.props.fieldDef.name}
						id={this.props.fieldId}
						ref={this.props.fieldId}
						type={this.props.fieldDef.type}
						>
						<option key='field-opt-' data-placement="right" title="Nenhum plano de metas cadastrado ainda">
							Nenhum plano de metas cadastrado ainda
						</option>
					</select>
				)
			}
		}
		return (!!this.props.undeletable || !!this.props.alterable ? (
			<div className="panel panel-default panel-margins">
				<div className="panel-heading attribute-input-opts">
					{!this.props.vizualization ? 
						<div className="edit-section-attribute"> 
							<input defaultValue={this.props.fieldDef.label} className="edit-section-attribute-input" maxLength="255" ref="edit-input"/> <br/>
							<div className="formAlertError" ref="formAlertError-edit-input"></div>
						</div>
					:
						<div>
							<div className={(!!this.props.undeletable ? (this.state.menuHidden ? "" : "widthLimit pull-left") : "")}>
								<b className="budget-title">{this.props.fieldDef.label}</b>
							</div>
							{strategicObjectivesPlans}
							{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
									PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.context.planMacro.get("archived")?
								(!!this.props.undeletable ? <span type="submit" className="mdi mdi-delete attribute-input-edit inner"
									title="Excluir campo" onClick={this.delete}/> : "")
							: ""}
							<div className="clearfix"/>
						</div>
					}
					
				</div>
				<div>
					{fieldEl}
				</div>
				<div className="formAlertError" ref="formAlertError"></div>
			</div>
		) : (
			<div className={"form-group form-group-sm" + (this.props.fieldDef.type == 'hidden' ? " hidden":"")}>
				{this.props.vizualization && this.props.fieldDef.name == "indicator-type" ? "" : 
				<label htmlFor={this.state.fieldId} className="fpdi-text-label">
					{this.props.fieldDef.label}
					{this.props.fieldDef.required && !this.props.vizualization ? <span className="fpdi-required">&nbsp;</span>:""}
				</label>}
				{fieldEl}
				{typeof this.props.fieldDef.helpBox === 'string' ?
					<p className="help-block">{this.props.fieldDef.helpBox}</p>
					:this.props.fieldDef.helpBox
				}
				<div className="formAlertError" ref="formAlertError"></div>
			</div>
		));
	}
});