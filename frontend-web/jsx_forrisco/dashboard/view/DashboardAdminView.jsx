import React from "react";

import RiskMatrix from "forpdi/jsx_forrisco/dashboard/view/admin/RiskMatrix.jsx";
import IncidentAxis from "forpdi/jsx_forrisco/dashboard/view/admin/IncidentAxis.jsx";
import MonitorDetails from "forpdi/jsx_forrisco/dashboard/view/admin/MonitorDetails.jsx";

import PerformanceIndicators from "forpdi/jsx/dashboard/widget/PerformanceIndicators.jsx";


export default React.createClass({

    contextTypes: {
        roles: React.PropTypes.object.isRequired
    },

    getInitialState() {
        return {
        	plan:this.props.plan,
        	units:this.props.units,
			risks:this.props.risks
        };
    },

    componentWillReceiveProps(newProps){
		this.setState({
			plan:newProps.plan,
		});
    },

    componentDidMount(){
    },

	render() {
		return (
      <div className='marginLeft30'>
        <div className="row">
					<RiskMatrix
            plan={this.props.plan}
            units={this.props.units}
            risks={this.props.risks}
          />
				</div>
				<div className="row">
					<div className="col-md-7">
						<IncidentAxis plan={this.props.plan} units={this.props.units}  risks={this.props.risks}/>
					</div>
					<div className="col-md-5">
						<MonitorDetails plan={this.props.plan} units={this.props.units}  risks={this.props.risks}/>
					</div>
				</div>
      </div>
		);
	}
});
