
import React from "react";
import Progress from 'react-progressbar';

import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import moment from 'moment'
var numeral = require('numeral');

export default React.createClass({

    getInitialState(){
        return {
        	plan: null,
			unit:-1,
			units: [],
			risks: [],
			monitors: [],
			monitor:{
				inDay:0,
				closeToMaturity:0,
				late:0,
				notStarted:0,
				Percentage:{inDay:0,
					closeToMaturity:0,
					late:0,
					notStarted:0,}
			},
			loading: true,
        };
    },

	componentWillReceiveProps(newProps){
		var me = this;

		this.setState({
			plan: newProps.plan,
			risks: newProps.risks,
			units: newProps.units,
			loading: true,
			monitors: []
		});

		if(newProps.units.length ==0){
			this.setState({
				loading: false,
			});
		}


		for(var i=0; i<newProps.units.length; i++){
			UnitStore.dispatch({
				action: UnitStore.ACTION_FIND_MONITOR,
				data:newProps.units[i].id
			});
		}

	},

    componentDidMount(){
		var me = this;

		UnitStore.on("findMonitor",(model) =>{
			this.state.monitors=[]

			for(var i=0;i< model.data.length;i++){
				this.state.monitors.push(model.data[i])
			}
			this.setState({
				monitors:this.state.monitors
			})

			this.state.unit=-1
			this.Quantify()

		},me);

	},

	componentWillUnmount() {
		UnitStore.off(null, null, this);
	},

	getRisks(){
		var risks=[]

		if(this.state.unit==-1){
			for(var i=0; i<this.state.risks.length; i++){
				for(var j=0; j<this.state.risks[i].array.length; j++){

					//if((this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "ameaça")
					//|| !this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "oportunidade"){
						risks.push(this.state.risks[i].array[j]);
					//}
				}
			}
		}else{
			for(var i=0; i<this.state.risks.length; i++){
				if(this.state.risks[i].unitid==this.state.unit){
					var array=this.state.risks[i].array
					for(var j=0; j<array.length; j++){
						//if((this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "ameaça")
						//|| !this.state.threats && this.state.risks[i].array[j].type.toLowerCase() == "oportunidade"){
							risks.push(array[j]);
						//}
					}
				}
			}
			return risks;
		}
		return risks;
	},

	onclick(){
		console.log("//TODO historico de monitoramento")
	},

	onUnitChange(evnt){

		this.state.unit=this.refs['selectUnits'].value

		this.Quantify();

		/*this.setState({
			unit:this.refs['selectUnits'].value
		})*/
	},

	Quantify(){

		var monitor={
				inDay:0,
				closeToMaturity:0,
				late:0,
				notStarted:0,
				Percentage:{inDay:0,
					closeToMaturity:0,
					late:0,
					notStarted:0,}
				}

		var risks=this.getRisks();

		for(var i=0;i<risks.length;i++){

			var started=false
			var latestMonitor=null
			var date=null

			for(var j=0;j<this.state.monitors.length;j++){
				if(risks[i].id == this.state.monitors[j].riskId){
					started=true

					if(latestMonitor==null){
						latestMonitor=this.state.monitors[j]
					}else{
						if (moment(latestMonitor.begin,'DD/MM/YYYY hh:mm:ss').unix() <
							moment(this.state.monitors[j].begin,'DD/MM/YYYY hh:mm:ss').unix()){
							latestMonitor=this.state.monitors[j]
						}
					}
				}
			}

			if (started==true){
				var diffDays=((new Date()).getTime()/1000 - moment(latestMonitor.begin,'DD/MM/YYYY hh:mm:ss').unix())/(60*60*24)

				switch(risks[i].periodicity.toLowerCase()){

					case "diária":
						if(diffDays<0.85){monitor.inDay+=1}
						else if(diffDays<1){monitor.closeToMaturity+=1}
						else{monitor.late+=1}
						break;

					case "semanal":
						if(diffDays<6){monitor.inDay+=1}
						else if(diffDays<7){monitor.closeToMaturity+=1}
						else{monitor.late+=1}
						break;

					case "quinzenal":
						if(diffDays<12){monitor.inDay+=1}
						else if(diffDays<15){monitor.closeToMaturity+=1}
						else{monitor.late+=1}
						break;

					case "mensal":
						if(diffDays<24){monitor.inDay+=1}
						else if(diffDays<30){monitor.closeToMaturity+=1}
						else{monitor.late+=1}
						break;


					case "bimestral":
						if(diffDays<48){monitor.inDay+=1}
						else if(diffDays<60){monitor.closeToMaturity+=1}
						else{monitor.late+=1}
						break;

					case "trimestral":
					if(diffDays<72){monitor.inDay+=1}
						else if(diffDays<90){monitor.closeToMaturity+=1}
						else{monitor.late+=1}
						break;

					case "semestral":
						if(diffDays<144){monitor.inDay+=1}
							else if(diffDays<180){monitor.closeToMaturity+=1}
							else{monitor.late+=1}
							break;

					case "anual":
					if(diffDays<288){monitor.inDay+=1}
							else if(diffDays<360){monitor.closeToMaturity+=1}
							else{monitor.late+=1}
							break;
				}
			}else{
				monitor.notStarted+=1
			}
		}

		this.setState({
			monitor:monitor,
			loading:false
		})
	},

	render() {
		var title = Messages.get("label.risk.monitor");
		return (
			<div className={this.props.className}>
				<div className="panel">
					<div className="dashboard-plan-details-header">
						<span title={title}>{title.toUpperCase()}
						</span>
						 <span className="frisco-containerSelect"> {Messages.get("label.units")}
                        <select onChange={this.onUnitChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectUnits">
                            <option value={-1} data-placement="right" title={Messages.get("label.viewAll_")}> {Messages.get("label.viewAll_")} </option>
                            {this.state.units.map((attr, idy) =>{
                                    return(
                                        <option key={attr.id} value={attr.id} data-placement="right" title={attr.name}>
                                            {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                        </option>
                                    );
                                })
                            }
                        </select>
						</span>
					</div>
					<div className="dashboard-risk-details-body" >
					<div className="mdi mdi-chart-line icon-link" style={{float: "right" ,padding: "10px"}} onClick={this.onclick}/>
						{this.state.loading ? <LoadingGauge/> :

						<div  style={{padding: "20px 50px"}}>
							<div className="row">
								<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.inDay}</h1>
									<h4>{"("+numeral(this.state.monitor.Percentage.inDay).format('0,0.00')+"%)"}</h4>
									<p>{Messages.getEditable("label.goals.inDay","fpdi-nav-label")}</p>
								</div>
								<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.closeToMaturity}</h1>
									<h4>{"("+numeral(this.state.monitor.Percentage.closeToMaturity).format('0,0.00')+"%)"}</h4>
									<p>{Messages.getEditable("label.goals.closeToMaturity_","fpdi-nav-label")}</p>
								</div>
								</div>
								<div className="row">
								<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.late}</h1>
									<h4>{"("+numeral(this.state.monitor.Percentage.late).format('0,0.00')+"%)"}</h4>
									<p>{Messages.getEditable("label.goals.late_","fpdi-nav-label")}</p>
								</div>
									<div className="dashboard-risk-goal-info col-sm-4">
									<h1>{this.state.monitor.notStarted}</h1>
									<h4>{"("+numeral(this.state.monitor.Percentage.notStarted).format('0,0.00')+"%)"}</h4>
									<p>{Messages.getEditable("label.goals.notStarted","fpdi-nav-label")}</p>
								</div>
							</div>
						</div>}
					</div>
				</div>
			</div>
		);
	}
});
