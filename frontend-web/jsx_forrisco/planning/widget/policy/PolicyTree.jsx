import _ from 'underscore';
import React from "react";
import {Link} from "react-router";
import FavoriteTree from "forpdi/jsx/planning/widget/plan/FavoriteTree.jsx";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import LevelSearch from "forpdi/jsx_forrisco/planning/widget/search/policy/LevelSearch.jsx";
import SearchResult from "forpdi/jsx_forrisco/planning/widget/search/policy/SearchResult.jsx";
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
		policy: React.PropTypes.object.isRequired,
		className: React.PropTypes.object
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
			unnumberedSections: 0,
			export:false,
			policyId:null
		};
	},

	componentDidMount() {
		var me = this;

		ItemStore.on("retrieveItens", (raw) => {
			var tree = raw.data.map((policy, index) => {
				var to = '/forrisco/policy/' + this.props.policy.id + '/item/' + policy.id;
				return {
					label: policy.name,
					expanded: false,
					expandable: true,
					to: to,
					key: to,
					model: policy,
					id: policy.id,
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				};
			});

			var overview= '/forrisco/policy/' + this.props.policyId + '/item/overview'
			var info = {
				label: "Informações Gerais",
				expanded: false,
				to: overview,
				key: overview,
				model: this.props.policy,
				id: this.props.policy.id,
			};

			var newItem = {
				hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions,
					PermissionsTypes.MANAGE_PLAN_PERMISSION)),
				label: Messages.get("label.newItem"),
				labelCls: 'fpdi-new-node-label',
				iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				expandable: false,
				to: '/forrisco/policy/' + this.props.policy.id + '/item/new',
				key: "newPolicy"
			}

			tree.unshift(info);
			tree.push(newItem);

			me.setState({
				itensSelect: raw.data,
				itens:raw.data,
				tree: tree
			});
		}, me);


		ItemStore.on("retrieveSubitens", (models, opts) => {
			var children = [];
			if (models && models.total > 0) {
				children = models.data.map((model, index) => {
					var to = "/forrisco/policy/" + this.props.policy.id + "/item/" + opts.node.id + "/subitem/" + model.id
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



			if (!this.props.policy.archived) {
				children.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_PLAN_PERMISSION)),
					label: "Novo Subitem",
					iconCls: 'mdi mdi-plus fpdi-new-node-icon',
					labelCls: 'fpdi-new-node-label',
					expandable: false,
					to: "/forrisco/policy/" + this.props.policy.id + "/item/" + opts.node.id + "/subitem/new",
					onNewNode: me.newLevelInstance,
					newNodePlaceholder: 'Digite o nome do Novo Subitem',
					key: 'newNode-' + opts.node.key,
					parent: opts.node
				});
			}

			for(var i=0 ; i < this.state.tree.length-1;i++){
				if(this.state.tree[i].id==opts.node.id){
					this.state.tree[i].children=children;
					break;
				}
			}

			me.setState({
				subitensSelect: models,
				tree:this.state.tree
			});

			opts.node.children = children;
			me.forceUpdate();
		}, me);

		ItemStore.on("retrieveAllSubitens",(model) => {
			this.setState({
				subitens:model.data,
				rootSections: this.state.itens,
				rootSubsections: model.data,
			})

			if(this.state.export){
				this.retrieveFilledSections();
				this.setState({
					subitens:model.data,
					export:false,
				})
			}
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

		//Atualiza a árvore quando um novo item é cadastrado
		ItemStore.on("newItem", () => {
			this.refreshOnUpdate(this.props.policy.id);
		});

		//Atualiza a árvore quando um novo subitem é cadastrado
		ItemStore.on("newSubItem", () => {
			this.refreshOnUpdate(this.props.policy.id);
		});

		//Atualiza a árvore quando um  item é deletado
		ItemStore.on("itemDeleted", () => {
			this.refreshOnUpdate(this.props.policy.id);
		});

		//Atualiza a árvore quando um  subitem é deletado
		ItemStore.on("subitemDeleted", () => {
			this.refreshOnUpdate(this.props.policy.id);
		});

		this.refreshOnUpdate(this.props.policy.id);
	},

	refreshOnUpdate(policyId) {
		ItemStore.dispatch({
			action: ItemStore.ACTION_RETRIEVE_ITENS,
			data: policyId,
		});
	},

	componentWillUnmount() {
		PolicyStore.off(null, null, this);
		ItemStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps) {
		ItemStore.dispatch({
			action: ItemStore.ACTION_RETRIEVE_ITENS,
			data:newProps.policy.id,
		});

		this.state.policyId=newProps.policyId

		if (document.URL.indexOf('details/overview') >= 0) {
			this.refreshPlans(newProps.policy.id);
		}

		if(newProps.subitemId != null){
			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_SUBITENS,
				data: newProps.itemId,
				opts: {
					node: {id:newProps.itemId, children:null},
				}
			});
		}

		var exists=false

		for(var i=0; i<this.state.itens.length;i++){
			if(this.state.itens[i].id == newProps.itemId){
				exists=true;
			}
		}

		if (newProps.itemId !== this.props.itemId) {
			//this.refresh(newProps.planRisk.id);
		}


		if (newProps.treeType == this.state.actualType && (exists || newProps.itemId == null)) {
			return;
		}

		ItemStore.dispatch({
			action: ItemStore.ACTION_RETRIEVE_ITENS,
			data:newProps.policy.id,
		});

	},

	treeSearch() {
		this.displayResult()
		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FINDALL_TERMS,
			data: {
				policyId: this.props.policy.id,
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
				data: this.props.policy.id,
			});

			this.setState({export:true})
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
				document.getElementById("checkbox-subitem-"+i).checked = document.getElementById("selectallsub").checked;
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
		document.getElementById("selectallsub").checked = selectedAll;
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
								<input type="checkbox" value="selectall" id="selectallsub" onChange={this.selectAllsubitens}></input>
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


			var url = `${PolicyStore.url}/exportReport?policyId=${this.props.policyId}&title=${title}&
				author=${author}&pre=${pre}&itens=${item}&subitens=${subitem}`;
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
							policyId={this.props.policy.id}
							terms={this.state.termsSearch}
							itensSelect={this.state.itensSelect}
							subitensSelect={this.state.subitensSelect}
							ordResult={this.state.ordResultSearch}
						/>
						:
						<div>
							<TreeView tree={this.state.tree}/>
								<hr className="divider"></hr>
							{(this.context.roles.MANAGER || _.contains(this.context.permissions,
								PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
								<a className="btn btn-sm btn-primary center" onClick={this.exportReport}>
									<span/>
									{Messages.getEditable("label.exportReport", "fpdi-nav-label")}
								</a>
							: ""}
						</div>
					}
					{this.state.hiddenSearch ?
					<div className="container Pesquisa-Avancada">
						<LevelSearch
							searchText={this.refs.term.value}
							subplans={this.state.itens}
							policy={this.props.policy.id}
							hiddenSearch={this.searchFilter}
							displayResult={this.displayResult}
						/>
					</div> : ""
				}
			<div className="fpdi-tabs-fill"></div>
			</div>);
		}
});
