import React from "react";
import PlanRiskTree from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTree.jsx";
import PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx"
import {Link} from "react-router";
import {number} from "prop-types";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import UnitTree from "forpdi/jsx_forrisco/planning/widget/unit/UnitTree.jsx";


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
		this.forceUpdate();
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		var planriskactive;

		if(!this.props.location.pathname.includes("unit")){
			planriskactive=true
		}

		if(planriskactive){
			return(<div className="fpdi-plan-details">
				<div className="fpdi-tabs">
					<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
						<Link role="tab" title="Plano"  className={"tabTreePanel active"}
						to={"forrisco/plan-risk/" + this.props.params.planRiskId + "/"}>
							{Messages.getEditable("label.plan", "fpdi-nav-label")}
						</Link>

						<Link role="tab" title="Unidade"  className={"tabTreePanel"}
						to={"forrisco/plan-risk/" + this.props.params.planRiskId + "/unit"}>
							{Messages.getEditable("label.unitys", "fpdi-nav-label")}
						</Link>
					</ul>
					{this.state.planRiskData?
					<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">
						<PlanRiskTree
							planRisk={this.state.planRiskData}
							//unit={this.state.planRiskData}
							ref="tree"
							treeType={this.props.route.path}
							location={this.props.location}
						/>
					</div>
					: <p>Nenhum dado de plano de risco encontrado.</p>}
				</div>

				<div className="fpdi-plan-tabs">
					<PlanRiskTabPanel
						{...this.props}
						planRisk={this.state.planRiskData}
						ref={"tabpanel-" + this.props.params.planRiskId}
						key={"tabpanel-" + this.props.params.planRiskId}
					/>
				</div>
			</div>)
		}else{
			return(
			<div className="fpdi-plan-details">
				<div className="fpdi-tabs">
					<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
						<Link role="tab" title="Plano"  className={"tabTreePanel"}
						to={"forrisco/plan-risk/" + this.props.params.planRiskId + "/"}>
							{Messages.getEditable("label.plan", "fpdi-nav-label")}
						</Link>
							<Link role="tab" title="Unidade"  className={"tabTreePanel active"}
						to={"forrisco/plan-risk/" + this.props.params.planRiskId + "/unit"}>
							{Messages.getEditable("label.unitys", "fpdi-nav-label")}
						</Link>
					</ul>
					{this.state.planRiskData?
				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">
					<UnitTree
						planRisk={this.state.planRiskData}
						unit={this.state.unitData}
						ref="tree"
						treeType={this.props.route.path}
						location={this.props.location}
					/>
				</div>
				: <p>Nenhum dado de plano de risco encontrado.</p>}
					</div>
					<div className="fpdi-plan-tabs">
					<PlanRiskTabPanel
						{...this.props}
						planRisk={this.state.planRiskData}
						ref={"tabpanel-" + this.props.params.planRiskId}
						key={"tabpanel-" + this.props.params.planRiskId}
					/>
				</div>
			</div>)
		}
	}
});
