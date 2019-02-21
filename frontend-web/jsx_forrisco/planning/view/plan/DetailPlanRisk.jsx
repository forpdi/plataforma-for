import React from "react";
import PlanRiskTree from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTree.jsx";
import PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
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
	},

	refreshData(planRiskId) {
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_RETRIEVE_PLANRISK,
			data: planRiskId
		});

		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
			data: { planRiskId }
		});
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
			);
		}
		return <p>Nenhum dado de plano de risco encontrado.</p>;
	}
});
