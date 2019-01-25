import React from "react";
import {Link} from 'react-router';


export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object.isRequired
	},

	childContextTypes: {
		planRisk: React.PropTypes.object,
		tabPanel: React.PropTypes.object
	},

	propTypes: {
		planRisk: React.PropTypes.object.isRequired
	},

	getChildContext() {
		return {
			planRisk: this.props.planRisk,
			tabPanel: this
		};
	},

	getInitialState() {
		return {
			tabs: [],
			tabsHash: '',
			tabsHidden: [],
			showTabsHidden: false
		};
	},

	componentDidMount() {

	},

	addTab(path, title) {
		var tab, tabIndex = -1, hash = "", findPlan = -1;
	},

	render() {
		return(
			<div className="fpdi-tabs">
				<ul  className={"fpdi-tabs-nav hide-close"}  role="tablist">
					<div className={"fpdi-tabs-hidden show-border"}>

					</div>
				</ul>

				<div className="fpdi-tabs-content container-fluid">
					{this.props.children}
				</div>
			</div>
		)
	}
})
