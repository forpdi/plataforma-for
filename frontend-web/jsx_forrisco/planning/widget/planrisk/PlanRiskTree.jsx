import React from "react";
import LevelSearch from "forpdi/jsx/planning/widget/search/LevelSearch.jsx";
import SearchResult from "forpdi/jsx/planning/widget/search/SearchResult.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import {Link} from "react-router";
import LoadingGauge from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {
		return {
			tree: [],
		};
	},

	componentDidMount() {

	},

	render() {
		return (
			null
		)
	},
})
