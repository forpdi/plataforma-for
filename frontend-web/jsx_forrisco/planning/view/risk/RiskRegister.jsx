import React from "react";
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Form from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import ListForm from "forpdi/jsx/planning/widget/attributeForm/ListAttributeForm.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import PolicyStore from "forpdi/jsx_forrisco/planning/store/Policy.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import _ from 'underscore';

//var ListForm = ListForm.ListForm
var VerticalForm = Form.VerticalForm;

export default React.createClass({
	contextTypes: {
		roles: React.PropTypes.object.isRequired,
		router: React.PropTypes.object,
		toastr: React.PropTypes.object.isRequired,
		permissions: React.PropTypes.array.isRequired,
		tabPanel: React.PropTypes.object,
		planRisk: React.PropTypes.object.isRequired,
		unit: React.PropTypes.object.isRequired
	},
	getInitialState() {
		return {
			visualization:true,
			fields: [],
			users: [],
			planRiskId: null,
			unitId: null,
			riskModel:null,
			strategy:[],
			process:[],
			activity:[],
			risk_pdi:false,
			risk_obj_process:false,
			risk_act_process:false,
		}
	},

	componentDidMount() {
			UserStore.on("retrieve-user",(model) => {
				this.setState({
					users:model.data
				})
			},this)

			/*PolicyStore.on("findpolicy", (model) => {
				me.setState({
					policyModel: model.data
				});
				if(model != null){
					PolicyStore.dispatch({
						action: PolicyStore.ACTION_RETRIEVE_RISK_LEVEL,
						data: model.data.id
					});
				}
			}, this);*/


		this.refresh()
	},
	componentWillUnmount(){
		UserStore.off(this,this,this)
		//PolicyStore.off(this,this,this)
	},


	componentWillReceiveProps(newProps, newContext) {

		if (this.state.riskModel==null || (newProps.risk.id != this.state.riskModel.id || this.state.visualization != newProps.visualization)) {
			this.setState({
				loading: true,
				fields: [],
				visualization: newProps.visualization,
				planRiskId: newProps.planRiskId,
				unitId: newProps.unitId,
				riskModel:  newProps.risk,
			});
		}
		this.refreshData()
	},


	// users
	//todas probabilidades	risklevel
	//todos impactos		risklevel
	//matrix de risco	 política
	refresh(){
		if (this.props.risk!=null) {
			this.setState({
				loading: true,
				fields: [],
				//visualization: newProps.visualization,
				//newField: false,
				//newFieldType: null,
				planRiskId: this.context.planRisk.attributes.id,
				unitId: this.props.risk.unit.id,
				riskModel:  this.props.risk,
			});
		}

		this.refreshData()
	},

	refreshData(){
		console.log(this.context.planRisk.attributes.policy)
		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
		});

		/*PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_POLICY,
			data: this.state.plan.policyId
		});*/
		this.setState({policy:this.context.planRisk.attributes.policy})

		if(this.props.risk){
			this.state.risk_pdi=this.props.risk.risk_pdi
			this.state.risk_obj_process=this.props.risk.risk_obj_process
			this.state.risk_act_process=this.props.risk.risk_act_process
		}
	},

	getName() {
		return [{
				name: "nome",
				type: AttributeTypes.TEXT_AREA,
				placeholder: "Nome do Risco",
				maxLength: 100,
				label: "Nome do Risco",
				value: this.state.riskModel!=null ? this.state.riskModel.name : null,
			}]
	},

	getFields() {
		var fields = [];

		if(this.state.riskModel != null){
			fields.push({
				name: "code",
				type: AttributeTypes.TEXT_AREA,
				placeholder: "Código",
				maxLength: 100,
				label: "Código de indentificação do risco",
				value: this.state.riskModel.code ,
			},{
				name: "author",
				type: AttributeTypes.SELECT_FIELD,
				optionsField:this.getUsers(),
				placeholder: "Selecione um responsável",
				maxLength: 100,
				label: "Responsável",
				value: this.state.riskModel.user.name,
			},{
				name: "date",
				type: AttributeTypes.DATE,
				placeholder: "",
				maxLength: 100,
				label: "Data e hora de criação do risco",
				value: this.state.riskModel.begin,
			},{
				name: "reason",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "Causas do risco",
				maxLength: 1000,
				label: "Causa",
				value: this.state.riskModel.reason,
			},{
				name: "result",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "Consequência do risco",
				maxLength: 1000,
				label: "Consequência",
				value: this.state.riskModel.result,
			},{
				name: "probability",
				type: AttributeTypes.SELECT_FIELD,
				optionsField:[{}],
				placeholder: "Selecione",
				maxLength: 100,
				label: "Probabilidade",
				value: this.state.riskModel.probability,
			},{
				name: "impact",
				type: AttributeTypes.SELECT_FIELD,
				optionsField:[{}],
				placeholder: "Selecione",
				maxLength: 100,
				label: "Impacto",
				value: this.state.riskModel.impact,
			},{
				name: "riskLevel",
				type: AttributeTypes.TEXT_AREA,
				placeholder: "",
				maxLength: 100,
				label: "Grau do risco",
				value: this.getLevel(this.state.riskModel.probability, this.state.riskModel.impact),
			},{
				name: "periodicity",
				label: "Periodicidade da análise",
				type: AttributeTypes.SELECT_FIELD,
				optionsField:[{label:'Diária'},
						{label:'Semanal'},
						{label:'Quinzenal'},
						{label:'Mensal'},
						{label:'Bimestral'},
						{label:'Trimestral'},
						{label:'Semestral'},
						{label:'Anual'}],
				displayField: 'label',
				placeholder: "Selecione a periodicidade",
				maxLength: 100,
				value: this.state.riskModel.periodicity,
			},{
				name: "tipology",
				type: AttributeTypes.SELECT_FIELD,
				optionsField:[{label:'Risco operacional'},
						{label:'Risco de imagem/reputação do órgão'},
						{label:'Risco legal'},
						{label:'Risco financeiro/orçamentário'}],
				placeholder: "Selecione",
				maxLength: 100,
				label: "Tipologia",
				value: this.state.riskModel.tipology,
			},{
				name: "type",
				type: AttributeTypes.SELECT_FIELD,
				optionsField:[{label:'Ameaça'},{label:'Oportunidade'}],
				displayField: 'label',
				placeholder: "Selecione",
				maxLength: 100,
				label: "Tipo",
				value: this.state.riskModel.type,
			}
		);
		}

		return fields;
	},

	getStrategies(){

		if(this.state.riskModel==null || this.state.riskModel.strategies==null||  !this.state.riskModel.risk_pdi){
			return [];
		}

		if (this.state.riskModel.strategies.total==0){
			return[{
				label:"Objetivo(s) do(s) processo(s) vinculado(s)",
				value:"Não está vinculado a nenhum objetivo estratégido do PDI",
				name:""
			}]
		}

		var fields=[]
		this.state.riskModel.strategies.list.map((fielditem, index) => {
			fields.push({
				name: "type",
				type: AttributeTypes.TEXT_AREA,
				placeholder: "",
				maxLength: 100,
				label: index==0 ? "Objetivo(s) estratégico(s) do PDI vinculado(s)": null,
				value: fielditem.name,
				link: fielditem.linkFPDI,
				linkName: "(Visualizar no PDI)",
				edit:false
			})
		})

		return fields;
	},

	getProcesses(){

		if(this.state.riskModel==null || this.state.riskModel.processes==null||  !this.state.riskModel.risk_obj_process){
			return [];
		}
		if (this.state.riskModel.processes.total==0){
			return[{
				label:"Objetivo(s) do(s) processo(s) vinculado(s)",
				value:"Não está vinculado a nenhum objetivo do processo",
				name:""
			}]
		}

		var fields=[]
		this.state.riskModel.processes.list.map((fielditem, index) => {
			fields.push({
				name: "type",
				type: AttributeTypes.SELECT_FIELD,
				placeholder: "",
				maxLength: 100,
				label: index==0 ? "Objetivo(s) do(s) processo(s) vinculado(s)" : null,
				value: fielditem.name,
				link: fielditem.linkFPDI,
				linkName: "(Visualizar objetivo do processo)",
				edit:false
			})
		})

		return fields;
	},

	getActivities(){

		if(this.state.riskModel==null || this.state.riskModel.activities==null||  !this.state.riskModel.risk_act_process){
			return [];
		}
		if (this.state.riskModel.activities.total==0){
			return[{
				label:"Atividade(s) do(s) processo(s) vinculado(s)",
				value:"Não está vinculado a nenhuma atividade",
				name:""
			}]
		}

		var fields=[]
		this.state.riskModel.activities.list.map((fielditem, index) => {
			fields.push({
				name: "activities"+(index),
				type: AttributeTypes.SELECT_FIELD,
				placeholder: "",
				maxLength: 100,
				label: index==0 ? "Atividade(s) do(s) processo(s) vinculado(s)" : null,
				value: fielditem.name,
				link: fielditem.linkFPDI,
				linkName: "(Visualizar processo)",
				edit:false
			})
		})

		return fields;
	},


	getProbLabel(i){
		var probs=	this.state.policyModel.data.probability.match(/\[.*?\]/g)
		if(probs[i] != null){
			return probs[i].substring(1,probs[i].length-1)
		}
		return null
	},

	getImpacLabel(i){
		var impac=	this.state.policyModel.data.impact.match(/\[.*?\]/g)
		if(impac[i] != null){
			return impac[i].substring(1,impac[i].length-1)
		}
		return null
	},

	getLevel(prob, imp){
		//console.log(prob,imp)
		return "zero grau";
	},

	getUsers(){
		var fields=[]
		for(var i=0; i<this.state.users.length;i++){
			fields.push(
				{label:this.state.users[i].name}
			)
		}
		return fields
	},

	onCancel(){
		this.setState({
			visualization:true
		})

		/*this.state.visualization=false
		if(this.state.riskModel){
			console.log("cancel",this.state.visualization)
			this.context.router.push("/forrisco/plan-risk/1/unit/1/risk/1");
			this.context.router.push("/forrisco/plan-risk/1/unit/1/risk/1/details");
			return
		}*/

		/*document.getElementById("field-name").value = "";
		document.getElementById("field-description").value = "";
		document.getElementById("field-risk_level_1").value = "";
		document.getElementById("field-risk_cor_1").value = "";
		document.getElementById("field-nline").value = "";
		document.getElementById("field-ncolumn").value = "";
		this.setState({
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
		})*/
	},
	handleStrategyChange: function (changeEvent) {
		this.setState({
		  risk_pdi: (changeEvent.target.value =="Sim"? true: false)
		});
	  },

	  handleProcessChange: function (changeEvent) {
		this.setState({
		  risk_obj_process: (changeEvent.target.value =="Sim"? true: false)
		});
	  },

	  handleActivityChange: function (changeEvent) {
		this.setState({
		  risk_act_process: (changeEvent.target.value =="Sim"? true: false)
		});
	  },


	onSubmit(){
		console.log("get all data and update if model exists or save a new risk")
	},

	render(){

		console.log(this.props)

			return( <div>
				<div className="fpdi-card fpdi-card-full floatLeft">


				{!this.state.visualization?
					<VerticalForm
							vizualization={this.state.visualization}
							fields={this.getName()}
							submitLabel={Messages.get("label.submitLabel")}
							noButtons={true}
							ref={'field-name'}
						/>
				:""}

				{this.getFields().map((fielditem, index) => {

							if(fielditem.type== AttributeTypes.SELECT_FIELD){
								//console.log(fielditem.name, fielditem.optionsField)
							}

					if((fielditem.name=="riskLevel" || fielditem.name=="date") && !this.state.visualization){
						return
					}

					return (<div><VerticalForm
						vizualization={this.state.visualization}
						fields={[fielditem]}
						submitLabel={Messages.get("label.submitLabel")}
						noButtons={true}
						ref={'field-'+index}
					/></div>)
				})}


				{!this.state.visualization ?<div>
				<div  style={{"display": "-webkit-box", margin: "10px 0px"}} className={"fpdi-text-label"}>{Messages.get('label.risk.objectivePDI')}</div>
					<form>
						<input  style={{"margin":"0px 5px"}} type="radio" name="objectivePDI"  checked={this.state.risk_pdi===true} onChange={this.handleStrategyChange}  value="Sim"/>Sim
						<input style={{"margin":"0px 5px"}} type="radio" name="objectivePDI"  checked={this.state.risk_pdi ===false} onChange={this.handleStrategyChange}  value="Não"/>Não
					</form>
					<br/>
				 </div>
				:""}
				{<ListForm
					vizualization={this.state.visualization}
					fields={this.getProcesses()}
					submitLabel={Messages.get("label.submitLabel")}
					noButtons={true}
				/>}


				{!this.state.visualization ?<div>
				<div  style={{"display": "-webkit-box", margin: "10px 0px"}} className={"fpdi-text-label"}>{Messages.get('label.risk.objectiveProcess')}</div>
					<form>
						<input  style={{"margin":"0px 5px"}} type="radio" name="objectivePDI"  checked={this.state.risk_pdi===true} onChange={this.handleProcessChange}  value="Sim"/>Sim
						<input style={{"margin":"0px 5px"}} type="radio" name="objectivePDI"  checked={this.state.risk_pdi ===false} onChange={this.handleProcessChange}  value="Não"/>Não
					</form>
					<br/>
				 </div>
				:""}
				{<ListForm
					vizualization={this.state.visualization}
					fields={this.getStrategies()}
					submitLabel={Messages.get("label.submitLabel")}
				/>}


				{!this.state.visualization ?<div>
				<div  style={{"display": "-webkit-box", margin: "10px 0px"}} className={"fpdi-text-label"}>{Messages.get('label.risk.activityProcess')}</div>
					<form>
						<input  style={{"margin":"0px 5px"}} type="radio" name="objectivePDI"  checked={this.state.risk_pdi===true} onChange={this.handleActivityChange}  value="Sim"/>Sim
						<input style={{"margin":"0px 5px"}} type="radio" name="objectivePDI"  checked={this.state.risk_pdi ===false} onChange={this.handleActivityChange}  value="Não"/>Não
					</form>
					<br/>
				 </div>
				:""}
				{<ListForm
					vizualization={this.state.visualization}
					fields={this.getActivities()}
					submitLabel={Messages.get("label.submitLabel")}
				/>}


				<br/>

				<br/>


				<VerticalForm
					vizualization={this.state.visualization}
					onCancel={this.onCancel}
					onSubmit={this.onSubmit}
				/>
			</div>
		</div>);
		/*}else{
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname+"/edit", this.state.riskModel?  this.state.riskModel.name:"");
			});
			return( <div> visualization = false (edit & save) </div>)
		}*/
	}
});
