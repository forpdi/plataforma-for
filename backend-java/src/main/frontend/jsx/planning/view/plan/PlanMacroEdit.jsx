
import React from "react";
import {Link} from 'react-router';

import _ from 'underscore';
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx"

import Form from "forpdi/jsx/core/widget/form/Form.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

import Messages from "forpdi/jsx/core/util/Messages.jsx";

import Validation from 'forpdi/jsx/core/util/Validation.jsx';
import moment from 'moment';
//import Toastr from 'toastr';

var VerticalForm = Form.VerticalForm;
var Validate = Validation.validate;

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		tabPanel: React.PropTypes.object,
		roles: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired
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
			maxLength:240,
			placeholder: "",
			label: Messages.get("label.name"),
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
			maxLength:9900,
			label: Messages.get("label.descriptionPlan"),
			value: model ? model.get("description"):null

		},{
			name: "documented",
			type: "checkbox",
			placeholder: "",
			required:true,
			label: Messages.get("label.useDocumentPdi"),
			value: model ? model.get("documented") : true
		}
		/* IMPORTAR ESTRUTURA DO PLANO NÃO ESTÁ IMPLEMENTADO
		,{
			name: "document",
			type: "upload",
			required: true,
			maxLength:255,
			placeholder: "Importar estrutura",
			label: "documento",
			value: null,
			extraFunction: this.importStructure

		}*/]; 
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

	importStructure(evt) {
		var me = this;
		evt.preventDefault();
		Modal.uploadFile(
			Messages.get("label.importEstructure"),
			<p>{Messages.get("label.uploadXml")}</p>,
			StructureStore.url+"/import",
			"text/xml",
			(response) => {				
				//console.log(response);
			},
			null,
			"xml."
		);
	},

	updateLoadingState() {
		this.setState({
			fields: this.getFields(this.state.model),
			loading: (this.props.params.id && !this.state.model)
		});
	},
	componentDidMount() {
		var me = this;
		// if (!me.context.roles.ADMIN) {
		// 	me.context.router.replace("/home");
		// 	return;
		// }
		PlanMacroStore.on("sync", (model) => {
			//Toastr.remove();
			if(this.props.params.id){
				//Toastr.success("Plano editado com sucesso");
				this.context.toastr.addAlertSuccess(Messages.get("label.success.planEdit"));
				me.context.router.push("/plan/"+model.get("id")+"/details/");
			}
			else{
			    //Toastr.success("Plano criado com sucesso");

				PlanMacroStore.dispatch({
            		action: PlanMacroStore.ACTION_FIND_UNARCHIVED
        		});

        		PlanMacroStore.dispatch({
           			action: PlanMacroStore.ACTION_FIND_ARCHIVED
        		});
 
				this.context.toastr.addAlertSuccess(Messages.get("label.success.planCreate"));
				me.context.router.push("/plan/"+model.get("id")+"/document/");
			}
		}, me);
		PlanMacroStore.on("retrieve", (model) => {
			me.setState({
				model: model
			});
			me.updateLoadingState();
		}, me);

		PlanMacroStore.on("planmacrocreated", (model) => {
            PlanMacroStore.dispatch({
                action: PlanMacroStore.ACTION_FIND_UNARCHIVED
            });

            PlanMacroStore.dispatch({
                action: PlanMacroStore.ACTION_FIND_ARCHIVED
            });
            this.context.toastr.addAlertSuccess(Messages.get("label.success.planCreate"));
            me.context.router.push("/plan/"+model.data.id+"/details/");
        }, me);

		if (this.props.params.id) {
			_.defer(() => {me.context.tabPanel.addTab(me.props.location.pathname,Messages.get("label.editPlanMacro"));});
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
	},
	componentWillUnmount() {
		PlanMacroStore.off(null, null, this);
	},
	
	refreshCancel () {
		Modal.hide();
		//this.setState({
		//	editUser: false
		//	
		//});
	},

	onSubmit(data) {
		var me = this;

		var validation = Validate.validationPlanMacroEdit(data, this.refs.planMacroEditForm);

		if(validation.boolMsg){
			//Toastr.remove();
			//Toastr.error(msg);
			this.context.toastr.addAlertError(validation.msg);
			return;
		}

		if (me.props.params.id) {
			var oldBeginDate = moment(me.state.model.attributes.begin,"DD/MM/YYYY").toDate(); // data início que estava no banco
			var oldEndDate = moment(me.state.model.attributes.end,"DD/MM/YYYY").toDate(); // data fim que estava no banco
			
			var newBeginDate = moment(data.begin,"D/M/YYYY").toDate(); // data início nova
			var newEndDate = moment(data.end,"D/M/YYYY").toDate(); // data fim nova
			var msg = "";
			if(oldEndDate > newEndDate || oldEndDate < newEndDate || oldBeginDate > newBeginDate || oldBeginDate < newBeginDate){ // se houver alguma alteração de datas
				msg = Messages.get("label.msg.changeDate");
			}else{
				msg = Messages.get("label.msgUpdate");
			}

			
			Modal.confirmCustom(() => {
				me.state.model.set(data);
				Modal.hide();
				PlanMacroStore.dispatch({
					action: PlanMacroStore.ACTION_UPDATE,
					data: me.state.model
				});
			},msg,this.refreshCancel);
		} else {
			var msg = Messages.get("label.msg.planMacroCreate");
			Modal.confirmCustom(() => {
				Modal.hide();
				PlanMacroStore.dispatch({
					action: PlanMacroStore.ACTION_NEWPLAN,
					data: data,
					opts: {
						wait: true
					}
				});
			},msg,this.refreshCancel);
		}

		/*if (me.props.params.id) {
			me.state.model.set(data);
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_UPDATE,
				data: me.state.model
			});
		} else {
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_CREATE,
				data: data,
				opts: {
					wait: true
				}
			});
		}*/
	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return <div>
				<h1 className="marginLeft115">{this.props.params.id ? Messages.get("label.editPlan"):Messages.get("label.newPlan")}</h1>
				<div className="fpdi-card padding40">
					
					<VerticalForm
					    ref="planMacroEditForm"
						onSubmit={this.onSubmit}
						fields={this.state.fields}
						store={PlanMacroStore}
						submitLabel={Messages.get("label.submitLabel")}
					/>
				</div>
			</div>;
	}
});
