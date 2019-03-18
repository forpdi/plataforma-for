import React from "react";
import _ from 'underscore';
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import GraphicFilter from "forpdi/jsx_forrisco/dashboard/view/graphic/GraphicFilter.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";


export default React.createClass({

	getInitialState() {
		return {
			planId: this.props.planId,
			unit: this.props.unit,
			year: new Date().getFullYear(),
			data: [],
			history: this.props.history,
			loadingGraph: true,
			updated: false,
			levelsActive: [
				{
					level: "em dia",
					color: 4,
				},
				{
					level: "próximo a vencer",
					color: 3,
				},
				{
					level: "atrasado",
					color: 0,
				},
			],
		};
	},

	componentDidMount() {
		this.refreshMonitorsHistory(this.state.year, this.state.unit);
	},

	componentWillReceiveProps(newProps) {
		if (this.state.unit !== newProps.unit || this.state.planId !== newProps.planId) {
			const newState = {
				planId: newProps.planId,
				unit: newProps.unit,
				loadingGraph: true,
			}
			if (this.state.planId !== newProps.planId) {
				_.assign(newState, { history: newProps.history })
			}
			this.setState(newState);
			this.props.displayGraph(false);
		} else {
			this.refreshMonitorsHistory(this.state.year, this.state.unit);
		}
	},

	onUnitChange(e) {
		const unit = parseInt(e.target.value);
		this.setState({
			unit,
			loadingGraph: false,
		});
		this.refreshMonitorsHistory(this.state.year, unit);
	},

	onYearChange(e) {
		year = e.target.value;
		this.setState({
			year,
			loadingGraph: false,
		});

		this.refreshMonitorsHistory(year, this.state.unit);
	},

	refreshMonitorsHistory(year, unitId) {
		const historyGroupedByUnit = this.getHistoryGroupedByUnit();
		const historyGroupedByDate = this.getHistoryGroupedByDate(historyGroupedByUnit);

		const monthList = [
			'jan', 'fev', 'mar', 'abr', 'mai', 'jun', 'jul', 'ago', 'set', 'out', 'nov', 'dez'
		];
		const estadoMapIndex = {
			'em dia': 0,
			'próximo a vencer': 1,
			'atrasado': 2,
		};
		const currentDate = new Date();
		const maxMonth = year === currentDate.getFullYear()
			? currentDate.getMonth()
			: 11;
		const levelActiveIdxs = _.map(this.state.levelsActive, value => this.getLevelIdxByLevel(value.level));
		const data = [['mês', ..._.map(this.state.levelsActive, value => value.level)]];
		for (let monthIdx = 0; monthIdx <= maxMonth; monthIdx++) {
			const monthData = [0, 0, 0];
			if (!unitId || unitId === -1) { // adiciona os dados para todas as unidades
				_.mapObject(historyGroupedByDate, dateValue => {
					const yearHistory = dateValue[year];
					_.forEach(yearHistory, value => {
						if (value.month == monthIdx) {
							const estadoIdx = estadoMapIndex[value.estado];
							monthData[estadoIdx] += value.quantity;
						}
					});
				});
			} else { // adiciona os dados para uma unidade determinada
				const yearHistory = historyGroupedByDate[unitId][year];
				_.forEach(yearHistory, value => {
					if (value.month == monthIdx) {
						const estadoIdx = estadoMapIndex[value.estado];
						monthData[estadoIdx] += value.quantity;
					}
				});
			}
			// seta o prefixo do mes na primeira posicao
			monthData.unshift(monthList[monthIdx]);
			// ajusta para que somente os levels selecionados sejam mostrados
			data.push(_.filter(monthData, (value, idx) => idx === 0 || levelActiveIdxs.includes(idx - 1)));
		}
		this.state.data = data
		this.state.loadingGraph = false
		this.state.updated = true
		this.setState({
			data: data,
			loadingGraph: false,
		});
	},

	getHistoryGroupedByUnit() {
		const historyGrouped = {};
		_.forEach(this.state.history, historyValue => {
			const { unit } = historyValue;
			const unitId = unit.parent ? unit.parent.id : unit.id;
			if (!historyGrouped[unitId]) {
				historyGrouped[unitId] = [historyValue];
			} else {
				historyGrouped[unitId].push(historyValue);
			}
		});
		return historyGrouped;
	},

	getHistoryGroupedByDate(historyGroupedByUnit) {
		const historyGrouped = {};
		_.mapObject(historyGroupedByUnit, (unitGroupedValue, key) => {
			const groupedByDate = {};
			_.forEach(unitGroupedValue, historyValue => {
				const { year } = historyValue;
				if (!groupedByDate[year]) {
					groupedByDate[year] = [historyValue];
				} else {
					groupedByDate[year].push(historyValue);
				}
			});
			historyGrouped[key] = groupedByDate;
		});
		return historyGrouped;
	},

	uniques(array) {
		return array.filter(function (value, index, self) {
			return self.indexOf(value) === index;
		});
	},

	toggleLevel(clickedLevel) {
		const levelSearched = _.find(this.state.levelsActive, value => value.level === clickedLevel.level);
		if (levelSearched) { // hide
			if (this.state.levelsActive.length === 1) {
				// para que pelo menos um level seja mostrado
				return;
			}
			this.setState({
				levelsActive: _.filter(this.state.levelsActive, value => value.level !== clickedLevel.level)
			}, () => this.refreshMonitorsHistory(this.state.year, this.state.unit));
		} else { // show
			const levelIdx = this.getLevelIdxByLevel(clickedLevel.level);
			const { levelsActive } = this.state;
			levelsActive.splice(levelIdx, 0, clickedLevel)
			this.setState({
				levelsActive,
			}, () => this.refreshMonitorsHistory(this.state.year, this.state.unit));
		}
	},

	getLevelIdxByLevel(estado) {
		switch (estado) {
			case 'em dia': return 0;
			case 'próximo a vencer': return 1;
			case 'atrasado': return 2;
			default: return null;
		}
	},


	Graph() {
		var years = [(new Date).getFullYear()]

		for (var i = 0; i < this.state.history.length; i++) {
			years.push(this.state.history[i].year)
		}

		years = this.uniques(years).sort().reverse()
		var lines = []
		var levels = []
		var color

		for (var j = 0; j < this.props.level.length; j++) {
			color = "Cinza"
			for (var i = 0; i < this.state.levelsActive.length; i++) {
				if (this.state.levelsActive[i].level == this.props.level[j].level) {
					switch (this.props.level[j].color) {
						case 0: color = "Vermelho"; break;
						case 1: color = "Marron"; break;
						case 2: color = "Amarelo"; break;
						case 3: color = "Laranja"; break;
						case 4: color = "Verde"; break;
						case 5: color = "Azul"; break;
						default: color = "Cinza"; break;
					}
				}
			}

			levels.push(<GraphicFilter
				level={this.props.level[j]}
				onClick={this.toggleLevel}
				color={color}
				quantity={false}
			/>)
		}

		for (var i = 0; i < this.state.levelsActive.length; i++) {
			switch (this.state.levelsActive[i].color) {
				case 0: lines.push("red"); break;
				case 1: lines.push("brown"); break;
				case 2: lines.push("yellow"); break;
				case 3: lines.push("orange"); break;
				case 4: lines.push("green"); break;
				case 5: lines.push("blue"); break;
				default: lines.push("gray"); break;
			}
		}

		var max = 0
		for (var i = 0; i < this.state.data.length; i++) {
			for (var j = 1; j < this.state.data[i].length; j++) {
				if (this.state.data[i][j] > max) {
					max = this.state.data[i][j]
				}
			}
		}

		var options = {
			hAxis: { title: "Tempo", minValue: 1, maxValue: 12 },
			vAxis: { title: 'Quantidade', minValue: 1, maxValue: max },
			// height: 250,
			// width: 658,
			// chartArea:{width:"471",height:"155"},
			legend: 'none',
			colors: lines,
		}

		return (<div>
			<div className="dashboard-panel-title">
				<span className="frisco-containerSelect"> {Messages.get("label.units")}
					<select
						onChange={this.onUnitChange}
						value={this.state.unit}
						className="form-control dashboard-select-box-graphs marginLeft10"
						id="selectUnits"
					>
						<option value={-1} data-placement="right" title={Messages.get("label.viewAll_")}>
							{Messages.get("label.viewAll_")}
						</option>
						{
							this.props.units.map((attr) => {
								return (
									<option key={attr.id} value={attr.id} data-placement="right" title={attr.name} >
										{(attr.name.length > 20) ? (string(attr.name).trim().substr(0, 20).concat("...").toString()) : (attr.name)}
									</option>
								);
							})
						}
					</select>
				</span>
			</div>
			<div style={{ "text-align": "center" }}>
				<select onChange={this.onYearChange} className="form-control dashboard-select-box-graphs marginLeft10" id="selectYear" >
					{
						years.map((attr, idx) => {
							return (
								<option key={idx} value={attr} data-placement="right" title={attr} >{attr}</option>
							);
						})
					}
				</select>
			</div>

			<div>
				{
					!this.loadingGraph &&
					<div>
						<ForPDIChart
							chartType="LineChart"
							data={this.state.data}
							options={options}
							graph_id={"LineChart-" + this.props.title}
							width='100%'
							height='250'
							// chartArea={{left:110,top:0,width:"471",height:"155"}}
							legend_toggle={true}
						/>
						<div className="colaborator-goal-performance-legend" key={this.props.title}>
							{levels}
						</div>
					</div>
				}
			</div>
		</div>);
	},

	render() {
		return (
			<div>
				{
					!this.state.loadingGraph &&
					Modal.GraphHistory(this.props.title, this.Graph())
				}
			</div>
		);
	}

});
