import React from "react";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import _ from "underscore";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		planRisk: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			planRisk: [],
			fields: [],
			title: '',
			description: '',
			policy: ''
		}
	},

	componentDidMount() {
		PlanRiskStore.on('retrivedplanrisk', response => {
			this.setState({
				title: response.attributes.name,
				description: response.attributes.description,
				policy: response.attributes.policy.name
			});
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname,  response.attributes.name);
			});
		});
	},

	componentWillMount() {
		PlanRiskStore.off('retrivedplanrisk');
	},

	render() {
		return (
			<div>
				<div className="fpdi-card fpdi-card-full floatLeft">
					<h1>
						{this.state.title}
					</h1>
					{
						this.state.description ?
							<div>
								<h3>{"DESCRIÇÃO"}</h3>
								{this.state.description}
								<br/>
								<h3>{"POLÍTICA VINCULADA"} </h3>
								{this.state.policy}
							</div>
							: ""
					}
				</div>
			</div>
		)
	}
})
