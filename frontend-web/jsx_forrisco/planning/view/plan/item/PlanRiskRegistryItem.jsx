import React from "react";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			planRisk: []
		}
	},

	render() {
		return(
			<span>bolo</span>
		)
	}
})
