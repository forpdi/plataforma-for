import React from "react";
import {Chart} from 'react-google-charts';
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import moment from 'moment';
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
  getInitialState() {
    return {
      hide:false,
      plan: this.props.plan,
      subPlan: this.props.subPlan,
      levelInstance: this.props.levelInstance,
      goalsInformation:null,
      sortIconStatus: ["","","","","","",""]
    };
  },

  getInfos(page, pageSize, opt){
    opt = opt || this.props;
    DashboardStore.dispatch({
        action: DashboardStore.ACTION_GET_COMMUNITY_INFO_TABLE,
        data: {
            macro: (opt.plan == -1 ? null : opt.plan.id),
            plan: (opt.subPLan == -1 ? null : opt.subPlan.id),
            levelInstance: (opt.levelInstance == -1 ? null : opt.levelInstance.id),
            page: page,
            pageSize: pageSize
        }
    });
  },

  componentWillReceiveProps(newProps){
	this.setState({
	plan: newProps.plan,
	subPlan:newProps.subPlan,
	levelInstance: newProps.levelInstance
	});
	this.getInfos(1, 5, newProps);
  },

  componentDidMount() {
    var me = this;
    this.getInfos(1, 5, this.state);

    DashboardStore.on("communityinfotableretrivied", (store) =>{
		me.setState({
			goalsInformation: store.data,
			tamGoalsInformation: store.total
		});
    });
  },

  componentWillUnmount() {
      DashboardStore.off(null, null, this);
      StructureStore.off(null, null, this);
      PlanMacroStore.off(null, null, this);
  },

  hideFields() {
    this.setState({
      hide: !this.state.hide
    })
  },

  quickSortByStrategicAxisName(sorting){
    var data = [];
    var iconStatusAux = [sorting, "", "", "", "", "", ""];

    data = this.state.goalsInformation;
    if(sorting == "asc"){

      data.sort(function(a, b) {
         if (a.strategicAxisName < b.strategicAxisName) {
          return -1;
         }
         if (a.strategicAxisName > b.strategicAxisName) {
          return 1;
         }
         return 0;
      })
    } else if(sorting == "desc"){
      data.sort(function(a, b) {
         if (a.strategicAxisName < b.strategicAxisName) {
          return 1;
         }
         if (a.strategicAxisName > b.strategicAxisName) {
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
    var iconStatusAux = ["", sorting, "", "","", "", ""];

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

  quickSortByIndicatorName(sorting){
    var data = [];
    var iconStatusAux = ["", "", sorting, "","", "", ""];

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

  quickSortByGoalName(sorting){
    var data = [];
    var iconStatusAux = ["", "", "", sorting, "", "", ""];

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
    var iconStatusAux = ["", "", "", "", sorting, "", ""];

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
      var dashboardTitle = "";
      if (this.state.levelInstance == -1 && this.state.subPlan == -1)
        dashboardTitle = " - Todos os " +Messages.get("label.goalsPlan")
      else if (this.state.levelInstance == -1)
        dashboardTitle = " - "+this.state.subPlan.name;
      else if (this.state.levelInstance.parent == null)
        dashboardTitle = " - "+this.state.levelInstance.name;
      else if (this.state.levelInstance.level.objective)
        dashboardTitle = " - "+this.state.levelInstance.name;
      else if (this.state.levelInstance.level.indicator)
        dashboardTitle = " - "+this.state.levelInstance.name;

      return (
        <div className="panel panel-default">
          <div className="panel-heading">
            <b className="budget-graphic-title"> <span className="fpdi-nav-label" title = {(Messages.get("label.goalsTable")+dashboardTitle).length > 70 ? (Messages.get("label.goalsTable")+dashboardTitle) : ""}> {(dashboardTitle.length) <= 70?(Messages.get("label.goalsTable")+dashboardTitle):((Messages.get("label.goalsTable")+dashboardTitle).split("",70).concat(" ..."))} </span> </b>
              <div className="performance-strategic-btns floatRight">
                <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
              </div>
          </div>
          {!this.state.hide && this.state.goalsInformation != null ?
          <div>
          <table className="dashboard-table table" id="goalsInfoTable">
              <tbody>
                <tr>
                    <th className = "column-goals-perfomance">{Messages.getEditable("label.thematicAxis","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[0] == "desc"?"mdi mdi-sort-descending cursorPointer":
                      (this.state.sortIconStatus[0] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                      onClick={(this.state.sortIconStatus[0] == "" || this.state.sortIconStatus[0] =="desc")
                      ? this.quickSortByStrategicAxisName.bind(this,"asc") :  this.quickSortByStrategicAxisName.bind(this,"desc")} > </span></th>

                    <th className = "column-goals-perfomance">{Messages.getEditable("label.objective","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[1] == "desc"?"mdi mdi-sort-descending cursorPointer":
                      (this.state.sortIconStatus[1] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                      onClick={(this.state.sortIconStatus[1] == "" || this.state.sortIconStatus[1] =="desc")
                      ? this.quickSortByObjectiveName.bind(this,"asc") :  this.quickSortByObjectiveName.bind(this,"desc")} > </span></th>

                    <th className = "column-goals-perfomance">{Messages.getEditable("label.indicator","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[2] == "desc"?"mdi mdi-sort-descending cursorPointer":
                      (this.state.sortIconStatus[2] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                      onClick={(this.state.sortIconStatus[2] == "" || this.state.sortIconStatus[2] =="desc")
                      ? this.quickSortByIndicatorName.bind(this,"asc") :  this.quickSortByIndicatorName.bind(this,"desc")} > </span></th>

                    <th id = "column-goals-perfomance">{Messages.getEditable("label.goalSing","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[3] == "desc"?"mdi mdi-sort-descending cursorPointer":
                      (this.state.sortIconStatus[3] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                      onClick={(this.state.sortIconStatus[3] == "" || this.state.sortIconStatus[3] =="desc")
                      ? this.quickSortByGoalName.bind(this,"asc") :  this.quickSortByGoalName.bind(this,"desc")} > </span></th>

                    {EnvInfo.company.showMaturity ?
                      <th className = "column-goals-perfomance">{Messages.getEditable("label.maturity","fpdi-nav-label")}
                        <span className={this.state.sortIconStatus[4] == "desc"?"mdi mdi-sort-descending cursorPointer":
                        (this.state.sortIconStatus[4] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                        onClick={(this.state.sortIconStatus[4] == "" || this.state.sortIconStatus[4] =="desc")
                        ? this.quickSortByFinishDate.bind(this,"asc") :  this.quickSortByFinishDate.bind(this,"desc")} > </span></th>
                    : null}

                    <th className = "column-goals-perfomance  column-goals-perfomance-action"> {Messages.getEditable("label.goals.expected","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[5] == "desc"?"mdi mdi-sort-descending cursorPointer":
                      (this.state.sortIconStatus[5] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                      onClick={(this.state.sortIconStatus[5] == "" || this.state.sortIconStatus[5] =="desc")
                      ? this.quickSortByExpected.bind(this,"asc") :  this.quickSortByExpected.bind(this,"desc")} > </span></th>

                    <th className = "column-goals-perfomance  column-goals-perfomance-action"> {Messages.getEditable("label.titleReached","fpdi-nav-label")}
                      <span className={this.state.sortIconStatus[6] == "desc"?"mdi mdi-sort-descending cursorPointer":
                      (this.state.sortIconStatus[6] =="asc" ? "mdi mdi-sort-ascending cursorPointer" : "mdi mdi-sort cursorPointer")}
                      onClick={(this.state.sortIconStatus[6] == "" || this.state.sortIconStatus[6] =="desc")
                      ? this.quickSortByReached.bind(this,"asc") :  this.quickSortByReached.bind(this,"desc")} > </span></th>
                </tr>

                {this.state.goalsInformation && this.state.goalsInformation.length>0 ?
                  this.state.goalsInformation.map((goal, idx) => {
                    return (<tr key={"goal-"+idx} name={"goal-"+idx}>
                      <td className="fdpi-table-cell">{this.state.goalsInformation[idx].strategicAxisName}</td>
                      <td className="fdpi-table-cell">{this.state.goalsInformation[idx].objectiveName}</td>
                      <td className="fdpi-table-cell">{this.state.goalsInformation[idx].indicatorName}</td>
                      <td className="fdpi-table-cell">{this.state.goalsInformation[idx].goalName}</td>

                      {EnvInfo.company.showMaturity ?
                        <td className="fdpi-table-cell">{this.state.goalsInformation[idx].finishDate ?
                          this.state.goalsInformation[idx].finishDate.split(" ")[0] : ""}</td>
                      : null}

                      <td className="fdpi-table-cell fdpi-table-cell-center">{this.state.goalsInformation[idx].expected}</td>
                      <td className="fdpi-table-cell fdpi-table-cell-center">{this.state.goalsInformation[idx].reached}</td>
                    </tr>);
                  })
                :
                  <tr>
                    <td id = "GoalsInformationTable"> {Messages.get("label.noRegister")} </td>
                    <td> </td>
                    <td> </td>
                    <td> </td>
                    <td> </td>
                    <td> </td>
                    <td> </td>
                  </tr>
                }
             </tbody>
          </table>
          <TablePagination
            total={this.state.tamGoalsInformation}
            onChangePage={this.getInfos}
            tableName={"communityInfo-table"}
          />
        </div>
        :""}
        </div>
      );
  }
});
