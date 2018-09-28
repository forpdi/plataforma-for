import React from "react";
import DashboardPlanDetails from "forpdi/jsx/dashboard/view/DashboardPlanDetails.jsx";
import GoalsInfo from "forpdi/jsx/dashboard/view/admin/GoalsInfo.jsx";
import StrategicAxis from "forpdi/jsx/dashboard/view/admin/PerformanceStrategicAxis.jsx";
import PerformanceIndicators from "forpdi/jsx/dashboard/widget/PerformanceIndicators.jsx";
import Budget from "forpdi/jsx/dashboard/view/admin/Budget.jsx";


export default React.createClass({

    contextTypes: {
        roles: React.PropTypes.object.isRequired
    },

    getInitialState() {
        return {
        	plan:this.props.plan,
        	subPlan:this.props.subPlan,
            profile: this.context.roles
        };
    },

    componentWillReceiveProps(newProps){
		this.setState({
			plan:newProps.plan,
			subPlan: newProps.subPlan
		});
    },

    componentDidMount(){

    },

	render() {
		return (
            <div className='marginLeft30'>
                {(EnvInfo.company && EnvInfo.company.showBudgetElement == true) ?
                    (<div className = "row">
						<div className="col-md-7">
							<StrategicAxis plan={this.state.plan} subPlan={this.state.subPlan} />
							<GoalsInfo plan={this.state.plan} subPlan={this.state.subPlan} />
						</div>
						<div className="col-md-5">
							<Budget plan={this.state.plan} subPlan={this.state.subPlan} profile={this.state.profile}/>
							<DashboardPlanDetails plan={this.state.plan}  subPlan={this.state.subPlan} />
						</div>
					</div>)
                :
					(<div>
						<div className = "row">
							<div className="col-md-12">
								<StrategicAxis plan={this.state.plan} subPlan={this.state.subPlan} />
							</div>
						</div>
						<div className = "row">
							<div className="col-md-12">
								<div className="col-md-6">
									<GoalsInfo plan={this.state.plan} subPlan={this.state.subPlan} />
								</div>
								<div className="col-md-6">
									<DashboardPlanDetails plan={this.state.plan}  subPlan={this.state.subPlan} />
								</div>
							</div>
						</div>
					</div>)
                }
            </div>

		);
	}
});
