
import React from 'react';
import Marked from "marked";
import {Link} from 'react-router';
import _ from 'underscore';
import moment from 'moment';
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";

import Modal from "forpdi/jsx/core/widget/Modal.jsx";
//import SummaryTable from "forpdi/jsx/planning/widget/plan/SummaryTable.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import Forrisco_PolicyEdit from "forpdi/jsx_forrisco/planning/view/policy/PolicyEdit.jsx";

export default React.createClass({
	contextTypes: {
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object.isRequired,
		policy: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object.isRequired,
		toastr: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			tabPath: this.props.location.pathname,
			archived: null,
			loading: !!this.props.params.id,
			modelId: this.props.params.id,
			model: null,
			undeletable: false
		};
	},
	updateLoadingState(showName) {
		this.setState({
			loading: (this.props.params.id && !this.state.model)
		});
	},

	componentDidMount() {
		var me = this;
		//this.context.tabPanel.addTab(this.state.tabPath, this.context.planMacro.get("name"));
		/*PlanMacroStore.on("planmacroarchived", (model) => {
			if(model.status == undefined || model.status == 200){
				this.setState({
					archived: true
				});
				this.context.toastr.addAlertSuccess(Messages.get("label.success.planField"));
				me.context.router.push("/plan/"+model.data.id+"/details/");
				PlanMacroStore.dispatch({
            		action: PlanMacroStore.ACTION_FIND_UNARCHIVED
        		});
        		PlanMacroStore.dispatch({
           			action: PlanMacroStore.ACTION_FIND_ARCHIVED
        		});
			} else {
				//Toastr.error(model.responseJSON.message);
				this.context.toastr.addAlertError(model.responseJSON.message);
			}
		}, me);

		PlanMacroStore.on("planmacrounarchived", (model) => {
			if(model.status == undefined || model.status == 200){
				this.setState({
					archived: false
				});
				this.context.toastr.addAlertSuccess(Messages.get("label.success.unarchived"));
				me.context.router.push("/plan/"+model.data.id+"/details/");
				PlanMacroStore.dispatch({
            		action: PlanMacroStore.ACTION_FIND_UNARCHIVED
        		});

        		PlanMacroStore.dispatch({
           			action: PlanMacroStore.ACTION_FIND_ARCHIVED
        		});
			} else {
				//Toastr.error(model.responseJSON.message);
				this.context.toastr.addAlertError(model.responseJSON.message);
			}
		}, me);

		PlanMacroStore.on("retrieve", (model) => {
			me.setState({
				model: model,
				archived: model.attributes.archived,
				undeletable: model.attributes.haveSons
			});
			me.updateLoadingState(false);
		}, me);

		PlanMacroStore.on('plan-deleted', (response, data) => {
			me.context.toastr.addAlertSuccess(data.attributes.name + " " + Messages.get("label.successDeleted"));
			me.context.router.push("/home");
			PlanMacroStore.dispatch({
                action: PlanMacroStore.ACTION_FIND
            });
		}, me);

		if (this.props.params.id) {
			//_.defer(() => {me.context.tabPanel.addTab(me.props.location.pathname,"Editar plano macro");});
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_RETRIEVE,
				data: this.state.modelId
			});
		}
		*/
		this.context.tabPanel.addTab(this.props.location.pathname, this.context.policy.get("name"));
	},
	componentWillReceiveProps(newProps) {
		//"/forrisco/policy/1/item/overview"
		//"/forrisco/policy/1/item/overview"
		if (newProps.location.pathname != this.state.tabPath) {
			this.setState({
				tabPath: newProps.location.pathname
			});
			this.context.tabPanel.addTab(newProps.location.pathname, this.context.policy.get("name"));
		}
	},

	cancelBlockUnblock () {
		Modal.hide();
	},

	deletePolicy(event){
		event && event.preventDefault();
		var me = this;
		var msg =  Messages.get("label.deleteConfirmation")+" "+me.state.model.attributes.name+"?";

		if (me.state.model != null) {
			Modal.confirmCancelCustom(() => {
				Modal.hide();
				/*PlanMacroStore.dispatch({
					action: PlanMacroStore.ACTION_DELETE,
					data: me.state.model
				});*/
			},msg,me.cancelBlockUnblock);
		}
		me.forceUpdate();
	},

	archivePlan(event) {
		event && event.preventDefault();
		var msg = Messages.get("label.msg.filePlan");
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			/*PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_ARCHIVE,
				data: {
					id: this.context.planMacro.get("id")
				},
				wait: true
			});*/

		}, msg, this.cancelBlockUnblock);
	},

	unarchivePolicy(event) {
		event && event.preventDefault();
		var me = this;
		var msg = Messages.get("label.msg.unarchivePlan");
		Modal.confirmCancelCustom(() => {
			Modal.hide();
			/*PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_UNARCHIVE,
				data: {
					id: this.context.policy.get("id")
				},
				wait: true
			});*/
		}, msg, this.cancelBlockUnblock);
	},

	onSubmit(data) {
		/*var me = this;
		var msg = Messages.get("label.form.error");
		var dataError = false;
		var boolMsg = false;
		var difference = 0;
		var valDateBegin,valDateFinal;

		var begin = this.refs.planMacroEditForm.refs["begin"].props.fieldDef.value.split(" ");
		begin = moment(begin,"DD/MM/YYYY").toDate();
		if(begin== null){
			this.refs.planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}else{
			data.begin = begin.getDate()+"/"+(begin.getMonth()+1)+"/"+begin.getFullYear();
			if(this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className && this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className.indexOf('borderError')){
				this.refs.planMacroEditForm.refs.begin.refs["field-begin"].refs.input.refs.input.className = "form-control";
				this.refs.planMacroEditForm.refs.begin.refs.formAlertError.innerHTML = "";
			}

		}

		var end = this.refs.planMacroEditForm.refs["end"].props.fieldDef.value.split(" ");
		end = moment(end,"DD/MM/YYYY").toDate();
		if(end== null){
			this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			dataError = true;
			boolMsg = true;
		}else{
			if(this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className && this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className.indexOf('borderError')){
				this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className = "form-control";
				this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = "";
			}
			data.end = end.getDate()+"/"+(end.getMonth()+1)+"/"+end.getFullYear();

		}

		if (!dataError) {
			valDateBegin = begin.getTime();
			valDateFinal = end.getTime();
			difference = valDateFinal - valDateBegin;
		}
		if (!dataError && begin.getTime() == end.getTime()) {
			this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Messages.get("label.endDateMustBeAfterBeginDate");
			this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		} else if (!dataError && difference < 86400000) {
			this.refs.planMacroEditForm.refs.end.refs.formAlertError.innerHTML = Message.get("label.endDateMustBeAfterBeginDate");
			this.refs.planMacroEditForm.refs.end.refs["field-end"].refs.input.refs.input.className += " borderError";
			boolMsg = true;
		}
		if(data.name == "" ||  !!data.name.match(/^(\s)+$/) ){
			boolMsg = true;
			this.refs.planMacroEditForm.refs.name.refs.formAlertError.innerHTML = Messages.get("label.alert.fieldEmpty");
			this.refs.planMacroEditForm.refs.name.refs["field-name"].className += " borderError";
		}else{
			if(this.refs.planMacroEditForm.refs.name.refs["field-name"].className && this.refs.planMacroEditForm.refs.name.refs["field-name"].className.indexOf('borderError')){
				this.refs.planMacroEditForm.refs.name.refs["field-name"].className = "form-control";
				this.refs.planMacroEditForm.refs.name.refs.formAlertError.innerHTML = "";
			}
		}

		if(boolMsg){
			this.context.toastr.addAlertError(msg);
			return;
		}

		if (me.props.params.id) {
			me.state.model.set(data);
			msg = Messages.get("label.msgUpdate");
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_UPDATE,
				data: me.state.model
			});
		} else {
			msg = Messages.get("label.msg.planMacroCreate");
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_NEWPLAN,
				data: data,
				opts: {
					wait: true
				}
			});
		}*/
	},
	renderUnarchivePolicy() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<a
						data-placement="bottom"
						onClick={this.unarchivePolicy}>
						<span className="mdi mdi-folder-upload mdi-18" title={"label.unarchivePolicy"} >
							<span id = "menu-levels">
								{Messages.getEditable("label.unarchivePolicy","fpdi-nav-label")}
							</span>
						</span>
					</a>
		         </li>
			</ul>
		);
	},

	renderArchivePolicy() {
		return (
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link to={"/plan/"+this.context.policy.get("id")+"/edit"} data-placement="bottom">
						<span className="mdi mdi-pencil mdi-18" title={Messages.get("label.editPlan")}>
							<span id="menu-levels">{Messages.getEditable("label.editPlan","fpdi-nav-label")}</span>
						</span>
					</Link>
				</li>
				<li>
					<Link
						to={"/plan/"+this.context.policy.get("id")+"/details/duplicate"}
						onClick={this.changeVizualization}
						data-placement="bottom">
						<span className="mdi mdi-content-copy mdi-18" title={Messages.get("label.duplicatePlan")}>
							<span id="menu-levels"> {Messages.getEditable("label.duplicatePlan","fpdi-nav-label")}</span>
						</span>
					</Link>
				</li>
				<li>
					<a onClick={this.archivePlan} data-placement="bottom">
						<span className="mdi mdi-folder-download mdi-18 deleteIcon" title={Messages.get("label.archivePlan")}>
							<span id="menu-levels"> {Messages.getEditable("label.archivePlan","fpdi-nav-label")}</span>
						</span>
					</a>
				</li>
				{this.state.undeletable ?
					<li>
						<a data-placement="bottom">
							<span className="mdi mdi-delete disabledIcon mdi-18" title={Messages.get("label.notDeleteChildLevels")}>
								<span id="menu-levels"> {Messages.getEditable("label.deletePlan","fpdi-nav-label")}</span>
							</span>
						</a>
					</li>
					:
					<li>
						<a onClick={this.deletePolicy} data-placement="bottom">
							<span className="mdi mdi-delete mdi-18 deleteIcon" title={Messages.get("label.deletePlan")}>
								<span id="menu-levels"> {Messages.getEditable("label.deletePlan","fpdi-nav-label")} </span>
							</span>
						</a>
					</li>
				}


			</ul>
		);

	},

	render() {

		return <div>
			<div className="media-list">
				<div className="media-header">
					<h1>{this.context.policy.get("name").length <= 24?this.context.policy.get("name"):this.context.policy.get("name").split("",20).concat(" ...")}&nbsp;

					{(this.context.roles.ADMIN || _.contains(this.context.permissions,PermissionsTypes.MANAGE_POLICY_PERMISSION))
					&& !this.context.router.isActive("forrisco/policy/"+this.props.params.policyId+"/edit") ?
						(<span className="dropdown">
							<a className="dropdown-toggle"
								data-toggle="dropdown"
								aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}
							>
								<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
								<span className="mdi mdi-chevron-down" />
							</a>

							{this.state.archived ? this.renderUnarchivePolicy() : this.renderArchivePolicy()}
						</span>

						):""}
					</h1>
				</div>

				{!this.context.router.isActive("forrisco/policy/"+this.props.params.policyId+"/edit") ?
					<div className="media-body">
						{this.state.archived ? <span className="fpdi-archived-label">{Messages.getEditable("label.planField","fpdi-nav-label")}</span> : ""}
						<div className="markdown-container" dangerouslySetInnerHTML={{__html: Marked(this.context.policy.get("description"))}} />
					</div>
				: <Forrisco_PolicyEdit
						params={{policyId:this.props.params.policyId}}
				/>
				}

			{//<SummaryTable policy={this.context.policy} />
			}
		</div>
		</div>
	}
});
