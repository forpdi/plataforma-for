import React from "react";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import _ from "underscore";
import UnitItemStore from "forpdi/jsx_forrisco/planning/store/UnitItem";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		unit: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			unit: [],
			fields: [],
			title: '',
			description: '',
			policy: ''
		}
	},

	componentDidMount() {
		UnitStore.on('retrivedplanrisk', response => {
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

	componentWillUnmount() {
		// console.log('desmonto');
		// UnitStore.off('retrivedplanrisk');
	},

	componentWillReceiveProps(newProps) {
	},

	render() {
		console.log("U G I")
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
