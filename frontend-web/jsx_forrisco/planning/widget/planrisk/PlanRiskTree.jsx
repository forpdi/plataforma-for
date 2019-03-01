import React from "react";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import Unit from "forpdi/jsx_forrisco/core/widget/unit/Unit.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import UnitItemStore from "forpdi/jsx_forrisco/planning/store/UnitItem.jsx"
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import {Link} from "react-router";
import LoadingGauge from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails";
import Messages from "@/core/util/Messages";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		tabPanel: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},

	propTypes: {
		planRisk: React.PropTypes.object.isRequired,
		unit: React.PropTypes.object,
		className : React.PropTypes.object
	},

	getInitialState() {
		return {
			cleanTree: [],
			treeItens: [],
			treeItensUnit: [],
			treeItemFields: [],
			newProps: null,
			actualType: this.props.treeType,
			prevProps: {},
			info: {},
			newItem: {},
			myroute: window.location.hash,
			showMenu:true,
			planriskactive:true
		};
	},

	componentDidMount() {
		this.setTreeItens(this.props.planRisk),
		this.setTreeItensUnit(this.props.planRisk)
	},

	componentWillReceiveProps(newProps) {
		if (newProps.planRisk.id !== this.props.planRisk.id) {
			this.setTreeItens(newProps.planRisk);
			this.setTreeItensUnit(newProps.planRisk);
		}
	},

	componentWillMount() {
		//this.context.router.push("/forrisco/plan-risk/" + this.props.planRisk.id + "/item/" + this.props.planRisk.id);
	},

	setTreeItensUnit(unit, treeItensUnit = []) {
		var me = this;
		var  info = {
			label: "Informações Gerais Unidade",
			expanded: false,
			to: '/forrisco/plan-risk/' + unit.id + '/unit/' + unit.id,
			key: '/forrisco/plan-risk/' + unit.id + '/unit/' + unit.id,
			model: unit,
			id: unit.id,
		};

		//Botão Novo Item Geral
		var newItem = {
			label: Messages.get("label.newItem"),
			labelCls: 'fpdi-new-node-label',
			iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
			to: '/forrisco/plan-risk/' + unit.id + '/unit/new',
			key: "newUnitItem"
		};

		/*Item de um Plano*/

		UnitItemStore.on('allitensunit', (response) => {
			response.data.map(itens => {
				//var linkToItem = '/forrisco/plan-risk/' + itens.id + '/item/' + itens.id;
				var linkToItem = '/forrisco/plan-risk/' + itens.id + '/unit/' + itens.id+"/info";
				treeItensUnit.push({
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

			treeItensUnit.unshift(info);
			treeItensUnit.push(newItem);

			this.setState({treeItensUnit: treeItensUnit});
			this.forceUpdate();

			UnitItemStore.off('allitensunit');
		}, me);

		/*Campos de um Item*/
		UnitItemStore.on('allFieldsUnit', (response, node) => {
			var fieldTree = [];

			//Botão Novo SubItem
			var newItemSubItem = {
				label: Messages.get("label.newItem"),
				labelCls: 'fpdi-new-node-label',
				iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				to: '#',
				key: "newUnitSubItem"
			};

			 response.data.map(field => {
				 fieldTree.push({
					 label: field.name,
					 to: '',
					 key: '',
					 id: field.id,
				 })
			});

			fieldTree.push(newItemSubItem);  //Adiciona o Botão de Novo SubItem

			node.node.children = fieldTree;
			me.forceUpdate();
		})
	},

    //PlanRisk
	setTreeItens(planRisk, treeItens = []) {
		var me = this;

		/* Redireciona para as Informações gerais ao carregar a Tree*/
		if(!this.props.location.pathname.includes("unit")){
			this.context.router.push("/forrisco/plan-risk/" + planRisk.id + "/item/" + planRisk.id + "/info");
		}
		/* ____________________  */

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

	toggleMenu() {
		this.setState({
		  showMenu: false
		})
	  },

	toggleMenu1() {
		this.setState({
		  showMenu: true
		})
	  },

	  renderRecords() {
		return (<div>
		<div className="row">Unidades
			<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10" >
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall" onChange={this.selectAllitens}></input>
							Selecionar todos
						</label>
					</div>
			</div>

			{this.state.treeItensUnit.map((rootSection, idx) => {
				return (
				<div key={"rootSection-filled"+idx}>
					<div className="checkbox marginLeft5 col-md-10" >
						<label name={"labelSection-filled"+idx} id={"labelSection-filled"+idx}>
							<input type="checkbox" value={rootSection.id} id={"checkbox-item-"+idx} onClick={this.verifySelectAllitens}></input>
							{rootSection.name}
						</label>
					</div>

				</div>);
			})}
			</div>
			<div className="row">Subunidades

				<div key="rootSection-selectall">
						<div className="checkbox marginLeft5 col-md-10" >
							<label name="labelSection-selectall" id="labelSection-selectall">
								<input type="checkbox" value="selectall" id="selectall" onChange={this.selectAllsubitens}></input>
								Selecionar todos
							</label>
						</div>
				</div>

			{/*this.state.subunits.map((rootSection, idx) => {
				return (
				<div key={"rootSection-filled"+idx}>
					<div className="checkbox marginLeft5 col-md-10" >
						<label name={"labelSection-filled"+idx} id={"labelSection-filled"+idx}>
							<input type="checkbox" value={rootSection.id} id={"checkbox-subitem-"+idx} onClick={this.verifySelectAllsubitens}></input>
							{rootSection.name}
						</label>
					</div>

				</div>);
			})*/}
			<br/><br/>
			</div>
		</div>);
	},

	  retrieveFilledSections(){
		//var me = this;
		//me.setState({
			//rootSections: this.state.itens,
			//rootSubsections: this.state.subitens,
			//loadingexport:true,
		//	});

		//	$('#container') heigth 150px
		Modal.exportDocument(
			Messages.get("label.exportConfirmation"),
			this.renderRecords(),
			() => {this.visualization(false)},
			({label:"Pré-visualizar",
			onClick:this.preClick,
			title:Messages.get("label.exportConfirmation")})
		);
		document.getElementById("paramError").innerHTML = "";
		document.getElementById("documentAuthor").className = "";
		document.getElementById("documentTitle").className = "";
	},

	  exportUnitReport(evt) {
		evt.preventDefault();
			//this.setState({exportUnit:true})

		//	if(this.state.export){
				this.retrieveFilledSections();
				this.setState({
					//subitens:model.data,
					//export:false,
				})
		//	}
	},

	exportPlanRiskReport(evt) {
		evt.preventDefault();
			this.setState({exportPlanRisk:true})
	},

	render() {
		this.state.myroute= window.location.hash.substring(1)
		var planriskactive

		if(!this.props.location.pathname.includes("unit")){
			planriskactive=true
		}

		return (
			<div className="fpdi-tabs">
				<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
					<Link role="tab" title="Plano"  className={"tabTreePanel "+(planriskactive? "active" :"")}
					to={"forrisco/plan-risk/" + this.props.planRisk.id + "/"}>
						{Messages.getEditable("label.plan", "fpdi-nav-label")}
					</Link>

					<Link role="tab" title="Unidade"  className={"tabTreePanel "+(!planriskactive? "active" :"")}
					to={"forrisco/plan-risk/" + this.props.planRisk.id + "/unit"}>
						{Messages.getEditable("label.unitys", "fpdi-nav-label")}
					</Link>
				</ul>
				
				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">

				{planriskactive ?
					<div className={"fpdi-tabs"}  role="tablist">
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

						<hr className="divider"></hr>
						{(this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
							<a className="btn btn-sm btn-primary center" onClick={this.exportPlanRiskReport}>
								<span/>{Messages.getEditable("label.exportReport", "fpdi-nav-label")}
							</a>
						: ""}

					</div>
			:
					<div className={"fpdi-tabs"}  role="tablist">
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
						<Unit treeUnit={this.state.treeItensUnit}/>

						<hr className="divider"></hr>
						{(this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
							<a className="btn btn-sm btn-primary center" onClick={this.exportUnitReport}>
								<span/>{Messages.getEditable("label.exportReport", "fpdi-nav-label")}
							</a>
						: ""}
					</div>
				}
				</div>
			</div>
		)
	},
})
