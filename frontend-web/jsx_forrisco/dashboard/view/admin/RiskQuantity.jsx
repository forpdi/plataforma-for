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
      	{/*} <div id = {!this.state.hide ? "budgetSection" : ""} className="panel panel-default">
        <div className="panel-heading dashboard-panel-title">
            <div className="performance-strategic-btns floatRight">
                <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
              </div>
            <div>
              <b className="budget-graphic-title"> {Messages.getEditable("label.budget","fpdi-nav-label")} </b>
              <select onChange={this.objectiveChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectObjectives">
              <option value={-1} data-placement="right" title={Messages.get("label.allObjectives")}>{Messages.get("label.allObjectives")} </option>
              {this.state.objectives ?
                this.state.objectives.map((attr, idy) =>{
                  return(<option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                    {(attr.name.length>20)?(string(attr.name).trim().substr(0, 15).concat("...").toString()):(attr.name)}
                    </option>);
                }) : ""
              }
              </select>
              <select  onChange={this.subActionChange} ref="selectSubAction" className="form-control dashboard-select-box-graphs marginLeft10"
               disabled={(this.state.selectedObjectives<0)?("disabled"):("")}>
                <option value={-1} data-placement="right" title="Todas as sub-ações">{Messages.get("label.allSubActions")}</option>
                {(this.state.actionBudgets)?(this.state.actionBudgets.map((attr, idy) =>{
                  return(<option key={attr.id} value={idy} data-placement="right" title={attr.subAction}>{attr.subAction}</option>);
                  }) ):("")
                }
              </select>
            </div>

        </div>
		*/}
		<div>
			{this.getTables()}
		</div>
    </div>);
    }
});
