
//import Toastr from 'toastr';
import React from "react";
import {Link, hashHistory} from "react-router";
import AttributeInput from "forpdi/jsx/planning/widget/attributeForm/AttributeInput.jsx";
import BudgetField from "forpdi/jsx/planning/view/field/BudgetField.jsx";
import AttachmentField from "forpdi/jsx/planning/view/field/AttachmentField.jsx";
import ActionPlanField from "forpdi/jsx/planning/view/field/ActionPlanField.jsx";
import ScheduleField from "forpdi/jsx/planning/view/field/ScheduleField.jsx";
import TableField from "forpdi/jsx/planning/view/field/TableField.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import Messages from "forpdi/jsx/core/util/Messages.jsx";


var Title = Validation.validate;


var VerticalForm =  React.createClass({
	contextTypes: {
		toastr: React.PropTypes.object.isRequired
	},
	getDefaultProps() {
		return {
			fields: [],
			onCancel: null,
			onSubmit: null,
			store: null,
			hideCanel: false,
			cancelUrl: null,
			cancelLabel: "Cancelar",
			submitLabel: "Enviar",
			blockButtons: false,
			fieldErrors:[],
			undeletable: false,
			alterable: false,
			onChage: null,
			dateBegin: null,
			dateEnd: null
		};
	},
	getInitialState() {
		return {
			error: false,
			errorMessage: ""
		};
	},
	cancelWrapper(evt) {
		for (var i = 0; i < this.props.fields.length; i++) {
			if (this.refs[this.props.fields[i].name])
				this.refs[this.props.fields[i].name].refs.formAlertError.innerHTML = "";
		}
		evt.preventDefault();
		if (typeof this.props.onCancel === 'function') {
			this.props.onCancel();
		} else {
			this.backWrapper();
		}
	},
	submitWrapper(evt) {
		evt.preventDefault();
		var error = false;
		if (!this.props.onSubmit)
			console.warn("AttributeForm: You must pass your own onSubmit callback.");
		else {
			this.props.fields.map((field,idx) => {		
				if (field.type != AttributeTypes.BUDGET_FIELD
					&& field.type != AttributeTypes.ACTION_PLAN_FIELD
					&& field.type != AttributeTypes.SCHEDULE_FIELD
					&& field.type != AttributeTypes.TABLE_FIELD) {
						if(this.props.isDocument) {
							if(!Title.validateTitle(this.refs[field.name], idx, this.props.editFunc)){
								this.context.toastr.addAlertError("Existem erros no formulário");
								error = true;
							}
						} 
					}
			});

			if (!error) {
				this.props.onSubmit(this.getValues());
			}
		}
	},
	getValues() {
		var data = {};
		this.props.fields.forEach(field => {			
			 if(field.type != AttributeTypes.BUDGET_FIELD &&
				field.type != AttributeTypes.ACTION_PLAN_FIELD &&
				field.type != AttributeTypes.SCHEDULE_FIELD &&
				field.type != AttributeTypes.TABLE_FIELD &&
				field.type != AttributeTypes.ATTACHMENT_FIELD) {
					data[field.name] = this.refs[field.name].getValue();
					if(!data[field.name]) {
						data[field.name] = this.refs[field.name].props.fieldDef.value;
					}
			}
		});

		return data;
	},
	backWrapper() {
		hashHistory.goBack();
	},
	componentDidMount() {
		if (this.props.store) {
			this.props.store.on("invalid", this.handleValidation, this);
			this.props.store.on("fail", this.handleFailure, this);
		}
	},
	componentDidUpdate() {
		var fieldName;
		var me = this;
		if(this.props.vizualization) {
			this.props.fields.map((field,idx) => {	
				fieldName = field.name;
				if (me.refs[fieldName] != undefined)
					me.refs[fieldName].refs.formAlertError.innerHTML = "";
			})
		}
	},
	componentWillUnmount() {
		if (this.props.store) {
			this.props.store.off("invalid", this.handleValidation, this);
			this.props.store.off("fail", this.handleFailure, this);
		}
	},
	handleValidation(model, errors, opts) {
		this.setState({
			error: true,
			errorMessage: errors
		});
	},
	handleFailure(errors) {
		//Toastr.remove();
		if (typeof this.state.errorMessage == 'string') {
			//Toastr.error(errors);
			this.context.toastr.addAlertError(errors);
		} else if (typeof this.state.errorMessage == 'object') {
			var msg = "<ul>";
			_.each(errors, (err) => {
				msg += "<li>"+err+"</li>";
			});
			msg += "</ul>";
			//Toastr.error(msg);
			this.context.toastr.addAlertError(msg);
		} else {
			//Toastr.error("Um erro inesperado ocorreu.");
			this.context.toastr.addAlertError("Um erro inesperado ocorreu.");
		}
		/*this.setState({
			error: true,
			errorMessage: errors
		});*/
	},
	closeAlerts() {
		this.setState({
			error: false
		});
	},
	render() {
		var alerts = null;
		var showButtons = !this.props.vizualization;
		var requiredFields = false;
		if (this.state.error) {
			if (typeof this.state.errorMessage == 'string') {
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<span className="close mdi mdi-close" aria-label="Fechar Alerta" onClick={this.closeAlerts} />
					{this.state.errorMessage}
				</div>);
			} else if (typeof this.state.errorMessage == 'object') {
				var errNo = 0;
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<span className="close mdi mdi-close" aria-label="Fechar Alerta" onClick={this.closeAlerts} />
					{this.state.errorMessage.map(err => {
						return <li key={"errmsg-"+(errNo++)}>{err}</li>;
					})}
				</div>);
			} else {
				alerts = (<div className="alert alert-danger animated fadeIn" role="alert">
					<span className="close mdi mdi-close" aria-label="Fechar Alerta" onClick={this.closeAlerts} />
					An unexpected error occurred.
				</div>);
			}
		}
				
		return (<form onSubmit={this.submitWrapper}>
			{this.props.fields.map((field,idx) => {		
				if (field.type != AttributeTypes.BUDGET_FIELD
					&& field.type != AttributeTypes.ACTION_PLAN_FIELD
					&& field.type != AttributeTypes.SCHEDULE_FIELD
					&& field.type != AttributeTypes.TABLE_FIELD
					&& field.type != AttributeTypes.ATTACHMENT_FIELD) {
						showButtons = !this.props.vizualization;						
						return (<AttributeInput
							id={field.id}
							index={idx}
							fieldDef={field}
							ref={field.name}
							key={field.name}
							undeletable={this.props.undeletable}
							deleteFunc={this.props.deleteFunc}
							editFunc={this.props.editFunc}
							alterable={this.props.alterable}
							vizualization={this.props.vizualization}
							isDocument={this.props.isDocument}
							onClick={this.props.onClick}
							onChage={this.props.onChage}
							/>
						);
				}
				if (field.required)
					requiredFields = true;
			})}
			{showButtons && requiredFields ? 
				<p className="help-block">
					<span className="fpdi-required" /> {Messages.get("label.requiredFields")}
				</p>
			: ""}
			{alerts}
			{showButtons ?
				(!!this.props.blockButtons ?
					(<div className="form-group">
						<button type="submit" className="btn btn-success btn-block">{this.props.submitLabel}</button>
						{!this.props.hideCanel ? (!this.props.cancelUrl ?
							<button className="btn btn-default  btn-block" onClick={this.cancelWrapper}>{this.props.cancelLabel}</button>
							:(
								<Link to={this.props.cancelUrl} className="btn btn-default btn-block">{this.props.cancelLabel}</Link>
							)):""}
					</div>)
				:
					(<div className="form-group text-left">
						<button type="submit" className="btn btn-sm btn-success">{this.props.submitLabel}</button>
						{!this.props.hideCanel ? (!this.props.cancelUrl ?
							<button className="btn btn-sm btn-default" onClick={this.cancelWrapper}>{this.props.cancelLabel}</button>
							:
							<Link className="btn btn-sm btn-default" to={this.props.cancelUrl}>{this.props.cancelLabel}</Link>
						):""}
					</div>)
				)
			: ""}
			{this.props.fields.map((field,idx) => {
				if(field.type == AttributeTypes.BUDGET_FIELD){
					return(<BudgetField key={field.name} data={field.budgets} newFunc={field.extra}/>);
				} else if(field.type == AttributeTypes.ACTION_PLAN_FIELD){
					return(<ActionPlanField key={field.name} responsible={this.props.userResponsible}
						newFunc={field.extraActionPlans} dateBegin={this.props.dateBegin} dateEnd={this.props.dateEnd} levelInstanceId = {this.props.levelInstanceIdActionPlan}/>);
				} else if(field.type == AttributeTypes.SCHEDULE_FIELD){
					return(<ScheduleField key={field.name} data={field.schedule} newFunc={field.extraSchedule} title={field.label}
						editFunc={this.props.editFunc} index={idx} isDocument={this.props.isDocument}/>);
				} else if(field.type == AttributeTypes.TABLE_FIELD){
					return(<TableField key={field.name} data={field.tableFields} newFunc={field.extraTableFields} title={field.label}
						editFunc={this.props.editFunc} index={idx} isDocument={this.props.isDocument} deleteFunc={this.props.deleteFunc}/>);
				} else if(field.type == AttributeTypes.ATTACHMENT_FIELD){
					return(<AttachmentField key={field.name} responsible={this.props.userResponsible} 
						levelInstanceId={this.props.levelInstanceId}/>);
				}
			})}
		</form>);
	}
});

export default {
	VerticalForm: VerticalForm
};
