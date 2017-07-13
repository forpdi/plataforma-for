import React from "react";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
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
      hide:false,
      objectives: [],
      actionBudgets: [],
      selectedactionBudget:-1,
      selectedObjectives:-1,
      loading: true,
      profile: this.props.profile,
      plan:this.props.plan,
      subPlan:this.props.subPlan,
       options:{
          title: Messages.get("label.generalBudget"),
          hAxis: {title: '', minValue: 0, maxValue: 15},
          vAxis: {title: 'Valor (R$)', minValue: 0, maxValue: 15},
          legend: 'none',
          bar: {groupWidth: '50%'}
        }
    };
  },  

  componentWillReceiveProps(newProps){
    var me = this;
    if(this.isMounted()) {
      me.setState({
        objectives: [],
        actionBudgets: [],
        plan:newProps.plan,
        subPlan:newProps.subPlan,
        loading: true
      });

        StructureStore.dispatch({
          action: StructureStore.ACTION_GET_OBJECTIVES,
          data: {            
            planId: newProps.subPlan.id,
            macroId: newProps.plan.id
          }
        });

        DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GENERAL_BUDGETS,
          data: {
            macro: (newProps.plan != -1)?(newProps.plan.id):(null),
            plan:(newProps.subPlan != -1)?(newProps.subPlan.id):(null)          
          }
        });
    }    
  },

  componentDidMount() {
    var me = this;

    if(EnvInfo && EnvInfo.company == null){
        me.setState({
            loading: false
        });
    }

    StructureStore.on("objectivesretrivied", (model) => { 
      if(this.isMounted()) {
        me.setState({
          objectives: model.data          
        });
        me.forceUpdate();
      }
    }, me);

    BudgetStore.on("find", (model, raw, opts) => {
      if(this.isMounted()) {
        me.setState({
          actionBudgets: raw,
          loading: false
        });
        DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GENERAL_BUDGETS,
          data: {
              macro: this.props.plan.id,
              plan:(this.state.subPlan != -1)?(this.state.subPlan.id):(null),
              objective: this.state.selectedObjectives.id
          }
        });
      }
   },me);


     DashboardStore.on("generalbudgetsretrivied", (store) => {
  	   var plannedTooltipText = "R$ "+this.formatBR(this.formatEUA(store.data.planned));         
       var conductedTooltipText = "R$ "+this.formatBR(this.formatEUA(store.data.conducted));
       var committedTooltipText = "R$ "+this.formatBR(this.formatEUA(store.data.committed));
     	

        if(this.isMounted()) {
          this.setState({      
              data: [ 
                ['Element', 'Verba', { role: 'style' }, {role: 'tooltip'}],
                [Messages.get("label.budget.planned"), store.data.planned, '#A7E2D2', plannedTooltipText],            
                [Messages.get("label.budget.committed"), store.data.committed , '#3AB795', committedTooltipText],
                [Messages.get("label.budget.conducted"), store.data.conducted , '#76D3BA', conductedTooltipText]            
              ],
              loading: false
          });
        }
    });

    if(this.isMounted()) {    	
    	this.setState({
        options:{
          title: Messages.get("label.generalBudget"),
          hAxis: {title: '', minValue: 0, maxValue: 15},
          vAxis: {title: 'Valor (R$)', minValue: 0, maxValue: 15},
          legend: 'none',
          bar: {groupWidth: '50%'}
        },
        data: [ 
        	['Element', 'Verba', { role: 'style' }, {role: 'tooltip'}],
       		[Messages.get("label.budget.planned"), 0, '#A7E2D2',"R$ "+ this.formatBR(this.formatEUA(0))],            
        	[Messages.get("label.budget.committed"), 0 , '#3AB795', "R$ "+this.formatBR(this.formatEUA(0))],
        	[Messages.get("label.budget.conducted"), 0 , '#76D3BA', "R$ "+this.formatBR(this.formatEUA(0))]            
      	]      
      });
    }
  },

  componentWillUnmount() {
    DashboardStore.off(null, null, this);
    BudgetStore.off(null, null, this);
    StructureStore.off(null, null, this);
  },

  hideFields() {
    this.setState({
      hide: !this.state.hide
    })
  },

  objectiveChange(data){
    //Verifica se a opção dos planos macro selecionada é a de todos os planos
    if(this.refs.selectObjectives.value == -1){
      this.setState({
        selectedObjectives:-1,
        actionBudgets: [],
        loading: true
      });
      DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GENERAL_BUDGETS,
          data: {
            macro: this.props.plan.id,
            plan:(this.state.subPlan != -1)?(this.state.subPlan.id):(null)
          }
      });
    } else {
      this.setState({
        selectedObjectives:this.state.objectives[this.refs.selectObjectives.value],
        loading: true
      });      
      BudgetStore.dispatch({
        action: BudgetStore.ACTION_FIND,
        data: {
          levelId: this.state.objectives[this.refs.selectObjectives.value].id
        }
      });
    }
  },

  subActionChange(){
    if(this.refs.selectSubAction.value == -1){
      this.setState({
        selectedactionBudget:-1,
        loading: true
      });
      DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GENERAL_BUDGETS,
          data: {
            macro: this.props.plan.id,
            plan:(this.state.subPlan != -1)?(this.state.subPlan.id):(null),
            objective: this.state.selectedObjectives.id
          }
      });
    } else {
      this.setState({
        selectedactionBudget:this.state.actionBudgets[this.refs.selectSubAction.value],
        loading: true
      });
      
      DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GENERAL_BUDGETS,
          data: {
            subAction: this.state.actionBudgets[this.refs.selectSubAction.value].subAction,
            objective: this.state.selectedObjectives.id
          }
      });
    }
  },

  formatEUA(num){
    var n = num.toFixed(2).toString(), p = n.indexOf('.');
    return n.replace(/\d(?=(?:\d{3})+(?:\.|$))/g, function($0, i){
        return p<0 || i<p ? ($0+',') : $0;
    });
  },

  formatBR(str){
   
    var x = str.split('.')[0];
    x = this.replaceAll(x,",",".");
    var decimal = str.split('.')[1];
    if(decimal == undefined){
      decimal = '00';
    }
    return x + "," + decimal;
  },

  replaceAll(str, needle, replacement) {
    var i = 0;
    while ((i = str.indexOf(needle, i)) != -1) {
        str = str.replace(needle, replacement);
    }
    return str;
  },


  render() {
    return (
      <div id = {!this.state.hide ? "budgetSection" : ""} className="panel panel-default">     
        <div className="panel-heading dashboard-panel-title">
            <div className="performance-strategic-btns floatRight">
                <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
              </div>
            <div>
              <b className="budget-graphic-title"> {Messages.get("label.budget")} </b>
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
        {!this.state.hide ?
            <div className={this.state.profile.ADMIN ? 'paddingTopBottom35' :  'paddingBottom84'}>
                {this.state.loading ? <LoadingGauge/> : 
                    <ForPDIChart 
                    chartType="ColumnChart"
                    data={this.state.data}
                    options={this.state.options}
                    graph_id="ColumnChart-Budget"
                    width={"100%"}
                    height={"300px"}
                    legend_toggle={true} />
                }               
            </div>
        :""}
    </div>);
    }
});