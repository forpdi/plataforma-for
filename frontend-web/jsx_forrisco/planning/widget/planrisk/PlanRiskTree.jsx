import React from "react";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import {Link} from "react-router";
import LoadingGauge from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails";
import Messages from "@/core/util/Messages";


export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		tabPanel: React.PropTypes.object,
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
			treeItemFields: [],
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

	componentWillMount() {
		//this.context.router.push("/forrisco/plan-risk/" + this.props.planRisk.id + "/item/" + this.props.planRisk.id);
	},

	setTreeItens(planRisk, treeItens = []) {
		var me = this;
		var  info = {
			label: "Informações Gerais",
			expanded: false,
			to: '/forrisco/plan-risk/' + planRisk.id + '/item/' + planRisk.id + '/info',
			key: '/forrisco/plan-risk/' + planRisk.id + '/item/' + planRisk.id + '/info',
			model: planRisk,
			id: planRisk.id,
		};

		//Botão Novo Item Geral
		var newItem = {
			label: Messages.get("label.newItem"),
			labelCls: 'fpdi-new-node-label',
			iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
			to: '/forrisco/plan-risk/' + planRisk.id + '/item/new',
			key: "newPlanRiskItem"
		};


		/*Item de um Plano*/
		PlanRiskItemStore.on('allItens', (response) => {
			response.data.map( itens => {
				var linkToItem = '/forrisco/plan-risk/' + planRisk.id  + '/item/' + itens.id;

				treeItens.push({
					label: itens.name,
					expanded: false,
					expandable: true, //Mudar essa condição para: Se houver subitens
					to: linkToItem,
					key: linkToItem,
					model: itens,
					id: itens.id,
					children: [],
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				});
			});

			treeItens.unshift(info);
			treeItens.push(newItem);

			this.setState({treeItens: treeItens});
			this.forceUpdate();

			PlanRiskItemStore.off('allItens');
		}, me);

		/*Campos de um Item*/
		PlanRiskItemStore.on('allSubItens', (response, node) => {
			var fieldTree = [];
			var toNewSubItem = '/forrisco/plan-risk/' + planRisk.id  + '/item/' + node.node.id + "/subitem/new";

			//Botão Novo SubItem
			var newItemSubItem = {
				label: "Novo Subitem",
				labelCls: 'fpdi-new-node-label',
				iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				to: toNewSubItem,
				key: "newPlanRiskSubItem"
			};

			 response.data.map(subField => {
				 var toSubItem = '/forrisco/plan-risk/' + planRisk.id  + '/item/' + node.node.id + "/subitem/" + subField.id;

				 fieldTree.push({
					 label: subField.name,
					 to: toSubItem,
					 key: toSubItem,
					 id: subField.id,
				 })
			});

			fieldTree.push(newItemSubItem);  //Adiciona o Botão de Novo SubItem

			node.node.children = fieldTree;
			me.forceUpdate();

			//PlanRiskItemStore.off('allFields');
		})
	},

	expandRoot(nodeProps, nodeLevel) {
		if (nodeLevel === 0) {
			PlanRiskItemStore.dispatch({
				action: PlanRiskItemStore.ACTION_GET_SUB_ITENS,
				data: {
					id: nodeProps.id
				},
				opts: {
					node: nodeProps
				}
			})
		}
		nodeProps.expanded = !nodeProps.expanded;
		this.forceUpdate();
	},

	shrinkRoot(nodeProps) {
		nodeProps.expanded = !nodeProps.expanded;
		this.forceUpdate();
	},

	componentWillUnmount() {
		PlanRiskItemStore.off('allItens');
	},

	render() {
		return (
			<div className="fpdi-tabs">
				<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
					<Link role="tab" title="Plano" activeClassName="active" className="tabTreePanel" to={'/forrisco/plan-risk/' + this.props.planRisk.id}>
						{Messages.getEditable("label.plan", "fpdi-nav-label")}
					</Link>

					<Link role="tab" title="Plano" activeClassName="active" className="tabTreePanel" to={'/forrisco/plan-risk/' + this.props.planRisk.id+'/unit'}>
						<span className="fpdi-nav-label">Unidade</span>
					</Link>
				</ul>


				{/* Barra de Pesquisa*/}
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
