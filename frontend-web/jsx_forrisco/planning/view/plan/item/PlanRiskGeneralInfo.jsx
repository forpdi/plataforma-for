import React from "react";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import _ from "underscore";
import {Link} from "react-router";
import Messages from "@/core/util/Messages";
import Modal from "@/core/widget/Modal";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem";

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
		}, this);
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

		PlanRiskStore.on('deletePlanRisk', response => {
			if(response.success === true) {
				this.context.router.push("forrisco/home/");
			}
		})
	},

	refreshCancel () {
		Modal.hide();
	},

	renderDropdown() {
		return(
			<ul id="level-menu" className="dropdown-menu">
				<li>
					<Link to={"/forrisco/plan-risk/" + this.props.params.planRiskId + "/item/" + this.props.params.itemId + "/edit"}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPolicy")}>
							<span id="menu-levels"> Editar Política </span>
						</span>
					</Link>
				</li>
				<li>
					<Link onClick={this.deletePlanRisk}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
						<span id="menu-levels"> Deletar Política </span>
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
						<span className="dropdown">
							<a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
							   aria-expanded="true"
							   title={Messages.get("label.actions")}>

								<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
								<span className="mdi mdi-chevron-down" />

							</a>

							{this.renderDropdown()}
						</span>
					</h1>
					{
						this.state.description ?
							<div>
								<h3>{"DESCRIÇÃO"}</h3>
								{this.state.description}
								<br/>
								<h3>{"POLÍTICA VINCULADA"} </h3>
								{this.state.policy}
							</div>
							: ""
					}
				</div>
			</div>
		)
	}
})
