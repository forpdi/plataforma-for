import React from "react";
import {Link} from 'react-router';
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import ItemStore from "forpdi/jsx_forrisco/planning/store/Item.jsx";
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Validation from 'forpdi/jsx_forrisco/core/util/Validation.jsx';
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import VerticalInput from "forpdi/jsx/core/widget/form/VerticalInput.jsx";

import Toastr from 'toastr';
import $ from 'jquery';

var Validate = Validation.validate;
var errorField = false;


export default React.createClass({
	contextTypes: {
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		tabPanel: React.PropTypes.object,
		roles: React.PropTypes.object.isRequired
	},

	getInitialState() {
		return {
			loading: null,
			itemModel:null,
			policyModel: null,
			risklevelModel: null,
			fields: null,
			visualization: true,
			hide: true,
			hidePI: true,
			validPI: false,
			ncolumn: 0,
			nline: 0,
			matrix_l: 0,
			matrix_c: 0,
			color: 1,
			submitLabel: "Salvar",
			cancelLabel: "Cancelar",
			cores: [{'label':"Vermelho", id:0},
					{'label':"Marron", id:1},
					{'label':"Amarelo", id:2},
					{'label':"Laranja", id:3},
					{'label':"Verde", id:4},
					{'label':"Azul", id:5}],
			probability: [],
			impact: []
		};
	},

	getFields() {
		var fields = [];
		fields.push({
			name: "name",
			type: "text",
			required: true,
			maxLength: 240,
			placeholder: "Nome da Política",
			label: Messages.getEditable("label.name","fpdi-nav-label"),
			value: this.state.policyModel ? this.state.policyModel.attributes.name : null,
		},{
			name: "description",
			type: "textarea",
			placeholder: "Descrição da Política",
			maxLength: 9900,
			label: Messages.getEditable("label.descriptionPolicy","fpdi-nav-label"),
			value: this.state.policyModel ? this.state.policyModel.attributes.description : null,
		})

		return fields;

	},

	getRisco(n) {
		var fields = [];
		var cor=null
		var  risk=null
		if(this.state.risklevelModel){
			if(this.state.risklevelModel.data[n-1]){
				switch(this.state.risklevelModel.data[n-1]['color']) {
					case 0: cor="Vermelho"; break;
					case 1: cor="Marron"; break;
					case 2: cor="Amarelo"; break;
					case 3: cor="Laranja"; break;
					case 4: cor="Verde"; break;
					case 5: cor="Azul"; break;
					default: cor="Vermelho";
				}
				risk=this.state.risklevelModel.data[n-1]['level']
			}
		}

		fields.push({
			name: "risk_level_"+n,
			type: "text",
			required: true,
			maxLength: 40,
			placeholder: " Ex: Crítico",
			label: Messages.getEditable("label.policyConfig","hide"),
			value: risk,
			onChange: this.onChangeMatrix
		},{
			name: "risk_cor_"+n,
			type: "select",
			required: true,
			maxLength: 40,
			label: Messages.getEditable("label.policySelect","hide"),
			value: null,
			value: cor,
			valueField: 'label',
			displayField: 'label',
			onChange: this.onChangeMatrix
		})
		return fields
	},

	getNumero() {
		var fields = [];

		fields.push({
			name: "nline",
			type: "number",
			required: true,
			maxLength: 5,
			placeholder: " Nº de linhas",
			//label: Messages.getEditable("label.policyPI","fpdi-nav-label"),
			value: this.state.policyModel ? this.state.policyModel.attributes.nline : null,
			onChange: this.change
		},{
			name: "ncolumn",
			type: "number",
			required: true,
			maxLength: 5,
			placeholder: " Nº de colunas",
			//label: "",//Messages.getEditable("label.policySelect","fpdi-nav-label"),
			value: this.state.policyModel ? this.state.policyModel.attributes.ncolumn : null,
			onChange: this.change
		})

		return fields
	},

	getProbabilidade(value,n) {
		return [{
		name: "probability_"+n,
		type: "text",
		required: true,
		maxLength: 1,
		hidden: true,
		placeholder: " Tipo de probabilidade (Ex.: Alto, Médio ou Baixo)",
		label: "",
		value: value
	}]},

	getImpacto(value,n) {
		return [{
		name: "impact_"+n,
		type: "text",
		required: true,
		maxLength: 1,
		hidden: true,
		placeholder: " Tipo de Impacto (Ex.: Alto, Médio ou Baixo)",
		label: "",
		value: value
	}]},

	componentDidMount() {
		var me = this;

		PolicyStore.on("sync", (model) => {
			if (EnvInfo.company) {
				EnvInfo.company.name = model.get("name");
				EnvInfo.company.logo = model.get("logo");
				EnvInfo.company.description = model.get("description");
				EnvInfo.company.showDashboard = model.get("showDashboard");
				EnvInfo.company.showMaturity = model.get("showMaturity");
				EnvInfo.company.showBudgetElement = model.get("showBudgetElement");
			}
			me.context.router.push("/system/companies");
		}, me);

		PolicyStore.on("retrieve", (model) => {
			me.setState({
				loading: false,
				policyModel:model,
				fields: me.getFields(),
				ncolumn: model.attributes.ncolumn,
				nline: model.attributes.nline,
				validPI: true,// model.attributes.nline>1 &&  model.attributes.ncolumn>1,
				hide:false,
				matrix_c: model.attributes.ncolumn,
				matrix_l: model.attributes.nline
			});
		}, me);

		PolicyStore.on("policycreated", (model) => {
			if(model.data.id){
				var msg = Messages.get("notification.policy.save");
				this.context.toastr.addAlertSuccess(msg);
				me.context.router.push("/forrisco/policy/"+model.data.id+"/item/overview");
			}else{
				var msg= model? model.msg : "Erro ao criar Política"
				this.context.toastr.addAlertError(msg);
			}
		}, me);

		PolicyStore.on("policyUpdated", (model) => {
			if(model.data.id != null){
				this.context.toastr.addAlertSuccess(Messages.get("notification.policy.update"));
				me.context.router.push("/forrisco/policy/"+model.data.id+"/item/"+this.state.itemModel.data.id);
			}else{
				var msg= Messages.get("label.errorUpdatePolicy") + (model ? ": "+model.msg :"")
				this.context.toastr.addAlertError(msg);
			}
		}, me);

		PolicyStore.on("retrieverisklevel", (model) => {
			if(model != null){
				me.setState({
					risklevelModel:model,
					color:model.data.length
				});
			}
		}, me);

		ItemStore.on("retrieveInfo", (model) => {

			if(model != null){
				me.setState({
					itemModel:model
				});
			}
		}, me);

		if(this.props.params.policyId){
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE,
				data: this.props.params.policyId
			});

			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
				data: this.props.params.policyId
			});

			ItemStore.dispatch({
				action: ItemStore.ACTION_RETRIEVE_INFO,
				data: {policyId: this.props.params.policyId}
			});
		}

		if(this.props.params.policyId){
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_RETRIEVE,
				data: this.props.params.policyId
			});
		}
	},
	componentWillUnmount() {
		PolicyStore.off(null, null, this);
	},

	fieldsDashboardBoardCommunity () {
		if (this.refs.policyEdit.refs.showMaturity.refs["field-showMaturity"].disabled == true) {
			this.refs.policyEdit.refs.showMaturity.refs["field-showMaturity"].disabled = false;
		} else {
			this.refs.policyEdit.refs.showMaturity.refs["field-showMaturity"].disabled = true;
		}
	},
	submitWrapper(evt) {
		if(evt)
			evt.preventDefault();
		if (!this.onSubmit)
			console.warn("VerticalForm: You must pass your own onSubmit callback.");
		else {
			if (this.onSubmit(this.getValues()))
				$(this.refs['btn-submit']).attr("disabled", "disabled");
		}
	},
	getValues() {
		var data = {};
		var risk_level=[];
		var level=[];
		var cor=[];


		for(var i=0;i<this.getFields().length;i++){
			data[this.getFields()[i].name]=this.refs.policyEditForm["field-"+this.getFields()[i].name].value;
		}

		for(var j=0;j<this.state.color;j++){
			var current_color;
			switch(this.refs.policyEditForm["field-"+this.getRisco(j+1)[1].name].value) {
				case "Vermelho": current_color=0; break;
				case "Marron": current_color=1; break;
				case "Amarelo": current_color=2; break;
				case "Laranja": current_color=3; break;
				case "Verde": current_color=4; break;
				case "Azul": current_color=5; break;
				default: current_color=0;
			}
			cor[j]=current_color;
			level[j]=this.refs.policyEditForm["field-"+this.getRisco(j+1)[0].name].value;
		}
		for(var i=0; i<2; i++) {
			risk_level[i] = new Array(cor.length);
		}
		for(var i=0;i<cor.length;i++){
			risk_level[0][i]=level[i]
			risk_level[1][i]=cor[i]
		}


		for(var i=0;i<this.getNumero().length;i++){
			data[this.getNumero()[i].name]=this.refs.policyEditForm["field-"+this.getNumero()[i].name].value;
		}

		var probabilidade=""
		var impacto=""
		for(var i=1;i<=this.state.nline;i++){
			probabilidade+="["+this.refs.policyEditForm["field-"+this.getProbabilidade(null,i)[0].name].value;
			probabilidade+="]"
		}

		for(var i=1;i<=this.state.ncolumn;i++){
			impacto+="["+this.refs.policyEditForm["field-"+this.getImpacto(null,i)[0].name].value;
			impacto+="]"
		}

		var matrix=""
		for(var i=0; i<=this.state.matrix_l; i++){
			for(var j=0; j<=this.state.matrix_c; j++){
				if(i!=this.state.matrix_l || j!=0){
					matrix+="["+i+","+j+"]"+this.refs.policyEditForm["field-["+i+","+j+"]"].value+";"
				}
			}
		}

		data["risk_level"]=risk_level
		data["probability"]=probabilidade
		data["impact"]=impacto
		data["matrix"]=matrix.substring(0,matrix.length-1)
		return data;
	},

	generateMatrix(){

		if(this.state.ncolumn>6 || this.state.nline>6){
			Toastr.error("colunas ou linhas ultrapassou o limite")
			return
		}

		this.setState({
			hide: false,
			matrix_l: this.state.nline,
			matrix_c: this.state.ncolumn
		})

		var string='{'
		switch(this.state.color){
			case 6: string+="["+this.refs.policyEditForm['field-risk_level_6'].value+" : "+this.refs.policyEditForm['field-risk_cor_6'].value+"],"
			case 5: string+="["+this.refs.policyEditForm['field-risk_level_5'].value+" : "+this.refs.policyEditForm['field-risk_cor_5'].value+"],"
			case 4: string+="["+this.refs.policyEditForm['field-risk_level_4'].value+" : "+this.refs.policyEditForm['field-risk_cor_4'].value+"],"
			case 3: string+="["+this.refs.policyEditForm['field-risk_level_3'].value+" : "+this.refs.policyEditForm['field-risk_cor_3'].value+"],"
			case 2: string+="["+this.refs.policyEditForm['field-risk_level_2'].value+" : "+this.refs.policyEditForm['field-risk_cor_2'].value+"],"
			default:
		}
		string+="["+this.refs.policyEditForm['field-risk_level_1'].value+" : "+this.refs.policyEditForm['field-risk_cor_1'].value+"]}"

		this.refs.policyEditForm['field-risk_level'].value=string

	},
	createTable(){

		let table = []

		var risk_level=[];
		var level=[];
		var cor=[];

		for(var j=0;j<this.state.color;j++){
			if(this.refs.policyEditForm["field-"+this.getRisco(j+1)[1].name] != null){
				cor[j]=this.refs.policyEditForm["field-"+this.getRisco(j+1)[1].name].value;
				level[j]=this.refs.policyEditForm["field-"+this.getRisco(j+1)[0].name].value;
			}
		}
		for(var i=0;i<cor.length;i++){
			risk_level.push({'label':level[i],'cor':cor[i]})
		}

		var probabilidade=[]
		var impacto=[]
		for(var i=1;i<=this.state.matrix_l;i++){
			if(this.refs.policyEditForm["field-"+this.getProbabilidade(null,i)[0].name]){
				probabilidade.push({'label': this.refs.policyEditForm["field-"+this.getProbabilidade(null,i)[0].name].value})
			}
		}

		for(var i=1;i<=this.state.matrix_c;i++){
			if(this.refs.policyEditForm["field-"+this.getImpacto(null,i)[0].name]){
				impacto.push({'label': this.refs.policyEditForm["field-"+this.getImpacto(null,i)[0].name].value})
			}
		}



		for (let i = 0; i <= this.state.matrix_l; i++) {
			let children = []
			for (let j = 0; j <= this.state.matrix_c; j++) {

				var classe="Cinza"

				if(j != 0 ){
					if(i!=this.state.matrix_l){

						if(this.refs.policyEditForm["field-["+i+","+j+"]"] != null){
							for(var k=0;k<cor.length;k++){
								if(risk_level[k]['label']==this.refs.policyEditForm["field-["+i+","+j+"]"].value){
									classe = risk_level[k]['cor'];
									k=cor.length;
								}
							}
						}

						children.push(<td><div className={classe}>{
							<VerticalInput
								formId={this.props.id}
								fieldDef={{
									name: "["+i+","+j+"]",
									type: "select",
									required: false,
									maxLength:240,
									placeholder: "Selecione o Grau",
									value: this.model ? this.model.get("name"):null,
									options: risk_level,
									valueField: 'label',
									displayField: 'label',
									className: "matrixSelect",
									onChange: this.onChangeMatrix
							}}/>
						}</div></td>)
					}else if(i==this.state.matrix_l){
						children.push(<td>{
							<VerticalInput
								formId={this.props.id}
								fieldDef={{
									name:  "["+i+","+j+"]",
									type: "select",
									required: false,
									maxLength:240,
									placeholder: "Selecione o Impacto".substring(0, 18)+"...",
									value: this.model ? this.model.get("name"):null,
									options: impacto,
									valueField: 'label',
									displayField: 'label',
									className: "matrixSelect",
									onChange: this.onChangeMatrix
								}}
							/>
						}</td>)
					}
				}else{
					if(i!=this.state.matrix_l){
						children.push(<td>{
							<VerticalInput
								formId={this.props.id}
								fieldDef={{
									name:  "["+i+","+j+"]",
									type: "select",
									required: false,
									maxLength:240,
									placeholder: "Selecione a Probabilidade".substring(0, 18)+"...",
									value: this.model ? this.model.get("name"):null,
									options: probabilidade,
									valueField: 'label',
									displayField: 'label',
									className: "matrixSelect",
									onChange: this.onChangeMatrix
							}}/>
						}</td>)
					}else{
						children.push(<div style={{display: "inline-block"}}>
						&emsp;&emsp;&emsp;&emsp;&emsp;
						&emsp;&emsp;&emsp;&emsp;&emsp;
						&emsp;&emsp;&emsp;&emsp;&emsp;
						&emsp;&emsp; &nbsp;&nbsp;</div>)
					}
				}
		  }
		  table.push(<tr>{children}</tr>)
		}

		return (

			<table style={{width: "min-content"}}>
				<th style={{top: (this.state.matrix_l*33 +30)+"px" , right: "10px", position: "relative"}} >
					<div style={{width: "30px" }} className="vertical-text">PROBABILIDADE</div>
				</th>
				<th>
					<tr>
						{table}
					</tr>
					<tr>
						<div style={{left: (this.state.matrix_c*61 +160)+"px", position: "relative"}}>IMPACTO</div>
					</tr>
				</th>
			</table>
		);
	},

	getProbLabel(i){
		var probs=	this.state.policyModel.attributes.probability.match(/\[.*?\]/g)
		if(probs[i] != null){
			return probs[i].substring(1,probs[i].length-1)
		}
		return null
	},

	getImpacLabel(i){
		var impac=	this.state.policyModel.attributes.impact.match(/\[.*?\]/g)
		if(impac[i] != null){
			return impac[i].substring(1,impac[i].length-1)
		}
		return null
	},

	probabilidadeImpacto(){
		let campos = []
		var contem=false
		var field
		for(var i=0;i<6;i++){
			if(this.state.nline>i && this.state.nline>1){

				field=document.getElementById("field-probability_"+(i+1))

				campos.push(this.getProbabilidade(this.state.policyModel ? this.getProbLabel(i) : (field ? field.value : null), i+1).map((field, idx) => {

					return (<HorizontalInput
						name={field.name}
						formId={this.props.id}
						fieldDef={field}
						key={field.name}
						confirmKey={idx == (this.getProbabilidade().length - 1) ? this.props.confirmKey : undefined}
						/>
					);
				}))
				contem=true;
			}else{
				if(this.state.ncolumn>i){
				campos.push(<div style={{display: "inline-block"}}>
				&emsp;&emsp;&emsp;&emsp;&emsp;
				&emsp;&emsp;&emsp;&emsp;&emsp;
				&emsp;&emsp;&emsp;&emsp;&emsp;
				&emsp;&emsp; &nbsp;&nbsp;</div>)
				}
			}

			field=document.getElementById("field-impact_"+(i+1))

			if(this.state.ncolumn>i && this.state.ncolumn>1){
				campos.push(this.getImpacto(this.state.policyModel ? this.getImpacLabel(i) : (field ? field.value : null), i+1).map((field, idx) => {
					return (<HorizontalInput
						name={field.name}
						formId={this.props.id}
						fieldDef={field}
						key={field.name}
						confirmKey={idx == (this.getImpacto().length - 1) ? this.props.confirmKey : undefined}
						/>
					);
				}))
				contem=true;
			}
			if(contem){
				campos.push(<br/>)
			}
		}
		return(<div>{campos}</div>)
	},

	grauRisco(n){
		let grau = []


		grau.push(this.getRisco(n).map((field, idx) => {

				field.options=this.state.cores

				return (<HorizontalInput
					name={field.name}
					formId={this.props.id}
					fieldDef={field}
					key={field.value? field.value: field.name}
					confirmKey={idx == (this.getRisco(1).length - 1) ? this.props.confirmKey : undefined}
					/>
				);
			}))

			if(n>1){
				grau.push
				(<Link onClick={this.deleteGrauRisco.bind(this,n)}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deleteRiskGrade")}>
					</span>
				</Link>)
			}
		return(<div>{grau}<br/></div>)
	},
	change(){

		this.state.ncolumn=this.refs.policyEditForm['field-ncolumn'].value
		this.state.nline=this.refs.policyEditForm['field-nline'].value

		this.setState({
			nline: this.state.nline,
			ncolumn: this.state.ncolumn,
			hidePI:false
		})

		if(this.state.nline>1  && this.state.ncolumn>1){
			this.setState({
				validPI:true
			})
		}else{
			this.setState({
				validPI:false
			})
		}
	},
	onChangeMatrix(){
		this.setState({})
	},
	deleteGrauRisco(id){

		if(id!=this.state.color){
			for(var i=id; i<this.state.color;i++){
				document.getElementById("field-risk_level_"+(i)).value = document.getElementById("field-risk_level_"+(i+1)).value;
				document.getElementById("field-risk_cor_"+(i)).value = document.getElementById("field-risk_cor_"+(i+1)).value;
			}
		}
		this.setState({
			color:this.state.color-1
		})
	},

	RiskColor(){

		if(this.state.color>5){
			this.setState({
				color: 6
			})
		}else{
			this.setState({
				color: this.state.color+1
			})
		}
	},


	cancelWrapper(event){
		event.preventDefault();

		document.getElementById("field-name").value = "";
		document.getElementById("field-description").value = "";
		document.getElementById("field-risk_level_1").value = "";
		document.getElementById("field-risk_cor_1").value = "";
		document.getElementById("field-nline").value = "";
		document.getElementById("field-ncolumn").value = "";
		this.setState({
			loading: !!this.props.params.policyId,
			policyModel: null,
			fields: null,
			visualization: true,
			hide: true,
			hidePI: true,
			validPI: false,
			ncolumn: 0,
			nline: 0,
			matrix_l: 0,
			matrix_c: 0,
			color: 1,
		})
	},

	onSubmit(data) {
		var me = this;
		var msg="";

		/*var msg = Validate.validationPolicyEdit(data, this.refs.policyEdit);

		if(msg!= ""){
			this.context.toastr.addAlertError(msg);
			return;
		}*/

		if (me.props.params.policyId) {
			data.id=this.state.policyModel.id
			data.levels=this.state.color
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_CUSTOM_UPDATE,
				data: data
			});
		} else {
			PolicyStore.dispatch({
				action: PolicyStore.ACTION_NEWPOLICY,
				data: data
			});
		}
	},

	render() {
		 var edit=this.context.router.isActive("forrisco/policy/"+this.props.params.policyId+"/edit")

		 var edit=this.context.router.isActive("forrisco/policy/"+this.props.params.policyId+"/edit")

		 if (this.state.loading) {
			return <LoadingGauge />;
		}

		return (
			<div>
			<h1 className="marginLeft115">{this.props.params.policyId ? Messages.getEditable("label.editPolicy","fpdi-nav-label"):Messages.getEditable("label.newPolicy","fpdi-nav-label")}</h1>
			<div className="fpdi-card padding40">
				<form onSubmit={this.submitWrapper} id={this.props.id} ref="policyEditForm">

					{this.getFields().map((field, idx) => {
						return (<VerticalInput
							formId={this.props.policyId}
							fieldDef={field}
							key={field.value? field.value: field.name}
							confirmKey={idx == (this.getFields().length - 1) ? this.props.confirmKey : undefined}
						/>);
					})}

					<label htmlFor={this.state.fieldId} className="fpdi-text-label">
						{Messages.getEditable("label.policyConfig","fpdi-nav-label")}
					</label>
					<br/>
					<label htmlFor={this.state.fieldId} className="fpdi-text-label">
						{Messages.getEditable("label.policyLevel","fpdi-nav-label")}
					</label>
					&nbsp;
					{(this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
						<a className="mdi mdi-plus " onClick={this.RiskColor}></a> : ""
					}

					{this.grauRisco(1)}
					{this.state.color >1 ? this.grauRisco(2) :""}
					{this.state.color >2 ? this.grauRisco(3) :""}
					{this.state.color >3 ? this.grauRisco(4) :""}
					{this.state.color >4 ? this.grauRisco(5) :""}
					{this.state.color >5 ? this.grauRisco(6) :""}


					<VerticalInput
						key={"risk_level"}
						formId={"risk_level"}
						fieldDef={{
							name: "risk_level",
							type: "text",
							value: "",
							required: false,
							type: 'hidden'}}
					/>


					<br/>
					<label htmlFor={this.state.fieldId} className="fpdi-text-label">
						{Messages.getEditable("label.policyPI","fpdi-nav-label")}
					</label>
					<br/>

					{this.getNumero().map((field, idx) => {
						return (<HorizontalInput
							name={field.name}
							formId={this.props.id}
							fieldDef={field}
							key={field.value? idx: field.name}
							//confirmKey={idx == (this.getNumero().length - 1) ? this.props.confirmKey : undefined}
							onConfirm={this.submitWrapper}
						/>);
					})}

					<br/><br/><br/>
					{!this.state.piHide ? this.probabilidadeImpacto(): ""}

					{
					((this.context.roles.MANAGER || _.contains(this.context.permissions,
						PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) && this.state.validPI) ?
						<a className="btn btn-sm btn-primary " onClick={this.generateMatrix}>
							<span/> {Messages.getEditable("label.generateMatrix","fpdi-nav-label")}
						</a> : ""
					}

					<br/><br/>
					{!this.state.hide ? this.createTable() : <br/> }


					{!!this.props.blockButtons ?
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
							<input type="submit" className="btn btn-sm btn-success" ref="btn-submit" value={this.state.submitLabel} />
							{!this.props.hideCanel ? (!this.props.cancelUrl ?
								<button className="btn btn-sm btn-default" onClick={this.cancelWrapper}>{this.state.cancelLabel}</button>
								:
								<Link className="btn btn-sm btn-default" to={this.props.cancelUrl}>{this.state.cancelLabel}</Link>
							):""}
						</div>)
					}
				</form>
			</div>
			</div>
		);
	}
});
