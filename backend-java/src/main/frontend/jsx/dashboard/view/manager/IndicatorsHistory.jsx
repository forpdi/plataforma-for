import React from "react";
import ForPDIChart from "forpdi/jsx/core/widget/ForPDIChart.jsx"
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({
  getInitialState() {
    return {
        options: {
          //title: Messages.getEditable("label.indicatorsHistory","fpdi-nav-label"),
          vAxis: {
            title: 'Desempenho (%)', minValue: 0
          },
          hAxis: {
            title: 'Período'
          },
          legend: 'none',
          "width":"100%"
        },
        data: [],
        plan: -1,
        subPlan: -1,
        indicators: [],
        loading: true,
        pageSize: 5,
        total: 0,
        selectedIndicator: {id: null}
    };
  },


  componentWillReceiveProps(newProps){
		this.setState({
		plan: newProps.plan,
		subPlan: newProps.subPlan,
		indicators: [],
		loading: true
		});

		StructureStore.dispatch({
		action: StructureStore.ACTION_GET_INDICATORS_MACRO_PLAN,
		data: {
			macroId: (newProps.plan!= -1)?(newProps.plan.id):(null),
			planId: (newProps.subPlan != -1)?(newProps.subPlan.id):(null)
		}
		});
		this.getInfos(1, this.state.pageSize, newProps);
  },

  componentDidMount() {
    var me = this;
    DashboardStore.on("indicatorsHistoryRetrivied", (store) => {
		var data = [];
		data.push(['Período', 'Desempenho (%)']);
		if(store.data.length > 0){
			store.data.map( (item) => {
			var valueForGraph = {
				v: item.value,
				f: parseFloat(item.value.toFixed(2))+"%"
			};
			data.push([item.period, valueForGraph]);
			});
		} else {
			data.push(["Nenhum Histórico Encontrado", 0]);
		}
		me.setState({
			data: data,
			loading: false,
			total: store.total
		});
    });
    StructureStore.on("indicatorsByMacroAndPlanRetrivied", (store) => {
		me.setState({
			indicators: store.data,
			loading: false
		});
    });
  },

  componentWillUnmount() {
      DashboardStore.off(null, null, this);
      StructureStore.off(null, null, this);
  },

  indicatorsChange(){
    this.state.selectedIndicator = this.state.indicators[this.refs['selectIndicator'].value] || {id: null};
    this.setState({
      loading: true
    });

    this.getInfos(1, this.state.pageSize);
  },

  hideFields() {
    this.setState({
      hide: !this.state.hide
    })
  },

  getInfos(page, pageSize, opt){
    opt = opt || this.state;
    DashboardStore.dispatch({
      action: DashboardStore.ACTION_GET_INDICATORS_HISTORY,
      data: {
        macro: (opt.plan!= -1)?(opt.plan.id):(null),
        plan: (opt.subPlan != -1)?(opt.subPlan.id):(null),
        indicator: this.state.selectedIndicator.id,
        page: page,
        pageSize: pageSize
      }
    });
  },

  render() {
    return (
      <div className="panel panel-default">
        <div className="panel-heading dashboard-panel-title">
          <b className="budget-graphic-title"> {Messages.getEditable("label.indicatorsHistory","fpdi-nav-label")} </b>
          <span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15 floatRight"):("mdi mdi-chevron-down marginLeft15 floatRight")} onClick={this.hideFields}/>
          <select onChange={this.indicatorsChange} className="form-control dashboard-select-box-graphs marginLeft10" ref="selectIndicator">
              <option value={-1} data-placement="right" title={Messages.get("label.allIndicators")}>{Messages.get("label.allIndicators")} </option>
                {this.state.indicators.map((attr, idy) =>{
                  return(
                    <option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                        {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                    </option>);
                })}
          </select>
        </div>
        {!this.state.hide ?
            (this.state.loading ? <LoadingGauge/> :
                <div>
                    <ForPDIChart
                      chartType="LineChart"
                      options={this.state.options}
                      data={this.state.data}
                      graph_id="ScatterChart"
                      width="100%"
                      height="300px"
                      legend_toggle={true}
                      pageSize={this.state.pageSize}
                      total={this.state.total}
                      onChangePage={this.getInfos}
                    />
                </div>
            )
        :""}

      </div>
    );
  }
});
