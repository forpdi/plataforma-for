import React from "react";
import Messages from "@/core/util/Messages";
import PlanRiskTree from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTree.jsx";
import PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import {Link} from "react-router";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {
		return {
			resultSearch: [],
			isLoading: true,
			planRiskData: [],
		};
	},

	componentDidMount() {
		PlanRiskStore.on('retrivedplanrisk', (response) => {
			if (response !== null) {
				this.setState({
					planRiskData: response,
					isLoading: false
				})
			}
		})
	},

	componentWillReceiveProps(newProps) {
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: newProps.params.planRiskId
		});
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		if (this.state.planRiskData) {
			return (
				<div className="fpdi-plan-details">
					<PlanRiskTree planRisk={this.state.planRiskData} ref="tree"/>
					<div className="fpdi-plan-tabs">
						<PlanRiskTabPanel
							{...this.state.planRiskData}
							planRisk={this.state.planRiskData}
							ref={"tabpanel-" + this.state.planRiskData.planRiskId}
							key={"tabpanel-" + this.state.planRiskData.planRiskId}
						/>
					</div>
				</div>
			)
		}
	}
});
