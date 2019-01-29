import React from "react";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import PolicyTabPanel from "forpdi/jsx_forrisco/planning/widget/plan/PolicyTabPanel.jsx";
import PolicyTree from "forpdi/jsx_forrisco/planning/widget/plan/PolicyTree.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import _ from 'underscore';

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
		PolicyStore.on("retrieve", (model) => {
			me.setState({
				model: model,
				policyId: model.get("id")
			});
		}, me);

		PolicyStore.dispatch({
			action: PolicyStore.ACTION_RETRIEVE,
			data: this.props.params.policyId
		});
	},

	componentWillUnmount() {
		PolicyStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.params.policyId !== this.state.policyId) {
			this.setState({
				model: null,
				policyId: null
			});
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE,
				data: newProps.params.policyId
			});
		}
	},

	render() {
		if (!this.state.model) {
			return <LoadingGauge />;
		}
		if(this.state.model.attributes.deleted){
			return(<div className="fpdi-plan-details">
					<h1 className="marginLeft30">{Messages.getEditable("label.policyUnavailable","fpdi-nav-label")}</h1>
				</div>);
		}
		if(this.state.model.attributes.archived){
			if(this.context.roles.ADMIN || _.contains(this.context.permissions, PermissionsTypes.MANAGE_FORRISCO_POLICY_PERMISSION)){
				return (<div className="fpdi-plan-details">
					<PolicyTree policy={this.state.model} ref="tree">
						<div className="fpdi-plan-tabs">
							<PolicyTabPanel
								{...this.props}
								policy={this.state.model}
								ref={"tabpanel-"+this.state.policyId}
								key={"tabpanel-"+this.state.policyId} />
						</div>
					</PolicyTree>
					</div>
				);
			}else{
				return(<div className="fpdi-plan-details">
					<h1 className="marginLeft30">{Messages.getEditable("label.planFiledNoPermission","fpdi-nav-label")}</h1>
				</div>);
			}
		}else{
			return (<div className="fpdi-plan-details">
				<PolicyTree policy={this.state.model} ref="tree" treeType={this.props.route.path}/>
					<div className="fpdi-plan-tabs">'
						<PolicyTabPanel
							{...this.props}
							policy={this.state.model}
							ref={"tabpanel-"+this.state.policyId}
							key={"tabpanel-"+this.state.policyId} />
					</div>
				</div>
			)
		}
	}
});
