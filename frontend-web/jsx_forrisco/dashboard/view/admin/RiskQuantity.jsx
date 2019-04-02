import React from "react";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import GraphicFilter from "forpdi/jsx_forrisco/dashboard/view/graphic/GraphicFilter.jsx";
import Graphic from "forpdi/jsx_forrisco/dashboard/view/graphic/Graphic.jsx";

export default React.createClass({

	getInitialState() {
		return {
			plan:null,
			risks: null,
			threats: null,
			riskLevelModel: null,
			risk_level_active:[],
			page2: false,

			unit: null,
			units: [],
			allRisks: null,
			year: null,
			years: [],
			loading: true,
			displayGraph: false,
			data:[],
			risk_history:[]
		}
	},

	componentWillReceiveProps(newProps){
		var me = this;
		this.state.risks=newProps.risks;
		this.state.unit=newProps.unit;
		this.state.plan= newProps.plan;
		this.state.threats= newProps.threats;
		this.state.riskLevelModel= newProps.riskLevel;
		this.state.allRisks= newProps.allRisks;
		this.state.loading=false;
		this.state.year=(new Date).getFullYear()
		this.state.displayGraph=false

		if(newProps.units.length>0 && this.state.units != newProps.units){
			this.state.units= newProps.units;
			this.refresh()
		}
	},

	refresh(){
		RiskStore.dispatch({
			action:RiskStore.ACTION_FIND_HISTORY_BY_UNIT,
			data:{unit: this.state.unit,
				plan:this.state.plan.id }
		})
	},

	componentDidMount() {
		var me = this;

		if(EnvInfo && EnvInfo.company == null){
			me.setState({
				loading: false
			});
		}

		RiskStore.on("historyByUnit",(model) =>{
			if(model.success){
				this.setState({
					risk_history:model.data
				})
			}
		})
	},

 	componentWillUnmount() {
		RiskStore.off(null, null, this);
		StructureStore.off(null, null, this);
	},

  	changePageTo1(){
		this.setState({
			page2:false
		})
	},

	changePageTo2(){
		this.setState({
			page2:true
		})
	},


	listRisk(risk_level){
		this.state.risk_level_active=[]
		this.state.risk_level_active.push(risk_level)
		this.setState({
			risk_level_active:this.state.risk_level_active,
			displayGraph:true
		})
	},

	getPanel(){
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

			let className = `dashboard-risk-board ${color}`;

			if(i < 4){
				if (i % 2 === 0 && i !== 0){
					panel1.push(<br/>)
				} else if (i % 2 === 1) {
					className += ' dashboard-risk-panel-right';
				}
				if (i >= 2) {
					className += ' dashboard-risk-panel-bottom';
				}
				panel1.push( <span className={className}>

						<GraphicFilter
							level={level}
							onClick={this.listRisk}
							color={color}
							quantity={true}
						/>

					<div className="dashboard-risk-number">{quantity}</div>
					<div className="dashboard-risk-text">{"Risco "+ this.state.riskLevelModel.data[i].level}</div>
				</span>)
			}else{

				if ( i % 2 === 0 && i !== 0){
					panel2.push(<br/>)
				} else if (i % 2 === 1) {
					className += ' dashboard-risk-panel-right';
				}

				panel2.push( <span className={className}>
					<a>
					<GraphicFilter
							level={level}
							onClick={this.listRisk}
							color={color}
							quantity={true}
						/>
					</a>
					<div className="dashboard-risk-number">{quantity}</div>
					<div className="dashboard-risk-text">{"Risco "+ this.state.riskLevelModel.data[i].level}</div>
				</span>)
			}
		}

		if(this.state.riskLevelModel.data.length>4){
			var arrow = <div><br/>
							<div className={"mdi mdi-arrow-left-bold-circle icon-link"} onClick={this.changePageTo1}/>
							<div className={"mdi mdi-arrow-right-bold-circle icon-link"}  style={{float: "right"}} onClick={this.changePageTo2}/>
						</div>
			//panel1.push(arrow)
			//panel2.push(arrow)
		}
		if(!this.state.page2){
			return <div>
					<div>
						{panel1}
					</div>
					<div     style={{position: "relative", top: "200px"}}>
						{arrow}
					</div>
				</div>
		}else{
			return <div>
					<div>
						{panel2}
					</div>
					<div     style={{position: "relative", top: "200px"}}>
						{arrow}
					</div>
				</div>
		}
	},

	setdisplayGraph(bool){
		this.state.displayGraph=bool
	},

	render() {
		if(this.state.risks==null){
			return (<LoadingGauge/>)
		}

		return (
			<div className="frisco-dashboard frisco-dashboard-right" style={{ position: 'relative' }}>
				{this.getPanel()}
				{
					this.state.displayGraph
					&&
					<Graphic
						title={Messages.get("label.risk.history").toUpperCase()}
						unit={this.state.unit}
						units={this.state.units}
						level={this.state.riskLevelModel.data}
						levelActive={this.state.risk_level_active}
						history={this.state.risk_history}
						displayGraph={this.setdisplayGraph}
					/>
				}
			</div>
		);
	}

});
