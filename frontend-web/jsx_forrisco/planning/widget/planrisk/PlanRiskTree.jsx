import React from "react";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import SearchResult from "forpdi/jsx/planning/widget/search/SearchResult.jsx";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import {Link} from "react-router";
import LoadingGauge from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails";
import Messages from "@/core/util/Messages";

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
			treeItens: [],
		};
	},

	componentDidMount() {
		var newItem = '/forrisco/plan-risk/' + this.props.planRisk.attributes.id + '/item/new';
		var treeItens = [];

		treeItens.push({
			label: Messages.get("label.newItem"),
			labelCls: 'fpdi-new-node-label',
			iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
			to: newItem,
			key: "newPlanRiskItem"
		});

		this.setState({
			treeItens: treeItens
		})
	},

	render() {
		return (
			<div className="fpdi-tabs">
				<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
					<Link role="tab" title="Plano" activeClassName="active" className="tabTreePanel">
						{Messages.getEditable("label.plan", "fpdi-nav-label")}
					</Link>

					<Link role="tab" title="Plano" activeClassName="active" className="tabTreePanel">
						<span className="fpdi-nav-label">Unidade</span>
					</Link>
				</ul>

				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">

					<div
						className="marginBottom10 inner-addon right-addon right-addonPesquisa plan-search-border">
						<i className="mdiClose mdi mdi-close pointer" onClick={this.resultSearch}
						   title={Messages.get("label.clean")}> </i>
						<input type="text" className="form-control-busca" ref="term"
							   onKeyDown={this.onKeyDown}/>
						<i className="mdiBsc mdi mdi-chevron-down pointer" onClick={this.searchFilter}
						   title={Messages.get("label.advancedSearch")}> </i>
						<i id="searchIcon" className="mdiIconPesquisa mdiBsc  mdi mdi-magnify pointer"
						   onClick={this.treeSearch} title={Messages.get("label.search")}> </i>
					</div>

					<TreeView tree={this.state.treeItens}/>
				</div>

			</div>
		)
	},
})
