import React from "react";
import Messages from "@/core/util/Messages";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import {Link} from "react-router";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item";

export default React.createClass({
	contextTypes: {
		accessLevel: React.PropTypes.number.isRequired,
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	getInitialState() {
		return {
			planRiskModel: null,
			resultSearch: [],
			hiddenResultSearch: false,
			tree: [{
				label: 'yolo',
			}],
		};
	},

	componentDidMount() {
		// PlanRiskStore.on('retrievePlanRisk', (model) => {
		// 	console.log(model);
		// })
	},

	componentWillReceiveProps(newProps) {
		//console.log(newProps.params.planRiskId);
		console.log(document.URL.indexOf);
		// if (document.URL.indexOf('plan-risk') >= 0) {
		// 	this.refreshPlans(newProps.policy.get("id"));
		// }
		// if (newProps.treeType == this.state.actualType) {
		// 	return;
		// }
		//
		// this.setState({
		// 	actualType: newProps.treeType
		// });
	},

	refreshPlans() {

	},

	render() {
		return (
			<div className="fpdi-tabs">
				<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
					<Link title="Plano" activeClassName="active" className="tabTreePanel">
						{Messages.getEditable("label.plan", "fpdi-nav-label")}
					</Link>

					<Link title="Unidade" activeClassName="active" className="tabTreePanel">
						Unidades
					</Link>
				</ul>

				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">
					<div className="marginBottom10 inner-addon right-addon right-addonPesquisa plan-search-border">
						<i className="mdiClose mdi mdi-close pointer" onClick={this.resultSearch} title={Messages.get("label.clean")}/>
						<input type="text" className="form-control-busca" ref="term" onKeyDown={this.onKeyDown}/>
						<i className="mdiBsc mdi mdi-chevron-down pointer" onClick={this.searchFilter} title={Messages.get("label.advancedSearch")}/>
						<i id="searchIcon" className="mdiIconPesquisa mdiBsc  mdi mdi-magnify pointer" onClick={this.treeSearch} title={Messages.get("label.search")}/>
					</div>
				</div>
			</div>
		)
	}
});
