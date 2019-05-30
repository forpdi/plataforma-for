
import React from "react";
import Progress from 'react-progressbar';
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import LoadingGaugeWhite from "forpdi/jsx/core/widget/LoadingGaugeWhite.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({

	getInitialState() {
		return {
			plan: this.props.plan,
			subplan: this.props.subplan,
			loading: true
		};
	},

	componentWillReceiveProps(newProps) {
		var me = this;

		if (this.props.plan.id != newProps.plan.id || this.props.subPlan.id != newProps.subPlan.id) {
			DashboardStore.dispatch({
				action: DashboardStore.ACTION_GET_PLAN_DETAILS,
				data: {
					plan: newProps.subPlan.id,
					macro: newProps.plan.id
				}
			});
			me.setState({
				plan: newProps.plan,
				subplan: newProps.subPlan,
				loading: true
			});
		}
	},

	componentDidMount() {
		var me = this;

		DashboardStore.on("planDetailsRetrieved", (store) => {
			me.setState({
				planDetails: store.data,
				loading: false
			});
		}, this);
		this.refreshComponent(this.props.plan.id, this.props.subPlan.id)
	},

	refreshComponent(planId, subPlanId) {
		DashboardStore.dispatch({
			action: DashboardStore.ACTION_GET_PLAN_DETAILS,
			data: {
				plan: subPlanId,
				macro: planId
			}
		});
	},

	hideFields() {
		this.setState({
			hide: !this.state.hide
		})
	},


	componentWillUnmount() {
		DashboardStore.off(null, null, this);
	},


	render() {
		var title = Messages.get("label.generalGoalInfo") + (this.state.plan != -1 ? " - " + this.state.plan.get("name") : "");
		return (
			<div className={this.props.className}>
				<div className="panel">
					<div className="dashboard-plan-details-header">
						<span title={title}>{(this.state.plan == -1) ? (Messages.get("label.allPlans")) :
							((this.state.plan != -1 ? (this.state.plan.get("name").length > 30 ?
									this.state.plan.get("name").substr(0, 30).concat("...") : this.state.plan.get("name")) +
									(this.state.subplan != -1 ? " - " + (this.state.subplan.name.length > 30 ?
										this.state.subplan.name.substr(0, 30).contat("...") : this.state.subplan.name) : "")
									: "")
							)
						}
						</span>
						<div className="performance-strategic-btns floatRight">
							<span className={(this.state.hide) ? ("mdi mdi-chevron-right marginLeft15") : ("mdi mdi-chevron-down marginLeft15")} onClick={this.hideFields} />
						</div>
					</div>
					{(this.state.hide) ? ("") : (
						<div className="dashboard-plan-details-body">
							{this.state.loading ? <LoadingGaugeWhite /> :
								<div>
									<div className="dashboard-indicator-container">
										<div className="col-sm-4 dashboard-plan-details-column">
											<div className="dashboard-indicator-header">{Messages.getEditable("label.objective", "fpdi-nav-label")}s</div>
											<div className="dashboard-indicator-number">{this.state.planDetails ? this.state.planDetails.numberOfObjectives : "0"}</div></div>
										<div className="col-sm-4 dashboard-plan-details-column">
											<div className="dashboard-indicator-header">{Messages.getEditable("label.indicators", "fpdi-nav-label")}</div>
											<div className="dashboard-indicator-number">{this.state.planDetails ? this.state.planDetails.numberOfIndicators : "0"}</div></div>
										<div className="col-sm-4 dashboard-plan-details-column">
											<div className="dashboard-indicator-header">{Messages.getEditable("label.goals", "fpdi-nav-label")}</div>
											<div className="dashboard-indicator-number">{this.state.planDetails ? this.state.planDetails.numberOfGoals : "0"}</div></div>
									</div>
									<div className="dashboard-goals-information">
										<div className='dashboard-goals-title'>{Messages.getEditable("label.lateGoals", "fpdi-nav-label")}</div>
										<div className="fontSize12 ">
											<div className="dashboard-goals-head"><span className='fontWeightBold'>{this.state.planDetails ? this.state.planDetails.goalsDelayedPerCent.toFixed(2) : "0"}%</span> das metas estão atrasadas<span className='fontWeightBold floatRight'>100%</span></div></div>
										<Progress completed={this.state.planDetails ? Number(this.state.planDetails.goalsDelayedPerCent) : 0} />


									</div>
									<div className="dashboard-objective-information">
										<div className='dashboard-goals-title'>{Messages.getEditable("label.budget", "fpdi-nav-label")}</div>
										{this.state.planDetails ? (this.state.planDetails.numberOfBudgets >= 1 ?
											<div className="fontSize12">{Messages.getEditable("label.thereAre", "fpdi-nav-label")} <span className="fontWeightBold">{this.state.planDetails.numberOfBudgets}</span> {Messages.getEditable("label.budgetaryElementsLinkedToTheObjectives", "fpdi-nav-label")}</div>
											: <div className="fontSize12">{Messages.getEditable("label.exist", "fpdi-nav-label")} <span className="fontWeightBold">{this.state.planDetails.numberOfBudgets}</span> {Messages.getEditable("label.budgetaryElementLinkedToTheObjective", "fpdi-nav-label")}</div>) : "0 "}
									</div>
								</div>}
						</div>)}
				</div>
			</div>
		);
	}
});
