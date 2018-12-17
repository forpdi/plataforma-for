import _ from 'underscore';
import React from "react";
import {Link} from "react-router";
import FavoriteTree from "forpdi/jsx/planning/widget/plan/FavoriteTree.jsx";
import TreeView from "forpdi/jsx_forrisco/core/widget/treeview/TreeView.jsx";
import LevelSearch from "forpdi/jsx/planning/widget/search/LevelSearch.jsx";
import SearchResult from "forpdi/jsx/planning/widget/search/SearchResult.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
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
			hiddenSearch: false,
			hiddenResultSearch: false,
			resultSearch: [],
			dataInitSearch:null,
			dataEndSearch:null,
			levelsSelectSearch:[],
			ordResultSearch:null,
			parentIdSearch:null,
			termsSearch:'',
			subPlansSelectSearch:[],
			unnumberedSections: 0
		};
	},
	componentDidMount(){
		var me = this;

		ItemStore.on("find", (store, raw, opts) => {
			var tree = raw.map((policy, index) => {
				var to = '/forrisco/policy/'+this.props.policy.get("id")+'/item/'+policy.id;
				return {
					label: policy.name,
					expanded: false,
					expandable: policy.name=="Informações gerais"? false:true ,
					to: to,
					key: to,
					model: policy,
					id: policy.id,
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot
				};
			});

			var toNew = '/forrisco/policy/'+this.props.policy.get("id")+'/item/new';
			if(!this.props.policy.attributes.archived){
				tree.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions,
							PermissionsTypes.MANAGE_PLAN_PERMISSION)),
					label: Messages.get("label.newItem"),
					labelCls:'fpdi-new-node-label',
				  	iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				  	expandable: false,
					to: toNew,
					key: "newPolicy"
				});
			}

			me.setState({
				subplans: raw,
				tree: tree
			});
		}, me);


        ItemStore.on("retrieveSubitens", (models, opts) =>{
			var children = [];
            if(models && models.total > 0){
	            children = models.data.map((model, index) => {
                    var to = "/forrisco/policy/"+this.props.policy.attributes.id+"/item/"+opts.node.id+"/subitem/"+model.id
                    var node = {
	                    label: model.name,
	                    expanded: false,
	                    expandable: false,//!model.level.leaf && !model.aggregate,
                		labelCls:'fpdi-node-label',
	                    to: to,
	                    key: to,
	                    model: model,
	                    id: model.id,
	                    onExpand: me.expandRoot,
	                    onShrink: me.shrinkRoot
	                };
	                /*if (model.level.leaf) {
	                	node.iconCls = "mdi mdi-menu-right";
	                }*/
	                return node;
	            });
			}

			if(!this.props.policy.attributes.archived){
	            children.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions,
	         			PermissionsTypes.MANAGE_PLAN_PERMISSION)),
	                label: "Novo Subitem",
	                iconCls: 'mdi mdi-plus fpdi-new-node-icon',
	                labelCls:'fpdi-new-node-label',
	                expandable: false,
	                to: "/forrisco/policy/"+this.props.policy.attributes.id+"/item/"+opts.node.id+"/subitem/new",
	                onNewNode: me.newLevelInstance,
	                newNodePlaceholder: 'Digite o nome do Novo Subitem',
	                key: 'newNode-'+opts.node.key,
	                parent: opts.node
	            });
        	}

            opts.node.children = children;
			me.forceUpdate();
        }, me);

		/*ItemStore.on("retrieveItens", (model) => {
        	var documentId;
        	var tree = [];
        	var sections = model.get("sections");


        	var unnumberedSections = 0;

        	if (sections) {
	        	tree = sections.map((section,idx) => {

	        		if(section.preTextSection){
	        			var node = me.createDocumentNodeDef(section, undefined, 0);
	        			unnumberedSections++;
	        		}else{
	        			var node = me.createDocumentNodeDef(section, undefined, idx-unnumberedSections+1);
						node.children.push({
							hidden: !((this.context.roles.MANAGER || _.contains(this.context.permissions,
								PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.props.plan.attributes.archived),
							label: "Nova subseção",
	               			labelCls:'fpdi-new-node-label',
			                iconCls: 'mdi mdi-plus',
			                expandable: false,
			                to: null,
			                onNewNode: me.newDocumentSection,
			                key: 'newNode',
			                parent: node,
			                parentId: section.id,
	                		documentId: model.get("document").id,
			                newNodePlaceholder: Messages.get("label.inputSubsectionTitle")
						});
					}
	                return node;
	        	});
	        }

	        if (model.get("document") == undefined) {
	        	documentId = null ;

	        } else {
	        	documentId = model.get("document").id;
	        }

	        tree.push({
				hidden: !((this.context.roles.MANAGER || _.contains(this.context.permissions,
				    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && !this.props.plan.attributes.archived),
				label: Messages.get("label.newSection"),
                iconCls: 'mdi mdi-plus',
                labelCls:'fpdi-new-node-label',
                expandable: false,
                to: null,
                onNewNode: me.newDocumentSection,
                key: 'newNode',
                documentId: documentId,
                newNodePlaceholder: Messages.get("label.inputSectionTitle")
			});
			me.setState({
				policyTree: tree,
				unnumberedSections: unnumberedSections
			});
		}, me);*/

		ItemStore.dispatch({
			action: ItemStore.ACTION_FIND,
			data: {
				policyId: this.props.policy.get("id"),
			},
			opts: {
				wait: true
			}
		});

		//this.retrieveFilledSections();
	},
	componentWillUnmount() {
		PolicyStore.off(null, null, this);
		ItemStore.off(null, null, this);
	},
	componentWillReceiveProps(newProps) {
	    if(document.URL.indexOf('details/overview')>=0){
	    	this.refreshPlans(newProps.policy.get("id"));
		}
		if(newProps.treeType == this.state.actualType){
			return;
		}

		this.setState({
			actualType: newProps.treeType
		});

		//this.refreshPlans(newProps.policy.get("id")	);

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
	/*retrieveFilledSections(){
	    var me = this;
	    PolicyStore.on("filledSectionsRetrieved", (model) => {
		    var tree = [];
	        var sections = model;
	        var empty = true;
		    tree = sections.map((section,idx) => {
				if(section.filled == 1){
					empty = false;
				}
	       		return section;
	    	});
			me.setState({
				rootSections: tree
			});
			if (empty) {
				this.context.toastr.addAlertError(Messages.get("label.noDocumentFieldsFilled"));
			} else {
				Modal.exportDocument(
					Messages.get("label.exportConfirmation"),
					this.renderRecords(),
					() => {
						var i = 0;
						var sections = "";
						var author = document.getElementById("documentAuthor").value;
						var title = document.getElementById("documentTitle").value;

						for(i=0; i<this.state.rootSections.length; i++){
							if(document.getElementById("checkbox-"+i).checked == true){
								sections = sections.concat(this.state.rootSections[i].id+"%2C");
							}
						}

						var lista = sections.substring(0, sections.length - 3);
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
							var url = DocumentStore.url + "/exportdocument" + "?title=" + title + "&author=" + author + "&lista=" + lista;
							url = url.replace(" ", "+");
							Modal.hide();
							var w = window.open(url,title);
						}
					});
				document.getElementById("paramError").innerHTML = "";
				document.getElementById("documentAuthor").className = "";
				document.getElementById("documentTitle").className = "";
			}
		});
	},*/
	/*treeSearch() {
		this.displayResult()
		PlanStore.dispatch({
			action: PlanStore.ACTION_FIND_TERMS,
			data: {
				parentId: this.props.policy.get("id"),
				terms:this.refs.term.value,
				page:1,
				limit:10
			},
			opts: {
				wait: true
			}
		});
	},*/

	expandRoot(nodeProps, nodeLevel) {

		if(nodeLevel == 0) {
        	// Nível raiz, subplano.
            ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_SUBITENS,
				data:nodeProps.id,
                opts: {
                	node: nodeProps,
					level: nodeLevel
				}
				 /* data: {
                    sequence: nodeProps.model.structure.levels[0].sequence,
                    planId: nodeProps.id,
                    parentId: 0
                },*/
            });
       /* } else {
        	// Níveis da estrutura, Level.
        	StructureStore.dispatch({
                action: StructureStore.ACTION_RETRIEVE_LEVEL_INSTANCE,
                data: {
                    sequence: nodeProps.model.level.sequence+1,
                    planId: nodeProps.model.plan.id,
                    parentId: nodeProps.id
                },
                opts: {
                	node: nodeProps,
                	level: nodeLevel
                }
            });*/
		}

		nodeProps.expanded = true;
	},
	shrinkRoot(nodeProps) {
		nodeProps.expanded = false;
		this.forceUpdate();
	},
	exportDocument(evt) {
		evt.preventDefault();
		PolicyStore.dispatch({
            action: PolicyStore.ACTION_RETRIEVE_FILLED_SECTIONS, // criar uma chamada para essa parte
         	data: {
              id: this.props.policy.attributes.id
            }
	    });
	},

	onKeyDown(evt){
		var key = evt.which;
		if(key == 13) {
			evt.preventDefault();
			this.treeSearch();
		}
	},

	searchFilter() {
		this.setState({
            hiddenSearch:!this.state.hiddenSearch
        });
	},
	displayResult () {
		this.setState({
            hiddenResultSearch:true
        });
	},

	render() {
		return (
		<div className="fpdi-tabs">
			<ul className="fpdi-tabs-nav marginLeft0" role="tablist">

				{this.props.policy.get('id') ?
				 <Link
					role="tab"
					to={"forrisco/policy/"+this.props.policy.get("id")+"/"}
					title="Política"
					activeClassName="active"
					className="tabTreePanel">
						{Messages.getEditable("label.forriscoPolicy","fpdi-nav-label")}
				</Link> : undefined}


			</ul>
			{this.context.router.isActive("forrisco/policy/"+this.props.policy.get("id")+"/item")
			|| this.context.router.isActive("forrisco/policy/"+this.props.policy.get("id")+"/edit") ?
			<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">

					<div className="marginBottom10 inner-addon right-addon right-addonPesquisa plan-search-border">
						<i className="mdiClose mdi mdi-close pointer" onClick={this.resultSearch} title={Messages.get("label.clean")}> </i>
	    				<input type="text" className="form-control-busca" ref="term" onKeyDown={this.onKeyDown}/>
	    				<i className="mdiBsc mdi mdi-chevron-down pointer" onClick={this.searchFilter} title={Messages.get("label.advancedSearch")}> </i>
	    				<i id="searchIcon" className="mdiIconPesquisa mdiBsc  mdi mdi-magnify pointer" onClick={this.treeSearch} title={Messages.get("label.search")}> </i>
					</div>


				{this.state.hiddenResultSearch ?
					<SearchResult
						resultSearch = {this.state.resultSearch}
						planId = {this.props.policy.get("id")}
						terms = {this.state.termsSearch}
						parentId = {this.state.parentIdSearch}
						subPlansSelect = {this.state.subPlansSelectSearch}
						levelsSelect = {this.state.levelsSelectSearch}
						dataInit = {this.state.dataInitSearch}
						dataEnd = {this.state.dataEndSearch}
						ordResult = {this.state.ordResultSearch}
					/>
				:
					<div>
						{this.context.roles.SYSADMIN ? "" : <FavoriteTree />}
						<TreeView tree={this.state.tree} />


						<hr className="divider"></hr>
						{(this.context.roles.MANAGER || _.contains(this.context.permissions,
							PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
							<a className="btn btn-sm btn-primary center" onClick={this.exportDocument}>
								<span /*className="mdi mdi-export"*/
								/> {Messages.getEditable("label.exportDocument","fpdi-nav-label")}
							</a> : ""
						}
					</div>
				}

				{this.state.hiddenSearch ?
					<div className = "container Pesquisa-Avancada">
						<LevelSearch
 							searchText= {this.refs.term.value}
  							subplans= {this.state.subplans}
  							policy= {this.props.policy.get("id")}
 							submit={this.treeSearch}
 							hiddenSearch = {this.searchFilter}
 							displayResult = {this.displayResult}
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
