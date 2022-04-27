import _ from "underscore";
import moment from 'moment';
import React from "react";
import {Link} from 'react-router';
import ActionPlanStore from "forpdi/jsx/planning/store/ActionPlan.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
//import Toastr from 'toastr';

var Validate = Validation.validate;

export default React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired,
		accessLevel: React.PropTypes.number.isRequired,
		accessLevels: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired
	},

	getInitialState() {
		var datatual = new Date();
		var ano = datatual.getFullYear();
		if(!(this.props.dataIniPlan<= ano && 
			this.props.dataFimPlan>=ano)){
				ano = '';
				
			}
		return {
			error: false,
			errorMessage: "",
			actionPlans: null,
			editingActionID: 0,
			dataFiltro: ano,
			editingActionIdx: null,
			checked: false,
			hide: false,
			initChanged:false,
			idx:-1,
			endChanged:false,
			ref: null,
			levelInstanceId: this.props.levelInstanceId,
			loading: true,
			totalPlans:null
		};
	},

	newActionPlan() {
		this.setState({
    		adding: true,
			editingActionID: 0,
			editingActionIdx: null,
    		hide: false,
			endDate: undefined,
			initDate: undefined,
			loading:false
    	});
	},

	onKeyUp(evt){
		var key = evt.which;
		if(key == 13 || key == 17) {
			evt.preventDefault();
			return;
		}
	},

	maxLengthMask(){
		if(this.refs[this.ref].value.length >= this.refs[this.ref].maxLength){
			//Toastr.remove();
			//Toastr.error("Limite de "+this.refs[this.ref].maxLength+" caracteres atingido!");
			this.context.toastr.addAlertError(Messages.get("label.limit") +this.refs[this.ref].maxLength+ " " + Messages.get("label.error.limitCaracteres"));
		}
	},

	emptyValidation(isDate, formAlertError){
		if(isDate){
			eval("this.refs." + formAlertError + ".innerHTML = ''");
		}else{
			if(this.refs[this.ref].value != ""){
				this.refs[this.ref].className = "budget-field-table";
				//Faz com que a string concatenada se torne código javascript
				eval("this.refs." + formAlertError + ".innerHTML = ''");
			}
		}
	},

	componentDidMount()	{
		this.setState({
    		adding: false,
    	});
    	ActionPlanStore.on("actionPlanSavedSuccess", model => {
			this.state.actionPlans.push(model.data);
			this.setState({
				adding: false,
				loading:false,
			});

			this.getActionPlans(this.state.levelInstanceId,1,5);

			//Toastr.remove();
			//Toastr.success("Plano de ação salvo com sucesso!");
			this.context.toastr.addAlertSuccess(Messages.get("label.success.planSaved"));
		},this);

		ActionPlanStore.on("actionPlanDeletedSuccess", model => {
			this.state.actionPlans.splice(this.state.idx,1);
			this.setState({
				loading: false,
			});
			this.getActionPlans(this.state.levelInstanceId,1,5);
			//Toastr.remove();
			//Toastr.success("Plano de ação deletado com sucesso!");
			this.context.toastr.addAlertSuccess(Messages.get("label.success.planDeleted"));
		},this);

		ActionPlanStore.on("actionPlanDeletedError", model => {
    		//Toastr.remove();
			//Toastr.error("Erro ao deletar plano de ação");
			this.context.toastr.addAlertError(Messages.get("label.error.deletePlanAction"));
			this.setState({
				adding: false,
				Loading:false
			});
		},this);

		ActionPlanStore.on("actionPlanEdited", model => {

			if(model.responseText){
				//Toastr.remove();
				//Toastr.error("Já existe um plano de ação cadastrado com esta descrição!");
				this.context.toastr.addAlertError(Messages.get("label.error.alreadyRegisteredPlanAction"));
			} else{
				//Toastr.remove();
				//Toastr.success("Plano de ação editado com sucesso!");
				this.context.toastr.addAlertSuccess(Messages.get("label.success.editPlanAction"));
				this.setState({
					loading:false
				})
			}
			this.state.actionPlans.splice(this.state.editingActionIdx,1,model.data);
			this.cancelNewActionPlan();
		});

		this.getActionPlans(this.state.levelInstanceId,1,5);

		ActionPlanStore.on("listActionPlanRetrieved", (model) => {
			this.setState({
				actionPlans:model.data,
				loading:false,
				totalPlans:model.total
			})
		},this);

	},

	componentWillUnmount() {
		ActionPlanStore.off(null, null, this);
	},

	cancelNewActionPlan(){
		this.setState({
    		adding: false,
    		editingActionID: 0,
    		editingActionIdx: null
    	});
	},



	acceptNewActionPlan(){
		var validation = Validate.validationNewActionPlan(this.state, this.refs);


		if (validation.boolMsg) {
			//Toastr.remove();
 			//Toastr.error(msg);
			this.context.toastr.addAlertError(validation.msg);
 			return;
		}

		var actionPlan = {
			checked : this.state.checked,
			description : this.refs.descricao.value,
			responsible : this.refs.responsavel.value,
			begin : validation.dataBegin,
			end : validation.dataEnd,
		};
		this.setState({
			checked:false
		});
		this.props.newFunc(actionPlan);
	},

	acceptEditActionPlan(id){
		var validation = Validate.validationEditActionPlan(this.state, this.refs);

		if (validation.boolMsg) {
			//Toastr.remove();
 			//Toastr.error(msg);
			 this.context.toastr.addAlertError(validation.msg);
 			return;
		}


		ActionPlanStore.dispatch({
			action: ActionPlanStore.ACTION_CUSTOM_UPDATE,
			data: {
				id: id,
				checked: this.state.checked,
				description: this.refs.descricaoEdit.value,
				responsible: this.refs.responsavelEdit.value,
				begin: validation.initDate,
				end:  validation.endDate
			}
		});
	},


	editActionPlan(id,idx) {
		this.setState({
			editingActionID: id,
			editingActionIdx: idx
		})
	},

	deleteActionPlan(id, idx,evt) {
		var msg = Messages.get("label.deleteConfirmation") + " " + ((this.state.actionPlans[idx].description.length >150)?(this.state.actionPlans[idx].description.substr(0,150)+"..."):
			(this.state.actionPlans[idx].description)) + "?"

		Modal.confirmCancelCustom(() => {
				Modal.hide();
					this.setState({
					loading: true,
					idx: idx //index a ser deletado
				});
				ActionPlanStore.dispatch({
					action: ActionPlanStore.ACTION_DELETE,
					data: {
					id: id
					}
				});

			},msg,()=>{Modal.hide()});

		/*Modal.deleteConfirmCustom(() => {
			Modal.hide();
			this.setState({
				loading: true,
				idx: idx //index a ser deletado
			});
			ActionPlanStore.dispatch({
				action: ActionPlanStore.ACTION_DELETE,
				data: {
				id: id
				}
			});

		},"Você tem certeza que deseja excluir " + ((this.state.actionPlans[idx].description.length >150)?(this.state.actionPlans[idx].description.substr(0,150)+"..."):
			(this.state.actionPlans[idx].description)) + "?");*/
	},

	renderActionField(edit, action,idx){
		if(edit){
			return this.renderEditActionPlan(action,idx);
		} else {
			return (
				<tr  key={"action"+idx} >
					{this.context.roles.MANAGER ||
						(_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
						this.props.responsible && UserSession.get("user").id == this.props.responsible.id) ?
						<td className="textAlignCenter" >
							<input type='checkbox' name={"checkbox"+action.id} key={action.id}
							defaultChecked={action.checked} ref={"checkbox-action-"+action.id}
							onChange={this.sendCheckBox.bind(this, action.id, action)} defaultValue={action.checked}/>
						</td>
					:<td/>}
					<td>{action.description}</td>
					<td>{action.responsible}</td>
					<td>{action.begin.split(" ")[0]}</td>
					<td>{action.end.split(" ")[0]}</td>
					{this.context.roles.MANAGER ||
						(_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
							this.props.responsible && UserSession.get("user").id == this.props.responsible.id) ?
						<td id={'options'+idx} className='edit-budget-col cursorDefault'>
							<span className='mdi mdi-pencil cursorPointer' onClick={this.editActionPlan.bind(this,action.id,idx)} title={Messages.get("label.title.editInformation")}/>
							<span className='mdi mdi-delete cursorPointer' onClick={this.deleteActionPlan.bind(this,action.id,idx)} title={Messages.get("label.delete")}/>
						</td>
					:<td/>}
				</tr>
			);
		}
	},

	renderEditActionPlan(action,idx){
		if(idx != this.state.idx){
			this.state.initChanged = false;
			this.state.endChanged = false;
		}
		this.state.idx = idx;
		var dataBegin = new Date ();
		var dataSimple = action.begin.split(" ");
		var data = dataSimple[0].split("/");
		dataBegin = data[0]+"/"+(data[1])+"/"+data[2];
		var dataEnd = new Date ();
		var dataSimple = action.end.split(" ");
		var data = dataSimple[0].split("/");
		dataEnd = data[0]+"/"+(data[1])+"/"+data[2];
		dataBegin = moment(dataBegin,"DD/MM/YYYY");
		dataEnd = moment(dataEnd,"DD/MM/YYYY");
		if(!this.state.initChanged){
		    this.state.initDate = dataBegin;
		}
		if(!this.state.endChanged){
			this.state.endDate = dataEnd;
		}
		var desc = 'descricaoEdit';
		var resp = 'responsavelEdit';
		var me = this;
		return(
			<tr key={"action"+idx} >
				<td className="textAlignCenter" >
					<input type='checkbox' ref='checkEdit' name='checkbox' defaultChecked={action.checked} defaultValue={action.checked}
					 onChange={this.onChange} />
				</td>
				<td><input
				type='text'
				ref='descricaoEdit'
				className='budget-field-table'
				defaultValue={action.description}
				maxLength="3900"
				onKeyUp={function(evt){
					me.ref = desc;
					if (evt.which != 17) {
						me.onKeyUp(evt);
						me.maxLengthMask();
						me.emptyValidation(false,"formAlertErrorDescriptionEdit");
					}
				}}/>
					<div ref="formAlertErrorDescriptionEdit" className="formAlertError"></div>
				</td>
				<td><input
				type='text'
				ref='responsavelEdit'
				className='budget-field-table'
				defaultValue={action.responsible}
				maxLength="255"
				onKeyUp={function(evt){
					me.ref = resp;
					if (evt.which != 17) {
						me.onKeyUp(evt);
						me.maxLengthMask();
						me.emptyValidation(false,"formAlertErrorResponsibleEdit");
					}
				}}/>
					<div ref="formAlertErrorResponsibleEdit" className="formAlertError"></div>
				</td>
				<td><DatePicker
					type="datepicker"
					ref='begin'
					dateFormat="DD/MM/YYYY"
					selected={this.state.initDate}
					onChange={this.onChangeInit.bind(this,"formAlertErrorBeginEdit")}
					placeholderText="DD/MM/AAAA"
					showYearDropdown
					/>
					<div ref="formAlertErrorBeginEdit" className="formAlertError"></div>
				</td>
				<td><DatePicker
					type="datepicker"
					ref='end'
					dateFormat="DD/MM/YYYY"
					selected={this.state.endDate}
					onChange={this.onChangeEnd.bind(this,"formAlertErrorEndEdit")}
					placeholderText="DD/MM/AAAA"
					showYearDropdown
					/>
					<div ref="formAlertErrorEndEdit" className="formAlertError"></div>
				</td>
				<td>
		            <div className='displayFlex'>
		               	<span className='mdi mdi-check accepted-budget' onClick={this.acceptEditActionPlan.bind(this,action.id)} title={Messages.get("label.submitLabel")}></span>
		              	<span className='mdi mdi-close reject-budget' onClick={this.cancelNewActionPlan} title={Messages.get("label.cancel")}></span>
		           	</div>
		        </td>
            </tr>
		);
	},

	onChange(e) {
		this.state.checked = e.target.checked;
	},

	sendCheckBox(id, actionPlan, evt) {
		var url = window.location.href;
		evt.preventDefault();
		var msg = (actionPlan.checked ?
		 Messages.get("label.reopenPlanAction")+actionPlan.description+"?" :
		 Messages.get("label.completeActionPlan")+actionPlan.description+"?");
		//Atualiza o checkbox do frontend
		Modal.confirmCustom(() => {
			actionPlan.checked = !actionPlan.checked;
			this.refs['checkbox-action-'+id].checked = actionPlan.checked;
			ActionPlanStore.dispatch({
				action: ActionPlanStore.ACTION_CHECKBOX_UPDATE,
				data: {
					id: id,
					checked: actionPlan.checked,
					url:url
				}
			});
			Modal.hide();
		},msg,()=>{Modal.hide()});

	},

	hideFields() {
		this.setState({
			hide: !this.state.hide
		})
	},

	onChangeInit(formAlertError, data){
		this.emptyValidation(true, formAlertError)
		this.setState({
			initDate:data,
			initChanged:true
		});
	},
	onChangeEnd(formAlertError, data){
		this.emptyValidation(true, formAlertError)
		this.setState({
			endDate: data,
			endChanged:true
		})
	},

	pageChange(page,pageSize) {
		this.setState({
			loading: true
		});
   		this.getActionPlans(this.state.levelInstanceId,page,pageSize);
  	},
	eventoAlteraFiltroData (data){
			this.getActionPlans(this.state.levelInstanceId,1,5,data);
			this.setState({dataFiltro:data});
		},
	getActionPlans(levelInstanceId,page,pageSize,dt = this.state.dataFiltro) {
		ActionPlanStore.dispatch({
			action: ActionPlanStore.ACTION_RETRIVE_ACTION_PLAN_ATTRIBUTE,
			data: {
				id: levelInstanceId,
				page: page,
				pageSize: pageSize,
				dtFiltro:dt
			}
		});
	},

	renderNewActionPlan(){
		this.state.idx = this.state.actionPlans.length;
		var desc = "descricao";
		var resp = "responsavel";
		var me = this;
		return(
			<tr key='new-actionPlan'>
				<td className="textAlignCenter"><input type="checkbox" name="checkbox"  onChange={this.onChange} /></td>
				<td><input
				type='text'
				ref="descricao"
				className='budget-field-table'
				maxLength="3900"
				onKeyUp={function(evt){
					me.ref = desc;
					if (evt.which != 17) {
						me.onKeyUp(evt);
						me.maxLengthMask();
						me.emptyValidation(false,"formAlertErrorDescription");
					}
				}}/>
					<div ref="formAlertErrorDescription" className="formAlertError"></div>
				</td>
				<td><input type='text'
				ref="responsavel"
				className='budget-field-table'
				maxLength="255"
				onKeyUp={function(evt){
					me.ref = resp;
					if (evt.which != 17) {
						me.onKeyUp(evt);
						me.maxLengthMask();
						me.emptyValidation(false,"formAlertErrorResponsible");
					}
				}}/>
					<div ref="formAlertErrorResponsible" className="formAlertError"></div>
				</td>

				<td><DatePicker
					type="datepicker"
					className = 'budget-field-table'
					ref='begin'
					dateFormat="DD/MM/YYYY"
					selected={this.state.initDate}
					onChange={this.onChangeInit.bind(this,"formAlertErrorBegin")}
					placeholderText="DD/MM/AAAA"
					showYearDropdown
					/>
					<div ref="formAlertErrorBegin" className="formAlertError"></div>
				</td>
				<td><DatePicker
					type="datepicker"
					className = 'budget-field-table'
					ref='end'
					dateFormat="DD/MM/YYYY"
					selected={this.state.endDate}
					onChange={this.onChangeEnd.bind(this,"formAlertErrorEnd")}
					placeholderText="DD/MM/AAAA"
					showYearDropdown
					/>
					<div ref="formAlertErrorEnd" className="formAlertError"></div>
				</td>
				<td>
	                <div className='displayFlex'>
	                   	<span className='mdi mdi-check accepted-budget' onClick={this.acceptNewActionPlan} title={Messages.get("label.submitLabel")}></span>
	                  	<span className='mdi mdi-close reject-budget' onClick={this.cancelNewActionPlan} title={Messages.get("label.cancel")}></span>
	               	</div>
	            </td>
			</tr>
		);
	},

	render(){
		return(
			<div className="panel panel-default panel-margins">
				<div className="panel-heading displayFlex">
					<b className="budget-graphic-title">{Messages.getEditable("label.planAction","fpdi-nav-label")} </b>
					{(this.state.adding)?
						"":
					<div className="budget-btns">
						{this.context.roles.MANAGER ||
								(_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
								(this.props.responsible && UserSession.get("user") !=null && UserSession.get("user").id == this.props.responsible.id)) ?

								<button type="button" className="btn btn-primary budget-new-btn" onClick={this.newActionPlan}>{Messages.getEditable("label.new","fpdi-nav-label")}</button>
						:""}
						<span className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")} onClick={this.hideFields}></span>
					</div>}

				</div>
				{!this.state.hide ?
					(<div className="table-responsive">
						{this.state.loading ? <LoadingGauge/> :
						<div>
							<section style={{display: 'flex',flexDirection: 'row',margin: '10px',
							justifyContent: 'flex-start',
						alignItems: 'baseline',gap:'5px'}}>
								<label htmlFor="dtfiltro">{Messages.get("label.ano")}</label> 
								<select value={this.state.dataFiltro} id="dtfiltro"
									onChange={date => this.eventoAlteraFiltroData(date.target.value)}
								>
									<option value=''>Todos</option>
									{ Array.from({length:(this.props.dataFimPlan
									-this.props.dataIniPlan)+1},(_,x) => this.props.dataIniPlan+x )
									.map((x) => 
										<option value={x} key={x}>{x}</option> 
									)

									}
								</select>
							</section>
						<table className="budget-field-table table">
							<thead>
								<tr>
									{this.context.roles.ADMIN || this.context.roles.MANAGER == true ||
										(_.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_PERMISSION) ||
										(this.props.responsible && UserSession.get("user") !=null && UserSession.get("user").id == this.props.responsible.id)) ?
										<th className="textAlignCenter">{Messages.getEditable("label.completed","fpdi-nav-label")}</th>
									:<th/>}
									<th>{Messages.getEditable("label.description","fpdi-nav-label")}</th>
									<th>{Messages.getEditable("label.responsible","fpdi-nav-label")}</th>
									<th>{Messages.getEditable("label.begin","fpdi-nav-label")}</th>
									<th>{Messages.getEditable("label.term","fpdi-nav-label")}</th>
								</tr>
							</thead>
								<tbody>
									{this.state.adding ? this.renderNewActionPlan() : undefined}
									{this.state.actionPlans.map((model, idx) => {
										return this.renderActionField((model.id == this.state.editingActionID),model,idx);
									})}
								</tbody>
						</table>
						</div>}

						<TablePagination
							total={this.state.totalPlans}
							onChangePage={this.pageChange}
							tableName={"actionPlan-table-"+this.props.levelInstanceId}
						/>
					</div>) : undefined
				}
			</div>
		);
	}

});
