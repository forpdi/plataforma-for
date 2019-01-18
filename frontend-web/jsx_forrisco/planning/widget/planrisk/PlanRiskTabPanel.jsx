import React from "react";
import {Link} from 'react-router';


export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	propTypes: {
		planRisk: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			tabs: [],
			tabsHash: '',
			tabsHidden: [],
			showTabsHidden: false
		};
	},

	componentWillReceiveProps() {
	},

	render() {
		return(
			<div>
				<span>
					.
				</span>
			</div>
		)
	}
})
