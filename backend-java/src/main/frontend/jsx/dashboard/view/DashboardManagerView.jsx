import React from "react";
import PerformanceIndicators from "forpdi/jsx/dashboard/view/manager/PerformanceIndicators.jsx";
import GoalsInfoTable from "forpdi/jsx/dashboard/view/manager/GoalsInfoTable.jsx";
import IndicatorsHistory from "forpdi/jsx/dashboard/view/manager/IndicatorsHistory.jsx";
import GoalsInfo from "forpdi/jsx/dashboard/view/admin/GoalsInfo.jsx";
import DashboardStore from "forpdi/jsx/dashboard/store/Dashboard.jsx";
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
			plan: newProps.plan,
			subPlan: newProps.subPlan
		});
    },

    componentDidMount(){
    	var me = this;
    },

	render() {
		return (
			<div className='marginLeft30'>
				{(EnvInfo.company && EnvInfo.company.showBudgetElement == true) ?
                    (<div>
						<div className="row">
							<div className="col-md-7">
								<PerformanceIndicators plan={this.state.plan} subPlan={this.state.subPlan}  profile={this.state.profile}/>
							</div>
							<div className="col-md-5">
								<Budget plan={this.state.plan} subPlan={this.state.subPlan} profile={this.state.profile} />
							</div>
						</div>
						<div className="row">
							<div className="col-md-7">
								<GoalsInfo plan={this.state.plan} subPlan={this.state.subPlan}/>
							</div>
							<div className="col-md-5">
								<IndicatorsHistory plan={this.state.plan} subPlan={this.state.subPlan}/>
							</div>
						</div>
						<div>
							<GoalsInfoTable plan={this.state.plan} subPlan={this.state.subPlan}/>
						</div>
					</div>)
                :
					(<div>
						<div className="row">
							<div className="col-md-12">
								<PerformanceIndicators plan={this.state.plan} subPlan={this.state.subPlan}  profile={this.state.profile}/>
							</div>
						</div>
						<div className="row">
							<div className="col-md-7">
								<GoalsInfo plan={this.state.plan} subPlan={this.state.subPlan}/>
							</div>
							<div className="col-md-5">
								<IndicatorsHistory plan={this.state.plan} subPlan={this.state.subPlan}/>
							</div>
						</div>
						<div>
							<GoalsInfoTable plan={this.state.plan} subPlan={this.state.subPlan}/>
						</div>
					</div>)
                }
			</div>
        );
	}
});
