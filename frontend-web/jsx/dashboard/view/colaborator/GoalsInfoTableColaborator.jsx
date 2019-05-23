import React from "react";
import _ from 'underscore';
import moment from 'moment';
import string from 'string';

import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";

const performanceMap = [
	{value: -1, label: Messages.get("label.selectPerformance")},
	{value: 1, label: Messages.get("label.goals.belowMinimum")},
	{value: 2, label: Messages.get("label.goals.belowExpected")},
	{value: 3, label: Messages.get("label.goals.reached")},
	{value: 4, label: Messages.get("label.goals.aboveExpected")},
	{value: 5, label: Messages.get("label.goals.notStarted")},
];

export default React.createClass({
	getInitialState() {
		return {
			hide: false,
			goalsInformation: null,
			totalGoalsInformation: null,
			indicators: [],
			selectedIndicator: null,
			selectedPerformance: null,
			plan: this.props.plan,
			subPlan: this.props.subPlan.id,
			sortedColumn: null,
			loading: true,
		};
	},

	componentDidMount() {
		DashboardStore.on("goalsinfotableretrivied", (store) => {
			this.setState({
				goalsInformation: store.data,
				totalGoalsInformation: store.total,
				loading: false,
			});
		}, this);
		StructureStore.on("indicatorsByMacroAndPlanRetrivied", (model) => {
			this.setState({
				indicators: model.data,
				hide: false,
			});
		}, this);
		//this.refresh(this.props);
	},

	componentWillReceiveProps(newProps) {
		if (this.state.subPlan !== newProps.subPlan.id || this.state.plan !== newProps.plan) {
			this.setState({
				subPlan: newProps.subPlan.id,
				plan: newProps.plan,
			});
			this.refresh(newProps);
		}
	},

	refresh(props) {
		this.setState({
			indicators: [],
			selectedIndicator: null,
		}, () => this.getInfos(1, 5, props));
		StructureStore.dispatch({
			action: StructureStore.ACTION_GET_INDICATORS_MACRO_PLAN,
			data: {
				macroId: props.plan.id === -1 ? null : props.plan.id,
				planId: props.subPlan === -1 ? null : props.subPlan.id,
			}
		});
	},

	getInfos(page, pageSize, opt) {
		opt = opt || this.props;
		DashboardStore.dispatch({
			action: DashboardStore.ACTION_GET_GOALS_INFO_TABLE,
			data: {
				macro: opt.plan === -1 ? null : opt.plan.get("id"),
				plan: opt.subPlan === -1 ? null : opt.subPlan.id,
				indicator: this.state.selectedIndicator,
				page,
				pageSize,
				filter: this.state.selectedPerformance,
			}
		});
		this.setState({
			loading: true,
		});
	},

	componentWillUnmount() {
		DashboardStore.off(null, null, this);
		StructureStore.off(null, null, this);
		PlanMacroStore.off(null, null, this);
	},

	onIndicatorSelectChange(e) {
		const value = parseInt(e.target.value);
		this.setState({
			selectedIndicator: value === -1 ? null : value,
			loading: true,
		}, () => this.getInfos(1, 5));
		this.resetTablePaginationSize();
	},

	onPerformanceSelectChange(e) {
		const value = parseInt(e.target.value);
		this.setState({
			selectedPerformance: value === -1 ? null : value,
			loading: true,
		}, () => this.getInfos(1, 5));
		this.resetTablePaginationSize();
	},

	resetTablePaginationSize() {
		this.refs['goals-info-table-col-pagination'].setState({
			pageSize: 5,
		});
	},

	hideFields() {
		this.setState({
			hide: !this.state.hide
		})
	},

	sortTableByFinishDate() {
		let { goalsInformation } = this.state;
		let { sortedColumn } = this.state;
		if (!sortedColumn || sortedColumn.name !== 'finishDate' || !sortedColumn.asc) {
			goalsInformation.sort((a, b) => {
				const d1 = !a.finishDate ? moment(1, "x") : moment(a.finishDate, "DD/MM/YYYY");
				const d2 = !b.finishDate ? moment(1, "x") : moment(b.finishDate, "DD/MM/YYYY");
				if (d1.isAfter(d2)) {
					return 1;
				}
				if (d1.isBefore(d2)) {
					return -1;
				}
				return 0;
			});
			sortedColumn = { asc: true };
		} else {
			goalsInformation.sort((a, b) => {
				const d1 = !a.finishDate ? moment(1, "x") : moment(a.finishDate, "DD/MM/YYYY");
				const d2 = !b.finishDate ? moment(1, "x") : moment(b.finishDate, "DD/MM/YYYY");
				if (d1.isBefore(d2)) {
					return 1;
				}
				if (d1.isAfter(d2)) {
					return -1;
				}
				return 0;
			});
			sortedColumn.asc = false;
		}
		sortedColumn.name = 'finishDate';
		this.setState({
			goalsInformation,
			sortedColumn,
		});
	},

	sortTable(columnName) {
		let { goalsInformation } = this.state;
		let { sortedColumn } = this.state;
		if (!sortedColumn || sortedColumn.name !== columnName || !sortedColumn.asc) {
			goalsInformation.sort((a, b) => {
				if (a[columnName] === undefined || a[columnName] < b[columnName]) {
					return -1;
				}
				if (a[columnName] > b[columnName]) {
					return 1;
				}
				return 0;
			});
			sortedColumn = { asc: true };
		} else {
			goalsInformation.sort((a, b) => {
				if (b[columnName] === undefined || a[columnName] > b[columnName]) {
					return -1;
				}
				if (a[columnName] < b[columnName]) {
					return 1;
				}
				return 0;
			});
			sortedColumn.asc = false;
		}
		sortedColumn.name = columnName;
		this.setState({
			goalsInformation,
			sortedColumn,
		});
	},

	getSortIconClass(columnName) {
		const { sortedColumn } = this.state;
		if (!sortedColumn || sortedColumn.name !== columnName) {
			return "mdi mdi-sort cursorPointer";
		} else if (sortedColumn.asc) {
			return "mdi mdi-sort-ascending cursorPointer";
		} else {
			return "mdi mdi-sort-descending cursorPointer";
		}
	},

	render() {
		return (
			<div className="panel panel-default">
				<div className="panel-heading">
					<b className="budget-graphic-title">{Messages.get("label.goals")}</b>
					<select onChange={this.onIndicatorSelectChange} className="form-control dashboard-select-box-graphs marginLeft10">
						<option value={-1} data-placement="right" title={Messages.get("label.allIndicators")}>
							{Messages.get("label.allIndicators")}
						</option>
						{
							_.map(this.state.indicators, (attr, idx) => (
								<option key={"ind-opt-" + idx} value={attr.id} data-placement="right" title={attr.name}>
									{
										attr.name.length > 20
											? (string(attr.name).trim().substr(0, 20).concat("...").toString())
											: (attr.name)
									}
								</option>
							))
						}
					</select>
					<select onChange={this.onPerformanceSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectPerformances">
						{
							_.map(performanceMap, (performance, idx) => (
								<option value={performance.value} data-placement="right" key={idx}>{performance.label}</option>
							))
						}
					</select>
					<div className="performance-strategic-btns floatRight">
						<span
							className={this.state.hide ? "mdi mdi-chevron-right marginLeft15" : "mdi mdi-chevron-down marginLeft15"}
							onClick={this.hideFields}
						/>
					</div>
				</div>
				{
					!this.state.hide && this.state.goalsInformation &&
					<div>
						{
							this.state.loading
							? <LoadingGauge />
							: (
								<table className="dashboard-table table goals-dashboard-table">
									<tbody>
									<tr>
										<th id="column-goals-perfomance">{Messages.getEditable("label.objective", "fpdi-nav-label")}
											<span
												className={this.getSortIconClass('objectiveName')}
												onClick={() => this.sortTable('objectiveName')}
											/>
										</th>
										<th id="column-goals-perfomance">{Messages.getEditable("label.indicator", "fpdi-nav-label")}
											<span
												className={this.getSortIconClass('indicatorName')}
												onClick={() => this.sortTable('indicatorName')}
											/>
										</th>
										<th id="column-goals-perfomance">{Messages.getEditable("label.goal", "fpdi-nav-label")}
											<span
												className={this.getSortIconClass('goalName')}
												onClick={() => this.sortTable('goalName')}
											/>
										</th>
										<th id="column-goals-perfomance">{Messages.getEditable("label.maturity", "fpdi-nav-label")}
											<span
												className={this.getSortIconClass('finishDate')}
												onClick={this.sortTableByFinishDate}
											/>
										</th>
										<th id="column-goals-perfomance">{Messages.getEditable("label.status", "fpdi-nav-label")}</th>
										<th id="column-goals-perfomance">{Messages.getEditable("label.goals.expected", "fpdi-nav-label")} </th>
										<th id="column-goals-perfomance">{Messages.getEditable("label.reached", "fpdi-nav-label")}
											<span
												className={this.getSortIconClass('reached')}
												onClick={() => this.sortTable('reached')}
											/>
										</th>
									</tr>
									{
										this.state.goalsInformation.length > 0
										? _.map(this.state.goalsInformation, (goal, idx) => {
											return (
												<tr key={"goal-" + idx} name={"goal-" + idx}>
													<td className="fdpi-table-cell">{goal.objectiveName}</td>
													<td className="fdpi-table-cell">{goal.indicatorName}</td>
													<td className="fdpi-table-cell">{goal.goalName}</td>
													<td className="fdpi-table-cell">{goal.finishDate ?
														goal.finishDate.split(" ")[0] : ""}</td>
													<td className="fdpi-table-cell">{goal.deadLineStatus == 3 ?
														<div className="lateGoalInfo">{goal.goalStatus} </div> :
														<div className="inDayGoalInfo">{goal.goalStatus} </div>}
														{Messages.getEditable("label.lastUpdate", "fpdi-nav-label")} {goal.lastModification}</td>
													<td className="fdpi-table-cell">{goal.expected}</td>
													<td className="fdpi-table-cell">{goal.reached}</td>
												</tr>);
										})
										: (
											<tr>
												<td id="GoalsInformationTable">
													{
														this.state.indicator &&
														this.state.indicator.aggregate
															? Messages.getEditable("label.aggIndicatorHaveNoGoals", "fpdi-nav-label")
															: Messages.getEditable("label.noRegister", "fpdi-nav-label")
													}
												</td>
											</tr>
										)
									}
									</tbody>
								</table>
							)
						}

						<TablePagination
							ref="goals-info-table-col-pagination"
							total={!this.state.goalsInformation ? 0 : this.state.totalGoalsInformation}
							onChangePage={this.getInfos}
							tableName={"goalsInfoColaborator-table"}
						/>
					</div>
				}
			</div>
		);
	}
});
