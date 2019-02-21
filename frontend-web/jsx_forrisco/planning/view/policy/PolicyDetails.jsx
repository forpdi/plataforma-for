import React from "react";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PolicyTabPanel from "forpdi/jsx_forrisco/planning/widget/policy/PolicyTabPanel.jsx";
import PolicyTree from "forpdi/jsx_forrisco/planning/widget/policy/PolicyTree.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";


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
			itemId: null,
			subitemId: null,
			model: null
		};
	},
	componentDidMount() {
		var me = this;
		PolicyStore.on("findpolicy", (model) => {
			me.setState({
				model: model.data,
				policyId: model.data.id
			});
		}, me);

		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_POLICY,
			data: this.props.params.policyId
		});
	},

	componentWillUnmount() {
		PolicyStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		this.state.itemId=newProps.params.itemId
		this.state.subitemId=newProps.params.subitemId
		if (newProps.params.policyId != this.state.policyId) {
			this.setState({
				model: null,
				policyId: null
			});
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_FIND_POLICY,
				data: newProps.params.policyId
			});
		}
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
			<PolicyTree policy={this.state.model} ref="tree" treeType={this.props.route.path} itemId={this.state.itemId} subitemId={this.state.subitemId}/>
			<div className="fpdi-plan-tabs">
				{<PolicyTabPanel
					{...this.props}
					policy={this.state.model}
					ref={"tabpanel-"+this.state.policyId}
					key={"tabpanel-"+this.state.policyId} />
				}
			</div>
		</div>
		)
	}
});
