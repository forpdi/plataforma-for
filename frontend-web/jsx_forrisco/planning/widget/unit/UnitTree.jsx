import React from "react";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import PlanRiskItemStore from "forpdi/jsx_forrisco/planning/store/PlanRiskItem.jsx"
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import Messages from "@/core/util/Messages";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import SearchResult from "forpdi/jsx_forrisco/planning/widget/search/unit/SearchResult.jsx";
import LevelSearch from "forpdi/jsx_forrisco/planning/widget/search/planrisk/LevelSearch.jsx";

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
			termsSearch: '',
			itensSelect: [],
			subitensSelect: [],
		};
	},

	componentDidMount() {
		const me = this;
		var treeItensUnit = [];

		//Botão Novo Item Geral
		var newItem = {
			label: Messages.get("label.newItem"),
			labelCls: 'fpdi-new-node-label',
			iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
			to: '/forrisco/plan-risk/' + this.props.planRisk.id + '/unit/new',
			key: "newUnitItem"
		};

		/*Unidades*/
		UnitStore.on('unitbyplan', (response, opt) => {
			if (!opt || !opt.refreshUnitTree) {
				return;
			}

			const treeItensUnit = [];
			response.data.map(item => {
				const linkToItem = `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${item.id}/info`;
				treeItensUnit.push({
					label: item.name,
					expanded: false,
					expandable: true, //Mudar essa condição para: Se houver subitem
					to: linkToItem,
					key: linkToItem,
					model: item,
					id: item.id,
					children: [],
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				});
			});

			treeItensUnit.push(newItem);

			this.setState({treeItensUnit: treeItensUnit});
			this.forceUpdate();
		}, me);

		UnitStore.on('subunitsListed', (response, node) => {
			const fieldTree = [];
			const toNewSubunit = `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/subunit/new`;
			const newSubunit = {
				label: "Nova Subunidade",
				labelCls: 'fpdi-new-node-label',
				iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				to: toNewSubunit,
				key: "newPlanRiskSubItem"
			};

			//Botão Novo SubItem
			 response.data.map(subField => {
				const toSubunit = `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/subunit/${subField.id}`;
				fieldTree.push({
					label: subField.name,
					to: toSubunit,
					key: toSubunit,
					id: subField.id,
				});
			});

			fieldTree.push(newSubunit);  //Adiciona o Botão de Novo SubItem

			node.node.children = fieldTree;
			me.forceUpdate();
		});
		this.refresh();
	},

	componentWillReceiveProps(newProps) {
		if (newProps.planRisk.id !== this.props.planRisk.id) {
			// this.refresh(newProps.planRisk.id);
		}
	},

	componentWillUnmount() {
		UnitStore.off('unitbyplan');
		UnitStore.off('subunitsListed');
	},

	refresh(){
		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data: this.props.planRisk.id,
			opts: {
				refreshUnitTree: true,
			},
		});
	},


	expandRoot(nodeProps, nodeLevel) {
		if (nodeLevel === 0) {
			UnitStore.dispatch({
				action: UnitStore.ACTION_LIST_SUBUNIT,
				data: {
					unitId: nodeProps.id
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


	verifySelectAllUnits() {
		var i;
		var selectedAll = true;
		for (i = 0; i < this.state.treeItensUnit.length - 1; i++) {
			if (document.getElementById("checkbox-unit-" + i).disabled == false && !document.getElementById("checkbox-unit-" + i).checked) {
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},

	selectAllUnits() {
		var i;
		for (i = 0; i < this.state.treeItensUnit.length - 1; i++) {
			if (document.getElementById("checkbox-unit-" + i).disabled == false) {
				document.getElementById("checkbox-unit-" + i).checked = document.getElementById("selectall").checked;
			}
		}
	},

	verifySelectAllsubitens() {
		var i;
		var selectedAll = true;
		for (i = 0; i < this.state.treeItensSubunit.length - 1; i++) {
			if (document.getElementById("checkbox-subunit-" + i).disabled == false && !document.getElementById("checkbox-subunit-" + i).checked) {
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},

	selectAllSubunits() {
		var i;
		for (i = 0; i < this.state.treeItensSubunit.length - 1; i++) {
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
								   onChange={this.selectAllUnits}/>
							Selecionar todos
						</label>
					</div>
				</div>
				{this.state.treeItensUnit.map((rootSection, idx) => {
					if (this.state.treeItensUnit.length - 1 != idx) {
						return (
							<div key={"rootSection-filled" + idx}>
								<div className="checkbox marginLeft5 col-md-10">
									<label name={"labelSection-filled" + idx} id={"labelSection-filled" + idx}>
										<input type="checkbox" value={rootSection.id} id={"checkbox-unit-" + idx}
											   onClick={this.verifySelectAllUnits}/>
										{rootSection.label}
									</label>
								</div>

							</div>)
					}
				})}
			</div>
			<div className="row">Subunidades
				<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10">
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall"
								   onChange={this.selectAllSubunits}/>
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

	preClick() {
		this.visualization(true);
	},

	visualization(pre) {

		var i = 0;
		var sections = "";
		var subsections = "";
		var author = document.getElementById("documentAuthor").value;
		var title = document.getElementById("documentTitle").value;
		for (i = 0; i < this.state.treeItensUnit.length - 1; i++) {
			if (document.getElementById("checkbox-unit-" + i).checked == true) {
				sections = sections.concat(this.state.treeItensUnit[i].id + "%2C");
			}
		}
		for (i = 0; i < this.state.treeItensSubunit.length - 1; i++) {
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

	onKeyDown(event) {
		var key = event.which;
		if (key === 13) {
			event.preventDefault();
			this.treeSearch();
		}
	},

	treeSearch() {
		this.displayResult();
		UnitStore.dispatch({
			action: UnitStore.ACTION_FINDALL_TERMS,
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
							<TreeView tree={this.state.treeItensUnit}/>
							<hr className="divider"/>

							{
								(this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
									<a className="btn btn-sm btn-primary center" onClick={this.exportUnitReport}>
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
			</div>)
	},
})
