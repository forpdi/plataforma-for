import React from "react";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import string from 'string';
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

var numeral = require('numeral');

export default React.createClass({
  getInitialState() {
    return {
        plan:this.props.plan,
        loading: true,
        subPlan:this.props.subPlan,
        elements:[],
        data: [],
        options:{
            title: '',
            hAxis: {minValue: 0, maxValue: 15},
            vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 15},
            legend: 'none',
            explorer: {axis: 'horizontal'},
            bar: {groupWidth: '50%'},
        },
        thematicAxes:[],
        selectedThematicAxes: -1,
        chartEvents: [
            {
                eventName : 'select',
                callback  : this.onChartClick
            },
        ],
        pageSize: 10
    };
  },

  componentWillReceiveProps(newProps){
    var me = this;
	me.setState({
	plan: newProps.plan,
	subPlan: newProps.subPlan,
	loading: true
	});

	if(!this.state.loading){
	this.refs.selectThematicAxes.value = -1;
	}
	this.getInfo(1, this.state.pageSize, newProps);
  },

  componentDidMount() {
    var me = this;

    this.getInfo(1, this.state.pageSize);

    DashboardStore.on("objectivesByThematicAxisRetrived", (model) => {
        var data = [];
        var element = [];

        if (model.data.length == 0) {
            data.push(['Element', 'Rendimento']);
            data.push([Messages.get("label.haveNoObjectives"),parseFloat(0)]);
            element.push([Messages.get("label.haveNoObjectives"),parseFloat(0)]);
        } else {
            data.push(['Element', 'Rendimento', { role: 'style' }]);
            var value;
            model.data.map((item) => {
                value = item.levelValue;
                var color;

                if (value == undefined) {
                    value = 0;
                } else {
                    value = parseFloat(item.levelValue);
                }
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

                if (!value)
                    color = "#A9A9A9";
                else if (value < item.levelMinimum)
                    color = "#E74C3C";
                else if (value < 100.0)
                    color = "#FFCC33";
                else if (value < item.levelMaximum || item.levelMaximum == 100.0)
                    color = "#51D466";
                else
                    color = "#4EB4FE";

                var valueForGraph = {
                    v: value,
                    f: numeral(parseFloat(value)).format('0,0.00')+"%"
                };

                if(item.name.length > 50){
                    element.push([item.name.substring(0, 50).concat("..."),valueForGraph, color]);
                }else{
                    element.push([item.name,valueForGraph, color]);
                }
            });
        }


        model.data.map((item, idx) => {
            data.push(element[idx]);
        })


        var bool = (model ? model.data.length > 0 : true);
        var hTitle = (model && model.data.length > 0 ? Messages.get("label.objectives") : "");
        this.setState({
            data:data,
            elements:element,
            objectives: model.data,
            total: model.total,
            options:{
                thitle: '',
                hAxis: {title: hTitle, minValue: 0, maxValue: 15,slantedText:bool, slantedTextAngle:30},
                vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 15},
                legend: 'none',
                bar: {groupWidth: '50%'}
            }
        })
    },me);

    DashboardStore.on("performanceStrategicAxisRetrived", (model) => {
		me.setState({
			thematicAxes:model.data,
			loading: false
		});
        if (this.refs.selectThematicAxes.value == -1) {
            var data = [];
            var element = [];

            if (model.data.length == 0) {
                data.push(['Element', 'Rendimento']);
                data.push([Messages.get("label.haveNoThematicAxes"),parseFloat(0)]);
                element.push([Messages.get("label.haveNoThematicAxes"),parseFloat(0)]);
            } else {
                data.push(['Element', 'Rendimento', { role: 'style' }]);

                model.data.map((item) => {
                    var value = parseFloat(item.levelValue);
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
                    }
                    */

                    if (!value)
                        color = "#A9A9A9";
                    else if (value < item.levelMinimum)
                        color = "#E74C3C";
                    else if (value < 100.0)
                        color = "#FFCC33";
                    else if (value < item.levelMaximum || item.levelMaximum == 100.0)
                        color = "#51D466";
                    else
                        color = "#4EB4FE";

                    var valueForGraph = {
                        v: value,
                        f: numeral(parseFloat(value)).format('0,0.00')+"%"
                    };

                    if(item.name.length > 50){
                        element.push([item.name.substring(0, 50).concat("..."),valueForGraph, color]);
                    }else{
                        element.push([item.name,valueForGraph, color]);
                    }
                });
            }
            model.data.map((item, idx) => {
                data.push(element[idx]);
            });


            var bool = (model ? model.data.length > 0 : true);
            var hTitle = (model && model.data.length > 0 ? Messages.get("label.thematicAxes") : "");
            this.setState({
                data:data,
                elements:element,
                total: model.total,
                options:{
                    thitle: '',
                    hAxis: {title: hTitle, minValue: 0, maxValue: 15,slantedText:bool, slantedTextAngle:30},
                    vAxis: {title: 'Valor (%)', minValue: 0, maxValue: 15},
                    legend: 'none',
                    bar: {groupWidth: '50%'}
                }
            })
        }
    },me);

  },

  componentWillUnmount() {
      DashboardStore.off(null, null, this);
  },

  hideFields() {
    this.setState({
      hide: !this.state.hide
    })
  },

  onChartClick(Chart){
    var me = this;
    if(Chart.chart.getSelection().length > 0){
        var level, url;
        if(me.state.selectedThematicAxes == -1){
            level = me.state.thematicAxes[Chart.chart.getSelection()[0].row];
        } else {
            level = me.state.objectives[Chart.chart.getSelection()[0].row];
        }

        url = window.location.origin+window.location.pathname+"#/plan/"+
        level.plan.parent.id+"/details/subplan/level/"+level.id;

        var msg = Messages.get("label.askGoToSelectedLevel");
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

  onThematicAxesChange(){
    this.getInfo(1, this.state.pageSize);
  },

  getInfo(page, pageSize, opt){
    opt = opt || this.state;
    if (this.refs.selectThematicAxes.value == -1) {
        DashboardStore.dispatch({
            action: DashboardStore.ACTION_PERFORMANCE_STRATEGICAXIS,
            data: {
                macro: (opt.plan == -1 ? (null) : opt.plan.id),
                plan:(opt.subPlan != -1)?(opt.subPlan.id):(null),
                page: page,
                pageSize: pageSize
            }
		});

		this.setState ({
            selectedThematicAxes: -1,
		});
    } else {
        DashboardStore.dispatch({
        action: DashboardStore.ACTION_GET_THEMATIC_AXIS_INFORMATION,
        data: {
                macro: (opt.plan != -1)?(opt.plan.id):(null),
                plan:(opt.subPlan != -1)?(opt.subPlan.id):(null),
                thematicAxis:(this.refs.selectThematicAxes.value == -1 ? null : opt.thematicAxes[this.refs.selectThematicAxes.value].id)
            }

        });

        this.setState ({
            selectedThematicAxes: opt.thematicAxes[this.refs.selectThematicAxes.value]
        });
    }
  },

  render() {
    return (
        <div>
            <div id= {!this.state.hide ? "panelSection" :""} className="panel panel-default">
                <div className="panel-heading dashboard-panel-title">
                    <b className="budget-graphic-title"> {Messages.getEditable("label.thematicAxesPerformance","fpdi-nav-label")} </b>
                        <select onChange={this.onThematicAxesChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectThematicAxes">
                            <option value={-1} data-placement="right" title={Messages.get("label.viewAll")}> {Messages.get("label.viewAll")} </option>
                            {
                                this.state.thematicAxes.map((attr, idy) =>{
                                    return(
                                        <option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                                            {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                        </option>
                                    );
                                })
                            }
                        </select>
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
                             graph_id="ColumnChart"
                             width="100%"
                             height="300px"
                             legend_toggle={true}
                             pageSize={this.state.pageSize}
                             total={this.state.total}
                             onChangePage={this.getInfo}
                             chartEvents={this.state.chartEvents} />
                            <div className="colaborator-goal-performance-legend">
                                <span className="legend-item"><input type="text"  className="legend-goals-minimumbelow marginLeft10" disabled/> {Messages.getEditable("label.goals.belowMinimum","fpdi-nav-label")}</span>
                                <span className="legend-item"><input type="text"  className="legend-goals-expectedbelow marginLeft10" disabled/> {Messages.getEditable("label.goals.belowExpected","fpdi-nav-label")}</span>
                                <span className="legend-item"><input type="text"  className="legend-goals-enough marginLeft10" disabled/> {Messages.getEditable("label.goals.reached","fpdi-nav-label")}</span>
                                <span className="legend-item"><input type="text"  className="legend-goals-expectedabove marginLeft10" disabled/> {Messages.getEditable("label.goals.aboveExpected","fpdi-nav-label")}</span>
                            </div>
                        </div>
                    )
                :""}
            </div>
        </div>
    );

  }
});
