import React from "react";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
//import ForPDIChart from "forpdi/jsx_forrisco/core/widget/ForRiscoChart.jsx"
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import string from 'string';
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import moment from 'moment'

var numeral = require('numeral');

export default React.createClass({

	getInitialState() {
		return {
			plan: null,
			units: [],
			incidents: [],
			unit: -1,
			loading: true,
			year: [],
			data: [],
			thematicAxes: [],
			selectedThematicAxes: -1,
			options: {
				hAxis: {title: "Tempo", minValue: 1, maxValue: 12,},
				vAxis: {title: 'Quantidade', minValue: 1, maxValue: 15},
				legend: {position: 'none'},
				explorer: {axis: 'horizontal'},
				bar: {groupWidth: '50%'},
			},
			chartEvents: [
				{
					eventName: 'select',
					callback: this.onChartClick
				},
			],
			pageSize: 10,
			opportunities: true,
			threats: true,
		};
	},

	uniques(array) {
		return array.filter(function (value, index, self) {
			return self.indexOf(value) === index;
		});
	},

	componentDidMount() {

		var me = this;

		RiskStore.on("incidentbByPlan", (model) => {
			this.state.incidents = [];

			for (var i = 0; i < model.data.length; i++) {
				this.state.incidents.push(model.data[i]);
				var data = moment(model.data[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate();
				this.state.year.push(data.getFullYear())
			}

			this.state.year = this.uniques(this.state.year).sort().reverse();

			this.setState({
				incidents: this.state.incidents,
				year: this.state.year,
				loading: false
			});

			this.state.unit = -1;

			this.LoadIncidents((new Date).getFullYear(), this.state.unit)
		}, me);
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		this.state.plan = newProps.plan;
		this.state.units = newProps.units;

		this.setState({
			plan: newProps.plan,
			units: newProps.units,
			loading: true,
		});

		this.state.year = [(new Date).getFullYear()];


		if (newProps.units.length == 0) {
			this.state.incidents = [];
			this.state.unit = -1;
			this.LoadIncidents((new Date).getFullYear(), this.state.unit)
		} else {
			RiskStore.dispatch({
				action: RiskStore.ACTION_FIND_INCIDENTS_BY_PLAN,
				data: newProps.plan.id
			});
		}

	},

	onThematicAxesChange() {
		this.getInfo(1, this.state.pageSize);
	},

	onChartClick(Chart) {
		const incidents = [];

		if (this.refs['selectUnits'].value == -1) {
			for (var i in this.state.incidents) {
				if (moment(this.state.incidents[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate().getFullYear() == this.refs['selectYear'].value) {
					if (moment(this.state.incidents[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate().getMonth() == Chart.chart.getSelection()[0].row) {
						if (this.state.incidents[i].type == Chart.chart.getSelection()[0].column) {
							incidents.push(this.state.incidents[i])
						}
					}
				}
			}
		} else {
			for (var i in this.state.incidents) {
				if (moment(this.state.incidents[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate().getFullYear() == this.refs['selectYear'].value) {
					if (moment(this.state.incidents[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate().getMonth() == Chart.chart.getSelection()[0].row) {
						if (this.state.incidents[i].type == Chart.chart.getSelection()[0].column) {
							if (this.state.incidents[i].unitId == this.refs['selectUnits'].value) {
								incidents.push(this.state.incidents[i])
							}
						}
					}
				}
			}
		}

		if (Chart.chart.getSelection().length > 0) {
			Modal.incidentModal(incidents)
		}
		Chart.chart.setSelection(false);
	},

	getInfo(page, pageSize, opt) {

	},

	onOpportunitiesChange() {
		if (!this.state.threats && this.state.opportunities) {
			return
		}

		this.state.opportunities = !this.state.opportunities;

		this.setState({
			opportunities: this.state.opportunities
		});

		this.onUnitChange(null)
	},

	onThreatsChange() {

		if (this.state.threats && !this.state.opportunities) {
			return
		}

		this.state.threats = !this.state.threats

		this.setState({
			threats: this.state.threats
		})

		this.onUnitChange(null)
	},

	onUnitChange(evnt) {
		this.state.unit = this.refs['selectUnits'].value
		this.LoadIncidents(this.refs['selectYear'].value, this.state.unit)
	},

	onYearChange() {
		this.onUnitChange();
	},

	LoadIncidents(year, unit) {

		var data = []
		var incidents = []
		var mes = {
			0: 'jan',
			1: 'fev',
			2: 'mar',
			3: 'abr',
			4: 'mai',
			5: 'jun',
			6: 'jul',
			7: 'ago',
			8: 'set',
			9: 'out',
			10: 'nov',
			11: 'dez'
		};

		var month_thr = {0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0, 11: 0};
		var month_opp = {0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0, 11: 0};

		for (var i = 0; i < this.state.incidents.length; i++) {
			if (moment(this.state.incidents[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate().getFullYear() == year) {
				if (unit == -1) {
					incidents.push(this.state.incidents[i])        //"Exibir Todas" selecionado
				} else {
					if (unit == this.state.incidents[i].unitId) {  //"Qualquer outra unidade" selecionada
						incidents.push(this.state.incidents[i])
					}
				}
			}
		}

		for (i = 0; i < incidents.length; i++) {
			//Converte mês para INT
			var this_month = moment(incidents[i].begin, 'DD/MM/YYYY hh:mm:ss').toDate().getMonth();

			if (incidents[i].type == 1) {
				month_thr[this_month] += 1 //Meses de ameaça
			} else {
				month_opp[this_month] += 1 //Meses de oportunidade
			}
		}

		var month;
		if (year == (new Date).getFullYear()) { month = (new Date).getMonth()} else {month = 12}

		var max = 0; var axis = [];
		if (this.state.opportunities && this.state.threats) {
			axis.push(['mes', 'ameaças', 'oportunidades'])
			for (var i = 0; i <= month; i++) {
				axis.push([mes[i], month_thr[i], month_opp[i]]);
				max = max < month_thr[i] ? month_thr[i] : max;
				max = max < month_opp[i] ? month_opp[i] : max;
			}
		} else if (this.state.opportunities) {
			axis.push(['mes', 'oportunidades'])
			for (var i = 0; i <= month; i++) {
				axis.push([mes[i], month_opp[i]])
				max = max < month_opp[i] ? month_opp[i] : max
			}
		} else {
			axis.push(['mes', 'ameaças'])
			for (var i = 0; i <= month; i++) {
				axis.push([mes[i], month_thr[i]])
				max = max < month_thr[i] ? month_thr[i] : max
			}
		}

		var colors;
		if (this.state.opportunities && this.state.threats) {
			colors = ["red", "blue"]
		} else if (this.state.opportunities) {
			colors = ["blue"]
		} else {
			colors = ["red"]
		}
		this.state.options.colors = colors
		this.state.options.vAxis.maxValue = max
		this.state.options.vAxis.format ='#'

		this.setState({
			data: axis,
			loading: false,
			options: this.state.options,
		});
	},

	render() {
		return (
			<div className="frisco-dashboard panel panel-default">
				<div className="panel-heading dashboard-panel-title">
					<b className="budget-graphic-title"
					   title={Messages.get("label.incidents")}>{Messages.get("label.incidents").toUpperCase()}</b>
					<span className="frisco-containerSelect"> {Messages.get("label.units")}
						<select onChange={this.onUnitChange}
								className="form-control dashboard-select-box-graphs marginLeft10" ref="selectUnits">
						<option value={-1} data-placement="right"
								title={Messages.get("label.viewAll_")}> {Messages.get("label.viewAll_")} </option>
							{this.state.units.map((attr, idy) => {
								return (
									<option key={attr.id} value={attr.id} data-placement="right" title={attr.name}>
										{(attr.name.length > 20) ? (string(attr.name).trim().substr(0, 20).concat("...").toString()) : (attr.name)}
									</option>);
							})
							}
					</select>
					</span>
				</div>
				<br/>
				<div style={{"textAlign": "center"}}>
					<select
						className="form-control dashboard-select-box-graphs marginLeft10"
						onChange={this.onYearChange}
						ref="selectYear"
					>
						{
							this.state.year.map((attr, idx) => (
								<option key={idx} value={attr} data-placement="right" title={attr}>{attr}</option>
							))
						}
					</select>
				</div>
				{this.state.loading ? <LoadingGauge/> :
					<div>
						<ForPDIChart
							chartType="LineChart"
							data={this.state.data}
							options={this.state.options}
							graph_id="LineChart-Incident"
							width="100%"
							height="300px"
							legend_toggle={false}
							pageSize={this.state.pageSize}
							total={this.state.total}
							onChangePage={this.getInfo}
							chartEvents={this.state.chartEvents}
						/>
						<div className="colaborator-goal-performance-legend">
							<span onClick={this.onThreatsChange}>
								<span className="legend-item icon-link">
									<input
										type="text"
								   	disabled
										className={"icon-link marginLeft10 legend-risk " + (this.state.threats ? "threat" : "")}
									/>
									{Messages.getEditable("label.risk.threats", "fpdi-nav-label")}
								</span>
							</span>
							<span onClick={this.onOpportunitiesChange}>
								<span className="legend-item icon-link">
									<input
										type="text"
								   	disabled
										className={"icon-link marginLeft10 legend-risk " + (this.state.opportunities ? "opportunities" : "")}
									/>
									{Messages.getEditable("label.risk.opportunities", "fpdi-nav-label")}
								</span>
							</span>
						</div>
					</div>
				}
			</div>
		);
	}

});
