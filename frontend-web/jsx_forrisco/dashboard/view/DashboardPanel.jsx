import React from "react";
import DashboardAdminView from "forpdi/jsx_forrisco/dashboard/view/DashboardAdminView.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

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
			selectedPlan:0,
			plans: [{name:"nome", id:1}],
			units:[],
			risks:[],
            loaded:false
        };
    },



	// PlanRisco Model
	//se PlanRisco >1 selectbox de planoRisco
    componentDidMount(){
		var me = this;
		PlanRiskStore.on("find", (store) => {
            me.setState({
                plans: store.models,
                loaded:true
            });
            me.forceUpdate();
		}, me);

		PlanRiskStore.on("retrieve", (model) =>{
			console.log("retrieve plan", model)
		}, me);

		me.setState({
			selectedPlan: 0,
			loaded:true
		});

		RiskStore.on("riskbyunit",(model) =>{
			this.state.risks.push({unitid:  model.total ,array:model.data})
			this.setState({
				risks:this.state.risks,
				loading:false
			})
		},me);

		UnitStore.on("unitbyplan",(model) =>{
			this.state.risks=[]
			for(var i=0; i<model.data.length;i++){
				RiskStore.dispatch({
					action: RiskStore.ACTION_FIND_BY_UNIT,
					data: model.data[i].id
				});
			}
			me.setState({
				units: model.data,
				loading:true
			});
		},me);

		this.refresh()
	},

	componentWillReceiveProps(newProps){
		var me = this;
		this.state.plan=newProps.plan
		this.setState({
			plan: newProps.plan,
			loading: true,
		});
	},

	refresh(){
		this.state.plan=1

		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data: this.state.plan
		});

		/*PlanRiskStore.on("retrieve", (model) =>{
			this.state.policyId=2

				PolicyStore.dispatch({
					action: PolicyStore.ACTION_RETRIEVE,
					data: this.state.policyId
				});
				}, me);
			*/
		/*PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE,
			data: newProps.plan
		});*/
	},

	componentWillUnmount() {
        PlanRiskStore.off(null, null, this);
		UnitStore.off(null, null, this);
		RiskStore.off(null, null, this);
	},


    planRiscoChange(data){
        if(this.refs.selectPlanMacro.value == -1 && EnvInfo.company != null){
            PlanMacroStore.dispatch({
                action: PlanMacroStore.ACTION_FIND
            });
            this.setState({
                selectedPlan:-1,
                selectedSubplan: -1,
                subplans: []
            });
        }else{
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

	exportReport(){
		console.log("exportar relatório")
	  },


	render() {
		/*console.log("policy",this.state.policy)
		console.log("plans",this.state.plans)
		console.log("units",this.state.units)
		console.log("risks",this.state.risks)
		*/
		return (
			<div className="dashboard-container">
				<h1 className="marginLeft30">{Messages.getEditable("label.dashboard","forrisco-nav-label")}</h1>
				{(
                    <div className="marginLeft30">
                        <span>

							{(this.state.plans.length<2) ? "" :
							<div>
							 <span className = "fpdi-nav-label">{Messages.getEditable("label.risk.Plans","fpdi-nav-label")}&nbsp;</span>
    				        <select  onChange={this.planChange} ref="selectPlan" className={"form-control dashboard-select-box"}
    				            disabled={(this.state.selectedPlan<0)?("disabled"):("")}>
    					            <option value={-1} data-placement="right" title={Messages.get("label.viewAll")}>
                                        {Messages.get("label.viewAll")}
                                    </option>
    					            {(this.state.plans)?(this.state.plans.map((attr, idy) =>{
    							        return(
                                            <option key={attr.id} value={idy} data-placement="right" title={attr.name}>
                                                {(attr.name.length>20)?(string(attr.name).trim().substr(0, 20).concat("...").toString()):(attr.name)}
                                            </option>
                                        );
    						        })):("")}
        					</select>
							</div>}
                        </span>
						<span onClick={this.exportReport} className="btn btn-sm btn-primary" style={{margin: "0 10px"}}>
								{Messages.getEditable("label.exportReport")}
						</span>
                    </div>)}
					<DashboardAdminView plan={this.state.plans[this.state.selectedPlan]} units={this.state.units}  risks={this.state.risks}/>
			</div>
		);
	}
});
