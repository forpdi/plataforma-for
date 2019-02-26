import React from "react";
import { Link } from 'react-router';
import Messages from "forpdi/jsx/core/util/Messages.jsx";
import Form from "forpdi/jsx/planning/widget/attributeForm/AttributeForm.jsx";
import ListForm from "forpdi/jsx/planning/widget/attributeForm/ListAttributeForm.jsx";
import HorizontalInput from "forpdi/jsx/core/widget/form/HorizontalInput.jsx";
import UserStore from "forpdi/jsx/core/store/User.jsx";
import RiskStore from "forpdi/jsx_forrisco/planning/store/Risk.jsx";
import StructureStore from "forpdi/jsx/planning/store/Structure.jsx";
import AttributeTypes from 'forpdi/jsx/planning/enum/AttributeTypes.json';
import LoadingGauge from "forpdi/jsx/core/widget/LoadingGauge.jsx";
import _ from 'underscore';


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
			loading:true,
			visualization: true,
			fields: [],
			users: [],
			planRiskId: null,
			unitId: null,
			riskModel: null,
			policyModel: null,
			strategy: [],
			process: [],
			activity: [],
			activities: 1,
			risk_pdi: false,
			risk_obj_process: false,
			risk_act_process: false,
		}
	},

	componentDidMount() {
		UserStore.on("retrieve-user", (model) => {
			this.setState({
				users: model.data,

		//this.setState({
			loading: false
		//})
			})
		}, this)

		StructureStore.on("companyobjectivesretrivied", (model) => {
			this.setState({
				strategy: model.data
			})
		}, this)


		//UnitStore.on("retrieveprocess",(model)=>{},this)

		RisktStore.on("riskUpdated",(model)=>{
			console.log("riskUpdated",(model))
		},this)

		RisktStore.on("riskcreated",(model)=>{
			console.log("riskcreated",(model))
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
	componentWillUnmount() {
		UserStore.off(this, this, this)
		StructureStore.off(this,this,this)
		RiskStore.off(this,this,this)
	},


	componentWillReceiveProps(newProps, newContext) {



	if (this.state.riskModel == null || (newProps.riskId != this.state.riskModel.id || this.state.visualization != newProps.visualization)) {
			//console.log("State	")
			//var bool=this.state.riskModel.id !=newProps.routeParams.riskId ?  true: newProps.visualization
			console.log(newProps.risk.id !=newProps.params.riskId),
			this.setState({
				loading: true,
				//visualization:true,
				fields: [],
				visualization: newProps.visualization,
				planRiskId: newProps.planRiskId,
				unitId: newProps.risk.unit.id,
				riskModel: newProps.risk,
			});
		}
		this.refreshData()
	},


	// users
	// structure (objetivos estratégicos)
	//processo (pendente)
	refresh() {
		console.log("this.props.risk",this.props.risk)
		if (this.props.risk != null) {
			this.setState({
				//loading: true,
				fields: [],
				//visualization: newProps.visualization,
				//newField: false,
				//newFieldType: null,
				planRiskId: this.context.planRisk.attributes.id,
				unitId: this.props.risk.unit.id,
				riskModel: this.props.risk,
			});
		}

		this.refreshData()
	},

	refreshData() {
		UserStore.dispatch({
			action: UserStore.ACTION_RETRIEVE_USER,
		});

		StructureStore.dispatch({
			action: StructureStore.ACTION_GET_OBJECTIVES_BY_COMPANY
		});

		/*UnitStore.dispatch({
			action: UnitStore.ACTION_GET_OBJECTIVES_PROCESSES
		});
*/
		/*PolicyStore.dispatch({
			action: PolicyStore.ACTION_FIND_POLICY,
			data: this.state.plan.policyId
		});*/
		this.setState({ policyModel: this.context.planRisk.attributes.policy })

		if (this.props.risk) {
			this.state.risk_pdi = this.props.risk.risk_pdi
			this.state.risk_obj_process = this.props.risk.risk_obj_process
			this.state.risk_act_process = this.props.risk.risk_act_process
		}
	},

	getName() {
		return [{
			name: "nome",
			type: AttributeTypes.TEXT_AREA,
			placeholder: "Nome do Risco",
			maxLength: 100,
			label: "Nome do Risco",
			value: this.state.riskModel != null ? this.state.riskModel.name : null,
		}]
	},

	getFields() {
		var fields = [];

		if (this.state.riskModel != null) {
			fields.push({
				name: "code",
				type: AttributeTypes.TEXT_AREA,
				placeholder: "Código",
				maxLength: 100,
				label: "Código de indentificação do risco",
				value: this.state.riskModel.code,
			}, {
					name: "user",
					type: AttributeTypes.SELECT_FIELD,
					optionsField: this.getUsers(),
					placeholder: "Selecione um responsável",
					maxLength: 100,
					label: "Responsável",
					value: this.state.riskModel.user.name,
				}, {
					name: "date",
					type: AttributeTypes.DATE,
					placeholder: "",
					maxLength: 100,
					label: "Data e hora de criação do risco",
					value: this.state.riskModel.begin,
				}, {
					name: "reason",
					type: AttributeTypes.TEXT_AREA_FIELD,
					placeholder: "Causas do risco",
					maxLength: 1000,
					label: "Causa",
					value: this.state.riskModel.reason,
				}, {
					name: "result",
					type: AttributeTypes.TEXT_AREA_FIELD,
					placeholder: "Consequência do risco",
					maxLength: 1000,
					label: "Consequência",
					value: this.state.riskModel.result,
				}, {
					name: "probability",
					type: AttributeTypes.SELECT_FIELD,
					optionsField:  this.getProbabilities(),
					displayField: 'label',
					placeholder: "Selecione",
					maxLength: 100,
					label: "Probabilidade",
					value: this.state.riskModel.probability,
				}, {
					name: "impact",
					type: AttributeTypes.SELECT_FIELD,
					optionsField: this.getImpacts(),
					displayField: 'label',
					placeholder: "Selecione",
					maxLength: 100,
					label: "Impacto",
					value: this.state.riskModel.impact,
				}, {
					name: "riskLevel",
					type: AttributeTypes.TEXT_AREA,
					placeholder: "",
					maxLength: 100,
					label: "Grau do risco",
					value: this.state.riskModel ? this.state.riskModel.riskLevel.level : null
				}, {
					name: "periodicity",
					label: "Periodicidade da análise",
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
					placeholder: "Selecione a periodicidade",
					maxLength: 100,
					value: this.state.riskModel.periodicity,
				}, {
					name: "tipology",
					type: AttributeTypes.SELECT_FIELD,
					optionsField: [{ label: 'Risco operacional' },
					{ label: 'Risco de imagem/reputação do órgão' },
					{ label: 'Risco legal' },
					{ label: 'Risco financeiro/orçamentário' }],
					placeholder: "Selecione",
					maxLength: 100,
					label: "Tipologia",
					value: this.state.riskModel.tipology,
				}, {
					name: "type",
					type: AttributeTypes.SELECT_FIELD,
					optionsField: [{ label: 'Ameaça' }, { label: 'Oportunidade' }],
					displayField: 'label',
					placeholder: "Selecione",
					maxLength: 100,
					label: "Tipo",
					value: this.state.riskModel.type,
				});
		}

		return fields;
	},

	getStrategies() {

		if (this.state.riskModel == null || this.state.riskModel.strategies == null || !this.state.riskModel.risk_pdi) {
			return [];
		}

		if (this.state.riskModel.strategies.total == 0) {
			return [{
				label: "Objetivo(s) do(s) processo(s) vinculado(s)",
				value: "Não está vinculado a nenhum objetivo estratégido do PDI",
				name: "",
				type:AttributeTypes.SELECT_MULTI_FIELD
			}]
		}

		var fields = []
		this.state.riskModel.strategies.list.map((fielditem, index) => {
			fields.push({
				name: "strategy-" + (index),
				type: AttributeTypes.SELECT_MULTI_FIELD,
				label: "Objetivo(s) do(s) processo(s) vinculado(s)",//index==0 ? (this.state.visualization ?"Objetivo(s) do(s) processo(s) vinculado(s)":"") : null,
				linkName: "(Visualizar no PDI)",
				placeholder: "Selecione um ou mais objetivos",
				maxLength: 100,
				value: fielditem.name,
				link: fielditem.linkFPDI,
				optionsField: index == 0 ? this.getAllStrategies() : null,
			})
		})

		return fields;
	},

	getProcesses() {

		if (this.state.riskModel == null || this.state.riskModel.processes == null || !this.state.riskModel.risk_obj_process) {
			return [];
		}
		if (this.state.riskModel.processes.total == 0) {
			return [{
				label: "Objetivo(s) do(s) processo(s) vinculado(s)",
				value: "Não está vinculado a nenhum objetivo do processo",
				name: "",
				type:AttributeTypes.SELECT_MULTI_FIELD
			}]
		}

		var fields = []
		this.state.riskModel.processes.list.map((fielditem, index) => {
			fields.push({
				name: "process-" + (index),
				type: AttributeTypes.SELECT_MULTI_FIELD,
				placeholder: "Selecione um ou mais objetivos",
				label: "Objetivo(s) do(s) processo(s) vinculado(s)",//index==0 ? (this.state.visualization ?"Objetivo(s) do(s) processo(s) vinculado(s)":"") : null,
				linkName: "(Visualizar objetivo do processo)",
				link: fielditem.linkFPDI,
				maxLength: 100,
				value: fielditem.name,
				optionsField: index == 0 ? this.getAllProcesses() : null,
			})
		})

		return fields;
	},

	getActivities() {

		if (this.state.riskModel == null || this.state.riskModel.activities == null || !this.state.riskModel.risk_act_process) {
			return [];
		}
		if (this.state.riskModel.activities.total == 0) {
			return [{
				label: "Atividade(s) do(s) processo(s) vinculado(s)",
				value: "Não está vinculado a nenhuma atividade",
				name: ""
			}]
		}

		var fields = []
		this.state.riskModel.activities.list.map((fielditem, index) => {
			fields.push({
				name: "activity-" + (index),
				type: AttributeTypes.SELECT_FIELD,
				placeholder: "",
				maxLength: 100,
				label: index == 0 ? (this.state.visualization ? "Atividade(s) do(s) processo(s) vinculado(s)" : "") : null,
				value: fielditem.name,
				link: fielditem.linkFPDI,
				linkName: "(Visualizar processo)",
			})
		})
		return fields;
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
		return [{ label: "AGM 2017", value: 1 }]
	},


	onCancel(evt) {
		evt.preventDefault();

		this.setState({
			visualization: true
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
		var cor = null
		var risk = null
		fields.push({
			name: "process_" + n,
			type: "select",
			required: true,
			maxLength: 40,
			placeholder: "Selecione um processo",
			label: Messages.getEditable("label.policySelect", "hide"),
			value: cor,
			valueField: 'label',
			displayField: 'label',
			options: [{ label: "processo 1", id: 1, name: "processo 1" }]
			//onChange: this.onChangeMatrix
		},
			{
				name: "activity_" + n,
				type: "text",
				required: true,
				maxLength: 40,
				placeholder: "Com quais atividades do processo o risco está associado?",
				label: Messages.getEditable("label.policyConfig", "hide"),
				value: risk,
				//onChange: this.onChangeMatrix
			})
		return fields
	},

	getProcessActivity() {
		var grau = []

		for (var i = 0; i < this.state.activities; i++) {
			grau.push(
				this.getPA(i).map((field, idx) => {
					return (<HorizontalInput
						name={field.name}
						fieldDef={field}
						key={field.value ? field.value : field.name}
						ref={'pa-' + (i) + "-" + (idx)}
					/>);
				})
			)

			if (i > 0) {
				grau.push(<Link onClick={this.deleteActivity.bind(this, i)}>
					<span className="mdi mdi-delete cursorPointer" title={Messages.get("label.deleteActicity")}></span>
				</Link>)
			}
			grau.push(<br />)
		}

		return (<div>{grau}<br /></div>)
	},

	deleteActivity(x, y) {
		console.log(x)
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
		console.log("add activity", this.state.activities + 1)
		this.setState({
			activities: this.state.activities + 1
		})
	},

	getValues() {
		var data = {};

		data['name']=document.getElementById("field-nome").value
		data['code']=document.getElementById("field-code").value
		data['impact']=document.getElementById("field-impact").value
		data['probability']=document.getElementById("field-probability").value
		data['periodicity']=document.getElementById("field-periodicity").value
		data['reason']=document.getElementById("field-reason").value
		data['result']=document.getElementById("field-result").value
		data['tipology']=document.getElementById("field-tipology").value
		data['type']=document.getElementById("field-type").value

		var index=this.refs["field-1"].refs.user.refs["field-user"].selectedIndex

		data['user']={id:this.state.users[index].id}
		data['unit']={id:this.state.unitId}

		data["risk_pdi"] = this.state.risk_pdi
		data["risk_obj_process"] = this.state.risk_obj_process
		data["risk_act_process"] = this.state.risk_act_process
		data['strategies']={}
		data['processes']={}
		data['activities']={}

		console.log("data", data)

		return data;
	},


	submitWrapper(evnt) {
		evnt.preventDefault();

		if (this.onSubmit(this.getValues())){
				$(this.refs['btn-submit']).attr("disabled", "disabled");
		}
	},

	onSubmit(data) {

		console.log("get all data and update if model exists or save a new risk")

		var me = this;
		var msg = "";

		//var msg = Validate.validationPolicyEdit(data, this.refs);

		if (msg != "") {
			this.context.toastr.addAlertError(msg);
			return;
		}

		if (me.props.params.riskId) {
			data.id=me.props.params.riskId
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
	console.log(this.getStrategies())
	console.log(this.getProcesses())

		if (this.state.loading) {
			return <LoadingGauge />;
		}
		return (<div>
				<form onSubmit={this.submitWrapper} className="fpdi-card fpdi-card-full floatLeft"  id={this.props.id} ref="riskEditForm">

				{!this.state.visualization ?
					<VerticalForm
						vizualization={this.state.visualization}
						fields={this.getName()}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
						ref={'field-name'} />
					: ""}

				{this.getFields().map((fielditem, index) => {
					if ((fielditem.name == "riskLevel" || fielditem.name == "date") && !this.state.visualization) {
						return
					}

					return (<div><VerticalForm
						vizualization={this.state.visualization}
						fields={[fielditem]}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
						ref={'field-' + index}
						 />
					</div>)
				})}


				{!this.state.visualization ? <div>
					<div style={{ "display": "-webkit-box", margin: "10px 0px" }} className={"fpdi-text-label"}>{Messages.get('label.risk.objectivePDI')}</div>
					<form>
						<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_pdi === true} onChange={this.handleStrategyChange} value="Sim" />Sim
						<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_pdi === false} onChange={this.handleStrategyChange} value="Não" />Não
					</form>
					<br />
				</div>
					: ""}
				{!this.state.visualization && this.state.risk_pdi ?
					<label htmlFor={"texto"} className="fpdi-text-label-none">
						{"Objetivo Estratégico do PDI"}
					</label>
					: ""}
				{this.state.risk_pdi ?
					<ListForm
						vizualization={this.state.visualization}
						fields={this.getStrategies()}
						submitLabel={Messages.get("label.submitLabel")}
						showButtons={false}
						/>
					: ""}
				<br />


				{!this.state.visualization ? <div>
					<div style={{ "display": "-webkit-box", margin: "10px 0px" }} className={"fpdi-text-label"}>{Messages.get('label.risk.objectiveProcess')}</div>
					<form>
						<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_obj_process === true} onChange={this.handleProcessChange} value="Sim" />Sim
						<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_obj_process === false} onChange={this.handleProcessChange} value="Não" />Não
					</form>
					<br />
				</div>
					: ""}
				{!this.state.visualization && this.state.risk_obj_process ?
					<label htmlFor={"texto"} className="fpdi-text-label-none">
						{"Processo/Objetivo"}
					</label>
					: ""}
				{this.state.risk_obj_process ?
					<ListForm
					vizualization={this.state.visualization}
					fields={this.getProcesses()}
					submitLabel={Messages.get("label.submitLabel")}
					showButtons={false}
					/>
					: ""}
				<br />

				{!this.state.visualization ?
					<div>
						<div style={{ "display": "-webkit-box", margin: "10px 0px" }} className={"fpdi-text-label"}>{Messages.get('label.risk.activityProcess')}</div>
						<form>
							<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_act_process === true} onChange={this.handleActivityChange} value="Sim" />Sim
							<input style={{ "margin": "0px 5px" }} type="radio" name="objectivePDI" checked={this.state.risk_act_process === false} onChange={this.handleActivityChange} value="Não" />Não
						</form>
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

				<br />
				<br />

				{/*<VerticalForm
					vizualization={this.state.visualization}
					onCancel={this.onCancel}
					onSubmit={this.submitWrapper}
				/>*/
				}
				{!this.state.visualization ?
							(<div className="form-group padding40">
								<button type="submit" className="btn btn-success btn-block">{this.state.submitLabel}</button>
								{!this.props.hideCanel ? (!this.props.cancelUrl ?
									<button className="btn btn-default  btn-block" onClick={this.onCancel}>{this.state.cancelLabel}</button>
									: (
										<Link to={this.props.cancelUrl} className="btn btn-default btn-block">{this.state.cancelLabel}</Link>
									)) : ""}
							</div>)
							:
							(<div className="form-group text-left">
								<input type="submit" className="btn btn-sm btn-success" ref="btn-submit" value={this.state.submitLabel} />
								{!this.props.hideCanel ? (!this.props.cancelUrl ?
									<button className="btn btn-sm btn-default" onClick={this.onCancel}>{this.state.cancelLabel}</button>
									:
									<Link className="btn btn-sm btn-default" to={this.props.cancelUrl}>{this.state.cancelLabel}</Link>
								) : ""}
							</div>)
							}
			</form>
		</div>);
		/*}else{
			_.defer(() => {
				this.context.tabPanel.addTab(this.props.location.pathname+"/edit", this.state.riskModel?  this.state.riskModel.name:"");
			});
			return( <div> visualization = false (edit & save) </div>)
		}*/
	}
});
