import React from "react";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlaRiskItem.jsx"
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
			cleanTree: [],
			treeItens: [],
			newProps: null,
			actualType: this.props.treeType,
			prevProps: {},
			info: {},
			newItem: {}
		};
	},

	componentDidMount() {
		this.setTreeItens(this.props.planRisk);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.planRisk.id !== this.props.planRisk.id) {
			this.setTreeItens(newProps.planRisk);
		}
	},

	setTreeItens(planRisk, treeItens = []) {
		var me = this;
		var  info = {
			label: "Informações Gerais",
			expanded: false,
			to: '/forrisco/plan-risk/' + planRisk.id + '/item/' + planRisk.id,
			key: '/forrisco/plan-risk/' + planRisk.id + '/item/' + planRisk.id,
			model: planRisk,
			id: planRisk.id,
		};

		var newItem = {
			label: Messages.get("label.newItem"),
			labelCls: 'fpdi-new-node-label',
			iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
			to: '/forrisco/plan-risk/' + planRisk.id + '/item/new',
			key: "newPlanRiskItem"
		};

		PlanRiskItemStore.on('allitens', (response) => {
			response.data.map(itens => {
				var linkToItem = '/forrisco/plan-risk/' + itens.id + '/item/' + itens.id;

				treeItens.push({
					label: itens.name,
					expanded: false,
					expandable: itens.name !== "Informações gerais", //Mudar essa condição para: Se houver subitens
					to: linkToItem,
					key: linkToItem,
					model: itens,
					id: itens.id,
				});
			});

			treeItens.unshift(info);
			treeItens.push(newItem);

			this.setState({treeItens: treeItens});
			this.forceUpdate();

			PlanRiskItemStore.off('allitens');
		}, me);
	},

	componentWillUnmount() {
		PlanRiskItemStore.off(null, null, this);
		PlanRiskStore.off(null, null, this);
	},

	render() {
		return (
			<div className="fpdi-tabs">
				<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
					<Link role="tab" title="Plano" activeClassName="active" className="tabTreePanel" 
					to={"/forrisco/plan-risk/" + this.props.planRisk.attributes.id + "/"}>
						{Messages.getEditable("label.plan", "fpdi-nav-label")}
					</Link>

					<Link role="tab" title="Plano" activeClassName="active" className="tabTreePanel" 
					to={"/forrisco/plan-risk/" + this.props.planRisk.attributes.id + "/unit"}>
						{Messages.getEditable("label.unity", "fpdi-nav-label")}
					</Link>
				</ul>
				
				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">
					
					<div className={"fpdi-tabs show"}  role="tablist">
						Teste1
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

					
					<div className={"fpdi-tabs show"}  role="tablist">
						Teste2
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

			</div>
		)
	},
})
