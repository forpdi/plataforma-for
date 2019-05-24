import React from "react";
import string from 'string';
import _ from 'underscore';

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var numeral = require('numeral');

export default React.createClass({

	getInitialState() {
		return {
			loading: true,
			indicators: [],
			selectedIndicator: null,
			planId: null,
			subPlanId: null,
			elements: [],
			aggregateIndicator: false,
			chartEvents: [
				{
					eventName: 'select',
					callback: this.onChartClick
				},
			],
			pageSize: 20,
			total: 0,
			options: {
				title: '',
				colors: ['#CCCCCC', '#333333'],
				vAxis: { title: 'Esperado x Alcançado', minValue: 0, maxValue: 15, format: '#,##0' },
				hAxis: { slantedText: true, slantedTextAngle: 45 },
				legend: { position: 'none' },
				bar: { groupWidth: '50%' },
				seriesType: 'bars',
				series: { 1: { type: 'line', pointsVisible: true, pointSize: 4 } },
			},
			data: [
				['titulo', 'Alcançado', { role: 'style' }, 'Esperado'],
				['', 0, '', 0],
			],
		};
	},

	componentDidMount() {
		var me = this;
		StructureStore.on("indicatorsByMacroAndPlanRetrivied", model => (
			this.setState({
				indicators: model.data,
			})
		), me);

		DashboardStore.on("goalsInformationColaborator", (model) => {
			var elements = [];
			var data = [['Element', 'Alcançado', { role: 'style' }, 'Esperado']];
			var goalValue;
			model.data.map((goal) => {
				goalValue = this.getGoalsValues(goal);
				elements.push(goalValue);
			});
			if (model.data.length == 0) {
				data = [['Element', 'Alcançado']];
				elements.push([Messages.get("label.haveNoGoals"), 0]);
				data.push([Messages.get("label.haveNoGoals"), 0]);
			} else {
				model.data.map((goal, idx) => {
					data.push(elements[idx]);
				});
			}
			this.setState({
				elements: elements,
				data: data,
				goals: model.data,
				loading: false,
				hide: false,
				total: model.total,
			});
			this.updateChartOptions(model);
		}, me);

		this.refresh(this.props);
	},

	componentWillReceiveProps(newProps) {
		const planId = newProps.plan !== -1 ? newProps.plan.id : null;
		const subPlanId = newProps.subPlanId !== -1 ? newProps.subPlan.id : null;
		if (planId !== this.state.planId || subPlanId !== this.state.subPlanId) {
			this.setState({
				planId,
				subPlanId,
			});
			this.refresh(newProps);
		}
	},

	componentWillUnmount() {
		DashboardStore.off(null, null, this);
		StructureStore.off(null, null, this);
	},

	refresh(props) {
		this.setState({
			indicators: [],
			selectedIndicator: null,
			loading: true,
		}, () => this.getGoalsInfo(1, this.state.pageSize, props));
		StructureStore.dispatch({
			action: StructureStore.ACTION_GET_INDICATORS_MACRO_PLAN,
			data: {
				macroId: props.plan === -1 ? null : props.plan.id,
				planId: props.subPlan === -1 ? null : props.subPlan.id,
			},
		});
	},

	getGoalsInfo(page, pageSize, opt) {
		opt = opt || this.props;
		DashboardStore.dispatch({
			action: DashboardStore.ACTION_GET_GOALS_INFO_COL,
			data: {
				macro: opt.plan != -1 ? opt.plan.get("id") : null,
				plan: opt.subPlan != -1 ? opt.subPlan.id : null,
				indicator: this.state.selectedIndicator,
				page: page,
				pageSize: pageSize
			},
		});
	},

	updateChartOptions(model) {
		var bool = (model ? model.data.length > 0 : true);
		var hTitle = (model && model.data.length > 0 ? Messages.get("label.goals") : "");
		this.setState({
			options: {
				title: '',
				colors: ['#CCCCCC', '#333333'],
				vAxis: { title: 'Esperado x Alcançado', minValue: 0, maxValue: 15, format: '#,##0' },
				hAxis: { title: hTitle, slantedText: bool, slantedTextAngle: 30 },
				legend: { position: 'none' },
				bar: { groupWidth: '50%' },
				seriesType: 'bars',
				series: { 1: { type: 'line', pointsVisible: true, pointSize: 4 } }
			},
		});
	},

	getGoalsValues(goal) {
		var expectedField, maximumField, minimumField, reachedField;
		var index;
		let fExp, fRec;
		for (var cont = 1; cont < goal.attributeList.length; cont++) {
			index = cont;
			if (goal.attributeInstanceList[index]) {
				if (goal.attributeList[cont].expectedField) {
					expectedField = goal.attributeInstanceList[index].valueAsNumber || 0;
					fExp = goal.attributeInstanceList[index].formattedValue || "0";
				} else if (goal.attributeList[cont].maximumField) {
					maximumField = goal.attributeInstanceList[index].valueAsNumber || 0;
				} else if (goal.attributeList[cont].minimumField) {
					minimumField = goal.attributeInstanceList[index].valueAsNumber || 0;
				} else if (goal.attributeList[cont].reachedField) {
					reachedField = goal.attributeInstanceList[index].valueAsNumber || 0;
					fRec = goal.attributeInstanceList[index].formattedValue || "0";
				}
			}
		}
		var graphItem = ["", 0, '', 0];
		if (goal.name.length > 50) {
			graphItem[0] = goal.name.slice(0, 50) + "...";
		} else {
			graphItem[0] = goal.name;
		}

		if (reachedField === undefined) {
			reachedField = 0;
		}

		var format = fExp.replace(/[0-9.,]/gi, "");
		var prefix = "", sufix = "";
		if (fExp.indexOf(format) == 0) {
			prefix = format;
		} else {
			sufix = format;
		}
		if (goal.polarity === Messages.get("label.highestBest")) {
			if (reachedField < minimumField) {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#E74C3C";
			} else if (reachedField < expectedField) {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#FFCC33";
			} else if (reachedField <= maximumField) {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#51D466";
			} else {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#4EB4FE";
			}
		} else if (goal.polarity === Messages.get("label.lowerBest")) {
			if (reachedField > minimumField) {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#E74C3C";
			} else if (reachedField > expectedField) {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#FFCC33";
			} else if (reachedField >= maximumField) {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#51D466";
			} else {
				graphItem[1] = {
					v: reachedField,
					f: fRec
				};
				graphItem[3] = {
					v: expectedField,
					f: prefix + numeral(expectedField).format('0,0.00') + sufix
				};
				graphItem[2] = "#4EB4FE";
			}
		}
		return graphItem;
	},

	hideFields() {
		this.setState({
			hide: !this.state.hide,
		})
	},

	onIndicatorSelectChange(e) {
		const value = parseInt(e.target.value);
		this.setState({
			selectedIndicator: value === -1 ? null : value,
			loading: true,
		}, () => this.getGoalsInfo(1, this.state.pageSize));
	},

	onChartClick(Chart) {
		var me = this;
		if (Chart.chart.getSelection().length > 0) {
			var level, url;
			level = me.state.goals[Chart.chart.getSelection()[0].row];

			if (level) {
				url = window.location.origin + window.location.pathname + "#/plan/" +
					level.plan.parent.id + "/details/subplan/level/" + level.id;

				var msg = Messages.get("label.askGoToSelectedLevel");
				Modal.confirmCustom(
					() => {
						Modal.hide();
						location.assign(url);
					},
					msg,
					() => {
						Chart.chart.setSelection([]);
						Modal.hide();
					}
				);
			}
		}
	},

	render() {
		return (
			<div className={this.props.className}>
				<div>
					<div className="panel panel-default dashboard-goals-info-ctn">
						<div className="panel-heading">
							<b className="budget-graphic-title">
								{Messages.getEditable("label.goalsPerformance", "fpdi-nav-label")}
							</b>
							<span
								className={this.state.hide
									? "mdi mdi-chevron-right marginLeft15 floatRight"
									: "mdi mdi-chevron-down marginLeft15 floatRight"}
								onClick={this.hideFields}
							/>
							<select
								onChange={this.onIndicatorSelectChange}
								className="form-control dashboard-select-box-graphs marginLeft10"
							>
								<option value={-1} data-placement="right">
									{Messages.get("label.allIndicators")}
								</option>
								{
									_.map(this.state.indicators, (indicator, idx) => (
										<option key={"ind-opt-" + idx} value={indicator.id} data-placement="right" title={indicator.name}>
											{
												indicator.name.length > 20
													? string(indicator.name).trim().substr(0, 20).concat("...").toString()
													: indicator.name
											}
										</option>
									))
								}
							</select>
						</div>
						{
							!this.state.hide && (
								this.state.loading ? <LoadingGauge /> : (
									<div>
										<ForPDIChart
											chartType="ComboChart"
											data={this.state.data}
											options={this.state.options}
											graph_id="ColumnChart-Budget"
											width="100%"
											height="300px"
											legend_toggle={true}
											chartEvents={this.state.chartEvents}
											pageSize={this.state.pageSize}
											total={this.state.total}
											onChangePage={this.getGoalsInfo}
										/>
										<div className="colaborator-goal-performance-legend">
											<div className="aggregate-indicator-without-goals-legend">
												<span className="legend-item">
													{
														this.state.aggregateIndicator &&
														<p id="aggregate-indicator-goals">{Messages.get("label.aggIndicatorHaveNoGoals")}</p>
													}
												</span>
											</div>
											<span className="legend-item"><input type="text" className="legend-goals-minimumbelow marginLeft10" disabled />
												{Messages.getEditable("label.goals.belowMinimum", "fpdi-nav-label")}
											</span>
											<span className="legend-item"><input type="text" className="legend-goals-expectedbelow marginLeft10" disabled />
												{Messages.getEditable("label.goals.belowExpected", "fpdi-nav-label")}
											</span>
											<span className="legend-item"><input type="text" className="legend-goals-enough marginLeft10" disabled />
												{Messages.getEditable("label.goals.reached", "fpdi-nav-label")}
											</span>
											<span className="legend-item"><input type="text" className="legend-goals-expectedabove marginLeft10" disabled />
												{Messages.getEditable("label.goals.aboveExpected", "fpdi-nav-label")}
											</span>
											<span className="legend-item"><input type="text" className="legend-goals-difference-expected marginLeft10" disabled />
												{Messages.getEditable("label.goals.expected", "fpdi-nav-label")}
											</span>
										</div>
									</div>
								)
							)
						}
					</div>
				</div>
			</div>
		);
	}
});
