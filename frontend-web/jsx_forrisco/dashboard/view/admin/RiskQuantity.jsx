import React from "react";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import BudgetStore from "forpdi/jsx/planning/store/Budget.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({

  getInitialState() {
    return {
	  plan:null,
	  risks: null,
	  threats: null,
	  policyModel: null,
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
		threats: newProps.threats,
		policyModel: newProps.policyModel,
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

  getTables(){
	var panel=[]
	var panel2=[]

	if(this.state.riskLevelModel == null){
		return
	}

	var top=Math.ceil(this.state.riskLevelModel.data.length/2)

	for(var i=0; i<this.state.riskLevelModel.data.length;i++){

		var color=null
		var quantity=0
		var c=this.state.riskLevelModel.data[i].color

		switch(c) {
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
			if(i==top+1){
				panel.push(<br/>)
			}
			panel.push( <span className={"dashboard-risk-board "+color}>
				<a href="#">
					<div className="mdi mdi-chart-line" style={ color=="Amarelo" ? {"left":"65px","position": "relative", "color":"black"} : {"left":"65px", "position": "relative", "color":"white"}}/>
				</a>
				<div className="dashboard-risk-number">{quantity}</div>
				<div className="dashboard-risk-text">{"Risco "+ this.state.riskLevelModel.data[i].level}</div>
			</span>)
		}else{
			panel2.push( <span className={"dashboard-risk-board  "+color}>
				<a href="#">
					<div className="mdi mdi-chart-line" style={ color=="Amarelo" ? {"left":"65px","position": "relative", "color":"black"} : {"left":"65px", "position": "relative", "color":"white"}}/>
				</a>
				<div className="dashboard-risk-number">{quantity}</div>
				<div className="dashboard-risk-text">{"Risco "+ this.state.riskLevelModel.data[i].level}</div>
			</span>)
		}
	}

	panel.push(<div><br/><div className={"mdi mdi-arrow-right-bold-circle icon-link"} onClick={this.changePage}/></div>)
	panel2.push(<div><br/><div className={"mdi mdi-arrow-left-bold-circle icon-link"} onClick={this.changePage}/></div>)

	if(!this.state.page2){
		return panel
	}else{
		return panel2
	}
  },


  render() {
    return (<div>
		<div>
			{this.getTables()}
		</div>
    </div>);
    }
});
