import React from "react";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import Unit from "forpdi/jsx_forrisco/planning/widget/unit/Unit.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import LevelSearch from "forpdi/jsx_forrisco/planning/widget/search/planrisk/LevelSearch.jsx";
import SearchResult from "forpdi/jsx_forrisco/planning/widget/search/planrisk/SearchResult.jsx";
import {Link} from "react-router";
import LoadingGauge from "forpdi/jsx_forrisco/planning/view/policy/PolicyDetails";
import Messages from "@/core/util/Messages";
import Modal from "@/core/widget/Modal";


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
		className: React.PropTypes.object
	},

	getInitialState() {
		return {
			cleanTree: [],
			treeItens: [],
			treeItensUnit: [],
			treeItensSubunit: [],
			treeItemFields: [],
			newProps: null,
			actualType: this.props.treeType,
			prevProps: {},
			info: {},
			newItem: {},
			myroute: window.location.hash,
			showMenu: true,
			planriskactive: true,
			hiddenSearch: false,
			termsSearch: '',
			itensSelect: [],
			subitensSelect: [],
			ordResultSearch: null,
			planRiskId: null
		};
	},

	componentDidMount() {
		this.setTreeItens(this.props.planRisk);
		this.refresh();

		PlanRiskStore.on('searchTerms', response => {
			console.log(response.terms);
			if (response.data) {
				this.setState({
					termsSearch: response.terms,
					itensSelect: response.itensSelect,
					subitensSelect: response.subitensSelect,
				})
			}
		}, this);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.planRisk.id !== this.props.planRisk.id) {
			this.setTreeItens(newProps.planRisk);
		}

		this.refresh()
	},
	refresh() {
		/*if(this.props.planRisk.id !=null && this.state.planRiskId != this.props.planRisk.id){
			PlanRiskItemStore.dispatch({
				action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
				data: this.props.planRisk.id
			});
		}*/
	},

	//PlanRisk
	setTreeItens(planRisk, treeItens = []) {
		var me = this;

		/* Redireciona para as Informações gerais ao carregar a Tree*/
		if (!this.props.location.pathname.includes("unit")) {
			this.context.router.push("/forrisco/plan-risk/" + planRisk.id + "/item/" + planRisk.id + "/info");
		}
		/* ____________________  */

		var info = {
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
			response.data.map(itens => {
				var linkToItem = '/forrisco/plan-risk/' + planRisk.id + '/item/' + itens.id;

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
			var toNewSubItem = '/forrisco/plan-risk/' + planRisk.id + '/item/' + node.node.id + "/subitem/new";

			//Botão Novo SubItem
			var newItemSubItem = {
				label: "Novo Subitem",
				labelCls: 'fpdi-new-node-label',
				iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				to: toNewSubItem,
				key: "newPlanRiskSubItem"
			};

			response.data.map(subField => {
				var toSubItem = '/forrisco/plan-risk/' + planRisk.id + '/item/' + node.node.id + "/subitem/" + subField.id;

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
		});
		PlanRiskItemStore.dispatch({
			action: PlanRiskItemStore.ACTION_GET_ALL_ITENS,
			data: this.props.planRisk.id
		});
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
		PlanRiskItemStore.off(null, null, this);
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

	onKeyDown(event) {
		var key = event.which;
		if (key === 13) {
			event.preventDefault();
			this.treeSearch();
		}
	},

	treeSearch() {
		this.displayResult();
		PlanRiskStore.dispatch({
			action: PlanRiskStore.ACTION_SEARCH_TERMS,
			data: {
				planRiskId: this.props.planRisk.id,
				terms: this.refs.term.value,
				page: 1,
				limit: 10,
			},
			opts: {
				wait: true
			}
		});
	},

	displayResult() {
		this.setState({
			hiddenResultSearch: true
		});
	},

	resultSearch() {
		this.setState({
			hiddenResultSearch: false
		});
		this.refs.term.value = "";
	},

	searchFilter() {
		this.setState({
			hiddenSearch: !this.state.hiddenSearch
		});
	},

	verifySelectAllUnits() {
		var i;
		var selectedAll = true;
		for (i = 0; i < this.state.treeItensUnit.length; i++) {
			if (document.getElementById("checkbox-unit-" + i).disabled == false && !document.getElementById("checkbox-unit-" + i).checked) {
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},

	selectAllUnits() {
		var i;
		for (i = 0; i < this.state.treeItensUnit.length; i++) {
			if (document.getElementById("checkbox-unit-" + i).disabled == false) {
				document.getElementById("checkbox-unit-" + i).checked = document.getElementById("selectall").checked;
			}
		}
	},
	verifySelectAllsubitens() {
		var i;
		var selectedAll = true;
		for (i = 0; i < this.state.treeItensSubunit.length; i++) {
			if (document.getElementById("checkbox-subunit-" + i).disabled == false && !document.getElementById("checkbox-subunit-" + i).checked) {
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},
	selectAllSubunits() {
		var i;
		for (i = 0; i < this.state.treeItensSubunit.length; i++) {
			if (document.getElementById("checkbox-subunit-" + i).disabled == false) {
				document.getElementById("checkbox-subunit-" + i).checked = document.getElementById("selectall").checked;
			}
		}
	},


	renderRecords() {
		return (<div>
			<div className="row">Unidades
				<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10">
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall"
								   onChange={this.selectAllUnits}></input>
							Selecionar todos
						</label>
					</div>
				</div>
				{this.state.treeItensUnit.map((rootSection, idx) => {
					return (
						<div key={"rootSection-filled" + idx}>
							<div className="checkbox marginLeft5 col-md-10">
								<label name={"labelSection-filled" + idx} id={"labelSection-filled" + idx}>
									<input type="checkbox" value={rootSection.id} id={"checkbox-unit-" + idx}
										   onClick={this.verifySelectAllUnits}></input>
									{rootSection.label}
								</label>
							</div>

						</div>);
				})}
			</div>
			<div className="row">Subunidades

				<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10">
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall"
								   onChange={this.selectAllSubunits}></input>
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
							{rootSection.label}
						</label>
					</div>

				</div>);
			})*/}
				<br/><br/>
			</div>
		</div>);
	},

	retrieveFilledSections() {
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
			() => {
				this.visualization(false)
			},
			({
				label: "Pré-visualizar",
				onClick: this.preClick,
				title: Messages.get("label.exportConfirmation")
			})
		);
		document.getElementById("paramError").innerHTML = "";
		document.getElementById("documentAuthor").className = "";
		document.getElementById("documentTitle").className = "";
	},

	visualization(pre) {

		var i = 0;
		var sections = "";
		var subsections = "";
		var author = document.getElementById("documentAuthor").value;
		var title = document.getElementById("documentTitle").value;
		for (i = 0; i < this.state.treeItensUnit.length; i++) {
			if (document.getElementById("checkbox-unit-" + i).checked == true) {
				sections = sections.concat(this.state.treeItensUnit[i].id + "%2C");
			}
		}
		for (i = 0; i < this.state.treeItensSubunit.length; i++) {
			if (document.getElementById("checkbox-subunit-" + i).checked == true) {
				subsections = subsections.concat(this.state.treeItensSubunit[i].id + "%2C");
			}
		}

		var item = sections.substring(0, sections.length - 3);
		var subitem = subsections.substring(0, subsections.length - 3);
		var elemError = document.getElementById("paramError");
		if (sections == '' || author.trim() == '' || title.trim() == '') {
			elemError.innerHTML = Messages.get("label.exportError");
			if (author.trim() == '') {
				document.getElementById("documentAuthor").className = "borderError";
			} else {
				document.getElementById("documentAuthor").className = "";
			}
			if (title.trim() == '') {
				document.getElementById("documentTitle").className = "borderError";
			} else {
				document.getElementById("documentTitle").className = "";
			}
		} else {
			document.getElementById("documentAuthor").className = "";
			document.getElementById("documentTitle").className = "";


			var url = UnitStore.url + "/exportUnitReport" + "?title=" + title + "&author=" + author + "&pre=" + pre + "&units=" + item + "&subunits=" + subitem;
			url = url.replace(" ", "+");

			if (pre) {
				window.open(url, title);
			} else {
				//this.context.router.push(url);
				window.open(url, title);
				Modal.hide();
			}
		}
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
		this.setState({exportPlanRisk: true})
	},

	render() {
		return (
			<div className={"fpdi-tabs"} role="tablist">
				<div className="marginBottom10 inner-addon right-addon right-addonPesquisa plan-search-border">
					<i className="mdiClose mdi mdi-close pointer" onClick={this.resultSearch} title={Messages.get("label.clean")}/>
					<input type="text" className="form-control-busca" ref="term" onKeyDown={this.onKeyDown}/>
					<i className="mdiBsc mdi mdi-chevron-down pointer" onClick={this.searchFilter} title={Messages.get("label.advancedSearch")}/>
					<i id="searchIcon" className="mdiIconPesquisa mdiBsc  mdi mdi-magnify pointer" onClick={this.treeSearch} title={Messages.get("label.search")}/>
				</div>

				{
					this.state.hiddenResultSearch === true ?
						<SearchResult
							planRiskId={this.props.planRisk.id}
							terms={this.state.termsSearch}
							itensSelect={this.state.itensSelect}
							subitensSelect={this.state.subitensSelect}
							ordResult={this.state.ordResultSearch}
						/>
						:
						<div>
							<TreeView tree={this.state.treeItens}/>
							<hr className="divider"/>

							{
								(this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
									<a className="btn btn-sm btn-primary center" onClick={this.exportPlanRiskReport}>
										{Messages.getEditable("label.exportReport", "fpdi-nav-label")}
									</a>

									: ""
							}
						</div>

				}

				{
					this.state.hiddenSearch === true ?
						<div className="container Pesquisa-Avancada">
							<LevelSearch
								searchText={this.refs.term.value}
								subplans={this.state.treeItens}
								planRisk={this.props.planRisk.id}
								hiddenSearch={this.searchFilter}
								displayResult={this.displayResult}
							/>
						</div> : ""
				}

			</div>
		)
	},
})
