import React from "react";
import {Link} from 'react-router';
 
import TreeView from "forpdi/jsx/core/widget/treeview/TreeView.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";
import ActionPlanStore from "forpdi/jsx/planning/store/ActionPlan.jsx";
import ScheduleStore from "forpdi/jsx/planning/store/Schedule.jsx";
import TableStore from "forpdi/jsx/planning/store/TableFields.jsx";
import DocumentStore from "forpdi/jsx/planning/store/Document.jsx"
import AggregateIndicator from "forpdi/jsx/planning/widget/plan/AggregateIndicator.jsx"
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import GoalPerformance from "forpdi/jsx/planning/widget/plan/GraphForIndicator.jsx";
import LevelSons from "forpdi/jsx/planning/widget/plan/LevelInstanceSons.jsx";
import AttributeForm from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
//import Toastr from 'toastr';
import moment from 'moment';
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";


import _ from 'underscore';

var VerticalForm = AttributeForm.VerticalForm;
var Validate = Validation.validate;

var dateBegin = null;
var dateEnd = null;

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object.isRequired,
		planMacro: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {				
		return {
			loading: true,
			fields: [],
			model: null,
			title: "",
			subTitle: "",
			undeletable: false,
			vizualization: true,
			tabPath: this.props.location.pathname,
			aggregate: false,
			level: "",
			showPolarityAlert: false,
			beginIdx: null,
			endIdx: null,
			finishIdx: null
		};
	},

	getFields(model, agg) {		
		var fields = [];
		if(model.data.level.indicator){			
			var options = [{
				name:  Messages.get("label.aggregateSimples"),
				value: !model.data.aggregate		
			},{				
				name: Messages.get("label.aggregateAggregated"),
				value: model.data.aggregate
			}];		

			fields.push({
				name: "indicator-type",
				type: AttributeTypes.INDICATOR_TYPE,
				required: true,
				placeholder: "",
				label: "tipo de indicador",				
				displayField: 'name',
				valueField: 'value',
				options: options,
				onClick: this.onIndicatorTypeClick,
				extraRender: this.renderAggregate,
				disabled: this.context.roles.MANAGER || _.contains(this.context.permissions, 
         							PermissionsTypes.MANAGE_PLAN_PERMISSION) ? false : true 
			});
		}

		fields.push({
			name: "name",
			type: "text",
			required: true,
			placeholder: "",
			label: Messages.getEditable("label.name","fpdi-nav-label"),
			value: model.data.name,
			disabled: this.context.roles.MANAGER || _.contains(this.context.permissions, 
         							PermissionsTypes.MANAGE_PLAN_PERMISSION) ? false : true 
		});

		if (model.data.level.attributes) {
			model.data.level.attributes.map((model,idx) =>{				
				var formattedValue = (model.attributeInstance ? model.attributeInstance.formattedValue : "");
				var value = (model.attributeInstance ? model.attributeInstance.value : "");
				var editModeValue = null;
				if(model.type == AttributeTypes.NUMBER_FIELD && value != undefined) {
					if (model.attributeInstance && model.attributeInstance.valueAsNumber) {
						value = model.attributeInstance.valueAsNumber;
						editModeValue = value.toString().replace(".", ",");
					} else {
						value = "";
						editModeValue = value;
					}
					
				}
				
				
				if (model.beginField) {
					dateBegin = value;
					if (this.isMounted()) {
						this.setState({
							beginIdx: idx
						});
					}
				} else if (model.endField) {
					dateEnd = value;
					if (this.isMounted()) {
						this.setState({
							endIdx: idx
						});
					}
				} else if (model.finishDate) {
					if (this.isMounted()) {
						this.setState({
							finishIdx: idx
						});
					}
				}

				if (model.formatField && agg) {
					fields.push({
						name: "attribute"+idx,
						type: model.type,
						required: model.required,
						placeholder: model.description,
						label: model.label,
						value: value,
						formattedValue: formattedValue,
						editModeValue: editModeValue,
						disabled: true,
						budgets: model.budgets,
						extra: (model.budgets ? this.newLevelInstanceBudget : undefined),
						schedule: model.schedule,
						optionsField: model.optionsField,
						users: model.users,
						onChange:(model.beginField ? this.onChangeBegin : model.endField ? this.onChangeEnd : model.finishDate ? this.onChangeFinish : null) ,
						extraSchedule: (model.schedule ? this.saveInstanceSchedule : undefined),
						actionPlans: model.actionPlans,
						extraActionPlans: (model.actionPlans ? this.newLevelInstanceActionPlan : undefined),
						tableFields: model.tableFields,
						extraTableFields: (model.tableFields ? this.saveInstanceTable : undefined),
					});	
				} else if (model.reachedField || model.justificationField) {
					fields.push({
						name: "attribute"+idx,
						type: model.type,
						required: model.required,
						placeholder: model.description,
						label: model.label,
						value: value,
						formattedValue: formattedValue,
						editModeValue: editModeValue,
						disabled: false,
						budgets: model.budgets,
						extra: (model.budgets ? this.newLevelInstanceBudget : undefined),
						schedule: model.schedule,
						optionsField: model.optionsField,
						users: model.users,
						onChange:(model.beginField ? this.onChangeBegin : model.endField ? this.onChangeEnd : model.finishDate ? this.onChangeFinish : null) ,
						extraSchedule: (model.schedule ? this.saveInstanceSchedule : undefined),
						actionPlans: model.actionPlans,
						extraActionPlans: (model.actionPlans ? this.newLevelInstanceActionPlan : undefined),
						tableFields: model.tableFields,
						extraTableFields: (model.tableFields ? this.saveInstanceTable : undefined),
					});	
				} else {
					fields.push({
						name: "attribute"+idx,
						type: model.type,
						required: model.required,
						placeholder: model.description,
						label: model.label,
						value: value,
						formattedValue: formattedValue,
						editModeValue: editModeValue,
						disabled: this.context.roles.MANAGER || _.contains(this.context.permissions, 
         							PermissionsTypes.MANAGE_PLAN_PERMISSION) ? false : true,
						budgets: model.budgets,
						extra: (model.budgets ? this.newLevelInstanceBudget : undefined),
						schedule: model.schedule,
						optionsField: model.optionsField,
						users: model.users,
						onChange:(model.beginField ? this.onChangeBegin : model.endField ? this.onChangeEnd : model.finishDate ? this.onChangeFinish : null) ,
						extraSchedule: (model.schedule ? this.saveInstanceSchedule : undefined),
						actionPlans: model.actionPlans,
						extraActionPlans: (model.actionPlans ? this.newLevelInstanceActionPlan : undefined),
						tableFields: model.tableFields,
						extraTableFields: (model.tableFields ? this.saveInstanceTable : undefined),
					});		
				}

				if (model.users) {
					if (this.isMounted()) {
						this.setState({
							users: model.users
						});
					}
				}	
			})
		}

		return fields;
	},

	onIndicatorTypeClick(evt){		
		var idx = evt.target.id.split('-')[1];
		let normalRadio = this.refs['levelForm'].refs['indicator-type'].refs['indicator-type-option-0'];
		let aggregateRadio = this.refs['levelForm'].refs['indicator-type'].refs['indicator-type-option-1'];

		let agg;
		if(idx == 0){
			aggregateRadio.checked = false;
			aggregateRadio.value = false;
			normalRadio.checked = true;
			normalRadio.value = true;
			agg = false;
		}else{
			if(this.state.haveChildren){				
				this.context.toastr.addAlertError(Messages.get("label.error.noGoalsAggregateAggregated"));
        		aggregateRadio.checked = false;
				aggregateRadio.value = false;
        		return false;
			}
			aggregateRadio.checked = true;
			aggregateRadio.value = true;
			normalRadio.checked = false;
			normalRadio.value = false;
			agg = true;
		}	
		
		if (this.isMounted()) {
			this.setState({
				aggregate: agg,
				fields: this.getFields(this.state.model, agg)
			});
		}
	},

	componentDidMount() {
		var me = this;
		this.getLevelInstanceAttributes(this.props.params.levelInstanceId);	

		StructureStore.on("levelGoalClosed", (model) => {
			if(this.isMounted()){
				this.setState({
					vizualization:true
				})
			}
		});	

		StructureStore.on("goalsGenerated", (model) => {
			if (model.data.auxValue && this.isMounted()) {
				if (this.isMounted()) {
					this.setState({
						levelValue: model.data.auxValue
					})
				}
			}
		});

		StructureStore.on('levelAttributeSaved', (model) => {			
			if(model.success == true){				
				this.context.toastr.addAlertSuccess(Messages.get("label.success.informationSaved"));
				this.refs.levelInstanceTitle.title = model.data.name;				
				if(this.isMounted()){		
					var showPolarityAlert = false;
					if (!this.state.undeletable && this.state.model.data.polarity != model.data.polarity)	
						showPolarityAlert = true;
					
					if (this.isMounted()) {
						this.setState({
							vizualization: true,
							model: model,
							title: model.data.level.name,
							subTitle: model.data.name,
							levelValue: model.data.levelValue,
							fields: this.getFields(model, model.data.aggregate),
							aggregate: model.data.aggregate,
							showPolarityAlert: showPolarityAlert
						});
					}
				}
			}
			else{				
				this.context.toastr.addAlertError(Messages.get("label.impossibleSaveAttributes"));
			}
		}, me);

		StructureStore.on('deleteLevelInstance', store => {
			me.context.tabPanel.removeTabByPath(me.state.tabPath);
			StructureStore.dispatch({
				action: StructureStore.ACTION_FIND,
				data: null
			});			
			this.context.toastr.addAlertSuccess(store.data.name + " " + Messages.get("label.successDeleted"));
		}, me);

		StructureStore.on('favoriteSaved', model => {
			if (this.isMounted()) {
				this.setState({
					favoriteExistent: true,
					favoriteTotal: this.state.favoriteTotal+1
				});
			}
			this.context.toastr.addAlertSuccess(model.data.levelInstance.name + " " + Messages.get("label.addedFavorites"));
		}, me);

		StructureStore.on('favoriteRemoved', model => {
			if (this.isMounted()) {
				this.setState({
					favoriteExistent: false,
					favoriteTotal: this.state.favoriteTotal-1
				});
			}
			this.context.toastr.addAlertSuccess(model.data.levelInstance.name + " " + Messages.get("label.removedFavorites"));
		}, me);

		StructureStore.on("levelAttributeRetrieved", (model) => {
			if(me.isMounted()){				
				me.setState({
					loading: false,
					model: model,
					title: model.data.level.name,
					subTitle: model.data.name,
					fields: this.getFields(model, model.data.aggregate),
					aggregate: model.data.aggregate,
					levelValue: model.data.levelValue,									
					level: model.data.level.name,
					showPolarityAlert: false,
					favoriteExistent: model.data.favoriteExistent,
					favoriteTotal: model.data.favoriteTotal
				});				
				me.context.tabPanel.addTab(me.state.tabPath, model.data.name);
			}
		}, me);	
			
		StructureStore.on("levelAttributeNoRetrieved", (model) => {
			var url = window.location.href;
			window.location.assign(url.split("#")[0]+"#/home");
		}, me);	

		StructureStore.on("levelSonsRetrieved", (model) => {
			if(me.isMounted()){		
				me.setState({
					undeletable: model.data.sons.list.length>0 ? false : true
				});
			}
		}, me);

		/*StructureStore.on("levelUpdate", (model) => {
			if(this.isMounted()){
				this.setState({
					levelValue: model.data.levelValue
				});
				this.getLevelInstanceAttributes(this.props.params.levelInstanceId);	
			}
		}, me);*/
	},

	componentWillUnmount() {
		StructureStore.off(null, null, this);
		BudgetStore.off(null, null, this);
		ScheduleStore.off(null, null, this);
 		ActionPlanStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		if(this.isMounted()){			
			this.setState({				
				vizualization: true			
			});
			var el = document.getElementsByClassName("fpdi-app-content")[0];		//pegando o elemento que contém os atributos
			el.scrollTop = 0;														//voltando seu scroll para o topo
			if (newProps.location.pathname != this.state.tabPath) {
				if (this.isMounted()) {
					this.setState({
						tabPath: newProps.location.pathname,
						loading: true
					});		
				}	
				this.getLevelInstanceAttributes(newProps.params.levelInstanceId);
			}			
		}
	},

	getLevelInstanceAttributes(levelInstanceId){
		StructureStore.dispatch({
			action: StructureStore.ACTION_RETRIEVE_LEVELATTRIBUTES,
			data: {
				id: levelInstanceId
			}
		});
	},

	newLevelInstanceBudget(name,subAction){		
		BudgetStore.dispatch({
			action: BudgetStore.ACTION_CREATE,
			data: {
				instanceId: this.props.params.levelInstanceId,
				name: name,
				subAction: subAction
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

	newLevelInstanceActionPlan(actionPlan){
		ActionPlanStore.dispatch({
			action: ActionPlanStore.ACTION_CUSTOM_CREATE,
			data: {
				actionPlan: {
					description: actionPlan.description,
					responsible: actionPlan.responsible,
					checked: actionPlan.checked
				},
				instanceId: this.state.model.data.id,
				begin: actionPlan.begin,
				end: actionPlan.end,
				url: window.location.href
			},
			opts: {
				wait: true
			}
		});
	},


	confirmCompleteGoal(id,closeOpenGoal) {
		var msg = "";

		{closeOpenGoal ? 
			msg = Messages.get("label.msg.completeGoal")

			: msg = Messages.get("label.msg.openGoal") 
		}
		
		Modal.confirmCustom(() => {
			Modal.hide();
			{StructureStore.dispatch({
				action: StructureStore.ACTION_CLOSE_GOAL,
				data: {
					id: id,
					openCloseGoal:closeOpenGoal
				}
			});
				var newModel = this.state.model;
				newModel.data.closed = !newModel.data.closed;
				this.setState({
					model: newModel
				})
			};
		},msg);
	},

	deleteLevelAttribute() {
		var data = this.state.model;		
		if(this.state.undeletable) {
			var msg =  Messages.get("label.deleteConfirmation") + " "  + this.state.model.data.name + "?";
			Modal.confirmCancelCustom(() => {
				Modal.hide();
				var levelInstance = {
					id: this.state.model.data.id,
				};
				StructureStore.dispatch({
					action: StructureStore.ACTION_DELETE_LEVELINSTANCE,
					data: levelInstance
				});

			},msg,()=>{Modal.hide()});

			/*Modal.deleteConfirmCustom(() => {
				Modal.hide();
				var levelInstance = {
					id: this.state.model.data.id,
				};
				StructureStore.dispatch({
					action: StructureStore.ACTION_DELETE_LEVELINSTANCE,
					data: levelInstance
				});
			},"Você tem certeza que deseja excluir " + this.state.model.data.name + "?");*/
		}

	},

	saveFavoriteLevel(){
		StructureStore.dispatch({
			action: StructureStore.ACTION_SAVE_FAVORITE,
			data: {
				levelInstanceId: this.state.model.data.id
			}
		});
	},

	removeFavoriteLevel(){
		var msg =  Messages.get("label.msg.removeConfirmation")  + " " + this.state.model.data.name + " " + Messages.get("label.msg.favorites");;
			Modal.confirmCancelCustom(() => {
				Modal.hide();

				StructureStore.dispatch({
					action: StructureStore.ACTION_REMOVE_FAVORITE,
					data: {
						levelInstanceId: this.state.model.data.id
					}
				});
			},msg,()=>{Modal.hide()}
		);
	},

	isNumber(n) {
		
    	return !isNaN(parseFloat(n)) && isFinite(n);
	},
	
	onSubmit(data) {
		var validation = Validate.validateAttributePlan(this.state.model, this.refs['levelForm'], data, this.state.aggregate)

		//finaliza aqui!

		if (validation.boolMsg) {			
			this.context.toastr.addAlertError(validation.msg);
 			return;
		}
				
		var aggregateArray = [];
		if(this.state.model.data.level.indicator && this.state.aggregate){
			var indicators = this.refs['levelForm'].refs['indicator-type'].refs['agg-ind-config'].selectedIndicators;
			indicators.map((indicator, idx) => {		
				aggregateArray.push({
					aggregate: {
						id: indicator.id
					},
					indicator: {
						id: this.state.model.data.id
					},
					percentage: indicator.percentage,
					deleted: indicator.deleted
				});				
			});			
		}
	
		var levelInstance = {
			id: this.state.model.data.id,
			name: validation.nome,
			plan: {
				id: this.state.model.data.plan.id
			},
			level: {
				id: this.state.model.data.level.id,
				attributes: validation.attributes
			},
			aggregate: this.state.aggregate,
			calculation: (this.refs['levelForm'].refs['indicator-type'] && 
				this.refs['levelForm'].refs['indicator-type'].refs['agg-ind-config'] ? 
				this.refs['levelForm'].refs['indicator-type'].refs['agg-ind-config'].calculationValue : 0),
			indicatorList: aggregateArray
		};

		var url = window.location.hash;		
		StructureStore.dispatch({
			action: StructureStore.ACTION_SAVE_LEVELATTRIBUTES,
			data: {
				levelInstance: levelInstance,
				url: url
			}				
		});
	},
	onCancel() {
		if(this.state.aggregate == true) {
			if (this.isMounted()) {
				this.setState({
					vizualization: !this.state.vizualization,				
					aggregate: this.state.model.data.aggregate
				});
			}
		} else {
			this.editingAttributes();
		}
	},

	editingAttributes() {
		var list = this.state.selectedIndicators;
		if (this.isMounted()) {		
			this.setState({
				vizualization: !this.state.vizualization,
				selectedIndicators: list,
				aggregate: this.state.model.data.aggregate
			});
		}
	},

	goalsGenerate() {
		StructureStore.dispatch({
			action: StructureStore.ACTION_GOALSGENERATEACTION_GOALSGENERATE,
			data: {
				name: "teste",
				expected: 10,
				minimum: 5,
				maximum: 20
			}
		});
	},

	onChangeBegin(data){
		var model = this.state.model;
		if (this.isMounted()) {
			this.setState({
				model:model,
			});
		}
		this.refs['levelForm'].refs["attribute"+this.state.beginIdx].props.fieldDef.value = data.format('DD/MM/YYYY');
	},
	onChangeEnd(data){
		var model = this.state.model;
		if (this.isMounted()) {
			this.setState({
				model:model,
			});
		}
		this.refs['levelForm'].refs["attribute"+this.state.endIdx].props.fieldDef.value = data.format('DD/MM/YYYY');
	},
	onChangeFinish(data){
		var model = this.state.model;
		if (this.isMounted()) {
			this.setState({
				model:model,
			});
		}
		this.refs['levelForm'].refs["attribute"+this.state.finishIdx].props.fieldDef.value = data.format('DD/MM/YYYY');
	},

	onlyNumber(evt){
 		var key = evt.which;
 		if((key < 48 || key > 57)) {			
 			evt.preventDefault();
 			return;
 		}
 	},

	isParent(model){
		if (this.isMounted()) {
			this.setState({
				haveChildren: model.data.sons.list.length > 0
			});
		}
	},

	exportLevelAttributes() {
		//implementar o exportar pdf
		var url = DocumentStore.url + "/exportLevelAttributes" + "?levelId=" + this.props.params.levelInstanceId;
		url = url.replace(" ", "+");
		Modal.hide();
		var w = window.open(url);
	},

	renderAggregate(){
		if(this.state.aggregate){
			return(<AggregateIndicator
				ref="agg-ind-config" 
				indicatorsList={this.state.model.data.indicatorList}
				calculationType={this.state.model.data.calculation}
				visualization={this.state.vizualization}
				selfId={this.props.params.levelInstanceId}/>);
		}
	},

	renderBreadcrumb() {
		return(
			<div>
				<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						to={'/plan/'+this.context.planMacro.attributes.id+'/details/'}
						title={this.context.planMacro.attributes.name}>{this.context.planMacro.attributes.name.length > 15 ? this.context.planMacro.attributes.name.substring(0, 15)+"..." : this.context.planMacro.attributes.name.substring(0, 15)
					}</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
				</span>
				<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						to={'/plan/'+this.context.planMacro.attributes.id+'/details/subplan/'+this.state.model.data.plan.id}
						title={this.state.model.data.plan.name}>{this.state.model.data.plan.name.length > 15 ? this.state.model.data.plan.name.substring(0, 15)+"..." : this.state.model.data.plan.name.substring(0, 15)}</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
				</span>
				{this.state.model.data.parents.map((parent, idx) => {							
					return (
						<span key={idx}>
							<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
								to={"/plan/"+this.context.planMacro.attributes.id+"/details/subplan/level/"+parent.id}
								title={parent.name}>{parent.name.length > 15 ? parent.name.substring(0, 15)+"..." : parent.name.substring(0, 15)}</Link>
							<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
						</span>
					);			
				})}
				<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
					{this.state.model.data.name.length > 15 ? this.state.model.data.name.substring(0, 15)+"..." : this.state.model.data.name.substring(0, 15)}
				</span>
			</div>
		);
	},

	renderDisableEdit() {
		return (
			<ul className="dropdown-menu dropdown-menu-level-archived">
				<li>
					<a
						className="mdi mdi-pencil disabledIcon"
						title={Messages.get("label.title.UnableEditArchivedPlan")}>
						<span id = "menu-levels">
							{Messages.getEditable("label.title.UnableEditArchivedPlan","fpdi-nav-label")}
						</span>
					</a>
		         </li>

		         <li>
					<a
						className="mdi mdi-delete disabledIcon"
						title={Messages.get("label.unableDeleteArchivedPlan")}
						type="submit">
						<span id = "menu-levels">
							{Messages.getEditable("label.unableDeleteArchivedPlan","fpdi-nav-label")}
						</span>
					</a>
		         </li>
			</ul>
		);
	},

	renderEditGoal(userResponsible) {		
		return (
			<ul id="level-menu" className="dropdown-menu" >
				{((this.context.roles.MANAGER || _.contains(this.context.permissions, 
 							PermissionsTypes.MANAGE_PLAN_PERMISSION)) && this.state.vizualization) ||
 							(userResponsible && userResponsible.id == UserSession.get("user").id &&
 							this.state.model.data.level.goal) ? (
					<li>
						<a onClick={this.editingAttributes}>
							<span className="mdi mdi-pencil cursorPointer cursorPointer" title={Messages.get("label.title.editInformation")}> 
								<span id="menu-levels"> {Messages.getEditable("label.title.editInformation","fpdi-nav-label")} </span>
							</span>
						</a>
						
			        </li>) : ""}
				
				{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) && this.state.model.data.level.name == "Meta" ? (
					<li>
						<a onClick={this.state.model.data.closed == false ? 
								this.confirmCompleteGoal.bind(this,this.state.model.data.id, true) : ((this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? this.confirmCompleteGoal.bind(this, this.state.model.data.id, false) : "") }>
							<span className={this.state.model.data.closed == false ? "mdi mdi-lock-open-outline lockGoal-open deleteIcon":"mdi  mdi-lock lockGoal-closedeleteIcon"} 
								title= {this.state.model.data.closed == false ? Messages.get("label.title.finishGoal") : ((this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? "Abrir Meta" : Messages.get("label.title.reopenGoal"))}> 
								<span id="menu-levels"> {this.state.model.data.closed == false ? Messages.get("label.msg.finishGoal"):Messages.get("label.msg.reopenGoal")} </span>
							</span>
						</a>


					</li>) : ""}
         		{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         							PermissionsTypes.MANAGE_PLAN_PERMISSION)) && this.state.vizualization ? (
			        <li>
						<a onClick={this.exportLevelAttributes}>
							<span className="mdi mdi-file-export cursorPointer" title={Messages.get("label.generateReport")}> 
								<span id="menu-levels"> {Messages.getEditable("label.generateReport","fpdi-nav-label")} </span>
							</span>
						</a>
					</li>) : ""}

		        {this.context.roles.SYSADMIN ? "" : 
			        (this.state.favoriteExistent ? (
		         		<li>
		         			<a onClick={this.removeFavoriteLevel}>
								<span className="mdi mdi-star-outline cursorPointer" title={Messages.get("label.removeFromFavorites")}> 
									<span id="menu-levels"> {Messages.getEditable("label.removeFromFavorites","fpdi-nav-label")} </span>
								</span>
							</a>
			         	</li>) 
			        	: (!this.state.favoriteTotal || this.state.favoriteTotal < 10 ?
	         		 		<li>
	         		 			<a onClick={this.saveFavoriteLevel}>
									<span className="mdi mdi-star cursorPointer" title={Messages.get("label.addFavorites")}> 
										<span id="menu-levels"> {Messages.getEditable("label.addFavorites","fpdi-nav-label")}  </span>
									</span>
								</a>
			         		</li>
		         			:
			         			<li>
					         		<a type="submit">
										<span className="mdi mdi-star disabledIcon" title={Messages.get("label.maxFavorites")}> 
											<span id="menu-levels"> {Messages.getEditable("label.notAddFavorites","fpdi-nav-label")} </span>
										</span>
									</a>
					         	</li>
		         		)
		         	)
			    }

			    {(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         							PermissionsTypes.MANAGE_PLAN_PERMISSION)) && this.state.vizualization ? (
			        (this.state.undeletable?

			         	<li>
							<a onClick={this.deleteLevelAttribute} type="submit">
								<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.delete") + " " + this.state.subTitle}> 
									<span id="menu-levels"> {Messages.getEditable("label.delete","fpdi-nav-label")} </span>
								</span>
							</a>
			         	</li>
			        : 
			        	<li>
			        		<a type="submit">
								<span className="mdi mdi-delete disabledIcon marginLeft5 cursorPointer" title={Messages.get("label.notDeleted") +(!this.state.model.data.leaf ? ", possui níveis filhos" : "")}> 
									<span id="menu-levels"> {Messages.getEditable("label.notDeleted","fpdi-nav-label")} </span>
								</span>
							</a>
			         	</li>
			        )) : ""}
			</ul>
		);
	},

	renderClosedGoal() {
	 	return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<a data-placement="top">
						<span className="mdi mdi-pencil disabledIcon marginLeft5" title={Messages.get("label.title.notEditGoalComplete")}> 
							<span id="menu-levels"> {Messages.getEditable("label.notEdit","fpdi-nav-label")} </span>
						</span>
					</a>
		         </li>

				 {(this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) && this.state.model.data.level.name == "Meta" ? (
					<li>
						<a onClick={this.state.model.data.closed == false ? 
								this.confirmCompleteGoal.bind(this,this.state.model.data.id, true) : ((this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? this.confirmCompleteGoal.bind(this, this.state.model.data.id, false) : "")}>
							<span className={this.state.model.data.closed == false ? "mdi mdi-lock-open-outline lockGoal-open":"mdi  mdi-lock lockGoal-close"}
								title= {this.state.model.data.closed == false ? Messages.get("label.title.finishGoal") : ((this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         					PermissionsTypes.MANAGE_PLAN_PERMISSION)) ? "Abrir Meta" : Messages.get("label.title.reopenGoal"))}> 
								<span id="menu-levels"> {this.state.model.data.closed == false ? Messages.getEditable("label.msg.finishGoal","fpdi-nav-label"):Messages.getEditable("label.msg.reopenGoal","fpdi-nav-label")} </span>
							</span>
						</a>

					</li>) : ""}

				{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
         							PermissionsTypes.MANAGE_PLAN_PERMISSION)) && this.state.vizualization ? (
			        <li>

			        	<a onClick={this.exportLevelAttributes}>
							<span className="mdi mdi-file-export cursorPointer" title={Messages.get("label.generateReport")}> 
								<span id="menu-levels"> {Messages.getEditable("label.generateReport","fpdi-nav-label")}</span>
							</span>
						</a>
			        </li>) : ""}

				{this.context.roles.SYSADMIN ? "" : 
			        (this.state.favoriteExistent ? (
		         		<li>
		         			<a onClick={this.removeFavoriteLevel}>
								<span className="mdi mdi-star-outline cursorPointer" title={Messages.get("label.removeFromFavorites")}> 
									<span id="menu-levels"> {Messages.getEditable("label.removeFromFavorites","fpdi-nav-label")} </span>
								</span>
							</a>
			         	</li>) 
			        	: (!this.state.favoriteTotal || this.state.favoriteTotal < 10 ?
	         		 		<li>
	         		 			<a onClick={this.saveFavoriteLevel}>
									<span className="mdi mdi-star cursorPointer" title={Messages.get("label.addFavorites")}> 
										<span id="menu-levels">	{Messages.getEditable("label.addFavorites","fpdi-nav-label")} </span>
									</span>
								</a>
			         		</li>
		         			:
			         			<li>
			         				<a type="submit">
										<span className="mdi mdi-star disabledIcon" title={Messages.get("label.maxFavorites")}> 
											<span id="menu-levels">	{Messages.getEditable("label.notAddFavorites","fpdi-nav-label")} </span>
										</span>
									</a>
					         	</li>
		         		)
		         	)
			    }

		         {this.state.undeletable ?
		         	<li>
		         		<a type="submit">
							<span className="mdi mdi-delete disabledIcon marginLeft5" title={Messages.get("label.notDeleted") + " " +(!!this.state.model.data.closed ? ", meta concluída\n" : "")}> 
								<span id="menu-levels">	{Messages.getEditable("label.notDeleted","fpdi-nav-label")} </span>
							</span>
						</a>

		         	</li>
		         : ""}

			</ul>
		);

	 },

	 renderDisableDelete() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<a>
						<span className="mdi mdi-delete disabledIcon" title={Messages.get("label.unableDeleteArchivedPlan")}> 
							<span id="menu-levels">	{Messages.getEditable("label.unableDeleteArchivedPlan","fpdi-nav-label")} </span>
						</span>
					</a>
		         </li>
			</ul>
		);
	},

	renderDeleteLevelAttributeEnable() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<a onClick={this.deleteLevelAttribute}>
						<span className="mdi mdi-delete cursorPointer deleteIcon" title={Messages.get("label.delete")}> 
							<span id="menu-levels">	{Messages.getEditable("label.delete"),"fpdi-nav-label"} </span>
						</span>
					</a>
		         </li>
			</ul>
		);

	},

	renderDeleteLevelAttributeDisable() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<a onClick={this.deleteLevelAttribute}>
						<span className="mdi mdi-delete disabledIcon marginLeft5" title={Messages.get("label.notDeleted") +(!!this.state.model.data.closed ? ", meta concluída\n" : "")}> 
							<span id="menu-levels">	{Messages.getEditable("label.notDeleted","fpdi-nav-label")} </span>
						</span>
					</a>
		         </li>
			</ul>
		);
	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		var userResponsible;
		for(var i = 0; i < this.state.fields.length; i++) {
			if(this.state.fields[i].type == AttributeTypes.RESPONSIBLE_FIELD) {
				if (this.state.fields[i].users) {
					for(var j = 0; j < this.state.fields[i].users.length;j++) {
						if(this.state.fields[i].users[j].id == this.state.fields[i].value) {
							userResponsible = this.state.fields[i].users[j];
						}
					}
				}
			}
		}
		return(
		<div> 
			<div className="row paddingLeft15">
				{this.state.model ? this.renderBreadcrumb() : ""}
					
				<div className={this.state.level == "Indicador" ? "col-lg-10 fpdi-indicatorTitle" : "col-lg-10 fpdi-attributesTitle"}>
					<h1>
						<div  className="fpdi-containerTitle" ref='levelInstanceTitle'>
							{this.state.subTitle}
						</div>	
						{this.state.vizualization ?
						<div className="fpdi-containerIcons">
							<span className="dropdown">
								<a
									className="dropdown-toggle"
									data-toggle="dropdown"
									aria-haspopup="true"
									aria-expanded="true"
									title={Messages.get("label.actions")}
								>
									<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
									<span className="mdi mdi-chevron-down" />
								</a>	
									{this.context.planMacro.attributes.archived ?  this.renderDisableEdit() : 
										(this.state.model.data.level.goal == false ? this.renderEditGoal(userResponsible) : 
				            				this.state.model.data.closed == false ? this.renderEditGoal(userResponsible) : this.renderClosedGoal()) 
				            		}  		
				            </span>
						</div>:""}
					</h1>
					{this.state.level != "Indicador" ?
						<div>
							<span className="fpdi-level-type-value">
								{this.state.level}
							</span>
						</div>
					: 
						this.state.aggregate ? 
							<div>
								<span className="fpdi-level-type-value">
									{Messages.getEditable("label.indicatorAggregate","fpdi-nav-label")}
								</span>
							</div>
						:
							<div>
								<span className="fpdi-level-type-value">
									{Messages.getEditable("label.indicatorSimple","fpdi-nav-label")}
								</span>
							</div>
					}	
				</div>
				<div className = "fpdi-levelValueBox">
					<div className = {this.state.levelValue == null || this.state.levelValue < 1000 ? "col-lg-1 box-proceeds" : "col-lg-1"} >
						<span id = "title-proceeds"> {Messages.getEditable("label.profit","fpdi-nav-label")} </span>
						<span id = "value-proceeds"> {this.state.levelValue == null ? 0 + "%" : this.state.levelValue.toFixed(2)+"%"} </span>
					</div>	
				</div>
			</div>
			
			<VerticalForm
			    ref="levelForm"
			    vizualization={this.state.vizualization}
				isPlan={true}
			    onCancel={this.onCancel}
				onSubmit={this.onSubmit}
				fields={this.state.fields}
				store={StructureStore}
				onChange={this.onChange}
				submitLabel="Salvar"
				dateBegin={dateBegin}
				dateEnd={dateEnd}
				userResponsible={userResponsible}
				levelInstanceId={this.state.model.data.id}
				levelInstanceIdActionPlan = {this.props.params.levelInstanceId}/>


			{this.state.showPolarityAlert ? <i><h5 className="fpdi-indicator-weigth-total">{Messages.getEditable("label.msg.polarityChanged")}</h5></i> : ""}
			{this.state.model.data.level.indicator ? <GoalPerformance  indicator = {this.state.model.data}/> : ""}

			<LevelSons 
				dateBegin={dateBegin} 
				dateEnd={dateEnd} 
				parentId={this.state.model.data.id} 
				isParent={this.isParent} 
				users={this.state.users} 
				enabled={!this.state.aggregate && this.state.vizualization}/>
	    </div>);
	}		
});