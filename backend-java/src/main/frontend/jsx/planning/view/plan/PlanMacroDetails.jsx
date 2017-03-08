
import React from "react";
import {Link} from 'react-router';

import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";

import PlanMacroTabPanel from "forpdi/jsx/planning/widget/plan/PlanMacroTabPanel.jsx";
import PlanMacroTree from "forpdi/jsx/planning/widget/plan/PlanMacroTree.jsx";

import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
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
			planId: null,
			model: null
		};
	},
	componentDidMount() {
		var me = this;
		PlanMacroStore.on("retrieve", (model) => {
			me.setState({
				model: model,
				planId: model.get("id")
			});
		}, me);


		PlanMacroStore.dispatch({
			action: PlanMacroStore.ACTION_RETRIEVE,
			data: this.props.params.id
		});

	},
	componentWillUnmount() {
		PlanMacroStore.off(null, null, this);
	},
	componentWillReceiveProps(newProps) {
		if (newProps.params.id != this.state.planId) {
			this.setState({
				model: null,
				planId: null
			});
			PlanMacroStore.dispatch({
				action: PlanMacroStore.ACTION_RETRIEVE,
				data: newProps.params.id
			});
		}
	},

	render() {
		if (!this.state.model) {
			return <LoadingGauge />;
		}
		if(this.state.model.attributes.archived){
			if(this.context.roles.ADMIN || _.contains(this.context.permissions,PermissionsTypes.MANAGE_PLAN_MACRO_PERMISSION)){
				return (
					<div className="fpdi-plan-details">
						<PlanMacroTree plan={this.state.model} ref="tree" treeType={this.props.route.path}/>
						<div className="fpdi-plan-tabs">
							<PlanMacroTabPanel
								{...this.props}
								planMacro={this.state.model}
								ref={"tabpanel-"+this.state.planId}
								key={"tabpanel-"+this.state.planId} />
						</div>
					</div>
				);
			}else{
				return(<div className="fpdi-plan-details">
					<h1 className="marginLeft30">Esse plano encontra-se arquivado e você não possui permissão para acessá-lo.</h1>
				</div>);
			}
		}else{
			return (
					<div className="fpdi-plan-details">
						<PlanMacroTree plan={this.state.model} ref="tree" treeType={this.props.route.path}/>
						<div className="fpdi-plan-tabs">
							<PlanMacroTabPanel
								{...this.props}
								planMacro={this.state.model}
								ref={"tabpanel-"+this.state.planId}
								key={"tabpanel-"+this.state.planId} />
						</div>
					</div>
				)
		}
	}
});
