import React from "react";
import PlanRiskTree from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTree.jsx";
import PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import UnitStore  from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import UnitItemStore from "forpdi/jsx_forrisco/planning/store/UnitItem.jsx"
import {Link} from "react-router";
import {number} from "prop-types";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
	},

	getInitialState() {
		return {
			resultSearch: [],
			isLoading: true,
			planRiskData: [],
			unitData: [],
			planRiskId: null,
			unitId: null
		};
	},

	componentDidMount() {
		PlanRiskStore.on('retrivedplanrisk', (response) => {
			if (response !== null) {
				this.setState({
					planRiskData: response,
					isLoading: false
				});
			}
		}, this);
		UnitStore.on('retrivedunit', (response) => {
			if (response !== null) {
				this.setState({
					unitData: response,
					unitId: response.get("id"),
					isLoading: false
				});
			}
		}, this);
		this.refreshData(this.props.params.planRiskId);
	},

	componentWillUnmount() {
		PlanRiskStore.off(null, null, this);
		PlanRiskItemStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.planRiskId !== this.props.params.planRiskId) {
			this.refreshData(newProps.params.planRiskId);
		}

		// if(this.props.location.action !== newProps.location.action) {
		// 	PlanRiskStore.on('retrivedplanrisk', (response) => {
		// 		if (response !== null) {
		// 			this.setState({
		// 				planRiskData: response,
		// 				isLoading: false
		// 			});
		// 		}
		// 	}, this);
		// }
	},

	refreshData(planRiskId) {
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: planRiskId
		});

		UnitStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: this.props.params.unitId
		});

		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
			data: { planRiskId }
		});

		UnitItemStore.dispatch({
			action: UnitItemStore.ACTION_GET_ALL_ITENS,
			data: {
				unitId: this.props.params.unitId
			}
		});

		this.forceUpdate();
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		if (this.state.planRiskData) {
			return (
				<div className="fpdi-plan-details">
					<PlanRiskTree
						planRisk={this.state.planRiskData}
						unit={this.state.planRiskData}
						ref="tree"
						treeType={this.props.route.path}
						location={this.props.location}
					/>
					<div className="fpdi-plan-tabs">
						<PlanRiskTabPanel
							{...this.props}
							planRisk={this.state.planRiskData}
							ref={"tabpanel-" + this.props.params.planRiskId}
							key={"tabpanel-" + this.props.params.planRiskId}
						/>
					</div>
				</div>
			);
		}
		return <p>Nenhum dado de plano de risco encontrado.</p>;
	}
});
