import React from "react";
import Messages from "@/core/util/Messages";
import PlanRiskTree from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTree.jsx";
import PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlaRiskItem.jsx"
import {Link} from "react-router";
import {number} from "prop-types";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
	},

	propTypes: {
		location: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			resultSearch: [],
			isLoading: true,
			planRiskData: [],
			planRiskId: null
		};
	},

	componentDidMount() {
		PlanRiskStore.on('retrivedplanrisk', (response) => {
			if (response !== null) {
				this.setState({
					planRiskData: response,
					planRiskId: response.get("id"),
					isLoading: false
				});
			}
		});

		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: this.props.params.planRiskId
		});

		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
			data: {
				planRiskId: this.props.params.planRiskId
			}
		});

		this.forceUpdate();
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.planRiskId != this.state.planRiskId) {
			PlanRiskStore.dispatch({
				action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
				data: newProps.params.planRiskId
			});

			PlanRiskItemStore.dispatch({
				action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
				data: {
					planRiskId: newProps.params.planRiskId
				}
			});
		}
	},

	componentWillUnmount() {
		PlanRiskStore.off(null, null, this);
		PlanRiskItemStore.off(null, null, this);
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		if (this.state.planRiskData) {
			return (
				<div className="fpdi-plan-details">
					<PlanRiskTree planRisk={this.state.planRiskData} ref="tree" treeType={this.props.route.path}/>
					<div className="fpdi-plan-tabs">
						<PlanRiskTabPanel
							{...this.props}
							planRisk={this.state.planRiskData}
							ref={"tabpanel-" + this.props.params.planRiskId}
							key={"tabpanel-" + this.props.params.planRiskId}
						/>
					</div>
				</div>
			)
		}
	}
});
