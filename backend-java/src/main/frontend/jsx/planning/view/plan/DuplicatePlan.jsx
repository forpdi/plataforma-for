
import React from "react";
import {Link} from 'react-router';

import _ from 'underscore';
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx"

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
//import Toastr from 'toastr';

var VerticalForm = Form.VerticalForm;
var Validate = Validation.validate;

export default React.createClass({
	contextTypes: {
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		tabPanel: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		planMacro: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			loading: !!this.props.params.id,
			modelId: this.props.params.id,
			model: null,
			fields: this.getFields()
		};
	},
	getFields(model) {
		return [{
			name: "name",
			type: "text",
			required: true,
			maxLength:255,
			placeholder: "",
			label: Messages.getEditable("label.name","fpdi-nav-label"),
			value: model ? model.get("name"):null
		},{
			name: "begin",
			type: "date",
			required: true,
			placeholder: "",
			label: Messages.get("label.dateBegin"),
			onChange:this.onStartDateChange,
			value: model ? model.get("begin"):null
		},{
			name: "end",
			type: "date",
			required: true,
			placeholder: "",
			label: Messages.get("label.dateEnd"),
			onChange:this.onEndDateChange,
			value: model ? model.get("end"):null
		},{
			name: "description",
			type: "textarea",
			placeholder: "",
			maxLength:10000,
			label: Messages.getEditable("label.descriptionPlan","fpdi-nav-label"),
			value: model ? model.get("description"):null

		}]; 
	},
	onStartDateChange(data){
		var model = this.state.model;
		this.setState({
			model:model,
		});
		this.refs.planMacroEditForm.refs.begin.props.fieldDef.value = data.format('DD/MM/YYYY');
	},
	onEndDateChange(data){
		var model = this.state.model;
		this.setState({
			model:model,
		});
		this.refs.planMacroEditForm.refs.end.props.fieldDef.value = data.format('DD/MM/YYYY');
	},

	updateLoadingState() {
		this.setState({
			fields: this.getFields(this.state.model),
			loading: (this.props.params.id && !this.state.model)
		});
	},

	componentDidMount() {
		var me = this;
		if (!me.context.roles.MANAGER && this.context.permissions.indexOf(PermissionsTypes.MANAGE_PLAN_MACRO_PERMISSION) < 0 ){
			me.context.router.replace("/home");
			return;
		}
		
		PlanMacroStore.on("planmacroduplicated", (model) => {			
			this.setState({
				loading: false
			});
			//Toastr.remove();
			if(model.status == undefined || model.status == 200){
				//Toastr.success("Plano duplicado com sucesso");
				this.context.toastr.addAlertSuccess(Messages.get("label.success.planDuplicate"));
				me.close();
				me.context.router.push("/plan/"+model.data.id+"/details/");
				PlanMacroStore.dispatch({
            		action: PlanMacroStore.ACTION_FIND
        		});
        		PlanMacroStore.dispatch({
                     action: PlanMacroStore.ACTION_FIND_ARCHIVED
                });   
                PlanMacroStore.dispatch({
                     action: PlanMacroStore.ACTION_FIND_UNARCHIVED
                });   
			} else {
				//Toastr.error(model.responseJSON.message);
				this.context.toastr.addAlertError(model.responseJSON.message);
			}
		}, me);
		PlanMacroStore.on("retrieve", (model) => {
			me.setState({
				model: model
			});
			me.updateLoadingState();
		}, me);
		if (this.props.params.id) {
			_.defer(() => {me.context.tabPanel.addTab(me.props.location.pathname,Messages.get("label.title.duplicatePlanMacro"));});
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
	},
	componentWillUnmount() {
		PlanMacroStore.off(null, null, this);
	},

	close(){
		this.context.tabPanel.removeTabByPath(this.props.location.pathname);
	},
	
	onSubmit(data) {
		var me = this;

		var validation = Validate.validationDuplicatePlan(data, this.refs.planMacroEditForm);

		if(validation.boolMsg){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(validation.msg);
			return;
		}
		data.id = this.props.params.id;
		me.state.model.set(data);
		this.setState({
			loading: true
		});
		PlanMacroStore.dispatch({
			action: PlanMacroStore.ACTION_DUPLICATE,
			data: {
				macro: data,
				keepPlanLevel: this.refs["keeplevels"].checked,
				keepPlanContent: this.refs["keeplevelscontent"].checked,
				keepDocSection: this.context.planMacro.get('documented'), // futuramente ao ser implementada a customização do documento, parametrizar normalmente
				keepDocContent: (this.context.planMacro.get('documented') ? this.refs["keepsectioncontent"].checked : false)
			}
		});
		
	},

	keepLevels(){		
		this.refs["keeplevelscontent"].disabled = !this.refs["keeplevels"].checked;
		this.refs["keeplevelscontent"].checked = (this.refs["keeplevels"].checked ? this.refs["keeplevelscontent"].checked : false);
	},


	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		
		return (
			<div className="fpdi-duplicate-plan">
				<h1>{Messages.getEditable("label.duplicatePlan","fpdi-nav-label")}</h1>
				<p>{Messages.getEditable("label.planDuplicate","fpdi-nav-label")} <b>{this.state.model.get("name")}</b></p>
				<div className="marginTop40">
					<h4 className="fpdi-text-label">{Messages.get("label.goalPlans")}</h4>
					<div className="row">
						<label className="col-sm-3">
							<input type="checkbox" defaultValue={true} defaultChecked={true} ref="keeplevels" onChange={this.keepLevels}/>
							<b className="fpdi-duplicate-checkbox-label">{Messages.getEditable("label.keepLevels","fpdi-nav-label")}</b>							
						</label>
						<label className="col-sm-3">
							<input type="checkbox" defaultValue={false} ref="keeplevelscontent"/>
							<b className="fpdi-duplicate-checkbox-label">{Messages.getEditable("label.keepContent","fpdi-nav-label")}</b>							
						</label>
					</div>
				</div>				
				<div className="marginBottom20">
				{this.context.planMacro.get('documented') ? 
					<div>
						<h4 className="fpdi-text-label">{Messages.getEditable("label.document","fpdi-nav-label")}</h4>
						<div className="row">
							<label className="col-sm-3">
								<input type="checkbox" defaultValue={false} ref="keepsectioncontent"/>
								<b className="fpdi-duplicate-checkbox-label">{Messages.getEditable("label.keepContent","fpdi-nav-label")}</b>							
							</label>
						</div>
					</div>: undefined}
				</div>
				
				<VerticalForm
				    ref="planMacroEditForm"
					onSubmit={this.onSubmit}
					fields={this.state.fields}
					store={PlanMacroStore}
					submitLabel={Messages.get("label.duplicate")}
					onCancel={this.close}
				/>
			</div>
		);
	}
});
