import React from "react";
import GoalBoard from "forpdi/jsx/dashboard/widget/GoalBoard.jsx";

import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";


export default React.createClass({

	getInitialState() {
		return {
			plan: this.props.plan,
			subPlan: this.props.subPlan,
			belowMinimum: 0,
			belowExpected: 0,
			reached: 0,
			aboveExpected: 0,
			notStarted: 0,
			loading: true
		};
	},

	componentWillReceiveProps(newProps) {
		if (this.props.plan != newProps.plan || this.props.subPlan != newProps.subPlan) {
			this.setState({
				plan: newProps.plan,
				subPlan: newProps.subPlan,
				loading: true
			});
			DashboardStore.dispatch({
				action: DashboardStore.ACTION_GET_GOALS_INFO_ADM,
				data: {
					macro: (newProps.plan != -1) ? (newProps.plan.get("id")) : (null),
					plan: (newProps.subPlan != -1) ? (newProps.subPlan.id) : (null)
				}
			});

		}
	},

	componentDidMount() {
		DashboardStore.on("goalsinfoadminretrivied", (store) => {
			this.setState({
				belowMinimum: store.data.belowMininum,
				belowExpected: store.data.belowExpected,
				reached: store.data.reached,
				aboveExpected: store.data.aboveExpected,
				notStarted: store.data.notStarted,
				loading: false
			});
		}, this);
		this.refreshComponent(this.props.plan, this.props.subPlan);
	},

	refreshComponent(plan, subPlan) {
		DashboardStore.dispatch({
			action: DashboardStore.ACTION_GET_GOALS_INFO_ADM,
			data: {
				macro: (plan != -1) ? (plan.get("id")) : (null),
				plan: (subPlan != -1) ? (subPlan.id) : (null)
			}
		});
	},

	componentWillUnmount() {
		DashboardStore.off(null, null, this);
	},

	render() {
		return (
			<div className="row">
				<div className="col-sm-2">
					<GoalBoard
						className="dashboard-goal-panel-color-red dashboard-goal-colaborator-box pull-left"
						numberValue={this.state.belowMinimum}
						goalSubLabel={"abaixo do mínimo"}
						goalSubLabelSingular={"abaixo do mínimo"}
						loading={this.state.loading}
					/>
				</div>
				<div className="col-sm-2 goals-board-group">
					<GoalBoard
						className="dashboard-goal-panel-color-yellow dashboard-goal-colaborator-box pull-left"
						numberValue={this.state.belowExpected}
						goalSubLabel={"abaixo do esperado"}
						goalSubLabelSingular={"abaixo do esperado"}
						loading={this.state.loading}
					/>
				</div>
				<div className="col-sm-2 goals-board-group">
					<GoalBoard
						className="dashboard-goal-panel-color-green dashboard-goal-colaborator-box pull-left"
						numberValue={this.state.reached}
						goalSubLabel={"suficientes"}
						goalSubLabelSingular={"suficiente"}
						loading={this.state.loading}
					/>
				</div>
				<div className="col-sm-2 goals-board-group">
					<GoalBoard
						className="dashboard-goal-panel-color-blue dashboard-goal-colaborator-box pull-left"
						numberValue={this.state.aboveExpected}
						goalSubLabel={"acima do máximo"}
						goalSubLabelSingular={"acima do máximo"}
						loading={this.state.loading}
					/>
				</div>
				<div className="col-sm-2 goals-board-group">
					<GoalBoard
						className="dashboard-goal-panel-color-gray dashboard-goal-colaborator-box margin-0 pull-left"
						numberValue={this.state.notStarted}
						goalSubLabel={"não iniciadas"}
						goalSubLabelSingular={"não iniciada"}
						loading={this.state.loading}
					/>
				</div>
				<div className="clearfix" />
			</div>
		);
	}
});
