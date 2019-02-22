import React from "react";
import Messages from "@/core/util/Messages";
import UnitTree from "forpdi/jsx_forrisco/planning/widget/unit/UnitTree.jsx";
import UnitTabPanel from "forpdi/jsx_forrisco/planning/widget/unit/UnitTabPanel.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import UnitItemStore from "forpdi/jsx_forrisco/planning/store/UnitItem.jsx"
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
			unitData: [],
			planRiskId: null,
			unitId: null
		};
	},

	componentDidMount() {
		console.log("D U")
		UnitStore.on('retrivedunit', (response) => {
			if (response !== null) {
				this.setState({
					planRiskData: response,
					planRiskId: response.get("id"),
					isLoading: false
				});
			}
		});

		UnitStore.on('retrivedunit', (response) => {
			if (response !== null) {
				this.setState({
					unitData: response,
					unitId: response.get("id"),
					isLoading: false
				});
			}
		});

		UnitStore.dispatch({
			action: UnitStore.ACTION_RETRIEVE_PLANRISK,
			data: this.props.params.planRiskId
		});


		UnitItemStore.dispatch({
			action: UnitItemStore.ACTION_GET_ALL_ITENS,
			data: {
				planRiskId: this.props.params.planRiskId
			}
		});


		this.forceUpdate();
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.planRiskId !== this.state.planRiskId) {

			UnitStore.dispatch({
				action: UnitStore.ACTION_RETRIEVE_PLANRISK,
				data: newProps.params.planRiskId
			});

			UnitItemStore.dispatch({
				action: UnitItemStore.ACTION_GET_ALL_ITENS,
				data: {
					planRiskId: newProps.params.planRiskId
				}
			});
		}
	},

	render() {
		if (this.state.isLoading === true) {
			//return <LoadingGauge/>;
		}

		if (this.state.planRiskData) {
			return (
				<div className="fpdi-plan-details">
					<UnitTree planRisk={this.state.planRiskData} unit={this.state.planRiskData} ref="tree" treeType={this.props.route.path}/>
					<div className="fpdi-plan-tabs">
						<UnitTabPanel
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
