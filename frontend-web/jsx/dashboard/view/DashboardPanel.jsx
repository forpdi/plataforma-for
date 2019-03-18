import React from "react";
import string from 'string';
import Messages from "forpdi/jsx/core/util/Messages.jsx";

import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import DashboardAdminView from "forpdi/jsx/dashboard/view/DashboardAdminView.jsx";
import DashboardColaboratorView from "forpdi/jsx/dashboard/view/DashboardColaboratorView.jsx";
import DashboardManagerView from "forpdi/jsx/dashboard/view/DashboardManagerView.jsx";

export default React.createClass({
  contextTypes: {
    router: React.PropTypes.object,
    accessLevel: React.PropTypes.number.isRequired,
    accessLevels: React.PropTypes.object.isRequired,
    permissions: React.PropTypes.array.isRequired,
    roles: React.PropTypes.object.isRequired
  },

  getInitialState() {
    return {
      plans: [],
      selectedPlan:-1,
      subplans:[],
      selectedSubplan:-1,
      loaded:false
    };
  },

  componentDidMount() {
    var me = this;

    // Listener que aguarda uma listagem de planos macros
    PlanMacroStore.on("find", (store) => {
      me.setState({
        plans: store.models,
        loaded: true,
      });
      me.forceUpdate();
    }, me);

    // Listener que aguarda uma listagem de planos de metas de um plano macro
    PlanStore.on("find", (store, raw, opts) => {
      me.setState({
        subplans: raw,
      });
      me.forceUpdate();
    }, me);

    // Dispatcher para obter os planos macros
    if (EnvInfo.company != null) {
      PlanMacroStore.dispatch({ action: PlanMacroStore.ACTION_FIND });
      PlanMacroStore.dispatch({ action: PlanMacroStore.ACTION_FIND_ARCHIVED });
      PlanMacroStore.dispatch({ action: PlanMacroStore.ACTION_FIND_UNARCHIVED });
    }
  },

  componentWillUnmount() {
    PlanStore.off(null, null, this);
    PlanMacroStore.off(null, null, this);
  },

  planMacroChange(data) {
    // Verifica se a opção dos planos macro selecionada é a de todos os planos
    if (this.refs.selectPlanMacro.value == -1 && EnvInfo.company != null) {
      PlanMacroStore.dispatch({
        action: PlanMacroStore.ACTION_FIND
      });
      this.setState({
        selectedPlan:-1,
        selectedSubplan: -1,
        subplans: []
      });
    } else {
      this.setState({
        selectedPlan:this.state.plans[this.refs.selectPlanMacro.value],
        selectedSubplan: -1,
        subplans: []
      });
      PlanStore.dispatch({
        action: PlanStore.ACTION_FIND,
        data: {
          parentId: this.state.plans[this.refs.selectPlanMacro.value].get("id"),
        },
        opts: {
          wait: true
        }
     });
    }
  },

  subplanChange() {
    this.setState({
      selectedSubplan: (this.refs.selectSubplan.value == -1 ? -1 : this.state.subplans[this.refs.selectSubplan.value])
    });
    PlanStore.dispatch({
      action: PlanStore.ACTION_FIND,
      data: {
        parentId: this.state.plans[this.refs.selectPlanMacro.value].id,
      },
   });
  },

  renderDashboard() {
    if (this.context.roles.ADMIN) {
     return (<DashboardAdminView plan={this.state.selectedPlan} subPlan={this.state.selectedSubplan} />);
    } else if (this.context.roles.MANAGER) {
     return (<DashboardManagerView plan={this.state.selectedPlan} subPlan={this.state.selectedSubplan} />);
    } else if (this.context.roles.COLABORATOR) {
     return (<DashboardColaboratorView plan={this.state.selectedPlan} subPlan={this.state.selectedSubplan} />);
    }
  },

	render() {
		return (
			<div className="dashboard-container">
				<h1 className="marginLeft30">{Messages.getEditable("label.dashboard","fpdi-nav-label")}</h1>
        <div className="marginLeft30">
          <span className="marginRight20 marginBottom10">
            <span className="fpdi-nav-label">
              {Messages.getEditable("label.title.plan","fpdi-nav-label")}&nbsp;
            </span>
            <select
              onChange={this.planMacroChange}
              className="form-control dashboard-select-box"
              ref="selectPlanMacro"
              disabled={!this.state.loaded}
            >
              <option value={-1} data-placement="right" title={Messages.get("label.viewAll")}>
                {Messages.get("label.viewAll")}
              </option>
              {
                this.state.plans.map((attr, idy) => {
                  return (
                    <option
                      key={attr.get('id')}
                      value={idy}
                      data-placement="right"
                      title={attr.get("name")}
                    >
                      {
                        (attr.get("name").length > 20)
                        ?
                        (string(attr.get("name")).trim().substr(0, 20).concat("...").toString())
                        :
                        (attr.get("name"))
                      }
                    </option>
                  );
                })
              }
		        </select>
          </span>
          <span>
            <span className="fpdi-nav-label">
              {Messages.getEditable("label.title.goalsPlan","fpdi-nav-label")}&nbsp;
            </span>
		        <select
              onChange={this.subplanChange}
              ref="selectSubplan"
              className={
                (this.state.selectedPlan < 0)
                ?
                "form-control dashboard-select-box dashboard-select-box-disabled"
                :
                "form-control dashboard-select-box"
              }
	            disabled={
                (this.state.selectedPlan < 0)
                ?
                ("disabled")
                :
                ("")
              }
            >
              <option value={-1} data-placement="right" title={Messages.get("label.viewAll")}>
                {Messages.get("label.viewAll")}
              </option>
              {
                (this.state.subplans)
                ?
                (this.state.subplans.map((attr, idy) => {
  				        return(
                    <option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                      {
                        (attr.name.length>20)
                        ?
                        (string(attr.name).trim().substr(0, 20).concat("...").toString())
                        :
                        (attr.name)
                      }
                    </option>
                  );
  			        }))
                :
                ("")
              }
  					</select>
          </span>
        </div>
				{this.renderDashboard()}
			</div>
		);
	}
});
