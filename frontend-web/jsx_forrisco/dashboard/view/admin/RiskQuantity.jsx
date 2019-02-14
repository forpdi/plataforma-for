import React from "react";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

export default React.createClass({

	getInitialState() {
		return {
		plan:null,
		risks: null,
		threats: null,
		riskLevelModel: null,
		tables: 0,
		page2: false,
		loading: true,
		}
	},

	componentWillReceiveProps(newProps){
		var me = this;
		var tables=1;

		me.setState({
			plan: newProps.plan,
			unit: newProps.unit,
			threats: newProps.threats,
			riskLevelModel: newProps.riskLevel,
			risks: newProps.risks,
			loading: false,
			tables: tables
		});
	},

	componentDidMount() {
		var me = this;

		if(EnvInfo && EnvInfo.company == null){
			me.setState({
				loading: false
			});
		}
	},

 	componentWillUnmount() {
		DashboardStore.off(null, null, this);
		BudgetStore.off(null, null, this);
		StructureStore.off(null, null, this);
	},

  	changePage(){
		this.setState({
			page2:!this.state.page2
		})
	},


	onUnitChange(){
		console.log("onUnitChange")
	},

	onYearChange(){
		console.log("onYearChange")
	},

	renderGraph() {
		var units=[{id:1, name:'nome'}];
		var year=['2019','2018'];
		var loading=false;
		var options = {
			title: '',
			hAxis: {title: "Tempo", minValue: 0, maxValue: 15, },
			vAxis: {title: 'Quantidade', minValue: 0, maxValue: 15},
			legend: true,
			explorer: {axis: 'horizontal'},
			bar: {groupWidth: '50%'},
			colors:["red","blue"],
		}
		var data=[
			['x', 'dogs'],
			[0, 0],
			[1, 10],
			[2, 23],
			[3, 17],
			[4, 18],
			[5, 9],
			[6, 11],
			[7, 27],
			[8, 33],
			[9, 40],
			[10, 32],
			[11, 35],
		  ]
		var thematicAxes=[]
		var	selectedThematicAxes= -1
		var chartEvents= [{
					eventName : 'select',
					callback  : this.onChartClick
						}]
		var	pageSize= 10


		return (<div>
				<div className="dashboard-panel-title">
					<span className="frisco-containerSelect"> {Messages.get("label.units")}
					<select onChange={this.onUnitChange} className="form-control dashboard-select-box-graphs marginLeft10" id="selectUnits" >
						<option value={-1} data-placement="right" title={Messages.get("label.viewAll_")}> {Messages.get("label.viewAll_")} </option>
						{units.map((attr, idy) =>{
							return(
							<option  key={attr.id} value={attr.id} data-placement="right" title={attr.name}>
									{(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
							</option>);})
						}
					</select>
					</span>
				</div>
				<div style={{"text-align": "center"}}>
					<select onChange={this.onYearChange} className="form-control dashboard-select-box-graphs marginLeft10" id="selectYear">
						{year.map((attr, idx) =>{
							return(
							<option key={idx} value={attr} data-placement="right" title={attr}>{attr}</option>);})
						}
					</select>
					</div>
				{loading ? <LoadingGauge/> :
					<div>
						<ForPDIChart
							chartType="LineChart"
							data={data}
							options={options}
							graph_id="LineChart"
							//width="120%"
							//height="100%"
							legend_toggle={false}
							pageSize={this.state.pageSize}
							total={this.state.total}
							onChangePage={this.getInfo}
							//chartEvents={this.state.chartEvents}
							/>
						<div className="colaborator-goal-performance-legend">
							<span className="legend-item"><input type="text"  className="legend-risk-threats marginLeft10" disabled/> {Messages.getEditable("label.risk.threats","fpdi-nav-label")}</span>
							<span className="legend-item"><input type="text"  className="legend-risk-opportunities marginLeft10" disabled/> {Messages.getEditable("label.risk.opportunities","fpdi-nav-label")}</span>
						</div>
					</div>}
			</div>
		);
	},

	listRisk(risk_level){
		console.log("//TODO mostrar lista de riscos", risk_level,this.state.threats, this.state.plan, this.state.unit)
		Modal.GraphHistory(Messages.get("label.risk.history").toUpperCase(),this.renderGraph())
	},

	getTables(){
		var panel1=[]
		var panel2=[]

		if(this.state.riskLevelModel == null){
			return
		}

		//var top=Math.ceil(this.state.riskLevelModel.data.length/2)

		for(var i=0; i<this.state.riskLevelModel.data.length;i++){

			var color=null
			var quantity=0
			var level=this.state.riskLevelModel.data[i]
			switch(this.state.riskLevelModel.data[i].color) {
				case 0: color="Vermelho"; break;
				case 1: color="Marron"; break;
				case 2: color="Amarelo"; break;
				case 3: color="Laranja"; break;
				case 4: color="Verde"; break;
				case 5: color="Azul"; break;
				default: color="Cinza";
			}

			for(var j=0; j<this.state.risks.length; j++){
				if(color == this.state.risks[j].color){
					quantity++
				}
			}

			if(i<4){
				if(i%2 ==0 ){
					panel1.push(<br/>)
				}
				panel1.push( <span className={"dashboard-risk-board "+color}>
					<a>
						<div title="Visualizar histórico" className="mdi mdi-chart-line" style={ color=="Amarelo" ? {"left":"65px","position": "relative", "color":"black"} : {"left":"65px", "position": "relative", "color":"white"}}
						onClick={() => this.listRisk(level)}/>
					</a>
					<div className="dashboard-risk-number">{quantity}</div>
					<div className="dashboard-risk-text">{"Risco "+ this.state.riskLevelModel.data[i].level}</div>
				</span>)
			}else{
				panel2.push( <span className={"dashboard-risk-board  "+color}>
					<a>
						<div title="Visualizar histórico" className="mdi mdi-chart-line" style={ color=="Amarelo" ? {"left":"65px","position": "relative", "color":"black"} : {"left":"65px", "position": "relative", "color":"white"}}
						onClick={() => this.listRisk(level)}/>
					</a>
					<div className="dashboard-risk-number">{quantity}</div>
					<div className="dashboard-risk-text">{"Risco "+ this.state.riskLevelModel.data[i].level}</div>
				</span>)
			}
		}


		if(this.state.riskLevelModel.data.length>4){
			panel1.push(<div><br/><div className={"mdi mdi-arrow-right-bold-circle icon-link"} onClick={this.changePage}/></div>)
			panel2.push(<div><br/><div className={"mdi mdi-arrow-left-bold-circle icon-link"} onClick={this.changePage}/></div>)
		}
		if(!this.state.page2){
			return panel1
		}else{
			return panel2
		}
	},

	render() {
		if(this.state.risks==null){
			return (<LoadingGauge/>)
		}
		return (<div>
			<div>
				{this.getTables()}
			</div>
		</div>);
	}

});
