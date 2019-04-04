import React from "react";
import { Link } from 'react-router';
import _ from 'underscore';

import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Form from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import ListForm from "forpdi/jsx/planning/widget/attributeForm/ListAttributeForm.jsx";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import UnitStore from "forpdi/jsx_forrisco/planning/store/Unit.jsx";
import ProcessStore from "forpdi/jsx_forrisco/planning/store/Process.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import Validation from 'forpdi/jsx_forrisco/core/util/Validation.jsx';

var VerticalForm = Form.VerticalForm;

var Validate = Validation.validate;

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
			loading: true,
			visualization: true,
			fields: [],
			users: [],
			riskModel: null,
			policyModel: null,
			strategy: [],
			process: [],
			activity: [],
			activities: 1,
			risk_pdi: false,
			risk_obj_process: false,
			risk_act_process: false,
			newRisk: false,
			strategyList:[],
			processList:[],
			unit: null,
			update:false,
			StrategyError:false,
			ObjectiveError:false,
		}
	},

	componentDidMount() {

		UserStore.on("retrieve-user", (model) => {
			this.setState({
				users: model.data,
			});
		}, this);

		ProcessStore.on("listProcessByPlan", (model) => {
			this.setState({
				process:model.data,
			});
		}, this);

		UnitStore.on("unitRetrieved", (model) => {
			if (model.data) {
				const unit = model.data;
				this.refreshTabinfo(this.props.location.pathname, `Novo Risco - ${unit.name}`);
			}
		}, this);

		StructureStore.on("companyobjectivesretrivied", (model) => {
			this.setState({
				strategy: model.data,
				loading: false
			})
		}, this)

		RiskStore.on("riskUpdated", (model) => {
			if (model.success) {
				this.context.toastr.addAlertSuccess(Messages.get("notification.risk.update"));
				this.context.router.push("/forrisco/plan-risk/" + this.props.params.planRiskId + "/unit/info");
				this.context.router.push("/forrisco/plan-risk/" + this.props.params.planRiskId + "/unit/" + this.props.params.unitId + "/risk/" + model.data.id+"/info");
			} else {
				if (model.message != null) {
					this.context.toastr.addAlertError(model.message);
				}
			}
		}, this)

		RiskStore.on("riskcreated", (model) => {
			if (model.success) {
				this.context.router.push("/forrisco/plan-risk/" + this.props.params.planRiskId + "/unit/" + this.props.params.unitId + "/risk/" + model.data.id+"/info");
			} else {
				if (model.message != null) {
					this.context.toastr.addAlertError(model.message);
				}
			}
		}, this)

		UnitStore.dispatch({
			action: UnitStore.ACTION_RETRIEVE_UNIT,
			data: { unitId: this.props.params.unitId },
		});

		this.refresh(this.props);
	},
	componentWillUnmount() {
		UserStore.off(null, null, this)
		StructureStore.off(null, null, this)
		RiskStore.off(null, null, this)
		UnitStore.off(null, null, this)
		ProcessStore.off(null, null, this)
	},


	componentWillReceiveProps(newProps, newContext) {

		if (newProps.route.path != "new") {
			if (this.state.riskModel == null || (newProps.risk.id != this.props.risk.id || this.state.visualization != newProps.visualization)) {
					this.setState({
						loading:true,
						visualization: newProps.visualization
				})
			this.refresh(newProps);
			}
		}

		if (newProps.location.pathname !== this.props.location.pathname) {
			UnitStore.dispatch({
				action: UnitStore.ACTION_RETRIEVE_UNIT,
				data: { unitId: newProps.params.unitId },
			});
		}
	},


	// users
	// structure (objetivos estratégicos)
	//processo (pendente)
	refresh(Props) {
		if (this.props.risk != null) {
			this.setState({
				fields: [],
				planRiskId: this.context.planRisk.attributes.id,
				unitId: Props.risk.unit.id,
				riskModel: Props.risk,
				activities: Props.risk.activities.list.length,
				activity: Props.risk.activities.list,
			});
		}

		if (Props.route.path == "new") {
			this.setState({
				loading: false,
				visualization: false,
				newRisk: true
			})
		}

		this.refreshData(Props)
	},

	refreshData(Props) {
		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
			data: {
				page: 1,
				pageSize: 500,
			},
		});

		StructureStore.dispatch({
			action: StructureStore.ACTION_RETRIEVE_OBJECTIVES_BY_COMPANY
		});

		ProcessStore.dispatch({
			action: ProcessStore.ACTION_LIST_BY_PLAN,
			data:Props.params.planRiskId
		});

		this.setState({
			policyModel: this.context.planRisk.attributes.policy,
			//loading: false,
		})

		if (this.props.risk) {
			this.state.risk_pdi = this.props.risk.risk_pdi
			this.state.risk_obj_process = this.props.risk.risk_obj_process
			this.state.risk_act_process = this.props.risk.risk_act_process
		}
	},


	componentDidUpdate(){
		if (this.props.route.path == "new" && !this.state.update) {
			this.state.update=true

			if(document.getElementById("field-user") !=null){
				document.getElementById("field-user").value = ''
			}

			if (document.getElementById("field-impact") != null) {
				document.getElementById("field-impact").value = ''
				document.getElementById("field-probability").value = ''
				document.getElementById("field-periodicity").value = ''
				document.getElementById("field-tipology").value = ''
				document.getElementById("field-type").value = ''
			}
		}
	},

	//_.defer(() => {this.context.tabPanel.addTab(this.props.location.pathname, response.attributes.policy.name);});
	refreshTabinfo(newPathname, tabName) {
		_.defer(() =>
			this.context.tabPanel.addTab(
				newPathname,
				this.state.riskModel ? this.state.riskModel.name : tabName,
			)
		);
	},

	getName() {
		return [{
			name: "name",
			type: AttributeTypes.TEXT_AREA,
			placeholder: "Nome do Risco",
			maxLength: 100,
			label: "Nome do Risco",
			value: this.state.riskModel != null ? this.state.riskModel.name : null,
		}]
	},

	getFields() {
		var fields = [];

		var risk = this.state.riskModel

		fields.push({
			name: "code",
			type: AttributeTypes.TEXT_AREA,
			placeholder: "Código",
			maxLength: 100,
			label: "Código de identificação do risco",
			value: risk != null ? risk.code : null,
		}, {
				name: "user",
				type: AttributeTypes.SELECT_FIELD,
				optionsField: this.getUsers(),
				placeholder: "Selecione um responsável",
				maxLength: 100,
				label: "Responsável",
				displayField: 'label',
				value: risk != null ? risk.user.name : null,
				id: risk.user.id,
				search: true
			}, {
				name: "reason",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "Causas do risco",
				maxLength: 1000,
				label: "Causa (s)",
				value: risk != null ? risk.reason : null,
			}, {
				name: "result",
				type: AttributeTypes.TEXT_AREA_FIELD,
				placeholder: "Consequência do risco",
				maxLength: 1000,
				label: "Consequência (s)",
				value: risk != null ? risk.result : null,
			}, {
				name: "probability",
				type: AttributeTypes.SELECT_FIELD,
				optionsField: this.getProbabilities(),
				displayField: 'label',
				placeholder: "Selecione",
				maxLength: 100,
				label: "Probabilidade",
				value: risk != null ? risk.probability : null,
			}, {
				name: "impact",
				type: AttributeTypes.SELECT_FIELD,
				optionsField: this.getImpacts(),
				displayField: 'label',
				placeholder: "Selecione",
				maxLength: 100,
				label: "Impacto",
				value: risk != null ? risk.impact : null,
			}, {
				name: "riskLevel",
				type: AttributeTypes.TEXT_AREA,
				placeholder: "",
				maxLength: 100,
				label: "Grau do risco",
				value: risk != null ? risk.riskLevel.level : null,
			}, {
				name: "periodicity",
				label: "Periodicidade do monitoramento",
				type: AttributeTypes.SELECT_FIELD,
				optionsField: [{ label: 'Diária' },
				{ label: 'Semanal' },
				{ label: 'Quinzenal' },
				{ label: 'Mensal' },
				{ label: 'Bimestral' },
				{ label: 'Trimestral' },
				{ label: 'Semestral' },
				{ label: 'Anual' }],
				displayField: 'label',
				placeholder: "Selecione",
				maxLength: 100,
				value: risk != null ? risk.periodicity : null,
			}, {
				name: "tipology",
				type: AttributeTypes.SELECT_FIELD,
				optionsField: [
					{ label: 'Risco operacional' },
					{ label: 'Risco de imagem/reputação do órgão' },
					{ label: 'Risco legal' },
					{ label: 'Risco financeiro/orçamentário' },
					{ label: 'Risco de Integridade'},
					{ label: 'Risco estratégico'},
				],
				placeholder: "Selecione",
				maxLength: 100,
				label: "Tipologia",
				value: risk != null ? risk.tipology : null,
			}, {
				name: "type",
				type: AttributeTypes.SELECT_FIELD,
				optionsField: [{ label: 'Ameaça' }, { label: 'Oportunidade' }],
				displayField: 'label',
				placeholder: "Selecione",
				maxLength: 100,
				label: "Tipo",
				value: risk != null ? risk.type : null,
			},{
				name: "date",
				type: AttributeTypes.DATE,
				placeholder: "",
				maxLength: 100,
				label: "Data e hora de criação do risco",
				value: risk != null ? risk.begin : null,
			},);


		return fields;
	},

	getStrategies() {

		if (this.state.riskModel == null || this.state.riskModel.strategies == null || !this.state.riskModel.risk_pdi || this.state.riskModel.strategies.list.length == 0) {
			return [{
				label: "Objetivo(s) Estratégico(s) do PDI vinculado(s)",
				value: "Não está vinculado a nenhum objetivo estratégido do PDI",
				name: "",
				placeholder: "Selecione um ou mais objetivos",
				type: AttributeTypes.SELECT_MULTI_FIELD,
				optionsField: this.getAllStrategies(),
				isvalue:false,
			}]
		}

		var fields = [];
		this.state.riskModel.strategies.list.map((fielditem, index) => {
			fields.push({
				name: "strategy-" + (index),
				type: AttributeTypes.SELECT_MULTI_FIELD,
				label: "Objetivo(s) Estratégico(s) do PDI vinculado(s)",//index==0 ? (this.state.visualization ?"Objetivo(s) do(s) processo(s) vinculado(s)":"") : null,
				linkName: "(Visualizar no PDI)",
				placeholder: "Selecione um ou mais objetivos",
				maxLength: 100,
				value: {name: fielditem.structure.name, id:fielditem.structure.id},
				link: fielditem.linkFPDI,
				optionsField: index == 0 ? this.getAllStrategies() : null,
				isvalue:true,
			})
		})

		return fields;
	},

	getProcesses() {
		if (this.state.riskModel == null
			|| this.state.riskModel.processes == null
			|| !this.state.riskModel.risk_obj_process
			||this.state.riskModel.processes.list.length == 0) {

			return [{
				label: "Objetivo(s) do(s) processo(s) vinculado(s)",
				value: "Não está vinculado a nenhum objetivo do processo",
				name: "",
				placeholder: "Selecione um ou mais objetivos",
				type: AttributeTypes.SELECT_MULTI_FIELD,
				optionsField: this.getAllProcessesObjective(),
				isvalue:false,
			}]
		}


		var fields = [];
		this.state.riskModel.processes.list.map((fielditem, index) => {
			var name= fielditem.process.objective +" - "+fielditem.process.name
			fields.push({
				name: "process-" + (index),
				type: AttributeTypes.SELECT_MULTI_FIELD,
				placeholder: "Selecione um ou mais objetivos",
				label: "Objetivo(s) do(s) processo(s) vinculado(s)",//index==0 ? (this.state.visualization ?"Objetivo(s) do(s) processo(s) vinculado(s)":"") : null,
				linkName: "(Visualizar objetivo do processo)",
				link: fielditem.linkFPDI,
				maxLength: 100,
				value: {name: name, id:fielditem.process.id},
				optionsField: index == 0 ? this.getAllProcessesObjective() : null,
				isvalue:true,
			})
		})

		return fields;
	},

	getActivities() {

		if (this.state.riskModel == null
			|| this.state.riskModel.activities == null
			|| !this.state.riskModel.risk_act_process
			|| this.state.activity.length == 0) {

			return [{
				label: "Atividade(s) do(s) processo(s) vinculado(s)",
				value: "Não está vinculado a nenhuma atividade",
				name: "",
				type: AttributeTypes.SELECT_MULTI_FIELD
			}]
		}

		var fields = []

		this.state.riskModel.activities.list.map((fielditem, index) => {
			var name=fielditem.name+" - "+fielditem.process.name
			fields.push({
				name: "activity-" + (index),
				type: AttributeTypes.SELECT_MULTI_FIELD,
				placeholder: "*",
				maxLength: 100,
				label: index == 0 ? (this.state.visualization ? "Atividade(s) do(s) processo(s) vinculado(s)" : "") : null,
				value: {name: name, id:fielditem.id},
				link: fielditem.linkFPDI,
				linkName: "(Visualizar processo)",
			})
		})


		return fields
	},


	getProbabilities() {
		var probility = this.state.policyModel.probability.match(/\[.*?\]/g)
		var fields = []

		if (probility != null) {
			for (var i in probility) {
				fields.push({ label: probility[i].substring(1, probility[i].length - 1) })
			}
		}
		return fields
	},

	getImpacts() {
		var impact = this.state.policyModel.impact.match(/\[.*?\]/g)
		var fields = []

		if (impact != null) {
			for (var i in impact) {
				fields.push({ label: impact[i].substring(1, impact[i].length - 1) })
			}
		}
		return fields
	},

	getUsers() {
		var fields = []

		for (var i = 0; i < this.state.users.length; i++) {
			fields.push(
				{ label: this.state.users[i].name, id: this.state.users[i].id }
			)
		}

		return fields
	},

	getAllStrategies() {
		var fields = []
		for (var i in this.state.strategy) {
			fields.push({ label: this.state.strategy[i].name, value: this.state.strategy[i].id })
		}

		return fields
	},

	getAllProcesses() {
		var fields = []
		for (var i in this.state.process) {
			fields.push({ label: this.state.process[i].name, value: this.state.process[i].id })
		}

		return fields
	},

	getAllProcessesObjective() {
		var fields = []
		for (var i in this.state.process) {
			fields.push({ label: this.state.process[i].objective, value: this.state.process[i].id })
		}

		return fields
	},


	onCancel() {

		if (this.state.newRisk) {
			document.getElementById("field-name").value = ''
			document.getElementById("field-code").value = ''
			document.getElementById("field-impact").value = ''
			document.getElementById("field-probability").value = ''
			document.getElementById("field-periodicity").value = ''
			document.getElementById("field-reason").value = ''
			document.getElementById("field-result").value = ''
			document.getElementById("field-tipology").value = ''
			document.getElementById("field-type").value = ''

			this.setState({
				risk_pdi: false,
				risk_obj_process: false,
				risk_act_process: false
			})
		} else{
			this.props.onChange()
		}

	},
	handleStrategyChange: function (changeEvent) {
		this.setState({
			risk_pdi: (changeEvent.target.value == "Sim" ? true : false)
		});
	},

	handleProcessChange: function (changeEvent) {
		this.setState({
			risk_obj_process: (changeEvent.target.value == "Sim" ? true : false)
		});
	},

	handleActivityChange: function (changeEvent) {
		this.setState({
			risk_act_process: (changeEvent.target.value == "Sim" ? true : false)
		});
	},


	getPA(n) {
		var fields = [];

		var processes=[]

		var activities=this.state.riskModel? this.state.riskModel.activities.list:[]

		for(var i in this.state.process){
			processes.push({ label: this.state.process[i].name,  value: this.state.process[i].id, name: this.state.process[i].name })
		}

			var process=null;

			for(var j in activities){
				for(var k in this.state.process){
					if(activities[j].process.id==this.state.process[k].id){
						process=this.state.process[k]
						j=activities.length
						break;
					}
				}
			}


			fields.push({
				name: "process_" + n,
				type: "select",
				required: true,
				maxLength: 40,
				placeholder: "Selecione um processo",
				label: Messages.getEditable("label.policySelect", "hide"),
				value: process!=null? process.name : null,
				valueField: 'label',
				displayField: 'label',
				options:  processes,
			},
			{
				name: "activity_" + n,
				type: "text",
				required: true,
				maxLength: 40,
				placeholder: "Com quais atividades do processo o risco está associado?",
				label: Messages.getEditable("label.policyConfig", "hide"),
				value: activities[n] !=null ? activities[n].name : null,
			})

		return fields
	},

	getProcessActivity() {
		var grau = []


		if(this.state.activities<1){
			this.state.activities=1
		}

		for (var i = 0; i < this.state.activities; i++) {
			var pa=[]
			pa.push(
				this.getPA(i).map((field, idx) => {
					return (<HorizontalInput
						name={field.name}
						fieldDef={field}
						key={field.value ? field.value : field.name}
						ref={'pa-' + (i)+"-"+(idx)}
					/>);
				})
			)

			if (i > 0) {
				pa.push(<span><Link onClick={this.deleteActivity.bind(this, i)}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deleteActicity")}></span>
				</Link></span>)
			}
			grau.push(<div style={{"display": "inline-flex"}}>{pa} <br/></div>)
		}

		return (<div>{grau}<br/></div>)
	},

	deleteActivity(x, y) {
		if (this.state.activities > 1) {
			this.setState({
				activities: this.state.activities - 1
			})

			for (var i = x; i < this.state.activities - 1; i++) {
				document.getElementById("field-process_" + (i)).value = document.getElementById("field-process_" + (i + 1)).value;
				document.getElementById("field-activity_" + (i)).value = document.getElementById("field-activity_" + (i + 1)).value;
			}
		}

	},

	addActivity() {
		this.setState({
			activities: this.state.activities + 1
		})
	},

	onChangeStrategies(list){
		if(list !=null){
			this.state.strategyList=list
		}

	},

	onChangeProcesses(list){
		if(list !=null){
			this.state.processList=list
		}
	},


	getValues() {
		var data = {};

		var strategyList=[]
		for(var i in this.state.strategyList){
			strategyList.push({name:this.state.strategyList[i].label, structure:{id:parseInt(this.state.strategyList[i].value)}})
		}

		var processList=[]
		for(var i in this.state.processList){
			processList.push({name:this.state.processList[i].label, process:{id: parseInt(this.state.processList[i].value)}})
		}

		var activityList=[]

		if(this.state.risk_act_process){
			for(var i=0; i < this.state.activities; i++){
				var index=this.refs["pa-"+(i)+"-0"].refs["field-process_"+(i)].selectedIndex
				activityList.push({name: this.refs["pa-"+(i)+"-1"].refs["field-activity_"+(i)].value, process:this.state.process[index-1]})
			}
		}


		data['name'] = document.getElementById("field-name").value
		data['code'] = document.getElementById("field-code").value
		data['impact'] = document.getElementById("field-impact").value
		data['probability'] = document.getElementById("field-probability").value
		data['periodicity'] = document.getElementById("field-periodicity").value
		data['reason'] = document.getElementById("field-reason").value
		data['result'] = document.getElementById("field-result").value
		data['tipology'] = document.getElementById("field-tipology").value
		data['type'] = document.getElementById("field-type").value

		if(this.refs["field-1"].refs.user.refs["field-user"].props.value.value != 0){
			data['user'] = { id: this.refs["field-1"].refs.user.refs["field-user"].props.value.value }
		}
		data['unit'] = { id: this.props.params.unitId }

		data["risk_pdi"] = this.state.risk_pdi
		data["risk_obj_process"] = this.state.risk_obj_process
		data["risk_act_process"] = this.state.risk_act_process

		data['strategies'] = {list:strategyList ,total:strategyList.length}
		data['processes'] = {list:processList ,total:processList.length}
		data['activities'] = {list:activityList, total:activityList.length}

		return data;
	},


	submitWrapper() {
		if (this.onSubmit(this.getValues())) {
			$(this.refs['btn-submit']).attr("disabled", "disabled");
		}
	},

	onSubmit(data) {
		var me = this;
		var msg = "";

		var msg = Validate.validationRiskRegister(data, this.refs);

		var objectivePDI= this.refs["objectivePDI"]
		var objectiveProcess= this.refs["objectiveProcess"]

		if(data.processes.total == 0 && objectivePDI.checked){
			if(msg==""){msg=Messages.get("notification.risk.objectivePDI")}
			this.setState({
				StrategyError:true
			})
		}else{
			this.setState({
				StrategyError:false
			})
		}

		if(data.processes.total == 0 && objectiveProcess.checked){
			if(msg==""){msg=Messages.get("notification.risk.objectiveProcess")}

			this.setState({
				ObjectiveError:true
			})
		}else{
			this.setState({
				ObjectiveError:false
			})
		}

		if (msg != "") {
			this.context.toastr.addAlertError(msg);
			return;
		}
		if (me.props.params.riskId) {
			data.id = me.props.params.riskId
			RiskStore.dispatch({
				action: RiskStore.ACTION_CUSTOM_UPDATE,
				data: data
			});
		} else {
			RiskStore.dispatch({
				action: RiskStore.ACTION_NEWRISK,
				data: data
			});
		}

	},

	render() {
		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (<div>
			{this.state.newRisk ?
				<h1>Novo Risco</h1>
				: ""}

			<form  className="fpdi-card fpdi-card-full floatLeft" id={this.props.id} >
				{!this.state.visualization ?
					<VerticalForm
						vizualization={this.state.visualization}
						fields={this.getName()}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
						ref={'field-name'}
						/>
					: ""}

				{this.getFields().map((fielditem, index) => {

					if ((fielditem.name == "riskLevel" || fielditem.name == "date") && !this.state.visualization) {
						return
					}

					if(index== 2 || index==3 || this.state.visualization){

						return (<VerticalForm
							vizualization={this.state.visualization}
							fields={[fielditem]}
							submitLabel={Messages.get("label.submitLabel")}
							showButtons={false}
							ref={'field-' + index}
						/>)
					}

					return (<span className="form-horizontal">
						<VerticalForm
							vizualization={this.state.visualization}
							fields={[fielditem]}
							submitLabel={Messages.get("label.submitLabel")}
							showButtons={false}
							ref={'field-' + index}
						/>
					</span>)

				})}

				{//Plano Estratégico
				}

				{!this.state.visualization ?
					<div>
						<div style={{ "display": "-webkit-box", margin: "10px 0px" }} className={"fpdi-text-label"}>{Messages.get('label.risk.objectivePDI')}</div>
						<div>
							<input  ref="objectivePDI" style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_pdi === true} onChange={this.handleStrategyChange} value="Sim" />Sim
							<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_pdi === false} onChange={this.handleStrategyChange} value="Não" />Não
						</div>
						<br />
					</div>
					: ""}
				{!this.state.visualization && this.state.risk_pdi ?
					<label htmlFor={"texto"} className="fpdi-text-label-none">
						{"Objetivo Estratégico do PDI"}
					</label>
					: ""}
				{this.state.visualization || this.state.risk_pdi ?
					<ListForm
						vizualization={this.state.visualization}
						fields={this.getStrategies()}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
						onChange={this.onChangeStrategies}
						className= {this.state.StrategyError  ? " borderError":""}
					/>
				: ""}
				<br />

				{//Processo
				}

				{!this.state.visualization ? <div>
					<div id="objectiveProcess" style={{ "display": "-webkit-box", margin: "10px 0px" }} className={"fpdi-text-label"}>{Messages.get('label.risk.objectiveProcess')}</div>
					<div>
						<input  ref="objectiveProcess" style={{ "margin": "0px 5px" }} type="radio" name="objectiveProcess" checked={this.state.risk_obj_process === true} onChange={this.handleProcessChange} value="Sim" />Sim
						<input style={{ "margin": "0px 5px" }} type="radio" name="objectiveProcess" checked={this.state.risk_obj_process === false} onChange={this.handleProcessChange} value="Não" />Não
					</div>
					<br />
				</div>
					: ""}
				{!this.state.visualization && this.state.risk_obj_process ?
					<label htmlFor={"texto"} className="fpdi-text-label-none">
						{"Processo/Objetivo"}
					</label>
					: ""}

				{this.state.visualization || this.state.risk_obj_process ?
					<ListForm
						vizualization={this.state.visualization}
						fields={this.getProcesses()}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
						onChange={this.onChangeProcesses}
						className= {this.state.ObjectiveError  ? " borderError":""}
					/>
				:""}
				<br />

				{//Atividade
				}

				{!this.state.visualization ?
					<div>
						<div style={{ "display": "-webkit-box", margin: "10px 0px" }} className={"fpdi-text-label"}>{Messages.get('label.risk.activityProcess')}</div>
						<div>
							<input style={{ "margin": "0px 5px" }} type="radio" name="activityProcess" checked={this.state.risk_act_process === true} onChange={this.handleActivityChange} value="Sim" />Sim
							<input style={{ "margin": "0px 5px" }} type="radio" name="activityProcess" checked={this.state.risk_act_process === false} onChange={this.handleActivityChange} value="Não" />Não
						</div>
						<br />
					</div>
				: ""}
				{!this.state.visualization && this.state.risk_act_process ?
					<div>
						<div style={{ position: "relative", bottom: '5px' }}>
							<label htmlFor={this.state.fieldId} className="fpdi-text-label-none">
								{"Processo"}&nbsp;&nbsp;
						</label>
							{(this.context.roles.MANAGER || _.contains(this.context.permissions,
								PermissionsTypes.MANAGE_DOCUMENT_PERMISSION)) ?
								<a className="mdi mdi-plus-circle icon-link" onClick={this.addActivity}></a> : ""
							}
							<br />
						</div>
					</div>
				: ""}

				{!this.state.visualization && this.state.risk_act_process ?
					this.getProcessActivity() : ""}

				{this.state.visualization  ?
					<ListForm
						vizualization={this.state.visualization}
						fields={this.getActivities()}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
					/>
				: ""}

				<br />
				<br />

				{!this.state.visualization ?
					<VerticalForm
						vizualization={this.state.visualization}
						onCancel={this.onCancel}
						onSubmit={this.submitWrapper}
					/>
				: ""}

			</form>
		</div>);
	}
});
