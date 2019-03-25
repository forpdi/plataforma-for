import React from "react";
import _ from 'underscore';

import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import RiskStore from 'forpdi/jsx_forrisco/planning/store/Risk.jsx';
import Messages from "@/core/util/Messages";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import SearchResult from "forpdi/jsx_forrisco/planning/widget/search/unit/SearchResult.jsx";
import LevelSearch from "forpdi/jsx_forrisco/planning/widget/search/unit/LevelSearch.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";

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
		unit: React.PropTypes.array,
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
			export:false,
		};
	},

	componentDidMount() {
		const me = this;

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
					expandable: true,
					startExpanded: false,
					to: linkToItem,
					key: linkToItem,
					model: item,
					id: item.id,
					children: [],
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				});
			});

			this.addLinkOfNewNode(
				this.context.roles.ADMIN,
				PermissionsTypes.FORRISCO_MANAGE_UNIT_PERMISSION,
				treeItensUnit,
				Messages.get("label.newUnity"),
				'fpdi-new-node-label',
				`/forrisco/plan-risk/${this.props.planRisk.id}/unit/new`,
				'newUnitItem',
			);

			this.setState({ treeItensUnit: treeItensUnit });
			this.forceUpdate();
		}, me);

		UnitStore.on('subunitsListed', (response, node) => {
			const fieldTree = [];

			//Botão Novo SubItem
			_.forEach(response.data, subunit => {
				const toSubunit = `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/subunit/${subunit.id}/info`;
				fieldTree.push({
					label: subunit.name,
					expanded: false,
					expandable: true,
					startExpanded: false,
					to: toSubunit,
					key: toSubunit,
					model: subunit,
					id: subunit.id,
					children: [],
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot,
				});
			});

			RiskStore.dispatch({
				action: RiskStore.ACTION_FIND_BY_UNIT,
				data: {
					unitId: node.node.id,
				},
				opts: {
					payload: {
						node,
						subunitTree: fieldTree,
					}
				},
			});

			node.node.children = [];
			// me.forceUpdate();
		}, me);

		RiskStore.on('riskbyunit', (response, { payload }) => {
			const { node, subunitTree } = payload;
			const fieldTree = subunitTree;

			//Botão Novo SubItem
			response.data.map(risk => {
				fieldTree.push({
					label: risk.name,
					iconCls: 'mdi mdi-play pointer',
					to: `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/risk/${risk.id}/info`,
					key: `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/risk/${risk.id}/info`,
					id: risk.id,
				});
			});

			// Adiciona o Botão de Novo Risco
			this.addLinkOfNewNode(
				this.context.roles.ADMIN,
				PermissionsTypes.FORRISCO_MANAGE_UNIT_PERMISSION,
				fieldTree,
				'Nova Subunidade',
				'fpdi-new-node-label',
				`/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/subunit/new`,
				'newSubunit',
			);

			// Adiciona o Botão de Novo Risco
			this.addLinkOfNewNode(
				this.context.roles.MANAGER,
				PermissionsTypes.FORRISCO_MANAGE_RISK_PERMISSION,
				fieldTree,
				'Novo Risco',
				'fpdi-new-node-label',
				`/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/risk/new`,
				'newRisk',
			);

			node.node.children = [...node.node.children, ...fieldTree];
			me.forceUpdate();
		}, me);

		RiskStore.on('riskbysubunits', (response, node) => {
			const fieldTree = [];
			const filteredRisks = _.filter(response.data, risk => risk.unit.id === node.node.id);
			_.forEach(filteredRisks, risk => {
				fieldTree.push({
					label: risk.name,
					iconCls: 'mdi mdi-play pointer',
					to: `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/risk/${risk.id}/info`,
					key: `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/risk/${risk.id}/info`,
					id: risk.id,
				});
			});

			// Adiciona o Botão de Novo Risco
			this.addLinkOfNewNode(
				this.context.roles.MANAGER,
				PermissionsTypes.FORRISCO_MANAGE_RISK_PERMISSION,
				fieldTree,
				'Novo Risco',
				'fpdi-new-node-label',
				`/forrisco/plan-risk/${this.props.planRisk.id}/unit/${node.node.id}/risk/new`,
				'newRisk',
			);

			node.node.children = fieldTree
			me.forceUpdate();
		}, me);

		RiskStore.on("riskDelete", (reponse) => {
			const risk = reponse.data
			const unitId = reponse.data.unit.id

			//unidades
			for (var i = 0; i < this.state.treeItensUnit.length - 1; i++) {
				if (this.state.treeItensUnit[i].id == unitId) {
					//riscos
					for (var k = 0; k < this.state.treeItensUnit[i].children.length; k++) {
						if (this.state.treeItensUnit[i].children[k].id == risk.id) {

							if (!this.state.treeItensUnit[i].expanded) { this.state.treeItensUnit[i].startExpanded = true }

							this.state.treeItensUnit[i].children.splice(k, 1)
							this.setState({ treeItensUnit: this.state.treeItensUnit });
							return;
						}
					}

				} else {
					//subunidades
					for (var j = 0; j < this.state.treeItensUnit[i].children.length - 2; j++) {
						if (this.state.treeItensUnit[i].children[j].id == unitId) {
							//riscos
							for (var k = 0; k < this.state.treeItensUnit[i].children[j].children.length; k++) {
								if (this.state.treeItensUnit[i].children[j].children[k].id == risk.id) {

									if (!this.state.treeItensUnit[i].expanded) { this.state.treeItensUnit[i].startExpanded = true }
									if (!this.state.treeItensUnit[i].children[j].expanded) { this.state.treeItensUnit[i].children[j].startExpanded = true }

									this.state.treeItensUnit[i].children[j].children.splice(k, 1)
									this.setState({ treeItensUnit: this.state.treeItensUnit });
									return;
								}
							}
						}
					}
				}
			}
		}, me)

		RiskStore.on("riskcreated", (reponse) => {
			const risk = reponse.data
			const unitId = reponse.data.unit.id

			const risknode={
				label: risk.name,
				iconCls: 'mdi mdi-play pointer',
				to: `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${unitId}/risk/${risk.id}/info`,
				key: `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${unitId}/risk/${risk.id}/info`,
				id: risk.id,
			}

			//unidades
			for (var i = 0; i < this.state.treeItensUnit.length - 1; i++) {
				if (this.state.treeItensUnit[i].id == unitId) {
					this.state.treeItensUnit[i].children.splice( this.state.treeItensUnit[i].children.length-2, 0, risknode);
					this.setState({ treeItensUnit: this.state.treeItensUnit });
					return;
				}else{
					//subunidades
					for (var j = 0; j < this.state.treeItensUnit[i].children.length - 2; j++) {
						if (this.state.treeItensUnit[i].children[j].id == unitId) {
							this.state.treeItensUnit[i].children[j].children.splice( this.state.treeItensUnit[i].children[j].children.length-1, 0, risknode);
							this.setState({ treeItensUnit: this.state.treeItensUnit });
							return;
						}
					}
				}
			}
		}, me)

		UnitStore.on('unitDeleted', response => {
			if (response.data) {
				const unitToDelete = response.data;
				if (!unitToDelete.parent) { // unit
					const unit = unitToDelete;
					const treeItensUnit = _.filter(this.state.treeItensUnit, unitItem => unitItem.id !== unit.id);
					this.setState({ treeItensUnit });
				} else { // subunit
					const subunit = unitToDelete;
					const treeItensUnit = _.map(this.state.treeItensUnit, unitItem => {
						if (unitItem.id && unitItem.id === subunit.parent.id) {
							let { children } = unitItem;
							children = _.filter(children, child => child.id !== subunit.id);
							unitItem.children = children;
						}
						return unitItem;
					});
					this.setState({ treeItensUnit });
				}
			}
		}, me);

		UnitStore.on("unitcreated", (response) => {
			if (response.data) {
				const unit = response.data;
				const { treeItensUnit } = this.state;
				const linkToUnit = `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${unit.id}/info`;
				treeItensUnit.splice(treeItensUnit.length - 1, 0, {
					label: unit.name,
					expanded: false,
					expandable: true,
					startExpanded: true,
					to: linkToUnit,
					key: linkToUnit,
					model: unit,
					id: unit.id,
					children: [],
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				});
				this.setState({
					treeItensUnit,
				})
			}
		}, me);

		UnitStore.on("unitUpdated", (response) => {
			if (response.data) {
				const unitToUpdate = response.data;
				if (!unitToUpdate.parent) { // unit
					const unit = unitToUpdate;
					const treeItensUnit = _.map(this.state.treeItensUnit, unitItem => {
						if (unitItem.id === unit.id) {
							return {
								...unitItem,
								model: unit,
								label: unit.name,
							}
						}
						return unitItem;
					});
					this.setState({ treeItensUnit });
				} else { // subunit
					const subunit = unitToUpdate;
					const treeItensUnit = _.map(this.state.treeItensUnit, unitItem => {
						if (unitItem.id && unitItem.id === subunit.parent.id) {
							let { children } = unitItem;
							children = _.map(children, child => {
								if (child.id === subunit.id) {
									return {
										...child,
										label: subunit.name,
									}
								}
								return child;
							});
							unitItem.children = children;
						}
						return unitItem;
					});
					this.setState({ treeItensUnit });
				}
			}
		}, me);

		UnitStore.on("subunitCreated", (response) => {
			if (response.data) {
				const subunit = response.data;
				const treeItensUnit = _.map(this.state.treeItensUnit, unitItem => {
					if (unitItem.id && unitItem.id === subunit.parent.id) {
						const toSubunit = `/forrisco/plan-risk/${this.props.planRisk.id}/unit/${unitItem.id}/subunit/${subunit.id}/info`;
						const { children } = unitItem;
						children.splice(children.length - 2, 0, {
							expanded: false,
							expandable: true,
							startExpanded: true,
							label: subunit.name,
							to: toSubunit,
							key: toSubunit,
							id: subunit.id,
							onExpand: this.expandRoot,
							onShrink: this.shrinkRoot
						});
					}
					return unitItem;
				});
				this.setState({ treeItensUnit });
			}
		}, me);

		UnitStore.on("allSubunitsListed",(model) => {
			if(this.state.export){
				this.setState({
					treeItensSubunit:model.data,
					export:false,
				})
				this.retrieveFilledSections();
			}
		},this);

		this.refresh(this.props.planRisk.id);
	},

	componentWillUnmount() {
		RiskStore.off(null, null, this);
		UnitStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		if (newProps.planRisk.id !== this.props.planRisk.id) {
			this.refresh(newProps.planRisk.id)
		}
	},

	addLinkOfNewNode(role, permissionType, treeItensUnit, label, iconCls, to, key) {
		if (role || _.contains(this.context.permissions, permissionType)) {
			treeItensUnit.push({
				label,
				labelCls: 'fpdi-new-node-label',
				iconCls: 'mdi mdi-plus',
				to,
				key,
			});
		}
	},

	refresh(planRiskId) {
		UnitStore.dispatch({
			action: UnitStore.ACTION_FIND_BY_PLAN,
			data: planRiskId,
			opts: {
				refreshUnitTree: true,
			},
		});
		this.forceUpdate();
	},

	expandRoot(nodeProps, nodeLevel) {
		nodeProps.startExpanded = false

		switch (nodeLevel) {
			case 0:
				UnitStore.dispatch({
					action: UnitStore.ACTION_LIST_SUBUNIT,
					data: {
						unitId: nodeProps.id
					},
					opts: {
						node: nodeProps
					}
				});
				break;
			case 1:
				RiskStore.dispatch({
					action: RiskStore.ACTION_FIND_BY_SUBUNITS,
					data: {
						unit: nodeProps,
					},
					opts: {
						node: nodeProps,
					},
				});
				break;
			default: return;
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
		for (i = 0; i < this.state.treeItensSubunit.length; i++) {
			if (document.getElementById("checkbox-subunit-" + i).disabled == false && !document.getElementById("checkbox-subunit-" + i).checked) {
				selectedAll = false;
			}
		}
		document.getElementById("selectall-sub").checked = selectedAll;
	},

	selectAllSubunits() {
		var i;
		for (i = 0; i < this.state.treeItensSubunit.length; i++) {
			if (document.getElementById("checkbox-subunit-" + i).disabled == false) {
				document.getElementById("checkbox-subunit-" + i).checked = document.getElementById("selectall-sub").checked;
			}
		}
	},


	renderRecords() {
		return (
			<div>
				<div className="row">Unidades
					<div key="rootSection-selectall">
						<div className="checkbox marginLeft5 col-md-10">
							<label name="labelSection-selectall" id="labelSection-selectall">
								<input
									type="checkbox"
									value="selectall"
									id="selectall"
									onChange={this.selectAllUnits}
								/>
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

								</div>
							)
						}
					})}
				</div>
				<div className="row">Subunidades
					<div key="rootSection-selectall">
						<div className="checkbox marginLeft5 col-md-10">
							<label name="labelSection-selectall" id="labelSection-selectall">
								<input type="checkbox" value="selectall" id="selectall-sub"
									   onChange={this.selectAllSubunits}/>
								Selecionar todos
							</label>
						</div>
					</div>

					{this.state.treeItensSubunit.map((rootSection, idx) => {
					return (
					<div key={"rootSection-filled"+idx}>
						<div className="checkbox marginLeft5 col-md-10" >
							<label name={"labelSection-filled"+idx} id={"labelSection-filled"+idx}>
								<input type="checkbox" value={rootSection.id} id={"checkbox-subunit-"+idx} onClick={this.verifySelectAllsubitens}></input>
								{rootSection.name}
							</label>
						</div>

					</div>);
				})}
					<br/><br/>
				</div>
			</div>
		);
	},

	retrieveFilledSections() {
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

	exportPlanRiskReport(evt) {
		evt.preventDefault();

		if(this.props.planRisk){
			PlanRiskItemStore.dispatch({
				action: PlanRiskItemStore.ACTION_GET_SUB_ITENS_BY_PLANRISK,
				data: {
					id: this.props.planRisk.id
				},
				opts: {
					node:{id:null}
				}
			});
			this.setState({export:true})
		}
	},

	exportUnitReport(evt) {
		evt.preventDefault();

		if(this.props.planRisk){
			UnitStore.dispatch({
				action: UnitStore.ACTION_LIST_SUBUNIT_BY_PLAN,
				data:  this.props.planRisk.id,
				opts: {
					node:{id:null}
				}
			});
			this.setState({export:true})
		}
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
					<i className="mdiClose mdi mdi-close pointer" onClick={this.resultSearch} title={Messages.get("label.clean")} />
					<input
						type="text"
						className="form-control-busca placeholder-italic"
						placeholder="Pesquisar"
						ref="term"
						onKeyDown={this.onKeyDown}
					/>
					<i
						className="mdiBsc mdi mdi-chevron-down pointer"
						onClick={this.searchFilter}
						title={Messages.get("label.advancedSearch")}
					/>
					<i
						id="searchIcon"
						className="mdiIconPesquisa mdiBsc  mdi mdi-magnify pointer"
						onClick={this.treeSearch}
						title={Messages.get("label.search")}
					/>
				</div>

				{
					this.state.hiddenResultSearch === true
					?
					<SearchResult
						planRiskId={this.props.planRisk.id}
						terms={this.state.termsSearch}
						itensSelect={this.state.itensSelect}
						subitensSelect={this.state.subitensSelect}
						ordResult={this.state.ordResultSearch}
					/>
					:
					<div>
						<TreeView tree={this.state.treeItensUnit} />
						<hr className="divider"/>

						{
							(this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_DOCUMENT_PERMISSION))
							?
							<a className="btn btn-sm btn-primary center" onClick={this.exportUnitReport}>
								{Messages.getEditable("label.exportReport", "fpdi-nav-label")}
							</a>
							:
							""
						}
					</div>
				}
				{
					this.state.hiddenSearch === true
					?
					<div className="container Pesquisa-Avancada">
						<LevelSearch
							searchText={this.refs.term.value}
							planRisk={this.props.planRisk.id}
							subplans={this.state.treeItensUnit}
							hiddenSearch={this.searchFilter}
							displayResult={this.displayResult}
						/>
					</div>
					:
					""
				}
			</div>
		);
	},
})
