import React from "react";
import {Chart} from 'react-google-charts';
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import string from 'string';
import TablePagination from "forpdi/jsx/core/widget/TablePagination.jsx"
import {Link} from 'react-router';

export default React.createClass({
  getInitialState() {
	return {
	  hide:true,
	  goalsInformation:null,
	  tamGoalsInformation:null,
	  indicator:-1,
	  indicators:[], 
	  plan: this.props.plan,
	  subPlan: this.props.subPlan,
	  performance: -1,
	  sortIconStatus: ["","","",""]
	};
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
	this.setState({
		page: page
	});
  },

  componentWillReceiveProps(newProps){
	if(this.isMounted()) {
	  this.setState({
		plan:newProps.plan,
		subPlan:newProps.subPlan
	  });
	  this.getInfos(1, 5, newProps);
	}
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

	
	  	this.setState({
			performance: this.refs.selectPerformances.value
	  	})

	  	this.getInfos(1, 5);
	  
	},

  hideFields() {
	this.setState({
	  hide: !this.state.hide
	})
  },

 
  quickSortByIndicatorName(sorting){
	var data = [];
	var iconStatusAux = ["", sorting, "", ""];
	
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
	var iconStatusAux = [sorting, "", "", ""];
	
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
	var iconStatusAux = ["", "", sorting, ""];
	
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

  quickSortByResponsible(sorting){
	var data = [];
	var iconStatusAux = ["", "", "", sorting];
	
	data = this.state.goalsInformation;
	if(sorting == "asc"){

	  data.sort(function(a, b) { 
		 if (a.responsible < b.responsible) {
		  return -1;
		 }
		 if (a.responsible > b.responsible) {
		  return 1;
		 }       
		 return 0; 
	  })
	} else if(sorting == "desc"){
	  data.sort(function(a, b) { 
		 if (a.responsible < b.responsible) {
		  return 1;
		 }
		 if (a.responsible > b.responsible) {
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
  

  render() {

  	if (this.state.tamGoalsInformation != (0)) {
		return (
			<div className="panel panel-default">
				<div className="panel-heading">
		  			<b className="budget-graphic-title"> Metas </b>
		  			{/*this.state.objectivesInformation != null ?
		  			<select onChange={this.onObjectivesSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectObjectives">
						<option value={-1} data-placement="right" title="Todos os objetivos">Todos os objetivos </option>
						{this.state.objectivesInformation.map((attr, idy) =>{
								  return(<option key={attr.id} value={idy} data-placement="right" title={attr.name}>{attr.name}</option>);
						  })
						}
					</select> : ""*/}
		 			<select onChange={this.onIndicatorSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectIndicator">
						<option value={-1} data-placement="right" title="Todos os indicadores">Todos os indicadores </option>
						{this.state.indicators.map((attr, idy) =>{
								  return(<option key={"ind-opt-"+idy} value={idy} data-placement="right" title={attr.name}>
												 {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
								</option>);
						  })
						}
					</select>
		  
		  			<select onChange={this.onPerformanceSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectPerformances">
						<option value={-1} data-placement="right" title="Selecione o desempenho">Selecione o desempenho </option>
						<option value={1} data-placement="right" title="Abaixo do mínimo">Abaixo do mínimo</option>
						<option value={2} data-placement="right" title="Abaixo do esperado">Abaixo do esperado</option>
						<option value={3} data-placement="right" title="Suficiente">Suficiente</option>
						<option value={4} data-placement="right" title="Acima do máximo">Acima do máximo</option>
						<option value={5} data-placement="right" title="Não iniciado">Não iniciado</option>
		  			</select>                    
		  			<div className="performance-strategic-btns floatRight">
						<span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
		  			</div>
				</div>
				{!this.state.hide && this.state.goalsInformation != null ?
		  			<div>
		  				<table className="dashboard-table table" id="goalsInfoTable">
			  				<tbody>
								<tr>
									<th   id = "column-goals-perfomance">Objetivo
					  					<span className={this.state.sortIconStatus[0] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[0] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[0] == "" || this.state.sortIconStatus[0] =="desc") 
					  				? this.quickSortByObjectiveName.bind(this,"asc") :  this.quickSortByObjectiveName.bind(this,"desc")} > </span></th>
					
									<th   id = "column-goals-perfomance">Indicador
					  					<span className={this.state.sortIconStatus[1] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[1] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[1] == "" || this.state.sortIconStatus[1] =="desc") 
					  				? this.quickSortByIndicatorName.bind(this,"asc") :  this.quickSortByIndicatorName.bind(this,"desc")} > </span></th>
					
									<th  id = "column-goals-perfomance">Meta                    
					  					<span className={this.state.sortIconStatus[2] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[2] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[2] == "" || this.state.sortIconStatus[2] =="desc") 
					  				? this.quickSortByGoalName.bind(this,"asc") :  this.quickSortByGoalName.bind(this,"desc")} > </span></th>
					
									<th id = "column-goals-perfomance">Responsável
					  					<span className={this.state.sortIconStatus[3] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[3] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[3] == "" || this.state.sortIconStatus[3] =="desc") 
					  				? this.quickSortByResponsible.bind(this,"asc") :  this.quickSortByResponsible.bind(this,"desc")} > </span></th>

									<th id = "column-goals-perfomance">Status</th>
								</tr>
							{
								this.state.goalsInformation.map((goal, idx) => {									
					   				return (<tr key={"goal-"+idx} name={"goal-"+idx}>
						  				<td className="fdpi-table-cell" title={this.state.goalsInformation[idx].objectiveName.length > 60 ? this.state.goalsInformation[idx].objectiveName : ""}>{this.state.goalsInformation[idx].objectiveName.length > 60 ? this.state.goalsInformation[idx].objectiveName.substr(0,60).concat("...") : this.state.goalsInformation[idx].objectiveName}</td>
						  				<td className="fdpi-table-cell" title={this.state.goalsInformation[idx].indicatorName.length > 60 ? this.state.goalsInformation[idx].indicatorName : ""}>{this.state.goalsInformation[idx].indicatorName.length > 60 ? this.state.goalsInformation[idx].indicatorName.substr(0,60).concat("...") : this.state.goalsInformation[idx].indicatorName}</td>
						  				<td className="fdpi-table-cell" title={this.state.goalsInformation[idx].goalName.length > 60 ? this.state.goalsInformation[idx].goalName : ""}>{this.state.goalsInformation[idx].goalName.length > 60 ? this.state.goalsInformation[idx].goalName.substr(0,60).concat("...") : this.state.goalsInformation[idx].goalName}</td>
						  				<td className="fdpi-table-cell">
						  					<Link
												to={"/users/"+this.state.goalsInformation[idx].idResponsible+"/edit"}
												title={this.state.goalsInformation[idx].responsible}>
												{this.state.goalsInformation[idx].responsible}
											</Link>
						  				</td>
						  				<td className="fdpi-table-cell">{this.state.goalsInformation[idx].deadLineStatus == 3 ? 
										<div className = "lateGoalInfo">{this.state.goalsInformation[idx].goalStatus} </div> :
										<div className = "inDayGoalInfo">{this.state.goalsInformation[idx].goalStatus} </div>}
							 			Última atualização: {this.state.goalsInformation[idx].lastModification}</td>
									</tr>);
					  			})						
							}
			 			</tbody>
					</table>
					<TablePagination 
						total={this.state.tamGoalsInformation}
						onChangePage={this.getInfos}
						page={this.state.page}
						tableName={"goalsInfo-table"}
					/>
		  		</div>
				:""}
			</div>);
	} else {

		return (
			<div className="panel panel-default">
				<div className="panel-heading">
		  			<b className="budget-graphic-title"> Metas </b>
		  			{/*this.state.objectivesInformation != null ?
		  			<select onChange={this.onObjectivesSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectObjectives">
						<option value={-1} data-placement="right" title="Todos os objetivos">Todos os objetivos </option>
						{this.state.objectivesInformation.map((attr, idy) =>{
								  return(<option key={attr.id} value={idy} data-placement="right" title={attr.name}>{attr.name}</option>);
						  })
						}
					</select> : ""*/}
		 			<select onChange={this.onIndicatorSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectIndicator">
						<option value={-1} data-placement="right" title="Todos os indicadores">Todos os indicadores </option>
						{this.state.indicators.map((attr, idy) =>{
								  return(<option key={"ind-opt-"+idy} value={idy} data-placement="right" title={attr.name}>
												 {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
								</option>);
						  })
						}
					</select>
		  
		  			<select onChange={this.onPerformanceSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectPerformances">
						<option value={-1} data-placement="right" title="Selecione o desempenho">Selecione o desempenho </option>
						<option value={1} data-placement="right" title="Abaixo do mínimo">Abaixo do mínimo</option>
						<option value={2} data-placement="right" title="Abaixo do esperado">Abaixo do esperado</option>
						<option value={3} data-placement="right" title="Suficiente">Suficiente</option>
						<option value={4} data-placement="right" title="Acima do máximo">Acima do máximo</option>
						<option value={5} data-placement="right" title="Não iniciado">Não iniciado</option>
		  			</select>                    
		  			<div className="performance-strategic-btns floatRight">
						<span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
		  			</div>
				</div>
				{!this.state.hide  ?
		  			<div>
		  				<table className="dashboard-table table" id="goalsInfoTable">
			  				<tbody>
								<tr>
									<th   id = "column-goals-perfomance">Objetivo
					  					<span className={this.state.sortIconStatus[0] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[0] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[0] == "" || this.state.sortIconStatus[0] =="desc") 
					  				? this.quickSortByObjectiveName.bind(this,"asc") :  this.quickSortByObjectiveName.bind(this,"desc")} > </span></th>
					
									<th   id = "column-goals-perfomance">Indicador
					  					<span className={this.state.sortIconStatus[1] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[1] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[1] == "" || this.state.sortIconStatus[1] =="desc") 
					  				? this.quickSortByIndicatorName.bind(this,"asc") :  this.quickSortByIndicatorName.bind(this,"desc")} > </span></th>
					
									<th  id = "column-goals-perfomance">Meta                    
					  					<span className={this.state.sortIconStatus[2] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[2] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[2] == "" || this.state.sortIconStatus[2] =="desc") 
					  				? this.quickSortByGoalName.bind(this,"asc") :  this.quickSortByGoalName.bind(this,"desc")} > </span></th>
					
									<th id = "column-goals-perfomance">Responsável
					  					<span className={this.state.sortIconStatus[3] == "desc"?"mdi mdi-sort-ascending cursorPointer":
					  					(this.state.sortIconStatus[3] =="asc" ? "mdi mdi-sort-descending cursorPointer" : "mdi mdi-sort cursorPointer")} 
					  					onClick={(this.state.sortIconStatus[3] == "" || this.state.sortIconStatus[3] =="desc") 
					  				? this.quickSortByResponsible.bind(this,"asc") :  this.quickSortByResponsible.bind(this,"desc")} > </span></th>

									<th id = "column-goals-perfomance">Status</th>
								</tr>
								<tr>
									  <th><p id = "GoalsInformationTable" className={this.state.indicator && this.state.indicator.aggregate ? "noWrap" : ""}>
									  	{this.state.indicator && this.state.indicator.aggregate ? "Indicador agregado não possui metas" : "Não há registros cadastrados"}
									  	</p></th>
								</tr>
			 				</tbody>
						</table>					
		  			</div>
				:""}
			</div>);
		}
	}
});