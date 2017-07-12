import React from "react";
import Progress from 'react-progressbar';
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import LoadingGaugeWhite from "forpdi/jsx/core/widget/LoadingGaugeWhite.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({

    getInitialState() {
        return {
        	plan:this.props.plan,
        	subplan:this.props.subPlan,
        	loading: true
        };
    },

    componentWillReceiveProps(newProps){
    	var me = this;
    	if(this.isMounted()) {
	    	me.setState({
	    		plan:newProps.plan,
	    		subplan:newProps.subPlan,
	    		loading: true
	    	});
	    }

	    DashboardStore.dispatch({
	    	action: DashboardStore.ACTION_GET_PLAN_DETAILS_COMMUNITY, 
	        data: {
           		macro: (this.state.plan != -1)?(newProps.plan.id):(null),
                plan:(this.state.subplan != -1)?(newProps.subPlan.id):(null)
           	}         
	    });

    },

    componentDidMount(){
    	var me = this;

		DashboardStore.dispatch({
	    	action: DashboardStore.ACTION_GET_PLAN_DETAILS_COMMUNITY, 
	         data: {
             	macro: (this.state.plan != -1)?(this.state.plan.id):(null),
                plan:(this.state.subplan != -1)?(this.state.subplan.id):(null)
             }         
	    });

	    DashboardStore.on("planDetailsRetrieved", (store) =>{
    		if(this.isMounted()) {
	        	me.setState({        		
	        		planDetails: store.data,
	        		loading: false
	        	})
	        }
        });
       
    },

    hideFields() {
	    this.setState({
	      hide: !this.state.hide
	    })
	},
	

	componentWillUnmount() {
		DashboardStore.off(null, null, this);
	},


	render() {
		var dashboardTitlePlan = "";

		if (this.state.plan != -1) {
			dashboardTitlePlan = this.state.plan.name;		
		} else {
			dashboardTitlePlan = "";
		}

	  	var dashboardTitle = "";
	  	
      	if (this.state.subplan == -1)
       	 	dashboardTitle = " - Todos os planos de metas";
     	else if (this.state.subplan)
        	dashboardTitle = " - "+this.state.subplan.name;

        var planName;
        if (typeof this.state.plan.get != 'function') {
            planName = this.state.plan.name;
        } else {
            planName = this.state.plan.get("name");
        }

		return (
			<div className={this.props.className}>
				<div className="panel">
					<div className="dashboard-plan-details-header">
					<b className="budget-graphic-title"> 
						<span className="fpdi-nav-label" title = {(dashboardTitlePlan+dashboardTitle).length > 55 ? 
							(dashboardTitlePlan+dashboardTitle) : ""}> {(this.state.plan == -1)?(""): 
							(((planName+dashboardTitle).length) <= 55?(planName+dashboardTitle):
								((planName+dashboardTitle).split("",55).concat(" ...")))} 
						</span>
					</b>
					<div className="performance-strategic-btns floatRight">
		              	<span  className={(this.state.hide)?("mdi mdi-chevron-right marginLeft15"):
		              	("mdi mdi-chevron-down marginLeft15")}  onClick={this.hideFields}/>
		            </div>
					</div>
					{(this.state.hide)?(""):					
					<div className="dashboard-plan-details-body-commmunity">
						{(this.state.loading ? <LoadingGaugeWhite/> :
							<div>
								<div className="dashboard-indicator-container-commmunity">
									<div className="col-sm-3 dashboard-plan-details-column">
										<div className="dashboard-indicator-header-axis">Eixos Temáticos </div>
										<div className="dashboard-indicator-number">{this.state.planDetails.numberOfIndicatorsThematicAxis}</div>
									</div>
									<div className="col-sm-3 dashboard-plan-details-column">
										<div className="dashboard-indicator-header">{Messages.get("label.objective")}s</div>
										<div className="dashboard-indicator-number">{this.state.planDetails.numberOfObjectives}</div> 
									</div>
									<div className="col-sm-3 dashboard-plan-details-column">
										<div className="dashboard-indicator-header">{Messages.get("label.indicators")}</div>
										<div className="dashboard-indicator-number">{this.state.planDetails.numberOfIndicators}</div>
									</div> 
									<div className="col-sm-3 dashboard-plan-details-column">
										<div className="dashboard-indicator-header">Metas</div>
										<div className="dashboard-indicator-number">{this.state.planDetails.numberOfGoals}</div>
									</div> 
								</div>
								<div className="dashboard-goals-information-commmunity">
								    <div className='dashboard-goals-title'>Metas Alcançadas </div>
								    <div className="fontSize12 ">
								   		<div className="dashboard-goals-head">
									   		<span className='fontWeightBold'>
									   			{this.state.planDetails.goalsDelayedPerCent.toFixed(2)}%
									   		</span> das metas estão alcançadas 
									   		<span className='fontWeightBold floatRight'>100%</span>
								   		</div>
								   	</div>
									<Progress completed={this.state.planDetails.goalsDelayedPerCent} />
								</div>
							</div>
						)}						
					</div>}
				</div>
			</div>
			);
	}
});
