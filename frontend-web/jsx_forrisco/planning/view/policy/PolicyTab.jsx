
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

		this.context.tabPanel.addTab(this.props.location.pathname, this.context.policy.name);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.location.pathname != this.state.tabPath) {
			this.setState({
				tabPath: newProps.location.pathname
			});
			this.context.tabPanel.addTab(newProps.location.pathname, this.context.policy.name);
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
			},msg,me.cancelBlockUnblock);
		}
		me.forceUpdate();
	},

	archivePlan(event) {
		event && event.preventDefault();
		var msg = Messages.get("label.msg.filePlan");
		Modal.confirmCancelCustom(() => {
			Modal.hide();

		}, msg, this.cancelBlockUnblock);
	},

	unarchivePolicy(event) {
		event && event.preventDefault();
		var me = this;
		var msg = Messages.get("label.msg.unarchivePlan");
		Modal.confirmCancelCustom(() => {
			Modal.hide();
		}, msg, this.cancelBlockUnblock);
	},

	onSubmit(data) {

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
					<h1>{this.context.policy.name.length <= 24?this.context.policy.name:this.context.policy.name.split("",20).concat(" ...")}&nbsp;

					{/*(this.context.roles.ADMIN || _.contains(this.context.permissions,PermissionsTypes.MANAGE_POLICY_PERMISSION))

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

						):""*/}
					</h1>
				</div>

				{!this.context.router.isActive("forrisco/policy/"+this.props.params.policyId+"/edit") ?
					<div className="media-body">
						{this.state.archived ? <span className="fpdi-archived-label">{Messages.getEditable("label.planField","fpdi-nav-label")}</span> : ""}
						<div className="markdown-container" dangerouslySetInnerHTML={{__html: Marked(this.context.policy.description)}} />
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
