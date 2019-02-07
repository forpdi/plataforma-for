import _ from 'underscore';
import React from "react";
import {Link, hashHistory} from "react-router";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import Form from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Validation from 'forpdi/jsx_forrisco/core/util/Validation.jsx';
import AttributeInput from 'forpdi/jsx/planning/widget/attributeForm/AttributeInput.jsx';
import FieldItemInput from  'forpdi/jsx_forrisco/planning/view/item/FieldItemInput.jsx'

var VerticalForm = Form.VerticalForm;
var Validate = Validation.validate;


export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		tabPanel: React.PropTypes.object,
		policy: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			loading: true,
			itemModel: null,
			policyModel: null,
			risklevelModel: null,
			fields:[],

			vizualization: false,
			tabPath: this.props.location.pathname,
			undeletable: false,

			//editar
			cancelLabel: "Cancelar",
			submitLabel: "Salvar",
			newField: false,
			newFieldType: null,
			length: 0,
			titulo: null,
			info: false
		};
	},
	getFields() {
		var fields = [];

		if(typeof this.state.fields === "undefined" || this.state.fields == null){
			fields.push({
				name: "description",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "",
				maxLength: 1000,
				label: Messages.getEditable("label.description","fpdi-nav-label"),
				value: this.state.itemModel ? this.state.itemModel.attributes.name : null,
				edit:false
			});
		}else{
			this.state.fields.map((fielditem, index) => {
				fields.push(fielditem)
			})
		}

		return fields;
	},
	getField() {
		var fields;
		fields= {
			name: "description",
			type: AttributeTypes.TEXT_FIELD,
			placeholder: "Título do item",
			maxLength: 100,
			label: "Título",
			value: this.state.itemModel != null ? this.state.itemModel.attributes.name : null,
			edit:false
		}
		return fields;
	},
	getMatrixValue(matrix, line, column) {

		var result=""

		for(var i=0; i<matrix.length;i++){
			if(matrix[i][1]==line){
				if(matrix[i][2]==column){
					if(matrix[i][2]==0 ){
					return <div style={{"text-align":"right"}}>{matrix[i][0]}&nbsp;&nbsp;&nbsp;&nbsp;</div>
					}else if(matrix[i][1]==this.state.policyModel.data.nline){
						return <div style={{"text-align":"-webkit-center",margin: "5px"}} className="">{/*&emsp;&emsp;&emsp;&nbsp;*/}{matrix[i][0]}</div>
					}else{

						var current_color=0;
						var cor=""
						if(this.state.risklevelModel != null){
							for(var k=0; k< this.state.risklevelModel.data.length;k++){
								if(this.state.risklevelModel.data[k]['level']==matrix[i][0]){
									current_color=this.state.risklevelModel.data[k]['color']
								}
							}
						}

						switch(current_color) {
							case 0: cor="Vermelho"; break;
							case 1: cor="Marron"; break;
							case 2: cor="Amarelo"; break;
							case 3: cor="Laranja"; break;
							case 4: cor="Verde"; break;
							case 5: cor="Azul"; break;
							default: cor="Vermelho";
						}

						return <div  className={"Cor "+cor} >{matrix[i][0]}</div>

					}
				}
			}
		}
		return ""
	},

	getMatrix() {

		if(this.state.policyModel ==null){
			return
		}


		var fields = [];
		if(typeof this.state.fields === "undefined" || this.state.fields == null){
			fields.push({
				name: "description",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "",
				maxLength: 9000,
				label: Messages.getEditable("label.description","fpdi-nav-label"),
				value: this.state.itemModel ? this.state.itemModel.get("description") : null,
				edit:false
			});
		}else{
			fields=this.state.fields
		}

		var aux=this.state.policyModel.data.matrix.split(/;/)
		var matrix=[]

		for(var i=0; i< aux.length;i++){
			matrix[i]= new Array(3)
			matrix[i][0]=aux[i].split(/\[.*\]/)[1]
			matrix[i][1]=aux[i].match(/\[.*\]/)[0].substring(1,aux[i].match(/\[.*\]/)[0].length-1).split(/,/)[0]
			matrix[i][2]=aux[i].match(/\[.*\]/)[0].substring(1,aux[i].match(/\[.*\]/)[0].length-1).split(/,/)[1]
		}

		var table=[]
		for (var i=0; i<=this.state.policyModel.data.nline;i++){
			var children=[]
			for (var j=0; j<=this.state.policyModel.data.ncolumn;j++){
				children.push(<td key={j}>{this.getMatrixValue(matrix,i,j)} </td>)
			}
			table.push(<tr key={i} >{children}</tr>)
		}

	return (
		<div>
			<label htmlFor={this.state.fieldId} className="fpdi-text-label">
				{"MATRIZ DE RISCO"}
			</label>
			<br/>
			<br/>
			<table style={{width: "min-content"}}>
			<th>
				<tr>
					<td style={{position: "relative", left: "30px"}}>
						{table}
					</td>
				</tr>
				<tr>
					<th style={{bottom: ((this.state.policyModel.data.nline-2)*20+80)+"px" , right: "50px", position: "relative"}} >
						<div style={{width: "115px" }} className="vertical-text">PROBABILIDADE</div>
					</th>
				</tr>
				<tr>
					<div style={{"text-align":"-webkit-center", position: "relative", left: "75px"}}>IMPACTO</div>
				</tr>
				</th>
			</table>

		</div>
		);
	},


	/*
	policy-Model
	policy-delete

	risklevel-Model

	item-Model
	item-new
	item-update
	item-delete

	itemfields-Model
	*/
	componentDidMount() {
		var me = this;
		PolicyStore.on("findpolicy", (model) => {

			if(!model.deleted){
				me.setState({
					policyModel: model,

				});
				me.forceUpdate();
				_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, model.data.name);});
			}else{
				me.setState({
					policyModel: null,
				});
			}
		}, me);

		PolicyStore.on("retrieverisklevel", (model) => {
				me.setState({
					risklevelModel: model
				});
				me.forceUpdate();
				//_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, model.get("name"));});
		}, me);

		ItemStore.on("retrieveItem", (model) => {
			if(!model.attributes.deleted){
				me.setState({
					itemModel: model,
					titulo: model.get("name"),
					vizualization: true,
					loading:false,
					fields: this.getFields()
				});

				if(model.get("name")=="Informações gerais"){
					me.setState({
						info:true
					})
				}

				me.forceUpdate();
				//_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, model.get("name"));});
			}else{
				me.setState({
					//model: null,
					titulo: null,
					vizualization: true
				});
				me.context.tabPanel.removeTabByPath(me.state.tabPath);
				//me.context.router.push("/plan/"+this.props.params.id+"/details/overview");
			}
		}, me);

		ItemStore.on("retrieveField", (model) => {
			if(model.attributes != null){
				var fields = [];
				for (var i in model.attributes) {

					if(!model.attributes[i].deleted){
						fields.push({
							name: model.attributes[i].name+"-"+(i),
							value: model.attributes[i].name,
							label: model.attributes[i].name,
							description: model.attributes[i].description,
							isText:  model.attributes[i].isText,
							type: model.attributes[i].isText ? AttributeTypes.TEXT_AREA_FIELD : AttributeTypes.ATTACHMENT_FIELD,
							edit: false,
							fileLink: model.attributes[i].fileLink
						});
					}
				}

				me.setState({
					fields: fields
				})
			}
		}, me);

		ItemStore.on("itemUpdated", (model) => {
			if(model !=null){

				var mod = this.state.itemModel;
				mod.attributes.name = model.data.name;
				mod.attributes.description = model.data.description;
				mod.attributes.policy = model.data.policy;

				for (var i in model.attributes) {
					if(!model.attributes[i].deleted){
						var fields = [];

						fields.push({
							name: model.attributes[i].name,
							description: model.attributes[i].description,
							isText:  model.attributes[i].isText,
							type: model.attributes[i].isText? AttributeTypes.TEXT_AREA_FIELD : AttributeTypes.ATTACHMENT_FIELD,
							value: model.attributes[i].description,
							label: model.attributes[i].name,
							edit: false,
							fileLink: model.attributes[i].fileLink
						});
					}
				}

				me.setState({
					fields: fields,
					itemModel: mod,
					titulo: model.data.name,
					vizualization: true,
					fields: me.getFields()
				});
				me.forceUpdate();

				me.context.toastr.addAlertSuccess(Messages.get("label.successUpdatedItem"));
			}else{
				me.context.toastr.addAlertError(Messages.get("label.errorUpdatedItem"));
			}
		}, me);

		ItemStore.on("newItem", (model) => {

			if(model !=null){
				this.state.fields.map((fielditem, index) => {
					ItemStore.dispatch({
						action: ItemStore.ACTION_CREATE_FIELD,
						data:{
							item: model.data,
							name: fielditem.value,
							isText: fielditem.type == AttributeTypes.TEXT_AREA_FIELD ? true : false,
							description: fielditem.description,
							fileLink:  fielditem.fileLink
						}
					})
				})
				me.context.toastr.addAlertSuccess(Messages.get("label.successNewItem"));
				this.context.router.push("/forrisco/policy/"+this.state.policyModel.data.id+"/item/"+model.data.id);
			}else{
				me.context.toastr.addAlertError(Messages.get("label.errorNewItem"));
			}
		}, me);

		PolicyStore.on("policyDeleted", (model) => {
			if(model.success){
				this.context.router.push("/forrisco/home");
			}else{
				if(model.message != null){
					this.context.toastr.addAlertError(model.message);
				}
			}

		})

		ItemStore.on("itemDeleted", (model) => {
			this.context.router.push("forrisco/policy/"+this.context.policy.id+"/item/overview");
		})

		me.refreshData(me.props, me.context);
	},


	componentWillUnmount() {
		ItemStore.off(null, null, this);
		PolicyStore.off(null, null, this);
	},

	componentWillReceiveProps(newProps, newContext) {

		if (newProps.params.itemId != this.props.params.itemId) {
			this.setState({
				loading: true,
				fields: [],
				tabPath: newProps.location.pathname,
				vizualization: false,

				newField: false,
				newFieldType: null,
				itemModel: null,
				policyModel: null,
				description: null,
				fileData: null,
				titulo: null,
				info: false
			});

			this.refreshData(newProps, newContext);

		}
	},
	UpdateLoading(bool){
		this.setState({
			loading: bool,
		});
	},

	refreshData(props, context) {

		PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_POLICY,
			data: props.params.policyId
		});

		if (props.params.policyId && props.params.itemId) {
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
				data: props.params.policyId
			});

			if(props.params.itemId){
				ItemStore.dispatch({
					action: ItemStore.ACTION_RETRIEVE_ITEM,
					data: props.params.itemId
				});
				ItemStore.dispatch({
					action: ItemStore.ACTION_RETRIEVE_FIELD,
					data: props.params.itemId
				});
			}else{
				this.setState({
					titulo: Messages.getEditable("label.newItem","fpdi-nav-label"),
					loading: false
				});
			}
		} else {
			this.setState({
				titulo: Messages.getEditable("label.newItem","fpdi-nav-label"),
				loading: false,
			});
		}
	},
	refreshCancel () {
		Modal.hide();
	},
	onCancel() {
		if (this.state.itemModel) {
			this.setState({
				vizualization: true,
				//fields: this.getFields()
			});
		} else {
			this.context.tabPanel.removeTabByPath(this.props.location.pathname);
		}
	},
	changeVizualization() {
		this.setState({
			vizualization: false,
			//fields: this.getFields()
		});
	},
	deleteItem() {
		var me = this;
		if (me.state.itemModel != null) {

			if(this.state.info){
				var msg = "Você tem certeza que deseja excluir essa política?"
				Modal.confirmCustom(() => {
					Modal.hide();
					PolicyStore.dispatch({
						action: PolicyStore.ACTION_DELETE,
						data: me.state.policyModel.data.id
					});
				},msg,me.refreshCancel);

			}else{
				var msg = "Você tem certeza que deseja excluir esse item?"
				Modal.confirmCustom(() => {
					Modal.hide();
					ItemStore.dispatch({
						action: ItemStore.ACTION_DELETE,
						data: me.state.itemModel.id
					});
				},msg,me.refreshCancel);
			}
			/*var msg = Messages.get("label.deleteConfirmation") + " " +me.state.model.attributes.name+"?";
			Modal.confirmCancelCustom(() => {
				Modal.hide();
				PlanStore.dispatch({
					action: PlanStore.ACTION_DELETE_PLAN,
					data: me.state.model
				});
			},msg,()=>{Modal.hide();me.refreshCancel;});*/
		}
	},tweakNewField() {
		this.setState({
			newField: !this.state.newField,
			newFieldType: null
		});
	},
	reset(){
		this.setState({
			newField: false,
			newFieldType: null,
			description: null,
			fileData: null
		});
	},
	getLength(){
		return this.state.length++
	},
	editFunc(id,bool){
		this.state.fields.map( (fielditem, i) => {
			if (id==i){
				fielditem.edit=bool
			}
		})
		this.setState({
			fields: this.state.fields
		})
	},
	deleteFunc(id){
		Modal.confirmCustom(() => {
			Modal.hide();
			this.state.fields.map( (fielditem, index) => {
				if (id==index){
					this.state.fields.splice(index,1)
				}
			})
			this.setState({
				fields: this.state.fields
			})
		}, Messages.get("label.msg.deleteField"),()=>{Modal.hide()});
	},
	setItem(index,item){
		this.state.fields.map( (fielditem, i) => {
			if (index==i){
				fielditem.name= item.name,
				fielditem.type= item.type,
				fielditem.label= item.label,
				fielditem.value= item.value,
				fielditem.description= item.description,
				fielditem.isText= item.isText,
				fielditem.edit=false
			}
		})
		this.setState({
			fields: this.state.fields
		})
	},
	cancelWrapper(evt) {
		for (var i = 0; i < this.getField().length; i++) {
			if (this.refs[this.getField().name])
				this.refs[this.getField().name].refs.formAlertError.innerHTML = "";
		}
		evt.preventDefault();
		if (typeof this.props.onCancel === 'function') {
			this.props.onCancel();
		} else {
			hashHistory.goBack();
		}
	},








	renderArchivePolicy() {
		return (
			<ul className="dropdown-menu">
				<li>
					<a onClick={this.deleteLevelAttribute}>
						<span className="mdi mdi-pencil disabledIcon" title={Messages.get("label.title.unableArchivedPlan")}>
							<span id="menu-levels">	{Messages.getEditable("label.title.unableArchivedPlan","fpdi-nav-label")} </span>
						</span>
					</a>
				</li>
			</ul>
		);

	},
	renderUnarchivePolicy() {
		if(this.state.info){
			return (<ul id="level-menu" className="dropdown-menu">
			<li>
				<Link
					to={"/forrisco/policy/"+this.props.params.policyId+"/edit"}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPolicy")}>
						<span id="menu-levels"> {"Editar Política"/*Messages.getEditable("label.title.editPolicy","fpdi-nav-label")*/} </span>
						</span>
				</Link>
			</li>
			<li>
				<Link
					to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}//this.state.model.get("id")}
					onClick={this.deleteItem}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
						<span id="menu-levels"> {"Deletar Política" /*Messages.getEditable("label.deletePolicy","fpdi-nav-label")*/} </span>
					</span>
				</Link>
			</li>
			</ul>);

		}else{
			return (
				<ul id="level-menu" className="dropdown-menu">
					<li>
						<Link
							to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}
							onClick={this.changeVizualization}>
							<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editInformation")}>
								<span id="menu-levels"> {Messages.getEditable("label.title.editInformation","fpdi-nav-label")} </span>
							</span>
						</Link>
					</li>
					{this.state.undeletable ?
					<li>
						<Link
							to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}>
							<span className="mdi mdi-delete disabledIcon cursorPointer" title={Messages.get("label.notDeletedHasChild")}>
								<span id="menu-levels"> {Messages.getEditable("label.deleteItem","fpdi-nav-label")}</span>
							</span>
						</Link>
					</li>
					:
					<li>
						<Link
							to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}//this.state.model.get("id")}
							onClick={this.deleteItem}>
							<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deleteItem")}>
								<span id="menu-levels"> {Messages.getEditable("label.deleteItem","fpdi-nav-label")} </span>
							</span>
						</Link>
					</li>
					}
				</ul>
			);
		}
	},
	renderBreadcrumb() {
		return(
			<div>
				<span>
					<Link className="fpdi-breadcrumb fpdi-breadcrumbDivisor"
						to={'/forrisco/policy/'+this.context.policy.id}
						title={this.context.policy.name}>{this.context.policy.name.length > 15 ? this.context.policy.name.substring(0, 15)+"..." : this.context.policy.name.substring(0, 15)
					}</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
				</span>
				<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
					{this.state.titulo > 15 ? this.state.titulo.substring(0, 15)+"..." : this.state.titulo.substring(0, 15)}
				</span>
			</div>
		);
	},

	onSubmit(event) {
		event && event.preventDefault();

		if(this.state.itemModel){
			/*var msg = "";
			Modal.confirmCustom(() => {
				Modal.hide();*/


				ItemStore.dispatch({
					action: ItemStore.ACTION_CUSTOM_UPDATE,
					data: {
						id: this.state.itemModel.attributes.id,
						name: this.refs.newItemForm['field-description'].value,//this.state.itemModel.attributes.name,
						policy: this.state.policyModel,
						fieldItem: this.state.fields
					}
				});
			//},msg,this.refreshCancel);
		} else {

			var name = this.refs.newItemForm['field-description'].value

			var validation = Validate.validationNewItem(this.refs.newItemForm);
			if (validation.errorField) {
				this.context.toastr.addAlertError(Messages.get("label.error.form"));
			} else {
				ItemStore.dispatch({
					action: ItemStore.ACTION_NEW_ITEM,
					data: { name: validation.titulo.s,
							description: "",
							policy: this.state.policyModel.data
					}
				});
			}


		}
	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}

		var showButtons = !this.state.vizualization;

		if(this.state.vizualization){

			return <div>
				{this.state.itemModel ? this.renderBreadcrumb() : ""}

				<div className="fpdi-card fpdi-card-full floatLeft">

				<h1>
					{(this.state.info && this.state.policyModel) ? this.state.policyModel.data.name : this.state.itemModel.attributes.name}
					{this.state.model && (this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_PLAN_PERMISSION)) || true ?
						(<span className="dropdown">
							<a
								className="dropdown-toggle"
								data-toggle="dropdown"
								aria-haspopup="true"
								aria-expanded="true"
								title={Messages.get("label.actions")}
								>
								<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
								<span className="mdi mdi-chevron-down" />
							</a>
							{this.context.policy.archived ? this.renderArchivePolicy() : this.renderUnarchivePolicy()}
						</span>
						):""}
				</h1>

				{this.getFields().map((fielditem, index) => {
					if(fielditem.type ==  AttributeTypes.TEXT_AREA_FIELD){
						return (<div><VerticalForm
							vizualization={this.state.vizualization}
							onCancel={this.onCancel}
							onSubmit={this.onSubmit}
							fields={[fielditem]}
							submitLabel={Messages.get("label.submitLabel")}
							//store={ItemStore}
							//ref='planRegisterForm'
						/></div>)
					}else{
						return (
							<div>
							<label className="fpdi-text-label">{fielditem.value}</label>
							<div className="panel panel-default">
								<table className="budget-field-table table">
									<tbody>
										<tr>
											<td className="fdpi-table-cell">
												<a target="_blank" rel="noopener noreferrer" href={fielditem.fileLink}>
													{fielditem.description}</a>
											</td>
										</tr>
									</tbody>
								</table>
								</div>
							</div>
						)
					}
					})
				}
			<br/>
			{this.state.info ?
			<div>
				<label htmlFor={this.state.fieldId} className="fpdi-text-label">
					{"DESCRIÇÃO"}
				</label>
				<br/>
				{this.state.itemModel.attributes.description}
				<br/><br/>
			</div>: ""}

			{this.state.info ? this.getMatrix(): ""}
			</div>
			</div>;
		}else{

			//editar
			return (
				<div>

				<form onSubmit={this.onSubmit} ref="newItemForm">

				{this.state.itemModel ? this.renderBreadcrumb() : ""}

				<div className="fpdi-card fpdi-card-full floatLeft">

					<h1>

						{(this.state.titulo)}
						{/*this.state.itemModel && (this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_PLAN_PERMISSION))  ?
							(<span className="dropdown">
								<a
									className="dropdown-toggle"
									data-toggle="dropdown"
									aria-haspopup="true"
									aria-expanded="true"
									title={Messages.get("label.actions")}
									>
									<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
									<span className="mdi mdi-chevron-down" />
								</a>
								{this.context.policy.attributes.archived ? this.renderArchivePolicy() : this.renderArchivePolicy()}
							</span>
						):""*/}
					</h1>

					{
						//título
					}

					<AttributeInput
						fieldDef={this.getField()}
						undeletable={false}
						vizualization={this.props.vizualization}
						//ref="formAlertErrorTitulo"
						//ref={this.getField().name}
						//key={this.getField().name}
						//deleteFunc={this.props.deleteFunc}
						//editFunc={this.props.editFunc}
						//alterable={this.props.alterable}
						//isDocument={this.props.isDocument}
						//onClick={this.props.onClick}
						//onChage={this.props.onChage}
					/>


					{
						//campos
					}

					{this.state.fields && (this.state.fields.length > 0) ?
					this.state.fields.map((fielditem, index) => {
						if(fielditem.type ==  AttributeTypes.TEXT_AREA_FIELD){
							//fielditem.name=fielditem.name
							//fielditem.value=fielditem.label
							fielditem.isText=true;
							return (
								<div>
								<FieldItemInput
									vizualization={!this.props.vizualization}
									deleteFunc={this.deleteFunc}
									editFunc={this.editFunc}
									setItem={this.setItem}
									fields={this.state.fields}
									reset={this.reset}
									field={fielditem}
									index={index}
									getLength={this.getLength}
									/>
								</div>
							)
						}else if (fielditem.type ==  AttributeTypes.ATTACHMENT_FIELD){
							fielditem.isText=false;
							return (<div>
								<FieldItemInput
									vizualization={!this.props.vizualization}
									deleteFunc={this.deleteFunc}
									editFunc={this.editFunc}
									setItem={this.setItem}
									fields={this.state.fields}
									reset={this.reset}
									field={fielditem}
									index={index}
									getLength={this.getLength}
									/>
								</div>)
						}
					}):""}


					{
						//Adicioonar novo campo
					}

					{this.state.newField ?
						<FieldItemInput
							//key={this.getLength()}
							vizualization={this.props.vizualization}
							deleteFunc={this.deleteFunc}
							editFunc={this.editFunc}
							setItem={this.setItem}
							fields={this.state.fields}
							reset={this.reset}
							getLength={this.getLength}
						/>
					:
					(((this.context.roles.MANAGER || _.contains(this.context.permissions,
					PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && this.props.params.policyId) ? // && !this.state.model.preTextSection) ?
					<button onClick={this.tweakNewField} id="addIconDocument" className="btn btn-sm btn-neutral marginTop20">
						<span className="mdi mdi-plus" /> {Messages.get("label.addNewField")}
					</button>
					:"")}


					<br/><br/><br/>
					{showButtons ?
					(!!this.props.blockButtons ?
						(<div className="form-group">
							<button type="submit" className="btn btn-success btn-block">{this.state.submitLabel}</button>
							{!this.props.hideCanel ? (!this.props.cancelUrl ?
								<button className="btn btn-default  btn-block" onClick={this.cancelWrapper}>{this.state.cancelLabel}</button>
								:(
									<Link to={this.props.cancelUrl} className="btn btn-default btn-block">{this.state.cancelLabel}</Link>
								)):""}
						</div>)
					:
						(<div className="form-group text-left">
							<button type="submit" className="btn btn-sm btn-success">{this.state.submitLabel}</button>
							{!this.props.hideCanel ? (!this.props.cancelUrl ?
								<button className="btn btn-sm btn-default" onClick={this.cancelWrapper}>{this.state.cancelLabel}</button>
								:
								<Link className="btn btn-sm btn-default" to={this.props.cancelUrl}>{this.state.cancelLabel}</Link>
							):""}
						</div>)
					)
				: ""}
				</div>
			</form>
		</div>);

		}
	}

});
