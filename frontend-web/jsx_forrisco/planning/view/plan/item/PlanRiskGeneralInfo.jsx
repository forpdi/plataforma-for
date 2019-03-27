import React from "react";
import _ from "underscore";
import {Link} from "react-router";

import Messages from "@/core/util/Messages";
import Modal from "@/core/widget/Modal";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		planRisk: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			planRisk: [],
			fields: [],
			title: '',
			description: '',
			policy: '',
			isLoading: true
		}
	},

	componentDidMount() {
		PlanRiskStore.on('retrivedplanrisk', response => {
			this.setState({
				title: response.attributes.name,
				description: response.attributes.description,
				policy: response.attributes.policy.name,
				isLoading: false
			});

			//Construção da Aba Superior
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname, this.state.title);
			});
		}, this);

		PlanRiskStore.on('deletePlanRisk', response => {
			if(response.success === true) {
				this.context.toastr.addAlertSuccess('Plano de Risco removido com sucesso');
				this.context.router.push("forrisco/home/");
				PlanRiskStore.dispatch({
					action: PlanRiskStore.ACTION_FIND_UNARCHIVED_FOR_MENU
				});
			}

			if(response.success === false) {
				this.context.toastr.addAlertError('O plano possui unidades e riscos que não podem ser deletados.');
			}
		});

		this.refreshData(this.props.params.planRiskId);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.planRiskId !== this.props.params.planRiskId) {
			this.refreshData(newProps.params.planRiskId);
		}
	},

	refreshData(planRiskId) {
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: planRiskId
		});
	},

	componentWillUnmount() {
		PlanRiskStore.off(null, null, this);
	},

	deletePlanRisk() {
		var me = this;
		var msg = "Você tem certeza que deseja excluir esse plano de risco?";

		Modal.confirmCustom(() => {
			Modal.hide();
			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_DELETE_PLANRISK,
				data:this.props.params.planRiskId
			});
		}, msg, me.refreshCancel);

	},

	refreshCancel () {
		Modal.hide();
	},

	renderDropdown() {
		return(
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link to={"/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/" + this.props.params.planRiskId + "/edit"}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPolicy")}>
							<span id="menu-levels"> Editar Plano </span>
						</span>
					</Link>
				</li>
				<li>
					<Link to={"/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/duplicate"}>
					<span className="mdi mdi-content-copy cursorPointer" title={Messages.get("label.deletePolicy")}>
						<span id="menu-levels"> Duplicar Plano </span>
					</span>
					</Link>
				</li>
				<li>
					<Link onClick={this.deletePlanRisk}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
						<span id="menu-levels"> Excluir Plano </span>
					</span>
					</Link>
				</li>
			</ul>
		)
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		return (
			<div>
				<div className="fpdi-card fpdi-card-full floatLeft">
					<h1>
						{this.state.title}
						{
							(this.context.roles.ADMIN ||
								_.contains(this.context.permissions, PermissionsTypes.FORRISCO_MANAGE_PLAN_RISK_PERMISSION))
							&&
							<span className="dropdown">
								<a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}>

									<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
									<span className="mdi mdi-chevron-down" />

								</a>

								{this.renderDropdown()}
							</span>
						}
					</h1>
					{
						//this.state.description ?
							<div >
								<h3 className="fpdi-text-label">{"DESCRIÇÃO"}</h3>
								<br/>
								<span className="pdi-normal-text"><p>{this.state.description}</p></span>
								<br/>
								<h3 className="fpdi-text-label">{"POLÍTICA VINCULADA"} </h3>
								<br/>
								<span className="pdi-normal-text"><p>{this.state.policy}</p></span>
								<br/>
							</div>
						//	: ""
					}
				</div>
			</div>
		)
	}
})
