import _ from 'underscore';
import moment from 'moment';
import React from "react";
import {Link} from 'react-router';
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import Form from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Modal from "forpdi/jsx/core/widget/Modal.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import Validation from 'forpdi/jsx/core/util/Validation.jsx';

var VerticalForm = Form.VerticalForm;
var validation = Validation.validate;


export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		policy: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired
	},
	getInitialState() {
		return {
			loading: true,
			modelId: null,
			itemModel: null,
			policyModel: null,
			risklevelModel: null,
			fields: [],
			structures: null,
			structureError:false,
			vizualization: false,
			tabPath: this.props.location.pathname,
			undeletable: false,
		};
	},
	getInfo() {
		var fields = [];
		if(typeof this.state.fields === "undefined" || this.state.fields == null){

			fields.push({
				name: "description",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "",
				maxLength: 9000,
				label: Messages.getEditable("label.description","fpdi-nav-label"),
				value: this.state.itemModel ? this.state.itemModel.get("description") : null
			});

		}else{
			fields=this.state.fields
		}

		return fields;
	},

	getMatrixValue(matrix, line, column) {

		var result=""

		for(var i=0; i<matrix.length;i++){
			if(matrix[i][1]==line){
				if(matrix[i][2]==column){
					if(matrix[i][2]==0 ){
						return <div className="">{matrix[i][0]}&nbsp;&nbsp;&nbsp;&nbsp;</div>
					}else if(matrix[i][1]==this.state.policyModel.attributes.nline){
						return <div className="">&emsp;&emsp;&emsp;&nbsp;{matrix[i][0]}</div>
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

						return <div style={{padding:"10px 50px"}} className={"form-control " + cor} >{matrix[i][0]}</div>
					}
				}
			}
		}
		return ""
	},

	getMatrix() {
		var fields = [];
		if(typeof this.state.fields === "undefined" || this.state.fields == null){

			fields.push({
				name: "description",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "",
				maxLength: 9000,
				label: Messages.getEditable("label.description","fpdi-nav-label"),
				value: this.state.itemModel ? this.state.itemModel.get("description") : null
			});

		}else{
			//fields.push({}
			//me.updateLoadingState(false);
			fields=this.state.fields
		}

		var aux=this.state.policyModel.attributes.matrix.split(/;/)
		var matrix=[]

		for(var i=0; i< aux.length;i++){
			matrix[i]= new Array(3)
			matrix[i][0]=aux[i].split(/\[.*\]/)[1]
			matrix[i][1]=aux[i].match(/\[.*\]/)[0].substring(1,aux[i].match(/\[.*\]/)[0].length-1).split(/,/)[0]
			matrix[i][2]=aux[i].match(/\[.*\]/)[0].substring(1,aux[i].match(/\[.*\]/)[0].length-1).split(/,/)[1]
		}

		var table=[]
		for (var i=0; i<=this.state.policyModel.attributes.nline;i++){
			var children=[]
			for (var j=0; j<=this.state.policyModel.attributes.ncolumn;j++){
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
					<th style={{bottom: (this.state.policyModel.attributes.ncolumn*33 -30)+"px" , right: "50px", position: "relative"}} >
						<div style={{width: "115px" }} className="vertical-text">PROBABILIDADE</div>
					</th>
				</tr>
				<tr>
					<th>
						<div style={{left: (this.state.policyModel.attributes.nline*15)+"px", position: "relative"}}>IMPACTO</div>
					</th>
				</tr>
				</th>
			</table>

		</div>
		);
	},

	updateLoadingState(showName) {
		this.setState({
			fields: this.getInfo(),
			loading:
				(this.props.params.modelId && !this.state.model)
		});
	},
	componentDidMount() {
		var me = this;
		PolicyStore.on("retrieve", (model) => {
			if(!model.attributes.deleted){
				me.setState({
					policyModel: model
				});
				me.forceUpdate();
				_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, model.get("name"));});
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
					modelId: model.get("id"),
					title: model.get("name"),
					vizualization: true,
					fields: this.getInfo()
				});
				me.forceUpdate();
				_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, model.get("name"));});
			}else{
				me.setState({
					model: null,
					modelId: null,
					title: null,
					vizualization: true
				});
				me.context.tabPanel.removeTabByPath(me.state.tabPath);
				me.context.router.push("/plan/"+this.props.params.id+"/details/overview");
			}
			me.updateLoadingState(false);
		}, me);

		ItemStore.on("retrieveField", (model) => {

			var fields = [];

			for (var i in model.attributes) {

				if(!model.attributes[i].deleted){
					fields.push({
						name: model.attributes[i].name,
						description: model.attributes[i].description,
						type: model.attributes[i].isText? AttributeTypes.TEXT_AREA_FIELD : AttributeTypes.ATTACHMENT_FIELD,
						value: model.attributes[i].description,
						label: model.attributes[i].name
					});
				}
			}

			me.setState({
				fields: fields
			})

		}, me);

		ItemStore.on("itemUpdated", (model) => {
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
						type: model.attributes[i].isText? AttributeTypes.TEXT_AREA_FIELD : AttributeTypes.ATTACHMENT_FIELD,
						value: model.attributes[i].description,
						label: model.attributes[i].name
					});
				}
			}

			me.setState({
				fields: fields
			})

			me.setState({
				itemModel: mod,
				title: model.data.name,
				vizualization: true,
				fields: me.getInfo()
			});
			me.context.toastr.addAlertSuccess(Messages.get("label.successUpdatedItem"));
		}, me);

		PolicyStore.on("policyDeleted", (model) => {
			this.context.router.push("/forrisco/home");
		})

		ItemStore.on("itemDeleted", (model) => {
			this.context.router.push("forrisco/policy/"+this.context.policy.get('id')+"/item/overview");
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
				modelId: null,
				model: null,
				fields: null,
				structureError:false,
				tabPath: newProps.location.pathname,
				vizualization: true,
				newField: false
			});

			this.refreshData(newProps, newContext);
		}
	},
	refreshData(props, context) {

		if (props.params.policyId && props.params.policyId !="new") {
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE,
				data: props.params.policyId
			});

			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
				data: props.params.policyId
			});

			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_ITEM,
				data: props.params.itemId
			});
			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_FIELD,
				data: props.params.itemId
			});

		} /*else {
			this.setState({
				title: Messages.getEditable("label.newGoalPlan","fpdi-nav-label"),
				vizualization: false
			});

			this.updateLoadingState(true);
		}*/
	},

	onChangeStructure(){
		this.setState({
			structureId: this.refs['planRegisterForm'].refs["structure"].refs["structure"].value,
		});
	},

	refreshCancel () {
		Modal.hide();
	},

	onSubmit(data) {
		var me = this;

		if(this.state.structureError){
			this.context.toastr.addAlertError(Messages.get("label.msg.beforeGoalPlan"));
			return;
		}

		/*var validate = validation.validationPlanRegister(data, this.refs.planRegisterForm, this.context.planMacro);
		if(validate.boolMsg){
			this.context.toastr.addAlertError(validate.msg);
			return;
		}*/

		data.parent = {
			id: me.props.params.id
		};
		data.structure = {
			id: (this.state.structureId ? this.state.structureId : 1)
		};
		if (this.state.modelId) {
			var msg = "";

			Modal.confirmCustom(() => {

				Modal.hide();
				ItemStore.dispatch({
					action: ItemStore.ACTION_CUSTOM_UPDATE,
					data: {
						id: this.state.itemModel.attributes.id,
						name: this.state.itemModel.attributes.name,
						deleted: this.state.itemModel.attributes.deleted,
						description: data.description,
						policy: me.props.params.policyModel
					}
				});
			},msg,this.refreshCancel);

		} else {
			ItemStore.dispatch({
				action: ItemStore.ACTION_CREATE,
				data: data
			});
		}
	},

	onCancel() {
		if (this.state.modelId) {
			this.setState({
				vizualization: true,
				fields: this.getInfo()
			});
		} else {
			this.context.tabPanel.removeTabByPath(this.props.location.pathname);
		}
	},

	changeVizualization() {
		this.setState({
			vizualization: false,
			fields: this.getInfo()
		});
	},

	deleteItem() {
		var me = this;
		if (me.state.itemModel != null) {

			if(this.state.itemModel.get("name") =="Informações gerais"){
				var msg = "Você tem certeza que deseja excluir essa política?"
				Modal.confirmCustom(() => {
					Modal.hide();
					PolicyStore.dispatch({
						action: PolicyStore.ACTION_DELETE,
						data: me.state.policyModel.attributes.id
					});
				},msg,me.refreshCancel);

			}else{
				var msg = "Você tem certeza que deseja excluir esse item?"
				Modal.confirmCustom(() => {
					Modal.hide();
					ItemStore.dispatch({
						action: ItemStore.ACTION_DELETE,
						data: me.state.itemModel.attributes.id
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

		if( this.state.itemModel.get("name") =="Informações gerais"){
			return (<ul id="level-menu" className="dropdown-menu">
			<li>
				<Link
					to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}
					onClick={this.changeVizualization}>
						<span className="mdi mdi-pencil cursorPointer" title={Messages.get("label.title.editPolicy")}>
						<span id="menu-levels"> {Messages.getEditable("label.title.editPolicy","fpdi-nav-label")} </span>
						</span>
				</Link>
			</li>
			<li>
				<Link
					to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}//this.state.model.get("id")}
					onClick={this.deleteItem}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
						<span id="menu-levels"> {Messages.getEditable("label.deletePolicy","fpdi-nav-label")} </span>
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
						to={'/forrisco/policy/'+this.context.policy.attributes.id}
						title={this.context.policy.attributes.name}>{this.context.policy.attributes.name.length > 15 ? this.context.policy.attributes.name.substring(0, 15)+"..." : this.context.policy.attributes.name.substring(0, 15)
					}</Link>
					<span className="mdi mdi-chevron-right fpdi-breadcrumbDivisor"></span>
				</span>
				<span className="fpdi-breadcrumb fpdi-selectedOnBreadcrumb">
					{this.state.title > 15 ? this.state.title.substring(0, 15)+"..." : this.state.title.substring(0, 15)}
				</span>
			</div>
		);
	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return <div>
			{this.state.model ? this.renderBreadcrumb() : ""}

			<div className="fpdi-card fpdi-card-full floatLeft">

			<h1>
				{this.state.itemModel.attributes.name=="Informações gerais" ? this.state.policyModel.attributes.name : this.state.itemModel.attributes.name}
				{this.state.model && (this.context.roles.MANAGER || _.contains(this.context.permissions, PermissionsTypes.MANAGE_PLAN_PERMISSION)) || true ?
					(<span className="dropdown">
						<a
							className="dropdown-toggle"
							data-toggle="dropdown"
							aria-haspopup="true"
							aria-expanded="true"
							title={Messages.get("label.actions")}>

							<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
							<span className="mdi mdi-chevron-down" />
						</a>

						{this.context.policy.attributes.archived ? this.renderArchivePolicy() : this.renderUnarchivePolicy()}

					</span>
				):""}
			</h1>

			<VerticalForm
				vizualization={this.state.vizualization}
			    ref='planRegisterForm'
			    onCancel={this.onCancel}
				onSubmit={this.onSubmit}
				fields={this.getInfo()}
				store={ItemStore}
				submitLabel={Messages.get("label.submitLabel")}
			/>

			{this.state.itemModel.attributes.name =="Informações gerais" ? this.getMatrix(): ""}


			</div>
		</div>;
	}
});
