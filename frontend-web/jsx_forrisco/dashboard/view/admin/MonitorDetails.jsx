import React from "react";
import _ from 'underscore';

import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Graphic from "forpdi/jsx_forrisco/dashboard/view/graphic/GraphicMonitor.jsx";
import moment from 'moment'
var numeral = require('numeral');

export default React.createClass({

	getInitialState() {
		return {
			plan: null,
			unit: -1,
			units: [],
			risks: [],
			monitors: [],
			monitor_history: [],
			monitor: {
				inDay: 0,
				closeToMaturity: 0,
				late: 0,
				Percentage: {
					inDay: 0,
					closeToMaturity: 0,
					late: 0,
				},
			},
			loading: true,
			displayGraph: false,
			level: [
				{ level: "em dia", color: 4 },
				{ level: "próximo a vencer", color: 3 },
				{ level: "atrasado", color: 0 },
			],
		};
	},

	componentDidMount() {
		RiskStore.on("monitorByPlan", (model) => {
			this.state.monitors = []
			for (var i = 0; i < model.data.length; i++) {
				this.state.monitors.push(model.data[i])
			}
			this.setState({
				monitors: this.state.monitors,
			})

			this.state.unit = -1
			this.Quantify()

		}, this);

		RiskStore.on("monitorHistoryByUnit", (model) => {
			if (model.success) {
				this.setState({
					monitor_history: model.data,
				});
			}
		}, this);
		//this.refreshComponent(this.state.plan.id, this.state.unit);
	},

	componentWillReceiveProps(newProps) {
		var me = this;
		this.state.plan = newProps.plan
		this.state.units = newProps.units
		this.state.risks = newProps.risks
		this.setState({
			plan: newProps.plan,
			risks: newProps.risks,
			units: newProps.units,
			loading: true,
			monitors: []
		});

		if (newProps.units.length == 0) {
			this.state.monitors = []
			this.state.risks = []
			this.state.unit = -1
			this.Quantify();
			this.setState({
				loading: false,
			});
		}
		else {
			this.refreshComponent(this.state.plan.id, this.state.unit);
		}
		if (this.state.plan !== newProps.plan.id) {
			this.refreshComponent(newProps.plan.id, this.state.unit);
			this.setState({
				plan: newProps.plan.id,
			})
		}
	},

	refreshComponent(planId, unit) {
		RiskStore.dispatch({
			action: RiskStore.ACTION_FIND_MONITORS_BY_PLAN,
			data: planId
		});

		RiskStore.dispatch({
			action: RiskStore.ACTION_FIND_MONITOR_HISTORY_BY_UNIT,
			data: {
				unit: unit,
				plan: planId
			}
		})
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
	},

	getRisks() {
		var risks = []

		if (this.state.unit == -1) {
			for (var i = 0; i < this.state.risks.length; i++) {
				///for(var j=0; j<this.state.risks[i].length; j++){

				//if((this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "ameaça")
				//|| !this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "oportunidade"){
				//risks.push(this.state.risks[i].array[j]);
				risks.push(this.state.risks[i])
				//}
				//}
			}
		} else {
			for (var i = 0; i < this.state.risks.length; i++) {
				if (this.state.risks[i].unit.id == this.state.unit) {
					//	var array=this.state.risks[i].array
					//for(var j=0; j<array.length; j++){
					//if((this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "ameaça")
					//|| !this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "oportunidade"){
					//risks.push(array[j]);
					risks.push(this.state.risks[i])
					//}
					//}
				}
			}
			return risks;
		}
		return risks;
	},

	switchGraph(displayGraph) {
		this.setState({
			displayGraph,
		})
	},

	onUnitChange(evnt) {

		this.state.unit = this.refs['selectUnits'].value

		this.Quantify();
	},

	Quantify() {

		var monitor = {
			inDay: 0,
			closeToMaturity: 0,
			late: 0,
			Percentage: {
				inDay: 0,
				closeToMaturity: 0,
				late: 0,
			}
		}

		var risks = this.getRisks();

		for (var i = 0; i < risks.length; i++) {

			var latestMonitor = null

			for (var j = 0; j < this.state.monitors.length; j++) {
				if (risks[i].id == this.state.monitors[j].risk.id) {
					if (latestMonitor == null) {
						latestMonitor = this.state.monitors[j]
					} else {
						if (moment(latestMonitor.begin, 'DD/MM/YYYY hh:mm:ss').unix() <
							moment(this.state.monitors[j].begin, 'DD/MM/YYYY hh:mm:ss').unix()) {
							latestMonitor = this.state.monitors[j]
						}
					}
				}
			}

			let latestDate = latestMonitor ? latestMonitor.begin : risks[i].begin;
			var diffDays = ((new Date()).getTime() / 1000 - moment(latestDate, 'DD/MM/YYYY hh:mm:ss').unix()) / (60 * 60 * 24)

			switch (risks[i].periodicity.toLowerCase()) {

				case "diária":
					if (diffDays < 0.2916666666666666) { monitor.inDay += 1 }
					else if (diffDays < 1) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "semanal":
					if (diffDays < 2) { monitor.inDay += 1 }
					else if (diffDays < 7) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "quinzenal":
					if (diffDays < 7) { monitor.inDay += 1 }
					else if (diffDays < 15) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "mensal":
					if (diffDays < 7) { monitor.inDay += 1 }
					else if (diffDays < 30) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "bimestral":
					if (diffDays < 21) { monitor.inDay += 1 }
					else if (diffDays < 60) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "trimestral":
					if (diffDays < 21) { monitor.inDay += 1 }
					else if (diffDays < 90) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "semestral":
					if (diffDays < 30) { monitor.inDay += 1 }
					else if (diffDays < 180) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;

				case "anual":
					if (diffDays < 30) { monitor.inDay += 1 }
					else if (diffDays < 360) { monitor.closeToMaturity += 1 }
					else { monitor.late += 1 }
					break;
			}
		}

		var percent = 100 / (risks.length != 0 ? risks.length : 1)

		monitor.Percentage.inDay = monitor.inDay * percent
		monitor.Percentage.closeToMaturity = monitor.closeToMaturity * percent
		monitor.Percentage.late = monitor.late * percent

		this.setState({
			monitor: monitor,
			loading: false
		})
	},

	displayGraph(bool) {
		this.state.displayGraph = bool
	},

	render() {
		var title = Messages.get("label.risk.monitor");
		return (
			<div className="frisco-dashboard frisco-dashboard-right dashboard-risk-details panel">
				<div className="dashboard-plan-details-header">
					<span title={title}>{title.toUpperCase()}</span>
					<span className="frisco-containerSelect">
						{Messages.get("label.units")}
						<select
							onChange={this.onUnitChange}
							className="form-control dashboard-select-box-graphs marginLeft10"
							ref="selectUnits"
						>
							<option value={-1} data-placement="right" title={Messages.get("label.viewAll_")}>
								{Messages.get("label.viewAll_")}
							</option>
							{
								this.state.units.map((attr, idy) => (
									<option key={attr.id} value={attr.id} data-placement="right" title={attr.name}>
										{(attr.name.length > 20) ? ((attr.name).trim().substr(0, 20).concat("...").toString()) : (attr.name)}
									</option>
								))
							}
						</select>
					</span>
				</div>
				<div className="dashboard-risk-details-body" >
					<div className="mdi mdi-chart-line icon-link" style={{ float: "right", padding: "10px" }} onClick={() => this.switchGraph(true)} />
					{
						this.state.loading ? <LoadingGauge /> :

						<div style={{ padding: "20px 50px" }}>
							<div className="row">
								<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.inDay}</h1>
									<h4>{"(" + numeral(this.state.monitor.Percentage.inDay).format('0,0.00') + "%)"}</h4>
									<p>{Messages.getEditable("label.goals.inDay", "fpdi-nav-label")}</p>
								</div>
								<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.closeToMaturity}</h1>
									<h4>{"(" + numeral(this.state.monitor.Percentage.closeToMaturity).format('0,0.00') + "%)"}</h4>
									<p>{Messages.getEditable("label.goals.closeToMaturity_", "fpdi-nav-label")}</p>
								</div>
							</div>
							<div className="row">
								<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.late}</h1>
									<h4>{"(" + numeral(this.state.monitor.Percentage.late).format('0,0.00') + "%)"}</h4>
									<p>{Messages.getEditable("label.goals.late_", "fpdi-nav-label")}</p>
								</div>
							</div>
						</div>
					}
				</div>
				{
					this.state.displayGraph
					&&
					<Graphic
						title={Messages.get("label.monitor.history").toUpperCase()}
						planId={this.props.plan.id}
						unit={this.state.unit}
						units={this.state.units}
						level={this.state.level}
						history={this.state.monitor_history}
						displayGraph={this.displayGraph}
					/>
				}
			</div>
		);
	}
});
