
import React from "react";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var numeral = require('numeral');

export default React.createClass({

	getInitialState() {
        return {
        	hide: false,
        	plan: this.props.plan,
        	subPlan: this.props.subPlan,
            profile: this.props.profile,
            loading: true,            
            elements:[],
            objectives:[],
            indicators:[],
            aggregateIndicator: false,
            selectedObjective: -1,
            selectedIndicator: -1,
            typeGraph: "",
            indicator:null,            
            pageSize: 10
        };
    },

    componentWillReceiveProps(newProps){
    	var me = this;
        if(this.isMounted()) {
        	me.setState({
        		plan:newProps.plan,
        		subPlan:newProps.subPlan,
                loading: true                
        	});
            if(!this.state.loading){
                this.refs.selectObjectives.value = -1;
                this.refs.selectIndicators.value = -1;
            }

            this.getInfos(1, this.state.pageSize, newProps);

            StructureStore.dispatch({
                action: StructureStore.ACTION_GET_OBJECTIVES,
                data: {
                    macroId: (newProps.plan && newProps.plan != -1)?(newProps.plan.get("id")):(null),
                    planId:(newProps.subPlan && newProps.subPlan != -1)?(newProps.subPlan.id):(null)
                }      
            });
        }
    },

    updateChartOptions(model){
        var bool = (model ? model.data.length > 0 : true);
        var hTitle1 = (model && model.data.length > 0 ? Messages.get("label.indicators") : ""); 
        var hTitle2 = (model && model.data.length > 0 ? Messages.get("label.goals") : ""); 
        this.setState({            
            options:{
                title: '',
                hAxis: {title:hTitle1, minValue: 0, maxValue: 100,slantedText: bool,slantedTextAngle:30},
                vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 100},
                legend: 'none',
                bar: {groupWidth: '50%'}
            },
            optionsGraphComboChart:{
                title: '',
                colors: ['#CCCCCC','#333333'],
                vAxis: {title: 'Esperado x Alcançado', minValue: 0, maxValue:15},
                hAxis: {title:hTitle2,slantedText:bool, slantedTextAngle:30},
                legend: {position: 'none'},
                bar: {groupWidth: '50%'},
                seriesType: 'bars',
                series: {1: {type: 'line', pointsVisible: true, pointSize:4}}
            }
        });
    },

	componentDidMount() {
        var me = this;
        me.setState({
            typeGraph: "ColumnChart",
            chartEvents: [
                {
                    eventName : 'select',
                    callback  : me.onChartClick
                },
            ]
        });
        me.updateChartOptions();
  
        StructureStore.on("objectivesretrivied", (model) => { 
            if(me.isMounted()) { 
                me.setState({
                    objectives: model.data,
                    loading: false
                });
                me.forceUpdate();
            }
        }, me);

        DashboardStore.on("generalGoalsInformation",(model)=> {
            if(me.isMounted()) {                
                var elements =[];
                var data = [
                    ['Element', 'Alcançado', { role: 'style' }, 'Esperado']];
                var goalValue;
                model.data.map((goal) => {
                    goalValue = me.getGoalsValues(goal);
                    elements.push(goalValue);
                });
                
                if(model.data.length == 0){
                    elements.push([Messages.get("label.haveNoGoals"),0,'',0]);
                    data.push([Messages.get("label.haveNoGoals"),0,'',0]);
                }else{
                    model.data.map((ind, idx) => {
                        data.push(elements[idx]);
                    }); 
                }
                
                me.setState({
                    elements:elements,
                    data:data,
                    startIndex:0,
                    endIndex:9,
                    loading: false,
                    goals: model.data,
                    total: model.total
                });
                me.updateChartOptions(model);
            }
        },me);

        DashboardStore.on("generalIndicatorsInformation",(model)=> {            
            if(me.isMounted()) {
                var data = [
                        ['Element', 'Rendimento', { role: 'style' }]];
                var elements=[];
                if (me.refs.selectIndicators.value == -1) {
                    var vet = [];                    
                    model.data.map((ind) => {
                        vet = [];
                        vet[0] = (ind.name.length > 60 ? ind.name.substr(0,60).concat("...") : ind.name);
                        if(ind.levelValue == undefined){
                            vet[1] = 0;
                        }else{
                            vet[1] = {
                                v: ind.levelValue,
                                f: numeral(parseFloat(parseFloat(ind.levelValue))).format('0,0.00') + "%"
                            };
                        }
                        /*
                        if(ind.levelValue<40){
                            vet[2] = "#E74C3C";
                        }else if(ind.levelValue<70){
                            vet[2] = "#FFCC33";
                        }else if(ind.levelValue<100){
                            vet[2] = "#51D466";
                        }else{
                            vet[2] = "#4EB4FE";
                        }
                        */

                        if (!ind.levelValue)
                            vet[2] = "#A9A9A9";
                        else if (ind.levelValue < ind.levelMinimum)
                            vet[2] = "#E74C3C";
                        else if (ind.levelValue < 100.0)
                            vet[2] = "#FFCC33";
                        else if (ind.levelValue < ind.levelMaximum || ind.levelMaximum == 100.0)
                            vet[2] = "#51D466";
                        else
                            vet[2] = "#4EB4FE";

                        elements.push(vet);
                    });                   
            
                    if(model.data.length==0){
                        vet[0] = Messages.get("label.haveNoIndicators");
                        vet[1] = 0;
                        vet[2] = "#FFFFFF";
                        elements.push(vet);
                        data.push(vet);
                    } else{
                        model.data.map((ind, idx) => {
                            data.push(elements[idx]);
                        });
                    }
                    me.setState({                        
                        indicators: model.data
                    });

                    me.updateChartOptions(model);
                    me.setState({
                        total: model.total
                    });
                } else {
                    var vet = [];                    
                    me.state.indicators[this.refs.selectIndicators.value].indicatorList.map((ind) => {
                        vet = [];
                        vet[0] = (ind.aggregate.name.length  > 60 ? ind.aggregate.name.substr(0,60).concat("...") : ind.aggregate.name);
                        if(ind.aggregate.levelValue == undefined){
                            vet[1] = 0;
                        }else{
                            vet[1] = {
                                v: parseFloat(ind.aggregate.levelValue),
                                f: numeral(parseFloat(parseFloat(ind.aggregate.levelValue))).format('0,0.00') + "%"
                            };
                        }
                        /*
                        if(ind.aggregate.levelValue < 40){
                            vet[2] = "#E74C3C";
                        }else if(ind.aggregate.levelValue < 70){
                            vet[2] = "#FFCC33";
                        }else if(ind.aggregate.levelValue < 100){
                            vet[2] = "#51D466";
                        }else{
                            vet[2] = "#4EB4FE";
                        }
                        */
                        
                        if (!ind.aggregate.levelValue)
                            vet[2] = "#A9A9A9";
                        else if (ind.aggregate.levelValue < ind.aggregate.levelMinimum)
                            vet[2] = "#E74C3C";
                        else if (ind.aggregate.levelValue < 100.0)
                            vet[2] = "#FFCC33";
                        else if (ind.aggregate.levelValue < ind.aggregate.levelMaximum || ind.aggregate.levelMaximum == 100.0)
                            vet[2] = "#51D466";
                        else
                            vet[2] = "#4EB4FE"; 
                        
                        elements.push(vet);
                    });

                    if(me.state.indicators[this.refs.selectIndicators.value].indicatorList.length == 0){
                       elements.push(["",0,'']);
                        data.push(["",0,'']);
                    }else{
                        me.state.indicators[this.refs.selectIndicators.value].indicatorList.map((ind, idx) => {
                            data.push(elements[idx]);                            
                        });
                    }
                    me.setState({
                        total: me.state.indicators[this.refs.selectIndicators.value].indicatorList.length
                    });
                }
                me.setState({
                    data:data,
                    elements:elements,                        
                    loading: false                    
                });
            }
        },me);       
  	},

    getGoalsValues(goal){ 
        var expectedField, maximumField,minimumField,reachedField;
        var index;
        var fExp, fMax, fMin, fRec;        
        for(var cont=1;cont<goal.attributeList.length;cont++){
            index = cont;
            if (goal.attributeInstanceList[index]) {  
                if(goal.attributeList[cont].expectedField){
                    expectedField = goal.attributeInstanceList[index].valueAsNumber || 0;
                    fExp = goal.attributeInstanceList[index].formattedValue || "0";
                }else if(goal.attributeList[cont].maximumField){
                    maximumField = goal.attributeInstanceList[index].valueAsNumber || 0;
                    fMax = goal.attributeInstanceList[index].formattedValue || "0";
                }else if(goal.attributeList[cont].minimumField){
                    minimumField = goal.attributeInstanceList[index].valueAsNumber || 0;
                    fMin = goal.attributeInstanceList[index].formattedValue || "0";
                }else if(goal.attributeList[cont].reachedField){
                    reachedField = goal.attributeInstanceList[index].valueAsNumber || 0;
                    fRec = goal.attributeInstanceList[index].formattedValue || "0";
                }
            }
        }
        var graphItem = ["",0,'',0];
        if(goal.name.length > 50){
            graphItem[0] = goal.name.slice(0,50)+"...";
        } else {
            graphItem[0] = goal.name;            
        }

        if(reachedField == undefined){
            reachedField = 0;
        }
        
        var format = fExp.replace(/[0-9.,]/gi,"");
        var prefix = "", sufix = "";
        if(fExp.indexOf(format) == 0){
            prefix = format;
        } else {
            sufix = format;
        }
        if(goal.polarity == "Maior-melhor"){
            if(reachedField<minimumField){
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#E74C3C";
            }else if(reachedField<expectedField){
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#FFCC33";
            }else if(reachedField <= maximumField){
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#51D466";
            }else{
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#4EB4FE";
            }
        } else if (goal.polarity == "Menor-melhor"){
            if(reachedField > minimumField){
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#E74C3C";
            } else if (reachedField > expectedField){
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#FFCC33";
            } else if (reachedField >= maximumField){
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#51D466";
            } else{
                graphItem[1] = {
                    v: reachedField,
                    f: fRec
                };
                graphItem[3] = {
                    v: expectedField,
                    f: prefix + numeral(expectedField).format('0,0.00') + sufix
                };
                graphItem[2] = "#4EB4FE";
            }
        }        
        return graphItem;
    },

	componentWillUnmount() {
        DashboardStore.off(null, null, this);
        StructureStore.off(null, null, this);
        PlanStore.off(null, null, this);
        PlanMacroStore.off(null, null, this);
	},

    hide(){
    	this.setState({
    		hide: !this.state.hide
    	});
    },

    onObjectivesSelectChange() {        
        if (this.refs.selectObjectives.value == -1) {
            this.setState ({
                typeGraph:"ColumnChart"
            });
        }

    	this.getInfos(1, this.state.pageSize);
        this.refs.selectIndicators.value = -1;
        this.setState({  
            selectedObjective: this.state.objectives[this.refs.selectObjectives.value],          
            loading: true
        });
    },

    indicatorChange() {
        this.getInfos(1, this.state.pageSize);
    }, 

    hideFields() {
        this.setState({
          hide: !this.state.hide
        })
    },

    getInfos(page, pageSize, opt){        
        opt = opt || this.state;        
        if (this.refs.selectIndicators.value == -1 || this.state.indicators[this.refs.selectIndicators.value].aggregate) {
            DashboardStore.dispatch({
                action: DashboardStore.ACTION_GET_INDICATORS_INFORMATION, 
                data: {
                    macro: (opt.plan == -1 ? null : opt.plan.id),
                    plan:(opt.subPlan == -1)? null : (opt.subPlan.id),
                    objective:(this.refs.selectObjectives.value == -1 ? null : this.state.objectives[this.refs.selectObjectives.value].id),
                    page: page, 
                    pageSize: pageSize
                }                
            });
            this.setState({
                typeGraph: "ColumnChart"
            });
        } else {
            DashboardStore.dispatch({
                action: DashboardStore.ACTION_GET_GOALS_INFORMATION,
                data: {
                    macro: (opt.plan == -1 ? null : opt.plan.id),
                    plan:(opt.subPlan == -1)? null : (opt.subPlan.id),
                    indicator:(this.refs.selectIndicators.value == -1 ? null : this.state.indicators[this.refs.selectIndicators.value].id),
                    page: page, 
                    pageSize: pageSize
                }
            });
            this.setState({
                typeGraph: "ComboChart"
            });
        }

        this.setState({            
            aggregateIndicator: (this.state.indicators[this.refs.selectIndicators.value] ? 
                this.state.indicators[this.refs.selectIndicators.value].aggregate : false)
        });  
    },

    onChartClick(Chart){
        var me = this;
        if(Chart.chart.getSelection().length > 0){
            var level, url;                            
            if(me.state.indicator && me.state.indicator.aggregate){
                level = me.state.indicator.indicatorList[Chart.chart.getSelection()[0].row];
                url = window.location.origin+window.location.pathname+"#/plan/"+
                level.aggregate.plan.parent.id+"/details/subplan/level/"+level.aggregate.id;
            } else if (me.state.typeGraph == "ColumnChart"){
                level = me.state.indicators[Chart.chart.getSelection()[0].row];
                url = window.location.origin+window.location.pathname+"#/plan/"+
                level.plan.parent.id+"/details/subplan/level/"+level.id;
            } else {                                
                level = me.state.goals[Chart.chart.getSelection()[0].row];
                url = window.location.origin+window.location.pathname+"#/plan/"+
                level.plan.parent.id+"/details/subplan/level/"+level.id;
            }
            
            var msg = "Você deseja ir para o nível selecionado?";                            
            Modal.confirmCustom(() => {
                Modal.hide();           
                location.assign(url);
            },msg,
            ()=>{
                Chart.chart.setSelection([]);
                Modal.hide();
            });
        }                    
    },

    render() {
        var me = this;
        return (
           <div>               
               <div className="panel panel-default" id = {!this.state.hide ? "panelSection" : ""}>
                   <div className="panel-heading dashboard-panel-title">
                         <div>
                             <b className="budget-graphic-title"> {Messages.get("label.indicatorsPerformance")}</b>
                             <select onChange={me.onObjectivesSelectChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectObjectives">
                                 <option value={-1} data-placement="right" title={Messages.get("label.allObjectives")}>{Messages.get("label.allObjectives")}</option>
                                 {
                                     me.state.objectives.map((attr, idy) =>{
                                         return(
                                             <option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                                                 {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                             </option>
                                         );
                                     })
                                 }
                              </select>
                             <select defaultValue={-1} onChange={me.indicatorChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectIndicators" >
                                 <option value={-1} data-placement="right" title={Messages.get("label.allIndicators")}>{Messages.get("label.allIndicators")}</option>
                                      {
                                        (me.state.indicators) ? (me.state.indicators.map((attr, idy) =>{
                                              return(
                                                  <option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                                                      {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                                 </option>);
                                         }) ) : ("")
                                      }

                            </select>
                               <span  className={(me.state.hide)?("mdi mdi-chevron-right marginLeft15 floatRight"):("mdi mdi-chevron-down marginLeft15 floatRight")}  onClick={me.hideFields}/>
                          </div>
         
                   </div>
                 {!me.state.hide ?
                     (me.state.loading ? <LoadingGauge/> : 
                     <div>          
                        <ForPDIChart 
                             chartType= {me.state.typeGraph}
                             data={me.state.data} 
                             options={me.state.typeGraph == "ColumnChart" ? me.state.options : me.state.optionsGraphComboChart} 
                             graph_id="ColumnChart-Budget-Mananger"  
                             width={"100%"} 
                             height={"300px"}  
                             legend_toggle={true}
                             pageSize={me.state.pageSize}
                             total={me.state.total}
                             onChangePage={me.getInfos}
                             chartEvents={me.state.chartEvents}/>

                         {me.state.typeGraph == "ComboChart" ?
                             (
                                 <div className="colaborator-goal-performance-legend">   
                                     
                                     <span className="legend-item"><input type="text"  className="legend-goals-minimumbelow marginLeft10" disabled/> {Messages.get("label.goals.belowMinimum")}</span>
                                     <span className="legend-item"><input type="text"  className="legend-goals-expectedbelow marginLeft10" disabled/> {Messages.get("label.goals.belowExpected")}</span>
                                     <span className="legend-item"><input type="text"  className="legend-goals-enough marginLeft10" disabled/> {Messages.get("label.goals.reached")}</span>
                                    <br/>
                                     <span className="legend-item"><input type="text"  className="legend-goals-expectedabove marginLeft10" disabled/> {Messages.get("label.goals.aboveExpected")}</span>
                                     <span className="legend-item"><input type="text"  className="legend-goals-difference-expected marginLeft10" disabled/> Esperado</span>
                                 </div>
                             ) :

                             (
                                 <div className={me.state.profile.ADMIN ? "colaborator-goal-performance-legend":"colaborator-goal-performance-legend-manager"}>
                                    <div className="aggregate-indicator-without-goals-legend"> 
                                            <span className="legend-item"> 
                                                    {me.state.aggregateIndicator == true ? 
                                                         <p id = "aggregate-indicator-goals">{Messages.get("label.aggIndicatorHaveNoGoals")}</p>
                                                        :
                                                    <p>&nbsp;</p>}
                                            </span>
                                    </div> 
                                    <span className="legend-item"><input type="text"  className="legend-goals-minimumbelow marginLeft10" disabled/> {Messages.get("label.goals.belowMinimum")}</span>
                                    <span className="legend-item"><input type="text"  className="legend-goals-expectedbelow marginLeft10" disabled/> {Messages.get("label.goals.belowExpected")}</span>
                                    <span className="legend-item"><input type="text"  className="legend-goals-enough marginLeft10" disabled/> {Messages.get("label.goals.reached")}</span>
                                    <span className="legend-item"><input type="text"  className="legend-goals-expectedabove marginLeft10" disabled/> {Messages.get("label.goals.aboveExpected")}</span>
                                 </div>
                             )                   
                         } 
                     </div>)                   
                 :""}               
               </div>              
            </div>
          );
    }

});
