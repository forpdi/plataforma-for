import React from "react";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";

export default React.createClass({

	getInitialState() {
        return {
        	info: {
        		inDayPercentage: 0.0,
        		latePercentage: 0.0,
        		belowMininum: 0,
        		belowMinimumPercentage: 0.0,
        		belowExpected: 0,
        		belowExpectedPercentage: 0.0,
        		reached: 0,
        		reachedPercentage: 0.0,
        		aboveExpected: 0,
        		aboveExpectedPercentage: 0.0,
        	},
        	hide: false,
        	plan: -1,
        	subPlan: -1
        };
    },

	componentDidMount(){
		
    },

    componentWillReceiveProps(newProps){    	
    	if(newProps.info){
	    	this.setState({
    			info: newProps.info,
    			plan: newProps.plan,
    			subPlan: newProps.subPlan
    		});
    	}
    },

    hide(){
    	this.setState({
    		hide: !this.state.hide
    	});
    },

	render() {
		return (
			<div className={this.props.className}>
				<div className="panel panel-default">
					<div className="panel-heading dashboard-panel-title">
						<b className="budget-graphic-title"> 
							{Messages.get("label.generalGoalInfo")+(this.state.plan != -1 ? " - "+this.state.plan.get("name") + 
								(this.state.subPlan != -1 ? " - "+this.state.subPlan.name : "")
								:"")
							}
						</b>
						<span className={this.state.hide ? "mdi mdi-chevron-right floatRight" : "mdi mdi-chevron-down floatRight"}
						 onClick={this.hide}/>
					</div>
					{this.state && this.state.info && !this.state.hide?
					<div className="panel-body dash-board-goals-info-adm">
						<div className="dashboard-goal-info col-sm-3">
							<h1>{this.state.info.belowMininum}</h1>
							<h4>{"("+this.state.info.belowMinimumPercentage+"%)"}</h4>
							<p>{Messages.get("label.goals.belowMinimum")}</p>
						</div>
						<div className="dashboard-goal-info col-sm-3">
							<h1>{this.state.info.belowExpected}</h1>
							<h4>{"("+this.state.info.belowExpectedPercentage+"%)"}</h4>
							<p>{Messages.get("label.goals.belowExpected")}</p>
						</div>
						<div className="dashboard-goal-info col-sm-3">
							<h1>{this.state.info.reached}</h1>
							<h4>{"("+this.state.info.reachedPercentage+"%)"}</h4>
							<p>{Messages.get("label.goals.reached")}</p>
						</div>
						<div className="dashboard-goal-info col-sm-3">
							<h1>{this.state.info.aboveExpected}</h1>
							<h4>{"("+this.state.info.aboveExpectedPercentage+"%)"}</h4>
							<p>{Messages.get("label.goals.aboveExpected")}</p>
						</div>
					</div> : ""}
				</div>
			</div>
		);
	}
});