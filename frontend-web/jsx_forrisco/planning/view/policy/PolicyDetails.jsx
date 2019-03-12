import React from "react";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PolicyTabPanel from "forpdi/jsx_forrisco/planning/widget/policy/PolicyTabPanel.jsx";
import PolicyTree from "forpdi/jsx_forrisco/planning/widget/policy/PolicyTree.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import {Link} from "react-router";

export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		permissions: React.PropTypes.array.isRequired,
		roles: React.PropTypes.object.isRequired
	},
	propTypes: {
		location: React.PropTypes.object.isRequired

	},
	getInitialState() {
		return {
			policyId: null,
			model: null
		};
	},

	componentDidMount() {
		var me = this;
		PolicyStore.on("findpolicy", (model) => {
			if(this.props.params.policyId){}
			me.setState({
				model: model.data,
				policyId: model.data.id
			});
		}, me);

		this.refreshData(this.props.params.policyId)
	},

	componentWillUnmount() {
		PolicyStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.policyId != this.state.policyId) {
			this.refreshData(newProps.params.policyId)
		}
	},

	refreshData(policyId) {
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_POLICY,
			data: policyId
		});
	},

	render() {
		if (!this.state.model) {
			return <LoadingGauge />;
		}
		if(this.state.model.deleted){
			return(<div className="fpdi-plan-details">
					<h1 className="marginLeft30">{Messages.getEditable("label.policyUnavailable","fpdi-nav-label")}</h1>
				</div>);
		}

		return (
			<div className="fpdi-plan-details">
			<div className="fpdi-tabs">
			<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
						<Link role="tab" title="Politica"  className={"tabTreePanel active"}
						to={"forrisco/policy/" + this.props.params.policyId + "/item/overview"}>
							{Messages.getEditable("label.plan", "fpdi-nav-label")}
						</Link>
					</ul>
				{this.state.policyId?
				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">
					<PolicyTree
						policy={this.state.model}
						policyId={this.props.params.policyId}
						ref="tree"
						treeType={this.props.route.path}
						//itemId={this.state.itemId}
						//subitemId={this.state.subitemId}
					/>
				</div>
				: <p>Nenhum dado de política encontrada.</p>}
			</div>

			<div className="fpdi-plan-tabs">
				<PolicyTabPanel
					{...this.props}
					policy={this.state.model}
					ref={"tabpanel-"+this.state.policyId}
					key={"tabpanel-"+this.state.policyId}
				/>
			</div>
		</div>
		)
	}
});
