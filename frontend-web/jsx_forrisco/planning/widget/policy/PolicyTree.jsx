import _ from 'underscore';
import React from "react";
import {Link} from "react-router";
import FavoriteTree from "forpdi/jsx/planning/widget/plan/FavoriteTree.jsx";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import LevelSearch from "forpdi/jsx_forrisco/planning/widget/search/LevelSearch.jsx";
import SearchResult from "forpdi/jsx_forrisco/planning/widget/search/SearchResult.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
//import Toastr from 'toastr';

var actualNode;
export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},
	propTypes: {
		policy: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			subplans: [],
			tree: [],
			policyTree: [],
			children: [],
			actualType: this.props.treeType,
			rootSections: [],
			rootSubsections: [],
			hiddenSearch: false,
			hiddenResultSearch: false,
			resultSearch: [],
			total:0,
			dataInitSearch: null,
			dataEndSearch: null,

			ordResultSearch: null,
			parentIdSearch: null,
			termsSearch: '',
			itens:[],
			subitens:[],
			itensSelect: [],
			subitensSelect: [],
			unnumberedSections: 0
		};
	},
	componentDidMount() {
		var me = this;


		ItemStore.on("find", (store, raw, opts) => {
			var tree = raw.map((policy, index) => {
				var to = '/forrisco/policy/' + this.props.policy.get("id") + '/item/' + policy.id;
				return {
					label: policy.name,
					expanded: false,
					expandable: policy.name == "Informações gerais" ? false : true,
					to: to,
					key: to,
					model: policy,
					id: policy.id,
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				};
			});

			var toNew = '/forrisco/policy/' + this.props.policy.get("id") + '/item/new';
			if (!this.props.policy.attributes.archived) {
				tree.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_PLAN_PERMISSION)),
					label: Messages.get("label.newItem"),
					labelCls: 'fpdi-new-node-label',
					iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
					expandable: false,
					to: toNew,
					key: "newPolicy"
				});
			}

			me.setState({
				itensSelect: raw,
				itens:raw,
				tree: tree
			});
		}, me);


		ItemStore.on("retrieveSubitens", (models, opts) => {
			var children = [];
			if (models && models.total > 0) {
				children = models.data.map((model, index) => {
					var to = "/forrisco/policy/" + this.props.policy.attributes.id + "/item/" + opts.node.id + "/subitem/" + model.id
					var node = {
						label: model.name,
						expanded: false,
						expandable: false,
						labelCls: 'fpdi-node-label',
						to: to,
						key: to,
						model: model,
						id: model.id,
						onExpand: me.expandRoot,
						onShrink: me.shrinkRoot
					};


					return node;
				});
			}

			me.setState({
				subitensSelect: models,
			});

			if (!this.props.policy.attributes.archived) {
				children.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_PLAN_PERMISSION)),
					label: "Novo Subitem",
					iconCls: 'mdi mdi-plus fpdi-new-node-icon',
					labelCls: 'fpdi-new-node-label',
					expandable: false,
					to: "/forrisco/policy/" + this.props.policy.attributes.id + "/item/" + opts.node.id + "/subitem/new",
					onNewNode: me.newLevelInstance,
					newNodePlaceholder: 'Digite o nome do Novo Subitem',
					key: 'newNode-' + opts.node.key,
					parent: opts.node
				});
			}



			opts.node.children = children;
			me.forceUpdate();
		}, me);

		ItemStore.on("retrieveAllSubitens",(model) => {
			this.setState({
				subitens:model.data
			})
			this.retrieveFilledSections();
		},this);


		PolicyStore.on("findTerms", (model, data) => {
			if(model.data){
				this.setState({
					termsSearch:data.terms,
					itensSelect: data.itensSelect,
					subitensSelect: data.subitensSelect,
				});
			}
		}, me);

		ItemStore.dispatch({
			action: ItemStore.ACTION_FIND,
			data: {
				policyId: this.props.policy.get("id"),
			},
			opts: {
				wait: true
			}
		});


	},
	componentWillUnmount() {
		PolicyStore.off(null, null, this);
		ItemStore.off(null, null, this);
	},
	componentWillReceiveProps(newProps) {
		if (document.URL.indexOf('details/overview') >= 0) {
			this.refreshPlans(newProps.policy.get("id"));
		}
		if (newProps.treeType == this.state.actualType) {
			return;
		}

		this.setState({
			actualType: newProps.treeType
		});

		ItemStore.dispatch({
			action: ItemStore.ACTION_FIND,
			data: {
				policyId: newProps.policy.get("id"),
			},
			opts: {
				wait: true
			}
		});

	},

	treeSearch() {
		this.displayResult()
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FINDALL_TERMS,
			data: {
				policyId: this.props.policy.get("id"),
				terms:this.refs.term.value,
				page:1,
				limit:10,
				//ordResult: this.props.ordResult,
			},
			opts: {
				wait: true
			}
		});
	},

	expandRoot(nodeProps, nodeLevel) {

		if (nodeLevel == 0) {
			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_SUBITENS,
				data: nodeProps.id,
				opts: {
					node: nodeProps,
					level: nodeLevel
				}
			});
		}

		nodeProps.expanded = true;
	},
	shrinkRoot(nodeProps) {
		nodeProps.expanded = false;
		this.forceUpdate();
	},
	exportReport(evt) {
		evt.preventDefault();

		if(this.props.policy){
			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_ALLSUBITENS,
				data: this.props.policy.get("id"),
			});
		}
	},
	selectAllitens(){
		var i;
		for(i=0; i<this.state.rootSections.length; i++){
			if(document.getElementById("checkbox-item-"+i).disabled == false){
				document.getElementById("checkbox-item-"+i).checked = document.getElementById("selectall").checked;
			}
		}
	},
	selectAllsubitens(){
		var i;
		for(i=0; i<this.state.rootSubsections.length; i++){
			if(document.getElementById("checkbox-subitem-"+i).disabled == false){
				document.getElementById("checkbox-subitem-"+i).checked = document.getElementById("selectall").checked;
			}
		}
	},
	verifySelectAllitens() {
		var i;
		var selectedAll = true;
		for(i=0; i<this.state.rootSections.length; i++){
			if(document.getElementById("checkbox-item-"+i).disabled == false && !document.getElementById("checkbox-item-"+i).checked){
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},
	verifySelectAllsubitens() {
		var i;
		var selectedAll = true;
		for(i=0; i<this.state.rootSubsections.length; i++){
			if(document.getElementById("checkbox-subitem-"+i).disabled == false && !document.getElementById("checkbox-subitem-"+i).checked){
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},


	renderRecords() {
		return (<div>
		<div className="row">Itens
			<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10" >
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall" onChange={this.selectAllitens}></input>
							Selecionar todos
						</label>
					</div>
			</div>


			{this.state.itens.map((rootSection, idx) => {
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
			<div className="row">Subitens

				<div key="rootSection-selectall">
						<div className="checkbox marginLeft5 col-md-10" >
							<label name="labelSection-selectall" id="labelSection-selectall">
								<input type="checkbox" value="selectall" id="selectall" onChange={this.selectAllsubitens}></input>
								Selecionar todos
							</label>
						</div>
				</div>

			{this.state.subitens.map((rootSection, idx) => {
				return (
				<div key={"rootSection-filled"+idx}>
					<div className="checkbox marginLeft5 col-md-10" >
						<label name={"labelSection-filled"+idx} id={"labelSection-filled"+idx}>
							<input type="checkbox" value={rootSection.id} id={"checkbox-subitem-"+idx} onClick={this.verifySelectAllsubitens}></input>
							{rootSection.name}
						</label>
					</div>

				</div>);
			})}
			<br/><br/>
			</div>
		</div>);
	},

	preClick(){
		this.visualization(true);
	},

	visualization(pre){

		var i = 0;
		var sections = "";
		var subsections = "";
		var author = document.getElementById("documentAuthor").value;
		var title = document.getElementById("documentTitle").value;
		for(i=0; i<this.state.rootSections.length; i++){
			if(document.getElementById("checkbox-item-"+i).checked == true){
				sections = sections.concat(this.state.rootSections[i].id+"%2C");
			}
		}
		for(i=0; i<this.state.rootSubsections.length; i++){
			if(document.getElementById("checkbox-subitem-"+i).checked == true){
				subsections = subsections.concat(this.state.rootSubsections[i].id+"%2C");
			}
		}

		var item = sections.substring(0, sections.length - 3);
		var subitem = subsections.substring(0, subsections.length - 3);
		var elemError = document.getElementById("paramError");
		if(sections=='' || author.trim()=='' || title.trim()==''){
			elemError.innerHTML = Messages.get("label.exportError");
			if(author.trim()=='') {
				document.getElementById("documentAuthor").className = "borderError";
			}
			else {
				document.getElementById("documentAuthor").className = "";
			}
			if(title.trim()=='') {
				document.getElementById("documentTitle").className = "borderError";
			}
			else {
				document.getElementById("documentTitle").className = "";
			}
		}else{
			document.getElementById("documentAuthor").className = "";
			document.getElementById("documentTitle").className = "";


			var url = PolicyStore.url + "/exportreport" + "?title=" + title + "&author=" + author + "&pre=" + pre+ "&itens=" + item +"&subitens=" + subitem;
			url = url.replace(" ", "+");

			if(pre){
				window.open(url,title);
			}else{
				//this.context.router.push(url);
				window.open(url,title);
				Modal.hide();
			}
		}
	},

	retrieveFilledSections(){
	    var me = this;

		me.setState({
			rootSections: this.state.itens,
			rootSubsections: this.state.subitens
		});
		/*if (empty) {
			this.context.toastr.addAlertError(Messages.get("label.noDocumentFieldsFilled"));
		} else {
		*/
		//	$('#container') heigth 150px
			Modal.exportDocument(
				Messages.get("label.exportConfirmation"),
				this.renderRecords(),
				() => {
					this.visualization(false)
				},
				(
				{label:"Pré-visualizar",
				onClick:this.preClick,
				title:Messages.get("label.exportConfirmation")}
				)
			);
			document.getElementById("paramError").innerHTML = "";
			document.getElementById("documentAuthor").className = "";
			document.getElementById("documentTitle").className = "";
		//}
	},







	onKeyDown(evt) {
		var key = evt.which;
		if (key == 13) {
			evt.preventDefault();
			this.treeSearch();
		}
	},

	searchFilter() {
		this.setState({
			hiddenSearch: !this.state.hiddenSearch
		});
	},
	displayResult() {
		this.setState({
			hiddenResultSearch: true
		});
	},

	resultSearch() {
		this.setState({
            hiddenResultSearch:false
        });
        this.refs.term.value = "";
	},

	render() {
		return (
			<div className="fpdi-tabs">
				<ul className="fpdi-tabs-nav marginLeft0" role="tablist">

					{this.props.policy.get('id') ?
						<Link
							role="tab"
							to={"forrisco/policy/" + this.props.policy.get("id") + "/"}
							title="Política"
							activeClassName="active"
							className="tabTreePanel">
							{Messages.getEditable("label.forriscoPolicy", "fpdi-nav-label")}
						</Link> : undefined}


				</ul>
				{
					this.context.router.isActive("forrisco/policy/" + this.props.policy.get("id") + "/item") ||
					this.context.router.isActive("forrisco/policy/" + this.props.policy.get("id") + "/edit") ?
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

							{this.state.hiddenResultSearch ?
									<SearchResult
										policyId={this.props.policy.get("id")}
										terms={this.state.termsSearch}
										itensSelect={this.state.itensSelect}
										subitensSelect={this.state.subitensSelect}
										ordResult={this.state.ordResultSearch}
										//total={this.state.total}
										//resultSearch={this.state.resultSearch}
										//parentId={this.state.parentIdSearch}
										//dataInit={this.state.dataInitSearch}
										//dataEnd={this.state.dataEndSearch}
									/>
									:
									<div>
										{this.context.roles.SYSADMIN ? "" : <FavoriteTree/>}
										<TreeView tree={this.state.tree}/>

										<hr className="divider"></hr>
										{(this.context.roles.MANAGER || _.contains(this.context.permissions,
											PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
											<a className="btn btn-sm btn-primary center" onClick={this.exportReport}>
										<span /*className="mdi mdi-export"*/
										/> {Messages.getEditable("label.exportReport", "fpdi-nav-label")}
											</a> : ""
										}
									</div>
							}

							{this.state.hiddenSearch ?
								<div className="container Pesquisa-Avancada">
									<LevelSearch
										searchText={this.refs.term.value}
										subplans={this.state.itens}
										policy={this.props.policy.get("id")}
										hiddenSearch={this.searchFilter}
										displayResult={this.displayResult}
										//submit={this.treeSearch}
									/>
								</div> : ""
							}
						</div> : ""
				}

				<div className="fpdi-tabs-fill">
				</div>
			</div>);
	}
});
