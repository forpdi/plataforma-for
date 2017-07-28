import React from "react";
import {Chart} from 'react-google-charts';
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import moment from 'moment';
import string from 'string';
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
  getInitialState() {
    return {
      hide:false,
      goalsInformation:null,
      tamGoalsInformation:null,      
      indicator:-1,
      indicators:[], 
      plan: this.props.plan,
      subPlan: this.props.subPlan,
      performance: -1,
      sortIconStatus: ["","","","","","",""]
    };
  },


  componentWillReceiveProps(newProps){
    if(this.isMounted()) {
      this.setState({
        subPlan:newProps.subPlan,
        plan: newProps.plan,
        performance: -1
      });
      this.getInfos(1, 5, newProps)
    }
  },

  getInfos(page, pageSize, opt){
    opt = opt || this.props;
    DashboardStore.dispatch({
        action: DashboardStore.ACTION_GET_OBJECTIVES_INFORMATION,
        data: {
          macro: (opt.plan == -1 ? null : opt.plan.get("id")),
          plan: (opt.subPlan == -1 ? null : opt.subPlan.id)
        }
    });
    DashboardStore.dispatch({
        action: DashboardStore.ACTION_GET_GOALS_INFO_TABLE,
        data: {
          macro: (opt.plan == -1 ? null : opt.plan.get("id")),
          plan: (opt.subPlan == -1 ? null : opt.subPlan.id),
          indicator:(this.refs.selectIndicator.value == -1)?(null):(this.state.indicators[this.refs.selectIndicator.value].id),
          page: page,
          pageSize: pageSize,
          filter: this.refs.selectPerformances.value
        }
    });
  },

  componentDidMount() {
    var me = this;  
    this.getInfos(1, 5);

    DashboardStore.on("goalsinfotableretrivied", (store) =>{        
        if(this.isMounted()) {            
            me.setState({
                goalsInformation: store.data,
                tamGoalsInformation: store.total
            });
        }     
    });

    /*DashboardStore.on("generalObjectivesInformation", (store) =>{     
          me.setState({      
            objectivesInformation: store.data,
            hide:false
          });         
        });*/
    
    StructureStore.dispatch({
      action: StructureStore.ACTION_GET_INDICATORS_MACRO_PLAN
    });
    StructureStore.on("indicatorsByMacroAndPlanRetrivied", (model) => {
        if(this.isMounted()) {  
            this.setState({
                indicators:model.data,
                hide:false
            });
            me.forceUpdate();
        }
    }, me);

    PlanStore.on("find", (store, raw, opts) => {
      StructureStore.dispatch({
        action: StructureStore.ACTION_GET_INDICATORS_MACRO_PLAN,
        data: {
                macroId:(this.state.plan != -1)?(this.state.plan.get("id")):(null),
                planId:(this.state.subPlan != -1)?(this.state.subPlan.id):(null)
            }      
      });
      DashboardStore.dispatch({
        action: DashboardStore.ACTION_GET_GOALS_INFO_COL,
        data: {
            macro: (this.state.plan != -1)?(this.state.plan.get("id")):(null),
            plan:(this.state.subPlan != -1)?(this.state.subPlan.id):(null),
            indicator:(this.state.indicator !=-1)?(this.state.indicator.id):(null)
        }   
      });
    });
    PlanMacroStore.on("find", (store) => {
      StructureStore.dispatch({
          action: StructureStore.ACTION_GET_INDICATORS_MACRO_PLAN
        });
      DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GOALS_INFO_COL
        });

      }, me);
  },

  componentWillUnmount() {
      DashboardStore.off(null, null, this);
      StructureStore.off(null, null, this);
      PlanMacroStore.off(null, null, this);
  },

  onIndicatorSelectChange(){
    this.setState({
      indicator:(this.refs.selectIndicator.value == -1)?(null):(this.state.indicators[this.refs.selectIndicator.value])
    });

    this.getInfos(1, 5);
        
  },

  /*onObjectivesSelectChange(){
      DashboardStore.dispatch({
          action: DashboardStore.ACTION_GET_GOALS_INFO_TABLE, 
          data: {
                macro: (this.state.plan != -1)?this.props.plan.id:(null),
                plan:(this.state.subPlan != -1)?(this.state.subPlan.id):(null),
                objective:(this.refs.selectObjectives.value!=-1)?(this.state.objectivesInformation[this.refs.selectObjectives.value].id):(null)
            }         
      });
    },*/

  onPerformanceSelectChange(){
    this.getInfos(1, 5);
  },

  hideFields() {
    this.setState({
      hide: !this.state.hide
    })
  },

 
  quickSortByIndicatorName(sorting){
    var data = [];
    var iconStatusAux = ["", sorting, "", "","", "", ""];
    
    data = this.state.goalsInformation;
    if(sorting == "asc"){

      data.sort(function(a, b) { 
         if (a.indicatorName < b.indicatorName) {
          return -1;
         }
         if (a.indicatorName > b.indicatorName) {
          return 1;
         }       
         return 0; 
      })
    } else if(sorting == "desc"){
      data.sort(function(a, b) { 
         if (a.indicatorName < b.indicatorName) {
          return 1;
         }
         if (a.indicatorName > b.indicatorName) {
          return -1;
         }       
         return 0; 
      })
    }

    this.setState({
      goalsInformation: data,
      sortIconStatus: iconStatusAux
    })
  },

  quickSortByObjectiveName(sorting){
    var data = [];
    var iconStatusAux = [sorting, "", "", "","", "", ""];
    
    data = this.state.goalsInformation;
    if(sorting == "asc"){

      data.sort(function(a, b) { 
         if (a.objectiveName < b.objectiveName) {
          return -1;
         }
         if (a.objectiveName > b.objectiveName) {
          return 1;
         }       
         return 0; 
      })
    } else if(sorting == "desc"){
      data.sort(function(a, b) { 
         if (a.objectiveName < b.objectiveName) {
          return 1;
         }
         if (a.objectiveName > b.objectiveName) {
          return -1;
         }       
         return 0; 
      })
    }

    this.setState({
      goalsInformation: data,
      sortIconStatus: iconStatusAux
    })
  },

  quickSortByGoalName(sorting){
    var data = [];
    var iconStatusAux = ["", "", sorting, "", "", "", ""];
    
    data = this.state.goalsInformation;
    if(sorting == "asc"){

      data.sort(function(a, b) { 
         if (a.goalName < b.goalName) {
          return -1;
         }
         if (a.goalName > b.goalName) {
          return 1;
         }       
         return 0; 
      })
    } else if(sorting == "desc"){
      data.sort(function(a, b) { 
         if (a.goalName < b.goalName) {
          return 1;
         }
         if (a.goalName > b.goalName) {
          return -1;
         }       
         return 0; 
      })
    }

    this.setState({
      goalsInformation: data,
      sortIconStatus: iconStatusAux
    })
  },

  quickSortByFinishDate(sorting){
    var data = [];
    var iconStatusAux = ["", "", "", sorting];
    
    data = this.state.goalsInformation;
    if(sorting == "desc"){

        data.sort(function(a, b) { 
            var d1, d2;
            if(a.finishDate == null){
                d1 = moment(1, "x"); // setando uma data muito baixa
            }else{
                d1 = moment(a.finishDate,"DD/MM/YYYY");
            }
            if(b.finishDate == null){
                d2 = moment(1, "x"); // setando uma data muito baixa
            }else{
                d2 = moment(b.finishDate,"DD/MM/YYYY");
            }
            if (d1.isBefore(d2)){
                return +1;
            }
            if (d1.isAfter(d2)){
                return -1;
            }       
            return 0; 
        })
    } else if(sorting == "asc"){
        data.sort(function(a, b) { 
            var d1, d2;
            if(a.finishDate == null){
                d1 = moment(1, "x"); // setando uma data muito baixa
            }else{
                d1 = moment(a.finishDate,"DD/MM/YYYY");
            }
            if(b.finishDate == null){
                d2 = moment(1, "x"); // setando uma data muito baixa
            }else{
                d2 = moment(b.finishDate,"DD/MM/YYYY");
            }
            
            
            if (d1.isAfter(d2)){
                return 1;
            }
            if (d1.isBefore(d2)){
                return -1;
            }       
            return 0; 
        })
    }

    this.setState({
      goalsInformation: data,
      sortIconStatus: iconStatusAux
    })
  },

  quickSortByExpected(sorting){
    var data = [];
    var iconStatusAux = ["", "", "", "", "", sorting, ""];
    
    data = this.state.goalsInformation;
    if(sorting == "asc"){

      data.sort(function(a, b) { 
         if (a.expected < b.expected) {
          return -1;
         }
         if (a.expected > b.expected) {
          return 1;
         }       
         return 0; 
      })
    } else if(sorting == "desc"){
      data.sort(function(a, b) { 
         if (a.expected < b.expected) {
          return 1;
         }
         if (a.expected > b.expected) {
          return -1;
         }       
         return 0; 
      })
    }

    this.setState({
      goalsInformation: data,
      sortIconStatus: iconStatusAux
    })
  },

  quickSortByReached(sorting){
    var data = [];
    var iconStatusAux = ["", "", "", "", "", "", sorting];
    
    data = this.state.goalsInformation;
    if(sorting == "asc"){

      data.sort(function(a, b) { 
        if(isNaN(Number(a.reached)) && isNaN(Number(b.reached)) ){
          return 0;
        } else if(isNaN(Number(a.reached))){
          return -1;
        } else if(isNaN(Number(b.reached))){
          return 1;
        }else {
         if (a.reached < b.reached) {
          return -1;
         }
         if (a.reached > b.reached) {
          return 1;
         }       
         return 0; 
       }
      })
    } else if(sorting == "desc"){
      data.sort(function(a, b) { 
        if(isNaN(Number(a.reached)) && isNaN(Number(b.reached)) ){
          return 0;
        } else if(isNaN(Number(a.reached))){
          return 1;
        } else if(isNaN(Number(b.reached))){
          return -1;
        }else {
         if (a.reached < b.reached) {
          return 1;
         }
         if (a.reached > b.reached) {
          return -1;
         }       
         return 0; 
       }
      })
    }

    this.setState({
      goalsInformation: data,
      sortIconStatus: iconStatusAux
    })
  },

  render() {
    if (this.state.tamGoalsInformation <= (0)) {        
        return (
        <div className="panel panel-default">
          <div className="panel-heading">
            <b className="budget-graphic-title"> {Messages.getEditable("label.goals","fpdi-nav-label")} </b>
                <select onChange={this.onIndicatorSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectIndicator">
                        <option value={-1} data-placement="right" title={Messages.get("label.allIndicators")}>{Messages.get("label.allIndicators")} </option>
                        {this.state.indicators.map((attr, idy) =>{
                                  return(<option key={"ind-opt-"+idy} value={idy} data-placement="right" title={attr.name}>
                                                 {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                </option>);
                          })
                        }
                 </select>
                <select onChange={this.onPerformanceSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectPerformances">
                    <option value={-1} data-placement="right" title={Messages.get("label.selectPerformance")}>{Messages.getEditable("label.selectPerformance","fpdi-nav-label")} </option>
                    <option value={1} data-placement="right" title={Messages.get("label.goals.belowMinimum")}>{Messages.getEditable("label.goals.belowMinimum","fpdi-nav-label")}</option>
                    <option value={2} data-placement="right" title={Messages.get("label.goals.belowExpected")}>{Messages.getEditable("label.goals.belowExpected","fpdi-nav-label")}</option>
                    <option value={3} data-placement="right" title={Messages.get("label.goals.reached")}>{Messages.getEditable("label.goals.reached","fpdi-nav-label")}</option>
                    <option value={4} data-placement="right" title={Messages.get("label.goals.aboveExpected")}>{Messages.getEditable("label.goals.aboveExpected","fpdi-nav-label")}</option>
                    <option value={5} data-placement="right" title={Messages.get("label.goals.notStarted")}>{Messages.getEditable("label.goals.notStarted","fpdi-nav-label")}</option>
                </select>                    

              <div className="performance-strategic-btns floatRight">
                <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
              </div>
          </div>
          {!this.state.hide ?
          <div>
          <table className="dashboard-table table goals-dashboard-table">
              <thead>
                <tr>
                    <th id = "column-goals-perfomance">{Messages.getEditable("label.objective","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[0] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                      (this.state.sortIconStatus[0] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                      onClick={(this.state.sortIconStatus[0] == "" || this.state.sortIconStatus[0] =="desc") 
                      ? this.quickSortByObjectiveName.bind(this,"asc") :  this.quickSortByObjectiveName.bind(this,"desc")} > </span></th>
                    
                    <th id = "column-goals-perfomance">{Messages.getEditable("label.indicator","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[1] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                      (this.state.sortIconStatus[1] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                      onClick={(this.state.sortIconStatus[1] == "" || this.state.sortIconStatus[1] =="desc") 
                      ? this.quickSortByIndicatorName.bind(this,"asc") :  this.quickSortByIndicatorName.bind(this,"desc")} > </span></th>
                    
                    <th id = "column-goals-perfomance">{Messages.getEditable("label.goal","fpdi-nav-label")}                    
                      <span className={this.state.sortIconStatus[2] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                      (this.state.sortIconStatus[2] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                      onClick={(this.state.sortIconStatus[2] == "" || this.state.sortIconStatus[2] =="desc") 
                      ? this.quickSortByGoalName.bind(this,"asc") :  this.quickSortByGoalName.bind(this,"desc")} > </span></th>
                    
                    <th id = "column-goals-perfomance">{Messages.getEditable("label.maturity","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[3] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                      (this.state.sortIconStatus[3] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                      onClick={(this.state.sortIconStatus[3] == "" || this.state.sortIconStatus[3] =="desc") 
                      ? this.quickSortByFinishDate.bind(this,"asc") :  this.quickSortByFinishDate.bind(this,"desc")} > </span></th>

                    <th id = "column-goals-perfomance">{Messages.getEditable("label.status","fpdi-nav-label")}</th>

                    <th id = "column-goals-perfomance">{Messages.getEditable("label.goals.expected","fpdi-nav-label")} </th>

                    <th id = "column-goals-perfomance">{Messages.getEditable("label.reached","fpdi-nav-label")}                    
                      <span className={this.state.sortIconStatus[6] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                      (this.state.sortIconStatus[6] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                      onClick={(this.state.sortIconStatus[6] == "" || this.state.sortIconStatus[6] =="desc") 
                      ? this.quickSortByReached.bind(this,"asc") :  this.quickSortByReached.bind(this,"desc")} > </span></th>
                </tr>
              </thead>
              <tbody>
                <tr> 
                  <td id = "GoalsInformationTable"> {this.state.indicator && this.state.indicator.aggregate ? Messages.getEditable("label.aggIndicatorHaveNoGoals","fpdi-nav-label") : Messages.getEditable("label.noRegister","fpdi-nav-label")} </td>
                </tr>
             </tbody>
          </table>
        </div>
        :""}
        </div>
      );
    } else {        
        return (
          <div className="panel panel-default">
            <div className="panel-heading">
              <b className="budget-graphic-title"> {Messages.getEditable("label.goals","fpdi-nav-label")} </b>
              {/*this.state.objectivesInformation != null ?
              <select onChange={this.onObjectivesSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectObjectives">
                            <option value={-1} data-placement="right" title={Messages.get("label.allObjectives")}>{Messages.get("label.allObjectives")} </option>
                            {this.state.objectivesInformation.map((attr, idy) =>{
                                      return(<option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                                         {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}</option>);
                              })
                            }
                          </select> : ""*/}
             
              <select onChange={this.onIndicatorSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectIndicator">
                            <option value={-1} data-placement="right" title={Messages.get("label.allIndicators")}>{Messages.get("label.allIndicators")}</option>
                            {this.state.indicators.map((attr, idy) =>{
                                      return(<option key={"ind-opt-"+idy} value={idy} data-placement="right" title={attr.name}>
                                                     {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                    </option>);
                              })
                            }
                          </select>
              <select onChange={this.onPerformanceSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectPerformances">
                            <option value={-1} data-placement="right" title={Messages.get("label.selectPerformance")}>{Messages.getEditable("label.selectPerformance","fpdi-nav-label")} </option>
                            <option value={1} data-placement="right" title={Messages.get("label.goals.belowMinimum")}>{Messages.getEditable("label.goals.belowMinimum","fpdi-nav-label")}</option>
                            <option value={2} data-placement="right" title={Messages.get("label.goals.belowExpected")}>{Messages.getEditable("label.goals.belowExpected","fpdi-nav-label")}</option>
                            <option value={3} data-placement="right" title={Messages.get("label.goals.reached")}>{Messages.getEditable("label.goals.reached","fpdi-nav-label")}</option>
                            <option value={4} data-placement="right" title={Messages.get("label.goals.aboveExpected")}>{Messages.getEditable("label.goals.aboveExpected","fpdi-nav-label")}</option>
                            <option value={5} data-placement="right" title={Messages.get("label.goals.notStarted")}>{Messages.getEditable("label.goals.notStarted","fpdi-nav-label")}</option>
              </select>                    
              <div className="performance-strategic-btns floatRight">
                <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
              </div>
            </div>
             {!this.state.hide ?
              <div>
              <table className="dashboard-table table goals-dashboard-table">
                  <tbody>
                    <tr>
                        <th id = "column-goals-perfomance">{Messages.getEditable("label.objective","fpdi-nav-label")}
                          <span className={this.state.sortIconStatus[0] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                          (this.state.sortIconStatus[0] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                          onClick={(this.state.sortIconStatus[0] == "" || this.state.sortIconStatus[0] =="desc") 
                          ? this.quickSortByObjectiveName.bind(this,"asc") :  this.quickSortByObjectiveName.bind(this,"desc")} > </span></th>
                        
                        <th id = "column-goals-perfomance">{Messages.getEditable("label.indicator","fpdi-nav-label")}
                          <span className={this.state.sortIconStatus[1] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                          (this.state.sortIconStatus[1] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                          onClick={(this.state.sortIconStatus[1] == "" || this.state.sortIconStatus[1] =="desc") 
                          ? this.quickSortByIndicatorName.bind(this,"asc") :  this.quickSortByIndicatorName.bind(this,"desc")} > </span></th>
                        
                        <th id = "column-goals-perfomance">{Messages.getEditable("label.goal","fpdi-nav-label")}                    
                          <span className={this.state.sortIconStatus[2] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                          (this.state.sortIconStatus[2] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                          onClick={(this.state.sortIconStatus[2] == "" || this.state.sortIconStatus[2] =="desc") 
                          ? this.quickSortByGoalName.bind(this,"asc") :  this.quickSortByGoalName.bind(this,"desc")} > </span></th>
                        
                        <th id = "column-goals-perfomance">{Messages.getEditable("label.maturity","fpdi-nav-label")}
                          <span className={this.state.sortIconStatus[3] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                          (this.state.sortIconStatus[3] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                          onClick={(this.state.sortIconStatus[3] == "" || this.state.sortIconStatus[3] =="desc") 
                          ? this.quickSortByFinishDate.bind(this,"asc") :  this.quickSortByFinishDate.bind(this,"desc")} > </span></th>

                        <th id = "column-goals-perfomance">{Messages.getEditable("label.status","fpdi-nav-label")}</th>

                        <th id = "column-goals-perfomance">{Messages.getEditable("label.goals.expected","fpdi-nav-label")} </th>

                        <th id = "column-goals-perfomance">{Messages.getEditable("label.reached","fpdi-nav-label")}                  
                          <span className={this.state.sortIconStatus[6] == "desc"?"mdi mdi-sort-ascending cursorPointer":
                          (this.state.sortIconStatus[6] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
                          onClick={(this.state.sortIconStatus[6] == "" || this.state.sortIconStatus[6] =="desc") 
                          ? this.quickSortByReached.bind(this,"asc") :  this.quickSortByReached.bind(this,"desc")} > </span></th>
                    </tr>
                   
                    {
                        this.state.goalsInformation.map((goal, idx) => {                            
                           return (
                            <tr key={"goal-"+idx} name={"goal-"+idx}>
                              <td className="fdpi-table-cell">{goal.objectiveName}</td>
                              <td className="fdpi-table-cell">{goal.indicatorName}</td>
                              <td className="fdpi-table-cell">{goal.goalName}</td>
                              <td className="fdpi-table-cell">{goal.finishDate ?
                                    goal.finishDate.split(" ")[0] : ""}</td>
                              <td className="fdpi-table-cell">{goal.deadLineStatus == 3 ? 
                                <div className = "lateGoalInfo">{goal.goalStatus} </div> :
                                <div className = "inDayGoalInfo">{goal.goalStatus} </div>}
                                 {Messages.getEditable("label.lastUpdate","fpdi-nav-label")} {goal.lastModification}</td>
                              <td className="fdpi-table-cell">{goal.expected}</td>
                              <td className="fdpi-table-cell">{goal.reached}</td>
                            </tr>);
                        })
                    }            
                 </tbody>
                </table>
                <TablePagination 
                  total={this.state.goalsInformation == undefined || this.state.goalsInformation == null ? 0 : this.state.tamGoalsInformation}
                  onChangePage={this.getInfos}
                  tableName={"goalsInfoColaborator-table"}
                />
              </div>
            :""}
        </div>);
    }
  }
});