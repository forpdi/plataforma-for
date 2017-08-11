import React from "react";
import ForPDIChart from 'forpdi/jsx/core/widget/ForPDIChart.jsx';
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var numeral = require('numeral');

export default React.createClass({
    getInitialState() {
        return {
          plan:this.props.plan,
          subPlan:this.props.subPlan,
          levelInstance:this.props.levelInstance,      
          elements:[],
          data: [],
          options:{
            title: '',
            hAxis: {title: Messages.get("label.thematicAxes"), minValue: 0, maxValue: 15, slantedText:true, slantedTextAngle:45},
            vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 15},
            legend: 'none',
            explorer: { axis: 'horizontal' },
          },
          aggregateIndicator:this.props.isAggregateIndicator,
          indicator: this.props.isIndicator,
          loading: true,
          pageSize: 6
        };
    },

    componentWillReceiveProps(newProps){
        var me = this;
        if(me.isMounted()) {
          me.setState({
            plan: newProps.plan,
            subPlan: newProps.subPlan,
            levelInstance: newProps.levelInstance,        
            options:{
              title: '',
              hAxis: {title: Messages.get("label.thematicAxes"), minValue: 0, maxValue: 15, slantedText:true, slantedTextAngle:45},
              vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 15},
              legend: 'none',
              explorer: { axis: 'horizontal' },
            },
            loading: true,
            aggregateIndicator:newProps.isAggregateIndicator,
            indicator: newProps.isIndicator
          });
          me.getInfos(1, me.state.pageSize, newProps);
        }
    },

    componentDidMount() {
        var me = this;        
        DashboardStore.on("levelSonsGraphRetrivied", (model) => {
            if(me.isMounted()){
                var data = [];
                var elements=[];
                if (me.state.aggregateIndicator == true) {
                    data = [
                        ['Element', 'Rendimento', { role: 'style' }]];                    
                    elements=[];
                    var vet = [];

                    me.state.indicator.indicatorList.map((ind) => {
                        vet = [];
                        vet[0] = (ind.aggregate.name.length  > 60 ? ind.aggregate.name.substr(0,60).concat("...") : ind.aggregate.name);
                        if(ind.aggregate.levelValue == undefined){
                            vet[1] = 0;
                        }else{
                            vet[1] = {
                                v: parseFloat(ind.aggregate.levelValue),
                                f: numeral(parseFloat(ind.aggregate.levelValue)).format('0,0.00') + "%"
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
                    if(me.state.indicator.indicatorList.length == 0){
                        elements.push(["",0,'']);
                        data.push(["",0,'']);
                    }else{
                        me.state.indicator.indicatorList.map((ind, cont) => {
                            data.push(elements[cont]);
                        });
                    }
                } else {
                    if (model.data.length == 0) {
                        data.push(['Element', 'Rendimento']);
                        data.push(['',parseFloat(0)]);
                        elements.push(['',parseFloat(0)]);
                    } else {
                        data.push(['Element', 'Rendimento', { role: 'style' }]); 
                        model.data.map((item) => {
                            var value =  {
                                v: parseFloat(item.levelValue),
                                f: numeral(parseFloat(item.levelValue)).format('0,0.00') + "%"
                            }

                            var color;
                            /*          
                            if(value < 40){
                                color = "#E74C3C";
                            }else if(value < 70){
                                color = "#FFCC33";
                            }else if(value < 100){
                                color = "#51D466";
                            }else{
                                color = "#4EB4FE";
                            }*/

                            if(value.v < item.levelMinimum){
                                color = "#E74C3C";
                            }else if(value.v < 100){
                                color = "#FFCC33";
                            }else if(value.v < item.levelMaximum || item.levelMaximum == 100){
                                color = "#51D466";
                            }else{
                                color = "#4EB4FE";
                            }

                            if(item.name.length > 50){
                                elements.push([item.name.substring(0, 50).concat("..."),value, color]);
                            }else{
                                elements.push([item.name,value, color]);
                            }
                        });
                    }                    
                    model.data.map((item, cont) => {
                        data.push(elements[cont]);
                    });                                   
                }
                me.setState({
                    data:data,
                    elements:elements,
                    loading: false,
                    total: model.total
                });
            }
    },me);  
  },

    componentWillUnmount() {
        DashboardStore.off(null, null, this);
        PlanStore.off(null, null, this);
        PlanMacroStore.off(null, null, this);
    },

    hideFields() {
        this.setState({
            hide: !this.state.hide
        })
    },
  

    getInfos(page, pageSize, opt){
        opt = opt || this.state;
        DashboardStore.dispatch({
            action: DashboardStore.ACTION_GET_LEVELSONS_GRAPH,
            data: {
              macro: (opt.plan == -1 ? null : opt.plan.id),
              plan: (opt.subPlan == -1 ? null : opt.subPlan.id),
              levelInstance: (opt.levelInstance == -1 ? null : opt.levelInstance.id),
              page: page,
              pageSize: pageSize
            }    
        });
    },

    render() {
        var dashboardTitle = "";
        if (this.state.levelInstance == -1 && this.state.subPlan == -1)
            dashboardTitle = Messages.get("label.thematicAxesPerformance") + " - Todos os "+Messages.get("label.goalsPlan");
        else if (this.state.levelInstance == -1)
            dashboardTitle = Messages.get("label.thematicAxesPerformance") + " - "+this.state.subPlan.name;
        else if (this.state.levelInstance.parent == null) 
            dashboardTitle = Messages.get("label.objectivesPerformance") + " - "+this.state.levelInstance.name;
        else if (this.state.levelInstance.level.objective)
            dashboardTitle = Messages.get("label.indicatorsPerformance") + " - "+this.state.levelInstance.name;
        else if (this.state.levelInstance.level.indicator)
            dashboardTitle = Messages.get("label.goalsPerformance") + " - "+this.state.levelInstance.name;
        return (
            <div className="panel panel-default">
                <div className="panel-heading dashboard-panel-title">
                <b className="budget-graphic-title"> <span className="fpdi-nav-label" title = {dashboardTitle.length > 70 ? dashboardTitle : ""}> {(dashboardTitle.length) <= 70?dashboardTitle:(dashboardTitle.split("",70).concat(" ..."))} </span> </b>
                <div className="performance-strategic-btns  floatRight">
                    <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
                </div>
                </div>
                {!this.state.hide ?
                    (this.state.loading ? <LoadingGauge/> :
                    <div>
                        <ForPDIChart
                         chartType="ColumnChart"
                         data={this.state.data}
                         options={this.state.options}
                         graph_id="ColumnChartCommunity"
                         width="100%"
                         height="300px"
                         legend_toggle={true}
                         pageSize={this.state.pageSize}
                         total={this.state.total}
                         onChangePage={this.getInfos} />
                        {this.state.levelInstance == -1 || !this.state.levelInstance.level.indicator ? <div className="aggregate-indicator-without-goals-legend height30"> </div>: 
                        <div className="aggregate-indicator-without-goals-legend"> 
                            {this.state.aggregateIndicator ? 
                            <span className="legend-item"> 
                                    <p id = "aggregate-indicator-goals">{Messages.getEditable("label.aggIndicatorText","fpdi-nav-label")}</p>
                            </span>
                            :<div className="height10">                                 
                            </div>}
                        </div>}                                     
                    </div>)            
                :""}
            </div>
        );
    }
});
