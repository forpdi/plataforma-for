import React from "react";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import ForPDIChart from 'forpdi/jsx/core/widget/ForPDIChart.jsx';
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var numeral = require('numeral');

export default React.createClass({

    getInitialState() {
        return {
            loaded:true,
            elements:[],
            indicator:this.props.indicator,
            typeGraph: "",
            pageSize: 10
        };
    },

    componentWillReceiveProps(newProps){
            this.getInfos(1, this.state.pageSize, newProps);
    },

    componentDidMount(){
    	var me = this;

        if(me.state.indicator.aggregate == true) {
            me.setState({
                typeGraph: "ColumnChart",
                chartEvents: [
                    {
                        eventName : 'select',
                        callback  : me.onChartClick
                    },
                ]
            });
        } else {
            me.setState({
                typeGraph: "ComboChart",
                chartEvents: [
                    {
                        eventName : 'select',
                        callback  : me.onChartClick
                    },
                ]
            });
        }

		me.setState({
			optionsIndicators:{
				title: '',
				hAxis: {minValue: 0, maxValue: 100,slantedText:true,
						slantedTextAngle:45},
				vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 100},
				legend: 'none',
				bar: {groupWidth: '50%'}
			},
			options:{
				title: '',
				colors: ['#CCCCCC','#333333'],
				vAxis: {title: 'Esperado x Alcançado', minValue: 0, maxValue:15},
				hAxis: {slantedText:true,slantedTextAngle:45},
				legend: {position: 'none'},
				bar: {groupWidth: '30%'},
				seriesType: 'bars',
				series: {1: {type: 'line', pointsVisible: true, pointSize:4}}
			},
			data: [
				['titulo', 'Alcançado', { role: 'style' }, 'Esperado'],
				["",0,'',0]
			]
		});

        DashboardStore.on("graphForIndicatorRetrived",(model)=>{
            var data = [];
            var elements = [];
			if (me.state.indicator.aggregate == true) {
				data = [['Element', 'Rendimento', { role: 'style' }]];
				elements=[];
				var vet = [];


				if (me.state.indicator.indicatorList != undefined) {

					me.state.indicator.indicatorList.map((ind) => {
						vet = [];
						vet[0] = (ind.aggregate.name.length  > 60 ? ind.aggregate.name.substr(0,60).concat("...") : ind.aggregate.name);
						if(ind.aggregate.levelValue == undefined){
							vet[1] = 0;
						}else{
							vet[1] = {
								v: parseFloat(ind.aggregate.levelValue),
								f: parseFloat(ind.aggregate.levelValue.toFixed(2))+"%"
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
						}*/

						if (!ind.aggregate.levelValue)
							vet[2] = "#A9A9A9";
						else if (ind.aggregate.levelValue < ind.aggregate.levelMinimum)
							vet[2] = "#E74C3C";
						else if (ind.aggregate.levelValue < 100.0)
							vet[2] = "#FFCC33";
						else if (ind.aggregate.levelValue < ind.aggregate.levelMaximum)
							vet[2] = "#51D466";
						else
							vet[2] = "#4EB4FE";

						elements.push(vet);
					});

					if(me.state.indicator.indicatorList.length == 0){
						data = [['Element', 'Rendimento']];
						elements.push([Messages.get("label.haveNoIndicators"),0]);
						data.push([Messages.get("label.haveNoIndicators"),0]);
					}else{
						me.state.indicator.indicatorList.map((ind, cont) =>{
							data.push(elements[cont]);
						});
					}
				} else {
					elements.push(["",0,'']);
					data.push(["",0,'']);
				}




			} else {
				elements =[];
				data = [['Element', 'Alcançado', { role: 'style' }, 'Esperado']];
				var goalValue;

				model.data.map((item) => {
					goalValue = me.getGoalsValues(item);
					elements.push(goalValue);
				});
				if(model.data.length == 0){
					data = [['Element', 'Alcançado']];
					elements.push([Messages.get("label.haveNoGoals"),0]);
					data.push([Messages.get("label.haveNoGoals"),0]);
				}else{
					model.data.map((item, cont) => {
						data.push(elements[cont]);
					});
				}
			}
			me.setState({
				elements:elements,
				data:data,
				total: model.total,
				goals: model.data,
			});
			me.updateChartOptions(model);
        },me);
    },

    updateChartOptions(model){
        var bool1 = (this.state.indicator.indicatorList && this.state.indicator.indicatorList.length == 0 ? false : true);
        var bool2 = (model ? model.data.length > 0 : true);
        var hTitle1 = (this.state.indicator.indicatorList && this.state.indicator.indicatorList.length == 0 ? "" : "Indicadores");
        var hTitle2 = (model && model.data.length > 0 ? Messages.get("label.goals") : "");
        this.setState({
            optionsIndicators:{
                title: '',
                hAxis: {title:hTitle1, minValue: 0, maxValue: 100,slantedText:bool1, slantedTextAngle:30},
                vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 100},
                legend: 'none',
                bar: {groupWidth: '50%'}
            },
            options:{
                title: '',
                colors: ['#CCCCCC','#333333'],
                vAxis: {title: 'Esperado x Alcançado', minValue: 0, maxValue:15},
                hAxis: {title:hTitle2, slantedText:bool2,slantedTextAngle:30},
                legend: {position: 'none'},
                bar: {groupWidth: '30%'},
                seriesType: 'bars',
                series: {1: {type: 'line', pointsVisible: true, pointSize:4}}
            }
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
        if(goal.polarity == Messages.get("label.highestBest")){
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
        } else if (goal.polarity == Messages.get("label.lowerBest")){
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

    hideFields() {
        this.setState({
            hide: !this.state.hide
        })
    },

	componentWillUnmount() {
        DashboardStore.off(null, null, this);
	},

    getInfos(page, pageSize, opt){
        opt = opt || this.state;
        DashboardStore.dispatch({
            action: DashboardStore.ACTION_GET_GRAPH_FOR_INDICATOR,
            data: {
                indicator: opt.indicator.id,
                page: page,
                pageSize: pageSize
            }
        });
    },

	render() {
		return(
			<div className={this.props.className}>
			{!this.state.loaded ? <LoadingGauge />:(
				<div>
					<div className="panel panel-default dashboard-goals-info-ctn">
						<div className="panel-heading">
							<b className="budget-graphic-title"> {Messages.getEditable("label.indicatorPerformance","fpdi-nav-label")} </b>
                            <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15 floatRight"):("mdi mdi-chevron-down marginLeft15 floatRight")}  onClick={this.hideFields}/>
						</div>
                        {!this.state.hide ?
                            <div>
                                <ForPDIChart
                                    chartType= {this.state.typeGraph}
                                    data={this.state.data}
                                    options= {this.state.typeGraph == "ColumnChart" ? this.state.optionsIndicators :  this.state.options}
                                    graph_id="ColumnChart-Budget"
                                    width={"100%"}
                                    height={"300px"}
                                    legend_toggle={true}
                                    pageSize={this.state.pageSize}
                                    total={this.state.total}
                                    onChangePage={this.getInfos}
                                    chartEvents={this.state.chartEvents}/>

                                <div className="colaborator-goal-performance-legend">
                                    {this.state.indicator.aggregate ?
                                        (   <div className="aggregate-indicator-without-goals-legend">
                                                <span className="legend-item">
                                                    <p id = "aggregate-indicator-goals"> Este é um indicador agregado, composto por outros indicadores.</p>
                                                </span>
                                            </div>
                                        ) : ""
                                    }

                                    <span className="legend-item"><input type="text"  className="legend-goals-minimumbelow marginLeft10" disabled/> {Messages.getEditable("label.goals.belowMinimum","fpdi-nav-label")}</span>
                                    <span className="legend-item"><input type="text"  className="legend-goals-expectedbelow marginLeft10" disabled/> {Messages.getEditable("label.goals.belowExpected","fpdi-nav-label")}</span>
                                    <span className="legend-item"><input type="text"  className="legend-goals-enough marginLeft10" disabled/> {Messages.getEditable("label.goals.reached","fpdi-nav-label")}</span>
                                    <span className="legend-item"><input type="text"  className="legend-goals-expectedabove marginLeft10" disabled/> {Messages.getEditable("label.goals.aboveExpected","fpdi-nav-label")}</span>
                                    {this.state.indicator.aggregate ? "" :
                                        <span className="legend-item"><input type="text"  className="legend-goals-difference-expected marginLeft10" disabled/> {Messages.getEditable("label.goals.expected", "fpdi-nav-label")}</span>
                                    }
                                </div>
                            </div>
                        :""}
					</div>
				</div>
			)}
            </div>
        );
	}
});
