import _ from 'underscore';
import React from "react";
import {Link} from "react-router";
import FavoriteTree from "forpdi/jsx/planning/widget/plan/FavoriteTree.jsx";
import TreeView from "forpdi/jsx/core/widget/treeview/TreeView.jsx";
import LevelSearch from "forpdi/jsx/planning/widget/search/LevelSearch.jsx";
import SearchResult from "forpdi/jsx/planning/widget/search/SearchResult.jsx";
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import UserSession from "forpdi/jsx/core/store/UserSession.jsx";
import PlanStore from "forpdi/jsx/planning/store/Plan.jsx";
import PlanMacroStore from "forpdi/jsx/planning/store/PlanMacro.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import DocumentStore from "forpdi/jsx/planning/store/Document.jsx"
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
		plan: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			subplans: [],
			tree: [],
			documentTree: [],
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
		StructureStore.on('levelAttributeSaved', (model) => {
			//Consulta para encontrar qual nó da árvore está ativo
			var nodeActive = document.getElementsByClassName("fpdi-node-label active");
			if(nodeActive.length>0){  // Caso encontre um valor, o texto dele será alterado pelo nome atual do nó
				if(model.data.name.length>50){
					nodeActive[0].innerText = (model.data.name.substring(0, 50)+"...");
				}else{
					nodeActive[0].innerText = model.data.name;
				}
			}
		}, me);
		PlanStore.on("find", (store, raw, opts) => {		
			var tree = raw.map((plan, index) => {
				var to = '/plan/'+this.props.plan.get("id")+'/details/subplan/'+plan.id;
				return {
					label: plan.name,
					expanded: false,
					expandable: true,
					to: to,
					key: to,
					model: plan,
					id: plan.id,
					onExpand: this.expandRoot,
					onShrink: this.shrinkRoot                    
				};
			});

			var toNew = '/plan/'+this.props.plan.get("id")+'/details/subplans/new';
			if(!this.props.plan.attributes.archived){
				tree.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions, 
							PermissionsTypes.MANAGE_PLAN_PERMISSION)),
					label: "Novo(a) Plano de metas",
					labelCls:'fpdi-new-node-label',
				  	iconCls: 'mdi mdi-plus fpdi-new-node-icon pointer',
				  	expandable: false,
					to: toNew,
					key: "newPlan"
				});
			}
			
			if(this.isMounted()){
				me.setState({
					subplans: raw,
					tree: tree
				});
			}
		}, me);

        PlanStore.on("sync", (model) =>{     
        	
        	if(model.get('updated')){
        		me.state.tree.map((child,idx) => {
        			if(child.id == model.get("id")){
        				me.state.tree.splice(idx, 1);
        			}
        		});
        	}

        	var to = '/plan/'+me.props.plan.get("id")+'/details/subplan/'+model.get("id");
            me.state.tree.splice(me.state.tree.length-1, 0, {
				label: model.get("name"),
				expanded: false,
				expandable: true,
				to: to,
				key: to,
				model: model.attributes,
				id: model.get("id"),
				onExpand: me.expandRoot,
				onShrink: me.shrinkRoot
            });

            if(model.get("updated")){
            	//Toastr.remove();
			    //Toastr.success("Plano de metas alterado com sucesso");
				this.context.toastr.addAlertSuccess("Plano de metas alterado com sucesso");
            }else{
	        	//Toastr.remove();
				//Toastr.success("Plano de metas salvo com sucesso");
				this.context.toastr.addAlertSuccess("Plano de metas salvo com sucesso");
			}

			//this.refreshPlans(this.props.plan.get("id"));
			
        },me);

        StructureStore.on("levelInstanceFind", (models, opts) =>{
            var children = [];
            if(models && models.length > 0){
	            children = models.map((model, index) => {	            	
                    var to = "/plan/"+me.props.plan.get("id")+"/details/subplan/level/"+model.id;
                    var node = {
	                    label: model.name,                    
	                    expanded: false,
	                    expandable: !model.level.leaf && !model.aggregate,
                		labelCls:'fpdi-node-label',
	                    to: to,
	                    key: to,
	                    model: model,
	                    id: model.id,
	                    onExpand: me.expandRoot,
	                    onShrink: me.shrinkRoot
	                };
	                if (model.level.leaf) {
	                	node.iconCls = "mdi mdi-menu-right";
	                }
	                return node;
	            });
	        }
			if(!this.props.plan.attributes.archived){
	            var label = "Novo(a) "+(models.length == undefined ? models.name : models[0].level.name);
	            children.push({
					hidden: !(this.context.roles.MANAGER || _.contains(this.context.permissions, 
	         			PermissionsTypes.MANAGE_PLAN_PERMISSION)),
	                label: label,
	                iconCls: 'mdi mdi-plus fpdi-new-node-icon',
	                labelCls:'fpdi-new-node-label',
	                expandable: false,
	                to: null,
	                onNewNode: me.newLevelInstance,
	                newNodePlaceholder: 'Digite o nome do(a)'+ label.split("Novo(a)")[1],
	                key: 'newNode-'+opts.node.key,
	                parent: opts.node
	            });
        	}
            
            opts.node.children = children;
            if(me.isMounted()){
	            me.forceUpdate();
	        }
        }, me);

        StructureStore.on("levelInstanceCreated", (model, opts) => {
        	this.refreshPlans(this.props.plan.get("id"));
        	//this.refreshPlans(this.props.plan.get("id"));


        	var actualParent = opts.node.parent;
        	var to = "/plan/"+me.props.plan.get("id")+"/details/subplan/level/"+model.id;
        	
        	var newNode = {
                label: model.name,
                expanded: false,
                expandable: true,
                labelCls:'fpdi-node-label',
                to: to,
                key: to,
                model: model,
                id: model.id,
                onExpand: me.expandRoot,
                onShrink: me.shrinkRoot
            };
            if (model.level.leaf) {
            	newNode.iconCls = "mdi mdi-menu-right";
            	newNode.expandable = false;
            }
            actualParent.children.splice(actualParent.children.length-1, 0, newNode);

            //Toastr.remove();
			//Toastr.success('"'+ model.name+'" criado com sucesso.');
			this.context.toastr.addAlertSuccess('"'+ model.name+'" criado com sucesso.');     
            me.context.router.push(newNode.to);
        }, me);

        StructureStore.on("deleteLevelInstance", (model) => {
        	this.refreshPlans(this.props.plan.get("id"));
        	for (var i=0; i<me.state.tree.length; i++) {
        		me.deleteNode(model.data.id, me.state.tree[i]);
        	}
        }, me);

		StructureStore.on("deleteLevelInstanceByTable", (model) => {
			this.refreshPlans(this.props.plan.get("id"));
        	for (var i=0; i<me.state.tree.length; i++) {
        		me.deleteNode(model.data.id, me.state.tree[i]);
        	}
        }, me);

		StructureStore.on('deletegoals', (model) => {			
			for (var j=0; j<model.data.length; j++) {
				for (var i=0; i<me.state.tree.length; i++) {
        			me.deleteNode(model.data[j], me.state.tree[i]);
				}
        	}
		}, me);

        StructureStore.on("goalsGenerated", (model) => {
			for (var i=0; i<me.state.tree.length; i++) {
        		me.insertNodes(model.data, me.state.tree[i]);
        	}
		}, me);

        PlanStore.on("planFind", (model, data) => {
        
        	this.setState({
				dataInitSearch:data.dataInit,
				dataEndSearch:data.dataEnd,
				levelsSelectSearch:data.levelsSelect,
				ordResultSearch:data.ordResult,
				termsSearch:data.terms,
				parentIdSearch:data.parentId,
				subPlansSelectSearch:data.subPlansSelect
			});

			if (model != null && this.isMounted()) {
				this.setState({
           			resultSearch:model.data
        		});	
			}
		}, me);	
		
		DocumentStore.on("sectionAttributesSaved", (model) => {
			var nodeActive = document.getElementsByClassName("active");
			
			if(nodeActive.length > 0) {
				var idx = nodeActive[2].innerText.split(" - ")[0];
				if(model.data.name.length > 50) {
					nodeActive[2].innerText = (idx +" - "+ model.data.name.substring(0,50) + "...");
				} else {
					nodeActive[2].innerText = idx +" - "+ model.data.name;
				}
			}
		}, me);
		DocumentStore.on("sectiondeleted", (model) => {
			for (var i=0; i<me.state.documentTree.length; i++) {
        		me.deleteNode(model.id, me.state.documentTree[i]);
        	}
		}, me);
        DocumentStore.on("retrieve", (model) => {
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
			                newNodePlaceholder: "Digite o título da subseção"
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
				label: "Nova seção",
                iconCls: 'mdi mdi-plus',
                labelCls:'fpdi-new-node-label',
                expandable: false,
                to: null,
                onNewNode: me.newDocumentSection,
                key: 'newNode',
                documentId: documentId,
                newNodePlaceholder: "Digite o título da seção"
			});
			if(this.isMounted()){
	        	me.setState({
	        		documentTree: tree,
	        		unnumberedSections: unnumberedSections
	        	});
	        }
        	
        }, me);

		DocumentStore.on("sectioncreated", (model, opts) => {			
        	var actualParent = opts.node.parent;
        	var newNode;
        	if (actualParent) {        		
        		newNode = me.createDocumentNodeDef(model, (actualParent.index)+".", actualParent.children.length);
           		actualParent.children.splice(actualParent.children.length-1, 0, newNode);
        	} else {
        		newNode = me.createDocumentNodeDef(model, undefined, this.state.documentTree.length-this.state.unnumberedSections);
        		newNode.children.push({
					hidden: !(this.context.roles.MANAGER),
					label: "Nova subseção",
					labelCls:'fpdi-new-node-label',
	                iconCls: 'mdi mdi-plus',
	                className: "fpdi-new-node-label",
	                expandable: false,
	                to: null,
	                onNewNode: me.newDocumentSection,
	                key: 'newNode',
	                parent: newNode,
	                parentId: model.id,
            		documentId: model.documentId,
	                newNodePlaceholder: "Digite o título da subseção"
				});
           		me.state.documentTree.splice(me.state.documentTree.length-1, 0, newNode);
        	}

            //Toastr.remove();

			//Toastr.success('Seção "'+ model.name+'" criada com sucesso.');
			this.context.toastr.addAlertSuccess('Seção "'+ model.name+'" criada com sucesso.');     
            me.context.router.push(newNode.to);
        }, me);

        if(this.props.treeType == "document"){
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_RETRIEVE,
				data: this.props.plan.id
			});
		} else {
			this.refreshPlans(this.props.plan.get("id"));
		}

		this.retrieveFilledSections();
		
	},

	componentWillUnmount() {
		PlanStore.off(null, null, this);
		PlanMacroStore.off(null, null, this);
		StructureStore.off(null,null,this);
		DocumentStore.off(null,null,this);
	},

	componentWillReceiveProps(newProps) {
	    if(document.URL.indexOf('details/overview')>=0)  {
	    	this.refreshPlans(newProps.plan.get("id"));	
		} 
		if(newProps.treeType == this.state.actualType){
			return;
		} else if(this.isMounted()){
			this.setState({
				actualType: newProps.treeType
			});
			this.refreshPlans(newProps.plan.id);
		}		
		if(newProps.treeType == "document"){
			DocumentStore.dispatch({
				action: DocumentStore.ACTION_RETRIEVE,
				data: newProps.plan.id
			});
		}
	},
	
	createDocumentNodeDef(section, namePrefix, idx) {
		namePrefix = namePrefix || "";
		var to = "/plan/"+this.props.plan.get("id")+"/document/section/"+section.id;
		var node;
		if(section.preTextSection){
			node = {
				index: namePrefix+idx,
				label: section.name,
				expanded: false,
				expandable: false,//section.children != "" && section.children != undefined ,
				to: to,
				key: 'section-'+section.id,
				model: section,
				id: section.id,
				//onExpand: this.expandDocumentRoot,
				//onShrink: this.shrinkRoot,
				//children: !section.leaf && section.children ? section.children.map((subsection,subIdx) => {
				//	return this.createDocumentNodeDef(subsection, idx+".", subIdx+1);
				//}):[]
				//root: section.attributesAmount <= 0
			};
		}else{
			node = {
				index: namePrefix+idx,
				label: namePrefix+idx+" - "+section.name,
				expanded: false,
				expandable: true,//section.children != "" && section.children != undefined ,
				to: to,
				key: 'section-'+section.id,
				model: section,
				id: section.id,
				onExpand: this.expandDocumentRoot,
				onShrink: this.shrinkRoot,
				children: !section.leaf && section.children ? section.children.map((subsection,subIdx) => {
					return this.createDocumentNodeDef(subsection, idx+".", subIdx+1);
				}):[]
				//root: section.attributesAmount <= 0
			};
		}
		if (section.leaf) {
			node.iconCls = "mdi mdi-menu-right";
		}
		return node;
	},

    newDocumentSection(title, nodeProps) {
    	if (!!title.match(/^(\s)*$/)){
        	//Toastr.remove();
        	//Toastr.error("O campo deve ser preenchido com o título da seção.");
			this.context.toastr.addAlertError("O campo deve ser preenchido com o título da seção.");
        	return false;
        }
        DocumentStore.dispatch({
            action: DocumentStore.ACTION_CREATE_SECTION,
            data: {
                name: title,
                document: nodeProps.documentId,
                parentId: nodeProps.parentId || undefined
            },
            opts: {
            	node: nodeProps
            }
        });
        return true;
    },

    newLevelInstance(name, nodeProps){    	
    	if(nodeProps.parent.model.aggregate){
    		//Toastr.remove();
        	//Toastr.error("Indicadores agregados não podem ter metas.");
			this.context.toastr.addAlertError("Indicadores agregados não podem ter metas.");
        	return false;
    	}
    	var model = nodeProps.parent.model;
    	var subplanId = (model.plan ? model.plan.id : model.id);
		var levelId = (model.level ? model.level.id+1 : model.structure.levels[0].id);
		var parentId = (model.level ? model.id:null);
    	if(name ==""  || !!name.match(/^(\s)*$/)){
        	//Toastr.remove();
        	//Toastr.error("O campo deve ser preenchido com o nome do "+nodeProps.label.toLowerCase()+"!");
			this.context.toastr.addAlertError("O campo deve ser preenchido com o nome do "+nodeProps.label.toLowerCase()+"!");
        	return false;
        }        
        StructureStore.dispatch({
            action: StructureStore.ACTION_CREATE_LEVELINSTANCE,
            data: {
                instanceName: name,
                planId: subplanId,
                levelId: levelId,
                parentId: parentId
            },
            opts: {
            	node: nodeProps
            }
        });
        return true;
    },

	deleteNode(deletedId, parentNode) {
		// Remove o nó da árvore usando busca em profundidade.
		var nodes = parentNode.children;
		if (nodes && nodes.length > 0) {
			for (var i = 0; i < nodes.length; i++) {
				if(deletedId == nodes[i].id){
					parentNode.children.splice(i,1);
					this.setState({
		                	tree: this.state.tree
					});
					return true;
				}
				if (this.deleteNode(deletedId, nodes[i])) {
					return true;
				}
			}
		}
		return false;
	},

	insertNodes(data, parentNode) {
		// Inserir os nós da árvore usando busca em profundidade.
		var nodes = parentNode.children;
		if (data.levelInstances && data.levelInstances.length > 0 && nodes && nodes.length > 0) {
			for (var i = 0; i < nodes.length; i++) {
				if(data.levelInstances[0].parent == nodes[i].id) {
					for (var j = 0; j < data.levelInstances.length; j++) {
						var me = this;
						var to = "/plan/"+me.props.plan.get("id")+"/details/subplan/level/"+data.levelInstances[j].id;
	                    var node = {
		                    label: data.levelInstances[j].name,                    
		                    expanded: false,
		                    expandable: !data.levelInstances[j].level.leaf,
	                		labelCls:'fpdi-node-label',
		                    to: to,
		                    key: to,
		                    model: data.levelInstances[j],
		                    id: data.levelInstances[j].id,
		                    onExpand: me.expandRoot,
		                    onShrink: me.shrinkRoot
		                };
		                if (data.levelInstances[j].level.leaf) {
		                	node.iconCls = "mdi mdi-menu-right";
		                }
		                if (nodes[i].children)
		                	nodes[i].children.splice(nodes[i].children.length-1, 0, node);
		                else 
		                	nodes[i].children = [node];
					}
					if(this.isMounted()){
						me.setState({
		                	tree: me.state.tree
		                });
					}
					return true;
				}
				if (this.insertNodes(data, nodes[i])) {
					return true;
				}
			}
		}
		return false;
	},

	refreshPlans(planMacroId) {
		PlanStore.dispatch({
			action: PlanStore.ACTION_FIND,
			data: {
				parentId: planMacroId,

			},
			opts: {
				wait: true
			}
		});

		StructureStore.dispatch({
			action: StructureStore.ACTION_LIST_FAVORITES,
			data: {
				macroId: planMacroId
			},
			opts: {
				wait: true
			}
		});
	},

	expandRoot(nodeProps, nodeLevel) {
        if(nodeLevel == 0) {
        	// Nível raiz, subplano.
            StructureStore.dispatch({
                action: StructureStore.ACTION_RETRIEVE_LEVEL_INSTANCE,
                data: {
                    levelId: nodeProps.model.structure.levels[0].id,
                    planId: nodeProps.id,
                    parentId: 0
                },
                opts: {
                	node: nodeProps,
                	level: nodeLevel
                }
            });
        } else {
        	// Níveis da estrutura, Level.
        	StructureStore.dispatch({
                action: StructureStore.ACTION_RETRIEVE_LEVEL_INSTANCE,
                data: {
                    levelId: nodeProps.model.level.sequence+2,                
                    planId: nodeProps.model.plan.id,
                    parentId: nodeProps.id
                },
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

	expandDocumentRoot(nodeProps){
		nodeProps.expanded = true;
		this.forceUpdate();
	},

	resultSearch() {
		this.setState({
            hiddenResultSearch:false
        });
        this.refs.term.value = "";
	},


	displayResult () {
		this.setState({
            hiddenResultSearch:true
        });
	},

	

	treeSearch() {
		this.displayResult()
		PlanStore.dispatch({
			action: PlanStore.ACTION_FIND_TERMS,
			data: {
				parentId: this.props.plan.get("id"),
				terms:this.refs.term.value,
				page:1,
				limit:10
			},
			opts: {
				wait: true
			}
		});
	},

	searchFilter() {
		this.setState({
            hiddenSearch:!this.state.hiddenSearch
        });
	},

	retrieveFilledSections(){
	    var me = this;
	    DocumentStore.on("filledSectionsRetrieved", (model) => { 
		    var tree = [];
	        var sections = model;
	        var empty = true;
		    tree = sections.map((section,idx) => {
				if(section.filled == 1){
					empty = false;
				}
	       		return section;
	    	});
	    	if(this.isMounted()){
			    me.setState({
					rootSections: tree
				});
			}
			if(me.isMounted()) {
			    if (empty) {
			    	this.context.toastr.addAlertError("Nenhum campo do documento foi preenchido.");
			    } else {
					Modal.exportDocument(
						"Confirmação de exportação",
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
								elemError.innerHTML = "Erro ao exportar documento. Preencha os campos autor e título, e selecione pelo menos uma seção.";
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
			}
		});
	       
	},
	selectAll(){
		var i;
		for(i=0; i<this.state.rootSections.length; i++){
			if(document.getElementById("checkbox-"+i).disabled == false){
				document.getElementById("checkbox-"+i).checked = document.getElementById("selectall").checked;					
			}
		}
	},
	verifySelectAll() {
		var i;
		var selectedAll = true;
		for(i=0; i<this.state.rootSections.length; i++){
			if(document.getElementById("checkbox-"+i).disabled == false && !document.getElementById("checkbox-"+i).checked){	
				selectedAll = false;
			}
		}
		document.getElementById("selectall").checked = selectedAll;
	},
	renderRecords() {
		return (<div className="row">
			<div key="rootSection-selectall">
					<div className="checkbox marginLeft5 col-md-10" >
						<label name="labelSection-selectall" id="labelSection-selectall">
							<input type="checkbox" value="selectall" id="selectall" onChange={this.selectAll}></input>
							Selecionar todos
						</label>
					</div>
			</div>
			{this.state.rootSections.map((rootSection, idx) => {
				return (rootSection.filled ?
				<div key={"rootSection-filled"+idx}>
					<div className="checkbox marginLeft5 col-md-10" >
						<label name={"labelSection-filled"+idx} id={"labelSection-filled"+idx}>
							<input type="checkbox" value={rootSection.id} id={"checkbox-"+idx} onClick={this.verifySelectAll}></input>
							{rootSection.name}
						</label>
					</div>
					
				</div>:
				<div key={"rootSection-"+idx}>
					<div className="checkbox disabled marginLeft5 col-md-10">
						<label className="disabled" name={"labelSection-"+idx} id={"labelSection-"+idx} >
							<input type="checkbox" value={rootSection.id} id={"checkbox-"+idx} disabled></input>
							<span  data-placement="right" title="Seção não preenchida.">{rootSection.name}</span>
						</label>
					</div>

				</div>

				);
			})}
			<br/><br/>
			</div>);
	},

	exportDocument(evt) {
		evt.preventDefault();
		DocumentStore.dispatch({
            action: DocumentStore.ACTION_RETRIEVE_FILLED_SECTIONS,
            data: {
                id: this.props.plan.get("id")
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


	render() {
		return (
		<div className="fpdi-tabs">
			<ul className="fpdi-tabs-nav marginLeft0" role="tablist">
			
				{this.props.plan.get('documented') ?
				 <Link
					role="tab"
					to={"/plan/"+this.props.plan.get("id")+"/document/"}
					title="Documento"
					activeClassName="active"
					className="tabTreePanel">
						Documento do PDI
				</Link> : undefined}
				<Link
					role="tab"
					to={"/plan/"+this.props.plan.get("id")+"/details"}
					title="Planos de metas"					
					activeClassName="active"
					className="tabTreePanel">
						Planos de metas
				</Link>
					
			</ul>
			{this.context.router.isActive("/plan/"+this.props.plan.get("id")+"/details") ?
			<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0 plan-search-border">
				{this.state.subplans.length > 0 ? 
					<div className="marginBottom10 inner-addon right-addon right-addonPesquisa plan-search-border">
						<i className="mdiClose mdi mdi-close pointer" onClick={this.resultSearch} title="Limpar"> </i>
	    				<input type="text" className="form-control-busca" ref="term" onKeyDown={this.onKeyDown}/>
	    				<i className="mdiBsc mdi mdi-chevron-down pointer" onClick={this.searchFilter} title="Pesquisa avançada"> </i>
	    				<i id="searchIcon" className="mdiIconPesquisa mdiBsc  mdi mdi-magnify pointer" onClick={this.treeSearch} title="Pesquisar"> </i>
					</div>
				: ""}

				{this.state.hiddenResultSearch ?
					<SearchResult 
						resultSearch = {this.state.resultSearch}
						planId = {this.props.plan.get("id")}
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
					</div>
				}

				{this.state.hiddenSearch ?
					<div className = "container Pesquisa-Avancada">
						<LevelSearch 
 							searchText= {this.refs.term.value}
  							subplans= {this.state.subplans}
  							plan= {this.props.plan.get("id")}
 							submit={this.treeSearch}
 							hiddenSearch = {this.searchFilter}
 							displayResult = {this.displayResult}
 							
  						/> 
  					</div> : ""

				}


			</div>
				:
				<div className="fpdi-tabs-content fpdi-plan-tree marginLeft0" style={{borderLeft: "none"}}>
					<TreeView tree={this.state.documentTree} />
					<hr className="divider"></hr>
					{(this.context.roles.MANAGER || _.contains(this.context.permissions, 
					    PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ? 
						<a className="btn btn-sm btn-primary center" onClick={this.exportDocument}>
							<span /*className="mdi mdi-export"*/
							/> Exportar documento
						</a> : ""
					}
				</div>
			}
			<div className="fpdi-tabs-fill">
			</div>
		</div>);
	}
});
