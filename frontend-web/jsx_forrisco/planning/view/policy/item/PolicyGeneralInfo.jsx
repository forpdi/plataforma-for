import React from "react";
import PlanRiskStore from "forpdi/jsx_forrisco/planning/store/PlanRisk.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import _ from "underscore";
import {Link} from "react-router";
import Messages from "@/core/util/Messages";
import Modal from "@/core/widget/Modal";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import PlanRiskTabPanel from "forpdi/jsx_forrisco/planning/widget/planrisk/PlanRiskTabPanel.jsx";
import Toastr from 'toastr';

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		policy: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
	},

	getInitialState() {
		return {
			planRisk: [],
			fields: [],
			title: 'Informações Gerais',
			description: '',
			policy: '',
			isLoading: true,
			policyModel:null,
			risklevelModel: null,
		}
	},

	componentDidMount() {
		if (this.props.params.policyId) {
			PolicyStore.on("findpolicy", (model) => {
				this.setState({
					policyModel: model,
					ncolumn: model.data.ncolumn,
					nline: model.data.nline,
					isLoading: false,
					//matrix_c: model.data.ncolumn,
					//matrix_l: model.data.nline,
					//validPI: true,

					//title:model.data.name
				});

				//Construção da Aba Superior
				_.defer(() => {
					this.context.tabPanel.addTab(this.props.location.pathname, this.state.title);
				});

			}, this);


			PolicyStore.on("policyDeleted", (model) => {
				if(model.success){
					this.context.router.push("/forrisco/home");
					PolicyStore.dispatch({
						action: PolicyStore.ACTION_FIND_UNARCHIVED_FOR_MENU
					});
				}else{
					if(model.message != null){
						this.context.toastr.addAlertError(model.message);
					}
				}

			},this)



			PolicyStore.on("retrieverisklevel", (model) => {
				this.setState({
					risklevelModel: model
				});
				this.forceUpdate();
				//_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, model.get("name"));});
			}, this);
		}

		this.refresh();
	},

	componentWillReceiveProps(newProps) {
		//this.refresh();
	},


	refresh() {


			PolicyStore.dispatch({
				action: PolicyStore.ACTION_FIND_POLICY,
				data: this.props.params.policyId
			});

			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
				data: this.props.params.policyId
			});
	},



	componentWillUnmount() {
		PolicyStore.off(null, null, this);
	},

	deletePolicy() {
		var me = this;
		var msg = "Você tem certeza que deseja excluir essa política?"
		Modal.confirmCustom(() => {
			Modal.hide();
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_DELETE,
				data: me.state.policyModel.data.id
			});
		},msg,me.refreshCancel);
			/*var msg = Messages.get("label.deleteConfirmation") + " " +me.state.model.attributes.name+"?";
			Modal.confirmCancelCustom(() => {
				Modal.hide();
				PlanStore.dispatch({
					action: PlanStore.ACTION_DELETE_PLAN,
					data: me.state.model
				});
			},msg,()=>{Modal.hide();me.refreshCancel;});*/

	},

	refreshCancel () {
		Modal.hide();
	},

	getMatrixValue(matrix, line, column) {

		var result=""

		for(var i=0; i<matrix.length;i++){
			if(matrix[i][1]==line){
				if(matrix[i][2]==column){
					if(matrix[i][2]==0 ){
					return <div style={{"text-align":"right"}}>{matrix[i][0]}&nbsp;&nbsp;&nbsp;&nbsp;</div>
					}else if(matrix[i][1]==this.state.policyModel.data.nline){
						return <div style={{"text-align":"-webkit-center",margin: "5px"}}>{matrix[i][0]}</div>
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
							default: cor="Cinza";
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
			<table style={{ width: "min-content" }}>
				<th>
					<td style={{top: ((this.state.policyModel.data.nline-2)*20+80)+"px" , right: "50px", position: "relative"}} >
						<div style={{width: "115px" }} className="vertical-text">PROBABILIDADE</div>
					</td>
					{table}
					<td></td>
					<td colSpan={this.state.policyModel.data.ncolumn} style={{"text-align":"-webkit-center"}}>IMPACTO</td>
				</th>
			</table>

		</div>
		);
	},


	renderDropdown() {
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
				//to={"/forrisco/policy/"+this.props.params.policyId+"/item/"+this.props.params.itemId}//this.state.model.get("id")}
				onClick={this.deletePolicy}>
				<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deletePolicy")}>
					<span id="menu-levels"> {"Deletar Política" /*Messages.getEditable("label.deletePolicy","fpdi-nav-label")*/} </span>
				</span>
			</Link>
		</li>
		</ul>);
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
					{this.state.title > 15 ? this.state.title.substring(0, 15)+"..." : this.state.title.substring(0, 15)}
				</span>
			</div>
		);
	},

	render() {
		if (this.state.isLoading === true) {
			return <LoadingGauge/>;
		}

		return (<div>
			{//this.renderBreadcrumb()
			}
			<div className="fpdi-card fpdi-card-full floatLeft">
				<h1>
					{this.state.policyModel.data.name}
					<span className="dropdown">
						<a className="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
						   aria-expanded="true"
						   title={Messages.get("label.actions")}>
							<span className="sr-only">{Messages.getEditable("label.actions","fpdi-nav-label")}</span>
							<span className="mdi mdi-chevron-down" />
						</a>
						{this.renderDropdown()}
					</span>
				</h1>

				<div>
					<br/>
					<label  className="fpdi-text-label">{"DESCRIÇÃO"}</label>
					<br/>
					{this.state.policyModel.data.description}
					<br/><br/>
					{this.getMatrix()}
					<br/>
				</div>

			</div>
		</div>)
	}
})
